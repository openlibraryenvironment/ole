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

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = RuleReportCriteria.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RuleReportCriteria.Constants.TYPE_NAME, propOrder = {
    RuleReportCriteria.Elements.RULE_DESCRIPTION,
    RuleReportCriteria.Elements.DOCUMENT_TYPE_NAME,
    RuleReportCriteria.Elements.RULE_TEMPLATE_NAME,
    RuleReportCriteria.Elements.ACTION_REQUEST_CODES,
    RuleReportCriteria.Elements.RESPONSIBLE_PRINCIPAL_ID,
    RuleReportCriteria.Elements.RESPONSIBLE_GROUP_ID,
    RuleReportCriteria.Elements.RESPONSIBLE_ROLE_NAME,
    RuleReportCriteria.Elements.RULE_EXTENSIONS,
    RuleReportCriteria.Elements.ACTIVE,
    RuleReportCriteria.Elements.CONSIDER_GROUP_MEMBERSHIP,
    RuleReportCriteria.Elements.INCLUDE_DELEGATIONS,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class RuleReportCriteria
    extends AbstractDataTransferObject
    implements RuleReportCriteriaContract
{

    @XmlElement(name = Elements.RULE_DESCRIPTION, required = false)
    private final String ruleDescription;

    @XmlElement(name = Elements.DOCUMENT_TYPE_NAME, required = false)
    private final String documentTypeName;

    @XmlElement(name = Elements.RULE_TEMPLATE_NAME, required = false)
    private final String ruleTemplateName;

    @XmlElementWrapper(name = Elements.ACTION_REQUEST_CODES, required = false)
    @XmlElement(name = Elements.ACTION_REQUEST_CODE, required = false)
    private final List<String> actionRequestCodes;

    @XmlElement(name = Elements.RESPONSIBLE_PRINCIPAL_ID, required = false)
    private final String responsiblePrincipalId;

    @XmlElement(name = Elements.RESPONSIBLE_GROUP_ID, required = false)
    private final String responsibleGroupId;

    @XmlElement(name = Elements.RESPONSIBLE_ROLE_NAME, required = false)
    private final String responsibleRoleName;

    @XmlElement(name = Elements.RULE_EXTENSIONS, required = false)
    @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
    private final Map<String, String> ruleExtensions;

    @XmlElement(name = Elements.ACTIVE, required = false)
    private final boolean active;

    @XmlElement(name = Elements.CONSIDER_GROUP_MEMBERSHIP, required = false)
    private final boolean considerGroupMembership;

    @XmlElement(name = Elements.INCLUDE_DELEGATIONS, required = false)
    private final boolean includeDelegations;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     *
     */
    private RuleReportCriteria() {
        this.ruleDescription = null;
        this.documentTypeName = null;
        this.ruleTemplateName = null;
        this.actionRequestCodes = null;
        this.responsiblePrincipalId = null;
        this.responsibleGroupId = null;
        this.responsibleRoleName = null;
        this.ruleExtensions = null;
        this.active = false;
        this.considerGroupMembership = false;
        this.includeDelegations = false;
    }

    private RuleReportCriteria(Builder builder) {
        this.ruleDescription = builder.getRuleDescription();
        this.documentTypeName = builder.getDocumentTypeName();
        this.ruleTemplateName = builder.getRuleTemplateName();
        this.actionRequestCodes = builder.getActionRequestCodes();
        this.responsiblePrincipalId = builder.getResponsiblePrincipalId();
        this.responsibleGroupId = builder.getResponsibleGroupId();
        this.responsibleRoleName = builder.getResponsibleRoleName();
        this.ruleExtensions = builder.getRuleExtensions();
        this.active = builder.isActive();
        this.considerGroupMembership = builder.isConsiderGroupMembership();
        this.includeDelegations = builder.isIncludeDelegations();
    }

    @Override
    public String getRuleDescription() {
        return this.ruleDescription;
    }

    @Override
    public String getDocumentTypeName() {
        return this.documentTypeName;
    }

    @Override
    public String getRuleTemplateName() {
        return this.ruleTemplateName;
    }

    @Override
    public List<String> getActionRequestCodes() {
        return this.actionRequestCodes;
    }

    @Override
    public String getResponsiblePrincipalId() {
        return this.responsiblePrincipalId;
    }

    @Override
    public String getResponsibleGroupId() {
        return this.responsibleGroupId;
    }

    @Override
    public String getResponsibleRoleName() {
        return this.responsibleRoleName;
    }

    @Override
    public Map<String, String> getRuleExtensions() {
        return this.ruleExtensions;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public boolean isConsiderGroupMembership() {
        return this.considerGroupMembership;
    }

    @Override
    public boolean isIncludeDelegations() {
        return this.includeDelegations;
    }


    /**
     * A builder which can be used to construct {@link RuleReportCriteria} instances.  Enforces the constraints of the {@link RuleReportCriteriaContract}.
     *
     */
    public final static class Builder
        implements Serializable, ModelBuilder, RuleReportCriteriaContract
    {

        private String ruleDescription;
        private String documentTypeName;
        private String ruleTemplateName;
        private List<String> actionRequestCodes;
        private String responsiblePrincipalId;
        private String responsibleGroupId;
        private String responsibleRoleName;
        private Map<String, String> ruleExtensions;
        private boolean active;
        private boolean considerGroupMembership;
        private boolean includeDelegations;

        private Builder() {
            setActive(true);
            setConsiderGroupMembership(true);
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(RuleReportCriteriaContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            builder.setRuleDescription(contract.getRuleDescription());
            builder.setDocumentTypeName(contract.getDocumentTypeName());
            builder.setRuleTemplateName(contract.getRuleTemplateName());
            builder.setActionRequestCodes(contract.getActionRequestCodes());
            builder.setResponsiblePrincipalId(contract.getResponsiblePrincipalId());
            builder.setResponsibleGroupId(contract.getResponsibleGroupId());
            builder.setResponsibleRoleName(contract.getResponsibleRoleName());
            builder.setRuleExtensions(contract.getRuleExtensions());
            builder.setActive(contract.isActive());
            builder.setConsiderGroupMembership(contract.isConsiderGroupMembership());
            builder.setIncludeDelegations(contract.isIncludeDelegations());
            return builder;
        }

        public RuleReportCriteria build() {
            return new RuleReportCriteria(this);
        }

        @Override
        public String getRuleDescription() {
            return this.ruleDescription;
        }

        @Override
        public String getDocumentTypeName() {
            return this.documentTypeName;
        }

        @Override
        public String getRuleTemplateName() {
            return this.ruleTemplateName;
        }

        @Override
        public List<String> getActionRequestCodes() {
            return this.actionRequestCodes;
        }

        @Override
        public String getResponsiblePrincipalId() {
            return this.responsiblePrincipalId;
        }

        @Override
        public String getResponsibleGroupId() {
            return this.responsibleGroupId;
        }

        @Override
        public String getResponsibleRoleName() {
            return this.responsibleRoleName;
        }

        @Override
        public Map<String, String> getRuleExtensions() {
            return this.ruleExtensions;
        }

        @Override
        public boolean isActive() {
            return this.active;
        }

        @Override
        public boolean isConsiderGroupMembership() {
            return this.considerGroupMembership;
        }

        @Override
        public boolean isIncludeDelegations() {
            return this.includeDelegations;
        }

        public void setRuleDescription(String ruleDescription) {

            this.ruleDescription = ruleDescription;
        }

        public void setDocumentTypeName(String documentTypeName) {

            this.documentTypeName = documentTypeName;
        }

        public void setRuleTemplateName(String ruleTemplateName) {

            this.ruleTemplateName = ruleTemplateName;
        }

        public void setActionRequestCodes(List<String> actionRequestCodes) {

            this.actionRequestCodes = actionRequestCodes;
        }

        public void setResponsiblePrincipalId(String responsiblePrincipalId) {

            this.responsiblePrincipalId = responsiblePrincipalId;
        }

        public void setResponsibleGroupId(String responsibleGroupId) {

            this.responsibleGroupId = responsibleGroupId;
        }

        public void setResponsibleRoleName(String responsibleRoleName) {

            this.responsibleRoleName = responsibleRoleName;
        }

        public void setRuleExtensions(Map<String, String> ruleExtensions) {

            this.ruleExtensions = Collections.unmodifiableMap(ruleExtensions);
        }

        public void setActive(boolean active) {

            this.active = active;
        }

        public void setConsiderGroupMembership(boolean considerGroupMembership) {

            this.considerGroupMembership = considerGroupMembership;
        }

        public void setIncludeDelegations(boolean includeDelegations) {

            this.includeDelegations = includeDelegations;
        }

    }


    /**
     * Defines some internal constants used on this class.
     *
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "ruleReportCriteria";
        final static String TYPE_NAME = "RuleReportCriteriaType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     *
     */
    static class Elements {

        final static String RULE_DESCRIPTION = "ruleDescription";
        final static String DOCUMENT_TYPE_NAME = "documentTypeName";
        final static String RULE_TEMPLATE_NAME = "ruleTemplateName";
        final static String ACTION_REQUEST_CODES = "actionRequestCodes";
        final static String ACTION_REQUEST_CODE = "actionRequestCode";
        final static String RESPONSIBLE_PRINCIPAL_ID = "responsiblePrincipalId";
        final static String RESPONSIBLE_GROUP_ID = "responsibleGroupId";
        final static String RESPONSIBLE_ROLE_NAME = "responsibleRoleName";
        final static String RULE_EXTENSIONS = "ruleExtensions";
        final static String ACTIVE = "active";
        final static String CONSIDER_GROUP_MEMBERSHIP = "considerGroupMembership";
        final static String INCLUDE_DELEGATIONS = "includeDelegations";

    }

}