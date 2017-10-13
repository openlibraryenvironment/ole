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
package org.kuali.rice.krms.api.repository.language;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.krms.api.KrmsConstants;
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
 * Generated using JVM arguments -DNOT_BLANK=name,typeId 
 * Concrete model object implementation, immutable. 
 * Instances can be (un)marshalled to and from XML.
 * 
 * @see NaturalLanguageUsageContract
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
@XmlRootElement(name = NaturalLanguageUsage.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = NaturalLanguageUsage.Constants.TYPE_NAME, propOrder = {
    NaturalLanguageUsage.Elements.NAME,
    NaturalLanguageUsage.Elements.DESCRIPTION,
    NaturalLanguageUsage.Elements.NAMESPACE,
    NaturalLanguageUsage.Elements.ID,
    NaturalLanguageUsage.Elements.ACTIVE,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class NaturalLanguageUsage
    extends AbstractDataTransferObject
    implements NaturalLanguageUsageContract
{

    @XmlElement(name = Elements.NAME, required = false)
    private final String name;
    @XmlElement(name = Elements.DESCRIPTION, required = false)
    private final String description;
    @XmlElement(name = Elements.NAMESPACE, required = false)
    private final String namespace;
    @XmlElement(name = Elements.ID, required = false)
    private final String id;
    @XmlElement(name = Elements.ACTIVE, required = false)
    private final boolean active;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB. This constructor should never be called.
     * It is only present for use during JAXB unmarshalling.
     * 
     */
    private NaturalLanguageUsage() {
        this.name = null;
        this.description = null;
        this.namespace = null;
        this.id = null;
        this.active = false;
        this.versionNumber = null;
    }

    /**
     * Constructs an object from the given builder.  This constructor is private and should only ever be invoked from the builder.
     * 
     * @param builder the Builder from which to construct the object.
     * 
     */
    private NaturalLanguageUsage(Builder builder) {
        this.name = builder.getName();
        this.description = builder.getDescription();
        this.namespace = builder.getNamespace();
        this.id = builder.getId();
        this.active = builder.isActive();
        this.versionNumber = builder.getVersionNumber();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getNamespace() {
        return this.namespace;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }


    /**
     * A builder which can be used to construct {@link NaturalLanguageUsage} instances.  Enforces the constraints of the {@link NaturalLanguageUsageContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, NaturalLanguageUsageContract
    {

        private String name;
        private String description;
        private String namespace;
        private String id;
        private boolean active;
        private Long versionNumber;

        private Builder(String name, String namespace) {
            // TODO modify this constructor as needed to pass any required values and invoke the appropriate 'setter' methods
            setName(name);
            setNamespace(namespace);
        }

        public static Builder create(String name, String namespace) {
            // TODO modify as needed to pass any required values and add them to the signature of the 'create' method
            return new Builder(name, namespace);
        }

        public static Builder create(NaturalLanguageUsageContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            // TODO if create() is modified to accept required parameters, this will need to be modified
            Builder builder = create(contract.getName(), contract.getNamespace());
            builder.setId(contract.getId());
            builder.setActive(contract.isActive());
            builder.setDescription(contract.getDescription());
            builder.setVersionNumber(contract.getVersionNumber());
            return builder;
        }

        /**
         * Builds an instance of a NaturalLanguageUsage based on the current state of the builder.
         * 
         * @return the fully-constructed NaturalLanguageUsage.
         * 
         */
        public NaturalLanguageUsage build() {
            return new NaturalLanguageUsage(this);
        }

        @Override
        public boolean isActive() {
            return this.active;
        }

        @Override
        public String getDescription() {
            return this.description;
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
         * Sets the value of active on this builder to the given value.
         * 
         * @param active the active value to set.
         * 
         */
        public void setActive(boolean active) {
            this.active = active;
        }

        /**
         * Sets the value of description on this builder to the given value.
         * 
         * @param description the description value to set.
         * 
         */
        public void setDescription(String description) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.description = description;
        }

        /**
         * Sets the value of id on this builder to the given value.
         * 
         * @param id the id value to set., may be null, representing the Object has not been persisted, but must not be blank.
         * @throws IllegalArgumentException if the id is blank
         * 
         */
        public void setId(String id) {
            if (id != null && org.apache.commons.lang.StringUtils.isBlank(id)) {
                throw new IllegalArgumentException("id is blank");
            }
            this.id = id;
        }

        /**
         * Sets the value of name on this builder to the given value.
         * 
         * @param name the name value to set., must not be null or blank
         * @throws IllegalArgumentException if the name is null or blank
         * 
         */
        public void setName(String name) {
            if (org.apache.commons.lang.StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("name is null or blank");
            }
            this.name = name;
        }

        /**
         * Sets the value of namespace on this builder to the given value.
         * 
         * @param namespace the namespace value to set., must not be null or blank
         * @throws IllegalArgumentException if the namespace is null or blank
         * 
         */
        public void setNamespace(String namespace) {
            if (org.apache.commons.lang.StringUtils.isBlank(namespace)) {
                throw new IllegalArgumentException("namespace is null or blank");
            }
            this.namespace = namespace;
        }

        /**
         * Sets the value of versionNumber on this builder to the given value.
         * 
         * @param versionNumber the versionNumber value to set.
         * 
         */
        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

    }


    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "naturalLanguageUsage";
        final static String TYPE_NAME = "NaturalLanguageUsageType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String NAME = "name";
        final static String DESCRIPTION = "description";
        final static String NAMESPACE = "namespace";
        final static String ID = "id";
        final static String ACTIVE = "active";

    }

    public static class Cache {
        public static final String NAME = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0 + "/" + NaturalLanguageUsage.Constants.TYPE_NAME;
    }

}
