package org.terracotta.utils.perftester.generators.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.GlobalConfigSingleton;
import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.generators.ObjectGeneratorFactory;

public class RandomNumberGeneratorFactory implements ObjectGeneratorFactory {
	private static Logger log = LoggerFactory.getLogger(RandomNumberGeneratorFactory.class);
	public static final int DEFAULT_MIN = 0;
	public static final int DEFAULT_MAX = 10000;
	
	public static final String PARAM_MINSIZE = "min";
	public static final String PARAM_MAXSIZE = "max";
	
	@Override
	public ObjectGenerator createObjectGenerator() {
		int min = GlobalConfigSingleton.getInstance().getPropWrapper().getPropertyAsInt(RandomNumberGeneratorFactory.class.getName() + "." + PARAM_MINSIZE, DEFAULT_MIN);
		int max = GlobalConfigSingleton.getInstance().getPropWrapper().getPropertyAsInt(RandomNumberGeneratorFactory.class.getName() + "." + PARAM_MAXSIZE, DEFAULT_MAX);
		
		return new RandomNumberGenerator(min, max);
	}
}