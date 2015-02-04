package org.kuali.ole;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

/**
 *
 */
public class LoadDefaultWorkflowsListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// LoadDefaultWorkflowsBean loadDefaultWorkflowsBean = GlobalResourceLoader.getService("loadDefaultWorkflowsBean");
		// loadDefaultWorkflowsBean.unpackWorkflows();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
