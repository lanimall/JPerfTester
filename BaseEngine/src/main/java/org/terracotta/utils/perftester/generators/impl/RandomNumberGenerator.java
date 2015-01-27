package org.terracotta.utils.perftester.generators.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabien Sanglier
 * 
 */
public class RandomNumberGenerator extends RandomGenerator<Long> {
	private static Logger log = LoggerFactory.getLogger(RandomNumberGenerator.class);
	
	private long min;
	private long max;
	
	public RandomNumberGenerator(long max) {
		this(0, max);
	}
	
	public RandomNumberGenerator(long min, long max) {
		super();
		this.min = min;
		this.max = max;
	}

	@Override
	protected Long generateSafe() throws Exception {
		return randomUtil.generateRandomLong(min, max);
	}

	@Override
	public String toString() {
		return "RandomNumberGenerator [min=" + min + ", max=" + max + "]";
	}
}