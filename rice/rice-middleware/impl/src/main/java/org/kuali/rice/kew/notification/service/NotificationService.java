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
package org.kuali.rice.kew.notification.service;

import java.util.List;

import org.kuali.rice.kew.api.action.ActionItem;



/**
 * A service which sends notifications regarding the generation of ActionItems.
 * 
 * @see ActionItem
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface NotificationService {

	/**
	 * Send out notifications in regards to the given ActionItems being generated.
	 */
	public void notify(List<ActionItem> actionItems);
	
	/**
	 * Remove notifications for action items that are being deleted
	 * @param actionItems actionItems that are being deleted
	 */
	public void removeNotification(List<ActionItem> actionItems);
}
