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

import org.kuali.rice.krad.exception.KualiExceptionIncident;

import java.util.Map;

/**
 * This is used for sending report of an incident
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface KualiExceptionIncidentService extends KualiFeedbackService {

    /**
     * This method send email to the defined mailing list using the exception incident
     * instance.
     *
     * @param exceptionIncident
     * @throws Exception
     */
    public void report(KualiExceptionIncident exceptionIncident) throws Exception;

    /**
     * This method create an instance of the KualiExceptionIncident from its factory.
     *
     * @param exception
     * @param properties Additional information when the exception is thrown
     * <p>example:
     * <ul>
     * <li>Document id</li>
     * <li>User email</li>
     * <li>User name</li>
     * <li>Component name</li>
     * </ul>
     * @return
     */
    public KualiExceptionIncident getExceptionIncident(
            Exception exception, Map<String, String> properties);

    /**
     * This method create an instance of the KualiExceptionIncident from its factory.
     * This method is used when the thrown exception is not available. It's an implicit
     * initialization.
     *
     * @param properties The list of name-value pairs containing the thrown exception
     * information
     * @return
     */
    public KualiExceptionIncident getExceptionIncident(Map<String, String> properties);
}
