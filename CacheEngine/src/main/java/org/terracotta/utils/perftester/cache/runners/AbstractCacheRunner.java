package org.terracotta.utils.perftester.cache.runners;

import net.sf.ehcache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.runners.impl.KeyValueRunner;

/**
 * @author Fabien Sanglier
 * 
 */
public abstract class AbstractCacheRunner<T> extends KeyValueRunner<T> {
	private static final Logger log = LoggerFactory.getLogger(AbstractCacheRunner.class);
	protected final Cache cache;
	
	protected AbstractCacheRunner(Cache cache, Condition termination, ObjectGenerator<T> generator) {
		super(termination,generator);
		
		if(cache == null)
			throw new IllegalArgumentException("Cache object may not be null");
		this.cache = cache;
	}
}