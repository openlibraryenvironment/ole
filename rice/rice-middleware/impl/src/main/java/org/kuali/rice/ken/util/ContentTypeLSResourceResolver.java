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

import java.io.Reader;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.kuali.rice.ken.bo.NotificationContentTypeBo;
import org.kuali.rice.ken.service.NotificationContentTypeService;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * Resource resolver for SchemaFactory.  For now used during validation of NotificationRequest content element.
 * Looks up XSD in NotificationContentType record.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ContentTypeLSResourceResolver extends ContentTypeResourceResolver implements LSResourceResolver {
    /**
     * Constructs a ContentTypeLSResourceResolver.java.
     * @param notificationContentTypeService
     */
    public ContentTypeLSResourceResolver(NotificationContentTypeService notificationContentTypeService) {
	super(notificationContentTypeService);
    }

    /**
     * @see org.w3c.dom.ls.LSResourceResolver#resolveResource(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
        if (!type.equals(XMLConstants.W3C_XML_SCHEMA_NS_URI)) {
            return null;
        }

        if (!systemId.startsWith(CONTENT_TYPE_PREFIX)) {
            LOG.warn("Cannot resolve non-ContentType resources");
            return null;
        }

        NotificationContentTypeBo notificationContentType = resolveContentType(systemId);
        if (notificationContentType == null) {
	    LOG.error("Unable to resolve system id '" + systemId + "' locally...delegating to default resolution strategy.");
	    return null;
	}

        Reader reader = new StringReader(notificationContentType.getXsd());
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation domImpl = builder.getDOMImplementation();
            DOMImplementationLS dils = (DOMImplementationLS) domImpl;
            LSInput input = dils.createLSInput();
            input.setCharacterStream(reader);
            return input;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
