package org.terracotta.utils.perftester.cache.runners;

import net.sf.ehcache.Ehcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.generators.ObjectGenerator;

/**
 * @author Fabien Sanglier
 * 
 */
public class CacheDeleteWithWriterOperation<K> extends AbstractCacheKeyRunner<K> {
	private static Logger log = LoggerFactory.getLogger(CacheDeleteWithWriterOperation.class);
	private static final boolean isDebug = log.isDebugEnabled();
	
	public CacheDeleteWithWriterOperation(Ehcache cache, Condition termination, ObjectGenerator<K> keyGenerator) {
		super(cache, termination, keyGenerator);
	}

	@Override
	public String getName() {
		return "Cache Delete Operation";
	}
	
	@Override
	public Object doUnitOfWork(K key) {
		if(isDebug)
			log.debug("Deleting cache entry with key:" + key);
		
		cache.removeWithWriter(key);
		return null;
	}
}
