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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.criteria.CriteriaLookupService;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.coreservice.api.namespace.Namespace;
import org.kuali.rice.coreservice.api.namespace.NamespaceService;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.role.RoleMember;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.api.type.KimTypeAttribute;
import org.kuali.rice.kim.api.type.KimTypeInfoService;
import org.kuali.rice.kim.framework.role.RoleEbo;
import org.kuali.rice.kim.framework.role.RoleTypeService;
import org.kuali.rice.kim.framework.type.KimTypeService;
import org.kuali.rice.kim.impl.common.attribute.KimAttributeBo;
import org.kuali.rice.kim.impl.common.delegate.DelegateMemberBo;
import org.kuali.rice.kim.impl.common.delegate.DelegateTypeBo;
import org.kuali.rice.kim.impl.responsibility.ResponsibilityInternalService;
import org.kuali.rice.kim.impl.role.RoleBoLite;
import org.kuali.rice.kim.impl.services.KimImplServiceLocator;
import org.kuali.rice.kim.impl.type.KimTypeAttributeBo;
import org.kuali.rice.kim.impl.type.KimTypeBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.LookupService;
import org.kuali.rice.krad.util.KRADPropertyConstants;
import org.kuali.rice.krad.util.ObjectUtils;

abstract class RoleServiceBase {
    private static final Logger LOG = Logger.getLogger( RoleServiceBase.class );

    private BusinessObjectService businessObjectService;
    private LookupService lookupService;
    private IdentityService identityService;
    private NamespaceService namespaceService;
    private KimTypeInfoService kimTypeInfoService;
    private GroupService groupService;
    private ResponsibilityInternalService responsibilityInternalService;
    private RoleDao roleDao;
    protected CriteriaLookupService criteriaLookupService;

    /**
     * A helper enumeration for indicating which KimRoleDao method to use when attempting to get role/delegation-related lists that are not in the cache.
     *
     * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    protected static enum RoleDaoAction {
        ROLE_PRINCIPALS_FOR_PRINCIPAL_ID_AND_ROLE_IDS,
        ROLE_GROUPS_FOR_GROUP_IDS_AND_ROLE_IDS,
        ROLE_MEMBERS_FOR_ROLE_IDS,
        ROLE_MEMBERSHIPS_FOR_ROLE_IDS_AS_MEMBERS,
        ROLE_MEMBERS_FOR_ROLE_IDS_WITH_FILTERS,
        DELEGATION_PRINCIPALS_FOR_PRINCIPAL_ID_AND_DELEGATION_IDS,
        DELEGATION_GROUPS_FOR_GROUP_IDS_AND_DELEGATION_IDS,
        DELEGATION_MEMBERS_FOR_DELEGATION_IDS
    }

    /**
     * Explicitly sets the BusinessObjectService to use. For testability.
     * @param bos the BusinessObjectService to use
     */
    void setBusinessObjectService(BusinessObjectService bos) {
        businessObjectService = bos;
    }

    /**
     * Converts the Qualifier Name/Value Role qualification set into Qualifier AttributeID/Value set
     *
     * @param qualification The original role qualification attribute set
     * @param validAttributeIds The mapping of attribute names to their matching attribute ids
     * @return Converted Map<String, String> containing ID/value pairs
     */
    private Map<String, String> convertQualifierKeys(Map<String, String> qualification, Map<String, String> validAttributeIds) {
        Map<String, String> convertedQualification = new HashMap<String, String>();
        if (qualification != null && CollectionUtils.isNotEmpty(qualification.entrySet())) {
            for (Map.Entry<String, String> entry : qualification.entrySet()) {
                String attributeId = validAttributeIds.get(entry.getKey());
                if (StringUtils.isNotEmpty(attributeId)) {
                    convertedQualification.put(attributeId, entry.getValue());
                }
            }
        }
        return convertedQualification;
    }

    protected void getNestedRoleTypeMemberIds(String roleId, Set<String> members) {
        ArrayList<String> roleList = new ArrayList<String>(1);
        roleList.add(roleId);
        List<RoleMemberBo> firstLevelMembers = getStoredRoleMembersForRoleIds(roleList, MemberType.ROLE.getCode(), Collections.<String, String>emptyMap());
        for (RoleMemberBo member : firstLevelMembers) {
            if (MemberType.ROLE.equals(member.getType())) {
                if (!members.contains(member.getMemberId())) {
                    members.add(member.getMemberId());
                    getNestedRoleTypeMemberIds(member.getMemberId(), members);
                }
            }
        }
    }

