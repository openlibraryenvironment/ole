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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.kcb.bo.MessageDelivery;
import org.kuali.rice.kcb.deliverer.MessageDeliverer;
import org.kuali.rice.kcb.exception.ErrorList;
import org.kuali.rice.kcb.api.exception.MessageDeliveryException;
import org.kuali.rice.kcb.api.exception.MessageDismissalException;

/**
 * This class is responsible for describing the AOL Instant Messenger delivery mechanism for
 * the system; however, it is not yet integrated into the system and is just a stub.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AOLInstantMessageDeliverer implements MessageDeliverer {
    private static Logger LOG = Logger.getLogger(AOLInstantMessageDeliverer.class);

    private static final String SCREEN_NAME = "aim_screen_name";

    /**
     * Constructs a AOLInstantMessageDeliverer.java.
     */
    public AOLInstantMessageDeliverer() {
    }

    /**
     * @see org.kuali.rice.kcb.deliverer.MessageDeliverer#deliver(org.kuali.rice.kcb.bo.MessageDelivery)
     */
    public void deliver(MessageDelivery messageDelivery) throws MessageDeliveryException {
    }

    /**
     * @see org.kuali.rice.kcb.deliverer.MessageDeliverer#dismiss(org.kuali.rice.kcb.bo.MessageDelivery, java.lang.String, java.lang.String)
     */
    public void dismiss(MessageDelivery messageDelivery, String user, String cause) throws MessageDismissalException {
    }

    /**
     * @see org.kuali.rice.kcb.deliverer.MessageDeliverer#getDescription()
     */
    public String getDescription() {
        return "This is the default AOL Instant Messenger delivery type.";
    }

    /**
     * @see org.kuali.rice.kcb.deliverer.MessageDeliverer#getName()
     */
    public String getName() {
        return "AIM";
    }

    /**
     * @see org.kuali.rice.kcb.deliverer.MessageDeliverer#getTitle()
     */
    public String getTitle() {
        return "AOL Instant Messenger Delivery";
    }

    /**
     * This implementation returns a screen name field.
     * @see org.kuali.rice.kcb.deliverer.MessageDeliverer#getPreferenceKeys()
     */
    public LinkedHashMap getPreferenceKeys() {
        LinkedHashMap<String, String> prefKeys = new LinkedHashMap<String, String>();
        prefKeys.put(SCREEN_NAME, "AIM Screen Name");
        return prefKeys;
    }

    /**
     * @see org.kuali.rice.kcb.deliverer.MessageDeliverer#validatePreferenceValues(java.util.HashMap)
     */
    public void validatePreferenceValues(HashMap prefs) throws ErrorList {
        boolean error = false;
        ErrorList errorList = new ErrorList();

        if (!prefs.containsKey(getName()+"."+SCREEN_NAME)) {
            errorList.addError("AIM Screen Name is a required field.");
            error = true;
        } else {
            String screenName = (String) prefs.get(getName()+"."+SCREEN_NAME);
            if(StringUtils.isBlank(screenName)) {
                errorList.addError("AIM Screen Name is a required.");
                error = true;
            }
        }
        if (error) throw errorList;
    }
}
