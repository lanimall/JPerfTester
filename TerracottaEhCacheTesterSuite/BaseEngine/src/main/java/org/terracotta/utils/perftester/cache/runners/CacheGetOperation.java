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
public class CacheGetOperation<K> extends AbstractCacheKeyRunner<K> {
	private static Logger log = LoggerFactory.getLogger(CacheGetOperation.class);
	private static final boolean isDebug = log.isDebugEnabled();

	public CacheGetOperation(Ehcache cache, Condition termination, ObjectGenerator<K> keyGenerator) {
		super(cache, termination, keyGenerator);
	}

	@Override
	public String getName() {
		return "Cache Get Operation";
	}
	
	@Override
	public Object doUnitOfWork(K key) {
		if(isDebug)
			log.debug("Getting cache entry with key:" + key);
		
		Element cacheElem = cache.get(key);
		Object obj = null;
		if(null != cacheElem){
			obj = cacheElem.getValue();
		} else {
			if(isDebug)
				log.debug("Could not find the object in cache for key:" + key);
		}
		
		return obj;
	}
}