    protected List<RoleMemberBo> getRoleMembersForPrincipalId(String roleId, String principalId) {
        return roleDao.getRolePrincipalsForPrincipalIdAndRoleIds(Collections.singletonList(roleId), principalId, null);
    }

    protected List<RoleMemberBo> getRoleMembersForGroupIds(String roleId, List<String> groupIds) {
        if (CollectionUtils.isEmpty(groupIds)) {
            return new ArrayList<RoleMemberBo>();
        }
        return roleDao.getRoleMembersForGroupIds(roleId, groupIds);
    }

    /**
     * Retrieves a list of RoleMemberBo instances from the KimRoleDao.
     *
     * @param daoActionToTake An indicator for which KimRoleDao method should be used to get the results if the desired RoleMemberBos are not cached.
     * @param roleIds         The role IDs to filter by; may get used as the IDs for members that are also roles, depending on the daoActionToTake value.
     * @param principalId     The principal ID to filter by; may get ignored depending on the daoActionToTake value.
     * @param groupIds        The group IDs to filter by; may get ignored depending on the daoActionToTake value.
     * @param memberTypeCode  The member type code to filter by; may get overridden depending on the daoActionToTake value.
     * @param qualification   The original role qualification attribute set
     * @return A list of RoleMemberBo instances based on the provided parameters.
     * @throws IllegalArgumentException if daoActionToTake refers to an enumeration constant that is not role-member-related.
     */
    protected List<RoleMemberBo> getRoleMemberBoList(RoleDaoAction daoActionToTake, Collection<String> roleIds, String principalId,
                                                     Collection<String> groupIds, String memberTypeCode, Map<String, String> qualification) {
        if (roleIds == null || roleIds.isEmpty()) {
            roleIds = Collections.emptyList();
        }
        if (groupIds == null || groupIds.isEmpty()) {
            groupIds = Collections.emptyList();
        }

        Map<String, String> validAttributeIds = new HashMap<String, String>();
        HashSet <String> kimTypeIds = new HashSet<String>();
        
        //Getting unique kim types
        for (String roleId : roleIds) {
            RoleBoLite role = getRoleBoLite(roleId);
            kimTypeIds.add(role.getKimTypeId());
        }

        if (qualification != null && CollectionUtils.isNotEmpty(qualification.entrySet())) {
            for (String kimTypeId : kimTypeIds) {
                for (Map.Entry<String, String> entry : qualification.entrySet()) {
                    validAttributeIds.put(entry.getKey(), getKimAttributeId(kimTypeId, entry.getKey()));
                }
            }
        }

        Map<String, String> convertedQualification = convertQualifierKeys(qualification, validAttributeIds);

        switch (daoActionToTake) {
            case ROLE_PRINCIPALS_FOR_PRINCIPAL_ID_AND_ROLE_IDS: // Search for principal role members only.
                return roleDao.getRolePrincipalsForPrincipalIdAndRoleIds(roleIds, principalId, convertedQualification);
            case ROLE_GROUPS_FOR_GROUP_IDS_AND_ROLE_IDS: // Search for group role members only.
               return roleDao.getRoleGroupsForGroupIdsAndRoleIds(roleIds, groupIds, convertedQualification);
            case ROLE_MEMBERS_FOR_ROLE_IDS: // Search for role members with the given member type code.
               return roleDao.getRoleMembersForRoleIds(roleIds, memberTypeCode, convertedQualification);
            case ROLE_MEMBERSHIPS_FOR_ROLE_IDS_AS_MEMBERS: // Search for role members who are also roles.
                return roleDao.getRoleMembershipsForRoleIdsAsMembers(roleIds, convertedQualification);
            case ROLE_MEMBERS_FOR_ROLE_IDS_WITH_FILTERS: // Search for role members that might be roles, principals, or groups.
                return roleDao.getRoleMembersForRoleIdsWithFilters(roleIds, principalId, groupIds, convertedQualification);
            default: // This should never happen, since the previous switch block should handle this case appropriately.
                throw new IllegalArgumentException("The 'daoActionToTake' parameter cannot refer to a non-role-member-related value!");
        }
    }

