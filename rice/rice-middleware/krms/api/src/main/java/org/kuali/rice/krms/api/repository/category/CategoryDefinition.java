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
package org.kuali.rice.krms.api.repository.category;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Collection;

/**
 * An immutable representation of a category definition.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */

@XmlRootElement(name = CategoryDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = CategoryDefinition.Constants.TYPE_NAME, propOrder = {
		CategoryDefinition.Elements.ID,
		CategoryDefinition.Elements.NAME,
		CategoryDefinition.Elements.NAMESPACE,
        CoreConstants.CommonElements.VERSION_NUMBER,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class CategoryDefinition extends AbstractDataTransferObject implements CategoryDefinitionContract {

    private static final long serialVersionUID = -4748818967880857017L;

    @XmlElement(name = Elements.ID, required=true)
    private final String id;
    @XmlElement(name = Elements.NAME, required=true)
    private final String name;
    @XmlElement(name = Elements.NAMESPACE, required=true)
    private final String namespace;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
    * This constructor should never be called.  It is only present for use during JAXB unmarshalling.
    */
   private CategoryDefinition() {
       this.id = null;
       this.name = null;
       this.namespace = null;
       this.versionNumber = null;
   }

    /**
	 * Constructs a CategoryDefinition from the given builder.  This constructor is private and should only
	 * ever be invoked from the builder.
	 *
	 * @param builder the Builder from which to construct the CategoryDefinition
	 */
    private CategoryDefinition(Builder builder) {
        this.id = builder.getId();
        this.name = builder.getName();
        this.namespace = builder.getNamespace();
        this.versionNumber = builder.getVersionNumber();
    }

    @Override
	public String getId() {
		return this.id;
	}

    @Override
	public String getName() {
		return this.name;
	}

    @Override
	public String getNamespace() {
		return this.namespace;
	}

    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }

    /**
     * This builder is used to construct instances of CategoryDefinition.  It enforces the constraints of the {@link org.kuali.rice.krms.api.repository.category.CategoryDefinitionContract}.
     */
    public static class Builder implements CategoryDefinitionContract, ModelBuilder, Serializable {

        private static final long serialVersionUID = -5775478956373560840L;

        private String id;
        private String name;
        private String namespace;
        private Long versionNumber;

        /**
         * Private constructor for creating a builder with all of it's required attributes.
         *
         * @param id the CategoryDefinition id
         * @param name the CategoryDefinition name
         * @param namespace the CategoryDefinition namespace
         */
        private Builder(String id, String name, String namespace) {
            setId(id);
            setName(name);
            setNamespace(namespace);
        }

        /**
         * Creates a builder from the given parameters.
         *
         * @param id the CategoryDefinition id
         * @param name the CategoryDefinition name
         * @param namespace the CategoryDefinition namespace
         * @return an instance of the builder with the fields already populated
         * @throws IllegalArgumentException if the either the id, name or namespace is null or blank
         */
        public static Builder create(String id, String name, String namespace) {
            return new Builder(id, name, namespace);
        }

        /**
         * Creates a builder by populating it with data from the given {@link CategoryDefinition}.
         *
         * @param category the category from which to populate this builder
         * @return an instance of the builder populated with data from the contract
         */
        public static Builder create(CategoryDefinitionContract category) {
            if (category == null) {
                throw new IllegalArgumentException("contract is null");
            }
            Builder builder =  new Builder(category.getId(), category.getName(), category.getNamespace());
            builder.setVersionNumber(category.getVersionNumber());
            return builder;
        }

        /**
         * Sets the value of the id on this builder to the given value.
         *
         * @param id the id value to set, must be null or non-blank
         * @throws IllegalArgumentException if the id is non-null and blank
         */
        public void setId(String id) {
            if (null != id && StringUtils.isBlank(id)) {
                throw new IllegalArgumentException("id must be null or non-blank");
            }
            this.id = id;
        }

        /**
         * Sets the name for the category definition that will be returned by this builder.
         * The name must not be null or blank.
         *
         * @param name the name to set on this builder, must not be null or blank
         *
         * @throws IllegalArgumentException if the given name is null or blank
         */
        public void setName(String name) {
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("name is blank");
            }
            this.name = name;
        }

        /**
         * Sets the namespace code for the category definition that will be returned by this builder.
         * The namespace must not be null or blank.
         *
         * @param namespace the namespace code to set on this builder, must not be null or blank
         *
         * @throws IllegalArgumentException if the given namespace is null or blank
         */
        public void setNamespace(String namespace) {
            if (StringUtils.isBlank(namespace)) {
                throw new IllegalArgumentException("namespace is blank");
            }
            this.namespace = namespace;
        }

        /**
         * Sets the version number on this builder to the given value.
         *
         * @param versionNumber the version number to set
         */
        public void setVersionNumber(Long versionNumber){
            this.versionNumber = versionNumber;
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getNamespace() {
            return this.namespace;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        /**
         * Builds an instance of a CategoryDefinition based on the current state of the builder.
         *
         * @return the fully-constructed CampusType
         */
        @Override
        public CategoryDefinition build() {
            return new CategoryDefinition(this);
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "category";
        final static String TYPE_NAME = "CategoryType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshalled to XML.
     */
    public static class Elements {
        final static String ID = "id";
        final static String NAME = "name";
        final static String NAMESPACE = "namespace";
    }
}


