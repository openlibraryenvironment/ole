package org.kuali.ole.service;

import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.base.BaseTestCase;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.xml.namespace.QName;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/23/12
 * Time: 12:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleUuidCheckWebservice_UT extends BaseTestCase {


    @Mock
    public ConfigContext mockconfigContext;


    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        //Mockito.when(mockconfigContext.getCurrentContextConfig()).thenReturn(new Config());
    }

    private static final Logger LOG = LoggerFactory.getLogger(OleUuidCheckWebservice_UT.class);
    ClientProxyFactoryBean clientFactory;

    @Test
    public void testUuidCheck() {

        OleUuidCheckWebService oleUuidCheckWebService = (OleUuidCheckWebService) getService();
         try{
             String exist = oleUuidCheckWebService.checkUuidExsistence("3f9ba69e-24ac-4737-9a4b-6e4b9a6d14c,bef35848-82e2-4af1-9c7b-e66443efaa1");
             LOG.info(" exist " + exist);
         }
         catch (Exception e)
         {
             LOG.info("OLE Server down..." + e);
         }
    }

    public Object getService() {
        try {
            clientFactory = new JaxWsProxyFactoryBean();
            clientFactory.setServiceClass(Class.forName("org.kuali.ole.service.OleUuidCheckWebService"));

        } catch (ClassNotFoundException e) {
            LOG.error("Failed to connect to soap service because failed to load interface class: ", e);
        }
        QName namespaceURI = new QName("http://service.select.ole.kuali.org/", "oleUuidCheckWebService");
        clientFactory.setServiceName(namespaceURI);
        String serviceURL = "";//ConfigContext.getCurrentContextConfig().getProperty("uuidCheckServiceURL");
        LOG.info(" uuidCheckServiceURL --------> " + serviceURL);
        clientFactory.setAddress(serviceURL);
        Object service = clientFactory.create();
        LOG.info("<<<< service >>>> " + service);
        return service;

    }
}
