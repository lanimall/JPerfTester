package org.terracotta.utils.perftester.runners.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.conditions.impl.IterationCondition;
import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.generators.impl.SequentialGenerator;
import org.terracotta.utils.perftester.runners.RunnerFactory;

/**
 * @author Fabien Sanglier
 * 
 */
public class SimpleRunner extends AbstractRunner<Long> {
	private static Logger log = LoggerFactory.getLogger(SimpleRunner.class);
	private static final boolean isDebug = log.isDebugEnabled();

	public SimpleRunner(Condition termination, ObjectGenerator<Long> generator) {
		super(termination,generator);
	}

	@Override
	public void doUnitOfWork(Long key) {
		if(isDebug)
			log.debug("Do something with this key:" + key);
	}
	
	public static class SimpleIterativeRunnerFactory extends RunnerFactory {
		public SimpleIterativeRunnerFactory(int numThreads, long numOperations) {
			super(numThreads, numOperations);
		}

		@Override
		public SimpleRunner create() {
			SequentialGenerator keyGen = new SequentialGenerator();
			return new SimpleRunner(new IterationCondition(getNumOperations() / getNumThreads()),keyGen);	
		}
	}
}