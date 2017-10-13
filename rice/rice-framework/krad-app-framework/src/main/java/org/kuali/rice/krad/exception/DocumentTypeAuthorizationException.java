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

import org.kuali.rice.core.api.util.RiceKeyConstants;

import java.util.Collections;
import java.util.Map;

/**
 * Represents an exception that is thrown when a given user is not authorized to take a given action on the given
 * document type
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentTypeAuthorizationException extends AuthorizationException {
    private static final long serialVersionUID = 1548057953855152103L;

    public DocumentTypeAuthorizationException(String userId, String action, String documentType) {
        this(userId, action, documentType, Collections.<String, Object>emptyMap());
    }

    public DocumentTypeAuthorizationException(String userId, String action, String documentType,
            Map<String, Object> requestAuthDetails) {
        super(userId, action, documentType, requestAuthDetails);
    }

    /**
     * @see AuthorizationException#getErrorMessageKey()
     */
    public String getErrorMessageKey() {
        return RiceKeyConstants.AUTHORIZATION_ERROR_DOCTYPE;
    }
}
