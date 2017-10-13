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

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.core.impl.config.property.JAXBConfigImpl;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.test.TestUtilities;

import javax.xml.namespace.QName;
import java.io.File;

import static org.junit.Assert.*;

/**
 * Tests the ZipFilePluginLoader. The zip file which is checked in has the following format:
 * 
 * <pre>
 *   classes/
 *   |---&gt; test-classes.txt
 *   |---&gt; workflow2.xml
 *   lib/
 *   |---&gt; test.jar
 *   META-INF/
 *   |---&gt; workflow.xml
 * </pre>
 * 
 * <p>
 * The test.jar which is in the zip file has one resource in it which is named test-lib.txt.
 * 
 * <p>
 * The workflow.xml file has 2 params in it and includes the workflow2.xml file.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ZipFilePluginLoaderTest extends KEWTestCase {

    private Plugin plugin;
    private File pluginDir;

    @Before
    // public void setUp() throws Exception {
    // super.setUp();
    // Config config = ConfigContext.getCurrentContextConfig();
    // if (config == null) {
    // // because of previously running tests, the config might already be initialized
    // config = new SimpleConfig();
    // config.getProperties().put(Config.SERVICE_NAMESPACE, "KEW");
    // ConfigContext.init(config);
    // }
    // // from RiceTestCase if this ever get put into that hierarchy
    //
    // }
    //
    // @After
    // public void tearDown() throws Exception {
    // super.setUp();
    // try {
    // plugin.stop();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // try {
    // FileUtils.deleteDirectory(pluginDir);
    // } catch (Exception e) {
    //
    // }
    // }
    @Test
    public void testLoad() throws Exception {
	Config config = ConfigContext.getCurrentContextConfig();
	if (config == null) {
	    // because of previously running tests, the config might already be initialized
	    config = new JAXBConfigImpl();
	    config.putProperty(CoreConstants.Config.APPLICATION_ID, "KEW");
	    ConfigContext.init(config);
	}

	File pluginZipFile = new File(this.getBaseDir() + "/src/test/resources/org/kuali/rice/kew/plugin/ziptest.zip");
	assertTrue(pluginZipFile.exists());
	assertTrue(pluginZipFile.isFile());

	// create a temp directory to copy the zip file into
	pluginDir = TestUtilities.createTempDir();

	// copy the zip file
	FileUtils.copyFileToDirectory(pluginZipFile, pluginDir);
	pluginZipFile = new File(pluginDir, pluginZipFile.getName());
	assertTrue(pluginZipFile.exists());
	pluginZipFile.deleteOnExit();

	// create the ZipFilePluginLoader and load the plugin
	ZipFilePluginLoader loader = new ZipFilePluginLoader(pluginZipFile, null, ClassLoaderUtils.getDefaultClassLoader(),
		ConfigContext.getCurrentContextConfig());
	this.plugin = loader.load();
	assertNotNull("Plugin should have been successfully loaded.", plugin);
	// check the plugin name, it's QName should be '{KUALI}ziptest', it's plugin name should be 'ziptest'
	assertEquals("Plugin QName should be '{KUALI}ziptest'", new QName("KUALI", "ziptest"), plugin.getName());

	// start the plugin
	this.plugin.start();

	// verify that the plugin was extracted, should be in a directory named the same as the local part of the
	// QName
	File extractedDirectory = new File(pluginDir, plugin.getName().getLocalPart());
	assertTrue("Plugin should have been extracted.", extractedDirectory.exists());
	assertTrue(extractedDirectory.isDirectory());
	File[] files = extractedDirectory.listFiles();
	assertEquals("Should be 3 files", 3, files.length);

	// try loading some classes and checking that things got loaded properly
	assertNotNull("Resource should exist.", plugin.getClassLoader().getResource("lib-test.txt"));
	assertNotNull("Resource should exist.", plugin.getClassLoader().getResource("classes-test.txt"));

	// check the config values
	assertEquals(plugin.getConfig().getProperty("test.param.1"), "test.value.1");
	assertEquals(plugin.getConfig().getProperty("test.param.2"), "test.value.2");
	assertEquals(plugin.getConfig().getProperty("test.param.3"), "test.value.3");

	// verify the modification checks on the plugin which drive hot deployment
	assertFalse("Plugin should not be modifed at this point.", loader.isModified());
	// record the last modified date of the extracted directory
	long lastModified = pluginDir.lastModified();

	// sleep for a milliseconds before touching the file, this will help our last modified check so we don't
	// get the same value
	Thread.sleep(1000);

	// touch the zip file
	FileUtils.touch(pluginZipFile);
	assertTrue("Plugin should be modifed after zip file is touched.", loader.isModified());
	plugin.stop();

	// reload the plugin
	this.plugin = loader.load();
	
	this.plugin.start();
	assertFalse("After reload, plugin should no longer be modifed.", loader.isModified());

	// check the last modified date of the extracted directory
	assertTrue("The extracted directory should have been modified.", pluginDir.lastModified() > lastModified);

	try {
	    plugin.stop();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	try {
	    FileUtils.deleteDirectory(pluginDir);
	} catch (Exception e) {

	}
    }
}
