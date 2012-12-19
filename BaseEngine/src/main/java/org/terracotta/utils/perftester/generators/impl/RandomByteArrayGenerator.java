package org.terracotta.utils.perftester.generators.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabien Sanglier
 * 
 */
public class RandomByteArrayGenerator extends RandomGenerator<Byte[]> {
	private static Logger log = LoggerFactory.getLogger(RandomByteArrayGenerator.class);
	
	private int size;
	
	public RandomByteArrayGenerator(int size) {
		super();
		
		if(log.isDebugEnabled())
			log.debug("Creating a RandomByteArrayGenerator with size:" + size);
		
		this.size = size;
	}

	@Override
	protected Byte[] generateSafe() throws Exception {
		return new Byte[size];
	}
}