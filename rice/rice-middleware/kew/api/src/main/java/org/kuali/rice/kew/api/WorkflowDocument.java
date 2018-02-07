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
package org.kuali.rice.kew.api;

import java.util.List;
import java.util.Set;

import org.kuali.rice.core.api.uif.RemotableAttributeErrorContract;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.api.action.ActionTaken;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.action.AdHocRevoke;
import org.kuali.rice.kew.api.action.AdHocToGroup;
import org.kuali.rice.kew.api.action.AdHocToPrincipal;
import org.kuali.rice.kew.api.action.MovePoint;
import org.kuali.rice.kew.api.action.RequestedActions;
import org.kuali.rice.kew.api.action.ReturnPoint;
import org.kuali.rice.kew.api.action.ValidActions;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.DocumentContent;
import org.kuali.rice.kew.api.document.DocumentContentUpdate;
import org.kuali.rice.kew.api.document.DocumentContract;
import org.kuali.rice.kew.api.document.DocumentDetail;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;
import org.kuali.rice.kew.api.document.node.RouteNodeInstance;

/**
 * WorkflowDocument is the core client API for interaction with the Kuali Enterprise Workflow system.
 * WorkflowDocument is an object-oriented facade/bridge to stateless KEW APIs, which maintains document
 * state, and flushes updates as actions are taken.
 *
 * WorkflowDocuments cannot be constructed directly, create them via the {@link WorkflowDocumentFactory}
 *
 * <p><b>This class is *not* thread safe.</b> If you are operating on the same document, be sure to synchronize
 * access to the {@link WorkflowDocument} instance.  If you are operating on different documents, obtain a distinct
 * instance from the {@link WorkflowDocumentFactory}.</p>
 */
public interface WorkflowDocument extends DocumentContract {
    /**
     * Returns the principalId with which this WorkflowDocument was constructed
     * @return the principalId with which this WorkflowDocument was constructed
     */
    String getPrincipalId();
    /**
     * Switches the principalId under which WorkflowDocument API are performed.
     * This method necessitates clearing of any state associated with the principalId
     * such as requested and valid actions.
     * @param principalId the new principalId
     */
    void switchPrincipal(String principalId);

    /**
     * Returns a read-only view of the underlying document meta-data
     * @return a read-only view of the underlying document meta-data
     */
    Document getDocument();

    /**
     * Returns a read-only view of the underlying mutable document content
     * @return a read-only view of the underlying mutable document content
     */
    DocumentContent getDocumentContent();

    /**
     * Returns the currently set application content.
     * @return the currently set application content.
     * @see #getDocumentContent()
     */
    String getApplicationContent();

    /**
     * Sets the document title. The update will be committed with the next document operation.
     * @param title the document title to set
     */
    void setTitle(String title);

    /**
     * Sets the application document id. The update will be committed with the next document operation.
     * @param applicationDocumentId the application document id to set.
     */
    void setApplicationDocumentId(String applicationDocumentId);

    /**
     * Sets the application document status. The update will be committed with the next document operation.
     * @param applicationDocumentStatus the application document status to set
     */
    void setApplicationDocumentStatus(String applicationDocumentStatus);

    /**
     * Sets the document's application content. The update will be committed with the next document operation.
     * @param applicationContent the document application content to set
     */
    void setApplicationContent(String applicationContent);

    /**
     * Sets the document's attribute content. The update will be committed with the next document operation.
     * @param attributeContent the document attribute content to set
     */
    void setAttributeContent(String attributeContent);
    /**
     * Clears the document's attribute content. The update will be committed with the next document operation.
     */
    void clearAttributeContent();
    /**
     * Returns the currently set document attribute content.
     */
    String getAttributeContent();

