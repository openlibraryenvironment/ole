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
package org.kuali.rice.kcb.service;

import java.util.Collection;

import org.kuali.rice.kcb.bo.Message;

/**
 * The MessageService class is responsible various functions regarding the 
 * Message records that exist within the system.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface MessageService {
    /**
     * Saves a message
     * @param message a Message
     */
    public void saveMessage(Message message);
    
    /**
     * Deletes a message
     * @param message a Message
     */
    public void deleteMessage(Message message);

    /**
     * Finds a message by id
     * @param id the message id
     * @return the message object if found
     */
    public Message getMessage(Long id);
    
    /**
     * Finds a message by origin id
     * @param id the origin message id
     * @return the message object if found
     */
    public Message getMessageByOriginId(String originId);

    /**
     * Returns all messages in the system
     * @return all messages in the system
     */
    public Collection<Message> getAllMessages();
}
