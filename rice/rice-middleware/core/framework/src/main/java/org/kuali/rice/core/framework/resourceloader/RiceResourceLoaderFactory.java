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
package org.kuali.rice.core.framework.resourceloader;

import org.kuali.rice.core.api.config.CoreConfigHelper;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.resourceloader.ResourceLoader;

import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/**
 * Creates resource loader for rice spring context.
 * Uses config object to store QNames so everything is good with the current context classloader.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RiceResourceLoaderFactory {

	private static final String RICE_ROOT_RESOURCE_LOADER_NAME = "RICE_ROOT_RESOURCE_LOADER";
	private static final String RICE_SPRING_RESOURCE_LOADER_NAME = "RICE_SPRING_RESOURCE_LOADER_NAME";
	
	public static ResourceLoader createRootRiceResourceLoader(ServletContext context, List<String> springFileLocations, String prefix) {
		//FIXME: RICE MODULARITY
		//hack to not break the hack in ResourceLoaderContainer.moveKSBLoadersDownHack();
		if ("KSB".equals(prefix.toUpperCase())) {
			prefix = "K~S~B";
		}
		
		String applicationId = CoreConfigHelper.getApplicationId();
		final QName root = new QName(applicationId, prefix.toUpperCase() + "_" + RICE_ROOT_RESOURCE_LOADER_NAME);
		
		if (getRootResourceLoaderNames() == null || !getRootResourceLoaderNames().contains(root)) {
			addRootResourceLoaderName(root);
		}
		
		final QName spring = new QName(applicationId, prefix.toUpperCase() + "_" + RICE_SPRING_RESOURCE_LOADER_NAME);
		if (getSpringResourceLoaderNames() == null || !getSpringResourceLoaderNames().contains(root)) {
			addSpringResourceLoaderName(spring);
		}
		
		final ResourceLoader rootResourceLoader = new BaseResourceLoader(root, new SimpleServiceLocator());
		
		final ResourceLoader springResourceLoader = new SpringResourceLoader(spring, springFileLocations, context);
		rootResourceLoader.addResourceLoaderFirst(springResourceLoader);
		
		return rootResourceLoader;
	}

	public static Collection<BaseResourceLoader> getRootResourceLoaders() {
		final Collection<QName> names = getRootResourceLoaderNames();
		final Collection<BaseResourceLoader> loaders = new ArrayList<BaseResourceLoader>();
		for (QName name : names) {
			loaders.add((BaseResourceLoader) GlobalResourceLoader.getResourceLoader(name));
		}
		return loaders;
	}

	public static Collection<SpringResourceLoader> getSpringResourceLoaders() {
		final Collection<QName> names = getSpringResourceLoaderNames();
		final Collection<SpringResourceLoader> loaders = new ArrayList<SpringResourceLoader>();
		for (QName name : names) {
			loaders.add((SpringResourceLoader) GlobalResourceLoader.getResourceLoader(name));
		}
		return loaders;
	}

	@SuppressWarnings("unchecked")
	private static Collection<QName> getRootResourceLoaderNames() {
		return (Collection<QName>) ConfigContext.getCurrentContextConfig().getObject(RICE_ROOT_RESOURCE_LOADER_NAME);
	}

	private static void addRootResourceLoaderName(QName name) {
		@SuppressWarnings("unchecked")
		Collection<QName> names = (Collection<QName>) ConfigContext.getCurrentContextConfig().getObject(RICE_ROOT_RESOURCE_LOADER_NAME);
		if (names == null) {
			names = new ArrayList<QName>();
		}
		names.add(name);
		
		ConfigContext.getCurrentContextConfig().putObject(RICE_ROOT_RESOURCE_LOADER_NAME, names);
	}

	@SuppressWarnings("unchecked")
	private static Collection<QName> getSpringResourceLoaderNames() {
		return (Collection<QName>) ConfigContext.getCurrentContextConfig().getObject(RICE_SPRING_RESOURCE_LOADER_NAME);
	}

	private static void addSpringResourceLoaderName(QName name) {
		@SuppressWarnings("unchecked")
		Collection<QName> names = (Collection<QName>) ConfigContext.getCurrentContextConfig().getObject(RICE_SPRING_RESOURCE_LOADER_NAME);
		if (names == null) {
			names = new ArrayList<QName>();
		}
		names.add(name);
		
		ConfigContext.getCurrentContextConfig().putObject(RICE_SPRING_RESOURCE_LOADER_NAME, names);
	}

}
