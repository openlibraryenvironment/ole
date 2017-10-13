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

import java.util.List;
import java.util.Map;

import org.kuali.rice.core.api.reflect.DataDefinition;
import org.kuali.rice.kew.api.exception.ResourceUnavailableException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;



/**
 * Maintains the registry of Workflow Actions.  Actions are (currently) identified by a one-letter
 * action code and map to a Class which should extend the org.kuali.rice.kew.actions.ActionTakenEvent class.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ActionRegistry {
	
	/**
	 * Registers the given Action.
	 * 
	 * @param actionCode Should be a one-letter unique code 
	 * @param actionClass the fully-qualified Java classname of the ActionTakenEvent implementation
	 * 
	 * @throws IllegalArgumentException if the actionCode is not one character or already in use,
	 * 	also throws this exception if the actionClass is null
	 */
	public void registerAction(String actionCode, String actionClass);
	
	/**
	 * Unregisters the Action with the given code.
	 */
	public void unregisterAction(String actionCode);
	
	/**
	 * Returns an immutable map of the Action Code to Action Class mappings in this registry.
	 */
	public Map getActionMap();

	/**
	 * Constructs and returns the ActionTakenEvent implementation which can be used to invoke the
	 * Action with the given parameters.

	 * @throws org.kuali.rice.kew.api.exception.ResourceUnavailableException if the action class cannot be constructed
	 * @throws IllegalArgumentException if the given actionCode has not been registered
	 */
	public ActionTakenEvent createAction(String actionCode, List<DataDefinition> parameters) throws ResourceUnavailableException;
	
    /**
     * Returns a List of valid action codes for the given user on the document.
     *
     * @throws ResourceUnavailableException if an action class cannot be constructed
     */
    public org.kuali.rice.kew.api.action.ValidActions getValidActions(PrincipalContract principal, DocumentRouteHeaderValue document);

}
