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
package org.kuali.rice.core.impl.config.module;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.lifecycle.BaseLifecycle;
import org.springframework.util.Log4jConfigurer;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Lifecycle implementation that initializes and shuts down Log4J logging
 */
class Log4jLifeCycle extends BaseLifecycle {

    private static final String LOG4J_FILE_NOT_FOUND = "log4j settings file not found at location: ";

	/**
     * Convenience constant representing a minute in milliseconds
     */
    private static final int MINUTE = 60 * 1000;

    /**
     * Location of default/automatic Log4J configuration properties, in Spring ResourceUtils resource/url syntax
     */
    private static final String AUTOMATIC_LOGGING_CONFIG_URL = "classpath:org/kuali/rice/core/logging/default-log4j.properties";

    /**
     * Default settings reload interval to use in the case that the settings are reloadable (i.e. they originate from a file)
     */
    private static final int DEFAULT_RELOAD_INTERVAL = 5 * MINUTE; // 5 minutes

    /**
     * Non-static and non-final so that it can be reset after configuration is read
     */
    private Logger log = Logger.getLogger(getClass());

    @Override
	public void start() throws Exception {
        // obtain the root workflow config
		Config config = ConfigContext.getCurrentContextConfig();

        boolean log4jFileExists = checkPropertiesFileExists(config.getProperty(Config.LOG4J_SETTINGS_PATH));

        // first check for in-line xml configuration
		String log4jconfig = config.getProperty(Config.LOG4J_SETTINGS_XML);
		if (log4jconfig != null) {
			try {
				DocumentBuilder b = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document doc = b.parse(new ByteArrayInputStream(log4jconfig.getBytes()));
				DOMConfigurator.configure(doc.getDocumentElement());
				// now get the reconfigured log instance
				log = Logger.getLogger(getClass());
			} catch (Exception e) {
				log.error("Error parsing Log4J configuration settings: " + log4jconfig, e);
			}
        // next check for in-line properties configuration
		} else if ((log4jconfig = config.getProperty(Config.LOG4J_SETTINGS_PROPS)) != null) {
			Properties p = new Properties(config.getProperties());
			try {
				p.load(new ByteArrayInputStream(log4jconfig.getBytes()));
				PropertyConfigurator.configure(p);
				log = Logger.getLogger(getClass());
			} catch (IOException ioe) {
				log.error("Error loading Log4J configuration settings: " + log4jconfig, ioe);
			}
        // check for an external file location specification
		} else if (log4jFileExists) {
			log.info("Configuring Log4J logging.");
			log4jconfig = config.getProperty(Config.LOG4J_SETTINGS_PATH);

            int reloadInterval = DEFAULT_RELOAD_INTERVAL;

            String log4jReloadInterval = config.getProperty(Config.LOG4J_SETTINGS_RELOADINTERVAL_MINS);
			if (log4jReloadInterval != null) {
				try {
                    reloadInterval = Integer.parseInt(log4jReloadInterval) * MINUTE;
				} catch (NumberFormatException nfe) {
					log.warn("Invalid reload interval: " + log4jReloadInterval + ", using default: " + DEFAULT_RELOAD_INTERVAL + " milliseconds");
				}
			}
            Log4jConfigurer.initLogging(log4jconfig, reloadInterval);
			log = Logger.getLogger(getClass());
		} else {
            Log4jConfigurer.initLogging(AUTOMATIC_LOGGING_CONFIG_URL);
            log = Logger.getLogger(getClass());
		}
		super.start();
	}

    /**
	 * Checks if the passed in file exists.
	 *
	 * @param log4jSettingsPath the file
	 * @return true if exists
	 */
	private boolean checkPropertiesFileExists(String log4jSettingsPath) {
		if (StringUtils.isBlank(log4jSettingsPath)) {
			return false;
		}
		
		boolean exists;

		try {
			exists = ResourceUtils.getFile(log4jSettingsPath).exists();
		} catch (FileNotFoundException e) {
			exists = false;
		}

		if (!exists) {
			System.out.println(LOG4J_FILE_NOT_FOUND + log4jSettingsPath);
		}

		return exists;
	}

}
