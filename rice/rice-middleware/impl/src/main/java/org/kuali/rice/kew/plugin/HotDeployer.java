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

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.kew.plugin.PluginUtils.PluginZipFileFilter;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Checks for plugins added to or removed from the configured plugin directories.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class HotDeployer implements Runnable {
	private static final Logger LOG = Logger.getLogger(HotDeployer.class);


	private PluginRegistry registry;
	private File sharedPluginDirectory;
	private List<String> pluginDirectories;

	public HotDeployer(PluginRegistry registry, File sharedPluginDirectory, List<String> pluginDirectories) {
		this.registry = registry;
		this.sharedPluginDirectory = sharedPluginDirectory;
		this.pluginDirectories = pluginDirectories;
	}

	public synchronized void run() {
		try {
			LOG.debug("Checking for added and removed plugins...");
			Set<PluginEnvironment> removedPlugins = getRemovedPlugins();
			for (PluginEnvironment pluginContext : removedPlugins) {
				LOG.info("Detected a removed plugin '" + pluginContext.getPluginName() + "', shutting down plugin.");
				try {
					pluginContext.unload();
					registry.removePluginEnvironment(pluginContext.getPluginName());
				} catch (Exception e) {
					LOG.error("Failed to unload plugin '" + pluginContext.getPluginName() + "'", e);
				}
			}
			Set<PluginEnvironment> addedPlugins = getAddedPlugins();
			for (PluginEnvironment pluginContext : addedPlugins) {
				try {
					LOG.info("Detected a new plugin.  Loading plugin...");
					pluginContext.load();
					LOG.info("...plugin '" + pluginContext.getPluginName() + "' loaded.");					
				} catch (Exception e) {
				    // see: KULRICE-1184
				    String pluginName= "<<unknown>>";
				    if (pluginContext.getPlugin() != null) {
				        pluginName = String.valueOf(pluginContext.getPluginName());
				    }
					LOG.error("Failed to load plugin '" + pluginName + "'", e);
				} finally {
				    // whether or not there is a load error, we want to add the environment to the registry, this prevents the registry
				    // continuously trying to hot deploy a bad plugin
				    registry.addPluginEnvironment(pluginContext);
				}
			}
		} catch (Exception e) {
			LOG.warn("Failed to check for hot deploy.", e);
		}
	}

	protected Set<PluginEnvironment> getRemovedPlugins() {
		Set<PluginEnvironment> removedPlugins = new HashSet<PluginEnvironment>();
		for (PluginEnvironment environment : registry.getPluginEnvironments()) {
			if (environment.getLoader().isRemoved()) {
				removedPlugins.add(environment);
			}
		}
		return removedPlugins;
	}

	protected Set<PluginEnvironment> getAddedPlugins() throws Exception {
		Set<PluginEnvironment> addedPlugins = new HashSet<PluginEnvironment>();
		Set<File> newPluginZipFiles = new HashSet<File>();
		// for now, this implementation should watch the plugin directories for more plugins
		// TODO somehow the code which checks for new plugins and which loads plugins initially needs to be
		// consolidated, maybe with some sort of set of PluginLocators? or something along those lines?
		for (String pluginDirName : pluginDirectories) {
			File pluginDir = new File(pluginDirName);
			if (pluginDir.exists() && pluginDir.isDirectory()) {
				File[] pluginDirFiles = pluginDir.listFiles(new PluginZipFileFilter());
				for (File pluginZip : pluginDirFiles) {
					int indexOf = pluginZip.getName().lastIndexOf(".zip");
					String pluginName = pluginZip.getName().substring(0, indexOf);
					// check to see if this plugin environment has already been loaded
					List<PluginEnvironment> currentEnvironments = registry.getPluginEnvironments();
					boolean pluginExists = false;
					for (PluginEnvironment environment : currentEnvironments) {
						if (environment.getPluginName().equals(pluginName)) {
							pluginExists = true;
							break;
						}
					}
					if (!pluginExists) {
						// make sure the plugin's not in the process of being copied
						long lastModified1 = pluginZip.lastModified();
						Thread.sleep(100);
						long lastModified2 = pluginZip.lastModified();
						if (lastModified1 == lastModified2) {
							newPluginZipFiles.add(pluginZip);
						} else {
							LOG.warn("Detected that the plugin zip is still being modified, holding off on hot deploy: " + pluginZip.getAbsolutePath());
						}
					}
				}
			}
		}

		ClassLoader parentClassLoader = ClassLoaderUtils.getDefaultClassLoader();
		Config parentConfig = ConfigContext.getCurrentContextConfig();
		for (File newPluginZipFile : newPluginZipFiles) {
			PluginLoader loader = new ZipFilePluginLoader(newPluginZipFile, sharedPluginDirectory, parentClassLoader, parentConfig);
			PluginEnvironment environment = new PluginEnvironment(loader, registry);
			addedPlugins.add(environment);
		}
		return addedPlugins;
	}

}
