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
 * Tests the {@link AndPredicate} class.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AndPredicateTest {

	private static final String XML = "<and xmlns=\"http://rice.kuali.org/core/v2_0\"><equal propertyPath=\"property.path\"><stringValue>abcdefg</stringValue></equal><greaterThan propertyPath=\"property.path2\"><decimalValue>100</decimalValue></greaterThan><or><greaterThan propertyPath=\"property.path3\"><integerValue>10000</integerValue></greaterThan><like propertyPath=\"property.path4\"><stringValue>wildcard*</stringValue></like></or></and>"; 
		
	/**
	 * Test method for {@link AndPredicate#AndPredicate(java.util.Set)}.
	 */
	@Test
	public void testAndExpression() {
		AndPredicate expression = create();
		assertNotNull(expression);
		
		// should default to an empty list
		expression = new AndPredicate(null);
		assertNotNull(expression.getPredicates());
		assertTrue(expression.getPredicates().isEmpty());
	}

	/**
	 * Test method for {@link AbstractCompositePredicate#getPredicates()}.
	 */
	@Test
	public void testGetExpressions() {
		AndPredicate andExpression = create();
		assertEquals("And expression should have 3 expressions", 3, andExpression.getPredicates().size());
		// one should be an OrExpression with 2 expressions
		for (Predicate expression : andExpression.getPredicates()) {
			if (expression instanceof OrPredicate) {
				assertEquals("Or expression should have 2 expressions", 2, ((OrPredicate)expression).getPredicates().size());
			}
		}
	}
	
	/**
	 * Tests that the AndExpression can be marshaled and unmarshaled properly via JAXB.
	 */
	@Test
	public void testJAXB() {
		AndPredicate andExpression = create();
		JAXBAssert.assertEqualXmlMarshalUnmarshal(andExpression, XML, AndPredicate.class);
	}
	
	private AndPredicate create() {
		Set<Predicate> andExpressions = new HashSet<Predicate>();
		andExpressions.add(new EqualPredicate("property.path", new CriteriaStringValue("abcdefg")));
		andExpressions.add(new GreaterThanPredicate("property.path2", new CriteriaDecimalValue(new BigDecimal(100))));
		
		Set<Predicate> orExpressions = new HashSet<Predicate>();
		orExpressions.add(new GreaterThanPredicate("property.path3", new CriteriaIntegerValue(BigInteger.valueOf(10000))));
		orExpressions.add(new LikePredicate("property.path4", new CriteriaStringValue("wildcard*")));
		
		OrPredicate orExpression = new OrPredicate(orExpressions);
		andExpressions.add(orExpression);
		
		return new AndPredicate(andExpressions);
	}


}
