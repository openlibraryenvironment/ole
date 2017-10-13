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
package org.kuali.rice.kew.impl.document;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.uif.RemotableAttributeErrorContract;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.api.action.ActionTaken;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.action.AdHocRevoke;
import org.kuali.rice.kew.api.action.AdHocToGroup;
import org.kuali.rice.kew.api.action.AdHocToPrincipal;
import org.kuali.rice.kew.api.action.DocumentActionParameters;
import org.kuali.rice.kew.api.action.DocumentActionResult;
import org.kuali.rice.kew.api.action.MovePoint;
import org.kuali.rice.kew.api.action.RequestedActions;
import org.kuali.rice.kew.api.action.ReturnPoint;
import org.kuali.rice.kew.api.action.ValidActions;
import org.kuali.rice.kew.api.action.WorkflowDocumentActionsService;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.DocumentContent;
import org.kuali.rice.kew.api.document.DocumentContentUpdate;
import org.kuali.rice.kew.api.document.DocumentDetail;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.document.DocumentUpdate;
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;
import org.kuali.rice.kew.api.document.node.RouteNodeInstance;
import org.kuali.rice.kew.api.document.WorkflowDocumentService;

/**
 * The implementation of {@link org.kuali.rice.kew.api.WorkflowDocument}.  Implements {@link WorkflowDocumentPrototype} to expose
 * and initialization method used for construction.
 * <p>NOTE: operations against document data on this are only "flushed" when an action is performed.</p>
 * <p><b>This class is *not* thread safe.</b></p>
 * @see org.kuali.rice.kew.api.WorkflowDocument
 */
public class WorkflowDocumentImpl implements Serializable, WorkflowDocumentPrototype {

    private static final long serialVersionUID = -3672966990721719088L;

    /**
     * The principal id under which all document actions will be performed.
     */
    private String principalId;
    /**
     * Stores local changes that need to be committed.
     */
    private ModifiableDocument modifiableDocument;
    /**
     * Stores local changes that need to be committed.
     */
    private ModifiableDocumentContent modifiableDocumentContent;
    /**
     * Local cache of valid document actions.
     * @see #getValidActions()  
     */
    private ValidActions validActions;
    /**
     * Local cache of requested document actions.
     * @see #getRequestedActions()
     */
    private RequestedActions requestedActions;
    /**
     * Flag that indicates whether the document has been deleted; if so the object is thereafter in an illegal state.
     */
    private boolean documentDeleted = false;

    private transient WorkflowDocumentActionsService workflowDocumentActionsService;
    private transient WorkflowDocumentService workflowDocumentService;

    public void init(String principalId, Document document) {
        if (StringUtils.isBlank("principalId")) {
            throw new IllegalArgumentException("principalId was null or blank");
        }
        if (document == null) {
            throw new IllegalArgumentException("document was null");
        }
        this.principalId = principalId;
        this.modifiableDocument = new ModifiableDocument(document);
        this.modifiableDocumentContent = null;
        this.validActions = null;
        this.requestedActions = null;
    }

    public WorkflowDocumentActionsService getWorkflowDocumentActionsService() {
        if (workflowDocumentActionsService == null) {
            workflowDocumentActionsService = KewApiServiceLocator.getWorkflowDocumentActionsService();
        }
        return workflowDocumentActionsService;
    }

    public void setWorkflowDocumentActionsService(WorkflowDocumentActionsService workflowDocumentActionsService) {
        this.workflowDocumentActionsService = workflowDocumentActionsService;
    }

    public WorkflowDocumentService getWorkflowDocumentService() {
        if (workflowDocumentService == null) {
            workflowDocumentService = KewApiServiceLocator.getWorkflowDocumentService();
        }
        return workflowDocumentService;
    }

    public void setWorkflowDocumentService(WorkflowDocumentService workflowDocumentService) {
        this.workflowDocumentService = workflowDocumentService;
    }

    protected ModifiableDocument getModifiableDocument() {
        return modifiableDocument;
    }

    protected ModifiableDocumentContent getModifiableDocumentContent() {
        if (this.modifiableDocumentContent == null) {
            DocumentContent documentContent = getWorkflowDocumentService().getDocumentContent(getDocumentId());
            if (documentContent == null) {
                throw new IllegalStateException("Failed to load document content for documentId: " + getDocumentId());
            }
            this.modifiableDocumentContent = new ModifiableDocumentContent(documentContent);
        }
        return this.modifiableDocumentContent;
    }

