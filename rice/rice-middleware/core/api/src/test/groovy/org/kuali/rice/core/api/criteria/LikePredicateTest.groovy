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
import static org.junit.Assert.assertEquals
import static org.junit.Assert.fail

/**
 * A test for the {@link LikePredicate} class.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LikePredicateTest {

	private static final String STRING_XML = "<like propertyPath=\"property.path\" xmlns=\"http://rice.kuali.org/core/v2_0\"><stringValue>value*</stringValue></like>";	
	
	/**
	 * Test method for {@link LikePredicate#LikePredicate(java.lang.String, org.kuali.rice.core.api.criteria.CriteriaValue)}.
	 * 
	 * <p>LikeExpression only supports CriteriaStringValue
	 */
	@Test
	public void testLikeExpression() {
		
		// Test that it can take a CriteriaStringValue
		LikePredicate likeExpression = new LikePredicate("property.path", new CriteriaStringValue("value*"));
		assertEquals("property.path", likeExpression.getPropertyPath());
		assertEquals("value*", likeExpression.getValue().getValue());
		
		// Doesn't support decimal, integer, or dateTime criteria values
		try {
			new LikePredicate("property.path", new CriteriaDecimalValue(BigDecimal.ZERO));
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected exception
		}
		try {
			new LikePredicate("property.path", new CriteriaIntegerValue(BigInteger.ZERO));
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected exception
		}
		try {
			new LikePredicate("property.path", new CriteriaDateTimeValue(Calendar.getInstance()));
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected exception
		}
		
		// test failure cases, should throw IllegalArgumentException when null is passed
		try {
			new LikePredicate(null, null);
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected exception
		}
		
	}
	
	/**
	 * Tests that the LikeExpression can be marshalled and unmarshalled properly via JAXB.
	 */
	@Test
	public void testJAXB() {
		LikePredicate likeExpression = new LikePredicate("property.path", new CriteriaStringValue("value*"));
		JAXBAssert.assertEqualXmlMarshalUnmarshal(likeExpression, STRING_XML, LikePredicate.class);
	}

}
