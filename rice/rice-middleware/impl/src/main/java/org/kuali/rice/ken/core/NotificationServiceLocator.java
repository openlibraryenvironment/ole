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
package org.kuali.rice.ken.core;

import org.kuali.rice.core.framework.persistence.dao.GenericDao;
import org.kuali.rice.ken.api.service.KENAPIService;
import org.kuali.rice.ken.service.NotificationAuthorizationService;
import org.kuali.rice.ken.service.NotificationChannelService;
import org.kuali.rice.ken.service.NotificationContentTypeService;
import org.kuali.rice.ken.service.NotificationMessageContentService;
import org.kuali.rice.ken.service.NotificationMessageDeliveryAutoRemovalService;
import org.kuali.rice.ken.service.NotificationMessageDeliveryResolverService;
import org.kuali.rice.ken.service.NotificationMessageDeliveryService;
import org.kuali.rice.ken.service.NotificationRecipientService;
import org.kuali.rice.ken.service.NotificationService;
import org.kuali.rice.ken.service.NotificationWorkflowDocumentService;
import org.kuali.rice.ken.service.UserPreferenceService;
import org.quartz.Scheduler;

/**
 * Interface for obtaining Notification System services
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface NotificationServiceLocator {
    /**
     * This method returns an instance of the Spring configured impl for the KENAPIService.
     * @return KENAPIService
     */
    public KENAPIService getKENAPIService();

    /**
     * This method returns an instance of the Spring configured impl for the NotificationService.
     * @return NotificationService
     */
    public NotificationService getNotificationService();

    /**
     * This method returns an instance of the Spring configured impl for the NotificationContentTypeService.
     * @return NotificationContentTypeService
     */
    public NotificationContentTypeService getNotificationContentTypeService();

    /**
     * This method returns an instance of the Spring configured impl for the NotificationMessageContentService.
     * @return NotificationMessageContentService
     */
    public NotificationMessageContentService getNotificationMessageContentService();
    
    /**
     * This method returns an instance of the Spring configured impl for the BusinessObjectDao.
     * @return GenericDao
     */
    public GenericDao getGenericDao();
    
    /**
     * This method returns an instance of the Spring configured impl for the NotificationAuthorizationService.
     * @return NotificationAuthorizationService
     */
    public NotificationAuthorizationService getNotificationAuthorizationService();
    
    /**
     * This method returns an instance of the Spring configured impl for the NotificationWorkflowDocumentService.
     * @return NotificationWorkflowDocumentService
     */
    public NotificationWorkflowDocumentService getNotificationWorkflowDocumentService();
    
    /**
     * This method returns an instance of the Spring configured impl for the NotificationMessageDeliveryAutoRemovalService.
     * @return NotificationMessageDeliveryAutoRemovalService
     */
    public NotificationMessageDeliveryAutoRemovalService getNotificationMessageDeliveryAutoRemovalService();

    /**
     * This method returns an instance of the Spring configured impl for the getNotificationMessageDeliveryResolverService.
     * @return NotificationMessageDeliveryResolverService
     */
    public NotificationMessageDeliveryResolverService getNotificationMessageDeliveryResolverService();
    
    /**
     * This method returns an instance of the Spring configured impl for the NotificationRecipientService.
     * @return NotificationRecipientService
     */
    public NotificationRecipientService getNotificationRecipientService();
    
    /**
     * This method returns an instance of the Spring configured impl for the NotificationMessageDeliveryService.
     * @return NotificationMessageDeliveryService
     */
    public NotificationMessageDeliveryService getNotificationMessageDeliveryService();
    
    /**
     * This method returns an instance of the Spring configured impl for the UserPreferenceService.
     * @return UserPreferenceService
     */
    public UserPreferenceService getUserPreferenceService();
    
    /**
     * This method returns an instance of the Spring configured impl for the NotificationChannelService.
     * @return NotificationChannelService
     */
    public NotificationChannelService getNotificationChannelService();
    
    /**
     * Returns the Quartz scheduler used by the Notification system
     * @return the Quartz scheduler used by the Notification system
     */
    public Scheduler getScheduler();
}