    @Override
    public String getDocumentId() {
        if (documentDeleted) {
            throw new IllegalStateException("Document has been deleted.");
        }
        return getModifiableDocument().getDocumentId();
    }

    @Override
    public Document getDocument() {
        return getModifiableDocument().getDocument();
    }

    @Override
    public DocumentContent getDocumentContent() {
        return getModifiableDocumentContent().getDocumentContent();
    }

    @Override
    public String getApplicationContent() {
        return getDocumentContent().getApplicationContent();
    }

    @Override
    public void setApplicationContent(String applicationContent) {
        getModifiableDocumentContent().setApplicationContent(applicationContent);
    }
    
    @Override
    public void setAttributeContent(String attributeContent) {
        getModifiableDocumentContent().setAttributeContent(attributeContent);
    }

    @Override
    public void clearAttributeContent() {
        getModifiableDocumentContent().setAttributeContent("");
    }

    @Override
    public String getAttributeContent() {
        return getDocumentContent().getAttributeContent();
    }

    @Override
    public void addAttributeDefinition(WorkflowAttributeDefinition attributeDefinition) {
        getModifiableDocumentContent().addAttributeDefinition(attributeDefinition);
    }

    @Override
    public void removeAttributeDefinition(WorkflowAttributeDefinition attributeDefinition) {
        getModifiableDocumentContent().removeAttributeDefinition(attributeDefinition);
    }

    @Override
    public void clearAttributeDefinitions() {
        getAttributeDefinitions().clear();
    }

    @Override
    public List<WorkflowAttributeDefinition> getAttributeDefinitions() {
        return getModifiableDocumentContent().getAttributeDefinitions();
    }

    @Override
    public void setSearchableContent(String searchableContent) {
        getModifiableDocumentContent().setSearchableContent(searchableContent);
    }
    
    @Override
    public void addSearchableDefinition(WorkflowAttributeDefinition searchableDefinition) {
        getModifiableDocumentContent().addSearchableDefinition(searchableDefinition);
    }

    @Override
    public void removeSearchableDefinition(WorkflowAttributeDefinition searchableDefinition) {
        getModifiableDocumentContent().removeSearchableDefinition(searchableDefinition);
    }

    @Override
    public void clearSearchableDefinitions() {
        getSearchableDefinitions().clear();
    }

    @Override
    public void clearSearchableContent() {
        getModifiableDocumentContent().setSearchableContent("");
    }

    @Override
    public List<WorkflowAttributeDefinition> getSearchableDefinitions() {
        return getModifiableDocumentContent().getSearchableDefinitions();
    }

    @Override
    public List<? extends RemotableAttributeErrorContract> validateAttributeDefinition(
            WorkflowAttributeDefinition attributeDefinition) {
        return getWorkflowDocumentActionsService().validateWorkflowAttributeDefinition(attributeDefinition);
    }

    @Override
    public List<ActionRequest> getRootActionRequests() {
        return getWorkflowDocumentService().getRootActionRequests(getDocumentId());
    }

    @Override
    public List<ActionTaken> getActionsTaken() {
        return getWorkflowDocumentService().getActionsTaken(getDocumentId());
    }

    @Override
    public void setApplicationDocumentId(String applicationDocumentId) {
        getModifiableDocument().setApplicationDocumentId(applicationDocumentId);
    }

    @Override
    public String getApplicationDocumentId() {
        return getModifiableDocument().getApplicationDocumentId();
    }

    @Override
    public DateTime getDateCreated() {
        return getModifiableDocument().getDateCreated();
    }

    @Override
    public String getTitle() {
        return getModifiableDocument().getTitle();
    }

    @Override
    public ValidActions getValidActions() {
        if (validActions == null) {
            validActions = getWorkflowDocumentActionsService().determineValidActions(getDocumentId(), getPrincipalId());
        }
        return validActions;
    }

    @Override
    public RequestedActions getRequestedActions() {
        if (requestedActions == null) {
            requestedActions = getWorkflowDocumentActionsService().determineRequestedActions(getDocumentId(),
                    getPrincipalId());
        }
        return requestedActions;
    }

    protected DocumentUpdate getDocumentUpdateIfDirty() {
        if (getModifiableDocument().isDirty()) {
            return getModifiableDocument().build();
        }
        return null;
    }

