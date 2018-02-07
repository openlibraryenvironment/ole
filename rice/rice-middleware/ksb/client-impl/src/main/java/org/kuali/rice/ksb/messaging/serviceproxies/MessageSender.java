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

import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.ksb.messaging.MessageServiceInvoker;
import org.kuali.rice.ksb.messaging.PersistedMessageBO;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.kuali.rice.ksb.util.KSBConstants;
import org.springframework.transaction.support.TransactionSynchronizationManager;


/**
 * Responsible for implementing policy to put message into threadpool for execution appropriately. Could make Spring and
 * overridable service.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public class MessageSender {

    public static void sendMessage(PersistedMessageBO message) throws Exception {
        if (!Boolean.valueOf(ConfigContext.getCurrentContextConfig().getProperty(KSBConstants.Config.MESSAGING_OFF))) {

            if (ConfigContext.getCurrentContextConfig().getObject(RiceConstants.SPRING_TRANSACTION_MANAGER) != null
                    || ConfigContext.getCurrentContextConfig().getObject(RiceConstants.TRANSACTION_MANAGER_OBJ) != null) {
                if (TransactionSynchronizationManager.isSynchronizationActive()) {
                    TransactionSynchronizationManager.registerSynchronization(new MessageSendingTransactionSynchronization(
                            message));
                } else {
                    KSBServiceLocator.getThreadPool().execute(new MessageServiceInvoker(message));
                }
            } else {
                KSBServiceLocator.getThreadPool().execute(new MessageServiceInvoker(message));
            }
        }
    }

}
