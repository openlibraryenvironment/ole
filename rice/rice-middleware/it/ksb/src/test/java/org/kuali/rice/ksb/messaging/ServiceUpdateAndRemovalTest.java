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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.api.bus.ServiceBus;
import org.kuali.rice.ksb.api.bus.support.JavaServiceDefinition;
import org.kuali.rice.ksb.api.registry.ServiceInfo;
import org.kuali.rice.ksb.api.registry.ServiceRegistry;
import org.kuali.rice.ksb.messaging.remotedservices.TestRepeatMessageQueue;
import org.kuali.rice.ksb.test.KSBTestCase;

/**
 * This test ensures that ServiceInfo and ServiceDefinition instances are being modified and removed correctly. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ServiceUpdateAndRemovalTest extends KSBTestCase {

	/**
	 * Tests the removeLocallyPublishedServices() method of the service registry to ensure that the local services are being deleted properly.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRemovalOfAllLocalServices() throws Exception {
		ServiceBus serviceBus = KsbApiServiceLocator.getServiceBus();
		ServiceRegistry serviceRegistry = KsbApiServiceLocator.getServiceRegistry();
		List<ServiceInfo> serviceInfos = findLocallyPublishedServices(serviceBus.getInstanceId(), serviceRegistry);
		assertFalse("There should be at least one locally published service in the database.", serviceInfos.isEmpty());
		serviceRegistry.takeInstanceOffline(serviceBus.getInstanceId());
		serviceInfos = findLocallyPublishedServices(serviceBus.getInstanceId(), serviceRegistry);
		assertEquals("There should not be any locally published services in the database.", 0, serviceInfos.size());
	}
	
	private List<ServiceInfo> findLocallyPublishedServices(String instanceId, ServiceRegistry serviceRegistry) {
		List<ServiceInfo> locallyPublishedServices = new ArrayList<ServiceInfo>();
		List<ServiceInfo> serviceInfos = serviceRegistry.getAllOnlineServices();
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (serviceInfo.getInstanceId().equals(instanceId)) {
				locallyPublishedServices.add(serviceInfo);
			}
		}
		return locallyPublishedServices;
	}
	
	private ServiceInfo findLocallyPublishedService(String instanceId, QName serviceName, ServiceRegistry serviceRegistry) {
		List<ServiceInfo> locallyPublishedServices = findLocallyPublishedServices(instanceId, serviceRegistry);
		for (ServiceInfo serviceInfo : locallyPublishedServices) {
			if (serviceInfo.getServiceName().equals(serviceName)) {
				return serviceInfo;
			}
		}
		return null;
	}
	
	/**
	 * Tests the deployment and modification of local services to ensure that they are being updated accordingly.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testModificationOfLocalServices() throws Exception {
		ServiceBus serviceBus = KsbApiServiceLocator.getServiceBus();
		ServiceRegistry serviceRegistry = KsbApiServiceLocator.getServiceRegistry();
		QName serviceName = new QName("KEW", "serviceForTestingModifications");
		ServiceInfo regularInfo = null;
		// Create and deploy a simple test service.
		JavaServiceDefinition serviceDefinition = new JavaServiceDefinition();
		serviceDefinition.setServiceName(serviceName);
		serviceDefinition.setPriority(4);
		serviceDefinition.setService(new TestRepeatMessageQueue());
		serviceDefinition.validate();
		serviceBus.publishService(serviceDefinition, true);
		// Retrieve the ServiceInfo for the original service and the ServiceInfo for the related forward.
		regularInfo = findLocallyPublishedService(serviceBus.getInstanceId(), serviceName, serviceRegistry);
		// Ensure that refreshing the local registry without modifying the ServiceDefinition yields the expected results.
		assertRegistryRefreshHasExpectedResults(serviceBus, serviceRegistry, regularInfo, serviceName, false);
		// Ensure that refreshing the local registry after modifying the ServiceDefinition yields the expected results.
		regularInfo = findLocallyPublishedService(serviceBus.getInstanceId(), serviceName, serviceRegistry);
		serviceDefinition.setPriority(3);
		serviceDefinition.validate();
		assertRegistryRefreshHasExpectedResults(serviceBus, serviceRegistry, regularInfo, serviceName, true);
	}
	
	/**
	 * A convenience method for asserting that expected similarities and differences should have occurred after refreshing the registry. This includes
	 * comparing the checksums and ensuring that non-similar checksums are corresponding to modifications to the serialized and non-serialized
	 * ServiceDefinition instances.
	 * 
	 * @param remotedServiceRegistry The service registry to test with.
	 * @param regularInfo The ServiceInfo containing the configured service.
	 * @param forwardInfo The ServiceInfo containing the ForwardedCallHandler for the configured service.
	 * @param serviceName The QName of the configured service.
	 * @param forwardServiceName The QName of the ForwardedCallHandler for the configured service.
	 * @param serviceDefinitionsShouldDiffer A flag indicating if the service definitions should be tested for similarity or difference after the refresh.
	 * @throws Exception
	 */
	private void assertRegistryRefreshHasExpectedResults(ServiceBus serviceBus, ServiceRegistry serviceRegistry, ServiceInfo regularInfo,
			QName serviceName, boolean serviceDefinitionsShouldDiffer) throws Exception {
		// Sync the bus
		serviceBus.synchronize();
		ServiceInfo newRegularInfo = findLocallyPublishedService(regularInfo.getInstanceId(), serviceName, serviceRegistry);
		// Perform the assertions that should have the same outcome regardless of whether or not a ServiceDefinition was modified.
		assertTrue("The ServiceInfo instances for the service should satisy the non-ServiceDefinition part of an isSame() check",
				regularInfo.getStatus().equals(newRegularInfo.getStatus()) && regularInfo.getServiceName().equals(newRegularInfo.getServiceName()) &&
						regularInfo.getServerIpAddress().equals(newRegularInfo.getServerIpAddress()) &&
								regularInfo.getApplicationId().equals(newRegularInfo.getApplicationId()));
		// Perform the appropriate assertions based on whether or not any updates are expected.
		if (serviceDefinitionsShouldDiffer) {
			assertNotSame("The checksum for the configured service should have been modified after refreshing the registry.",
					regularInfo.getChecksum(), newRegularInfo.getChecksum());
		} else {
			assertEquals("The checksum for the configured service should not have been modified after refreshing the registry.",
					regularInfo.getChecksum(), newRegularInfo.getChecksum());
		}
	}
}
