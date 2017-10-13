/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.core.api.criteria;


import org.junit.Test
import org.kuali.rice.core.test.JAXBAssert
import static org.junit.Assert.*

/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class NotInPredicateTest {

	private static final String STRING_XML = "<notIn propertyPath=\"stringValues.path\" xmlns=\"http://rice.kuali.org/core/v2_0\"><stringValue>abcdefg</stringValue><stringValue>gfedcabc</stringValue><stringValue>should have failed by now!</stringValue></notIn>";
	private static final String DECIMAL_XML = "<notIn propertyPath=\"decimalValues.path\" xmlns=\"http://rice.kuali.org/core/v2_0\"><decimalValue>1.0</decimalValue><decimalValue>1.1</decimalValue><decimalValue>2.5</decimalValue></notIn>";
	private static final String INTEGER_XML = "<notIn propertyPath=\"integerValues.path\" xmlns=\"http://rice.kuali.org/core/v2_0\"><integerValue>1</integerValue><integerValue>2</integerValue><integerValue>3</integerValue><integerValue>10</integerValue><integerValue>4</integerValue></notIn>";
	private static final String DATE_TIME_XML = "<notIn propertyPath=\"dateTimeValues.path\" xmlns=\"http://rice.kuali.org/core/v2_0\"><dateTimeValue>2011-01-15T05:30:15.500Z</dateTimeValue></notIn>";
	
	/**
	 * Test method for {@link NotInPredicate#NotInPredicate(java.lang.String, java.util.Set)}.
	 */
	@Test
	public void testInExpression() {
		
		// test failure case, null propertyPath, but a valid list
		try {
            Set<CriteriaStringValue> set = new HashSet<CriteriaStringValue>();
			CriteriaStringValue value = new CriteriaStringValue("value1");
            set.add(value);
			new NotInPredicate(null, set);
			fail("IllegalArgumentException should have been thrown.");
		} catch (IllegalArgumentException e) {
			// expected exception
		}
		
		// test a null list
		try {
			new NotInPredicate("property.path", null);
			fail("IllegalArgumentException should have been thrown.");
		} catch (IllegalArgumentException e) {
			// expected exception
		}
		
		// test an empty list
		try {
            new NotInPredicate("property.path", new HashSet<CriteriaValue<?>>());
			fail("IllegalArgumentException should have been thrown.");
		} catch (IllegalArgumentException e) {
			// expected exception
		}
		
		// test a list with different CriteriaValue types in it
		try {
			Set<CriteriaValue<?>> valueList = new HashSet<CriteriaValue<?>>();
			valueList.add(new CriteriaStringValue("abcdefg"));
			valueList.add(new CriteriaStringValue("gfedcabc"));
			valueList.add(new CriteriaIntegerValue(100));
			valueList.add(new CriteriaStringValue("should have failed by now!"));
			new NotInPredicate("property.path", valueList);
			fail("IllegalArgumentException should have been thrown.");
		} catch (IllegalArgumentException e) {
			// expected exception
		}
		
		// now create a valid NotInExpression
		NotInPredicate expression = createWithStringCriteria();
		assertNotNull(expression);
		expression = createWithDecimalCriteria();
		assertNotNull(expression);
		expression = createWithIntegerCriteria();
		assertNotNull(expression);
		expression = createWithDateTimeCriteria();
		assertNotNull(expression);
		
	}

	/**
	 * Test method for {@link NotInPredicate#getPropertyPath()}.
	 */
	@Test
	public void testGetPropertyPath() {
		NotInPredicate expression = createWithStringCriteria();
		assertEquals("stringValues.path", expression.getPropertyPath());
		expression = createWithDecimalCriteria();
		assertEquals("decimalValues.path", expression.getPropertyPath());
		expression = createWithIntegerCriteria();
		assertEquals("integerValues.path", expression.getPropertyPath());
		expression = createWithDateTimeCriteria();
		assertEquals("dateTimeValues.path", expression.getPropertyPath());
	}

	/**
	 * Test method for {@link NotInPredicate#getValues()}.
	 */
	@Test
	public void testGetValues() {
		NotInPredicate expression = createWithStringCriteria();
		assertEquals(3, expression.getValues().size());
		for (CriteriaValue<?> value : expression.getValues()) {
			assertTrue("Expression should be CriteriaStringValue", value instanceof CriteriaStringValue);
		}
		
		expression = createWithDecimalCriteria();
		assertEquals(3, expression.getValues().size());
		for (CriteriaValue<?> value : expression.getValues()) {
			assertTrue("Expression should be CriteriaDecimalValue", value instanceof CriteriaDecimalValue);
		}
		
		expression = createWithIntegerCriteria();
		assertEquals(5, expression.getValues().size());
		for (CriteriaValue<?> value : expression.getValues()) {
			assertTrue("Expression should be CriteriaIntegerValue", value instanceof CriteriaIntegerValue);
		}
		
		expression = createWithDateTimeCriteria();
		assertEquals(1, expression.getValues().size());
		for (CriteriaValue<?> value : expression.getValues()) {
			assertTrue("Expression should be CriteriaDateValue", value instanceof CriteriaDateTimeValue);
		}
	}
	
	/**
	 * Tests serialization to and from XML using JAXB.
	 */
	@Test
	public void testJAXB() {
		
		NotInPredicate expression = createWithStringCriteria();
		JAXBAssert.assertEqualXmlMarshalUnmarshal(expression, STRING_XML, NotInPredicate.class);
		
		expression = createWithDecimalCriteria();
		JAXBAssert.assertEqualXmlMarshalUnmarshal(expression, DECIMAL_XML, NotInPredicate.class);
		
		expression = createWithIntegerCriteria();
		JAXBAssert.assertEqualXmlMarshalUnmarshal(expression, INTEGER_XML, NotInPredicate.class);
		
		expression = createWithDateTimeCriteria();
		JAXBAssert.assertEqualXmlMarshalUnmarshal(expression, DATE_TIME_XML, NotInPredicate.class);
	}
	
	private static NotInPredicate createWithStringCriteria() {
		Set<CriteriaStringValue> valueList = new HashSet<CriteriaStringValue>();
		valueList.add(new CriteriaStringValue("abcdefg"));
		valueList.add(new CriteriaStringValue("gfedcabc"));
		valueList.add(new CriteriaStringValue("should have failed by now!"));
		return new NotInPredicate("stringValues.path", valueList);
	}
	
	private static NotInPredicate createWithDecimalCriteria() {
		Set<CriteriaDecimalValue> valueList = new HashSet<CriteriaDecimalValue>();
		valueList.add(new CriteriaDecimalValue(1.0));
		valueList.add(new CriteriaDecimalValue(1.1));
		valueList.add(new CriteriaDecimalValue(2.5));
		return new NotInPredicate("decimalValues.path", valueList);
	}
	
	private static NotInPredicate createWithIntegerCriteria() {
		Set<CriteriaIntegerValue> valueList = new HashSet<CriteriaIntegerValue>();
		valueList.add(new CriteriaIntegerValue(1));
		valueList.add(new CriteriaIntegerValue(2));
		valueList.add(new CriteriaIntegerValue(3));
		valueList.add(new CriteriaIntegerValue(10));
		valueList.add(new CriteriaIntegerValue(4));
		return new NotInPredicate("integerValues.path", valueList);
	}
	
	private static NotInPredicate createWithDateTimeCriteria() {
		// set the date and time to January 15, 2100 at 5:30:15.500 am in the GMT timezone
		Calendar dateTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		dateTime.set(Calendar.HOUR_OF_DAY, 5);
		dateTime.set(Calendar.MINUTE, 30);
		dateTime.set(Calendar.SECOND, 15);
		dateTime.set(Calendar.MILLISECOND, 500);
		dateTime.set(Calendar.MONTH, 0);
		dateTime.set(Calendar.DATE, 15);
		dateTime.set(Calendar.YEAR, 2011);
		Set<CriteriaDateTimeValue> valueList = new HashSet<CriteriaDateTimeValue>();
		valueList.add(new CriteriaDateTimeValue(dateTime));
		return new NotInPredicate("dateTimeValues.path", valueList);
	}

}
