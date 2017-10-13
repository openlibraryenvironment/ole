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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Contains the error message key and parameters for a specific instantiation of an error message
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ErrorMessage implements Serializable {
    private static final long serialVersionUID = 4397449554212875250L;

    private String namespaceCode;
    private String componentCode;

    private String errorKey;
    private String[] messageParameters;

    private String messagePrefixKey;
    private String[] messagePrefixParameters;

    private String messageSuffixKey;
    private String[] messageSuffixParameters;

    /**
     * Default constructor
     */
    public ErrorMessage() {
    }

    /**
     * Convenience constructor which sets both fields
     *
     * @param errorKey - message key for the error
     * @param messageParameters - zero or more parameters for the message text
     */
    public ErrorMessage(String errorKey, String... messageParameters) {
        if (StringUtils.isBlank(errorKey)) {
            StringBuilder builder = null;
            if (messageParameters != null && messageParameters.length > 0) {
                builder = new StringBuilder("  Message parameters are: ");
                for (String param: messageParameters) {
                    builder.append(param).append("\n");
                }
            } else {
                builder = new StringBuilder("  Message parameters were null or empty.");
            }
            throw new IllegalArgumentException("invalid (blank) errorKey." + builder.toString());
        }

        setErrorKey(errorKey);
        setMessageParameters((String[]) ArrayUtils.clone(messageParameters));
    }

    /**
     * Namespace code (often an application or module code) the error message is associated with
     *
     * <p>
     * Used with the component code and error key for retrieving the message text (and prefix, suffix). If null,
     * the default namespace code will be used
     * </p>
     *
     * @return String error namespace code
     */
    public String getNamespaceCode() {
        return namespaceCode;
    }

    /**
     * Setter for the error's associated namespace code
     *
     * @param namespaceCode
     */
    public void setNamespaceCode(String namespaceCode) {
        this.namespaceCode = namespaceCode;
    }

    /**
     * A code within the namespace that identifies a component or group the error message is associated with
     *
     * <p>
     * Used with the namespace and error key for retrieving the message text (and prefix, suffix). If null,
     * the default component code will be used
     * </p>
     *
     * @return String component code
     */
    public String getComponentCode() {
        return componentCode;
    }

    /**
     * Setter for the error's associated component code
     *
     * @param componentCode
     */
    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    /**
     * Sets the key to use to retrieve the message for this ErrorMessage
     *
     * @param errorKey
     */
    public void setErrorKey(String errorKey) {
        if (StringUtils.isBlank(errorKey)) {
            throw new IllegalArgumentException("invalid (blank) errorKey");
        }

        this.errorKey = errorKey;
    }

    /**
     * Gets the message key for this ErrorMessage
     *
     * @return message key
     */
    public String getErrorKey() {
        return errorKey;
    }

    /**
     * Sets the messageParameters for this ErrorMessage
     *
     * @param messageParameters
     */
    public void setMessageParameters(String[] messageParameters) {
        this.messageParameters = messageParameters;
    }

    /**
     * Get the messageParameters which should be used when evaluating and generating the message for
     * the ErrorMessage.
     *
     * @return the messageParameters
     */
    public String[] getMessageParameters() {
        return messageParameters;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer s = new StringBuffer(getErrorKey());

        String[] params = getMessageParameters();
        if (params != null) {
            s.append("(");
            for (int i = 0; i < params.length; ++i) {
                if (i > 0) {
                    s.append(", ");
                }
                s.append(params[i]);
            }
            s.append(")");
        }
        return s.toString();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        boolean equals = false;

        if (this == obj) {
            equals = true;
        } else if (obj instanceof ErrorMessage) {
            ErrorMessage other = (ErrorMessage) obj;

            if (StringUtils.equals(getErrorKey(), other.getErrorKey())) {
                equals = Arrays.equals(getMessageParameters(), other.getMessageParameters());
            }
        }

        return equals;
    }

    /**
     * Defined because when you redefine equals, you must redefine hashcode.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hashCode = 5011966;

        if (getErrorKey() != null) {
            hashCode = getErrorKey().hashCode();
        }

        return hashCode;
    }

    /**
     * Gets the messagePrefixKey which defines the message key for the message to be prefixed to the message
     * defined by errorKey.  It is up to the code using this errorMessage to prepend the prefix message to the original
     * message.
     *
     * @return the messagePrefixKey
     */
    public String getMessagePrefixKey() {
        return messagePrefixKey;
    }

    /**
     * Set the messagePrefixKey
     *
     * @param messagePrefixKey
     */
    public void setMessagePrefixKey(String messagePrefixKey) {
        this.messagePrefixKey = messagePrefixKey;
    }

    /**
     * Gets the messageSuffixKey which defines the message key for the message to be appended to the message
     * defined by errorKey.  It is up to the code using this errorMessage to append the suffix message to the original
     * message.
     *
     * @return the messageSuffixKey
     */
    public String getMessageSuffixKey() {
        return messageSuffixKey;
    }

    /**
     * Set the messageSuffixKey
     *
     * @param messageSuffixKey
     */
    public void setMessageSuffixKey(String messageSuffixKey) {
        this.messageSuffixKey = messageSuffixKey;
    }

    /**
     * Get the messagePrefixParameters which should be used when evaluating and generating the message for
     * the messagePrefixKey.
     *
     * @return the messagePrefixParameters
     */
    public String[] getMessagePrefixParameters() {
        return messagePrefixParameters;
    }

    /**
     * Set the messagePrefixParameters
     *
     * @param messagePrefixParameters
     */
    public void setMessagePrefixParameters(String[] messagePrefixParameters) {
        this.messagePrefixParameters = messagePrefixParameters;
    }

    /**
     * Get the messagePrefixParameters which should be used when evaluating and generating the message for
     * the messageSuffixKey.
     *
     * @return the messageSuffixParameters
     */
    public String[] getMessageSuffixParameters() {
        return messageSuffixParameters;
    }

    /**
     * Set the messageSuffixParameters
     *
     * @param messageSuffixParameters
     */
    public void setMessageSuffixParameters(String[] messageSuffixParameters) {
        this.messageSuffixParameters = messageSuffixParameters;
    }
}