    protected DocumentContentUpdate getDocumentContentUpdateIfDirty() {
        if (getModifiableDocumentContent().isDirty()) {
            return getModifiableDocumentContent().build();
        }
        return null;
    }

    protected void resetStateAfterAction(DocumentActionResult response) {
        this.modifiableDocument = new ModifiableDocument(response.getDocument());
        this.validActions = null;
        if (response.getValidActions() != null) {
            this.validActions = response.getValidActions();
        }
        this.requestedActions = null;
        if (response.getRequestedActions() != null) {
            this.requestedActions = response.getRequestedActions();
        }
        // regardless of whether modifiable document content is dirty, we null it out so it will be re-fetched next time it's needed
        this.modifiableDocumentContent = null;
    }

    @Override
    public void saveDocument(String annotation) {
        DocumentActionResult result = getWorkflowDocumentActionsService().save(
                constructDocumentActionParameters(annotation));
        resetStateAfterAction(result);
    }

    @Override
    public void route(String annotation) {
        DocumentActionResult result = getWorkflowDocumentActionsService().route(
                constructDocumentActionParameters(annotation));
        resetStateAfterAction(result);
    }

    @Override
    public void disapprove(String annotation) {
        DocumentActionResult result = getWorkflowDocumentActionsService().disapprove(
                constructDocumentActionParameters(annotation));
        resetStateAfterAction(result);
    }

    @Override
    public void approve(String annotation) {
        DocumentActionResult result = getWorkflowDocumentActionsService().approve(
                constructDocumentActionParameters(annotation));
        resetStateAfterAction(result);
    }

    @Override
    public void cancel(String annotation) {
        DocumentActionResult result = getWorkflowDocumentActionsService().cancel(
                constructDocumentActionParameters(annotation));
        resetStateAfterAction(result);
    }

    @Override
    public void recall(String annotation, boolean cancel) {
        DocumentActionResult result = getWorkflowDocumentActionsService().recall(
                constructDocumentActionParameters(annotation), cancel);
        resetStateAfterAction(result);
    }

    @Override
    public void blanketApprove(String annotation) {
        DocumentActionResult result = getWorkflowDocumentActionsService().blanketApprove(
                constructDocumentActionParameters(annotation));
        resetStateAfterAction(result);
    }

    @Override
    public void blanketApprove(String annotation, String... nodeNames) {
        if (nodeNames == null) {
            throw new IllegalArgumentException("nodeNames was null");
        }
        Set<String> nodeNamesSet = new HashSet<String>(Arrays.asList(nodeNames));
        DocumentActionResult result = getWorkflowDocumentActionsService().blanketApproveToNodes(
                constructDocumentActionParameters(annotation), nodeNamesSet);
        resetStateAfterAction(result);
    }

    @Override
    public void saveDocumentData() {
        DocumentActionResult result = getWorkflowDocumentActionsService().saveDocumentData(
                constructDocumentActionParameters(null));
        resetStateAfterAction(result);
    }

    @Override
    public void setApplicationDocumentStatus(String applicationDocumentStatus) {
        getModifiableDocument().setApplicationDocumentStatus(applicationDocumentStatus);
    }

    @Override
    public void acknowledge(String annotation) {
        DocumentActionResult result = getWorkflowDocumentActionsService().acknowledge(
                constructDocumentActionParameters(annotation));
        resetStateAfterAction(result);
    }

    @Override
    public void fyi(String annotation) {
        DocumentActionResult result = getWorkflowDocumentActionsService().clearFyi(
                constructDocumentActionParameters(annotation));
        resetStateAfterAction(result);
    }

    @Override
    public void fyi() {
        fyi("");
    }

    @Override
    public void delete() {
        getWorkflowDocumentActionsService().delete(getDocumentId(), principalId);
        documentDeleted = true;
    }

    @Override
    public void refresh() {
        Document document = getWorkflowDocumentService().getDocument(getDocumentId());
        this.modifiableDocument = new ModifiableDocument(document);
        this.validActions = null;
        this.requestedActions = null;
        this.modifiableDocumentContent = null;
    }

    @Override
    public void adHocToPrincipal(ActionRequestType actionRequested, String annotation, String targetPrincipalId,
            String responsibilityDescription, boolean forceAction) {
        adHocToPrincipal(actionRequested, null, annotation, targetPrincipalId, responsibilityDescription, forceAction);
    }

