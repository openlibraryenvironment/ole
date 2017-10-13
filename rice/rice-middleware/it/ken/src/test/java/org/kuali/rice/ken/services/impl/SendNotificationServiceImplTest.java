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
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.xml.XmlException;
import org.kuali.rice.ken.api.KenApiConstants;
import org.kuali.rice.ken.api.notification.Notification;
import org.kuali.rice.ken.api.notification.NotificationResponse;
import org.kuali.rice.ken.api.service.SendNotificationService;
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
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.test.BaselineTestCase;
import org.quartz.SchedulerException;

import javax.xml.namespace.QName;
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
public class SendNotificationServiceImplTest extends KENTestCase {

    public SendNotificationServiceImplTest() {
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
        //final NotificationService nSvc = services.getNotificationService();
        final SendNotificationService sendNotificationService = (SendNotificationService) GlobalResourceLoader.getService(new QName(
                KenApiConstants.Namespaces.KEN_NAMESPACE_2_0, "sendNotificationService"));
        
        final String notificationMessageAsXml = IOUtils.toString(getClass().getResourceAsStream("valid_input.xml"));

        Map map = new HashMap();
        map.put(NotificationConstants.BO_PROPERTY_NAMES.PROCESSING_FLAG, NotificationConstants.PROCESSING_FLAGS.UNRESOLVED);
        Collection<NotificationBo> notifications = services.getGenericDao().findMatching(NotificationBo.class, map);
        assertEquals(0, notifications.size());
        final String[] result = new String[1];

        NotificationResponse response = sendNotificationService.invoke(notificationMessageAsXml);

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
        SendNotificationService sendNotificationService = (SendNotificationService) GlobalResourceLoader.getService(new QName(
                KenApiConstants.Namespaces.KEN_NAMESPACE_2_0, "sendNotificationService"));

        final String notificationMessageAsXml = IOUtils.toString(getClass().getResourceAsStream("invalid_input.xml"));

        try {
            sendNotificationService.invoke(notificationMessageAsXml);
            fail("XmlException not thrown");
        } catch (XmlException ixe) {
            // expected
        } catch (WorkflowRuntimeException wre) {
            // expected
        } catch (Exception e) {
            fail("Wrong exception thrown, expected XmlException: " + e);
        }

    }

    @Test
    public void testSendNotificationAsXml_producerNotAuthorized() throws IOException, XmlException {
        SendNotificationService sendNotificationService = (SendNotificationService) GlobalResourceLoader.getService(new QName(
                KenApiConstants.Namespaces.KEN_NAMESPACE_2_0, "sendNotificationService"));

        final String notificationMessageAsXml = IOUtils.toString(getClass().getResourceAsStream("producer_not_authorized.xml"));

        NotificationResponse response = sendNotificationService.invoke(notificationMessageAsXml);
        assertEquals(NotificationConstants.RESPONSE_STATUSES.FAILURE, response.getStatus());
        assertEquals(NotificationConstants.RESPONSE_MESSAGES.PRODUCER_NOT_AUTHORIZED_FOR_CHANNEL, response.getMessage());
    }


    @Test
    // deadlocks
    public void testSendNotificationAsBo_validInput() throws InterruptedException, SchedulerException, IOException, XmlException  {
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
        //final NotificationService nSvc = services.getNotificationService();
        final SendNotificationService sendNotificationService = (SendNotificationService) GlobalResourceLoader.getService(new QName(
                KenApiConstants.Namespaces.KEN_NAMESPACE_2_0, "sendNotificationService"));

        final String notificationMessageAsXml = IOUtils.toString(getClass().getResourceAsStream("valid_input.xml"));
        NotificationBo notificationBo = services.getNotificationMessageContentService().parseNotificationRequestMessage(
                notificationMessageAsXml);
        Map map = new HashMap();
        map.put(NotificationConstants.BO_PROPERTY_NAMES.PROCESSING_FLAG, NotificationConstants.PROCESSING_FLAGS.UNRESOLVED);
        Collection<NotificationBo> notifications = services.getGenericDao().findMatching(NotificationBo.class, map);
        assertEquals(0, notifications.size());
        final String[] result = new String[1];
        Notification notificiation = NotificationBo.to(notificationBo);
        NotificationResponse response = sendNotificationService.sendNotification(notificiation);

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
}
