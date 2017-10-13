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
package org.kuali.rice.kew.actiontaken.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.actiontaken.dao.ActionTakenDAO;
import org.kuali.rice.kew.actiontaken.service.ActionTakenService;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.exception.WorkflowServiceErrorException;
import org.kuali.rice.kew.exception.WorkflowServiceErrorImpl;
import org.kuali.rice.kew.routeheader.service.RouteHeaderService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/**
 * Default implementation of the {@link ActionTakenService}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ActionTakenServiceImpl implements ActionTakenService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ActionTakenServiceImpl.class);
    private ActionTakenDAO actionTakenDAO;

    public ActionTakenValue load(String id) {
        return getActionTakenDAO().load(id);
    }

    public ActionTakenValue findByActionTakenId(String actionTakenId) {
        return getActionTakenDAO().findByActionTakenId(actionTakenId);
    }

    public ActionTakenValue getPreviousAction(ActionRequestValue actionRequest) {
    	return getPreviousAction(actionRequest, null);
    }

    public ActionTakenValue getPreviousAction(ActionRequestValue actionRequest, List<ActionTakenValue> simulatedActionsTaken)
    {
        GroupService ims = KimApiServiceLocator.getGroupService();
        ActionTakenValue foundActionTaken = null;
        List<String> principalIds = new ArrayList<String>();
        if (actionRequest.isGroupRequest()) {
            principalIds.addAll( ims.getMemberPrincipalIds(actionRequest.getGroup().getId()));
        } else if (actionRequest.isUserRequest()) {
            principalIds.add(actionRequest.getPrincipalId());
        }

        for (String id : principalIds)
        {
            List<ActionTakenValue> actionsTakenByUser =
                getActionTakenDAO().findByDocumentIdWorkflowId(actionRequest.getDocumentId(), id );
            if (simulatedActionsTaken != null) {
                for (ActionTakenValue simulatedAction : simulatedActionsTaken)
                {
                    if (id.equals(simulatedAction.getPrincipalId()))
                    {
                        actionsTakenByUser.add(simulatedAction);
                    }
                }
            }

            for (ActionTakenValue actionTaken : actionsTakenByUser)
            {
                if (ActionRequestValue.compareActionCode(actionTaken.getActionTaken(),
                        actionRequest.getActionRequested(), true) >= 0)
                {
                  foundActionTaken = actionTaken;
                }
            }
        }

        return foundActionTaken;
    }

    public Collection findByDocIdAndAction(String docId, String action) {
        return getActionTakenDAO().findByDocIdAndAction(docId, action);
    }

    public Collection<ActionTakenValue> findByDocumentId(String documentId) {
        return getActionTakenDAO().findByDocumentId(documentId);
    }

    public List<ActionTakenValue> findByDocumentIdWorkflowId(String documentId, String workflowId) {
        return getActionTakenDAO().findByDocumentIdWorkflowId(documentId, workflowId);
    }

    public Collection getActionsTaken(String documentId) {
        return getActionTakenDAO().findByDocumentId(documentId);
    }

    public List findByDocumentIdIgnoreCurrentInd(String documentId) {
        return getActionTakenDAO().findByDocumentIdIgnoreCurrentInd(documentId);
    }

    public void saveActionTaken(ActionTakenValue actionTaken) {
        this.getActionTakenDAO().saveActionTaken(actionTaken);
    }

    public void delete(ActionTakenValue actionTaken) {
        getActionTakenDAO().deleteActionTaken(actionTaken);
    }

    public ActionTakenDAO getActionTakenDAO() {
        return actionTakenDAO;
    }

    public void setActionTakenDAO(ActionTakenDAO actionTakenDAO) {
        this.actionTakenDAO = actionTakenDAO;
    }

    public void deleteByDocumentId(String documentId){
        actionTakenDAO.deleteByDocumentId(documentId);
    }

    public void validateActionTaken(ActionTakenValue actionTaken){
        LOG.debug("Enter validateActionTaken(..)");
        List<WorkflowServiceErrorImpl> errors = new ArrayList<WorkflowServiceErrorImpl>();

        String documentId = actionTaken.getDocumentId();
        if(documentId == null){
            errors.add(new WorkflowServiceErrorImpl("ActionTaken documentid null.", "actiontaken.documentid.empty", actionTaken.getActionTakenId().toString()));
        } else if(getRouteHeaderService().getRouteHeader(documentId) == null){
            errors.add(new WorkflowServiceErrorImpl("ActionTaken documentid invalid.", "actiontaken.documentid.invalid", actionTaken.getActionTakenId().toString()));
        }

        String principalId = actionTaken.getPrincipalId();
        if(StringUtils.isBlank(principalId)){
            errors.add(new WorkflowServiceErrorImpl("ActionTaken personid null.", "actiontaken.personid.empty", actionTaken.getActionTakenId().toString()));
        } else {
        	Principal principal = KimApiServiceLocator.getIdentityService().getPrincipal(principalId);
        	if (principal == null) {
                errors.add(new WorkflowServiceErrorImpl("ActionTaken personid invalid.", "actiontaken.personid.invalid", actionTaken.getActionTakenId().toString()));
            }
        }
        String actionTakenCd = actionTaken.getActionTaken();
        if(actionTakenCd == null || actionTakenCd.trim().equals("")){
            errors.add(new WorkflowServiceErrorImpl("ActionTaken cd null.", "actiontaken.actiontaken.empty", actionTaken.getActionTakenId().toString()));
        } else if(!KewApiConstants.ACTION_TAKEN_CD.containsKey(actionTakenCd)){
            errors.add(new WorkflowServiceErrorImpl("ActionTaken invalid.", "actiontaken.actiontaken.invalid", actionTaken.getActionTakenId().toString()));
        }
        if(actionTaken.getActionDate() == null){
            errors.add(new WorkflowServiceErrorImpl("ActionTaken actiondate null.", "actiontaken.actiondate.empty", actionTaken.getActionTakenId().toString()));
        }

        if(actionTaken.getDocVersion() == null){
            errors.add(new WorkflowServiceErrorImpl("ActionTaken docversion null.", "actiontaken.docverion.empty", actionTaken.getActionTakenId().toString()));
        }
        LOG.debug("Exit validateActionRequest(..) ");
        if (!errors.isEmpty()) {
            throw new WorkflowServiceErrorException("ActionRequest Validation Error", errors);
        }
    }

    public boolean hasUserTakenAction(String principalId, String documentId) {
    	return getActionTakenDAO().hasUserTakenAction(principalId, documentId);
    }

    private RouteHeaderService getRouteHeaderService() {
        return (RouteHeaderService) KEWServiceLocator.getService(KEWServiceLocator.DOC_ROUTE_HEADER_SRV);
    }

    public Timestamp getLastApprovedDate(String documentId)
    {
        return getActionTakenDAO().getLastActionTakenDate(documentId, ActionType.APPROVE);
    }

}
