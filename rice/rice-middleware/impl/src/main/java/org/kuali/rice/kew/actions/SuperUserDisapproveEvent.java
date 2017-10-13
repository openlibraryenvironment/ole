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

import org.kuali.rice.kew.actionrequest.ActionRequestFactory;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.doctype.DocumentTypePolicy;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;

import java.util.Collection;
import java.util.List;

/**
 * Performs a disapprove as a super user
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SuperUserDisapproveEvent extends SuperUserActionTakenEvent {

    private final boolean sendAcknowledgements;

    public SuperUserDisapproveEvent(DocumentRouteHeaderValue routeHeader, PrincipalContract principal) {
        this(routeHeader, principal, DEFAULT_ANNOTATION, DEFAULT_RUN_POSTPROCESSOR_LOGIC);
    }

    public SuperUserDisapproveEvent(DocumentRouteHeaderValue routeHeader, PrincipalContract principal, String annotation, boolean runPostProcessor) {
        super(KewApiConstants.ACTION_TAKEN_SU_DISAPPROVED_CD, KewApiConstants.SUPER_USER_DISAPPROVE, routeHeader, principal, annotation, runPostProcessor);
        this.sendAcknowledgements = isPolicySet(routeHeader.getDocumentType(), DocumentTypePolicy.SEND_NOTIFICATION_ON_SU_DISAPPROVE);
    }

    @Override
    protected void processActionTaken(ActionTakenValue actionTaken) {
        if (sendAcknowledgements) {
            Collection<ActionRequestValue> actionRequests = actionTaken.getActionRequests();
            if (!actionRequests.isEmpty()) {
               generateAcknowledgementsToPreviousActionTakers(actionRequests.iterator().next().getNodeInstance());
            }
        }
    }

    protected void markDocument() throws WorkflowException {
        //this.event = new DocumentRouteStatusChange(this.documentId, this.getRouteHeader().getAppDocId(), this.getRouteHeader().getDocRouteStatus(), KewApiConstants.ROUTE_HEADER_DISAPPROVED_CD);
        getRouteHeader().markDocumentDisapproved();
        KEWServiceLocator.getRouteHeaderService().saveRouteHeader(getRouteHeader());
    }
}
