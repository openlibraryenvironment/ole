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
package org.kuali.rice.kcb.config;

import org.kuali.rice.kcb.service.GlobalKCBServiceLocator;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.quartz.JobDetail;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

/**
 * Initializing bean that initializes KCB (specifically the GlobalKCBServiceLocator)
 * on Spring context initialization.  This bean should be eagerly initialized (not marked lazy) 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KCBInitializer implements BeanFactoryAware, InitializingBean, DisposableBean {
    private BeanFactory beanFactory;
    private Trigger messageProcessingTrigger;
    private JobDetail messageProcessingJobDetail;
    protected Scheduler scheduler;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * Sets the Trigger for the message processing job
     * @param messageProcessingTrigger the Trigger for the message processing job
     */
    @Required
    public void setMessageProcessingTrigger(Trigger messageProcessingTrigger) {
        this.messageProcessingTrigger = messageProcessingTrigger;
    }
    
    /**
     * Sets the JobDetail for the message processing job
     * @param messageProcessingJobDetail the JobDetail for the message processing job
     */
    @Required
    public void setMessageProcessingJobDetail(JobDetail messageProcessingJobDetail) {
        this.messageProcessingJobDetail = messageProcessingJobDetail;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        GlobalKCBServiceLocator.init(beanFactory);
        // kill the reference, our job is done
        beanFactory = null;

        Scheduler scheduler = getScheduler()==null?KSBServiceLocator.getScheduler():getScheduler();
        scheduler.addJob(messageProcessingJobDetail, true);

        addTriggerToScheduler(messageProcessingTrigger);
    }
    
    private void addTriggerToScheduler(Trigger trigger) throws SchedulerException {
		boolean triggerExists = (getScheduler().getTrigger(trigger.getName(), trigger.getGroup()) != null);
		if (!triggerExists) {
			try {
				getScheduler().scheduleJob(trigger);
			} catch (ObjectAlreadyExistsException ex) {
				getScheduler().rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
			}
		} else {
		    getScheduler().rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
		}
	}

    @Override
    public void destroy() throws Exception {
        // prevent anything from accessing our services after the module has been destroyed/shutdown
        // our module's lifecycle is tied to the Spring context lifecycle for now
        GlobalKCBServiceLocator.destroy();
    }

	/**
	 * @return the scheduler
	 */
	public Scheduler getScheduler() {
		return this.scheduler;
	}

	/**
	 * @param scheduler the scheduler to set
	 */
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
    
}
