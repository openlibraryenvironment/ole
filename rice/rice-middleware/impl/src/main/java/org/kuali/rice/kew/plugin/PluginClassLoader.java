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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.core.api.util.collect.CollectionUtils;

/**
 * A simple class loader implementation which looks at itself before delegating to its parent.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PluginClassLoader extends URLClassLoader implements Lifecycle {//implements Modifiable {
    static final String CLASSES_DIR = "classes";
    static final String LIB_DIR = "lib";
    private static final String[] SYSTEM_CLASSES = new String[] { "java.", "javax.servlet.", "javax.xml.", "javax.management.", "org.xml.", "org.w3c." };

    //private ModificationTracker modTracker = new ModificationTracker();
    //this is purposely typed.
    private PluginConfig config;
    private boolean started = false;

    public PluginClassLoader() {
        super(new URL[0]);
    }

    public PluginClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
    }

    public PluginClassLoader(ClassLoader parent, File sharedDirectory, File pluginDirectory) throws MalformedURLException {
        super(new URL[0], parent);
        if (sharedDirectory != null) {
            addClassesDirectory(new File(sharedDirectory, CLASSES_DIR));
            addLibDirectory(new File(sharedDirectory, LIB_DIR));
        }
        addClassesDirectory(new File(pluginDirectory, CLASSES_DIR));
        addLibDirectory(new File(pluginDirectory, LIB_DIR));
    }

    public void addClassesDirectory(File classesDir) throws MalformedURLException {
        if (classesDir != null && classesDir.isDirectory()) {
            addURL(classesDir.toURI().toURL());
        }
    }

    public void addLibDirectory(File libDir) throws MalformedURLException {
        File[] jars = PluginUtils.findJars(libDir);
        for (int index = 0; index < jars.length; index++) {
            addURL(jars[index].toURI().toURL());
        }
    }

    public Class loadClass(String className) throws ClassNotFoundException {
        return loadClass(className, false);
    }

    public void addURL(URL url) {
        super.addURL(url);
        //modTracker.addURL(url);
    }

    //public boolean isModified() {
    //    return modTracker.isModified();
    //}

    public synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class loadedClass = loadExistingClass(name, resolve);
        if (loadedClass != null) {
            return loadedClass;
        }
        loadedClass = loadSystemClass(name, resolve);
        if (loadedClass != null) {
            return loadedClass;
        }
        loadedClass = loadLocalClass(name, resolve);
        if (loadedClass != null) {
            return loadedClass;
        }
        loadedClass = loadParentClass(name, resolve);
        if (loadedClass != null) {
            return loadedClass;
        }
        throw new ClassNotFoundException(name);
    }

    public URL getResource(String name) {
        URL resource = findResource(name);
        if (resource == null) {
            resource = getParent().getResource(name);
        }
        return resource;
    }

    public Enumeration<URL> getResources(String name) throws IOException {
    	Enumeration<URL> localResources = findResources(name);
    	Enumeration<URL> parentResources = getParent().getResources(name);
    	return CollectionUtils.concat(localResources, parentResources);
    }



    private Class loadExistingClass(String name, boolean resolve) {
        Class loadedClass = findLoadedClass(name);
        if (loadedClass != null && resolve) {
            resolveClass(loadedClass);
        }
        return loadedClass;
    }

    private Class loadSystemClass(String name, boolean resolve) {
    	Class loadedClass = null;
    	if (isSystemClass(name)) {
    		try {
    			loadedClass = getSystemClassLoader().loadClass(name);
    			if (loadedClass != null && resolve) {
    				resolveClass(loadedClass);
    			}
    		} catch (ClassNotFoundException e) {
    			// not found in system class loader
    		}
    	}
		return loadedClass;
    }

    private Class loadLocalClass(String name, boolean resolve) {
        Class loadedClass = null;
        try {
            loadedClass = findClass(name);
            if (loadedClass != null && resolve) {
                resolveClass(loadedClass);
            }
        } catch (ClassNotFoundException e) {
            // not found locally
        }
        return loadedClass;
    }

    private Class loadParentClass(String name, boolean resolve) {
        Class loadedClass = null;
        try {
            loadedClass = getParent().loadClass(name);
            if (loadedClass != null && resolve) {
                resolveClass(loadedClass);
            }
        } catch (ClassNotFoundException e) {
            // not found in parent
        }
        return loadedClass;
    }

    /**
     * This method modeled on the isSystemPath method in Jetty's ContextLoader.
     *
     * When loading classes from the system classloader, we really only want to load certain classes
     * from there so this will tell us whether or not the class name given is one we want to load
     * from the system classloader.
     */
    private boolean isSystemClass(String name) {
    	name = name.replace('/','.');
        while(name.startsWith(".")) {
        	name=name.substring(1);
        }
        for (int index = 0; index < SYSTEM_CLASSES.length; index++) {
        	String systemClass = SYSTEM_CLASSES[index];
        	if (systemClass.endsWith(".")) {
        		if (name.startsWith(systemClass)) {
        			return true;
        		}
        	}
        	else if (name.equals(systemClass)) {
        		return true;
        	}
        }
        return false;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("[PluginClassLoader: urls=");
        URL[] urls = getURLs();
        if (urls == null) {
            sb.append("null");
        } else {
            for (int i = 0; i < urls.length; i++) {
                sb.append(urls[i]);
                sb.append(",");
            }
            // remove trailing comma
            if (urls.length > 1) {
                sb.setLength(sb.length() - 1);
            }
        }
        sb.append("]");
        return sb.toString();
    }

	public PluginConfig getConfig() {
		return config;
	}

	public void setConfig(PluginConfig config) {
		this.config = config;
	}

	public void start() {
		started = true;
	}

	public void stop() {
		config = null;
		started = false;
	}

	public boolean isStarted() {
		return started;
	}
}
