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
package org.kuali.rice.ken.util;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.DefaultPropertiesPersister;

/**
 * PropertyPlaceholderConfigurer subclass that overrides any statically defined properties and properties locations,
 * with configuration settings from a properties file specified in a System property.
 * @see PropertyPlaceholderConfigurer
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NotificationPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    private static final String CFG_LOCATION_PROPERTY = "notification.config";

    /**
     * @see org.springframework.core.io.support.PropertiesLoaderSupport#mergeProperties()
     */
    @Override
    protected Properties mergeProperties() throws IOException {
        Properties properties = super.mergeProperties();
        return transformProperties(properties);
    }

    /**
     * This method consolidates config properties.
     * @param properties
     * @return Properties
     * @throws IOException
     */
    protected Properties transformProperties(Properties properties) throws IOException {
        String cfgLocation = System.getProperty(CFG_LOCATION_PROPERTY);

        if (cfgLocation != null) {
            Resource resource = new FileSystemResourceLoader().getResource(cfgLocation);
            if (resource != null && resource.exists()) {
                new DefaultPropertiesPersister().load(properties, resource.getInputStream());
            }
        }
        
        return properties;
    }

    /* I would have liked to just dynamically add a new Resource to the list of resource locations and
     * delegate entirely to the superclass hierarchy for loading, but unfortunately the locations member
     * is inaccessible, so we have to load the properties into the existing properties object ourselves,
     * in the process overridding any existing entries
     */
//    public void afterPropertiesSet() throws Exception {
//        String cfg = System.getProperty(CFG_LOCATION_PROPERTY);
//        if (cfg == null) return;
//        
//        Resource resource = new FileSystemResourceLoader().getResource(cfg);
//        // add resource to locations
//    }
}