    /**
     * Adds a workflow attribute definition. The update will be committed with the next document operation.
     * @param attributeDefinition the WorkflowAttributeDefinition to add
     */
    void addAttributeDefinition(WorkflowAttributeDefinition attributeDefinition);
    /**
     * Removes a workflow attribute definition. The update will be committed with the next document operation.
     * Equality is determined on the basis of WorkflowAttributeDefinition fields.
     * @see org.kuali.rice.core.api.mo.AbstractDataTransferObject#equals(Object)
     * @param attributeDefinition the WorkflowAttributeDefinition to remove
     */
    void removeAttributeDefinition(WorkflowAttributeDefinition attributeDefinition);
    /**
     * Clears workflow attribute definitions. The update will be committed with the next document operation.
     */
    void clearAttributeDefinitions();
    /**
     * Returns the currently set workflow attribute definitions
     * @return the currently set workflow attribute definitions
     */
    List<WorkflowAttributeDefinition> getAttributeDefinitions();

    /**
     * Sets the document's searchable content. The update will be committed with the next document operation.
     * @param searchableContent the searchable content to set
     */
    public void setSearchableContent(String searchableContent);
    /**
     * Adds a searchable workflow attribute definition. The update will be committed with the next document operation.
     * @param searchableDefinition the WorkflowAttributeDefinition to add
     */
    void addSearchableDefinition(WorkflowAttributeDefinition searchableDefinition);
    /**
     * Removes a searchable workflow attribute definition. The update will be committed with the next document operation.
     * Equality is determined on the basis of WorkflowAttributeDefinition fields.
     * @see org.kuali.rice.core.api.mo.AbstractDataTransferObject#equals(Object)
     * @param searchableDefinition the WorkflowAttributeDefinition to remove
     */
    void removeSearchableDefinition(WorkflowAttributeDefinition searchableDefinition);
    /**
     * Clears searchable workflow attribute definitions. The update will be committed with the next document operation.
     */
    void clearSearchableDefinitions();
    /**
     * Clears the searchable content. The update will be committed with the next document operation.
     */
    void clearSearchableContent();
    /**
     * Returns the currently set searchable workflow attribute definitions
     * @return the currently set searchable workflow attribute definitions
     */
    List<WorkflowAttributeDefinition> getSearchableDefinitions();

    /**
     * Sets a workflow variable. The update will be committed with the next document operation.
     * @see #getVariables() 
     * @param name variable name
     * @param value variable value
     */
    void setVariable(String name, String value);
    /**
     * Gets a workflow variable value.
     * @see #getVariables()
     * @param name variable name
     */
    String getVariableValue(String name);
    /**
     * Sets the workflow variable that specifies that this principal should receive future requests
     * @see KewApiConstants#RECEIVE_FUTURE_REQUESTS_BRANCH_STATE_KEY
     * @see KewApiConstants#RECEIVE_FUTURE_REQUESTS_BRANCH_STATE_VALUE
     */
    void setReceiveFutureRequests();
    /**
     * Sets the workflow variable that specifies that this principal should NOT receive future requests
     * @see KewApiConstants#RECEIVE_FUTURE_REQUESTS_BRANCH_STATE_KEY
     * @see KewApiConstants#DONT_RECEIVE_FUTURE_REQUESTS_BRANCH_STATE_VALUE
     */
    void setDoNotReceiveFutureRequests();
    /**
     * Sets the workflow variable that specifies that ...
     * TODO: what does this do? (org.kuali.rice.kew.util.FutureRequestDocumentStateManager#clearStateFromDocument)
     * @see KewApiConstants#RECEIVE_FUTURE_REQUESTS_BRANCH_STATE_KEY
     * @see KewApiConstants#CLEAR_FUTURE_REQUESTS_BRANCH_STATE_VALUE
     */
    void setClearFutureRequests();
    /**
     * Returns whether the workflow variable that specifies that this principal should receive future requests has been set
     * @see #setReceiveFutureRequests()
     * @return whether the workflow variable that specifies that this principal should receive future requests has been set
     */
    String getReceiveFutureRequestsValue();
    /**
     * Returns whether the workflow variable that specifies that this principal should NOT receive future requests has been set
     * @see #setDoNotReceiveFutureRequests()
     * @return whether the workflow variable that specifies that this principal should NOT receive future requests has been set
     */
    String getDoNotReceiveFutureRequestsValue();
    /**
     * Returns whether the workflow variable that specifies that ... has been set
     * @return whether the workflow variable that specifies that ... has been set
     */
    String getClearFutureRequestsValue();

