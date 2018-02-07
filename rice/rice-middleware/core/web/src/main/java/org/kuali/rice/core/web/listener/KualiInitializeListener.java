/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.core.web.listener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.impl.config.property.JAXBConfigImpl;
import org.kuali.rice.kew.api.KewApiConstants;
import org.springframework.util.Log4jConfigurer;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * A ServletContextListener responsible for initializing the a Kuali Rice application.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KualiInitializeListener implements ServletContextListener {

    private static final Log LOG = LogFactory.getLog(KualiInitializeListener.class);

    private static final String DEFAULT_SPRING_BEANS_REPLACEMENT_VALUE = "${bootstrap.spring.file}";
    private static final String WEB_BOOTSTRAP_SPRING_FILE = "web.bootstrap.spring.file";

    private XmlWebApplicationContext context;

    /**
     * ServletContextListener interface implementation that schedules the start of the lifecycle
     */
    @Override
	public void contextInitialized(ServletContextEvent sce) {
        long startInit = System.currentTimeMillis();
        LOG.info("Initializing Kuali Rice Application...");

        List<String> configLocations = new ArrayList<String>();
        String additionalConfigLocations = System.getProperty(KewApiConstants.ADDITIONAL_CONFIG_LOCATIONS_PARAM);
        if (!StringUtils.isBlank(additionalConfigLocations)) {
            String[] additionalConfigLocationArray = additionalConfigLocations.split(",");
            for (String additionalConfigLocation : additionalConfigLocationArray) {
                configLocations.add(additionalConfigLocation);
            }
        }

        String bootstrapSpringBeans = "";
        if (!StringUtils.isBlank(System.getProperty(WEB_BOOTSTRAP_SPRING_FILE))) {
            bootstrapSpringBeans = System.getProperty(WEB_BOOTSTRAP_SPRING_FILE);
        } else if (!StringUtils.isBlank(sce.getServletContext().getInitParameter(WEB_BOOTSTRAP_SPRING_FILE))) {
            String bootstrapSpringInitParam = sce.getServletContext().getInitParameter(WEB_BOOTSTRAP_SPRING_FILE);
            // if the value comes through as ${bootstrap.spring.beans}, we ignore it
            if (!DEFAULT_SPRING_BEANS_REPLACEMENT_VALUE.equals(bootstrapSpringInitParam)) {
            	bootstrapSpringBeans = bootstrapSpringInitParam;
            	LOG.info("Found bootstrap Spring Beans file defined in servlet context: " + bootstrapSpringBeans);
            }
        }

        Properties baseProps = new Properties();
        baseProps.putAll(getContextParameters(sce.getServletContext()));
        baseProps.putAll(System.getProperties());
        JAXBConfigImpl config = new JAXBConfigImpl(baseProps);
        ConfigContext.init(config);
        
        context = new XmlWebApplicationContext();
        if (!StringUtils.isEmpty(bootstrapSpringBeans)) {
            context.setConfigLocation(bootstrapSpringBeans);
        }
        context.setServletContext(sce.getServletContext());

        try {
            context.refresh();
        } catch (RuntimeException e) {
            LOG.error("problem during context.refresh()", e);

            throw e;
        }

        context.start();
        long endInit = System.currentTimeMillis();
        LOG.info("...Kuali Rice Application successfully initialized, startup took " + (endInit - startInit) + " ms.");
    }

    /**
     * Translates context parameters from the web.xml into entries in a Properties file.
     */
    protected Properties getContextParameters(ServletContext context) {
        Properties properties = new Properties();
        Enumeration<String> paramNames = context.getInitParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            properties.put(paramName, context.getInitParameter(paramName));
        }
        return properties;
    }

    @Override
	public void contextDestroyed(ServletContextEvent sce) {
        LOG.info("Shutting down Kuali Rice...");
        if (context != null) {
            context.close();
        }
        LOG.info("...completed shutdown of Kuali Rice.");
        Log4jConfigurer.shutdownLogging();
    }

    public XmlWebApplicationContext getContext() {
        return context;
    }

}