    @Override
    public void adHocToPrincipal(ActionRequestType actionRequested, String nodeName, String annotation,
            String targetPrincipalId, String responsibilityDescription, boolean forceAction) {
        adHocToPrincipal(actionRequested, nodeName, annotation, targetPrincipalId, responsibilityDescription,
                forceAction, null);
    }

    @Override
    public void adHocToPrincipal(ActionRequestType actionRequested, String nodeName, String annotation,
            String targetPrincipalId, String responsibilityDescription, boolean forceAction, String requestLabel) {
        AdHocToPrincipal.Builder builder = AdHocToPrincipal.Builder
                .create(actionRequested, nodeName, targetPrincipalId);
        builder.setResponsibilityDescription(responsibilityDescription);
        builder.setForceAction(forceAction);
        builder.setRequestLabel(requestLabel);
        DocumentActionResult result = getWorkflowDocumentActionsService().adHocToPrincipal(
                constructDocumentActionParameters(annotation), builder.build());
        resetStateAfterAction(result);
    }

    @Override
    public void adHocToPrincipal(AdHocToPrincipal adHocToPrincipal, String annotation) {
        DocumentActionResult result = getWorkflowDocumentActionsService().adHocToPrincipal(
                constructDocumentActionParameters(annotation), adHocToPrincipal);
        resetStateAfterAction(result);
    }

    @Override
    public void adHocToGroup(ActionRequestType actionRequested, String annotation, String targetGroupId,
            String responsibilityDescription, boolean forceAction) {
        adHocToGroup(actionRequested, null, annotation, targetGroupId, responsibilityDescription, forceAction);
    }

    @Override
    public void adHocToGroup(ActionRequestType actionRequested, String nodeName, String annotation,
            String targetGroupId, String responsibilityDescription, boolean forceAction) {
        adHocToGroup(actionRequested, nodeName, annotation, targetGroupId, responsibilityDescription, forceAction, null);
    }

    @Override
    public void adHocToGroup(ActionRequestType actionRequested, String nodeName, String annotation,
            String targetGroupId, String responsibilityDescription, boolean forceAction, String requestLabel) {
        AdHocToGroup.Builder builder = AdHocToGroup.Builder.create(actionRequested, nodeName, targetGroupId);
        builder.setResponsibilityDescription(responsibilityDescription);
        builder.setForceAction(forceAction);
        builder.setRequestLabel(requestLabel);
        DocumentActionResult result = getWorkflowDocumentActionsService().adHocToGroup(
                constructDocumentActionParameters(annotation), builder.build());
        resetStateAfterAction(result);
    }

    @Override
    public void adHocToGroup(AdHocToGroup adHocToGroup, String annotation) {
        DocumentActionResult result = getWorkflowDocumentActionsService().adHocToGroup(
                constructDocumentActionParameters(annotation), adHocToGroup);
        resetStateAfterAction(result);
    }

    @Override
    public void revokeAdHocRequestById(String actionRequestId, String annotation) {
        if (StringUtils.isBlank(actionRequestId)) {
            throw new IllegalArgumentException("actionRequestId was null or blank");
        }
        DocumentActionResult result = getWorkflowDocumentActionsService().revokeAdHocRequestById(
                constructDocumentActionParameters(annotation), actionRequestId);
        resetStateAfterAction(result);
    }

    @Override
    public void revokeAdHocRequests(AdHocRevoke revoke, String annotation) {
        if (revoke == null) {
            throw new IllegalArgumentException("revokeFromPrincipal was null");
        }
        DocumentActionResult result = getWorkflowDocumentActionsService().revokeAdHocRequests(
                constructDocumentActionParameters(annotation), revoke);
        resetStateAfterAction(result);
    }

    @Override
    public void revokeAllAdHocRequests(String annotation) {
        DocumentActionResult result = getWorkflowDocumentActionsService().revokeAllAdHocRequests(
                constructDocumentActionParameters(annotation));
        resetStateAfterAction(result);
    }

    @Override
    public void setTitle(String title) {
        getModifiableDocument().setTitle(title);
    }

    @Override
    public String getDocumentTypeName() {
        return getDocument().getDocumentTypeName();
    }

    @Override
    public boolean isCompletionRequested() {
        return getRequestedActions().isCompleteRequested();
    }

    @Override
    public boolean isApprovalRequested() {
        return getRequestedActions().isApproveRequested();
    }

