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
package org.kuali.rice.krad.datadictionary.validation.charlevel;

import org.junit.Test;
import org.kuali.rice.kns.datadictionary.validation.charlevel.AlphaNumericValidationPattern;
import org.kuali.rice.krad.datadictionary.validation.ValidationTestUtils;
import org.kuali.rice.test.BaseRiceTestCase;

/**
 * AlphaNumericValidationPatternTest tests {@link AlphaNumericValidationPattern}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AlphaNumericValidationPatternTest extends BaseRiceTestCase {
    private AlphaNumericValidationPattern pattern;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        pattern = new AlphaNumericValidationPattern();
    }

    /**
     * tests that only numbers and letters of the alphabet are allowed
     */
    @Test public final void testMatch_allowDefault() {
        boolean[] expected = { true, // ""
                false, // "!!!"
                false, // "[a-9]"
                false, // "^A-Z"
                true, // "abc"
                false, // "a bc"
                false, // "a_bc"
                true, // "123"
                false, // "12 3"
                false, // "12_3"
                true, // "a1b2c3"
                false, // "a1b2_c3"
                false, // "a 1b2c3"
                false, // "a 1b2_c3"
                false, //"foo.bar"
                false, //"foo.bar_baz"
                false, //".bar_foo baz"
        };

        ValidationTestUtils.assertPatternMatches(pattern, expected);
    }

    /**
     * tests that underscores, numbers and letters of the alphabet are allowed
     */
    @Test public final void testMatch_allowUnderscore() {
        boolean[] expected = { true, // ""
                false, // "!!!"
                false, // "[a-9]"
                false, // "^A-Z"
                true, // "abc"
                false, // "a bc"
                true, // "a_bc"
                true, // "123"
                false, // "12 3"
                true, // "12_3"
                true, // "a1b2c3"
                true, // "a1b2_c3"
                false, // "a 1b2c3"
                false, // "a 1b2_c3"
                false, //"foo.bar"
                false, //"foo.bar_baz"
                false, //".bar_foo baz"
        };

        pattern.setAllowUnderscore(true);
        ValidationTestUtils.assertPatternMatches(pattern, expected);
    }

    /**
     * tests that white space, numbers and letters of the alphabet are allowed
     */
    @Test public final void testMatch_allowWhitespace() {
        boolean[] expected = { true, // ""
                false, // "!!!"
                false, // "[a-9]"
                false, // "^A-Z"
                true, // "abc"
                true, // "a bc"
                false, // "a_bc"
                true, // "123"
                true, // "12 3"
                false, // "12_3"
                true, // "a1b2c3"
                false, // "a1b2_c3"
                true, // "a 1b2c3"
                false, // "a 1b2_c3"
                false, //"foo.bar"
                false, //"foo.bar_baz"
                false, //".bar_foo baz"
        };

        pattern.setAllowWhitespace(true);
        ValidationTestUtils.assertPatternMatches(pattern, expected);
    }

    /**
     * tests that white space, underscores, numbers and letters of the alphabet are allowed
     */
    @Test public final void testMatch_allowUnderScoreAndWhiteSpace() {
        boolean[] expected = { true, // ""
                false, // "!!!"
                false, // "[a-9]"
                false, // "^A-Z"
                true, // "abc"
                true, // "a bc"
                true, // "a_bc"
                true, // "123"
                true, // "12 3"
                true, // "12_3"
                true, // "a1b2c3"
                true, // "a1b2_c3"
                true, // "a 1b2c3"
                true, // "a 1b2_c3"
                false, //"foo.bar"
                false, //"foo.bar_baz"
                false, //".bar_foo baz"
        };

        pattern.setAllowUnderscore(true);
        pattern.setAllowWhitespace(true);

        ValidationTestUtils.assertPatternMatches(pattern, expected);
    }

    /**
     * tests that periods, numbers and letters of the alphabet are allowed
     */
    @Test public final void testMatch_allowPeriod() {
        boolean[] expected = { true, // ""
                false, // "!!!"
                false, // "[a-9]"
                false, // "^A-Z"
                true, // "abc"
                false, // "a bc"
                false, // "a_bc"
                true, // "123"
                false, // "12 3"
                false, // "12_3"
                true, // "a1b2c3"
                false, // "a1b2_c3"
                false, // "a 1b2c3"
                false, // "a 1b2_c3"
                true, //"foo.bar"
                false, //"foo.bar_baz"
                false, //".bar_foo baz"
        };

        pattern.setAllowPeriod(true);

        ValidationTestUtils.assertPatternMatches(pattern, expected);
    }

    /**
     * tests that periods, underscores, numbers and letters of the alphabet are allowed
     */
    @Test public final void testMatch_allowPeriodAndUnderscore() {
        boolean[] expected = { true, // ""
                false, // "!!!"
                false, // "[a-9]"
                false, // "^A-Z"
                true, // "abc"
                false, // "a bc"
                true, // "a_bc"
                true, // "123"
                false, // "12 3"
                true, // "12_3"
                true, // "a1b2c3"
                true, // "a1b2_c3"
                false, // "a 1b2c3"
                false, // "a 1b2_c3"
                true, //"foo.bar"
                true, //"foo.bar_baz"
                false, //".bar_foo baz"
        };

        pattern.setAllowPeriod(true);
        pattern.setAllowUnderscore(true);

        ValidationTestUtils.assertPatternMatches(pattern, expected);
    }

    /**
     * tests that periods, underscores, white space, numbers and letters of the alphabet are allowed
     */
    @Test public final void testMatch_allowPeriodAndUnderscoreAndSpace() {
        boolean[] expected = { true, // ""
                false, // "!!!"
                false, // "[a-9]"
                false, // "^A-Z"
                true, // "abc"
                true, // "a bc"
                true, // "a_bc"
                true, // "123"
                true, // "12 3"
                true, // "12_3"
                true, // "a1b2c3"
                true, // "a1b2_c3"
                true, // "a 1b2c3"
                true, // "a 1b2_c3"
                true, //"foo.bar"
                true, //"foo.bar_baz"
                true, //".bar_foo baz"
        };

        pattern.setAllowPeriod(true);
        pattern.setAllowUnderscore(true);
        pattern.setAllowWhitespace(true);

        ValidationTestUtils.assertPatternMatches(pattern, expected);
    }
}
