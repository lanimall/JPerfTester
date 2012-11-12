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
public class CachePutOperation extends AbstractCacheRunner<Long> {
	private static Logger log = LoggerFactory.getLogger(CachePutOperation.class);
	private static final boolean isDebug = log.isDebugEnabled();
	private final ObjectGenerator valueGenerator;
	
	public CachePutOperation(Cache cache, Condition termination, ObjectGenerator<Long> keyGenerator, ObjectGenerator valueGenerator) {
		super(cache, termination, keyGenerator);
		this.valueGenerator = valueGenerator;
	}

	@Override
	public String getName() {
		return "Cache Loader Operation";
	}
	
	@Override
	public void doUnitOfWork(Long key) {
		if(isDebug)
			log.debug("Putting cache entry with key:" + key);
		
		cache.put(new Element(key, valueGenerator.generate()));
	}
	
	public static class CachePutOperationFactory extends CacheRunnerFactory {
		private final long keyStart;
		private final ObjectGenerator valueGenerator;
		
		public CachePutOperationFactory(Cache cache, long numOperations, ObjectGenerator valueGenerator) {
			this(cache, numOperations, valueGenerator, 0);
		}
		
		public CachePutOperationFactory(Cache cache, long numOperations, ObjectGenerator valueGenerator, long keyStart) {
			super(cache, numOperations);
			this.keyStart = keyStart;
			this.valueGenerator = valueGenerator;
		}

		@Override
		public CachePutOperation create() {
			SequentialGenerator keyGen = new SequentialGenerator(keyStart);
			return new CachePutOperation(getCache(), new IterationCondition(getNumOperations()), keyGen, valueGenerator);	
		}
	}
}
