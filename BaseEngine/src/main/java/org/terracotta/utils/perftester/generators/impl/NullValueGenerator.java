package org.terracotta.utils.perftester.generators.impl;

import org.terracotta.utils.perftester.generators.ObjectGenerator;

/**
 * @author Fabien Sanglier
 * A Null generator used in case no generator is specified...simplifying the code
 */
public class NullValueGenerator implements ObjectGenerator {
	public NullValueGenerator() {
		//do nothing
	}
	
	public Object generate() {
		return null;
	}
}