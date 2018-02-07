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
package org.kuali.rice.kim.api.role;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.mo.ModelObjectComplete;
import org.kuali.rice.core.api.mo.common.active.InactivatableFromToUtils;
import org.kuali.rice.core.api.util.jaxb.DateTimeAdapter;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.kim.api.KimConstants;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = RoleMember.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RoleMember.Constants.TYPE_NAME, propOrder = {
        RoleMember.Elements.ID,
        RoleMember.Elements.ROLE_ID,
        RoleMember.Elements.ATTRIBUTES,
        RoleMember.Elements.ROLE_RESPONSIBILITY_ACTIONS,
        RoleMember.Elements.MEMBER_ID,
        RoleMember.Elements.TYPE_CODE,
        RoleMember.Elements.MEMBER_NAME,
        RoleMember.Elements.MEMBER_NAMESPACE_CODE,
        CoreConstants.CommonElements.ACTIVE_FROM_DATE,
        CoreConstants.CommonElements.ACTIVE_TO_DATE,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class RoleMember extends AbstractDataTransferObject implements RoleMemberContract {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = Elements.ID, required = false)
    private final String id;

    @XmlElement(name = Elements.ROLE_ID, required = true)
    private final String roleId;

    @XmlElement(name = Elements.ATTRIBUTES, required = false)
    @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
    private final Map<String, String> attributes;

    @XmlElementWrapper(name = Elements.ROLE_RESPONSIBILITY_ACTIONS, required = false)
    @XmlElement(name = Elements.ROLE_RESPONSIBILITY_ACTION, required = false)
    private final List<RoleResponsibilityAction> roleResponsibilityActions;

    @XmlElement(name = Elements.MEMBER_ID, required = true)
    private final String memberId;

    @XmlElement(name = Elements.TYPE_CODE, required = true)
    private final String typeCode;

    @XmlElement(name = Elements.MEMBER_NAME, required = true)
    private final String memberName;

     @XmlElement(name = Elements.MEMBER_NAMESPACE_CODE, required = true)
    private final String memberNamespaceCode;

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @XmlElement(name = CoreConstants.CommonElements.ACTIVE_FROM_DATE, required = false)
    private final DateTime activeFromDate;

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @XmlElement(name = CoreConstants.CommonElements.ACTIVE_TO_DATE, required = false)
    private final DateTime activeToDate;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor for JAXB
     */
    @SuppressWarnings("unused")
    private RoleMember() {
        id = null;
        roleId = null;
        attributes = null;
        roleResponsibilityActions = null;
        memberId = null;
        typeCode = null;
        memberName = null;
        memberNamespaceCode = null;
        activeFromDate = null;
        activeToDate = null;
        this.versionNumber = null;
        this.objectId = null;
    }

    private RoleMember(Builder b) {
        id = b.getId();
        roleId = b.getRoleId();
        attributes = b.getAttributes();

        List<RoleResponsibilityAction> roleResponsibilityActions = new ArrayList<RoleResponsibilityAction>();
        if (!CollectionUtils.isEmpty(b.getRoleRspActions())) {
            for (RoleResponsibilityAction.Builder rraBuilder : b.getRoleRspActions()) {
                roleResponsibilityActions.add(rraBuilder.build());
            }
        }
        this.roleResponsibilityActions = roleResponsibilityActions;

        memberId = b.getMemberId();
        typeCode = b.getType().getCode();
        memberName = b.getMemberName();
        memberNamespaceCode = b.getMemberNamespaceCode();
        activeFromDate = b.getActiveFromDate();
        activeToDate = b.getActiveToDate();
        versionNumber = b.getVersionNumber();
        objectId = b.getObjectId();
    }

    @Override
    public String getMemberId() {
        return this.memberId;
    }

    @Override
    public MemberType getType() {
        return MemberType.fromCode(this.typeCode);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getRoleId() {
        return this.roleId;
    }

    /**
     * @return the attributes
     */
    @Override
    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    /**
     * @return the roleResponsibilityActions
     */
    @Override
    public List<RoleResponsibilityAction> getRoleRspActions() {
        return this.roleResponsibilityActions;
    }

    @Override
    public DateTime getActiveFromDate() {
        return activeFromDate;
    }

    @Override
    public DateTime getActiveToDate() {
        return activeToDate;
    }

    @Override
    public boolean isActive(DateTime activeAsOfDate) {
        return InactivatableFromToUtils.isActive(activeFromDate, activeToDate, activeAsOfDate);
    }

    @Override
    public boolean isActive() {
        return InactivatableFromToUtils.isActive(activeFromDate, activeToDate, null);
    }

    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }

    @Override
    public String getObjectId() {
        return objectId;
    }

    @Override
    public String getMemberName() {
            return memberName;
        }

    @Override
     public String getMemberNamespaceCode() {
            return memberNamespaceCode;
        }


    public static final class Builder implements ModelBuilder, RoleMemberContract, ModelObjectComplete {

        private String id;
        private String roleId;
        private Map<String, String> attributes;
        private List<RoleResponsibilityAction.Builder> roleRspActions;
        private String memberId;
        private MemberType type;
        private DateTime activeFromDate;
        private DateTime activeToDate;
        private Long versionNumber;
        private String objectId;
        private String memberName;
        private String memberNamespaceCode;

        private Builder(String roleId, String memberId, MemberType type) {
            setRoleId(roleId);
            setMemberId(memberId);
            setType(type);
        }

        public static Builder create(String roleId, String id, String memberId,
                                     MemberType memberType, DateTime activeFromDate, DateTime activeToDate, Map<String, String> attributes,String memberName,String memberNamespaceCode) {
            Builder b = new Builder(roleId, memberId, memberType);
            b.setId(id);
            b.setActiveFromDate(activeFromDate);
            b.setActiveToDate(activeToDate);
            b.setAttributes(attributes);
            b.setMemberName(memberName);
            b.setMemberNamespaceCode(memberNamespaceCode);
            return b;
        }

        public static Builder create(RoleMemberContract contract) {
            Builder b = new Builder(contract.getRoleId(), contract.getMemberId(), contract.getType());
            b.setId(contract.getId());
            b.setAttributes(contract.getAttributes());

            List<RoleResponsibilityAction.Builder> rraBuilders = new ArrayList<RoleResponsibilityAction.Builder>();
            if (!CollectionUtils.isEmpty(contract.getRoleRspActions())) {
                for (RoleResponsibilityActionContract rrac : contract.getRoleRspActions()) {
                    rraBuilders.add(RoleResponsibilityAction.Builder.create(rrac));
                }
            }
            b.setRoleRspActions(rraBuilders);

            b.setActiveFromDate(contract.getActiveFromDate());
            b.setActiveToDate(contract.getActiveToDate());
            b.setMemberName(contract.getMemberName());
            b.setMemberNamespaceCode(contract.getMemberNamespaceCode());
            b.setVersionNumber(contract.getVersionNumber());
            b.setObjectId(contract.getObjectId());
            return b;
        }

        @Override
        public RoleMember build() {
            return new RoleMember(this);
        }

        @Override
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String getRoleId() {
            return roleId;
        }

        public void setRoleId(String roleId) {
            if (StringUtils.isEmpty(roleId)) {
                throw new IllegalArgumentException("roleId is empty");
            }
            this.roleId = roleId;
        }

        @Override
        public Map<String, String> getAttributes() {
            return attributes;
        }

        public void setAttributes(Map<String, String> attributes) {
            this.attributes = attributes;
        }

        @Override
        public List<RoleResponsibilityAction.Builder> getRoleRspActions() {
            return roleRspActions;
        }

        public void setRoleRspActions(List<RoleResponsibilityAction.Builder> roleRspActions) {
            this.roleRspActions = roleRspActions;
        }

        @Override
        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            if (StringUtils.isBlank(memberId)) {
                throw new IllegalArgumentException("memberId may not be null");
            }
            this.memberId = memberId;
        }

        @Override
        public MemberType getType() {
            return type;
        }

        @Override
        public String getMemberName() {
            return memberName;
        }

         public void setMemberName(String memberName){
             this.memberName = memberName;
         }
        @Override
        public String getMemberNamespaceCode() {
            return memberNamespaceCode;
        }

        public void setMemberNamespaceCode(String memberNamespaceCode){
             this.memberNamespaceCode = memberNamespaceCode;
        }
        public void setType(final MemberType type) {
            if (type == null) {
                throw new IllegalArgumentException("type is null");
            }
            this.type = type;
        }

        @Override
        public DateTime getActiveFromDate() {
            return activeFromDate;
        }

        public void setActiveFromDate(DateTime activeFromDate) {
            this.activeFromDate = activeFromDate;
        }

        @Override
        public DateTime getActiveToDate() {
            return activeToDate;
        }

        public void setActiveToDate(DateTime activeToDate) {
            this.activeToDate = activeToDate;
        }

        @Override
        public boolean isActive(DateTime activeAsOfDate) {
            return InactivatableFromToUtils.isActive(activeFromDate, activeToDate, activeAsOfDate);
        }

        @Override
        public boolean isActive() {
            return InactivatableFromToUtils.isActive(activeFromDate, activeToDate, null);
        }

        @Override
        public Long getVersionNumber() {
            return versionNumber;
        }

        public void setVersionNumber(final Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        @Override
        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(final String objectId) {
            this.objectId = objectId;
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public boolean equals(Object obj) {
            return EqualsBuilder.reflectionEquals(obj, this);
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    /**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshalled to XML.
     */
    static class Elements {
        final static String ID = "id";
        final static String ROLE_ID = "roleId";
        final static String ATTRIBUTES = "attributes";
        final static String ROLE_RESPONSIBILITY_ACTIONS = "roleResponsibilityActions";
        final static String ROLE_RESPONSIBILITY_ACTION = "roleResponsibilityAction";
        final static String MEMBER_ID = "memberId";
        final static String TYPE_CODE = "typeCode";
        final static String MEMBER_NAME = "memberName";
        final static String MEMBER_NAMESPACE_CODE = "memberNamespaceCode";
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "roleMember";
        final static String TYPE_NAME = "RoleMemberType";
    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + RoleMember.Constants.TYPE_NAME;
    }
}
