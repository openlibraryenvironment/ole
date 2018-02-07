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
package org.kuali.rice.kew.util;

import org.apache.log4j.Logger;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.engine.node.BranchState;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import java.util.Date;
import java.util.List;


/**
 * Manages document state in relation to users seeing future requests for a particular document.
 * Construct the object with a document and a user and ask it questions in relation to future requests.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class FutureRequestDocumentStateManager {

    private static final Logger LOG = Logger.getLogger(FutureRequestDocumentStateManager.class);

    private boolean receiveFutureRequests;
    private boolean doNotReceiveFutureRequests;
    private boolean clearFutureRequestState;


    public static final String FUTURE_REQUESTS_VAR_KEY = BranchState.VARIABLE_PREFIX + KewApiConstants.RECEIVE_FUTURE_REQUESTS_BRANCH_STATE_KEY;
    public static final String DEACTIVATED_REQUESTS_VARY_KEY = BranchState.VARIABLE_PREFIX + KewApiConstants.DEACTIVATED_FUTURE_REQUESTS_BRANCH_STATE_KEY;

    public FutureRequestDocumentStateManager (DocumentRouteHeaderValue document, String principalId)
    {
        if (document.getRootBranch() != null) {
        	for (BranchState state : document.getRootBranchState()) {
        	    if (isStateForUser(state, principalId)) {
            		if (isReceiveFutureRequests(state)) {
            		    this.receiveFutureRequests = true;
            		} else if (isDoNotReceiveFutureRequests(state)) {
            		    this.doNotReceiveFutureRequests = true;
            		} else if (isClearFutureRequests(state)) {
            		    this.clearFutureRequestState = true;
            		    this.receiveFutureRequests = false;
            		    this.doNotReceiveFutureRequests = false;
            		    break;
            		}
        	    }
        	}
        }
    	if (this.isClearFutureRequestState()) {
    	    this.clearStateFromDocument(document);
    	}
    }

    public FutureRequestDocumentStateManager (DocumentRouteHeaderValue document, Group kimGroup)
    {
        GroupService ims = KimApiServiceLocator.getGroupService();
        List<String> principalIds =
            ims.getMemberPrincipalIds(kimGroup.getId());

        for (String id : principalIds)
		{
		    FutureRequestDocumentStateManager requestStateMngr =
		        new FutureRequestDocumentStateManager(document, id);
		    if (requestStateMngr.isReceiveFutureRequests()) {
			this.receiveFutureRequests = true;
		    } else if (requestStateMngr.isDoNotReceiveFutureRequests()) {
			this.doNotReceiveFutureRequests = true;
		    }
		}
    }

    protected void clearStateFromDocument(DocumentRouteHeaderValue document) {
        if (document.getRootBranchState() != null) {
        	for (BranchState state : document.getRootBranchState()) {
        	    if (state.getKey().contains(FUTURE_REQUESTS_VAR_KEY)) {
        		String values[] = state.getKey().split(",");
        		state.setKey(DEACTIVATED_REQUESTS_VARY_KEY + "," + values[1] + "," + new Date().toString());
        	    }
        	}
        	KEWServiceLocator.getRouteNodeService().save(document.getRootBranch());
        }
    }

    protected boolean isStateForUser(BranchState state, String principalId)
    {
        String[] values = state.getKey().split(",");
        if (values.length != 4 || ! values[0].contains(FUTURE_REQUESTS_VAR_KEY)) {
            return false;
        }
        String statePrincipalId = values[1];
        return principalId.equals(statePrincipalId);
    }

    protected boolean isReceiveFutureRequests(BranchState state) {
        return state.getValue().equals(KewApiConstants.RECEIVE_FUTURE_REQUESTS_BRANCH_STATE_VALUE);
    }


    protected boolean isDoNotReceiveFutureRequests(BranchState state) {
        return state.getValue().equals(KewApiConstants.DONT_RECEIVE_FUTURE_REQUESTS_BRANCH_STATE_VALUE);
    }

    protected boolean isClearFutureRequests(BranchState state) {
        return state.getValue().equals(KewApiConstants.CLEAR_FUTURE_REQUESTS_BRANCH_STATE_VALUE);
    }

    public boolean isClearFutureRequestState() {
        return this.clearFutureRequestState;
    }

    public boolean isDoNotReceiveFutureRequests() {
        return this.doNotReceiveFutureRequests;
    }

    public boolean isReceiveFutureRequests() {
        return this.receiveFutureRequests;
    }

}
