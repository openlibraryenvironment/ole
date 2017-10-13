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
package org.kuali.rice.ksb.messaging.threadpool;

import org.kuali.rice.ksb.messaging.MessageServiceInvoker;
import org.kuali.rice.ksb.messaging.PersistedMessageBO;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * A comparator to put into the {@link PriorityBlockingQueue} used in the {@link KSBThreadPoolImpl}.
 * 
 *  Determines execution order by priority and create date.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class PriorityBlockingQueuePersistedMessageComparator implements Comparator {

    
    public int compare(Object arg0, Object arg1) {
	if (! (arg0 instanceof MessageServiceInvoker) || ! (arg1 instanceof MessageServiceInvoker) ) {
	    return 0;
	}
	PersistedMessageBO message0 = ((MessageServiceInvoker)arg0).getMessage();
	PersistedMessageBO message1 = ((MessageServiceInvoker)arg1).getMessage();
	
	if (message0.getQueuePriority() < message1.getQueuePriority()) {
	    return -1;
	} else if (message0.getQueuePriority() > message1.getQueuePriority()) {
	    return 1;
	}
	
	if (message0.getQueueDate().before(message1.getQueueDate())) {
	    return -1;
	} else if (message0.getQueueDate().after(message1.getQueueDate())) {
	    return 1;
	}
	
	return 0;
    }

}
