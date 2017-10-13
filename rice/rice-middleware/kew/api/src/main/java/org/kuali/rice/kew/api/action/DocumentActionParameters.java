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
package org.kuali.rice.kew.api.action;

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
import org.kuali.rice.kew.api.document.DocumentContentUpdate;
import org.kuali.rice.kew.api.document.DocumentUpdate;
import org.w3c.dom.Element;

@XmlRootElement(name = DocumentActionParameters.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DocumentActionParameters.Constants.TYPE_NAME, propOrder = {
        DocumentActionParameters.Elements.DOCUMENT_ID,
        DocumentActionParameters.Elements.PRINCIPAL_ID,
        DocumentActionParameters.Elements.ANNOTATION,
        DocumentActionParameters.Elements.DOCUMENT_UPDATE,
        DocumentActionParameters.Elements.DOCUMENT_CONTENT_UPDATE,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class DocumentActionParameters extends AbstractDataTransferObject {

    private static final long serialVersionUID = -7589214734683758734L;

    @XmlElement(name = Elements.DOCUMENT_ID, required = true)
    private final String documentId;

    @XmlElement(name = Elements.PRINCIPAL_ID, required = true)
    private final String principalId;

    @XmlElement(name = Elements.ANNOTATION, required = false)
    private final String annotation;

    @XmlElement(name = Elements.DOCUMENT_UPDATE, required = false)
    private final DocumentUpdate documentUpdate;

    @XmlElement(name = Elements.DOCUMENT_CONTENT_UPDATE, required = false)
    private final DocumentContentUpdate documentContentUpdate;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    private DocumentActionParameters() {
        this.documentId = null;
        this.principalId = null;
        this.annotation = null;
        this.documentUpdate = null;
        this.documentContentUpdate = null;
    }

    private DocumentActionParameters(Builder builder) {
        this.documentId = builder.getDocumentId();
        this.principalId = builder.getPrincipalId();
        this.annotation = builder.getAnnotation();
        this.documentUpdate = builder.getDocumentUpdate();
        this.documentContentUpdate = builder.getDocumentContentUpdate();
    }

    public static DocumentActionParameters create(String documentId, String principalId) {
        return create(documentId, principalId, "");
    }

    public static DocumentActionParameters create(String documentId, String principalId, String annotation) {
        Builder builder = Builder.create(documentId, principalId);
        builder.setAnnotation(annotation);
        return builder.build();
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getPrincipalId() {
        return principalId;
    }

    public String getAnnotation() {
        return annotation;
    }

    public DocumentUpdate getDocumentUpdate() {
        return documentUpdate;
    }

    public DocumentContentUpdate getDocumentContentUpdate() {
        return documentContentUpdate;
    }

    /**
     * A builder which can be used to construct {@link DocumentActionParameters} instances.
     * 
     */
    public final static class Builder implements Serializable, ModelBuilder {

        private static final long serialVersionUID = -9209748637365086000L;

        private String documentId;
        private String principalId;
        private String annotation;
        private DocumentUpdate documentUpdate;
        private DocumentContentUpdate documentContentUpdate;

        private Builder(String documentId, String principalId) {
            setDocumentId(documentId);
            setPrincipalId(principalId);
        }

        public static Builder create(String documentId, String principalId) {
            return new Builder(documentId, principalId);
        }

        public DocumentActionParameters build() {
            return new DocumentActionParameters(this);
        }

        public String getDocumentId() {
            return documentId;
        }

        public void setDocumentId(String documentId) {
            if (StringUtils.isBlank(documentId)) {
                throw new IllegalArgumentException("documentId was null or blank");
            }
            this.documentId = documentId;
        }

        public String getPrincipalId() {
            return principalId;
        }

        public void setPrincipalId(String principalId) {
            if (StringUtils.isBlank(principalId)) {
                throw new IllegalArgumentException("principalId was null or blank");
            }
            this.principalId = principalId;
        }

        public String getAnnotation() {
            return annotation;
        }

        public void setAnnotation(String annotation) {
            this.annotation = annotation;
        }

        public DocumentUpdate getDocumentUpdate() {
            return documentUpdate;
        }

        public void setDocumentUpdate(DocumentUpdate documentUpdate) {
            this.documentUpdate = documentUpdate;
        }

        public DocumentContentUpdate getDocumentContentUpdate() {
            return documentContentUpdate;
        }

        public void setDocumentContentUpdate(DocumentContentUpdate documentContentUpdate) {
            this.documentContentUpdate = documentContentUpdate;
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "documentActionParameters";
        final static String TYPE_NAME = "DocumentActionParametersType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String DOCUMENT_ID = "documentId";
        final static String PRINCIPAL_ID = "principalId";
        final static String ANNOTATION = "annotation";
        final static String DOCUMENT_UPDATE = "documentUpdate";
        final static String DOCUMENT_CONTENT_UPDATE = "documentContentUpdate";
    }

}
