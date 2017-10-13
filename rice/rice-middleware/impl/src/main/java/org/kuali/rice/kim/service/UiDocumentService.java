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
package org.kuali.rice.kim.service;

import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupMember;
import org.kuali.rice.kim.api.identity.employment.EntityEmployment;
import org.kuali.rice.kim.api.identity.entity.Entity;
import org.kuali.rice.kim.api.identity.entity.EntityDefault;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.role.RoleMember;
import org.kuali.rice.kim.api.type.KimAttributeField;
import org.kuali.rice.kim.bo.ui.KimDocumentRoleMember;
import org.kuali.rice.kim.document.IdentityManagementGroupDocument;
import org.kuali.rice.kim.document.IdentityManagementPersonDocument;
import org.kuali.rice.kim.document.IdentityManagementRoleDocument;
import org.kuali.rice.kim.impl.common.delegate.DelegateTypeBo;
import org.kuali.rice.kim.impl.role.RoleMemberBo;
import org.kuali.rice.kim.impl.role.RoleResponsibilityActionBo;
import org.kuali.rice.krad.bo.BusinessObject;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;
import java.util.Map;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface UiDocumentService {
	/**
	 * 
	 * This method to populate Entity tables from person document pending tables when it is approved.
	 * 	  
	 * @param identityManagementPersonDocument
	 */
    @CacheEvict(value={Entity.Cache.NAME, EntityDefault.Cache.NAME, Principal.Cache.NAME, Role.Cache.NAME, GroupMember.Cache.NAME, RoleMember.Cache.NAME}, allEntries = true)
    void saveEntityPerson(IdentityManagementPersonDocument identityManagementPersonDocument);
    
    /**
     * 
     * This method is to set up the DD attribute entry map for role qualifiers, so it can be rendered.
     * 
     * @param definitions
     */
    Map<String,Object> getAttributeEntries( List<KimAttributeField> definitions );
	/**
	 * 
	 * This method is to load identity to person document pending Bos when user 'initiate' a document for 'editing' identity.
	 * 
	 * @param identityManagementPersonDocument
	 * @param principalId
	 */
	void loadEntityToPersonDoc(IdentityManagementPersonDocument identityManagementPersonDocument, String principalId);

	/**
	 * 
	 * This method loads a role document
	 * 
	 * @param identityManagementRoleDocument
	 */
	public void loadRoleDoc(IdentityManagementRoleDocument identityManagementRoleDocument, Role kimRole);

    /**
     *
     * This method loads a role document members based on search criteria
     *
     * @param identityManagementRoleDocument
     */
    public void loadRoleMembersBasedOnSearch(IdentityManagementRoleDocument identityManagementRoleDocument,
                                             String memberSearchValue);

    /**
     *
     * This method loads a document's original role members
     *
     * @param identityManagementRoleDocument
     */
    public void clearRestrictedRoleMembersSearchResults(IdentityManagementRoleDocument identityManagementRoleDocument);

    /**
	 * 
	 * This method ...
	 * 
	 * @param identityManagementRoleDocument
	 */
    @CacheEvict(value={Role.Cache.NAME, RoleMember.Cache.NAME, Permission.Cache.NAME}, allEntries = true)
	public void saveRole(IdentityManagementRoleDocument identityManagementRoleDocument);


	/**
	 * 
	 * This method loads a role document
	 * 
	 * @param identityManagementGroupDocument
	 */
	public void loadGroupDoc(IdentityManagementGroupDocument identityManagementGroupDocument, Group kimGroup);
	
	/**
	 * 
	 * This method ...
	 * 
	 * @param identityManagementGroupDocument
	 */
    @CacheEvict(value={Group.Cache.NAME,GroupMember.Cache.NAME, Role.Cache.NAME}, allEntries = true)
	public void saveGroup(IdentityManagementGroupDocument identityManagementGroupDocument);

	public BusinessObject getMember(MemberType memberType, String memberId);
	
	public String getMemberName(MemberType memberType, String memberId);
	
	public String getMemberNamespaceCode(MemberType memberType, String memberId);

	public String getMemberName(MemberType memberType, BusinessObject member);
	
	public String getMemberNamespaceCode(MemberType memberType, BusinessObject member);

	public List<RoleResponsibilityActionBo> getRoleMemberResponsibilityActionImpls(String roleMemberId);
	
	public List<DelegateTypeBo> getRoleDelegations(String roleId);
	
	public KimDocumentRoleMember getKimDocumentRoleMember(MemberType memberType, String memberId, String roleId);
	
	public String getMemberIdByName(MemberType memberType, String memberNamespaceCode, String memberName);

	public void setDelegationMembersInDocument(IdentityManagementRoleDocument identityManagementRoleDocument);

    public void setMembersInDocument(IdentityManagementRoleDocument identityManagementRoleDocument);

    public RoleMemberBo getRoleMember(String roleMemberId);
	
	public List<KimDocumentRoleMember> getRoleMembers(Map<String,String> fieldValues);
	
	public boolean canModifyEntity( String currentUserPrincipalId, String toModifyPrincipalId );
	public boolean canOverrideEntityPrivacyPreferences( String currentUserPrincipalId, String toModifyPrincipalId );

	public List<EntityEmployment> getEntityEmploymentInformationInfo(String entityId);
}
