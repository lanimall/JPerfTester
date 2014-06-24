package org.terracotta.utils.perftester.cache.runners;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.loader.CacheLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.generators.ObjectGenerator;

/**
 * @author Fabien Sanglier
 * 
 */
public class CacheGetWithLoaderOperation<K> extends AbstractCacheKeyRunner<K> {
	private static Logger log = LoggerFactory.getLogger(CacheGetWithLoaderOperation.class);
	private static final boolean isDebug = log.isDebugEnabled();
	
	private final CacheLoader loader;
	private final Object loaderArgument;
	
	public CacheGetWithLoaderOperation(Ehcache cache, Condition termination, ObjectGenerator<K> keyGenerator, CacheLoader loader, Object loaderArgument) {
		super(cache, termination, keyGenerator);
		this.loader = loader;
		this.loaderArgument = loaderArgument;
	}

	@Override
	public String getName() {
		return "Cache Get With Loader Operation";
	}
	
	@Override
	public Object doUnitOfWork(K key) {
		if(isDebug)
			log.debug("Getting cache entry with key:" + key);
		
		Element cacheElem = cache.getWithLoader(key, loader, loaderArgument);
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
