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
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.action.ActionRequestStatus;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import java.util.List;



/**
 * This is the inverse of the {@link TakeWorkgroupAuthority} action.  This puts the document back
 * in all the peoples action lists that have the document routed to them.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ReleaseWorkgroupAuthority extends ActionTakenEvent {

    private String groupId;
    /**
     * @param routeHeader
     * @param principal
     */
    public ReleaseWorkgroupAuthority(DocumentRouteHeaderValue routeHeader, PrincipalContract principal) {
        super(KewApiConstants.ACTION_TAKEN_RELEASE_WORKGROUP_AUTHORITY_CD, routeHeader, principal);
    }

    /**
     * @param routeHeader
     * @param principal
     * @param annotation
     * @param groupId
     */
    public ReleaseWorkgroupAuthority(DocumentRouteHeaderValue routeHeader, PrincipalContract principal, String annotation, String groupId) {
        super(KewApiConstants.ACTION_TAKEN_RELEASE_WORKGROUP_AUTHORITY_CD, routeHeader, principal, annotation);
        this.groupId = groupId;
    }

    /* (non-Javadoc)
     * @see org.kuali.rice.kew.actions.ActionTakenEvent#validateActionRules()
     */
    @Override
    public String validateActionRules() {
        if (groupId == null) {
            return "User cannot Release Workgroup Authority without a given workgroup";
        } else {
            return performReleaseWorkgroupAuthority(true);
        }
    }
    
    /**
     * This overridden method ...
     * 
     * @see org.kuali.rice.kew.actions.ActionTakenEvent#validateActionRules(java.util.List)
     */
    @Override
    public String validateActionRules(List<ActionRequestValue> actionRequests) {
    	return validateActionRules();
    }

    public void recordAction() throws InvalidActionTakenException {
        String error = performReleaseWorkgroupAuthority(false);
        if (!org.apache.commons.lang.StringUtils.isEmpty(error)) {
            throw new InvalidActionTakenException(error);
        }

        queueDocumentProcessing();
    }

    private String performReleaseWorkgroupAuthority(boolean forValidationOnly) {
        if (!KimApiServiceLocator.getGroupService().isMemberOfGroup(getPrincipal().getPrincipalId(), groupId)){
            return (getPrincipal().getPrincipalName() + " not a member of workgroup " + groupId);
        }

        List<ActionRequestValue> actionRequests = getActionRequestService().findPendingByDoc(getDocumentId());
        //List groupRequestsToActivate = new ArrayList();//requests for this group that need action items
        for (ActionRequestValue actionRequest : actionRequests)
        {
            //we left the group active from take authority action.  pending havent been normally activated yet
            if (actionRequest.isGroupRequest() && actionRequest.isActive() && actionRequest.getGroupId().equals(groupId)) {
            	List<ActionItem> actionItems = actionRequest.getActionItems();
                if (actionItems.size() == 1) {
                    ActionItem actionItem = actionItems.get(0);
                    if (! actionItem.getPrincipalId().equals(getPrincipal().getPrincipalId())) {
                        return "User attempting to release workgroup authority did not take it.";
                    } else if (!forValidationOnly)
                    {
                        actionRequest.setStatus(ActionRequestStatus.INITIALIZED.getCode());//to circumvent check in service during activation
                        getActionRequestService().activateRequest(actionRequest);
                    }
                }
            }
        }
        return "";
    }
}
