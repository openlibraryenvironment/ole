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
package org.kuali.rice.krad.datadictionary.exception;

/**
 * This class represents an UnknownDocumentTypeException.
 */
public class UnknownDocumentTypeException extends RuntimeException {
    private static final long serialVersionUID = -148438432691990699L;

    /**
     * Create an UnknownDocumentTypeException with the given message
     *
     * @param message
     */
    public UnknownDocumentTypeException(String message) {
        super(message);

    }

    /**
     * Create an UnknownDocumentTypeException with the given message and cause
     *
     * @param message
     * @param cause
     */
    public UnknownDocumentTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
