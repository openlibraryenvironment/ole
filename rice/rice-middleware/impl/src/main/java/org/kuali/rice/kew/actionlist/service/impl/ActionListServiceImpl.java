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
package org.kuali.rice.kew.actionlist.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionitem.ActionItemActionListExtension;
import org.kuali.rice.kew.actionitem.OutboxItemActionListExtension;
import org.kuali.rice.kew.actionitem.dao.ActionItemDAO;
import org.kuali.rice.kew.actionlist.ActionListFilter;
import org.kuali.rice.kew.actionlist.dao.ActionListDAO;
import org.kuali.rice.kew.actionlist.service.ActionListService;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.actionrequest.service.ActionRequestService;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.exception.WorkflowServiceErrorException;
import org.kuali.rice.kew.exception.WorkflowServiceErrorImpl;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.useroptions.UserOptions;
import org.kuali.rice.kew.useroptions.UserOptionsService;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

/**
 * Default implementation of the {@link ActionListService}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ActionListServiceImpl implements ActionListService {

    protected final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(getClass());

    private ActionListDAO actionListDAO;

    private ActionItemDAO actionItemDAO;

    public Collection<Recipient> findUserSecondaryDelegators(String principalId) {
        return getActionItemDAO().findSecondaryDelegators(principalId);
    }

    public Collection<Recipient> findUserPrimaryDelegations(String principalId) {
        return getActionItemDAO().findPrimaryDelegationRecipients(principalId);
    }

    public Collection<ActionItemActionListExtension> getActionList(String principalId, ActionListFilter filter) {
        return getActionListDAO().getActionList(principalId, filter);
    }

    public Collection<ActionItemActionListExtension> getActionListForSingleDocument(String documentId) {
        return getActionListDAO().getActionListForSingleDocument(documentId);
    }

    public void setActionListDAO(ActionListDAO actionListDAO) {
        this.actionListDAO = actionListDAO;
    }

    public ActionListDAO getActionListDAO() {
        return actionListDAO;
    }

    public void deleteActionItem(ActionItem actionItem) {
        deleteActionItem(actionItem, false);
    }

    public void deleteActionItem(ActionItem actionItem, boolean forceIntoOutbox) {
        getActionItemDAO().deleteActionItem(actionItem);
        // remove notification from KCB
        KEWServiceLocator.getNotificationService().removeNotification(Collections.singletonList(ActionItem.to(actionItem)));
        this.saveOutboxItem(actionItem, forceIntoOutbox);
    }

    public void deleteByDocumentId(String documentId) {
        getActionItemDAO().deleteByDocumentId(documentId);
    }

    public Collection<ActionItem> findByDocumentId(String documentId) {
        return getActionItemDAO().findByDocumentId(documentId);
    }

    public Collection<ActionItem> findByActionRequestId(String actionRequestId) {
        return getActionItemDAO().findByActionRequestId(actionRequestId);
    }

    public Collection<ActionItem> findByWorkflowUserDocumentId(String workflowUserId, String documentId) {
        return getActionItemDAO().findByWorkflowUserDocumentId(workflowUserId, documentId);
    }

    public Collection<ActionItem> findByDocumentTypeName(String documentTypeName) {
        return getActionItemDAO().findByDocumentTypeName(documentTypeName);
    }

    public ActionItem createActionItemForActionRequest(ActionRequestValue actionRequest) {
        ActionItem actionItem = new ActionItem();

        DocumentRouteHeaderValue routeHeader = actionRequest.getRouteHeader();
        DocumentType docType = routeHeader.getDocumentType();

        actionItem.setActionRequestCd(actionRequest.getActionRequested());
        actionItem.setActionRequestId(actionRequest.getActionRequestId());
        actionItem.setDocName(docType.getName());
        actionItem.setRoleName(actionRequest.getQualifiedRoleName());
        actionItem.setPrincipalId(actionRequest.getPrincipalId());
        actionItem.setDocumentId(actionRequest.getDocumentId());
        actionItem.setDateAssigned(new Timestamp(new Date().getTime()));
        actionItem.setDocHandlerURL(docType.getResolvedDocumentHandlerUrl());
        actionItem.setDocLabel(docType.getLabel());
        actionItem.setDocTitle(routeHeader.getDocTitle());
        actionItem.setGroupId(actionRequest.getGroupId());
        actionItem.setResponsibilityId(actionRequest.getResponsibilityId());
        actionItem.setDelegationType(actionRequest.getDelegationType());
        actionItem.setRequestLabel(actionRequest.getRequestLabel());

        ActionRequestValue delegatorActionRequest = getActionRequestService().findDelegatorRequest(actionRequest);
        if (delegatorActionRequest != null) {
            actionItem.setDelegatorPrincipalId(delegatorActionRequest.getPrincipalId());
            actionItem.setDelegatorGroupId(delegatorActionRequest.getGroupId());
        }

        return actionItem;
    }


    public void updateActionItemsForTitleChange(String documentId, String newTitle) {
        Collection<ActionItem> items = getActionItemDAO().findByDocumentId(documentId);
        for (Iterator<ActionItem> iterator = items.iterator(); iterator.hasNext();) {
            ActionItem item = iterator.next();
            item.setDocTitle(newTitle);
            saveActionItem(item);
        }
    }

    public void saveActionItem(ActionItem actionItem) {
        getActionItemDAO().saveActionItem(actionItem);
    }

    public ActionItemDAO getActionItemDAO() {
        return actionItemDAO;
    }

    public ActionRequestService getActionRequestService() {
        return (ActionRequestService) KEWServiceLocator.getActionRequestService();
    }

    public GroupService getGroupService(){
        return KimApiServiceLocator.getGroupService();
    }

    public void setActionItemDAO(ActionItemDAO actionItemDAO) {
        this.actionItemDAO = actionItemDAO;
    }


    public void validateActionItem(ActionItem actionItem) {
        List<WorkflowServiceErrorImpl> errors = new ArrayList<WorkflowServiceErrorImpl>();
        String principalId = actionItem.getPrincipalId();
        if (principalId == null || principalId.trim().equals("")) {
            errors.add(new WorkflowServiceErrorImpl("ActionItem person null.", "actionitem.personid.empty", actionItem
                    .getId().toString()));
        } else {
            Principal principal = KimApiServiceLocator.getIdentityService().getPrincipal(principalId);
            if (principal == null) {
                errors.add(new WorkflowServiceErrorImpl("ActionItem person invalid.", "actionitem.personid.invalid",
                        actionItem.getId().toString()));
            }
        }

        if (actionItem.getDateAssigned() == null) {
            errors.add(new WorkflowServiceErrorImpl("ActionItem date assigned empty.", "actionitem.dateassigned.empty",
                    actionItem.getId().toString()));
        }

        String actionRequestCd = actionItem.getActionRequestCd();
        if (actionRequestCd == null || actionRequestCd.trim().equals("")) {
            errors.add(new WorkflowServiceErrorImpl("ActionItem action request cd empty.",
                    "actionitem.actionrequestcd.empty", actionItem.getId().toString()));
        } else if (!KewApiConstants.ACTION_REQUEST_CD.containsKey(actionRequestCd)) {
            errors.add(new WorkflowServiceErrorImpl("ActionItem action request cd invalid.",
                    "actionitem.actionrequestcd.invalid", actionItem.getId().toString()));
        }

        if (actionItem.getActionRequestId() == null) {
            errors.add(new WorkflowServiceErrorImpl("ActionItem action request id empty.",
                    "actionitem.actionrequestid.empty", actionItem.getId().toString()));
        }

        if (actionItem.getDocumentId() == null) {
            errors.add(new WorkflowServiceErrorImpl("ActionItem Document id empty.", "actionitem.documentid.empty",
                    actionItem.getId().toString()));
        } else if (KEWServiceLocator.getRouteHeaderService().getRouteHeader(actionItem.getDocumentId()) == null) {
            errors.add(new WorkflowServiceErrorImpl("ActionItem Document id invalid.", "actionitem.documentid.invalid",
                    actionItem.getId().toString()));
        }

        String docTypeName = actionItem.getDocName();
        DocumentType docType = null;
        if (docTypeName == null || docTypeName.trim().equals("")) {
            errors.add(new WorkflowServiceErrorImpl("ActionItem doctypename empty.", "actionitem.doctypename.empty",
                    actionItem.getId().toString()));
        } else {
            docType = KEWServiceLocator.getDocumentTypeService().findByName(actionItem.getDocName());
            if (docType == null) {
                errors.add(new WorkflowServiceErrorImpl("ActionItem doctypename invalid.", "actionitem.doctypename.invalid",
                        actionItem.getId().toString()));
            }
        }

        if (actionItem.getDocLabel() == null || actionItem.getDocLabel().trim().equals("")) {
            errors.add(new WorkflowServiceErrorImpl("ActionItem doctypelabel empty.", "actionitem.doctypelabel.empty",
                    actionItem.getId().toString()));
        } else if (docType != null && !docType.getLabel().equals(actionItem.getDocLabel())) {
            errors.add(new WorkflowServiceErrorImpl("ActionItem doctypelabel no match.", "actionitem.doctypelabel.nomatch",
                    actionItem.getId().toString()));
        }

        // first check to see if the document type has an empty document handler url
        if (StringUtils.isNotBlank(docType.getResolvedDocumentHandlerUrl())) {
            if (actionItem.getDocHandlerURL() == null || actionItem.getDocHandlerURL().trim().equals("")) {
                errors.add(new WorkflowServiceErrorImpl("ActionItem doc handler url empty.", "actionitem.dochdrurl.empty",
                        actionItem.getId().toString()));
            } else if (docType != null && !docType.getResolvedDocumentHandlerUrl().equals(actionItem.getDocHandlerURL())) {
                errors.add(new WorkflowServiceErrorImpl("ActionItem doc handler url no match.", "actionitem.dochdrurl.nomatch",
                        actionItem.getId().toString()));
            }
        } else {
            // if the doc type doc handler url is blank, verify that the action item doc handler url is also blank
            if (StringUtils.isNotBlank(actionItem.getDocHandlerURL())) {
                errors.add(new WorkflowServiceErrorImpl("ActionItem doc handler url not empty.", "actionitem.dochdrurl.not.empty",
                        actionItem.getId().toString()));
            }
        }

        if (!errors.isEmpty()) {
            throw new WorkflowServiceErrorException("ActionItem Validation Error", errors);
        }
    }

    public ActionItem findByActionItemId(String actionItemId) {
        return getActionItemDAO().findByActionItemId(actionItemId);
    }

    public int getCount(String principalId) {
        return getActionListDAO().getCount(principalId);
    }

    public List<Object> getMaxActionItemDateAssignedAndCountForUser(String principalId) {
        return getActionListDAO().getMaxActionItemDateAssignedAndCountForUser(principalId);
    }

    /**
     *
     * This overridden method ...
     *
     * @see org.kuali.rice.kew.actionlist.service.ActionListService#getOutbox(java.lang.String, org.kuali.rice.kew.actionlist.ActionListFilter)
     */
    public Collection<OutboxItemActionListExtension> getOutbox(String principalId, ActionListFilter filter) {
        return this.getActionListDAO().getOutbox(principalId, filter);
    }

    public Collection<OutboxItemActionListExtension> getOutboxItemsByDocumentType(String documentTypeName) {
        return this.getActionItemDAO().getOutboxItemsByDocumentType(documentTypeName);
    }

    /**
     * This overridden method ...
     *
     * @see org.kuali.rice.kew.actionlist.service.ActionListService#removeOutboxItems(String, java.util.List)
     */
    public void removeOutboxItems(String principalId, List<String> outboxItems) {
        this.getActionListDAO().removeOutboxItems(principalId, outboxItems);
    }

    public void saveOutboxItem(ActionItem actionItem) {
        saveOutboxItem(actionItem, false);
    }

    /**
     *
     * save the ouboxitem unless the document is saved or the user already has the item in their outbox.
     *
     * @see org.kuali.rice.kew.actionlist.service.ActionListService#saveOutboxItem(org.kuali.rice.kew.actionitem.ActionItem, boolean)
     */
    public void saveOutboxItem(ActionItem actionItem, boolean forceIntoOutbox) {
        UserOptionsService userOptionsService = KEWServiceLocator.getUserOptionsService();
        Boolean isUsingOutBox = true;
        List<UserOptions> options = userOptionsService.findByUserQualified(actionItem.getPrincipalId(), KewApiConstants.USE_OUT_BOX);
        if (options == null || options.isEmpty()){
            isUsingOutBox = true;
        } else {
            for (Iterator iter = options.iterator(); iter.hasNext();) {
                UserOptions u = (UserOptions) iter.next();
                if (u.getOptionVal() == null || !(u.getOptionVal().equals("yes"))){
                    isUsingOutBox = false;
                }
            }
        }

        if (isUsingOutBox
                && ConfigContext.getCurrentContextConfig().getOutBoxOn()
                && getActionListDAO().getOutboxByDocumentIdUserId(actionItem.getDocumentId(), actionItem.getPrincipalId()) == null
                && !KEWServiceLocator.getRouteHeaderService().getRouteHeader(actionItem.getDocumentId()).getDocRouteStatus().equals(
                KewApiConstants.ROUTE_HEADER_SAVED_CD)) {

            // only create an outbox item if this user has taken action on the document
            ActionRequestValue actionRequest = KEWServiceLocator.getActionRequestService().findByActionRequestId(
                    actionItem.getActionRequestId());
            ActionTakenValue actionTaken = actionRequest.getActionTaken();
            // if an action was taken...
            if (forceIntoOutbox || (actionTaken != null && actionTaken.getPrincipalId().equals(actionItem.getPrincipalId()))) {
                this.getActionListDAO().saveOutboxItem(new OutboxItemActionListExtension(actionItem));
            }

        }
    }

    public Collection<ActionItem> findByPrincipalId(String principalId) {
        return getActionItemDAO().findByPrincipalId(principalId);
    }

}
