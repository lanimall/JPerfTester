package org.terracotta.utils.commons;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author Fabien Sanglier
 * 
 */
public class RandomUtil {
	private Random hdrRndm = new Random();

	public RandomUtil(){
		hdrRndm.setSeed(System.currentTimeMillis());
	}

	//convert the int value into a "xxx-xxx-xxxx" string
	public String generateRandomSSN() throws Exception{
		return generateRandomNumericString(3)+"-"+generateRandomNumericString(2)+"-"+generateRandomNumericString(4);
	}

	//convert the int value into a "xxx-xxx-xxxx" string
	public String generateRandomSSN(long number){
		String ssn = String.format("%09d", number);
		return ssn.substring(0, 3) + "-" + ssn.substring(3, 5) + "-" + ssn.substring(5, 9);
	}

	public boolean generateRandomBoolean(){
		return generateRandomBoolean(2);
	}

	public boolean generateRandomBoolean(int ratioTrueFalse){
		return generateRandom(ratioTrueFalse) == 0;
	}

	public int generateRandom(int length){
		int value = hdrRndm.nextInt(length);
		return value;
	}

	public int generateRandom(int minValue, int maxValue, boolean maxInclusive) throws Exception{
		if(maxValue < minValue)
			throw new Exception("max value should be higher than min value");

		int rdmValue;
		if(maxInclusive)
			rdmValue = hdrRndm.nextInt(maxValue-minValue+1);
		else
			rdmValue = hdrRndm.nextInt(maxValue-minValue);

		return minValue + rdmValue;
	}

	public String generateRandomUUID(){
		return UUID.randomUUID().toString();
	}

	public long generateRandomLong(){
		long value = hdrRndm.nextLong();
		return value;
	}

	public Double generateRandomDouble(){
		Double value = hdrRndm.nextDouble();
		return value;
	}

	public Float generateRandomFloat(){
		Float value = hdrRndm.nextFloat();
		return value;
	}

	public BigDecimal generateRandomDecimal(int Length) throws Exception{
		if(Length == 0){
			return null;
		}
		String sNums = generateRandomNumericString(Length);
		String sNums2 = generateRandomNumericString(2);
		sNums = sNums + "." + sNums2;
		return new BigDecimal(sNums);
	}

	public Long generateRandomNumeric(int length, int prependDigits, int appendDigits)throws Exception{
		if(length == 0){
			return null;
		}
		String sNums = generateRandomNumericString(length);

		if(prependDigits > -1)
			sNums = prependDigits + sNums;

		if(appendDigits > -1)
			sNums =  sNums + appendDigits;

		return new Long(sNums);
	}

	public Long generateRandomNumeric(int length, int prependDigits) throws Exception{
		return generateRandomNumeric(length, prependDigits, -1);
	}

	public Long generateRandomNumeric(int length) throws Exception{
		return generateRandomNumeric(length, -1, -1);
	}

