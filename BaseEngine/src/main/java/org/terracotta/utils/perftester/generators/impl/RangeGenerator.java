package org.terracotta.utils.perftester.generators.impl;

import java.util.concurrent.atomic.AtomicLong;

import org.terracotta.utils.perftester.generators.ObjectGenerator;

/**
 * @author Fabien Sanglier
 * 
 */
public class RangeGenerator implements ObjectGenerator<Long[]> {
	private AtomicLong counter;
	private int stride = 1;

	public RangeGenerator(int stride) throws Exception {
		this(0, stride);
	}

	public RangeGenerator(long start, int stride) {
		if(stride < 1)
			throw new IllegalArgumentException("Stride param must be positive integer.");
		
		this.counter =  new AtomicLong(start);
		this.stride = stride;
	}

	@Override
	public Long[] generate() {
		long start = counter.getAndAdd(stride);
		Long[] range = new Long[stride];
		range[0] = start;
		for(int i = 1; i < stride; i++){
			range[i] = range[i-1] + 1;
		}
		return range;
	}

	@Override
	public String toString() {
		return "RangeGenerator [counter=" + counter + ", stride=" + stride
				+ "]";
	}
}