    /**
     * Calls the KimRoleDao's "getRolePrincipalsForPrincipalIdAndRoleIds" method and/or retrieves any corresponding members from the cache.
     */
    protected List<RoleMemberBo> getStoredRolePrincipalsForPrincipalIdAndRoleIds(Collection<String> roleIds, String principalId, Map<String, String> qualification) {
        return getRoleMemberBoList(RoleDaoAction.ROLE_PRINCIPALS_FOR_PRINCIPAL_ID_AND_ROLE_IDS, roleIds, principalId, Collections.<String>emptyList(), null, qualification);
    }

    /**
     * Calls the KimRoleDao's "getRoleGroupsForGroupIdsAndRoleIds" method and/or retrieves any corresponding members from the cache.
     */
    protected List<RoleMemberBo> getStoredRoleGroupsForGroupIdsAndRoleIds(Collection<String> roleIds, Collection<String> groupIds, Map<String, String> qualification) {
        return getRoleMemberBoList(RoleDaoAction.ROLE_GROUPS_FOR_GROUP_IDS_AND_ROLE_IDS, roleIds, null, groupIds, null, qualification);
    }

    /**
     * Calls the KimRoleDao's "getRoleMembersForRoleIds" method and/or retrieves any corresponding members from the cache.
     */
    protected List<RoleMemberBo> getStoredRoleMembersForRoleIds(Collection<String> roleIds, String memberTypeCode, Map<String, String> qualification) {
        return getRoleMemberBoList(RoleDaoAction.ROLE_MEMBERS_FOR_ROLE_IDS, roleIds, null, Collections.<String>emptyList(), memberTypeCode, qualification);
    }

    /**
     * Calls the KimRoleDao's "getRoleMembershipsForRoleIdsAsMembers" method and/or retrieves any corresponding members from the cache.
     */
    protected List<RoleMemberBo> getStoredRoleMembershipsForRoleIdsAsMembers(Collection<String> roleIds, Map<String, String> qualification) {
        return getRoleMemberBoList(RoleDaoAction.ROLE_MEMBERSHIPS_FOR_ROLE_IDS_AS_MEMBERS, roleIds, null, Collections.<String>emptyList(), null, qualification);
    }

    /**
     * Calls the KimRoleDao's "getRoleMembersForRoleIdsWithFilters" method and/or retrieves any corresponding members from the cache.
     */
    protected List<RoleMemberBo> getStoredRoleMembersForRoleIdsWithFilters(Collection<String> roleIds, String principalId, List<String> groupIds, Map<String, String> qualification) {
        return getRoleMemberBoList(RoleDaoAction.ROLE_MEMBERS_FOR_ROLE_IDS_WITH_FILTERS, roleIds, principalId, groupIds, null, qualification);
    }

    /**
     * Retrieves a RoleMemberBo object by its ID. If the role member already exists in the cache, this method will return the cached
     * version; otherwise, it will retrieve the uncached version from the database and then cache it (if it belongs to a role that allows
     * its members to be cached) before returning it.
     */
    protected RoleMemberBo getRoleMemberBo(String roleMemberId) {
        if (StringUtils.isBlank(roleMemberId)) {
            return null;
        }

        return getBusinessObjectService().findByPrimaryKey(RoleMemberBo.class, Collections.singletonMap(
               KimConstants.PrimaryKeyConstants.ID, roleMemberId));
    }

    /**
     * Retrieves a RoleResponsibilityActionBo object by its ID.
     */
    protected RoleResponsibilityActionBo getRoleResponsibilityActionBo(String roleResponsibilityActionId) {
        if (StringUtils.isBlank(roleResponsibilityActionId)) {
            return null;
        }

        return getBusinessObjectService().findByPrimaryKey(RoleResponsibilityActionBo.class, Collections.singletonMap(
                KimConstants.PrimaryKeyConstants.ID, roleResponsibilityActionId));
    }

    /**
     * Calls the KimRoleDao's "getDelegationImplMapFromRoleIds" method and/or retrieves any corresponding delegations from the cache.
     */
    protected Map<String, DelegateTypeBo> getStoredDelegationImplMapFromRoleIds(Collection<String> roleIds) {
        if (roleIds != null && !roleIds.isEmpty()) {
            return roleDao.getDelegationImplMapFromRoleIds(roleIds);
        }

        return Collections.emptyMap();
    }

    /**
     * Calls the KimRoleDao's "getDelegationBosForRoleIds" method and/or retrieves any corresponding delegations from the cache.
     */
    protected List<DelegateTypeBo> getStoredDelegationImplsForRoleIds(Collection<String> roleIds) {
        if (roleIds != null && !roleIds.isEmpty()) {
            return roleDao.getDelegationBosForRoleIds(roleIds);
        }
        return Collections.emptyList();
    }

