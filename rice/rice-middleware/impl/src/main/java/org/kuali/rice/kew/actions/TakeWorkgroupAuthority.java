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
package org.kuali.rice.kew.actions;

import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionlist.service.ActionListService;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/**
 * Removes all workgroup action items for a document from everyone's action list except the person
 * who took the workgroup authority
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class TakeWorkgroupAuthority extends ActionTakenEvent {
    
    private String groupId;
    
    /**
     * @param routeHeader
     * @param principal
     */
    public TakeWorkgroupAuthority(DocumentRouteHeaderValue routeHeader, PrincipalContract principal) {
        super(KewApiConstants.ACTION_TAKEN_TAKE_WORKGROUP_AUTHORITY_CD, routeHeader, principal);
    }

    /**
     * @param routeHeader
     * @param principal
     * @param annotation
     * @param groupId
     */
    public TakeWorkgroupAuthority(DocumentRouteHeaderValue routeHeader, PrincipalContract principal, String annotation, String groupId) {
        super(KewApiConstants.ACTION_TAKEN_TAKE_WORKGROUP_AUTHORITY_CD, routeHeader, principal, annotation);
        this.groupId = groupId;
    }

    /* (non-Javadoc)
     * @see org.kuali.rice.kew.actions.ActionTakenEvent#validateActionRules()
     */
    @Override
    public String validateActionRules() {
        if  ( (groupId != null) && (!KimApiServiceLocator.getGroupService().isMemberOfGroup(getPrincipal().getPrincipalId(), groupId))) {
            return (getPrincipal().getPrincipalName() + " not a member of workgroup " + groupId);
        }
        return "";
    }

    @Override
    public String validateActionRules(List<ActionRequestValue> actionRequests) {
    	return validateActionRules();
    }
    
    public void recordAction() throws InvalidActionTakenException {

        String errorMessage = validateActionRules();
        if (!org.apache.commons.lang.StringUtils.isEmpty(errorMessage)) {
            throw new InvalidActionTakenException(errorMessage);
        }
//        if (! workgroup.hasMember(getUser())) {
//            throw new InvalidActionTakenException(getUser().getPrincipalName() + " not a member of workgroup " + workgroup.getDisplayName());
//        }

        List<ActionRequestValue> documentRequests = getActionRequestService().findPendingByDoc(getDocumentId());
        List<ActionRequestValue> workgroupRequests = new ArrayList<ActionRequestValue>();
        for (ActionRequestValue actionRequest : documentRequests)
        {
            if (actionRequest.isGroupRequest() && actionRequest.getGroup().getId().equals(groupId))
            {
                workgroupRequests.add(actionRequest);
            }
        }

        ActionTakenValue actionTaken = saveActionTaken(findDelegatorForActionRequests(workgroupRequests));
        notifyActionTaken(actionTaken);

        ActionListService actionListService = KEWServiceLocator.getActionListService();
        Collection<ActionItem> actionItems = actionListService.findByDocumentId(getDocumentId());
        for (ActionItem actionItem : actionItems)
        {
            //delete all requests for this workgroup on this document not to this user
            if (actionItem.isWorkgroupItem() && actionItem.getGroupId().equals(groupId) &&
                    !actionItem.getPrincipalId().equals(getPrincipal().getPrincipalId()))
            {
                actionListService.deleteActionItem(actionItem);
            }
        }
    }
}
