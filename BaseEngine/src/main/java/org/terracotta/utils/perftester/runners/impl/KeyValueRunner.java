package org.terracotta.utils.perftester.runners.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.runners.Runner;

/**
 * @author Fabien Sanglier
 * 
 * An abstract Runner meant to encapsulate complex operation, allowing sub-classes to implement only a certain number of work methods:
 *  - doBeforeLoad(): init operations to execute before the multi-threaded load
 *  - doAfterLoad(): clean up operations to execute after the multi-threaded load
 *  - getUnitOfWork(): detail of the unit of work that each thread will repeatedly execute 
 * 
 * @param <T>
 */
public abstract class KeyValueRunner<K, V> extends KeyRunner<K> implements Runner {
	private static Logger log = LoggerFactory.getLogger(KeyValueRunner.class);

	private final ObjectGenerator<V> valueGenerator;
	
	protected KeyValueRunner(Condition termination, ObjectGenerator<K> keyGenerator, ObjectGenerator<V> valueGenerator) {
		super(termination, keyGenerator);
		
		if(null == valueGenerator){
			log.warn("The valueGenerator object is null...make sure it's intended.");
		}
		
		this.valueGenerator = valueGenerator;
	}
	
	@Override
	public void doUnitOfWork(K key) {
		doUnitOfWork(key, null);
	}

	public abstract void doUnitOfWork(K key, V value);

	@Override
	protected void execute() {
		long iterationStartTime;
		K key = null;
		V value = null;
		
		//generate value outside timing measurements
		if(null != valueGenerator)
			value = valueGenerator.generate();
		
		//avoid performing null check at every iteration
		if(null != keyGenerator){
			do {
				try {
					//perform the object generation outside the loop timing to avoid timing issues if object generation is slow
					key = keyGenerator.generate();
					
					iterationStartTime = System.currentTimeMillis();
					doUnitOfWork(key, value);
					stats.add(System.currentTimeMillis() - iterationStartTime);
				} catch (Exception e) {
					log.error("Error during execution of unit of work. will not count this operation in the timing stats.", e);
				}
			} while (!termination.isDone());
		} else {
			do {
				try {
					iterationStartTime = System.currentTimeMillis();
					doUnitOfWork(null, value);
					stats.add(System.currentTimeMillis() - iterationStartTime);
				} catch (Exception e) {
					log.error("Error during execution of unit of work. will not count this operation in the timing stats.", e);
				}
			} while (!termination.isDone());
		}
	}
}
