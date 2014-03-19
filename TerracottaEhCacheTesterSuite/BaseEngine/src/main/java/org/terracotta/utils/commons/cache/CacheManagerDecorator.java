package org.terracotta.utils.commons.cache;

import java.net.MalformedURLException;
import java.net.URL;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.api.ClusteringToolkit;
import org.terracotta.coordination.Barrier;

/**
 * @author Fabien Sanglier
 * singleton
 */
public class CacheManagerDecorator {
	private static Logger log = LoggerFactory.getLogger(CacheManagerDecorator.class);

	public static final String ENV_CACHE_CONFIGPATH = "ehcache.config.path";
	private URL ehcacheURL;

	private CacheManagerDecorator() {
		String configLocationToLoad = null;
		if(null != System.getProperty(ENV_CACHE_CONFIGPATH)){
			configLocationToLoad = System.getProperty(ENV_CACHE_CONFIGPATH);
		}

		if(null != configLocationToLoad){
			if(configLocationToLoad.indexOf("classpath:") > -1){
				ehcacheURL = CacheManagerDecorator.class.getClassLoader().getResource(configLocationToLoad.substring("classpath:".length()));
			} else {
				try {
					ehcacheURL = new URL(configLocationToLoad);
				} catch (MalformedURLException e) {
					log.error("Could not create a valid URL from " + configLocationToLoad, e);
					ehcacheURL = null;
				}
			}
		} else {
			log.warn("No config location specified...");
			ehcacheURL = null;
		}
	}

	private static class CacheManagerDecoratorHolder {
		public static CacheManagerDecorator cacheManagerDecorator = new CacheManagerDecorator();
	}

	public static CacheManagerDecorator getInstance() {
		return CacheManagerDecoratorHolder.cacheManagerDecorator;
	}
	
	public CacheManager getCacheManager() {
		if(null != ehcacheURL)
			return CacheManager.create(ehcacheURL);
		else 
			return CacheManager.getInstance();
	}

	public Cache getCache(String cacheName) {
		Cache ehCacheCache  = null;
		
		if (log.isDebugEnabled()) {
			log.debug("Retrieving cache for cacheName: " + cacheName);
		}
		
		ehCacheCache = getCacheManager().getCache(cacheName);
		if (ehCacheCache == null) {
			log.error("Unable to retrieve cache " + cacheName + "from CacheManager.");
		}
		
		return ehCacheCache;
	}
	
	public Barrier getBarrier(String barrierName, int numberOfClients){
		Barrier barrier = null;
		if(null != barrierName && !"".equals(barrierName)){
			try {
				if(null != getCacheManager().getConfiguration().getTerracottaConfiguration()){
					//instanciate the toolkit and create a barrier
					ClusteringToolkit toolkit = new org.terracotta.api.TerracottaClient(getCacheManager().getConfiguration().getTerracottaConfiguration().getUrl()).getToolkit();
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