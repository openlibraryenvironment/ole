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
public class InIgnoreCasePredicateTest {

	private static final String STRING_XML =
        """<inIgnoreCase propertyPath="stringValues.path" xmlns="http://rice.kuali.org/core/v2_0">
            <stringValue>abcdefg</stringValue>
            <stringValue>gfedcabc</stringValue>
            <stringValue>should have failed by now!</stringValue>
          </inIgnoreCase>""";

	/**
	 * Test method for {@link InPredicate#InPredicate(java.lang.String, java.util.Set)}.
	 */
	@Test
	public void testInExpression() {
		
		// test failure case, null propertyPath, but a valid list
		try {
			Set<CriteriaStringValue> set = new HashSet<CriteriaStringValue>();
			CriteriaStringValue value = new CriteriaStringValue("value1");
            set.add(value);
			new InIgnoreCasePredicate(null, set);
			fail("IllegalArgumentException should have been thrown.");
		} catch (IllegalArgumentException e) {
			// expected exception
		}
		
		// test a null list
		try {
			new InIgnoreCasePredicate("property.path", null);
			fail("IllegalArgumentException should have been thrown.");
		} catch (IllegalArgumentException e) {
			// expected exception
		}
		
		// test an empty list
		try {
			new InIgnoreCasePredicate("property.path", new HashSet<CriteriaValue<?>>());
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
			new InIgnoreCasePredicate("property.path", valueList);
			fail("IllegalArgumentException should have been thrown.");
		} catch (IllegalArgumentException e) {
			// expected exception
		}
		
		// now create a valid InIgnoreCase expression
        InIgnoreCasePredicate expression = createWithStringCriteria();
		assertNotNull(expression);

	}

	/**
	 * Test method for {@link InPredicate#getPropertyPath()}.
	 */
	@Test
	public void testGetPropertyPath() {
        InIgnoreCasePredicate expression = createWithStringCriteria();
		assertEquals("stringValues.path", expression.getPropertyPath());
	}

	/**
	 * Test method for {@link InPredicate#getValues()}.
	 */
	@Test
	public void testGetValues() {
        InIgnoreCasePredicate expression = createWithStringCriteria();
		assertEquals(3, expression.getValues().size());
		for (CriteriaValue<?> value : expression.getValues()) {
			assertTrue("Expression should be CriteriaStringValue", value instanceof CriteriaStringValue);
		}
	}
	
	/**
	 * Tests serialization to and from XML using JAXB.
	 */
	@Test
	public void testJAXB() {

        InIgnoreCasePredicate expression = createWithStringCriteria();
		JAXBAssert.assertEqualXmlMarshalUnmarshal(expression, STRING_XML, InIgnoreCasePredicate.class);
	}
	
	private static InIgnoreCasePredicate createWithStringCriteria() {
		Set<CriteriaStringValue> valueList = new HashSet<CriteriaStringValue>();
		valueList.add(new CriteriaStringValue("abcdefg"));
		valueList.add(new CriteriaStringValue("gfedcabc"));
		valueList.add(new CriteriaStringValue("should have failed by now!"));
		return new InIgnoreCasePredicate("stringValues.path", valueList);
	}


}
