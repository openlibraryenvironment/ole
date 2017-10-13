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
package org.kuali.rice.core.api.criteria



import org.kuali.rice.core.api.criteria.PredicateFactory as pf

import org.junit.Test
import org.kuali.rice.core.test.JAXBAssert
import static org.kuali.rice.core.api.criteria.PredicateFactory.*

/**
 * A test for the {@link Predicate} class.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class LargePredicateTest {

	private static final String XML = """
	    <and xmlns="http://rice.kuali.org/core/v2_0">
	        <like propertyPath="display" xmlns="http://rice.kuali.org/core/v2_0">
	            <stringValue>*Eric*</stringValue>
	        </like>
	        <greaterThan propertyPath="birthDate">
	            <dateTimeValue>1980-09-01T00:00:00Z</dateTimeValue>
	        </greaterThan>
	        <lessThan propertyPath="birthDate">
	            <dateTimeValue>1980-10-01T00:00:00Z</dateTimeValue>
	        </lessThan>
	        <or>
	            <equal propertyPath="name.first">
	                <stringValue>Eric</stringValue>
	            </equal>
	            <equal propertyPath="name.last">
	                <stringValue>Westfall</stringValue>
	            </equal>
	        </or>
	    </and>""";

	private static final String UBER_XML = """
        <and xmlns="http://rice.kuali.org/core/v2_0">
            <or>
                <equal propertyPath="pp">
                    <stringValue>val</stringValue>
                </equal>
                <like propertyPath="pp">
                    <stringValue>val*</stringValue>
                </like>
                <greaterThan propertyPath="pp">
                    <integerValue>100</integerValue>
                </greaterThan>
                <greaterThanOrEqual propertyPath="pp">
                    <decimalValue>500.5</decimalValue>
                </greaterThanOrEqual>
                <null propertyPath="pp" />
                <and>
                    <in propertyPath="pp">
                        <stringValue>val</stringValue>
                    </in>
                    <notIn propertyPath="pp">
                        <decimalValue>50.1</decimalValue>
                        <decimalValue>42</decimalValue>
                    </notIn>
                </and>
            </or>
            <and>
                <notEqual propertyPath="pp">
                    <dateTimeValue>1970-01-01T00:00:00Z</dateTimeValue>
                </notEqual>
                <lessThan propertyPath="pp">
                    <integerValue>9223372036854775807</integerValue>
                </lessThan>
                <lessThanOrEqual propertyPath="pp">
                    <integerValue>32767</integerValue>
                </lessThanOrEqual>
                <notNull propertyPath="pp" />
            </and>
            <notIn propertyPath="pp">
                <dateTimeValue>1970-01-01T00:00:00Z</dateTimeValue>
            </notIn>
        </and>""";
	
	/**
	 * Creates a Criteria object using the CriteriaBuilder which covers as many possible permutations of
	 * Criteria expressions and values as possible.  Then tests marhsal and unmarshal with JAXB. 
	 */
	@Test
	public void testUberCriteria() throws Exception {
		
		Calendar epochZero = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		epochZero.setTimeInMillis(0);
		// the criteria here is totally wacky and woudl be guaranteed to always return no results, but
		// it allows us to hit as many of the expressions as possible
        Predicate orPredicate = or(
			equal("pp", "val"),
			like("pp", "val*"),
			greaterThan("pp", 100),
			greaterThanOrEqual("pp", 500.5),
			isNull("pp"),
            and(
                pf.in("pp", "val"),
                notIn("pp", new BigDecimal("50.1"), new BigDecimal("42"))));

        Predicate andPredicate = and(
            notEqual("pp", epochZero),
			lessThan("pp", Long.MAX_VALUE),
			lessThanOrEqual("pp", Short.MAX_VALUE),
			isNotNull("pp")
        )

        Predicate outerAndPredicate = and(
            orPredicate, andPredicate, notIn("pp", epochZero)
        )

		JAXBAssert.assertEqualXmlMarshalUnmarshal(outerAndPredicate, UBER_XML, AndPredicate.class);
	}
	
	/**
	 * Tests the serialization of a Criteria object to and from XML using JAXB.
	 */
	@Test
	public void testJAXB() throws Exception {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(create(), XML, AndPredicate.class);
	}
	
	// TODO add a test which tests jaxb with all possible combinations of expressions to make sure they are all getting serialized properly
	
	private static Predicate create() throws Exception {
		
		// try to create this in a way that won't break based on machine locale or system clock
		Calendar gtBirthDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        gtBirthDate.with {
            set(Calendar.MONTH, 8);
            set(Calendar.DAY_OF_MONTH, 1);
            set(Calendar.YEAR, 1980);
            set(Calendar.HOUR_OF_DAY, 0);
            set(Calendar.MINUTE, 0);
            set(Calendar.SECOND, 0);
            set(Calendar.MILLISECOND, 0);
        }
		Calendar ltBirthDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        ltBirthDate.with {
            set(Calendar.MONTH, 9);
            set(Calendar.DAY_OF_MONTH, 1);
            set(Calendar.YEAR, 1980);
            set(Calendar.HOUR_OF_DAY, 0);
            set(Calendar.MINUTE, 0);
            set(Calendar.SECOND, 0);
            set(Calendar.MILLISECOND, 0);
        }
		return and(
            like("display", "*Eric*"),
		    greaterThan("birthDate", gtBirthDate),
		    lessThan("birthDate", ltBirthDate),
            or(
                equal("name.first", "Eric"),
                equal("name.last", "Westfall")
            )
        )
	}

}

