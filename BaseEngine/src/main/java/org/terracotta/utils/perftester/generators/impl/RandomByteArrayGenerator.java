package org.terracotta.utils.perftester.generators.impl;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabien Sanglier
 * 
 */
public class RandomByteArrayGenerator extends RandomGenerator<byte[]> {
	private static Logger log = LoggerFactory.getLogger(RandomByteArrayGenerator.class);

	private int minSize;
	private int maxSize;

	public RandomByteArrayGenerator(int fixedSize) {
		this(fixedSize, fixedSize);
	}

	public RandomByteArrayGenerator(int minSize, int maxSize) {
		super();

		if(log.isDebugEnabled())
			log.debug("Creating a RandomByteArrayGenerator with minSize=" + minSize + " and maxSize=" + maxSize);

		if(maxSize < minSize)
			throw new IllegalArgumentException("max value should be higher than min value");

		if(maxSize == 0 && minSize == 0)
			throw new IllegalArgumentException("max and min value should not be 0...For this load tester tool, it does not make sense to generate a byte array with 0 elements in it...");

		this.minSize = minSize;
		this.maxSize = maxSize;
	}

	@Override
	protected byte[] generateSafe() throws Exception {
		final int size = randomUtil.generateRandomInt(minSize, maxSize, true);
		byte[] object = new byte[size];
		Arrays.fill(object, (byte)randomUtil.generateRandomInt());
		return object;
	}
}