    /**
     * Validates a workflow attribute definition and returns a list of validation errors.
     * This action is stateless and does not cause a commit of document updates.
     *
     * Implementation note: this is currently only used by EDL.
     *
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#validateWorkflowAttributeDefinition(org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition)
     * @param attributeDefinition the workflow attribute definition to validate
     * @return list of attribute validation errors
     */
    List<? extends RemotableAttributeErrorContract> validateAttributeDefinition(WorkflowAttributeDefinition attributeDefinition);

    /**
     * Return the list of root action requests on the document.
     * @return the list of root action requests on the document.
     * @see org.kuali.rice.kew.api.document.WorkflowDocumentService#getRootActionRequests(String) 
     */
    List<ActionRequest> getRootActionRequests();

    /**
     * Return the list of past actions taken on the document
     * @return the list of past actions taken on the document
     * @see org.kuali.rice.kew.api.document.WorkflowDocumentService#getActionsTaken(String)
     */
    List<ActionTaken> getActionsTaken();

    /**
     * Returns the list of valid actions on this document for the current user
     * @return the list of valid actions on this document for the current user
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#determineValidActions(String, String) 
     */
    ValidActions getValidActions();

    /**
     * Returns the list of requested actions on this document for the current user
     * @return the list of requested actions on this document for the current user
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#determineRequestedActions(String, String)
     */
    RequestedActions getRequestedActions();

