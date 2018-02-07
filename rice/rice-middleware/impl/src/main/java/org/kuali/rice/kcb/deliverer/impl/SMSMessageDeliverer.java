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
 * This class is responsible for describing the SMS delivery mechanism for
 * the system.  It is not yet fully implemented - this class is just a stub.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SMSMessageDeliverer implements MessageDeliverer {
    private static Logger LOG = Logger.getLogger(SMSMessageDeliverer.class);

    private static final String MOBILE_NUMBER = "sms_mobile_number";

    /**
     * Constructs a SMSMessageDeliverer.java.
     */
    public SMSMessageDeliverer() {
    }

    /**
     * @see org.kuali.rice.kcb.deliverer.MessageDeliverer#deliver(org.kuali.rice.kcb.bo.MessageDelivery)
     */
    public void deliver(MessageDelivery messageDelivery) throws MessageDeliveryException {
    }

    /**
     * @see org.kuali.rice.ken.deliverer.NotificationMessageDeliverer#autoRemoveMessageDelivery(org.kuali.rice.ken.bo.NotificationMessageDelivery)
     */
    /*public void autoRemoveMessageDelivery(NotificationMessageDelivery messageDelivery) throws NotificationAutoRemoveException {
	// we can't remove an sms message once it has been sent
    }*/

    /**
     * @see MessageDeliverer#dismiss(MessageDelivery, String, String)
     */
    public void dismiss(MessageDelivery messageDelivery, String user, String cause) throws MessageDismissalException {
        // we can't remove an sms message once it has been sent
    }

    /**
     * @see org.kuali.rice.kcb.deliverer.MessageDeliverer#getDescription()
     */
    public String getDescription() {
        return "This is the default SMS message delivery type.  Please note that you may incur charges for each SMS message that you receive to your mobile phone.";
    }

    /**
     * @see org.kuali.rice.kcb.deliverer.MessageDeliverer#getName()
     */
    public String getName() {
        return "SMS";
    }

    /**
     * @see org.kuali.rice.kcb.deliverer.MessageDeliverer#getTitle()
     */
    public String getTitle() {
        return "SMS Message Delivery";
    }

    /**
     * This implementation returns an address field.
     * @see org.kuali.rice.kcb.deliverer.MessageDeliverer#getPreferenceKeys()
     */
    public LinkedHashMap getPreferenceKeys() {
        LinkedHashMap<String, String> prefKeys = new LinkedHashMap<String, String>();
        prefKeys.put(MOBILE_NUMBER, "Mobile Phone Number (\"555-555-5555\")");
        return prefKeys;
    }

    /**
     * @see org.kuali.rice.kcb.deliverer.MessageDeliverer#validatePreferenceValues(java.util.HashMap)
     */
    public void validatePreferenceValues(HashMap prefs) throws ErrorList {
        boolean error = false;
        ErrorList errorList = new ErrorList();

        if (!prefs.containsKey(getName()+"."+MOBILE_NUMBER)) {
            errorList.addError("Mobile Phone Number is a required field.");
            error = true;
        } else {
            String mobileNumber = (String) prefs.get(getName()+"."+MOBILE_NUMBER);
            if(StringUtils.isBlank(mobileNumber)) {
                errorList.addError("Mobile Phone Number is a required.");
                error = true;
            }
        }
        if (error) throw errorList;
    }
}
