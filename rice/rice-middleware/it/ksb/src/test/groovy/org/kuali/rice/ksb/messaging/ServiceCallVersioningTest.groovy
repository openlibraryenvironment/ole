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
package org.kuali.rice.ksb.messaging

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


import javax.xml.namespace.QName;


import org.junit.Test;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.ksb.messaging.remotedservices.EchoService;
import org.kuali.rice.ksb.messaging.remotedservices.JaxWsEchoService;


import org.kuali.rice.ksb.test.KSBTestCase

import org.kuali.rice.ksb.messaging.remotedservices.ServiceCallInformationHolder
import org.kuali.rice.core.api.config.property.Config
import org.kuali.rice.ksb.impl.cxf.interceptors.ServiceCallVersioningHelper
import org.kuali.rice.ksb.messaging.remotedservices.BaseballCardCollectionService
import org.kuali.rice.ksb.messaging.remotedservices.BaseballCard

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
class ServiceCallVersioningTest extends KSBTestCase {
    @Override
    public void setUp() throws Exception {
        super.setUp();
        Config c = ConfigContext.getCurrentContextConfig();
        c.putProperty(Config.APPLICATION_NAME, "ServiceCallVersioningTest");
        c.putProperty(Config.APPLICATION_VERSION, "99.99-SNAPSHOT");
    }

    public boolean startClient1() {
        return true;
    }

    private String getClient1Port() {
        return ConfigContext.getCurrentContextConfig().getProperty("ksb.client1.port")
    }

    @Test void testSimpleSOAPService() throws Exception{
        EchoService echoService = (EchoService)GlobalResourceLoader.getService(new QName("TestCl1", "soap-echoService"))
        echoService.captureHeaders()
        assertHeadersCaptured()
    }

    @Test void testJaxWsSOAPService(){
        JaxWsEchoService jaxwsEchoService = (JaxWsEchoService) GlobalResourceLoader.getService(new QName("TestCl1", "jaxwsEchoService"))
        jaxwsEchoService.captureHeaders();
        assertHeadersCaptured();
    }

    @Test void testJaxRsService(){
        BaseballCardCollectionService service = (BaseballCardCollectionService) GlobalResourceLoader.getService(new QName("test", "baseballCardCollectionService"))
        // invoke a method that stores the headers
        List<BaseballCard> allCards = service.getAll();
        assertNotNull(allCards);
        assertHeadersCaptured();
    }

    def void assertHeadersCaptured() {
        Map<String, List<String>> headers = ServiceCallInformationHolder.stuff.get("capturedHeaders")
        System.out.println("HEADERS");
        System.out.println(headers);
        assertTrue(headers.get(ServiceCallVersioningHelper.KUALI_RICE_ENVIRONMENT_HEADER).contains("dev"))
        assertTrue(headers.get(ServiceCallVersioningHelper.KUALI_RICE_VERSION_HEADER).contains(ConfigContext.getCurrentContextConfig().getRiceVersion())) //any { it =~ /2\.0.*/ })
        assertTrue(headers.get(ServiceCallVersioningHelper.KUALI_APP_NAME_HEADER).contains("ServiceCallVersioningTest"))
        assertTrue(headers.get(ServiceCallVersioningHelper.KUALI_APP_VERSION_HEADER).contains("99.99-SNAPSHOT"))
    }
}
