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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.NamespaceContext;

/**
 * XPath NamespaceContext implementation that delegates in sequence to a list of NamespaceContexts,
 * returning the first match. 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CompoundNamespaceContext implements NamespaceContext {
    private final List<NamespaceContext> contexts;

    /**
     * Constructs a CompoundNamespaceContext.java.
     * @param first
     * @param second
     */
    public CompoundNamespaceContext(NamespaceContext first, NamespaceContext second) {
        this.contexts = new ArrayList<NamespaceContext>(2);
        this.contexts.add(first);
        this.contexts.add(second);
    }
    
    /**
     * Constructs a CompoundNamespaceContext.java.
     * @param contexts
     */
    public CompoundNamespaceContext(List<NamespaceContext> contexts) {
        this.contexts = contexts;
    }

    /**
     * @see javax.xml.namespace.NamespaceContext#getNamespaceURI(java.lang.String)
     */
    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("The prefix cannot be null.");
        }
        for (NamespaceContext nc: contexts) {
            String uri = nc.getNamespaceURI(prefix);
            if (uri != null) {
                return uri;
            }
        }
        return null;
    }

    /**
     * @see javax.xml.namespace.NamespaceContext#getPrefix(java.lang.String)
     */
    public String getPrefix(String namespaceURI) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException("The namespace uri cannot be null.");
        }
        for (NamespaceContext nc: contexts) {
            String prefix = nc.getPrefix(namespaceURI);
            if (prefix != null) {
                return prefix;
            }
        }
        return null;
    }

    /**
     * @see javax.xml.namespace.NamespaceContext#getPrefixes(java.lang.String)
     */
    public Iterator getPrefixes(String namespaceURI) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException("The namespace uri cannot be null.");
        }
        for (NamespaceContext nc: contexts) {
            Iterator prefixes = nc.getPrefixes(namespaceURI);
            if (prefixes != null) {
                return prefixes;
            }
        }
        return null;
    }
}
