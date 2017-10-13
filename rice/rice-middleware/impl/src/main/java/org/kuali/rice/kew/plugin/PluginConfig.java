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

import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.impl.config.property.ConfigParserImplConfig;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Class representing a plugin's config, containing configuration
 * settings parsed from the config.
 *
 * @see Config
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PluginConfig extends ConfigParserImplConfig {

	private String resourceLoaderClassname;
	private List listeners = new ArrayList();
	private Properties parentProperties;
	private Map parentObjects;
	private Config parentConfig;

	public PluginConfig(URL url, Config parentConfig) {
		super(url.toString());
		this.parentProperties = parentConfig.getProperties();
		this.parentObjects = parentConfig.getObjects();
		this.parentConfig = parentConfig;
	}

	public PluginConfig(File configFile, Config parentConfig) throws MalformedURLException {
		this(configFile.toURI().toURL(), parentConfig);
	}

	public Properties getBaseProperties() {
		return this.parentProperties;
	}

	public Map getBaseObjects() {
		return this.parentObjects;
	}

	public void addListener(String listenerClass) {
		listeners.add(listenerClass);
	}

	public List getListeners() {
		return listeners;
	}

	public void setResourceLoaderClassname(String resourceLoaderClassname) {
		this.resourceLoaderClassname = resourceLoaderClassname;
	}

	public String getResourceLoaderClassname() {
		return resourceLoaderClassname;
	}

    public String toString() {
        return "[PluginConfig: resourceLoaderClassname: " + getResourceLoaderClassname() + "]";
    }
    
	 
	@Override
	public Object getObject(String key) {
		Object object = super.getObject(key);
		if (object == null && parentConfig != null) {
			object = parentConfig.getObject(key);
		}
		return object;
	}

	@Override
	public Map<String, Object> getObjects() {
		Map<String, Object> finalObjects = new HashMap<String, Object>(super.getObjects());
		if (parentConfig != null) {
			Map<String, Object> parentObjects = parentConfig.getObjects();
			for (String key : parentObjects.keySet()) {
				if (!finalObjects.containsKey(key)) {
					finalObjects.put(key, parentObjects.get(key));
				}
			}
		}
		
		return finalObjects;
	}
}
