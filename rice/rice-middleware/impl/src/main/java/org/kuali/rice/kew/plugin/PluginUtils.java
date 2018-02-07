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
import org.kuali.rice.core.api.config.CoreConfigHelper;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.resourceloader.ResourceLoader;
import org.kuali.rice.core.framework.resourceloader.BaseResourceLoader;
import org.kuali.rice.core.impl.resourceloader.ResourceLoaderUtil;

import javax.xml.namespace.QName;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Iterator;
import java.util.List;


/**
 * Various plugin utilities.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class PluginUtils {

    private static final Logger LOG = Logger.getLogger(PluginUtils.class);
    private static final String SHARED_DIR = "shared";

    // maximum time we should wait for a new plugin directory to stop being
    // modified before we give up on loading it this time around
    public static final long INFINITE_MAX_WAIT = -1;
    // NOTE: must be greater than the SAFE TIME
    public static final long DEFAULT_MAX_WAIT = 90000;

    // amount of time since the last time the plugin dir was updated that we
    // consider "safe" to proceed with registering the plugin
    // basically, with the current implementation, an amount of time that we
    // expect any files in process of modification to complete (e.g. copy)
    // NOTE: MUST be LESS than the MAX WAIT otherwise, we will ALWAYS fail to wait
    public static final long DEFAULT_SAFE_TIME = 60000;

    private static final FilenameFilter JAR_FILES_FILTER = new FilenameFilter() {
        public boolean accept(File dir, String name) {
            return name.matches(".+\\.jar");
        }
    };

    private static final FileFilter SHARED_DIR_FILTER = new FileFilter() {
        public boolean accept(File file) {
            return file.isDirectory() && file.getName().equals(SHARED_DIR);
        }
    };
    
	private PluginUtils() {
		throw new UnsupportedOperationException("do not call");
	}

    public static class PluginZipFileFilter implements FileFilter {
        public boolean accept(File file) {
            return file.isFile() && file.getName().endsWith(".zip");
        }
    }

    public static String getLogPrefix(Plugin plugin) {
    	return getLogPrefix(plugin.getName());
    }

    public static String getLogPrefix(QName pluginName) {
    	return "[Plugin: " + pluginName + "]";
    }

    static File[] findJars(File libDir) {
        File[] jarFiles = new File[0];
        if (libDir.isDirectory()) {
            jarFiles = libDir.listFiles(JAR_FILES_FILTER);
        }
        return jarFiles;
    }

    public static File findSharedDirectory(List pluginDirectories) {
        for (Iterator iterator = pluginDirectories.iterator(); iterator.hasNext();) {
            File dir = new File((String) iterator.next());
            if (dir.isDirectory()) {
                File[] subDirs = dir.listFiles(SHARED_DIR_FILTER);
                if (subDirs.length > 0) {
                    return subDirs[0];
                }
            }
        }
        return null;
    }

    public static void validatePluginZipFile(File file) {
    	if (file == null) {
			throw new IllegalArgumentException("Given plugin file was 'null'");
		} else if (!file.exists()) {
			throw new IllegalArgumentException("Given plugin file does not exist: " + file.getAbsolutePath());
		} else if (!file.isFile()) {
			throw new IllegalArgumentException("Given plugin file is not a valid file: " + file.getAbsolutePath());
		} else if (!file.canRead()) {
			throw new IllegalArgumentException("Permission denied to read given plugin file: " + file.getAbsolutePath());
		} else if (!file.getName().endsWith(".zip")) {
			throw new IllegalArgumentException("Given plugin file does not end in .zip extension: " + file.getAbsolutePath());
		}
    	// now look at the directory the plugin file is in because we need to be able to extract it there
    	File pluginDirectory = file.getParentFile();
    	validatePluginDirectory(pluginDirectory);
    	// also verify that we can write to this directory
    	if (pluginDirectory == null) {
    		throw new IllegalArgumentException("Given plugin directory was 'null'");
    	} else if (!pluginDirectory.canWrite()) {
    		throw new IllegalArgumentException("Impossible to write to plugin directory so plugin cannot be expanded: " + pluginDirectory.getAbsolutePath());
    	}
    }

    public static void validatePluginDirectory(File directory) {
    	if (directory == null) {
			throw new IllegalArgumentException("Given directory was 'null'");
		} else if (!directory.exists()) {
			throw new IllegalArgumentException("Given directory does not exist: " + directory.getAbsolutePath());
		} else if (!directory.isDirectory()) {
			throw new IllegalArgumentException("Given plugin directory is not a valid directory: " + directory.getAbsolutePath());
		} else if (!directory.canRead()) {
			throw new IllegalArgumentException("Permission denied to read given plugin directory: " + directory.getAbsolutePath());
		}
    }

    public static PluginRegistry getPluginRegistry() {
    	return (PluginRegistry)GlobalResourceLoader.getResourceLoader(new QName(CoreConfigHelper.getApplicationId(), ResourceLoader.PLUGIN_REGISTRY_LOADER_NAME));
    }

    public static void installResourceLoader(Plugin plugin) {
    	if (plugin.getConfig() instanceof PluginConfig) {
			PluginConfig pluginConfig = (PluginConfig)plugin.getConfig();
			if (!StringUtils.isEmpty(pluginConfig.getResourceLoaderClassname())) {
                ResourceLoader resourceLoader = (ResourceLoader) ResourceLoaderUtil.createObject(pluginConfig.getResourceLoaderClassname(), plugin.getClassLoader());
                if (resourceLoader == null) {
                    LOG.warn("Could not create resource loader from plugin resource loader class: " + pluginConfig.getResourceLoaderClassname());
                    // if null, use a default resource loader
					resourceLoader = new BaseResourceLoader(plugin.getName());
                }
				plugin.addResourceLoader(resourceLoader);
			}
		}
    }

    public static void installPluginListeners(Plugin plugin) {
    	if (plugin.getConfig() instanceof PluginConfig) {
			PluginConfig pluginConfig = (PluginConfig)plugin.getConfig();
			for (Iterator iterator = pluginConfig.getListeners().iterator(); iterator.hasNext();) {
	            String pluginListenerClassName = (String) iterator.next();
	            try {
	                Class listenerClass = Class.forName(pluginListenerClassName, true, plugin.getClassLoader());
	                plugin.addPluginListener((PluginListener)listenerClass.newInstance());
	            } catch (ClassNotFoundException e) {
	                throw new PluginException(getLogPrefix(plugin)+" Error finding listener class '"+pluginListenerClassName+"'.", e);
	            } catch (InstantiationException e) {
	                throw new PluginException(getLogPrefix(plugin)+" Error creating an instance of listener class '"+pluginListenerClassName+"'.", e);
	            } catch (IllegalAccessException e) {
	                throw new PluginException(getLogPrefix(plugin)+" Error creating an instance of listener class '"+pluginListenerClassName+"'.", e);
	            } catch (ClassCastException e) {
	                throw new PluginException(getLogPrefix(plugin)+" Listener class '"+pluginListenerClassName+"' does not implement PluginListener.", e);
	            }
	        }
		}
    }
}
