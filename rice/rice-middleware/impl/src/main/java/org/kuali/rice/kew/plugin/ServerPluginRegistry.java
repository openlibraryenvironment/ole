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

import org.kuali.rice.core.api.config.CoreConfigHelper;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.ResourceLoader;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.kew.plugin.PluginUtils.PluginZipFileFilter;

import javax.xml.namespace.QName;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * A PluginRegistry implementation which loads plugins from the file system on the server.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ServerPluginRegistry extends BasePluginRegistry {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ServerPluginRegistry.class);

	private List<String> pluginDirectories = new ArrayList<String>();
	private File sharedPluginDirectory;
	private Reloader reloader;
	private HotDeployer hotDeployer;

	private ScheduledExecutorService scheduledExecutor;
	private ScheduledFuture<?> reloaderFuture;
	private ScheduledFuture<?> hotDeployerFuture;


	public ServerPluginRegistry() {
		super(new QName(CoreConfigHelper.getApplicationId(), ResourceLoader.PLUGIN_REGISTRY_LOADER_NAME));
	}

	public void start() throws Exception {
        if (!isStarted()) {
		    LOG.info("Starting server Plugin Registry...");
		    scheduledExecutor = Executors.newScheduledThreadPool(2, new KEWThreadFactory());
		    sharedPluginDirectory = loadSharedPlugin();
		    reloader = new Reloader();
		    hotDeployer = new HotDeployer(this, sharedPluginDirectory, pluginDirectories);
		    loadPlugins(sharedPluginDirectory);
		    this.reloaderFuture = scheduledExecutor.scheduleWithFixedDelay(reloader, 5, 5, TimeUnit.SECONDS);
		    this.hotDeployerFuture = scheduledExecutor.scheduleWithFixedDelay(hotDeployer, 5, 5, TimeUnit.SECONDS);
		    super.start();
		    LOG.info("...server Plugin Registry successfully started.");
        }
	}

	public void stop() throws Exception {
        if (isStarted()) {
		    LOG.info("Stopping server Plugin Registry...");
		    stopReloader();
		    stopHotDeployer();
		    reloader = null;
		    hotDeployer = null;

		    if (scheduledExecutor != null) {
			    scheduledExecutor.shutdownNow();
			    scheduledExecutor = null;
		    }
		    super.stop();
		    LOG.info("...server Plugin Registry successfully stopped.");
        }
	}

	protected void stopReloader() {
		if (reloaderFuture != null) {
			if (!reloaderFuture.cancel(true)) {
				LOG.warn("Failed to cancel the plugin reloader.");
			}
			reloaderFuture = null;
		}
	}

	protected void stopHotDeployer() {
		if (hotDeployerFuture != null) {
			if (!hotDeployerFuture.cancel(true)) {
				LOG.warn("Failed to cancel the hot deployer.");
			}
			hotDeployerFuture = null;
		}
	}

	protected void loadPlugins(File sharedPluginDirectory) {
        Map<String, File> pluginLocations = new TreeMap<String, File>(new PluginNameComparator());
		PluginZipFileFilter pluginFilter = new PluginZipFileFilter();
        Set<File> visitedFiles = new HashSet<File>();
        for (String pluginDir : pluginDirectories) {
            LOG.info("Reading plugins from " + pluginDir);
            File file = new File(pluginDir);
            if (visitedFiles.contains(file)) {
                LOG.info("Skipping visited directory: " + pluginDir);
                continue;
            }
            visitedFiles.add(file);
            if (!file.exists() || !file.isDirectory()) {
                LOG.warn(file.getAbsoluteFile()+" is not a valid plugin directory.");
                continue;
            }
            File[] pluginZips = file.listFiles(pluginFilter);
            for (int i = 0; i < pluginZips.length; i++) {
                File pluginZip = pluginZips[i];
                int indexOf = pluginZip.getName().lastIndexOf(".zip");
                String pluginName = pluginZip.getName().substring(0, indexOf);
                if (pluginLocations.containsKey(pluginName)) {
                	LOG.warn("There already exists an installed plugin with the name '"+ pluginName + "', ignoring plugin " + pluginZip.getAbsolutePath());
                	continue;
                }
                pluginLocations.put(pluginName, pluginZip);
            }
        }
        for (String pluginName : pluginLocations.keySet()) {
        	File pluginZipFile = pluginLocations.get(pluginName);
        	try {
        		LOG.info("Loading plugin '" + pluginName + "'");
        		ClassLoader parentClassLoader = ClassLoaderUtils.getDefaultClassLoader();
        		Config parentConfig = ConfigContext.getCurrentContextConfig();
        		ZipFilePluginLoader loader = new ZipFilePluginLoader(pluginZipFile,
        				sharedPluginDirectory,
        				parentClassLoader,
        				parentConfig);
        		PluginEnvironment environment = new PluginEnvironment(loader, this);
        		try {
        		    environment.load();
        		} finally {
        		    // regardless of whether the plugin loads or not, let's add it to the environment
        		    addPluginEnvironment(environment);
        		}        		
        	} catch (Exception e) {
        		LOG.error("Failed to read workflow plugin '"+pluginName+"'", e);
        	}
        }
    }

	@Override
	public void addPluginEnvironment(PluginEnvironment pluginEnvironment) {
		super.addPluginEnvironment(pluginEnvironment);
		reloader.addReloadable(pluginEnvironment);
	}

	@Override
	public PluginEnvironment removePluginEnvironment(String pluginName) {
		PluginEnvironment environment = super.removePluginEnvironment(pluginName);
		reloader.removeReloadable(environment);
		return environment;
	}

	public File loadSharedPlugin() {
		return PluginUtils.findSharedDirectory(pluginDirectories);
	}

	public void setPluginDirectories(List<String> pluginDirectories) {
		this.pluginDirectories = pluginDirectories;
	}

	public void setSharedPluginDirectory(File sharedPluginDirectory) {
		this.sharedPluginDirectory = sharedPluginDirectory;
	}

	protected HotDeployer getHotDeployer() {
		return hotDeployer;
	}

	protected Reloader getReloader() {
		return reloader;
	}
	
	private static class KEWThreadFactory implements ThreadFactory {
		
		private ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();
		
		public Thread newThread(Runnable runnable) {
			Thread thread = defaultThreadFactory.newThread(runnable);
			thread.setName("ServerPluginRegistry-" + thread.getName());
			return thread;
	    }
	}

}
