package org.terracotta.utils.perftester.cache.runners;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.conditions.impl.IterationCondition;
import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.generators.impl.SequentialGenerator;

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
	public void doUnitOfWork(K key, V value) {
		if(isDebug)
			log.debug("Putting cache entry with key:" + key);
		
		if(null != key)
			cache.putWithWriter(new Element(key, value));
		else
			log.warn("key is null...cannot add a new cache entry");
	}

	public static class CachePutOperationFactory extends CacheRunnerFactory {
		private final ObjectGenerator valueGenerator;

		//NOTE: With this type of factory, we must make sure that the sequential generator is not being recreated each time create() is called...
		//that way multiple thread can all work against the same thread-safe generator
		private final ObjectGenerator keyGen;
		
		//if using this constrcutor, sequential generator 
		public CachePutOperationFactory(Cache cache, long numOperations, ObjectGenerator valueGenerator) {
			this(cache, numOperations, null, valueGenerator);
		}

		public CachePutOperationFactory(Cache cache, long numOperations, ObjectGenerator keyGenerator, ObjectGenerator valueGenerator) {
			super(cache, numOperations);
			this.valueGenerator = valueGenerator;

			if(null == keyGenerator)
				this.keyGen = new SequentialGenerator(0);
			else
				this.keyGen = keyGenerator;
		}

		@Override
		public CachePutOperation create() {
			return new CachePutOperation(getCache(), new IterationCondition(getNumOperations()), keyGen, valueGenerator);	
		}
	}
}
