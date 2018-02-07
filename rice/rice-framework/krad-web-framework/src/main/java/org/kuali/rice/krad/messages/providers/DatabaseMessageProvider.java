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
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.search.SearchOperator;
import org.kuali.rice.krad.messages.Message;
import org.kuali.rice.krad.messages.MessageProvider;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.LookupService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link MessageProvider} that stores messages in a database
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DatabaseMessageProvider implements MessageProvider {
    private LookupService lookupService;

    /**
     * @see org.kuali.rice.krad.messages.MessageProvider#getMessage(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public Message getMessage(String namespace, String component, String key, String locale) {
        Collection<Message> results = getMessageByCriteria(namespace, component, key, locale);

        if ((results != null) && !results.isEmpty()) {
            return results.iterator().next();
        }

        return null;
    }

    /**
     * @see org.kuali.rice.krad.messages.MessageProvider#getAllMessagesForComponent(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public Collection<Message> getAllMessagesForComponent(String namespace, String component, String locale) {
        return getMessageByCriteria(namespace, component, null, locale);
    }

    /**
     * Performs a query using the {@link LookupService} to retrieve messages that match the given
     * namespace, component, name, and locale. Not parameters maybe empty in which case they will not be added
     * to the criteria
     *
     * @param namespace namespace code to search for
     * @param component component code to search for
     * @param key key of the parameter to find
     * @param locale locale code to search for
     * @return Collection<Message> matching messages or empty collection if not are found
     */
    protected Collection<Message> getMessageByCriteria(String namespace, String component, String key, String locale) {
        Collection<Message> results = null;

        Map<String, String> criteria = new HashMap<String, String>();

        if (StringUtils.isNotBlank(namespace)) {
            criteria.put("namespaceCode", namespace);
        }

        if (StringUtils.isNotBlank(component)) {
            criteria.put("componentCode", component);
        }

        if (StringUtils.isNotBlank(key)) {
            criteria.put("key", key);
        }

        if (StringUtils.isNotBlank(locale)) {
            // build or condition that will match just the language as well
            String[] localeIdentifiers = StringUtils.split(locale, "-");
            if ((localeIdentifiers == null) || (localeIdentifiers.length != 2)) {
                throw new RiceRuntimeException("Invalid locale code: " + (locale == null ? "Null" : locale));
            }

            String localeLanguage = localeIdentifiers[0];
            criteria.put("locale", locale + SearchOperator.OR.op() + localeLanguage);
        }

        results = getLookupService().findCollectionBySearch(Message.class, criteria);

        // filter out duplicate message results due to locale wildcard search (for example could have a match 
        // for en and en-US, in which case we want to take the record for the more specific locale
        Map<String, Message> uniqueMessages = new HashMap<String, Message>();
        for (Message message : results) {
            String messageKey = message.getNamespaceCode() + "|" + message.getComponentCode() + "|" + message.getKey();
            if (uniqueMessages.containsKey(messageKey)) {
                Message duplicateMessage = uniqueMessages.get(messageKey);
                // attempt to find the one that matches the locale exactly
                if (message.getLocale().equals(locale)) {
                    // use current message, otherwise leave the previous message
                    uniqueMessages.put(messageKey, message);
                }
            } else {
                uniqueMessages.put(messageKey, message);
            }
        }

        return uniqueMessages.values();
    }

    public LookupService getLookupService() {
        if (lookupService == null) {
            lookupService = KRADServiceLocatorWeb.getLookupService();
        }

        return lookupService;
    }

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }
}
