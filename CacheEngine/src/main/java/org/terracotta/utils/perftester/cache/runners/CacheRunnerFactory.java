package org.terracotta.utils.perftester.cache.runners;

import org.terracotta.utils.perftester.runners.RunnerFactory;

import net.sf.ehcache.Cache;

/**
 * @author Fabien Sanglier
 * 
 */
public abstract class CacheRunnerFactory extends RunnerFactory {
	private Cache cache;
	
	protected CacheRunnerFactory(Cache cache, int numThreads, long numOperations) {
		super(numThreads, numOperations);
		this.cache = cache;
	}

	public Cache getCache() {
		return cache;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}
}