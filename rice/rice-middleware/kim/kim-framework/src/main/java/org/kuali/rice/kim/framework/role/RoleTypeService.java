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
package org.kuali.rice.kim.framework.role;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.role.RoleMembership;
import org.kuali.rice.kim.framework.type.KimTypeService;

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
 * A {@link KimTypeService} with specific methods for Roles.
 */
@WebService(name = "roleTypeService", targetNamespace = KimConstants.Namespaces.KIM_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface RoleTypeService extends KimTypeService {

    /**
     * Gets whether a role assignment with the given qualifier is applicable for the given qualification.
     * 
     * For example, the qualifier for a role could be as follows:
     *   chartOfAccountsCode = BL
     *   organizationCode = ARSC
     *   descendsHierarchy = true
     *   
     * The qualification could be:
     *   chartOfAccountsCode = BL
     *   organizationCode = PSY    (reports to BL-ARSC)
     *   
     * This method would return true for this set of arguments.  This would require a query of 
     * the KFS organization hierarchy, so an implementation of this sort must be done by
     * a service which lives within KFS and will be called remotely by KIM.
     * 
     * The contents of the passed in attribute sets should not be modified as they may be used in future calls by
     * the role service.
     *
     * @param qualification the qualification.  cannot be null.
     * @param roleQualifier the role qualifier. cannot be null.
     * @return true if the qualifications match
     * @throws RiceIllegalArgumentException if the qualification or roleQualifier is null
     */
    @WebMethod(operationName="doesRoleQualifierMatchQualification")
    @WebResult(name = "match")
    boolean doesRoleQualifierMatchQualification(@WebParam(name = "qualification")
                                                @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                                                Map<String, String> qualification,
                                                @WebParam(name = "roleQualifier")
                                                @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                                                Map<String, String> roleQualifier) throws RiceIllegalArgumentException;

    /**
     * Gets whether a role membership with the given details is applicable for the given qualification.
     *
     * @param qualification the qualification.  cannot be null.
     * @param roleMemberships the list of roleMemberships to check for matches. cannot be null.
     * @return an immutable list of matched roleMemberships.  will not return null.
     * @throws RiceIllegalArgumentException if the qualification or roleMemberships is null.
     */
    @WebMethod(operationName="getMatchingRoleMemberships")
    @XmlElementWrapper(name = "roleMemberships", required = true)
    @XmlElement(name = "roleMembership", required = false)
    @WebResult(name = "roleMemberships")
    List<RoleMembership> getMatchingRoleMemberships(@WebParam(name = "qualification")
                                                    @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                                                    Map<String, String> qualification,
                                                    @WebParam(name = "roleMemberships")
                                                    List<RoleMembership> roleMemberships) throws RiceIllegalArgumentException;

    /**
     * Returns true if this role type represents a "derived" role type.  That is, the members of the 
     * role are known to the host application, not to KIM.  This is needed for cases like the KFS
     * Fiscal Officer, where the members of the role are in the Account table in the KFS database.
     *
     * @return true if derived type
     */
    @WebMethod(operationName="isDerivedRoleType")
    @WebResult(name = "derivedRoleType")
    boolean isDerivedRoleType();

    /**
     * This method can be used to check if the given principal has this derived role.  It is designed to be used in case
     * there is a more efficient way to check for whether a principal is in a role rather than retrieving all the
     * members of the role and checking against that.
     * 
     * The groupIds parameter is intended to be the complete list of groups to which the principal belongs.  If either the
     * principalId or the groupIds parameters are blank/empty, that parameter should be ignored.
     *
     * @param principalId the principalId. cannot be null or blank.
     * @param groupIds the groupIds the principal is a member of. cannot be null.
     * @param namespaceCode the namespace code the role is in. cannot be blank or null.
     * @param roleName the name of the role.  cannot be blank or null.
     * @param qualification the qualification.  cannot be null.
     * @return if the principal has a derived role.
     * @throws RiceIllegalArgumentException if the principalId, namespaceCode, roleName is blank or null.
     * @throws RiceIllegalArgumentException if the groupIds, qualification is null.
     */
    @WebMethod(operationName="hasDerivedRole")
    @WebResult(name = "derivedRole")
    boolean hasDerivedRole( @WebParam(name = "principalId")
                                String principalId,
                                @WebParam(name = "groupIds")
                                List<String> groupIds,
                                @WebParam(name = "namespaceCode")
                                String namespaceCode,
                                @WebParam(name = "roleName")
                                String roleName,
                                @WebParam(name = "qualification")
                                @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                                Map<String, String> qualification ) throws RiceIllegalArgumentException;

    @WebMethod(operationName = "getRoleMembersFromDerivedRole")
    @XmlElementWrapper(name = "roleMemberships", required = true)
    @XmlElement(name = "roleMembership", required = false)
    @WebResult(name = "roleMemberships")
    List<RoleMembership> getRoleMembersFromDerivedRole(
            @WebParam(name = "namespaceCode") String namespaceCode,
            @WebParam(name = "roleName") String roleName,
            @WebParam(name = "qualification")
            @XmlJavaTypeAdapter(value = MapStringStringAdapter.class) Map<String, String> qualification
    ) throws RiceIllegalArgumentException;

    /**
     * For roles where the order of members returned may be meaningful,
     * this method provides a hook to sort the results before they
     * are returned from getRoleMembers on the RoleService.
     *
     * This method may alter the passed in list directly and return it rather than
     * allocating a new list.
     * 
     * This is also the place where the roleSortingCode property on the RoleMembershipInfo objects can be
     * populated in preparation for routing if not all members of this role should be group as separate
     * units for routing.
     * 
     * @param roleMemberships the list of roleMemberships to check for matches. cannot be null.
     * @return an immutable list of matched roleMemberships.  will not return null.
     * @throws RiceIllegalArgumentException if the roleMemberships is null.
     */
    @WebMethod(operationName="sortRoleMembers")
    @XmlElementWrapper(name = "roleMemberships", required = true)
    @XmlElement(name = "roleMembership", required = false)
    @WebResult(name = "roleMemberships")
    List<RoleMembership> sortRoleMembers( @WebParam(name = "roleMemberships")
                                          List<RoleMembership> roleMembers ) throws RiceIllegalArgumentException;
    
    /**
     * Takes the passed in qualifications and converts them, if necessary, for any downstream roles which may be present.
     *
     * @deprecated use convertQualificationForMemberRolesAndMemberAttributes
     * @param namespaceCode the namespace code the role is in. cannot be blank or null.
     * @param roleName the name of the role.  cannot be blank or null.
     * @param memberRoleNamespaceCode the namespace code the member role is in. cannot be blank or null.
     * @param memberRoleName the name of the member role.  cannot be blank or null.
     * @param qualification the qualification.  cannot be null.
     * @return an immutable map of qualifiers. Will never return null.
     * @throws RiceIllegalArgumentException if the namespaceCode, roleName, memberRoleNamespaceCode, memberRoleName is blank or null.
     * @throws RiceIllegalArgumentException if the qualification is null.
     */
    @Deprecated
    @WebMethod(operationName="convertQualificationForMemberRoles")
    @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
    @WebResult(name = "qualification")
    Map<String, String> convertQualificationForMemberRoles( @WebParam(name = "namespaceCode")
                                                            String namespaceCode,
                                                            @WebParam(name = "roleName")
                                                            String roleName,
                                                            @WebParam(name = "memberRoleNamespaceCode")
                                                            String memberRoleNamespaceCode,
                                                            @WebParam(name = "memberRoleName")
                                                            String memberRoleName,
                                                            @WebParam(name = "qualification")
                                                            @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                                                            Map<String, String> qualification ) throws RiceIllegalArgumentException;

    /**
     * Takes the passed in qualifications and converts them, if necessary, for any downstream roles which may be present.
     *
     * @since 2.3.4
     * @param namespaceCode the namespace code the role is in. cannot be blank or null.
     * @param roleName the name of the role.  cannot be blank or null.
     * @param memberRoleNamespaceCode the namespace code the member role is in. cannot be blank or null.
     * @param memberRoleName the name of the member role.  cannot be blank or null.
     * @param qualification the qualification.  cannot be null.
     * @param memberQualification the attributes defined for the memberRole
     * @return an immutable map of qualifiers. Will never return null.
     * @throws RiceIllegalArgumentException if the namespaceCode, roleName, memberRoleNamespaceCode, memberRoleName is blank or null.
     * @throws RiceIllegalArgumentException if the qualification is null.
     */
    @WebMethod(operationName="convertQualificationForMemberRolesAndMemberAttributes")
    @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
    @WebResult(name = "qualification")
    Map<String, String> convertQualificationForMemberRolesAndMemberAttributes( @WebParam(name = "namespaceCode")
    String namespaceCode,
            @WebParam(name = "roleName")
            String roleName,
            @WebParam(name = "memberRoleNamespaceCode")
            String memberRoleNamespaceCode,
            @WebParam(name = "memberRoleName")
            String memberRoleName,
            @WebParam(name = "qualification")
            @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
            Map<String, String> qualification,
            @WebParam(name = "memberQualification")
            @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
            Map<String, String> memberQualification) throws RiceIllegalArgumentException;
    
    /**
     * Determines if the role specified by the given namespace and role name has a dynamic role membership.
     *
     * A dynamic role membership means that a role membership may be changed over time and cannot be safely cached.
     * 
     * @param namespaceCode the namespace code of the role. cannot be null or blank
     * @param roleName the name of the role. cannot be null or blank.
     * @return true if the membership results of the Role are dynamic, false otherwise
     * @throws IllegalArgumentException if the namespaceCode, roleName is blank or null.
     */
    @WebMethod(operationName="dynamicRoleMembership")
    @WebResult(name = "dynamic")
    boolean dynamicRoleMembership(@WebParam(name = "namespaceCode") String namespaceCode, @WebParam(name = "roleName") String roleName) throws RiceIllegalArgumentException;
    
    /**
     * Roles whose memberships may be matched exactly by qualifiers,
     * this method returns the list of such qualifier names.
     * 
     * @return immutable list of qualifier names that can be used for exact match.  Will never return null.
     */
    @WebMethod(operationName="getQualifiersForExactMatch")
    @XmlElementWrapper(name = "names", required = true)
    @XmlElement(name = "name", required = false)
    @WebResult(name = "names")
    List<String> getQualifiersForExactMatch();

    /**
     * Returns whether a membertype should have its qualifiers validated
     * @return true if
     * @since 2.1.2
     */
    @WebMethod(operationName="shouldvalidateQualifiersForMemberType")
    @WebResult(name="validateQualifiers")
    boolean shouldValidateQualifiersForMemberType(@WebParam(name="memberType") MemberType memberType);
}
