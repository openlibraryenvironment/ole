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

/**
 * Represents an UnknownDocumentIdException
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UnknownDocumentIdException extends KualiException {
    private static final long serialVersionUID = -7973140950936642618L;

    /**
     * Create an UnknownDocumentIdException with the given message
     *
     * @param message
     */
    public UnknownDocumentIdException(String message) {
        super(message);

    }

    /**
     * Create an UnknownDocumentIdException with the given message and cause
     *
     * @param message
     * @param cause
     */
    public UnknownDocumentIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
