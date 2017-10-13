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
package org.kuali.rice.kim.api.common.delegate;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.mo.ModelObjectComplete;
import org.kuali.rice.kim.api.KimConstants;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@XmlRootElement(name = DelegateType.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DelegateType.Constants.TYPE_NAME, propOrder = {
        DelegateType.Elements.ROLE_ID,
        DelegateType.Elements.DELEGATION_ID,
        DelegateType.Elements.DELEGATION_TYPE_CODE,
        DelegateType.Elements.KIM_TYPE_ID,
        DelegateType.Elements.MEMBERS,
        DelegateType.Elements.ACTIVE,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class DelegateType extends AbstractDataTransferObject implements DelegateTypeContract {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = Elements.ROLE_ID)
    private final String roleId;

    @XmlElement(name = Elements.DELEGATION_ID, required = false)
    private final String delegationId;

    @XmlElement(name = Elements.DELEGATION_TYPE_CODE)
    private final String delegationTypeCode;

    @XmlElement(name = Elements.KIM_TYPE_ID)
    private final String kimTypeId;

    @XmlElementWrapper(name = Elements.MEMBERS, required = false)
    @XmlElement(name = Elements.MEMBER)
    private final List<DelegateMember> members;

    @XmlElement(name = Elements.ACTIVE)
    private final boolean active;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Default constructor invoked by JAXB only
     */
    @SuppressWarnings("unused")
    private DelegateType() {
        roleId = null;
        delegationId = null;
        delegationTypeCode = null;
        kimTypeId = null;
        members = null;
        active = false;
    }

    private DelegateType(Builder b) {
        roleId = b.getRoleId();
        delegationId = b.getDelegationId();
        delegationTypeCode = b.getDelegationType() != null ? b.getDelegationType().getCode() : null;
        kimTypeId = b.getKimTypeId();
        active = b.isActive();

        List<DelegateMember> delegateMembers = new ArrayList<DelegateMember>();
        if (!CollectionUtils.isEmpty(b.getMembers())) {
            for (DelegateMember.Builder delgateBuilder : b.getMembers()) {
                delegateMembers.add(delgateBuilder.build());
            }
        }
        members = delegateMembers;
    }

    @Override
    public String getKimTypeId() {
        return this.kimTypeId;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public DelegationType getDelegationType() {
        return DelegationType.fromCode(this.delegationTypeCode);
    }

    @Override
    public String getDelegationId() {
        return this.delegationId;
    }

    @Override
    public String getRoleId() {
        return this.roleId;
    }

    @Override
    public List<DelegateMember> getMembers() {
        return Collections.unmodifiableList(this.members);
    }

    public static final class Builder implements DelegateTypeContract, ModelBuilder, ModelObjectComplete {
        private String roleId;
        private String delegationId;
        private DelegationType delegationType;
        private String kimTypeId;
        private List<DelegateMember.Builder> members;
        private boolean active;

        public static Builder create(DelegateTypeContract dtc) {
            Builder b = new Builder();
            b.setRoleId(dtc.getRoleId());
            b.setDelegationId(dtc.getDelegationId());
            b.setDelegationType(dtc.getDelegationType());
            b.setActive(dtc.isActive());
            b.setKimTypeId(dtc.getKimTypeId());

            ArrayList<DelegateMember.Builder> delegateBuilders = new ArrayList<DelegateMember.Builder>();
            for (DelegateMemberContract delegate : dtc.getMembers()) {
                delegateBuilders.add(DelegateMember.Builder.create(delegate));
            }
            b.setMembers(delegateBuilders);

            return b;
        }

        public static Builder create(String roleId, DelegationType delegationType, List<DelegateMember.Builder> members) {
            Builder b = new Builder();
            b.setRoleId(roleId);
            b.setDelegationType(delegationType);
            b.setMembers(members);
            b.setActive(true);

            return b;
        }

        @Override
        public DelegateType build() {
            return new DelegateType(this);
        }

        @Override
        public String getRoleId() {
            return roleId;
        }

        public void setRoleId(String roleId) {
            if (StringUtils.isBlank(roleId)) {
                throw new IllegalArgumentException("roleId cannot be null or blank");
            }
            this.roleId = roleId;
        }

        @Override
        public String getDelegationId() {
            return delegationId;
        }

        public void setDelegationId(String delegationId) {
            this.delegationId = delegationId;
        }

        @Override
        public DelegationType getDelegationType() {
            return delegationType;
        }

        public void setDelegationType(DelegationType delegationType) {
            if (delegationType == null) {
                throw new IllegalArgumentException("delegationTypeCode cannot be null");
            }
            this.delegationType = delegationType;
        }

        @Override
        public String getKimTypeId() {
            return kimTypeId;
        }

        public void setKimTypeId(String kimTypeId) {
            this.kimTypeId = kimTypeId;
        }

        @Override
        public List<DelegateMember.Builder> getMembers() {
            return members;
        }

        public void setMembers(List<DelegateMember.Builder> members) {
            this.members = members;
        }

        @Override
        public boolean isActive() {
            return active;
        }

        public void setActive(Boolean active) {
            this.active = active;
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
        static final String ROLE_ID = "roleId";
        static final String DELEGATION_ID = "delegationId";
        static final String DELEGATION_TYPE_CODE = "delegationTypeCode";
        static final String KIM_TYPE_ID = "kimTypeId";
        static final String MEMBERS = "members";
        static final String MEMBER = "member";
        static final String ACTIVE = "active";
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "delegateType";
        final static String TYPE_NAME = "DelegateTypeType";
    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + DelegateType.Constants.TYPE_NAME;
    }
}
