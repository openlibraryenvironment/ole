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
package org.kuali.rice.core.framework.util

import java.util.concurrent.CountDownLatch
import org.junit.Test
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNull

/**
 * Tests setting and clearing ApplicationThreadLocals
 */
class ApplicationThreadLocalTest {
    ApplicationThreadLocal<String> TL1 = new ApplicationThreadLocal<String>();
    ApplicationThreadLocal<String> TL2 = new ApplicationThreadLocal<String>();

    /**
     * Tests the clearing of ApplicationThreadLocals by spawning a thread which sets an ATL value,
     * invoking clear() in a separate (main) thread, then resuming the spawned thread and verifying
     * that all ATLs have been cleared.
     */
    @Test void testApplicationThreadLocal() {
        def threadReady = new CountDownLatch(1)
        def atlsCleared = new CountDownLatch(1)

        def tester = new Runnable() {
            public void run() {
                assertNull(TL1.get());
                TL1.set("Test1");
                assertEquals("Test1", TL1.get());

                assertNull(TL2.get());
                TL2.set("Test2");
                assertEquals("Test2", TL2.get());

                // wait for the test to clear the ATLs
                threadReady.countDown();
                atlsCleared.await();

                System.err.println("Testing ATLS cleared")
                // ATL should be cleared
                assertNull(TL1.get());
                assertNull(TL2.get());
            }
        }
        
        Throwable error = null;
        def t = new Thread(tester);
        t.uncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
            void uncaughtException(Thread th, Throwable e) {
                error = e;
            }
        };
        t.start();

        // wait for spawned thread to be ready
        threadReady.await();
        // clear the ATLs
        System.err.println("Clearing ATLS")
        ApplicationThreadLocal.clear();
        // notify the thread to resume
        atlsCleared.countDown();
        if (error != null) {
            throw error;
        }
    }
}
