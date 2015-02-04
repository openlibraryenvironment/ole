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
package org.kuali.ole.utility;

import static org.apache.commons.lang.StringUtils.isBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logic for determining a base directory for Kuali applications by examining system properties and the environment the application is running in
 */
public class ConfigUtil {
    private static final Logger logger = LoggerFactory.getLogger(ConfigUtil.class);

    public static final String KUALI_HOME_SYSTEM_PROPERTY = "kuali.home";
    public static final String KUALI_HOME_ENVIRONMENT_VAR = "KUALI_HOME";
    public static final String ENVIRONMENT = System.getProperty(Constants.ENVIRONMENT_PROPERTY, Constants.DEFAULT_ENVIRONMENT);
    public static final String KUALI_HOME_DEFAULT_VALUE = System.getProperty("user.home") + "/kuali/main/" + ENVIRONMENT;

    /**
     * Return the configured value for the <code>kuali.home</code> system property, the <code>KUALI_HOME</code> environment variable, or a default value for "kuali.home" based on
     * "user.home".
     * <p/>
     * When this method finishes the system property <code>kuali.home</code> is guaranteed to be set to the same value returned by this method.
     */
    public String getKualiHome() {
        String kualiHome = getValue(KUALI_HOME_SYSTEM_PROPERTY, KUALI_HOME_ENVIRONMENT_VAR, KUALI_HOME_DEFAULT_VALUE);
        String systemProperty = System.getProperty(KUALI_HOME_SYSTEM_PROPERTY);
        if (!kualiHome.equals(systemProperty)) {
            logger.info("Setting " + KUALI_HOME_SYSTEM_PROPERTY + "=" + kualiHome);
            System.setProperty(KUALI_HOME_SYSTEM_PROPERTY, kualiHome);
        }
        return kualiHome;
    }

    public String getGroupHome(String group) {
        return getKualiHome() + "/" + group;
    }

    public String getApplicationHome(String group, String application) {
        return getGroupHome(group) + "/" + application;
    }

    /**
     * Locate a value by examining system properties and environment variables. If a system property is found, the system property always wins. Otherwise the logic defers to the
     * environment variable before falling through to the default value.
     *
     * @param systemProperty
     * @param environmentVariable
     * @param defaultValue
     * @return
     */
    public String getValue(String systemProperty, String environmentVariable, String defaultValue) {
        if (!isBlank(systemProperty) && !isBlank(System.getProperty(systemProperty))) {
            return System.getProperty(systemProperty);
        } else if (!isBlank(environmentVariable) && !isBlank(System.getenv(environmentVariable))) {
            return System.getenv(environmentVariable);
        } else {
            return defaultValue;
        }
    }
}
