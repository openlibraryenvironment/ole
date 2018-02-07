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
package org.kuali.rice.kcb.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.kuali.rice.kcb.bo.Message;
import org.kuali.rice.kcb.service.MessageService;

/**
 * MessageService implementation 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class MessageServiceImpl extends BusinessObjectServiceImpl implements MessageService {
    /**
     * @see org.kuali.rice.kcb.service.MessageService#deleteMessage(org.kuali.rice.kcb.bo.Message)
     */
    public void deleteMessage(Message message) {
        dao.delete(message);
    }

    /**
     * @see org.kuali.rice.kcb.service.MessageService#getMessage(java.lang.Long)
     */
    public Message getMessage(Long id) {
        Map<String, Object> fields = new HashMap<String, Object>(1);
        fields.put(Message.ID_FIELD, id);
        Message m = (Message) dao.findByPrimaryKey(Message.class, fields);
        return m;
    }

    /**
     * @see org.kuali.rice.kcb.service.MessageService#getAllMessages()
     */
    public Collection<Message> getAllMessages() {
        return dao.findAll(Message.class);
    }

    /**
     * @see org.kuali.rice.kcb.service.MessageService#saveMessage(org.kuali.rice.kcb.bo.Message)
     */
    public void saveMessage(Message message) {
        dao.save(message);
    }

    /**
     * @see org.kuali.rice.kcb.service.MessageService#getMessageByOriginId(java.lang.String)
     */
    public Message getMessageByOriginId(String originId) {
        Map<String, Object> fields = new HashMap<String, Object>(1);
        fields.put(Message.ORIGINID_FIELD, originId);
        Collection<Message> messages = dao.findMatching(Message.class, fields);
        if (messages.size() == 0) {
            return null;
        }
        return messages.iterator().next();
    }
}
