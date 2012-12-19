package org.terracotta.utils.perftester.sampleclient.generators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.generators.impl.RandomGenerator;
import org.terracotta.utils.perftester.sampleclient.domain.Address;

public class RandomAddressGenerator extends RandomGenerator<Address> {
	private static Logger log = LoggerFactory.getLogger(RandomAddressGenerator.class);
	private RandomAddressCategoryGenerator randomAddressCategoryGenerator;

	public RandomAddressGenerator(RandomAddressCategoryGenerator randomAddressCategoryGenerator) {
		super();
		this.randomAddressCategoryGenerator = randomAddressCategoryGenerator;
	}
	
	@Override
	protected Address generateSafe() throws Exception {
		Address address = new Address();
		address.setLine1(randomUtil.generateRandomNumericString(5) + " " + randomUtil.generateRandomText(30) + " Avenue");
		address.setLine2(randomUtil.generateRandomNumericString(5));
		address.setCity(randomUtil.generateRandomText(10));
		address.setState(randomUtil.generateRandomText(2));
		address.setZip(randomUtil.generateRandomNumericString(5));
		address.setCountry(randomUtil.generateRandomText(20));
		
		if(null != randomAddressCategoryGenerator)
			address.setAddressCategory(randomAddressCategoryGenerator.generate());
		
		return address;
	}
}
