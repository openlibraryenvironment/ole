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
package org.kuali.rice.ksb.messaging.serviceproxies;

import org.apache.log4j.Logger;
import org.kuali.rice.ksb.messaging.MessageServiceInvoker;
import org.kuali.rice.ksb.messaging.PersistedMessageBO;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.springframework.core.Ordered;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Sends message when current transaction commits.  
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class MessageSendingTransactionSynchronization extends TransactionSynchronizationAdapter {
    
    private static final Logger LOG = Logger.getLogger(MessageSendingTransactionSynchronization.class);
    public static final AtomicBoolean CALLED_TRANS_COMMITTED = new AtomicBoolean(false);
    public static final AtomicBoolean CALLED_TRANS_ROLLEDBACKED = new AtomicBoolean(false);

    private final PersistedMessageBO message;
    
    public MessageSendingTransactionSynchronization(PersistedMessageBO message) {
	    this.message = message;
    }

    @Override
    public void afterCompletion(int status) {
        if (status == STATUS_COMMITTED) {
            KSBServiceLocator.getThreadPool().execute(new MessageServiceInvoker(message));
            CALLED_TRANS_COMMITTED.set(true);
        } else {
            LOG.info("Message " + message + " not sent because transaction not committed.");
            CALLED_TRANS_ROLLEDBACKED.set(true);
        }
    }

    @Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
