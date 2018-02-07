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

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Holds the text and metadata for a message that will be given by the system, including validation
 * messages, UI text (labels, instructions), and other text that has been externalized from the
 * system
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class Message extends PersistableBusinessObjectBase {

    private String namespaceCode;
    private String componentCode;
    private String key;
    private String locale;
    private String description;
    private String text;

    public Message() {
        super();
    }

    /**
     * Namespace code (often an application or module code) that message is associated with, used for
     * grouping messages
     *
     * @return String namespace code
     */
    public String getNamespaceCode() {
        return namespaceCode;
    }

    /**
     * Setter for the namespace code the message should be associated with
     *
     * @param namespaceCode
     */
    public void setNamespaceCode(String namespaceCode) {
        this.namespaceCode = namespaceCode;
    }

    /**
     * A code within the namespace that identifies a component or group, used for further grouping
     * of messages within the namespace
     *
     * <p>
     * Examples here could be a bean id, the class name of an object, or any application/module defined code
     * </p>
     *
     * @return String representing a component code
     */
    public String getComponentCode() {
        return componentCode;
    }

    /**
     * Setter for the component code the message should be associated with
     *
     * @param componentCode
     */
    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    /**
     * A key that uniquely identifies the message within the namespace and component
     *
     * <p>
     * Within the UIF, this is generally used to indicate the property path the message is associated with
     * (for example: "control.label"). For validation messages this is generally a combination that identifies
     * the type of validation message and the validation performed (for example: "error.account.missing")
     * </p>
     *
     * @return String message key
     */
    public String getKey() {
        return key;
    }

    /**
     * Setter for the message key
     *
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Locale code the message is represented for, used for supporting messages in different
     * languages
     *
     * @return message locale code
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Setter for the message locale code
     *
     * @param locale
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * A description for the message
     *
     * <p>
     * Not used by the framework, here for purposes of editing of messages and providing a description
     * of the message to users
     * </p>
     *
     * @return String message description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the message description
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Text value for the message
     *
     * <p>
     * This holds the actual text for the message which is what will be displayed. Depending on how
     * the message is being used it might contain parameters or other special syntax
     * </p>
     *
     * @return String text for the message
     */
    public String getText() {
        return text;
    }

    /**
     * Setter for the message text
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Generate toString using message key fields
     *
     * @return String representing the message object
     */
    @Override
    public final String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("namespaceCode=" + this.namespaceCode);
        buffer.append(",componentCode=" + this.componentCode);
        buffer.append(",key=" + this.key);
        buffer.append(",locale=" + this.locale);

        return buffer.toString();
    }
}