	public String generateRandomAlphaString(int StringLength)throws Exception{
		if(StringLength == 0){
			return null;
		}
		StringBuffer returnVal = new StringBuffer();
		String[] vals = {"a","b","c","d","e","f","g","h","i","j","k","l","m",
				"n","o","p","q","r","s","t","u","v","w","x","y","z"};
		for(int lp = 0;lp < StringLength; lp++){
			returnVal.append(vals[generateRandom(vals.length)]);
		}
		return returnVal.toString();
	}
	public String generateRandomText(int StringLength)throws Exception{
		if(StringLength == 0){
			return null;
		}
		StringBuffer returnVal = new StringBuffer();
		String[] vals = {"a","b","c","d","e","f","g","h","i","j",
				"k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
		for(int lp = 0;lp < StringLength; lp++){
			returnVal.append(vals[generateRandom(vals.length)]);
		}
		return returnVal.toString();
	}
	public String generateRandomNumericString(int StringLength)throws Exception{
		if(StringLength == 0){
			return null;
		}
		StringBuffer returnVal = new StringBuffer();
		String[] vals = {"0","1","2","3","4","5","6","7","8","9"};
		for(int lp = 0;lp < StringLength; lp++){
			returnVal.append(vals[generateRandom(vals.length)]);
		}
		return returnVal.toString();
	}

	public Integer generateRandomNumbers(int length, int prependDigits, int appendDigits) throws Exception{
		if(length == 0){
			return null;
		}
		String sNums = generateRandomNumericString(length);

		if(prependDigits > -1)
			sNums = prependDigits + sNums;

		if(appendDigits > -1)
			sNums =  sNums + appendDigits;

		return new Integer(sNums);
	}

	public Integer generateRandomNumbers(int length, int prependDigits) throws Exception{
		return generateRandomNumbers(length, prependDigits, -1);
	}

	public Integer generateRandomNumbers(int length)throws Exception{
		return generateRandomNumbers(length, -1, -1);
	}

	public int[] generateRandomIntArray(int arrayLength, int randomDigitLength) throws Exception{
		int[] array = new int[arrayLength];
		for(int i=0;i<arrayLength; i++){
			array[i] = generateRandomNumbers(randomDigitLength);
		}

		return array;
	}
	
	public float[] generateRandomFloatArray(int arrayLength) throws Exception{
		float[] array = new float[arrayLength];
		for(int i=0;i<arrayLength; i++){
			array[i] = generateRandomFloat();
		}

		return array;
	}
	
	public long[] generateRandomLongArray(int arrayLength, int randomDigitLength) throws Exception{
		long[] array = new long[arrayLength];
		for(int i=0;i<arrayLength; i++){
			array[i] = generateRandomNumeric(randomDigitLength);
		}

		return array;
	}
	
	public String[] generateRandomStringArray(int arrayLength, int randomStringLength) throws Exception{
		String[] array = new String[arrayLength];
		for(int i=0;i<arrayLength; i++){
			array[i] = generateRandomText(randomStringLength);
		}

		return array;
	}

	public <T> T getRandomObjectFromList(List<T> objList)throws Exception{
		if(objList == null || objList.size() == 0){
			return null;
		} else if(objList.size() == 1){
			return objList.get(0);
		}
		int lGetObjIndex = generateRandom(objList.size());
		return objList.get(lGetObjIndex);
	}

	public <T> T getRandomObjectFromArray(T[] objArray)throws Exception{
		if(objArray == null || objArray.length == 0){
			return null;
		}else if(objArray.length == 1){
			return objArray[0];
		}
		int lGetObjIndex = generateRandom(objArray.length);
		return objArray[lGetObjIndex];
	}

	/**Returns a string of randomly generate alpha numeric characters. This string
	 * will be the length specified by the input parameter.
	 * @param length the total length of random characters you wish returned
	 * @return <b>string</b> a randomly generated string of alphanumeric characters only
	 */
	public String getAlphaNumericRandom(int length) throws Exception{
		Random generator = new Random();
		String[] mapOfCharacters = getCharacterMap();
		StringBuffer sRandomString = new StringBuffer();

		// Now lets return the number of characters requested
		for (int j = 0; j < length; j++) {
			int rndm = generator.nextInt(61) + 0;
			String sItem = mapOfCharacters[rndm];
			sRandomString.append(sItem);
		}
		return sRandomString.toString();
	}

	public Date getRandomDate() {
		return getRandomDateBetween(getRandomDate(1900), Calendar.getInstance().getTime());
	}

	public Date getRandomDate(int year) {
		return getRandomDate(year, null, true);
	}

	public Date getRandomDate(int year, Date cutOff, boolean afterCutOff) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, 1, 1);

		Calendar cal2 = Calendar.getInstance();
		cal2.set(year, 12, 31);

		if(cutOff != null){
			if(afterCutOff){
				return getRandomDateBetween(cutOff, cal2.getTime());
			}
			else{
				return getRandomDateBetween(cal.getTime(), cutOff);
			}
		}

		return getRandomDateBetween(cal.getTime(), cal2.getTime());
	}

	public Date getRandomDateBetween(Date from, Date to) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(from);
		BigDecimal decFrom = new BigDecimal(cal1.getTimeInMillis());

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(to);
		BigDecimal decTo = new BigDecimal(cal2.getTimeInMillis());

		BigDecimal selisih = decTo.subtract(decFrom);
		BigDecimal factor = selisih.multiply(new BigDecimal(Math.random()));

		return new Date((factor.add(decFrom)).longValue());
	}

	private String[] getCharacterMap() throws Exception{
		String[] universeValues = new String[62];
		int asciiAlpha = 65; // The start of the alpha ascii character set

		// Add the numbers
		for (int i = 0; i < 62; i++) {
			if (i < 10) {
				// numbers zero through 9
				universeValues[i] = new Integer(i).toString();
			} else {
				universeValues[i] = Character.toString((char) asciiAlpha);
				// 91 - 96 are not alpha characters in the ascii map
				if (asciiAlpha + 1 == 91) {
					asciiAlpha = 97;
				} else {
					asciiAlpha = asciiAlpha + 1;
				}
			}
		}
		return universeValues;
	}
}
