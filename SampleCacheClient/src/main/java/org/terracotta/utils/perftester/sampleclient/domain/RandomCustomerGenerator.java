package org.terracotta.utils.perftester.sampleclient.domain;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.generators.impl.RandomGenerator;

public class RandomCustomerGenerator extends RandomGenerator<Customer> {
	private static Logger log = LoggerFactory.getLogger(RandomCustomerGenerator.class);
	private RandomAddressGenerator randomAddressGenerator;

	public RandomCustomerGenerator(RandomAddressGenerator randomAddressGenerator) {
		super();
		this.randomAddressGenerator = randomAddressGenerator;
	}

	@Override
	protected Customer generateSafe() throws Exception {
		log.debug("Creating random person object");
		Customer customer = new Customer();
		customer.setFirstName(randomUtil.getRandomObjectFromList(firstNameList));
		customer.setLastName(randomUtil.getRandomObjectFromList(lastNameList));
		customer.setMiddleName(randomUtil.getRandomObjectFromList(firstNameList));
		customer.setDob(randomUtil.getRandomDate());
		customer.setOccupation(randomUtil.generateRandomText(30));
		
		if(null != randomAddressGenerator)
			customer.setAddress(randomAddressGenerator.generate());

		log.debug("Customer created:" + customer.toString());
		return customer;
	}
	
	public static final List<String> firstNameList = new ArrayList<String>();
	public static final List<String> lastNameList = new ArrayList<String>();
	static {
		lastNameList.add("Smith");
		lastNameList.add("Baker");
		lastNameList.add("Finley");
		lastNameList.add("Hunter");
		lastNameList.add("Furter");
		lastNameList.add("Carlson");
		lastNameList.add("Beaks");
		lastNameList.add("Jones");
		lastNameList.add("Hathaway");
		lastNameList.add("Lawson");
		lastNameList.add("Night");
		lastNameList.add("Yunts");
		lastNameList.add("Jenks");
		lastNameList.add("Williams");
		lastNameList.add("Potter");
		lastNameList.add("Henry");
		lastNameList.add("McKenny");
		lastNameList.add("Woods");
		lastNameList.add("Milborne");
		lastNameList.add("Winkler");
		lastNameList.add("Katz");
		lastNameList.add("Wilkinson");
		lastNameList.add("Barns");
		lastNameList.add("Cobra");

		firstNameList.add("Joe");
		firstNameList.add("Sally");
		firstNameList.add("Sara");
		firstNameList.add("Holly");
		firstNameList.add("Frank");
		firstNameList.add("George");
		firstNameList.add("Henry");
		firstNameList.add("Eric");
		firstNameList.add("Tracey");
		firstNameList.add("Kyle");
		firstNameList.add("Hailey");
		firstNameList.add("Sue");
		firstNameList.add("Dan");
	}
}
