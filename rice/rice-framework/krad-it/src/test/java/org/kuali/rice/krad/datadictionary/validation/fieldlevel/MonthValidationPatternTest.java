/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.krad.datadictionary.validation.fieldlevel;

import org.junit.Test;
import org.kuali.rice.kns.datadictionary.validation.fieldlevel.MonthValidationPattern;
import org.kuali.rice.krad.test.KRADTestCase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * MonthValidationPatternTest tests {@link MonthValidationPattern}
 *
 * <p>Valid months (01-12) should match</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MonthValidationPatternTest extends KRADTestCase {
    MonthValidationPattern pattern;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        pattern = new MonthValidationPattern();
    }

    @Test public final void testMatches_valid1() {
        assertTrue(pattern.matches("1"));
    }

    @Test public final void testMatches_valid2() {
        assertTrue(pattern.matches("01"));
    }

    @Test public final void testMatches_valid3() {
        assertTrue(pattern.matches("11"));
    }


    @Test public final void testMatches_invalid1() {
        assertFalse(pattern.matches("00"));
    }

    @Test public final void testMatches_invalid2() {
        assertFalse(pattern.matches("0"));
    }

    @Test public final void testMatches_invalid3() {
        assertFalse(pattern.matches("13"));
    }
}
