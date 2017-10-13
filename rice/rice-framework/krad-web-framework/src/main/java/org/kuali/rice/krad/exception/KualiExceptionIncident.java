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

import java.util.Map;

/**
 * This class contains the exception incident information, exception, form and
 * session user. It is constructed and saved into the HTTP Request for passing to the
 * jsp when an exception occurs.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface KualiExceptionIncident {
    /**
     * The error report subject built from current settings and caught exception
     * <p>Value is exceptionReportSubject
     */
     public static final String EXCEPTION_REPORT_SUBJECT="exceptionReportSubject";

     /**
      * Boolean value for incident report display
      */
     public static final String EXCEPTION_HIDE_INCIDENT_REPORT = "exceptionHideIncidentReport";
    /**
     * The error report message
     * <p>Value is exceptionReportMessage
     */
     public static final String EXCEPTION_REPORT_MESSAGE="exceptionReportMessage";
     /**
      * The error message
      * <p>Value is exceptionMessage
      */
      public static final String EXCEPTION_MESSAGE="exceptionMessage";
     /**
      * The error message to be displayed
      * <p>Value is displayMessage
      */
     public static final String DISPLAY_MESSAGE="displayMessage";
     /**
     * Additional message from user
      * <p>Value is description
     */
     public static final String DESCRIPTION="description";
     /**
      * Document id. it's blank if not a document process
      * <p>Value is documentId
      */
     public static final String DOCUMENT_ID="documentId";
     /**
      * Session user email address
      * <p>Value is userEmail
      */
     public static final String USER_EMAIL="userEmail";
     /**
      * Session user login name
      * <p>Value is principalName
      */
     public static final String UUID="principalName";
     /**
      * Session user name
      * <p>Value is userName
      */
     public static final String USER_NAME="userName";
     /**
      * Detail message not for displaying
     * <p>Value is stackTrace
      */
     public static final String STACK_TRACE="stackTrace";
     /**
     * Form that threw the exception
     * <p>Value is componentName
     */
     public static final String COMPONENT_NAME="componentName";

    /**
     * This method return list of {key,value} pairs that each key is the constants
     * defined in this interface.
     *
     * @return
     * <p>Example:
     * <code>
     * documentId, 2942084
     * userEmail, someone@somewhere
     * userName, some name
     * componentFormName, Form that threw exception name
     * exceptionMessage, Error message from exception
     * displayMessage, Either exception error message or generic exception error message
     * stackTrace, Exception stack trace here
     * </code>
     *
     */
    public Map<String, String> toProperties();

    /**
     * This method checks the exception (set during construction) and return errror
     * message if it's Kuali type of exception (defined by the list of exception names).
     * Otherwise, it returns a generic message.
     *
     * @param The caught exception
     * @return
     */
    public String getDisplayMessage(Exception exception);

    /**
     * This method get the specified key value from the implementing class.
     *
     * @param key
     * @return null is return if not found
     */
    public String getProperty(String key);

}
