package org.kuali.ole.deliver.defaultload; /**
 * Created with IntelliJ IDEA.
 * User: peris
 * Date: 10/30/12
 * Time: 9:22 PM
 * To change this template use File | Settings | File Templates.
 */

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class LoadDefaultPatronsListener implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener {
    private static final Logger LOG = Logger.getLogger(LoadDefaultPatronsListener.class);

    // Public constructor is required by servlet spec
    public LoadDefaultPatronsListener() {
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
        /* This method is called when the servlet context is
           initialized(when the Web application is deployed).
           You can initialize servlet context related data here.
        */
        LoadDefaultPatronsBean loadDefaultPatronsBean = GlobalResourceLoader.getService("loadDefaultPatronsBean");
        try {
            loadDefaultPatronsBean.loadDefaultPatrons(false);
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        /* This method is invoked when the Servlet Context
           (the Web application) is undeployed or
           Application Server shuts down.
        */
    }

    // -------------------------------------------------------
    // HttpSessionListener implementation
    // -------------------------------------------------------
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }

    // -------------------------------------------------------
    // HttpSessionAttributeListener implementation
    // -------------------------------------------------------

    public void attributeAdded(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute
           is added to a session.
        */
    }

    public void attributeRemoved(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute
           is removed from a session.
        */
    }

    public void attributeReplaced(HttpSessionBindingEvent sbe) {
        /* This method is invoked when an attibute
           is replaced in a session.
        */
    }
}
