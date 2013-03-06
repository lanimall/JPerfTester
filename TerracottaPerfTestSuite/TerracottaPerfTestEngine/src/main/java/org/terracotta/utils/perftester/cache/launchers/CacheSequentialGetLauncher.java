package org.terracotta.utils.perftester.cache.launchers;

import net.sf.ehcache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.cache.runners.CacheGetOperation.CacheGetOperationFactory;
import org.terracotta.utils.perftester.generators.impl.SequentialGenerator;

/**
 * @author Fabien Sanglier
 * 
 */
public class CacheSequentialGetLauncher extends BaseCacheLauncher {
	private static Logger log = LoggerFactory.getLogger(CacheSequentialGetLauncher.class);

	public CacheSequentialGetLauncher(Cache cache, int numThreads, long numOperations,long keyStart) {
		super(numThreads, new CacheGetOperationFactory(cache, numOperations/numThreads,new SequentialGenerator(keyStart)));

		System.out.println("*********** Getting cache entries *************");
		System.out.println("Params:");
		System.out.println("Number of Loader Threads: " + numThreads);
		System.out.println("Number of objects to fetch: " + numOperations);
		System.out.println("Start at key: " + keyStart);
		System.out.println("Number of operations per thread: " + (numOperations/numThreads));
	}
}