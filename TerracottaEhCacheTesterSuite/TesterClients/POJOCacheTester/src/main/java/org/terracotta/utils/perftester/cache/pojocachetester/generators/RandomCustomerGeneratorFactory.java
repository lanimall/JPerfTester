package org.terracotta.utils.perftester.cache.pojocachetester.generators;

import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.generators.ObjectGeneratorFactory;

public class RandomCustomerGeneratorFactory implements ObjectGeneratorFactory {
	@Override
	public ObjectGenerator createObjectGenerator() {
		return new RandomCustomerGenerator(new RandomAddressGenerator(new RandomAddressCategoryGenerator()));
	}
}