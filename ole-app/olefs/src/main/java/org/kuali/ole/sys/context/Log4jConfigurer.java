/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.ole.sys.context;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This configurer serves 2 purposes:<br>
 * 1 - Provide the ability to override the bundled <code>log4j.properties<code> with a custom <code>log4j.properties</code> file<br>
 * 2 - Provide the ability to dynamically alter the log4j configuration at runtime by modifying a <code>log4j.properties</code> on
 * the file system that is being monitored for changes<br>
 * <br>
 * Unless the property <code>ole.fs.log4j.override</code> is set to <code>true</code>, this configurer takes no action and the
 * default log4j configuration bundled with the application is used.
 */
public class Log4jConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(Log4jConfigurer.class);
    private static final double MILLISECONDS_CONVERSION_MULTIPLIER = 60 * 1000;
    private static final NumberFormat NF = getNumberFormatter();

    /**
     * If you set the system property -Dlog4j.debug, you will see that the <code>isOverride()</code> method runs code that issues
     * logging statements. This means that log4 has already initialized itself and has issued a few logging statements prior to the
     * point where this method attempts to configure log4j with a custom <code>log4j.properties</code>.<br>
     * <br>
     * So this method is re-configuring log4j immediately after log4j uses its internal procedures to automatically configure
     * itself. <br>
     * <br>
     * Kinda funky, but it seems to work ok.<br>
     * <br>
     * Logging statements issued after this method finishes honor the new settings.
     */
    public static final void configureLogging(boolean doStartupStatsLogging) {
        boolean override = isOverride();
        logger.info(OLEConstants.LOG4J_OVERRIDE_KEY + "=" + override);
        if (!override) {
            return;
        }

        File customConfigFile = getCustomConfigFile();
        long reloadMillis = getReloadMillis();
        double minutes = reloadMillis / MILLISECONDS_CONVERSION_MULTIPLIER;
        logger.info("Reconfiguring log4j using [" + customConfigFile + "] Reload interval is " + NF.format(minutes) + " minutes");
        PropertyConfigurator.configureAndWatch(customConfigFile.getAbsolutePath(), reloadMillis);
        debugClasspath();
    }

    /**
     * Return true if we need to override the default log4j configuration with custom settings. False otherwise
     */
    protected static boolean isOverride() {
        String value = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.LOG4J_OVERRIDE_KEY);
        return Boolean.parseBoolean(value);
    }

    protected static File getCustomConfigFile() {
        String filename = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.LOG4J_SETTINGS_FILE_KEY);
        if (StringUtils.isBlank(filename)) {
            throw new IllegalStateException("log4j override requested, but the property " + OLEConstants.LOG4J_SETTINGS_FILE_KEY + " is blank");
        }
        File file = new File(filename);
        if (!file.exists()) {
            throw new IllegalStateException("[" + OLEConstants.LOG4J_SETTINGS_FILE_KEY + "=" + filename + "], but " + filename + " does not exist.");
        }
        if (!file.canRead()) {
            throw new IllegalStateException("[" + OLEConstants.LOG4J_SETTINGS_FILE_KEY + "=" + filename + "], but " + filename + " is not readable.");
        }
        return file;
    }

    protected static long getReloadMillis() {
        String s = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.LOG4J_RELOAD_MINUTES_KEY);
        try {
            Double minutes = new Double(s);
            Double millis = minutes * MILLISECONDS_CONVERSION_MULTIPLIER;
            return millis.longValue();
        }
        catch (NumberFormatException e) {
            throw new IllegalStateException("Could not parse '" + s + "'", e);
        }
    }

    protected static void debugClasspath() {
        URLClassLoader ucl = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        URL[] urls = ucl.getURLs();
        List<String> files = new ArrayList<String>();
        for (URL url : urls) {
            files.add(url.getFile());
        }
        Collections.sort(files);
        logger.debug("Located " + files.size() + " classpath entries");
        for (String file : files) {
            logger.debug("Classpath entry: " + file);
        }
    }

    protected static NumberFormat getNumberFormatter() {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(1);
        nf.setMinimumFractionDigits(1);
        return nf;
    }

}
