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
package org.kuali.rice.krad.messages.providers;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.krad.messages.Message;
import org.kuali.rice.krad.messages.MessageProvider;
import org.kuali.rice.krad.messages.MessageService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.ModuleService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Implementation of {@link MessageProvider} that stores messages in resource files
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ResourceMessageProvider implements MessageProvider {

    private static final String COMPONENT_PLACEHOLDER_BEGIN = "@cmp{";
    private static final String COMPONENT_PLACEHOLDER_END = "}";

    protected Map<String, List<ResourceBundle>> cachedResourceBundles;

    public ResourceMessageProvider() {
        cachedResourceBundles = new HashMap<String, List<ResourceBundle>>();
    }

    /**
     * Iterates through the resource bundles for the give namespace (or the application if namespace is not given)
     * and finds the message that matches the given key
     *
     * <p>
     * If the message is found in more than one bundle, the text from the bundle that is loaded last will be used
     * </p>
     *
     * <p>
     * If the given component code is the default component, resource keys that do not have a component defined
     * and match the given key will also be considered matches
     * </p>
     *
     * @see org.kuali.rice.krad.messages.MessageProvider#getMessage(java.lang.String, java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public Message getMessage(String namespace, String component, String key, String locale) {
        Message message = null;

        List<ResourceBundle> bundles = getCachedResourceBundles(namespace, locale);

        // iterate through bundles and find message text, if more than one bundle contains the message
        // the last one iterated over will be used
        String messageText = null;
        for (ResourceBundle bundle : bundles) {
            // check for key with component first
            String resourceKey = COMPONENT_PLACEHOLDER_BEGIN + component + COMPONENT_PLACEHOLDER_END + key;
            if (bundle.containsKey(resourceKey)) {
                messageText = bundle.getString(resourceKey);
            }
            // if component is default then check for key without component code
            else if (MessageService.DEFAULT_COMPONENT_CODE.equals(component) && bundle.containsKey(key)) {
                messageText = bundle.getString(key);
            }
        }

        // if message text was found build message object
        if (StringUtils.isNotBlank(messageText)) {
            message = buildMessage(namespace, component, key, messageText, locale);
        }

        return message;
    }

    /**
     * Iterates through the resource bundles for the give namespace (or the application if namespace is not given)
     * and finds all messages that match the given namespace
     *
     * <p>
     * If the same resource key is found in more than one bundle, the text from the bundle that is
     * loaded last will be used
     * </p>
     *
     * <p>
     * If the given component code is the default component, resource keys that do not have a component defined
     * and match the given key will also be considered matches
     * </p>
     *
     * @see org.kuali.rice.krad.messages.MessageProvider#getAllMessagesForComponent(java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    public Collection<Message> getAllMessagesForComponent(String namespace, String component, String locale) {
        List<ResourceBundle> bundles = getCachedResourceBundles(namespace, locale);

        Map<String, Message> messagesByKey = new HashMap<String, Message>();
        for (ResourceBundle bundle : bundles) {
            Enumeration<String> resourceKeys = bundle.getKeys();
            while (resourceKeys.hasMoreElements()) {
                String resourceKey = resourceKeys.nextElement();

                boolean match = false;
                if (StringUtils.contains(resourceKey,
                        COMPONENT_PLACEHOLDER_BEGIN + component + COMPONENT_PLACEHOLDER_END)) {
                    match = true;
                } else if (MessageService.DEFAULT_COMPONENT_CODE.equals(component) && !StringUtils.contains(resourceKey,
                        COMPONENT_PLACEHOLDER_BEGIN)) {
                    match = true;
                }

                if (match) {
                    String messageText = bundle.getString(resourceKey);

                    resourceKey = cleanResourceKey(resourceKey);
                    Message message = buildMessage(namespace, component, resourceKey, messageText, locale);
                    messagesByKey.put(resourceKey, message);
                }
            }
        }

        return messagesByKey.values();
    }

    /**
     * Removes any component declaration within the given resource key
     *
     * @param resourceKey resource key to clean
     * @return String cleaned resource key
     */
    protected String cleanResourceKey(String resourceKey) {
        String cleanedKey = resourceKey;

        String component = StringUtils.substringBetween(cleanedKey, COMPONENT_PLACEHOLDER_BEGIN,
                COMPONENT_PLACEHOLDER_END);
        if (StringUtils.isNotBlank(component)) {
            cleanedKey = StringUtils.remove(cleanedKey,
                    COMPONENT_PLACEHOLDER_BEGIN + component + COMPONENT_PLACEHOLDER_END);
        }

        return cleanedKey;
    }

    /**
     * Helper method to build a {@link Message} object from the given parameters
     *
     * @param namespace namespace for the message
     * @param component component code for the message
     * @param key message key
     * @param messageText text for the message
     * @param locale locale of the message
     * @return Message instance populated with parameters
     */
    protected Message buildMessage(String namespace, String component, String key, String messageText, String locale) {
        Message message = new Message();

        message.setNamespaceCode(namespace);
        message.setComponentCode(component);

        key = cleanResourceKey(key);
        message.setKey(key);

        message.setText(messageText);
        message.setLocale(locale);

        return message;
    }

    /**
     * Retrieves the list of resource bundles for the given namespace or locale from cache if present, otherwise
     * the list is retrieved and then stored in cache for subsequent calls
     *
     * @param namespace namespace to retrieve bundles for
     * @param localeCode locale code to use in selecting bundles
     * @return List<ResourceBundle> list of resource bundles for the namespace or empty list if none were found
     */
    protected List<ResourceBundle> getCachedResourceBundles(String namespace, String localeCode) {
        if (StringUtils.isBlank(namespace)) {
            namespace = MessageService.DEFAULT_NAMESPACE_CODE;
        }

        String cacheKey = namespace + "|" + localeCode;
        if (cachedResourceBundles.containsKey(cacheKey)) {
            return cachedResourceBundles.get(cacheKey);
        }

        List<ResourceBundle> bundles = null;
        if (StringUtils.isBlank(namespace) || MessageService.DEFAULT_NAMESPACE_CODE.equals(namespace)) {
            bundles = getResourceBundlesForApplication(localeCode);
        } else {
            bundles = getResourceBundlesForNamespace(namespace, localeCode);
        }

        cachedResourceBundles.put(cacheKey, bundles);

        return bundles;
    }

    /**
     * Retrieves the configured {@link ResourceBundle} instances for the given namespace and locale
     *
     * @param namespace namespace to retrieve bundles for
     * @param localeCode locale code to use in selecting bundles
     * @return List<ResourceBundle> list of resource bundles for the namespace or empty list if none were found
     */
    protected List<ResourceBundle> getResourceBundlesForNamespace(String namespace, String localeCode) {
        List<String> resourceBundleNames = getResourceBundleNamesForNamespace(namespace);

        return getResourceBundles(resourceBundleNames, localeCode);
    }

    /**
     * Retrieves the configured {@link ResourceBundle} instances for the application using the given locale code
     *
     * @param localeCode locale code to use in selecting bundles
     * @return List<ResourceBundle> list of resource bundles for the application or empty list if none were found
     */
    protected List<ResourceBundle> getResourceBundlesForApplication(String localeCode) {
        List<String> resourceBundleNames = getResourceBundleNamesForApplication();

        return getResourceBundles(resourceBundleNames, localeCode);
    }

    /**
     * Helper method to build a list of resource bundles for the given list of bundle names and locale code
     *
     * <p>
     * For details on how resource bundles are selected given a bundle name and locale code see
     * {@link ResourceBundle}
     * </p>
     *
     * @param resourceBundleNames list of bundle names to get bundles for
     * @param localeCode locale code to use when selecting bundles
     * @return List<ResourceBundle> list of resource bundles (one for each bundle name if found)
     */
    protected List<ResourceBundle> getResourceBundles(List<String> resourceBundleNames, String localeCode) {
        List<ResourceBundle> resourceBundles = new ArrayList<ResourceBundle>();

        String[] localeIdentifiers = StringUtils.split(localeCode, "-");
        if ((localeIdentifiers == null) || (localeIdentifiers.length != 2)) {
            throw new RiceRuntimeException("Invalid locale code: " + (localeCode == null ? "Null" : localeCode));
        }

        Locale locale = new Locale(localeIdentifiers[0], localeIdentifiers[1]);

        if (resourceBundleNames != null) {
            for (String bundleName : resourceBundleNames) {
                ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);
                if (bundle != null) {
                    resourceBundles.add(bundle);
                }
            }
        }

        return resourceBundles;
    }

    /**
     * Retrieves the list of configured bundle names for the namespace
     *
     * <p>
     * Resource bundle names are configured for a namespace using the property <code>resourceBundleNames</code>
     * on the corresponding {@link org.kuali.rice.krad.bo.ModuleConfiguration}
     * </p>
     *
     * @param namespace namespace to retrieve configured bundle names for
     * @return List<String> list of bundle names or null if module was not found for given namespace
     */
    protected List<String> getResourceBundleNamesForNamespace(String namespace) {
        ModuleService moduleService = KRADServiceLocatorWeb.getKualiModuleService().getModuleServiceByNamespaceCode(
                namespace);
        if (moduleService != null) {
            return moduleService.getModuleConfiguration().getResourceBundleNames();
        }

        return null;
    }

    /**
     * Retrieves the list of configured bundle names for the application
     *
     * <p>
     * Resource bundle names are configured for the application using the configuration property
     * <code>resourceBundleNames</code>
     * </p>
     *
     * @return List<String> list of bundle names configured for the application
     */
    protected List<String> getResourceBundleNamesForApplication() {
        String resourceBundleNamesConfig = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(
                "resourceBundleNames");
        if (StringUtils.isNotBlank(resourceBundleNamesConfig)) {
            String[] resourceBundleNames = StringUtils.split(resourceBundleNamesConfig, ",");

            return Arrays.asList(resourceBundleNames);
        }

        return null;
    }
}
