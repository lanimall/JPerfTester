package org.terracotta.utils.commons.cache;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.api.ClusteringToolkit;
import org.terracotta.coordination.Barrier;

/**
 * @author Fabien Sanglier
 * 
 */
public class CacheUtils {
	private static Logger log = LoggerFactory.getLogger(CacheUtils.class);

	public static final String ENV_CACHE_CONFIGPATH = "ehcache.config.path";

	public static Cache getCache(String cacheName) throws Exception {
		return getCache(cacheName, null);
	}

	public static Cache getCache(String cacheName, String cacheMgrResourcePath) throws Exception {
		CacheManager cacheManager = getCacheManager(cacheMgrResourcePath);
		if (null == cacheName || !cacheManager.cacheExists(cacheName)){
			if(null == cacheName)
				log.info("cache name not specified.");
			else if(!cacheManager.cacheExists(cacheName)){
				log.info("cache name [" + cacheName + "] not found in the cache manager.");
			}
			return null;
		}

		return cacheManager.getCache(cacheName);
	}

	public static CacheManager getCacheManager(){
		return getCacheManager(null);
	}
	
	public static CacheManager getCacheManager(String resourcePath){
		String configLocationToLoad = null;
		if(null != resourcePath && !"".equals(resourcePath)){
			configLocationToLoad = resourcePath;
		} else if(null != System.getProperty(ENV_CACHE_CONFIGPATH)){
			configLocationToLoad = System.getProperty(ENV_CACHE_CONFIGPATH);
		}
		
		if(null != configLocationToLoad){
			InputStream inputStream = null;
			try {
				if(configLocationToLoad.indexOf("file:") > -1){
					inputStream = new FileInputStream(configLocationToLoad.substring("file:".length()));
				}else if(configLocationToLoad.indexOf("classpath:") > -1){
					inputStream = CacheUtils.class.getClassLoader().getResourceAsStream(configLocationToLoad.substring("classpath:".length()));
				} else { //default to classpath if no prefix is specified
					inputStream = CacheUtils.class.getClassLoader().getResourceAsStream(configLocationToLoad);
				}

				if (inputStream == null) {
					throw new FileNotFoundException("File at '" + configLocationToLoad	+ "' not found");
				}
				
				log.info("Loading Cache manager from " + configLocationToLoad);
				return CacheManager.create(inputStream);
			} catch(IOException ioe) {
				throw new CacheException(ioe);
			}
			finally{
				if(null != inputStream){
					try {
						inputStream.close();
					} catch (IOException e) {
						throw new CacheException(e);
					}
					inputStream = null;
				}
			}
		} else{
			log.info("Loading Cache manager from default classpath");
			return CacheManager.getInstance();
		}
	}
	
	public static Barrier getBarrier(CacheManager cacheManager, String barrierName, int numberOfClients){
		if(null == cacheManager)
			throw new IllegalArgumentException("A valid cacheManager should be provided");

		Barrier barrier = null;
		if(null != barrierName && !"".equals(barrierName)){
			try {
				if(null != cacheManager.getConfiguration().getTerracottaConfiguration()){
					//instanciate the toolkit and create a barrier
					ClusteringToolkit toolkit = new org.terracotta.api.TerracottaClient(cacheManager.getConfiguration().getTerracottaConfiguration().getUrl()).getToolkit();
					barrier = toolkit.getBarrier(barrierName, numberOfClients);
				} else {
					log.error("Terracotta configuration could not be found...cannot instanciate barrier");
				}
			} catch (Exception e) {
				log.error("Could not implement a terracotta toolkit barrier", e);
			}
		} else {
			log.warn("Barriername was not provided...cannot instanciate barrier");
		}

		return barrier;
	}
}
