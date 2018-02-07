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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = SoapServiceConfiguration.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = SoapServiceConfiguration.Constants.TYPE_NAME, propOrder = {
		SoapServiceConfiguration.Elements.SERVICE_INTERFACE,
		SoapServiceConfiguration.Elements.JAX_WS_SERVICE
})
public class SoapServiceConfiguration extends AbstractServiceConfiguration {

	private static final long serialVersionUID = -4226512121638441108L;

	@XmlElement(name = Elements.SERVICE_INTERFACE, required = true)
	private final String serviceInterface;
	
	@XmlElement(name = Elements.JAX_WS_SERVICE, required = true)
	private final boolean jaxWsService;
	
	/**
     * Private constructor used only by JAXB.
     */
	private SoapServiceConfiguration() {
		super();
		this.serviceInterface = null;
		this.jaxWsService = false;
	}
	
	private SoapServiceConfiguration(Builder builder) {
		super(builder);
		this.serviceInterface = builder.getServiceInterface();
		this.jaxWsService = builder.isJaxWsService();
	}
	
	public static SoapServiceConfiguration fromServiceDefinition(SoapServiceDefinition soapServiceDefinition) {
		return Builder.create(soapServiceDefinition).build();
	}
	
	public String getServiceInterface() {
		return serviceInterface;
	}

	public boolean isJaxWsService() {
		return jaxWsService;
	}
	
	public static final class Builder extends AbstractServiceConfiguration.Builder<SoapServiceConfiguration> {

		private static final long serialVersionUID = 722267174667364588L;

		private String serviceInterface;
		private boolean jaxWsService = false;		
				
		public String getServiceInterface() {
			return serviceInterface;
		}

		public void setServiceInterface(String serviceInterface) {
			this.serviceInterface = serviceInterface;
		}

		public boolean isJaxWsService() {
			return jaxWsService;
		}

		public void setJaxWsService(boolean jaxWsService) {
			this.jaxWsService = jaxWsService;
		}

		private Builder() {}
		
		public static Builder create() {
			return new Builder();
		}
		
		public static Builder create(SoapServiceDefinition soapServiceDefinition) {
			Builder builder = create();
			builder.copyServiceDefinitionProperties(soapServiceDefinition);
			builder.setServiceInterface(soapServiceDefinition.getServiceInterface());
			builder.setJaxWsService(soapServiceDefinition.isJaxWsService());
			return builder;
		}

		@Override
		public SoapServiceConfiguration build() {
			return new SoapServiceConfiguration(this);
		}
		
	}
	
	/**
     * Defines some internal constants used on this class.
     */
    static class Constants {
    	final static String ROOT_ELEMENT_NAME = "soapServiceConfiguration";
        final static String TYPE_NAME = "SoapServiceConfigurationType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshalled to XML.
     */
     static class Elements {
    	protected final static String SERVICE_INTERFACE = "serviceInterface";
    	protected final static String JAX_WS_SERVICE = "jaxWsService";
    }
	
}
