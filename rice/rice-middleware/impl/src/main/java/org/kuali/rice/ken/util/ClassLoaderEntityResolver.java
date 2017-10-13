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

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Internal notification EntityResolver which resolves system ids with the "resource:" prefix to ClassLoader resources
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ClassLoaderEntityResolver extends ClassLoaderResourceResolver implements EntityResolver {
    /**
     * Constructs a ClassLoaderEntityResolver.java.
     */
    public ClassLoaderEntityResolver() {
        super();
    }

    /**
     * Constructs a ClassLoaderEntityResolver.java.
     * @param base
     * @param prefix
     */
    public ClassLoaderEntityResolver(String base, String prefix) {
        super(base, prefix);
    }
    
    /**
     * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String, java.lang.String)
     */
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        LOG.debug("Resolving '" + publicId + "' / '" + systemId + "'");
        String path = resolveSystemId(systemId);
        if (path == null) {
            LOG.error("Unable to resolve system id '" + systemId + "' locally...delegating to default resolution strategy.");
            return null;
        }
        LOG.debug("Looking up resource '" + path + "' for entity '" + systemId + "'");
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        if (is == null) {
            String message = "Unable to find schema (" + path + ") for: " + systemId;
            LOG.warn(message);
            // necessary if processContents is lax, because lax doesn't care...if it doesn't resolve it won't validate
            // (not quite clear, as lax could be interpreted as *if the namespace is valid*, treating a present, but invalid
            // namespace as a fatal error. instead, apparently a present but invalid namespace is ignored with 'lax'
            // so w should use strict to ensure that is an error instead of throwing an exception here gratuitously
            // which will screw up compound resolution
            //throw new SAXException(message);
            return null;
        }
        return new InputSource(is);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "[ClassLoaderEntityResolver: base=" + base + ", prefix=" + prefix + "]";
    }
}
