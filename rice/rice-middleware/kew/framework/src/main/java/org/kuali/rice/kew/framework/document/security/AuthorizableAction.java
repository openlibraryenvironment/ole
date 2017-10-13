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

import org.kuali.rice.kew.api.action.ActionType;

/**
 * Encapsulates the type of authorization check the DocumentTypeAuthorizer is making.
 * @since 2.1.3
 */
public class AuthorizableAction {
    /**
     * The authorization check type: either a document action, initiation,
     * or super user approve action request check
     */
    public static enum CheckType {
        ACTION,
        INITIATION,
        SU_APPROVE_ACTION_REQUEST
    }

    /**
     * The document ActionType if application (CheckType.ACTION)
     */
    public final ActionType actionType;
    /**
     * The CheckType
     */
    public final CheckType type;

    /**
     * Construct AuthorizableAction for a document action
     * @param actionType the document action type
     */
    public AuthorizableAction(ActionType actionType) {
        this(CheckType.ACTION, actionType);
    }

    /**
     * Construct AuthorizableAction for non-action CheckType
     * @param checkType
     */
    public AuthorizableAction(CheckType checkType) {
        this(checkType, null);
    }

    public AuthorizableAction(CheckType checkType, ActionType actionType) {
        if (checkType == null) {
            throw new IllegalArgumentException("CheckType must not be null");
        }
        // if we have specified an action check without an action type
        // or a non-action check with an action type
        // this is an illegal combination
        if ((checkType == CheckType.ACTION) == (actionType == null)) {
            throw new IllegalArgumentException("ActionType must be specified with ACTION CheckType");
        }
        this.type = checkType;
        this.actionType = actionType;
    }
}