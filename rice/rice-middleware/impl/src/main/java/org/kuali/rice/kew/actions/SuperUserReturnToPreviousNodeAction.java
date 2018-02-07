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

import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;



/**
 * Does a return to previous as a superuser
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SuperUserReturnToPreviousNodeAction extends SuperUserActionTakenEvent {
    
    private String nodeName;
    
    public SuperUserReturnToPreviousNodeAction(DocumentRouteHeaderValue routeHeader, PrincipalContract principal) {
        super(KewApiConstants.ACTION_TAKEN_SU_RETURNED_TO_PREVIOUS_CD, KewApiConstants.SUPER_USER_RETURN_TO_PREVIOUS_ROUTE_LEVEL, routeHeader, principal);
    }
    
    public SuperUserReturnToPreviousNodeAction(DocumentRouteHeaderValue routeHeader, PrincipalContract principal, String annotation, boolean runPostProcessor, String nodeName) {
        super(KewApiConstants.ACTION_TAKEN_SU_RETURNED_TO_PREVIOUS_CD, KewApiConstants.SUPER_USER_RETURN_TO_PREVIOUS_ROUTE_LEVEL, routeHeader, principal, annotation, runPostProcessor);
        this.nodeName = nodeName;
    }
    
    protected void markDocument() throws WorkflowException {
        if (getRouteHeader().isInException()) {
            //this.event = new DocumentRouteStatusChange(this.documentId, this.getRouteHeader().getAppDocId(), this.getRouteHeader().getDocRouteStatus(), KewApiConstants.ROUTE_HEADER_ENROUTE_CD);
            getRouteHeader().markDocumentEnroute();
        }
        ReturnToPreviousNodeAction returnAction = new ReturnToPreviousNodeAction(this.getActionTakenCode(), getRouteHeader(), getPrincipal(), annotation, nodeName, true, isRunPostProcessorLogic());
        returnAction.setSuperUserUsage(true);
        returnAction.performAction();
    }
    
    protected ActionTakenValue processActionRequests() throws InvalidActionTakenException {
        //do nothing
        return null;
    }

}