    /**
     * Retrieves a List of delegation members from the KimRoleDao as appropriate.
     *
     * @param daoActionToTake An indicator for which KimRoleDao method to use for retrieving results.
     * @param delegationIds   The IDs of the delegations that the members belong to.
     * @param principalId     The principal ID of the principal delegation members; may get ignored depending on the RoleDaoAction value.
     * @param groupIds        The group IDs of the group delegation members; may get ignored depending on the RoleDaoAction value.
     * @return A List of DelegateMemberBo objects based on the provided parameters.
     * @throws IllegalArgumentException if daoActionToTake does not represent a delegation-member-list-related enumeration value.
     */
    protected List<DelegateMemberBo> getDelegationMemberBoList(RoleDaoAction daoActionToTake, Collection<String> delegationIds,
                                                               String principalId, List<String> groupIds) {
        if (delegationIds == null || delegationIds.isEmpty()) {
            delegationIds = Collections.emptyList();
        }
        if (groupIds == null || groupIds.isEmpty()) {
            groupIds = Collections.emptyList();
        }

        switch (daoActionToTake) {
            case DELEGATION_PRINCIPALS_FOR_PRINCIPAL_ID_AND_DELEGATION_IDS: // Search for principal delegation members.
                return roleDao.getDelegationPrincipalsForPrincipalIdAndDelegationIds(delegationIds, principalId);
            case DELEGATION_GROUPS_FOR_GROUP_IDS_AND_DELEGATION_IDS: // Search for group delegation members.
                return roleDao.getDelegationGroupsForGroupIdsAndDelegationIds(delegationIds, groupIds);
            default: // This should never happen since the previous switch block should handle this case appropriately.
                throw new IllegalArgumentException("The 'daoActionToTake' parameter cannot refer to a non-delegation-member-list-related value!");
        }
    }

    /**
     * Calls the KimRoleDao's "getDelegationPrincipalsForPrincipalIdAndDelegationIds" method and/or retrieves any corresponding members from the cache.
     */
    protected List<DelegateMemberBo> getStoredDelegationPrincipalsForPrincipalIdAndDelegationIds(Collection<String> delegationIds, String principalId) {
        return getDelegationMemberBoList(RoleDaoAction.DELEGATION_PRINCIPALS_FOR_PRINCIPAL_ID_AND_DELEGATION_IDS,
                delegationIds, principalId, null);
    }

    /**
     * Retrieves a DelegateMemberBo object by its ID. If the delegation member already exists in the cache, this method will return the cached
     * version; otherwise, it will retrieve the uncached version from the database and then cache it before returning it.
     */
    protected DelegateMemberBo getDelegateMemberBo(String delegationMemberId) {
        if (StringUtils.isBlank(delegationMemberId)) {
            return null;
        }

        return getBusinessObjectService().findByPrimaryKey(DelegateMemberBo.class,
                Collections.singletonMap(KimConstants.PrimaryKeyConstants.DELEGATION_MEMBER_ID, delegationMemberId));
    }

    /**
     * Retrieves a DelegateMemberBo List by (principal/group/role) member ID and delegation ID. If the List already exists in the cache,
     * this method will return the cached one; otherwise, it will retrieve the uncached version from the database and then cache it before returning it.
     */
    protected List<DelegateMemberBo> getDelegationMemberBoListByMemberAndDelegationId(String memberId, String delegationId) {

        Map<String, String> searchCriteria = new HashMap<String, String>();
        searchCriteria.put(KimConstants.PrimaryKeyConstants.MEMBER_ID, memberId);
        searchCriteria.put(KimConstants.PrimaryKeyConstants.DELEGATION_ID, delegationId);
        return new ArrayList<DelegateMemberBo>(getBusinessObjectService().findMatching(DelegateMemberBo.class, searchCriteria));
    }

    protected Object getMember(String memberTypeCode, String memberId) {
        if (StringUtils.isBlank(memberId)) {
            return null;
        }
        if (MemberType.PRINCIPAL.getCode().equals(memberTypeCode)) {
            return getIdentityService().getPrincipal(memberId);
        } else if (MemberType.GROUP.getCode().equals(memberTypeCode)) {
            return getGroupService().getGroup(memberId);
        } else if (MemberType.ROLE.getCode().equals(memberTypeCode)) {
            return getRoleBo(memberId);
        }
        return null;
    }

