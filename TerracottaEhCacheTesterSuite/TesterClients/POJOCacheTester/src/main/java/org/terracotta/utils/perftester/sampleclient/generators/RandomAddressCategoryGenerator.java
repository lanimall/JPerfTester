package org.terracotta.utils.perftester.sampleclient.generators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.generators.impl.RandomGenerator;
import org.terracotta.utils.perftester.sampleclient.domain.AddressCategory;

public class RandomAddressCategoryGenerator extends RandomGenerator<AddressCategory> {
	private static Logger log = LoggerFactory.getLogger(RandomAddressCategoryGenerator.class);
	
	@Override
	protected AddressCategory generateSafe() throws Exception {
		AddressCategory addressCategory = new AddressCategory();
		addressCategory.setType(AddressCategory.valuesCategoryType[randomUtil.generateRandomInt(AddressCategory.valuesCategoryType.length)]);
		addressCategory.setSubType(AddressCategory.valuesCategorySubType[randomUtil.generateRandomInt(AddressCategory.valuesCategorySubType.length)]);
		return addressCategory;
	}
}
