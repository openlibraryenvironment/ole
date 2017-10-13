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
import org.kuali.rice.ken.service.NotificationMessageDeliveryService;
import org.kuali.rice.ken.test.KENTestCase;
import org.kuali.rice.ken.test.TestConstants;
import org.kuali.rice.test.BaselineTestCase;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * This class tests the message delivery service implementation
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.CLEAR_DB)
public class NotificationMessageDeliveryServiceImplTest extends KENTestCase {

    @Test
    public void testGetNotificationMessageDelivery_validId() {
        NotificationMessageDeliveryService nSvc = services.getNotificationMessageDeliveryService();

        NotificationMessageDelivery nmd = nSvc.getNotificationMessageDelivery(TestConstants.VALID_MESSAGE_DELIVERY_ID);

        assertNotNull(nmd.getMessageDeliveryStatus());
    }

    @Test
    public void testGetNotification_nonExistentNotification() {
        NotificationMessageDeliveryService nSvc = services.getNotificationMessageDeliveryService();

        NotificationMessageDelivery nmd = nSvc.getNotificationMessageDelivery(TestConstants.NON_EXISTENT_ID);

        assertNull(nmd);
    }

    @Test
    public void testGetAllNotificationMessageDeliveries() {
        NotificationMessageDeliveryService nSvc = services.getNotificationMessageDeliveryService();
        Collection<NotificationMessageDelivery> deliveries = nSvc.getNotificationMessageDeliveries();
        assertNotNull(deliveries);
        assertEquals(TestConstants.NUM_OF_MSG_DELIVS_IN_TEST_DATA, deliveries.size());
    }

    @Test
    public void testGetSpecificNotificationMessageDeliveries() {
        NotificationBo n = services.getNotificationService().getNotification(TestConstants.NOTIFICATION_1);
        NotificationMessageDeliveryService nSvc = services.getNotificationMessageDeliveryService();
        Collection<NotificationMessageDelivery> deliveries = nSvc.getNotificationMessageDeliveries(n, TestConstants.TEST_USER_FIVE);
        assertNotNull(deliveries);
        assertEquals(TestConstants.NUM_OF_MSG_DELIVS_FOR_NOTIF_1_TEST_USER_5, deliveries.size());
    }
}
