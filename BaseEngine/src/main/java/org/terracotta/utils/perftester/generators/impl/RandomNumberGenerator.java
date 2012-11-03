package org.terracotta.utils.perftester.generators.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabien Sanglier
 * 
 */
public class RandomNumberGenerator extends RandomGenerator<Long> {
	private static Logger log = LoggerFactory.getLogger(RandomNumberGenerator.class);
	
	private int nbDigits;
	private int prependDigits;
	private int appendDigits;
	
	public RandomNumberGenerator(int nbDigits, int prependDigits,
			int appendDigits) {
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