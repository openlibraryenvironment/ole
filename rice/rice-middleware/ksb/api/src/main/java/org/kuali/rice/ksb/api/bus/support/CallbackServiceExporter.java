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
package org.kuali.rice.ksb.api.bus.support;

import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.ksb.api.bus.ServiceBus;
import org.kuali.rice.ksb.api.bus.ServiceDefinition;
import org.springframework.beans.factory.InitializingBean;

import javax.xml.namespace.QName;
import java.net.URL;

/**
 * A helper class which can be used to by a client application to export a callback service to the Kuali Service Bus
 * service registry.  A callback service is a service published by a client application which is invoked by one of the
 * Kuali Rice modules running as part of the standalone server.
 *
 * <p>While it's perfectly legal for an application to handle publishing of callback service implementations to the
 * service registry manually using the {@link ServiceBus} api or the {@link ServiceBusExporter}, this class helps with
 * publishing the services in such a way that they are compatible with the requirements of how the specific callback
 * services are supposed to be published.  This includes ensuring that information about the version of the callback
 * services is properly present in the service registry.  This additionally ensures the service is published using the
 * correct type of {@link ServiceDefinition} and the proper security settings.  By default, callback services use SOAP
 * and have bus security turned on.</p>
 *
 * <p>With the exception of the {@code callbackService}, most of the properties on this class are passed through to
 * either a {@code ServiceBusExporter} or a {@code SoapServiceDefinition}.  As a result, many of them are optional (see
 * the documentation on the aforementioned classes for details).  The callback service must be injected into this class
 * or else an {@code IllegalStateException} will be thrown during startup of this bean.
 *
 * @see org.kuali.rice.ksb.api.bus.support.ServiceBusExporter
 * @see org.kuali.rice.ksb.api.bus.support.SoapServiceDefinition
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CallbackServiceExporter implements InitializingBean {

    private String localServiceName;
	private String serviceNameSpaceURI;
	private QName serviceName;
    private String servicePath;
    private URL endpointUrl;
    private Boolean busSecurity;
    private String serviceInterface;
    private Object callbackService;

    private ServiceBus serviceBus;

    public CallbackServiceExporter() {
        this.busSecurity = Boolean.TRUE;
    }

    @Override
    public final void afterPropertiesSet() throws Exception {
        if (getCallbackService() == null) {
            throw new IllegalStateException("No callback service was provided to this exporter.");
        }
        ServiceBusExporter serviceBusExporter = createServiceBusExporter();
        serviceBusExporter.afterPropertiesSet();
    }

    /**
     * Creates a {@link org.kuali.rice.ksb.api.bus.support.ServiceBusExporter} based on the properties set on this
     * exporter.  Subclasses may override this method in order to customize how the exporter or it's
     * {@link org.kuali.rice.ksb.api.bus.ServiceDefinition} are created.
     *
     * @return a fully constructed ServiceBusExporter which is ready to be exported
     */
    protected ServiceBusExporter createServiceBusExporter() {
        ServiceBusExporter serviceBusExporter = new ServiceBusExporter();
        serviceBusExporter.setServiceDefinition(createSoapServiceDefinition());
        return serviceBusExporter;
    }

    /**
     * Creates a {@link org.kuali.rice.ksb.api.bus.support.SoapServiceDefinition} based on the properties set on this
     * exporter.  Subclasses may override this method in order to customize how the SOAP service definition is created.
     *
     * @return the SoapServiceDefinition to be exported
     */
    protected SoapServiceDefinition createSoapServiceDefinition() {
        SoapServiceDefinition serviceDefinition = new SoapServiceDefinition();

        // configured setup
        serviceDefinition.setLocalServiceName(getLocalServiceName());
        serviceDefinition.setServiceNameSpaceURI(getServiceNameSpaceURI());
        serviceDefinition.setServiceName(getServiceName());

        serviceDefinition.setService(getCallbackService());
        serviceDefinition.setServicePath(getServicePath());
        serviceDefinition.setEndpointUrl(getEndpointUrl());
        serviceDefinition.setServiceInterface(getServiceInterface());

        // standard setup
        serviceDefinition.setJaxWsService(true);
        serviceDefinition.setBusSecurity(getBusSecurity());
        serviceDefinition.setServiceVersion(ConfigContext.getCurrentContextConfig().getRiceVersion());

        return serviceDefinition;
    }

    protected final String getLocalServiceName() {
        return localServiceName;
    }

    public final void setLocalServiceName(String localServiceName) {
        this.localServiceName = localServiceName;
    }

    public final String getServiceNameSpaceURI() {
        return serviceNameSpaceURI;
    }

    public final void setServiceNameSpaceURI(String serviceNameSpaceURI) {
        this.serviceNameSpaceURI = serviceNameSpaceURI;
    }

    public final QName getServiceName() {
        return serviceName;
    }

    public final void setServiceName(QName serviceName) {
        this.serviceName = serviceName;
    }

    public final String getServicePath() {
        return servicePath;
    }

    public final void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }

    public final URL getEndpointUrl() {
        return endpointUrl;
    }

    public final void setEndpointUrl(URL endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public final Boolean getBusSecurity() {
        return busSecurity;
    }

    public final void setBusSecurity(Boolean busSecurity) {
        this.busSecurity = busSecurity;
    }

    public String getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(String serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public Object getCallbackService() {
        return callbackService;
    }

    public void setCallbackService(Object callbackService) {
        this.callbackService = callbackService;
    }

    public final ServiceBus getServiceBus() {
        return serviceBus;
    }

    public final void setServiceBus(ServiceBus serviceBus) {
        this.serviceBus = serviceBus;
    }

}
