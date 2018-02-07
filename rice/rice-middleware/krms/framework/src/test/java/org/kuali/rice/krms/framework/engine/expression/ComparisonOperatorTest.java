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
package org.kuali.rice.krms.framework.engine.expression;

import java.math.BigInteger;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

public class ComparisonOperatorTest {
    @Test
    public void testEquals() {
        ComparisonOperator op = ComparisonOperator.fromCode(ComparisonOperator.EQUALS.toString());
        op.setComparisonOperatorService(ComparisonOperatorServiceImpl.getInstance());
        assertTrue(op.compare("StringOne", "StringOne"));
        assertTrue(op.compare(123, "123"));
        assertTrue(op.compare(BigInteger.TEN, "10"));
//        assertFalse(op.compare(11, "elf"));  // throws:
// org.kuali.rice.krms.api.engine.IncompatibleTypeException: Could not coerce String to Integer = -> Type should have been one of [java.lang.Integer] but was java.lang.String
//        at org.kuali.rice.krms.framework.engine.expression.ComparisonOperator.coerceRhsHelper
//        at org.kuali.rice.krms.framework.engine.expression.ComparisonOperator.coerceRhs
//        at org.kuali.rice.krms.framework.engine.expression.ComparisonOperator.compare
    }

    @Test
    public void testNotEquals() {
        ComparisonOperator op = ComparisonOperator.fromCode(ComparisonOperator.NOT_EQUALS.toString());
        op.setComparisonOperatorService(ComparisonOperatorServiceImpl.getInstance());
        assertTrue(op.compare("StringOne", "StringTwo"));
        assertTrue(op.compare(122, "123"));
        assertTrue(op.compare(BigInteger.TEN, "11"));
        assertTrue(op.compare(null, "124"));
        assertFalse(op.compare(null, null));
    }

    @Test
    public void testLess() {
        ComparisonOperator op = ComparisonOperator.fromCode(ComparisonOperator.LESS_THAN.toString());
        op.setComparisonOperatorService(ComparisonOperatorServiceImpl.getInstance());
        assertTrue(op.compare(123, "124"));
        assertTrue(op.compare(new Double(123.2), "124"));
        assertTrue(op.compare(null, "124"));
        assertFalse(op.compare(124, null));
        assertFalse(op.compare(null, null));
        assertFalse(op.compare(123, "123"));
        assertFalse(op.compare(new Integer(123), "122"));
        assertFalse(op.compare(122.1, "122"));
    }

    @Test
    public void testLessThanEqual() {
        ComparisonOperator op = ComparisonOperator.fromCode(ComparisonOperator.LESS_THAN_EQUAL.toString());
        op.setComparisonOperatorService(ComparisonOperatorServiceImpl.getInstance());
        assertTrue(op.compare(123, "124"));
        assertTrue(op.compare(123.1, "123.1"));
        assertTrue(op.compare(null, null));
        assertFalse(op.compare(new Double(123.1), "123.01"));
        assertFalse(op.compare(123, "122"));
    }

    @Test
    public void testGreaterThanEqual() {
        ComparisonOperator op = ComparisonOperator.fromCode(ComparisonOperator.GREATER_THAN_EQUAL.toString());
        op.setComparisonOperatorService(ComparisonOperatorServiceImpl.getInstance());
        assertFalse(op.compare(123, "124"));
        assertTrue(op.compare(123.1, "123.1"));
        assertFalse(op.compare(null, "124"));
        assertTrue(op.compare(124, null));
        assertTrue(op.compare(null, null));
        assertTrue(op.compare(new Double(123.1), "123.01"));
        assertTrue(op.compare(123, "122"));
    }


    @Test
    public void testExists() {
        ComparisonOperator op = ComparisonOperator.fromCode(ComparisonOperator.EXISTS.toString());
        op.setComparisonOperatorService(ComparisonOperatorServiceImpl.getInstance());
        assertTrue(op.compare("123", "0"));
        assertFalse(op.compare("123", null));
    }

    @Test
    public void testDoesNotExists() {
        ComparisonOperator op = ComparisonOperator.fromCode(ComparisonOperator.DOES_NOT_EXIST.toString());
        op.setComparisonOperatorService(ComparisonOperatorServiceImpl.getInstance());
        assertFalse(op.compare("123", "0"));
        assertTrue(op.compare("123", null));
    }
}
