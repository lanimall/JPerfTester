package org.terracotta.utils.perftester.conditions.impl;

import java.util.List;

import org.terracotta.utils.perftester.conditions.Condition;

public class ListLowerThanSizeCondition implements Condition<List> {
	private final int maxSize;
	
	public ListLowerThanSizeCondition(int maxSize) {
		super();
		this.maxSize = maxSize;
	}

	@Override
	public boolean isDone(final List obj) {
		return obj == null || obj.size() < maxSize;
	}
}