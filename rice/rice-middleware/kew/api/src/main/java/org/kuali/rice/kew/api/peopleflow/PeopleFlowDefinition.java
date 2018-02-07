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
package org.kuali.rice.kew.api.peopleflow;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.mo.ModelObjectUtils;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = PeopleFlowDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = PeopleFlowDefinition.Constants.TYPE_NAME, propOrder = {
        PeopleFlowDefinition.Elements.ID,
        PeopleFlowDefinition.Elements.NAMESPACE_CODE,
        PeopleFlowDefinition.Elements.NAME,
        PeopleFlowDefinition.Elements.TYPE_ID,
        PeopleFlowDefinition.Elements.DESCRIPTION,
        PeopleFlowDefinition.Elements.MEMBERS,
        PeopleFlowDefinition.Elements.ATTRIBUTES,
        PeopleFlowDefinition.Elements.ACTIVE,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class PeopleFlowDefinition extends AbstractDataTransferObject implements PeopleFlowContract {

    @XmlElement(name = Elements.NAME, required = true)
    private final String name;

    @XmlElement(name = Elements.ATTRIBUTES, required = false)
    @XmlJavaTypeAdapter(MapStringStringAdapter.class)
    private final Map<String, String> attributes;

    @XmlElement(name = Elements.NAMESPACE_CODE, required = true)
    private final String namespaceCode;

    @XmlElement(name = Elements.TYPE_ID, required = false)
    private final String typeId;

    @XmlElement(name = Elements.DESCRIPTION, required = false)
    private final String description;

    @XmlElementWrapper(name = Elements.MEMBERS, required = false)
    @XmlElement(name = Elements.MEMBER, required = false)
    private final List<PeopleFlowMember> members;
    
    @XmlElement(name = Elements.ID, required = false)
    private final String id;

    @XmlElement(name = Elements.ACTIVE, required = false)
    private final boolean active;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private PeopleFlowDefinition() {
        this.name = null;
        this.attributes = null;
        this.namespaceCode = null;
        this.typeId = null;
        this.description = null;
        this.members = null;
        this.id = null;
        this.active = false;
        this.versionNumber = null;
    }

    private PeopleFlowDefinition(Builder builder) {
        this.name = builder.getName();
        this.attributes = builder.getAttributes();
        this.namespaceCode = builder.getNamespaceCode();
        this.typeId = builder.getTypeId();
        this.description = builder.getDescription();
        this.members = ModelObjectUtils.buildImmutableCopy(builder.getMembers());
        this.id = builder.getId();
        this.active = builder.isActive();
        this.versionNumber = builder.getVersionNumber();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getNamespaceCode() {
        return this.namespaceCode;
    }

    @Override
    public String getTypeId() {
        return this.typeId;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public List<PeopleFlowMember> getMembers() {
        return this.members;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    /**
     * A builder which can be used to construct {@link PeopleFlowDefinition} instances.  Enforces the constraints of the
     * {@link PeopleFlowContract}.
     */
    public final static class Builder implements Serializable, ModelBuilder, PeopleFlowContract {

        private String name;
        private Map<String, String> attributes;
        private String namespaceCode;
        private String typeId;
        private String description;
        private List<PeopleFlowMember.Builder> members;
        private String id;
        private boolean active;
        private Long versionNumber;

        private Builder(String namespaceCode, String name) {
            setNamespaceCode(namespaceCode);
            setName(name);
            setActive(true);
            setAttributes(new HashMap<String, String>());
            setMembers(new ArrayList<PeopleFlowMember.Builder>());
        }

        public static Builder create(String namespaceCode, String name) {
            return new Builder(namespaceCode, name);
        }

        public static Builder create(PeopleFlowContract contract) {
            Builder builder = createCopy(contract);
            builder.setVersionNumber(contract.getVersionNumber());
            if (contract.getMembers() != null) {
                for (PeopleFlowMemberContract member : contract.getMembers()) {
                    builder.getMembers().add(PeopleFlowMember.Builder.create(member));
                }
            }
            return builder;
        }

        private static Builder createCopy(PeopleFlowContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(contract.getNamespaceCode(), contract.getName());
            if (contract.getAttributes() != null) {
                builder.getAttributes().putAll(contract.getAttributes());
            }
            if (StringUtils.isEmpty(contract.getTypeId())) {
                // type_id is a foreign key, it needs to be either null or a real value, not empty String to avoid SQL Exception
                builder.setTypeId(null);
            } else {
                builder.setTypeId(contract.getTypeId());
            }
            builder.setDescription(contract.getDescription());
            builder.setId(contract.getId());
            builder.setActive(contract.isActive());
            return builder;
        }

        public static Builder createMaintenanceCopy(PeopleFlowContract contract) {
            Builder builder = createCopy(contract);
            if (contract.getMembers() != null) {
                for (PeopleFlowMemberContract member : contract.getMembers()) {
                    builder.getMembers().add(PeopleFlowMember.Builder.createCopy(member));
                }
            }
            return builder;
        }

        public PeopleFlowDefinition build() {
            return new PeopleFlowDefinition(this);
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public Map<String, String> getAttributes() {
            return this.attributes;
        }

        @Override
        public String getNamespaceCode() {
            return this.namespaceCode;
        }

        @Override
        public String getTypeId() {
            return this.typeId;
        }

        @Override
        public String getDescription() {
            return this.description;
        }

        @Override
        public List<PeopleFlowMember.Builder> getMembers() {
            return this.members;
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public boolean isActive() {
            return this.active;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        public void setName(String name) {
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("name was null or blank");
            }
            this.name = name;
        }

        public void setAttributes(Map<String, String> attributes) {
            this.attributes = attributes;
        }

        public void setNamespaceCode(String namespaceCode) {
            if (StringUtils.isBlank(namespaceCode)) {
                throw new IllegalArgumentException("namespaceCode was null or blank");
            }
            this.namespaceCode = namespaceCode;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setMembers(List<PeopleFlowMember.Builder> members) {
            this.members = members;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        public PeopleFlowMember.Builder addPrincipal(String principalId) {
            PeopleFlowMember.Builder member = PeopleFlowMember.Builder.create(principalId, MemberType.PRINCIPAL);
            getMembers().add(member);
            return member;
        }

        public PeopleFlowMember.Builder addGroup(String groupId) {
            PeopleFlowMember.Builder member = PeopleFlowMember.Builder.create(groupId, MemberType.GROUP);
            getMembers().add(member);
            return member;
        }

        public PeopleFlowMember.Builder addRole(String roleId) {
            PeopleFlowMember.Builder member = PeopleFlowMember.Builder.create(roleId, MemberType.ROLE);
            getMembers().add(member);
            return member;
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "peopleFlowDefinition";
        final static String TYPE_NAME = "PeopleFlowDefinitionType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String NAME = "name";
        final static String ATTRIBUTES = "attributes";
        final static String NAMESPACE_CODE = "namespaceCode";
        final static String TYPE_ID = "typeId";
        final static String DESCRIPTION = "description";
        final static String MEMBERS = "members";
        final static String MEMBER = "member";
        final static String ID = "id";
        final static String ACTIVE = "active";
    }

}
