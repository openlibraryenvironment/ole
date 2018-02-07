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
 * This is a description of what this class does - shyu don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = Role.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = Role.Constants.TYPE_NAME, propOrder = {
        Role.Elements.ID,
        Role.Elements.NAME,
        Role.Elements.NAMESPACE_CODE,
        Role.Elements.DESCRIPTION,
        Role.Elements.KIM_TYPE_ID,
        Role.Elements.ACTIVE,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class Role extends AbstractDataTransferObject implements RoleContract {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = Role.Elements.ID, required = true)
    private final String id;

    @XmlElement(name = Role.Elements.NAME, required = true)
    private final String name;

    @XmlElement(name = Role.Elements.NAMESPACE_CODE, required = true)
    private final String namespaceCode;

    @XmlElement(name = Role.Elements.DESCRIPTION)
    private final String description;

    @XmlElement(name = Role.Elements.KIM_TYPE_ID, required = true)
    private final String kimTypeId;

    @XmlElement(name = CoreConstants.CommonElements.ACTIVE, required = true)
    private final boolean active;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER)
    private final Long versionNumber;

    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;


    /**
     * This constructor should never be called except during JAXB unmarshalling.
     */
    @SuppressWarnings("unused")
    private Role() {
        id = null;
        name = null;
        namespaceCode = null;
        description = null;
        kimTypeId = null;
        active = false;
        objectId = null;
        versionNumber = null;
    }

    private Role(Builder builder) {
        id = builder.getId();
        name = builder.getName();
        namespaceCode = builder.getNamespaceCode();
        description = builder.getDescription();
        kimTypeId = builder.getKimTypeId();
        active = builder.isActive();
        versionNumber = builder.getVersionNumber();
        objectId = builder.getObjectId();
    }


    /**
     * Unique identifier for this role.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Namespace for this role - identifies the system/module to which this role applies
     */
    @Override
    public String getNamespaceCode() {
        return namespaceCode;
    }

    /**
     * Name for this role.  This value will be seen by the users.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Verbose description of the role and functionally what permissions it implies.
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Type identifier for this role.  This will control what additional attributes are available
     */
    @Override
    public String getKimTypeId() {
        return kimTypeId;
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
    public boolean isActive() {
        return active;
    }

    public static final class Builder implements RoleContract, ModelBuilder, ModelObjectComplete {

        private String id;
        private String name;
        private String namespaceCode;
        private String description;
        private String kimTypeId;
        private boolean active;
        private Long versionNumber;
        private String objectId;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(String id, String name, String namespaceCode, String description, String kimTypeId) {
            Builder b = new Builder();
            b.setId(id);
            b.setName(name);
            b.setNamespaceCode(namespaceCode);
            b.setDescription(description);
            b.setKimTypeId(kimTypeId);
            b.setActive(true);

            return b;
        }

        public static Builder create(RoleContract roleContract) {
            Builder b = new Builder();
            b.setId(roleContract.getId());
            b.setName(roleContract.getName());
            b.setNamespaceCode(roleContract.getNamespaceCode());
            b.setDescription(roleContract.getDescription());
            b.setKimTypeId(roleContract.getKimTypeId());
            b.setActive(roleContract.isActive());
            b.setVersionNumber(roleContract.getVersionNumber());
            b.setObjectId(roleContract.getObjectId());
            return b;
        }

        @Override
        public Role build() {
            return new Role(this);
        }

        public void setId(String id) {
            if (StringUtils.isWhitespace(id)) {
                throw new IllegalArgumentException("id cannot be blank");
            }
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }

        public void setNamespaceCode(String namespaceCode) {
            if (StringUtils.isBlank(namespaceCode)) {
                throw new IllegalArgumentException("namespaceCode cannot be blank or null");
            }
            this.namespaceCode = namespaceCode;
        }

        @Override
        public String getNamespaceCode() {
            return namespaceCode;
        }

        public void setName(String name) {
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("name cannot be blank or null");
            }
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String getDescription() {
            return description;
        }

        public void setKimTypeId(String kimTypeId) {
            if (StringUtils.isBlank(kimTypeId)) {
                throw new IllegalArgumentException("kimTypeId cannot be blank or null");
            }
            this.kimTypeId = kimTypeId;
        }

        @Override
        public String getKimTypeId() {
            return kimTypeId;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        @Override
        public boolean isActive() {
            return active;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        @Override
        public Long getVersionNumber() {
            return versionNumber;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        @Override
        public String getObjectId() {
            return objectId;
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
        final static String NAME = "name";
        final static String DESCRIPTION = "description";
        final static String KIM_TYPE_ID = "kimTypeId";
        final static String NAMESPACE_CODE = "namespaceCode";
        final static String ACTIVE = "active";
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "role";
        final static String TYPE_NAME = "RoleType";
    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + Role.Constants.TYPE_NAME;
    }
}
