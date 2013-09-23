package org.terracotta.utils.perftester.cache.runnerFactories;

import net.sf.ehcache.Cache;

import org.terracotta.utils.perftester.cache.runners.CachePutOperation;
import org.terracotta.utils.perftester.cache.runners.CachePutWithWriterOperation;
import org.terracotta.utils.perftester.conditions.impl.IterationCondition;
import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.generators.impl.SequentialGenerator;
import org.terracotta.utils.perftester.runners.impl.KeyValueRunner;

public class CachePutOperationFactory extends CacheRunnerFactory {
	private final ObjectGenerator valueGenerator;

	//NOTE: With this type of factory, we must make sure that the sequential generator is not being recreated each time create() is called...
	//that way multiple thread can all work against the same thread-safe generator
	private final ObjectGenerator keyGen;
	
	//if using this constructor, sequential generator 
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
	public KeyValueRunner create() {
		if(null != getCache() && null != getCache().getRegisteredCacheWriter())
			return new CachePutWithWriterOperation(getCache(), new IterationCondition(getNumOperations()), keyGen, valueGenerator);
		else
			return new CachePutOperation(getCache(), new IterationCondition(getNumOperations()), keyGen, valueGenerator);
	}
}
