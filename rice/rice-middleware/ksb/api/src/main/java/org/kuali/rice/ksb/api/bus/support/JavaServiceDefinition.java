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
package org.kuali.rice.ksb.api.bus.support;

import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.ksb.api.KsbApiConstants;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class JavaServiceDefinition extends AbstractServiceDefinition {
	
	private List<String> serviceInterfaces = new ArrayList<String>();

	@Override
	public String getType() {
		return KsbApiConstants.ServiceTypes.HTTP_INVOKER;
	}
	
	public List<String> getServiceInterfaces() {
		return this.serviceInterfaces;
	}
	public void setServiceInterfaces(List<String> serviceInterfaces) {
		this.serviceInterfaces = serviceInterfaces;
	}
	public void setServiceInterface(String serviceName) {
	    this.serviceInterfaces.add(serviceName);
	}
	public String getServiceInterface() {
		return this.serviceInterfaces.get(0);
	}
	
	@Override
	public void validate() {
		super.validate();
		if (this.serviceInterfaces == null || this.serviceInterfaces.isEmpty()) {
			Class<?>[] interfaces = ClassLoaderUtils.getInterfacesToProxy(getService(), null, null);
			for (Class<?> serviceInterfaceClass : interfaces) {
			    this.serviceInterfaces.add(serviceInterfaceClass.getName());
			}
		}
	}
	
	protected ServiceConfiguration configure() {
		return JavaServiceConfiguration.fromServiceDefinition(this);
	}
	
	/**
     * Defines some internal constants used on this class.
     */
    static class Constants {
    	final static String ROOT_ELEMENT_NAME = "javaServiceDefinition";
        final static String TYPE_NAME = "JavaServiceDefinitionType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshalled to XML.
     */
    protected static class Elements {
    	protected final static String SERVICE_INTERFACES = "serviceInterfaces";
    	protected final static String SERVICE_INTERFACE = "serviceInterface";
    }
	
}
