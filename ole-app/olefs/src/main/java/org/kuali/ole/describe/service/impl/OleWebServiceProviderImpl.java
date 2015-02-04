package org.kuali.ole.describe.service.impl;

import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.kuali.ole.describe.service.OleWebServiceProvider;
import org.kuali.rice.core.api.exception.RiceRuntimeException;

import javax.xml.namespace.QName;

/**
 * OleWebServiceProviderImpl uses OleWebServiceProvider for consuming webservices which are exposed in other applications.
 */
public class OleWebServiceProviderImpl implements OleWebServiceProvider {
    private ClientProxyFactoryBean clientFactory = null;

    /**
     * Gets the services from JaxWsProxyFactoryBean,And sets the ServiceClass,ServiceName,and URL.
     *
     * @param serviceClassName
     * @param serviceName
     * @param serviceURL
     * @return Returns the Service.
     */
    @Override
    public Object getService(String serviceClassName, String serviceName, String serviceURL) {
        try {
            clientFactory = new JaxWsProxyFactoryBean();
            clientFactory.setServiceClass(Class.forName(serviceClassName));
        } catch (ClassNotFoundException e) {
            throw new RiceRuntimeException("Failed to connect to soap service because failed to load interface class: ", e);
        }
        QName namespaceURI = new QName("http://service.select.ole.kuali.org/", serviceName);
        clientFactory.setServiceName(namespaceURI);
        clientFactory.setAddress(serviceURL);
        Object service = clientFactory.create();
        return service;
    }
}
