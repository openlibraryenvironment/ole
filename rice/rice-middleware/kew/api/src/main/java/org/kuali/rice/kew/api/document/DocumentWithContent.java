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
package org.kuali.rice.kew.api.document;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;

/**
 * Immutable implementation of the {@code DocumentWithContentContract}.  This class does not have a builder since it is
 * intended to simply be a wrapper a document and it's content.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = DocumentWithContent.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DocumentWithContent.Constants.TYPE_NAME, propOrder = {
    DocumentWithContent.Elements.DOCUMENT,
    DocumentWithContent.Elements.DOCUMENT_CONTENT,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class DocumentWithContent extends AbstractDataTransferObject implements DocumentWithContentContract {

    @XmlElement(name = Elements.DOCUMENT, required = true)
    private final Document document;

    @XmlElement(name = Elements.DOCUMENT_CONTENT, required = true)
    private final DocumentContent documentContent;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    private DocumentWithContent() {
        this.document = null;
        this.documentContent = null;
    }

    private DocumentWithContent(Document document, DocumentContent documentContent) {
        if (document == null) {
            throw new IllegalArgumentException("document was null");
        }
        if (documentContent == null) {
            throw new IllegalArgumentException("documentContent was null");
        }
        this.document = document;
        this.documentContent = documentContent;
    }

    public static DocumentWithContent create(Document document, DocumentContent documentContent) {
        return new DocumentWithContent(document, documentContent);
    }

    @Override
    public Document getDocument() {
        return this.document;
    }

    @Override
    public DocumentContent getDocumentContent() {
        return this.documentContent;
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "documentWithContent";
        final static String TYPE_NAME = "DocumentWithContentType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String DOCUMENT = "document";
        final static String DOCUMENT_CONTENT = "documentContent";
    }

}
