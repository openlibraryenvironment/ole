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
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.mo.ModelObjectComplete;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.kim.api.KimConstants;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = RoleMembership.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RoleMembership.Constants.TYPE_NAME, propOrder = {
        RoleMembership.Elements.ROLE_ID,
        RoleMembership.Elements.ID,
        RoleMembership.Elements.EMBEDDED_ROLE_ID,
        RoleMembership.Elements.MEMBER_ID,
        RoleMembership.Elements.TYPE_CODE,
        RoleMembership.Elements.ROLE_SORTING_CODE,
        RoleMembership.Elements.QUALIFIER,
        RoleMembership.Elements.DELEGATES,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class RoleMembership extends AbstractDataTransferObject implements RoleMembershipContract {
    private static final long serialVersionUID = 1L;

    @XmlElement(name=Elements.ROLE_ID, required = true)
    private final String roleId;

    @XmlElement(name=Elements.ID, required = false)
    private final String id;

    @XmlElement(name=Elements.EMBEDDED_ROLE_ID, required = false)
    private final String embeddedRoleId;

    @XmlElement(name=Elements.MEMBER_ID, required = true)
    private final String memberId;

    @XmlElement(name=Elements.TYPE_CODE, required = true)
    private final String typeCode;

    @XmlElement(name=Elements.ROLE_SORTING_CODE, required = false)
    private final String roleSortingCode;

    @XmlElement(name=Elements.QUALIFIER, required = false)
    @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
    private final Map<String, String> qualifier;

    @XmlElementWrapper(name = Elements.DELEGATES, required = false)
    @XmlElement(name=Elements.DELEGATE, required = false)
    private final List<DelegateType> delegates;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor for JAXB only
     */
    @SuppressWarnings("unused")
    private RoleMembership() {
        roleId = null;
        id = null;
        embeddedRoleId = null;
        memberId = null;
        typeCode = null;
        roleSortingCode = null;
        qualifier = null;
        delegates = null;
    }

    private RoleMembership(Builder b) {
        roleId = b.getRoleId();
        id = b.getId();
        embeddedRoleId = b.getEmbeddedRoleId();
        memberId = b.getMemberId();
        typeCode = b.getType().getCode();
        roleSortingCode = b.getRoleSortingCode();
        qualifier = b.getQualifier();

        delegates = new ArrayList<DelegateType>();
        if (!CollectionUtils.isEmpty(b.getDelegates())) {
            for (DelegateType.Builder delegateBuilder : b.getDelegates()) {
                delegates.add(delegateBuilder.build());
            }
        }
    }

    @Override
    public String getRoleId() {
        return roleId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getEmbeddedRoleId() {
        return embeddedRoleId;
    }

    @Override
    public String getMemberId() {
        return memberId;
    }

    @Override
    public MemberType getType() {
        return MemberType.fromCode(typeCode);
    }

    @Override
    public String getRoleSortingCode() {
        return roleSortingCode;
    }

    @Override
    public Map<String, String> getQualifier() {
        return qualifier;
    }

    @Override
    public List<DelegateType> getDelegates() {
        return Collections.unmodifiableList(delegates);
    }


    public static final class Builder implements ModelBuilder, RoleMembershipContract, ModelObjectComplete {
        private String roleId;
        private String id;
        private String embeddedRoleId;
        private String memberId;
        private MemberType type;
        private String roleSortingCode;
        private Map<String, String> qualifier;
        private List<DelegateType.Builder> delegates;

        private Builder(String roleId, String memberId, MemberType type) {
            setRoleId(roleId);
            setMemberId(memberId);
            setType(type);
        }

        public static Builder create(String roleId, String id, String memberId, MemberType memberType,
                                     Map<String, String> qualifier) {

            Builder b = new Builder(roleId, memberId, memberType);
            b.setId(id);
            b.setQualifier(qualifier);
            return b;
        }

        public static Builder create(RoleMembershipContract contract) {
            Builder b = new Builder(contract.getRoleId(), contract.getMemberId(), contract.getType());
            b.setId(contract.getId());
            b.setEmbeddedRoleId(contract.getEmbeddedRoleId());
            b.setRoleSortingCode(contract.getRoleSortingCode());
            b.setQualifier(contract.getQualifier());

            List<DelegateType.Builder> delegateBuilders = new ArrayList<DelegateType.Builder>();
            if (!CollectionUtils.isEmpty(contract.getDelegates())) {
                for (DelegateTypeContract delegateContract : contract.getDelegates()) {
                    delegateBuilders.add(DelegateType.Builder.create(delegateContract));
                }
            }
            b.setDelegates(delegateBuilders);

            return b;
        }

        @Override
        public RoleMembership build() {
            return new RoleMembership(this);
        }

        @Override
        public String getRoleId() {
            return this.roleId;
        }

        public void setRoleId(String roleId) {
            this.roleId = roleId;
        }

        @Override
        public Map<String, String> getQualifier() {
            return this.qualifier;
        }

        public void setQualifier(Map<String, String> qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public List<DelegateType.Builder> getDelegates() {
            return this.delegates;
        }

        public void setDelegates(List<DelegateType.Builder> delegates) {
            this.delegates = delegates;
        }

        @Override
        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String getMemberId() {
            return this.memberId;
        }

        public void setMemberId(String memberId) {
            if (StringUtils.isEmpty(memberId)) {
                throw new IllegalArgumentException("memberId cannot be empty or null");
            }
            this.memberId = memberId;
        }

        @Override
        public MemberType getType() {
            return this.type;
        }

        public void setType(MemberType type) {
            if (type == null) {
                throw new IllegalArgumentException("type cannot be null");
            }
            this.type = type;
        }

        @Override
        public String getEmbeddedRoleId() {
            return this.embeddedRoleId;
        }

        public void setEmbeddedRoleId(String embeddedRoleId) {
            this.embeddedRoleId = embeddedRoleId;
        }

        @Override
        public String getRoleSortingCode() {
            return this.roleSortingCode;
        }

        public void setRoleSortingCode(String roleSortingCode) {
            this.roleSortingCode = roleSortingCode;
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
        final static String ROLE_ID = "roleId";
        final static String ID = "id";
        final static String EMBEDDED_ROLE_ID = "embeddedRoleId";
        final static String MEMBER_ID = "memberId";
        final static String TYPE_CODE = "typeCode";
        final static String ROLE_SORTING_CODE = "roleSortingCode";
        final static String QUALIFIER = "qualifier";
        final static String DELEGATES = "delegates";
        final static String DELEGATE = "delegate";
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "roleMembership";
        final static String TYPE_NAME = "RoleMembershipType";
    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + RoleMembership.Constants.TYPE_NAME;
    }
}
