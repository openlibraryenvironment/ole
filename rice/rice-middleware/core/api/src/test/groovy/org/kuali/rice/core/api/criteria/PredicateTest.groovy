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


import org.kuali.rice.core.api.criteria.PredicateFactory as pf

import java.text.SimpleDateFormat
import org.junit.Test
import static org.junit.Assert.*
import static org.kuali.rice.core.api.criteria.PredicateFactory.*

/**
 * Test the {@link PredicateFactory}.  Runs through a few different PredicateFactory scenarios.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PredicateTest {

	@Test
	public void testBuild() throws Exception {
		
		Date gtBirthDate = new SimpleDateFormat("yyyyMMdd").parse("19800901");
		Date ltBirthDate = new SimpleDateFormat("yyyyMMdd").parse("19801001");
		
		def pred = and(
            like("display", "*Eric*"),
            greaterThan("birthDate", gtBirthDate),
            lessThan("birthDate", ltBirthDate),
            or(
                equal("name.first", "Eric"),
		        equal("name.last", "Westfall"),
            )
        )

		assertEquals("Criteria should have 4 expressions", 4, pred.getPredicates().size());
		
		LikePredicate foundLike = null;
		GreaterThanPredicate foundGt = null;
		LessThanPredicate foundLt = null;
		OrPredicate foundOr = null;
		for (Predicate expression : pred.getPredicates()) {
			if (expression instanceof LikePredicate) {
				foundLike = (LikePredicate)expression;
			} else if (expression instanceof GreaterThanPredicate) {
				foundGt = (GreaterThanPredicate)expression;
			} else if (expression instanceof LessThanPredicate) {
				foundLt = (LessThanPredicate)expression;
			} else if (expression instanceof OrPredicate) {
				foundOr = (OrPredicate)expression;
			} else {
				fail("Found an expression which should not have been found: " + expression);
			}
		}
		assertNotNull("Should have found a LikePredicate", foundLike);
		assertNotNull("Should have found a GreaterThanPredicate", foundGt);
		assertNotNull("Should have found a LessThanPredicate", foundLt);
		assertNotNull("Should have found an OrPredicate", foundOr);
		
		assertEquals("display", foundLike.getPropertyPath());
		assertEquals("*Eric*", foundLike.getValue().getValue());
		
		assertEquals("birthDate", foundGt.getPropertyPath());
		assertTrue(foundGt.getValue() instanceof CriteriaDateTimeValue);
		assertEquals(new CriteriaDateTimeValue(gtBirthDate), foundGt.getValue());
		
		assertEquals("birthDate", foundLt.getPropertyPath());
		assertTrue(foundLt.getValue() instanceof CriteriaDateTimeValue);
		assertEquals(new CriteriaDateTimeValue(ltBirthDate), foundLt.getValue());
		
		assertEquals("OrPredicate should have 2 expressions", 2, foundOr.getPredicates().size());

		EqualPredicate nameFirstPredicate = (EqualPredicate)foundOr.getPredicates().asList().get(1);
		EqualPredicate nameLastPredicate = (EqualPredicate)foundOr.getPredicates().asList().get(0);

		assertEquals("name.first", nameFirstPredicate.getPropertyPath());
		assertEquals("Eric", nameFirstPredicate.getValue().getValue());
		assertEquals("name.last", nameLastPredicate.getPropertyPath());
		assertEquals("Westfall", nameLastPredicate.getValue().getValue());

	}


	@Test(expected=IllegalArgumentException.class)
	public void testEqual_nullPropertyPath() {
		equal(null, "value");
	}


	@Test(expected=IllegalArgumentException.class)
	public void testEqual_nullValue() {
		equal("propertyPath", null);
	}

	@Test
	public void testEqual() {
		and(
		    equal("propertyPath", "propertyValue"),
		    equal("propertyPath", 100),
		    equal("propertyPath", 50.5),
		    equal("propertyPath", BigDecimal.ONE),
		    equal("propertyPath", new Date()),
		    equal("propertyPath", Calendar.getInstance()),
        )
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNotEqual_nullPropertyPath() {
		notEqual(null, "value");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNotEqual_nullValue() {
		notEqual("propertyPath", null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testLike_nullPropertyPath() {
		like(null, "value");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testLike_nullValue() {
		like("propertyPath", null);
	}

    @Test
	public void testLike() {
		like("pp", "val*");
	}

    @Test(expected=IllegalArgumentException.class)
	public void testNotLike_nullPropertyPath() {
		notLike(null, "value");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNotLike_nullValue() {
		notLike("propertyPath", null);
	}

    @Test
	public void testNotLike() {
		notLike("pp", "val*");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIn_nullPropertyPath() {
		pf.in(null, "value");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIn_nullValues() {
		pf.in("propertyPath", null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIn_emptyValues() {
		pf.in("propertyPath");
	}
	
	@Test
	public void testIn() {
		pf.in("pp", "value1", "value2", "value3");
		pf.in("pp", 1, 2, 3);
		pf.in("pp", 1.0, 2.6);
		pf.in("pp", new Date(), new Date());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNotIn_nullPropertyPath() {
		notIn(null, "value");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNotIn_nullValues() {
		notIn("propertyPath", null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNotIn_emptyValues() {
		notIn("propertyPath");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGreaterThan_nullPropertyPath() {
		greaterThan(null, 100);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGreaterThan_nullValue() {
		greaterThan("propertyPath", null);
	}
	
	@Test
	public void testGreaterThan() {
		greaterThan("pp", 100);
		greaterThan("pp", 50.7654354);
		greaterThan("pp", Calendar.getInstance());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGreaterThanOrEqual_nullPropertyPath() {
		greaterThanOrEqual(null, 100);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGreaterThanOrEqual_nullValue() {
		greaterThanOrEqual("propertyPath", null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testLessThan_nullPropertyPath() {
		lessThan(null, 100);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testLessThan_nullValue() {
		lessThan("propertyPath", null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testLessThanOrEqual_nullPropertyPath() {
		lessThanOrEqual(null, 100);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testLessThanOrEqual_nullValue() {
		lessThanOrEqual("propertyPath", null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testIsNull_nullPropertyPath() {
		isNull(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIsNull_emptyPropertyPath() {
		isNull("");
	}
	
	public void testIsNull() {
		isNull("any");
		isNull("non-empty");
		isNull("value");
		isNull("will do");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIsNotNull_nullPropertyPath() {
		isNotNull(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIsNotNull_emptyPropertyPath() {
		isNotNull("");
	}
	
	public void testIsNotNull() {
		isNotNull("any");
		isNotNull("non-empty");
		isNotNull("value");
		isNotNull("will do");
	}
	
	@Test
	public void testAnd() {
		assertNotNull(and());
	}
	
	@Test
	public void testOr() {
		assertNotNull(or());
	}
	
	@Test
	public void testDeepNesting() {
		and(or(and(or(and(and(and(equal("whatWasThat?", "That was crazy!"))))))))
	}
}

