package org.terracotta.utils.perftester.cache.customcachesearchtester.generators;

import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.generators.ObjectGeneratorFactory;

public class RandomCacheKeyGeneratorFactory implements ObjectGeneratorFactory {
	@Override
	public ObjectGenerator createObjectGenerator() {
		return new RandomCacheKeyGenerator();
	}
}