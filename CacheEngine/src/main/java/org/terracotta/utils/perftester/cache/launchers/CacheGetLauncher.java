package org.terracotta.utils.perftester.cache.launchers;

import net.sf.ehcache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.cache.runners.CacheGetOperation.CacheGetOperationFactory;

/**
 * @author Fabien Sanglier
 * 
 */
public class CacheGetLauncher extends BaseCacheLauncher {
	private static Logger log = LoggerFactory.getLogger(CacheGetLauncher.class);

	public CacheGetLauncher(Cache cache, int numThreads, long numOperations,long keyStart) {
		super(new CacheGetOperationFactory(cache, numThreads, numOperations,keyStart));
		
		if(log.isInfoEnabled()){
			log.info("*********** Getting cache entries *************");
			log.info("Params:");
			log.info("Number of Loader Threads: " + numThreads);
			log.info("Number of objects to fetch: " + numOperations);
			log.info("Start at key: " + keyStart);
			log.info("Number of operations per thread: " + (numOperations/numThreads));
		}
	}
}