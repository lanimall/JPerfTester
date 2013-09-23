package org.terracotta.utils.perftester.cache.runnerFactories;

import net.sf.ehcache.Cache;

import org.terracotta.utils.perftester.runners.OpsCountRunnerFactory;

/**
 * @author Fabien Sanglier
 * 
 */
public abstract class CacheRunnerFactory extends OpsCountRunnerFactory {
	private Cache cache;
	
	protected CacheRunnerFactory(Cache cache, long numOperations) {
		super(numOperations);
		this.cache = cache;
	}

	public Cache getCache() {
		return cache;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}
}