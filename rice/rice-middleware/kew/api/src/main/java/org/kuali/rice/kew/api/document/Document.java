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

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.util.jaxb.DateTimeAdapter;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement(name = Document.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = Document.Constants.TYPE_NAME, propOrder = {
    Document.Elements.DOCUMENT_ID,
    Document.Elements.STATUS,
    Document.Elements.DATE_CREATED,
    Document.Elements.DATE_LAST_MODIFIED,
    Document.Elements.DATE_APPROVED,
    Document.Elements.DATE_FINALIZED,
    Document.Elements.TITLE,
    Document.Elements.APPLICATION_DOCUMENT_ID,
    Document.Elements.INITIATOR_PRINCIPAL_ID,
    Document.Elements.ROUTED_BY_PRINCIPAL_ID,
    Document.Elements.DOCUMENT_TYPE_NAME,
    Document.Elements.DOCUMENT_TYPE_ID,
    Document.Elements.DOCUMENT_HANDLER_URL,
    Document.Elements.APPLICATION_DOCUMENT_STATUS,
    Document.Elements.APPLICATION_DOCUMENT_STATUS_DATE,
    Document.Elements.VARIABLES,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class Document extends AbstractDataTransferObject implements DocumentContract {

	private static final long serialVersionUID = -6954090887747605047L;

	@XmlElement(name = Elements.DOCUMENT_ID, required = true)
    private final String documentId;

    @XmlElement(name = Elements.STATUS, required = true)
    private final String status;

    @XmlElement(name = Elements.DATE_CREATED, required = true)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime dateCreated;

    @XmlElement(name = Elements.DATE_LAST_MODIFIED, required = true)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime dateLastModified;

    @XmlElement(name = Elements.DATE_APPROVED, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime dateApproved;

    @XmlElement(name = Elements.DATE_FINALIZED, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime dateFinalized;

    @XmlElement(name = Elements.TITLE, required = false)
    private final String title;

    @XmlElement(name = Elements.APPLICATION_DOCUMENT_ID, required = false)
    private final String applicationDocumentId;

    @XmlElement(name = Elements.INITIATOR_PRINCIPAL_ID, required = true)
    private final String initiatorPrincipalId;

    @XmlElement(name = Elements.ROUTED_BY_PRINCIPAL_ID, required = false)
    private final String routedByPrincipalId;

    @XmlElement(name = Elements.DOCUMENT_TYPE_NAME, required = true)
    private final String documentTypeName;

    @XmlElement(name = Elements.DOCUMENT_TYPE_ID, required = true)
    private final String documentTypeId;

    @XmlElement(name = Elements.DOCUMENT_HANDLER_URL, required = false)
    private final String documentHandlerUrl;

    @XmlElement(name = Elements.APPLICATION_DOCUMENT_STATUS, required = false)
    private final String applicationDocumentStatus;

    @XmlElement(name = Elements.APPLICATION_DOCUMENT_STATUS_DATE, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime applicationDocumentStatusDate;

    @XmlElement(name = Elements.VARIABLES, required = false)
    @XmlJavaTypeAdapter(MapStringStringAdapter.class)
    private final Map<String, String> variables;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    private Document() {
        this.documentId = null;
        this.status = null;
        this.dateCreated = null;
        this.dateLastModified = null;
        this.dateApproved = null;
        this.dateFinalized = null;
        this.title = null;
        this.applicationDocumentId = null;
        this.initiatorPrincipalId = null;
        this.routedByPrincipalId = null;
        this.documentTypeName = null;
        this.documentTypeId = null;
        this.documentHandlerUrl = null;
        this.applicationDocumentStatus = null;
        this.applicationDocumentStatusDate = null;
        this.variables = null;
    }

    private Document(Builder builder) {
        this.documentId = builder.getDocumentId();
        this.status = builder.getStatus().getCode();
        this.dateCreated = builder.getDateCreated();
        this.dateLastModified = builder.getDateLastModified();
        this.dateApproved = builder.getDateApproved();
        this.dateFinalized = builder.getDateFinalized();
        this.title = builder.getTitle();
        this.applicationDocumentId = builder.getApplicationDocumentId();
        this.initiatorPrincipalId = builder.getInitiatorPrincipalId();
        this.routedByPrincipalId = builder.getRoutedByPrincipalId();
        this.documentTypeName = builder.getDocumentTypeName();
        this.documentTypeId = builder.getDocumentTypeId();
        this.documentHandlerUrl = builder.getDocumentHandlerUrl();
        this.applicationDocumentStatus = builder.getApplicationDocumentStatus();
        this.applicationDocumentStatusDate = builder.getApplicationDocumentStatusDate();
        this.variables = new HashMap<String, String>(builder.getVariables());
    }

    @Override
    public String getDocumentId() {
        return this.documentId;
    }

    @Override
    public DocumentStatus getStatus() {
    	if (StringUtils.isBlank(this.status)) {
    		throw new IllegalStateException("Status should not be null");
    	}
		return DocumentStatus.fromCode(this.status);
    }

    @Override
    public DateTime getDateCreated() {
        return this.dateCreated;
    }

    @Override
    public DateTime getDateLastModified() {
        return this.dateLastModified;
    }

    @Override
    public DateTime getDateApproved() {
        return this.dateApproved;
    }

    @Override
    public DateTime getDateFinalized() {
        return this.dateFinalized;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getApplicationDocumentId() {
        return this.applicationDocumentId;
    }

    @Override
    public String getInitiatorPrincipalId() {
        return this.initiatorPrincipalId;
    }

    @Override
    public String getRoutedByPrincipalId() {
        return this.routedByPrincipalId;
    }

    @Override
    public String getDocumentTypeName() {
        return this.documentTypeName;
    }

    @Override
    public String getDocumentTypeId() {
        return this.documentTypeId;
    }

    @Override
    public String getDocumentHandlerUrl() {
        return this.documentHandlerUrl;
    }

    @Override
    public String getApplicationDocumentStatus() {
        return this.applicationDocumentStatus;
    }

    @Override
    public DateTime getApplicationDocumentStatusDate() {
        return this.applicationDocumentStatusDate;
    }

    @Override
    public Map<String, String> getVariables() {
        return Collections.unmodifiableMap(this.variables);
    }

    /**
     * A builder which can be used to construct {@link Document} instances.  Enforces the constraints of the {@link DocumentContract}.
     *
     */
    public final static class Builder
        implements Serializable, ModelBuilder, DocumentContract
    {

		private static final long serialVersionUID = -1584497024880308500L;

		private String documentId;
        private DocumentStatus status;
        private DateTime dateCreated;
        private DateTime dateLastModified;
        private DateTime dateApproved;
        private DateTime dateFinalized;
        private String title;
        private String applicationDocumentId;
        private String initiatorPrincipalId;
        private String routedByPrincipalId;
        private String documentTypeName;
        private String documentTypeId;
        private String documentHandlerUrl;
        private String applicationDocumentStatus;
        private DateTime applicationDocumentStatusDate;
        private Map<String, String> variables;

        private Builder(String documentId, DocumentStatus status, DateTime dateCreated, String initiatorPrincipalId, String documentTypeName, String documentTypeId) {
            setDocumentId(documentId);
            setStatus(status);
            setDateCreated(dateCreated);
            setTitle("");
            setInitiatorPrincipalId(initiatorPrincipalId);
            setDocumentTypeName(documentTypeName);
            setDocumentTypeId(documentTypeId);
            setVariables(new HashMap<String, String>());
        }

        public static Builder create(String documentId, DocumentStatus status, DateTime dateCreated, String initiatorPrincipalId, String documentTypeName, String documentTypeId) {
            return new Builder(documentId, status, dateCreated, initiatorPrincipalId, documentTypeName, documentTypeId);
        }

        public static Builder create(String documentId, String initiatorPrinicpalId, String documentTypeName, String documentTypeId) {
        	return new Builder(documentId, DocumentStatus.INITIATED, new DateTime(), initiatorPrinicpalId, documentTypeName, documentTypeId);
        }

        public static Builder create(DocumentContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(
            		contract.getDocumentId(),
            		contract.getStatus(),
            		contract.getDateCreated(),
            		contract.getInitiatorPrincipalId(),
            		contract.getDocumentTypeName(),
            		contract.getDocumentTypeId()
            );
            builder.setDateLastModified(contract.getDateLastModified());
            builder.setDateApproved(contract.getDateApproved());
            builder.setDateFinalized(contract.getDateFinalized());
            builder.setTitle(contract.getTitle());
            builder.setApplicationDocumentId(contract.getApplicationDocumentId());
            builder.setRoutedByPrincipalId(contract.getRoutedByPrincipalId());
            builder.setDocumentHandlerUrl(contract.getDocumentHandlerUrl());
            builder.setApplicationDocumentStatus(contract.getApplicationDocumentStatus());
            builder.setApplicationDocumentStatusDate(contract.getApplicationDocumentStatusDate());
            builder.setVariables(new HashMap<String, String>(contract.getVariables()));
            return builder;
        }

        public Document build() {
            return new Document(this);
        }

        @Override
        public String getDocumentId() {
            return this.documentId;
        }

        @Override
        public DocumentStatus getStatus() {
            return this.status;
        }

        @Override
        public DateTime getDateCreated() {
            return this.dateCreated;
        }

        @Override
        public DateTime getDateLastModified() {
            return this.dateLastModified;
        }

        @Override
        public DateTime getDateApproved() {
            return this.dateApproved;
        }

        @Override
        public DateTime getDateFinalized() {
            return this.dateFinalized;
        }

        @Override
        public String getTitle() {
            return this.title;
        }

        @Override
        public String getApplicationDocumentId() {
            return this.applicationDocumentId;
        }

        @Override
        public String getInitiatorPrincipalId() {
            return this.initiatorPrincipalId;
        }

        @Override
        public String getRoutedByPrincipalId() {
            return this.routedByPrincipalId;
        }

        @Override
        public String getDocumentTypeName() {
            return this.documentTypeName;
        }

        @Override
        public String getDocumentTypeId() {
            return this.documentTypeId;
        }

        @Override
        public String getDocumentHandlerUrl() {
            return this.documentHandlerUrl;
        }

        @Override
        public String getApplicationDocumentStatus() {
            return this.applicationDocumentStatus;
        }

        @Override
        public DateTime getApplicationDocumentStatusDate() {
            return this.applicationDocumentStatusDate;
        }

        @Override
        public Map<String, String> getVariables() {
            return this.variables;
        }

        public void setDocumentId(String documentId) {
            if (StringUtils.isBlank(documentId)) {
            	throw new IllegalArgumentException("documentId was null or blank");
            }
            this.documentId = documentId;
        }

        public void setStatus(DocumentStatus status) {
            if (status == null) {
            	throw new IllegalArgumentException("status was null");
            }
            this.status = status;
        }

        public void setDateCreated(DateTime dateCreated) {
            if (dateCreated == null) {
            	throw new IllegalArgumentException("dateCreated was null");
            }
            this.dateCreated = dateCreated;
        }

        public void setDateLastModified(DateTime dateLastModified) {
            this.dateLastModified = dateLastModified;
        }

        public void setDateApproved(DateTime dateApproved) {
            this.dateApproved = dateApproved;
        }

        public void setDateFinalized(DateTime dateFinalized) {
            this.dateFinalized = dateFinalized;
        }

        public void setTitle(String title) {
        	if (title == null) {
        		title = "";
        	}
            this.title = title;
        }

        public void setApplicationDocumentId(String applicationDocumentId) {
            this.applicationDocumentId = applicationDocumentId;
        }

        public void setInitiatorPrincipalId(String initiatorPrincipalId) {
            if (StringUtils.isBlank(initiatorPrincipalId)) {
            	throw new IllegalArgumentException("initiatorPrincipalId was null or blank");
            }
            this.initiatorPrincipalId = initiatorPrincipalId;
        }

        public void setRoutedByPrincipalId(String routedByPrincipalId) {
            this.routedByPrincipalId = routedByPrincipalId;
        }

        public void setDocumentTypeName(String documentTypeName) {
        	if (StringUtils.isBlank(documentTypeName)) {
            	throw new IllegalArgumentException("documentTypeName was null or blank");
            }
            this.documentTypeName = documentTypeName;
        }

        public void setDocumentTypeId(String documentTypeId) {
        	if (StringUtils.isBlank(documentTypeId)) {
            	throw new IllegalArgumentException("documentTypeId was null or blank");
            }
            this.documentTypeId = documentTypeId;
        }

        public void setDocumentHandlerUrl(String documentHandlerUrl) {
            this.documentHandlerUrl = documentHandlerUrl;
        }

        public void setApplicationDocumentStatus(String applicationDocumentStatus) {
            this.applicationDocumentStatus = applicationDocumentStatus;
        }

        public void setApplicationDocumentStatusDate(DateTime applicationDocumentStatusDate) {
            this.applicationDocumentStatusDate = applicationDocumentStatusDate;
        }

        public void setVariables(Map<String, String> variables) {
        	if (variables == null) {
        		variables = new HashMap<String, String>();
        	}
            this.variables = variables;
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "document";
        final static String TYPE_NAME = "DocumentType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String DOCUMENT_ID = "documentId";
        final static String STATUS = "status";
        final static String DATE_CREATED = "dateCreated";
        final static String DATE_LAST_MODIFIED = "dateLastModified";
        final static String DATE_APPROVED = "dateApproved";
        final static String DATE_FINALIZED = "dateFinalized";
        final static String TITLE = "title";
        final static String APPLICATION_DOCUMENT_ID = "applicationDocumentId";
        final static String INITIATOR_PRINCIPAL_ID = "initiatorPrincipalId";
        final static String ROUTED_BY_PRINCIPAL_ID = "routedByPrincipalId";
        final static String DOCUMENT_TYPE_NAME = "documentTypeName";
        final static String DOCUMENT_TYPE_ID = "documentTypeId";
        final static String DOCUMENT_HANDLER_URL = "documentHandlerUrl";
        final static String APPLICATION_DOCUMENT_STATUS = "applicationDocumentStatus";
        final static String APPLICATION_DOCUMENT_STATUS_DATE = "applicationDocumentStatusDate";
        final static String VARIABLES = "variables";
    }

}

