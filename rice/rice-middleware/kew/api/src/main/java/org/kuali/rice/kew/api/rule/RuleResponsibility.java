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
import java.lang.reflect.Type;
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
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kew.api.KewApiConstants;
import org.w3c.dom.Element;

@XmlRootElement(name = RuleResponsibility.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RuleResponsibility.Constants.TYPE_NAME, propOrder = {
    RuleResponsibility.Elements.ID,
    RuleResponsibility.Elements.PRIORITY,
    RuleResponsibility.Elements.RESPONSIBILITY_ID,
    RuleResponsibility.Elements.ACTION_REQUESTED_CD,
    RuleResponsibility.Elements.APPROVE_POLICY,
    RuleResponsibility.Elements.PRINCIPAL_ID,
    RuleResponsibility.Elements.GROUP_ID,
    RuleResponsibility.Elements.ROLE_NAME,
    RuleResponsibility.Elements.DELEGATION_RULES,
    RuleResponsibility.Elements.USING_ROLE,
    RuleResponsibility.Elements.USING_GROUP,
    RuleResponsibility.Elements.USING_PRINCIPAL,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.OBJECT_ID,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class RuleResponsibility
    extends AbstractDataTransferObject
    implements RuleResponsibilityContract
{
    @XmlElement(name = Elements.ID, required = false)
    private final String id;

    @XmlElement(name = Elements.PRIORITY, required = false)
    private final Integer priority;

    @XmlElement(name = Elements.RESPONSIBILITY_ID, required = true)
    private final String responsibilityId;

    @XmlElement(name = Elements.ACTION_REQUESTED_CD, required = false)
    private final String actionRequestedCd;

    @XmlElement(name = Elements.APPROVE_POLICY, required = false)
    private final String approvePolicy;

    @XmlElement(name = Elements.PRINCIPAL_ID, required = false)
    private final String principalId;

    @XmlElement(name = Elements.GROUP_ID, required = false)
    private final String groupId;

    @XmlElement(name = Elements.ROLE_NAME, required = false)
    private final String roleName;

    @XmlElementWrapper(name = Elements.DELEGATION_RULES, required = false)
    @XmlElement(name = Elements.DELEGATION_RULE, required = false)
    private final List<RuleDelegation> delegationRules;

    @XmlElement(name = Elements.USING_ROLE, required = false)
    private final boolean usingRole;

    @XmlElement(name = Elements.USING_PRINCIPAL, required = false)
    private final boolean usingPrincipal;

    @XmlElement(name = Elements.USING_GROUP, required = false)
    private final boolean usingGroup;

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
    private RuleResponsibility() {
        this.id = null;
        this.priority = null;
        this.responsibilityId = null;
        this.actionRequestedCd = null;
        this.approvePolicy = null;
        this.principalId = null;
        this.groupId = null;
        this.roleName = null;
        this.delegationRules = null;
        this.usingGroup = false;
        this.usingPrincipal = false;
        this.usingRole = false;
        this.versionNumber = null;
        this.objectId = null;
    }

    private RuleResponsibility(Builder builder) {
        this.id = builder.getId();
        this.priority = builder.getPriority();
        this.responsibilityId = builder.getResponsibilityId();
        this.actionRequestedCd = builder.getActionRequestedCd();
        this.approvePolicy = builder.getApprovePolicy();
        this.principalId = builder.getPrincipalId();
        this.groupId = builder.getGroupId();
        this.roleName = builder.getRoleName();
        if (CollectionUtils.isNotEmpty(builder.getDelegationRules())) {
            List<RuleDelegation> delegationList = new ArrayList<RuleDelegation>();
            for (RuleDelegation.Builder b : builder.getDelegationRules()) {
                delegationList.add(b.build());
            }
            this.delegationRules = delegationList;
        } else {
            this.delegationRules = Collections.emptyList();
        }
        this.usingGroup = builder.isUsingGroup();
        this.usingPrincipal = builder.isUsingPrincipal();
        this.usingRole = builder.isUsingRole();
        versionNumber = builder.getVersionNumber();
        objectId = builder.getObjectId();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Integer getPriority() {
        return this.priority;
    }

    @Override
    public String getResponsibilityId() {
        return this.responsibilityId;
    }

    @Override
    public String getActionRequestedCd() {
        return this.actionRequestedCd;
    }

    @Override
    public String getApprovePolicy() {
        return this.approvePolicy;
    }

    @Override
    public String getPrincipalId() {
        return this.principalId;
    }

    @Override
    public String getGroupId() {
        return this.groupId;
    }

    @Override
    public String getRoleName() {
        return this.roleName;
    }

    @Override
    public List<RuleDelegation> getDelegationRules() {
        return this.delegationRules;
    }

    @Override
    public boolean isUsingRole() {
        return this.usingRole;
    }

    @Override
    public boolean isUsingPrincipal() {
        return this.usingPrincipal;
    }

    @Override
    public boolean isUsingGroup() {
        return this.usingGroup;
    }

    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }

    @Override
    public String getObjectId() {
        return objectId;
    }

    public String getRoleAttributeName() {
	    return getRoleName().substring(0, getRoleName().indexOf("!"));
    }

    public String getResolvedRoleName() {
        if (isUsingRole()) {
            return getRoleName().substring(getRoleName().indexOf("!") + 1, getRoleName().length());
        }
        return null;
    }

    /**
     * A builder which can be used to construct {@link RuleResponsibility} instances.  Enforces the constraints of the {@link RuleResponsibilityContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, RuleResponsibilityContract
    {
        private String id;
        private Integer priority;
        private String responsibilityId;
        private String actionRequestedCd;
        private String approvePolicy;
        private String principalId;
        private String groupId;
        private String roleName;
        private List<RuleDelegation.Builder> delegationRules;
        private boolean usingRole = false;
        private boolean usingPrincipal = false;
        private boolean usingGroup = false;
        private Long versionNumber;
        private String objectId;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(RuleResponsibilityContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            builder.setId(contract.getId());
            builder.setPriority(contract.getPriority());
            builder.setResponsibilityId(contract.getResponsibilityId());
            builder.setActionRequestedCd(contract.getActionRequestedCd());
            builder.setApprovePolicy(contract.getApprovePolicy());
            builder.setPrincipalId(contract.getPrincipalId());
            builder.setGroupId(contract.getGroupId());
            builder.setRoleName(contract.getRoleName());
            if (CollectionUtils.isNotEmpty(contract.getDelegationRules())) {
                List<RuleDelegation.Builder> builders = new ArrayList<RuleDelegation.Builder>();
                for (RuleDelegationContract delegationContract : contract.getDelegationRules()) {
                    builders.add(RuleDelegation.Builder.create(delegationContract));
                }
                builder.setDelegationRules(builders);
            } else {
                builder.setDelegationRules(Collections.<RuleDelegation.Builder>emptyList());
            }
            builder.setUsingGroup(contract.isUsingGroup());
            builder.setUsingPrincipal(contract.isUsingPrincipal());
            builder.setUsingRole(contract.isUsingRole());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            return builder;
        }

        public RuleResponsibility build() {
            return new RuleResponsibility(this);
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public Integer getPriority() {
            return this.priority;
        }

        @Override
        public String getResponsibilityId() {
            return this.responsibilityId;
        }

        @Override
        public String getActionRequestedCd() {
            return this.actionRequestedCd;
        }

        @Override
        public String getApprovePolicy() {
            return this.approvePolicy;
        }

        @Override
        public String getPrincipalId() {
            return this.principalId;
        }

        @Override
        public String getGroupId() {
            return this.groupId;
        }

        @Override
        public String getRoleName() {
            return this.roleName;
        }

        @Override
        public List<RuleDelegation.Builder> getDelegationRules() {
            return this.delegationRules;
        }

        @Override
        public boolean isUsingRole() {
            return this.usingRole;
        }

        @Override
        public boolean isUsingPrincipal() {
            return this.usingPrincipal;
        }

        @Override
        public boolean isUsingGroup() {
            return this.usingGroup;
        }

        @Override
        public Long getVersionNumber() {
            return versionNumber;
        }

        @Override
        public String getObjectId() {
            return objectId;
        }

        public void setId(String id) {
            if (StringUtils.isWhitespace(id)) {
                throw new IllegalArgumentException("id is whitespace");
            }
            this.id = id;
        }
        public void setPriority(Integer priority) {
            this.priority = priority;
        }

        public void setResponsibilityId(String responsibilityId) {
            this.responsibilityId = responsibilityId;
        }

        public void setActionRequestedCd(String actionRequestedCd) {
            this.actionRequestedCd = actionRequestedCd;
        }

        public void setApprovePolicy(String approvePolicy) {
            this.approvePolicy = approvePolicy;
        }

        public void setPrincipalId(String principalId) {
            this.principalId = principalId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public void setDelegationRules(List<RuleDelegation.Builder> delegationRules) {
            this.delegationRules = delegationRules;
        }

        public void setUsingRole(boolean usingRole) {
            this.usingRole = usingRole;
        }

        public void setUsingPrincipal(boolean usingPrincipal) {
            this.usingPrincipal = usingPrincipal;
        }

        public void setUsingGroup(boolean usingGroup) {
            this.usingGroup = usingGroup;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }


    }

    @Override
    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (!(o instanceof RuleResponsibilityContract)) { return false; }
        RuleResponsibilityContract resp = (RuleResponsibilityContract) o;
        return StringUtils.equals(getPrincipalId(),  resp.getPrincipalId()) &&
               StringUtils.equals(getRoleName(), resp.getRoleName()) &&
               StringUtils.equals(getGroupId(), resp.getGroupId()) &&
               StringUtils.equals(getActionRequestedCd(), resp.getActionRequestedCd()) &&
               ObjectUtils.equals(getPriority(), resp.getPriority()) &&
               StringUtils.equals(getApprovePolicy(), resp.getApprovePolicy());
    }

    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "ruleResponsibility";
        final static String TYPE_NAME = "RuleResponsibilityType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {
        final static String ID = "id";
        final static String PRIORITY = "priority";
        final static String RESPONSIBILITY_ID = "responsibilityId";
        final static String ACTION_REQUESTED_CD = "actionRequestedCd";
        final static String APPROVE_POLICY = "approvePolicy";
        final static String PRINCIPAL_ID = "principalId";
        final static String GROUP_ID = "groupId";
        final static String ROLE_NAME = "roleName";
        final static String DELEGATION_RULES = "delegationRules";
        final static String DELEGATION_RULE = "delegationRule";
        final static String USING_ROLE = "usingRole";
        final static String USING_PRINCIPAL = "usingPrincipal";
        final static String USING_GROUP = "usingGroup";
    }

    public static class Cache {
        public static final String NAME = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0 + "/" + RuleResponsibility.Constants.TYPE_NAME;
    }
}