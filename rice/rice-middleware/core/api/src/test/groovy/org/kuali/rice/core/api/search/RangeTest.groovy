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
package org.kuali.rice.core.api.search

import org.junit.Test
import static org.junit.Assert.assertEquals

/**
 * Tests Range toString expression
 */
class RangeTest {
    private static String range(def low, def hi, def low_inc, def hi_inc) {
        def r = new Range()
        r.setLowerBoundInclusive(low_inc);
        r.setUpperBoundInclusive(hi_inc);
        r.setLowerBoundValue(low)
        r.setUpperBoundValue(hi)
        r.toString()
    }

    @Test void testLowIncHiInc() {
        assertEquals("a..b", range("a", "b", true, true))
    }

    @Test void testLowIncHiEx() {
        assertEquals("a..<b", range("a", "b", true, false))
    }

    @Test void testLowExHiInc() {
        assertEquals("a>..b", range("a", "b", false, true))
    }

    @Test void testLowExHiEx() {
        assertEquals("a>..<b", range("a", "b", false, false))
    }

    @Test void testLowInc() {
        assertEquals(">=a", range("a", null, true, true))
    }

    @Test void testLowEx() {
        assertEquals(">a", range("a", null, false, true))
    }

    @Test void testHiInc() {
        assertEquals("<=b", range(null, "b", true, true))
    }

    @Test void testHiEx() {
        assertEquals("<b", range(null, "b", true, false))
    }
}
