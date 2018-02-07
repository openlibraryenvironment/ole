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
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinitionContract;
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
 * Generated using JVM arguments -DNOT_BLANK=name 
 * Concrete model object implementation, immutable. 
 * Instances can be (un)marshalled to and from XML.
 * 
 * @see NaturalLanguageTemplateAttributeContract
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
@XmlRootElement(name = NaturalLanguageTemplateAttribute.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = NaturalLanguageTemplateAttribute.Constants.TYPE_NAME, propOrder = {
    NaturalLanguageTemplateAttribute.Elements.NATURAL_LANGUAGE_TEMPLATE_ID,
    CoreConstants.CommonElements.VERSION_NUMBER,
    NaturalLanguageTemplateAttribute.Elements.VALUE,
    NaturalLanguageTemplateAttribute.Elements.ATTRIBUTE_DEFINITION_ID,
    NaturalLanguageTemplateAttribute.Elements.ATTRIBUTE_DEFINITION,
    NaturalLanguageTemplateAttribute.Elements.ID,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class NaturalLanguageTemplateAttribute
    extends AbstractDataTransferObject
    implements NaturalLanguageTemplateAttributeContract
{

    @XmlElement(name = Elements.NATURAL_LANGUAGE_TEMPLATE_ID, required = false)
    private final String naturalLanguageTemplateId;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    @XmlElement(name = Elements.VALUE, required = false)
    private final String value;
    @XmlElement(name = Elements.ATTRIBUTE_DEFINITION_ID, required = false)
    private final String attributeDefinitionId;
    @XmlElement(name = Elements.ATTRIBUTE_DEFINITION, required = false)
    private final KrmsAttributeDefinition attributeDefinition;
    @XmlElement(name = Elements.ID, required = false)
    private final String id;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB. This constructor should never be called.
     * It is only present for use during JAXB unmarshalling.
     * 
     */
    private NaturalLanguageTemplateAttribute() {
        this.naturalLanguageTemplateId = null;
        this.versionNumber = null;
        this.value = null;
        this.attributeDefinitionId = null;
        this.attributeDefinition = null;
        this.id = null;
    }

    /**
     * Constructs an object from the given builder.  This constructor is private and should only ever be invoked from the builder.
     * 
     * @param builder the Builder from which to construct the object.
     * 
     */
    private NaturalLanguageTemplateAttribute(Builder builder) {
        this.naturalLanguageTemplateId = builder.getNaturalLanguageTemplateId();
        this.versionNumber = builder.getVersionNumber();
        this.value = builder.getValue();
        this.attributeDefinitionId = builder.getAttributeDefinitionId();
        if (builder.getAttributeDefinition() != null) {
            this.attributeDefinition = builder.getAttributeDefinition().build();
        } else {
            this.attributeDefinition = null;
        }
        this.id = builder.getId();
    }

    @Override
    public String getNaturalLanguageTemplateId() {
        return this.naturalLanguageTemplateId;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getAttributeDefinitionId() {
        return this.attributeDefinitionId;
    }

    @Override
    public KrmsAttributeDefinition getAttributeDefinition() {
        return this.attributeDefinition;
    }

    @Override
    public String getId() {
        return this.id;
    }


    /**
     * A builder which can be used to construct {@link NaturalLanguageTemplateAttribute} instances.  Enforces the constraints of the {@link NaturalLanguageTemplateAttributeContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, NaturalLanguageTemplateAttributeContract
    {

        private String naturalLanguageTemplateId;
        private Long versionNumber;
        private String value;
        private String attributeDefinitionId;
        private KrmsAttributeDefinition.Builder attributeDefinition;
        private String id;

        private Builder() {
            // TODO modify this constructor as needed to pass any required values and invoke the appropriate 'setter' methods
        }

        public static Builder create() {
            // TODO modify as needed to pass any required values and add them to the signature of the 'create' method
            return new Builder();
        }

        public static Builder create(NaturalLanguageTemplateAttributeContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            // TODO if create() is modified to accept required parameters, this will need to be modified
            Builder builder = create();
            builder.setId(contract.getId());
            if (contract.getAttributeDefinition() != null) {
                builder.setAttributeDefinition(KrmsAttributeDefinition.Builder.create(contract.getAttributeDefinition()));
            }
            builder.setAttributeDefinitionId(contract.getAttributeDefinitionId());
            builder.setNaturalLanguageTemplateId(contract.getNaturalLanguageTemplateId());
            builder.setValue(contract.getValue());
            builder.setVersionNumber(contract.getVersionNumber());
            return builder;
        }

        /**
         * Builds an instance of a NaturalLanguageTemplateAttribute based on the current state of the builder.
         * 
         * @return the fully-constructed NaturalLanguageTemplateAttribute.
         * 
         */
        public NaturalLanguageTemplateAttribute build() {
            return new NaturalLanguageTemplateAttribute(this);
        }

        @Override
        public KrmsAttributeDefinition.Builder getAttributeDefinition() {
            return this.attributeDefinition;
        }

        @Override
        public String getAttributeDefinitionId() {
            return this.attributeDefinitionId;
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public String getNaturalLanguageTemplateId() {
            return this.naturalLanguageTemplateId;
        }

        @Override
        public String getValue() {
            return this.value;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        public void setAttributeDefinition(KrmsAttributeDefinition.Builder attributeDefinition) {
                this.attributeDefinition = attributeDefinition;
        }

        /**
         * Sets the value of attributeDefinitionId on this builder to the given value.
         * 
         * @param attributeDefinitionId the attributeDefinitionId value to set.
         * 
         */
        public void setAttributeDefinitionId(String attributeDefinitionId) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.attributeDefinitionId = attributeDefinitionId;
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
         * Sets the value of naturalLanguageTemplateId on this builder to the given value.
         * 
         * @param naturalLanguageTemplateId the naturalLanguageTemplateId value to set.
         * 
         */
        public void setNaturalLanguageTemplateId(String naturalLanguageTemplateId) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.naturalLanguageTemplateId = naturalLanguageTemplateId;
        }

        /**
         * Sets the value of value on this builder to the given value.
         * 
         * @param value the value value to set.
         * 
         */
        public void setValue(String value) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.value = value;
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

        final static String ROOT_ELEMENT_NAME = "naturalLanguageTemplateAttribute";
        final static String TYPE_NAME = "NaturalLanguageTemplateAttributeType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String NATURAL_LANGUAGE_TEMPLATE_ID = "naturalLanguageTemplateId";
        final static String VALUE = "value";
        final static String ATTRIBUTE_DEFINITION_ID = "attributeDefinitionId";
        final static String ATTRIBUTE_DEFINITION = "attributeDefinition";
        final static String ID = "id";

    }

}