    @Override
    public boolean isAcknowledgeRequested() {
        return getRequestedActions().isAcknowledgeRequested();
    }

    @Override
    public boolean isFYIRequested() {
        return getRequestedActions().isFyiRequested();
    }

    @Override
    public boolean isBlanketApproveCapable() {
        return isValidAction(ActionType.BLANKET_APPROVE)
                && (isCompletionRequested() || isApprovalRequested() || isInitiated());
    }

    @Override
    public boolean isRouteCapable() {
        return isValidAction(ActionType.ROUTE);
    }

    @Override
    public boolean isValidAction(ActionType actionType) {
        if (actionType == null) {
            throw new IllegalArgumentException("actionType was null");
        }
        return getValidActions().getValidActions().contains(actionType);
    }

    @Override
    public void superUserBlanketApprove(String annotation) {
        DocumentActionResult result = getWorkflowDocumentActionsService().superUserBlanketApprove(
                constructDocumentActionParameters(annotation), true);
        resetStateAfterAction(result);
    }

    @Override
    public void superUserNodeApprove(String nodeName, String annotation) {
        DocumentActionResult result = getWorkflowDocumentActionsService().superUserNodeApprove(
                constructDocumentActionParameters(annotation), true, nodeName);
        resetStateAfterAction(result);
    }

    @Override
    public void superUserTakeRequestedAction(String actionRequestId, String annotation) {
        DocumentActionResult result = getWorkflowDocumentActionsService().superUserTakeRequestedAction(
                constructDocumentActionParameters(annotation), true, actionRequestId);
        resetStateAfterAction(result);
    }

    @Override
    public void superUserDisapprove(String annotation) {
        DocumentActionResult result = getWorkflowDocumentActionsService().superUserDisapprove(
                constructDocumentActionParameters(annotation), true);
        resetStateAfterAction(result);
    }

    @Override
    public void superUserCancel(String annotation) {
        DocumentActionResult result = getWorkflowDocumentActionsService().superUserCancel(
                constructDocumentActionParameters(annotation), true);
        resetStateAfterAction(result);
    }

    @Override
    public void superUserReturnToPreviousNode(ReturnPoint returnPoint, String annotation) {
        DocumentActionResult result = getWorkflowDocumentActionsService().superUserReturnToPreviousNode(
                constructDocumentActionParameters(annotation), true, returnPoint);
        resetStateAfterAction(result);
    }

    @Override
    public void complete(String annotation) {
        DocumentActionResult result = getWorkflowDocumentActionsService().complete(
                constructDocumentActionParameters(annotation));
        resetStateAfterAction(result);
    }

    @Override
    public void logAnnotation(String annotation) {
        getWorkflowDocumentActionsService().logAnnotation(getDocumentId(), principalId, annotation);
    }

    @Override
    public DocumentStatus getStatus() {
        return getDocument().getStatus();
    }

