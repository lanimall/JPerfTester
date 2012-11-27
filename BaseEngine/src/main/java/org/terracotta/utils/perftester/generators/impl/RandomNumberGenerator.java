package org.terracotta.utils.perftester.generators.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabien Sanglier
 * 
 */
public class RandomNumberGenerator extends RandomGenerator<Long> {
	private static Logger log = LoggerFactory.getLogger(RandomNumberGenerator.class);
	
	private Integer nbDigits;
	private Integer[] prependDigits;
	private Integer[] appendDigits;
	
	public RandomNumberGenerator(Integer nbDigits) {
		this(nbDigits,null,null);
	}
	
	public RandomNumberGenerator(Integer nbDigits, Integer[] prependDigits, Integer[] appendDigits) {
		super();
		this.nbDigits = nbDigits;
		this.prependDigits = prependDigits;
		this.appendDigits = appendDigits;
	}

	@Override
	protected Long generateSafe() throws Exception {
		return randomUtil.generateRandomNumeric(nbDigits, prependDigits, appendDigits);
	}
}