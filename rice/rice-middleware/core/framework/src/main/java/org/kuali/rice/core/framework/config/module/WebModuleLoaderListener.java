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
package org.kuali.rice.core.framework.config.module;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.framework.resourceloader.SpringResourceLoader;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *  A ServletContextListener which scans for Rice modules and loads the web modules for each of them which has a web
 *  module configuration associated with it.
 *
 *  @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class WebModuleLoaderListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(WebModuleLoaderListener.class);
    private static final QName RESOURCE_LOADER_NAME = new QName("org.kuali.rice.web", "WebModuleResourceLoader");

    @Override
    public void contextInitialized(ServletContextEvent event) {
        List<String> springFileLocations = getSpringFileLocations();
        if (CollectionUtils.isNotEmpty(springFileLocations)) {
            LOG.info("Initializing " + getClass().getSimpleName() + " with spring files: " + springFileLocations);
            SpringResourceLoader resourceLoader = new SpringResourceLoader(RESOURCE_LOADER_NAME, springFileLocations, event.getServletContext());
            try {
                resourceLoader.start();
            } catch (Exception e) {
                throw new ConfigurationException("Failed to load web module spring configuration", e);
            }
            GlobalResourceLoader.addResourceLoader(resourceLoader);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // shutdown of resource loaders is coordinated by the GRL
    }

    protected List<String> getSpringFileLocations() {
        List<String> springFileLocations = new ArrayList<String>();
        // loop over the installed modules, adding their web module configuration spring files
        @SuppressWarnings("unchecked")
        final Collection<ModuleConfigurer> riceModules = ModuleConfigurer.getCurrentContextConfigurers();
        for ( ModuleConfigurer module : riceModules ) {
            if ( module.shouldRenderWebInterface() ) {
                WebModuleConfiguration webModuleConfiguration = module.getWebModuleConfiguration();
                if (webModuleConfiguration == null) {
                    throw new ConfigurationException("Attempting to load WebModuleConfiguration for module '" + module.getModuleName() + "' but no configuration was provided!");
                }
                List<String> webModuleSpringFiles = webModuleConfiguration.getWebSpringFiles();
                if (CollectionUtils.isNotEmpty(webModuleSpringFiles)) {
                    springFileLocations.addAll(webModuleSpringFiles);
                }
            }
        }
        return springFileLocations;
    }
    
}
