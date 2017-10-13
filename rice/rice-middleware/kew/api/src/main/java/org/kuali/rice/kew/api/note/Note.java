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
package org.kuali.rice.kew.api.note;

import java.io.Serializable;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.util.jaxb.DateTimeAdapter;
import org.w3c.dom.Element;

@XmlRootElement(name = Note.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = Note.Constants.TYPE_NAME, propOrder = {
	    Note.Elements.ID,
		Note.Elements.DOCUMENT_ID,
		Note.Elements.AUTHOR_PRINCIPAL_ID,
		Note.Elements.CREATE_DATE,
		Note.Elements.TEXT,
		Note.Elements.CREATE_DATE_VALUE,
        CoreConstants.CommonElements.VERSION_NUMBER,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class Note extends AbstractDataTransferObject implements NoteContract {

	private static final long serialVersionUID = 6619061362854480922L;

	@XmlElement(name = Elements.ID, required = false)
    private final String id;
	
    @XmlElement(name = Elements.DOCUMENT_ID, required = true)
    private final String documentId;
    
    @XmlElement(name = Elements.AUTHOR_PRINCIPAL_ID, required = true)
    private final String authorPrincipalId;

    @Deprecated
    @XmlElement(name = Elements.CREATE_DATE, required = false)
    private final DateTime createDate;

    @XmlElement(name = Elements.CREATE_DATE_VALUE, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime createDateValue;
    
    @XmlElement(name = Elements.TEXT, required = false)
    private final String text;
    
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private Note() {
        this.id = null;
    	this.documentId = null;
        this.authorPrincipalId = null;
        this.createDate = null;
        this.createDateValue = null;
        this.text = null;
        this.versionNumber = null;
    }

    private Note(Builder builder) {
        this.id = builder.getId();
    	this.documentId = builder.getDocumentId();
        this.authorPrincipalId = builder.getAuthorPrincipalId();
        this.createDate = builder.getCreateDate();
        this.createDateValue = builder.getCreateDate();
        this.text = builder.getText();
        this.versionNumber = builder.getVersionNumber();
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
    public String getAuthorPrincipalId() {
        return this.authorPrincipalId;
    }

    @Override
    public DateTime getCreateDate() {
        return this.createDateValue == null ? this.createDate : this.createDateValue;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    /**
     * A builder which can be used to construct {@link Note} instances.  Enforces the constraints of the {@link NoteContract}.
     */
    public final static class Builder implements Serializable, ModelBuilder, NoteContract {

		private static final long serialVersionUID = 6457130539374835936L;

		private String id;
        private String documentId;
        private String authorPrincipalId;
        private DateTime createDate;
        private String text;
        private Long versionNumber;

        private Builder(String documentId, String authorPrincipalId) {
            setDocumentId(documentId);
            setAuthorPrincipalId(authorPrincipalId);
        }

        public static Builder create(String documentId, String authorPrincipalId) {
            return new Builder(documentId, authorPrincipalId);
        }

        public static Builder create(NoteContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            // TODO if create() is modified to accept required parameters, this will need to be modified
            Builder builder = create(contract.getDocumentId(), contract.getAuthorPrincipalId());
            builder.setId(contract.getId());
            builder.setCreateDate(contract.getCreateDate());
            builder.setText(contract.getText());
            builder.setVersionNumber(contract.getVersionNumber());
            return builder;
        }

        public Note build() {
            return new Note(this);
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
        public String getAuthorPrincipalId() {
            return this.authorPrincipalId;
        }

        @Override
        public DateTime getCreateDate() {
            return this.createDate;
        }

        @Override
        public String getText() {
            return this.text;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setDocumentId(String documentId) {
        	if (StringUtils.isBlank(documentId)) {
        		throw new IllegalArgumentException("documentId was null or blank");
        	}
            this.documentId = documentId;
        }

        public void setAuthorPrincipalId(String authorPrincipalId) {
        	if (StringUtils.isBlank(authorPrincipalId)) {
        		throw new IllegalArgumentException("authorPrincipalId was null or blank");
        	}
            this.authorPrincipalId = authorPrincipalId;
        }

        public void setCreateDate(DateTime createDate) {
            this.createDate = createDate;
        }

        public void setText(String text) {
            this.text = text;
        }
        
        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "note";
        final static String TYPE_NAME = "NoteType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String DOCUMENT_ID = "documentId";
        final static String AUTHOR_PRINCIPAL_ID = "authorPrincipalId";
        final static String CREATE_DATE = "createDate";
        final static String CREATE_DATE_VALUE = "createDateValue";
        final static String TEXT = "text";
        final static String ID = "id";
    }

}