    protected String getMemberName(Object member) {
        if (member == null) {
            return "";
        }
        if (member instanceof Principal) {
            return ((Principal) member).getPrincipalName();
        }
        if (member instanceof Group) {
            return ((Group) member).getName();
        }
        if (member instanceof Role) {
            return ((Role) member).getName();
        }
        return member.toString();
    }

    protected RoleBo getRoleBo(String roleId) {
        if (StringUtils.isBlank(roleId)) {
            return null;
        }
        return getBusinessObjectService().findBySinglePrimaryKey(RoleBo.class, roleId);
    }
    
    protected RoleBoLite getRoleBoLite(String roleId) {
        if (StringUtils.isBlank(roleId)) {
            return null;
        }
        return getBusinessObjectService().findBySinglePrimaryKey(RoleBoLite.class, roleId);
    }

    protected DelegateTypeBo getDelegationOfType(String roleId, DelegationType delegationType) {
        List<DelegateTypeBo> roleDelegates = getRoleDelegations(roleId);
        if (isDelegationPrimary(delegationType)) {
            return getPrimaryDelegation(roleId, roleDelegates);
        } else {
            return getSecondaryDelegation(roleId, roleDelegates);
        }
    }

    private DelegateTypeBo getSecondaryDelegation(String roleId, List<DelegateTypeBo> roleDelegates) {
        DelegateTypeBo secondaryDelegate = null;
        RoleBoLite roleBo = getRoleBoLite(roleId);
        for (DelegateTypeBo delegate : roleDelegates) {
            if (isDelegationSecondary(delegate.getDelegationType())) {
                secondaryDelegate = delegate;
            }
        }
        if (secondaryDelegate == null) {
            secondaryDelegate = new DelegateTypeBo();
            secondaryDelegate.setRoleId(roleId);
            secondaryDelegate.setDelegationType(DelegationType.SECONDARY);
            secondaryDelegate.setKimTypeId(roleBo.getKimTypeId());
        }
        return secondaryDelegate;
    }

    protected DelegateTypeBo getPrimaryDelegation(String roleId, List<DelegateTypeBo> roleDelegates) {
        DelegateTypeBo primaryDelegate = null;
        RoleBoLite roleBo = getRoleBoLite(roleId);
        for (DelegateTypeBo delegate : roleDelegates) {
            if (isDelegationPrimary(delegate.getDelegationType())) {
                primaryDelegate = delegate;
            }
        }
        if (primaryDelegate == null) {
            primaryDelegate = new DelegateTypeBo();
            primaryDelegate.setRoleId(roleId);
            primaryDelegate.setDelegationType(DelegationType.PRIMARY);
            primaryDelegate.setKimTypeId(roleBo.getKimTypeId());
        }
        return primaryDelegate;
    }

    protected RoleMemberBo matchingMemberRecord(List<RoleMemberBo> roleMembers, String memberId, String memberTypeCode, Map<String, String> qualifier) {
        for (RoleMemberBo rm : roleMembers) {
            if (doesMemberMatch(rm, memberId, memberTypeCode, qualifier)) {
                return rm;
            }
        }
        return null;
    }

    protected boolean isDelegationPrimary(DelegationType delegationType) {
        return DelegationType.PRIMARY.equals(delegationType);
    }

    protected boolean isDelegationSecondary(DelegationType delegationType) {
        return DelegationType.SECONDARY.equals(delegationType);
    }


    private List<DelegateTypeBo> getRoleDelegations(String roleId) {
        if (roleId == null) {
            return new ArrayList<DelegateTypeBo>();
        }
        return getStoredDelegationImplsForRoleIds(Collections.singletonList(roleId));

    }

    protected RoleBo getRoleBoByName(String namespaceCode, String roleName) {
        if (StringUtils.isBlank(namespaceCode)
                || StringUtils.isBlank(roleName)) {
            return null;
        }
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put(KimConstants.UniqueKeyConstants.NAMESPACE_CODE, namespaceCode);
        criteria.put(KimConstants.UniqueKeyConstants.NAME, roleName);
        criteria.put(KRADPropertyConstants.ACTIVE, "Y");
        // while this is not actually the primary key - there will be at most one row with these criteria
        return getBusinessObjectService().findByPrimaryKey(RoleBo.class, criteria);
    }
    
