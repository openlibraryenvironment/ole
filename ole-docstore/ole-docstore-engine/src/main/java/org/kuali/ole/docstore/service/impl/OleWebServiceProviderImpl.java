package org.kuali.ole.docstore.service.impl;

import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.kuali.ole.docstore.service.OleWebServiceProvider;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/22/12
 * Time: 6:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleWebServiceProviderImpl
        implements OleWebServiceProvider {
    org.slf4j.Logger LOG = LoggerFactory.getLogger(OleWebServiceProviderImpl.class);
    private ClientProxyFactoryBean clientFactory = null;

    @Override
    public Object getService(String serviceClassName, String serviceName, String serviceURL) {
        try {
            clientFactory = new JaxWsProxyFactoryBean();
            clientFactory.setServiceClass(Class.forName(serviceClassName));
        } catch (ClassNotFoundException e) {
            LOG.error("Failed to connect to soap service because failed to load interface class: ", e);
        }
        QName namespaceURI = new QName("http://service.select.ole.kuali.org/", serviceName);
        clientFactory.setServiceName(namespaceURI);
        clientFactory.setAddress(serviceURL);
        Object service = clientFactory.create();
        return service;
    }
}
