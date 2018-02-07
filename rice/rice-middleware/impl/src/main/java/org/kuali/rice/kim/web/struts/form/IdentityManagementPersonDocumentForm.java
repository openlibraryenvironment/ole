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
import org.kuali.rice.core.web.format.DateDisplayTimestampObjectFormatter;
import org.kuali.rice.kim.bo.ui.PersonDocumentAddress;
import org.kuali.rice.kim.bo.ui.PersonDocumentAffiliation;
import org.kuali.rice.kim.bo.ui.PersonDocumentCitizenship;
import org.kuali.rice.kim.bo.ui.PersonDocumentEmail;
import org.kuali.rice.kim.bo.ui.PersonDocumentEmploymentInfo;
import org.kuali.rice.kim.bo.ui.PersonDocumentGroup;
import org.kuali.rice.kim.bo.ui.PersonDocumentName;
import org.kuali.rice.kim.bo.ui.PersonDocumentPhone;
import org.kuali.rice.kim.bo.ui.PersonDocumentRole;
import org.kuali.rice.kim.bo.ui.RoleDocumentDelegationMember;
import org.kuali.rice.kim.document.IdentityManagementPersonDocument;
import org.kuali.rice.krad.util.GlobalVariables;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class IdentityManagementPersonDocumentForm extends IdentityManagementDocumentFormBase {

	{
		requiredNonEditableProperties.add("methodToCall");
	}

    protected static final long serialVersionUID = -4787045391777666988L;
    protected String principalId;
    protected PersonDocumentAffiliation newAffln;
    protected PersonDocumentEmploymentInfo newEmpInfo;
    protected PersonDocumentCitizenship newCitizenship;
    protected PersonDocumentName newName;
    protected PersonDocumentAddress newAddress;
    protected PersonDocumentPhone newPhone;
    protected PersonDocumentEmail newEmail;
    protected PersonDocumentGroup newGroup;
    protected PersonDocumentRole newRole;
    protected RoleDocumentDelegationMember newDelegationMember = new RoleDocumentDelegationMember();
    protected String newDelegationMemberRoleId = null;
    protected boolean delegationMemberLookup = false;
    protected boolean canModifyEntity = false;
    protected boolean canOverrideEntityPrivacyPreferences = false;
    protected boolean userSameAsPersonEdited = false;
    
	/**
	 * @return the canModifyEntity
	 */
	public boolean isCanModifyEntity() {
		return this.canModifyEntity;
	}

	/**
	 * @param canModifyEntity the canModifyEntity to set
	 */
	public void setCanModifyEntity(boolean canModifyEntity) {
		this.canModifyEntity = canModifyEntity;
	}

	public IdentityManagementPersonDocumentForm() {
        super();
        //this.registerEditableProperty("methodToCall.approve.x");
        this.setNewAffln(new PersonDocumentAffiliation());
        this.setNewAddress(new PersonDocumentAddress());
        this.setNewEmpInfo(new PersonDocumentEmploymentInfo());
        this.setNewName(new PersonDocumentName());
        this.setNewPhone(new PersonDocumentPhone());
        this.setNewEmail(new PersonDocumentEmail());
        this.setNewGroup(new PersonDocumentGroup());
        this.setNewRole(new PersonDocumentRole());
        this.setNewDelegationMember(new RoleDocumentDelegationMember());
        setFormatterType("document.groups.activeFromDate", DateDisplayTimestampObjectFormatter.class);
        setFormatterType("document.groups.activeToDate", DateDisplayTimestampObjectFormatter.class);
    }

	@Override
	public String getDefaultDocumentTypeName(){
		return "IdentityManagementPersonDocument";
	}
	
	public IdentityManagementPersonDocument getPersonDocument() {
        return (IdentityManagementPersonDocument) this.getDocument();
    }

	public PersonDocumentAffiliation getNewAffln() {
		return this.newAffln;
	}

	public void setNewAffln(PersonDocumentAffiliation newAffln) {
		this.newAffln = newAffln;
	}

	public PersonDocumentEmploymentInfo getNewEmpInfo() {
		return this.newEmpInfo;
	}

	public void setNewEmpInfo(PersonDocumentEmploymentInfo newEmpInfo) {
		this.newEmpInfo = newEmpInfo;
	}

	public PersonDocumentCitizenship getNewCitizenship() {
		return this.newCitizenship;
	}

	public void setNewCitizenship(PersonDocumentCitizenship newCitizenship) {
		this.newCitizenship = newCitizenship;
	}

	public PersonDocumentName getNewName() {
		return this.newName;
	}

	public void setNewName(PersonDocumentName newName) {
		this.newName = newName;
	}

	public PersonDocumentAddress getNewAddress() {
		return this.newAddress;
	}

	public void setNewAddress(PersonDocumentAddress newAddress) {
		this.newAddress = newAddress;
	}

	public PersonDocumentPhone getNewPhone() {
		return this.newPhone;
	}

	public void setNewPhone(PersonDocumentPhone newPhone) {
		this.newPhone = newPhone;
	}

	public PersonDocumentEmail getNewEmail() {
		return this.newEmail;
	}

	public void setNewEmail(PersonDocumentEmail newEmail) {
		this.newEmail = newEmail;
	}

	public PersonDocumentGroup getNewGroup() {
		return this.newGroup;
	}

	public void setNewGroup(PersonDocumentGroup newGroup) {
		this.newGroup = newGroup;
	}

	public PersonDocumentRole getNewRole() {
		return this.newRole;
	}

	public void setNewRole(PersonDocumentRole newRole) {
		this.newRole = newRole;
	}

	public String getPrincipalId() {
		return this.principalId;
	}

	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}

	/**
	 * @return the newDelegationMember
	 */
	public RoleDocumentDelegationMember getNewDelegationMember() {
		return this.newDelegationMember;
	}

	/**
	 * @param newDelegationMember the newDelegationMember to set
	 */
	public void setNewDelegationMember(
			RoleDocumentDelegationMember newDelegationMember) {
		this.newDelegationMember = newDelegationMember;
	}

	/**
	 * @return the delegationMemberLookup
	 */
	public boolean isDelegationMemberLookup() {
		return this.delegationMemberLookup;
	}

	/**
	 * @param delegationMemberLookup the delegationMemberLookup to set
	 */
	public void setDelegationMemberLookup(boolean delegationMemberLookup) {
		this.delegationMemberLookup = delegationMemberLookup;
	}

	/**
	 * @return the newDelegationMemberRoleId
	 */
	public String getNewDelegationMemberRoleId() {
		return this.newDelegationMemberRoleId;
	}

	/**
	 * @param newDelegationMemberRoleId the newDelegationMemberRoleId to set
	 */
	public void setNewDelegationMemberRoleId(String newDelegationMemberRoleId) {
		this.newDelegationMemberRoleId = newDelegationMemberRoleId;
		if(StringUtils.isNotEmpty(newDelegationMemberRoleId)){
			newDelegationMember.getRoleBo().setId(newDelegationMemberRoleId);
		}
	}

	/**
	 * @return the canOverrideEntityPrivacyPreferences
	 */
	public boolean isCanOverrideEntityPrivacyPreferences() {
		return this.canOverrideEntityPrivacyPreferences || isUserSameAsPersonEdited();
	}

	/**
	 * @param canOverrideEntityPrivacyPreferences the canOverrideEntityPrivacyPreferences to set
	 */
	public void setCanOverrideEntityPrivacyPreferences(
			boolean canOverrideEntityPrivacyPreferences) {
		this.canOverrideEntityPrivacyPreferences = canOverrideEntityPrivacyPreferences;
	}

	/**
	 * @return the userSameAsPersonEdited
	 */
	public boolean isUserSameAsPersonEdited() {
		if(StringUtils.isNotEmpty(getPrincipalId())){
			userSameAsPersonEdited = StringUtils.equals(GlobalVariables.getUserSession().getPrincipalId(), getPrincipalId());
		}
		return this.userSameAsPersonEdited;
	}

	/**
	 * @param userSameAsPersonEdited the userSameAsPersonEdited to set
	 */
	public void setUserSameAsPersonEdited(boolean userSameAsPersonEdited) {
		this.userSameAsPersonEdited = userSameAsPersonEdited;
	}

}
