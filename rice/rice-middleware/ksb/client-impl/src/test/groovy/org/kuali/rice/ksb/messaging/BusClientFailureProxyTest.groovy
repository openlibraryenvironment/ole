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
package org.kuali.rice.ksb.messaging

import org.junit.Before
import org.junit.Test
import org.kuali.rice.core.api.CoreConstants
import org.kuali.rice.core.api.config.property.ConfigContext
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader
import org.kuali.rice.core.api.resourceloader.ResourceLoader
import org.kuali.rice.core.impl.config.property.JAXBConfigImpl
import org.kuali.rice.ksb.api.bus.Endpoint
import org.kuali.rice.ksb.api.bus.ServiceBus
import org.kuali.rice.ksb.api.bus.ServiceConfiguration

import javax.xml.namespace.QName

import static org.junit.Assert.assertTrue

/**
 * Unit tests for the BusClientFailureProxy
 */
class BusClientFailureProxyTest {

    // A list of endpoints for a service
    private Set<ServiceConfiguration> serviceConfigurations = [
            buildServiceConfiguration("bogusService", "http://foo.bar/endpoint1", "MyApp"),
            buildServiceConfiguration("bogusService", "http://foo.bar/endpoint2", "MyApp"),
            buildServiceConfiguration("bogusService", "http://foo.bar/endpoint3", "MyApp"),
            buildServiceConfiguration("bogusService", "http://foo.bar/endpoint4", "MyApp"),
            buildServiceConfiguration("bogusService", "http://foo.bar/endpoint5", "MyApp"),
    ]

    // A list that we'll populate with endpoints for the bogus services
    private List<Endpoint> endpoints = []

    @Before
    void setupFakeEnv() {
        // build the endpoints
        for (serviceConfiguration in serviceConfigurations) {
            endpoints.add(buildEndpoint(new BogusServiceImpl(), serviceConfiguration))
        }

        // make our mock ServiceBus available via the GlobalResourceLoader
        def config = new JAXBConfigImpl();
        config.putProperty(CoreConstants.Config.APPLICATION_ID, "MyApp");
        ConfigContext.init(config);

        GlobalResourceLoader.stop()
        GlobalResourceLoader.addResourceLoader([
                getName: { -> new QName("Foo", "Bar") },
                getService: { [ getEndpoints: { a, b -> endpoints } ] as ServiceBus },
                stop: {}
        ] as ResourceLoader)
    }

    /**
     * tests that the fail over logic in BusClientFailureProxy will try all endpoint options for a service
     * @throws Exception
     */
    @Test
    void testFailoverTriesAllOptions() throws Exception {

        BogusService unwrappedService = new BogusServiceImpl();

        // wrap our service in a BusClientFailureProxy
        BogusService service = BusClientFailureProxy.wrap(unwrappedService,
                buildServiceConfiguration("bogusService", "http://foo.bar/endpoint0", "MyApp"));

        try {
            // since the service apparently fails in a way that indicates that the endpoint is down, all the endpoints
            // should be tried here
            service.doSomething();
            fail("should have thrown an exception");
        } catch (IOException e) {
            // fine, we expect the end result to be an exception since the calls to all service endpoints will result in
            // exceptions
        }

        assertTrue("original service should have been called", ((BogusServiceImpl)unwrappedService).wasCalled);

        for (endpoint in endpoints) {
            assertTrue("service endpoint should have been called", ((BogusServiceImpl)endpoint.service).wasCalled)
        }
    }

    private ServiceConfiguration buildServiceConfiguration(String serviceName, String endpointUrl, String appId) {
        return [
                getServiceName: { -> new QName(appId, serviceName) },
                getEndpointUrl: { -> new URL(endpointUrl) },
                getApplicationId: { -> appId }
        ] as ServiceConfiguration
    }

    private Endpoint buildEndpoint(Object service, ServiceConfiguration serviceConfiguration) {
        return [
                getServiceConfiguration: { -> serviceConfiguration },
                getService: { -> service },
        ] as Endpoint
    }
}
