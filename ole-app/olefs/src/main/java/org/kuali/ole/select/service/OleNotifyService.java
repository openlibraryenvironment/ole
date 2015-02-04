/*
 * Copyright 2012 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.service;

import org.kuali.rice.kew.api.exception.WorkflowException;

import java.util.List;

public interface OleNotifyService {

    /**
     * This method takes List of users and message and send the message as a notification to all the users.
     *
     * @param userRecipients
     * @param message
     * @throws WorkflowException
     */
    public void notify(List<String> userRecipients, String message) throws WorkflowException;
}
