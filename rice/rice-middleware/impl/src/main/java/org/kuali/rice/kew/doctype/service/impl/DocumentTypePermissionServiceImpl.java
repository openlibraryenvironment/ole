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
package org.kuali.rice.kew.doctype.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.service.DocumentTypePermissionService;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import java.util.*;

/**
 * Implementation of the DocumentTypePermissionService. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentTypePermissionServiceImpl extends DocumentActionsPermissionBase implements DocumentTypePermissionService {
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DocumentTypePermissionServiceImpl.class);

    @Override
    public boolean canReceiveAdHocRequest(String principalId, DocumentRouteHeaderValue document, String actionRequestType) {
        validatePrincipalId(principalId);
        validateDocument(document);
        DocumentType documentType = document.getDocumentType();
        validateDocumentType(documentType);
        validateActionRequestType(actionRequestType);
        final Boolean result;

        Map<String, String> permissionDetails = buildDocumentTypePermissionDetails(documentType, null, actionRequestType, null);
        if (useKimPermission(KewApiConstants.KEW_NAMESPACE, KewApiConstants.AD_HOC_REVIEW_PERMISSION, permissionDetails, true)) {
            result = getPermissionService().isAuthorizedByTemplate(principalId, KewApiConstants.KEW_NAMESPACE,
                    KewApiConstants.AD_HOC_REVIEW_PERMISSION, permissionDetails, new HashMap<String, String>());
        } else {
            result = Boolean.TRUE;
        }
        return result;
    }

    @Override
    public boolean canGroupReceiveAdHocRequest(String groupId, DocumentRouteHeaderValue document, String actionRequestType) {
        validateGroupId(groupId);
        validateDocument(document);
        DocumentType documentType = document.getDocumentType();
        validateDocumentType(documentType);
        validateActionRequestType(actionRequestType);

        Boolean result = Boolean.TRUE;
        Map<String, String> permissionDetails = buildDocumentTypePermissionDetails(documentType, null, actionRequestType, null);
        if (useKimPermission(KewApiConstants.KEW_NAMESPACE, KewApiConstants.AD_HOC_REVIEW_PERMISSION, permissionDetails, true)) {
            List<String> principalIds = getGroupService().getMemberPrincipalIds(groupId);
            // if any member of the group is not allowed to receive the request, then the group may not receive it
            for (String principalId : principalIds) {
                if (!getPermissionService().isAuthorizedByTemplate(principalId, KewApiConstants.KEW_NAMESPACE,
                        KewApiConstants.AD_HOC_REVIEW_PERMISSION, permissionDetails, new HashMap<String, String>())) {
                    result = Boolean.FALSE;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public boolean canAdministerRouting(String principalId, DocumentType documentType) {
        validatePrincipalId(principalId);
        validateDocumentType(documentType);

        final Boolean result;
        if (documentType.isSuperUserGroupDefined()) {
            result = documentType.isSuperUser(principalId);
        } else {
            Map<String, String> permissionDetails = buildDocumentTypePermissionDetails(documentType, null, null, null);
            result = getPermissionService().isAuthorizedByTemplate(principalId, KewApiConstants.KEW_NAMESPACE,
                    KewApiConstants.ADMINISTER_ROUTING_PERMISSION, permissionDetails, new HashMap<String, String>());
        }
        return result;
    }

    @Override
    public boolean canSuperUserApproveSingleActionRequest(String principalId, DocumentType documentType,
                                                          List<RouteNodeInstance> routeNodeInstances, String routeStatusCode) {
        return canSuperUserApproveSingleActionRequest(principalId, documentType, toRouteNodeNames(routeNodeInstances), routeStatusCode);
    }

    @Override
    public boolean canSuperUserApproveDocument(String principalId, DocumentType documentType,
                                               List<RouteNodeInstance> routeNodeInstances, String routeStatusCode) {
        return canSuperUserApproveDocument(principalId, documentType, toRouteNodeNames(routeNodeInstances), routeStatusCode);
    }

    @Override
    public boolean canSuperUserDisapproveDocument(String principalId, DocumentType documentType,
                                                  List<RouteNodeInstance> routeNodeInstances,String routeStatusCode) {
        return canSuperUserDisapproveDocument(principalId, documentType, toRouteNodeNames(routeNodeInstances), routeStatusCode);
    }

    @Override
    public boolean canAddRouteLogMessage(String principalId, DocumentRouteHeaderValue document) {
        validatePrincipalId(principalId);
        validateDocument(document);
        String documentId = document.getDocumentId();
        DocumentType documentType = document.getDocumentType();
        String documentStatus = document.getDocRouteStatus();
        String initiatorPrincipalId = document.getInitiatorWorkflowId();
        validateDocumentType(documentType);
        validateDocumentStatus(documentStatus);
        validatePrincipalId(initiatorPrincipalId);

        Map<String, String> permissionDetails = buildDocumentTypePermissionDetails(documentType, documentStatus, null, null);
        Map<String, String> roleQualifiers = buildDocumentRoleQualifiers(document, permissionDetails.get(KewApiConstants.ROUTE_NODE_NAME_DETAIL));

        if (LOG.isDebugEnabled()) {
            LOG.debug("Permission details values: " + permissionDetails);
            LOG.debug("Role qualifiers values: " + roleQualifiers);
        }

        if (useKimPermission(KewApiConstants.KEW_NAMESPACE, KewApiConstants.ADD_MESSAGE_TO_ROUTE_LOG, permissionDetails, false)) {
            return getPermissionService().isAuthorizedByTemplate(principalId, KewApiConstants.KEW_NAMESPACE,
                    KewApiConstants.ADD_MESSAGE_TO_ROUTE_LOG, permissionDetails, roleQualifiers);
        }

        return false;
    }

    /**
     * Converts list of RouteNodeInstance objects to a list of the route node names
     * @param routeNodeInstances the list RouteNodeInstance objects, may be null
     * @return non-null, possibly empty, Collection of routenode names
     */
    protected Collection<String> toRouteNodeNames(Collection<RouteNodeInstance> routeNodeInstances) {
        if (routeNodeInstances != null) {
            return CollectionUtils.collect(routeNodeInstances, new Transformer() {
                @Override
                public Object transform(Object input) {
                    return ((RouteNodeInstance) input).getName();
                }
            });
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * Validates groupId parameter
     * @param groupId the group id
     * @throw IllegalArgumentException if groupId is empty or null
     */
    private void validateGroupId(String groupId) {
        if (StringUtils.isBlank(groupId)) {
            throw new IllegalArgumentException("Invalid group ID, value was empty");
        }
    }

    /**
     * Validates actionRequestType parameter
     * @param actionRequestType the actionRequest type
     * @throw IllegalArgumentException if the actionRequest type is empty or null, or an invalid value
     */
    private void validateActionRequestType(String actionRequestType) {
        if (StringUtils.isBlank(actionRequestType)) {
            throw new IllegalArgumentException("Invalid action request type, value was empty");
        }
        if (!KewApiConstants.ACTION_REQUEST_CODES.containsKey(actionRequestType)) {
            throw new IllegalArgumentException("Invalid action request type was given, value was: " + actionRequestType);
        }
    }

    // convenience method to look up KIM GroupService
    protected GroupService getGroupService() {
        return KimApiServiceLocator.getGroupService();
    }
}
