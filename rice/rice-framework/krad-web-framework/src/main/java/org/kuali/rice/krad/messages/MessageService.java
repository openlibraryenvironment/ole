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

import java.util.Collection;

/**
 * Message Service API
 *
 * <p>
 * Messages given within an application can be externalized to a separate repository. Those messages are
 * then retrieved with use of the message service. The API provides various retrieval methods based on how
 * the message is identified
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface MessageService {

    public static final String DEFAULT_NAMESPACE_CODE = "KUALI";
    public static final String DEFAULT_COMPONENT_CODE = "All";

    /**
     * Gets the {@link Message} object that has the given namespace, component, key, and the default
     * system locale
     *
     * @param namespace namespace code the message belongs to, if null the default namespace should
     * be used
     * @param component component code the namespace is associated with, if null the default component
     * should be used
     * @param key key that identifies the message within the namespace and component
     * @return Message matching message object, or null if a message was not found
     */
    public Message getMessage(String namespace, String component, String key);

    /**
     * Gets the {@link Message} object that has the given namespace, component, key, and locale
     *
     * @param namespace namespace code the message belongs to, if null the default namespace should
     * be used
     * @param component component code the namespace is associated with, if null the default component
     * should be used
     * @param key key that identifies the message within the namespace and component
     * @param locale locale code for the message to return
     * @return Message matching message object, or null if a message was not found
     */
    public Message getMessage(String namespace, String component, String key, String locale);

    /**
     * Gets the text for the message that has the given namespace, component, key, and the default
     * system locale
     *
     * @param namespace namespace code the message belongs to, if null the default namespace should
     * be used
     * @param component component code the namespace is associated with, if null the default component
     * should be used
     * @param key key that identifies the message within the namespace and component
     * @return String text for the matched message, or null if no message was found
     */
    public String getMessageText(String namespace, String component, String key);

    /**
     * Gets the text for the message that has the given namespace, component, key, and locale
     *
     * @param namespace namespace code the message belongs to, if null the default namespace should
     * be used
     * @param component component code the namespace is associated with, if null the default component
     * should be used
     * @param key key that identifies the message within the namespace and component
     * @param locale locale code for the message to return
     * @return String text for the matched message, or null if no message was found
     */
    public String getMessageText(String namespace, String component, String key, String locale);

    /**
     * Gets the text for the message that has the given key within the default namespace, component,
     * and locale (note the defaults are determined by the service implementation)
     *
     * @param key key that identifies the message within the default namespace and component
     * @return String text for the matched message, or null if no message was found
     */
    public String getMessageText(String key);

    /**
     * Gets the text for the message that has the given key and locale within the default namespace and
     * component (note the defaults are determined by the service implementation)
     *
     * @param key key that identifies the message within the default namespace and component
     * @param locale locale code for the message to return
     * @return String text for the matched message, or null if no message was found
     */
    public String getMessageText(String key, String locale);

    /**
     * Gets all message objects for the given namespace and component using the default locale
     *
     * @param namespace namespace code the message belongs to
     * @param component component code the namespace is associated with
     * @return Collection<Message> collection of messages that match, or empty collection if no messages
     *         are found
     */
    public Collection<Message> getAllMessagesForComponent(String namespace, String component);

    /**
     * Gets all message objects for the given namespace, component, and locale
     *
     * @param namespace namespace code the message belongs to
     * @param component component code the namespace is associated with
     * @param locale locale code for the message to return
     * @return Collection<Message> collection of messages that match, or empty collection if no messages
     *         are found
     */
    public Collection<Message> getAllMessagesForComponent(String namespace, String component, String locale);

}
