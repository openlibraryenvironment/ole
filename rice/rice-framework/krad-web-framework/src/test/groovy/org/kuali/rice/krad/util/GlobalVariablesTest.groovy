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
package org.kuali.rice.krad.util

import org.junit.Before
import org.junit.Test
import org.junit.After
import static org.junit.Assert.assertNotNull
import org.kuali.rice.krad.UserSession
import org.kuali.rice.kim.api.identity.Person
import org.springframework.test.AssertThrows
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertEquals

class GlobalVariablesTest {
    @After
    void reset() {
        GlobalVariables.reset()
    }

    @Test
    void testInitialValue() {
        assertEmptyGlobalVariables()
    }

    @Test(expected=NoSuchElementException)
    void testUnderflow() {
        GlobalVariables.popGlobalVariables()
        GlobalVariables.popGlobalVariables()
    }

    @Test
    void testStack() {
        assertEmptyGlobalVariables()

        GlobalVariables.pushGlobalVariables()
        assertEmptyGlobalVariables()
        setGlobalVariables("a")
        assertGlobalVariables("a")

        GlobalVariables.pushGlobalVariables()
        assertEmptyGlobalVariables()
        setGlobalVariables("b")
        assertGlobalVariables("b")

        GlobalVariables.pushGlobalVariables()
        assertEmptyGlobalVariables()
        setGlobalVariables("c")
        assertGlobalVariables("c")

        GlobalVariables.popGlobalVariables()
        assertGlobalVariables("b")

        GlobalVariables.popGlobalVariables()
        assertGlobalVariables("a")

        new AssertThrows(NoSuchElementException.class) {
            public void test() {
                GlobalVariables.popGlobalVariables()
            }
        };
    }

    @Test(expected=RuntimeException)
    void testHideSessionFromTests() {
        GlobalVariables.hideSessionFromTestsMessage = "test"
        GlobalVariables.userSession
    }

    private static void assertEmptyGlobalVariables(seed = null) {
        assertNull(GlobalVariables.userSession)
        assertTrue(GlobalVariables.messageMap.hasNoMessages())
        assertNull(GlobalVariables.getRequestCache(seed))
    }

    private static void setGlobalVariables(String seed) {
        GlobalVariables.messageMap.putError(seed, seed)
        GlobalVariables.setRequestCache(seed, seed)
    }

    private static void assertGlobalVariables(String seed) {
        assertEquals(seed, GlobalVariables.messageMap.getErrorMessagesForProperty(seed).get(0).errorKey)
        assertEquals(seed, GlobalVariables.getRequestCache(seed))
    }
}
