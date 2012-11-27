package org.terracotta.utils.perftester.cache.launchers;

import net.sf.ehcache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.cache.runners.CacheGetOperation.CacheRandomGetOperationFactory;

/**
 * @author Fabien Sanglier
 * 
 */
public class CacheRandomGetLauncher extends BaseCacheLauncher {
	private static Logger log = LoggerFactory.getLogger(CacheRandomGetLauncher.class);

	public CacheRandomGetLauncher(Cache cache, int numThreads, long numOperations, int nbRandomDigits, Integer[] randomPrependDigits, Integer[] randomAppendDigits) {
		super(numThreads, new CacheRandomGetOperationFactory(cache, numOperations/numThreads, nbRandomDigits, randomPrependDigits, randomAppendDigits));

		System.out.println("*********** Getting random cache entries *************");
		System.out.println("Params:");
		System.out.println("Number of Loader Threads: " + numThreads);
		System.out.println("Number of objects to fetch: " + numOperations);
		System.out.println("Number of operations per thread: " + (numOperations/numThreads));
	}
}