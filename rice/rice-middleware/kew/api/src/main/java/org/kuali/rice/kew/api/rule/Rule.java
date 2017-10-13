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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.util.jaxb.DateTimeAdapter;
import org.kuali.rice.kew.api.KewApiConstants;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = Rule.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = Rule.Constants.TYPE_NAME, propOrder = {
    Rule.Elements.ID,
    Rule.Elements.NAME,
    Rule.Elements.RULE_TEMPLATE,
    Rule.Elements.ACTIVE,
    Rule.Elements.DESCRIPTION,
    Rule.Elements.DOC_TYPE_NAME,
    Rule.Elements.FROM_DATE,
    Rule.Elements.TO_DATE,
    Rule.Elements.FORCE_ACTION,
    Rule.Elements.PREVIOUS_VERSION_ID,
    Rule.Elements.RULE_RESPONSIBILITIES,
    Rule.Elements.RULE_EXTENSIONS,
    Rule.Elements.RULE_TEMPLATE_NAME,
    Rule.Elements.RULE_EXPRESSION_DEF,
    Rule.Elements.FROM_DATE_VALUE,
    Rule.Elements.TO_DATE_VALUE,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class Rule
    extends AbstractDataTransferObject
    implements RuleContract
{
    @XmlElement(name = Elements.ID, required = false)
    private final String id;

    @XmlElement(name = Elements.NAME, required = false)
    private final String name;

    @XmlElement(name = Elements.RULE_TEMPLATE, required = false)
    private final RuleTemplate ruleTemplate;

    @XmlElement(name = Elements.ACTIVE, required = false)
    private final boolean active;

    @XmlElement(name = Elements.DESCRIPTION, required = false)
    private final String description;

    @XmlElement(name = Elements.DOC_TYPE_NAME, required = false)
    private final String docTypeName;

    @Deprecated
    @XmlElement(name = Elements.FROM_DATE, required = false)
    private final DateTime fromDate;

    @Deprecated
    @XmlElement(name = Elements.TO_DATE, required = false)
    private final DateTime toDate;

    @XmlElement(name = Elements.FROM_DATE_VALUE, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime fromDateValue;

    @XmlElement(name = Elements.TO_DATE_VALUE, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime toDateValue;

    @XmlElement(name = Elements.FORCE_ACTION, required = false)
    private final boolean forceAction;

    @XmlElement(name = Elements.PREVIOUS_VERSION_ID, required = false)
    private final String previousRuleId;

    @XmlElementWrapper(name = Elements.RULE_RESPONSIBILITIES, required = false)
    @XmlElement(name = Elements.RULE_RESPONSIBILITY, required = false)
    private final List<RuleResponsibility> ruleResponsibilities;

    @XmlElementWrapper(name = Elements.RULE_EXTENSIONS, required = false)
    @XmlElement(name = Elements.RULE_EXTENSION, required = false)
    private final List<RuleExtension> ruleExtensions;

    @XmlElement(name = Elements.RULE_TEMPLATE_NAME, required = false)
    private final String ruleTemplateName;

    @XmlElement(name = Elements.RULE_EXPRESSION_DEF, required = false)
    private final RuleExpression ruleExpressionDef;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private Rule() {
        this.id = null;
        this.name = null;
        this.ruleTemplate = null;
        this.active = false;
        this.description = null;
        this.docTypeName = null;
        this.fromDate = null;
        this.toDate = null;
        this.fromDateValue = null;
        this.toDateValue = null;
        this.forceAction = false;
        this.ruleResponsibilities = null;
        this.ruleExtensions = null;
        this.ruleTemplateName = null;
        this.ruleExpressionDef = null;
        this.previousRuleId = null;
    }

    private Rule(Builder builder) {
        this.id = builder.getId();
        this.name = builder.getName();
        this.ruleTemplate = builder.getRuleTemplate() == null ? null : builder.getRuleTemplate().build();
        this.active = builder.isActive();
        this.description = builder.getDescription();
        this.docTypeName = builder.getDocTypeName();
        this.fromDate = builder.getFromDate();
        this.toDate = builder.getToDate();
        this.fromDateValue = builder.getFromDate();
        this.toDateValue = builder.getToDate();
        this.forceAction = builder.isForceAction();
        if (CollectionUtils.isNotEmpty(builder.getRuleResponsibilities())) {
            List<RuleResponsibility> responsibilities = new ArrayList<RuleResponsibility>();
            for (RuleResponsibility.Builder b : builder.getRuleResponsibilities()) {
                responsibilities.add(b.build());
            }
            this.ruleResponsibilities = responsibilities;
        } else {
            this.ruleResponsibilities = Collections.emptyList();
        }
        if (CollectionUtils.isNotEmpty(builder.getRuleExtensions())) {
            List<RuleExtension> extensions = new ArrayList<RuleExtension>();
            for (RuleExtension.Builder b : builder.getRuleExtensions()) {
                extensions.add(b.build());
            }
            this.ruleExtensions = extensions;
        } else {
            this.ruleExtensions = Collections.emptyList();
        }
        this.ruleTemplateName = builder.getRuleTemplateName();
        this.ruleExpressionDef = builder.getRuleExpressionDef() == null ? null : builder.getRuleExpressionDef().build();
        this.previousRuleId = builder.getPreviousRuleId();
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
    public RuleTemplate getRuleTemplate() {
        return this.ruleTemplate;
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
    public String getPreviousRuleId() {
        return this.previousRuleId;
    }

    @Override
    public String getDocTypeName() {
        return this.docTypeName;
    }

    @Override
    public DateTime getFromDate() {
        return this.fromDateValue == null ? this.fromDate : this.fromDateValue;
    }

    @Override
    public DateTime getToDate() {
        return this.toDateValue == null ? this.toDate : this.toDateValue;
    }

    @Override
    public boolean isForceAction() {
        return this.forceAction;
    }

    @Override
    public List<RuleResponsibility> getRuleResponsibilities() {
        return this.ruleResponsibilities;
    }

    @Override
    public List<RuleExtension> getRuleExtensions() {
        return this.ruleExtensions;
    }

    public Map<String, String> getRuleExtensionMap() {
        Map<String, String> extensions = new HashMap<String, String>();
        for (RuleExtension ext : this.getRuleExtensions()) {
            extensions.putAll(ext.getExtensionValuesMap());
        }
        return Collections.unmodifiableMap(extensions);
    }

    @Override
    public String getRuleTemplateName() {
        return this.ruleTemplateName;
    }

    @Override
    public RuleExpression getRuleExpressionDef() {
        return this.ruleExpressionDef;
    }


    /**
     * A builder which can be used to construct {@link Rule} instances.  Enforces the constraints of the {@link RuleContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, RuleContract
    {
        private String id;
        private String name;
        private RuleTemplate.Builder ruleTemplate;
        private boolean active;
        private String description;
        private String docTypeName;
        private DateTime fromDate;
        private DateTime toDate;
        private boolean forceAction;
        private List<RuleResponsibility.Builder> ruleResponsibilities = Collections.<RuleResponsibility.Builder>emptyList();
        private List<RuleExtension.Builder> ruleExtensions = Collections.emptyList();
        private String ruleTemplateName;
        private String previousRuleId;
        private RuleExpression.Builder ruleExpressionDef;

        private Builder() {
            setActive(true);
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(RuleContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            builder.setId(contract.getId());
            builder.setName(contract.getName());
            builder.setRuleTemplate(
                    contract.getRuleTemplate() == null ? null : RuleTemplate.Builder.create(contract.getRuleTemplate()));
            builder.setActive(contract.isActive());
            builder.setDescription(contract.getDescription());
            builder.setDocTypeName(contract.getDocTypeName());
            builder.setFromDate(contract.getFromDate());
            builder.setToDate(contract.getToDate());
            builder.setForceAction(contract.isForceAction());
            builder.setPreviousRuleId(contract.getPreviousRuleId());
            if (CollectionUtils.isNotEmpty(contract.getRuleResponsibilities())) {
                List<RuleResponsibility.Builder> responsibilityBuilders = new ArrayList<RuleResponsibility.Builder>();
                for (RuleResponsibilityContract c : contract.getRuleResponsibilities()) {
                    responsibilityBuilders.add(RuleResponsibility.Builder.create(c));
                }
                builder.setRuleResponsibilities(responsibilityBuilders);
            } else {
                builder.setRuleResponsibilities(Collections.<RuleResponsibility.Builder>emptyList());
            }
            if (CollectionUtils.isNotEmpty(contract.getRuleExtensions())) {
                List<RuleExtension.Builder> extensionBuilders = new ArrayList<RuleExtension.Builder>();
                for (RuleExtensionContract c : contract.getRuleExtensions()) {
                    extensionBuilders.add(RuleExtension.Builder.create(c));
                }
                builder.setRuleExtensions(extensionBuilders);
            } else {
                builder.setRuleExtensions(Collections.<RuleExtension.Builder>emptyList());
            }
            builder.setRuleTemplateName(contract.getRuleTemplateName());
            if (contract.getRuleExpressionDef() != null) {
                builder.setRuleExpressionDef(RuleExpression.Builder.create(contract.getRuleExpressionDef()));
        }
            return builder;
        }

        public Rule build() {
            return new Rule(this);
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
        public RuleTemplate.Builder getRuleTemplate() {
            return this.ruleTemplate;
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
        public String getDocTypeName() {
            return this.docTypeName;
        }

        @Override
        public DateTime getFromDate() {
            return this.fromDate;
        }

        @Override
        public DateTime getToDate() {
            return this.toDate;
        }

        @Override
        public boolean isForceAction() {
            return this.forceAction;
        }

        @Override
        public String getPreviousRuleId() {
            return this.previousRuleId;
        }

        @Override
        public List<RuleResponsibility.Builder> getRuleResponsibilities() {
            return this.ruleResponsibilities;
        }

        @Override
        public List<RuleExtension.Builder> getRuleExtensions() {
            return this.ruleExtensions;
        }

        @Override
        public String getRuleTemplateName() {
            return this.ruleTemplateName;
        }

        @Override
        public RuleExpression.Builder getRuleExpressionDef() {
            return this.ruleExpressionDef;
        }

        public void setId(String id) {
            if (StringUtils.isWhitespace(id)) {
                throw new IllegalArgumentException("id is blank");
            }
            this.id  = id;
        }

        public void setName(String name) {
            this.name = name;
        }
        public void setRuleTemplate(RuleTemplate.Builder ruleTemplate) {
            this.ruleTemplate = ruleTemplate;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setDocTypeName(String docTypeName) {
            this.docTypeName = docTypeName;
        }

        public void setFromDate(DateTime fromDate) {
            this.fromDate = fromDate;
        }

        public void setToDate(DateTime toDate) {
            this.toDate = toDate;
        }

        public void setForceAction(boolean forceAction) {
            this.forceAction = forceAction;
        }

        public void setPreviousRuleId(String previousRuleId) {
            this.previousRuleId = previousRuleId;
        }

        public void setRuleResponsibilities(List<RuleResponsibility.Builder> ruleResponsibilities) {
            this.ruleResponsibilities = Collections.unmodifiableList(ruleResponsibilities);
        }

        public void setRuleExtensions(List<RuleExtension.Builder> ruleExtensions) {
            this.ruleExtensions = Collections.unmodifiableList(ruleExtensions);
        }

        public void setRuleTemplateName(String ruleTemplateName) {
            this.ruleTemplateName = ruleTemplateName;
        }

        public void setRuleExpressionDef(RuleExpression.Builder ruleExpressionDef) {
            this.ruleExpressionDef = ruleExpressionDef;
        }

    }


    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "rule";
        final static String TYPE_NAME = "RuleType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {
        final static String ID = "id";
        final static String NAME = "name";
        final static String RULE_TEMPLATE = "ruleTemplate";
        final static String ACTIVE = "active";
        final static String DESCRIPTION = "description";
        final static String DOC_TYPE_NAME = "docTypeName";
        final static String FROM_DATE = "fromDate";
        final static String TO_DATE = "toDate";
        final static String FROM_DATE_VALUE = "fromDateValue";
        final static String TO_DATE_VALUE = "toDateValue";
        final static String FORCE_ACTION = "forceAction";
        final static String RULE_RESPONSIBILITIES = "ruleResponsibilities";
        final static String RULE_RESPONSIBILITY = "ruleResponsibility";
        final static String RULE_EXTENSIONS = "ruleExtensions";
        final static String RULE_EXTENSION = "ruleExtension";
        final static String RULE_TEMPLATE_NAME = "ruleTemplateName";
        final static String RULE_EXPRESSION_DEF = "ruleExpressionDef";
        final static String PREVIOUS_VERSION_ID = "previousRuleId";
    }

    public static class Cache {
        public static final String NAME = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0 + "/" + Rule.Constants.TYPE_NAME;
    }
}