    protected RoleBoLite getRoleBoLiteByName(String namespaceCode, String roleName) {
        if (StringUtils.isBlank(namespaceCode)
                || StringUtils.isBlank(roleName)) {
            return null;
        }
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put(KimConstants.UniqueKeyConstants.NAMESPACE_CODE, namespaceCode);
        criteria.put(KimConstants.UniqueKeyConstants.NAME, roleName);
        criteria.put(KRADPropertyConstants.ACTIVE, "Y");
        // while this is not actually the primary key - there will be at most one row with these criteria
        return getBusinessObjectService().findByPrimaryKey(RoleBoLite.class, criteria);
    }

	protected List<RoleMember> doAnyMemberRecordsMatchByExactQualifier( RoleEbo role, String memberId, RoleDaoAction daoActionToTake, Map<String, String> qualifier ) {
		List<RoleMemberBo> roleMemberBos = getRoleMembersByExactQualifierMatch(role, memberId, daoActionToTake, qualifier);
        List<RoleMember> roleMembers = new ArrayList<RoleMember>();
        if(CollectionUtils.isNotEmpty(roleMemberBos)) {
            for (RoleMemberBo bo : roleMemberBos) {
                roleMembers.add(RoleMemberBo.to(bo));
            }
			return roleMembers;
		}

		return Collections.emptyList();
	}
	
	protected List<RoleMemberBo> getRoleMembersByExactQualifierMatch(RoleEbo role, String memberId, RoleDaoAction daoActionToTake, Map<String, String> qualifier) {
		List<RoleMemberBo> rms = new ArrayList<RoleMemberBo>();
		RoleTypeService roleTypeService = getRoleTypeService( role.getId() );
		if(roleTypeService != null) {
    		List<String> attributesForExactMatch = roleTypeService.getQualifiersForExactMatch();
    		if(CollectionUtils.isNotEmpty(attributesForExactMatch)) {
    			switch (daoActionToTake) {
	    			case ROLE_GROUPS_FOR_GROUP_IDS_AND_ROLE_IDS : // Search for group role members only.
	        			rms = getStoredRoleGroupsForGroupIdsAndRoleIds(Collections.singletonList(role.getId()), Collections.singletonList(memberId), populateQualifiersForExactMatch(qualifier, attributesForExactMatch));
	    				break;
	    			case ROLE_PRINCIPALS_FOR_PRINCIPAL_ID_AND_ROLE_IDS : // Search for principal role members only.
	        			rms = getStoredRolePrincipalsForPrincipalIdAndRoleIds(Collections.singletonList(role.getId()), memberId, populateQualifiersForExactMatch(qualifier, attributesForExactMatch));
	    				break;
	    			case ROLE_MEMBERSHIPS_FOR_ROLE_IDS_AS_MEMBERS : // Search for roles as role members only.
	    				List<RoleMemberBo> allRoleMembers = getStoredRoleMembershipsForRoleIdsAsMembers(Collections.singletonList(role.getId()), populateQualifiersForExactMatch(qualifier, attributesForExactMatch));
	        			for(RoleMemberBo rm : allRoleMembers) {
	        				if ( rm.getMemberId().equals(memberId) ) { 
	        					rms.add(rm);
	        				}
	        			}
                        break;
	    			default : // The daoActionToTake parameter is invalid; throw an exception.
	    				throw new IllegalArgumentException("The 'daoActionToTake' parameter cannot refer to a non-role-member-related value!");
    			}
    			
    		} 
		}
		return rms;
	}
    
    //return roleMemberId of match or null if no match
    protected RoleMember doAnyMemberRecordsMatch(List<RoleMemberBo> roleMembers, String memberId, String memberTypeCode, Map<String, String> qualifier) {
        for (RoleMemberBo rm : roleMembers) {
            if (rm.isActive() && doesMemberMatch(rm, memberId, memberTypeCode, qualifier)) {
                return RoleMemberBo.to(rm);
            }
        }
        return null;
    }

    protected boolean doesMemberMatch(RoleMemberBo roleMember, String memberId, String memberTypeCode, Map<String, String> qualifier) {
        if (roleMember.getMemberId().equals(memberId) && roleMember.getType().getCode().equals(memberTypeCode)) {
            // member ID/type match
            Map<String, String> roleQualifier = roleMember.getAttributes();
            if ((qualifier == null || qualifier.isEmpty())
                    && (roleQualifier == null || roleQualifier.isEmpty())) {
                return true; // blank qualifier match
            } else {
                if (qualifier != null && roleQualifier != null && qualifier.equals(roleQualifier)) {
                    return true; // qualifier match
                }
            }
        }
        return false;
    }
    
