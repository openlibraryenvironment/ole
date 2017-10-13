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

import javax.xml.XMLConstants;

import org.apache.log4j.Logger;

/**
 * Utility base class for Entity and LSResource Resolvers that should resolve
 * arguments to resources in the ClassLoader.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ClassLoaderResourceResolver {
    protected final Logger LOG = Logger.getLogger(getClass());

    /**
     * This contains definitions for items in the core "xml" schema, i.e. base, id, lang, and space attributes. 
     */
    private static final String XML_NAMESPACE_SCHEMA = "http://www.w3.org/2001/xml.xsd";
    private static final String XSD_NAMESPACE_SCHEMA = XMLConstants.W3C_XML_SCHEMA_NS_URI;

    /**
     * Root path in class loader.  Defaults to "schema".
     */
    protected final String base;
    /**
     * Prefix of resources to honor.  Defaults to "" (no specific prefix).
     */
    protected final String prefix;

    /**
     * Constructs a ClassLoaderResourceResolver.java.
     */
    public ClassLoaderResourceResolver() {
        this.base = "schema";
        this.prefix = "";
    }
    
    /**
     * Constructs a ClassLoaderResourceResolver.java.
     * @param base
     * @param prefix
     */
    public ClassLoaderResourceResolver(String base, String prefix) {
        this.base = base;
        this.prefix = prefix;
    }

    /**
     * @param systemId
     * @return String
     */
    protected String resolveSystemId(String systemId) {
        if (systemId.equals(XML_NAMESPACE_SCHEMA)) {
            return base + "/xml.xsd";
        } else if (systemId.equals(XSD_NAMESPACE_SCHEMA)) {
            return base + "/XMLSchema.xsd";
        } else if (systemId.startsWith("resource:" + prefix +"/")) {
            /* It turns out that the stock XMLSchema.xsd refers to XMLSchema.dtd in a relative
               fashion which results in the parser qualifying it to some local file:// path
               which breaks our detection here.
               So I have made a small mod to the stock XMLSchema.xsd so that it instead refers to
               resource:XMLSchema.dtd which can be looked up locally.
               The same is true for XMLSchema.dtd with regard to datatypes.dtd, so I have also
               modified XMLSchema.dtd to refer to resource:datatypes.dtd.
               An alternative would be to rely on publicId, however that would essentially hard code
               the lookup to always be in the classpath and rule out being able to redirect the location
               of the physical resource through the systemId, which is useful.
            */

            String path = base + "/" + systemId.substring(("resource:" + prefix + "/").length());
            // ok, if the path does not itself end in .xsd or .dtd, it is bare/abstract
            // so realize it by appending .xsd
            // this allows us to support looking up files ending with ".dtd" through resource: without
            // having extra logic to attempt to look up both suffixes for every single resource:
            // (all of which except XMLSchema.dtd and datatypes.dtd at this point are .xsd files)
            if (!(systemId.endsWith(".xsd") || systemId.endsWith(".dtd"))) {
                path += ".xsd";
            }
            return path;
        } else {
            return null;
        }
    }
}
