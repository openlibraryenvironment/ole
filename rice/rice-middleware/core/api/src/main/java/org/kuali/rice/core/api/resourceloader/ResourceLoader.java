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

import java.util.List;

import javax.xml.namespace.QName;

import org.kuali.rice.core.api.lifecycle.Lifecycle;

/**
 * A general purpose resource loader which fetches objects and services from the
 * approriate source.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ResourceLoader extends ObjectLoader, ServiceLocator, Lifecycle {

	public static final String KSB_CONFIGURER_CONTAINER_NAME = "ksbConfigurerContainer";
	public static final String ROOT_RESOURCE_LOADER_NAME = "ROOT_RESOURCE_LOADER_NAME";
	public static final String PLUGIN_REGISTRY_LOADER_NAME = "pluginRegistryResourceLoader";
	public static final String EMBEDDED_PLUGIN = "embeddedPlugin";
	public static final String WEB_SERVICE_PLUGIN = "webServicePlugin";
	public static final String RMI_PLUGIN = "RMIPlugin";
	public static final String EMBEDDED_CLIENT_APP_RESOURCE_LOADER = "embeddedClientApplicationResourceLoader";


	public void addResourceLoader(ResourceLoader resourceLoader);
	public void addResourceLoaderFirst(ResourceLoader resourceLoader);
	public ResourceLoader getResourceLoader(QName name);
	public List<QName> getResourceLoaderNames();
	public List<ResourceLoader> getResourceLoaders();
	public void removeResourceLoader(QName name);
	public void setName(QName name);
	public QName getName();
	public String getContents(String indent, boolean servicePerLine);
}
