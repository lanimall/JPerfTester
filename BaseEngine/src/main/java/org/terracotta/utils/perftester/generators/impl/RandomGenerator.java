package org.terracotta.utils.perftester.generators.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.RandomUtil;
import org.terracotta.utils.perftester.generators.ObjectGenerator;

/**
 * @author Fabien Sanglier
 * 
 */
public abstract class RandomGenerator<T> implements ObjectGenerator<T> {
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

	@Override
	public T generate() {
		try {
			return generateSafe();
		} catch (Exception e) {
			log.error("An unexpected error", e);
		}
		return null;
	}
	
	protected abstract T generateSafe() throws Exception;
}