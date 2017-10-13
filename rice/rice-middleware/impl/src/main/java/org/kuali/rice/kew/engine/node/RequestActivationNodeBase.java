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
package org.kuali.rice.kew.engine.node;

import java.util.ArrayList;
import java.util.List;

import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.service.impl.NotificationSuppression;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.service.KEWServiceLocator;

/**
 * Abstract superclass for request activation node types
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public abstract class RequestActivationNodeBase implements SimpleNode {

	/**
	 * 
	 * <p>This method takes care of notification for ActionItemS.  It has logic for suppressing notifications a)
	 * during simulations, and b) when the RouteNodeInstance has NodeState specifically hinting for notification
	 * suppression for a given ActionItem.
	 * 
	 * <p>A side effect during non-simulation calls is that any notification suppression NodeStateS will be removed
	 * from the RouteNodeInstance after notifications are sent.
	 * 
	 * @param context
	 * @param actionItems
	 * @param actionRequests
	 * @param routeNodeInstance
	 */
	protected void notify(RouteContext context, List<ActionItem> actionItems, RouteNodeInstance routeNodeInstance) {
		
		if (!context.isSimulation()) {
			new NotificationSuppression().notify(actionItems, routeNodeInstance);
		}
	}

}
