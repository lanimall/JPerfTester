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
public class CachePutOperation<K, V> extends AbstractCacheKeyValueRunner<K, V> {
	private static Logger log = LoggerFactory.getLogger(CachePutOperation.class);
	private static final boolean isDebug = log.isDebugEnabled();

	public CachePutOperation(Cache cache, Condition termination,
			ObjectGenerator<K> keyGenerator, ObjectGenerator<V> valueGenerator) {
		super(cache, termination, keyGenerator, valueGenerator);
	}

	@Override
	public String getName() {
		return "Cache Loader Operation";
	}

	@Override
	public Object doUnitOfWork(K key, V value) {
		if(isDebug)
			log.debug("Putting cache entry with key:" + key);
		
		if(null != key)
			cache.put(new Element(key, value));
		else
			log.warn("key is null...cannot add a new cache entry");
		
		return null;
	}
}