    @Override
    public boolean checkStatus(DocumentStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("status was null");
        }
        return status == getStatus();
    }

    /**
     * Indicates if the document is in the initiated state or not.
     * 
     * @return true if in the specified state
     */
    @Override
    public boolean isInitiated() {
        return checkStatus(DocumentStatus.INITIATED);
    }

    /**
     * Indicates if the document is in the saved state or not.
     * 
     * @return true if in the specified state
     */
    @Override
    public boolean isSaved() {
        return checkStatus(DocumentStatus.SAVED);
    }

    /**
     * Indicates if the document is in the enroute state or not.
     * 
     * @return true if in the specified state
     */
    @Override
    public boolean isEnroute() {
        return checkStatus(DocumentStatus.ENROUTE);
    }

    /**
     * Indicates if the document is in the exception state or not.
     * 
     * @return true if in the specified state
     */
    @Override
    public boolean isException() {
        return checkStatus(DocumentStatus.EXCEPTION);
    }

    /**
     * Indicates if the document is in the canceled state or not.
     * 
     * @return true if in the specified state
     */
    @Override
    public boolean isCanceled() {
        return checkStatus(DocumentStatus.CANCELED);
    }

    /**
     * Indicates if the document is in the recalled state or not.
     *
     * @return true if in the specified state
     */
    @Override
    public boolean isRecalled() {
        return checkStatus(DocumentStatus.RECALLED);
    }

    /**
     * Indicates if the document is in the disapproved state or not.
     * 
     * @return true if in the specified state
     */
    @Override
    public boolean isDisapproved() {
        return checkStatus(DocumentStatus.DISAPPROVED);
    }

    /**
     * Indicates if the document is in the Processed or Finalized state.
     * 
     * @return true if in the specified state
     */
    @Override
    public boolean isApproved() {
        return isProcessed() || isFinal();
    }

    /**
     * Indicates if the document is in the processed state or not.
     * 
     * @return true if in the specified state
     */
    @Override
    public boolean isProcessed() {
        return checkStatus(DocumentStatus.PROCESSED);
    }

    /**
     * Indicates if the document is in the final state or not.
     * 
     * @return true if in the specified state
     */
    @Override
    public boolean isFinal() {
        return checkStatus(DocumentStatus.FINAL);
    }

    /**
     * Returns the principalId with which this WorkflowDocument was constructed
     * 
     * @return the principalId with which this WorkflowDocument was constructed
     */
    @Override
    public String getPrincipalId() {
        return principalId;
    }

    @Override
    public void switchPrincipal(String principalId) {
        if (StringUtils.isBlank(this.principalId)) {
            throw new IllegalArgumentException("principalId was null or blank");
        }
        this.principalId = principalId;
        this.validActions = null;
        this.requestedActions = null;
    }

    @Override
    public void takeGroupAuthority(String annotation, String groupId) {
        DocumentActionResult result = getWorkflowDocumentActionsService().takeGroupAuthority(
                constructDocumentActionParameters(annotation), groupId);
        resetStateAfterAction(result);
    }

    @Override
    public void releaseGroupAuthority(String annotation, String groupId) {
        DocumentActionResult result = getWorkflowDocumentActionsService().releaseGroupAuthority(
                constructDocumentActionParameters(annotation), groupId);
        resetStateAfterAction(result);
    }

    @Override
    public Set<String> getNodeNames() {
    	final List<String> names = getWorkflowDocumentService().getActiveRouteNodeNames(getDocumentId());
        return Collections.unmodifiableSet(new HashSet<String>(names));
    }

    public Set<String> getCurrentNodeNames() {
    	final List<String> names = getWorkflowDocumentService().getCurrentRouteNodeNames(getDocumentId());
        return Collections.unmodifiableSet(new HashSet<String>(names));
    }

    @Override
    public void returnToPreviousNode(String annotation, String nodeName) {
        if (nodeName == null) {
            throw new IllegalArgumentException("nodeName was null");
        }
        returnToPreviousNode(annotation, ReturnPoint.create(nodeName));
    }

    @Override
    public void returnToPreviousNode(String annotation, ReturnPoint returnPoint) {
        if (returnPoint == null) {
            throw new IllegalArgumentException("returnPoint was null");
        }
        DocumentActionResult result = getWorkflowDocumentActionsService().returnToPreviousNode(
                constructDocumentActionParameters(annotation), returnPoint);
        resetStateAfterAction(result);
    }

    @Override
    public void move(MovePoint movePoint, String annotation) {
        if (movePoint == null) {
            throw new IllegalArgumentException("movePoint was null");
        }
        DocumentActionResult result = getWorkflowDocumentActionsService().move(
                constructDocumentActionParameters(annotation), movePoint);
        resetStateAfterAction(result);
    }

    @Override
    public List<RouteNodeInstance> getActiveRouteNodeInstances() {
        return getWorkflowDocumentService().getActiveRouteNodeInstances(getDocumentId());
    }

    @Override
    public List<RouteNodeInstance> getCurrentRouteNodeInstances() {
        return getWorkflowDocumentService().getCurrentRouteNodeInstances(getDocumentId());
    }

    @Override
    public List<RouteNodeInstance> getRouteNodeInstances() {
        return getWorkflowDocumentService().getRouteNodeInstances(getDocumentId());
    }

    @Override
    public List<String> getPreviousNodeNames() {
        return getWorkflowDocumentService().getPreviousRouteNodeNames(getDocumentId());
    }

    @Override
    public DocumentDetail getDocumentDetail() {
        return getWorkflowDocumentService().getDocumentDetail(getDocumentId());
    }

    @Override
    public void updateDocumentContent(DocumentContentUpdate documentContentUpdate) {
        if (documentContentUpdate == null) {
            throw new IllegalArgumentException("documentContentUpdate was null.");
        }
        getModifiableDocumentContent().setDocumentContentUpdate(documentContentUpdate);
    }

    @Override
    public void placeInExceptionRouting(String annotation) {
        DocumentActionResult result = getWorkflowDocumentActionsService().placeInExceptionRouting(
                constructDocumentActionParameters(annotation));
        resetStateAfterAction(result);
    }

    @Override
    public void setVariable(String name, String value) {
        getModifiableDocument().setVariable(name, value);
    }

    @Override
    public String getVariableValue(String name) {
        return getModifiableDocument().getVariableValue(name);
    }

    @Override
    public void setReceiveFutureRequests() {
        setVariable(getFutureRequestsKey(principalId), getReceiveFutureRequestsValue());
    }

    @Override
    public void setDoNotReceiveFutureRequests() {
        this.setVariable(getFutureRequestsKey(principalId), getDoNotReceiveFutureRequestsValue());
    }

    @Override
    public void setClearFutureRequests() {
        this.setVariable(getFutureRequestsKey(principalId), getClearFutureRequestsValue());
    }

    protected String getFutureRequestsKey(String principalId) {
        return KewApiConstants.RECEIVE_FUTURE_REQUESTS_BRANCH_STATE_KEY + "," + principalId + ","
                + new Date().toString() + ", " + Math.random();
    }

    @Override
    public String getReceiveFutureRequestsValue() {
        return KewApiConstants.RECEIVE_FUTURE_REQUESTS_BRANCH_STATE_VALUE;
    }

    @Override
    public String getDoNotReceiveFutureRequestsValue() {
        return KewApiConstants.DONT_RECEIVE_FUTURE_REQUESTS_BRANCH_STATE_VALUE;
    }

    @Override
    public String getClearFutureRequestsValue() {
        return KewApiConstants.CLEAR_FUTURE_REQUESTS_BRANCH_STATE_VALUE;
    }

    protected DocumentActionParameters constructDocumentActionParameters(String annotation) {
        DocumentActionParameters.Builder builder = DocumentActionParameters.Builder.create(getDocumentId(),
                getPrincipalId());
        builder.setAnnotation(annotation);
        builder.setDocumentUpdate(getDocumentUpdateIfDirty());
        builder.setDocumentContentUpdate(getDocumentContentUpdateIfDirty());
        return builder.build();
    }
    
    @Override
    public DateTime getDateLastModified() {
        return getDocument().getDateLastModified();
    }

    @Override
    public DateTime getDateApproved() {
        return getDocument().getDateApproved();
    }

    @Override
    public DateTime getDateFinalized() {
        return getDocument().getDateFinalized();
    }

    @Override
    public String getInitiatorPrincipalId() {
        return getDocument().getInitiatorPrincipalId();
    }

    @Override
    public String getRoutedByPrincipalId() {
        return getDocument().getRoutedByPrincipalId();
    }

    @Override
    public String getDocumentTypeId() {
        return getDocument().getDocumentTypeId();
    }

    @Override
    public String getDocumentHandlerUrl() {
        return getDocument().getDocumentHandlerUrl();
    }

    @Override
    public String getApplicationDocumentStatus() {
        return getDocument().getApplicationDocumentStatus();
    }

    @Override
    public DateTime getApplicationDocumentStatusDate() {
        return getDocument().getApplicationDocumentStatusDate();
    }

    @Override
    public Map<String, String> getVariables() {
        return getDocument().getVariables();
    }

    /**
     * A wrapper around DocumentContent which keeps track of local changes and generates
     * a new updated DocumentContent as necessary.
     */
    protected static class ModifiableDocumentContent implements Serializable {

        private static final long serialVersionUID = -4458431160327214042L;

        private boolean dirty;
        private DocumentContent originalDocumentContent;
        private DocumentContentUpdate.Builder builder;

        protected ModifiableDocumentContent(DocumentContent documentContent) {
            this.dirty = false;
            this.originalDocumentContent = documentContent;
            this.builder = DocumentContentUpdate.Builder.create(documentContent);
        }

        protected DocumentContent getDocumentContent() {
            if (!dirty) {
                return originalDocumentContent;
            }
            DocumentContent.Builder documentContentBuilder = DocumentContent.Builder.create(originalDocumentContent);
            documentContentBuilder.setApplicationContent(builder.getApplicationContent());
            documentContentBuilder.setAttributeContent(builder.getAttributeContent());
            documentContentBuilder.setSearchableContent(builder.getSearchableContent());
            return documentContentBuilder.build();
        }

        protected DocumentContentUpdate build() {
            return builder.build();
        }

        protected void setDocumentContentUpdate(DocumentContentUpdate update) {
            this.builder = DocumentContentUpdate.Builder.create(update);
            this.dirty = true;
        }

        protected void addAttributeDefinition(WorkflowAttributeDefinition definition) {
            builder.getAttributeDefinitions().add(definition);
            dirty = true;
        }

        protected void removeAttributeDefinition(WorkflowAttributeDefinition definition) {
            builder.getAttributeDefinitions().remove(definition);
            dirty = true;
        }

        protected List<WorkflowAttributeDefinition> getAttributeDefinitions() {
            return builder.getAttributeDefinitions();
        }

        protected void addSearchableDefinition(WorkflowAttributeDefinition definition) {
            builder.getSearchableDefinitions().add(definition);
            dirty = true;
        }

        protected void removeSearchableDefinition(WorkflowAttributeDefinition definition) {
            builder.getSearchableDefinitions().remove(definition);
            dirty = true;
        }

        protected List<WorkflowAttributeDefinition> getSearchableDefinitions() {
            return builder.getAttributeDefinitions();
        }

        protected void setApplicationContent(String applicationContent) {
            builder.setApplicationContent(applicationContent);
            dirty = true;
        }

        protected void setAttributeContent(String attributeContent) {
            builder.setAttributeContent(attributeContent);
            dirty = true;
        }

        public void setAttributeDefinitions(List<WorkflowAttributeDefinition> attributeDefinitions) {
            builder.setAttributeDefinitions(attributeDefinitions);
            dirty = true;
        }

        public void setSearchableContent(String searchableContent) {
            builder.setSearchableContent(searchableContent);
            dirty = true;
        }

        public void setSearchableDefinitions(List<WorkflowAttributeDefinition> searchableDefinitions) {
            builder.setSearchableDefinitions(searchableDefinitions);
            dirty = true;
        }

        boolean isDirty() {
            return dirty;
        }

    }

    /**
     * A wrapper around Document which keeps track of local changes and generates
     * a new updated Document as necessary.
     */
    protected static class ModifiableDocument implements Serializable {

        private static final long serialVersionUID = -3234793238863410378L;

        private boolean dirty;
        private Document originalDocument;
        private DocumentUpdate.Builder builder;

        protected ModifiableDocument(Document document) {
            this.dirty = false;
            this.originalDocument = document;
            this.builder = DocumentUpdate.Builder.create(document);
        }

        protected Document getDocument() {
            if (!dirty) {
                return originalDocument;
            }
            Document.Builder documentBuilder = Document.Builder.create(originalDocument);
            documentBuilder.setApplicationDocumentId(builder.getApplicationDocumentId());
            documentBuilder.setTitle(builder.getTitle());
            documentBuilder.setApplicationDocumentStatus(builder.getApplicationDocumentStatus());
            documentBuilder.setVariables(builder.getVariables());
            return documentBuilder.build();
        }

        protected DocumentUpdate build() {
            return builder.build();
        }

        /**
         * Immutable value which is accessed frequently, provide direct access to it.
         */
        protected String getDocumentId() {
            return originalDocument.getDocumentId();
        }

        /**
         * Immutable value which is accessed frequently, provide direct access to it.
         */
        protected DateTime getDateCreated() {
            return originalDocument.getDateCreated();
        }

        protected String getApplicationDocumentId() {
            return builder.getApplicationDocumentId();
        }

        protected void setApplicationDocumentId(String applicationDocumentId) {
            builder.setApplicationDocumentId(applicationDocumentId);
            dirty = true;
        }

        protected String getTitle() {
            return builder.getTitle();
        }

        protected void setTitle(String title) {
            builder.setTitle(title);
            dirty = true;
        }

        protected String getApplicationDocumentStatus() {
            return builder.getApplicationDocumentStatus();
        }

        protected void setApplicationDocumentStatus(String applicationDocumentStatus) {
            builder.setApplicationDocumentStatus(applicationDocumentStatus);
            dirty = true;
        }

        protected void setVariable(String name, String value) {
            builder.setVariable(name, value);
            dirty = true;
        }

        protected String getVariableValue(String name) {
            return builder.getVariableValue(name);
        }

        boolean isDirty() {
            return dirty;
        }

    }

}