    /**
     * Saves the document, commits updates.
     * @param annotation the document action annotation
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#save(org.kuali.rice.kew.api.action.DocumentActionParameters)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void saveDocument(String annotation);
    /**
     * Routes the document, commits updates.
     * @param annotation the document action annotation
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#route(org.kuali.rice.kew.api.action.DocumentActionParameters)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void route(String annotation);
    /**
     * Completes the document, commits updates.
     * @param annotation the document action annotation
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#complete(org.kuali.rice.kew.api.action.DocumentActionParameters)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void complete(String annotation);
    /**
     * Disapproves the document, commits updates.
     * @param annotation the document action annotation
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#route(org.kuali.rice.kew.api.action.DocumentActionParameters)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void disapprove(String annotation);
    /**
     * Approves the document, commits updates.
     * @param annotation the document action annotation
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#approve(org.kuali.rice.kew.api.action.DocumentActionParameters)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void approve(String annotation);
    /**
     * Approves the document, commits updates.
     * @param annotation the document action annotation
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#approve(org.kuali.rice.kew.api.action.DocumentActionParameters)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void cancel(String annotation);
    /**
     * Recalls the document, commits updates.
     * @since 2.1
     * @param annotation the document action annotation
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#approve(org.kuali.rice.kew.api.action.DocumentActionParameters)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void recall(String annotation, boolean cancel);
    /**
     * Cancels the document, commits updates.
     * @param annotation the document action annotation
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#cancel(org.kuali.rice.kew.api.action.DocumentActionParameters)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void blanketApprove(String annotation);
    /**
     * Blanket-approves the document, commits updates.
     * @param annotation the document action annotation
     * @param nodeNames a set of node names to which to blanket approve the given document
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#blanketApproveToNodes(org.kuali.rice.kew.api.action.DocumentActionParameters, java.util.Set)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void blanketApprove(String annotation, String... nodeNames);
    /**
     * Save the document data without affecting routing, commits updates.
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#saveDocumentData(org.kuali.rice.kew.api.action.DocumentActionParameters)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void saveDocumentData();
    /**
     * Acknowledges the document, commits updates.
     * @param annotation the document action annotation
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#acknowledge(org.kuali.rice.kew.api.action.DocumentActionParameters)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void acknowledge(String annotation);
    /**
     * Clears an outstanding FYI on the document, commits updates.
     * @param annotation the document action annotation
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#clearFyi(org.kuali.rice.kew.api.action.DocumentActionParameters)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void fyi(String annotation);
    /**
     * Clears an outstanding FYI on the document without an annotation, commits updates.
     * @see #fyi(String)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void fyi();
    /**
     * Deletes the document.  Any pending updates are <b>NOT</b> committed, they are discarded.
     * After deletion this WorkflowDocument object will no longer be valid, and any operations
     * that interact with the workflow system will throws an {@link IllegalStateException}
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#delete(String, String)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidActionTakenException
     */
    void delete();
    /**
     * Reloads the document state, any pending changes will be <i><b>discarded</b></i>
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException
     * @see org.kuali.rice.kew.api.document.WorkflowDocumentService#getDocument(String)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException
     */
    void refresh();
    /**
     * Sends an "ad-hoc" action request to the specified principal; commits updates.
     * @param actionRequested the request action type
     * @param annotation the route annotation
     * @param targetPrincipalId the target principal id
     * @param responsibilityDescription description of the responsibility
     * @param forceAction whether the adhoc requests forces action by the recipient
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#adHocToPrincipal(org.kuali.rice.kew.api.action.DocumentActionParameters, org.kuali.rice.kew.api.action.AdHocToPrincipal)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void adHocToPrincipal(ActionRequestType actionRequested, String annotation,
            String targetPrincipalId, String responsibilityDescription,
            boolean forceAction);
    /**
     * Sends an "ad-hoc" action request to the specified principal; commits updates.
     * @param actionRequested the request action type
     * @param nodeName the node on which to generate the adhoc request
     * @param annotation the route annotation
     * @param targetPrincipalId the target principal id
     * @param responsibilityDescription description of the responsibility
     * @param forceAction whether the adhoc requests forces action by the recipient
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#adHocToPrincipal(org.kuali.rice.kew.api.action.DocumentActionParameters, org.kuali.rice.kew.api.action.AdHocToPrincipal)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void adHocToPrincipal(ActionRequestType actionRequested, String nodeName,
            String annotation, String targetPrincipalId,
            String responsibilityDescription, boolean forceAction);
    /**
     * Sends an "ad-hoc" action request to the specified principal; commits updates.
     * @param actionRequested the request action type
     * @param nodeName the node on which to generate the adhoc request
     * @param annotation the route annotation
     * @param targetPrincipalId the target principal id
     * @param responsibilityDescription description of the responsibility
     * @param forceAction whether the adhoc requests forces action by the recipient
     * @param requestLabel the request label
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#adHocToPrincipal(org.kuali.rice.kew.api.action.DocumentActionParameters, org.kuali.rice.kew.api.action.AdHocToPrincipal)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void adHocToPrincipal(ActionRequestType actionRequested, String nodeName,
            String annotation, String targetPrincipalId,
            String responsibilityDescription, boolean forceAction,
            String requestLabel);
    /**
     * Sends an "ad-hoc" action request to the specified principal; commits updates.
     * @param adHocToPrincipal a pre-constructed AdHocToPrincipal object
     * @param annotation the route annotation
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#adHocToPrincipal(org.kuali.rice.kew.api.action.DocumentActionParameters, org.kuali.rice.kew.api.action.AdHocToPrincipal)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void adHocToPrincipal(AdHocToPrincipal adHocToPrincipal, String annotation);
    /**
     * Sends an "ad-hoc" action request to the specified group; commits updates.
     * @param actionRequested the request action type
     * @param annotation the route annotation
     * @param targetGroupId the target group id
     * @param responsibilityDescription description of the responsibility
     * @param forceAction whether the adhoc requests forces action by the recipient
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#adHocToGroup(org.kuali.rice.kew.api.action.DocumentActionParameters, org.kuali.rice.kew.api.action.AdHocToGroup)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void adHocToGroup(ActionRequestType actionRequested, String annotation,
            String targetGroupId, String responsibilityDescription,
            boolean forceAction);
    /**
     * Sends an "ad-hoc" action request to the specified group; commits updates.
     * @param actionRequested the request action type
     * @param nodeName the node on which to generate the adhoc request
     * @param annotation the route annotation
     * @param targetGroupId the target group id
     * @param responsibilityDescription description of the responsibility
     * @param forceAction whether the adhoc requests forces action by the recipient
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#adHocToGroup(org.kuali.rice.kew.api.action.DocumentActionParameters, org.kuali.rice.kew.api.action.AdHocToGroup)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void adHocToGroup(ActionRequestType actionRequested, String nodeName,
            String annotation, String targetGroupId,
            String responsibilityDescription, boolean forceAction);
/**
     * Sends an "ad-hoc" action request to the specified principal; commits updates.
     * @param actionRequested the request action type
     * @param nodeName the node on which to generate the adhoc request
     * @param annotation the route annotation
     * @param targetGroupId the target group id
     * @param responsibilityDescription description of the responsibility
     * @param forceAction whether the adhoc requests forces action by the recipient
     * @param requestLabel the request label
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#adHocToGroup(org.kuali.rice.kew.api.action.DocumentActionParameters, org.kuali.rice.kew.api.action.AdHocToGroup)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void adHocToGroup(ActionRequestType actionRequested, String nodeName,
            String annotation, String targetGroupId,
            String responsibilityDescription, boolean forceAction,
            String requestLabel);
    /**
     * Sends an "ad-hoc" action request to the specified principal; commits updates.
     * @param adHocToGroup a pre-constructed AdHocToGroup object
     * @param annotation the route annotation
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#adHocToPrincipal(org.kuali.rice.kew.api.action.DocumentActionParameters, org.kuali.rice.kew.api.action.AdHocToPrincipal)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void adHocToGroup(AdHocToGroup adHocToGroup, String annotation);
    /**
     * Revokes an Ad-Hoc request by id; commits updates.
     * @param actionRequestId the action request id to revoke
     * @param annotation the routing annotation
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#revokeAdHocRequestById(org.kuali.rice.kew.api.action.DocumentActionParameters, String)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void revokeAdHocRequestById(String actionRequestId, String annotation);
    /**
     * Revokes an Ad-Hoc request by the specified criteria; commits updates.
     * @param revoke the criteria for matching ad hoc action requests on the specified document that
     *        should be revoked
     * @param annotation the routing annotation
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#revokeAdHocRequests(org.kuali.rice.kew.api.action.DocumentActionParameters, org.kuali.rice.kew.api.action.AdHocRevoke)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void revokeAdHocRequests(AdHocRevoke revoke, String annotation);
    /**
     * Revokes all Ad-Hoc requests; commits updates; commits updates.
     * @param annotation the routing annotation
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#revokeAllAdHocRequests(org.kuali.rice.kew.api.action.DocumentActionParameters)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void revokeAllAdHocRequests(String annotation);
    /**
     * Returns the document to a previous node; commits updates.
     * @param annotation the routing annotation
     * @param nodeName the node to return to
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#returnToPreviousNode(org.kuali.rice.kew.api.action.DocumentActionParameters, org.kuali.rice.kew.api.action.ReturnPoint)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void returnToPreviousNode(String annotation, String nodeName);
    /**
     * Returns the document to a previous node; commits updates.
     * @param annotation the routing annotation
     * @param returnPoint the node to return to
     * @see #returnToPreviousNode(String, String)
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#returnToPreviousNode(org.kuali.rice.kew.api.action.DocumentActionParameters, org.kuali.rice.kew.api.action.ReturnPoint)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void returnToPreviousNode(String annotation, ReturnPoint returnPoint);
    /**
     * Moves the document to a different node; commits updates.
     * @param movePoint the node to move to
     * @param annotation the routing annotation
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#move(org.kuali.rice.kew.api.action.DocumentActionParameters, org.kuali.rice.kew.api.action.MovePoint)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void move(MovePoint movePoint, String annotation);
    /**
     * Takes authority of a group by a member of that group; commits updates.
     * @param annotation the routing annotation
     * @param groupId the group for which to take authority
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#takeGroupAuthority(org.kuali.rice.kew.api.action.DocumentActionParameters, String)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void takeGroupAuthority(String annotation, String groupId);
    /**
     * Releases authority of a group by a member of that group; commits updates.
     * @param annotation the routing annotation
     * @param groupId the group for which to take authority
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#releaseGroupAuthority(org.kuali.rice.kew.api.action.DocumentActionParameters, String)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void releaseGroupAuthority(String annotation, String groupId);

    /**
     * Places the document in exception routing; commits updates.
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#placeInExceptionRouting(org.kuali.rice.kew.api.action.DocumentActionParameters)
     * @param annotation the routing annotation
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void placeInExceptionRouting(String annotation);

    /**
     * Performs a super-user blanket-approve action; commits updates.
     * The current user must be a super-user for this document.
     * @param annotation the routing annotation
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#superUserBlanketApprove(org.kuali.rice.kew.api.action.DocumentActionParameters, boolean)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void superUserBlanketApprove(String annotation);
    /**
     * Performs a super-user approve action for the specified node; commits updates.
     * The current user must be a super-user for this document.
     * @param nodeName the node to super-user approve
     * @param annotation the routing annotation
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#superUserNodeApprove(org.kuali.rice.kew.api.action.DocumentActionParameters, boolean, String)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void superUserNodeApprove(String nodeName, String annotation);
    /**
     * Performs a super-user approve action for the specified node; commits updates.
     * The current user must be a super-user for this document.
     * @param actionRequestId the action request to satisfy/approve
     * @param annotation the routing annotation
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#superUserTakeRequestedAction(org.kuali.rice.kew.api.action.DocumentActionParameters, boolean, String)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void superUserTakeRequestedAction(String actionRequestId, String annotation);
    /**
     * Performs a super-user disapprove action; commits updates.
     * The current user must be a super-user for this document.
     * @param annotation the routing annotation
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#superUserDisapprove(org.kuali.rice.kew.api.action.DocumentActionParameters, boolean)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void superUserDisapprove(String annotation);
    /**
     * Performs a super-user cancel action; commits updates.
     * The current user must be a super-user for this document.
     * @param annotation the routing annotation
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#superUserCancel(org.kuali.rice.kew.api.action.DocumentActionParameters, boolean)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void superUserCancel(String annotation);
    /**
     * Performs a super-user "return to previous node" action; commits updates.
     * The current user must be a super-user for this document.
     * @param returnPoint the node to return to
     * @param annotation the routing annotation
     * @see org.kuali.rice.kew.api.action.WorkflowDocumentActionsService#superUserReturnToPreviousNode(org.kuali.rice.kew.api.action.DocumentActionParameters, boolean, org.kuali.rice.kew.api.action.ReturnPoint)
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException
     */
    void superUserReturnToPreviousNode(ReturnPoint returnPoint, String annotation);
    /**
     * Records a log action which adds an annotation on the document but does not affect routing.
     * This action is stateless and does not cause a commit of document updates.
     * @param annotation the annotation to log
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException, InvalidActionTakenException
     */
    void logAnnotation(String annotation);

