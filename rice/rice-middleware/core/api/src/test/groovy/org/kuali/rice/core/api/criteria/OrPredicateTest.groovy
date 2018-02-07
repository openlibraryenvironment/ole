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
 * Tests the {@link OrPredicate} class.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class OrPredicateTest {

	private static final String XML = "<or xmlns=\"http://rice.kuali.org/core/v2_0\"><equal propertyPath=\"property.path\"><stringValue>abcdefg</stringValue></equal><greaterThan propertyPath=\"property.path2\"><decimalValue>100</decimalValue></greaterThan><and><greaterThan propertyPath=\"property.path3\"><integerValue>10000</integerValue></greaterThan><like propertyPath=\"property.path4\"><stringValue>wildcard*</stringValue></like></and></or>"; 
		
	/**
	 * Test method for {@link OrPredicate#OrPredicate(java.util.Set)}.
	 */
	@Test
	public void testOrExpression() {
		OrPredicate expression = create();
		assertNotNull(expression);
		
		// should default to an empty list
		expression = new OrPredicate(null);
		assertNotNull(expression.getPredicates());
		assertTrue(expression.getPredicates().isEmpty());
	}

	/**
	 * Test method for {@link AbstractCompositePredicate#getPredicates()}.
	 */
	@Test
	public void testGetExpressions() {
		OrPredicate orExpression = create();
		assertEquals("Or expression should have 3 expressions", 3, orExpression.getPredicates().size());
		// one should be an OrExpression with 2 expressions
		for (Predicate expression : orExpression.getPredicates()) {
			if (expression instanceof AndPredicate) {
				assertEquals("And expression should have 2 expressions", 2, ((AndPredicate)expression).getPredicates().size());
			}
		}
	}
	
	/**
	 * Tests that the AndExpression can be marshaled and unmarshaled properly via JAXB.
	 */
	@Test
	public void testJAXB() {
		OrPredicate orExpression = create();
		JAXBAssert.assertEqualXmlMarshalUnmarshal(orExpression, XML, OrPredicate.class);
	}
	
	private OrPredicate create() {
		Set<Predicate> orExpressions = new HashSet<Predicate>();
		orExpressions.add(new EqualPredicate("property.path", new CriteriaStringValue("abcdefg")));
		orExpressions.add(new GreaterThanPredicate("property.path2", new CriteriaDecimalValue(new BigDecimal(100))));
		
		Set<Predicate> andExpressions = new HashSet<Predicate>();
		andExpressions.add(new GreaterThanPredicate("property.path3", new CriteriaIntegerValue(BigInteger.valueOf(10000))));
		andExpressions.add(new LikePredicate("property.path4", new CriteriaStringValue("wildcard*")));
		
		AndPredicate andExpression = new AndPredicate(andExpressions);
		orExpressions.add(andExpression);
		
		return new OrPredicate(orExpressions);
	}


}
