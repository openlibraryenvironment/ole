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
package org.kuali.rice.krad.messages;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Test cases for {@link MessageServiceImpl}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MessageServiceImplTest {

    /**
     * Test that list of messages are being correctly merged, messages of same
     * type should not be overridden
     */
    @Test
    public void testMergeMessages() {
        Collection<Message> messages1 = new ArrayList<Message>();
        Collection<Message> messages2 = new ArrayList<Message>();

        MessageBuilder builder = MessageBuilder.create("Default", "All", "Test Message");

        Message message1 = builder.build();

        builder.setKey("Test Message 2");
        Message message2 = builder.build();

        builder.setComponentCode("TestView");
        Message message3 = builder.build();

        builder.setNamespaceCode("KR-SAP");
        Message message4 = builder.build();

        Message message5 = builder.build();

        messages1.add(message1);
        messages1.add(message2);
        messages1.add(message4);

        messages2.add(message1);
        messages2.add(message3);
        messages2.add(message4);
        messages2.add(message5);

        MessageServiceImpl messageServiceImpl = new MessageServiceImpl();
        messageServiceImpl.mergeMessages(messages1, messages2);

        assertEquals("Merged map is not correct size", 5, messages1.size());

    }

    public static class MessageBuilder {
        private String namespaceCode;
        private String componentCode;
        private String key;
        private String locale;
        private String description;
        private String text;

        public MessageBuilder(String namespaceCode, String componentCode, String key) {
            setNamespaceCode(namespaceCode);
            setComponentCode(componentCode);
            setKey(key);
        }

        public static MessageBuilder create(String namespaceCode, String componentCode, String key) {
            return new MessageBuilder(namespaceCode, componentCode, key);
        }

        public Message build() {
            Message message = new Message();

            message.setNamespaceCode(getNamespaceCode());
            message.setComponentCode(getComponentCode());
            message.setKey(getKey());
            message.setText(getText());
            message.setDescription(getDescription());
            message.setLocale(getLocale());

            return message;
        }

        public String getNamespaceCode() {
            return namespaceCode;
        }

        public void setNamespaceCode(String namespaceCode) {
            this.namespaceCode = namespaceCode;
        }

        public String getComponentCode() {
            return componentCode;
        }

        public void setComponentCode(String componentCode) {
            this.componentCode = componentCode;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getLocale() {
            return locale;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
