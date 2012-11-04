package org.terracotta.utils.perftester.runners.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.monitoring.Stats;
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
public abstract class AbstractRunner<T> implements Runner {
	private static Logger log = LoggerFactory.getLogger(AbstractRunner.class);

	private Stats stats = new Stats();
	protected Condition termination;
	protected final ObjectGenerator<T> objGenerator;
	
	protected AbstractRunner(Condition termination, ObjectGenerator<T> objGenerator) {
		super();
		this.termination = termination;
		this.objGenerator = objGenerator;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	@Override
	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public Condition getTermination() {
		return termination;
	}

	public void setTermination(Condition termination) {
		this.termination = termination;
	}

	@Override
	public String getName(){
		return this.getClass().toString();
	}

	@Override
	public void doBeforeRun() {
		//noop
	}

	@Override
	public void doAfterRun() {
		//noop
	}

	public abstract void doUnitOfWork(T arg);

	@Override
	public void run() {
		long start = System.currentTimeMillis();
		try {
			doBeforeRun();
			execute();
		} catch(Exception e) {
			log.error("Error in processing Pending Events.", e);
		} finally {
			try {
				//make sure we execute the after load
				doAfterRun();
			} catch (Exception e) {
				log.error("Error in during execution of the afterLoad().", e);
			}
			long stop = System.currentTimeMillis();
			log.debug("Iterations done in {}ms", stop - start);
		}
	}

	private void execute() {
		if(log.isInfoEnabled())
			log.info("Entering Operation execute()");

		long iterationStartTime;
		T arg = null;
		
		stats.reset();
		
		//avoid performing null check at every iteration
		if(null != objGenerator){
			do {
				try {
					//perform the object generation outside the loop timing to avoid timing issues if object generation is slow
					arg = objGenerator.generate();
					
					iterationStartTime = System.currentTimeMillis();
					doUnitOfWork(arg);
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
		
		stats.finalise();

		stats.printToConsole("Final Stats for:" + getName());
	}
}
