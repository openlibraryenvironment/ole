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

import java.io.Serializable;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kew.api.KewApiConstants;
import org.w3c.dom.Element;

@XmlRootElement(name = DocumentContent.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DocumentContent.Constants.TYPE_NAME, propOrder = {
    DocumentContent.Elements.DOCUMENT_ID,
    DocumentContent.Elements.APPLICATION_CONTENT,
    DocumentContent.Elements.ATTRIBUTE_CONTENT,
    DocumentContent.Elements.SEARCHABLE_CONTENT,
    DocumentContent.Elements.FORMAT_VERSION,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class DocumentContent extends AbstractDataTransferObject implements DocumentContentContract {

	private static final long serialVersionUID = 6110079520547685342L;

	@XmlElement(name = Elements.DOCUMENT_ID, required = true)
    private final String documentId;
    
	@XmlElement(name = Elements.APPLICATION_CONTENT, required = false)
    private final String applicationContent;
    
	@XmlElement(name = Elements.ATTRIBUTE_CONTENT, required = false)
    private final String attributeContent;
    
	@XmlElement(name = Elements.SEARCHABLE_CONTENT, required = false)
    private final String searchableContent;
    
	@XmlElement(name = Elements.FORMAT_VERSION, required = true)
    private final int formatVersion;
    
	@SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private DocumentContent() {
        this.documentId = null;
        this.applicationContent = null;
        this.attributeContent = null;
        this.searchableContent = null;
        this.formatVersion = 0;
    }

    private DocumentContent(Builder builder) {
        this.documentId = builder.getDocumentId();
        this.applicationContent = builder.getApplicationContent();
        this.attributeContent = builder.getAttributeContent();
        this.searchableContent = builder.getSearchableContent();
        this.formatVersion = builder.getFormatVersion();
    }

    @Override
    public String getDocumentId() {
        return this.documentId;
    }

    @Override
    public String getApplicationContent() {
        return this.applicationContent;
    }

    @Override
    public String getAttributeContent() {
        return this.attributeContent;
    }

    @Override
    public String getSearchableContent() {
        return this.searchableContent;
    }

    @Override
    public int getFormatVersion() {
        return this.formatVersion;
    }
    
    public String getFullContent() {
        StringBuilder fullContent = new StringBuilder();
        fullContent.append("<").append(KewApiConstants.DOCUMENT_CONTENT_ELEMENT).append(">");
        if (!StringUtils.isBlank(getApplicationContent())) {
            fullContent.append("<").append(KewApiConstants.APPLICATION_CONTENT_ELEMENT).append(">");
            fullContent.append(getApplicationContent());
            fullContent.append("</").append(KewApiConstants.APPLICATION_CONTENT_ELEMENT).append(">");        	
        }
        fullContent.append(getAttributeContent());
        fullContent.append(getSearchableContent());
        fullContent.append("</").append(KewApiConstants.DOCUMENT_CONTENT_ELEMENT).append(">");
        return fullContent.toString();
    }

    /**
     * A builder which can be used to construct {@link DocumentContent} instances.  Enforces the constraints of the {@link DocumentContentContract}.
     */
    public final static class Builder implements Serializable, ModelBuilder, DocumentContentContract {

		private static final long serialVersionUID = 7549637048594326790L;

		private String documentId;
        private String applicationContent;
        private String attributeContent;
        private String searchableContent;
        private int formatVersion;

        private Builder(String documentId) {
            setDocumentId(documentId);
            setFormatVersion(KewApiConstants.DocumentContentVersions.CURRENT);
        }

        public static Builder create(String documentId) {
            return new Builder(documentId);
        }

        public static Builder create(DocumentContentContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(contract.getDocumentId());
            builder.setApplicationContent(contract.getApplicationContent());
            builder.setAttributeContent(contract.getAttributeContent());
            builder.setSearchableContent(contract.getSearchableContent());
            builder.setFormatVersion(contract.getFormatVersion());
            return builder;
        }

        public DocumentContent build() {
            return new DocumentContent(this);
        }

        @Override
        public String getDocumentId() {
            return this.documentId;
        }

        @Override
        public String getApplicationContent() {
            return this.applicationContent;
        }

        @Override
        public String getAttributeContent() {
            return this.attributeContent;
        }

        @Override
        public String getSearchableContent() {
            return this.searchableContent;
        }

        @Override
        public int getFormatVersion() {
            return this.formatVersion;
        }

        public void setDocumentId(String documentId) {
            if (StringUtils.isBlank(documentId)) {
            	throw new IllegalArgumentException("documentId was null or blank");
            }
            this.documentId = documentId;
        }

        public void setApplicationContent(String applicationContent) {
            this.applicationContent = applicationContent;
        }

        public void setAttributeContent(String attributeContent) {
            this.attributeContent = attributeContent;
        }

        public void setSearchableContent(String searchableContent) {
            this.searchableContent = searchableContent;
        }

        public void setFormatVersion(int formatVersion) {
        	if (formatVersion < 0) {
        		throw new IllegalArgumentException("formatVersion must be a valid version, was " + formatVersion);
        	}
            this.formatVersion = formatVersion;
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "documentContent";
        final static String TYPE_NAME = "DocumentContentType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {

        final static String DOCUMENT_ID = "documentId";
        final static String APPLICATION_CONTENT = "applicationContent";
        final static String ATTRIBUTE_CONTENT = "attributeContent";
        final static String SEARCHABLE_CONTENT = "searchableContent";
        final static String FORMAT_VERSION = "formatVersion";

    }

}

