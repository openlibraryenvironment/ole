package org.kuali.ole.web;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.base.BaseTestCase;
import org.kuali.ole.docstore.discovery.servlet.DiscoveryContextListener;

import javax.servlet.ServletContextEvent;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/26/12
 * Time: 7:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiscoveryContextListener_UT extends BaseTestCase{
    DiscoveryContextListener discoveryContextListener = new DiscoveryContextListener();
    ServletContextEvent servletContextEvent     = null;



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
          discoveryContextListener.contextInitialized(servletContextEvent);
      }

}
