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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kew.api.KewApiConstants;
import org.w3c.dom.Element;

@XmlRootElement(name = RuleTemplate.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RuleTemplate.Constants.TYPE_NAME, propOrder = {
    RuleTemplate.Elements.NAME,
    RuleTemplate.Elements.DESCRIPTION,
    RuleTemplate.Elements.DELEGATION_TEMPLATE,
    RuleTemplate.Elements.RULE_TEMPLATE_ATTRIBUTES,
    RuleTemplate.Elements.RULE_TEMPLATE_OPTIONS,
    RuleTemplate.Elements.ID,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.OBJECT_ID,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class RuleTemplate
    extends AbstractDataTransferObject
    implements RuleTemplateContract
{

    @XmlElement(name = Elements.NAME, required = false)
    private final String name;

    @XmlElement(name = Elements.DESCRIPTION, required = false)
    private final String description;

    @XmlElement(name = Elements.DELEGATION_TEMPLATE, required = false)
    private final RuleTemplate delegationTemplate;

    @XmlElementWrapper(name = Elements.RULE_TEMPLATE_ATTRIBUTES, required = false)
    @XmlElement(name = Elements.RULE_TEMPLATE_ATTRIBUTE, required = false)
    private final List<RuleTemplateAttribute> ruleTemplateAttributes;

    @XmlElementWrapper(name = Elements.RULE_TEMPLATE_OPTIONS, required = false)
    @XmlElement(name = Elements.RULE_TEMPLATE_OPTION, required = false)
    private final List<RuleTemplateOption> ruleTemplateOptions;

    @XmlElement(name = Elements.ID, required = false)
    private final String id;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private RuleTemplate() {
        this.name = null;
        this.description = null;
        this.delegationTemplate = null;
        this.ruleTemplateAttributes = null;
        this.ruleTemplateOptions = null;
        this.id = null;
        this.versionNumber = null;
        this.objectId = null;
    }

    private RuleTemplate(Builder builder) {
        this.name = builder.getName();
        this.description = builder.getDescription();
        this.delegationTemplate = builder.getDelegationTemplate() == null ? null : builder.getDelegationTemplate().build();
        if (CollectionUtils.isNotEmpty(builder.getRuleTemplateAttributes())) {
            List<RuleTemplateAttribute> rta = new ArrayList<RuleTemplateAttribute>();
            for (RuleTemplateAttribute.Builder attribute : builder.getRuleTemplateAttributes()) {
                rta.add(attribute.build());
            }
            this.ruleTemplateAttributes = Collections.unmodifiableList(rta);
        } else {
            this.ruleTemplateAttributes = Collections.emptyList();
        }
        if (CollectionUtils.isNotEmpty(builder.getRuleTemplateOptions())) {
            List<RuleTemplateOption> rto = new ArrayList<RuleTemplateOption>();
            for (RuleTemplateOption.Builder option : builder.getRuleTemplateOptions()) {
                rto.add(option.build());
            }
            this.ruleTemplateOptions = Collections.unmodifiableList(rto);
        } else {
            this.ruleTemplateOptions = Collections.emptyList();
        }
        this.id = builder.getId();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
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
    public RuleTemplate getDelegationTemplate() {
        return this.delegationTemplate;
    }

    @Override
    public List<RuleTemplateAttribute> getRuleTemplateAttributes() {
        return this.ruleTemplateAttributes;
    }

    public List<RuleTemplateAttribute> getActiveRuleTemplateAttributes() {
        List<RuleTemplateAttribute> activeAttributes = new ArrayList<RuleTemplateAttribute>();
        for (RuleTemplateAttribute templateAttribute : getRuleTemplateAttributes())
        {
            if (templateAttribute.isActive())
            {
                activeAttributes.add(templateAttribute);
            }
        }
        Collections.sort(activeAttributes);
        return activeAttributes;
    }

    @Override
    public List<RuleTemplateOption> getRuleTemplateOptions() {
        return this.ruleTemplateOptions;
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


    /**
     * A builder which can be used to construct {@link RuleTemplate} instances.  Enforces the constraints of the {@link RuleTemplateContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, RuleTemplateContract
    {

        private String name;
        private String description;
        private RuleTemplate.Builder delegationTemplate;
        private List<RuleTemplateAttribute.Builder> ruleTemplateAttributes;
        private List<RuleTemplateOption.Builder> ruleTemplateOptions;
        private String id;
        private Long versionNumber;
        private String objectId;

        private Builder() {
            // TODO modify this constructor as needed to pass any required values and invoke the appropriate 'setter' methods
        }

        public static Builder create() {
            // TODO modify as needed to pass any required values and add them to the signature of the 'create' method
            return new Builder();
        }

        public static Builder create(RuleTemplateContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            // TODO if create() is modified to accept required parameters, this will need to be modified
            Builder builder = create();
            builder.setName(contract.getName());
            builder.setDescription(contract.getDescription());
            builder.setDelegationTemplate(
                    contract.getDelegationTemplate() == null ?
                            null : RuleTemplate.Builder.create(contract.getDelegationTemplate()));
            if (CollectionUtils.isNotEmpty(contract.getRuleTemplateAttributes())) {
                List<RuleTemplateAttribute.Builder> attrs = new ArrayList<RuleTemplateAttribute.Builder>();
                for (RuleTemplateAttributeContract attr : contract.getRuleTemplateAttributes()) {
                    attrs.add(RuleTemplateAttribute.Builder.create(attr));
                }
                builder.setRuleTemplateAttributes(attrs);
            } else {
                builder.setRuleTemplateAttributes(Collections.<RuleTemplateAttribute.Builder>emptyList());
            }
            if (CollectionUtils.isNotEmpty(contract.getRuleTemplateOptions())) {
                List<RuleTemplateOption.Builder> options = new ArrayList<RuleTemplateOption.Builder>();
                for (RuleTemplateOptionContract option : contract.getRuleTemplateOptions()) {
                    options.add(RuleTemplateOption.Builder.create(option));
                }
                builder.setRuleTemplateOptions(options);
            } else {
                builder.setRuleTemplateOptions(Collections.<RuleTemplateOption.Builder>emptyList());
            }
            builder.setId(contract.getId());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            return builder;
        }

        public RuleTemplate build() {
            return new RuleTemplate(this);
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
        public RuleTemplate.Builder getDelegationTemplate() {
            return this.delegationTemplate;
        }

        @Override
        public List<RuleTemplateAttribute.Builder> getRuleTemplateAttributes() {
            return this.ruleTemplateAttributes;
        }

        @Override
        public List<RuleTemplateOption.Builder> getRuleTemplateOptions() {
            return this.ruleTemplateOptions;
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

        public void setName(String name) {
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("name is null or blank");
            }
            this.name = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setDelegationTemplate(RuleTemplate.Builder delegationTemplate) {
            this.delegationTemplate = delegationTemplate;
        }

        public void setRuleTemplateAttributes(List<RuleTemplateAttribute.Builder> ruleTemplateAttributes) {
            this.ruleTemplateAttributes = Collections.unmodifiableList(ruleTemplateAttributes);
        }

        public void setRuleTemplateOptions(List<RuleTemplateOption.Builder> ruleTemplateOptions) {
            this.ruleTemplateOptions = Collections.unmodifiableList(ruleTemplateOptions);
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

    }


    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "ruleTemplate";
        final static String TYPE_NAME = "RuleTemplateType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String NAME = "name";
        final static String DESCRIPTION = "description";
        final static String DELEGATION_TEMPLATE = "delegationTemplate";
        final static String RULE_TEMPLATE_ATTRIBUTES = "ruleTemplateAttributes";
        final static String RULE_TEMPLATE_OPTIONS = "ruleTemplateOptions";
        final static String RULE_TEMPLATE_ATTRIBUTE = "ruleTemplateAttribute";
        final static String RULE_TEMPLATE_OPTION = "ruleTemplateOption";
        final static String ID = "id";

    }

    public static class Cache {
        public static final String NAME = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0 + "/" + RuleTemplate.Constants.TYPE_NAME;
    }
}