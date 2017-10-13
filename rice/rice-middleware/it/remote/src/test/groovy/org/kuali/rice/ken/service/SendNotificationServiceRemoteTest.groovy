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
package org.kuali.rice.ken.service;

import org.junit.After;


import org.kuali.rice.test.remote.RemoteTestHarness

import org.kuali.rice.ken.service.impl.SendNotificationServiceKewXmlImpl
import groovy.mock.interceptor.MockFor

import org.junit.Test
import org.kuali.rice.ken.api.service.SendNotificationService

import org.kuali.rice.ken.bo.NotificationResponseBo;

/**
 * Tests invoking the SendNotificationService via SOAP
 */
class SendNotificationServiceRemoteTest {

    RemoteTestHarness harness = new RemoteTestHarness();

    @Test
    void testSendNotificationServiceInvocation() {
        def mockNotificationService = new MockFor(NotificationService.class)
        mockNotificationService.demand.sendNotification(1) { new NotificationResponseBo() }
        def svc = new SendNotificationServiceKewXmlImpl(mockNotificationService.proxyDelegateInstance())
        def sendNotificationService = harness.publishEndpointAndReturnProxy(SendNotificationService.class, svc);
        sendNotificationService.invoke("notification XML")
    }

    @After
    public void unPublishEndpoint() {
        harness.stopEndpoint();
    }
}
