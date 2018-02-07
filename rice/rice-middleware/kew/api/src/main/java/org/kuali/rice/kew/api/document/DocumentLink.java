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
import org.w3c.dom.Element;

@XmlRootElement(name = DocumentLink.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DocumentLink.Constants.TYPE_NAME, propOrder = {
	    DocumentLink.Elements.ID,
		DocumentLink.Elements.ORIGINATING_DOCUMENT_ID,
		DocumentLink.Elements.DESTINATION_DOCUMENT_ID,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class DocumentLink extends AbstractDataTransferObject implements DocumentLinkContract {

	private static final long serialVersionUID = -1193048221115914280L;

	@XmlElement(name = Elements.ID, required = false)
    private final String id;

    @XmlElement(name = Elements.ORIGINATING_DOCUMENT_ID, required = true)
    private final String originatingDocumentId;
    
    @XmlElement(name = Elements.DESTINATION_DOCUMENT_ID, required = true)
    private final String destinationDocumentId;
        
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    private DocumentLink() {
        this.id = null;
    	this.originatingDocumentId = null;
        this.destinationDocumentId = null;
    }

    private DocumentLink(Builder builder) {
        this.id = builder.getId();
    	this.originatingDocumentId = builder.getOriginatingDocumentId();
        this.destinationDocumentId = builder.getDestinationDocumentId();
    }

    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public String getOriginatingDocumentId() {
        return this.originatingDocumentId;
    }

    @Override
    public String getDestinationDocumentId() {
        return this.destinationDocumentId;
    }

    /**
     * A builder which can be used to construct {@link DocumentLink} instances.  Enforces the constraints of the {@link DocumentLinkContract}.
     */
    public final static class Builder implements Serializable, ModelBuilder, DocumentLinkContract {

		private static final long serialVersionUID = -6713990840543140054L;

		private String id;
        private String originatingDocumentId;
        private String destinationDocumentId;

        private Builder(String originatingDocumentId, String destinationDocumentId) {
            setOriginatingDocumentId(originatingDocumentId);
            setDestinationDocumentId(destinationDocumentId);
            if (getOriginatingDocumentId().equals(getDestinationDocumentId())) {
            	throw new IllegalArgumentException("originating and destination document ids were the same, cannot link a document with itself");
            }
        }

        public static Builder create(String originatingDocumentId, String destinationDocumentId) {
            return new Builder(originatingDocumentId, destinationDocumentId);
        }

        public static Builder create(DocumentLinkContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(contract.getOriginatingDocumentId(), contract.getDestinationDocumentId());
            builder.setId(contract.getId());
            return builder;
        }

        public DocumentLink build() {
            return new DocumentLink(this);
        }

        @Override
        public String getId() {
            return this.id;
        }
        
        @Override
        public String getOriginatingDocumentId() {
            return this.originatingDocumentId;
        }

        @Override
        public String getDestinationDocumentId() {
            return this.destinationDocumentId;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setOriginatingDocumentId(String originatingDocumentId) {
            if (StringUtils.isBlank(originatingDocumentId)) {
            	throw new IllegalArgumentException("originatingDocumentId was null or blank");
            }
            this.originatingDocumentId = originatingDocumentId;
        }

        public void setDestinationDocumentId(String destinationDocumentId) {
        	if (StringUtils.isBlank(destinationDocumentId)) {
            	throw new IllegalArgumentException("destinationDocumentId was null or blank");
            }
            this.destinationDocumentId = destinationDocumentId;
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "documentLink";
        final static String TYPE_NAME = "DocumentLinkType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String ID = "id";
        final static String ORIGINATING_DOCUMENT_ID = "originatingDocumentId";
        final static String DESTINATION_DOCUMENT_ID = "destinationDocumentId";
    }

}
