package org.terracotta.utils.perftester.generators.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.RandomUtil;

/**
 * @author Fabien Sanglier
 * 
 */
public abstract class RandomGenerator<T> extends BaseObjectGenerator<T> {
	private static Logger log = LoggerFactory.getLogger(RandomGenerator.class);

	protected final RandomUtil randomUtil;

	public RandomGenerator() {
		super();
		this.randomUtil = new RandomUtil();
	}
	
	public RandomGenerator(RandomUtil randomUtil) {
		super();
		this.randomUtil = randomUtil;
	}
}