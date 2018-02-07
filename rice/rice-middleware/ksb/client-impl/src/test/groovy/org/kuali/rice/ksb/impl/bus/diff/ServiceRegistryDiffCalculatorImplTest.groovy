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
package org.kuali.rice.ksb.impl.bus.diff

import groovy.mock.interceptor.MockFor
import javax.xml.namespace.QName
import org.apache.commons.lang.RandomStringUtils
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.kuali.rice.core.api.CoreConstants
import org.kuali.rice.core.api.config.property.ConfigContext
import org.kuali.rice.core.framework.config.property.SimpleConfig
import org.kuali.rice.ksb.api.KsbApiConstants
import org.kuali.rice.ksb.api.bus.support.SoapServiceDefinition
import org.kuali.rice.ksb.api.registry.ServiceEndpointStatus
import org.kuali.rice.ksb.api.registry.ServiceInfo
import org.kuali.rice.ksb.api.registry.ServiceRegistry
import org.kuali.rice.ksb.impl.bus.LocalService
import org.kuali.rice.ksb.impl.bus.RemoteService

class ServiceRegistryDiffCalculatorImplTest {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ServiceRegistryDiffCalculatorImplTest.class)
	private static final String LOCALHOST_IP = "localhost"
	private static final String TEST1_NAMESPACE = "TEST1"
	private static final String TEST1_INSTANCE_ID = "${TEST1_NAMESPACE}-${LOCALHOST_IP}";
	private static final String TEST2_NAMESPACE = "TEST2"
	private static final String TEST2_INSTANCE_ID = "${TEST2_NAMESPACE}-${LOCALHOST_IP}";
	private static final String TEST3_NAMESPACE = "TEST3"
	private static final String TEST3_INSTANCE_ID = "${TEST3_NAMESPACE}-${LOCALHOST_IP}";
	
	private static final def CONFIG_MAP = [(CoreConstants.Config.APPLICATION_ID) : "TEST", (CoreConstants.Config.INSTANCE_ID) : "TEST1"]
	
	int nextServiceId = 1
    ServiceRegistryDiffCalculatorImpl diffCalculatorImpl
    ServiceRegistry serviceRegistryMock

    //importing the shouldFail method since I don't want to extend
    //GroovyTestCase which is junit 3 style
    private final shouldFail = new GroovyTestCase().&shouldFail

    @Before
    void setupServiceUnderTest() {
        diffCalculatorImpl = new ServiceRegistryDiffCalculatorImpl()
        serviceRegistryMock = new MockFor(ServiceRegistry).proxyDelegateInstance()
    }

    @Test
    void testCalculateRemoteServicesDiff_emptyLists() {
        RemoteServicesDiff remoteServicesDiff = diffCalculatorImpl.calculateRemoteServicesDiff([], [])
		assert remoteServicesDiff != null
		assert remoteServicesDiff.getNewServices().isEmpty()
		assert remoteServicesDiff.getRemovedServices().isEmpty()
    }
	
	@Test
	void testCalculateRemoteServicesDiff_emptyClientRegistryCache() {
		
		List registryServices = [
			newServiceInfoPrototype().build(),
			newServiceInfoPrototype().build(),
			newServiceInfoPrototype().build(),
			newServiceInfoPrototype().build(),
			newServiceInfoPrototype().build()
		]
		
		RemoteServicesDiff remoteServicesDiff = diffCalculatorImpl.calculateRemoteServicesDiff(registryServices, [])
		assert remoteServicesDiff != null
		assert !remoteServicesDiff.getNewServices().isEmpty()
		assert remoteServicesDiff.getNewServices().size() == 5
		assert remoteServicesDiff.getRemovedServices().isEmpty()
	}
	
	@Test
	void testCalculateRemoteServicesDiff_emptyRegistry() {
		List clientRegistryCache = convertToRemoteServiceList([
			newServiceInfoPrototype().build(),
			newServiceInfoPrototype().build(),
			newServiceInfoPrototype().build(),
			newServiceInfoPrototype().build(),
			newServiceInfoPrototype().build()
		])
		RemoteServicesDiff remoteServicesDiff = diffCalculatorImpl.calculateRemoteServicesDiff([], clientRegistryCache)
		assert remoteServicesDiff != null
		assert !remoteServicesDiff.getRemovedServices().isEmpty()
		assert remoteServicesDiff.getRemovedServices().size() == 5
		assert remoteServicesDiff.getNewServices().isEmpty()
	}
	
	@Test
	void testCalculateRemoteServicesDiff_sameServiceAndClientRegistry() {
		
		List services = [
			newServiceInfoPrototype().build(),
			newServiceInfoPrototype().build(),
			newServiceInfoPrototype().build(),
			newServiceInfoPrototype().build(),
			newServiceInfoPrototype().build()
		]
		
		List clientRegistryCache = convertToRemoteServiceList(services)
		
		RemoteServicesDiff remoteServicesDiff = diffCalculatorImpl.calculateRemoteServicesDiff(services, clientRegistryCache)
		assert remoteServicesDiff != null
		assert remoteServicesDiff.getNewServices().isEmpty()
		assert remoteServicesDiff.getRemovedServices().isEmpty()
	}
	
	@Test
	void testCalculateRemoteServicesDiff_fullDiff() {
		
		// create 5 services for the central registry
		List<ServiceInfo> allRegistryServices = [
			newServiceInfoPrototype().build(),
			newServiceInfoPrototype().build(),
			newServiceInfoPrototype().build(),
			newServiceInfoPrototype().build(),
			newServiceInfoPrototype().build()
		]
		
		// create 2 local modified versions of the services
		ServiceInfo.Builder modified1Builder = ServiceInfo.Builder.create(allRegistryServices[0])
		ServiceInfo.Builder modified2Builder = ServiceInfo.Builder.create(allRegistryServices[1])
		modified1Builder.setServiceVersion("2.0")
		modified1Builder.setChecksum(RandomStringUtils.randomAlphanumeric(30))
		modified2Builder.setServiceVersion("2.0")
		modified2Builder.setChecksum(RandomStringUtils.randomAlphanumeric(30))
		RemoteService modified1 = constructRemoteService(modified1Builder.build())
		RemoteService modified2 = constructRemoteService(modified2Builder.build())
		
		List<RemoteService> clientRegistryCache = convertToRemoteServiceList(allRegistryServices)
		// modify first two entries, delete the last entry, add two new entries
		clientRegistryCache[0] = modified1
		clientRegistryCache[1] = modified2
		clientRegistryCache.remove(4)
		RemoteService new1 = constructRemoteService(newServiceInfoPrototype().build())
		RemoteService new2 = constructRemoteService(newServiceInfoPrototype().build())
		clientRegistryCache.add(new1)
		clientRegistryCache.add(new2)
		
		assert clientRegistryCache.size() == 6
		
		diffCalculatorImpl.calculateRemoteServicesDiff(allRegistryServices, clientRegistryCache)
		
		RemoteServicesDiff remoteServicesDiff = diffCalculatorImpl.calculateRemoteServicesDiff(allRegistryServices, clientRegistryCache)
		assert remoteServicesDiff != null
		
		assert remoteServicesDiff.getNewServices().size() == 3
		boolean[] found = [false, false, false]
		for (ServiceInfo serviceInfo : remoteServicesDiff.getNewServices()) {
			if (serviceInfo.is(allRegistryServices[0])) {
				found[0] = true
			} else if (serviceInfo.is(allRegistryServices[1])) {
				found[1] = true
			} else if (serviceInfo.is(allRegistryServices[4])) {
				found[2] = true
			}
		}
		assert found[0]
		assert found[1]
		assert found[2]
		
		assert remoteServicesDiff.getRemovedServices().size() == 4
		boolean[] removedFound = [false, false, false, false]
		for (RemoteService remoteService : remoteServicesDiff.getRemovedServices()) {
			if (remoteService.is(modified1)) {
				removedFound[0] = true
			} else if (remoteService.is(modified2)) {
				removedFound[1] = true
			} else if (remoteService.is(new1)) {
				removedFound[2] = true
			} else if (remoteService.is(new2)) {
				removedFound[3] = true
			}
		}
		assert removedFound[0]
		assert removedFound[1]
		assert removedFound[2]
		assert removedFound[3]
	}
	
	@Test
	void testCalculateLocalServicesDiff_emptyLists() {
		LocalServicesDiff localServicesDiff = diffCalculatorImpl.calculateLocalServicesDiff([], TEST1_INSTANCE_ID, [])
		assert localServicesDiff != null
		assert localServicesDiff.getLocalServicesToPublish().isEmpty()
		assert localServicesDiff.getLocalServicesToUpdate().isEmpty()
		assert localServicesDiff.getServicesToRemoveFromRegistry().isEmpty()
	}
	
	@Test
	void testCalculateLocalServicesDiff_emptyLocalServices() {
		
		// first test with a registry containing services different from the instance id being checked
		
		// create 5 services for the central registry
		List<ServiceInfo> allRegistryServices = [
			newServiceInfoPrototype(TEST1_INSTANCE_ID).build(),
			newServiceInfoPrototype(TEST1_INSTANCE_ID).build(),
			newServiceInfoPrototype(TEST2_INSTANCE_ID).build(),
			newServiceInfoPrototype(TEST2_INSTANCE_ID).build(),
			newServiceInfoPrototype(TEST2_INSTANCE_ID).build()
		]
		
		LocalServicesDiff localServicesDiff = diffCalculatorImpl.calculateLocalServicesDiff(filterForInstance(allRegistryServices, TEST3_INSTANCE_ID), TEST3_INSTANCE_ID, [])
		assert localServicesDiff != null
		assert localServicesDiff.getLocalServicesToPublish().isEmpty()
		assert localServicesDiff.getLocalServicesToUpdate().isEmpty()
		assert localServicesDiff.getServicesToRemoveFromRegistry().isEmpty()
		
		// now check with TEST1_INSTANCE_ID
		localServicesDiff = diffCalculatorImpl.calculateLocalServicesDiff(filterForInstance(allRegistryServices, TEST1_INSTANCE_ID), TEST1_INSTANCE_ID, [])
		assert localServicesDiff != null
		assert localServicesDiff.getLocalServicesToPublish().isEmpty()
		assert localServicesDiff.getLocalServicesToUpdate().isEmpty()
		assert localServicesDiff.getServicesToRemoveFromRegistry().size() == 2
		
		// now check with TEST2_INSTANCE_ID
		localServicesDiff = diffCalculatorImpl.calculateLocalServicesDiff(filterForInstance(allRegistryServices, TEST2_INSTANCE_ID), TEST2_INSTANCE_ID, [])
		assert localServicesDiff != null
		assert localServicesDiff.getLocalServicesToPublish().isEmpty()
		assert localServicesDiff.getLocalServicesToUpdate().isEmpty()
		assert localServicesDiff.getServicesToRemoveFromRegistry().size() == 3
	}
	
	@Test
    @Ignore
	void testCalculateLocalServicesDiff_emptyRegistryServices() {
				
		List<LocalService> localServices = [
			newLocalService(TEST1_INSTANCE_ID),
			newLocalService(TEST1_INSTANCE_ID),
			newLocalService(TEST1_INSTANCE_ID),
			newLocalService(TEST1_INSTANCE_ID),
			newLocalService(TEST1_INSTANCE_ID)
		]
		
		// should fail when encountering a local service that does not match instance id
		shouldFail(IllegalStateException.class) {
			diffCalculatorImpl.calculateLocalServicesDiff([], TEST2_INSTANCE_ID, localServices)
		}
		shouldFail(IllegalStateException.class) {
			diffCalculatorImpl.calculateLocalServicesDiff([], TEST3_INSTANCE_ID, localServices)
		}
		
		LocalServicesDiff localServicesDiff = diffCalculatorImpl.calculateLocalServicesDiff([], TEST1_INSTANCE_ID, localServices)
		assert localServicesDiff != null
		assert localServicesDiff.getLocalServicesToPublish().size() == 5
		assert localServicesDiff.getServicesToRemoveFromRegistry().isEmpty()
		
	}
	
	@Test
    @Ignore
	void testCalculateLocalServicesDiff_fullDiff() {
				
		List<LocalService> localServices = [
			newLocalService(TEST1_INSTANCE_ID),
			newLocalService(TEST1_INSTANCE_ID),
			newLocalService(TEST1_INSTANCE_ID),
			newLocalService(TEST1_INSTANCE_ID),
			newLocalService(TEST1_INSTANCE_ID)
		]
		
		ServiceInfo.Builder modified1Builder = ServiceInfo.Builder.create(localServices[2].getServiceEndpoint().getInfo())
		ServiceInfo.Builder modified2Builder = ServiceInfo.Builder.create(localServices[3].getServiceEndpoint().getInfo())
		modified1Builder.setServiceVersion("2.0")
		modified1Builder.setChecksum(RandomStringUtils.randomAlphanumeric(30))
		modified2Builder.setServiceVersion("2.0")
		modified2Builder.setChecksum(RandomStringUtils.randomAlphanumeric(30))
		ServiceInfo modified1 = modified1Builder.build()
		ServiceInfo modified2 = modified2Builder.build()
		
		// create 5 services for the central registry
		List<ServiceInfo> allRegistryServices = [
			assignServiceId(localServices[0].getServiceEndpoint().getInfo()),
			assignServiceId(localServices[1].getServiceEndpoint().getInfo()),
			assignServiceId(modified1),
			assignServiceId(modified2),
			newServiceInfoPrototype(TEST1_INSTANCE_ID).build(),
			newServiceInfoPrototype(TEST1_INSTANCE_ID).build(),
			newServiceInfoPrototype(TEST2_INSTANCE_ID).build(),
			newServiceInfoPrototype(TEST2_INSTANCE_ID).build(),
			newServiceInfoPrototype(TEST3_INSTANCE_ID).build(),
			newServiceInfoPrototype(TEST3_INSTANCE_ID).build(),
		]
		// in the above set of registry services, services for instance TEST2 and TEST3 should be ignored since they don't match the instance id we are using (TEST1)
		
		LocalServicesDiff localServicesDiff = diffCalculatorImpl.calculateLocalServicesDiff(filterForInstance(allRegistryServices, TEST1_INSTANCE_ID), TEST1_INSTANCE_ID, localServices)
		assert localServicesDiff != null
		
		assert localServicesDiff.getLocalServicesToPublish().size() == 1
		boolean[] found = [false]
		for (LocalService localService : localServicesDiff.getLocalServicesToPublish()) {
			if (localService.is(localServices[4])) {
				found[0] = true
			}
		}
		assert found[0]
				
		assert localServicesDiff.getLocalServicesToUpdate().size() == 4
		found = [false, false, false, false]
		for (ServiceInfo serviceInfo : localServicesDiff.getLocalServicesToUpdate().values()) {
			if (serviceInfo.is(allRegistryServices[0])) {
				found[0] = true
			} else if (serviceInfo.is(allRegistryServices[1])) {
				found[1] = true
			} else if (serviceInfo.is(allRegistryServices[2])) {
				found[2] = true
			} else if (serviceInfo.is(allRegistryServices[3])) {
				found[3] = true
			}
		}
		assert found[0]
		assert found[1]
		assert found[2]
		assert found[3]

		
		assert localServicesDiff.getServicesToRemoveFromRegistry().size() == 2
		found = [false, false]
		for (ServiceInfo serviceInfo : localServicesDiff.getServicesToRemoveFromRegistry()) {
			if (serviceInfo.is(allRegistryServices[4])) {
				found[0] = true
			} else if (serviceInfo.is(allRegistryServices[5])) {
				found[1] = true
			}
		}
		assert found[0]
		assert found[1]
	}
	
	ServiceInfo.Builder newServiceInfoPrototype(String instanceId = TEST1_INSTANCE_ID) {
		ServiceInfo.Builder builder = ServiceInfo.Builder.create()
		builder.setApplicationId(TEST1_NAMESPACE)
		builder.setChecksum(RandomStringUtils.randomAlphanumeric(30))
		builder.setEndpointUrl("http://localhost/remoting/" + RandomStringUtils.randomAlphanumeric(10))
		builder.setInstanceId(instanceId)
		builder.setServerIpAddress(LOCALHOST_IP)
		builder.setServiceName(new QName("TEST", RandomStringUtils.randomAlphanumeric(20)))
		builder.setServiceVersion("1.0")
		builder.setStatus(ServiceEndpointStatus.ONLINE)
		builder.setType(KsbApiConstants.ServiceTypes.SOAP)
		builder.setServiceId("" + nextServiceId++)
		builder.setServiceDescriptorId("" + nextServiceId)
		return builder
	}
	
	List<RemoteService> convertToRemoteServiceList(List<ServiceInfo> services) {
		List<RemoteService> remoteServices = []
		for (ServiceInfo serviceInfo : services) {
			remoteServices.add(constructRemoteService(serviceInfo))
		}
		return remoteServices
	}
	
	RemoteService constructRemoteService(ServiceInfo serviceInfo) {
		return new RemoteService(serviceInfo, serviceRegistryMock)
	}
	
	LocalService newLocalService(String instanceId) {
		SoapServiceDefinition soapServiceDefinition = new SoapServiceDefinition()
		soapServiceDefinition.setEndpointUrl(new URL("http://localhost/remoting/" + RandomStringUtils.randomAlphanumeric(10)))
		soapServiceDefinition.setService(new SimpleServiceImpl())
		soapServiceDefinition.setServiceInterface(SimpleService.class.getName())
		soapServiceDefinition.setServiceName(new QName("TEST", RandomStringUtils.randomAlphanumeric(20)))
		doInConfig(CONFIG_MAP) {
			soapServiceDefinition.validate()
		}
		return new LocalService(instanceId, soapServiceDefinition)
	}
	
	void doInConfig(Map<String, String> configProperties, Closure closure) {
        // ensure that it's destroyed first and we don't have anything left over from previous tests
        ConfigContext.destroy();
        try {
            Properties p = new Properties();
            p.putAll(configProperties);
		    SimpleConfig simpleConfig = new SimpleConfig(p)
		    simpleConfig.parseConfig()
			ConfigContext.init(simpleConfig)
			closure.call()
		} finally {
            ConfigContext.destroy();
        }
	}
	
	List<ServiceInfo> filterForInstance(List<ServiceInfo> allRegistryServices, String instanceId) {
		List<ServiceInfo> filtered = [];
		for (ServiceInfo serviceInfo : allRegistryServices) {
			if (instanceId.equals(serviceInfo.getInstanceId())) {
				filtered.add(serviceInfo);
			}
		}
		return filtered;
	}
	
	ServiceInfo assignServiceId(ServiceInfo service) {
		ServiceInfo.Builder builder = ServiceInfo.Builder.create(service);
		builder.setServiceId("" + nextServiceId++);
		builder.setServiceDescriptorId("" + nextServiceId);
		return builder.build();
	}
	
}
