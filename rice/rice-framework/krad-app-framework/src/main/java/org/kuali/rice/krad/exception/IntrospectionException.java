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

/**
 * Represents an exception that is thrown during some reflection or introspection-related task
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class IntrospectionException extends RuntimeException {

    private static final long serialVersionUID = -5155596091679664527L;

    /**
     * @param message
     */
    public IntrospectionException(String message) {
        super(message);
    }

    /**
     * @param message
     */
    public IntrospectionException(String message, Throwable t) {
        super(message, t);
    }
}
