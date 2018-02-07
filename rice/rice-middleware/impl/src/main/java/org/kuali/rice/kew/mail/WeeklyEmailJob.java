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

import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 * Quartz job for sending weekly email reminders.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class WeeklyEmailJob implements Job {

	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		if (shouldExecute()) {
			KEWServiceLocator.getActionListEmailService().sendWeeklyReminder();
		}
	}

	protected boolean shouldExecute() {
		return Boolean.valueOf(ConfigContext.getCurrentContextConfig().getProperty(KewApiConstants.WEEKLY_EMAIL_ACTIVE));
	}

}
