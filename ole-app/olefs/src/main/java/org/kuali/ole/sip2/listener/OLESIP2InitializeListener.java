package org.kuali.ole.sip2.listener;

import org.kuali.ole.sip2.service.OLESIP2HelperService;
import org.kuali.ole.sip2.service.impl.OLESIP2HelperServiceImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by sheiksalahudeenm on 12/29/14.
 */
public class OLESIP2InitializeListener implements ServletContextListener {
    OLESIP2HelperService OLESIP2HelperService = new OLESIP2HelperServiceImpl();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        OLESIP2HelperService.startOLESip2Server();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        OLESIP2HelperService.stopOLESip2Server();
    }
}
