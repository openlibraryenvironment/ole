/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kew.routemodule;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TestResponsibility {

    private String actionRequested;
    private int priority = -1;
    private TestRecipient recipient;
    private List delegations = new ArrayList();
    
    public String getActionRequested() {
        return actionRequested;
    }
    public void setActionRequested(String actionRequested) {
        this.actionRequested = actionRequested;
    }
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public List getDelegations() {
        return delegations;
    }
    public void setDelegations(List delegations) {
        this.delegations = delegations;
    }
    public TestRecipient getRecipient() {
        return recipient;
    }
    public void setRecipient(TestRecipient recipient) {
        this.recipient = recipient;
    }
    
}
