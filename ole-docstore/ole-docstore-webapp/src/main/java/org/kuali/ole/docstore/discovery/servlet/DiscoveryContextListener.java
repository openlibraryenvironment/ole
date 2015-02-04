package org.kuali.ole.docstore.discovery.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by IntelliJ IDEA.
 * User: SG7940
 * Date: 3/27/12
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class DiscoveryContextListener
        implements ServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(DiscoveryContextListener.class);

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOG.info("solr.solr.home system property value=" + System.getProperty("solr.solr.home"));
        //EnvironmentUtil.initEnvironment();
        //EnvironmentUtil.printEnvironment();
        LOG.info("solr.solr.home system property value=" + System.getProperty("solr.solr.home"));
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}

