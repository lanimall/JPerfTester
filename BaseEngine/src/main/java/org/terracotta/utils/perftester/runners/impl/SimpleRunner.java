package org.terracotta.utils.perftester.runners.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.generators.impl.SequentialGenerator;
import org.terracotta.utils.perftester.runners.OpsCountRunnerFactory;

/**
 * @author Fabien Sanglier
 * 
 */
public class SimpleRunner extends KeyValueRunner<Long> {
	
	private static Logger log = LoggerFactory.getLogger(SimpleRunner.class);
	private static final boolean isDebug = log.isDebugEnabled();

	public SimpleRunner(Condition termination, ObjectGenerator<Long> generator) {
		super(termination,generator);
	}

	@Override
	public String getName() {
		return "Simple Key/Value Runner";
	}

	@Override
	public void doUnitOfWork(Long key) {
		if(isDebug)
			log.debug("Do something with this key:" + key);
	}
	
	public static class SimpleIterativeRunnerFactory extends OpsCountRunnerFactory {
		public SimpleIterativeRunnerFactory(long numOperations) {
			super(numOperations);
		}

		@Override
		public SimpleRunner create() {
			return new SimpleRunner(getTerminationCondition(), new SequentialGenerator());	
		}
	}
}