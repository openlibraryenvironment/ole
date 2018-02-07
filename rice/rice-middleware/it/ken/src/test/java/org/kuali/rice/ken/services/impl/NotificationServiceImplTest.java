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

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.kuali.rice.core.api.util.xml.XmlException;
import org.kuali.rice.ken.bo.NotificationBo;
import org.kuali.rice.ken.bo.NotificationMessageDelivery;
import org.kuali.rice.ken.bo.NotificationResponseBo;
import org.kuali.rice.ken.service.NotificationMessageDeliveryService;
import org.kuali.rice.ken.service.NotificationService;
import org.kuali.rice.ken.service.ProcessingResult;
import org.kuali.rice.ken.test.KENTestCase;
import org.kuali.rice.ken.test.TestConstants;
import org.kuali.rice.ken.util.NotificationConstants;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.test.BaselineTestCase;
import org.quartz.SchedulerException;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * This class tests the notification service impl.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
// this whole test case is suspect
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.CLEAR_DB)
public class NotificationServiceImplTest extends KENTestCase {

    public NotificationServiceImplTest() {
    }

    @Test
    public void testGetNotification_validNotification() {
        NotificationService nSvc = services.getNotificationService();

        NotificationBo notification = nSvc.getNotification(TestConstants.NOTIFICATION_1);

        assertNotNull(notification.getContent());
        assertTrue(notification.getDeliveryType().equals(TestConstants.NOTIFICATION_1_DELIVERY_TYPE));
    }

    @Test
    public void testGetNotification_nonExistentNotification() {
        NotificationService nSvc = services.getNotificationService();

        NotificationBo notification = nSvc.getNotification(TestConstants.NON_EXISTENT_ID);

        assertNull(notification);
    }

    @Test
    public void testGetNotificationsForRecipientByType_validInput() {
        NotificationService nSvc = services.getNotificationService();

        assertTrue(nSvc.getNotificationsForRecipientByType(TestConstants.NOTIFICATION_RECIPIENT_CONTENT_TYPE, TestConstants.NOTIFICATION_RECIPIENT_ID).size() > 0);
    }

    @Test
    public void testGetNotificationsForRecipientByType_invalidInput() {
        NotificationService nSvc = services.getNotificationService();

        assertTrue(nSvc.getNotificationsForRecipientByType(TestConstants.INVALID_CONTENT_TYPE, TestConstants.NOTIFICATION_RECIPIENT_ID).size() == 0);
    }

    @Test
    // deadlocks
    public void testSendNotificationAsXml_validInput() throws InterruptedException, SchedulerException, IOException, XmlException  {
        services.getNotificationMessageDeliveryResolverService().resolveNotificationMessageDeliveries();
        
        Thread.sleep(10000);
        services.getNotificationMessageDeliveryAutoRemovalService().processAutoRemovalOfDeliveredNotificationMessageDeliveries();

        // get the count of pending action requests
        DocumentType docType = KEWServiceLocator.getDocumentTypeService().findByName("KualiNotification");
        List<ActionRequestValue> list = KEWServiceLocator.getActionRequestService().findPendingRootRequestsByDocumentType(docType.getDocumentTypeId());
        int count_before = list.size();
        LOG.info("ActionRequests: " + count_before);
        for (ActionRequestValue v: list) {
            LOG.info("Root request: " + v.getActionRequested() + " " + v.getPrincipalId() + " " + v.getStatus() + " " + v.getRoleName());
        }

        // now send ours
        final NotificationService nSvc = services.getNotificationService();

        final String notificationMessageAsXml = IOUtils.toString(getClass().getResourceAsStream("valid_input.xml"));

        Map map = new HashMap();
        map.put(NotificationConstants.BO_PROPERTY_NAMES.PROCESSING_FLAG, NotificationConstants.PROCESSING_FLAGS.UNRESOLVED);
        Collection<NotificationBo> notifications = services.getGenericDao().findMatching(NotificationBo.class, map);
        assertEquals(0, notifications.size());
        final String[] result = new String[1];

        NotificationResponseBo response = nSvc.sendNotification(notificationMessageAsXml);

        LOG.info("response XML: " + response);
        assertEquals(NotificationConstants.RESPONSE_STATUSES.SUCCESS, response.getStatus());
        notifications = services.getGenericDao().findMatching(NotificationBo.class, map);
        assertEquals(1, notifications.size());
        LOG.info("Notification: " + notifications.iterator().next());

        services.getNotificationMessageDeliveryResolverService().resolveNotificationMessageDeliveries();
        services.getNotificationMessageDeliveryAutoRemovalService().processAutoRemovalOfDeliveredNotificationMessageDeliveries();

        list = KEWServiceLocator.getActionRequestService().findPendingRootRequestsByDocumentType(docType.getDocumentTypeId());
        int count_after = list.size();
        LOG.info("ActionRequests before: " + count_before);
        LOG.info("ActionRequests after: " + count_after);
        for (ActionRequestValue v: list) {
            LOG.info("Root request: " + v.getActionRequested() + " " + v.getPrincipalId() + " " + v.getStatus() + " " + v.getRoleName());
        }

        // should have 6 requests, 1 to each user in in Rice Team group
        assertEquals(6, count_after - count_before);
    }

