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
package org.kuali.rice.ksb.api.registry;

import java.io.Serializable;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.w3c.dom.Element;

/**
 * Immutable implementation of the {@link ServiceEndpointContract} interface.
 * Represents a service endpoint that has been published to the service registry.
 * Includes both a {@link ServiceInfo} and {@link ServiceDescriptor} which
 * compose the two different pieces of information about a service endpoint.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@XmlRootElement(name = ServiceEndpoint.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = ServiceEndpoint.Constants.TYPE_NAME, propOrder = {
		ServiceEndpoint.Elements.INFO,
		ServiceEndpoint.Elements.DESCRIPTOR,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class ServiceEndpoint extends AbstractDataTransferObject implements ServiceEndpointContract {

	private static final long serialVersionUID = -2295962329997871877L;
    
	@XmlElement(name = Elements.INFO, required = false)
    private final ServiceInfo info;
	
    @XmlElement(name = Elements.DESCRIPTOR, required = false)
    private final ServiceDescriptor descriptor;
    
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    private ServiceEndpoint() {
        this.info = null;
    	this.descriptor = null;
    }
    
    private ServiceEndpoint(Builder builder) {
        this.info = builder.getInfo().build();
        this.descriptor = builder.getDescriptor().build();
    }
    
    @Override
    public ServiceInfo getInfo() {
        return this.info;
    }
    
    @Override
    public ServiceDescriptor getDescriptor() {
        return this.descriptor;
    }

    /**
     * A builder which can be used to construct {@link ServiceEndpoint} instances.  Enforces the constraints of the {@link ServiceEndpointContract}.
     */
    public final static class Builder implements Serializable, ModelBuilder, ServiceEndpointContract {

		private static final long serialVersionUID = -1783718474634197837L;

		private ServiceInfo.Builder info;
        private ServiceDescriptor.Builder descriptor;

        private Builder(ServiceInfo.Builder info, ServiceDescriptor.Builder descriptor) {
            setInfo(info);
            setDescriptor(descriptor);
        }

        public static Builder create(ServiceInfo.Builder info, ServiceDescriptor.Builder descriptor) {
            return new Builder(info, descriptor);
        }

        public static Builder create(ServiceEndpointContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(ServiceInfo.Builder.create(contract.getInfo()), ServiceDescriptor.Builder.create(contract.getDescriptor()));
            return builder;
        }

        public ServiceEndpoint build() {
            return new ServiceEndpoint(this);
        }

        @Override
        public ServiceInfo.Builder getInfo() {
            return this.info;
        }
        
        @Override
        public ServiceDescriptor.Builder getDescriptor() {
            return this.descriptor;
        }
        
        public void setInfo(ServiceInfo.Builder info) {
            if (info == null) {
            	throw new IllegalArgumentException("info was null");
            }
            this.info = info;
        }

        public void setDescriptor(ServiceDescriptor.Builder descriptor) {
        	if (descriptor == null) {
            	throw new IllegalArgumentException("descriptor was null");
            }
            this.descriptor = descriptor;
        }
        
    }


    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "serviceEndpoint";
        final static String TYPE_NAME = "ServiceEndpointType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {

        final static String INFO = "info";
        final static String DESCRIPTOR = "descriptor";

    }
    
}
