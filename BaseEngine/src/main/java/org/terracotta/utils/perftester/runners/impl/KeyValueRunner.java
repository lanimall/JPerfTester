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
public abstract class KeyValueRunner<T> extends BaseRunner implements Runner {
	private static Logger log = LoggerFactory.getLogger(KeyValueRunner.class);

	protected final ObjectGenerator<T> keyGenerator;
	
	protected KeyValueRunner(Condition termination, ObjectGenerator<T> keyGenerator) {
		super(termination);
		
		if(termination == null)
			throw new IllegalArgumentException("Termination object may not be null");
		
		this.keyGenerator = keyGenerator;
	}
	
	public abstract void doUnitOfWork(T arg);

	@Override
	protected void execute() {
		long iterationStartTime;
		T key = null;
		
		//avoid performing null check at every iteration
		if(null != keyGenerator){
			do {
				try {
					//perform the object generation outside the loop timing to avoid timing issues if object generation is slow
					key = keyGenerator.generate();
					
					iterationStartTime = System.currentTimeMillis();
					doUnitOfWork(key);
					stats.add(System.currentTimeMillis() - iterationStartTime);
				} catch (Exception e) {
					log.error("Error during execution of unit of work. will not count this operation in the timing stats.", e);
				}
			} while (!termination.isDone());
		} else {
			do {
				try {
					iterationStartTime = System.currentTimeMillis();
					doUnitOfWork(null);
					stats.add(System.currentTimeMillis() - iterationStartTime);
				} catch (Exception e) {
					log.error("Error during execution of unit of work. will not count this operation in the timing stats.", e);
				}
			} while (!termination.isDone());
		}
	}
}
