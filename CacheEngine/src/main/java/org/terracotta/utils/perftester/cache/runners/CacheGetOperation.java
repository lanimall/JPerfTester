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
public class CacheGetOperation extends AbstractCacheRunner<Long> {
	private static Logger log = LoggerFactory.getLogger(CacheGetOperation.class);
	private static final boolean isDebug = log.isDebugEnabled();
	
	public CacheGetOperation(Cache cache, Condition termination, ObjectGenerator<Long> generator) {
		super(cache, termination, generator);
	}

	@Override
	public String getName() {
		return "Cache Get Operation";
	}
	
	@Override
	public void doUnitOfWork(Long key) {
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
		
		public CacheGetOperationFactory(Cache cache, int numThreads, long numOperations, ObjectGenerator keyGen) {
			super(cache, numThreads, numOperations);
			this.keyGen = keyGen;
		}

		@Override
		public CacheGetOperation create() {
			if(null == keyGen)
				throw new IllegalArgumentException("KenGen object may not be null");
			
			return new CacheGetOperation(getCache(), new IterationCondition(getNumOperations() / getNumThreads()), keyGen);	
		}
	}
	
	public static class CacheSequentialGetOperationFactory extends CacheGetOperationFactory {
		public CacheSequentialGetOperationFactory(Cache cache, int numThreads, long numOperations, long keyStart) {
			super(cache, numThreads, numOperations, new SequentialGenerator(keyStart));
		}
	}
	
	public static class CacheRandomGetOperationFactory extends CacheGetOperationFactory {
		public CacheRandomGetOperationFactory(Cache cache, int numThreads, long numOperations, int nbRandomDigits) {
			super(cache, numThreads, numOperations, new RandomNumberGenerator(nbRandomDigits));
		}
		public CacheRandomGetOperationFactory(Cache cache, int numThreads, long numOperations, int nbRandomDigits, int randomPrependDigits, int randomAppendDigits) {
			super(cache, numThreads, numOperations, new RandomNumberGenerator(nbRandomDigits, randomPrependDigits, randomAppendDigits));
		}
	}
}
