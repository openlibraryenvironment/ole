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
import org.kuali.rice.kns.datadictionary.validation.charlevel.UTF8AnyCharacterValidationPattern;
import org.kuali.rice.krad.datadictionary.validation.UTF8ValidationTestUtils;
import org.kuali.rice.test.BaseRiceTestCase;

/**
 * UTF8AnyCharacterValidationPatternTest tests {@link UTF8AnyCharacterValidationPattern}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UTF8AnyCharacterValidationPatternTest extends BaseRiceTestCase {
    private UTF8AnyCharacterValidationPattern pattern;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        pattern = new UTF8AnyCharacterValidationPattern();
    }

    @Test public final void testMatch_allUTF8ExceptWhiteSpace() {
        boolean[] expected = { true, // ""
                true, // "!!!"
                true, // "[a-9]"
                true, // "^A-Z"
                true, // "abc"
                false, // "12a 3bc"
                true, // "12a_3bc"
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
                true, //"!\"#abs$%&'()*+,./:;<=abs>?@\\^_abs`{|}~-"
                true,
                false, //"\t"
                false,
                
        };
        
        UTF8ValidationTestUtils.assertPatternMatches(pattern, expected);
    }
    
    @Test public final void testMatch_allowALLUTF8() {
        boolean[] expected = { true, // ""
                true, // "!!!"
                true, // "[a-9]"
                true, // "^A-Z"
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
                true, //"!\"#abs$%&'()*+,./:;<=abs>?@\\^_abs`{|}~-"
                true,
                true, //"\t"
                true, 
        };
        
        pattern.setAllowWhitespace(true);
        UTF8ValidationTestUtils.assertPatternMatches(pattern, expected);
    }
}
