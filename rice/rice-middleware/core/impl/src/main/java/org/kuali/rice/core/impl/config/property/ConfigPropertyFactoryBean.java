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
import org.springframework.beans.factory.FactoryBean;

/**
 * 
 * A FactoryBean which reads the value of the specified config parameter name and returns it.
 * If the config parameter does not exist, it will return null. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ConfigPropertyFactoryBean implements FactoryBean {

	private String name;
	
	public Object getObject() throws Exception {
		return ConfigContext.getCurrentContextConfig().getProperty(name);
	}
	
	public Class getObjectType() {
		return String.class;
	}

	public boolean isSingleton() {
		return true;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

}
