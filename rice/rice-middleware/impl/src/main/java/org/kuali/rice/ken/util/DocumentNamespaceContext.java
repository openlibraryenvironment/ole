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

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

/**
 * XPath NamespaceContext implementation that delegates all lookups to a DOM Document,
 * which supplies all prefix/NS mappings defined in the doc.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentNamespaceContext implements NamespaceContext {
    private static final Logger LOG = Logger.getLogger(DocumentNamespaceContext.class);

    // the DOM Document
    private final Document doc;

    /**
     * Constructs a DocumentNamespaceContext.java.
     * @param doc
     */
    public DocumentNamespaceContext(Document doc) {
        this.doc = doc;
    }

    /**
     * @see javax.xml.namespace.NamespaceContext#getNamespaceURI(java.lang.String)
     */
    public String getNamespaceURI(String prefix) {
        LOG.debug("getNamespaceURI(" + prefix + ")");
        if (prefix == null) {
            throw new IllegalArgumentException("The prefix cannot be null.");
        }
        if (prefix.length() == 0) {
            return doc.lookupNamespaceURI(null);
        }
        return doc.lookupNamespaceURI(prefix);
    }

    /**
     * @see javax.xml.namespace.NamespaceContext#getPrefix(java.lang.String)
     */
    public String getPrefix(String namespaceURI) {
        LOG.debug("getPrefix(" + namespaceURI + ")");
        if (namespaceURI == null) {
            throw new IllegalArgumentException("The namespace uri cannot be null.");
        }
        return doc.lookupPrefix(namespaceURI);
    }

    /**
     * @see javax.xml.namespace.NamespaceContext#getPrefixes(java.lang.String)
     */
    public Iterator getPrefixes(String namespaceURI) {
        LOG.debug("getPrefixes(" + namespaceURI + ")");
        if (namespaceURI == null) {
            throw new IllegalArgumentException("The namespace uri cannot be null.");
        }
        List<String> list = new ArrayList<String>(1);
        String s = getPrefix(namespaceURI);
        if (s != null) {
            list.add(s);
        }
        return list.iterator();
    }
}
