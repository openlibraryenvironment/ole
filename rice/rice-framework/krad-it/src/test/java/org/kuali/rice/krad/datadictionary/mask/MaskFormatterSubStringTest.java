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
package org.kuali.rice.krad.datadictionary.mask;

import org.junit.Test;
import org.kuali.rice.krad.test.KRADTestCase;

import static org.junit.Assert.*;

/**
 * MaskFormatterSubStringTest tests {@link MaskFormatterSubString} methods
 *
 * TODO: should be unit test
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MaskFormatterSubStringTest extends KRADTestCase {

    @Test
    public void testMaskValue() {
        MaskFormatterSubString formatterSubString = new MaskFormatterSubString();
        assertEquals(null, formatterSubString.maskValue(null));
        try {
            formatterSubString.setMaskCharacter(null);
            formatterSubString.maskValue("someMessage");
            fail("Mask Character needs specification");
        }
        catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Mask character not specified"));

        }

        formatterSubString = new MaskFormatterSubString();
        formatterSubString.setMaskLength(0);
        assertEquals("FifteenthValues", formatterSubString.maskValue("FifteenthValues"));
        assertEquals("123456", formatterSubString.maskValue(new Long(123456)));
        formatterSubString.setMaskLength(6);
        assertEquals(6, formatterSubString.getMaskLength());
        formatterSubString.setMaskCharacter("*");
        assertEquals("*", formatterSubString.getMaskCharacter());
        assertEquals("******nthValues", formatterSubString.maskValue("FifteenthValues"));
        assertEquals("******.0", formatterSubString.maskValue(new Float(123456)));
        formatterSubString.setMaskCharacter("#");
        formatterSubString.setMaskLength(3);
        assertEquals("###", formatterSubString.maskValue("12"));
        formatterSubString.setMaskLength(-2);
        assertEquals("FifteenthValues", formatterSubString.maskValue("FifteenthValues"));

    }


}
