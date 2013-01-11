package org.terracotta.utils.perftester.conditions.impl;

import org.terracotta.utils.perftester.conditions.Condition;

/**
 * @author Fabien Sanglier
 * 
 */
public class IterationCondition implements Condition {
	protected final long nbIterations;
	private long counter = 0;

	public IterationCondition(long nbIterations) {
		this.nbIterations = nbIterations;
	}

	@Override
	public boolean isDone() {
		return ++counter > nbIterations - 1;
	}
}