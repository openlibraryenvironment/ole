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
package org.kuali.rice.kim.api.group;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.mo.common.active.InactivatableFromToUtils;
import org.kuali.rice.core.api.util.jaxb.DateTimeAdapter;
import org.kuali.rice.kim.api.KimConstants;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Collection;

@XmlRootElement(name = GroupMember.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = GroupMember.Constants.TYPE_NAME, propOrder = {
        GroupMember.Elements.ID,
        GroupMember.Elements.GROUP_ID,
        GroupMember.Elements.MEMBER_ID,
        GroupMember.Elements.TYPE_CODE,
        CoreConstants.CommonElements.ACTIVE_FROM_DATE,
        CoreConstants.CommonElements.ACTIVE_TO_DATE,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class GroupMember extends AbstractDataTransferObject implements GroupMemberContract  {

    @XmlElement(name = Elements.ID, required = false)
    private final String id;

    @XmlElement(name = Elements.GROUP_ID, required = true)
    private final String groupId;

    @XmlElement(name = Elements.MEMBER_ID, required = true)
    private final String memberId;

    @XmlElement(name = Elements.TYPE_CODE, required = true)
    private final String typeCode;

    @XmlElement(name = CoreConstants.CommonElements.ACTIVE_FROM_DATE, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime activeFromDate;

    @XmlElement(name = CoreConstants.CommonElements.ACTIVE_TO_DATE, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
	private final DateTime activeToDate;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    private GroupMember() {
        this.id = null;
        this.groupId = null;
        this.memberId = null;
        this.typeCode = null;
        this.versionNumber = null;
        this.objectId = null;
        this.activeFromDate = null;
        this.activeToDate = null;
    }


    public GroupMember(Builder builder) {
        this.id = builder.getId();
        this.groupId = builder.getGroupId();
        this.memberId = builder.getMemberId();
        this.typeCode = builder.getType().getCode();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
        this.activeFromDate = builder.getActiveFromDate();
        this.activeToDate = builder.getActiveToDate();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getGroupId() {
        return groupId;
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
    public DateTime getActiveFromDate() {
        return activeFromDate;
    }

    @Override
    public DateTime getActiveToDate() {
        return activeToDate;
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
    public boolean isActive(DateTime activeAsOf) {
        return InactivatableFromToUtils.isActive(activeFromDate, activeToDate, activeAsOf);
    }

    @Override
    public boolean isActive() {
        return InactivatableFromToUtils.isActive(activeFromDate, activeToDate, null);
    }

    public static class Builder implements GroupMemberContract, ModelBuilder, Serializable {
        private String id;
        private String groupId;
        private String memberId;
        private MemberType type;
        private DateTime activeFromDate;
        private DateTime activeToDate;
        private Long versionNumber;
        private String objectId;

        private Builder(String groupId, String memberId, MemberType type) {
            setGroupId(groupId);
            setMemberId(memberId);
            setType(type);
        }

        /**
         * creates a Parameter with the required fields.
         */
        public static Builder create(String groupId, String memberId, MemberType type) {
            return new Builder(groupId, memberId, type);
        }

        /**
         * creates a GroupMember from an existing {@link org.kuali.rice.kim.api.group.GroupMemberContract}.
         */
        public static Builder create(GroupMemberContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = new Builder(contract.getGroupId(), contract.getMemberId(), contract.getType());
            builder.setId(contract.getId());
            builder.setActiveFromDate(contract.getActiveFromDate());
            builder.setActiveToDate(contract.getActiveToDate());
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
        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(final String groupId) {
            if (StringUtils.isEmpty(groupId)) {
                throw new IllegalArgumentException("groupId is empty");
            }
            this.groupId = groupId;
        }

        @Override
        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(final String memberId) {
            if (StringUtils.isEmpty(memberId)) {
                throw new IllegalArgumentException("memberId is empty");
            }
            this.memberId = memberId;
        }

        @Override
        public MemberType getType() {
            return type;
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

        public void setActiveFromDate(final DateTime activeFromDate) {
            this.activeFromDate = activeFromDate;
        }

        @Override
        public DateTime getActiveToDate() {
            return activeToDate;
        }

        public void setActiveToDate(final DateTime activeToDate) {
            this.activeToDate = activeToDate;
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
        public boolean isActive(DateTime activeAsOf) {
            return InactivatableFromToUtils.isActive(activeFromDate, activeToDate, activeAsOf);
        }

        @Override
        public boolean isActive() {
            return InactivatableFromToUtils.isActive(activeFromDate, activeToDate, null);
        }

        @Override
        public GroupMember build() {
            return new GroupMember(this);
        }
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "groupMember";
        final static String TYPE_NAME = "GroupMemberType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshalled to XML.
     */
    static class Elements {
        final static String ID = "id";
        final static String GROUP_ID = "groupId";
        final static String MEMBER_ID = "memberId";
        final static String TYPE_CODE = "typeCode";
    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + GroupMember.Constants.TYPE_NAME;
    }
}
