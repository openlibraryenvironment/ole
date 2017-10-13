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
 * Use this when the data dictionary cannot find a matching business object's attribute entry for a populated business
 * object that
 * runs through the data dictionary validation service and its methods.
 */
public class UnknownBusinessClassAttributeException extends RuntimeException {
    private static final long serialVersionUID = -2021739544938001742L;

    /**
     * Create an UnknownBusinessClassAttributeException with the given message
     *
     * @param message
     */
    public UnknownBusinessClassAttributeException(String message) {
        super(message);
    }

    /**
     * Create an UnknownBusinessClassAttributeException with the given message and cause
     *
     * @param message
     * @param cause
     */
    public UnknownBusinessClassAttributeException(String message, Throwable cause) {
        super(message, cause);
    }
}
