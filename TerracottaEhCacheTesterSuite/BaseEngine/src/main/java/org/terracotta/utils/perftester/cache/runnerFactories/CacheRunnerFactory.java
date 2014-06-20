package org.terracotta.utils.perftester.cache.runnerFactories;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Ehcache;

import org.terracotta.utils.perftester.runners.OpsCountRunnerFactory;

/**
 * @author Fabien Sanglier
 * 
 */
public abstract class CacheRunnerFactory extends OpsCountRunnerFactory {
	private Ehcache cache;
	
	protected CacheRunnerFactory(Ehcache cache, long numOperations) {
		super(numOperations);
		this.cache = cache;
	}

	public Ehcache getCache() {
		return cache;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}
}