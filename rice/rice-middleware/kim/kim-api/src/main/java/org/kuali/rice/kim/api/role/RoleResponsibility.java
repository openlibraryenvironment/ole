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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.mo.ModelObjectComplete;
import org.kuali.rice.kim.api.KimConstants;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;

/**
 * An lightweight association of a Responsibility and a Role represented by references to the identifiers of a
 * Role and a Responsibility that are related to each other.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = RoleResponsibility.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RoleResponsibility.Constants.TYPE_NAME, propOrder = {
        RoleResponsibility.Elements.ROLE_RESPONSIBILITY_ID,
        RoleResponsibility.Elements.ROLE_ID,
        RoleResponsibility.Elements.RESPONSIBILITY_ID,
        CoreConstants.CommonElements.ACTIVE,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class RoleResponsibility extends AbstractDataTransferObject implements RoleResponsibilityContract {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = RoleResponsibility.Elements.ROLE_RESPONSIBILITY_ID, required = false)
    private final String roleResponsibilityId;

    @XmlElement(name = RoleResponsibility.Elements.ROLE_ID)
    private final String roleId;

    @XmlElement(name = RoleResponsibility.Elements.RESPONSIBILITY_ID)
    private final String responsibilityId;

    @XmlElement(name = CoreConstants.CommonElements.ACTIVE)
    private final boolean active;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER)
    private final Long versionNumber;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;


    /**
     * This constructor should never be called except during JAXB unmarshalling.
     */
    @SuppressWarnings("unused")
    private RoleResponsibility() {
        this.roleResponsibilityId = null;
        this.roleId = null;
        this.responsibilityId = null;
        this.versionNumber = null;
        this.active = false;
    }

    private RoleResponsibility(Builder b) {
        this.roleResponsibilityId = b.getRoleResponsibilityId();
        this.responsibilityId = b.getResponsibilityId();
        this.roleId = b.getRoleId();
        this.active = b.isActive();
        this.versionNumber = b.getVersionNumber();
    }

    @Override
    public String getResponsibilityId() {
        return this.responsibilityId;
    }

    @Override
    public String getRoleId() {
        return this.roleId;
    }

    @Override
    public String getRoleResponsibilityId() {
        return this.roleResponsibilityId;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    public static class Builder implements RoleResponsibilityContract, ModelBuilder, ModelObjectComplete {
        private String roleResponsibilityId;
        private String roleId;
        private String responsibilityId;
        private boolean active = true;
        private Long versionNumber;


        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(String roleId, String responsibilityId) {
            Builder b = create();

            b.setRoleId(roleId);
            b.setResponsibilityId(responsibilityId);
            return b;
        }

        public static Builder create(RoleResponsibilityContract rrContract) {
            Builder b = create();
            b.setRoleResponsibilityId(rrContract.getRoleResponsibilityId());
            b.setResponsibilityId(rrContract.getResponsibilityId());
            b.setRoleId(rrContract.getRoleId());
            b.setActive(rrContract.isActive());
            b.setVersionNumber(rrContract.getVersionNumber());
            return b;
        }

        @Override
        public RoleResponsibility build() {
            return new RoleResponsibility(this);
        }

        @Override
        public String getRoleResponsibilityId() {
            return roleResponsibilityId;
        }

        public void setRoleResponsibilityId(String roleResponsibilityId) {
            if (StringUtils.isWhitespace(roleResponsibilityId)) {
                throw new IllegalArgumentException("roleResponsibilityId cannot be whitespace");
            }
            this.roleResponsibilityId = roleResponsibilityId;
        }

        @Override
        public String getRoleId() {
            return roleId;
        }

        public void setRoleId(String roleId) {
            this.roleId = roleId;
        }

        @Override
        public String getResponsibilityId() {
            return responsibilityId;
        }

        public void setResponsibilityId(String responsibilityId) {
            this.responsibilityId = responsibilityId;
        }


        @Override
        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        @Override
        public Long getVersionNumber() {
            return versionNumber;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
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
        final static String ROLE_RESPONSIBILITY_ID = "roleResponsibilityId";
        final static String ROLE_ID = "roleId";
        final static String RESPONSIBILITY_ID = "responsibilityId";
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "roleResponsibility";
        final static String TYPE_NAME = "RoleResponsibilityType";
    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + RoleResponsibility.Constants.TYPE_NAME;
    }
}
