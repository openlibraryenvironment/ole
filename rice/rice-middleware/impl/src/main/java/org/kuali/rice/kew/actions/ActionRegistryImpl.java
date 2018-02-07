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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.reflect.DataDefinition;
import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.core.framework.resourceloader.ObjectDefinitionResolver;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.action.ActionRequestStatus;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.exception.ResourceUnavailableException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * A simple implementation of an ActionRegistry which includes all of the default Workflow Actions.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ActionRegistryImpl implements ActionRegistry {
    private static final Logger LOG = Logger.getLogger(ActionRegistryImpl.class);

	private static Map<String, String> actionMap = new HashMap<String, String>();
	static {
		actionMap.put(KewApiConstants.ACTION_TAKEN_ACKNOWLEDGED_CD, AcknowledgeAction.class.getName());
		actionMap.put(KewApiConstants.ACTION_TAKEN_ADHOC_CD, AdHocAction.class.getName());
		actionMap.put(KewApiConstants.ACTION_TAKEN_ADHOC_REVOKED_CD, RevokeAdHocAction.class.getName());
		actionMap.put(KewApiConstants.ACTION_TAKEN_APPROVED_CD, ApproveAction.class.getName());
		actionMap.put(KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD, BlanketApproveAction.class.getName());
		actionMap.put(KewApiConstants.ACTION_TAKEN_CANCELED_CD, CancelAction.class.getName());
		actionMap.put(KewApiConstants.ACTION_TAKEN_COMPLETED_CD, CompleteAction.class.getName());
        actionMap.put(KewApiConstants.ACTION_TAKEN_ROUTED_CD, RouteDocumentAction.class.getName());
		actionMap.put(KewApiConstants.ACTION_TAKEN_DENIED_CD, DisapproveAction.class.getName());
		actionMap.put(KewApiConstants.ACTION_TAKEN_FYI_CD, ClearFYIAction.class.getName());
		actionMap.put(KewApiConstants.ACTION_TAKEN_LOG_DOCUMENT_ACTION_CD, LogDocumentActionAction.class.getName());
		actionMap.put(KewApiConstants.ACTION_TAKEN_MOVE_CD, MoveDocumentAction.class.getName());
		actionMap.put(KewApiConstants.ACTION_TAKEN_TAKE_WORKGROUP_AUTHORITY_CD, TakeWorkgroupAuthority.class.getName());
		actionMap.put(KewApiConstants.ACTION_TAKEN_RELEASE_WORKGROUP_AUTHORITY_CD, ReleaseWorkgroupAuthority.class.getName());
		actionMap.put(KewApiConstants.ACTION_TAKEN_RETURNED_TO_PREVIOUS_CD, ReturnToPreviousNodeAction.class.getName());
		actionMap.put(KewApiConstants.ACTION_TAKEN_SAVED_CD, SaveActionEvent.class.getName());
		//actionMap.put(KewApiConstants.ACTION_TAKEN_SU_ACTION_REQUEST_ACKNOWLEDGED_CD, SuperUserActionRequestAcknowledgeEvent.class.getName());
		actionMap.put(KewApiConstants.ACTION_TAKEN_SU_ACTION_REQUEST_APPROVED_CD, SuperUserActionRequestApproveEvent.class.getName());
		//actionMap.put(KewApiConstants.ACTION_TAKEN_SU_ACTION_REQUEST_COMPLETED_CD, SuperUserActionRequestCompleteEvent.class.getName());
		//actionMap.put(KewApiConstants.ACTION_TAKEN_SU_ACTION_REQUEST_FYI_CD, SuperUserActionRequestFYIEvent.class.getName());
		actionMap.put(KewApiConstants.ACTION_TAKEN_SU_APPROVED_CD, SuperUserApproveEvent.class.getName());
		actionMap.put(KewApiConstants.ACTION_TAKEN_SU_CANCELED_CD, SuperUserCancelEvent.class.getName());
		actionMap.put(KewApiConstants.ACTION_TAKEN_SU_DISAPPROVED_CD, SuperUserDisapproveEvent.class.getName());
		actionMap.put(KewApiConstants.ACTION_TAKEN_SU_RETURNED_TO_PREVIOUS_CD, SuperUserReturnToPreviousNodeAction.class.getName());
		actionMap.put(KewApiConstants.ACTION_TAKEN_SU_ROUTE_LEVEL_APPROVED_CD, SuperUserNodeApproveEvent.class.getName());
        actionMap.put(ActionType.RECALL.getCode(), RecallAction.class.getName());
	}

	public void registerAction(String actionCode, String actionClass) {
		if (actionClass == null) {
			throw new IllegalArgumentException("Action Code '" + actionCode + "' cannot be registered with a null action class.");
		}
		if (actionMap.containsKey(actionCode)) {
			throw new WorkflowRuntimeException("Action Code is already in use.  [" +
					actionCode + ", " + actionClass + "].  "+
					"Please unregister the existing implementation first.");
		}
		actionMap.put(actionCode, actionClass);
	}

	public void unregisterAction(String actionCode) {
		actionMap.remove(actionCode);
	}

	public Map getActionMap() {
		return Collections.unmodifiableMap(actionMap);
	}

	/* (non-Javadoc)
	 * @see org.kuali.rice.kew.actions.ActionRegistry#createAction(java.lang.String, java.util.List)
	 */
	public ActionTakenEvent createAction(String actionCode, List<DataDefinition> parameters) throws ResourceUnavailableException {
		String actionClassName = actionMap.get(actionCode);
		if (actionClassName == null) {
			throw new IllegalArgumentException("No action has been registered for the given action code of '" + actionCode + "'.");
		}
		ObjectDefinition actionDefinition = new ObjectDefinition(actionClassName);
		if (parameters != null && !parameters.isEmpty()) {
			actionDefinition.setConstructorParameters(parameters);
		}
		try {
			//ActionTakenEvent actionTaken = (ActionTakenEvent)GlobalResourceLoader.getResourceLoader().getObject(actionDefinition);
			// TODO ActionTakenEvent is not an interface so we can't fetch them through the GlobalResourceLoader, for now, just use
			// the ObjectDefinitionResolver
			ActionTakenEvent actionTaken = (ActionTakenEvent) ObjectDefinitionResolver.createObject(actionDefinition, ClassLoaderUtils.getDefaultClassLoader(), false);
			if (actionTaken == null) {
				// TODO the exception handling here is a bit wonky
				throw new ResourceUnavailableException("Could not locate action taken class '" + actionClassName + "'");
			}
			return actionTaken;
		} catch (Exception e) {
            LOG.debug("createAction() Exception thrown while working with action class name '" + actionClassName + "'");
			if (e instanceof ResourceUnavailableException) {
				throw (ResourceUnavailableException)e;
			}
			throw new ResourceUnavailableException(e);
		}
	}

    public org.kuali.rice.kew.api.action.ValidActions getValidActions(PrincipalContract principal, DocumentRouteHeaderValue document) {
    	try {
    		org.kuali.rice.kew.api.action.ValidActions.Builder builder = org.kuali.rice.kew.api.action.ValidActions.Builder.create();
    		List<ActionRequestValue> activeRequests = new ArrayList<ActionRequestValue>();
            // this looks like ActionRequestServiceImpl.findAllPendingRequests
    		for ( ActionRequestValue ar : document.getActionRequests() ) {
    			if ( (ar.getCurrentIndicator() != null && ar.getCurrentIndicator()) && StringUtils.equals( ar.getStatus(), ActionRequestStatus.ACTIVATED.getCode() ) ) {
    				activeRequests.add(ar);
    			}
    		}
    		for (String actionTakenCode : actionMap.keySet())
    		{
    			List<DataDefinition> parameters = new ArrayList<DataDefinition>();
    			parameters.add(new DataDefinition(document));
    			parameters.add(new DataDefinition(principal));
    			ActionTakenEvent actionEvent = createAction(actionTakenCode, parameters);
    			if (StringUtils.isEmpty(actionEvent.validateActionRules(activeRequests)))
    			{
    				builder.addValidAction(ActionType.fromCode(actionTakenCode));
    			}
    		}
    		return builder.build();
    	} catch (ResourceUnavailableException e) {
    		throw new WorkflowRuntimeException(e);
    	}
    }
}
