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


import org.joda.time.DateTime
import org.junit.Test
import org.kuali.rice.core.test.JAXBAssert

import static org.junit.Assert.assertEquals
import static org.junit.Assert.fail

/**
 * A test for the {@link GreaterThanPredicate} class.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LessThanPredicateTest {

	private static final String DECIMAL_XML = "<lessThan propertyPath=\"property.path\" xmlns=\"http://rice.kuali.org/core/v2_0\"><decimalValue>0</decimalValue></lessThan>";
	private static final String INTEGER_XML = "<lessThan propertyPath=\"property.path\" xmlns=\"http://rice.kuali.org/core/v2_0\"><integerValue>0</integerValue></lessThan>";
	private static final String DATE_TIME_XML = "<lessThan propertyPath=\"property.path\" xmlns=\"http://rice.kuali.org/core/v2_0\"><dateTimeValue>2011-01-15T05:30:15.500Z</dateTimeValue></lessThan>";
	
	/**
	 * Test method for {@link GreaterThanPredicate#GreaterThanPredicate(java.lang.String, org.kuali.rice.core.api.criteria.CriteriaValue)}.
	 * 
	 * <p>GreaterThanExpression should support all of the different CriteriaValues except for {@link CriteriaStringValue}
	 */
	@Test
	public void testLessThanExpression() {
				
		// Test that it can take a CriteriaDecimalValue
        LessThanPredicate lessThanExpression = new LessThanPredicate("property.path", new CriteriaDecimalValue(BigDecimal.ZERO));
		assertEquals("property.path", lessThanExpression.getPropertyPath());
		assertEquals(BigDecimal.ZERO, lessThanExpression.getValue().getValue());
		
		// Test that it can take a CriteriaIntegerValue
		lessThanExpression = new LessThanPredicate("property.path", new CriteriaIntegerValue(BigInteger.ZERO));
		assertEquals("property.path", lessThanExpression.getPropertyPath());
		assertEquals(BigInteger.ZERO, lessThanExpression.getValue().getValue());
		
		// Test that it can take a CriteriaDateTimeValue
		DateTime dateTime = new DateTime();
		lessThanExpression = new LessThanPredicate("property.path", new CriteriaDateTimeValue(dateTime));
		assertEquals("property.path", lessThanExpression.getPropertyPath());
		assertEquals(dateTime, lessThanExpression.getValue().getValue());


        // Test that it can take a CriteriaStringValue
		lessThanExpression = new LessThanPredicate("property.path", new CriteriaStringValue("test"));
		assertEquals("property.path", lessThanExpression.getPropertyPath());
		assertEquals("test", lessThanExpression.getValue().getValue());

		
		// test failure cases, should throw IllegalArgumentException when null is passed
		try {
			new LessThanPredicate(null, null);
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected exception
		}
	}
	
	/**
	 * Tests that the LessThanExpression can be marshalled and unmarshalled properly via JAXB.
	 */
	@Test
	public void testJAXB() {
		
		LessThanPredicate lessThanExpression = new LessThanPredicate("property.path", new CriteriaDecimalValue(BigDecimal.ZERO));
		JAXBAssert.assertEqualXmlMarshalUnmarshal(lessThanExpression, DECIMAL_XML, LessThanPredicate.class);

        lessThanExpression = new LessThanPredicate("property.path", new CriteriaIntegerValue(BigInteger.ZERO));
		JAXBAssert.assertEqualXmlMarshalUnmarshal(lessThanExpression, INTEGER_XML, LessThanPredicate.class);
		
		// set the date and time to January 15, 2100 at 5:30:15.500 am in the GMT timezone
		Calendar dateTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		dateTime.set(Calendar.HOUR_OF_DAY, 5);
		dateTime.set(Calendar.MINUTE, 30);
		dateTime.set(Calendar.SECOND, 15);
		dateTime.set(Calendar.MILLISECOND, 500);
		dateTime.set(Calendar.MONTH, 0);
		dateTime.set(Calendar.DATE, 15);
		dateTime.set(Calendar.YEAR, 2011);

        lessThanExpression = new LessThanPredicate("property.path", new CriteriaDateTimeValue(dateTime));
		JAXBAssert.assertEqualXmlMarshalUnmarshal(lessThanExpression, DATE_TIME_XML, LessThanPredicate.class);
	}

}
