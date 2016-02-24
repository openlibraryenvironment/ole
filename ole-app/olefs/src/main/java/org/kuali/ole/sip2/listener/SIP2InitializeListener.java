package org.kuali.ole.sip2.listener;

import org.kuali.ole.sip2.service.SIP2HelperService;
import org.kuali.ole.sip2.service.impl.SIP2HelperServiceImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by chenchulakshmig on 8/27/15.
 */
public class SIP2InitializeListener implements ServletContextListener {
    SIP2HelperService OLESIP2HelperService = new SIP2HelperServiceImpl();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //OLESIP2HelperService.startOLESip2Server();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //OLESIP2HelperService.stopOLESip2Server();
    }
}
