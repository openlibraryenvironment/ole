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
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Collection;


@XmlRootElement(name = RolePermission.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RolePermission.Constants.TYPE_NAME, propOrder = {
        RolePermission.Elements.ID,
        RolePermission.Elements.ROLE_ID,
        RolePermission.Elements.PERMISSION_ID,
        RolePermission.Elements.ACTIVE,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class RolePermission extends AbstractDataTransferObject implements RolePermissionContract {
    @XmlElement(name = Elements.ID, required = false)
    private final String id;

    @XmlElement(name = RolePermission.Elements.ROLE_ID, required = false)
    private final String roleId;

    @XmlElement(name = RolePermission.Elements.PERMISSION_ID, required = false)
    private final String permissionId;

    @XmlElement(name = RolePermission.Elements.ACTIVE, required = false)
    private final boolean active;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * A constructor to be used only by JAXB unmarshalling.
     */
    private RolePermission() {
        this.id = null;
        this.roleId = null;
        this.permissionId = null;
        this.active = false;
        this.versionNumber = null;
        this.objectId = null;
    }

    /**
     * A constructor using the Builder.
     *
     * @param builder
     */
    public RolePermission(Builder builder) {
        this.id = builder.getId();
        this.roleId = builder.getRoleId();
        this.permissionId = builder.getPermissionId();
        this.active = builder.isActive();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getPermissionId() {
        return permissionId;
    }

    @Override
    public String getRoleId() {
        return roleId;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }

    @Override
    public String getObjectId() {
        return objectId;
    }

    /**
     * This builder constructs a RolePermission enforcing the constraints of the {@link RolePermissionContract}.
     */
    public static final class Builder implements RolePermissionContract, ModelBuilder, Serializable {
        private String id;
        private String roleId;
        private String permissionId;
        private Long versionNumber = 1L;
        private String objectId;
        private boolean active;

        private Builder(String id, String roleId, String permissionId) {
            setId(id);
            setRoleId(roleId);
            setPermissionId(permissionId);
        }

        /**
         * Creates a RolePermission with the required fields.
         */
        public static Builder create(String id, String roleId, String permissionId) {
            return new Builder(id, roleId, permissionId);
        }

        /**
         * Creates a RolePermission from an existing {@link RolePermissionContract}.
         */
        public static Builder create(RolePermissionContract contract) {
            Builder builder = new Builder(contract.getId(), contract.getRoleId(), contract.getPermissionId());
            builder.setActive(contract.isActive());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());

            return builder;
        }

        @Override
        public String getId() {
            return id;
        }

        public void setId(final String id) {
            if (StringUtils.isWhitespace(id)) {
                throw new IllegalArgumentException("id is blank");
            }
            this.id = id;
        }

        @Override
        public String getPermissionId() {
            return permissionId;
        }

        public void setPermissionId(final String permissionId) {
            this.permissionId = permissionId;
        }

        @Override
        public String getRoleId() {
            return roleId;
        }

        public void setRoleId(final String roleId) {
            this.roleId = roleId;
        }

        @Override
        public Long getVersionNumber() {
            return versionNumber;
        }

        public void setVersionNumber(Long versionNumber) {
            if (versionNumber != null && versionNumber <= 0) {
                throw new IllegalArgumentException("versionNumber is invalid");
            }
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
        public boolean isActive() {
            return active;
        }

        public void setActive(final boolean active) {
            this.active = active;
        }

        @Override
        public RolePermission build() {
            if (versionNumber == null || versionNumber <= 0) {
                throw new IllegalStateException("versionNumber is invalid");
            }
            if (StringUtils.isWhitespace(id)) {
                throw new IllegalStateException("id is blank");
            }
            return new RolePermission(this);
        }
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "rolePermission";
        final static String TYPE_NAME = "RolePermissionType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshalled to XML.
     */
    static class Elements {
        final static String ID = "id";
        final static String PERMISSION_ID = "permissionId";
        final static String ROLE_ID = "roleId";
        final static String ACTIVE = "active";
    }
}
