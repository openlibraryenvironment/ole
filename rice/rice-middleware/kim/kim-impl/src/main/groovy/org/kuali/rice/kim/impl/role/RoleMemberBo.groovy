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
package org.kuali.rice.kim.impl.role;


import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.Transient
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.kuali.rice.kim.api.role.Role
import org.kuali.rice.kim.api.role.RoleMember
import org.kuali.rice.kim.api.role.RoleMemberContract
import org.kuali.rice.kim.api.services.KimApiServiceLocator
import org.kuali.rice.kim.api.type.KimTypeInfoService
import org.kuali.rice.kim.impl.common.attribute.KimAttributeDataBo
import org.kuali.rice.kim.impl.membership.AbstractMemberBo
import org.springframework.util.AutoPopulatingList
import java.sql.Timestamp
import org.kuali.rice.core.api.membership.MemberType
import org.kuali.rice.kim.api.group.Group
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.role.RoleService;
import org.apache.commons.lang.StringUtils
import org.kuali.rice.kim.api.identity.principal.Principal
import org.kuali.rice.krad.bo.BusinessObject
import org.kuali.rice.kim.impl.group.GroupBo
import org.kuali.rice.kim.impl.identity.principal.PrincipalBo;
import org.kuali.rice.kim.api.KimConstants
import org.kuali.rice.krad.service.KRADServiceLocator
import org.apache.commons.lang.ObjectUtils;

/**
 * The column names have been used in a native query in RoleDaoOjb and will need to be modified if any changes to the
 * column names are made here.
 */
