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

/**
 * A PluginEnvironment represents a Plugin and the PluginLoader from which it was loaded.
 * Grouping these together allows us to execute a reload of the Plugin since the Plugin
 * itself is not responsible for handling how it's own loading or reloading.
 * 
 * <p>The PluginEnvironment also keeps a reference to the PluginRegistry because this
 * allows it to add and remove Plugins as child resource loaders of the registry.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PluginEnvironment implements Reloadable {

	private boolean loaded = false;
	private Plugin plugin;
	private final PluginLoader loader;
	private final PluginRegistry registry;
	private boolean suppressStartupFailure = true;
	
	/**
	 * Constructs an unloaded PluginEnvironment from the given PluginLoader and PluginRegistry.
	 * The environment will not be loaded until the first time that load() is executed.
	 */
	public PluginEnvironment(PluginLoader loader, PluginRegistry registry) {
		this.loader = loader;
		this.registry = registry;
	}
	
	/**
	 * Constructs a PluginEnvironment representing the given loaded Plugin.  Environments created
	 * in this manner will indicate that they are loaded from the moment they are constructed.
	 */
	public PluginEnvironment(Plugin plugin, PluginLoader loader, PluginRegistry registry) {
		this(loader, registry);
		this.plugin = plugin;
		this.loaded = true;
	}

	/**
	 * Returns true if this environment has been loaded, false otherwise.  If this method returns
	 * false then getPlugin() may return null if the environment was never loaded after construction.
	 */
	public boolean isLoaded() {
		return loaded;
	}
	
    /**
	 * Returns a boolean indicating whether or not this PluginEnvironment is truly reloadable.
	 * 
	 * This will return false if the Plugin represents a plugin which can not be reloaded.
	 */
	public boolean isReloadable() {
	    return true;
	}

	/**
	 * Indicates whether or not a reload of this environment is needed.  If this method
	 * returns true, it should continue to return true until a reload() is executed.
	 */
	public synchronized boolean isReloadNeeded() {
		return loader.isModified();
	}
	
	/**
	 * Reloads the environment by effectively executing an unload() followed by a load().
	 */
	public synchronized void reload() throws Exception {
		unload();
		load();
	}
	
	/**
	 * Loads the plugin from the PluginLoader.  This will also start the Plugin and add it
	 * to the PluginRegistry's resoure loaders.
	 */
	public synchronized void load() throws Exception {
	    plugin = loader.load();
		plugin.setSuppressStartupFailure(suppressStartupFailure);
		// it's important that the plugin is added to the resource loading system prior to startup because
		// startup may need to grab services
		registry.addResourceLoader(plugin);
		plugin.start();
		loaded = true;
	}

	/**
	 * Unloads the plugin.  The effectively shuts the plugin down and removes it from the
	 * PluginRegistry's resource loaders.
	 * @throws Exception
	 */
	public synchronized void unload() throws Exception {
	    if (plugin != null) {
	        plugin.stop();
	        // it's important that the plugin be removed from the resource loading system after shutdown in
	        // case the plugin needs to access the resource loader during shutdown
	        registry.removeResourceLoader(plugin.getName());
	    }
		loaded = false;
	}
	
	public String getPluginName() {
	    if (getPlugin() != null) {
	        return getPlugin().getName().getLocalPart();
	    }
	    return loader.getPluginName();
	}
	
	/**
	 * Gets the Plugin represented by this environment.  May be null if this environment has not
	 * yet been loaded.
	 */
	public Plugin getPlugin() {
		return plugin;
	}

	/**
	 * Gets the PluginLoader which loaded the Plugin in this environment.
	 */
	public PluginLoader getLoader() {
		return loader;
	}
	
	/**
	 * By default, startup failure is suppressed.  If it is indicated that startup failure
	 * suppression is not desired, then startup errors will be thrown directly from calls to
	 * load().
	 */
	public void setSuppressStartupFailure(boolean suppressStartupFailure) {
		this.suppressStartupFailure = suppressStartupFailure;
	}
	
}
