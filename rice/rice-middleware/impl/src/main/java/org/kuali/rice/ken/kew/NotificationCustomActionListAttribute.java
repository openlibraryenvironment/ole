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
package org.kuali.rice.ken.kew;

import org.kuali.rice.kew.api.action.ActionItem;
import org.kuali.rice.kew.actionlist.CustomActionListAttribute;
import org.kuali.rice.kew.api.action.ActionSet;
import org.kuali.rice.kew.api.actionlist.DisplayParameters;


/**
 * This class is our custom action list for Notifications in KEW.  It's wired in as the implementation to use as part of the NotificationData.xml 
 * bootstrap file for KEW.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NotificationCustomActionListAttribute implements CustomActionListAttribute {

    @Override
	public DisplayParameters getDocHandlerDisplayParameters(String principalId, ActionItem actionItem) throws Exception {
	DisplayParameters dp = DisplayParameters.Builder.create(new Integer(400)).build();
	return dp;
    }

    @Override
	public ActionSet getLegalActions(String principalId,ActionItem actionItem) throws Exception {
	ActionSet as = ActionSet.Builder.create().build();
	as.addFyi();
	return as;
    }

}
