package org.terracotta.utils.commons;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
	public void testGenerateRandomLongFromTo() {
		long from = 123456;
		long to = 2345000;

		int i = 0;
		while(i < 1000000){
			long rdm = randomUtil.generateRandomLong(from, to);
			assertTrue(
					rdm <= to && rdm >= from
			);
			i++;
		}
		
		//check the opposite stays true
		long fromFalse = from - 100000;
		long toFalse = from-1;
		i = 0;
		while(i < 1000000){
			long rdm = randomUtil.generateRandomLong(fromFalse, toFalse);
			assertTrue(
					!(rdm <= to && rdm >= from)
			);
			i++;
		}
	}

	@Test
	public void testGenerateRandomDateYear() {
		int year = 2002;

		int i = 0;
		while(i < 1000000){
			Date date = randomUtil.generateRandomDate(year);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			assertTrue(
					year == cal.get(Calendar.YEAR)
			);
			i++;
		}
		
		//check the opposite stays true
		int yearFalse = year - 5;
		i = 0;
		while(i < 1000000){
			Date date = randomUtil.generateRandomDate(yearFalse);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			assertTrue(
					year != cal.get(Calendar.YEAR)
			);
			i++;
		}
	}

	@Test
	public void testGenerateRandomDateBetween() {
		Calendar calFrom = Calendar.getInstance();
		calFrom.set(1900, 1, 1);

		Calendar calTo = Calendar.getInstance();
		calTo.set(2012, 1, 1);

		int i = 0;
		while(i < 1000000){
			Date date = randomUtil.generateRandomDateBetween(calFrom.getTime(), calTo.getTime());

			Calendar calRdm = Calendar.getInstance();
			calRdm.setTime(date);

			assertTrue(
					calRdm.before(calTo) && calRdm.after(calFrom)
			);
			i++;
		}
		
		//check the opposite stays true
		Calendar calFromFalse = Calendar.getInstance();
		calFromFalse.setTime(calFrom.getTime());
		calFromFalse.add(Calendar.YEAR, -50);

		Calendar calToFalse = Calendar.getInstance();
		calToFalse.setTime(calFrom.getTime());
		calToFalse.add(Calendar.YEAR, -1);
		
		i = 0;
		while(i < 1000000){
			Date dateRdm = randomUtil.generateRandomDateBetween(calFromFalse.getTime(), calToFalse.getTime());

			Calendar calRdm = Calendar.getInstance();
			calRdm.setTime(dateRdm);

			assertTrue(
					!(calRdm.before(calTo) && calRdm.after(calFrom))
			);
			i++;
		}
	}

}