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
package org.kuali.rice.kcb.service;

import org.kuali.rice.core.framework.persistence.dao.GenericDao;
import org.kuali.rice.kcb.api.service.MessagingService;
import org.quartz.JobDetail;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Service locator interface for the KCB module.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface KCBServiceLocator {
    public GenericDao getKcbGenericDao();
    public JobDetail getMessageProcessingJobDetail();
    public PlatformTransactionManager getTransactionManager();
    public MessageDeliveryService getMessageDeliveryService();
    public MessageService getMessageService();
    public MessagingService getMessagingService();
    public MessageDelivererRegistryService getMessageDelivererRegistryService();
    public EmailService getEmailService();
    public RecipientPreferenceService getRecipientPreferenceService();
    public KENIntegrationService getKenIntegrationService();
}
