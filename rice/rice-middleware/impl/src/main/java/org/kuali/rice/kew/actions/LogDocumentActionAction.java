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

import org.apache.log4j.MDC;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;

import java.util.List;


/**
 * Simply records an action taken with an annotation.  
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LogDocumentActionAction extends ActionTakenEvent {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(LogDocumentActionAction.class);

    /**
     * @param rh RouteHeader for the document upon which the action is taken.
     * @param principal User taking the action.
     */
    public LogDocumentActionAction(DocumentRouteHeaderValue rh, PrincipalContract principal) {
        super(KewApiConstants.ACTION_TAKEN_LOG_DOCUMENT_ACTION_CD, rh, principal);
    }

    /**
     * @param rh RouteHeader for the document upon which the action is taken.
     * @param principal User taking the action.
     * @param annotation User comment on the action taken
     */
    public LogDocumentActionAction(DocumentRouteHeaderValue rh, PrincipalContract principal, String annotation) {
        super(KewApiConstants.ACTION_TAKEN_LOG_DOCUMENT_ACTION_CD, rh, principal, annotation);
    }

    /* (non-Javadoc)
     * @see org.kuali.rice.kew.actions.ActionTakenEvent#validateActionRules()
     */
    @Override
    public String validateActionRules() {
        // log action is always valid so return no error message
        return "";
    }

    /**
     * Records the non-routed document action. - Checks to make sure the document status allows the action. Records the action.
     * 
     * @throws InvalidActionTakenException
     */
    public void recordAction() throws InvalidActionTakenException {
        MDC.put("docId", getRouteHeader().getDocumentId());

        String errorMessage = validateActionRules();
        if (!org.apache.commons.lang.StringUtils.isEmpty(errorMessage)) {
            throw new InvalidActionTakenException(errorMessage);
        }

        LOG.debug("Logging document action");
        ActionTakenValue actionTaken = saveActionTaken(Boolean.TRUE);
        // LogDocumentAction should not contact the PostProcessor which is why we don't call notifyActionTaken
       
    }

	@Override
	public String validateActionRules(List<ActionRequestValue> actionRequests) {
        // log action is always valid so return no error message
		return "";
	}
}
