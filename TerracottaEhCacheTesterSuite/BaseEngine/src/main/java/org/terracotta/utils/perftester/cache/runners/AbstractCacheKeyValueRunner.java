package org.terracotta.utils.perftester.cache.runners;

import net.sf.ehcache.Ehcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.runners.impl.KeyValueRunner;

/**
 * @author Fabien Sanglier
 * 
 */
public abstract class AbstractCacheKeyValueRunner<K, V> extends KeyValueRunner<K, V> {
	private static final Logger log = LoggerFactory.getLogger(AbstractCacheKeyValueRunner.class);
	protected final Ehcache cache;
	
	public AbstractCacheKeyValueRunner(Ehcache cache, Condition termination, ObjectGenerator<K> keyGenerator) {
		this(cache, termination, keyGenerator, null);
	}
	
	public AbstractCacheKeyValueRunner(Ehcache cache, Condition termination, ObjectGenerator<K> keyGenerator, ObjectGenerator<V> valueGenerator) {
		super(termination, keyGenerator, valueGenerator);
		
		if(cache == null)
			throw new IllegalArgumentException("Cache object may not be null");
		this.cache = cache;
	}
}