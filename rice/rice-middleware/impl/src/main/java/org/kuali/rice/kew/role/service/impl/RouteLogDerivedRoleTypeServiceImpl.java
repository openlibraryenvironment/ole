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
package org.kuali.rice.kew.role.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.document.WorkflowDocumentService;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.role.RoleMembership;
import org.kuali.rice.kim.framework.common.delegate.DelegationTypeService;
import org.kuali.rice.kim.framework.role.RoleTypeService;
import org.kuali.rice.kns.kim.role.DerivedRoleTypeServiceBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class RouteLogDerivedRoleTypeServiceImpl extends DerivedRoleTypeServiceBase implements RoleTypeService, DelegationTypeService {
    public static final String INITIATOR_ROLE_NAME = "Initiator";
    public static final String INITIATOR_OR_REVIEWER_ROLE_NAME = "Initiator or Reviewer";
    public static final String ROUTER_ROLE_NAME = "Router";

    @Override
    protected boolean isCheckRequiredAttributes() {
        return true;
    }
	
	/**
	 *	- qualifier is document number
	 *	- the roles that will be of this type are KR-WKFLW Initiator and KR-WKFLW Initiator or Reviewer, KR-WKFLW Router
	 *	- only the initiator of the document in question gets the KR-WKFLW Initiator role
	 *	- user who routed the document according to the route log should get the KR-WKFLW Router role
	 *	- users who are authorized by the route log, 
	 *		i.e. initiators, people who have taken action, people with a pending action request, 
	 *		or people who will receive an action request for the document in question get the KR-WKFLW Initiator or Reviewer Role 
	 *
	 */
	@Override
    public List<RoleMembership> getRoleMembersFromDerivedRole(String namespaceCode, String roleName, Map<String, String> qualification) {
	    if (StringUtils.isBlank(namespaceCode)) {
            throw new RiceIllegalArgumentException("namespaceCode was null or blank");
        }

        if (roleName == null) {
            throw new RiceIllegalArgumentException("roleName was null");
        }

        validateRequiredAttributesAgainstReceived(qualification);
		List<RoleMembership> members = new ArrayList<RoleMembership>();
		if(qualification!=null && !qualification.isEmpty()){
			String documentId = qualification.get(KimConstants.AttributeConstants.DOCUMENT_NUMBER);
			if (StringUtils.isNotBlank(documentId)) {
				try{
                    WorkflowDocumentService workflowDocumentService = KewApiServiceLocator.getWorkflowDocumentService();
					if (INITIATOR_ROLE_NAME.equals(roleName)) {
					    String principalId = KewApiServiceLocator.getWorkflowDocumentService().getDocumentInitiatorPrincipalId(
                                documentId);
                        RoleMembership roleMembership = RoleMembership.Builder.create(null,null,principalId, MemberType.PRINCIPAL, null).build();
	                    members.add(roleMembership);
					} else if(INITIATOR_OR_REVIEWER_ROLE_NAME.equals(roleName)) {
					    List<String> ids = KewApiServiceLocator.getWorkflowDocumentActionsService().getPrincipalIdsInRouteLog(
                                documentId, true);
					    for ( String id : ids ) {
					    	if ( StringUtils.isNotBlank(id) ) {
                                RoleMembership roleMembership = RoleMembership.Builder.create(null,null,id, MemberType.PRINCIPAL, null).build();
					    		members.add(roleMembership );
					    	}
					    }
					} else if(ROUTER_ROLE_NAME.equals(roleName)) {
                        String principalId = workflowDocumentService.getRoutedByPrincipalIdByDocumentId(documentId);
                        RoleMembership roleMembership = RoleMembership.Builder.create(null,null,principalId, MemberType.PRINCIPAL, null).build();
	                    members.add(roleMembership);
					}
				} catch(Exception wex){
					throw new RuntimeException(
					"Error in getting principal Ids in route log for document id: "+ documentId +" :"+wex.getLocalizedMessage(),wex);
				}
			}
		}

		return members;
	}

	@Override
	public boolean hasDerivedRole(
			String principalId, List<String> groupIds, String namespaceCode, String roleName, Map<String, String> qualification){
		if (StringUtils.isBlank(principalId)) {
            throw new RiceIllegalArgumentException("principalId was null or blank");
        }

        if (groupIds == null) {
            throw new RiceIllegalArgumentException("groupIds was null or blank");
        }

        if (StringUtils.isBlank(namespaceCode)) {
            throw new RiceIllegalArgumentException("namespaceCode was null or blank");
        }

        if (StringUtils.isBlank(roleName)) {
            throw new RiceIllegalArgumentException("roleName was null or blank");
        }

        if (qualification == null) {
            throw new RiceIllegalArgumentException("qualification was null");
        }


        validateRequiredAttributesAgainstReceived(qualification);
        boolean isUserInRouteLog = false;
		if(qualification!=null && !qualification.isEmpty()){
			String documentId = qualification.get(KimConstants.AttributeConstants.DOCUMENT_NUMBER);
            WorkflowDocumentService workflowDocumentService = KewApiServiceLocator.getWorkflowDocumentService();
			try {
				if (INITIATOR_ROLE_NAME.equals(roleName)){
					isUserInRouteLog = principalId.equals(workflowDocumentService.getDocumentInitiatorPrincipalId(documentId));
				} else if(INITIATOR_OR_REVIEWER_ROLE_NAME.equals(roleName)){
					isUserInRouteLog = KewApiServiceLocator.getWorkflowDocumentActionsService().isUserInRouteLog(
                            documentId, principalId, true);
				} else if(ROUTER_ROLE_NAME.equals(roleName)){
					isUserInRouteLog = principalId.equals(workflowDocumentService.getRoutedByPrincipalIdByDocumentId(
                            documentId));
				}
			} catch (Exception wex) {
				throw new RuntimeException("Error in determining whether the principal Id: " + principalId + " is in route log " +
						"for document id: " + documentId + " :"+wex.getLocalizedMessage(),wex);
			}
		}
		return isUserInRouteLog;
	}

	/**
     * Determines if the role specified by the given namespace and role name has a dynamic role membership.
	 * Returns true, as the Route Log changes often enough that role membership is highly volatile
	 * 
	 * @see RoleTypeService#dynamicRoleMembership(String, String) dynamicRoleMembership(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean dynamicRoleMembership(String namespaceCode, String roleName) {
		if (StringUtils.isBlank(namespaceCode)) {
            throw new RiceIllegalArgumentException("namespaceCode was null or blank");
        }

	    if (StringUtils.isBlank(roleName)) {
            throw new RiceIllegalArgumentException("roleName was null or blank");
        }

        return true;
	}
}
