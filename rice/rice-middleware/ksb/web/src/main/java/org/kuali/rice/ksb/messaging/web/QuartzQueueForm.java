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
import org.quartz.JobDetail;
import org.quartz.Trigger;


/**
 * Struts form for quartz queue processing 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class QuartzQueueForm extends ActionForm {

    private static final long serialVersionUID = 4589673941470488079L;
    
    private String jobName;
    private String jobGroup;
    private JobDetail jobDetail;
    private Trigger trigger;
    
    public QuartzQueueForm() {}
    
    public QuartzQueueForm(JobDetail jobDetail, Trigger trigger) {
	this.jobDetail = jobDetail;
	this.trigger = trigger;
    }
    
    public JobDetail getJobDetail() {
        return this.jobDetail;
    }
    public void setJobDetail(JobDetail jobDetail) {
        this.jobDetail = jobDetail;
    }
    public Trigger getTrigger() {
        return this.trigger;
    }
    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }
    public String getJobGroup() {
        return this.jobGroup;
    }
    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }
    public String getJobName() {
        return this.jobName;
    }
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }   
}