    /**
     * Retrieves the role type service associated with the given role ID
     *
     * @param roleId the role ID to get the role type service for
     * @return the Role Type Service
     */
    protected RoleTypeService getRoleTypeService(String roleId) {
        RoleBoLite roleBo = getRoleBoLite(roleId);
        if(roleBo != null){
            KimType roleType = KimTypeBo.to(roleBo.getKimRoleType());
            if (roleType != null) {
                return getRoleTypeService(roleType);
            }
        }
        return KimImplServiceLocator.getDefaultRoleTypeService();
    }

    /**
     * Retrieves the role type service for the given service name.
     *
     * @param serviceName the name of the service to retrieve
     * @return the Role Type Service
     */
    protected RoleTypeService getRoleTypeServiceByName(String serviceName) {
        try {
            KimTypeService service = (KimTypeService) GlobalResourceLoader.getService(QName.valueOf(serviceName));
            if (service != null && service instanceof RoleTypeService) {
                return (RoleTypeService) service;
            }
            LOG.warn("Unable to find role type service by name: " + serviceName + ". Defaulting to: kimNoMembersRoleTypeService ");
            return (RoleTypeService) KimImplServiceLocator.getService("kimNoMembersRoleTypeService");
        } catch (Exception ex) {
            LOG.warn("Unable to find role type service by name: " + serviceName, ex);
            return (RoleTypeService) KimImplServiceLocator.getService("kimNoMembersRoleTypeService");
        }
    }

    protected RoleTypeService getRoleTypeService(KimType typeInfo) {
        String serviceName = typeInfo.getServiceName();
        if (serviceName != null) {
            try {
                KimTypeService service = (KimTypeService) GlobalResourceLoader.getService(QName.valueOf(serviceName));
                if (service != null && service instanceof RoleTypeService) {
                    return (RoleTypeService) service;
                }
                LOG.warn("Unable to find role type service with name: " + serviceName + ". Defaulting to: kimNoMembersRoleTypeService ");
                return (RoleTypeService) KimImplServiceLocator.getService("kimNoMembersRoleTypeService");
            } catch (Exception ex) {
                LOG.error("Unable to find role type service with name: " + serviceName, ex);
                return (RoleTypeService) KimImplServiceLocator.getService("kimNoMembersRoleTypeService");
            }
        }
        return KimImplServiceLocator.getDefaultRoleTypeService();
    }
    
    protected Map<String, String> populateQualifiersForExactMatch(Map<String, String> defaultQualification, List<String> attributes) {
        Map<String,String> qualifiersForExactMatch = new HashMap<String,String>();
        if (defaultQualification != null && CollectionUtils.isNotEmpty(defaultQualification.keySet())) {
            for (String attributeName : attributes) {
                if (StringUtils.isNotEmpty(defaultQualification.get(attributeName))) {
                    qualifiersForExactMatch.put(attributeName, defaultQualification.get(attributeName));
                }
            }
        }
        return qualifiersForExactMatch;
    }

    // TODO: pulling attribute IDs repeatedly is inefficient - consider caching the entire list as a map
    /*
     * search by attribute name, if none return null, if there is only one, return. If there are multiple, then
     * search by kimType: if found return else 
     *     search by appId of kimType : if found return else
     *          search by rice app id : if found return else
     *              search by kuali app id : if found return else
     *                  return null.
     */
    protected String getKimAttributeId(String kimTypeId, String attributeName) {
        Collection<KimAttributeBo> attributeData = getAttributeByName(attributeName);
        String kimAttributeId = null;
        
        if (CollectionUtils.isNotEmpty(attributeData)) {
            if (CollectionUtils.size(attributeData) == 1) {
                kimAttributeId = attributeData.iterator().next().getId();
            } else {
                kimAttributeId = getCorrectAttributeId(kimTypeId, attributeName, attributeData);
            }
        }
        
        return kimAttributeId;
    }
    
    /*
     * Searches the KimAttributeBo for the attribute by name
     */
    protected Collection<KimAttributeBo> getAttributeByName(String attributeName) {
        Map<String, Object> critieria = new HashMap<String, Object>(1);
        critieria.put(KimConstants.AttributeConstants.ATTRIBUTE_NAME, attributeName);
        Collection<KimAttributeBo> attributeData = getBusinessObjectService().findMatching(KimAttributeBo.class, critieria);
        
        return attributeData;
    }
    
