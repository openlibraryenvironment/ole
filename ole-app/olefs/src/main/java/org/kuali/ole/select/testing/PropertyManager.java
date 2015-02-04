/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.testing;

import org.springframework.core.io.ClassPathResource;

import java.util.Enumeration;
import java.util.Properties;

public class PropertyManager {
    static PropertyManager propManager;
    static Properties prop;

    public static PropertyManager getInstance() {
        if (propManager == null) {
            propManager = new PropertyManager();
            propManager.loadProperties();
        }
        return propManager;
    }

    public static String getProperty(String key) {
        String value = prop.getProperty(key);
        return value;
    }

    private void loadProperties() {
        ClassPathResource classPathResource = new ClassPathResource("org/kuali/ole/select/testing/webservice.properties");
        prop = new Properties();
        try {
            prop.load(classPathResource.getInputStream());
            Enumeration keys = prop.propertyNames();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                prop.put(key, prop.getProperty((String) key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
