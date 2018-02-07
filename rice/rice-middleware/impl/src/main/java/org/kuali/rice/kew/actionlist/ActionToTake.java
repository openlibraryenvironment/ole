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

import java.io.Serializable;

/**
 * Represents a mass action taken from the action list
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ActionToTake implements Serializable {

	private String actionItemId;
    private String actionTakenCd;
    
    public String getActionItemId() {
		return actionItemId;
	}

	public void setActionItemId(String actionItemId) {
		this.actionItemId = actionItemId;
	}

	public String getActionTakenCd() {
        return actionTakenCd;
    }

    public void setActionTakenCd(String actionTakenCd) {
        this.actionTakenCd = actionTakenCd;
    }


}
