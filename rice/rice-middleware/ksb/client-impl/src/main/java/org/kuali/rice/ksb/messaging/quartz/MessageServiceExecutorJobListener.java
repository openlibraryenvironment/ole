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
package org.kuali.rice.ksb.messaging.quartz;

import org.kuali.rice.ksb.messaging.PersistedMessageBO;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.kuali.rice.ksb.util.KSBConstants;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;


/**
 * This is a description of what this class does - rkirkend don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class MessageServiceExecutorJobListener implements JobListener {

    public static final String NAME = "MessageServiceExecutorJobListener";
    
    /**
     * This overridden method ...
     * 
     * @see org.quartz.JobListener#getName()
     */
    public String getName() {
	return NAME;
    }

    /**
     * This overridden method ...
     * 
     * @see org.quartz.JobListener#jobExecutionVetoed(org.quartz.JobExecutionContext)
     */
    public void jobExecutionVetoed(JobExecutionContext context) {
    }

    /**
     * This overridden method ...
     * 
     * @see org.quartz.JobListener#jobToBeExecuted(org.quartz.JobExecutionContext)
     */
    public void jobToBeExecuted(JobExecutionContext context) {
    }

    /**
     * This overridden method ...
     * 
     * @see org.quartz.JobListener#jobWasExecuted(org.quartz.JobExecutionContext, org.quartz.JobExecutionException)
     */
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException exception) {
	if (context.getJobInstance() instanceof MessageServiceExecutorJob && exception != null) {
	    PersistedMessageBO message = (PersistedMessageBO)context.getJobDetail().getJobDataMap().get(MessageServiceExecutorJob.MESSAGE_KEY);
	    message.setQueueStatus(KSBConstants.ROUTE_QUEUE_EXCEPTION);
	    KSBServiceLocator.getMessageQueueService().save(message);
	}

    }

}
