package org.terracotta.utils.perftester.generators.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.GlobalConfigSingleton;
import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.generators.ObjectGeneratorFactory;

public class RandomBytesArrayGeneratorFactory implements ObjectGeneratorFactory {
	private static Logger log = LoggerFactory.getLogger(RandomBytesArrayGeneratorFactory.class);
	public static final int DEFAULT_SIZE = 1024;
	public static final int DEFAULT_DEPTH = 10;
	
	public static final String PARAM_MINSIZE = "minsize";
	public static final String PARAM_MAXSIZE = "maxsize";
	public static final String PARAM_DEPTH = "depth";
	
	@Override
	public ObjectGenerator createObjectGenerator() {
		int minSize = GlobalConfigSingleton.getInstance().getPropWrapper().getPropertyAsInt(RandomBytesArrayGeneratorFactory.class.getName() + "." + PARAM_MINSIZE, DEFAULT_SIZE);
		int maxSize = GlobalConfigSingleton.getInstance().getPropWrapper().getPropertyAsInt(RandomBytesArrayGeneratorFactory.class.getName() + "." + PARAM_MAXSIZE, DEFAULT_SIZE);
		int depth = GlobalConfigSingleton.getInstance().getPropWrapper().getPropertyAsInt(RandomBytesArrayGeneratorFactory.class.getName() + "." + PARAM_DEPTH, DEFAULT_SIZE);

		return new RandomByteArrayGenerator(minSize, maxSize, depth);
	}
}