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
package org.kuali.rice.kew.routeheader.service;

import java.util.List;
import java.util.Set;

import org.kuali.rice.kew.actions.ActionTakenEvent;
import org.kuali.rice.kew.api.action.ActionInvocation;
import org.kuali.rice.kew.api.action.AdHocRevoke;
import org.kuali.rice.kew.api.action.MovePoint;
import org.kuali.rice.kew.api.action.WorkflowDocumentActionsService;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;


/**
 * Service for initiating actions against documents.  Uses from the service endpoint
 * for the client API.
 *
 * @see WorkflowDocumentActionsService
 * @see ActionTakenEvent
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface WorkflowDocumentService {

    public DocumentRouteHeaderValue acknowledgeDocument(String principalId, DocumentRouteHeaderValue routeHeader, String annotation) throws InvalidActionTakenException;
    public DocumentRouteHeaderValue approveDocument(String principalId, DocumentRouteHeaderValue routeHeader, String annotation) throws InvalidActionTakenException;
    public DocumentRouteHeaderValue adHocRouteDocumentToPrincipal(String principalId, DocumentRouteHeaderValue routeHeader, String actionRequested, String routeMethodName, Integer priority, String annotation, String targetPrincipalId, String responsibilityDesc, Boolean forceAction, String requestLabel) throws WorkflowException;
    public DocumentRouteHeaderValue adHocRouteDocumentToGroup(String principalId, DocumentRouteHeaderValue routeHeader, String actionRequested, String routeMethodName, Integer priority, String annotation, String groupId, String responsibilityDesc, Boolean forceAction, String requestLabel) throws WorkflowException;
    public DocumentRouteHeaderValue cancelDocument(String principalId, DocumentRouteHeaderValue routeHeader, String annotation) throws InvalidActionTakenException;
    public DocumentRouteHeaderValue clearFYIDocument(String principalId, DocumentRouteHeaderValue routeHeader, String annotation) throws InvalidActionTakenException;
    public DocumentRouteHeaderValue completeDocument(String principalId, DocumentRouteHeaderValue routeHeader, String annotation) throws InvalidActionTakenException;
    public DocumentRouteHeaderValue createDocument(String principalId, DocumentRouteHeaderValue routeHeader) throws WorkflowException;
    public DocumentRouteHeaderValue disapproveDocument(String principalId, DocumentRouteHeaderValue routeHeader, String annotation) throws InvalidActionTakenException;

    public DocumentRouteHeaderValue routeDocument(String principalId, DocumentRouteHeaderValue routeHeader, String annotation) throws WorkflowException, InvalidActionTakenException;
    public DocumentRouteHeaderValue saveRoutingData(String principalId, DocumentRouteHeaderValue routeHeader);
    public DocumentRouteHeaderValue saveDocument(String principalId, DocumentRouteHeaderValue routeHeader, String annotation) throws InvalidActionTakenException;
    public void deleteDocument(String principalId, DocumentRouteHeaderValue routeHeader) throws WorkflowException;
    public void logDocumentAction(String principalId, DocumentRouteHeaderValue routeHeader, String annotation) throws InvalidActionTakenException;

    public DocumentRouteHeaderValue superUserActionRequestApproveAction(String principalId, DocumentRouteHeaderValue routeHeader, String actionRequestId, String annotation, boolean runPostProcessor) throws InvalidActionTakenException;
    public DocumentRouteHeaderValue superUserActionRequestApproveAction(String principalId, String documentId, String actionRequestId, String annotation, boolean runPostProcessor) throws InvalidActionTakenException;
    public DocumentRouteHeaderValue superUserApprove(String principalId, DocumentRouteHeaderValue routeHeader, String annotation, boolean runPostProcessor) throws InvalidActionTakenException;
    public DocumentRouteHeaderValue superUserCancelAction(String principalId, DocumentRouteHeaderValue routeHeader, String annotation, boolean runPostProcessor) throws InvalidActionTakenException;
    public DocumentRouteHeaderValue superUserDisapproveAction(String principalId, DocumentRouteHeaderValue routeHeader, String annotation, boolean runPostProcessor) throws InvalidActionTakenException;

    public DocumentRouteHeaderValue placeInExceptionRouting(String principalId, DocumentRouteHeaderValue routeHeader, String annotation) throws InvalidActionTakenException;
    
    // Introduced in 2.1 //

    /**
     * @since 2.1
     */
    public DocumentRouteHeaderValue blanketApproval(String principalId, DocumentRouteHeaderValue routeHeader, String annotation, Set nodeNames) throws InvalidActionTakenException;

    /**
     * @since 2.1
     */
    public DocumentRouteHeaderValue returnDocumentToPreviousNode(String principalId, DocumentRouteHeaderValue routeHeader, String destinationNodeName, String annotation) throws InvalidActionTakenException;

    /**
     * @since 2.1
     */
    public DocumentRouteHeaderValue superUserReturnDocumentToPreviousNode(String principalId, DocumentRouteHeaderValue routeHeader, String nodeName, String annotation, boolean runPostProcessor) throws InvalidActionTakenException;

    /**
     * @since 2.1
     */
    public DocumentRouteHeaderValue takeGroupAuthority(String principalId, DocumentRouteHeaderValue routeHeader, String groupId, String annotation) throws InvalidActionTakenException;

    /**
     * @since 2.1
     */
    public DocumentRouteHeaderValue releaseGroupAuthority(String principalId, DocumentRouteHeaderValue routeHeader, String groupId, String annotation) throws InvalidActionTakenException;

    /**
     * @since 2.1
     */
    public DocumentRouteHeaderValue superUserNodeApproveAction(String principalId, DocumentRouteHeaderValue routeHeader, String nodeName, String annotation, boolean runPostProcessor) throws InvalidActionTakenException;

    /**
     * @since 2.1
     */
    public DocumentRouteHeaderValue moveDocument(String principalId, DocumentRouteHeaderValue routeHeader, MovePoint movePoint, String annotation) throws InvalidActionTakenException;

    /**
     * TODO: docs
     * @since 2.1
     */
    public DocumentRouteHeaderValue recallDocument(String principalId, DocumentRouteHeaderValue routeHeader, String annotation, boolean cancel) throws InvalidActionTakenException;

    // Introduced in 2.2

    /**
     * Invokes a List of actions at once.  This method will remove the approriate action items from the user's action
     * list and then schedule the actual processing of the actions.
     *
     * @since 2.2
     */
    public void takeMassActions(String principalId, List<ActionInvocation> actionInvocations);

    /**
     * @since 2.2
     */
    public DocumentRouteHeaderValue superUserReturnDocumentToPreviousNode(String principalId, String documentId, String nodeName, String annotation, boolean runPostProcessor) throws InvalidActionTakenException;

    /**
     * @since 2.2.7
     */
    public DocumentRouteHeaderValue superUserNodeApproveAction(String principalId, String documentId, String nodeName, String annotation, boolean runPostProcessor) throws InvalidActionTakenException;

    // Introduced in 2.2.2

    /**
     * @since 2.2.2
     */
    public DocumentRouteHeaderValue revokeAdHocRequests(String principalId, DocumentRouteHeaderValue document, AdHocRevoke revoke, String annotation) throws InvalidActionTakenException;
    
    public DocumentRouteHeaderValue revokeAdHocRequests(String principalId, DocumentRouteHeaderValue document, String actionRequestId, String annotation) throws InvalidActionTakenException;

    // Deprecated as of 2.1 //

    /**
     * @deprecated use blanketApproval which takes a Set of nodeNames instead.
     */
    public DocumentRouteHeaderValue blanketApproval(String principalId, DocumentRouteHeaderValue routeHeader, String annotation, Integer routeLevel) throws InvalidActionTakenException;

    /**
     * @deprecated use returnDocumentToPreviousNode instead
     */
    public DocumentRouteHeaderValue returnDocumentToPreviousRouteLevel(String principalId, DocumentRouteHeaderValue routeHeader, Integer destRouteLevel, String annotation) throws InvalidActionTakenException;

}
