package org.terracotta.utils.perftester.cache.runners;

import net.sf.ehcache.Ehcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.cache.runnerFactories.CacheRunnerFactory;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.conditions.impl.IterationCondition;
import org.terracotta.utils.perftester.generators.ObjectGenerator;

/**
 * @author Fabien Sanglier
 * 
 */
public class CacheDeleteOperation<K> extends AbstractCacheKeyRunner<K> {
	private static Logger log = LoggerFactory.getLogger(CacheDeleteOperation.class);
	private static final boolean isDebug = log.isDebugEnabled();
	
	public CacheDeleteOperation(Ehcache cache, Condition termination, ObjectGenerator<K> keyGenerator) {
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
		
		cache.remove(key);
		return null;
	}

	public static class CacheDeleteOperationFactory extends CacheRunnerFactory {
		private final ObjectGenerator keyGen;
		
		public CacheDeleteOperationFactory(Ehcache cache, long numOperations, ObjectGenerator keyGen) {
			super(cache, numOperations);
			this.keyGen = keyGen;
		}

		@Override
		public CacheDeleteOperation create() {
			if(null == keyGen)
				throw new IllegalArgumentException("KenGen object may not be null");
			
			return new CacheDeleteOperation(getCache(), new IterationCondition(getNumOperations()), keyGen);	
		}
	}
}
