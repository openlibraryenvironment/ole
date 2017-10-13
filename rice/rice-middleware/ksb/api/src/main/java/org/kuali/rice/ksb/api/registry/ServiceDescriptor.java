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
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;
import org.w3c.dom.Element;

/**
 * Immutable implementation of the {@link ServiceDescriptorContract} interface.
 * Includes a serialized XML representation of the {@link ServiceConfiguration}
 * for the service.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@XmlRootElement(name = ServiceDescriptor.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = ServiceDescriptor.Constants.TYPE_NAME, propOrder = {
    ServiceDescriptor.Elements.ID,
    ServiceDescriptor.Elements.DESCRIPTOR,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class ServiceDescriptor extends AbstractDataTransferObject
    implements ServiceDescriptorContract
{

	private static final long serialVersionUID = 4555599272613878634L;

	@XmlElement(name = Elements.ID, required = false)
    private final String id;
	
    @XmlElement(name = Elements.DESCRIPTOR, required = false)
    private final String descriptor;
    
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private ServiceDescriptor() {
        this.id = null;
        this.descriptor = null;
        this.versionNumber = null;
    }

    private ServiceDescriptor(Builder builder) {
        this.id = builder.getId();
        this.descriptor = builder.getDescriptor();
        this.versionNumber = builder.getVersionNumber();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getDescriptor() {
        return this.descriptor;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }


    /**
     * A builder which can be used to construct {@link ServiceDescriptor} instances.  Enforces the constraints of the {@link ServiceDescriptorContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, ServiceDescriptorContract
    {

		private static final long serialVersionUID = 4439417051199359358L;

		private String id;
        private String descriptor;
        private Long versionNumber;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(ServiceDescriptorContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            builder.setId(contract.getId());
            builder.setDescriptor(contract.getDescriptor());
            builder.setVersionNumber(contract.getVersionNumber());
            return builder;
        }

        public ServiceDescriptor build() {
            return new ServiceDescriptor(this);
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public String getDescriptor() {
            return this.descriptor;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setDescriptor(String descriptor) {
            this.descriptor = descriptor;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

    }


    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "serviceDescriptor";
        final static String TYPE_NAME = "ServiceDescriptorType";
    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String ID = "id";
        final static String DESCRIPTOR = "descriptor";

    }

}

