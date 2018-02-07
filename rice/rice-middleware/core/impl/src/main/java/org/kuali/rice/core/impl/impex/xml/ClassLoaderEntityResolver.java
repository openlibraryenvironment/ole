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
package org.kuali.rice.core.impl.impex.xml;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Internal workflow EntityResolver which resolves system ids with the
 * "resource:" prefix to ClassLoader resources
 * 
 * TODO: maybe prefix should be changed from "resource:" to "internal:" or just "workflow:"
 * given that it can be resolved in arbitrary ways other than ClassLoader "resources"
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ClassLoaderEntityResolver implements EntityResolver {
    private static final Logger LOG = Logger.getLogger(ClassLoaderEntityResolver.class);

    /**
     * This contains definitions for items in the core "xml" schema, i.e. base, id, lang, and space attributes. 
     */
    private static final String XML_NAMESPACE_SCHEMA = "http://www.w3.org/2001/xml.xsd";
    private static final String XSD_NAMESPACE_SCHEMA = "http://www.w3.org/2001/XMLSchema.xsd";
    private static final String XML_SCHEMA_DTD_PUBLIC_ID = "-//W3C//DTD XMLSCHEMA 200102//EN";
    private static final String DATATYPES_DTD_PUBLIC_ID = "datatypes";
    private static final String CLASSPATH_PREFIX = "classpath:";
    
    private final String base;
    public ClassLoaderEntityResolver() {
        this.base = "schema";
    }
    public ClassLoaderEntityResolver(String base) {
        this.base = base;
    }
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        LOG.debug("Resolving '" + publicId + "' / '" + systemId + "'");
        String path = "";
        if (XML_NAMESPACE_SCHEMA.equals(systemId)) {
            path = base + "/xml.xsd";
        } else if (systemId.equals(XSD_NAMESPACE_SCHEMA)) {
            path = base + "/XMLSchema.xsd";
        } else if (XML_SCHEMA_DTD_PUBLIC_ID.equals(publicId)) {
            path = base + "/XMLSchema.dtd";
        } else if (DATATYPES_DTD_PUBLIC_ID.equals(publicId)) {
            path = base + "/datatypes.dtd";
        } else if (systemId.startsWith(CLASSPATH_PREFIX)) {
            path = systemId.substring(CLASSPATH_PREFIX.length());
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
        } else if (systemId.startsWith("resource")) {
            path = base + "/" + systemId.substring("resource:".length());
            // ok, if the path does not itself end in .xsd or .dtd, it is bare/abstract
            // so realize it by appending .xsd
            // this allows us to support looking up files ending with ".dtd" through resource: without
            // having extra logic to attempt to look up both suffixes for every single resource:
            // (all of which except XMLSchema.dtd and datatypes.dtd at this point are .xsd files)
            if (!(systemId.endsWith(".xsd") || systemId.endsWith(".dtd"))) {
                path += ".xsd";
            }
        } else {
            LOG.error("Unable to resolve system id '" + systemId + "' or public id '" + publicId + "' locally...delegating to default resolution strategy.");
            return null;
        }
        InputStream is = ClassLoaderUtils.getDefaultClassLoader().getResourceAsStream(path);
        if (is == null) {
            String message = "Unable to find schema (" + path + ") for: " + systemId;
            LOG.error(message);
            throw new SAXException(message);
        }
        return new InputSource(is);
    }
}
