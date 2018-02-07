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
package org.kuali.rice.core.api.util;

import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * Test cases for VersionHelper class
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */

public class VersionHelperTest extends TestCase
{

    @Test
    public void testVersionHelper() {
        String verOne[] = {"1.2.3","6.8.83.4","5.0","snapshot-2.3.5", "something-0.333.447...-nice" };
        String verTwo[] = {"1.2.1","6.8.83","5.9","2.3-snapshot", "0.345...777" };
        boolean results[] = {false, false, true, false, true, false};
        int intResults[] = {1,1,-1,1,-1};

        for(int i=0;i<verOne.length;i++) {
              assertEquals(VersionHelper.compareVersion(verOne[i],verTwo[i]), intResults[i]);
        }

        for(int i=0;i<verOne.length;i++) {
            assertEquals(VersionHelper.compareVersion(verTwo[i],verOne[i]), -1*intResults[i]);
        }


        //  let's check the case where the version numbers are equal
        assertEquals(VersionHelper.compareVersion("7.7.7","7.7.7"), 0);
    }

    @Test
    public void testUndefined() {
        assertEquals(VersionHelper.compareVersion("undefined", "2.1.3-snapshot"), -1);
        assertEquals(VersionHelper.compareVersion("2.1.3", "undefined"), -1);

    }

}