    /*
     * Attempts to get the right attribute for the kimType. If it fails, then tries by namespace.
     */
    protected String getCorrectAttributeId(String kimTypeId, String attributeName, Collection<KimAttributeBo> attributeData) {
        KimType kimType = getKimTypeInfoService().getKimType(kimTypeId);
        String attribute = getAttributeFromKimType(kimType, attributeName);
        
        return ObjectUtils.isNotNull(attribute) ? attribute : getAttributeFromNamespace(kimType, attributeName, attributeData);
    }
    
    protected String getAttributeFromKimType(KimType kimType, String attributeName) {
        if (kimType != null) {
            for (KimTypeAttribute attribute : kimType.getAttributeDefinitions()) {
                if (attribute.getKimAttribute() != null
                        && StringUtils.equals(attributeName, attribute.getKimAttribute().getAttributeName())) {
                    return attribute.getKimAttribute().getId();
                }
            }
        }
        
        return null;
    }
    
    /*
     * Gets the attribute based on the app namespace, if it cannot find then tries Rice namespace and then Kuali.
     */
    protected String getAttributeFromNamespace(KimType kimType, String attributeName, Collection<KimAttributeBo> attributes) {
        String appId = getAppIdFromNamespace(kimType.getNamespaceCode());
        String attributeId = getAttributeFromAppId(attributes, appId);

        if (ObjectUtils.isNull(attributeId)) {
            attributeId = getAttributeFromAppId(attributes, KimConstants.KIM_TYPE_RICE_NAMESPACE);
            if (ObjectUtils.isNull(attributeId)) {
                attributeId = getAttributeFromAppId(attributes, KimConstants.KIM_TYPE_DEFAULT_NAMESPACE);
            }
        }
        
        return attributeId;
    }
    
    protected String getAppIdFromNamespace(String namespaceCode) {
        Namespace appNamespace = getNamespaceService().getNamespace(namespaceCode);
        if (appNamespace == null) {
            throw new RuntimeException("Namespace " + namespaceCode + " not mapped in namespace table.");
        }
        
        return appNamespace.getApplicationId();
    }
    
    /*
     * Compares the appId of the attribute with the given appId.
     * Here we make the assumption that there are not multiple attributes with the same name
     * for a given application.
     */
    protected String getAttributeFromAppId(Collection<KimAttributeBo> attributes, String appId) {
        for (KimAttributeBo attribute : attributes) {
            if (StringUtils.equalsIgnoreCase(getAppIdFromNamespace(attribute.getNamespaceCode()), appId)) {
                return attribute.getId();
            }
        }
        
        return null;
    }
    
    protected KimTypeInfoService getKimTypeInfoService() {
        if (kimTypeInfoService == null) {
            kimTypeInfoService = KimApiServiceLocator.getKimTypeInfoService();
        }
        
        return kimTypeInfoService;
    }

    protected NamespaceService getNamespaceService() {
        if (namespaceService == null) {
            namespaceService = CoreServiceApiServiceLocator.getNamespaceService();
        }
        
        return namespaceService;
    }

    protected BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    /**
     * @return the lookupService
     */
    protected LookupService getLookupService() {
        if (lookupService == null) {
            lookupService = KRADServiceLocatorWeb.getLookupService();
        }
        return lookupService;
    }

    protected IdentityService getIdentityService() {
        if (identityService == null) {
            identityService = KimApiServiceLocator.getIdentityService();
        }

        return identityService;
    }
    
    protected GroupService getGroupService() {
        if (groupService == null) {
            groupService = KimApiServiceLocator.getGroupService();
        }

        return groupService;
    }

    protected ResponsibilityInternalService getResponsibilityInternalService() {
        if (responsibilityInternalService == null) {
            responsibilityInternalService = KimImplServiceLocator.getResponsibilityInternalService();
        }
        return responsibilityInternalService;
    }

    /**
     * @return the roleDao
     */
    protected RoleDao getRoleDao() {
        return this.roleDao;
    }

    /**
     * @param roleDao the roleDao to set
     */
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public void setCriteriaLookupService(final CriteriaLookupService criteriaLookupService) {
        this.criteriaLookupService = criteriaLookupService;
    }

    public CriteriaLookupService getCriteriaLookupService() {
        return criteriaLookupService;
    }

}
