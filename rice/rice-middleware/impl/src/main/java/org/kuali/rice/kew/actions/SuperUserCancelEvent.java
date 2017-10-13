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

import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;



/**
 * performs a cancel action as a super user
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SuperUserCancelEvent extends SuperUserActionTakenEvent {
    
    public SuperUserCancelEvent(DocumentRouteHeaderValue routeHeader, PrincipalContract principal) {
        super(KewApiConstants.ACTION_TAKEN_SU_CANCELED_CD, KewApiConstants.SUPER_USER_CANCEL, routeHeader, principal);
    }

    public SuperUserCancelEvent(DocumentRouteHeaderValue routeHeader, PrincipalContract principal, String annotation, boolean runPostProcessor) {
        super(KewApiConstants.ACTION_TAKEN_SU_CANCELED_CD, KewApiConstants.SUPER_USER_CANCEL, routeHeader, principal, annotation, runPostProcessor);
    }

    protected void markDocument() throws WorkflowException {
        //this.event = new DocumentRouteStatusChange(this.documentId, this.getRouteHeader().getAppDocId(), this.getRouteHeader().getDocRouteStatus(), KewApiConstants.ROUTE_HEADER_CANCEL_CD);
        getRouteHeader().markDocumentCanceled();
        KEWServiceLocator.getRouteHeaderService().saveRouteHeader(getRouteHeader());
    }
}
