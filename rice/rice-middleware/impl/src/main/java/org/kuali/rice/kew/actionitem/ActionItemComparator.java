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
package org.kuali.rice.kew.actionitem;

import java.util.Comparator;

import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.action.ActionItemContract;
import org.kuali.rice.kew.api.action.RecipientType;

/**
 * Compares an action item to another action item.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ActionItemComparator implements Comparator {

	public int compare(Object object1, Object object2) throws ClassCastException {
		ActionItemContract actionItem1 = (ActionItemContract)object1;
		ActionItemContract actionItem2 = (ActionItemContract)object2;
		int actionCodeValue = ActionRequestValue.compareActionCode(actionItem1.getActionRequestCd(), actionItem2.getActionRequestCd(), true);
		if (actionCodeValue != 0) {
			return actionCodeValue;
		}
		int recipientTypeValue = ActionRequestValue.compareRecipientType(getRecipientTypeCode(actionItem1), getRecipientTypeCode(actionItem2));
		if (recipientTypeValue != 0) {
			return recipientTypeValue;
		}
		return ActionRequestValue.compareDelegationType(actionItem1.getDelegationType(), actionItem2.getDelegationType());
	}

    private String getRecipientTypeCode(Object object) {
        ActionItemContract actionItem = (ActionItemContract)object;
        String recipientTypeCode = RecipientType.PRINCIPAL.getCode();
        if (actionItem.getRoleName() != null) {
            recipientTypeCode = RecipientType.ROLE.getCode();
        }
        if (actionItem.getGroupId() != null) {
            recipientTypeCode = RecipientType.GROUP.getCode();
        }
        return recipientTypeCode;
    }

}
