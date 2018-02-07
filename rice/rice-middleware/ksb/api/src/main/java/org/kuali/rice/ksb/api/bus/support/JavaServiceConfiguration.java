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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = JavaServiceConfiguration.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = JavaServiceConfiguration.Constants.TYPE_NAME, propOrder = {
		JavaServiceConfiguration.Elements.SERVICE_INTERFACES
})
public final class JavaServiceConfiguration extends AbstractServiceConfiguration {

	private static final long serialVersionUID = -4226512121638441108L;

	@XmlElementWrapper(name = Elements.SERVICE_INTERFACES, required = false)
	@XmlElement(name = Elements.SERVICE_INTERFACE, required = false)
	private final List<String> serviceInterfaces;
	
	/**
     * Private constructor used only by JAXB.
     */
	private JavaServiceConfiguration() {
		super();
		this.serviceInterfaces = null;
	}

	
	private JavaServiceConfiguration(Builder builder) {
		super(builder);
		this.serviceInterfaces = builder.getServiceInterfaces() == null ? null : new ArrayList<String>(builder.getServiceInterfaces());
	}
	
	public static JavaServiceConfiguration fromServiceDefinition(JavaServiceDefinition javaServiceDefinition) {
		return Builder.create(javaServiceDefinition).build();
	}
		
	public List<String> getServiceInterfaces() {
		if (this.serviceInterfaces == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(this.serviceInterfaces);
	}
		
	public static final class Builder extends AbstractServiceConfiguration.Builder<JavaServiceConfiguration> {

		private static final long serialVersionUID = 4300659121377259098L;

		private List<String> serviceInterfaces;
				
		public List<String> getServiceInterfaces() {
			return serviceInterfaces;
		}
		
		public void setServiceInterfaces(List<String> serviceInterfaces) {
			if (serviceInterfaces == null) {
				this.serviceInterfaces = null;
			} else {
				this.serviceInterfaces = new ArrayList<String>(serviceInterfaces);
			}
		}
		
		private Builder() {}
		
		public static Builder create() {
			return new Builder();
		}
		
		public static Builder create(JavaServiceDefinition javaServiceDefinition) {
			Builder builder = create();
			builder.copyServiceDefinitionProperties(javaServiceDefinition);
			builder.setServiceInterfaces(javaServiceDefinition.getServiceInterfaces());
			return builder;
		}

		@Override
		public JavaServiceConfiguration build() {
			return new JavaServiceConfiguration(this);
		}
		
	}
	
	/**
     * Defines some internal constants used on this class.
     */
    static class Constants {
    	final static String ROOT_ELEMENT_NAME = "javaServiceConfiguration";
        final static String TYPE_NAME = "JavaServiceConfigurationType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshalled to XML.
     */
    static class Elements {
    	protected final static String SERVICE_INTERFACES = "serviceInterfaces";
    	protected final static String SERVICE_INTERFACE = "serviceInterface";
    }
	
}
