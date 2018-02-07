/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.krad.impls;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.kuali.rice.core.api.uif.RemotableAttributeErrorContract;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.api.action.ActionTaken;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.action.AdHocRevoke;
import org.kuali.rice.kew.api.action.MovePoint;
import org.kuali.rice.kew.api.action.RequestedActions;
import org.kuali.rice.kew.api.action.ReturnPoint;
import org.kuali.rice.kew.api.action.ValidActions;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.DocumentContent;
import org.kuali.rice.kew.api.document.DocumentContentUpdate;
import org.kuali.rice.kew.api.document.DocumentDetail;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;
import org.kuali.rice.kew.api.document.node.RouteNodeInstance;

/**
 * MockWorkflowDocument is the base class for a MockWorkflowDocument
 *
 * <p>It can be extended by any other kind of
 * mock document that needs to override certain methods. This class has absolutely no state or
 * behavior. There is no public constructor, and no member variables. All void methods do nothing.
 * All methods with a return value return null. All state and behavior needs to be added via a
 * subclass.</p>
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public abstract class MockWorkflowDocument implements WorkflowDocument {

    @Override
    public DateTime getDateLastModified() {
        return null;
    }

    @Override
    public DateTime getDateApproved() {
        return null;
    }

    @Override
    public DateTime getDateFinalized() {
        return null;
    }

    @Override
    public String getInitiatorPrincipalId() {
        return null;
    }

    @Override
    public String getRoutedByPrincipalId() {
        return null;
    }

    @Override
    public String getDocumentTypeId() {
        return null;
    }

    @Override
    public String getDocumentHandlerUrl() {
        return null;
    }

    @Override
    public String getApplicationDocumentStatus() {
        return null;
    }

    @Override
    public DateTime getApplicationDocumentStatusDate() {
        return null;
    }

    @Override
    public Map<String, String> getVariables() {
        return null;
    }

    @Override
    public String getDocumentId() {
        return null;
    }

    @Override
    public Document getDocument() {
        return null;
    }

    @Override
    public DocumentContent getDocumentContent() {
        return null;
    }

    @Override
    public String getApplicationContent() {
        return null;
    }

    @Override
    public void setApplicationContent(String applicationContent) {}

    @Override
    public void clearAttributeContent() {}

    @Override
    public String getAttributeContent() {
        return null;
    }

    @Override
    public void addAttributeDefinition(WorkflowAttributeDefinition attributeDefinition) {}

    @Override
    public void removeAttributeDefinition(WorkflowAttributeDefinition attributeDefinition) {}

    @Override
    public void clearAttributeDefinitions() {}

    @Override
    public List<WorkflowAttributeDefinition> getAttributeDefinitions() {
        return null;
    }

    @Override
    public void addSearchableDefinition(WorkflowAttributeDefinition searchableDefinition) {}

    @Override
    public void removeSearchableDefinition(WorkflowAttributeDefinition searchableDefinition) {}

    @Override
    public void clearSearchableDefinitions() {}

    @Override
    public void clearSearchableContent() {}

    @Override
    public List<WorkflowAttributeDefinition> getSearchableDefinitions() {
        return null;
    }

    @Override
    public List<? extends RemotableAttributeErrorContract> validateAttributeDefinition(
            WorkflowAttributeDefinition attributeDefinition) {
        return null;
    }

    @Override
    public List<ActionRequest> getRootActionRequests() {
        return null;
    }

    @Override
    public List<ActionTaken> getActionsTaken() {
        return null;
    }

    @Override
    public void setApplicationDocumentId(String applicationDocumentId) {}

    @Override
    public String getApplicationDocumentId() {
        return null;
    }

    @Override
    public DateTime getDateCreated() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public ValidActions getValidActions() {
        return null;
    }

    @Override
    public RequestedActions getRequestedActions() {
        return null;
    }

    @Override
    public void saveDocument(String annotation) {}

    @Override
    public void route(String annotation) {}

    @Override
    public void disapprove(String annotation) {}

    @Override
    public void approve(String annotation) {}

    @Override
    public void cancel(String annotation) {}

    @Override
    public void recall(String annotation, boolean cancel) {}

    @Override
    public void blanketApprove(String annotation) {}

    @Override
    public void blanketApprove(String annotation, String... nodeNames) {}

    @Override
    public void saveDocumentData() {}

    @Override
    public void setApplicationDocumentStatus(String applicationDocumentStatus) {}

    @Override
    public void acknowledge(String annotation) {}

    @Override
    public void fyi(String annotation) {}

    @Override
    public void fyi() {}

    @Override
    public void delete() {}

    @Override
    public void refresh() {}

    @Override
    public void adHocToPrincipal(ActionRequestType actionRequested, String annotation, String targetPrincipalId,
            String responsibilityDescription, boolean forceAction) {}

    @Override
    public void adHocToPrincipal(ActionRequestType actionRequested, String nodeName, String annotation,
            String targetPrincipalId, String responsibilityDescription, boolean forceAction) {}

    @Override
    public void adHocToPrincipal(ActionRequestType actionRequested, String nodeName, String annotation,
            String targetPrincipalId, String responsibilityDescription, boolean forceAction, String requestLabel) {}

    @Override
    public void adHocToGroup(ActionRequestType actionRequested, String annotation, String targetGroupId,
            String responsibilityDescription, boolean forceAction) {}

    @Override
    public void adHocToGroup(ActionRequestType actionRequested, String nodeName, String annotation,
            String targetGroupId, String responsibilityDescription, boolean forceAction) {

    }

    @Override
    public void adHocToGroup(ActionRequestType actionRequested, String nodeName, String annotation,
            String targetGroupId, String responsibilityDescription, boolean forceAction, String requestLabel) {}

    @Override
    public void revokeAdHocRequestById(String actionRequestId, String annotation) {}

    @Override
    public void revokeAdHocRequests(AdHocRevoke revoke, String annotation) {}

    @Override
    public void revokeAllAdHocRequests(String annotation) {}

    @Override
    public void setTitle(String title) {}

    @Override
    public String getDocumentTypeName() {
        return null;
    }

    @Override
    public boolean isCompletionRequested() {
        return false;
    }

    @Override
    public boolean isApprovalRequested() {
        return false;
    }

    @Override
    public boolean isAcknowledgeRequested() {
        return false;
    }

    @Override
    public boolean isFYIRequested() {
        return false;
    }

    @Override
    public boolean isBlanketApproveCapable() {
        return false;
    }

    @Override
    public boolean isRouteCapable() {
        return false;
    }

    @Override
    public boolean isValidAction(ActionType actionType) {
        return false;
    }

    @Override
    public void superUserBlanketApprove(String annotation) {}

    @Override
    public void superUserNodeApprove(String nodeName, String annotation) {}

    @Override
    public void superUserTakeRequestedAction(String actionRequestId, String annotation) {}

    @Override
    public void superUserDisapprove(String annotation) {}

    @Override
    public void superUserCancel(String annotation) {}

    @Override
    public void superUserReturnToPreviousNode(ReturnPoint returnPoint, String annotation) {}

    @Override
    public void complete(String annotation) {}

    @Override
    public void logAnnotation(String annotation) {}

    @Override
    public DocumentStatus getStatus() {
        return null;
    }

    @Override
    public boolean checkStatus(DocumentStatus status) {
        return false;
    }

    @Override
    public boolean isInitiated() {
        return false;
    }

    @Override
    public boolean isSaved() {
        return false;
    }

    @Override
    public boolean isEnroute() {
        return false;
    }

    @Override
    public boolean isException() {
        return false;
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public boolean isRecalled() {
        return false;
    }

    @Override
    public boolean isDisapproved() {
        return false;
    }

    @Override
    public boolean isApproved() {
        return false;
    }

    @Override
    public boolean isProcessed() {
        return false;
    }

    @Override
    public boolean isFinal() {
        return false;
    }

    @Override
    public String getPrincipalId() {
        return null;
    }

    @Override
    public void switchPrincipal(String principalId) {}

    @Override
    public void takeGroupAuthority(String annotation, String groupId) {}

    @Override
    public void releaseGroupAuthority(String annotation, String groupId) {}

    @Override
    public Set<String> getNodeNames() {
        return null;
    }

    @Override
    public void returnToPreviousNode(String nodeName, String annotation) {}

    @Override
    public void returnToPreviousNode(String annotation, ReturnPoint returnPoint) {}

    @Override
    public void move(MovePoint movePoint, String annotation) {}

    @Override
    public List<RouteNodeInstance> getActiveRouteNodeInstances() {
        return null;
    }

    @Override
    public List<RouteNodeInstance> getRouteNodeInstances() {
        return null;
    }

    @Override
    public List<String> getPreviousNodeNames() {
        return null;
    }

    @Override
    public DocumentDetail getDocumentDetail() {
        return null;
    }

    @Override
    public void updateDocumentContent(DocumentContentUpdate documentContentUpdate) {}

    @Override
    public void placeInExceptionRouting(String annotation) {}

    @Override
    public void setVariable(String name, String value) {}

    @Override
    public String getVariableValue(String name) {
        return null;
    }

    @Override
    public void setReceiveFutureRequests() {}

    @Override
    public void setDoNotReceiveFutureRequests() {}

    @Override
    public void setClearFutureRequests() {}

    @Override
    public String getReceiveFutureRequestsValue() {
        return null;
    }

    @Override
    public String getDoNotReceiveFutureRequestsValue() {
        return null;
    }

    @Override
    public String getClearFutureRequestsValue() {
        return null;
    }

}
