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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.test.TestUtilities;


/**
 * Tests that the extra classpath features of the plugin work as advertised.
 *
 * <p>Adds the test/src/org/kuali/rice/kew/plugin/classes directory to the extra classes on
 * the classpath.  Adds the test/src/org/kuali/rice/kew/plugin/lib directory to the extra
 * libs on the classpath.  Within the lib directory is a jar called extraclasspath.jar.
 * Inside this jar is a single resource called extraclasspath-lib.txt.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ExtraClassPathTest extends KEWTestCase {
    
    private PluginRegistry registry;

	@Override
	public void setUp() throws Exception {
		// we want to copy the ziptest plugin into the plugin directories before the
		// test harness starts up.  That way the plugin will be loaded at startup time.
        super.setUp();
		TestUtilities.initializePluginDirectories();
		String pluginZipFileLocation = getBaseDir() + "/src/test/resources/org/kuali/rice/kew/plugin/extraclasspathtest.zip";
		File pluginZipFile = new File(pluginZipFileLocation);
		assertTrue("File " + pluginZipFileLocation + " should exist", pluginZipFile.exists());
		assertTrue("File " + pluginZipFileLocation + " should be a file", pluginZipFile.isFile());
		FileUtils.copyFileToDirectory(pluginZipFile, TestUtilities.getPluginsDirectory());
		pluginZipFile = new File(TestUtilities.getPluginsDirectory(), pluginZipFile.getName());
		FileUtils.forceDeleteOnExit(pluginZipFile);
        registry = new PluginRegistryFactory().createPluginRegistry();
        registry.start();
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		if (registry != null) {
		    registry.stop();
		}
		TestUtilities.cleanupPluginDirectories();
	}
	
	@Test public void testExtraClassPath() throws Exception {
		// first of all, let's check that the plugin was loaded when the test harness started up
		List<PluginEnvironment> environments = registry.getPluginEnvironments();
		assertEquals("There should be 1 plugin environment.", 1, environments.size());

		PluginEnvironment environment = environments.get(0);
		assertEquals("Should be the extraclasspathtest plugin.", "extraclasspathtest", environment.getPlugin().getName().getLocalPart());

		// check that the properties were configured correctly
        String extraClassesDirName = environment.getPlugin().getConfig().getProperty(Config.EXTRA_CLASSES_DIR);
        String extraLibDirName = environment.getPlugin().getConfig().getProperty(Config.EXTRA_LIB_DIR);
		
		File extraClassesDir = new File(extraClassesDirName);
		assertTrue("extra classes dir (" + extraClassesDirName + ") should exist.", extraClassesDir.exists());
		assertTrue("extra classes dir (" + extraClassesDirName + ") should be a directory.", extraClassesDir.isDirectory());
		File extraLibDir = new File(extraLibDirName);
		assertTrue("extra lib dir (" + extraLibDirName + ") should exist.", extraLibDir.exists());
		assertTrue("extra lib dir (" + extraLibDirName + ") should be a directory.", extraLibDir.isDirectory());

		// now verify that the resources from the extra classes and extra lib dirs got loaded
		ClassLoader classLoader = environment.getPlugin().getClassLoader();
		assertNotNull("Resource should exist.", classLoader.getResource("extraclasspath-classes.txt"));
		assertNotNull("Resource should exist.", classLoader.getResource("extraclasspath-lib.txt"));
	}

}
