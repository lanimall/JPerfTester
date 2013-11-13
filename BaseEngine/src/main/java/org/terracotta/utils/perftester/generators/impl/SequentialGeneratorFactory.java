package org.terracotta.utils.perftester.generators.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.GlobalConfigSingleton;
import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.generators.ObjectGeneratorFactory;

public class SequentialGeneratorFactory implements ObjectGeneratorFactory {
	private static Logger log = LoggerFactory.getLogger(SequentialGeneratorFactory.class);
	public static final int DEFAULT_START = 0;
	public static final String PARAM_START = "start";
	
	@Override
	public ObjectGenerator createObjectGenerator() {
		long start = GlobalConfigSingleton.getInstance().getPropWrapper().getPropertyAsInt(SequentialGeneratorFactory.class.getName() + "." + PARAM_START, DEFAULT_START);
		return new SequentialGenerator(start);
	}
}