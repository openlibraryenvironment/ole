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
package org.kuali.rice.kew.doctype.service;

import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;

import java.util.List;

/**
 * Implements permission checks related to Document Type.  In general,
 * these permission checks are invoked from the various actions
 * which require authorization.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DocumentTypePermissionService {

	/**
	 * Determines if the given principal is authorized to receive ad hoc requests
	 * for the given DocumentType and action request type.
	 */
	boolean canReceiveAdHocRequest(String principalId, DocumentRouteHeaderValue document, String actionRequestCode);

	/**
	 * Determines if the given group is authorized to receive ad hoc requests of the
	 * specified action request code for the given DocumentType and action request type.  
	 * A group is considered to be authorized to receive an ad hoc request if all
	 * of it's members can receive the request.
	 */
	boolean canGroupReceiveAdHocRequest(String groupId, DocumentRouteHeaderValue document, String actionRequestCode);
	
	/**
	 * Determines if the given principal can administer routing for the given
	 * DocumentType.  Having this permission gives them "super user" capabilities.
	 */
	boolean canAdministerRouting(String principalId, DocumentType documentType);

    /**
     * Determines if the given principal can super user approve a single action
     * request for a given DocumentType, route node, and routeStatusCode
     */
    boolean canSuperUserApproveSingleActionRequest(String principalId, DocumentType documentType,
            List<RouteNodeInstance> routeNodeInstances, String routeStatusCode);

    /**
     * Determines if the given principal can super user approve a document
     * for a given DocumentType, route node, and routeStatusCode
     */
    boolean canSuperUserApproveDocument(String principalId, DocumentType documentType,
            List<RouteNodeInstance> routeNodeInstances, String routeStatusCode);

    /**
     * Determines if the given principal can super user disapprove a document
     * for a given DocumentType, route node, and routeStatusCode
     */
    boolean canSuperUserDisapproveDocument(String principalId, DocumentType documentType,
            List<RouteNodeInstance> routeNodeInstances, String routeStatusCode);

    /**
	 * Determines if the given principal can initiate documents of the given DocumentType.
	 */
	boolean canInitiate(String principalId, DocumentType documentType);
	
	/**
	 * Determines if the given principal can route documents of the given DocumentRouteHeaderValue.  The permission check
	 * also considers the document status and initiator of the document.
	 */
	boolean canRoute(String principalId, DocumentRouteHeaderValue documentRouteHeaderValue);
	
	/**
	 * Determines if the given principal can save documents of the given DocumentType.  The permission check
	 * also considers the document's current route nodes, document status, and initiator of the document.
	 * 
	 * <p>It is intended the only one of the given route nodes will need to satisfy the permission check.
	 * For example, if the save permission is defined for node 1 but not for node 2, then a document which
	 * is at both node 1 and node 2 should satisfy the permission check.
	 */
	boolean canSave(String principalId, DocumentRouteHeaderValue document);

	/**
	 * Determines if the given principal can blanket approve documents of the given DocumentType.  The permission check
	 * also considers the document status and the initiator of the document.
	 */
	boolean canBlanketApprove(String principalId, DocumentRouteHeaderValue document);

	/**
	 * Determines if the given principal can cancel documents of the given DocumentType.  The permission check
	 * also considers the document's current route nodes, document status, and initiator of the document.
	 * 
	 * <p>It is intended the only one of the given route nodes will need to satisfy the permission check.
	 * For example, if the cancel permission is defined for node 1 but not for node 2, then a document which
	 * is at both node 1 and node 2 should satisfy the permission check.
	 */
	boolean canCancel(String principalId, DocumentRouteHeaderValue document);
	
	/**
	 * Determines if the given principal can add route log messages for documents of the given DocumentRouteHeaderValue.  The permission check
	 * also considers the document status and initiator of the document.
	 */
	boolean canAddRouteLogMessage(String principalId, DocumentRouteHeaderValue documentRouteHeaderValue);

    /**
     * Determines if the given principal can recall the specified document given the permission details.
     * @since 2.1
     */
    boolean canRecall(String principalId, DocumentRouteHeaderValue document);

}
