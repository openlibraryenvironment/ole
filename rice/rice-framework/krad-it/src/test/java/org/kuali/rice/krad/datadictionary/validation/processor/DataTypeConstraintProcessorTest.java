/**
 * Copyright 2005-2013 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.krad.datadictionary.validation.processor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.core.api.uif.DataType;
import org.kuali.rice.core.impl.datetime.DateTimeServiceImpl;
import org.kuali.rice.krad.datadictionary.AttributeDefinition;
import org.kuali.rice.krad.datadictionary.validation.AttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.ErrorLevel;
import org.kuali.rice.krad.datadictionary.validation.SingleAttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.constraint.DataTypeConstraint;
import org.kuali.rice.krad.datadictionary.validation.result.ConstraintValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;


/**
 * Things this test should check:
 * 
 * 1. Boolean as boolean true (success) {@link DataTypeConstraintProcessorTest#testBooleanAsBooleanTrue()}
 * 2. Boolean as boolean false (success) {@link DataTypeConstraintProcessorTest#testBooleanAsBooleanFalse()}
 * 3. Boolean as null (no constraint) {@link DataTypeConstraintProcessorTest#testBooleanAsNull()}
 * 4. Boolean as string "true" (success) {@link DataTypeConstraintProcessorTest#testBooleanAsStringTrue()}
 * 5. Boolean as string "false" (success) {@link DataTypeConstraintProcessorTest#testBooleanAsStringFalse()}
 * 6. Boolean as string "potato" (failure) {@link DataTypeConstraintProcessorTest#testBooleanAsStringPotato()}
 * 7. Integer as string "12" (success) {@link DataTypeConstraintProcessorTest#testIntegerAsString12()}
 * 8. Integer as out of integer range string (failure) {@link DataTypeConstraintProcessorTest#testIntegerAsOutOfRangeIntegerString()}
 * 9. Integer as negative integer (success) {@link DataTypeConstraintProcessorTest#testIntegerAsNegativeIntegerString()}
 * 10. Long as long (success) {@link DataTypeConstraintProcessorTest#testLongAsOutOfIntegerRangeLong()}
 * 11. Long as negative long (success) {@link DataTypeConstraintProcessorTest#testLongAsNegativeLong()}
 * 12. Long as out of integer range string (success) {@link DataTypeConstraintProcessorTest#testLongAsOutOfIntegerRangeString()}
 * 13. Double as string "234897.2323" (success) {@link DataTypeConstraintProcessorTest#testDoubleAsPositiveDoubleString()}
 * 14. Double as string "-234897.2323" (success) {@link DataTypeConstraintProcessorTest#testDoubleAsNegativeDouble()}
 * 15. Double as null (no constraint) {@link DataTypeConstraintProcessorTest#testDoubleAsNull()}
 * 16. Double as out of double range string (failure) {@link DataTypeConstraintProcessorTest#testDoubleAsOutOfDoubleRangeString()}
 * 17. Double as out of negative double range string (failure) {@link DataTypeConstraintProcessorTest#testDoubleAsNegativeOutOfDoubleRangeString()}
 * 18. Float as string "123.2" (success) {@link DataTypeConstraintProcessorTest#testFloatAsPositiveFloatString()}
 * 19. Float as string "-12312.42" (success) {@link DataTypeConstraintProcessorTest#testFloatAsNegativeFloatString()}
 * 20. Float as out of float range string (failure) {@link DataTypeConstraintProcessorTest#testFloatAsOutOfFloatRangeString()}
 * 21. Float as out of negative float range string (failure) {@link DataTypeConstraintProcessorTest#testFloatAsNegativeOutOfFloatRangeString()}
 * 22. Date as string in format yyyy-MM-dd'T'HH:mm:ss.SSSZ (success) {@link DataTypeConstraintProcessorTest#testDateAsStringInFormat1Success()}
 * 23. Date as string in format yyyy-MM-dd (success) {@link DataTypeConstraintProcessorTest#testDateAsStringInFormat2Success()}
 * 24. Date as string in format yyyy-MMM-dd (success) {@link DataTypeConstraintProcessorTest#testDateAsStringInFormat3Success()}
 * 25. Date as string in format dd-MM-yyyy (success) {@link DataTypeConstraintProcessorTest#testDateAsStringInFormat4Success()}
 * 26. Date as string in format dd-MMM-yyyy (success) {@link DataTypeConstraintProcessorTest#testDateAsStringInFormat5Success()}
 * 27. Date as string in format 'yyyymmdd' (failure) {@link DataTypeConstraintProcessorTest#testDateAsStringInFormat6Failure()}
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

//FIXME: cannot move to unit test dir b/c DateTimeService needs to be mocked.
public class DataTypeConstraintProcessorTest {

	private DataTypeConstraintProcessor processor;
	private AttributeValueReader attributeValueReader;
	
	private DataTypeConstraint booleanConstraint, dateConstraint, doubleConstraint, floatConstraint, integerConstraint, longConstraint;
	
	private DictionaryValidationResult dictionaryValidationResult;
	
	private String[] testStringToDateFormats = {"yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd", "yyyy-MMM-dd", "dd-MM-yyyy", "dd-MMM-yyyy"};
	
	@Before
	public void setUp() throws Exception {
		DateTimeServiceImpl dateTimeService = new DateTimeServiceImpl() {
			public Date convertToDate(String dateString) throws ParseException {
				return parseAgainstFormatArray(dateString, testStringToDateFormats);
			}
		};
		
		processor = new DataTypeConstraintProcessor();
		processor.setDateTimeService(dateTimeService);
		booleanConstraint = new SimpleDataTypeConstraint(DataType.BOOLEAN);
		dateConstraint = new SimpleDataTypeConstraint(DataType.DATE);
		doubleConstraint = new SimpleDataTypeConstraint(DataType.DOUBLE); 
		floatConstraint = new SimpleDataTypeConstraint(DataType.FLOAT);
		integerConstraint = new SimpleDataTypeConstraint(DataType.INTEGER);
		longConstraint = new SimpleDataTypeConstraint(DataType.LONG);
		dictionaryValidationResult = new DictionaryValidationResult();
	}
	
	@Test
	public void testBooleanAsBooleanTrue() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, Boolean.TRUE, booleanConstraint);
		assertSuccess(validationResult);
	}
	
	@Test
	public void testBooleanAsBooleanFalse() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, Boolean.FALSE, booleanConstraint);
		assertSuccess(validationResult);
	}
	
	@Test
	public void testBooleanAsNull() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, null, booleanConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.INAPPLICABLE, validationResult.getStatus());
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testBooleanAsStringTrue() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "true", booleanConstraint);
		assertSuccess(validationResult);
	}
	
	@Test
	public void testBooleanAsStringFalse() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "false", booleanConstraint);
		assertSuccess(validationResult);
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testBooleanAsStringPotato() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "potato", booleanConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, validationResult.getStatus());
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testIntegerAsString12() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "12", integerConstraint);
		assertSuccess(validationResult);
	}
	
	@Test
	public void testIntegerAsOutOfRangeIntegerString() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "923423423423423412", integerConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, validationResult.getStatus());
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testIntegerAsNegativeIntegerString() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "-3412", integerConstraint);
		assertSuccess(validationResult);
	}
	
	@Test
	public void testIntegerAsNull() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, null, integerConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		
		Assert.assertEquals(ErrorLevel.INAPPLICABLE, validationResult.getStatus());
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testIntegerAsStringPotato() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "potato", integerConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, validationResult.getStatus());
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testIntegerAsInteger12() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, Integer.valueOf(12), integerConstraint);
		assertSuccess(validationResult);
	}
	
	@Test
	public void testIntegerAsBigDecimal12() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, BigDecimal.valueOf(12), integerConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		
		assertSuccess(validationResult);
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testIntegerAsBigDecimal12point32() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, BigDecimal.valueOf(12.32), integerConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		assertSuccess(validationResult);
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testLongAsOutOfIntegerRangeString() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "923423423423423412", longConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		assertSuccess(validationResult);
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testLongAsOutOfIntegerRangeLong() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, Long.valueOf(923423423423423412l), longConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		assertSuccess(validationResult);
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testLongAsNegativeLong() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, Long.valueOf(-923423423423423412l), longConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		assertSuccess(validationResult);
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testDoubleAsPositiveDoubleString() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "234897.2323", doubleConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		assertSuccess(validationResult);
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testDoubleAsNegativeDoubleString() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "-234897.2323", doubleConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		assertSuccess(validationResult);
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testDoubleAsNull() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, null, doubleConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.INAPPLICABLE, validationResult.getStatus());
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testDoubleAsNegativeDouble() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, Double.valueOf(-234897.2323d), doubleConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		assertSuccess(validationResult);
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testDoubleAsOutOfDoubleRangeString() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "923423234234423423423412999999999999999999999999e12312321", doubleConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, validationResult.getStatus());
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testDoubleAsNegativeOutOfDoubleRangeString() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "-923423234234423423423412999999999999999999999999e99234234", doubleConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, validationResult.getStatus());
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testFloatAsPositiveFloatString() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "234897.2323", floatConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		assertSuccess(validationResult);
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testFloatAsNegativeFloatString() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "-234897.2323", floatConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		assertSuccess(validationResult);
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testFloatAsOutOfFloatRangeString() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "923423234234423423423412999999999999999999999999", floatConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, validationResult.getStatus());
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testFloatAsNegativeOutOfFloatRangeString() {
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "-923423234234423423423412999999999999999999999999", floatConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, validationResult.getStatus());
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	/*
	  	* 22. Date as string in format yyyy-MM-dd'T'HH:mm:ss.SSSZ (success)
		* 23. Date as string in format yyyy-MM-dd (success)
		* 24. Date as string in format yyyy-MMM-dd (success)
		* 25. Date as string in format dd-MM-yyyy (success)
		* 26. Date as string in format dd-MMM-yyyy (success)
		* 27. Date as string in format 'yyyymmdd' (failure)
	 */
	
	
	@Test
	public void testDateAsStringInFormat1Success() {
		// Format yyyy-MM-dd'T'HH:mm:ss.SSSZ
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "2001-07-04T12:08:56.235-0700", dateConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		assertSuccess(validationResult);
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testDateAsStringInFormat2Success() {
		// Format yyyy-MM-dd
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "2001-03-04", dateConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		assertSuccess(validationResult);
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testDateAsStringInFormat3Success() {
		// Format yyyy-MMM-dd
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "2001-JUL-12", dateConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		assertSuccess(validationResult);
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testDateAsStringInFormat3Failure() {
		// Format yyyy-MMM-dd
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "2001-KUA-12", dateConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, validationResult.getStatus());
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testDateAsStringInFormat4Success() {
		// Format dd-MM-yyyy
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "22-12-2001", dateConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		assertSuccess(validationResult);
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testDateAsStringInFormat5Success() {
		// Format dd-MMM-yyyy
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "12-AUG-2001", dateConstraint);
		assertSuccess(validationResult);
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testDateAsStringInFormat5Failure() {
		// Format dd-MMM-yyyy
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "12-KUA-2001", dateConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, validationResult.getStatus());
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	@Test
	public void testDateAsStringInFormat6Failure() {
		// Format yyyymmdd
		ConstraintValidationResult validationResult = process(dictionaryValidationResult, "20010704", dateConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, validationResult.getStatus());
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	protected ConstraintValidationResult process(DictionaryValidationResult result, Object value, DataTypeConstraint constraint) {
		AttributeDefinition definition = new AttributeDefinition();
		definition.setName("testAttribute");
		attributeValueReader = new SingleAttributeValueReader(value, "testEntry", "testAttribute", definition);
		return processor.process(result, value, constraint, attributeValueReader).getFirstConstraintValidationResult();
	}

	protected void assertSuccess(ConstraintValidationResult validationResult) {
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.OK, validationResult.getStatus());
		Assert.assertEquals(new DataTypeConstraintProcessor().getName(), validationResult.getConstraintName());
	}
	
	public class SimpleDataTypeConstraint implements DataTypeConstraint {

		private DataType dataType;
		
		public SimpleDataTypeConstraint(DataType dataType) {
			this.dataType = dataType;
		}
		
		/**
		 * @see org.kuali.rice.krad.datadictionary.validation.constraint.DataTypeConstraint#getDataType()
		 */
		@Override
		public DataType getDataType() {
			return dataType;
		}
		
	}
	
}
