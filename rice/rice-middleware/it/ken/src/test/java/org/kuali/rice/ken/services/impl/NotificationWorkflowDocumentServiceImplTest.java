/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.ken.services.impl;

import org.junit.Test;
import org.kuali.rice.ken.bo.NotificationBo;
import org.kuali.rice.ken.bo.NotificationMessageDelivery;
import org.kuali.rice.ken.test.KENTestCase;
import org.kuali.rice.ken.test.TestConstants;
import org.kuali.rice.ken.util.NotificationConstants;
import org.kuali.rice.ken.util.Util;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.test.BaselineTestCase;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the notification workflow document service service impl.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.CLEAR_DB)
public class NotificationWorkflowDocumentServiceImplTest extends KENTestCase {

    private static final String CUSTOM_NOTIFICATION_DOC_TYPE = "CustomNotification";

    /**
     * Tests whether the default KEW document type {@code KualiNotification} is used when set in this
     * {@code Notification}.
     *
     * @throws WorkflowException when KEW cannot find the document
     */
    @Test
    public void createAndAdHocRouteNotificationWorkflowDocument_defaultKEW() throws WorkflowException {
        NotificationBo notification = services.getNotificationService().getNotification(TestConstants.NOTIFICATION_1);
        notification.setDocTypeName(NotificationConstants.KEW_CONSTANTS.NOTIFICATION_DOC_TYPE);

        Document document = createNotificationWorkflowDocument(notification);

        assertEquals(NotificationConstants.KEW_CONSTANTS.NOTIFICATION_DOC_TYPE, document.getDocumentTypeName());
    }

    /**
     * Tests whether the default KEW document type {@code KualiNotification} is used when nothing is set in this
     * {@code Notification}.
     *
     * @throws WorkflowException when KEW cannot find the document
     */
    @Test
    public void createAndAdHocRouteNotificationWorkflowDocument_undefinedKEW() throws WorkflowException {
        NotificationBo notification = services.getNotificationService().getNotification(TestConstants.NOTIFICATION_1);

        Document document = createNotificationWorkflowDocument(notification);

        assertEquals(NotificationConstants.KEW_CONSTANTS.NOTIFICATION_DOC_TYPE, document.getDocumentTypeName());
    }

    /**
     * Tests whether the custom KEW document type {@code CustomNotification} is used when set in this
     * {@code Notification}.
     *
     * @throws WorkflowException when KEW cannot find the document
     */
    @Test
    public void createAndAdHocRouteNotificationWorkflowDocument_customKEW() throws WorkflowException {
        NotificationBo notification = services.getNotificationService().getNotification(TestConstants.NOTIFICATION_1);
        notification.setDocTypeName(CUSTOM_NOTIFICATION_DOC_TYPE);

        Document document = createNotificationWorkflowDocument(notification);

        assertEquals(CUSTOM_NOTIFICATION_DOC_TYPE, document.getDocumentTypeName());
    }

    /**
     * Helper method for creating a {@code Notification} workflow document.
     *
     * @param notification the {@code Notification} to include in the workflow document
     *
     * @return a KEW workflow document
     */
    protected Document createNotificationWorkflowDocument(NotificationBo notification) {
        NotificationMessageDelivery messageDelivery = new NotificationMessageDelivery();
        messageDelivery.setId(0L);
        messageDelivery.setMessageDeliveryStatus(NotificationConstants.MESSAGE_DELIVERY_STATUS.UNDELIVERED);
        messageDelivery.setNotification(notification);
        messageDelivery.setUserRecipientId(TestConstants.TEST_USER_FIVE);

        String documentId =
                services.getNotificationWorkflowDocumentService().createAndAdHocRouteNotificationWorkflowDocument(
                        messageDelivery, Util.getNotificationSystemUser(), messageDelivery.getUserRecipientId(),
                        NotificationConstants.KEW_CONSTANTS.GENERIC_DELIVERY_ANNOTATION);
        Document document = KewApiServiceLocator.getWorkflowDocumentService().getDocument(documentId);

        return document;
    }
}
