package org.kuali.ole.select.document.service.impl;

import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.kuali.ole.select.document.service.OleWebServiceProvider;

import javax.xml.namespace.QName;

/**
 * Created by IntelliJ IDEA.
 * User: balakumaranm
 * Date: 4/12/12
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleWebServiceProviderImpl implements OleWebServiceProvider {
    private ClientProxyFactoryBean clientFactory = null;

    @Override
    public Object getService(String serviceClassName, String serviceName, String serviceURL) {
        try {
            clientFactory = new JaxWsProxyFactoryBean();
            clientFactory.setServiceClass(Class.forName(serviceClassName));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to connect to soap service because failed to load interface class: " + serviceClassName, e);
        }
        QName namespaceURI = new QName("http://service.select.ole.kuali.org/", serviceName);
        clientFactory.setServiceName(namespaceURI);
        clientFactory.setAddress(serviceURL);
        Object service = clientFactory.create();
        return service;
    }
}
