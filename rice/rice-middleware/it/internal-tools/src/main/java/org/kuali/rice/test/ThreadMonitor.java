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
package org.kuali.rice.test;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Some tests will spawn threads which we want to wait for completion on before we move onto the
 * next test.  The ThreadMonitor is a place where those outstanding thread can be stored
 * and handled by the test harnesses tearDown method.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ThreadMonitor {

	private static List<Thread> threads = new ArrayList<Thread>();
	
	public static void addThread(Thread thread) {
		threads.add(thread);
	}
	
	/**
	 * Waits for all outstanding monitored threads to complete.  If the
	 * specified timeout is exceeded for any given thread then the test
	 * will fail.
	 * 
	 * @param maxWait maximum number of milliseconds to wait for any particular thread to die
	 */
	public static void tearDown(long maxWait) {
		Thread thread = null;
		try {
			for (Thread t : threads) {
				thread = t;
				thread.join(maxWait);	
			}
		} catch (InterruptedException e) {
			Assert.fail("Failed to wait for test thread to complete: " + (thread == null ? null : thread.getName()));
		} finally {
			threads.clear();
		}
	}
	
}
