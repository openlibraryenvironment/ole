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
package org.kuali.rice.kew.mail;


import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.quartz.Scheduler;


/**
 * A {@link Lifecycle} which is initialized on system startup that sets up
 * the daily and weekly email reminders.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EmailReminderLifecycle implements Lifecycle {

	private boolean started;

	public boolean isStarted() {
		return started;
	}

	public void start() throws Exception {
		// fetch scheduler here to initialize it outside of a transactional context, otherwise we get weird transaction errors
	    Scheduler scheduler = KSBServiceLocator.getScheduler();
	    if (scheduler == null) {
		throw new WorkflowException("Failed to locate Quartz Scheduler Service.");
	    }
	    KEWServiceLocator.getActionListEmailService().scheduleBatchEmailReminders();
	    started = true;
	}

	public void stop() throws Exception {
		started = false;
	}

}
