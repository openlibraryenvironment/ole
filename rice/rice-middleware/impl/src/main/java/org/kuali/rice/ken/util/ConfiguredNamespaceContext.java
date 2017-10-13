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
package org.kuali.rice.ken.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

import org.apache.log4j.Logger;

/**
 * XPath NamespaceContext that is configured with a predefined prefix->NS map.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ConfiguredNamespaceContext implements NamespaceContext {
    private static final Logger LOG = Logger.getLogger(ConfiguredNamespaceContext.class);

    private Map<String, String> prefixToNamespace = new HashMap<String, String>();
    private Map<String, HashSet<String>> namespaceToPrefix = new HashMap<String, HashSet<String>>();

    /**
     * Constructs a ConfiguredNamespaceContext.java.
     * @param prefixToNamespace
     */
    public ConfiguredNamespaceContext(Map<String, String> prefixToNamespace) {
        this.prefixToNamespace = prefixToNamespace;
        // create a reverse namespace to prefix(es) map
        for (Map.Entry<String, String> entry: prefixToNamespace.entrySet()) {
            String namespace = entry.getValue();
            String prefix = entry.getKey();
            HashSet<String> prefixes = namespaceToPrefix.get(namespace);
            if (prefixes == null) {
                prefixes = new HashSet<String>(4);
                namespaceToPrefix.put(namespace, prefixes);
            }
            prefixes.add(prefix);
        }
    }

    /**
     * @see javax.xml.namespace.NamespaceContext#getNamespaceURI(java.lang.String)
     */
    public String getNamespaceURI(String prefix) {
        //LOG.trace("getNamespaceURI(" + prefix + ")");
        if (prefix == null) {
            throw new IllegalArgumentException("The prefix cannot be null.");
        }

        return prefixToNamespace.get(prefix);
    }

    /**
     * @see javax.xml.namespace.NamespaceContext#getPrefix(java.lang.String)
     */
    public String getPrefix(String namespaceURI) {
        //LOG.trace("getPrefix(" + namespaceURI + ")");
        if (namespaceURI == null) {
            throw new IllegalArgumentException("The namespace uri cannot be null.");
        }
        Iterator<String> prefixes = getPrefixes(namespaceURI);
        if (prefixes != null) {
            return prefixes.next();
        } else {
            return null;
        }
    }

    /**
     * @see javax.xml.namespace.NamespaceContext#getPrefixes(java.lang.String)
     */
    public Iterator<String> getPrefixes(String namespaceURI) {
        //LOG.trace("getPrefixes(" + namespaceURI + ")");
        if (namespaceURI == null) {
            throw new IllegalArgumentException("The namespace uri cannot be null.");
        }

        HashSet<String> prefixes = namespaceToPrefix.get(namespaceURI);
        if (prefixes != null && prefixes.size() > 0) {
            return prefixes.iterator();
        } else {
            return null;
        }
    }
}
