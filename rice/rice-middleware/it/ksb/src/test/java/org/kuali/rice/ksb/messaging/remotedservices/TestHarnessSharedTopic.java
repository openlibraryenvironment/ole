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
package org.kuali.rice.ksb.messaging.remotedservices;

import java.io.Serializable;

import org.kuali.rice.core.api.config.CoreConfigHelper;
import org.kuali.rice.ksb.messaging.service.KSBJavaService;


public class TestHarnessSharedTopic implements KSBJavaService {

    public static int CALL_COUNT = 0;
    public static int CALL_COUNT_NOTIFICATION_THRESHOLD = 0;
    public static Object LOCK = new Object();

    public synchronized void invoke(Serializable payLoad) {
            CALL_COUNT++;
        System.out.println("!!!TestHarnessSharedTopic called with M.E " + CoreConfigHelper.getApplicationId() + " !!! ");
        ServiceCallInformationHolder.stuff.put("TestHarnessCalled", Boolean.TRUE);
        if (CALL_COUNT_NOTIFICATION_THRESHOLD > 0) {
            if (CALL_COUNT == CALL_COUNT_NOTIFICATION_THRESHOLD) {
            notifyOnLock();
            }
        } else {
            notifyOnLock();
        }


    }
    
    public void notifyOnLock() {
        synchronized (LOCK) {
            LOCK.notifyAll();
        }
    }

}