@Entity
@Table(name = "KRIM_ROLE_MBR_T")
public class RoleMemberBo extends AbstractMemberBo implements RoleMemberContract {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "KRIM_ROLE_MBR_ID_S")
    @GenericGenerator(name = "KRIM_ROLE_MBR_ID_S", strategy = "org.kuali.rice.core.jpa.spring.RiceNumericStringSequenceStyleGenerator", parameters = [
    @Parameter(name = "sequence_name", value = "KRIM_ROLE_MBR_ID_S"),
    @Parameter(name = "value_column", value = "id")
    ])
    @Column(name = "ROLE_MBR_ID")
    String id;

    @Column(name = "ROLE_ID")
    String roleId;

    @OneToMany(targetEntity = RoleMemberAttributeDataBo.class, cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "ROLE_MBR_ID", insertable = false, updatable = false)
    List<RoleMemberAttributeDataBo> attributeDetails; // = new AutoPopulatingList(RoleMemberAttributeDataBo.class);

    @Transient
    List<RoleResponsibilityActionBo> roleRspActions;

    @Transient
    Map<String, String> attributes

    @Transient
    protected String memberName;

    @Transient
    protected String memberNamespaceCode;


    List<RoleMemberAttributeDataBo> getAttributeDetails() {
        if (this.attributeDetails == null) {
            return new AutoPopulatingList(RoleMemberAttributeDataBo.class);
        }
        return this.attributeDetails;
    }

    void setAttributeDetails(List<RoleMemberAttributeDataBo> attributeDetails) {
        this.attributeDetails = attributeDetails;
    }

    Map<String, String> getAttributes() {
        return attributeDetails != null ? KimAttributeDataBo.toAttributes(attributeDetails) : attributes
    }

    public static RoleMember to(RoleMemberBo bo) {
        if (bo == null) {return null;}
        return RoleMember.Builder.create(bo).build();
    }

    public static RoleMemberBo from(RoleMember immutable) {
        if (immutable == null) { return null; }

        return new RoleMemberBo(
                id: immutable.id,
                roleId: immutable.roleId,
                roleRspActions: immutable.roleRspActions.collect { RoleResponsibilityActionBo.from(it) },
                memberId: immutable.memberId,
                typeCode: immutable.getType().code,
                activeFromDateValue: immutable.activeFromDate == null ? null : new Timestamp(immutable.activeFromDate.getMillis()),
                activeToDateValue: immutable.activeToDate == null ? null : new Timestamp(immutable.activeToDate.getMillis()),
                objectId: immutable.objectId,
                versionNumber: immutable.versionNumber,
                memberName: immutable.memberName,
                memberNamespaceCode: immutable.memberNamespaceCode

        )
    }

    public getTypeCode() {
        return this.typeCode
    }

    protected BusinessObject getMember(MemberType memberType, String memberId) {
        if (MemberType.PRINCIPAL.equals(memberType)) {
            return PrincipalBo.from(KimApiServiceLocator.getIdentityService().getPrincipal(memberId));
        } else if (MemberType.GROUP.equals(memberType)) {
           return GroupBo.from(KimApiServiceLocator.getGroupService().getGroup(memberId));
        } else if (MemberType.ROLE.equals(memberType)) {
           return RoleBo.from(KimApiServiceLocator.getRoleService().getRole(memberId));
        }
    }

    public String getMemberName() {
        if (getType() == null || StringUtils.isEmpty(getMemberId())) { return "";}
        BusinessObject member = getMember(getType(), memberId);
        if (member == null) {
            this.memberName = "";
            Principal kp = KimApiServiceLocator.getIdentityService().getPrincipal(getMemberId());
            if (kp != null && kp.getPrincipalName() != null && !"".equals(kp.getPrincipalName())) {
                this.memberName = kp.getPrincipalName();
            }

            return this.memberName;
        }
        return getRoleMemberName(getType(), member);
    }

    public String getRoleMemberName(MemberType memberType, BusinessObject member) {
        String roleMemberName = "";
        if (MemberType.PRINCIPAL.equals(memberType)) {
            roleMemberName = ((PrincipalBo) member).getPrincipalName();
        } else if (MemberType.GROUP.equals(memberType)) {
            roleMemberName = ((GroupBo) member).getName();
        } else if (MemberType.ROLE.equals(memberType)) {
            roleMemberName = ((RoleBo) member).getName();
        }
        return roleMemberName;
    }

    public String getMemberNamespaceCode() {
        if (getType() == null || StringUtils.isEmpty(getMemberId())) {return "";}
        this.memberNamespaceCode = "";
        if (MemberType.PRINCIPAL.equals(getType())) {
            this.memberNamespaceCode = "";
        } else if (MemberType.GROUP.equals(getType())) {
            Group groupInfo = KimApiServiceLocator.getGroupService().getGroup(memberId);
            if (groupInfo != null) {
                this.memberNamespaceCode = groupInfo.getNamespaceCode();
            }
        } else if (MemberType.ROLE.equals(getType())) {
            Role role = KimApiServiceLocator.getRoleService().getRole(memberId);
            if (role != null) {
                this.memberNamespaceCode = role.getNamespaceCode();
            }
        }
        return this.memberNamespaceCode;

    }

    @Override
    public List buildListOfDeletionAwareLists() {
        List managedLists = super.buildListOfDeletionAwareLists();

        managedLists.add(attributeDetails);
        return managedLists;
    }

    /**
     *
     * This method compares role member passed with this role member object and returns true if no differences or returns false.
     *
     * @param targetMbrImpl
     * @return boolean true if the member has not changed, false if the member has changed
     */
    public boolean equals(RoleMemberBo targetMbrBo) {
        if (!StringUtils.equals(getId(), targetMbrBo.getId())) {
            return false;
        }

        if (!StringUtils.equals(getType().getCode(), targetMbrBo.getType().getCode())) {
            return false;
        }

        if (!StringUtils.equals(getMemberId(), targetMbrBo.getMemberId())) {
            return false;
        }

        if (!ObjectUtils.equals(getActiveFromDate(), targetMbrBo.getActiveFromDate())) {
            return false;
        }

        if (!ObjectUtils.equals(getActiveToDate(), targetMbrBo.getActiveToDate())) {
            return false;
        }

        // Prepare list of attributes from this role member eliminating blank attributes
        Map<String, String> sourceMbrAttrDataList =  new HashMap<String, String>();

        for (Iterator iter = getAttributes().entrySet().iterator(); iter.hasNext();) {
            Map.Entry mbrAttr = (Map.Entry) iter.next();
            if (StringUtils.isNotBlank(mbrAttr.getValue())) {
                sourceMbrAttrDataList.put(mbrAttr.getKey(), mbrAttr.getValue());
            };
        }

        // Prepare list of attributes from target role member eliminating blank attributes
        Map<String, String> targetMbrAttrDataList =  new HashMap<String, String>();

        for (Iterator iter = targetMbrBo.getAttributes().entrySet().iterator(); iter.hasNext();) {
            Map.Entry mbrAttr = (Map.Entry) iter.next();
            if (StringUtils.isNotBlank(mbrAttr.getValue())) {
                targetMbrAttrDataList.put(mbrAttr.getKey(), mbrAttr.getValue());
            };
        }

        if (targetMbrAttrDataList.size() != sourceMbrAttrDataList.size()) {
            return false;
        }

        // Check if any attributes changed, then return false
        Map<String, String> matchedAttrs =  new HashMap<String, String>();
        for (Iterator iter = sourceMbrAttrDataList.entrySet().iterator(); iter.hasNext();) {
            Map.Entry newAttr = (Map.Entry) iter.next();
            for (Iterator iter2 = targetMbrAttrDataList.entrySet().iterator(); iter2.hasNext();) {
                Map.Entry origAttr = (Map.Entry) iter2.next();
                if (StringUtils.equals(origAttr.getKey(), newAttr.getKey())) {
                    if (StringUtils.equals(origAttr.getValue(), newAttr.getValue())) {
                        matchedAttrs.put(newAttr.getKey(), newAttr.getValue());
                    }
                }
            }
        }


        if (matchedAttrs.size() != sourceMbrAttrDataList.size()) {
            return false;
        }

        // Check responsibility actions
        int targetMbrActionsSize = (targetMbrBo.getRoleRspActions() == null) ? 0 : targetMbrBo.getRoleRspActions().size();
        int sourceMbrActionsSize = (getRoleRspActions() == null) ? 0 : getRoleRspActions().size();

        if (targetMbrActionsSize != sourceMbrActionsSize) {
            return false;
        }

        if (sourceMbrActionsSize != 0) {
            List<RoleResponsibilityActionBo> matchedRspActions = new ArrayList<RoleResponsibilityActionBo>();

            // Check if any responsibility actions changed
            for (RoleResponsibilityActionBo newAction : getRoleRspActions()) {
                for (RoleResponsibilityActionBo origAction : targetMbrBo.getRoleRspActions()) {

                    if (StringUtils.equals(origAction.getId(), newAction.getId())) {
                        if (origAction.equals(newAction)) {
                            matchedRspActions.add(newAction);
                        }
                    }
                }
            }

            if (matchedRspActions.size() != getRoleRspActions().size()) {
                return false;
            }
        }

        return true;
    }

}
