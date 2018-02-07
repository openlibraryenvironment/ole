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
import org.kuali.rice.kim.api.common.delegate.DelegateType;
import org.kuali.rice.kim.api.common.delegate.DelegateTypeContract;
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
import java.util.List;
import java.util.Map;

@XmlRootElement(name = ResponsibilityAction.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = ResponsibilityAction.Constants.TYPE_NAME, propOrder = {
        ResponsibilityAction.Elements.PRINCIPAL_ID,
        ResponsibilityAction.Elements.ROLE_RESPONSIBILITY_ACTION_ID,
        ResponsibilityAction.Elements.PARALLEL_ROUTING_GROUPING_CODE,
        ResponsibilityAction.Elements.ACTION_TYPE_CODE,
        ResponsibilityAction.Elements.ACTION_POLICY_CODE,
        ResponsibilityAction.Elements.PRIORITY_NUMBER,
        ResponsibilityAction.Elements.GROUP_ID,
        ResponsibilityAction.Elements.MEMBER_ROLE_ID,
        ResponsibilityAction.Elements.RESPONSIBILITY_NAME,
        ResponsibilityAction.Elements.RESPONSIBILITY_ID,
        ResponsibilityAction.Elements.RESPONSIBILITY_NAMESPACE_CODE,
        ResponsibilityAction.Elements.FORCE_ACTION,
        ResponsibilityAction.Elements.QUALIFIER,
        ResponsibilityAction.Elements.DELEGATES,
        ResponsibilityAction.Elements.ROLE_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class ResponsibilityAction extends AbstractDataTransferObject
        implements ResponsibilityActionContract {

    @XmlElement(name = Elements.PRINCIPAL_ID, required = false)
    private final String principalId;

    @XmlElement(name = Elements.ROLE_RESPONSIBILITY_ACTION_ID, required = false)
    private final String roleResponsibilityActionId;

    @XmlElement(name = Elements.PARALLEL_ROUTING_GROUPING_CODE, required = false)
    private final String parallelRoutingGroupingCode;

    @XmlElement(name = Elements.ACTION_TYPE_CODE, required = false)
    private final String actionTypeCode;

    @XmlElement(name = Elements.ACTION_POLICY_CODE, required = false)
    private final String actionPolicyCode;

    @XmlElement(name = Elements.PRIORITY_NUMBER, required = false)
    private final Integer priorityNumber;

    @XmlElement(name = Elements.GROUP_ID, required = true)
    private final String groupId;

    @XmlElement(name = Elements.MEMBER_ROLE_ID, required = true)
    private final String memberRoleId;

    @XmlElement(name = Elements.RESPONSIBILITY_NAME, required = true)
    private final String responsibilityName;

    @XmlElement(name = Elements.RESPONSIBILITY_ID, required = true)
    private final String responsibilityId;

    @XmlElement(name = Elements.RESPONSIBILITY_NAMESPACE_CODE, required = true)
    private final String responsibilityNamespaceCode;

    @XmlElement(name = Elements.FORCE_ACTION, required = true)
    private final boolean forceAction;

    @XmlElement(name = Elements.QUALIFIER, required = true)
    @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
    private final Map<String, String> qualifier;

    @XmlElementWrapper(name = Elements.DELEGATES, required = false)
    @XmlElement(name = Elements.DELEGATE, required = true)
    private final List<DelegateType> delegates;

    @XmlElement(name = Elements.ROLE_ID, required = true)
    private final String roleId;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    private ResponsibilityAction() {
        this.principalId = null;
        this.roleResponsibilityActionId = null;
        this.parallelRoutingGroupingCode = null;
        this.actionTypeCode = null;
        this.actionPolicyCode = null;
        this.priorityNumber = null;
        this.groupId = null;
        this.memberRoleId = null;
        this.responsibilityName = null;
        this.responsibilityId = null;
        this.responsibilityNamespaceCode = null;
        this.forceAction = false;
        this.qualifier = null;
        this.delegates = null;
        this.roleId = null;
    }

    private ResponsibilityAction(Builder builder) {
        this.principalId = builder.getPrincipalId();
        this.roleResponsibilityActionId = builder.getRoleResponsibilityActionId();
        this.parallelRoutingGroupingCode = builder.getParallelRoutingGroupingCode();
        this.actionTypeCode = builder.getActionTypeCode();
        this.actionPolicyCode = builder.getActionPolicyCode();
        this.priorityNumber = builder.getPriorityNumber();
        this.groupId = builder.getGroupId();
        this.memberRoleId = builder.getMemberRoleId();
        this.responsibilityName = builder.getResponsibilityName();
        this.responsibilityId = builder.getResponsibilityId();
        this.responsibilityNamespaceCode = builder.getResponsibilityNamespaceCode();
        this.forceAction = builder.isForceAction();
        this.qualifier = builder.getQualifier();
        final List<DelegateType> ds = new ArrayList<DelegateType>();
        for (DelegateType.Builder d : builder.getDelegates()) {
            if (d != null) {
                ds.add(d.build());
            }
        }
        this.delegates = ds;
        this.roleId = builder.getRoleId();
    }

    @Override
    public String getPrincipalId() {
        return this.principalId;
    }

    @Override
    public String getRoleResponsibilityActionId() {
        return this.roleResponsibilityActionId;
    }

    @Override
    public String getParallelRoutingGroupingCode() {
        return this.parallelRoutingGroupingCode;
    }

    @Override
    public String getActionTypeCode() {
        return this.actionTypeCode;
    }

    @Override
    public String getActionPolicyCode() {
        return this.actionPolicyCode;
    }

    @Override
    public Integer getPriorityNumber() {
        return this.priorityNumber;
    }

    @Override
    public String getGroupId() {
        return this.groupId;
    }

    @Override
    public String getMemberRoleId() {
        return this.memberRoleId;
    }

    @Override
    public String getResponsibilityName() {
        return this.responsibilityName;
    }

    @Override
    public String getResponsibilityId() {
        return this.responsibilityId;
    }

    @Override
    public String getResponsibilityNamespaceCode() {
        return this.responsibilityNamespaceCode;
    }

    @Override
    public boolean isForceAction() {
        return this.forceAction;
    }

    @Override
    public Map<String, String> getQualifier() {
        return this.qualifier;
    }

    @Override
    public List<DelegateType> getDelegates() {
        return Collections.unmodifiableList(this.delegates);
    }

    @Override
    public String getRoleId() {
        return this.roleId;
    }


    /**
     * A builder which can be used to construct {@link ResponsibilityAction} instances.  Enforces the constraints of the {@link ResponsibilityActionContract}.
     */
    public final static class Builder
            implements Serializable, ModelBuilder, ResponsibilityActionContract {

        private String principalId;
        private String roleResponsibilityActionId;
        private String parallelRoutingGroupingCode;
        private String actionTypeCode;
        private String actionPolicyCode;
        private Integer priorityNumber;
        private String groupId;
        private String memberRoleId;
        private String responsibilityName;
        private String responsibilityId;
        private String responsibilityNamespaceCode;
        private boolean forceAction;
        private Map<String, String> qualifier;
        private List<DelegateType.Builder> delegates;
        private String roleId;

        private Builder() {
        }

        /**
         * All required fields are enforced in the {@link org.kuali.rice.kim.api.responsibility.ResponsibilityAction.Builder#build()}
         * method.  Please see {@link ResponsibilityActionContract} to see what fields are required.
         *
         * @return a new builder, not yet in a valid state
         */
        public static Builder create() {
            //there is a lot of required fields - require fields are enforced at build time
            return new Builder();
        }

        public static Builder create(ResponsibilityActionContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            builder.setPrincipalId(contract.getPrincipalId());
            builder.setRoleResponsibilityActionId(contract.getRoleResponsibilityActionId());
            builder.setParallelRoutingGroupingCode(contract.getParallelRoutingGroupingCode());
            builder.setActionTypeCode(contract.getActionTypeCode());
            builder.setActionPolicyCode(contract.getActionPolicyCode());
            builder.setPriorityNumber(contract.getPriorityNumber());
            builder.setGroupId(contract.getGroupId());
            builder.setMemberRoleId(contract.getMemberRoleId());
            builder.setResponsibilityName(contract.getResponsibilityName());
            builder.setResponsibilityId(contract.getResponsibilityId());
            builder.setResponsibilityNamespaceCode(contract.getResponsibilityNamespaceCode());
            builder.setForceAction(contract.isForceAction());
            builder.setQualifier(contract.getQualifier());
            final List<DelegateType.Builder> dbs = new ArrayList<DelegateType.Builder>();
            for (DelegateTypeContract d : contract.getDelegates()) {
                if (d != null) {
                    dbs.add(DelegateType.Builder.create(d));
                }
            }
            builder.setDelegates(dbs);
            builder.setRoleId(contract.getRoleId());
            return builder;
        }

        public ResponsibilityAction build() {
            //validate required fields
            final boolean requiredSet = (groupId != null ^ principalId != null) &&
                            memberRoleId != null &&
                            responsibilityName != null &&
                            responsibilityId != null &&
                            responsibilityNamespaceCode != null &&
                            delegates != null &&
                            roleId != null;

            if (!requiredSet) {
                throw new IllegalStateException("all the required fields are not set");
            }
            return new ResponsibilityAction(this);
        }

        @Override
        public String getPrincipalId() {
            return this.principalId;
        }

        @Override
        public String getRoleResponsibilityActionId() {
            return this.roleResponsibilityActionId;
        }

        @Override
        public String getParallelRoutingGroupingCode() {
            return this.parallelRoutingGroupingCode;
        }

        @Override
        public String getActionTypeCode() {
            return this.actionTypeCode;
        }

        @Override
        public String getActionPolicyCode() {
            return this.actionPolicyCode;
        }

        @Override
        public Integer getPriorityNumber() {
            return this.priorityNumber;
        }

        @Override
        public String getGroupId() {
            return this.groupId;
        }

        @Override
        public String getMemberRoleId() {
            return this.memberRoleId;
        }

        @Override
        public String getResponsibilityName() {
            return this.responsibilityName;
        }

        @Override
        public String getResponsibilityId() {
            return this.responsibilityId;
        }

        @Override
        public String getResponsibilityNamespaceCode() {
            return this.responsibilityNamespaceCode;
        }

        @Override
        public boolean isForceAction() {
            return this.forceAction;
        }

        @Override
        public Map<String, String> getQualifier() {
            return this.qualifier;
        }

        @Override
        public List<DelegateType.Builder> getDelegates() {
            return Collections.unmodifiableList(this.delegates);
        }

        @Override
        public String getRoleId() {
            return this.roleId;
        }

        public void setPrincipalId(String principalId) {
            this.principalId = principalId;
        }

        public void setRoleResponsibilityActionId(String roleResponsibilityActionId) {
            this.roleResponsibilityActionId = roleResponsibilityActionId;
        }

        public void setParallelRoutingGroupingCode(String parallelRoutingGroupingCode) {
            this.parallelRoutingGroupingCode = parallelRoutingGroupingCode;
        }

        public void setActionTypeCode(String actionTypeCode) {
            this.actionTypeCode = actionTypeCode;
        }

        public void setActionPolicyCode(String actionPolicyCode) {
            this.actionPolicyCode = actionPolicyCode;
        }

        public void setPriorityNumber(Integer priorityNumber) {
            this.priorityNumber = priorityNumber;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public void setMemberRoleId(String memberRoleId) {
            if (StringUtils.isBlank(memberRoleId)) {
                throw new IllegalArgumentException("memberRoleId is blank");
            }

            this.memberRoleId = memberRoleId;
        }

        public void setResponsibilityName(String responsibilityName) {
            if (StringUtils.isBlank(responsibilityName)) {
                throw new IllegalArgumentException("responsibilityName is blank");
            }

            this.responsibilityName = responsibilityName;
        }

        public void setResponsibilityId(String responsibilityId) {
            if (StringUtils.isBlank(responsibilityId)) {
                throw new IllegalArgumentException("responsibilityId is blank");
            }

            this.responsibilityId = responsibilityId;
        }

        public void setResponsibilityNamespaceCode(String responsibilityNamespaceCode) {
            if (StringUtils.isBlank(responsibilityNamespaceCode)) {
                throw new IllegalArgumentException("responsibilityNamespaceCode is blank");
            }
            this.responsibilityNamespaceCode = responsibilityNamespaceCode;
        }

        public void setForceAction(boolean forceAction) {
            this.forceAction = forceAction;
        }

        public void setQualifier(Map<String, String> qualifier) {
            this.qualifier = (qualifier != null) ? Collections.unmodifiableMap(Maps.newHashMap(qualifier)) : qualifier;
        }

        public void setDelegates(List<DelegateType.Builder> delegates) {
            if (delegates == null) {
                throw new IllegalArgumentException("delegates is null");
            }
            this.delegates = new ArrayList<DelegateType.Builder>(delegates);
        }

        public void setRoleId(String roleId) {
            if (StringUtils.isBlank(roleId)) {
                throw new IllegalArgumentException("roleId is blank");
            }
            this.roleId = roleId;
        }

    }


    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "responsibilityAction";
        final static String TYPE_NAME = "ResponsibilityActionType";
    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {

        final static String PRINCIPAL_ID = "principalId";
        final static String ROLE_RESPONSIBILITY_ACTION_ID = "roleResponsibilityActionId";
        final static String PARALLEL_ROUTING_GROUPING_CODE = "parallelRoutingGroupingCode";
        final static String ACTION_TYPE_CODE = "actionTypeCode";
        final static String ACTION_POLICY_CODE = "actionPolicyCode";
        final static String PRIORITY_NUMBER = "priorityNumber";
        final static String GROUP_ID = "groupId";
        final static String MEMBER_ROLE_ID = "memberRoleId";
        final static String RESPONSIBILITY_NAME = "responsibilityName";
        final static String RESPONSIBILITY_ID = "responsibilityId";
        final static String RESPONSIBILITY_NAMESPACE_CODE = "responsibilityNamespaceCode";
        final static String FORCE_ACTION = "forceAction";
        final static String QUALIFIER = "qualifier";
        final static String DELEGATES = "delegates";
        final static String DELEGATE = "delegate";
        final static String ROLE_ID = "roleId";

    }

}
