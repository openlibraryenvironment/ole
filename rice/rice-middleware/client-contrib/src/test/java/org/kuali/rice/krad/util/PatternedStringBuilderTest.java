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
package org.kuali.rice.krad.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PatternedStringBuilderTest {

    @Test
    public void testSprintf() {
        double pi = Math.PI;
        PatternedStringBuilder patterenedStringBuilder = new PatternedStringBuilder("pi = %5.3f");
        String expectedVal = patterenedStringBuilder.sprintf(pi);
        assertEquals("pi = 3.142", expectedVal);
        patterenedStringBuilder.setPattern("%4$2s %3$2s %2$2s %1$2s");
        assertEquals(" z  y  x  w", patterenedStringBuilder.sprintf("w", "x", "y", "z"));
        patterenedStringBuilder.setPattern("");
        assertEquals("", patterenedStringBuilder.sprintf("somethingElse"));
        // This basically replicates tests done via jdk to test java.util.Formatter.java
    }
}
