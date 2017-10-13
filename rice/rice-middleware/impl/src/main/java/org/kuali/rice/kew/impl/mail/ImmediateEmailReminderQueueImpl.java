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
package org.kuali.rice.kew.impl.mail;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.action.ActionItem;
import org.kuali.rice.kew.api.mail.ImmediateEmailReminderQueue;
import org.kuali.rice.kew.mail.service.ActionListEmailService;
import org.kuali.rice.kew.service.KEWServiceLocator;

/**
 * Reference implementation of an {@code ImmediateEmailReminderQueue}.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ImmediateEmailReminderQueueImpl implements ImmediateEmailReminderQueue {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
            .getLogger(ImmediateEmailReminderQueueImpl.class);

    private ActionListEmailService actionListEmailService;

    public void sendReminder(ActionItem actionItem, Boolean skipOnApprovals) {
        if (actionItem == null) {
			throw new RiceIllegalArgumentException("actionItem was null");
		}

        if (skipOnApprovals == null) {
            skipOnApprovals = false;
		}

        getActionListEmailService().sendImmediateReminder(actionItem, skipOnApprovals);
    }

    private ActionListEmailService getActionListEmailService() {
        if (actionListEmailService == null)
            actionListEmailService = (ActionListEmailService) KEWServiceLocator.getService(KEWServiceLocator.ACTION_LIST_EMAIL_SERVICE);
    
        return actionListEmailService;
    }
    
    /**
     * @param actionListEmailService the actionListEmailService to set
     */
    public void setActionListEmailService(ActionListEmailService actionListEmailService) {
        this.actionListEmailService = actionListEmailService;
    }
    
}
