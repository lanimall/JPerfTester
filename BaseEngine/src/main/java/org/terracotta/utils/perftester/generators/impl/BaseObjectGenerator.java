package org.terracotta.utils.perftester.generators.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.generators.ObjectGenerator;

public abstract class BaseObjectGenerator<T> implements ObjectGenerator<T> {
	private static Logger log = LoggerFactory.getLogger(RandomNumberGenerator.class);

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
