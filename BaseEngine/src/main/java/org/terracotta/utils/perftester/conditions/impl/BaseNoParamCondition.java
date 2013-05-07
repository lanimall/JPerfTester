package org.terracotta.utils.perftester.conditions.impl;

import org.terracotta.utils.perftester.conditions.Condition;

public abstract class BaseNoParamCondition implements Condition {
	@Override
	public boolean isDone(Object obj) {
		return isDone();
	}

	public abstract boolean isDone();
}
