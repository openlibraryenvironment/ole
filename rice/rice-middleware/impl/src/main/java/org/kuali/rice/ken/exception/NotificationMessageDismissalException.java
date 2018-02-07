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
package org.kuali.rice.ken.exception;

/**
 * This class represents a dismissal exception - when notifications are not properly dismissed 
 * by a deliverer
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NotificationMessageDismissalException extends Exception {
    /**
     * Constructs a NotificationMessageDismissalException instance.
     */
    public NotificationMessageDismissalException() {
        super();
    }

    /**
     * Constructs a NotificationMessageDismissalException instance.
     * @param message
     * @param cause
     */
    public NotificationMessageDismissalException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a NotificationMessageDismissalException instance.
     * @param message
     */
    public NotificationMessageDismissalException(String message) {
        super(message);
    }

    /**
     * Constructs a NotificationMessageDismissalException instance.
     * @param cause
     */
    public NotificationMessageDismissalException(Throwable cause) {
        super(cause);
    }
}
