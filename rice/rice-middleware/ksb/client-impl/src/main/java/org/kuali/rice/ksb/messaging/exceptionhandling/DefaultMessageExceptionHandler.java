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

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;
import org.kuali.rice.ksb.messaging.PersistedMessageBO;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.kuali.rice.ksb.util.KSBConstants;


/**
 * Default implementation of the {@link MessageExceptionHandler} which handles exceptions thrown from message processing.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DefaultMessageExceptionHandler implements MessageExceptionHandler {

    private static final Logger LOG = Logger.getLogger(DefaultMessageExceptionHandler.class);

    private static final long DEFAULT_TIME_INCREMENT = 60 * 60 * 1000;

    private static final int DEFAULT_MAX_RETRIES = 7;

    public void handleException(Throwable throwable, PersistedMessageBO message, Object service) throws Exception {
        if (isInException(message)) {
            placeInException(throwable, message);
        } else {
            requeue(throwable, message);
        }
    }

    public void handleExceptionLastDitchEffort(Throwable throwable, PersistedMessageBO message, Object service) throws Exception {
		LOG.error("Complete failure when attempting to put message into exception routing!  Message was: " + message, throwable);
	}

	public boolean isInException(PersistedMessageBO message) {
        ServiceConfiguration serviceConfiguration = message.getMethodCall().getServiceConfiguration();

        if (getImmediateExceptionRouting()) {
            return true;
        }

        Integer globalMaxRetryAttempts = getGlobalMaxRetryAttempts();
        if (globalMaxRetryAttempts != null) {
            LOG.info("Global Max Retry has been set, so is overriding other max retry attempts.");
            LOG.info("Global Max Retry count = " + globalMaxRetryAttempts + ".");
            return (message.getRetryCount().intValue() >= globalMaxRetryAttempts.intValue());
        }

        if (serviceConfiguration.getRetryAttempts() > 0) {
            LOG.info("Message set for retry exception handling.  Message retry count = " + message.getRetryCount());
            if (message.getRetryCount() >= serviceConfiguration.getRetryAttempts()) {
                return true;
            }
        } else if (serviceConfiguration.getMillisToLive() > 0) {
            LOG.info("Message set for time to live exception handling.  Message expiration date = " + message.getExpirationDate().getTime());
            if (System.currentTimeMillis() > message.getExpirationDate().getTime()) {
                return true;
            }
        } else if (message.getRetryCount() >= this.getMaxRetryAttempts()) {
            LOG.info("Message set for default exception handling.  Comparing retry count = " + message.getRetryCount() + " against default max count.");
            return true;
        }
        return false;
    }

    protected void requeue(Throwable throwable, PersistedMessageBO message) throws Exception {
        Integer retryCount = message.getRetryCount();
        message.setQueueStatus(KSBConstants.ROUTE_QUEUE_QUEUED);
        long addMilliseconds = Math.round(getTimeIncrement() * Math.pow(2, retryCount));
        Timestamp currentTime = message.getQueueDate();
        Timestamp newTime = new Timestamp(currentTime.getTime() + addMilliseconds);
        message.setQueueStatus(KSBConstants.ROUTE_QUEUE_QUEUED);
        message.setRetryCount(new Integer(retryCount + 1));
        message.setQueueDate(newTime);
        scheduleExecution(throwable, message);
    }

    protected void placeInException(Throwable throwable, PersistedMessageBO message) throws Exception {
        message.setQueueStatus(KSBConstants.ROUTE_QUEUE_EXCEPTION);
        message.setQueueDate(new Timestamp(System.currentTimeMillis()));
        KSBServiceLocator.getMessageQueueService().save(message);
    }

    protected void scheduleExecution(Throwable throwable, PersistedMessageBO message) throws Exception {
        KSBServiceLocator.getExceptionRoutingService().scheduleExecution(throwable, message, null);
    }

    public Integer getMaxRetryAttempts() {
        try {
            return new Integer(ConfigContext.getCurrentContextConfig().getProperty(KSBConstants.Config.ROUTE_QUEUE_MAX_RETRY_ATTEMPTS_KEY));
        } catch (NumberFormatException e) {
            LOG.error("Constant '" + KSBConstants.Config.ROUTE_QUEUE_MAX_RETRY_ATTEMPTS_KEY + "' is not a number and is being " + "used as a default for exception messages.  " + DEFAULT_MAX_RETRIES + " will be used as a retry limit until this number is fixed");
            return DEFAULT_MAX_RETRIES;
        }
    }

    public Integer getGlobalMaxRetryAttempts() {
        String globalMax = ConfigContext.getCurrentContextConfig().getProperty(KSBConstants.Config.ROUTE_QUEUE_MAX_RETRY_ATTEMPTS_OVERRIDE_KEY);
        if (StringUtils.isBlank(globalMax)) {
            return null;
        }
        try {
            Integer globalMaxRetries = new Integer(globalMax);
            if (globalMaxRetries >= 0) {
                return globalMaxRetries;
            }
        } catch (NumberFormatException e) {
            LOG.error("Constant '" + KSBConstants.Config.ROUTE_QUEUE_MAX_RETRY_ATTEMPTS_OVERRIDE_KEY + "' is not a number and is being " + "used as a default for exception messages.  " + DEFAULT_MAX_RETRIES + " will be used as a retry limit until this number is fixed");
        }
        return null;
    }

    public Long getTimeIncrement() {
        try {
            return new Long(ConfigContext.getCurrentContextConfig().getProperty(KSBConstants.Config.ROUTE_QUEUE_TIME_INCREMENT_KEY));
        } catch (NumberFormatException e) {
            LOG.error("Constant '" + KSBConstants.Config.ROUTE_QUEUE_TIME_INCREMENT_KEY + "' is not a number and will not be used " + "as the default time increment for exception routing.  Default of " + DEFAULT_TIME_INCREMENT + " will be used.");
            return DEFAULT_TIME_INCREMENT;
        }
    }

    public Boolean getImmediateExceptionRouting() {
        return new Boolean(ConfigContext.getCurrentContextConfig().getProperty(KSBConstants.Config.IMMEDIATE_EXCEPTION_ROUTING));
    }
}
