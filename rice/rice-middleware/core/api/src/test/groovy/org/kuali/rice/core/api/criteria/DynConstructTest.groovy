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

import org.junit.Test
import static org.kuali.rice.core.api.criteria.PredicateFactory.*;
import static org.junit.Assert.*
import org.junit.Ignore;

/** no asserts here.  just confirming that the dynamic method calls work w/o error. */
class DynConstructTest {

    @Test
    void testAnd() {
        dynConstruct("and", [[equal("foo", "f"), notEqual("bar", "b")] as Predicate[]] as Object[])
        dynConstruct(AndPredicate.class.getSimpleName(), [[equal("foo", "f"), notEqual("bar", "b")] as Predicate[]] as Object[])
    }

    @Test
    void testEqualIgnoreCase() {
        dynConstruct("equalIgnoreCase", "foo", "f")
        dynConstruct(EqualIgnoreCasePredicate.class.getSimpleName(), "foo", "f")
    }

    @Test
    void testEqual() {
        dynConstruct("equal", "foo", "f")
        dynConstruct(EqualPredicate.class.getSimpleName(), "foo", "f")
    }

    @Test
    void testGreaterThanOrEqual() {
        dynConstruct("greaterThanOrEqual", "foo", 1)
        dynConstruct(GreaterThanOrEqualPredicate.class.getSimpleName(), "foo", 1)
    }

    @Test
    void testGreaterThan() {
        dynConstruct("greaterThan", "foo", 1)
        dynConstruct(GreaterThanPredicate.class.getSimpleName(), "foo", 1)
    }

    @Test
    void testInIgnoreCase() {
        dynConstruct("inIgnoreCase", "foo", ["1", "2", "3"] as String[])
        dynConstruct(InIgnoreCasePredicate.class.getSimpleName(), "foo", ["1", "2", "3"] as String[])
    }

    @Test
    void testIn() {
        dynConstruct("in", "foo", [1, 2, 3] as Object[])
        dynConstruct(InPredicate.class.getSimpleName(), "foo", [1, 2, 3] as Object[])
    }

    @Test
    void testLessThanOrEqual() {
        dynConstruct("lessThanOrEqual", "foo", 1)
        dynConstruct(LessThanOrEqualPredicate.class.getSimpleName(), "foo", 1)
    }

    @Test
    void testLessThan() {
        dynConstruct("lessThan", "foo", 1)
        dynConstruct(LessThanPredicate.class.getSimpleName(), "foo", 1)
    }

    @Test
    void testLike() {
        dynConstruct("like", "foo", "*bar")
        dynConstruct(LikePredicate.class.getSimpleName(), "foo", "*bar")
    }

    @Test
    void testNotEqualIgnoreCase() {
        dynConstruct("notEqualIgnoreCase", "foo", "bar")
        dynConstruct(NotEqualIgnoreCasePredicate.class.getSimpleName(), "foo", "bar")
    }

    @Test
    void testNotEqual() {
        dynConstruct("notEqual", "foo", "bar")
        dynConstruct(NotEqualPredicate.class.getSimpleName(), "foo", "bar")
    }

    @Test
    void testNotInIgnoreCase() {
        dynConstruct("notInIgnoreCase", "foo", ["1", "2", "3"] as String[])
        dynConstruct(NotInIgnoreCasePredicate.class.getSimpleName(), "foo", ["1", "2", "3"] as String[])
    }

    @Test
    void testNotIn() {
        dynConstruct("notIn", "foo", [1, 2, 3] as Object[])
        dynConstruct(NotInPredicate.class.getSimpleName(), "foo", [1, 2, 3] as Object[])
    }

    @Test
    void testNotLike() {
        dynConstruct("notLike", "foo", "*bar")
        dynConstruct(NotLikePredicate.class.getSimpleName(), "foo", "*bar")
    }

    @Test
    void testNotNull() {
        dynConstruct("notNull", "foo")
        dynConstruct("isNotNull", "foo")
        dynConstruct(NotNullPredicate.class.getSimpleName(), "foo")
    }

    @Test
    void testNull() {
        dynConstruct("null", "foo")
        dynConstruct("isNull", "foo")
        dynConstruct(NullPredicate.class.getSimpleName(), "foo")
    }

    @Test
    void testOr() {
        dynConstruct("or", [[equal("foo", "f"), notEqual("bar", "b")] as Predicate[]] as Object[])
        dynConstruct(OrPredicate.class.getSimpleName(), [[equal("foo", "f"), notEqual("bar", "b")] as Predicate[]] as Object[])
    }
}
