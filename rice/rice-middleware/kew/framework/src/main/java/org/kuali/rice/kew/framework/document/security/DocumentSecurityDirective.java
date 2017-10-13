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
package org.kuali.rice.kew.framework.document.security;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.ModelObjectUtils;
import org.kuali.rice.kew.api.document.Document;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;
import java.util.List;

/**
 * Defines a directive for processing a list of security attributes against a supplied list of documents.  The names of
 * these security attributes represent the name of an {@link org.kuali.rice.kew.api.extension.ExtensionDefinition} which
 * will be used to load the appropriate {@link DocumentSecurityAttribute} implementation in order to perform the security filtering.
 *
 * <p>The actual directive is supplied to the appropriate application that is responsible for the given security attributes
 * by invoking that applications {@link DocumentSecurityHandlerService}.  This class primarily functions as a form of
 * data transport in order to package and send the required information.</p>
 *
 * @see DocumentSecurityAttribute
 * @see DocumentSecurityHandlerService
 * @see org.kuali.rice.kew.api.extension.ExtensionDefinition
 */
@XmlRootElement(name = DocumentSecurityDirective.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DocumentSecurityDirective.Constants.TYPE_NAME, propOrder = {
    DocumentSecurityDirective.Elements.DOCUMENT_SECURITY_ATTRIBUTE_NAMES,
    DocumentSecurityDirective.Elements.DOCUMENTS,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class DocumentSecurityDirective {

    @XmlElementWrapper(name = Elements.DOCUMENT_SECURITY_ATTRIBUTE_NAMES, required = true)
    @XmlElement(name = Elements.DOCUMENT_SECURITY_ATTRIBUTE_NAME, required = true)
    private final List<String> documentSecurityAttributeNames;

    @XmlElementWrapper(name = Elements.DOCUMENTS, required = true)
    @XmlElement(name = Elements.DOCUMENT, required = true)
    private final List<Document> documents;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    @SuppressWarnings("unused")
    private DocumentSecurityDirective() {
        this.documentSecurityAttributeNames = null;
        this.documents = null;
    }

    private DocumentSecurityDirective(List<String> documentSecurityAttributeNames, List<Document> documents) {
        if (CollectionUtils.isEmpty(documentSecurityAttributeNames)) {
            throw new IllegalArgumentException("documentSecurityAttributeNames cannot be null or empty");
        }
        this.documentSecurityAttributeNames = ModelObjectUtils.createImmutableCopy(documentSecurityAttributeNames);
        this.documents = ModelObjectUtils.createImmutableCopy(documents);
    }

    /**
     * Creates a new security directive from the given list of document secruity attribute names and documents.
     *
     * @param documentSecurityAttributeNames the list of document security attribute names with which to create this
     * security directive
     * @param documents the list of documents with which to create this security directive
     *
     * @return a new document security directive instance
     *
     * @throws IllegalArgumentException if the given list of security attribute names is null or empty
     */
    public static DocumentSecurityDirective create(List<String> documentSecurityAttributeNames,
            List<Document> documents) {
        return new DocumentSecurityDirective(documentSecurityAttributeNames, documents);
    }

    /**
     * Returns the list of document security attribute names on this security directive.  Will never return a null or
     * empty list.
     *
     * @return the list of document security attribute names on this security directive
     */
    public List<String> getDocumentSecurityAttributeNames() {
        return documentSecurityAttributeNames;
    }

    /**
     * Returns the list of documents on this security directive.  Will never return null, but may return an empty list.
     *
     * @return the list of documents on this security directive
     */
    public List<Document> getDocuments() {
        return documents;
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "documentSecurityDirective";
        final static String TYPE_NAME = "DocumentSecurityDirectiveType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String DOCUMENT_SECURITY_ATTRIBUTE_NAMES = "documentSecurityAttributeNames";
        final static String DOCUMENT_SECURITY_ATTRIBUTE_NAME = "documentSecurityAttributeName";
        final static String DOCUMENTS = "documents";
        final static String DOCUMENT = "document";
    }

}
