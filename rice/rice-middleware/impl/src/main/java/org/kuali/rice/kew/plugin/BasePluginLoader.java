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
package org.kuali.rice.kew.plugin;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.config.CoreConfigHelper;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.core.api.util.ContextClassLoaderBinder;
import org.kuali.rice.core.api.util.xml.XmlException;

import javax.xml.namespace.QName;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Abstract base PluginLoader implementation.
 * Delegates to template methods to obtain plugin ClassLoader and plugin config file URL,
 * then load the config under the plugin ClassLoader, and constructs a Plugin object.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class BasePluginLoader implements PluginLoader {
    private static final Logger LOG = Logger.getLogger(BasePluginLoader.class);

    private static final String META_INF_PATH = "META-INF";
    private static final String PLUGIN_CONFIG_PATH = META_INF_PATH + "/workflow.xml";

    protected final String simplePluginName;
    protected String logPrefix;

    protected final ClassLoader parentClassLoader;
    protected final Config parentConfig;
    protected final File sharedPluginDirectory;
    protected String pluginConfigPath = PLUGIN_CONFIG_PATH;

    public BasePluginLoader(String simplePluginName, File sharedPluginDirectory, ClassLoader parentClassLoader, Config parentConfig) {
        this.sharedPluginDirectory = sharedPluginDirectory;
        if (parentClassLoader == null) {
            parentClassLoader = ClassLoaderUtils.getDefaultClassLoader();
        }
        this.parentClassLoader = parentClassLoader;
        this.parentConfig = parentConfig;
        this.simplePluginName = simplePluginName;
        this.logPrefix = simplePluginName;
    }

    protected String getLogPrefix() {
        return logPrefix;
    }
    
    public String getPluginName() {
        return simplePluginName;
    }

    public void setPluginConfigPath(String pluginConfigPath) {
        this.pluginConfigPath = pluginConfigPath;
    }

    protected String getSimplePluginName() {
    	return simplePluginName;
    }

    /**
     * Template method that subclasses should implement to supply an appropriate
     * plugin ClassLoader
     * @return an appropriate PluginClassLoader
     * @throws IOException if anything goes awry
     */
    protected abstract PluginClassLoader createPluginClassLoader() throws IOException;
    /**
     * Template method that subclasses should implement to supply an appropriate
     * URL to the plugin's configuration
     * @return an appropriate URL to the plugin's configuration
     * @throws IOException if anything goes awry
     */
    protected abstract URL getPluginConfigURL() throws PluginException, IOException;

    /**
     * Loads and creates the Plugin.
     */
    public Plugin load() throws Exception {
        final PluginClassLoader classLoader = createPluginClassLoader();
        LOG.info("Created plugin ClassLoader: " + classLoader);
        return ContextClassLoaderBinder.doInContextClassLoader(classLoader, new Callable<Plugin>() {
            public Plugin call() throws IOException {
                return loadWithinContextClassLoader(classLoader);
            }
        });
    }

    public boolean isRemoved() {
    	return false;
    }

    /**
     * Executes loading of the plugin within the current context classloader set to the Plugin's classloader.
     */
    protected Plugin loadWithinContextClassLoader(PluginClassLoader classLoader) throws PluginException, IOException {
    	URL url = getPluginConfigURL();
        PluginConfig pluginConfig = loadPluginConfig(url);
        QName qPluginName = getPluginName(pluginConfig);
        classLoader.setConfig(pluginConfig);
        ConfigContext.init(classLoader, pluginConfig);
        configureExtraClasspath(classLoader, pluginConfig);
        this.logPrefix = PluginUtils.getLogPrefix(qPluginName).toString();
        LOG.info("Constructing plugin '" + simplePluginName + "' with classloader: " + classLoader);
        Plugin plugin = new Plugin(qPluginName, pluginConfig, classLoader);
        installResourceLoader(plugin);
        installPluginListeners(plugin);
        return plugin;
    }

    protected void installResourceLoader(Plugin plugin) {
    	PluginUtils.installResourceLoader(plugin);
    }

    protected void installPluginListeners(Plugin plugin) {
    	PluginUtils.installPluginListeners(plugin);
    }

    protected void configureExtraClasspath(PluginClassLoader classLoader, PluginConfig config) throws MalformedURLException {
		String extraClassesDirs = config.getProperty(Config.EXTRA_CLASSES_DIR);
		if (!org.apache.commons.lang.StringUtils.isEmpty(extraClassesDirs)) {
			String[] extraClasses = extraClassesDirs.split(",");
			for (int index = 0; index < extraClasses.length; index++) {
				File extraClassesDir = new File(extraClasses[index]);
				if (extraClassesDir.exists()) {
					classLoader.addClassesDirectory(extraClassesDir);
				}
			}
		}
		String extraLibDirs = config.getProperty(Config.EXTRA_LIB_DIR);
		if (!org.apache.commons.lang.StringUtils.isEmpty(extraLibDirs)) {
			String[] extraLibs = extraLibDirs.split(",");
			for (int index = 0; index < extraLibs.length; index++) {
				File extraLibDir = new File(extraLibs[index]);
				if (extraLibDir.exists()) {
					classLoader.addLibDirectory(extraLibDir);
				}
			}
		}
	}


    protected QName getPluginName(PluginConfig pluginConfig) {
    	String applicationId = pluginConfig.getProperty(CoreConstants.Config.APPLICATION_ID);
    	QName qPluginName = null;
        if (StringUtils.isBlank(applicationId)) {
        	qPluginName = new QName(CoreConfigHelper.getApplicationId(), simplePluginName);
        } else {
        	qPluginName = new QName(applicationId, simplePluginName);
        }
    	return qPluginName;
    }

    protected PluginConfig loadPluginConfig(URL url) {
        PluginConfigParser parser = new PluginConfigParser();
        try {
            PluginConfig pluginConfig  = parser.parse(url, parentConfig);
            pluginConfig.parseConfig();
            return pluginConfig;
        } catch (FileNotFoundException e) {
            throw new PluginException(getLogPrefix() + " Could not locate the plugin config file at path " + url, e);
        } catch (IOException ioe) {
            throw new PluginException(getLogPrefix() + " Could not read the plugin config file", ioe);
        } catch (XmlException ixe) {
            throw new PluginException(getLogPrefix() + " Could not parse the plugin config file", ixe);
        }
    }
}
