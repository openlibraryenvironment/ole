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
package org.kuali.rice.kew.actionrequest.service;

import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.engine.ActivationContext;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service to handle the building, sorting, saving, activating and deactivating of action request graphs.  These lists are
 * what determine role and delegation behaviors in graphs of action requests.
 *
 * Fetching that is being done is also taking into account the 'weight' of action request codes.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ActionRequestService {
	public ActionRequestValue initializeActionRequestGraph(ActionRequestValue actionRequest, DocumentRouteHeaderValue document, RouteNodeInstance nodeInstance);

    public void deactivateRequest(ActionTakenValue actionTaken, ActionRequestValue actionRequest);

    public void deactivateRequests(ActionTakenValue actionTaken, List actionRequests);

    public void deactivateRequest(ActionTakenValue actionTaken, ActionRequestValue actionRequest, boolean simulate);

    public void deactivateRequest(ActionTakenValue actionTaken, ActionRequestValue actionRequest, ActivationContext activationContext);

    public void deactivateRequests(ActionTakenValue actionTaken, List actionRequests, boolean simulate);

    public void deactivateRequests(ActionTakenValue actionTaken, List actionRequests, ActivationContext activationContext);

    public void deleteActionRequestGraph(ActionRequestValue actionRequest);

    public List findAllValidRequests(String principalId, String documentId, String requestCode);

    public List findAllValidRequests(String principalId, Collection actionRequests, String requestCode);

    public List<ActionRequestValue> findPendingByDoc(String documentId);

    public void saveActionRequest(ActionRequestValue actionRequest);

    public void activateRequest(ActionRequestValue actionRequest);

    public void activateRequest(ActionRequestValue actionRequest, boolean simulate);

    public void activateRequest(ActionRequestValue actionRequest, ActivationContext activationContext);

    public void activateRequests(Collection actionRequests);

    public void activateRequests(Collection actionRequests, boolean simulate);

	public void activateRequests(Collection actionRequests, ActivationContext activationContext);

    public List activateRequestNoNotification(ActionRequestValue actionRequest, boolean simulate);

    public List activateRequestNoNotification(ActionRequestValue actionRequest, ActivationContext activationContext);

    public ActionRequestValue findByActionRequestId(String actionRequestId);

    public List<ActionRequestValue> findPendingRootRequestsByDocId(String documentId);

    public List<ActionRequestValue> findPendingRootRequestsByDocIdAtRouteLevel(String documentId, Integer routeLevel);

    public List<ActionRequestValue> findPendingByDocIdAtOrBelowRouteLevel(String documentId, Integer routeLevel);

    public List<ActionRequestValue> findPendingRootRequestsByDocIdAtOrBelowRouteLevel(String documentId, Integer routeLevel);

    public List<ActionRequestValue> findPendingRootRequestsByDocumentType(String documentTypeId);

    public List<ActionRequestValue> findAllActionRequestsByDocumentId(String documentId);
    
    public List<ActionRequestValue> findAllRootActionRequestsByDocumentId(String documentId);

    public List<ActionRequestValue> findPendingByActionRequestedAndDocId(String actionRequestedCdCd, String documentId);

    /**
     *
     * This method gets a list of ids of all principals who have a pending action request for a document.
     *
     * @param actionRequestedCd
     * @param documentId
     * @return
     */
    public List<String> getPrincipalIdsWithPendingActionRequestByActionRequestedAndDocId(String actionRequestedCd, String documentId);

    public List<ActionRequestValue> findByStatusAndDocId(String statusCd, String documentId);

    public void alterActionRequested(List actionRequests, String actionRequestCd);

    public List<ActionRequestValue> findByDocumentIdIgnoreCurrentInd(String documentId);

    public List findActivatedByGroup(String groupId);

    public void updateActionRequestsForResponsibilityChange(Set<String> responsibilityIds);

    public ActionRequestValue getRoot(ActionRequestValue actionRequest);

    public List<ActionRequestValue> getRootRequests(Collection<ActionRequestValue> actionRequests);

    public boolean isDuplicateRequest(ActionRequestValue actionRequest);

    public List<ActionRequestValue> findPendingByDocRequestCdRouteLevel(String documentId, String requestCode, Integer routeLevel);

    public List<ActionRequestValue> findPendingByDocRequestCdNodeName(String documentId, String requestCode, String nodeName);
    
    /**
     * Returns all pending requests for a given routing entity
     * @param documentId the id of the document header being routed
     * @return a List of all pending ActionRequestValues for the document
     */
    public abstract List<ActionRequestValue> findAllPendingRequests(String documentId);
    
	/**
	 * Filters action requests based on if they occur after the given requestCode, and if they relate to 
	 * the given principal
	 * @param actionRequests the List of ActionRequestValues to filter
	 * @param principalId the id of the principal to find active requests for
	 * @param principalGroupIds List of group ids that the principal belongs to
	 * @param requestCode the request code for all ActionRequestValues to be after
	 * @return the filtered List of ActionRequestValues
	 */
	public abstract List<ActionRequestValue> filterActionRequestsByCode(List<ActionRequestValue> actionRequests, String principalId, List<String> principalGroupIds, String requestCode);

    /**
     * Returns the highest priority delegator in the list of action requests.
     */
    public Recipient findDelegator(List actionRequests);

    /**
     * Returns the closest delegator for the given ActionRequest
     */
    public Recipient findDelegator(ActionRequestValue actionRequest);

    public ActionRequestValue findDelegatorRequest(ActionRequestValue actionRequest);

    public void deleteByDocumentId(String documentId);

    public void deleteByActionRequestId(String actionRequestId);

    public void validateActionRequest(ActionRequestValue actionRequest);

    public List<ActionRequestValue> findPendingRootRequestsByDocIdAtRouteNode(String documentId, String nodeInstanceId);

    public List<ActionRequestValue> findRootRequestsByDocIdAtRouteNode(String documentId, String nodeInstanceId);

    public List getDelegateRequests(ActionRequestValue actionRequest);

    /**
     * If this is a role request, then this method returns a List of the action request for each recipient within the
     * role.  Otherwise, it will return a List with just the original action request.
     */
    public List getTopLevelRequests(ActionRequestValue actionRequest);

    public boolean isValidActionRequestCode(String actionRequestCode);

    /**
     * Checks if the given user has any Action Requests on the given document.
     */
    public boolean doesPrincipalHaveRequest(String principalId, String documentId);

    public Map<String, String> getActionsRequested(DocumentRouteHeaderValue routeHeader, String principalId, boolean completeAndApproveTheSame);

    public ActionRequestValue getActionRequestForRole(String actionTakenId);
}
