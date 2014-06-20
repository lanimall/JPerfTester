package org.terracotta.utils.perftester.cache.runners;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.generators.ObjectGenerator;

/**
 * @author Fabien Sanglier
 * 
 */
public class CacheUpdateOperation<K, V> extends AbstractCacheKeyValueRunner<K, V> {
	private static Logger log = LoggerFactory.getLogger(CacheUpdateOperation.class);
	private static final boolean isDebug = log.isDebugEnabled();

	public CacheUpdateOperation(Ehcache cache, Condition termination,
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
			cache.replace(new Element(key, value));
		else
			log.warn("key is null...cannot update entry without the key");
		
		//do a get right after
		Element elem = cache.get(key);
		return (null != elem)?elem.getObjectValue():null;
	}
}
