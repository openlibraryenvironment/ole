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

import org.kuali.rice.core.api.config.property.ConfigContext;

/**
 * This initializes a config object into the ConfigContext
 * as a root config or merges a config object into an already initialized root config  
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ConfigInitializer {
	
	private ConfigInitializer() {
		throw new UnsupportedOperationException("do not call");
	}
	
	/** 
	 * inits the root config or merges this config with the root config.
	 * 
	 * <p>
	 * This logic used to happen in the RiceConfigurer but was moved to facilitate the modularity work.
	 * </p>
	 */
	public static void initialize(org.kuali.rice.core.api.config.property.Config config) {
		final org.kuali.rice.core.api.config.property.Config rootConfig = ConfigContext.getCurrentContextConfig();
		if (rootConfig == null) {
			ConfigContext.init(config);
		} else {
			rootConfig.putConfig(config);
		}
	}
}
