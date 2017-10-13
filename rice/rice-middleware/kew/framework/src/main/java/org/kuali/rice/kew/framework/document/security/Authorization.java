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
package org.kuali.rice.kew.framework.document.security;

/**
 * Authorization response for document routing authorization checks
 *
 * @see org.kuali.rice.kew.framework.document.security.DocumentTypeAuthorizer
 */
public final class Authorization {
    /**
     * Whether the action was authorized
     */
    private final boolean authorized;
    /**
     * Authorization or authorization failure reason
     */
    private final String reason;

    /**
     * Short-hand constructor for authorization without a reason
     * @param authorized whether the authorization check succeeded
     */
    public Authorization(boolean authorized) {
        this.authorized = authorized;
        this.reason = null;
    }

    /**
     * Construct authorization response with a reason
     * @param authorized whether the authorization check succeeded
     * @param reason reason message
     */
    public Authorization(boolean authorized, String reason) {
        this.authorized = authorized;
        this.reason = reason;
    }

    /**
     * @return whether authorization succeeded
     */
    public boolean isAuthorized() {
        return authorized;
    }

    /**
     * @return success or failure reason
     */
    public String getReason() {
        return reason;
    }
}