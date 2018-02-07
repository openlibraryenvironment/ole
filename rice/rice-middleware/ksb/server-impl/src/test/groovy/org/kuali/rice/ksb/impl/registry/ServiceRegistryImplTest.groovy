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
package org.kuali.rice.ksb.impl.registry

import org.kuali.rice.ksb.api.registry.ServiceInfo
import org.junit.Test
import org.kuali.rice.ksb.api.registry.ServiceRegistry
import org.junit.Before
import org.apache.commons.collections.CollectionUtils
import junit.framework.Assert
import groovy.mock.interceptor.MockFor

class ServiceRegistryImplTest {
    private MockFor mockServiceRegistryDao
    ServiceRegistryDao serviceRegistryDao
    ServiceRegistry serviceRegistry;
    ServiceRegistryImpl serviceRegistryImpl;
    List<ServiceInfo> listToReturn = Collections.emptyList()
    List<ServiceInfoBo> bosToReturn = Collections.emptyList()

    @Before
    void setupServiceUnderTest() {
        serviceRegistryImpl = new ServiceRegistryImpl()
        serviceRegistry = serviceRegistryImpl    //assign Interface type to implementation reference for unit test only
    }

    @Before
    void setupMockContext() {
        mockServiceRegistryDao = new MockFor(ServiceRegistryDao.class);

    }

    void injectServiceRegistryDao() {
        serviceRegistryDao = mockServiceRegistryDao.proxyDelegateInstance();
        serviceRegistryImpl.setServiceRegistryDao(serviceRegistryDao);
    }

    @Test
    public void testGetAllServiceInfosForInstance() {
        mockServiceRegistryDao.demand.getAllServiceInfosForInstance(1..1) {
            String id -> return bosToReturn;
        }

        injectServiceRegistryDao();

        List<ServiceInfo> serviceInfos = serviceRegistry.getAllServicesForInstance("instanceName")
        Assert.assertEquals(serviceInfos, listToReturn)
    }

    @Test
    public void testGetAllServiceInfosForApplication() {
        mockServiceRegistryDao.demand.getAllServiceInfosForApplication(1..1) {
            String id -> return bosToReturn;
        }

        injectServiceRegistryDao();

        List<ServiceInfo> serviceInfos = serviceRegistry.getAllServicesForApplication("applicationId")
        Assert.assertEquals(serviceInfos, listToReturn)
    }
}
