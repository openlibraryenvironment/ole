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


import static org.junit.Assert.assertEquals
import static org.junit.Assert.fail

import org.joda.time.DateTime
import org.junit.Test
import org.kuali.rice.core.test.JAXBAssert

/**
 * A test for the {@link EqualPredicate} class.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EqualPredicateTest {

	private static final String STRING_XML = "<equal propertyPath=\"property.path\" xmlns=\"http://rice.kuali.org/core/v2_0\"><stringValue>value</stringValue></equal>";
	private static final String DECIMAL_XML = "<equal propertyPath=\"property.path\" xmlns=\"http://rice.kuali.org/core/v2_0\"><decimalValue>0</decimalValue></equal>";
	private static final String INTEGER_XML = "<equal propertyPath=\"property.path\" xmlns=\"http://rice.kuali.org/core/v2_0\"><integerValue>0</integerValue></equal>";
	private static final String DATE_TIME_XML = "<equal propertyPath=\"property.path\" xmlns=\"http://rice.kuali.org/core/v2_0\"><dateTimeValue>2011-01-15T05:30:15.500Z</dateTimeValue></equal>";
	
	
	/**
	 * Test method for {@link EqualPredicate#EqualPredicate(java.lang.String, org.kuali.rice.core.api.criteria.CriteriaValue)}.
	 * 
	 * <p>EqualExpression should support all four of the different CriteriaValues
	 */
	@Test
	public void testEqualExpression() {
		
		// Test that it can take a CriteriaStringValue
		EqualPredicate equalExpression = new EqualPredicate("property.path", new CriteriaStringValue("value"));
		assertEquals("property.path", equalExpression.getPropertyPath());
		assertEquals("value", equalExpression.getValue().getValue());
		
		// Test that it can take a CriteriaDecimalValue
		equalExpression = new EqualPredicate("property.path", new CriteriaDecimalValue(BigDecimal.ZERO));
		assertEquals("property.path", equalExpression.getPropertyPath());
		assertEquals(BigDecimal.ZERO, equalExpression.getValue().getValue());
		
		// Test that it can take a CriteriaIntegerValue
		equalExpression = new EqualPredicate("property.path", new CriteriaIntegerValue(BigInteger.ZERO));
		assertEquals("property.path", equalExpression.getPropertyPath());
		assertEquals(BigInteger.ZERO, equalExpression.getValue().getValue());
		
		// Test that it can take a CriteriaDateTimeValue
		DateTime dateTime = new DateTime();
		equalExpression = new EqualPredicate("property.path", new CriteriaDateTimeValue(dateTime));
		assertEquals("property.path", equalExpression.getPropertyPath());
		assertEquals(dateTime, equalExpression.getValue().getValue());
		
		// test failure cases, should throw IllegalArgumentException when null is passed
		try {
			new EqualPredicate(null, null);
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected exception
		}
	}
	
	/**
	 * Tests that the EqualExpression can be marshalled and unmarshalled properly via JAXB.
	 */
	@Test
	public void testJAXB() {
		EqualPredicate equalExpression = new EqualPredicate("property.path", new CriteriaStringValue("value"));
		JAXBAssert.assertEqualXmlMarshalUnmarshal(equalExpression, STRING_XML, EqualPredicate.class);
		
		equalExpression = new EqualPredicate("property.path", new CriteriaDecimalValue(BigDecimal.ZERO));
		JAXBAssert.assertEqualXmlMarshalUnmarshal(equalExpression, DECIMAL_XML, EqualPredicate.class);
		
		equalExpression = new EqualPredicate("property.path", new CriteriaIntegerValue(BigInteger.ZERO));
		JAXBAssert.assertEqualXmlMarshalUnmarshal(equalExpression, INTEGER_XML, EqualPredicate.class);
		
		// set the date and time to January 15, 2100 at 5:30:15.500 am in the GMT timezone
		Calendar dateTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		dateTime.set(Calendar.HOUR_OF_DAY, 5);
		dateTime.set(Calendar.MINUTE, 30);
		dateTime.set(Calendar.SECOND, 15);
		dateTime.set(Calendar.MILLISECOND, 500);
		dateTime.set(Calendar.MONTH, 0);
		dateTime.set(Calendar.DATE, 15);
		dateTime.set(Calendar.YEAR, 2011);
		
		equalExpression = new EqualPredicate("property.path", new CriteriaDateTimeValue(dateTime));
		JAXBAssert.assertEqualXmlMarshalUnmarshal(equalExpression, DATE_TIME_XML, EqualPredicate.class);
	}

}
