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
package org.kuali.rice.ksb.messaging.web;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.kuali.rice.ksb.messaging.PersistedMessageBO;
import org.kuali.rice.ksb.messaging.quartz.MessageServiceExecutorJob;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.kuali.rice.ksb.util.KSBConstants;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


/**
 * action to view messages in quartz and push them into the message queue. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class QuartzQueueAction extends KSBAction {
    
    private static final String RENDER_LIST_OVERRIDE = "_renderlistoverride";

    @Override
    public ActionMessages establishRequiredState(HttpServletRequest request, ActionForm form) throws Exception {
	if ("moveToRouteQueue".equals(request.getParameter("methodToCall")) && request.getAttribute(RENDER_LIST_OVERRIDE) == null) {
	    return null;
	}
	
	List<QuartzQueueForm> jobs = new ArrayList<QuartzQueueForm>();
	String[] jobGroups = KSBServiceLocator.getScheduler().getJobGroupNames(); 
	for (int i = 0; i < jobGroups.length; i++) {
	    String jobGroup = KSBServiceLocator.getScheduler().getJobGroupNames()[i];
	    String[] jobNames = KSBServiceLocator.getScheduler().getJobNames(jobGroup);
	    for (int j = 0; j < jobNames.length; j++) {
		JobDetail job = KSBServiceLocator.getScheduler().getJobDetail(jobNames[j], jobGroup);
		Trigger trigger = KSBServiceLocator.getScheduler().getTriggersOfJob(job.getName(), job.getGroup())[0];
		jobs.add(new QuartzQueueForm(job, trigger));
	    }
	}
	request.setAttribute("jobs", jobs);
	return null;
    }

    @Override
    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return mapping.findForward("joblisting");
    }
    
    public ActionForward moveToRouteQueue(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	QuartzQueueForm quartzForm = (QuartzQueueForm)form;

	JobDetail job = KSBServiceLocator.getScheduler().getJobDetail(quartzForm.getJobName(), quartzForm.getJobGroup());
	PersistedMessageBO message = (PersistedMessageBO)job.getJobDataMap().get(MessageServiceExecutorJob.MESSAGE_KEY);
     if(message != null){
	    message.setQueueStatus(KSBConstants.ROUTE_QUEUE_EXCEPTION);

        KSBServiceLocator.getMessageQueueService().save(message);
        KSBServiceLocator.getScheduler().deleteJob(quartzForm.getJobName(), quartzForm.getJobGroup());
     }
    request.setAttribute(RENDER_LIST_OVERRIDE, new Object());
    establishRequiredState(request, form);
	return mapping.findForward("joblisting");
    }
    
    
}
