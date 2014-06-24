package org.terracotta.utils.perftester.cache.runnerFactories;

import net.sf.ehcache.Ehcache;

import org.terracotta.utils.perftester.cache.runners.CacheDeleteOperation;
import org.terracotta.utils.perftester.cache.runners.CacheDeleteWithWriterOperation;
import org.terracotta.utils.perftester.conditions.impl.IterationCondition;
import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.runners.impl.KeyRunner;

public class CacheDeleteOperationFactory extends CacheRunnerFactory {
	private final ObjectGenerator keyGen;
	
	public CacheDeleteOperationFactory(Ehcache cache, long numOperations, ObjectGenerator keyGen) {
		super(cache, numOperations);
		this.keyGen = keyGen;
	}

	@Override
	public KeyRunner create() {
		if(null == keyGen)
			throw new IllegalArgumentException("KenGen object may not be null");
		
		KeyRunner cacheDeleteRunner = null;
		if(null != getCache() && null != getCache().getRegisteredCacheWriter())
			cacheDeleteRunner = new CacheDeleteWithWriterOperation(getCache(), new IterationCondition(getNumOperations()), keyGen);
		else
			cacheDeleteRunner = new CacheDeleteOperation(getCache(), new IterationCondition(getNumOperations()), keyGen);
		
		
		return cacheDeleteRunner;	
	}
}