    /**
     * Returns whether a completion request is one of the requested actions.
     * @see #getRequestedActions()
     * @return whether a completion request is one of the requested actions.
     */
    boolean isCompletionRequested();
    /**
     * Returns whether an approval request is one of the requested actions.
     * @see #getRequestedActions()
     * @return whether an approval request is one of the requested actions.
     */
    boolean isApprovalRequested();
    /**
     * Returns whether an acknowledge request is one of the requested actions.
     * @see #getRequestedActions()
     * @return whether an acknowledge request is one of the requested actions.
     */
    boolean isAcknowledgeRequested();
    /**
     * Returns whether an FYI is one of the requested actions.
     * @see #getRequestedActions()
     * @return whether an FYI is one of the requested actions.
     */
    boolean isFYIRequested();

    /**
     * Returns whether a blank-approve action is a valid action.
     * @see #getValidActions() 
     * @return whether a blank-approve action is a valid action.
     */
    boolean isBlanketApproveCapable();
    /**
     * Returns whether a route action is a valid action.
     * @see #getValidActions()
     * @return whether a route action is a valid action.
     */
    boolean isRouteCapable();
    /**
     * Returns whether the specified action type is valid
     * @param actionType the non-null ActionType to check
     * @see #getValidActions()
     * @return whether the specified action type is valid
     */
    boolean isValidAction(ActionType actionType);

