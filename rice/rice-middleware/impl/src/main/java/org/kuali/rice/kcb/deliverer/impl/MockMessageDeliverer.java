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
package org.kuali.rice.kcb.deliverer.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.kuali.rice.kcb.bo.MessageDelivery;
import org.kuali.rice.kcb.deliverer.MessageDeliverer;
import org.kuali.rice.kcb.exception.ErrorList;
import org.kuali.rice.kcb.api.exception.MessageDeliveryException;
import org.kuali.rice.kcb.api.exception.MessageDismissalException;

/**
 * A mock message deliverer that does nothing 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class MockMessageDeliverer implements MessageDeliverer {
    /**
     * Map of deliveries
     */
    private final Map<Long, MessageDelivery> deliveries = new HashMap<Long, MessageDelivery>();

    /**
     * @see org.kuali.rice.kcb.deliverer.MessageDeliverer#deliver(org.kuali.rice.kcb.bo.MessageDelivery)
     */
    public void deliver(MessageDelivery messageDelivery) throws MessageDeliveryException {
        deliveries.put(messageDelivery.getId(), messageDelivery);
    }

    /**
     * @see org.kuali.rice.kcb.deliverer.MessageDeliverer#dismiss(org.kuali.rice.kcb.bo.MessageDelivery, java.lang.String, java.lang.String)
     */
    public void dismiss(MessageDelivery messageDelivery, String user, String cause) throws MessageDismissalException {
        deliveries.remove(messageDelivery.getId());

    }

    /**
     * @see org.kuali.rice.kcb.deliverer.MessageDeliverer#getDescription()
     */
    public String getDescription() {
        return getName();
    }

    /**
     * @see org.kuali.rice.kcb.deliverer.MessageDeliverer#getName()
     */
    public String getName() {
        return "Mock";
    }

    /**
     * @see org.kuali.rice.kcb.deliverer.MessageDeliverer#getPreferenceKeys()
     */
    public LinkedHashMap<String, String> getPreferenceKeys() {
        // none for now
        return new LinkedHashMap<String, String>();
    }

    /**
     * @see org.kuali.rice.kcb.deliverer.MessageDeliverer#getTitle()
     */
    public String getTitle() {
        return getName();
    }

    /**
     * @see org.kuali.rice.kcb.deliverer.MessageDeliverer#validatePreferenceValues(java.util.HashMap)
     */
    public void validatePreferenceValues(HashMap prefs) throws ErrorList {
        // no validation for now
    }
}
