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
import org.kuali.rice.kns.datadictionary.validation.fieldlevel.EmailAddressValidationPattern;
import org.kuali.rice.krad.test.KRADTestCase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * EmailAddressValidationPatternTest tests {@link EmailAddressValidationPattern} - only valid email addresses should match
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EmailAddressValidationPatternTest extends KRADTestCase {
    private EmailAddressValidationPattern pattern;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        pattern = new EmailAddressValidationPattern();
    }


    @Test public final void testMatches_valid1() {
        assertTrue(pattern.matches("ww5@a.b.c.org"));
    }

    @Test public final void testMatches_valid2() {
        assertTrue(pattern.matches("something.else@a2.com"));
    }

    @Test public final void testMatches_valid3() {
        assertTrue(pattern.matches("something_else@something.else.com"));
    }

    @Test public final void testMatches_valid4() {
        assertTrue(pattern.matches("something-else@et-tu.com"));
    }


    @Test public final void testMatches_invalid1() {
        assertFalse(pattern.matches("a"));
    }

    @Test public final void testMatches_invalid2() {
        assertFalse(pattern.matches("@a.b.c.org"));
    }

    @Test public final void testMatches_invalid3() {
        assertFalse(pattern.matches("1@a.b.c.org"));
    }

    @Test public final void testMatches_invalid4() {
        assertFalse(pattern.matches("1@org"));
    }

    @Test public final void testMatches_invalid5() {
        assertFalse(pattern.matches("1@a"));
    }

    @Test public final void testMatches_invalid6() {
        assertFalse(pattern.matches(".@a.org"));
    }

    @Test public final void testMatches_invalid7() {
        assertFalse(pattern.matches("_@a.org"));
    }

    @Test public final void testMatches_invalid8() {
        assertFalse(pattern.matches("something@a.o-rg"));
    }
}
