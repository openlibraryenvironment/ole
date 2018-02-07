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
package org.kuali.rice.kim.impl.group

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.Transient

import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.Type
import org.joda.time.DateTime
import org.kuali.rice.kim.api.KimConstants.KimGroupMemberTypes
import org.kuali.rice.kim.api.group.Group
import org.kuali.rice.kim.api.identity.Person
import org.kuali.rice.kim.api.services.KimApiServiceLocator
import org.kuali.rice.kim.api.type.KimType
import org.kuali.rice.kim.framework.group.GroupEbo
import org.kuali.rice.kim.impl.common.attribute.KimAttributeDataBo
import org.kuali.rice.kim.impl.type.KimTypeBo
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase

@Entity
@Table(name="KRIM_GRP_T")
public class GroupBo extends PersistableBusinessObjectBase implements GroupEbo {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="GRP_ID")
    String id

    @Column(name="GRP_NM")
    String name

    @Column(name="GRP_DESC",length=4000)
    String description

    @Column(name="ACTV_IND")
    @Type(type="yes_no")
    boolean active

    @Column(name="KIM_TYP_ID")
    String kimTypeId

    @Column(name="NMSPC_CD")
    String namespaceCode

    @OneToMany(targetEntity=GroupMemberBo.class,cascade=[CascadeType.ALL],fetch=FetchType.EAGER,mappedBy="id")
    @Fetch(value = FetchMode.SELECT)
    List<GroupMemberBo> members

    @OneToMany(targetEntity=GroupAttributeBo.class,cascade=[CascadeType.ALL],fetch=FetchType.EAGER,mappedBy="id")
    @Fetch(value = FetchMode.SELECT)
    List<GroupAttributeBo> attributeDetails

    @Transient
    private List<Person> memberPersons

    @Transient
    private List<Group> memberGroups

    @Transient
    Map<String,String> attributes

    Map<String,String> getAttributes() {
        return attributeDetails != null ? KimAttributeDataBo.toAttributes(attributeDetails) : attributes
    }

    /**
     * Converts a mutable bo to its immutable counterpart
     * @param bo the mutable business object
     * @return the immutable object
     */
    static Group to(GroupBo bo) {
        if (bo == null) {
            return null
        }

        return Group.Builder.create(bo).build();
    }

    /**
     * Converts a immutable object to its mutable counterpart
     * @param im immutable object
     * @return the mutable bo
     */
    static GroupBo from(Group im) {
        if (im == null) {
            return null
        }

        GroupBo bo = new GroupBo()
        bo.id = im.id
        bo.namespaceCode = im.namespaceCode
        bo.name = im.name
        bo.description = im.description
        bo.active = im.active
        bo.kimTypeId = im.kimTypeId
        bo.attributes = im.attributes
        //bo.attributeDetails = KimAttributeDataBo.createFrom(GroupAttributeBo.class, im.attributes, im.kimTypeId )
        bo.versionNumber = im.versionNumber
        bo.objectId = im.objectId;

        return bo
    }

    //helper function to get Attribute Value with specific id
    public String getGroupAttributeValueById(String attributeId) {
        for (GroupAttributeBo gad : getAttributeDetails()) {
            if (gad.getKimAttributeId().equals(attributeId.trim())) {
                return gad.getAttributeValue();
            }
        }
        return null;
    }

    private void splitMembersToTypes() {
        memberPersons = new ArrayList<Person>()
        memberGroups = new ArrayList<Group>()
        if (getMembers() != null) {
            for ( GroupMemberBo groupMember : getMembers() ) {
                if (groupMember.isActive(new DateTime())) {
                    if ( KimGroupMemberTypes.PRINCIPAL_MEMBER_TYPE.equals(groupMember.getType())) {
                        Person tempPerson =  KimApiServiceLocator.getPersonService().getPerson(groupMember.getMemberId())
                        if (tempPerson != null && tempPerson.isActive()) {
                            memberPersons.add(tempPerson)
                        }
                    } else if (KimGroupMemberTypes.GROUP_MEMBER_TYPE.equals(groupMember.getType())) {
                        Group tempGroup =  KimApiServiceLocator.getGroupService().getGroup(groupMember.getMemberId())
                        if (tempGroup != null && tempGroup.isActive()) {
                            memberGroups.add(tempGroup)
                        }
                    }
                }
            }
        }
    }

    public List<Person> getMemberPersons() {
        if (this.memberPersons == null) {
            splitMembersToTypes()
        }
        return this.memberPersons
    }

    public void setMemberPersons(List<Person> memberPersons) {
        this.memberPersons = memberPersons;
    }

    public List<String> getMemberPrincipalIds() {
        List<String> principalIds = new ArrayList<String>()
        if (getMembers() != null) {
            for ( GroupMemberBo groupMember : getMembers() ) {
                if (groupMember.isActive(new DateTime())) {
                    if ( KimGroupMemberTypes.PRINCIPAL_MEMBER_TYPE.equals(groupMember.getType())) {
                        principalIds.add(groupMember.getMemberId());
                    }
                }
            }
        }
        return principalIds;
    }

    public List<String> getMemberGroupIds() {
        List<String> principalIds = new ArrayList<String>()
        if (getMembers() != null) {
            for ( GroupMemberBo groupMember : getMembers() ) {
                if (groupMember.isActive(new DateTime())) {
                    if ( KimGroupMemberTypes.GROUP_MEMBER_TYPE.equals(groupMember.getType())) {
                        principalIds.add(groupMember.getMemberId());
                    }
                }
            }
        }
        return principalIds;
    }

    public List<Group> getMemberGroups() {
        if (this.memberGroups == null) {
            splitMembersToTypes()
        }
        return this.memberGroups
    }

    public void setMemberGroups(List<Group> memberGroups) {
        this.memberGroups = memberGroups;
    }

    public KimTypeBo getKimTypeInfo() {
        if (this.kimTypeId == null) {
            return null
        }
        KimType type = KimApiServiceLocator.getKimTypeInfoService().getKimType(this.kimTypeId)
        if (type == null) {
            return null
        }
        return KimTypeBo.from(type)
    }
}
