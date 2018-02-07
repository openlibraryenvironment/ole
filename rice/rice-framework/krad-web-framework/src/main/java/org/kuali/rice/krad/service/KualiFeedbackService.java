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
package org.kuali.rice.krad.service;

/**
 * This new feedback service was added to refactor
 * KualiExceptionIncidentService.  Now the KualiExceptionIncidentService
 * extends this service so that exception reporting is considered to be a type
 * of feedback.  Both services share the emailReport method which formats and
 * sends an email to the appropriate list.
 */
public interface KualiFeedbackService {

	/**
     * This property must be defined in the base configuration file for specifying
     * the mailing list for the report to be sent.
     * <p>Example:
     * <code>
     * <param name="KualiFeedbackService.REPORT_MAIL_LIST">a@y,b@z</param>
     * </code>
     */
    public static final String REPORT_MAIL_LIST = String.format("%s.REPORT_MAIL_LIST", KualiFeedbackService.class.getSimpleName());

	/**
     * This method send email to the defined mailing list with a specified subject and
     * message.
     *
     * @param subject
     * @param message
     * @throws Exception
     */
    public void emailReport(String subject, String message) throws Exception;

    public void sendFeedback(String documentId, String componentName, String description) throws Exception;
}
