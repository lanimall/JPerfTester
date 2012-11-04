package org.terracotta.utils.perftester.sampleclient.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.generators.impl.RandomGenerator;

public class RandomAddressCategoryGenerator extends RandomGenerator<AddressCategory> {
	private static Logger log = LoggerFactory.getLogger(RandomAddressCategoryGenerator.class);
	
	@Override
	protected AddressCategory generateSafe() throws Exception {
		AddressCategory addressCategory = new AddressCategory();
		addressCategory.setType(AddressCategory.valuesCategoryType[randomUtil.generateRandom(AddressCategory.valuesCategoryType.length)]);
		addressCategory.setSubType(AddressCategory.valuesCategorySubType[randomUtil.generateRandom(AddressCategory.valuesCategorySubType.length)]);
		return addressCategory;
	}
}
