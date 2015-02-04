package org.kuali.ole.web;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.base.BaseTestCase;
import org.kuali.ole.utility.Constants;
import org.springframework.util.FileCopyUtils;

import javax.servlet.ServletContextEvent;
import java.io.File;
import java.net.URL;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/26/12
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocStoreContextListener_UT
        extends BaseTestCase {

    DocStoreContextListener docStoreContextListener = new DocStoreContextListener();
    ServletContextEvent     servletContextEvent     = null;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void testContextInitialized() throws Exception {
        File inputDir = new File(this.getClass().getResource("repository").toURI());
        System.out.println(inputDir.getPath());
        System.setProperty(Constants.OLE_DOCSTORE_HOME_SYSTEM_PROPERTY, inputDir.getPath());
        docStoreContextListener.getDocStoreEnvUtil().setVendor("derby");
        docStoreContextListener.getDocStoreEnvUtil().setProperties(new Properties());
        docStoreContextListener.contextInitialized(servletContextEvent);
    }
}
