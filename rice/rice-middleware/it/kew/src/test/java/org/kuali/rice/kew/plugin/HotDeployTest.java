/**
 * Copyright 2005-2013 The Kuali Foundation
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.xml.namespace.QName;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.kuali.rice.core.api.config.CoreConfigHelper;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.test.TestUtilities;


/**
 * Tests the HotDeployer and Reloader which handle hot deployment and hot reloading
 * of Plugins.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class HotDeployTest extends KEWTestCase {

	private File pluginDir;
	
	@Override
	public void setUp() throws Exception {
        super.setUp();
		TestUtilities.initializePluginDirectories();
		this.pluginDir = TestUtilities.getPluginsDirectory(); 
	}
		
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		TestUtilities.cleanupPluginDirectories();
	}

	@Test public void testHotDeploy() throws Exception {
		// Grab the ServerPluginRegistry
		PluginRegistry theRegistry = PluginUtils.getPluginRegistry();
		assertNotNull("PluginRegistry should exist.", theRegistry);
		assertTrue(theRegistry instanceof ServerPluginRegistry);
		ServerPluginRegistry registry = (ServerPluginRegistry)theRegistry; 
		
		// Let's shut down the asynchronous reloader and hot deployer because we want to do this synchronously.
		HotDeployer hotDeployer = registry.getHotDeployer();
		Reloader reloader = registry.getReloader();
		registry.stopHotDeployer();
		registry.stopReloader();
		
		// Assert that there are currently no plugins
		assertEquals("There should be no plugins.", 0, registry.getPluginEnvironments().size());
		assertEquals("Resource loader should have no children.", 0, registry.getResourceLoaders().size());
		
		// query the hot deployer directly about it's added and removed plugins
		assertEquals("There should be no plugins added.", 0, hotDeployer.getAddedPlugins().size());
		assertEquals("There should be no plugins removed.", 0, hotDeployer.getRemovedPlugins().size());
		hotDeployer.run();
		assertEquals("There should still be no plugins.", 0, registry.getPluginEnvironments().size());
		
		// now let's copy a plugin over and run the hot deployer
        String pluginZipFileLocation = getBaseDir() + "/src/test/resources/org/kuali/rice/kew/plugin/ziptest.zip";
		File pluginZipFile = new File(pluginZipFileLocation);
		assertTrue("Plugin file '" + pluginZipFileLocation + "' should exist", pluginZipFile.exists());
		assertTrue("Plugin file '" + pluginZipFileLocation + "' should be a file", pluginZipFile.isFile());
		FileUtils.copyFileToDirectory(pluginZipFile, pluginDir);
			
		assertEquals("There should be one plugin added.", 1, hotDeployer.getAddedPlugins().size());
		assertEquals("There should be no plugins removed.", 0, hotDeployer.getRemovedPlugins().size());
			
		hotDeployer.run();
			
		// the plugin should have been hot deployed
		assertEquals("Plugin should have been hot deployed.", 1, registry.getPluginEnvironments().size());
		
		// check added plugins again, it should now indicate no new added plugins
		assertEquals("There should be no plugins added.", 0, hotDeployer.getAddedPlugins().size());
		assertEquals("There should be no plugins removed.", 0, hotDeployer.getRemovedPlugins().size());
		
		// verify that the resource loading and the registry are sane and properly set up with the new plugin
		assertEquals("Resource loader should have 1 plugin child.", 1, registry.getResourceLoaders().size());
		Plugin plugin = (Plugin)registry.getResourceLoaders().get(0);
		assertEquals("Plugin has wrong name.", new QName(CoreConfigHelper.getApplicationId(), "ziptest"), plugin.getName());
		assertTrue("Plugin should be started.", plugin.isStarted());
		assertEquals("Plugin in resource loader and environment should be the same.", plugin, registry.getPluginEnvironment(plugin.getName().getLocalPart()).getPlugin());
		
		// The reloader should have a reference to the environment
		assertEquals("Reloader should have a reference to environment.", 1, reloader.getReloadables().size());
		
		// now remove the plugin and ensure that it goes away
		FileUtils.forceDelete(new File(pluginDir, "ziptest.zip"));
		assertEquals("There should be no plugins added.", 0, hotDeployer.getAddedPlugins().size());
		assertEquals("There should be one plugin removed.", 1, hotDeployer.getRemovedPlugins().size());
		hotDeployer.run();
		
		// verify that the resource loading and the registry no longer contain the plugin
		assertEquals("No plugins should be deployed.", 0, registry.getPluginEnvironments().size());
		assertEquals("Resource loader should have 0 plugin children.", 0, registry.getResourceLoaders().size());
		
		// also assert that the reloader no longer has a reference to the environment
		assertEquals("Reloader should no longer have reference to environment.", 0, reloader.getReloadables().size());
				
	}
	
	@Test public void testReloader() throws Exception {
		// Grab the ServerPluginRegistry
		PluginRegistry theRegistry = PluginUtils.getPluginRegistry();
		assertNotNull("PluginRegistry should exist.", theRegistry);
		assertTrue(theRegistry instanceof ServerPluginRegistry);
		ServerPluginRegistry registry = (ServerPluginRegistry)theRegistry; 
		
		// Let's shut down the asynchronous reloader and hot deployer because we want to do this synchronously.
		HotDeployer hotDeployer = registry.getHotDeployer();
		Reloader reloader = registry.getReloader();
		registry.stopHotDeployer();
		registry.stopReloader();
		
		// Assert that there are currently no plugins
		assertEquals("There should be no plugins.", 0, registry.getPluginEnvironments().size());
		assertEquals("Resource loader should have no children.", 0, registry.getResourceLoaders().size());
				
        // now let's copy a plugin over and run the hot deployer
        String pluginZipFileLocation = getBaseDir() + "/src/test/resources/org/kuali/rice/kew/plugin/ziptest.zip";
        File pluginZipFile = new File(pluginZipFileLocation);
        assertTrue("Plugin file '" + pluginZipFileLocation + "' should exist", pluginZipFile.exists());
        assertTrue("Plugin file '" + pluginZipFileLocation + "' should be a file", pluginZipFile.isFile());
        FileUtils.copyFileToDirectory(pluginZipFile, pluginDir);
		
		// update pluginZipFile to point to the copy
		pluginZipFile = new File(pluginDir, pluginZipFile.getName());
		assertTrue(pluginZipFile.exists());
		
		// execute a hot deploy
		hotDeployer.run();
			
		// the plugin should have been hot deployed
		assertEquals("Plugin should have been hot deployed.", 1, registry.getPluginEnvironments().size());
		assertEquals("Resource loader should have 1 plugin child.", 1, registry.getResourceLoaders().size());
		PluginEnvironment environment = registry.getPluginEnvironments().get(0);
		Plugin plugin = environment.getPlugin();
		assertTrue(environment.isReloadable());
		assertFalse(environment.isReloadNeeded());
		
		// let's attempt to execute a Reload
		reloader.run();
		
		// a reload should not have occurred here since nothing was updated
		assertTrue("Original plugin should still be running.", plugin.isStarted());
		assertEquals("Plugin should not have changed.", plugin, registry.getPluginEnvironments().get(0).getPlugin());
		
		// touch the plugin file and then reload
		FileUtils.touch(pluginZipFile);
		assertTrue("A reload should be needed now.", environment.isReloadNeeded());
		reloader.run();
		
		// the original plugin should now be stopped
		assertTrue("original plugin should be stopped.", !plugin.isStarted());
		assertEquals("There should only be one Plugin.", 1, registry.getResourceLoaders().size());
		
		PluginEnvironment newPluginEnvironment = registry.getPluginEnvironments().get(0);
		Plugin newPlugin = newPluginEnvironment.getPlugin();
		assertEquals("There should still only be one environment.", 1, registry.getPluginEnvironments().size());
		assertEquals("The plugin environments should still be the same.", environment, registry.getPluginEnvironments().get(0));
		
		assertFalse("The old and new plugins should be different.", newPlugin.equals(plugin));
		
		// verify that the resource loader was updated
		assertEquals("The resource loaders should have been updated with the new plugin.", newPlugin, registry.getResourceLoaders().get(0));
		
	}

	
}
