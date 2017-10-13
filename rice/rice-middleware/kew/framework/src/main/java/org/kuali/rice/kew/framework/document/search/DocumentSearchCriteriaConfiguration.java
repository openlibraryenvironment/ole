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

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * An immutable data transfer object implementation of the {@link DocumentSearchCriteriaConfigurationContract}.
 * Instances of this class should be constructed using the nested {@link Builder} class.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = DocumentSearchCriteriaConfiguration.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DocumentSearchCriteriaConfiguration.Constants.TYPE_NAME, propOrder = {
    DocumentSearchCriteriaConfiguration.Elements.SEARCH_ATTRIBUTE_FIELDS,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class DocumentSearchCriteriaConfiguration extends AbstractDataTransferObject implements DocumentSearchCriteriaConfigurationContract {

    private static final long serialVersionUID = -5764134034667636217L;

    @XmlElementWrapper(name = Elements.SEARCH_ATTRIBUTE_FIELDS, required = false)
    @XmlElement(name = Elements.ATTRIBUTE_FIELDS, required = false)
    private final List<AttributeFields> searchAttributeFields;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    @SuppressWarnings("unused")
    private DocumentSearchCriteriaConfiguration() {
        this.searchAttributeFields = null;
    }

    private DocumentSearchCriteriaConfiguration(Builder builder) {
        if (builder.getSearchAttributeFields() == null) {
            this.searchAttributeFields = Collections.emptyList();
        } else {
            this.searchAttributeFields = Collections.unmodifiableList(new ArrayList<AttributeFields>(builder.getSearchAttributeFields()));
        }
    }

    @Override
    public List<AttributeFields> getSearchAttributeFields() {
        return this.searchAttributeFields;
    }

    public List<RemotableAttributeField> getFlattenedSearchAttributeFields() {
        List<RemotableAttributeField> searchAttributeFields = new ArrayList<RemotableAttributeField>();
        for (AttributeFields attributeFields : getSearchAttributeFields()) {
            searchAttributeFields.addAll(attributeFields.getRemotableAttributeFields());
        }
        return searchAttributeFields;
    }

    /**
     * A builder which can be used to construct {@link DocumentSearchCriteriaConfiguration} instances.  Enforces the
     * constraints of the {@link DocumentSearchCriteriaConfigurationContract}.
     */
    public final static class Builder implements Serializable, ModelBuilder, DocumentSearchCriteriaConfigurationContract {

        private List<AttributeFields> searchAttributeFields;

        private Builder() {
            setSearchAttributeFields(new ArrayList<AttributeFields>());
        }

        /**
         * Creates new empty builder instance.  The list of search attributes on this builder is intialized to an empty
         * list.
         *
         * @return a new empty builder instance
         */
        public static Builder create() {
            return new Builder();
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
        public static Builder create(DocumentSearchCriteriaConfigurationContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            builder.setSearchAttributeFields(contract.getSearchAttributeFields());
            return builder;
        }

        @Override
        public DocumentSearchCriteriaConfiguration build() {
            return new DocumentSearchCriteriaConfiguration(this);
        }

        @Override
        public List<AttributeFields> getSearchAttributeFields() {
            return this.searchAttributeFields;
        }

        /**
         * Sets the search attribute fields on this builder to the given list of attribute fields.
         *
         * @param searchAttributeFields the list of search attribute fields to set
         */
        public void setSearchAttributeFields(List<AttributeFields> searchAttributeFields) {
            this.searchAttributeFields = searchAttributeFields;
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "documentSearchCriteriaConfiguration";
        final static String TYPE_NAME = "DocumentSearchCriteriaConfigurationType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled
     * to XML.
     */
    static class Elements {
        final static String SEARCH_ATTRIBUTE_FIELDS = "searchAttributeFields";
        final static String ATTRIBUTE_FIELDS = "attributeFields";
    }

}
