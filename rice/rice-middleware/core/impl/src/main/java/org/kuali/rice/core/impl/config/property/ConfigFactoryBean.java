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
package org.kuali.rice.core.impl.config.property;

import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.springframework.beans.factory.FactoryBean;

import java.util.List;

public class ConfigFactoryBean implements FactoryBean<org.kuali.rice.core.api.config.property.Config> {

	private List<String> configLocations;
	private boolean initialize = false;

	public static String CONFIG_OVERRIDE_LOCATION;

	@Override
	public org.kuali.rice.core.api.config.property.Config getObject() throws Exception {
		
		org.kuali.rice.core.api.config.property.Config oldConfig = null;
		
		if (getConfigLocations() == null) {
			throw new ConfigurationException("No config locations declared, at least one is required");
		}

		if (ConfigContext.getCurrentContextConfig() != null) {
			oldConfig = ConfigContext.getCurrentContextConfig();
		}
		//SimpleConfig config = null;
		JAXBConfigImpl config = null;
		if (CONFIG_OVERRIDE_LOCATION != null) {
			config = new JAXBConfigImpl(CONFIG_OVERRIDE_LOCATION, oldConfig);		
		} else {
			config = new JAXBConfigImpl(getConfigLocations(), oldConfig);
		}

		config.parseConfig();
		
		if (initialize) {
			ConfigContext.init(config);
		}
		
		return config;
	}

	@Override
	public Class<Config> getObjectType() {
		return org.kuali.rice.core.api.config.property.Config.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public List<String> getConfigLocations() {
		return this.configLocations;
	}

	public void setConfigLocations(List<String> configLocations) {
		this.configLocations = configLocations;
	}
	
	public void setInitialize(boolean initialize) {
		this.initialize = initialize;
	}

}
