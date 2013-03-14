package org.terracotta.utils.perftester.cache.runners;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.generators.ObjectGenerator;

/**
 * @author Fabien Sanglier
 * 
 */
public class CachePutWithWriterOperation<K, V> extends AbstractCacheKeyValueRunner<K, V> {
	private static Logger log = LoggerFactory.getLogger(CachePutWithWriterOperation.class);
	private static final boolean isDebug = log.isDebugEnabled();

	public CachePutWithWriterOperation(Cache cache, Condition termination,
			ObjectGenerator<K> keyGenerator, ObjectGenerator<V> valueGenerator) {
		super(cache, termination, keyGenerator, valueGenerator);
	}

	@Override
	public String getName() {
		return "Cache Loader Operation";
	}

	@Override
	public void doUnitOfWork(K key, V value) {
		if(isDebug)
			log.debug("Putting cache entry with key:" + key);
		
		if(null != key)
			cache.putWithWriter(new Element(key, value));
		else
			log.warn("key is null...cannot add a new cache entry");
	}
}
