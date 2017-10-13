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
import org.kuali.rice.ken.dao.NotificationDao;
import org.kuali.rice.ken.dao.NotificationMessegeDeliveryDao;
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
import org.springframework.beans.factory.BeanFactory;

/**
 * NotificationServiceLocator backed by a Spring Bean Factory - responsible for returning instances of services instantiated by the Spring context loader.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SpringNotificationServiceLocator implements NotificationServiceLocator {
    // Spring bean names
    private static final String KENAPI_SERVICE = "kenApiService";
    private static final String NOTIFICATION_SERVICE = "notificationService";
    private static final String NOTIFICATION_CONTENT_TYPE_SERVICE = "notificationContentTypeService";
    private static final String MESSAGE_CONTENT_SERVICE = "messageContentService";
    private static final String GENERIC_DAO = "kenGenericDao";
    private static final String NOTIFICATION_DAO = "kenNotificationDao";
    private static final String NOTIFICATION_MESSEGE_DELIVERY_DAO = "kenNotificationMessegeDeliveryDao";
    
    private static final String NOTIFICATION_AUTHORIZATION_SERVICE = "notificationAuthorizationService";
    private static final String NOTIFICATION_WORKFLOW_DOCUMENT_SERVICE = "notificationWorkflowDocumentService";
    private static final String NOTIFICATION_MESSAGE_DELIVERY_DISPATCH_SERVICE = "notificationMessageDeliveryDispatchService";
    private static final String NOTIFICATION_MESSAGE_DELIVERY_RESOLVER_SERVICE = "notificationMessageDeliveryResolverService";
    private static final String NOTIFICATION_MESSAGE_DELIVERY_AUTOREMOVAL_SERVICE = "notificationMessageDeliveryAutoRemovalService";
    private static final String NOTIFICATION_RECIPIENT_SERVICE = "notificationRecipientService";
    private static final String NOTIFICATION_MESSAGE_DELIVERY_SERVICE = "notificationMessageDeliveryService";
    private static final String NOTIFICATION_MESSAGE_DELIVERER_REGISTRY_SERVICE = "notificationMessageDelivererRegistryService";
    private static final String USER_PREFERENCE_SERVICE = "userPreferenceService";
    private static final String NOTIFICATION_CHANNEL_SERVICE = "notificationChannelService";
    private static final String NOTIFICATION_EMAIL_SERVICE = "notificationEmailService";
    private static final String NOTIFICATION_CONFIG = "notificationConfig";
    private static final String NOTIFICATION_SCHEDULER = "notificationScheduler";

    private BeanFactory beanFactory;

    /**
     * Constructs a SpringNotificationServiceLocator.java.
     * @param beanFactory
     */
    public SpringNotificationServiceLocator(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
    
    public KENAPIService getKENAPIService() {
        return (KENAPIService) beanFactory.getBean(KENAPI_SERVICE);
    }


    /**
     * @see org.kuali.rice.ken.core.NotificationServiceLocator#getNotificationService()
     */
    public NotificationService getNotificationService() {
        return (NotificationService) beanFactory.getBean(NOTIFICATION_SERVICE);
    }

    /**
     * @see org.kuali.rice.ken.core.NotificationServiceLocator#getNotificationContentTypeService()
     */
    public NotificationContentTypeService getNotificationContentTypeService() {
        return (NotificationContentTypeService) beanFactory.getBean(NOTIFICATION_CONTENT_TYPE_SERVICE);
    }

    /**
     * @see org.kuali.rice.ken.core.NotificationServiceLocator#getNotificationMessageContentService()
     */
    public NotificationMessageContentService getNotificationMessageContentService() {
        return (NotificationMessageContentService) beanFactory.getBean(MESSAGE_CONTENT_SERVICE);
    }

    /**
     * @see org.kuali.rice.ken.core.NotificationServiceLocator#getGenericDao()
     */
    public GenericDao getGenericDao() {
        return (GenericDao) beanFactory.getBean(GENERIC_DAO);
    }
    
    public NotificationDao getNotificationDao() {
        return (NotificationDao) beanFactory.getBean(NOTIFICATION_DAO);
    }
    
    public NotificationMessegeDeliveryDao getNotificationMessegDeliveryDao() {
        return (NotificationMessegeDeliveryDao) beanFactory.getBean(NOTIFICATION_MESSEGE_DELIVERY_DAO);
    }

    /**
     * @see org.kuali.rice.ken.core.NotificationServiceLocator#getNotificationAuthorizationService()
     */
    public NotificationAuthorizationService getNotificationAuthorizationService() {
        return (NotificationAuthorizationService) beanFactory.getBean(NOTIFICATION_AUTHORIZATION_SERVICE);
    }
    
    /**
     * @see org.kuali.rice.ken.core.NotificationServiceLocator#getNotificationWorkflowDocumentService()
     */
    public NotificationWorkflowDocumentService getNotificationWorkflowDocumentService() {
        return (NotificationWorkflowDocumentService) beanFactory.getBean(NOTIFICATION_WORKFLOW_DOCUMENT_SERVICE);
    }
    
    /**
     * @see org.kuali.rice.ken.core.NotificationServiceLocator#getNotificationMessageDeliveryAutoRemovalService()
     */
    public NotificationMessageDeliveryAutoRemovalService getNotificationMessageDeliveryAutoRemovalService() {
        return (NotificationMessageDeliveryAutoRemovalService) beanFactory.getBean(NOTIFICATION_MESSAGE_DELIVERY_AUTOREMOVAL_SERVICE);
    }

    /**
     * @see org.kuali.rice.ken.core.NotificationServiceLocator#getNotificationMessageDeliveryResolverService()
     */
    public NotificationMessageDeliveryResolverService getNotificationMessageDeliveryResolverService() {
        return (NotificationMessageDeliveryResolverService) beanFactory.getBean(NOTIFICATION_MESSAGE_DELIVERY_RESOLVER_SERVICE);
    }
    
    /**
     * @see org.kuali.rice.ken.core.NotificationServiceLocator#getNotificationRecipientService()
     */
    public NotificationRecipientService getNotificationRecipientService() {
        return (NotificationRecipientService) beanFactory.getBean(NOTIFICATION_RECIPIENT_SERVICE);
    }
    
    /**
     * @see org.kuali.rice.ken.core.NotificationServiceLocator#getNotificationMessageDeliveryService()
     */
    public NotificationMessageDeliveryService getNotificationMessageDeliveryService() {
        return (NotificationMessageDeliveryService) beanFactory.getBean(NOTIFICATION_MESSAGE_DELIVERY_SERVICE);
    }
    
    /**
     * @see org.kuali.rice.ken.core.NotificationServiceLocator#getUserPreferenceService()
     */
    public UserPreferenceService getUserPreferenceService() {
        return (UserPreferenceService) beanFactory.getBean(USER_PREFERENCE_SERVICE);
    }
    
    /**
     * @see org.kuali.rice.ken.core.NotificationServiceLocator#getNotificationChannelService()
     */
    public NotificationChannelService getNotificationChannelService() {
        return (NotificationChannelService) beanFactory.getBean(NOTIFICATION_CHANNEL_SERVICE);
    }

    /**
     * @see org.kuali.rice.ken.core.NotificationServiceLocator#getScheduler()
     */
    public Scheduler getScheduler() {
        return (Scheduler) beanFactory.getBean(NOTIFICATION_SCHEDULER);
    }
}
