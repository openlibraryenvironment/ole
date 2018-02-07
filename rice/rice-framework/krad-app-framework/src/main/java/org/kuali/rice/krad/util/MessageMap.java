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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.util.AutoPopulatingList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Holds errors due to validation
 *
 * <p>Keys of map represent property paths, and value is a AutoPopulatingList that contains resource string
 * keys (to retrieve the error message).</p>
 *
 * <p>Note, prior to rice 0.9.4, this class implemented {@link java.util.Map}.  The implements has been removed as of
 * rice 0.9.4</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MessageMap implements Serializable {
    private static final long serialVersionUID = -2328635367656516150L;

    private List<String> errorPath = new ArrayList<String>();

    private Map<String, AutoPopulatingList<ErrorMessage>> errorMessages =
            new LinkedHashMap<String, AutoPopulatingList<ErrorMessage>>();
    private Map<String, AutoPopulatingList<ErrorMessage>> warningMessages =
            new LinkedHashMap<String, AutoPopulatingList<ErrorMessage>>();
    private Map<String, AutoPopulatingList<ErrorMessage>> infoMessages =
            new LinkedHashMap<String, AutoPopulatingList<ErrorMessage>>();
    private AutoPopulatingList<GrowlMessage> growlMessages;

    public MessageMap() {
        growlMessages = new AutoPopulatingList<GrowlMessage>(GrowlMessage.class);
    }

    public MessageMap(MessageMap messageMap) {
        this.errorPath = messageMap.errorPath;
        this.errorMessages = messageMap.errorMessages;
        this.warningMessages = messageMap.warningMessages;
        this.infoMessages = messageMap.infoMessages;

        growlMessages = new AutoPopulatingList<GrowlMessage>(GrowlMessage.class);
    }

    public void merge(MessageMap messageMap) {
        if (messageMap != null) {
            if (messageMap.hasErrors()) {
                merge(messageMap.getErrorMessages(), errorMessages);
            }
            if (messageMap.hasInfo()) {
                merge(messageMap.getInfoMessages(), infoMessages);
            }
            if (messageMap.hasWarnings()) {
                merge(messageMap.getWarningMessages(), warningMessages);
            }
            if (messageMap.getGrowlMessages() != null) {
                growlMessages.addAll(messageMap.getGrowlMessages());
            }
        }

    }

    /**
     * Takes one message map and merges it into another.  Makes sure there are no duplicates.
     *
     * @param messagesFrom
     * @param messagesTo
     */
    public void merge(Map<String, AutoPopulatingList<ErrorMessage>> messagesFrom,
            Map<String, AutoPopulatingList<ErrorMessage>> messagesTo) {
        for (String key : messagesFrom.keySet()) {

            if (messagesTo.containsKey(key)) {
                // now we need to merge the messages
                AutoPopulatingList<ErrorMessage> tal = messagesFrom.get(key);
                AutoPopulatingList<ErrorMessage> parentList = messagesTo.get(key);

                for (Object o : tal) {

                    if (!parentList.contains(o)) {
                        parentList.add((ErrorMessage) o);
                    }
                }

            } else {
                messagesTo.put(key, messagesFrom.get(key));
            }

        }
    }

    public AutoPopulatingList<ErrorMessage> putError(String propertyName, String errorKey, String... errorParameters) {
        ErrorMessage message = new ErrorMessage(errorKey, errorParameters);
        return putMessageInMap(errorMessages, propertyName, message, true, true);
    }

    public AutoPopulatingList<ErrorMessage> putWarning(String propertyName, String messageKey,
            String... messageParameters) {
        ErrorMessage message = new ErrorMessage(messageKey, messageParameters);
        return putMessageInMap(warningMessages, propertyName, message, true, true);
    }

    public AutoPopulatingList<ErrorMessage> putInfo(String propertyName, String messageKey,
            String... messageParameters) {
        ErrorMessage message = new ErrorMessage(messageKey, messageParameters);
        return putMessageInMap(infoMessages, propertyName, message, true, true);
    }

    public AutoPopulatingList<ErrorMessage> putError(String propertyName, ErrorMessage message) {
        return putMessageInMap(errorMessages, propertyName, message, true, true);
    }

    public AutoPopulatingList<ErrorMessage> putWarning(String propertyName, ErrorMessage message) {
        return putMessageInMap(warningMessages, propertyName, message, true, true);
    }

    public AutoPopulatingList<ErrorMessage> putInfo(String propertyName, ErrorMessage message) {
        return putMessageInMap(infoMessages, propertyName, message, true, true);
    }

    public AutoPopulatingList<ErrorMessage> putErrorWithoutFullErrorPath(String propertyName, String errorKey,
            String... errorParameters) {
        ErrorMessage message = new ErrorMessage(errorKey, errorParameters);
        return putMessageInMap(errorMessages, propertyName, message, false, true);
    }

    public AutoPopulatingList<ErrorMessage> putWarningWithoutFullErrorPath(String propertyName, String messageKey,
            String... messageParameters) {
        ErrorMessage message = new ErrorMessage(messageKey, messageParameters);
        return putMessageInMap(warningMessages, propertyName, message, false, true);
    }

    public AutoPopulatingList<ErrorMessage> putInfoWithoutFullErrorPath(String propertyName, String messageKey,
            String... messageParameters) {
        ErrorMessage message = new ErrorMessage(messageKey, messageParameters);
        return putMessageInMap(infoMessages, propertyName, message, false, true);
    }

    public AutoPopulatingList<ErrorMessage> putErrorWithoutFullErrorPath(String propertyName, ErrorMessage message) {
        return putMessageInMap(errorMessages, propertyName, message, false, true);
    }

    public AutoPopulatingList<ErrorMessage> putWarningWithoutFullErrorPath(String propertyName, ErrorMessage message) {
        return putMessageInMap(warningMessages, propertyName, message, false, true);
    }

    public AutoPopulatingList<ErrorMessage> putInfoWithoutFullErrorPath(String propertyName, ErrorMessage message) {
        return putMessageInMap(infoMessages, propertyName, message, false, true);
    }

    public AutoPopulatingList<ErrorMessage> putErrorForSectionId(String sectionId, String errorKey,
            String... errorParameters) {
        return putErrorWithoutFullErrorPath(sectionId, errorKey, errorParameters);
    }

    public AutoPopulatingList<ErrorMessage> putWarningForSectionId(String sectionId, String messageKey,
            String... messageParameters) {
        return putWarningWithoutFullErrorPath(sectionId, messageKey, messageParameters);
    }

    public AutoPopulatingList<ErrorMessage> putInfoForSectionId(String sectionId, String messageKey,
            String... messageParameters) {
        return putInfoWithoutFullErrorPath(sectionId, messageKey, messageParameters);
    }

    public AutoPopulatingList<ErrorMessage> putErrorForSectionId(String sectionId, ErrorMessage message) {
        return putErrorWithoutFullErrorPath(sectionId, message);
    }

    public AutoPopulatingList<ErrorMessage> putWarningForSectionId(String sectionId, ErrorMessage message) {
        return putWarningWithoutFullErrorPath(sectionId, message);
    }

    public AutoPopulatingList<ErrorMessage> putInfoForSectionId(String sectionId, ErrorMessage message) {
        return putInfoWithoutFullErrorPath(sectionId, message);
    }

    /**
     * Adds a growl (using the default theme) to the message map with the given title and message
     *
     * @param growlTitle - title for the growl
     * @param messageKey - key for the message in resources
     * @param messageParameters - parameters for the message
     */
    public void addGrowlMessage(String growlTitle, String messageKey, String... messageParameters) {
        GrowlMessage growl = new GrowlMessage();

        growl.setTitle(growlTitle);
        growl.setMessageKey(messageKey);
        growl.setMessageParameters(messageParameters);

        growlMessages.add(growl);
    }

    /**
     * Add a growl to the message map
     *
     * @param growl - growl instance to add
     */
    public void addGrowlMessage(GrowlMessage growl) {
        growlMessages.add(growl);
    }

    /**
     * Adds an error message to the given message map, adjusting the error path and message parameters if necessary
     *
     * @param messagesMap
     * @param propertyName name of the property to add error under
     * @param errorMessage
     * @param prependFullErrorPath true if you want the whole parent error path prepended, false otherwise
     * @param escapeHtmlMessageParameters whether to escape HTML characters in the message parameters, provides
     * protection against XSS attacks
     * @return TypeArrayList
     */
    protected AutoPopulatingList<ErrorMessage> putMessageInMap(Map<String, AutoPopulatingList<ErrorMessage>> messagesMap,
            String propertyName, ErrorMessage errorMessage, boolean prependFullErrorPath,
            boolean escapeHtmlMessageParameters) {
        if (StringUtils.isBlank(propertyName)) {
            throw new IllegalArgumentException("invalid (blank) propertyName");
        }
        if (StringUtils.isBlank(errorMessage.getErrorKey())) {
            throw new IllegalArgumentException("invalid (blank) errorKey");
        }

        // check if we have previous errors for this property
        AutoPopulatingList<ErrorMessage> errorList = null;
        String propertyKey = getKeyPath(propertyName, prependFullErrorPath);
        if (messagesMap.containsKey(propertyKey)) {
            errorList = messagesMap.get(propertyKey);
        } else {
            errorList = new AutoPopulatingList<ErrorMessage>(ErrorMessage.class);
        }

        if (escapeHtmlMessageParameters) {
            if (errorMessage.getMessageParameters() != null) {
                String[] filteredMessageParameters = new String[errorMessage.getMessageParameters().length];
                for (int i = 0; i < errorMessage.getMessageParameters().length; i++) {
                    filteredMessageParameters[i] = StringEscapeUtils.escapeHtml(errorMessage.getMessageParameters()[i]);
                }
                errorMessage.setMessageParameters(filteredMessageParameters);
            }

            if (errorMessage.getMessagePrefixParameters() != null) {
                String[] filteredMessageParameters = new String[errorMessage.getMessagePrefixParameters().length];
                for (int i = 0; i < errorMessage.getMessagePrefixParameters().length; i++) {
                    filteredMessageParameters[i] = StringEscapeUtils.escapeHtml(
                            errorMessage.getMessagePrefixParameters()[i]);
                }
                errorMessage.setMessagePrefixParameters(filteredMessageParameters);
            }

            if (errorMessage.getMessageSuffixParameters() != null) {
                String[] filteredMessageParameters = new String[errorMessage.getMessageSuffixParameters().length];
                for (int i = 0; i < errorMessage.getMessageSuffixParameters().length; i++) {
                    filteredMessageParameters[i] = StringEscapeUtils.escapeHtml(
                            errorMessage.getMessageSuffixParameters()[i]);
                }
                errorMessage.setMessageSuffixParameters(filteredMessageParameters);
            }
        }

        // check if this error has already been added to the list
        boolean alreadyAdded = false;
        for (ErrorMessage e : errorList) {
            if (e.equals(errorMessage)) {
                alreadyAdded = true;
                break;
            }
        }
        if (!alreadyAdded) {
            errorList.add(errorMessage);
        }

        return messagesMap.put(propertyKey, errorList);
    }

    /**
     * If any error messages with the key targetKey exist in this ErrorMap for the named property, those ErrorMessages
     * will be replaced with a new ErrorMessage with the given replaceKey and replaceParameters.
     *
     * @param propertyName name of the property where existing error will be replaced
     * @param targetKey error key of message to be replaced
     * @param replaceParameters zero or more string parameters for the replacement error message
     * @return true if the replacement occurred
     * @paran replaceKey error key which will replace targetKey
     */
    public boolean replaceError(String propertyName, String targetKey, String replaceKey, String... replaceParameters) {
        return replaceError(propertyName, targetKey, true, replaceKey, replaceParameters);
    }

    /**
     * If any error messages with the key targetKey exist in this ErrorMap for the named property, those ErrorMessages
     * will be replaced with a new ErrorMessage with the given replaceKey and replaceParameters. The targetKey
     * and replaceKey will be prepended with the current errorPath, if any.
     *
     * @param propertyName name of the property where existing error will be replaced
     * @param targetKey error key of message to be replaced
     * @param replaceParameters zero or more string parameters for the replacement error message
     * @return true if the replacement occurred
     * @paran replaceKey error key which will replace targetKey
     */
    public boolean replaceErrorWithoutFullErrorPath(String propertyName, String targetKey, String replaceKey,
            String... replaceParameters) {
        return replaceError(propertyName, targetKey, false, replaceKey, replaceParameters);
    }

    /**
     * If any error messages with the key targetKey exist in this ErrorMap for the named property, those ErrorMessages
     * will be replaced with a new ErrorMessage with the given replaceKey and replaceParameters.
     *
     * @param propertyName name of the property to add error under
     * @param targetKey resource key used to retrieve the error text
     * @param withFullErrorPath true if you want the whole parent error path appended, false otherwise
     * @param replaceParameters zero or more string parameters for the displayed error message
     * @return true if the replacement occurred
     */
    private boolean replaceError(String propertyName, String targetKey, boolean withFullErrorPath, String replaceKey,
            String... replaceParameters) {
        boolean replaced = false;

        if (StringUtils.isBlank(propertyName)) {
            throw new IllegalArgumentException("invalid (blank) propertyName");
        }
        if (StringUtils.isBlank(targetKey)) {
            throw new IllegalArgumentException("invalid (blank) targetKey");
        }
        if (StringUtils.isBlank(replaceKey)) {
            throw new IllegalArgumentException("invalid (blank) replaceKey");
        }

        // check if we have previous errors for this property
        AutoPopulatingList<ErrorMessage> errorList = null;
        String propertyKey = getKeyPath(propertyName, withFullErrorPath);
        if (errorMessages.containsKey(propertyKey)) {
            errorList = errorMessages.get(propertyKey);

            // look for the specific targetKey
            for (int i = 0; i < errorList.size(); ++i) {
                ErrorMessage em = errorList.get(i);

                // replace matching messages
                if (em.getErrorKey().equals(targetKey)) {
                    ErrorMessage rm = new ErrorMessage(replaceKey, replaceParameters);
                    errorList.set(i, rm);
                    replaced = true;
                }
            }
        }

        return replaced;
    }

    /**
     * Returns true if the named field has a message with the given errorKey
     *
     * @param errorKey
     * @param fieldName
     * @return boolean
     */
    public boolean fieldHasMessage(String fieldName, String errorKey) {
        boolean found = false;

        List<ErrorMessage> fieldMessages = errorMessages.get(fieldName);
        if (fieldMessages != null) {
            for (Iterator<ErrorMessage> i = fieldMessages.iterator(); !found && i.hasNext(); ) {
                ErrorMessage errorMessage = i.next();
                found = errorMessage.getErrorKey().equals(errorKey);
            }
        }

        return found;
    }

    /**
     * Returns the number of messages for the given field
     *
     * @param fieldName
     * @return int
     */
    public int countFieldMessages(String fieldName) {
        int count = 0;

        List<ErrorMessage> fieldMessages = errorMessages.get(fieldName);
        if (fieldMessages != null) {
            count = fieldMessages.size();
        }

        return count;
    }

    /**
     * @return true if the given messageKey is associated with some property in this ErrorMap
     */
    public boolean containsMessageKey(String messageKey) {
        ErrorMessage foundMessage = null;

        if (!hasNoErrors()) {
            for (Iterator<Map.Entry<String, AutoPopulatingList<ErrorMessage>>> i =
                         getAllPropertiesAndErrors().iterator(); (foundMessage == null) && i.hasNext(); ) {
                Map.Entry<String, AutoPopulatingList<ErrorMessage>> e = i.next();
                AutoPopulatingList<ErrorMessage> entryErrorList = e.getValue();
                for (Iterator<ErrorMessage> j = entryErrorList.iterator(); j.hasNext(); ) {
                    ErrorMessage em = j.next();
                    if (messageKey.equals(em.getErrorKey())) {
                        foundMessage = em;
                    }
                }
            }
        }

        return (foundMessage != null);
    }

    private int getMessageCount(Map<String, AutoPopulatingList<ErrorMessage>> messageMap) {
        int messageCount = 0;
        for (Iterator<String> iter = messageMap.keySet().iterator(); iter.hasNext(); ) {
            String errorKey = iter.next();
            List<ErrorMessage> errors = messageMap.get(errorKey);
            messageCount += errors.size();
        }

        return messageCount;
    }

    /**
     * Counts the total number of error messages in the map
     *
     * @return returns an int for the total number of errors
     */
    public int getErrorCount() {
        return getMessageCount(errorMessages);
    }

    /**
     * Counts the total number of warning messages in the map
     *
     * @return returns an int for the total number of warnings
     */
    public int getWarningCount() {
        return getMessageCount(warningMessages);
    }

    /**
     * Counts the total number of info messages in the map
     *
     * @return returns an int for the total number of info
     */
    public int getInfoCount() {
        return getMessageCount(infoMessages);
    }

    /**
     * @param path
     * @return Returns a List of ErrorMessages for the given path
     */
    public AutoPopulatingList<ErrorMessage> getMessages(String path) {
        return errorMessages.get(path);
    }

    /**
     * Adds a string prefix to the error path.
     *
     * @param parentName
     */
    public void addToErrorPath(String parentName) {
        errorPath.add(parentName);
    }

    /**
     * This method returns the list that holds the error path values.
     *
     * @return List
     */
    public List<String> getErrorPath() {
        return errorPath;
    }

    /**
     * Removes a string prefix from the error path.
     *
     * @param parentName
     * @return boolean Returns true if the parentName existed, false otherwise.
     */
    public boolean removeFromErrorPath(String parentName) {
        return errorPath.remove(parentName);
    }

    /**
     * Clears the errorPath.
     */
    public void clearErrorPath() {
        errorPath.clear();
    }

    /**
     * This is what's prepended to the beginning of the key. This is built by iterating over all of the entries in the
     * errorPath
     * list and concatenating them together with a "."
     *
     * @param propertyName
     * @param prependFullErrorPath
     * @return String Returns the keyPath.
     */
    public String getKeyPath(String propertyName, boolean prependFullErrorPath) {
        String keyPath = "";

        if (KRADConstants.GLOBAL_ERRORS.equals(propertyName)) {
            return KRADConstants.GLOBAL_ERRORS;
        }

        if (!errorPath.isEmpty() && prependFullErrorPath) {
            keyPath = StringUtils.join(errorPath.iterator(), ".");
            keyPath += (keyPath != null && keyPath.endsWith(".")) ? propertyName : "." + propertyName;
        } else {
            keyPath = propertyName;
        }

        return keyPath;
    }

    /**
     * @return List of the property names that have errors.
     */
    public List<String> getPropertiesWithErrors() {
        List<String> properties = new ArrayList<String>();

        for (Iterator<String> iter = errorMessages.keySet().iterator(); iter.hasNext(); ) {
            properties.add(iter.next());
        }

        return properties;
    }

    /**
     * @return List of the property names that have warnings.
     */
    public List<String> getPropertiesWithWarnings() {
        List<String> properties = new ArrayList<String>(warningMessages.keySet());
        return properties;
    }

    /**
     * @return List of the property names that have info.
     */
    public List<String> getPropertiesWithInfo() {
        List<String> properties = new ArrayList<String>(infoMessages.keySet());
        return properties;
    }

    public void clearErrorMessages() {
        errorMessages.clear();
    }

    public boolean doesPropertyHaveError(String key) {
        return errorMessages.containsKey(key);
    }

    /**
     * @param pattern comma separated list of keys, optionally ending with * wildcard
     */
    public boolean containsKeyMatchingPattern(String pattern) {
        List<String> simplePatterns = new ArrayList<String>();
        List<String> wildcardPatterns = new ArrayList<String>();
        String[] patterns = pattern.split(",");
        for (int i = 0; i < patterns.length; i++) {
            String s = patterns[i];
            if (s.endsWith("*")) {
                wildcardPatterns.add(s.substring(0, s.length() - 1));
            } else {
                simplePatterns.add(s);
            }
        }
        for (Iterator<String> keys = errorMessages.keySet().iterator(); keys.hasNext(); ) {
            String key = keys.next();
            if (simplePatterns.contains(key)) {
                return true;
            }
            for (Iterator<String> wildcardIterator = wildcardPatterns.iterator(); wildcardIterator.hasNext(); ) {
                String wildcard = wildcardIterator.next();
                if (key.startsWith(wildcard)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Set<Map.Entry<String, AutoPopulatingList<ErrorMessage>>> getAllPropertiesAndErrors() {
        return errorMessages.entrySet();
    }

    public AutoPopulatingList<ErrorMessage> getErrorMessagesForProperty(String propertyName) {
        return errorMessages.get(propertyName);
    }

    public AutoPopulatingList<ErrorMessage> getWarningMessagesForProperty(String propertyName) {
        return warningMessages.get(propertyName);
    }

    public AutoPopulatingList<ErrorMessage> getInfoMessagesForProperty(String propertyName) {
        return infoMessages.get(propertyName);
    }

    /**
     * Gets a list of lists that represent errors that matched by the
     * propertyName passed in (multiple lists because the wildcard can match
     * multiple keys). If wildcard is true, the propertyName ends with a
     * wildcard character. Otherwise, it will only match on the single key and
     * return a list with one list
     *
     * @param propertyName
     * @param allowWildcard
     * @return
     */
    public List<AutoPopulatingList<ErrorMessage>> getErrorMessagesForProperty(String propertyName,
            boolean allowWildcard) {
        List<AutoPopulatingList<ErrorMessage>> foundMessages = new ArrayList<AutoPopulatingList<ErrorMessage>>();
        if (allowWildcard) {
            boolean wildcard = false;
            if (propertyName.endsWith("*")) {
                wildcard = true;
                propertyName = propertyName.substring(0, propertyName.length() - 1);
            }
            for (Iterator<String> keys = errorMessages.keySet().iterator(); keys.hasNext(); ) {
                String key = keys.next();
                if (!wildcard && propertyName.equals(key)) {
                    foundMessages.add(errorMessages.get(key));
                    break;
                } else if (wildcard && key.startsWith(propertyName)) {
                    foundMessages.add(errorMessages.get(key));
                }
            }
        } else {
            foundMessages.add(getErrorMessagesForProperty(propertyName));
        }

        return foundMessages;
    }

    /**
     * Gets a list of lists that represent warnings that matched by the
     * propertyName passed in (multiple lists because the wildcard can match
     * multiple keys). If wildcard is true, the propertyName ends with a
     * wildcard character. Otherwise, it will only match on the single key and
     * return a list with one list.
     *
     * @param propertyName
     * @param allowWildcard
     * @return
     */
    public List<AutoPopulatingList<ErrorMessage>> getWarningMessagesForProperty(String propertyName,
            boolean allowWildcard) {
        List<AutoPopulatingList<ErrorMessage>> foundMessages = new ArrayList<AutoPopulatingList<ErrorMessage>>();
        if (allowWildcard) {
            boolean wildcard = false;
            if (propertyName.endsWith("*")) {
                wildcard = true;
                propertyName = propertyName.substring(0, propertyName.length() - 1);
            }
            for (Iterator<String> keys = warningMessages.keySet().iterator(); keys.hasNext(); ) {
                String key = keys.next();
                if (!wildcard && propertyName.equals(key)) {
                    foundMessages.add(warningMessages.get(key));
                    break;
                } else if (wildcard && key.startsWith(propertyName)) {
                    foundMessages.add(warningMessages.get(key));
                }
            }
        } else {
            foundMessages.add(getWarningMessagesForProperty(propertyName));
        }

        return foundMessages;
    }

    /**
     * Gets a list of lists that represent info messages that matched by the
     * propertyName passed in (multiple lists because the wildcard can match
     * multiple keys). If wildcard is true, the propertyName ends with a
     * wildcard character. If it is false, it will only match on the single key
     * and return a list with one list.
     *
     * @param propertyName
     * @param allowWildcard
     * @return
     */
    public List<AutoPopulatingList<ErrorMessage>> getInfoMessagesForProperty(String propertyName,
            boolean allowWildcard) {
        List<AutoPopulatingList<ErrorMessage>> foundMessages = new ArrayList<AutoPopulatingList<ErrorMessage>>();
        if (allowWildcard) {
            boolean wildcard = false;
            if (propertyName.endsWith("*")) {
                wildcard = true;
                propertyName = propertyName.substring(0, propertyName.length() - 1);
            }
            for (Iterator<String> keys = infoMessages.keySet().iterator(); keys.hasNext(); ) {
                String key = keys.next();
                if (!wildcard && propertyName.equals(key)) {
                    foundMessages.add(infoMessages.get(key));
                    break;
                } else if (wildcard && key.startsWith(propertyName)) {
                    foundMessages.add(infoMessages.get(key));
                }
            }
        } else {
            foundMessages.add(getInfoMessagesForProperty(propertyName));
        }

        return foundMessages;
    }

    public boolean hasErrors() {
        return !errorMessages.isEmpty();
    }

    public boolean hasNoErrors() {
        return errorMessages.isEmpty();
    }

    public boolean hasWarnings() {
        return !warningMessages.isEmpty();
    }

    public boolean hasNoWarnings() {
        return warningMessages.isEmpty();
    }

    public boolean hasInfo() {
        return !infoMessages.isEmpty();
    }

    public boolean hasNoInfo() {
        return infoMessages.isEmpty();
    }

    public boolean hasMessages() {
        if (!errorMessages.isEmpty() || !warningMessages.isEmpty() || !infoMessages.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean hasNoMessages() {
        if (errorMessages.isEmpty() && warningMessages.isEmpty() && infoMessages.isEmpty()) {
            return true;
        }
        return false;
    }

    public Set<String> getAllPropertiesWithErrors() {
        return errorMessages.keySet();
    }

    public Set<String> getAllPropertiesWithWarnings() {
        return warningMessages.keySet();
    }

    public Set<String> getAllPropertiesWithInfo() {
        return infoMessages.keySet();
    }

    public AutoPopulatingList<ErrorMessage> removeAllErrorMessagesForProperty(String property) {
        return errorMessages.remove(property);
    }

    public AutoPopulatingList<ErrorMessage> removeAllWarningMessagesForProperty(String property) {
        return warningMessages.remove(property);
    }

    public AutoPopulatingList<ErrorMessage> removeAllInfoMessagesForProperty(String property) {
        return infoMessages.remove(property);
    }

    public int getNumberOfPropertiesWithErrors() {
        return errorMessages.size();
    }

    public Map<String, AutoPopulatingList<ErrorMessage>> getErrorMessages() {
        return this.errorMessages;
    }

    public Map<String, AutoPopulatingList<ErrorMessage>> getWarningMessages() {
        return this.warningMessages;
    }

    public Map<String, AutoPopulatingList<ErrorMessage>> getInfoMessages() {
        return this.infoMessages;
    }

    /**
     * Returns the list of growl messages (@{link GrowlMessage}) that have been added to
     * the message map
     *
     * @return List<GrowlMessage>
     */
    public List<GrowlMessage> getGrowlMessages() {
        return this.growlMessages;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
