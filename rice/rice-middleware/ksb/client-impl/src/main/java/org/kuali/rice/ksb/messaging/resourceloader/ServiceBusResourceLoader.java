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
package org.kuali.rice.ksb.messaging.resourceloader;

import javax.xml.namespace.QName;

import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.core.api.resourceloader.ResourceLoader;
import org.kuali.rice.core.framework.resourceloader.BaseResourceLoader;
import org.kuali.rice.ksb.api.bus.ServiceBus;

/**
 * A simple {@link ResourceLoader} implementation which delegates {@link #getService(QName)}
 * calls to the {@link ServiceBus}. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ServiceBusResourceLoader extends BaseResourceLoader {

	private final ServiceBus serviceBus;
	
	public ServiceBusResourceLoader(QName resourceLoaderName, ServiceBus serviceBus) {
		super(resourceLoaderName);
		this.serviceBus = serviceBus;
	}
	
	@Override
	public Object getObject(ObjectDefinition definition) {
		// object remoting has been removed!
		return null;
	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.core.api.resourceloader.ServiceLocator#getService(javax.xml.namespace.QName)
	 */
	@Override
	public Object getService(QName serviceName) {
		return serviceBus.getService(serviceName);
	}

}
