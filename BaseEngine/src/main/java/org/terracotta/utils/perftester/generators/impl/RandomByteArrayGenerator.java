package org.terracotta.utils.perftester.generators.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabien Sanglier
 * 
 */
public class RandomByteArrayGenerator extends RandomGenerator<Collection<byte[]>> {
	private static Logger log = LoggerFactory.getLogger(RandomByteArrayGenerator.class);

	private final int minSize;
	private final int maxSize;
	private final int depth;

	public RandomByteArrayGenerator(int byteArraySize) {
		this(byteArraySize, byteArraySize, 0);
	}

	public RandomByteArrayGenerator(final int minByteArraySize, final int maxByteArraySize, final int depth) {
		super();

		if(log.isDebugEnabled())
			log.debug("Creating a RandomByteArrayGenerator with minSize=" + minByteArraySize + " and maxSize=" + maxByteArraySize);

		if(maxByteArraySize < minByteArraySize)
			throw new IllegalArgumentException("max value should be higher than min value");

		if(maxByteArraySize == 0 && minByteArraySize == 0)
			throw new IllegalArgumentException("max and min value should not be 0...For this load tester tool, it does not make sense to generate a byte array with 0 elements in it...");

		this.minSize = minByteArraySize;
		this.maxSize = maxByteArraySize;
		this.depth = depth;
	}

	private Collection<byte[]> getCollection(final int recursiveCounter) {
		List returnList = new ArrayList();
		if (recursiveCounter > 0) {
			final int maxCollections = randomUtil.generateRandomInt(depth + 1);
			for (int j = 0; j < maxCollections; j++) {
				returnList.add(getCollection(recursiveCounter - 1));
			}
		} else {
			final int maxCollectionObjects = randomUtil.generateRandomInt(depth + 1);
			for (int j = 0; j < maxCollectionObjects; j++) {
				returnList.add(getRandomByteArray());
			}
		}
		return returnList;
	}

	private byte[] getRandomByteArray() {
		final int size = randomUtil.generateRandomInt(minSize, maxSize, true);
		byte[] object = new byte[size];
		Arrays.fill(object, (byte)randomUtil.generateRandomInt());
		return object;
	}

	@Override
	protected Collection<byte[]> generateSafe() throws Exception {
		return getCollection(depth);
	}
}