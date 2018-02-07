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
package org.kuali.rice.core.api.resourceloader;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.CoreConfigHelper;
import org.kuali.rice.core.api.exception.RiceRemoteServiceConnectionException;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.core.api.util.ClassLoaderUtils;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper on all the Resource loaders.  This is what programmers typically use to get in the resource loading
 * framework.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class GlobalResourceLoader {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(GlobalResourceLoader.class);

	private static Map<ClassLoader, ResourceLoader> rootResourceLoaders = new HashMap<ClassLoader, ResourceLoader>();

	private static boolean initializing;

	public static synchronized ResourceLoader getResourceLoader() {
		ClassLoader classLoader = ClassLoaderUtils.getDefaultClassLoader();
		return getResourceLoaderCheckParent(classLoader);
	}

	private static synchronized ResourceLoader getResourceLoaderCheckParent(ClassLoader classLoader) {
        ResourceLoader resourceLoader = getResourceLoader(classLoader);

        if (resourceLoader != null && classLoader.getParent() != null) {
            ResourceLoader parentResourceLoader = getResourceLoaderCheckParent(classLoader.getParent());

            if (parentResourceLoader != null) {
                resourceLoader = new ParentChildResourceLoader(parentResourceLoader, resourceLoader);
		    }
	    }

	    if (resourceLoader == null && classLoader.getParent() != null) {
		    resourceLoader = getResourceLoaderCheckParent(classLoader.getParent());
	    }
	    return resourceLoader;
	}

	public static synchronized ResourceLoader getResourceLoader(ClassLoader classloader) {
		return rootResourceLoaders.get(classloader);
	}

	public static synchronized void start() throws Exception {
		try {
			initializing = true;
			ResourceLoader internalResourceLoader = getResourceLoader();
			if (internalResourceLoader == null) {
				throw new RiceRuntimeException("Cannot start GlobalResourceLoader because no resource loaders have been added for the current ContextClassLoader :" + Thread.currentThread().getContextClassLoader());
			}
			internalResourceLoader.start();
		} finally {
			initializing = false;
		}
	}

	public static synchronized void addResourceLoader(ResourceLoader resourceLoader) {
		initialize();
		if (resourceLoader == null) {
			throw new ResourceLoaderException("Attempted to add a null resource loader to the Global resource loader.");
		}
		LOG.info("Adding ResourceLoader " + resourceLoader.getName() + " to GlobalResourceLoader");
		getResourceLoader().addResourceLoader(resourceLoader);
	}

	public static synchronized void addResourceLoaderFirst(ResourceLoader resourceLoader) {
		initialize();
		if (resourceLoader == null) {
			throw new ResourceLoaderException("Attempted to add a null resource loader to the Global resource loader.");
		}
		LOG.info("Adding ResourceLoader " + resourceLoader.getName() + " to GlobalResourceLoader");
		getResourceLoader().addResourceLoaderFirst(resourceLoader);
	}

	protected static synchronized void initialize() {
		if (getResourceLoader(ClassLoaderUtils.getDefaultClassLoader()) == null) {
			LOG.info("Creating CompositeResourceLoader in GlobalResourceLoader");
			rootResourceLoaders.put(ClassLoaderUtils.getDefaultClassLoader(), new ResourceLoaderContainer(new QName(CoreConfigHelper.getApplicationId(), ResourceLoader.ROOT_RESOURCE_LOADER_NAME)));
		}
	}

	public static synchronized ResourceLoader getResourceLoader(QName name) {
		return getResourceLoader().getResourceLoader(name);
	}

	/**
	 * Stop the resource loader for the current context classloader.  Don't stop or clear them all
	 * because the stop was issued from the context of a single classloader.
	 *
	 * @throws Exception
	 */
	public static synchronized void stop() throws Exception {
		LOG.info("Stopping the GlobalResourceLoader...");
		if (getResourceLoader(ClassLoaderUtils.getDefaultClassLoader()) != null) {
			LOG.info("Destroying GlobalResourceLoader");
			getResourceLoader(ClassLoaderUtils.getDefaultClassLoader()).stop();
			rootResourceLoaders.remove(ClassLoaderUtils.getDefaultClassLoader());
		}
		LOG.info("...GlobalResourceLoader successfully stopped.");
	}

	public static <T extends Object> T getService(QName serviceName) {
		if (serviceName == null) {
			throw new IllegalArgumentException("The service name must be non-null.");
		}
		LOG.debug("GlobalResourceLoader fetching service " + serviceName);
		try {
			return getResourceLoader().<T>getService(serviceName);
		} catch (RiceRemoteServiceConnectionException ex) {
			LOG.warn(ex.getMessage());
			return null;
		}
	}

	public static <T extends Object> T getService(String localServiceName) {
		if (StringUtils.isEmpty(localServiceName)) {
			throw new IllegalArgumentException("The service name must be non-null.");
		}
		return GlobalResourceLoader.<T>getService(new QName(localServiceName));
	}

	public static <T extends Object> T getObject(ObjectDefinition objectDefinition) {
		return getResourceLoader().<T>getObject(objectDefinition);
	}

	public static boolean isInitialized() {
		return getResourceLoader() != null;
	}

	public static void logContents() {
		if (LOG.isInfoEnabled()) {
			LOG.info(getResourceLoader().getContents("", false));
		}
	}
	
	public static void logAllContents() {
		if (LOG.isInfoEnabled()) {
			LOG.info("######################### Logging All Contents ###########################");
			for (ResourceLoader rl : rootResourceLoaders.values()) {
				LOG.info("Logging contents for ResourceLoader: " + rl.getName() + "\n" + rl.getContents("  ", true));
			}
			LOG.info("###################### Done Logging All Contents #########################");
		}
	}

	public static synchronized boolean isInitializing() {
		return initializing;
	}

	public static synchronized void setInitializing(boolean initializing) {
		GlobalResourceLoader.initializing = initializing;
	}
}
