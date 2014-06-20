package org.terracotta.utils.perftester.cache.runners;

import net.sf.ehcache.Ehcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.runners.impl.KeyRunner;

/**
 * @author Fabien Sanglier
 * 
 */
public abstract class AbstractCacheKeyRunner<K> extends KeyRunner<K> {
	private static final Logger log = LoggerFactory.getLogger(AbstractCacheKeyRunner.class);
	protected final Ehcache cache;
	
	public AbstractCacheKeyRunner(Ehcache cache, Condition termination, ObjectGenerator<K> keyGenerator) {
		super(termination, keyGenerator);
		
		if(cache == null)
			throw new IllegalArgumentException("Cache object may not be null");
		this.cache = cache;
	}
}