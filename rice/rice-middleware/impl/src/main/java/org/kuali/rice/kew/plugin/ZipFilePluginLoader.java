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

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.kew.api.WorkflowRuntimeException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * Loads a plugin from a zip file on the file system.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ZipFilePluginLoader extends BasePluginLoader {
	private static final Logger LOG = Logger.getLogger(ZipFilePluginLoader.class);

	private final File pluginZipFile;
    private final File extractionDirectory;
    private long zipFileLastModified = -1;
    private boolean loadFailed = false;

    private static String validatePluginZipFile(File pluginZipFile) {
        PluginUtils.validatePluginZipFile(pluginZipFile);
        String fileName = pluginZipFile.getName();
        int indexOf = fileName.lastIndexOf(".zip");
        return fileName.substring(0, indexOf);
    }

    public ZipFilePluginLoader(File pluginZipFile, File sharedPluginDirectory, ClassLoader parentClassLoader, Config parentConfig) {
    	super(validatePluginZipFile(pluginZipFile), sharedPluginDirectory, parentClassLoader, parentConfig);
    	this.pluginZipFile = pluginZipFile;
    	this.extractionDirectory = determineExtractionDirectory(getSimplePluginName(), pluginZipFile);
    }
    
	public boolean isModified() {
		long currentZipFileLastModified = pluginZipFile.lastModified();
		if (zipFileLastModified == -1) {
			zipFileLastModified = currentZipFileLastModified;
			return false;
		} else if (currentZipFileLastModified > zipFileLastModified) {
			return !isZipFileStillBeingModified();
		}
		return false;
	}

	protected boolean isZipFileStillBeingModified() {
		long lastModified = pluginZipFile.lastModified();
		long size = pluginZipFile.length();
		// sleep for a fraction of a second and then check again if the values have changed
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
		if (lastModified != pluginZipFile.lastModified()) {
			return true;
		}
		if (size != pluginZipFile.length()) {
			return true;
		}
		return false;
	}

	public boolean isRemoved() {
		return pluginZipFile != null && !pluginZipFile.exists();
	}

    @Override
	public Plugin load() throws Exception {
        try {
            updateLastModified();
            extractPluginFiles();
            Plugin plugin = super.load();
            loadFailed = false;
            return plugin;
        } catch (Exception e) {
            loadFailed = true;
            throw e;
        }
	}

    protected File determineExtractionDirectory(String pluginName, File pluginZipFile) {
		return new File(pluginZipFile.getParentFile(), pluginName);
	}

    /**
     * Extracts the plugin files if necessary.
     */
    protected void extractPluginFiles() throws Exception {
    	if (isExtractNeeded()) {
    		// first, delete the current copy of the extracted plugin
    		if (extractionDirectory.exists()) {
    			// TODO how to handle locked files under windows?!?  This will throw an IOException in this case.
    			FileUtils.deleteDirectory(extractionDirectory);
    		}
    		if (!extractionDirectory.mkdir()) {
    			throw new WorkflowRuntimeException("Could not create the extraction directory for the plugin: " + extractionDirectory.getAbsolutePath());
    		}
    		ZipFile zipFile = new ZipFile(pluginZipFile, ZipFile.OPEN_READ);
    		for (Enumeration entries = zipFile.entries(); entries.hasMoreElements();) {
    			ZipEntry entry = (ZipEntry)entries.nextElement();
				File entryFile = new File(extractionDirectory + java.io.File.separator + entry.getName());
    			if (entry.isDirectory()) { // if its a directory, create it
    				if (!entryFile.mkdir()) {
    					throw new WorkflowRuntimeException("Failed to create directory: " + entryFile.getAbsolutePath());
    				}
    				continue;
    			}
    			InputStream is = null;
    			OutputStream os = null;
    			try {
    				is = new BufferedInputStream(zipFile.getInputStream(entry)); // get the input stream
    				os = new BufferedOutputStream(new FileOutputStream(entryFile));
    				while (is.available() > 0) {  // write contents of 'is' to 'fos'
    					os.write(is.read());
    				}
    			} finally {
    				if (os != null) {
    					os.close();
    				}
    				if (is != null) {
    					is.close();
    				}
    			}
    		}
    	}
    }
    
    /**
     * @return the loadFailed
     */
    public boolean isLoadFailed() {
        return this.loadFailed;
    }

    /**
     * An extract is required if the plugin has been modified or the last modified date of the zip file
     * is later than the last modified date of the extraction directory.
     */
    protected boolean isExtractNeeded() {
    	return isModified() || pluginZipFile.lastModified() > extractionDirectory.lastModified();
    }

    protected void updateLastModified() {
    	zipFileLastModified = pluginZipFile.lastModified();
    }

	protected PluginClassLoader createPluginClassLoader() throws MalformedURLException {
        LOG.info(getLogPrefix() + " Initiating loading of plugin from file system: " + extractionDirectory.getPath());
        LOG.info(getLogPrefix() + " Absolute path on file system is: " + extractionDirectory.getAbsolutePath());
        /* MalformedURLException should technically never be thrown as the URLs are coming from (presumably)
         * valid File objects
         */
        return new PluginClassLoader(parentClassLoader, sharedPluginDirectory, extractionDirectory);
    }

    protected URL getPluginConfigURL() throws PluginException, MalformedURLException {
        File pluginConfigFile = new File(extractionDirectory, pluginConfigPath);
        if (!pluginConfigFile.exists() || !pluginConfigFile.isFile()) {
            throw new PluginException(getLogPrefix() + " Could not locate the plugin config file at path " + pluginConfigFile.getAbsolutePath());
        }
        return pluginConfigFile.toURI().toURL();
    }
}
