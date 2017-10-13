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
package org.kuali.rice.krad.exception;

import org.kuali.rice.core.api.exception.KualiException;
import org.kuali.rice.core.api.util.RiceKeyConstants;

import java.util.Collections;
import java.util.Map;

/**
 * Represents an exception that is thrown when a given user is not authorized to take the given action on the given
 * target type
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AuthorizationException extends KualiException {
    private static final long serialVersionUID = -3874239711783179351L;

    protected final String userId;
    protected final String action;
    protected final String targetType;
    protected final Map<String, Object> additionalDetails;

    public AuthorizationException(String userId, String action, String targetType) {
        this(userId, action, targetType, Collections.<String, Object>emptyMap());
    }

    /**
     * Constructs a exception with a message from the passed in information.
     *
     * @param userId the userid of the user who failed authorization
     * @param action the action the user was trying to take
     * @param targetType what the user was trying to take action on
     * @param additionalDetails additional details about the authorization failure to be passed in and added to the
     * exception message (ex: permission name, qualifiers, etc.)
     */
    public AuthorizationException(String userId, String action, String targetType,
            Map<String, Object> additionalDetails) {
        this(userId, action, targetType, "user '" + userId + "' is not authorized to take action '" + action
                + "' on targets of type '" + targetType + "'"
                + (additionalDetails != null && !additionalDetails.isEmpty() ?
                " Additional Details : " + additionalDetails : ""), additionalDetails);
    }

    /**
     * Allows you to construct the exception message manually
     */
    public AuthorizationException(String userId, String action, String targetType, String message,
            Map<String, Object> additionalDetails) {
        super(message);

        this.userId = userId;
        this.action = action;
        this.targetType = targetType;
        this.additionalDetails = additionalDetails;
    }

    public String getUserId() {
        return userId;
    }

    public String getAction() {
        return action;
    }

    public String getTargetType() {
        return targetType;
    }

    public Map<String, Object> getAdditionalDetails() {
        return additionalDetails;
    }

    /**
     * @return message key used by Struts to select the error message to be displayed
     * @deprecated
     */
    @Deprecated
    public String getErrorMessageKey() {
        return RiceKeyConstants.AUTHORIZATION_ERROR_GENERAL;
    }
}
