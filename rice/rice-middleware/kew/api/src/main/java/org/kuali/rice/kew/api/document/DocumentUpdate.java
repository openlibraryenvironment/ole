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
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.kew.api.KewApiConstants;
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

/**
 * Defines an update to document meta-data on a particular workflow document,
 * including title, application document id, application document status,
 * and routing branch variables.  This structure is used to convey changes to
 * the document meta-data on document actions, and to retrieve their state
 * afterwards.
 * Document titles will be truncated to to {@link KewApiConstants#TITLE_MAX_LENGTH} length.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @see org.kuali.rice.kew.api.WorkflowDocumentFactory
 * @see WorkflowDocumentImpl
 * @see WorkflowDocumentImpl.ModifiableDocument
 * @see WorkflowDocumentActionsServiceImpl
 */
@XmlRootElement(name = DocumentUpdate.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DocumentUpdate.Constants.TYPE_NAME, propOrder = {
    DocumentUpdate.Elements.TITLE,
    DocumentUpdate.Elements.APPLICATION_DOCUMENT_ID,
    DocumentUpdate.Elements.APPLICATION_DOCUMENT_STATUS,
    DocumentUpdate.Elements.VARIABLES,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class DocumentUpdate extends AbstractDataTransferObject {

    private static final long serialVersionUID = 608839901744771499L;

    @XmlElement(name = Elements.TITLE, required = false)
    private final String title;

    @XmlElement(name = Elements.APPLICATION_DOCUMENT_ID, required = false)
    private final String applicationDocumentId;

    @XmlElement(name = Elements.APPLICATION_DOCUMENT_STATUS, required = false)
    private final String applicationDocumentStatus;

    @XmlElement(name = Elements.VARIABLES, required = false)
    @XmlJavaTypeAdapter(MapStringStringAdapter.class)
    private final Map<String, String> variables;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    private DocumentUpdate() {
        this.title = null;
        this.applicationDocumentId = null;
        this.applicationDocumentStatus = null;
        this.variables = null;
    }

    private DocumentUpdate(Builder builder) {
        this.title = builder.getTitle();
        this.applicationDocumentId = builder.getApplicationDocumentId();
        this.applicationDocumentStatus = builder.getApplicationDocumentStatus();
        this.variables = builder.getVariables();
    }

    public String getTitle() {
        return title;
    }

    public String getApplicationDocumentId() {
        return applicationDocumentId;
    }

    public String getApplicationDocumentStatus() {
        return applicationDocumentStatus;
    }

    public Map<String, String> getVariables() {
        if (variables == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(variables);
    }

    /**
     * A builder which can be used to construct {@link DocumentUpdate} instances.
     */
    public final static class Builder implements Serializable, ModelBuilder {

        private static final long serialVersionUID = 2220000561051177421L;

        private String title;
        private String applicationDocumentId;
        private String applicationDocumentStatus;
        private Map<String, String> variables;


        private Builder() {
            this.title = "";
            this.variables = new HashMap<String, String>();
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(Document document) {
            if (document == null) {
                throw new IllegalArgumentException("document was null");
            }
            Builder builder = create();
            builder.setTitle(document.getTitle());
            builder.setApplicationDocumentId(document.getApplicationDocumentId());
            builder.setApplicationDocumentStatus(document.getApplicationDocumentStatus());
            builder.setVariables(document.getVariables());
            return builder;
        }

        public DocumentUpdate build() {
            return new DocumentUpdate(this);
        }

        public String getTitle() {
            return title;
        }

        /**
         * Sets the document title - will be truncated to {@link KewApiConstants#TITLE_MAX_LENGTH} length.
         */
        public void setTitle(String title) {
            if (title == null) {
                title = "";
            }
            if (title.length() > KewApiConstants.TITLE_MAX_LENGTH) {
                title = title.substring(0, KewApiConstants.TITLE_MAX_LENGTH);
            }
            this.title = title;
        }

        public String getApplicationDocumentId() {
            return applicationDocumentId;
        }

        public void setApplicationDocumentId(String applicationDocumentId) {
            this.applicationDocumentId = applicationDocumentId;
        }

        public String getApplicationDocumentStatus() {
            return applicationDocumentStatus;
        }

        public void setApplicationDocumentStatus(String applicationDocumentStatus) {
            this.applicationDocumentStatus = applicationDocumentStatus;
        }

        public void setVariables(Map<String, String> variables) {
            if (variables == null) {
                this.variables = new HashMap<String, String>();
            } else {
                this.variables = new HashMap<String, String>(variables);
            }
        }

        public Map<String, String> getVariables() {
            return variables;
        }

        public String getVariableValue(String name) {
            return variables.get(name);
        }

        public void setVariable(String name, String value) {
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("name was null or blank");
            }
            variables.put(name, value);
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "documentUpdate";
        final static String TYPE_NAME = "DocumentUpdateType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String TITLE = "title";
        final static String APPLICATION_DOCUMENT_ID = "applicationDocumentId";
        final static String APPLICATION_DOCUMENT_STATUS = "applicationDocumentStatus";
        final static String VARIABLES = "variables";
    }
}