    /**
     * Helper that checks whether the document has the given status
     * NOTE: does this really need to be in the public API? it only appears to be used internally
     * @see #getStatus()
     * @param status the status to check
     * @return whether the document status is the specified status
     */
    boolean checkStatus(DocumentStatus status);
    /**
     * Indicates if the document is in the initiated state or not.
     * @see #getStatus()
     * @return true if in the specified state
     */
    boolean isInitiated();
    /**
     * Indicates if the document is in the saved state or not.
     * @see #getStatus()
     * @return true if in the specified state
     */
    boolean isSaved();
    /**
     * Indicates if the document is in the enroute state or not.
     * @see #getStatus()
     * @return true if in the specified state
     */
    boolean isEnroute();
    /**
     * Indicates if the document is in the exception state or not.
     * @see #getStatus()
     * @return true if in the specified state
     */
    boolean isException();
    /**
     * Indicates if the document is in the canceled state or not.
     * @see #getStatus()
     * @return true if in the specified state
     */
    boolean isCanceled();
    /**
     * Indicates if the document is in the recalled state or not.
     * @since 2.1
     * @see #getStatus()
     * @return true if in the specified state
     */
    boolean isRecalled();
    /**
     * Indicates if the document is in the disapproved state or not.
     * @see #getStatus()
     * @return true if in the specified state
     */
    boolean isDisapproved();
    /**
     * Indicates if the document is in the Processed or Finalized state.
     * @see #getStatus()
     * @return true if in the specified state
     */
    boolean isApproved();
    /**
     * Indicates if the document is in the processed state or not.
     * @see #getStatus()
     * @return true if in the specified state
     */
    boolean isProcessed();
    /**
     * Indicates if the document is in the final state or not.
     * @see #getStatus()
     * @return true if in the specified state
     */
    boolean isFinal();

