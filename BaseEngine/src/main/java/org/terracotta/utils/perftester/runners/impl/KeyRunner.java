package org.terracotta.utils.perftester.runners.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.generators.impl.NullValueGenerator;
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
public abstract class KeyRunner<K> extends BareRunner implements Runner {
	private static Logger log = LoggerFactory.getLogger(KeyRunner.class);

	protected final ObjectGenerator<K> keyGenerator;
	private List<K> buffer;

	protected KeyRunner(Condition termination, ObjectGenerator<K> keyGenerator) {
		super(termination);

		if(termination == null)
			throw new IllegalArgumentException("Termination object may not be null");

		if(null == keyGenerator){
			log.warn("The keyGenerator object is null...make sure it's intended.");
			this.keyGenerator = new NullValueGenerator();
		} else {
			this.keyGenerator = keyGenerator;
		}
	}

	public abstract Object doUnitOfWork(K key);

	@Override
	public Object doUnitOfWork(){
		throw new UnsupportedOperationException("This method should not be accessed through the KeyRunner implementation");
	}

	@Override
	protected void execute() {
		Object unitOfWorkResult = null;
		K key = null;

		long iterationStartTime;
		if(enableDefaultStats()){
			do {
				try {
					//perform the object generation outside the timing to time more accurately the doUnitOfWork operation
					key = keyGenerator.generate();
					iterationStartTime = System.currentTimeMillis();
					unitOfWorkResult = doUnitOfWork(key);
					stats.add(System.currentTimeMillis() - iterationStartTime);
				} catch (Exception e) {
					log.error("Error during execution of unit of work. will not count this operation in the timing stats.", e);
				}
			} while (!termination.isDone(unitOfWorkResult));
		} else {
			do {
				try {
					key = keyGenerator.generate();
					unitOfWorkResult = doUnitOfWork(key);
				} catch (Exception e) {
					log.error("Error during execution of unit of work. will not count this operation in the timing stats.", e);
				}
			} while (!termination.isDone(unitOfWorkResult));
		}
	}
}
