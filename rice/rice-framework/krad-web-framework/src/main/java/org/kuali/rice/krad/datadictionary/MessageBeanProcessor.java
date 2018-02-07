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
package org.kuali.rice.krad.datadictionary;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.messages.Message;
import org.kuali.rice.krad.messages.MessageService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.component.DataBinding;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.field.ActionField;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADPropertyConstants;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ListFactoryBean;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Dictionary bean processor that retrieves external messages for bean definitions and alters the bean
 * definitions to use the external text
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MessageBeanProcessor extends DictionaryBeanProcessorBase {

    private ConfigurableListableBeanFactory beanFactory;
    private DataDictionary dataDictionary;

    public MessageBeanProcessor(DataDictionary dataDictionary, ConfigurableListableBeanFactory beanFactory) {
        this.dataDictionary = dataDictionary;
        this.beanFactory = beanFactory;
    }

    /**
     * @see DictionaryBeanProcessor#processRootBeanDefinition(java.lang.String,
     *      org.springframework.beans.factory.config.BeanDefinition)
     */
    public void processRootBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        processBeanMessages(beanName, beanDefinition, null);
    }

    /**
     * @see DictionaryBeanProcessor#processNestedBeanDefinition(java.lang.String,
     *      org.springframework.beans.factory.config.BeanDefinition, java.lang.String,
     *      java.util.Stack<org.springframework.beans.factory.config.BeanDefinitionHolder>)
     */
    public void processNestedBeanDefinition(String beanName, BeanDefinition beanDefinition, String propertyName,
            Stack<BeanDefinitionHolder> nestedBeanStack) {
        processBeanMessages(beanName, beanDefinition, nestedBeanStack);
    }

    /**
     * @see DictionaryBeanProcessor#processStringPropertyValue(java.lang.String, java.lang.String,
     *      java.util.Stack<org.springframework.beans.factory.config.BeanDefinitionHolder>)
     */
    public String processStringPropertyValue(String propertyName, String propertyValue,
            Stack<BeanDefinitionHolder> nestedBeanStack) {
        return processMessagePlaceholders(propertyValue, nestedBeanStack);
    }

    /**
     * @see DictionaryBeanProcessor#processCollectionBeanDefinition(java.lang.String,
     *      org.springframework.beans.factory.config.BeanDefinition, java.lang.String,
     *      java.util.Stack<org.springframework.beans.factory.config.BeanDefinitionHolder>)
     */
    public void processCollectionBeanDefinition(String beanName, BeanDefinition beanDefinition, String propertyName,
            Stack<BeanDefinitionHolder> nestedBeanStack) {
        processBeanMessages(beanName, beanDefinition, nestedBeanStack);
    }

    /**
     * @see DictionaryBeanProcessor#processArrayStringPropertyValue(java.lang.String, java.lang.Object[],
     *      java.lang.String, int, java.util.Stack<org.springframework.beans.factory.config.BeanDefinitionHolder>)
     */
    public String processArrayStringPropertyValue(String propertyName, Object[] propertyValue, String elementValue,
            int elementIndex, Stack<BeanDefinitionHolder> nestedBeanStack) {
        return processMessagePlaceholders(elementValue, nestedBeanStack);
    }

    /**
     * @see DictionaryBeanProcessor#processListStringPropertyValue(java.lang.String, java.util.List<?>,
     *      java.lang.String, int, java.util.Stack<org.springframework.beans.factory.config.BeanDefinitionHolder>)
     */
    public String processListStringPropertyValue(String propertyName, List<?> propertyValue, String elementValue,
            int elementIndex, Stack<BeanDefinitionHolder> nestedBeanStack) {
        return processMessagePlaceholders(elementValue, nestedBeanStack);
    }

    /**
     * @see DictionaryBeanProcessor#processSetStringPropertyValue(java.lang.String, java.util.Set<?>, java.lang.String,
     *      java.util.Stack<org.springframework.beans.factory.config.BeanDefinitionHolder>)
     */
    public String processSetStringPropertyValue(String propertyName, Set<?> propertyValue, String elementValue,
            Stack<BeanDefinitionHolder> nestedBeanStack) {
        return processMessagePlaceholders(elementValue, nestedBeanStack);
    }

    /**
     * @see DictionaryBeanProcessor#processMapStringPropertyValue(java.lang.String, java.util.Map<?,?>,
     *      java.lang.String, java.lang.Object, java.util.Stack<org.springframework.beans.factory.config.BeanDefinitionHolder>)
     */
    public String processMapStringPropertyValue(String propertyName, Map<?, ?> propertyValue, String elementValue,
            Object elementKey, Stack<BeanDefinitionHolder> nestedBeanStack) {
        return processMessagePlaceholders(elementValue, nestedBeanStack);
    }

    /**
     * Retrieves external messages whose namespace and component matches the bean definition and applies
     * the message text to the bean property values
     *
     * @param beanName name of the bean to process
     * @param beanDefinition bean definition to process
     * @param nestedBeanStack stack of beans that contain the given bean, used for finding a namespace
     */
    protected void processBeanMessages(String beanName, BeanDefinition beanDefinition,
            Stack<BeanDefinitionHolder> nestedBeanStack) {
        Class<?> beanClass = getBeanClass(beanDefinition, beanFactory);
        if ((beanClass == null) || !(DictionaryBean.class.isAssignableFrom(beanClass) || ListFactoryBean.class
                .isAssignableFrom(beanClass))) {
            return;
        }

        String namespace = getNamespaceForBean(beanName, beanDefinition);
        if (StringUtils.isBlank(namespace)) {
            namespace = getNamespaceForBeanInStack(nestedBeanStack);
        }

        String componentCode = getComponentForBean(beanName, beanDefinition);
        if (StringUtils.equals(componentCode, beanName)) {
            // check if there is a parent bean in the factory using the standard suffix, if so we will skip this
            // bean as messages will be picked up by that parent bean definition. Note this is not for all parents,
            // just where the convention has been setup for extension (ex. 'bean' and 'bean-parentName')
            String extensionParentBeanName = beanName + KRADConstants.DICTIONARY_BEAN_PARENT_SUFFIX;
            if (beanFactory.containsBean(extensionParentBeanName)) {
                return;
            }
        }

        // if a namespace and component was found retrieve all messages associated with them
        if (StringUtils.isNotBlank(namespace) && StringUtils.isNotBlank(componentCode)) {
            Collection<Message> beanMessages = getMessageService().getAllMessagesForComponent(namespace, componentCode);
            for (Message beanMessage : beanMessages) {
                applyMessageToBean(beanMessage, beanDefinition, beanClass);
            }
        }
    }

    /**
     * Applies the text for a given message to the associated bean definition property
     *
     * @param message message instance to apply
     * @param beanDefinition bean definition the message should be applied to
     * @param beanClass class for the bean definition
     */
    protected void applyMessageToBean(Message message, BeanDefinition beanDefinition, Class<?> beanClass) {
        String key = message.getKey().trim();

        // if message doesn't start with path indicator, it will be an explicit key that is matched when
        // iterating over the property values, so we will just return in that case
        if (!key.startsWith(KRADConstants.MESSAGE_KEY_PATH_INDICATOR)) {
            return;
        }

        // if here dealing with a path key, strip off indicator and then process as a property path
        key = StringUtils.stripStart(key, KRADConstants.MESSAGE_KEY_PATH_INDICATOR);

        // list factory beans just have the one list property (with no name)
        if (ListFactoryBean.class.isAssignableFrom(beanClass)) {
            MutablePropertyValues pvs = beanDefinition.getPropertyValues();

            PropertyValue propertyValue = pvs.getPropertyValueList().get(0);
            List<?> listValue = (List<?>) propertyValue.getValue();

            applyMessageToNestedListBean(message, listValue, key);
        } else if (StringUtils.contains(key, ".")) {
            applyMessageToNestedBean(message, beanDefinition, key);
        } else {
            applyMessageTextToPropertyValue(key, message.getText(), beanDefinition);
        }
    }

    /**
     * Applies the given message text to the property within the given bean definition
     *
     * <p>
     * The message text is applied to the bean definiton based on the property path. The path could be nested
     * in which case the property values of the bean definition are traversed to find the nested bean definition
     * that should hold the property value. The path could also represent a nested list bean. In this case a
     * helper method is called to find the correct list bean to apply the message to
     * </p>
     *
     * @param message message containing the text to apply
     * @param beanDefinition bean definition containing the property
     * @param propertyPath path to property within the bean definition the message should be applied to
     */
    protected void applyMessageToNestedBean(Message message, BeanDefinition beanDefinition, String propertyPath) {
        MutablePropertyValues pvs = beanDefinition.getPropertyValues();

        String beanPath = StringUtils.substringBefore(propertyPath, ".");
        String nestedPropertyPath = StringUtils.substringAfter(propertyPath, ".");

        boolean foundNestedBean = false;
        while (StringUtils.isNotBlank(nestedPropertyPath)) {
            if (pvs.contains(beanPath)) {
                PropertyValue propertyValue = pvs.getPropertyValue(beanPath);

                BeanDefinition propertyBeanDefinition = getPropertyValueBeanDefinition(propertyValue);
                if (propertyBeanDefinition != null) {
                    applyMessageToNestedBean(message, propertyBeanDefinition, nestedPropertyPath);

                    foundNestedBean = true;
                    break;
                } else if (propertyValue.getValue() instanceof List) {
                    applyMessageToNestedListBean(message, (List<?>) propertyValue.getValue(), nestedPropertyPath);

                    foundNestedBean = true;
                    break;
                }
            }

            beanPath += "." + StringUtils.substringBefore(nestedPropertyPath, ".");
            nestedPropertyPath = StringUtils.substringAfter(nestedPropertyPath, ".");
        }

        if (!foundNestedBean) {
            applyMessageTextToPropertyValue(propertyPath, message.getText(), beanDefinition);
        }
    }

    /**
     * Applies a message to a nested list bean definition
     *
     * <p>
     * Here the property path first gives an identifier value (such as property name) to use for finding
     * the bean definition with the list the message should apply to. Any part of the path after the identifier
     * value is treated like a nested property path
     * </p>
     *
     * @param message message instance that contains the text to apply
     * @param listPropertyValue property value list that should contain the bean definition the message
     * will be applied to
     * @param propertyPath path to the bean definition the message should apply to
     */
    protected void applyMessageToNestedListBean(Message message, List<?> listPropertyValue, String propertyPath) {
        // property path must be nested, with first part giving the list bean identifier value
        if (!StringUtils.contains(propertyPath, ".")) {
            throw new RiceRuntimeException(
                    "Key for nested list bean must contain the identifer value followed by the path.");
        }

        String listIdentifierPropertyValue = StringUtils.substringBefore(propertyPath, ".");
        String listBeanPropertyPath = StringUtils.substringAfter(propertyPath, ".");

        // iterate through list and find beans that match the given identifier
        for (int i = 0; i < listPropertyValue.size(); i++) {
            Object elem = listPropertyValue.get(i);

            if ((elem instanceof BeanDefinition) || (elem instanceof BeanDefinitionHolder)) {
                BeanDefinition beanDefinition;
                if (elem instanceof BeanDefinition) {
                    beanDefinition = (BeanDefinition) elem;
                } else {
                    beanDefinition = ((BeanDefinitionHolder) elem).getBeanDefinition();
                }

                boolean isMatch = isBeanMessageMatch(listIdentifierPropertyValue, beanDefinition);
                if (isMatch) {
                    if (StringUtils.contains(listBeanPropertyPath, ".")) {
                        applyMessageToNestedBean(message, beanDefinition, listBeanPropertyPath);
                    } else {
                        applyMessageTextToPropertyValue(listBeanPropertyPath, message.getText(), beanDefinition);
                    }
                }
            }
        }
    }

    /**
     * Determines whether the given bean definition is a matched based on the given identifier value
     *
     * <p>
     * Based on the class for the bean definition an identifier property name is used, the corresponding value
     * from the bean definition is then retrieved and compared to the given value to determine whether the
     * bean definition is a match
     * </p>
     *
     * @param matchListIdentifierPropertyValue property value to match bean definitions on
     * @param beanDefinition bean definition to determine the match for
     * @return boolean true if the bean definition is a match, false if not
     */
    protected boolean isBeanMessageMatch(String matchListIdentifierPropertyValue, BeanDefinition beanDefinition) {
        boolean isMatch = false;

        String listIdentifierPropertyName = null;

        Class<?> beanClass = getBeanClass(beanDefinition, beanFactory);
        if (DataBinding.class.isAssignableFrom(beanClass)) {
            listIdentifierPropertyName = KRADPropertyConstants.PROPERTY_NAME;
        } else if (Action.class.isAssignableFrom(beanClass) || ActionField.class.isAssignableFrom(beanClass)) {
            listIdentifierPropertyName = KRADPropertyConstants.METHOD_TO_CALL;
        } else if (KeyValue.class.isAssignableFrom(beanClass)) {
            listIdentifierPropertyName = KRADPropertyConstants.KEY;
        }

        if (StringUtils.isNotBlank(listIdentifierPropertyName)) {
            String listIdentifierPropertyValue = findPropertyValueInBeanDefinition(beanDefinition,
                    listIdentifierPropertyName);
            if (listIdentifierPropertyValue != null) {
                isMatch = StringUtils.equals(listIdentifierPropertyValue, matchListIdentifierPropertyValue);
            }
        }

        return isMatch;
    }

    /**
     * Attempts to find a property value for the given property name within the bean definition, if the property
     * does not exist in the bean and there is a parent, the parent is checked for containing the property. This
     * continues until a property value is found or all the parents have been traversed
     *
     * @param beanDefinition bean definition to find property value in
     * @param propertyName name of the property to find the value for
     * @return String value for property in the bean definition or null if the property was not found
     */
    protected String findPropertyValueInBeanDefinition(BeanDefinition beanDefinition, String propertyName) {
        String beanPropertyValue = null;

        MutablePropertyValues pvs = beanDefinition.getPropertyValues();
        if (pvs.contains(propertyName)) {
            PropertyValue propertyValue = pvs.getPropertyValue(propertyName);
            if (propertyValue.getValue() != null) {
                beanPropertyValue = propertyValue.getValue().toString();
            }
        } else {
            if (StringUtils.isNotBlank(beanDefinition.getParentName())) {
                BeanDefinition parentBeanDefinition = beanFactory.getBeanDefinition(beanDefinition.getParentName());

                beanPropertyValue = findPropertyValueInBeanDefinition(parentBeanDefinition, propertyName);
            }
        }

        return beanPropertyValue;
    }

    /**
     * Checks a string property value for a message placeholder and if found the message is retrieved and updated
     * in the property value
     *
     * @param propertyValue string value to process for message placeholders
     * @param nestedBeanStack stack of bean definitions that contain the property, used to determine the namespace
     * and component for the message retrieval
     * @return String new value for the property (possibly modified from an external message)
     */
    protected String processMessagePlaceholders(String propertyValue, Stack<BeanDefinitionHolder> nestedBeanStack) {
        String trimmedPropertyValue = StringUtils.stripStart(propertyValue, " ");
        if (StringUtils.isBlank(trimmedPropertyValue)) {
            return propertyValue;
        }

        String newPropertyValue = propertyValue;

        // first check for a replacement message key
        if (trimmedPropertyValue.startsWith(KRADConstants.MESSAGE_KEY_PLACEHOLDER_PREFIX) && StringUtils.contains(
                trimmedPropertyValue, KRADConstants.MESSAGE_KEY_PLACEHOLDER_SUFFIX)) {
            String messageKeyStr = StringUtils.substringBetween(trimmedPropertyValue,
                    KRADConstants.MESSAGE_KEY_PLACEHOLDER_PREFIX, KRADConstants.MESSAGE_KEY_PLACEHOLDER_SUFFIX);

            // get any default specified value (given after the message key)
            String messageKeyWithPlaceholder = KRADConstants.MESSAGE_KEY_PLACEHOLDER_PREFIX + messageKeyStr +
                    KRADConstants.MESSAGE_KEY_PLACEHOLDER_SUFFIX;

            String defaultPropertyValue = StringUtils.substringAfter(trimmedPropertyValue, messageKeyWithPlaceholder);

            // set the new property value to the message text (if found), or the default value if a message was not found
            // note the message text could be an empty string, in which case it will override the default
            String messageText = getMessageTextForKey(messageKeyStr, nestedBeanStack);
            if (messageText != null) {
                // if default value set then we need to merge any expressions
                if (StringUtils.isNotBlank(defaultPropertyValue)) {
                    newPropertyValue = getMergedMessageText(messageText, defaultPropertyValue);
                } else {
                    newPropertyValue = messageText;
                }
            } else {
                newPropertyValue = defaultPropertyValue;
            }
        }
        // now check for message keys within an expression
        else if (StringUtils.contains(trimmedPropertyValue, KRADConstants.EXPRESSION_MESSAGE_PLACEHOLDER_PREFIX)) {
            String[] expressionMessageKeys = StringUtils.substringsBetween(newPropertyValue,
                    KRADConstants.EXPRESSION_MESSAGE_PLACEHOLDER_PREFIX,
                    KRADConstants.EXPRESSION_MESSAGE_PLACEHOLDER_SUFFIX);

            for (String expressionMessageKey : expressionMessageKeys) {
                String expressionMessageText = getMessageTextForKey(expressionMessageKey, nestedBeanStack);
                newPropertyValue = StringUtils.replace(newPropertyValue,
                        KRADConstants.EXPRESSION_MESSAGE_PLACEHOLDER_PREFIX + expressionMessageKey +
                                KRADConstants.EXPRESSION_MESSAGE_PLACEHOLDER_SUFFIX, expressionMessageText);
            }
        }

        return newPropertyValue;
    }

    /**
     * Retrieves the test associated with the message give by the message key string
     *
     * @param messageKeyStr key string for the message, can contain just the key, or also the component and/or
     * namespace. If component or namespace not given it is determined from the bean stack
     * @param nestedBeanStack bean stack that contains the property for which the message applies
     * @return String test associated with the message
     */
    protected String getMessageTextForKey(String messageKeyStr, Stack<BeanDefinitionHolder> nestedBeanStack) {
        String namespace = null;
        String componentCode = null;
        String key = null;

        // check for specification of namespace and component
        if (StringUtils.contains(messageKeyStr, ":")) {
            String[] messageParams = StringUtils.split(messageKeyStr, ":");

            if (messageParams.length == 3) {
                namespace = messageParams[0];
                componentCode = messageParams[1];
                key = messageParams[2];
            } else if (messageParams.length == 2) {
                componentCode = messageParams[0];
                key = messageParams[1];
            } else {
                throw new RiceRuntimeException("Message key '" + messageKeyStr + "' has an invalid format");
            }
        } else {
            key = messageKeyStr;
        }

        if (StringUtils.isBlank(namespace)) {
            namespace = getNamespaceForBeanInStack(nestedBeanStack);
        }

        if (StringUtils.isBlank(componentCode)) {
            for (int i = nestedBeanStack.size() - 1; i >= 0; i--) {
                BeanDefinitionHolder definitionHolder = nestedBeanStack.get(i);
                componentCode = getComponentForBean(definitionHolder.getBeanName(),
                        definitionHolder.getBeanDefinition());
                if (StringUtils.isNotBlank(componentCode)) {
                    break;
                }
            }
        }

        String messageText = null;
        if (StringUtils.isNotBlank(namespace) && StringUtils.isNotBlank(componentCode) && StringUtils.isNotBlank(key)) {
            messageText = getMessageService().getMessageText(namespace, componentCode, key);
        }

        return messageText;
    }

    /**
     * Applies the given message text to the bean definition with the given property name, if a current
     * value exists for the property name the value is checked for expressions which are then merged with
     * the message
     *
     * @param propertyName - name of the property to set on the bean definition
     * @param messageText - message text that will be the property value
     * @param beanDefinition - bean definition to set property on
     */
    protected void applyMessageTextToPropertyValue(String propertyName, String messageText,
            BeanDefinition beanDefinition) {
        String newPropertyValue = messageText;

        MutablePropertyValues pvs = beanDefinition.getPropertyValues();
        if (pvs.contains(propertyName)) {
            PropertyValue propertyValue = pvs.getPropertyValue(propertyName);

            String stringPropertyValue = getStringValue(propertyValue.getValue());
            if (StringUtils.isNotBlank(stringPropertyValue)) {
                newPropertyValue = getMergedMessageText(messageText, stringPropertyValue);
            }
        }

        applyPropertyValueToBean(propertyName, newPropertyValue, pvs);
    }

    /**
     * Prepares the message text that will replace the property value checking for any expression placeholders
     *
     * <p>
     * The message text may contain placeholders (using brace delimiters) for expression placement. It is
     * expected when these placeholders are given the property value contains the expressions (using the
     * expression placeholders) that will be inserted into the message text
     * </p>
     *
     * @param messageText - raw text of the message
     * @param propertyValue - current value for the property
     * @return String the message text with expressions inserted (if any expressions were found)
     */
    protected String getMergedMessageText(String messageText, String propertyValue) {
        String mergedText = messageText;

        String[] expressions = StringUtils.substringsBetween(propertyValue, UifConstants.EL_PLACEHOLDER_PREFIX,
                UifConstants.EL_PLACEHOLDER_SUFFIX);
        if ((expressions != null) && expressions.length > 0) {
            // add expression placeholders back on
            String[] messageParameters = new String[expressions.length];
            for (int i = 0; i < expressions.length; i++) {
                String expression = expressions[i];

                expression = UifConstants.EL_PLACEHOLDER_PREFIX + expression + UifConstants.EL_PLACEHOLDER_SUFFIX;
                messageParameters[i] = expression;
            }

            // escape single quotes for message format process
            messageText = messageText.replace("'", "''");
            try {
                mergedText = MessageFormat.format(messageText, messageParameters);
            } catch (IllegalArgumentException e) {
                throw new RiceRuntimeException(
                        "Unable to merge expressions with message text. Expression count is: " + expressions.length, e);
            }
        }

        return mergedText;
    }

    /**
     * Walks up the stack of bean definitions until a namespace is found and returns that namespace
     *
     * @param nestedBeanStack stack of bean definitions to find namespace for
     * @return String namespace found in stack or null if one was not found
     */
    protected String getNamespaceForBeanInStack(Stack<BeanDefinitionHolder> nestedBeanStack) {
        String namespace = null;

        if (nestedBeanStack != null) {
            for (int i = nestedBeanStack.size() - 1; i >= 0; i--) {
                BeanDefinitionHolder definitionHolder = nestedBeanStack.get(i);
                namespace = getNamespaceForBean(definitionHolder.getBeanName(), definitionHolder.getBeanDefinition());
                if (StringUtils.isNotBlank(namespace)) {
                    break;
                }
            }
        }

        return namespace;
    }

    /**
     * Retrieves the namespace associated with the bean definition
     *
     * @param beanName name of the bean to find namespace for
     * @param beanDefinition bean definition to find namespace for
     * @return String namespace for bean or null if a namespace was not found
     */
    protected String getNamespaceForBean(String beanName, BeanDefinition beanDefinition) {
        String namespace = null;

        MutablePropertyValues pvs = beanDefinition.getPropertyValues();
        if (pvs.contains(KRADPropertyConstants.NAMESPACE_CODE)) {
            PropertyValue propertyValue = pvs.getPropertyValue(KRADPropertyConstants.NAMESPACE_CODE);
            namespace = getStringValue(propertyValue.getValue());
        } else if (StringUtils.isNotBlank(beanName) && !isGeneratedBeanName(beanName)) {
            // if not on bean definition, get from associated module in the dictionary
            namespace = dataDictionary.getNamespaceForBeanDefinition(beanName);
        }

        return namespace;
    }

    /**
     * Retrieves the component code associated with the bean definition
     *
     * @param beanName name of the bean to find component code for
     * @param beanDefinition bean definition to find component code for
     * @return String component code for bean or null if a component code was not found
     */
    protected String getComponentForBean(String beanName, BeanDefinition beanDefinition) {
        String componentCode = null;

        MutablePropertyValues pvs = beanDefinition.getPropertyValues();
        if (pvs.contains(KRADPropertyConstants.COMPONENT_CODE)) {
            PropertyValue propertyValue = pvs.getPropertyValue(KRADPropertyConstants.COMPONENT_CODE);

            componentCode = getStringValue(propertyValue.getValue());
        }

        if ((componentCode == null) && StringUtils.isNotBlank(beanName) && !isGeneratedBeanName(beanName)) {
            componentCode = beanName;
        }

        if (StringUtils.isNotBlank(componentCode)) {
            componentCode = StringUtils.removeEnd(componentCode, KRADConstants.DICTIONARY_BEAN_PARENT_SUFFIX);
        }

        return componentCode;
    }

    /**
     * Returns instance of the Message Service
     *
     * @return MessageService isntanc
     */
    protected MessageService getMessageService() {
        return KRADServiceLocatorWeb.getMessageService();
    }
}
