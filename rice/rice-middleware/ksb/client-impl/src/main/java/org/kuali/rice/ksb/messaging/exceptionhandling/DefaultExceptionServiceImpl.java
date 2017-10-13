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
package org.kuali.rice.ksb.messaging.exceptionhandling;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.api.bus.Endpoint;
import org.kuali.rice.ksb.api.bus.ServiceBus;
import org.kuali.rice.ksb.api.messaging.AsynchronousCall;
import org.kuali.rice.ksb.messaging.PersistedMessageBO;
import org.kuali.rice.ksb.messaging.quartz.MessageServiceExecutorJob;
import org.kuali.rice.ksb.messaging.quartz.MessageServiceExecutorJobListener;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;


/**
 * Default implementation of {@link ExceptionRoutingService}.  Just saves 
 * the message in the queue as is, which should be marked Exception by the 
 * {@link MessageExceptionHandler}.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class DefaultExceptionServiceImpl implements ExceptionRoutingService {
	
	private static final Logger LOG = Logger.getLogger(DefaultExceptionServiceImpl.class);

	public void placeInExceptionRouting(Throwable throwable, PersistedMessageBO message, Object service) throws Exception {
		LOG.error("Exception caught processing message " + message.getRouteQueueId() + " " + message.getServiceName() + ": " + throwable);
		
		
		AsynchronousCall methodCall = null;
		if (message.getMethodCall() != null) {
			methodCall = message.getMethodCall();
		} else {
			methodCall = message.getPayload().getMethodCall();
		}
		message.setMethodCall(methodCall);
		MessageExceptionHandler exceptionHandler = getMessageExceptionHandler(methodCall.getServiceConfiguration().getServiceName());
		exceptionHandler.handleException(throwable, message, service);
	}
	
	public void placeInExceptionRoutingLastDitchEffort(Throwable throwable, PersistedMessageBO message, Object service) throws Exception {
		LOG.error("Exception caught processing message " + message.getRouteQueueId() + " " + message.getServiceName() + ": " + throwable);
		
		AsynchronousCall methodCall = null;
		if (message.getMethodCall() != null) {
			methodCall = message.getMethodCall();
		} else {
			methodCall = message.getPayload().getMethodCall();
		}
		message.setMethodCall(methodCall);
		MessageExceptionHandler exceptionHandler = getMessageExceptionHandler(methodCall.getServiceConfiguration().getServiceName());
		exceptionHandler.handleExceptionLastDitchEffort(throwable, message, service);
	}
	
	protected MessageExceptionHandler getMessageExceptionHandler(QName serviceName) {
		ServiceBus serviceBus = KsbApiServiceLocator.getServiceBus();
		Endpoint endpoint = serviceBus.getEndpoint(serviceName);
		if (endpoint == null) {
			throw new RiceRuntimeException("No services found for name " + serviceName);
		}
		String messageExceptionHandlerName = endpoint.getServiceConfiguration().getMessageExceptionHandler();
		if (messageExceptionHandlerName == null) {
			messageExceptionHandlerName = DefaultMessageExceptionHandler.class.getName();
		}
		return (MessageExceptionHandler) GlobalResourceLoader.getObject(new ObjectDefinition(messageExceptionHandlerName));
	}
	
	

	public void scheduleExecution(Throwable throwable, PersistedMessageBO message, String description) throws Exception {
		KSBServiceLocator.getMessageQueueService().delete(message);
		Scheduler scheduler = KSBServiceLocator.getScheduler();
		JobDataMap jobData = new JobDataMap();
		jobData.put(MessageServiceExecutorJob.MESSAGE_KEY, message);
		JobDetail jobDetail = new JobDetail("Exception_Message_Job " + Math.random(), "Exception Messaging",
			MessageServiceExecutorJob.class);
		jobDetail.setJobDataMap(jobData);
		if (!StringUtils.isBlank(description)) {
		    jobDetail.setDescription(description);
		}
		jobDetail.addJobListener(MessageServiceExecutorJobListener.NAME);
		Trigger trigger = new SimpleTrigger("Exception_Message_Trigger " + Math.random(), "Exception Messaging", message
			.getQueueDate());
		trigger.setJobDataMap(jobData);// 1.6 bug required or derby will choke
		scheduler.scheduleJob(jobDetail, trigger);    
	}
		
}
