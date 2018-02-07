/**
 * Copyright 2005-2013 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.ksb.server;

import org.kuali.rice.core.api.config.property.ConfigContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;

public class KSBTestContextLoaderListener extends ContextLoaderListener {
	
	 
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		String testClientName = event.getServletContext().getInitParameter("test.client.spring.context.name");
		ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		ConfigContext.getCurrentContextConfig().putObject(testClientName, appContext);
	}
}
