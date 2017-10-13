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

import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.kim.api.KimApiConstants;
import org.kuali.rice.kim.api.common.delegate.DelegateMember;
import org.kuali.rice.kim.api.common.delegate.DelegateType;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kim.api.responsibility.Responsibility;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * This service provides operations for querying role and role qualification
 * data.
 *
 * <p>A role is where permissions and responsibilities are granted.  Roles have
 * a membership consisting of principals, groups or even other roles.  By
 * being assigned as members of a role, the associated principals will be
 * granted all permissions and responsibilities that have been granted to the
 * role.
 *
 * <p>Each membership assignment on the role can have a qualification which
 * defines extra information about that particular member of the role.  For
 * example, one may have the role of "Dean" but that can be further qualified
 * by the school they are the dean of, such as "Dean of Computer Science".
 * Authorization checks that are then done in the permission service can pass
 * qualifiers as part of the operation if they want to restrict the subset of
 * the role against which the check is made.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@WebService(name = "roleService", targetNamespace = KimApiConstants.Namespaces.KIM_NAMESPACE_2_0 )
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface RoleService {
    /**
     * This will create a {@link org.kuali.rice.kim.api.role.Role} exactly like the role passed in.
     *
     * @param role the role to create
     * @return the newly created object.  will never be null.
     * @throws RiceIllegalArgumentException if the role passed in is null
     * @throws RiceIllegalStateException if the role is already existing in the system
     */
    @WebMethod(operationName="createRole")
    @WebResult(name = "role")
    @CacheEvict(value={Permission.Cache.NAME, Responsibility.Cache.NAME,  Role.Cache.NAME, RoleMembership.Cache.NAME, RoleMember.Cache.NAME, DelegateMember.Cache.NAME, RoleResponsibility.Cache.NAME, DelegateType.Cache.NAME}, allEntries = true)
    Role createRole(@WebParam(name = "role") Role role)
            throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     * This will update a {@link Role}.
     *
     * @param role the role to update
     * @throws RiceIllegalArgumentException if the role is null
     * @throws RiceIllegalStateException if the role does not exist in the system
     */
    @WebMethod(operationName="updateRole")
    @WebResult(name = "role")
    @CacheEvict(value={Permission.Cache.NAME, Responsibility.Cache.NAME, Role.Cache.NAME, RoleMembership.Cache.NAME, RoleMember.Cache.NAME, DelegateMember.Cache.NAME, RoleResponsibility.Cache.NAME, DelegateType.Cache.NAME}, allEntries = true)
    Role updateRole(@WebParam(name = "role") Role role)
            throws RiceIllegalArgumentException, RiceIllegalStateException;

	/**
	 * Get the KIM Role object with the given ID.
	 *
     * @param id the id of the role.
     * @return the role with the given id or null if role doesn't exist.
     * @throws RiceIllegalArgumentException if roleId is null or Blank
	 */
    @WebMethod(operationName = "getRole")
    @WebResult(name = "role")
    @Cacheable(value= Role.Cache.NAME, key="'id=' + #p0")
    Role getRole(@WebParam(name = "id") String id) throws RiceIllegalArgumentException;

	/**
	 * Get the KIM Role objects for the role IDs in the given List.
     *
     * @param ids the ids of the roles.
     * @return a list of roles with the given ids or null if no roles are found.
     * @throws RiceIllegalArgumentException if ids is null or Blank
	 */
    @WebMethod(operationName = "getRoles")
    @XmlElementWrapper(name = "roles", required = true)
    @XmlElement(name = "role", required = false)
    @WebResult(name = "roles")
    @Cacheable(value= Role.Cache.NAME, key="'ids=' + T(org.kuali.rice.core.api.cache.CacheKeyUtils).key(#p0)")
	List<Role> getRoles( @WebParam(name="ids") List<String> ids ) throws RiceIllegalArgumentException;

	/** Get the KIM Role object with the unique combination of namespace, component,
	 * and role name.
	 *
     * @param namespaceCode the namespace code of the role.
     * @param name the name of the role.
     * @return a role with the given namespace code and name or null if role does not exist.
     * @throws RiceIllegalArgumentException if namespaceCode or name is null or blank.
	 */
    @WebMethod(operationName = "getRoleByNamespaceCodeAndName")
    @WebResult(name = "role")
    @Cacheable(value=Role.Cache.NAME, key="'namespaceCode=' + #p0 + '|' + 'name=' + #p1")
    Role getRoleByNamespaceCodeAndName(@WebParam(name = "namespaceCode") String namespaceCode,
            @WebParam(name = "name") String name) throws RiceIllegalArgumentException;

	/**
	 * Return the Role ID for the given unique combination of namespace,
	 * component and role name.
     *
     * @param namespaceCode the namespace code of the role.
     * @param name the name of the role.
     * @return a role id for a role with the given namespace code and name or null if role does not exist.
     * @throws RiceIllegalArgumentException if namespaceCode or name is null or blank.
	 */
    @WebMethod(operationName = "getRoleIdByNamespaceCodeAndName")
    @WebResult(name = "roleId")
    @Cacheable(value=Role.Cache.NAME, key="'{getRoleIdByNamespaceCodeAndName}' + 'namespaceCode=' + #p0 + '|' + 'name=' + #p1")
	String getRoleIdByNamespaceCodeAndName(@WebParam(name = "namespaceCode") String namespaceCode,
            @WebParam(name = "name") String name) throws RiceIllegalArgumentException;

	/**
	 * Checks whether the role with the given role ID is active.
	 *
	 * @param id the unique id of a role.
	 * @return true if the role with the given id is active.
     * @throws RiceIllegalArgumentException if id is null or blank.
	 */
    @WebMethod(operationName = "isRoleActive")
    @WebResult(name = "isRoleActive")
    @Cacheable(value=Role.Cache.NAME, key="'{isRoleActive}' + 'id=' + #p0")
    boolean isRoleActive( @WebParam(name="id") String id ) throws RiceIllegalArgumentException;

    /**
     * Returns a list of role qualifiers that the given principal has without taking into consideration
     * that the principal may be a member via an assigned group or role.  Use in situations where
     * you are only interested in the qualifiers that are directly assigned to the principal.
     *
     * @param principalId the principalId to
     * @param roleIds the namespace code of the role.
     * @param qualification the qualifications for the roleIds.
     * @return a map of role qualifiers for the given principalId, roleIds and qualifications or an empty map if none found.
     * @throws RiceIllegalArgumentException if principalId is null or blank or roleIds is null.
     */
    @WebMethod(operationName = "getRoleQualifersForPrincipalByRoleIds")
    @XmlElementWrapper(name = "attributes", required = true)
    @XmlElement(name = "attribute", required = false)
    @WebResult(name = "attributes")
    @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
    List<Map<String, String>> getRoleQualifersForPrincipalByRoleIds(@WebParam(name = "principalId") String principalId,
            @WebParam(name = "roleIds") List<String> roleIds, @WebParam(name = "qualification") @XmlJavaTypeAdapter(
            value = MapStringStringAdapter.class) Map<String, String> qualification)
            throws RiceIllegalArgumentException;

    /**
     * Returns a list of role qualifiers that the given principal has without taking into consideration
     * that the principal may be a member via an assigned group or role.  Use in situations where
     * you are only interested in the qualifiers that are directly assigned to the principal.
     *
     * @param principalId the principalId to
     * @param namespaceCode the namespace code of the role.
     * @param roleName the name of the role.
     * @param qualification the qualifications for the roleIds.
     * @return a map of role qualifiers for the given parameters or an empty map if none found.
     * @throws RiceIllegalArgumentException if principalId, namespaceCode, or roleName is null or blank.
     */
    @WebMethod(operationName = "getRoleQualifersForPrincipalByNamespaceAndRolename")
    @XmlElementWrapper(name = "attributes", required = true)
    @XmlElement(name = "attribute", required = false)
    @WebResult(name = "attributes")
    @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
    List<Map<String, String>> getRoleQualifersForPrincipalByNamespaceAndRolename(
            @WebParam(name = "principalId") String principalId, @WebParam(name = "namespaceCode") String namespaceCode,
            @WebParam(name = "roleName") String roleName, @WebParam(name = "qualification") @XmlJavaTypeAdapter(
            value = MapStringStringAdapter.class) Map<String, String> qualification)
            throws RiceIllegalArgumentException;

    /**
     * Returns a list of role qualifiers that the given principal.  If the principal's membership
     * is via a group or role, that group or role's qualifier on the given role is returned.
     *
     * @param principalId the principalId to
     * @param namespaceCode the namespace code of the role.
     * @param roleName the name of the role.
     * @param qualification the qualifications for the roleIds.
     * @return a map of nested role qualifiers for the given parameters or an empty map if none found.
     * @throws RiceIllegalArgumentException if principalId, namespaceCode, or roleName is null or blank.
     */
    @WebMethod(operationName = "getNestedRoleQualifersForPrincipalByNamespaceAndRolename")
    @XmlElementWrapper(name = "attributes", required = true)
    @XmlElement(name = "attribute", required = false)
    @WebResult(name = "attributes")
    @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
	List<Map<String, String>> getNestedRoleQualifersForPrincipalByNamespaceAndRolename(
            @WebParam(name = "principalId") String principalId, @WebParam(name = "namespaceCode") String namespaceCode,
            @WebParam(name = "roleName") String roleName, @WebParam(name = "qualification") @XmlJavaTypeAdapter(
            value = MapStringStringAdapter.class) Map<String, String> qualification)
            throws RiceIllegalArgumentException;

    /**
     * Returns a list of role qualifiers that the given principal.  If the principal's membership
     * is via a group or role, that group or role's qualifier on the given role is returned.
     *
     * @param principalId the principalId to
     * @param roleIds the namespace code of the role.
     * @param qualification the qualifications for the roleIds.
     * @return a map of role qualifiers for the given roleIds and qualifications or an empty map if none found.
     * @throws RiceIllegalArgumentException if principalId, namespaceCode, or roleName is null or blank.
     */
    @WebMethod(operationName = "getNestedRoleQualifiersForPrincipalByRoleIds")
    @XmlElementWrapper(name = "attributes", required = true)
    @XmlElement(name = "attribute", required = false)
    @WebResult(name = "attributes")
    @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
	List<Map<String, String>> getNestedRoleQualifiersForPrincipalByRoleIds(
            @WebParam(name = "principalId") String principalId, @WebParam(name = "roleIds") List<String> roleIds,
            @WebParam(name = "qualification") @XmlJavaTypeAdapter(
                    value = MapStringStringAdapter.class) Map<String, String> qualification)
            throws RiceIllegalArgumentException;


    // --------------------
    // Role Membership Checks
    // --------------------

    /**
     * Get all the role members (groups and principals) associated with the given list of roles
     * where their role membership/assignment matches the given qualification.  The list of RoleMemberships returned
     * will only contain group and principal members.  Any nested role members will be resolved and flattened into
     * the principals and groups that are members of that nested role (assuming qualifications match).
     *
     * The return object will have each membership relationship along with the delegations
     *
     * @param roleIds a list of role Ids.
     * @param qualification the qualifications for the roleIds.
     * @return a list of role members for the given roleIds and qualifications or an empty list if none found.
     * @throws RiceIllegalArgumentException if roleIds is null.
     */
    @WebMethod(operationName = "getRoleMembers")
    @XmlElementWrapper(name = "roleMemberships", required = true)
    @XmlElement(name = "roleMembership", required = false)
    @WebResult(name = "roleMemberships")
    @Cacheable(value= RoleMember.Cache.NAME,
               key="'roleIds=' + T(org.kuali.rice.core.api.cache.CacheKeyUtils).key(#p0) + '|' + 'qualification=' + T(org.kuali.rice.core.api.cache.CacheKeyUtils).mapKey(#p1)",
               condition="!T(org.kuali.rice.kim.api.cache.KimCacheUtils).isDynamicRoleMembership(#p0)" )
    List<RoleMembership> getRoleMembers(
                @WebParam(name="roleIds")
                List<String> roleIds,
                @WebParam(name="qualification")
                @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                Map<String, String> qualification )
            throws RiceIllegalArgumentException;

    /**
	 * This method gets all the members, then traverses down into members of type role and group to obtain the nested principal ids
	 *
     * @param namespaceCode the namespace code of the role.
     * @param roleName the name of the role
     * @param qualification the qualifications for the roleIds.
     * @return a list of role member principalIds for the given roleIds and qualifications, or an empty list if none found.
     * @throws RiceIllegalArgumentException if namespaceCode, or roleName is null or blank.
	 */
    @WebMethod(operationName = "getRoleMemberPrincipalIds")
    @XmlElementWrapper(name = "principalIds", required = true)
    @XmlElement(name = "principalId", required = false)
    @WebResult(name = "principalIds")
    @Cacheable(value= RoleMember.Cache.NAME,
               key="'namespaceCode=' + #p0 + '|' + 'roleName=' + #p1 + '|' + 'qualification=' + T(org.kuali.rice.core.api.cache.CacheKeyUtils).mapKey(#p2)",
               condition="!T(org.kuali.rice.kim.api.cache.KimCacheUtils).isDynamicMembshipRoleByNamespaceAndName(#p0, #p1)" )
    Collection<String> getRoleMemberPrincipalIds(@WebParam(name="namespaceCode") String namespaceCode,
                @WebParam(name="roleName") String roleName,
                @WebParam(name="qualification")
                @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                Map<String, String> qualification)
            throws RiceIllegalArgumentException;

    /**
     * Returns whether the given principal has any of the passed role IDs with the given qualification.
     *
     * @param principalId the principal Id to check.
     * @param roleIds the list of role ids.
     * @param qualification the qualifications for the roleIds.
     * @return true if the principal is assigned the one of the given roleIds with the passed in qualifications.
     * @throws RiceIllegalArgumentException if roleIds is null or principalId is null or blank.
     */
    @WebMethod(operationName = "principalHasRole")
    @WebResult(name = "principalHasRole")
    boolean principalHasRole( @WebParam(name="principalId") String principalId,
            @WebParam(name="roleIds") List<String> roleIds,
            @WebParam(name="qualification") @XmlJavaTypeAdapter(value = MapStringStringAdapter.class) Map<String, String> qualification )
            throws RiceIllegalArgumentException;
    
    /**
     * Returns whether the given principal has any of the passed role IDs with the given qualification.
     *
     * @param principalId the principal Id to check.
     * @param roleIds the list of role ids.
     * @param qualification the qualifications for the roleIds.
     * @param checkDelegations whether delegations should be checked or not
     * @return true if the principal is assigned the one of the given roleIds with the passed in qualifications.
     * @throws RiceIllegalArgumentException if roleIds is null or principalId is null or blank.
     * @since 2.1.1
     */
    @WebMethod(operationName = "principalHasRoleCheckDelegation")
    @WebResult(name = "principalHasRoleCheckDelegation")
    boolean principalHasRole( @WebParam(name="principalId") String principalId,
            @WebParam(name="roleIds") List<String> roleIds,
            @WebParam(name="qualification") @XmlJavaTypeAdapter(value = MapStringStringAdapter.class) Map<String, String> qualification, boolean checkDelegations)
            throws RiceIllegalArgumentException;

    /**
     * Returns the subset of the given principal ID list which has the given role and qualification.
     * This is designed to be used by lookups of people by their roles.
     *
     * @param principalIds the principal Ids to check.
     * @param roleNamespaceCode the namespaceCode of the role.
     * @param roleName the name of the role.
     * @param qualification the qualifications for the roleIds.
     * @return list of principalIds that is the subset of list passed in with the given role and qualifications or an empty list.
     * @throws RiceIllegalArgumentException if principalIds is null or the roleNamespaceCode or roleName is null or blank.
     */
    @WebMethod(operationName = "getPrincipalIdSubListWithRole")
    @XmlElementWrapper(name = "principalIds", required = true)
    @XmlElement(name = "principalId", required = false)
    @WebResult(name = "principalIds")
    @Cacheable(value= RoleMember.Cache.NAME,
               key="'getPrincipalIdSubListWithRole' + 'principalIds=' + T(org.kuali.rice.core.api.cache.CacheKeyUtils).key(#p0) + '|' + 'roleNamespaceCode=' + #p1 + '|' + 'roleName=' + #p2 + '|' + 'qualification=' + T(org.kuali.rice.core.api.cache.CacheKeyUtils).mapKey(#p3)",
            condition="!T(org.kuali.rice.kim.api.cache.KimCacheUtils).isDynamicMembshipRoleByNamespaceAndName(#p1, #p2)" )
            List<String> getPrincipalIdSubListWithRole( @WebParam(name="principalIds") List<String> principalIds,
            @WebParam(name="roleNamespaceCode") String roleNamespaceCode,
            @WebParam(name="roleName") String roleName,
            @WebParam(name="qualification") @XmlJavaTypeAdapter(value = MapStringStringAdapter.class) Map<String, String> qualification )
            throws RiceIllegalArgumentException;

    /**
	 *
	 * This method gets search results for role lookup
     *
     * @param queryByCriteria the qualifications for the roleIds.
     * @return query results.  will never return null.
     * @throws RiceIllegalArgumentException if queryByCriteria is null.
	 */
    @WebMethod(operationName = "getRolesSearchResults")
    @WebResult(name = "results")
	RoleQueryResults findRoles(@WebParam(name = "query") QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException;



    /**
     * Gets all direct members of the roles that have ids within the given list
     * of role ids.  This method does not recurse into any nested roles.
     *
     *  <p>The resulting List of role membership will contain membership for
     *  all the roles with the specified ids.  The list is not guaranteed to be
     *  in any particular order and may have membership info for the
     *  different roles interleaved with each other.
     *
     * @param roleIds a list of  role Ids.
     * @return list of RoleMembership that contains membership for the specified roleIds or empty list if none found.
     * @throws RiceIllegalArgumentException if roleIds is null.
     */
    @WebMethod(operationName = "getFirstLevelRoleMembers")
    @XmlElementWrapper(name = "roleMemberships", required = true)
    @XmlElement(name = "roleMembership", required = false)
    @WebResult(name = "roleMemberships")
    @Cacheable(value=RoleMembership.Cache.NAME, key="'roleIds=' + T(org.kuali.rice.core.api.cache.CacheKeyUtils).key(#p0)")
	List<RoleMembership> getFirstLevelRoleMembers(
                @WebParam(name="roleIds") List<String> roleIds) throws RiceIllegalArgumentException;

	/**
	 * Gets role member information based on the given search criteria.
     *
     * @param queryByCriteria the qualifications for the roleIds.
     * @return query results.  will never return null.
     * @throws RiceIllegalArgumentException if queryByCriteria is null.
	 */
    @WebMethod(operationName = "findRoleMemberships")
    @WebResult(name = "results")
	RoleMembershipQueryResults findRoleMemberships(@WebParam(name = "query") QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException;

	/**
	 * Gets a list of Roles that the given member belongs to.
     *
     * @param memberType the role member type.
     * @param memberId the role member id (principalId, roleId, groupId).
     * @return list of RoleMembership that contains membership for the specified roleIds or an empty list if none found.
     * @throws RiceIllegalArgumentException if memberType or memberId is null or blank.
	 */
    @WebMethod(operationName = "getMemberParentRoleIds")
    @XmlElementWrapper(name = "roleIds", required = true)
    @XmlElement(name = "roleId", required = false)
    @WebResult(name = "roleIds")
    @Cacheable(value=RoleMembership.Cache.NAME, key="'memberType=' + #p0 + '|' + 'memberId=' + #p1")
	List<String> getMemberParentRoleIds(String memberType, String memberId) throws RiceIllegalArgumentException;


    /**
     * Gets role members based on the given search criteria.
     *
     * @param queryByCriteria the qualifications for the roleIds.
     * @return query results.  will never return null.
     * @throws RiceIllegalArgumentException if queryByCriteria is null.
     */
    @WebMethod(operationName = "findRoleMembers")
    @WebResult(name = "results")
	RoleMemberQueryResults findRoleMembers(@WebParam(name = "query") QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException;


    /**
     * Gets a list of Roles Ids that are a member of the given roleId, including nested membership.
     *
     * @param roleId the role id.
     * @return list of RoleIds that are members of the given role or and empty list if none found.
     * @throws RiceIllegalArgumentException if roleId is null or blank.
     */
    @WebMethod(operationName = "getRoleTypeRoleMemberIds")
    @XmlElementWrapper(name = "memberIds", required = true)
    @XmlElement(name = "memberId", required = false)
    @WebResult(name = "memberIds")
    @Cacheable(value=RoleMember.Cache.NAME, key="'{getRoleTypeRoleMemberIds}' + 'roleId=' + #p0")
    Set<String> getRoleTypeRoleMemberIds(@WebParam(name = "roleId") String roleId) throws RiceIllegalArgumentException;


    /**
     * Gets role members based on the given search criteria.
     *
     * @param queryByCriteria the qualifications for the roleIds.
     * @return query results.  will never return null.
     * @throws RiceIllegalArgumentException if queryByCriteria is null.
     */
    @WebMethod(operationName = "findDelegateMembers")
    @WebResult(name = "results")
    DelegateMemberQueryResults findDelegateMembers(@WebParam(name = "query") QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException;

	/**
	 * Gets the delegate members for the given delegation.
     *
     * @param delegateId the delegate id.
     * @return list of delegate members that are members of the given delegation or an empty list if none found.
     * @throws RiceIllegalArgumentException if delegationId is null or blank.
	 */
    @WebMethod(operationName = "getDelegationMembersByDelegationId")
    @XmlElementWrapper(name = "delegateMembers", required = true)
    @XmlElement(name = "delegateMember", required = false)
    @WebResult(name = "delegateMembers")
    @Cacheable(value=DelegateMember.Cache.NAME, key="'delegateId=' + #p0")
    List<DelegateMember> getDelegationMembersByDelegationId(
            @WebParam(name = "delegateId") String delegateId) throws RiceIllegalArgumentException;


    /**
     * Gets the delegate member for the given delegationId and memberId.
     *
     * @param delegationId the delegate id.
     * @param memberId the member id matching the DelegateMember
     * @return the delegate member with the given parameters or null if not found.
     * @throws RiceIllegalArgumentException if delegationId or memberId is null or blank.
     */
    @WebMethod(operationName = "getDelegationMemberByDelegationAndMemberId")
    @WebResult(name = "delegateMember")
    @Cacheable(value=DelegateMember.Cache.NAME, key="'delegationId=' + #p0 + '|' + 'memberId=' + #p1")
    DelegateMember getDelegationMemberByDelegationAndMemberId(
            @WebParam(name = "delegationId") String delegationId, @WebParam(name = "memberId") String memberId) throws RiceIllegalArgumentException;


    /**
     * Gets the delegate member with the given delegation member id.
     *
     * @param id the member id matching the DelegateMember
     * @return the delegate member with the given parameters or null if not found.
     * @throws RiceIllegalArgumentException if delegationId or memberId is null or blank.
     */
    @WebMethod(operationName = "getDelegationMemberById")
    @WebResult(name = "delegateMember")
    @Cacheable(value=DelegateMember.Cache.NAME, key="'id=' + #p0")
    DelegateMember getDelegationMemberById(@WebParam(name = "id") String id) throws RiceIllegalArgumentException;


    /**
     * Gets a list of role reponsibilities for the given role id.
     *
     * @param roleId the role Id.
     * @return a list of RoleResponsibilities for the given role Id, or an empty list if none found.
     * @throws RiceIllegalArgumentException if roleId is null or blank.
     */
    @WebMethod(operationName = "getRoleResponsibilities")
    @XmlElementWrapper(name = "roleResponsibilities", required = true)
    @XmlElement(name = "roleResponsibility", required = false)
    @WebResult(name = "roleResponsibilities")
    @Cacheable(value=RoleResponsibility.Cache.NAME, key="'roleId=' + #p0")
	List<RoleResponsibility> getRoleResponsibilities(@WebParam(name="roleId") String roleId)  throws RiceIllegalArgumentException;


    /**
     * Gets a list of RoleResponsibilityActions for the given role member id.
     *
     * @param roleMemberId the role member Id.
     * @return a list of RoleResponsibilityActions for the given role member Id, or an empty list if none found.
     * @throws RiceIllegalArgumentException if roleMemberId is null or blank.
     */
    @WebMethod(operationName = "getRoleMemberResponsibilityActions")
    @XmlElementWrapper(name = "roleResponsibilityActions", required = true)
    @XmlElement(name = "roleResponsibilityAction", required = false)
    @WebResult(name = "roleResponsibilityActions")
    @Cacheable(value=RoleResponsibility.Cache.NAME, key="'roleMemberId=' + #p0")
	List<RoleResponsibilityAction> getRoleMemberResponsibilityActions(
            @WebParam(name = "roleMemberId") String roleMemberId)  throws RiceIllegalArgumentException;


    /**
     * Gets a DelegateType for the given role id and delegation type.
     *
     * @param roleId the role Id.
     * @param delegateType type of delegation
     * @return the DelegateType for the given role Id and delegationType, or null if none found.
     * @throws RiceIllegalArgumentException if roleId or delegationType is null or blank.
     */
    @WebMethod(operationName = "getDelegateTypeByRoleIdAndDelegateTypeCode")
    @WebResult(name = "delegateType")
    @Cacheable(value=DelegateType.Cache.NAME, key="'roleId=' + #p0 + '|' + 'delegateType=' + #p1")
    DelegateType getDelegateTypeByRoleIdAndDelegateTypeCode(@WebParam(name = "roleId") String roleId,
            @WebParam(name = "delegateType") DelegationType delegateType)  throws RiceIllegalArgumentException;


    /**
     * Gets a DelegateType for the given delegation id.
     *
     * @param delegationId the id of delegation
     * @return the DelegateType for the given delegation Id, or null if none found.
     * @throws RiceIllegalArgumentException if delegationId is null or blank.
     */
    @WebMethod(operationName = "getDelegateTypeByDelegationId")
    @WebResult(name = "delegateType")
    @Cacheable(value=DelegateType.Cache.NAME, key="'delegationId=' + #p0")
    DelegateType getDelegateTypeByDelegationId(@WebParam(name = "delegationId") String delegationId)  throws RiceIllegalArgumentException;

    /**
	 * Assigns the principal with the given id to the role with the specified
	 * namespace code and name with the supplied set of qualifications.
     *
     * @param principalId the principalId
     * @param namespaceCode the namespaceCode of the Role
     * @param roleName the name of the role
     * @param qualifications the qualifications for the principalId to be assigned to the role
     * @return newly created/assigned RoleMember.
     * @throws RiceIllegalArgumentException if princialId, namespaceCode or roleName is null or blank.
	 */
    @WebMethod(operationName = "assignPrincipalToRole")
    @WebResult(name = "roleMember")
    @CacheEvict(value={Role.Cache.NAME, Permission.Cache.NAME, Responsibility.Cache.NAME, RoleMembership.Cache.NAME, RoleMember.Cache.NAME, DelegateMember.Cache.NAME, RoleResponsibility.Cache.NAME, DelegateType.Cache.NAME }, allEntries = true)
    RoleMember assignPrincipalToRole(@WebParam(name="principalId") String principalId,
                @WebParam(name="namespaceCode")
                String namespaceCode,
                @WebParam(name="roleName")
                String roleName,
                @WebParam(name="qualifications")
                @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                Map<String, String> qualifications)
            throws RiceIllegalArgumentException;

	/**
	 * Assigns the group with the given id to the role with the specified
	 * namespace code and name with the supplied set of qualifications.
     *
     * @param groupId the groupId
     * @param namespaceCode the namespaceCode of the Role
     * @param roleName the name of the role
     * @param qualifications the qualifications for the principalId to be assigned to the role
     * @return newly created/assigned RoleMember.
     * @throws RiceIllegalArgumentException if groupId, namespaceCode or roleName is null or blank.
	 */
    @WebMethod(operationName = "assignGroupToRole")
    @WebResult(name = "roleMember")
    @CacheEvict(value={Role.Cache.NAME, Permission.Cache.NAME, Responsibility.Cache.NAME, RoleMembership.Cache.NAME, RoleMember.Cache.NAME, DelegateMember.Cache.NAME, RoleResponsibility.Cache.NAME, DelegateType.Cache.NAME }, allEntries = true)
    RoleMember assignGroupToRole(@WebParam(name="groupId") String groupId,
    		@WebParam(name="namespaceCode") String namespaceCode,
    		@WebParam(name="roleName") String roleName,
    		@WebParam(name="qualifications") @XmlJavaTypeAdapter(value = MapStringStringAdapter.class) Map<String, String> qualifications)
            throws RiceIllegalArgumentException;

	/**
	 * Assigns the role with the given id to the role with the specified
	 * namespace code and name with the supplied set of qualifications.
     *
     * @param roleId the roleId
     * @param namespaceCode the namespaceCode of the Role
     * @param roleName the name of the role
     * @param qualifications the qualifications for the principalId to be assigned to the role
     * @return newly created/assigned RoleMember.
     * @throws RiceIllegalArgumentException if princiapId, namespaceCode or roleName is null or blank.
	 */
    @WebMethod(operationName = "assignRoleToRole")
    @WebResult(name = "roleMember")
    @CacheEvict(value={Role.Cache.NAME, Permission.Cache.NAME, Responsibility.Cache.NAME, RoleMembership.Cache.NAME, RoleMember.Cache.NAME, DelegateMember.Cache.NAME, RoleResponsibility.Cache.NAME, DelegateType.Cache.NAME }, allEntries = true)
    RoleMember assignRoleToRole(@WebParam(name="roleId") String roleId,
    		@WebParam(name="namespaceCode") String namespaceCode,
    		@WebParam(name="roleName") String roleName,
    		@WebParam(name="qualifications") @XmlJavaTypeAdapter(value = MapStringStringAdapter.class) Map<String, String> qualifications)
            throws RiceIllegalArgumentException;

	/**
	 * Creates a new RoleMember.  Needs to be passed a valid RoleMember object that does not currently exist.
     *
     * @param roleMember the new RoleMember to save.
     * @return RoleMember as created.
     * @throws RiceIllegalArgumentException if roleMember is null.
     * @throws RiceIllegalStateException if roleMember already exists.
	 */
    @WebMethod(operationName = "createRoleMember")
    @WebResult(name = "roleMember")
    @CacheEvict(value={Role.Cache.NAME, Permission.Cache.NAME, Responsibility.Cache.NAME, RoleMembership.Cache.NAME, RoleMember.Cache.NAME, DelegateMember.Cache.NAME, RoleResponsibility.Cache.NAME, DelegateType.Cache.NAME }, allEntries = true)
    RoleMember createRoleMember(
                @WebParam(name = "roleMember")
                RoleMember roleMember) throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
	 * Updates the given roleMember to the values in the passed in roleMember
     *
     * @param roleMember the new RoleMember to save.
     * @return RoleMember as updated.
     * @throws RiceIllegalArgumentException if roleMember is null.
     * @throws RiceIllegalStateException if roleMember does not yet exist.
	 */
    @WebMethod(operationName = "updateRoleMember")
    @WebResult(name = "roleMember")
    @CacheEvict(value={Role.Cache.NAME, Permission.Cache.NAME, Responsibility.Cache.NAME, RoleMembership.Cache.NAME, RoleMember.Cache.NAME, DelegateMember.Cache.NAME, RoleResponsibility.Cache.NAME, DelegateType.Cache.NAME }, allEntries = true)
    RoleMember updateRoleMember(@WebParam(name = "roleMember") RoleMember roleMember) throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     * Updates the given delegateMember to the values in the passed in delegateMember
     *
     * @param delegateMember the new DelegateMember to save.
     * @return DelegateMember as updated.
     * @throws RiceIllegalArgumentException if delegateMember is null.
     * @throws RiceIllegalStateException if delegateMember does not yet exist.
     */
    @WebMethod(operationName = "updateDelegateMember")
    @WebResult(name = "delegateMember")
    @CacheEvict(value={Role.Cache.NAME, RoleMembership.Cache.NAME, RoleMember.Cache.NAME, DelegateMember.Cache.NAME, RoleResponsibility.Cache.NAME, DelegateType.Cache.NAME }, allEntries = true)
    DelegateMember updateDelegateMember(@WebParam(name = "delegateMember") DelegateMember delegateMember) throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     * Creates a new DelegateMember.  Needs to be passed a valid DelegateMember object that does not currently exist.
     *
     * @param delegateMember the new DelegateMember to save.
     * @return DelegateMember as created.
     * @throws RiceIllegalArgumentException if delegateMember is null.
     * @throws RiceIllegalStateException if delegateMember already exists.
     */
    @WebMethod(operationName = "createDelegateMember")
    @WebResult(name = "delegateMember")
    @CacheEvict(value={Role.Cache.NAME, RoleMembership.Cache.NAME, RoleMember.Cache.NAME, DelegateMember.Cache.NAME, RoleResponsibility.Cache.NAME, DelegateType.Cache.NAME }, allEntries = true)
    DelegateMember createDelegateMember(
            @WebParam(name = "delegateMember")
            DelegateMember delegateMember) throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     * Removes existing DelegateMembers.  Needs to be passed DelegateMember objects.
     *
     * @param  DelegateMembers to remove.
     * @throws RiceIllegalArgumentException if delegateMember is null.
     */
    @WebMethod(operationName = "removeDelegateMembers")
    @CacheEvict(value={Role.Cache.NAME, RoleMembership.Cache.NAME, RoleMember.Cache.NAME, DelegateMember.Cache.NAME, RoleResponsibility.Cache.NAME, DelegateType.Cache.NAME }, allEntries = true)
    void removeDelegateMembers(
            @WebParam(name = "delegateMembers")
            List<DelegateMember> delegateMembers) throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     * Creates a new RoleResponsibilityAction.  Needs to be passed a valid RoleResponsibilityAction
     * object that does not currently exist.
     *
     * @param roleResponsibilityAction the new RoleResponsibilityAction to save.
     * @return RoleResponsibilityAction as created.
     * @throws RiceIllegalArgumentException if roleResponsibilityAction is null.
     * @throws RiceIllegalStateException if roleResponsibilityAction already exists.
     */
    @WebMethod(operationName = "createRoleResponsibilityAction")
    @CacheEvict(value={Permission.Cache.NAME, Responsibility.Cache.NAME, RoleMembership.Cache.NAME, RoleMember.Cache.NAME, DelegateMember.Cache.NAME, RoleResponsibility.Cache.NAME, DelegateType.Cache.NAME }, allEntries = true)
    RoleResponsibilityAction createRoleResponsibilityAction(@WebParam(name = "roleResponsibilityAction") RoleResponsibilityAction roleResponsibilityAction) throws RiceIllegalArgumentException;

    /**
     * Updates the given RoleResponsibilityAction to the values in the passed in roleResponsibilityAction
     *
     * @since 2.1.2
     * @param roleResponsibilityAction the new RoleResponsibilityAction to save.
     * @return RoleResponsibilityAction as updated.
     * @throws RiceIllegalArgumentException if roleResponsibilityAction is null.
     * @throws RiceIllegalStateException if roleResponsibilityAction does not exist.
     */
    @WebMethod(operationName = "updateRoleResponsibilityAction")
    @CacheEvict(value={Permission.Cache.NAME, Responsibility.Cache.NAME, RoleMembership.Cache.NAME, RoleMember.Cache.NAME, DelegateMember.Cache.NAME, RoleResponsibility.Cache.NAME, DelegateType.Cache.NAME }, allEntries = true)
    RoleResponsibilityAction updateRoleResponsibilityAction(@WebParam(name = "roleResponsibilityAction") RoleResponsibilityAction roleResponsibilityAction) throws RiceIllegalArgumentException;

    /**
     * Deletes the given RoleResponsibilityAction
     *
     * @since 2.1.2
     * @param roleResponsibilityActionId id of the RoleResponsibilityAction to delete.
     * @throws RiceIllegalArgumentException if roleResponsibilityActionId is null.
     * @throws RiceIllegalStateException if roleResponsibilityAction does not exist.
     */
    @WebMethod(operationName = "deleteRoleResponsibilityAction")
    @CacheEvict(value={Permission.Cache.NAME, Responsibility.Cache.NAME, RoleMembership.Cache.NAME, RoleMember.Cache.NAME, DelegateMember.Cache.NAME, RoleResponsibility.Cache.NAME, DelegateType.Cache.NAME }, allEntries = true)
    void deleteRoleResponsibilityAction(@WebParam(name = "roleResponsibilityActionId") String roleResponsibilityActionId) throws RiceIllegalArgumentException;

    /**
     * Creates a new DelegateType.  Needs to be passed a valid DelegateType
     * object that does not currently exist.
     *
     * @param delegateType the new DelegateType to save.
     * @return DelegateType as created.
     * @throws RiceIllegalArgumentException if delegateType is null.
     * @throws RiceIllegalStateException if delegateType already exists.
     */
    @WebMethod(operationName = "createDelegateType")
    @CacheEvict(value={Permission.Cache.NAME, Responsibility.Cache.NAME, RoleMembership.Cache.NAME, RoleMember.Cache.NAME, DelegateMember.Cache.NAME, RoleResponsibility.Cache.NAME, DelegateType.Cache.NAME }, allEntries = true)
    DelegateType createDelegateType(@WebParam(name="delegateType") DelegateType delegateType) throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     * Updates the given DelegateType to the values in the passed in delegateType
     *
     * @param delegateType the new DelegateType to save.
     * @return DelegateType as updated.
     * @throws RiceIllegalArgumentException if delegateType is null.
     * @throws RiceIllegalStateException if delegateType does not yet exist.
     */
    @WebMethod(operationName = "updateDelegateType")
    @CacheEvict(value={Permission.Cache.NAME, Responsibility.Cache.NAME, RoleMembership.Cache.NAME, RoleMember.Cache.NAME, DelegateMember.Cache.NAME, RoleResponsibility.Cache.NAME, DelegateType.Cache.NAME }, allEntries = true)
    DelegateType updateDelegateType(@WebParam(name="delegateType") DelegateType delegateType) throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     * Remove the principal with the given id and qualifications from the role
     * with the specified namespace code and role name.
     *
     * @param principalId the principalId
     * @param namespaceCode the namespaceCode of the Role
     * @param roleName the name of the role
     * @param qualifications the qualifications for the principalId to be assigned to the role
     * @return void.
     * @throws RiceIllegalArgumentException if principalId, namespaceCode or roleName is null or blank.
     */
    @WebMethod(operationName = "removePrincipalFromRole")
    @CacheEvict(value={Role.Cache.NAME, Permission.Cache.NAME, Responsibility.Cache.NAME, RoleMembership.Cache.NAME, RoleMember.Cache.NAME, DelegateMember.Cache.NAME, RoleResponsibility.Cache.NAME, DelegateType.Cache.NAME }, allEntries = true)
    void removePrincipalFromRole(@WebParam(name="principalId") String principalId,
    		@WebParam(name="namespaceCode") String namespaceCode,
    		@WebParam(name="roleName") String roleName,
    		@WebParam(name="qualifications") @XmlJavaTypeAdapter(value = MapStringStringAdapter.class) Map<String, String> qualifications) throws RiceIllegalArgumentException;

    /**
     * Remove the group with the given id and qualifications from the role
     * with the specified namespace code and role name.
     *
     * @param groupId the groupId
     * @param namespaceCode the namespaceCode of the Role
     * @param roleName the name of the role
     * @param qualifications the qualifications for the principalId to be assigned to the role
     * @return void.
     * @throws RiceIllegalArgumentException if groupId, namespaceCode or roleName is null or blank.
     */
    @WebMethod(operationName = "removeGroupFromRole")
    @CacheEvict(value={Role.Cache.NAME, Permission.Cache.NAME, Responsibility.Cache.NAME, RoleMembership.Cache.NAME, RoleMember.Cache.NAME, DelegateMember.Cache.NAME, RoleResponsibility.Cache.NAME, DelegateType.Cache.NAME }, allEntries = true)
    void removeGroupFromRole(@WebParam(name="groupId") String groupId,
    		@WebParam(name="namespaceCode") String namespaceCode,
    		@WebParam(name="roleName") String roleName,
    		@WebParam(name="qualifications") @XmlJavaTypeAdapter(value = MapStringStringAdapter.class) Map<String, String> qualifications) throws RiceIllegalArgumentException;

    /**
     * Remove the group with the given id and qualifications from the role
     * with the specified namespace code and role name.
     *
     * @param roleId the roleId
     * @param namespaceCode the namespaceCode of the Role
     * @param roleName the name of the role
     * @param qualifications the qualifications for the principalId to be assigned to the role
     * @return void.
     * @throws RiceIllegalArgumentException if roleId, namespaceCode or roleName is null or blank.
     */
    @WebMethod(operationName = "removeRoleFromRole")
    @CacheEvict(value={Role.Cache.NAME, Permission.Cache.NAME, Responsibility.Cache.NAME, RoleMembership.Cache.NAME, RoleMember.Cache.NAME, DelegateMember.Cache.NAME, RoleResponsibility.Cache.NAME, DelegateType.Cache.NAME }, allEntries = true)
    void removeRoleFromRole(@WebParam(name="roleId") String roleId,
    		@WebParam(name="namespaceCode") String namespaceCode,
    		@WebParam(name="roleName") String roleName,
    		@WebParam(name="qualifications") @XmlJavaTypeAdapter(value = MapStringStringAdapter.class) Map<String, String> qualifications) throws RiceIllegalArgumentException;

    /**
     * Assigns the given permission to the given role
     *
     * @param permissionId the permissionId
     * @param roleId the roleId
     * @return void.
     * @throws RiceIllegalArgumentException if permissionId or roleId is null or blank.
     */
    @WebMethod(operationName = "assignPermissionToRole")
    @CacheEvict(value={Role.Cache.NAME, Permission.Cache.NAME, Responsibility.Cache.NAME, RoleMembership.Cache.NAME, RoleMember.Cache.NAME, DelegateMember.Cache.NAME, RoleResponsibility.Cache.NAME, DelegateType.Cache.NAME }, allEntries = true)
    void assignPermissionToRole(
            @WebParam(name = "permissionId") String permissionId,
            @WebParam(name = "roleId") String roleId)
            throws RiceIllegalArgumentException;

    /**
     * Removes the given permission to the given role
     *
     * @param permissionId the permissionId
     * @param roleId the roleId
     * @return void.
     * @throws RiceIllegalArgumentException if permissionId or roleId is null or blank.
     */
    @WebMethod(operationName = "revokePermissionFromRole")
    @CacheEvict(value={Role.Cache.NAME, Permission.Cache.NAME, Responsibility.Cache.NAME, RoleMembership.Cache.NAME, RoleMember.Cache.NAME, DelegateMember.Cache.NAME, RoleResponsibility.Cache.NAME, DelegateType.Cache.NAME }, allEntries = true)
    void revokePermissionFromRole(
            @WebParam(name = "permissionId") String permissionId,
            @WebParam(name = "roleId") String roleId)
            throws RiceIllegalArgumentException;


    /**
     * Determines if a role with a provided id is a derived role
     *
     * @since 2.1.1
     * @param roleId the roleId
     * @return true if role is a derived role
     * @throws RiceIllegalArgumentException if roleId is null or blank.
     */
    @WebMethod(operationName = "isDerivedRole")
    @WebResult(name = "isDerivedRole")
    @Cacheable(value= Role.Cache.NAME, key="'{isDerivedRole}' + 'roleId=' + #p0")
    boolean isDerivedRole(@WebParam(name = "roleId") String roleId) throws RiceIllegalArgumentException;

    /**
     * Determines if a role with a provided id is a uses dynamic role memberships
     *
     * @since 2.1.1
     * @param roleId the roleId
     * @return true if role uses dynamic memberships
     * @throws RiceIllegalArgumentException if roleId is null or blank.
     */
    @WebMethod(operationName = "isDynamicRoleMembership")
    @WebResult(name = "isDynamicRoleMembership")
    @Cacheable(value= Role.Cache.NAME, key="'{isDynamicRoleMembership}' + 'roleId=' + #p0")
    boolean isDynamicRoleMembership(@WebParam(name = "roleId") String roleId) throws RiceIllegalArgumentException;
}