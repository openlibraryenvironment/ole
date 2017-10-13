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
package org.kuali.rice.kim.api.responsibility;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.common.template.Template;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * An immutable representation of a {@link ResponsibilityContract}.
 *
 * <p>To construct an instance of a Permission, use the {@link org.kuali.rice.kim.api.responsibility.Responsibility.Builder} class.<p/>
 *
 * @see ResponsibilityContract
 */
@XmlRootElement(name = Responsibility.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = Responsibility.Constants.TYPE_NAME, propOrder = {
		Responsibility.Elements.ID,
		Responsibility.Elements.NAMESPACE_CODE,
		Responsibility.Elements.NAME,
		Responsibility.Elements.DESCRIPTION,
		Responsibility.Elements.TEMPLATE,
        Responsibility.Elements.ACTIVE,
        Responsibility.Elements.ATTRIBUTES,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class Responsibility extends AbstractDataTransferObject implements ResponsibilityContract {

	private static final long serialVersionUID = 1L;

    @XmlElement(name = Responsibility.Elements.ID, required = false)
    private final String id;

    @XmlElement(name = Responsibility.Elements.NAMESPACE_CODE, required = true)
    private final String namespaceCode;

    @XmlElement(name = Responsibility.Elements.NAME, required = true)
    private final String name;

    @XmlElement(name = Responsibility.Elements.DESCRIPTION, required = false)
    private final String description;

    @XmlElement(name = Responsibility.Elements.TEMPLATE, required = false)
    private final Template template;

    @XmlElement(name = Responsibility.Elements.ATTRIBUTES, required = false)
    @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
    private final Map<String, String> attributes;

    @XmlElement(name = Responsibility.Elements.ACTIVE, required = false)
    private boolean active;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
	 *  A constructor to be used only by JAXB unmarshalling.
	 *
	 */
	private Responsibility() {
		this.id = null;
        this.namespaceCode = null;
        this.name = null;
        this.description = null;
        this.template = null;
        this.attributes = null;
        this.active = false;
        this.versionNumber = Long.valueOf(1L);
        this.objectId = null;
	}

    /**
	 * A constructor using the Builder.
	 *
	 * @param builder
	 */
	private Responsibility(Builder builder) {
		this.id = builder.getId();
        this.namespaceCode = builder.getNamespaceCode();
        this.name = builder.getName();
        this.description = builder.getDescription();
        this.template = builder.getTemplate() != null ? builder.getTemplate().build() : null;
        this.attributes = builder.getAttributes() != null ? builder.getAttributes() : Collections.<String, String>emptyMap();
        this.active = builder.isActive();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
	}

	/**
	 * @see ResponsibilityContract#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * @see ResponsibilityContract#getNamespaceCode()
	 */
	@Override
	public String getNamespaceCode() {
		return namespaceCode;
	}

	/**
	 * @see ResponsibilityContract#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @see ResponsibilityContract#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * @see ResponsibilityContract#getTemplate()
	 */
	@Override
	public Template getTemplate() {
		return template;
	}

	/**
	 * @see org.kuali.rice.core.api.mo.common.active.Inactivatable#isActive()
	 */
	@Override
	public boolean isActive() {
		return active;
	}

	/**
	 *
	 * @see ResponsibilityContract#getAttributes()
	 */
	@Override
	public Map<String, String> getAttributes() {
		return this.attributes;
	}

	/**
	 * @see org.kuali.rice.core.api.mo.common.Versioned#getVersionNumber()
	 */
	@Override
	public Long getVersionNumber() {
		return versionNumber;
	}

	/**
	 * @see org.kuali.rice.core.api.mo.common.GloballyUnique#getObjectId()
	 */
	@Override
	public String getObjectId() {
		return objectId;
	}

    /**
     * This builder constructs a Responsibility enforcing the constraints of the {@link ResponsibilityContract}.
     */
    public static final class Builder implements ResponsibilityContract, ModelBuilder, Serializable {
        private String id;
        private String namespaceCode;
        private String name;
        private String description;
        private Template.Builder template;
        private Map<String, String> attributes;
        private Long versionNumber = 1L;
        private String objectId;
        private boolean active;

        private Builder(String namespaceCode, String name) {
            setNamespaceCode(namespaceCode);
            setName(name);
        }

        /**
         * Creates a Responsibility with the required fields.
         */
        public static Builder create(String namespaceCode, String name) {
            return new Builder(namespaceCode, name);
        }

        /**
         * Creates a Responsibility from an existing {@link ResponsibilityContract}.
         */
        public static Builder create(ResponsibilityContract contract) {
            Builder builder = new Builder(contract.getNamespaceCode(), contract.getName());
            builder.setId(contract.getId());
            builder.setDescription(contract.getDescription());
            if (contract.getAttributes() != null) {
                builder.setAttributes(contract.getAttributes());
            }
            builder.setActive(contract.isActive());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            if (contract.getTemplate() != null
                    && contract.getTemplate().getName() != null
                    && contract.getTemplate().getNamespaceCode() != null) {
                builder.setTemplate(Template.Builder.create(contract.getTemplate()));
            }

            return builder;
        }

        @Override
        public String getId() {
            return id;
        }

        public void setId(final String id) {
        	this.id = id;
        }
        
        @Override
        public String getNamespaceCode() {
            return namespaceCode;
        }

        public void setNamespaceCode(final String namespaceCode) {
        	if (StringUtils.isBlank(namespaceCode)) {
                throw new IllegalArgumentException("namespaceCode is blank");
            }
        	this.namespaceCode = namespaceCode;
        }

        @Override
        public String getName() {
            return name;
        }

        public void setName(final String name) {
        	if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("name is blank");
            }
        	this.name = name;
        }

		@Override
		public String getDescription() {
			return description;
		}
		
		public void setDescription(final String description) {
			this.description = description;
		}

		@Override
		public Template.Builder getTemplate() {
			return template;
		}
		
		public void setTemplate(final Template.Builder template) {
			if (template == null) {
                throw new IllegalArgumentException("template is null");
            }
            if (StringUtils.isNotBlank(template.getName())
                    && StringUtils.isNotBlank(template.getNamespaceCode())) {
			    this.template = template;
            } else {
                this.template = null;
            }

		}
		
		@Override
		public boolean isActive() {
			return active;
		}
		
		public void setActive(final boolean active) {
            this.active = active;
        }

		@Override
		public Long getVersionNumber() {
			return versionNumber;
		}

		public void setVersionNumber(final Long versionNumber) {
			if (versionNumber != null && versionNumber <= 0) {
	            throw new IllegalArgumentException("versionNumber is invalid");
	        }
			this.versionNumber = versionNumber;
	    }
		 
		@Override
		public String getObjectId() {
			return objectId;
		}

        public void setObjectId(final String objectId) {
            this.objectId = objectId;
        }

		@Override
		public Map<String, String> getAttributes() {
			return attributes;
		}
		
		public void setAttributes(Map<String, String> attributes) {
            this.attributes = Collections.unmodifiableMap(Maps.newHashMap(attributes));
        }
		
        @Override
        public Responsibility build() {
            return new Responsibility(this);
        }
    }
    
    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        static final String ROOT_ELEMENT_NAME = "responsibility";
        static final String TYPE_NAME = "ResponsibilityType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshalled to XML.
     */
    static class Elements {
        static final String ID = "id";
        static final String NAMESPACE_CODE = "namespaceCode";
        static final String NAME = "name";
        static final String DESCRIPTION = "description";
        static final String TEMPLATE = "template";
        static final String ATTRIBUTES = "attributes";
        static final String ACTIVE = "active";
    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + Responsibility.Constants.TYPE_NAME;
    }
}
