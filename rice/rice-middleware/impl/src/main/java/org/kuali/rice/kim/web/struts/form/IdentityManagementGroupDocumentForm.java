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
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.bo.ui.GroupDocumentMember;
import org.kuali.rice.kim.document.IdentityManagementGroupDocument;
import org.kuali.rice.kim.impl.group.GroupBo;
import org.kuali.rice.kim.impl.identity.PersonImpl;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.kim.impl.type.KimTypeBo;

import java.util.List;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class IdentityManagementGroupDocumentForm extends IdentityManagementDocumentFormBase {
	protected static final long serialVersionUID = -107836689162363400L;
	
	{
		requiredNonEditableProperties.add("methodToCall");
	}
	
	protected boolean canAssignGroup = true;
	
	protected KimType kimType;
	protected GroupDocumentMember member = new GroupDocumentMember();
	protected String groupId;
    
	public IdentityManagementGroupDocumentForm() {
        super();
        setFormatterType("document.members.activeFromDate", DateDisplayTimestampObjectFormatter.class);
        setFormatterType("document.members.activeToDate", DateDisplayTimestampObjectFormatter.class);
    }

	@Override
	public String getDefaultDocumentTypeName(){
		return "IdentityManagementGroupDocument";
	}
	
	public IdentityManagementGroupDocument getGroupDocument() {
        return (IdentityManagementGroupDocument) this.getDocument();
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

	protected String getMemberFieldConversions(String memberTypeCode){
		if(MemberType.PRINCIPAL.getCode().equals(memberTypeCode)) {
			return "principalId:member.memberId,principalName:member.memberName";
        }
		else if(MemberType.ROLE.getCode().equals(memberTypeCode)) {
			return "id:member.memberId,name:member.memberName";
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
		if(StringUtils.isNotBlank(getGroupDocument().getGroupTypeId())) {
			return KimApiServiceLocator.getKimTypeInfoService().getKimType(getGroupDocument().getGroupTypeId());
        }
		else {return kimType;}
	}

	public String getKimTypeClass(){
		return KimTypeBo.class.getName();
	}
	/**
	 * @return the canAssignGroup
	 */
	public boolean isCanAssignGroup() {
		return this.canAssignGroup;
	}

	/**
	 * @param canAssignGroup the canAssignGroup to set
	 */
	public void setCanAssignGroup(boolean canAssignGroup) {
		this.canAssignGroup = canAssignGroup;
	}

	/**
	 * @return the member
	 */
	public GroupDocumentMember getMember() {
		return this.member;
	}

	/**
	 * @param member the member to set
	 */
	public void setMember(GroupDocumentMember member) {
		this.member = member;
	}
	
	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.kim.web.struts.form.IdentityManagementDocumentFormBase#getMemberRows()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List getMemberRows() {
		return getGroupDocument().getMembers();
	}

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return this.groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @param kimType the kimType to set
	 */
	public void setKimType(KimType kimType) {
		this.kimType = kimType;
	}

}
