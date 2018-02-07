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

import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * Resource resolver for SchemaFactory.  For now used during validation of NotificationRequest content element.
 * Looks up XSD from classloader.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ClassLoaderLSResourceResolver extends ClassLoaderResourceResolver implements LSResourceResolver {
    /**
     * Constructs a ClassLoaderLSResourceResolver.java.
     * @param base
     * @param prefix
     */
    public ClassLoaderLSResourceResolver(String base, String prefix) {
        super(base, prefix);
    }

    /**
     * @see org.w3c.dom.ls.LSResourceResolver#resolveResource(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
        if (!type.equals(XMLConstants.W3C_XML_SCHEMA_NS_URI)) {
            return null;
        }
        LOG.error(type);
        LOG.error(namespaceURI);
        LOG.error(publicId);
        LOG.error(systemId);
        LOG.error(baseURI);
        String path = resolveSystemId(systemId);
        if (path == null) {
            return null;
        }
        LOG.debug("Looking up resource '" + path + "' for system id '" + systemId + "'");
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        if (is == null) {
            String message = "Unable to find schema (" + path + ") for: " + systemId;
            LOG.error(message);
            throw new RuntimeException/*SAXException*/(message);
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation domImpl = builder.getDOMImplementation();
            DOMImplementationLS dils = (DOMImplementationLS) domImpl;
            LSInput input = dils.createLSInput();
            input.setByteStream(is);
            return input;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
