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
package org.kuali.rice.kew.actionlist;

import org.kuali.rice.kew.api.action.ActionItem;
import org.kuali.rice.kew.api.action.ActionSet;
import org.kuali.rice.kew.api.actionlist.DisplayParameters;



/**
 * The default implementation of a CustomActionListAttribute.  Shows only FYI actions.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DefaultCustomActionListAttribute implements CustomActionListAttribute {

	private static final long serialVersionUID = 6776164670024486696L;

	/**
	 * Sets up the default ActionSet which includes only FYIs.
	 */
	private static ActionSet DEFAULT_LEGAL_ACTIONS = ActionSet.Builder.create().build();
	static {
		DEFAULT_LEGAL_ACTIONS.addFyi();
	}
    
    @Override
	public ActionSet getLegalActions(String principalId, ActionItem actionItem) throws Exception {
    	return DEFAULT_LEGAL_ACTIONS;
	}
    
    @Override
	public DisplayParameters getDocHandlerDisplayParameters(String principalId, ActionItem actionItem) throws Exception {
		return null;
	}
    
}
