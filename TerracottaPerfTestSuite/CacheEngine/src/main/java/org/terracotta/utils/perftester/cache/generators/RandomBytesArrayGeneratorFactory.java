package org.terracotta.utils.perftester.cache.generators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.cache.configs.GlobalConfigSingleton;
import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.generators.ObjectGeneratorFactory;
import org.terracotta.utils.perftester.generators.impl.RandomByteArrayGenerator;

public class RandomBytesArrayGeneratorFactory implements ObjectGeneratorFactory {
	private static Logger log = LoggerFactory.getLogger(RandomBytesArrayGeneratorFactory.class);
	public static final int DEFAULT_SIZE = 1024;
	
	@Override
	public ObjectGenerator createObjectGenerator() {
		int size = GlobalConfigSingleton.getInstance().getPropWrapper().getPropertyAsInt(RandomBytesArrayGeneratorFactory.class.getName() + ".size", DEFAULT_SIZE);
		return new RandomByteArrayGenerator(size);
	}
}