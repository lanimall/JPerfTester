package org.terracotta.utils.commons;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.terracotta.utils.commons.RandomUtil;

/**
 * @author Fabien Sanglier
 * 
 */
public class RandomUtilTest {
	private RandomUtil randomUtil = null;
	
	@Before
	public void setUp() throws Exception {
		randomUtil = new RandomUtil();
	}

	@After
	public void tearDown() throws Exception {
		randomUtil = null;
	}

	@Test
	public void generateRandomSSN(){
		long number = 123456789;
		String ssn = randomUtil.generateRandomSSN(number);
		assertTrue("123-45-6789".equals(ssn));

		number = 123;
		ssn = randomUtil.generateRandomSSN(number);
		assertTrue("000-00-0123".equals(ssn));

		number = 12345;
		ssn = randomUtil.generateRandomSSN(number);
		assertTrue("000-01-2345".equals(ssn));

		number = 12345678;
		ssn = randomUtil.generateRandomSSN(number);	
		assertTrue("012-34-5678".equals(ssn));

		//if the number is longer than 9 digits, then we truncate...
		number = 1234567899;
		ssn = randomUtil.generateRandomSSN(number);	
		assertTrue("123-45-6789".equals(ssn));
	}

	@Test
	public void testrandomUtil() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGenerateRandomBoolean() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGenerateRandomBooleanInt() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGenerateRandomInt() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGenerateRandomIntIntBoolean() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGenerateRandomLong() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGenerateRandomDouble() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGenerateRandomDecimal() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGenerateRandomNumeric() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGenerateRandomAlphaString() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGenerateRandomText() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGenerateRandomNumericString() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGenerateRandomNumbers() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetRandomObjectFromList() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetRandomObjectFromArray() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetAlphaNumericRandom() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetRandomDate() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetRandomDateInt() {
		int year = 2002;
		Date date = randomUtil.getRandomDate(year);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		assertTrue(
				year == cal.get(Calendar.YEAR)
		);
	}

	@Test
	public void testGetRandomDateBetween() {
		Calendar cal = Calendar.getInstance();
		cal.set(1900, 1, 1);

		Calendar cal2 = Calendar.getInstance();
		cal2.set(2012, 1, 1);

		Date date = randomUtil.getRandomDateBetween(cal.getTime(), cal2.getTime());

		Calendar cal3 = Calendar.getInstance();
		cal3.setTime(date);

		assertTrue(
				cal3.before(cal2) && cal3.after(cal)
		);
	}

}