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
package org.kuali.rice.kew.actionrequest.dao;

import org.kuali.rice.kew.actionrequest.ActionRequestValue;

import java.util.Collection;
import java.util.List;


/**
 * Data Access Object for {@link ActionRequestValue}s.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ActionRequestDAO {

    public ActionRequestValue getActionRequestByActionRequestId(String actionRequestId);

    public void saveActionRequest(ActionRequestValue actionRequest);

    public List<ActionRequestValue> findPendingRootRequestsByDocIdAtRouteLevel(String documentId, Integer routeLevel);

    public List<ActionRequestValue> findPendingByDocIdAtOrBelowRouteLevel(String documentId, Integer routeLevel);

    public List<ActionRequestValue> findPendingRootRequestsByDocIdAtOrBelowRouteLevel(String documentId, Integer routeLevel);

    public void delete(String actionRequestId);

     public List<ActionRequestValue> findPendingByActionRequestedAndDocId(String actionRequestedCd, String documentId);

    public List<ActionRequestValue> findAllPendingByDocId(String documentId);

    public List<ActionRequestValue> findAllByDocId(String documentId);

    public List<ActionRequestValue> findAllRootByDocId(String documentId);

    public List<ActionRequestValue> findByStatusAndDocId(String statusCd, String documentId);

    public List<ActionRequestValue> findByDocumentIdIgnoreCurrentInd(String documentId);

    public List<ActionRequestValue> findActivatedByGroup(String groupId);

    public List<ActionRequestValue> findPendingByResponsibilityIds(Collection responsibilityIds);

    public  void deleteByDocumentId(String documentId);

    public List<ActionRequestValue> findPendingRootRequestsByDocumentType(String documentTypeId);

    public List<ActionRequestValue> findPendingRootRequestsByDocIdAtRouteNode(String documentId, String nodeInstanceId);

    public List<ActionRequestValue> findRootRequestsByDocIdAtRouteNode(String documentId, String nodeInstanceId);

    //public List findFutureAdHocRequestsByDocIdAtRouteNode(String documentId, String nodeName);

    public boolean doesDocumentHaveUserRequest(String workflowId, String documentId);
  
    public List<String> getRequestGroupIds(String documentId);

    public ActionRequestValue getRoleActionRequestByActionTakenId(String actionTakenId);

}