    /**
     * Returns the names of the route nodes on the document which are currently active.
     *
     * <p>If the document has completed its routing (i.e. it is in processed or final status) then this method may
     * return an empty set since no nodes are active at that time.  In order to get either the active *or* terminal
     * nodes, use the {@link #getCurrentNodeNames()} method.</p>
     *
     * @see #getActiveRouteNodeInstances()
     * @see org.kuali.rice.kew.api.document.WorkflowDocumentService#getActiveRouteNodeInstances(String)
     * @return an unmodifiable set containing the names of the active nodes for this document
     */
    Set<String> getNodeNames();
    /**
     * Returns the names of the nodes at which the document is currently at in it's route path.
     *
     * <p>This method differs from {@link #getNodeNames()} in the fact that if there are no active nodes, it will
     * return the last nodes on the document instead (a.k.a. the document's terminal nodes).</p>
     *
     * @see #getCurrentRouteNodeInstances() 
     * @see org.kuali.rice.kew.api.document.WorkflowDocumentService#getCurrentRouteNodeInstances(String)
     * @return an unmodifiable set containing the names of the nodes at which this document is currently located within it's route path
     */
    Set<String> getCurrentNodeNames();
    /**
     * Returns the list of active route node instances
     * @see org.kuali.rice.kew.api.document.WorkflowDocumentService#getActiveRouteNodeInstances(String)
     * @return the list of active route node instances
     */
    List<RouteNodeInstance> getActiveRouteNodeInstances();
    /**
     * Returns the list of active route node instances
     * @see org.kuali.rice.kew.api.document.WorkflowDocumentService#getCurrentRouteNodeInstances(String)
     * @return the list of active route node instances
     */
    List<RouteNodeInstance> getCurrentRouteNodeInstances();
    /**
     * Returns the flattened list of route node instances
     * @see org.kuali.rice.kew.api.document.WorkflowDocumentService#getRouteNodeInstances(String) 
     * @return the flattened list of route node instances
     */
    List<RouteNodeInstance> getRouteNodeInstances();
    /**
     * Returns the list of previous route node names
     * @see org.kuali.rice.kew.api.document.WorkflowDocumentService#getPreviousRouteNodeNames(String)
     * @return the list of previous route node names
     */
    List<String> getPreviousNodeNames();


    /**
     * Returns detailed document information
     * TODO: consolidate with Document/ModifiableDocument or eliminate? currently bypasses
     * locally cached Document data
     * @see org.kuali.rice.kew.api.document.WorkflowDocumentService#getDocumentDetail(String)
     * @return detailed document information
     */
    DocumentDetail getDocumentDetail();
    /**
     * Sets internal DocumentContentUpdate object
     * TODO: exposes internal API and used only by tests, candidate for elimination?
     * @param documentContentUpdate the DocumentContentUpdate to set
     */
    void updateDocumentContent(DocumentContentUpdate documentContentUpdate);
}