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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated using JVM arguments -DNOT_BLANK=languageCode,naturalLanguageUsageId,typeId,template -DFOREIGN_KEY=naturalLanguageUsageId:org.kuali.rice.krms.api.repository.language.NaturalLanguageUsage,typeId:org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition 
 * Concrete model object implementation, immutable. 
 * Instances can be (un)marshalled to and from XML.
 * 
 * @see NaturalLanguageTemplateContract
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
@XmlRootElement(name = NaturalLanguageTemplate.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = NaturalLanguageTemplate.Constants.TYPE_NAME, propOrder = {
    NaturalLanguageTemplate.Elements.ATTRIBUTES,
    NaturalLanguageTemplate.Elements.LANGUAGE_CODE,
    NaturalLanguageTemplate.Elements.NATURAL_LANGUAGE_USAGE_ID,
    NaturalLanguageTemplate.Elements.TYPE_ID,
    NaturalLanguageTemplate.Elements.TEMPLATE,
    NaturalLanguageTemplate.Elements.ID,
    NaturalLanguageTemplate.Elements.ACTIVE,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class NaturalLanguageTemplate
    extends AbstractDataTransferObject
    implements NaturalLanguageTemplateContract
{

    @XmlJavaTypeAdapter(org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter.class)
    @XmlElement(name = Elements.ATTRIBUTES, required = false)
    private final Map<String, String> attributes;
    @XmlElement(name = Elements.LANGUAGE_CODE, required = false)
    private final String languageCode;
    @XmlElement(name = Elements.NATURAL_LANGUAGE_USAGE_ID, required = false)
    private final String naturalLanguageUsageId;
    @XmlElement(name = Elements.TYPE_ID, required = false)
    private final String typeId;
    @XmlElement(name = Elements.TEMPLATE, required = false)
    private final String template;
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
    private NaturalLanguageTemplate() {
        this.attributes = null;
        this.languageCode = null;
        this.naturalLanguageUsageId = null;
        this.typeId = null;
        this.template = null;
        this.id = null;
        this.active = true;
        this.versionNumber = null;
    }

    /**
     * Constructs an object from the given builder.  This constructor is private and should only ever be invoked from the builder.
     * 
     * @param builder the Builder from which to construct the object.
     * 
     */
    private NaturalLanguageTemplate(Builder builder) {
        this.attributes = builder.getAttributes();
        this.languageCode = builder.getLanguageCode();
        this.naturalLanguageUsageId = builder.getNaturalLanguageUsageId();
        this.typeId = builder.getTypeId();
        this.template = builder.getTemplate();
        this.id = builder.getId();
        this.active = builder.isActive();
        this.versionNumber = builder.getVersionNumber();
    }

    @Override
    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getLanguageCode() {
        return this.languageCode;
    }

    @Override
    public String getNaturalLanguageUsageId() {
        return this.naturalLanguageUsageId;
    }

    @Override
    public String getTypeId() {
        return this.typeId;
    }

    @Override
    public String getTemplate() {
        return this.template;
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
     * A builder which can be used to construct {@link NaturalLanguageTemplate} instances.  Enforces the constraints of the {@link NaturalLanguageTemplateContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, NaturalLanguageTemplateContract
    {

        private Map<String, String> attributes;
        private String languageCode;
        private String naturalLanguageUsageId;
        private String typeId;
        private String template;
        private String id;
        private boolean active;
        private Long versionNumber;

        private Builder(String languageCode, String naturalLanguageUsageId, String template, String typeId) {
            // TODO modify this constructor as needed to pass any required values and invoke the appropriate 'setter' methods
            setAttributes(null); // setAttributes will create empty map for null
            setLanguageCode(languageCode);
            setNaturalLanguageUsageId(naturalLanguageUsageId);
            setTemplate(template);
            setTypeId(typeId);
        }

        public static Builder create(String languageCode, String naturalLanguageUsageId, String template, String typeId) {
            // TODO modify as needed to pass any required values and add them to the signature of the 'create' method
            return new Builder(languageCode, naturalLanguageUsageId, template, typeId);
        }

        public static Builder create(NaturalLanguageTemplateContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            // TODO if create() is modified to accept required parameters, this will need to be modified
            Builder builder = create(contract.getLanguageCode(), contract.getNaturalLanguageUsageId(), contract.getTemplate(), contract.getTypeId());
            builder.setId(contract.getId());
            builder.setActive(contract.isActive());
            builder.setAttributes(contract.getAttributes());
            builder.setVersionNumber(contract.getVersionNumber());
            return builder;
        }

        /**
         * Builds an instance of a NaturalLanguageTemplate based on the current state of the builder.
         * 
         * @return the fully-constructed NaturalLanguageTemplate.
         * 
         */
        public NaturalLanguageTemplate build() {
            return new NaturalLanguageTemplate(this);
        }

        @Override
        public boolean isActive() {
            return this.active;
        }

        @Override
        public Map<String, String> getAttributes() {
            return this.attributes;
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public String getLanguageCode() {
            return this.languageCode;
        }

        @Override
        public String getNaturalLanguageUsageId() {
            return this.naturalLanguageUsageId;
        }

        @Override
        public String getTemplate() {
            return this.template;
        }

        @Override
        public String getTypeId() {
            return this.typeId;
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
         * Sets the Map of attributes as name / value pairs.
         * 
         * @param attributes a Map of name value String pairs representing the attributes.
         * 
         */
        public void setAttributes(Map<String, String> attributes) {
            if (attributes == null){
                this.attributes = Collections.emptyMap();
            } else {
                this.attributes = Collections.unmodifiableMap(attributes);
            }
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
         * Sets the value of languageCode on this builder to the given value.
         * 
         * @param languageCode the languageCode value to set., must not be null or blank
         * @throws IllegalArgumentException if the languageCode is null or blank
         * 
         */
        public void setLanguageCode(String languageCode) {
            if (org.apache.commons.lang.StringUtils.isBlank(languageCode)) {
                throw new IllegalArgumentException("languageCode is null or blank");
            }
            this.languageCode = languageCode;
        }

        /**
         * Sets the value of naturalLanguageUsageId on this builder to the given value.
         * 
         * @param naturalLanguageUsageId the naturalLanguageUsageId value to set., must not be null or blank
         * @throws IllegalArgumentException if the naturalLanguageUsageId is null or blank
         * 
         */
        public void setNaturalLanguageUsageId(String naturalLanguageUsageId) {
            if (org.apache.commons.lang.StringUtils.isBlank(naturalLanguageUsageId)) {
                throw new IllegalArgumentException("naturalLanguageUsageId is null or blank");
            }
            this.naturalLanguageUsageId = naturalLanguageUsageId;
        }

        /**
         * Sets the value of template on this builder to the given value.
         * 
         * @param template the template value to set., must not be null or blank
         * @throws IllegalArgumentException if the template is null or blank
         * 
         */
        public void setTemplate(String template) {
            if (org.apache.commons.lang.StringUtils.isBlank(template)) {
                throw new IllegalArgumentException("template is null or blank");
            }
            this.template = template;
        }

        /**
         * Sets the value of typeId on this builder to the given value.
         * 
         * @param typeId the typeId value to set., must not be null or blank
         * @throws IllegalArgumentException if the typeId is null or blank
         * 
         */
        public void setTypeId(String typeId) {
            if (org.apache.commons.lang.StringUtils.isBlank(typeId)) {
                throw new IllegalArgumentException("typeId is null or blank");
            }
            this.typeId = typeId;
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

        final static String ROOT_ELEMENT_NAME = "naturalLanguageTemplate";
        final static String TYPE_NAME = "NaturalLanguageTemplateType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String ATTRIBUTES = "attributes";
        final static String LANGUAGE_CODE = "languageCode";
        final static String NATURAL_LANGUAGE_USAGE_ID = "naturalLanguageUsageId";
        final static String TYPE_ID = "typeId";
        final static String TEMPLATE = "template";
        final static String ID = "id";
        final static String ACTIVE = "active";

    }

    public static class Cache {
        public static final String NAME = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0 + "/" + NaturalLanguageTemplate.Constants.TYPE_NAME;
    }

}
