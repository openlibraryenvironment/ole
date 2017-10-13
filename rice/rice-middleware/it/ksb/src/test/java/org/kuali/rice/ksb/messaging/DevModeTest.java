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
package org.kuali.rice.ksb.messaging;

import static org.junit.Assert.assertTrue;

import javax.xml.namespace.QName;

import junit.framework.Assert;

import org.junit.Test;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.api.bus.ServiceBus;
import org.kuali.rice.ksb.messaging.remotedservices.GenericTestService;
import org.kuali.rice.ksb.messaging.remotedservices.TestServiceInterface;
import org.kuali.rice.ksb.messaging.serviceconnectors.BusLocalConnector;
import org.kuali.rice.ksb.messaging.serviceconnectors.ServiceConnector;
import org.kuali.rice.ksb.messaging.serviceconnectors.ServiceConnectorFactory;
import org.kuali.rice.ksb.test.KSBTestCase;

/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DevModeTest extends KSBTestCase {

    @Override
	public void setUp() throws Exception {
		// included in ksb-test-config.xml
		System.setProperty("a.test.specific.config.location", "classpath:org/kuali/rice/ksb/messaging/dev_mode_config.xml");
		super.setUp();
	}

	@Override
	public void tearDown() throws Exception {
	    try {
	        // included in ksb-test-config.xml
            System.clearProperty("a.test.specific.config.location");
	    } finally {
		    super.tearDown();
	    }
	}

	@Test public void testCallInDevMode() throws Exception {
        //make sure we are actually in dev mode
		Assert.assertEquals(ConfigContext.getCurrentContextConfig().getDevMode(), Boolean.TRUE);

        QName serviceName = new QName("KEW", "testLocalServiceFavoriteCall");
		TestServiceInterface service = (TestServiceInterface) GlobalResourceLoader.getService(serviceName);
		service.invoke();
		assertTrue("No calls to dev defined service", GenericTestService.NUM_CALLS > 0);

		ServiceBus serviceBus = KsbApiServiceLocator.getServiceBus();

		ServiceConnector serviceConnector = ServiceConnectorFactory.getServiceConnector(serviceBus.getLocalEndpoint(serviceName).getServiceConfiguration());
		assertTrue("Not BusLocalConnector", serviceConnector instanceof BusLocalConnector);
		//assertNull("Service in service definition needs to be null for async communications serialization", ((BusLocalConnector)serviceConnector).getServiceConfiguration()().getServiceDefinition().getService());

		service = (TestServiceInterface) KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(serviceName);
		service.invoke();
		assertTrue("No calls to dev defined service", GenericTestService.NUM_CALLS > 1);

		Assert.assertEquals("should be no registered services", KsbApiServiceLocator.getServiceRegistry().getAllServices().size(), 0);
	}
}
