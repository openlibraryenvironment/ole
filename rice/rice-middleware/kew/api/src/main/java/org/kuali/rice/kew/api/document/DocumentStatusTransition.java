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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.util.jaxb.DateTimeAdapter;
import org.w3c.dom.Element;

@XmlRootElement(name = DocumentStatusTransition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DocumentStatusTransition.Constants.TYPE_NAME, propOrder = {
    DocumentStatusTransition.Elements.ID,
    DocumentStatusTransition.Elements.DOCUMENT_ID,
    DocumentStatusTransition.Elements.OLD_APP_DOC_STATUS,
    DocumentStatusTransition.Elements.NEW_APP_DOC_STATUS,
    DocumentStatusTransition.Elements.STATUS_TRANSITION_DATE,
        DocumentStatusTransition.Elements.STATUS_TRANSITION_DATE_VALUE,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class DocumentStatusTransition
    extends AbstractDataTransferObject
    implements DocumentStatusTransitionContract
{

    @XmlElement(name = Elements.ID, required = false)
    private final String id;
    @XmlElement(name = Elements.DOCUMENT_ID, required = false)
    private final String documentId;
    @XmlElement(name = Elements.OLD_APP_DOC_STATUS, required = false)
    private final String oldStatus;
    @XmlElement(name = Elements.NEW_APP_DOC_STATUS, required = false)
    private final String newStatus;
    @Deprecated
    @XmlElement(name = Elements.STATUS_TRANSITION_DATE, required = false)
    private final DateTime statusTransitionDate;
    @XmlElement(name = Elements.STATUS_TRANSITION_DATE_VALUE, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime statusTransitionDateValue;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private DocumentStatusTransition() {
        this.id = null;
        this.documentId = null;
        this.oldStatus = null;
        this.newStatus = null;
        this.statusTransitionDate = null;
        this.statusTransitionDateValue = null;

    }

    private DocumentStatusTransition(Builder builder) {
        this.id = builder.getId();
        this.documentId = builder.getDocumentId();
        this.oldStatus = builder.getOldStatus();
        this.newStatus = builder.getNewStatus();
        this.statusTransitionDate = builder.getStatusTransitionDate();
        this.statusTransitionDateValue = builder.getStatusTransitionDate();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getDocumentId() {
        return this.documentId;
    }

    @Override
    public String getOldStatus() {
        return this.oldStatus;
    }

    @Override
    public String getNewStatus() {
        return this.newStatus;
    }

    @Override
    public DateTime getStatusTransitionDate() {
        return this.statusTransitionDateValue == null ? this.statusTransitionDate : this.statusTransitionDateValue;
    }


    /**
     * A builder which can be used to construct {@link DocumentStatusTransition} instances.  Enforces the constraints of the {@link DocumentStatusTransitionContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, DocumentStatusTransitionContract
    {

        private String id;
        private String documentId;
        private String oldStatus;
        private String newStatus;
        private DateTime statusTransitionDate;

        private Builder(String documentId, String oldStatus, String newStatus) {
            setDocumentId(documentId);
            setOldStatus(oldStatus);
            setNewStatus(newStatus);
        }

        public static Builder create(String documentId, String oldStatus, String newStatus) {
            return new Builder(documentId, oldStatus, newStatus);
        }

        public static Builder create(DocumentStatusTransitionContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(contract.getDocumentId(), contract.getOldStatus(), contract.getNewStatus());
            builder.setId(contract.getId());
            builder.setStatusTransitionDate(contract.getStatusTransitionDate());
            return builder;
        }

        public DocumentStatusTransition build() {
            return new DocumentStatusTransition(this);
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public String getDocumentId() {
            return this.documentId;
        }

        @Override
        public String getOldStatus() {
            return this.oldStatus;
        }

        @Override
        public String getNewStatus() {
            return this.newStatus;
        }

        @Override
        public DateTime getStatusTransitionDate() {
            return this.statusTransitionDate;
        }

        public void setId(String id) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.id = id;
        }

        public void setDocumentId(String documentId) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.documentId = documentId;
        }

        public void setOldStatus(String oldStatus) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.oldStatus = oldStatus;
        }

        public void setNewStatus(String newStatus) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.newStatus = newStatus;
        }

        public void setStatusTransitionDate(DateTime statusTransitionDate) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.statusTransitionDate = statusTransitionDate;
        }

    }


    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "documentStatusTransition";
        final static String TYPE_NAME = "DocumentStatusTransitionType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String ID = "id";
        final static String DOCUMENT_ID = "documentId";
        final static String OLD_APP_DOC_STATUS = "oldStatus";
        final static String NEW_APP_DOC_STATUS = "newStatus";
        final static String STATUS_TRANSITION_DATE = "statusTransitionDate";
        final static String STATUS_TRANSITION_DATE_VALUE = "statusTransitionDateValue";

    }

}