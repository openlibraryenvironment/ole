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
package org.kuali.rice.ksb.messaging;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.ksb.messaging.service.MessageQueueService;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.kuali.rice.ksb.util.KSBConstants;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

/**
 * Fetches messages from the db. Marks as 'R'. Gives messages to ThreadPool for execution
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public class MessageFetcher implements Runnable {

    private static final Logger LOG = Logger.getLogger(MessageFetcher.class);

    private Integer maxMessages;
    private Long routeQueueId;

    public MessageFetcher(Integer maxMessages) {
	this.maxMessages = maxMessages;
    }

    public MessageFetcher(Long routeQueueId) {
	this.routeQueueId = routeQueueId;
    }

    public void run() {
    	if (ConfigContext.getCurrentContextConfig().getBooleanProperty(KSBConstants.Config.MESSAGE_PERSISTENCE, false)) {
    		try {
    			requeueDocument();
    			requeueMessages();
    		} catch (Throwable t) {
    			LOG.error("Failed to fetch messages.", t);
    		}
    	}
    }

    private void requeueMessages() {
	if (this.routeQueueId == null) {
	    try {
		for (final PersistedMessageBO message : getRouteQueueService().getNextDocuments(maxMessages)) {
		    markEnrouteAndSaveMessage(message);
		    executeMessage(message);
		}
	    } catch (Throwable t) {
		LOG.error("Failed to fetch or process some messages during requeueMessages", t);
	    }
	}
    }

    private void requeueDocument() {
	try {
	    if (this.routeQueueId != null) {
		PersistedMessageBO message = getRouteQueueService().findByRouteQueueId(this.routeQueueId);
		message.setQueueStatus(KSBConstants.ROUTE_QUEUE_ROUTING);
		getRouteQueueService().save(message);
		executeMessage(message);
	    }
	} catch (Throwable t) {
	    LOG.error("Failed to fetch or process some messages during requeueDocument", t);
	}
    }

    private void executeMessage(PersistedMessageBO message) {
	try {
	    KSBServiceLocator.getThreadPool().execute(new MessageServiceInvoker(message));
	} catch (Throwable t) {
	    LOG.error("Failed to place message " + message + " in thread pool for execution", t);
	}
    }

    private void markEnrouteAndSaveMessage(final PersistedMessageBO message) {
	try {
	    KSBServiceLocator.getTransactionTemplate().execute(new TransactionCallback() {
		public Object doInTransaction(TransactionStatus status) {
		    message.setQueueStatus(KSBConstants.ROUTE_QUEUE_ROUTING);
		    getRouteQueueService().save(message);
		    return null;
		}
	    });
	} catch (Throwable t) {
	    LOG.error("Caught error attempting to mark message " + message + " as R", t);
	}
    }

    private MessageQueueService getRouteQueueService() {
	return KSBServiceLocator.getMessageQueueService();
    }

}
