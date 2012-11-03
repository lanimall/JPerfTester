package org.terracotta.utils.commons.cache;

import net.sf.ehcache.Cache;
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
		return getCache(getCacheManager(), cacheName);
	}

	public static Cache getCache(CacheManager cacheManager, String cacheName) throws Exception {
		if(null == cacheManager)
			throw new IllegalArgumentException("A valid cacheManager should be provided");

		if (null == cacheName || !cacheManager.cacheExists(cacheName)){
			log.info("EhCache xml not found or needed cache not found.");
			return null;
		}

		return cacheManager.getCache(cacheName);
	}

	public static CacheManager getCacheManager(){
		if(null != System.getProperty(ENV_CACHE_CONFIGPATH)){
			log.info("Loading Cache manager from " + System.getProperty(ENV_CACHE_CONFIGPATH));
			return CacheManager.newInstance(System.getProperty(ENV_CACHE_CONFIGPATH));
		}
		else{
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
				//instanciate the toolkit and create a barrier
				ClusteringToolkit toolkit = new org.terracotta.api.TerracottaClient(cacheManager.getConfiguration().getTerracottaConfiguration().getUrl()).getToolkit();
				barrier = toolkit.getBarrier(barrierName, numberOfClients);
			} catch (Exception e) {
				log.error("Could not implement a terracotta toolkit barrier", e);
			}
		} else {
			log.warn("Barriername was not provided...cannot instanciate barrier");
		}
		
		return barrier;
	}
}
