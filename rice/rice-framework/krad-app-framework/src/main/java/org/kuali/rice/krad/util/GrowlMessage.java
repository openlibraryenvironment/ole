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
package org.kuali.rice.krad.util;

import java.io.Serializable;

/**
 * Contains configuration for displaying a growl message
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class GrowlMessage implements Serializable {
    private static final long serialVersionUID = 6588969539633862559L;

    private String namespaceCode;
    private String componentCode;

    private String title;
    private String titleKey;

    private String messageKey;
    private String[] messageParameters;

    private String theme;

    public GrowlMessage() {

    }

    /**
     * Namespace code (often an application or module code) the growl message is associated with
     *
     * <p>
     * Used with the component code and message key for retrieving the message text (and title text). If null,
     * the default namespace code will be used
     * </p>
     *
     * @return String error namespace code
     */
    public String getNamespaceCode() {
        return namespaceCode;
    }

    /**
     * Setter for the growl's associated namespace code
     *
     * @param namespaceCode
     */
    public void setNamespaceCode(String namespaceCode) {
        this.namespaceCode = namespaceCode;
    }

    /**
     * A code within the namespace that identifies a component or group the growl message is associated with
     *
     * <p>
     * Used with the namespace and message key for retrieving the message text (and title text). If null,
     * the default component code will be used
     * </p>
     *
     * @return String component code
     */
    public String getComponentCode() {
        return componentCode;
    }

    /**
     * Setter for the growl's associated component code
     *
     * @param componentCode
     */
    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    /**
     * Title for growl message (displays on top bar of growl)
     *
     * @return String title text
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for the growl title
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Key for the title message within the message repository
     *
     * <p>
     * Growl title text can be externalized into a message repository (see {@link
     * org.kuali.rice.krad.messages.MessageService}.
     * This gives the key for which the message which is used with the namespace and component to retrieve
     * the message text
     * </p>
     *
     * @return String message key
     */
    public String getTitleKey() {
        return titleKey;
    }

    /**
     * Setter for the growl title message key
     *
     * @param titleKey
     */
    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
    }

    /**
     * Key for the growl message within the message repository
     *
     * <p>
     * Growl message text must be externalized into a message repository (see {@link
     * org.kuali.rice.krad.messages.MessageService}.
     * This gives the key for which the message which is used with the namespace and component to retrieve
     * the message text
     * </p>
     *
     * @return String message key
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * Setter for the growl message key
     *
     * @param messageKey
     */
    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    /**
     * One or more parameters for complete the growl message
     *
     * <p>
     * An externally defined message can contain one or more placeholders which will get completed from runtime
     * variables. This array of strings is used for completing the message. The message parameters are filled based
     * on the order or parameters within the array
     * </p>
     *
     * @return String[] array of string values to fill message parameters
     */
    public String[] getMessageParameters() {
        return messageParameters;
    }

    /**
     * Setter for the message parameters array
     *
     * @param messageParameters
     */
    public void setMessageParameters(String[] messageParameters) {
        this.messageParameters = messageParameters;
    }

    /**
     * Name of the growl theme to use (must be setup through the view growl property
     *
     * @return String name of growl theme
     * @{link org.kuali.rice.krad.uif.view.View#getGrowls()} )
     */
    public String getTheme() {
        return theme;
    }

    /**
     * Setter for the growl theme to use
     *
     * @param theme
     */
    public void setTheme(String theme) {
        this.theme = theme;
    }
}
