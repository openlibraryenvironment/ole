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
package org.kuali.rice.kew.framework.document.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.mo.ModelObjectUtils;
import org.kuali.rice.kew.api.document.attribute.DocumentAttribute;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeContract;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeFactory;
import org.w3c.dom.Element;

/**
 * An immutable data transfer object implementation of the {@link DocumentSearchResultValueContract}.
 * Instances of this class should be constructed using the nested {@link Builder} class.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = DocumentSearchResultValue.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DocumentSearchResultValue.Constants.TYPE_NAME, propOrder = {
    DocumentSearchResultValue.Elements.DOCUMENT_ID,
    DocumentSearchResultValue.Elements.DOCUMENT_ATTRIBUTES,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class DocumentSearchResultValue extends AbstractDataTransferObject
        implements DocumentSearchResultValueContract {

    @XmlElement(name = Elements.DOCUMENT_ID, required = true)
    private final String documentId;

    @XmlElementWrapper(name = Elements.DOCUMENT_ATTRIBUTES, required = false)
    @XmlElement(name = Elements.DOCUMENT_ATTRIBUTE, required = false)
    private final List<DocumentAttribute> documentAttributes;
    
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    @SuppressWarnings("unused")
    private DocumentSearchResultValue() {
        this.documentId = null;
        this.documentAttributes = null;
    }

    private DocumentSearchResultValue(Builder builder) {
        this.documentId = builder.getDocumentId();
        this.documentAttributes = ModelObjectUtils.buildImmutableCopy(builder.getDocumentAttributes());
    }

    @Override
    public String getDocumentId() {
        return this.documentId;
    }

    @Override
    public List<DocumentAttribute> getDocumentAttributes() {
        return this.documentAttributes;
    }

    /**
     * A builder which can be used to construct {@link DocumentSearchResultValue} instances.  Enforces the constraints
     * of the {@link DocumentSearchResultValueContract}.
     */
    public final static class Builder implements Serializable, ModelBuilder, DocumentSearchResultValueContract {

        private String documentId;
        private List<DocumentAttribute.AbstractBuilder<?>> documentAttributes;

        private Builder(String documentId) {
            setDocumentId(documentId);
            setDocumentAttributes(new ArrayList<DocumentAttribute.AbstractBuilder<?>>());
        }

        /**
         * Creates a new builder instance initialized with the given document id.  The list of document attributes on
         * this builder is initialized to an empty list.
         *
         * @param documentId the id of the document with which to initialize this builder, must not be a null or blank
         * value
         *
         * @return a new builder instance initialized with the given document id
         */
        public static Builder create(String documentId) {
            return new Builder(documentId);
        }

        /**
         * Creates a new builder instance initialized with copies of the properties from the given contract.
         *
         * @param contract the contract from which to copy properties
         *
         * @return a builder instance initialized with properties from the given contract
         *
         * @throws IllegalArgumentException if the given contract is null
         */
        public static Builder create(DocumentSearchResultValueContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(contract.getDocumentId());
            if (contract.getDocumentAttributes() != null) {
                for (DocumentAttributeContract documentAttribute : contract.getDocumentAttributes()) {
                    builder.getDocumentAttributes().add(DocumentAttributeFactory.loadContractIntoBuilder(documentAttribute));
                }
            }
            return builder;
        }

        @Override
        public DocumentSearchResultValue build() {
            return new DocumentSearchResultValue(this);
        }

        @Override
        public String getDocumentId() {
            return this.documentId;
        }

        @Override
        public List<DocumentAttribute.AbstractBuilder<?>> getDocumentAttributes() {
            return this.documentAttributes;
        }

        /**
         * Sets the document id on this builder to the given value.  The given document id must not be a null or blank
         * value.
         *
         * @param documentId the id of the document to set on this builder, must not be a null or blank value
         *
         * @throws IllegalArgumentException if documentId is a null or blank value
         */
        public void setDocumentId(String documentId) {
            if (StringUtils.isBlank(documentId)) {
                throw new IllegalArgumentException("documentId was null or blank");
            }
            this.documentId = documentId;
        }

        /**
         * Sets the list of document attribute builders on this builder to the given list.
         *
         * @param documentAttributes the list of document attribute builders to set on this builder
         */
        public void setDocumentAttributes(List<DocumentAttribute.AbstractBuilder<?>> documentAttributes) {
            this.documentAttributes = documentAttributes;
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "documentSearchResultValue";
        final static String TYPE_NAME = "DocumentSearchResultValueType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled
     * to XML.
     */
    static class Elements {
        final static String DOCUMENT_ID = "documentId";
        final static String DOCUMENT_ATTRIBUTES = "documentAttributes";
        final static String DOCUMENT_ATTRIBUTE = "documentAttribute";
    }

}