    @Test
    public void testSendNotificationAsXml_invalidInput() throws IOException {
        NotificationService nSvc = services.getNotificationService();

        final String notificationMessageAsXml = IOUtils.toString(getClass().getResourceAsStream("invalid_input.xml"));

        try {
            nSvc.sendNotification(notificationMessageAsXml);
            fail("XmlException not thrown");
        } catch (IOException ioe) {
            fail("Wrong exception thrown, expected XmlException: " + ioe);
        } catch (XmlException ixe) {
            // expected
        } catch (Exception e) {
            fail("Wrong exception thrown, expected XmlException: " + e);
        }

    }

    @Test
    public void testSendNotificationAsXml_producerNotAuthorized() throws IOException, XmlException {
        NotificationService nSvc = services.getNotificationService();

        final String notificationMessageAsXml = IOUtils.toString(getClass().getResourceAsStream("producer_not_authorized.xml"));

        NotificationResponseBo response = nSvc.sendNotification(notificationMessageAsXml);
        assertEquals(NotificationConstants.RESPONSE_STATUSES.FAILURE, response.getStatus());
        assertEquals(NotificationConstants.RESPONSE_MESSAGES.PRODUCER_NOT_AUTHORIZED_FOR_CHANNEL, response.getMessage());
    }

    /**
     * Tests that dismissing a single message delivery for a given user, also removes all other message deliveries for that
     * user that were generated for the same notification 
     * @throws InterruptedException
     */
    @Test
    public void testDismiss() throws InterruptedException {
        // first check that the right amount of deliveries are present in the test data
        NotificationBo n = services.getNotificationService().getNotification(TestConstants.NOTIFICATION_1);
        NotificationMessageDeliveryService nmds = services.getNotificationMessageDeliveryService();
        Collection<NotificationMessageDelivery> deliveries = nmds.getNotificationMessageDeliveries(n, TestConstants.TEST_USER_FIVE);
        assertNotNull(deliveries);
        assertEquals(TestConstants.NUM_OF_MSG_DELIVS_FOR_NOTIF_1_TEST_USER_5, deliveries.size());
        // make sure they are not yet delivered
        for (NotificationMessageDelivery delivery: deliveries) {
            assertEquals(NotificationConstants.MESSAGE_DELIVERY_STATUS.UNDELIVERED, delivery.getMessageDeliveryStatus());
        }

        NotificationService nSvc = services.getNotificationService();

        // go ahead and dispatch the message deliveries...
        ProcessingResult result = services.getNotificationMessageDeliveryResolverService().resolveNotificationMessageDeliveries();

        deliveries = nmds.getNotificationMessageDeliveries(n, TestConstants.TEST_USER_FIVE);
        assertNotNull(deliveries);
        assertEquals(TestConstants.NUM_OF_MSG_DELIVS_FOR_NOTIF_1_TEST_USER_5, deliveries.size());
        // they should be delivered now
        for (NotificationMessageDelivery delivery: deliveries) {
            if (delivery.getId().equals(TestConstants.BAD_MESSAGE_DELIVERY_ID)) {
                assertEquals("Message Delivery #" + delivery.getId() + "was not delivered", NotificationConstants.MESSAGE_DELIVERY_STATUS.DELIVERED, delivery.getMessageDeliveryStatus());
            }
        }

        String action;
        if (NotificationConstants.DELIVERY_TYPES.FYI.equals(TestConstants.NOTIFICATION_1_DELIVERY_TYPE)) {
            action = "fyi";
        } else if (NotificationConstants.DELIVERY_TYPES.ACK.equals(TestConstants.NOTIFICATION_1_DELIVERY_TYPE)) {
            action = "ack";
        } else {
            throw new RuntimeException("A new delivery type was defined...this test needs to be updated");
        }
        nSvc.dismissNotificationMessageDelivery(TestConstants.NOT_MSG_DELIV_NOTIF_1_TEST_USER_5, TestConstants.TEST_USER_FIVE, action);

        // after dismissing ONE message delivery, they should ALL be dismissed/removed
        deliveries = nmds.getNotificationMessageDeliveries(n, TestConstants.TEST_USER_FIVE);
        assertNotNull(deliveries);
        assertEquals(TestConstants.NUM_OF_MSG_DELIVS_FOR_NOTIF_1_TEST_USER_5, deliveries.size());
        for (NotificationMessageDelivery delivery: deliveries) {
            if (delivery.getId() != TestConstants.BAD_MESSAGE_DELIVERY_ID) {
                assertEquals("Message Delivery #" + delivery.getId() + "was not removed", NotificationConstants.MESSAGE_DELIVERY_STATUS.REMOVED, delivery.getMessageDeliveryStatus());
            }
        }

    }

}
