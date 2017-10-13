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

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.exception.KualiException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains the exception incident information, exception, form and
 * session user. It is constructed and saved into the HTTP Request for passing to the
 * jsp when an exception occurs.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ExceptionIncident implements KualiExceptionIncident {
    private static final Logger LOG = Logger.getLogger(ExceptionIncident.class);
    public static final String GENERIC_SYSTEM_ERROR_MESSAGE = "The system has" +
    		" encountered an error and is unable to complete your request at this time."+
            " Please provide more information regarding this error by completing"+
            " this Incident Report.";

    /**
     * Basic exception information is initialized and contained in this class instance.
     * Additional setting and other information can be added when exception is caught.
     * Also, an JSP is displayed to collect additional user information and the returned
     * parameters from the JSP can be used to initialize this class instance for
     * reporting.
     * <p>Note: The mechanism for passing data to and receiving data from the JSP uses
     * java.util.Map. Therefore, the exception is not passed to JSP using HttpRequest.
     * But rather a Map instance.
     */
    protected Map<String, String> properties=new HashMap<String, String>();

    /**
     * This constructs list of key-value pairs from the caught exception and current
     * settings.
     *
     * @param exception Caught exception
     * @param exceptionNames List of Kuali exception for determining the display exception
     * message
     * @param properties Input information when the exception is caught
     * <p>Example:
     * <ul>
     * <li>DOCUMENT_ID</li>
     * <li>USER_EMAIL</li>
     * <li>USER_NAME</li>
     * <li>COMPONENT_NAME</li>
     * </ul>
     */
    public ExceptionIncident(Exception exception,
            Map<String,String> properties) {
        if (LOG.isTraceEnabled()) {
            String message=String.format("ENTRY %s%n%s",
                    (exception==null)?"null":exception.toString(),
                    (properties==null)?"":properties.toString());
            LOG.trace(message);
        }

        initialize(exception, properties);

        if (LOG.isTraceEnabled()) {
            String message=String.format("EXIT %s", this.properties);
            LOG.trace(message);
        }

    }

    /**
     * This constructs an instance of this class from list of name-value pairs.
     *
     * @param inputs List of exception information such as
     * <ul>
     * <li>DOCUMENT_ID - If it's document form</li>
     * <li>USER_EMAIL - Session user email</li>
     * <li>USER_NAME - Session user name</li>
     * <li>COMPONENT_NAME - Document or lookup or inquiry form</li>
     * attribute of GlobalVariables</li>
     * <li>EXCEPTION_REPORT_SUBJECT - Exception error message and current settings</li>
     * <li>EXCEPTION_MESSAGE - Exception error message</li>
     * <li>STACK_TRACE - Exception stack trace</li>
     * <li>DESCRIPTION - Information input by user or blank</li>
     * </ul>
     */
    public ExceptionIncident(Map<String, String> inputs) {

        this.properties=inputs;

    }

    /**
     * This method create and populate the internal properties parameter.
     *
     * @param thrownException The caught exception
     * @param inputs Input information when the exception is caught
     * <p>Example:
     * <ul>
     * <li>DOCUMENT_ID</li>
     * <li>USER_EMAIL</li>
     * <li>USER_NAME</li>
     * <li>COMPONENT_NAME</li>
     * </ul>
     */
    private void initialize(Exception thrownException, Map<String, String> inputs) {
        if (LOG.isTraceEnabled()) {
            String lm=String.format("ENTRY %s%n%s",
                    thrownException.getMessage(),
                    (inputs==null)?"null":inputs.toString());
            LOG.trace(lm);
        }

        properties=new HashMap<String, String>();
        // Add all inputs
        if (inputs != null) {
            properties.putAll(inputs);
        }
        // Add all exception information
        properties.putAll(getExceptionInfo(thrownException));

        if (LOG.isTraceEnabled()) {
            String lm=String.format("EXIT %s", properties.toString());
            LOG.trace(lm);
        }
    }

    /**
     * This method return list of required information provided by the caught exception.
     *
     * @return
     * <p>Example:
     * <code>
     * exceptionSubject, Caught exception message and settings information
     * exceptionMessage, Caught exception message
     * displayMessage, Either exception error message or generic exception error message
     * stackTrace, Exception stack trace here
     * </code>
     *
     */
    private Map<String, String> getExceptionInfo(Exception exception) {
        if (LOG.isTraceEnabled()) {
            String message=String.format("ENTRY");
            LOG.trace(message);
        }

        Map<String, String> map=new HashMap<String, String>();
        map.put(EXCEPTION_REPORT_SUBJECT, createReportSubject(exception));
        map.put(EXCEPTION_MESSAGE, exception.getMessage());
        map.put(DISPLAY_MESSAGE, getDisplayMessage(exception));
        map.put(STACK_TRACE, getExceptionStackTrace(exception));
        if(exception instanceof KualiException){
        	boolean hideIncidentReport = ((KualiException) exception).isHideIncidentReport();
        	map.put(EXCEPTION_HIDE_INCIDENT_REPORT, String.valueOf(hideIncidentReport));
        }else{
        	map.put(EXCEPTION_HIDE_INCIDENT_REPORT, String.valueOf(false));
        }

        if (LOG.isTraceEnabled()) {
            String message=String.format("ENTRY %s", map.toString());
            LOG.trace(message);
        }

        return map;
    }

    /**
     * This method compose the exception information that includes
     * <ul>
     * <li>environment - Application environment</li>
     * <li>componentName- Document or lookup or inquiry form</li>
     * <li>errorMessage - Exception error message</li>
     * </ul>
     * <p>Example;
     * <code>
     * kr-dev:SomeForm:Some error message
     * </code>
     *
     * @param exception The caught exception
     * @return
     */
    private String createReportSubject(Exception exception) {
        if (LOG.isTraceEnabled()) {
            String lm=String.format("ENTRY");
            LOG.trace(lm);
        }
        String app = CoreApiServiceLocator.getKualiConfigurationService().
				getPropertyValueAsString("application.id");
        String env= CoreApiServiceLocator.getKualiConfigurationService().
                getPropertyValueAsString("environment");
        String format="%s:%s:%s:%s";
        String componentName=properties.get(COMPONENT_NAME);
        String subject=String.format(format,
        		app,
                env,
                (componentName==null)?"":componentName,
                exception.getMessage());

        if (LOG.isTraceEnabled()) {
            String lm=String.format("EXIT %s", subject);
            LOG.trace(lm);
        }

        return subject;
    }

    /**
     * This method compose the exception information that includes
     * <ul>
     * <li>documentId - If it's document form</li>
     * <li>userEmail - Session user email</li>
     * <li>userName - Session user name</li>
     * <li>component - Document or lookup or inquiry form</li>
     * <li>description - Information input by user or blank</li>
     * <li>errorMessage - Exception error message</li>
     * <li>stackTrace - Exception stack trace</li>
     * </ul>
     * <p>Example;
     * <code>
     * documentId: 2942084
     * userEmail: someone@somewhere
     * userName: some name
     * description: Something went wrong!
     * component: document
     * errorMessage: Some error message
     * stackTrace: Exception stack trace here
     * </code>
     *
     * @return
     */
    private String createReportMessage() {
        if (LOG.isTraceEnabled()) {
            String lm=String.format("ENTRY");
            LOG.trace(lm);
        }

        String documentId=properties.get(DOCUMENT_ID);
        String userEmail=properties.get(USER_EMAIL);
        String uuid=properties.get(UUID);
        String description=properties.get(DESCRIPTION);
        String component=properties.get(COMPONENT_NAME);
        String exceptionMessage=properties.get(EXCEPTION_MESSAGE);
        String stackTrace=properties.get(STACK_TRACE);
        String format="Document Id: %s%n"+
                      "User Email: %s%n"+
                      "Person User Identifier: %s%n"+
                      "User Input: %s%n"+
                      "component: %s%n"+
                      "errorMessage: %s%n"+
                      "%s%n";
        String message=String.format(format,
                (documentId==null)?"":documentId,
                (userEmail==null)?"":userEmail,
                (uuid==null)?"":uuid,
                (description==null)?"":description,
                (component==null)?"":component,
                (exceptionMessage==null)?"":exceptionMessage,
                (stackTrace==null)?"":stackTrace);

        if (LOG.isTraceEnabled()) {
            String lm=String.format("EXIT %s", message);
            LOG.trace(lm);
        }

        return message;
    }

    /**
     * This method return the thrown exception stack trace as string.
     *
     * @param thrownException
     * @return
     */
    public String getExceptionStackTrace(Exception thrownException) {
        if (LOG.isTraceEnabled()) {
            String lm=String.format("ENTRY");
            LOG.trace(lm);
        }

        StringWriter wrt=new StringWriter();
        PrintWriter pw=new PrintWriter(wrt);
        thrownException.printStackTrace(pw);
        pw.flush();
        String stackTrace=wrt.toString();
        try {
            wrt.close();
            pw.close();
        } catch (Exception e) {
            LOG.trace(e.getMessage(), e);
        }

        if (LOG.isTraceEnabled()) {
            String lm=String.format("EXIT %s", stackTrace);
            LOG.trace(lm);
        }

        return stackTrace;
    }

    /**
     * This overridden method return the exception if the ixception type is in the
     * defined list. Otherwise, it returns the GENERIC_SYSTEM_ERROR_MESSAGE.
     *
     * @see org.kuali.rice.krad.exception.KualiExceptionIncident#getDisplayMessage(Exception)
     */
    public String getDisplayMessage(Exception exception) {
        if (LOG.isTraceEnabled()) {
            String message=String.format("ENTRY %s", exception.getMessage());
            LOG.trace(message);
        }

        // Create the display message
        String displayMessage;
        if (exception instanceof KualiException) {
            displayMessage=exception.getMessage();
        } else {
            displayMessage=GENERIC_SYSTEM_ERROR_MESSAGE;
        }

        if (LOG.isTraceEnabled()) {
            String message=String.format("EXIT %s", displayMessage);
            LOG.trace(message);
        }

        return displayMessage;
    }

    /**
     * This overridden method returns value of the found property key. Except the
     * property EXCEPTION_REPORT_MESSAGE
     *
     * @see org.kuali.rice.krad.exception.KualiExceptionIncident#getProperty(java.lang.String)
     */
    public String getProperty(String key) {
        if (LOG.isTraceEnabled()) {
            String message=String.format("ENTRY %s", key);
            LOG.trace(message);
        }

        String value;
        if (key.equals(EXCEPTION_REPORT_MESSAGE) && !properties.containsKey(key)) {
            value=createReportMessage();
            properties.put(EXCEPTION_REPORT_MESSAGE, value);
        } else {
            value=properties.get(key);
        }

        if (LOG.isTraceEnabled()) {
            String message=String.format("EXIT %s", value);
            LOG.trace(message);
        }

        return value;
    }

    /**
     * This overridden method return current internal properties.
     *
     * @see org.kuali.rice.krad.exception.KualiExceptionIncident#toProperties()
     */
    public Map<String, String> toProperties() {
        if (LOG.isTraceEnabled()) {
            String message=String.format("ENTRY");
            LOG.trace(message);
        }

        if (LOG.isTraceEnabled()) {
            String message=String.format("EXIT %s", properties.toString());
            LOG.trace(message);
        }

        return properties;
    }
}
