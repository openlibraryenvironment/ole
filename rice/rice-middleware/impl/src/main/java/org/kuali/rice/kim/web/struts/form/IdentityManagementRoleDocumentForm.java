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
package org.kuali.rice.kim.web.struts.form;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.web.format.DateDisplayTimestampObjectFormatter;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.bo.ui.KimDocumentRoleMember;
import org.kuali.rice.kim.bo.ui.KimDocumentRolePermission;
import org.kuali.rice.kim.bo.ui.KimDocumentRoleQualifier;
import org.kuali.rice.kim.bo.ui.KimDocumentRoleResponsibility;
import org.kuali.rice.kim.bo.ui.RoleDocumentDelegationMember;
import org.kuali.rice.kim.bo.ui.RoleDocumentDelegationMemberQualifier;
import org.kuali.rice.kim.document.IdentityManagementRoleDocument;
import org.kuali.rice.kim.impl.group.GroupBo;
import org.kuali.rice.kim.impl.identity.PersonImpl;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.kns.util.TableRenderUtil;

import java.util.List;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class IdentityManagementRoleDocumentForm extends IdentityManagementDocumentFormBase {
	protected static final long serialVersionUID = 7099079353241080483L;
	{
		requiredNonEditableProperties.add("methodToCall");
		requiredNonEditableProperties.add("roleCommand");
	}
	
	protected String delegationMemberRoleMemberId;
	protected String dmrmi;
	protected boolean canAssignRole = true;
	protected boolean canModifyAssignees = true;
	protected KimType kimType;
    protected String memberSearchValue;

    //kim type id
    protected String id;

	protected KimDocumentRoleMember member;
	{
		member = new KimDocumentRoleMember();
		member.getQualifiers().add(new KimDocumentRoleQualifier());

	}
	protected KimDocumentRolePermission permission = new KimDocumentRolePermission();
    protected KimDocumentRoleResponsibility responsibility = new KimDocumentRoleResponsibility();
    protected RoleDocumentDelegationMember delegationMember;
	{
		delegationMember = new RoleDocumentDelegationMember();
		delegationMember.getQualifiers().add(new RoleDocumentDelegationMemberQualifier());
	}
	protected String roleId;


	public IdentityManagementRoleDocumentForm() {
        super();

        setFormatterType("document.members.activeFromDate", DateDisplayTimestampObjectFormatter.class);
        setFormatterType("document.delegationMembers.activeFromDate", DateDisplayTimestampObjectFormatter.class);
        setFormatterType("document.members.activeToDate", DateDisplayTimestampObjectFormatter.class);
        setFormatterType("document.delegationMembers.activeToDate", DateDisplayTimestampObjectFormatter.class);
    }

	
    /**
	 * @return the delegationMember
	 */
	public RoleDocumentDelegationMember getDelegationMember() {
		return this.delegationMember;
	}

	/**
	 * @param delegationMember the delegationMember to set
	 */
	public void setDelegationMember(RoleDocumentDelegationMember delegationMember) {
		this.delegationMember = delegationMember;
	}

	@Override
	public String getDefaultDocumentTypeName(){
		return "IdentityManagementRoleDocument";
	}
	
	public IdentityManagementRoleDocument getRoleDocument() {
        return (IdentityManagementRoleDocument) this.getDocument();
    }

	/**
	 * @return the member
	 */
	public KimDocumentRoleMember getMember() {
		return this.member;
	}

	/**
	 * @param member the member to set
	 */
	public void setMember(KimDocumentRoleMember member) {
		this.member = member;
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        setKimType(KimApiServiceLocator.getKimTypeInfoService().getKimType(this.id));
    }

	/**
	 * @return the permission
	 */
	public KimDocumentRolePermission getPermission() {
		return this.permission;
	}

	/**
	 * @param permission the permission to set
	 */
	public void setPermission(KimDocumentRolePermission permission) {
		this.permission = permission;
	}

	/**
	 * @return the responsibility
	 */
	public KimDocumentRoleResponsibility getResponsibility() {
		return this.responsibility;
	}

	/**
	 * @param responsibility the responsibility to set
	 */
	public void setResponsibility(KimDocumentRoleResponsibility responsibility) {
		this.responsibility = responsibility;
	}

	public String getMemberFieldConversions(){
		if(member==null) {
			return "";
        }
		return getMemberFieldConversions(member.getMemberTypeCode());
	}

	public String getMemberBusinessObjectName(){
		if(member==null) {
			return "";
        }
		return getMemberBusinessObjectName(member.getMemberTypeCode());
	}

	public String getDelegationMemberFieldConversions(){
		if(getDelegationMember()==null) {
			return "";
        }
		String memberTypeCode = getDelegationMember().getMemberTypeCode();
		if(MemberType.PRINCIPAL.getCode().equals(memberTypeCode)) {
			return "principalId:delegationMember.memberId,principalName:delegationMember.memberName";
        }
		else if(MemberType.ROLE.getCode().equals(memberTypeCode)) {
			return "id:delegationMember.memberId,name:delegationMember.memberName,namespaceCode:delegationMember.memberNamespaceCode";
        }
		else if(MemberType.GROUP.getCode().equals(memberTypeCode)) {
			return "id:delegationMember.memberId,name:delegationMember.memberName,namespaceCode:delegationMember.memberNamespaceCode";
        }
		return "";
	}

	public String getDelegationMemberBusinessObjectName(){
		if(getDelegationMember()==null) {
			return "";
        }
		return getMemberBusinessObjectName(getDelegationMember().getMemberTypeCode());
	}

	protected String getMemberFieldConversions(String memberTypeCode){
		if(MemberType.PRINCIPAL.getCode().equals(memberTypeCode)) {
			return "principalId:member.memberId,principalName:member.memberName";
        }
		else if(MemberType.ROLE.getCode().equals(memberTypeCode)) {
			return "id:member.memberId,name:member.memberName,namespaceCode:member.memberNamespaceCode";
        }
		else if(MemberType.GROUP.getCode().equals(memberTypeCode)) {
			return "id:member.memberId,name:member.memberName,namespaceCode:member.memberNamespaceCode";
        }
		return "";
	}

	protected String getMemberBusinessObjectName(String memberTypeCode){
		if(MemberType.PRINCIPAL.getCode().equals(memberTypeCode)) {
			return PersonImpl.class.getName();
        }
		else if(MemberType.ROLE.getCode().equals(memberTypeCode)) {
			return RoleBo.class.getName();
        }
		else if(MemberType.GROUP.getCode().equals(memberTypeCode)) {
			return GroupBo.class.getName();
        }
		return "";
	}

	/**
	 * @return the kimType
	 */
	public KimType getKimType() {
		return this.kimType;
	}

	/**
	 * @param kimType the kimType to set
	 */
	public void setKimType(KimType kimType) {
		this.kimType = kimType;
		if ( kimType != null && getRoleDocument() != null ) {
			getRoleDocument().setKimType(kimType);
		}
	}

	/**
	 * @return the canAssignRole
	 */
	public boolean isCanAssignRole() {
		return this.canAssignRole;
	}

	/**
	 * @param canAssignRole the canAssignRole to set
	 */
	public void setCanAssignRole(boolean canAssignRole) {
		this.canAssignRole = canAssignRole;
	}

	/**
	 * @return the canModifyAssignees
	 */
	public boolean isCanModifyAssignees() {
		return this.canModifyAssignees;
	}

	/**
	 * @param canModifyAssignees the canModifyAssignees to set
	 */
	public void setCanModifyAssignees(boolean canModifyAssignees) {
		this.canModifyAssignees = canModifyAssignees;
	}
	
	public List<KimDocumentRoleMember> getMemberRows() {
		return getRoleDocument().getMembers();
	}

	public int getIndexOfRoleMemberFromMemberRows(String roleMemberId){
		int index = 0;
		for(KimDocumentRoleMember roleMember: getMemberRows()){
			if(StringUtils.equals(roleMember.getRoleMemberId(), roleMemberId)){
				break;
			}
			index++;
		}
		return index;
	}

	public int getPageNumberOfRoleMemberId(String roleMemberId){
		if(StringUtils.isEmpty(roleMemberId)) {return 1;}
		int index = getIndexOfRoleMemberFromMemberRows(roleMemberId);
		return TableRenderUtil.computeTotalNumberOfPages(index + 1, getRecordsPerPage())-1;
	}
	
	/**
	 * @return the roleId
	 */
	public String getRoleId() {
		return this.roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the delegationMemberRoleMemberId
	 */
	public String getDelegationMemberRoleMemberId() {
		return this.delegationMemberRoleMemberId;
	}

	/**
	 * @param delegationMemberRoleMemberId the delegationMemberRoleMemberId to set
	 */
	public void setDelegationMemberRoleMemberId(String delegationMemberRoleMemberId) {
		this.delegationMemberRoleMemberId = delegationMemberRoleMemberId;
		getDelegationMember().setRoleMemberId(delegationMemberRoleMemberId);
		KimDocumentRoleMember roleMember = getRoleDocument().getMember(delegationMemberRoleMemberId);
		if(roleMember!=null){
			delegationMember.setRoleMemberId(roleMember.getRoleMemberId());
			delegationMember.setRoleMemberName(roleMember.getMemberName());
			delegationMember.setRoleMemberNamespaceCode(roleMember.getMemberNamespaceCode());
			RoleDocumentDelegationMemberQualifier delegationMemberQualifier;
			for(KimDocumentRoleQualifier roleQualifier: roleMember.getQualifiers()){
				delegationMemberQualifier = getDelegationMember().getQualifier(roleQualifier.getKimAttrDefnId());
				delegationMemberQualifier.setAttrVal(roleQualifier.getAttrVal());
			}
		}
	}

	/**
	 * @return the dmrmi
	 */
	public String getDmrmi() {
		return this.dmrmi;
	}

	/**
	 * @param dmrmi the dmrmi to set
	 */
	public void setDmrmi(String dmrmi) {
		this.dmrmi = dmrmi;
	}

    /**
     * @return the memberSearchValue
     */
    public String getMemberSearchValue() {
        return this.memberSearchValue;
    }

    /**
     * @param memberSearchValue the memberSearchValue to set
     */
    public void setMemberSearchValue(String memberSearchValue) {
        this.memberSearchValue = memberSearchValue;
    }

}
