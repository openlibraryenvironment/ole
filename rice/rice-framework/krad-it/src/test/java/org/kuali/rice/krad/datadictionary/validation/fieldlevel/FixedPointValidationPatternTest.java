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

import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.kns.datadictionary.validation.fieldlevel.FixedPointValidationPattern;
import org.kuali.rice.krad.datadictionary.validation.ValidationPattern;
import org.kuali.rice.test.BaseRiceTestCase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * FixedPointValidationPatternTest tests {@link FixedPointValidationPattern}
 *
 * <p>Valid negative and positive fixed point numbers should match</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class FixedPointValidationPatternTest extends BaseRiceTestCase {
    // Unlike its superclass, FixedPointValidationPattern does not use Spring.
    FixedPointValidationPattern pattern;

    @Before
    public final void setUp() throws Exception {

        pattern = new FixedPointValidationPattern();
        pattern.setPrecision(2);
        pattern.setScale(1);
    }


    @Test public final void testDefaultAllows_empty() {
        assertFalse(pattern.matches(""));
    }

    @Test public final void testDefaultAllows_positive1() {
        assertTrue(pattern.matches(".1"));
    }

    @Test public final void testDefaultAllows_positive2() {
        assertTrue(pattern.matches("0.1"));
    }

    @Test public final void testDefaultAllows_positive3() {
        assertTrue(pattern.matches("1.1"));
    }

    @Test public final void testDefaultAllows_positive4() {
        assertTrue(pattern.matches("1"));
    }

    @Test public final void testDefaultAllows_positive5() {
        assertTrue(pattern.matches("1.0"));
    }


    @Test public final void testDefaultAllows_negative1() {
        assertFalse(pattern.matches("-.1"));
    }

    @Test public final void testDefaultAllows_negative2() {
        assertFalse(pattern.matches("-0.1"));
    }

    @Test public final void testDefaultAllows_negative3() {
        assertFalse(pattern.matches("-1.1"));
    }

    @Test public final void testDefaultAllows_negative4() {
        assertFalse(pattern.matches("-1"));
    }

    @Test public final void testDefaultAllows_negative5() {
        assertFalse(pattern.matches("-1.0"));
    }


    @Test public final void testDefaultAllows_invalid1() {
        assertFalse(pattern.matches("-."));
    }

    @Test public final void testDefaultAllows_invalid2() {
        assertFalse(pattern.matches("1."));
    }

    @Test public final void testDefaultAllows_invalid3() {
        assertFalse(pattern.matches("-1."));
    }

    @Test public final void testDefaultAllows_invalid4() {
        assertFalse(pattern.matches("12."));
    }

    @Test public final void testDefaultAllows_invalid5() {
        assertFalse(pattern.matches("1245678901234567890123.23"));
    }

    @Test public final void testDefaultAllows_invalid6() {
        assertFalse(pattern.matches("123"));
    }

    @Test public final void testAllowNegative_positive1() {
        assertTrue(pattern.matches(".1"));
    }

    @Test public final void testAllowNegative_positive2() {
        pattern.setAllowNegative(true);

        assertTrue(pattern.matches("0.1"));
    }

    @Test public final void testAllowNegative_positive3() {
        pattern.setAllowNegative(true);

        assertTrue(pattern.matches("1.1"));
    }

    @Test public final void testAllowNegative_positive4() {
        pattern.setAllowNegative(true);

        assertTrue(pattern.matches("1"));
    }

    @Test public final void testAllowNegative_positive5() {
        pattern.setAllowNegative(true);

        assertTrue(pattern.matches("1.0"));
    }


    @Test public final void testAllowNegative_negative1() {
        pattern.setAllowNegative(true);

        assertTrue(pattern.matches("-.1"));
    }

    @Test public final void testAllowNegative_negative2() {
        pattern.setAllowNegative(true);

        assertTrue(pattern.matches("-0.1"));
    }

    @Test public final void testAllowNegative_negative3() {
        pattern.setAllowNegative(true);

        assertTrue(pattern.matches("-1.1"));
    }

    @Test public final void testAllowNegative_negative4() {
        pattern.setAllowNegative(true);

        assertTrue(pattern.matches("-1"));
    }

    @Test public final void testAllowNegative_negative5() {
        pattern.setAllowNegative(true);

        assertTrue(pattern.matches("-1.0"));
    }


    @Test public final void testAllowNegative_invalid1() {
        pattern.setAllowNegative(true);

        assertFalse(pattern.matches("-."));
    }

    @Test public final void testAllowNegative_invalid2() {
        pattern.setAllowNegative(true);

        assertFalse(pattern.matches("1."));
    }

    @Test public final void testAllowNegative_invalid3() {
        pattern.setAllowNegative(true);

        assertFalse(pattern.matches("-1."));
    }

    @Test public final void testAllowNegative_invalid4() {
        pattern.setAllowNegative(true);

        assertFalse(pattern.matches("-12."));
    }

    @Test public final void testAllowNegative_invalid5() {
        pattern.setAllowNegative(true);

        assertFalse(pattern.matches("-1.23"));
    }

    @Test public final void testAllowNegative_invalid6() {
        pattern.setAllowNegative(true);

        assertFalse(pattern.matches("123."));
    }

    @Test public final void testAllowNegative_invalid7() {
        pattern.setAllowNegative(true);

        assertFalse(pattern.matches(".123"));
    }
    
    @Test(expected=ValidationPattern.ValidationPatternException.class)
    public final void testZero_Percision_And_Scale_invalid8() {
        //what happens when precision & scale are not set in DD
    	pattern.setPrecision(0);
        pattern.setScale(0);
        pattern.completeValidation();
    }
    
    @Test(expected=ValidationPattern.ValidationPatternException.class)
    public final void testPrecision_Less_Than_Scale_invalid9() {
    	pattern.setPrecision(1);
        pattern.setScale(3);
        pattern.completeValidation();
    }
    
    @Test(expected=ValidationPattern.ValidationPatternException.class)
    public final void testPrecision_And_Scale_Negative_invalid10() {
    	pattern.setPrecision(-3);
        pattern.setScale(-1);
        pattern.completeValidation();
    }

}
