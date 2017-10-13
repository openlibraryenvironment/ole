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
package org.kuali.rice.kew.api.rule;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.w3c.dom.Element;

@XmlRootElement(name = RuleTemplateAttribute.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RuleTemplateAttribute.Constants.TYPE_NAME, propOrder = {
    RuleTemplateAttribute.Elements.DEFAULT_VALUE,
    RuleTemplateAttribute.Elements.RULE_TEMPLATE_ID,
    RuleTemplateAttribute.Elements.REQUIRED,
    RuleTemplateAttribute.Elements.DISPLAY_ORDER,
    RuleTemplateAttribute.Elements.RULE_ATTRIBUTE,
    RuleTemplateAttribute.Elements.RULE_EXTENSIONS,
    RuleTemplateAttribute.Elements.ID,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.OBJECT_ID,
    RuleTemplateAttribute.Elements.ACTIVE,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class RuleTemplateAttribute
    extends AbstractDataTransferObject
    implements RuleTemplateAttributeContract,
        Comparable<RuleTemplateAttribute>
{

    @XmlElement(name = Elements.DEFAULT_VALUE, required = false)
    private final String defaultValue;
    @XmlElement(name = Elements.RULE_TEMPLATE_ID, required = true)
    private final String ruleTemplateId;
    @XmlElement(name = Elements.REQUIRED, required = true)
    private final boolean required;
    @XmlElement(name = Elements.DISPLAY_ORDER, required = true)
    private final Integer displayOrder;
    @XmlElement(name = Elements.RULE_ATTRIBUTE, required = true)
    private final ExtensionDefinition ruleAttribute;
    @XmlElement(name = Elements.RULE_EXTENSIONS, required = false)
    @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
    private final Map<String, String> ruleExtensionMap;
    @XmlElement(name = Elements.ID, required = false)
    private final String id;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;
    @XmlElement(name = Elements.ACTIVE, required = true)
    private final boolean active;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private RuleTemplateAttribute() {
        this.defaultValue = null;
        this.ruleTemplateId = null;
        this.required = false;
        this.displayOrder = null;
        this.ruleAttribute = null;
        this.ruleExtensionMap = null;
        this.id = null;
        this.versionNumber = null;
        this.objectId = null;
        this.active = false;
    }

    private RuleTemplateAttribute(Builder builder) {
        this.defaultValue = builder.getDefaultValue();
        this.ruleTemplateId = builder.getRuleTemplateId();
        this.required = builder.isRequired();
        this.displayOrder = builder.getDisplayOrder();
        this.ruleAttribute = builder.getRuleAttribute() == null ? null : builder.getRuleAttribute().build();
        this.ruleExtensionMap = builder.getRuleExtensionMap();
        this.id = builder.getId();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
        this.active = builder.isActive();
    }

    @Override
    public String getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public String getRuleTemplateId() {
        return this.ruleTemplateId;
    }

    @Override
    public boolean isRequired() {
        return this.required;
    }

    @Override
    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    @Override
    public ExtensionDefinition getRuleAttribute() {
        return this.ruleAttribute;
    }

    @Override
    public Map<String, String> getRuleExtensionMap() {
        return this.ruleExtensionMap;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    @Override
    public String getObjectId() {
        return this.objectId;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    public int compareTo(RuleTemplateAttribute ruleTemplateAttribute) {
        if ((this.getDisplayOrder() != null) && (ruleTemplateAttribute.getDisplayOrder() != null)) {
            return this.getDisplayOrder().compareTo(ruleTemplateAttribute.getDisplayOrder());
        }
        return 0;
    }


    /**
     * A builder which can be used to construct {@link RuleTemplateAttribute} instances.  Enforces the constraints of the {@link RuleTemplateAttributeContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, RuleTemplateAttributeContract
    {

        private String defaultValue;
        private String ruleTemplateId;
        private boolean required;
        private Integer displayOrder;
        private ExtensionDefinition.Builder ruleAttribute;
        private Map<String, String> ruleExtensionMap;
        private String id;
        private Long versionNumber;
        private String objectId;
        private boolean active;

        private Builder(String ruleTemplateId,
                ExtensionDefinition.Builder ruleAttribute,
                boolean required,
                Integer displayOrder) {
            setActive(true);
            setRuleTemplateId(ruleTemplateId);
            setRuleAttribute(ruleAttribute);
            setRequired(required);
            setDisplayOrder(displayOrder);
        }

        public static Builder create(String ruleTemplateId,
                ExtensionDefinition.Builder ruleAttribute,
                boolean required,
                Integer displayOrder) {
            return new Builder(ruleTemplateId, ruleAttribute, required, displayOrder);
        }

        public static Builder create(RuleTemplateAttributeContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(contract.getRuleTemplateId(),
                contract.getRuleAttribute()  == null ? null : ExtensionDefinition.Builder.create(contract.getRuleAttribute()),
                contract.isRequired(),
                contract.getDisplayOrder());
            builder.setDefaultValue(contract.getDefaultValue());
            builder.setRuleExtensionMap(contract.getRuleExtensionMap());
            builder.setId(contract.getId());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            builder.setActive(contract.isActive());
            return builder;
        }

        @Override
        public RuleTemplateAttribute build() {
            return new RuleTemplateAttribute(this);
        }

        @Override
        public String getDefaultValue() {
            return this.defaultValue;
        }

        @Override
        public String getRuleTemplateId() {
            return this.ruleTemplateId;
        }

        @Override
        public boolean isRequired() {
            return this.required;
        }

        @Override
        public Integer getDisplayOrder() {
            return this.displayOrder;
        }

        @Override
        public ExtensionDefinition.Builder getRuleAttribute() {
            return this.ruleAttribute;
        }

        @Override
        public Map<String, String> getRuleExtensionMap() {
            return this.ruleExtensionMap;
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        @Override
        public String getObjectId() {
            return this.objectId;
        }

        @Override
        public boolean isActive() {
            return this.active;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public void setRuleTemplateId(String ruleTemplateId) {
            if (StringUtils.isBlank(ruleTemplateId)) {
                throw new IllegalArgumentException("ruleTemplateId was null or blank");
            }
            this.ruleTemplateId = ruleTemplateId;
        }

        public void setRequired(boolean required) {
            this.required = required;
        }

        public void setDisplayOrder(Integer displayOrder) {
            if (displayOrder == null) {
                throw new IllegalArgumentException("displayOrder was null");
            }
            this.displayOrder = displayOrder;
        }

        public void setRuleAttribute(ExtensionDefinition.Builder ruleAttribute) {
            if (ruleAttribute == null) {
                throw new IllegalArgumentException("ruleAttribute was null");
            }
            this.ruleAttribute = ruleAttribute;
        }

        public void setRuleExtensionMap(Map<String, String> ruleExtensionMap) {
            this.ruleExtensionMap = ruleExtensionMap;
        }

        public void setId(String id) {
            if (StringUtils.isWhitespace(id)) {
                throw new IllegalArgumentException("id was whitespace");
            }
            this.id = id;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

    }


    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "ruleTemplateAttribute";
        final static String TYPE_NAME = "RuleTemplateAttributeType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String DEFAULT_VALUE = "defaultValue";
        final static String RULE_TEMPLATE_ID = "ruleTemplateId";
        final static String RULE_ATTRIBUTE_ID = "ruleAttributeId";
        final static String REQUIRED = "required";
        final static String DISPLAY_ORDER = "displayOrder";
        final static String RULE_ATTRIBUTE = "ruleAttribute";
        final static String RULE_EXTENSIONS = "ruleExtensionMap";
        final static String ID = "id";
        final static String ACTIVE = "active";

    }

}