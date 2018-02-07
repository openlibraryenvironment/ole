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
package org.kuali.rice.ksb.messaging;

import org.junit.Test;
import org.kuali.rice.ksb.messaging.threadpool.KSBThreadPool;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.kuali.rice.ksb.test.KSBTestCase;


/**
 * This is a description of what this class does - rkirkend don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KSBThreadPoolTest extends KSBTestCase {

    
    @Test public void testKSBThreadPoolBasicFunctionality() throws Exception {
	KSBThreadPool threadPool = KSBServiceLocator.getThreadPool();
	threadPool.setCorePoolSize(1);
	threadPool.execute(new TestRunnable());
    }
    
    private class TestRunnable implements Runnable {
    	public void run() {}
    }
    
    
}
