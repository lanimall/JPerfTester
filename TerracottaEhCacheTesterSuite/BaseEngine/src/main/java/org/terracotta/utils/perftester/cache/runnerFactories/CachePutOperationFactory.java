package org.terracotta.utils.perftester.cache.runnerFactories;

import net.sf.ehcache.Ehcache;

import org.terracotta.utils.perftester.cache.runners.CachePutOperation;
import org.terracotta.utils.perftester.cache.runners.CachePutWithWriterOperation;
import org.terracotta.utils.perftester.cache.runners.CacheUpdateOperation;
import org.terracotta.utils.perftester.conditions.impl.IterationCondition;
import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.generators.impl.SequentialGenerator;
import org.terracotta.utils.perftester.runners.impl.KeyValueRunner;

public class CachePutOperationFactory extends CacheRunnerFactory {
	private final ObjectGenerator valueGenerator;

	//NOTE: With this type of factory, we must make sure that the sequential generator is not being recreated each time create() is called...
	//that way multiple thread can all work against the same thread-safe generator
	private final ObjectGenerator keyGen;
	
	private final boolean doUpdates;
	
	//if using this constructor, sequential generator 
	public CachePutOperationFactory(Ehcache cache, long numOperations, ObjectGenerator valueGenerator){
		this(cache, numOperations, null, valueGenerator, false);
	}

	//if using this constructor, sequential generator 
	public CachePutOperationFactory(Ehcache cache, long numOperations, ObjectGenerator valueGenerator, final boolean doUpdates) {
		this(cache, numOperations, null, valueGenerator, doUpdates);
	}

	public CachePutOperationFactory(Ehcache cache, long numOperations, ObjectGenerator keyGenerator, ObjectGenerator valueGenerator) {
		this(cache, numOperations, keyGenerator, valueGenerator, false);
	}
	
	public CachePutOperationFactory(Ehcache cache, long numOperations, ObjectGenerator keyGenerator, ObjectGenerator valueGenerator, final boolean doUpdates) {
		super(cache, numOperations);
		this.valueGenerator = valueGenerator;
		
		if(null == keyGenerator)
			this.keyGen = new SequentialGenerator(0);
		else
			this.keyGen = keyGenerator;
		
		this.doUpdates = doUpdates;
	}

	@Override
	public KeyValueRunner create() {
		if(null != getCache() && null != getCache().getRegisteredCacheWriter())
			return new CachePutWithWriterOperation(getCache(), new IterationCondition(getNumOperations()), keyGen, valueGenerator);
		else if(doUpdates)
			return new CacheUpdateOperation(getCache(), new IterationCondition(getNumOperations()), keyGen, valueGenerator);
		else
			return new CachePutOperation(getCache(), new IterationCondition(getNumOperations()), keyGen, valueGenerator);
	}
}
