package org.terracotta.utils.perftester.runners;

import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.conditions.impl.IterationCondition;

/**
 * @author Fabien Sanglier
 * 
 */
public abstract class OpsCountRunnerFactory implements RunnerFactory {
	private final long numOperations;
	
	protected OpsCountRunnerFactory(long numOperations) {
		this.numOperations = numOperations;
	}

	public long getNumOperations() {
		return numOperations;
	}

	public Condition getTerminationCondition(){
		return new IterationCondition(getNumOperations());
	}
	
	public abstract Runner create();
}