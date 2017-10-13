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
package org.kuali.rice.krad.uif.util;

import org.kuali.rice.core.api.util.AbstractKeyValue;
import org.kuali.rice.krad.uif.element.Message;

/**
 * KeyMessage object for key-value pairs that contain rich content in the value portion.  By translating this content
 * to message, the content will be parsed and replaced appropriately for KeyValue controls.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KeyMessage extends AbstractKeyValue {
    private Message message;

    /**
     * Constructor for KeyMessage
     *
     * @param key key
     * @param value value
     * @param message message with messageText set to value
     */
    public KeyMessage(String key, String value, Message message) {
        this.key = key;
        this.value = value;
        this.message = message;
    }

    /**
     * Set the key
     *
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Set the value
     *
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Get the message.  The message will contain the translated/parsed value after its lifecycle executes.
     *
     * @return message with translated structure
     */
    public Message getMessage() {
        return message;
    }

    /**
     * Set the message.  The message should have the rich message value as its messageText
     *
     * @param message
     */
    public void setMessage(Message message) {
        this.message = message;
    }


}
