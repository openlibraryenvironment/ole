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
package org.kuali.rice.kim.api.permission;

import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.common.assignee.Assignee;
import org.kuali.rice.kim.api.common.template.Template;
import org.kuali.rice.kim.api.common.template.TemplateQueryResults;
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
import java.util.List;
import java.util.Map;

/**
 * This service provides operations for evaluating permissions and querying for permission data.
 * 
 * <p>A permission is the ability to perform an action.  All permissions have a permission template.
 * Both permissions and permission templates are uniquely identified by a namespace code plus a name.
 * The permission template defines the course-grained permission and specifies what additional
 * permission details need to be collected on permissions that use that template.  For example, a
 * permission template might have a name of "Initiate Document" which requires a permission detail
 * specifying the document type that can be initiated.  A permission created from the "Initiate Document"
 * template would define the name of the specific Document Type that can be initiated as a permission
 * detail.
 * 
 * <p>The isAuthorized and isAuthorizedByTemplate operations
 * on this service are used to execute authorization checks for a principal against a
 * permission.  Permissions are always assigned to roles (never directly to a principal or
 * group).  A particular principal will be authorized for a given permission if the permission
 * evaluates to true (according to the permission evaluation logic and based on any supplied
 * permission details) and that principal is assigned to a role which has been granted the permission.
 * 
 * <p>The actual logic for how permission evaluation logic is defined and executed is dependent upon
 * the permission service implementation.  However, it will typically be associated with the permission
 * template used on the permission.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = "permissionService", targetNamespace = KimConstants.Namespaces.KIM_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface PermissionService {

    /**
     * This will create a {@link org.kuali.rice.kim.api.permission.Permission} exactly like the permission passed in.
     *
     * @param permission the permission to create
     * @return the newly created object.  will never be null.
     * @throws RiceIllegalArgumentException if the permission is null
     * @throws RiceIllegalStateException if the permission is already existing in the system
     */
    @WebMethod(operationName="createPermission")
    @WebResult(name = "permission")
    @CacheEvict(value={Permission.Cache.NAME, Template.Cache.NAME + "{Permission}"}, allEntries = true)
    Permission createPermission(@WebParam(name = "permission") Permission permission)
            throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     * This will update a {@link Permission}.
     *
     * @param permission the permission to update
     * @return the updated object.  will never be null
     * @throws RiceIllegalArgumentException if the permission is null
     * @throws RiceIllegalStateException if the permission does not exist in the system
     */
    @WebMethod(operationName="updatePermission")
    @WebResult(name = "permission")
    @CacheEvict(value={Permission.Cache.NAME, Template.Cache.NAME + "{Permission}"}, allEntries = true)
    Permission updatePermission(@WebParam(name = "permission") Permission permission)
            throws RiceIllegalArgumentException, RiceIllegalStateException;

    // --------------------
    // Authorization Checks
    // --------------------
	
    /**
     * Checks in a given principal id has a permission using the passed in permission information.
     * This method should not be used for true authorization checks since a principal
     * may only have this permission within a given context.  It could be used to
     * identify that the user would have some permissions within a certain area.
     * Later checks would identify exactly what permissions were granted.
     *
     * It can also be used when the client application KNOWS that this is a role which
     * is never qualified.
     *
     * @param principalId the principal id to check.  cannot be null or blank.
     * @param namespaceCode the namespace code.  cannot be null or blank.
     * @param permissionName the permission name. cannot be null or blank.
     * @return true is principal has permission
     * @throws RiceIllegalArgumentException if the principalId, namespaceCode, permissionName is null or blank
     */
    @WebMethod(operationName = "hasPermission")
    @WebResult(name = "hasPermission")
    boolean hasPermission( @WebParam(name="principalId") String principalId,
    					   @WebParam(name="namespaceCode") String namespaceCode,
    					   @WebParam(name="permissionName") String permissionName) throws RiceIllegalArgumentException;



    /**
     * Checks whether the given qualified permission is granted to the principal given
     * the passed roleQualification.  If no roleQualification is passed (null or empty)
     * then this method behaves the same as {@link #hasPermission(String, String, String)}.
     *
     * Each role assigned to the principal is checked for qualifications.  If a qualifier
     * exists on the principal's membership in that role, that is checked first through
     * the role's type service.  Once it is determined that the principal has the role
     * in the given context (qualification), the permissions are examined.
     *
     *
     * @param principalId the principal id to check.  cannot be null or blank.
     * @param namespaceCode the namespace code.  cannot be null or blank.
     * @param permissionName the permission name. cannot be null or blank.
     * @param qualification the qualifications to test against.
     * @return true is principal has permission
     * @throws RiceIllegalArgumentException if the principalId, namespaceCode, permissionName is null or blank
     */
    @WebMethod(operationName = "isAuthorized")
    @WebResult(name = "isAuthorized")
    boolean isAuthorized( @WebParam(name="principalId") String principalId,
    					  @WebParam(name="namespaceCode") String namespaceCode,
    					  @WebParam(name="permissionName") String permissionName,
                          @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
    					  @WebParam(name="qualification") Map<String, String> qualification  ) throws RiceIllegalArgumentException;

    /**
     * Checks whether the principal has been granted a permission matching the given details
     * without taking role qualifiers into account.
     *
     * This method should not be used for true authorization checks since a principal
     * may only have this permission within a given context.  It could be used to
     * identify that the user would have some permissions within a certain area.
     * Later checks would identify exactly what permissions were granted.
     *
     * It can also be used when the client application KNOWS that this is a role which
     * is never qualified.
     *
     * @param principalId the principal id to check.  cannot be null or blank.
     * @param namespaceCode the namespace code.  cannot be null or blank.
     * @param permissionTemplateName the permission name. cannot be null or blank.
     * @param permissionDetails the permission details
     * @return true is principal has permission
     * @throws RiceIllegalArgumentException if the principalId, namespaceCode, permissionName is null or blank
     */
    @WebMethod(operationName = "hasPermissionByTemplate")
    @WebResult(name = "hasPermission")
    boolean hasPermissionByTemplate(@WebParam(name = "principalId") String principalId,
            @WebParam(name = "namespaceCode") String namespaceCode,
            @WebParam(name = "permissionTemplateName") String permissionTemplateName,
            @XmlJavaTypeAdapter(value = MapStringStringAdapter.class) @WebParam(
                    name = "permissionDetails") Map<String, String> permissionDetails) throws RiceIllegalArgumentException;
    

    /**
     * Checks whether the given qualified permission is granted to the principal given
     * the passed roleQualification.  If no roleQualification is passed (null or empty)
     * then this method behaves the same as {@link #hasPermission(String, String, String)}.
     *
     * Each role assigned to the principal is checked for qualifications.  If a qualifier
     * exists on the principal's membership in that role, that is checked first through
     * the role's type service.  Once it is determined that the principal has the role
     * in the given context (qualification), the permissions are examined.
     *
     * Each permission is checked against the permissionDetails.  The PermissionTypeService
     * is called for each permission with the given permissionName to see if the
     * permissionDetails matches its details.
     *
     * @param principalId the principal id to check.  cannot be null or blank.
     * @param namespaceCode the namespace code.  cannot be null or blank.
     * @param permissionTemplateName the permission name. cannot be null or blank.
     * @param permissionDetails the permission details
     * @param qualification the permission qualifications
     * @return true is principal has permission
     * @throws RiceIllegalArgumentException if the principalId, namespaceCode, permissionName is null or blank
     */
    @WebMethod(operationName = "isAuthorizedByTemplate")
    @WebResult(name = "isAuthorized")
    boolean isAuthorizedByTemplate(@WebParam(name = "principalId") String principalId,
                                   @WebParam(name = "namespaceCode") String namespaceCode,
                                   @WebParam(name = "permissionTemplateName") String permissionTemplateName,
                                   @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                                   @WebParam(name = "permissionDetails") Map<String, String> permissionDetails,
                                   @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                                   @WebParam(name = "qualification") Map<String, String> qualification)
            throws RiceIllegalArgumentException;
    
    
    /**
     * Get the list of principals/groups who have a given permission.  This also returns delegates
     * for the given principals/groups who also have this permission given the context in the
     * qualification parameter.
     * 
     * Each role assigned to the principal is checked for qualifications.  If a qualifier 
     * exists on the principal's membership in that role, that is checked first through
     * the role's type service.  Once it is determined that the principal has the role
     * in the given context (qualification), the permissions are examined.
     *
     * @param namespaceCode the namespace code.  cannot be null or blank.
     * @param permissionName the permission name. cannot be null or blank.
     * @param qualification the permission qualifications
     * @return list of assignees that have been assigned the permissions
     * @throws RiceIllegalArgumentException if the principalId, namespaceCode, permissionName is null or blank
     */
	@WebMethod(operationName = "getPermissionAssignees")
    @XmlElementWrapper(name = "assignees", required = true)
    @XmlElement(name = "assignee", required = false)
    @WebResult(name = "assignees")
    List<Assignee> getPermissionAssignees( @WebParam(name="namespaceCode") String namespaceCode,
    									   @WebParam(name="permissionName") String permissionName,
                                           @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
    									   @WebParam(name="qualification") Map<String, String> qualification )
            throws RiceIllegalArgumentException;

    /**
     * Get the list of principals/groups who have a given permission that match the given 
     * permission template and permission details.  This also returns delegates
     * for the given principals/groups who also have this permission given the context in the
     * qualification parameter.
     * 
     * Each role assigned to the principal is checked for qualifications.  If a qualifier 
     * exists on the principal's membership in that role, that is checked first through
     * the role's type service.  Once it is determined that the principal has the role
     * in the given context (qualification), the permissions are examined.
     *
     * @param namespaceCode the namespace code.  cannot be null or blank.
     * @param permissionTemplateName the permission name. cannot be null or blank.
     * @param permissionDetails the permission details.
     * @param qualification the permission qualifications
     * @return list of assignees that have been assigned the permissions by template
     * @throws RiceIllegalArgumentException if the principalId, namespaceCode, permissionName is null or blank
     */
	@WebMethod(operationName = "getPermissionAssigneesByTemplate")
    @XmlElementWrapper(name = "assignees", required = true)
    @XmlElement(name = "assignee", required = false)
    @WebResult(name = "assignees")
    List<Assignee> getPermissionAssigneesByTemplate(@WebParam(name = "namespaceCode") String namespaceCode,
            @WebParam(name = "permissionTemplateName") String permissionTemplateName,
            @XmlJavaTypeAdapter(value = MapStringStringAdapter.class) @WebParam(
                    name = "permissionDetails") Map<String, String> permissionDetails,
            @XmlJavaTypeAdapter(value = MapStringStringAdapter.class) @WebParam(
                    name = "qualification") Map<String, String> qualification) throws RiceIllegalArgumentException;
    
    /**
     * Returns true if the given permission is defined on any Roles.
     *
     * @param namespaceCode the namespace code.  cannot be null or blank.
     * @param permissionName the permission name. cannot be null or blank.
     * @return true if given permission is defined on any Roles
     * @throws RiceIllegalArgumentException if the namespaceCode or permissionName is null or blank
     */
    @WebMethod(operationName = "isPermissionDefined")
    @WebResult(name = "isPermissionDefined")
    @Cacheable(value= Permission.Cache.NAME, key="'{isPermissionDefined}' + 'namespaceCode=' + #p0 + '|' + 'permissionName=' + #p1")
    boolean isPermissionDefined( @WebParam(name="namespaceCode") String namespaceCode,
    							 @WebParam(name="permissionName") String permissionName)
            throws RiceIllegalArgumentException;
    
    /**
     * Returns true if the given permission template is defined on any Roles.
     *
     * @param namespaceCode the namespace code.  cannot be null or blank.
     * @param permissionTemplateName the permission name. cannot be null or blank.
     * @param permissionDetails the permission template details
     * @return true if given permission template is defined on any Roles
     * @throws RiceIllegalArgumentException if the namespaceCode or permissionName is null or blank
     */
    @WebMethod(operationName = "isPermissionDefinedByTemplate")
    @WebResult(name = "isPermissionDefinedByTemplate")
    @Cacheable(value= Permission.Cache.NAME, key="'{isPermissionDefinedByTemplate}' + 'namespaceCode=' + #p0 + '|' + 'permissionTemplateName=' + #p1 + '|' + 'permissionDetails=' + T(org.kuali.rice.core.api.cache.CacheKeyUtils).mapKey(#p2)")
    boolean isPermissionDefinedByTemplate(@WebParam(name = "namespaceCode")
                                          String namespaceCode,
                                          @WebParam(name = "permissionTemplateName")
                                          String permissionTemplateName,
                                          @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                                          @WebParam(name = "permissionDetails")
                                          Map<String, String> permissionDetails)
            throws RiceIllegalArgumentException;
    
    /**
     * Returns permissions (with their details) that are granted to the principal given
     * the passed qualification.  If no qualification is passed (null or empty)
     * then this method does not check any qualifications on the roles.
     *
     * After the permissions are determined, the roles that hold those permissions are determined.
     * Each role that matches between the principal and the permission objects is checked for 
     * qualifications.  If a qualifier 
     * exists on the principal's membership in that role, that is checked through
     * the role's type service. 
     *
     * @param principalId the principal Id.  cannot be null or blank.
     * @param namespaceCode the namespace code.  cannot be null or blank.
     * @param permissionName the permission name. cannot be null or blank.
     * @param qualification the permission qualifications
     * @return list of permissions that are authorized with the given parameters
     * @throws RiceIllegalArgumentException if the principalId, namespaceCode or permissionName is null or blank
     */
	@WebMethod(operationName = "getAuthorizedPermissions")
    @XmlElementWrapper(name = "permissions", required = true)
    @XmlElement(name = "permission", required = false)
    @WebResult(name = "permissions")
    List<Permission> getAuthorizedPermissions( @WebParam(name="principalId") String principalId,
    										   @WebParam(name="namespaceCode") String namespaceCode,
    										   @WebParam(name="permissionName") String permissionName,
                                               @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
    										   @WebParam(name="qualification") Map<String, String> qualification )
            throws RiceIllegalArgumentException;

    /**
     * Returns permissions (with their details) that are granted to the principal given
     * the passed qualification.  If no qualification is passed (null or empty)
     * then this method does not check any qualifications on the roles.
     * 
     * All permissions with the given name are checked against the permissionDetails.  
     * The PermissionTypeService is called for each permission to see if the 
     * permissionDetails matches its details.
     * 
     * An asterisk (*) as a value in any permissionDetails key-value pair will match any value.
     * This forms a way to provide a wildcard to obtain multiple permissions in one call.
     * 
     * After the permissions are determined, the roles that hold those permissions are determined.
     * Each role that matches between the principal and the permission objects is checked for 
     * qualifications.  If a qualifier 
     * exists on the principal's membership in that role, that is checked through
     * the role's type service. 
     *
     * @param principalId the principal Id.  cannot be null or blank.
     * @param namespaceCode the namespace code.  cannot be null or blank.
     * @param permissionTemplateName the permission name. cannot be null or blank.
     * @param permissionDetails the permission template details.
     * @param qualification the permission qualifications
     * @return list of permissions that are authorized with the given parameters
     * @throws RiceIllegalArgumentException if the principalId, namespaceCode or permissionTemplateName is null or blank
     */
	@WebMethod(operationName = "getAuthorizedPermissionsByTemplate")
    @XmlElementWrapper(name = "permissions", required = true)
    @XmlElement(name = "permission", required = false)
    @WebResult(name = "permissions")
    List<Permission> getAuthorizedPermissionsByTemplate(@WebParam(name = "principalId") String principalId,
            @WebParam(name = "namespaceCode") String namespaceCode,
            @WebParam(name = "permissionTemplateName") String permissionTemplateName,
            @XmlJavaTypeAdapter(value = MapStringStringAdapter.class) @WebParam(
                    name = "permissionDetails") Map<String, String> permissionDetails,
            @XmlJavaTypeAdapter(value = MapStringStringAdapter.class) @WebParam(
                    name = "qualification") Map<String, String> qualification) throws RiceIllegalArgumentException;

    // --------------------
    // Permission Data
    // --------------------

    /**
     * Gets a {@link org.kuali.rice.kim.api.permission.Permission} from an id.
     *
     * <p>
     *   This method will return null if the permission does not exist.
     * </p>
     *
     * @param id the unique id to retrieve the permission by. cannot be null or blank.
     * @return a {@link org.kuali.rice.kim.api.permission.Permission} or null
     * @throws RiceIllegalArgumentException if the id is null or blank
     */
	@WebMethod(operationName = "getPermission")
    @WebResult(name = "permission")
    @Cacheable(value=Permission.Cache.NAME, key="'id=' + #p0")
    Permission getPermission( @WebParam(name="id") String id );

    /**
     * Gets a {@link org.kuali.rice.kim.api.permission.Permission} with the unique combination of namespace and name.
     *
     * <p>
     *   This method will return null if the permission does not exist.
     * </p>
     *
     * @param namespaceCode namespace code for permission. cannot be null or blank.
     * @param name name of permission.  cannot be null or blank.
     * @return a {@link org.kuali.rice.kim.api.permission.Permission} or null
     * @throws RiceIllegalArgumentException if the namespaceCode or name is null or blank
     */
    @WebMethod(operationName = "findPermByNamespaceCodeAndName")
    @WebResult(name = "permission")
    @Cacheable(value=Permission.Cache.NAME, key="'namespaceCode=' + #p0 + '|' + 'name=' + #p1")
    Permission findPermByNamespaceCodeAndName(
            @WebParam(name = "namespaceCode") String namespaceCode,
            @WebParam(name = "name") String name)
            throws RiceIllegalArgumentException;
   
	/** 
	 * Return the permissions for the given unique combination of namespace,
	 * component and permission template name.
     *
     * @param namespaceCode namespace code for permission. cannot be null or blank.
     * @param templateName name of permission template.  cannot be null or blank.
     * @return a list of {@link org.kuali.rice.kim.api.permission.Permission} or null
     * @throws RiceIllegalArgumentException if the namespaceCode or name is null or blank
	 */
	@WebMethod(operationName = "findPermissionsByTemplate")
    @XmlElementWrapper(name = "permissions", required = true)
    @XmlElement(name = "permission", required = false)
    @WebResult(name = "permissions")
    @Cacheable(value=Permission.Cache.NAME, key="'namespaceCode=' + #p1 + '|' + 'templateName=' + #p2")
    List<Permission> findPermissionsByTemplate(
            @WebParam(name = "namespaceCode") String namespaceCode,
            @WebParam(name = "templateName") String templateName)
            throws RiceIllegalArgumentException;

    /**
     * Gets a {@link Template} from an id.
     *
     * <p>
     *   This method will return null if the template does not exist.
     * </p>
     *
     * @param id the unique id to retrieve the template by. cannot be null or blank.
     * @return a {@link Template} or null
     * @throws RiceIllegalArgumentException if the id is null or blank
     */
	@WebMethod(operationName = "getPermissionTemplate")
    @WebResult(name = "id")
    @Cacheable(value=Template.Cache.NAME + "{Permission}", key="'id=' + #p0")
    Template getPermissionTemplate( @WebParam(name="id") String id ) throws RiceIllegalArgumentException;

    /**
     * Finds a {@link Template} for namespaceCode and name.
     *
     * @param namespaceCode the namespace code.  cannot be null or blank.
     * @param name the template name. cannot be null or blank.
     * @return a {@link Template} or null
     * @throws RiceIllegalArgumentException if the id or namespaceCode is null or blank
     */
	@WebMethod(operationName = "findPermTemplateByNamespaceCodeAndName")
    @WebResult(name = "permissionTemplate")
    @Cacheable(value=Template.Cache.NAME + "{Permission}", key="'namespaceCode=' + #p0 + '|' + 'name=' + #p1")
    Template findPermTemplateByNamespaceCodeAndName(
            @WebParam(name = "namespaceCode") String namespaceCode,
            @WebParam(name = "name") String name) throws RiceIllegalArgumentException;


    /**
     * Finds a {@link Template} for namespaceCode and name.
     *
     * @return a list of {@link Template} or an empty list if none found
     */
	@WebMethod(operationName = "getAllTemplates")
    @XmlElementWrapper(name = "templates", required = true)
    @XmlElement(name = "template", required = false)
    @WebResult(name = "templates")
    @Cacheable(value=Template.Cache.NAME + "{Permission}", key="'all'")
    List<Template> getAllTemplates();

    /**
     * Get the role IDs for the given permission.
     *
     * @param namespaceCode the permission namespace code.  cannot be null or blank.
     * @param permissionName the permission name. cannot be null or blank.
     * @return a list of role Ids, or an empty list if none found
     * @throws RiceIllegalArgumentException if the namespaceCode or permissionName is null or blank
     */
	@WebMethod(operationName = "getRoleIdsForPermission")
    @XmlElementWrapper(name = "roleIds", required = true)
    @XmlElement(name = "roleId", required = false)
    @WebResult(name = "roleIds")
	@Cacheable(value=Permission.Cache.NAME, key="'{RoleIds}namespaceCode=' + #p0 + '|' + 'name=' + #p1")
    List<String> getRoleIdsForPermission( @WebParam(name="namespaceCode") String namespaceCode,
    									  @WebParam(name="permissionName") String permissionName) throws RiceIllegalArgumentException;

    /**
     * This method find Permissions based on a query criteria.  The criteria cannot be null.
     *
     * @param queryByCriteria the criteria.  Cannot be null.
     * @return query results.  will never return null.
     * @throws RiceIllegalArgumentException if the queryByCriteria is null
     */
    @WebMethod(operationName = "findPermissions")
    @WebResult(name = "results")
    PermissionQueryResults findPermissions(@WebParam(name = "query") QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException;


    /**
     * This method find Permission Templates based on a query criteria.  The criteria cannot be null.
     *
     * @param queryByCriteria the criteria.  Cannot be null.
     * @return query results.  will never return null.
     * @throws RiceIllegalArgumentException if the queryByCriteria is null
     */
    @WebMethod(operationName = "findPermissionTemplates")
    @WebResult(name = "results")
    TemplateQueryResults findPermissionTemplates(@WebParam(name = "query") QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException;
}
