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
import org.kuali.rice.kns.datadictionary.validation.charlevel.NumericValidationPattern;
import org.kuali.rice.krad.datadictionary.validation.ValidationTestUtils;
import org.kuali.rice.test.BaseRiceTestCase;

/**
 * tests {@link NumericValidationPattern}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NumericValidationPatternTest extends BaseRiceTestCase {
    private NumericValidationPattern pattern;

    
    public void setUp() throws Exception {
        pattern = new NumericValidationPattern();
    }

    /**
     * tests that sequences containing only numbers match
     */
    @Test public final void testMatch_allowDefault() {
        boolean[] expected = { true, // ""
                false, // "!!!"
                false, // "[a-9]"
                false, // "^A-Z"
                false, // "abc"
                false, // "a bc"
                false, // "a_bc"
                true, // "123"
                false, // "12 3"
                false, // "12_3"
                false, // "a1b2c3"
                false, // "a1b2_c3"
                false, // "a 1b2c3"
                false, // "a 1b2_c3"
                false, //"foo.bar"
                false, //"foo.bar_baz"
                false, //".bar_foo baz"
        };

        ValidationTestUtils.assertPatternMatches(pattern, expected);
    }
}
