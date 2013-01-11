package org.terracotta.utils.perftester.cache.runners;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.conditions.impl.IterationCondition;
import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.generators.impl.RandomNumberGenerator;
import org.terracotta.utils.perftester.generators.impl.SequentialGenerator;

/**
 * @author Fabien Sanglier
 * 
 */
public class CacheGetOperation<K> extends AbstractCacheKeyRunner<K> {
	private static Logger log = LoggerFactory.getLogger(CacheGetOperation.class);
	private static final boolean isDebug = log.isDebugEnabled();
	
	public CacheGetOperation(Cache cache, Condition termination, ObjectGenerator<K> keyGenerator) {
		super(cache, termination, keyGenerator);
	}

	@Override
	public String getName() {
		return "Cache Get Operation";
	}
	
	@Override
	public void doUnitOfWork(K key) {
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
	}

	public static class CacheGetOperationFactory extends CacheRunnerFactory {
		private final ObjectGenerator keyGen;
		
		public CacheGetOperationFactory(Cache cache, long numOperations, ObjectGenerator keyGen) {
			super(cache, numOperations);
			this.keyGen = keyGen;
		}

		@Override
		public CacheGetOperation create() {
			if(null == keyGen)
				throw new IllegalArgumentException("KenGen object may not be null");
			
			return new CacheGetOperation(getCache(), new IterationCondition(getNumOperations()), keyGen);	
		}
	}
	
	public static class CacheSequentialGetOperationFactory extends CacheGetOperationFactory {
		public CacheSequentialGetOperationFactory(Cache cache, long numOperations, long keyStart) {
			super(cache, numOperations, new SequentialGenerator(keyStart));
		}
	}
	
	public static class CacheRandomGetOperationFactory extends CacheGetOperationFactory {
		public CacheRandomGetOperationFactory(Cache cache, long numOperations, long keyMaxValue) {
			super(cache, numOperations, new RandomNumberGenerator(keyMaxValue));
		}
		public CacheRandomGetOperationFactory(Cache cache, long numOperations, long keyMinValue, long keyMaxValue) {
			super(cache, numOperations, new RandomNumberGenerator(keyMinValue, keyMinValue));
		}
	}
}
