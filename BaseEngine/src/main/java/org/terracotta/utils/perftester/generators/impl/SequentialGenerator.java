package org.terracotta.utils.perftester.generators.impl;

import java.util.concurrent.atomic.AtomicLong;

import org.terracotta.utils.perftester.generators.ObjectGenerator;

/**
 * @author Fabien Sanglier
 * A generator that creates sequential numbers
 * Thread-Safe
 * 
 */
public class SequentialGenerator implements ObjectGenerator<Long> {
	private AtomicLong counter;

	public SequentialGenerator() {
		this(0);
	}
	
	public SequentialGenerator(long start) {
		counter = new AtomicLong(start);
	}
	
	public Long generate() {
		return counter.getAndIncrement();
	}

	@Override
	public String toString() {
		return "SequentialGenerator [counter=" + counter.toString() + "]";
	}
}
