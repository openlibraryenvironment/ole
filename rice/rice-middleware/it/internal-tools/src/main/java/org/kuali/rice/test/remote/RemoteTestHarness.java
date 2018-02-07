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
package org.kuali.rice.test.remote;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.kuali.rice.ksb.impl.cxf.interceptors.ImmutableCollectionsInInterceptor;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

/**
 * Harness used to hold a reference to an endpoint that is published to support remote tests.  Tests using
 * this harness should pass in a @WebService annotated interface class and an object of an implementing class
 * of that interface to the publishEndpointAndReturnProxy method in @Before or setUp methods used in tests.
 * <p/>
 * The endpoint will always be published at a URL like http://localhost:1025/service where the port number changes
 * each time publishEndpointAndReturnProxy is called and guarantees that an open port is used.
 * <p/
 * After each test is run, stopEndPoint should be called in @After or tearDown methods in order to unpublish the
 * endpoint.
 * <p/>
 *
 */
public class RemoteTestHarness {

    private static final Log LOG = LogFactory.getLog(RemoteTestHarness.class);

    private static String ENDPOINT_ROOT = "http://localhost"; //Default URL
    private static String ENDPOINT_PATH = "/service";

    private Endpoint endpoint;

    @SuppressWarnings("unchecked")
    /**
     * Creates a published endpoint from the passed in serviceImplementation and also returns a proxy implementation
     * of the passed in interface for clients to use to hit the created endpoint.
     */
    public <T> T publishEndpointAndReturnProxy(Class<T> jaxWsAnnotatedInterface, T serviceImplementation) {
        if (jaxWsAnnotatedInterface.isInterface() &&
                jaxWsAnnotatedInterface.getAnnotation(WebService.class) != null &&
                jaxWsAnnotatedInterface.isInstance(serviceImplementation)) {

            String endpointUrl = getAvailableEndpointUrl();
            LOG.info("Publishing service to: " + endpointUrl);
            endpoint = Endpoint.publish(endpointUrl, serviceImplementation);

            JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
            factory.setServiceClass(jaxWsAnnotatedInterface);
            factory.setAddress(endpointUrl);

            T serviceProxy = (T) factory.create();

            /* Add the ImmutableCollectionsInInterceptor to mimic interceptors added in the KSB */
            Client cxfClient = ClientProxy.getClient(serviceProxy);
            cxfClient.getInInterceptors().add(new ImmutableCollectionsInInterceptor());

            return serviceProxy;
        } else {
            throw new IllegalArgumentException("Passed in interface class type must be annotated with @WebService " +
                    "and object reference must be an implementing class of that interface.");

        }
    }

    /**
     * Stops and makes an internal endpoint unpublished if it was previously published.
     * Otherwise, this method is a no-op.
     */
    public void stopEndpoint() {
        if (endpoint != null) {
            endpoint.stop();
        }
    }

    private String getAvailableEndpointUrl() {
        String port = Integer.toString(AvailablePortFinder.getNextAvailable());
        return ENDPOINT_ROOT + ":" + port + ENDPOINT_PATH;
    }
}
