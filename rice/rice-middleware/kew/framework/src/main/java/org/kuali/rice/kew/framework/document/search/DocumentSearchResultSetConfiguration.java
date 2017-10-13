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

import org.apache.commons.collections.CollectionUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.mo.ModelObjectUtils;
import org.kuali.rice.core.api.uif.RemotableAttributeFieldContract;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.w3c.dom.Element;

/**
 * An immutable data transfer object implementation of the {@link DocumentSearchResultSetConfigurationContract}.
 * Instances of this class should be constructed using the nested {@link Builder} class.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = DocumentSearchResultSetConfiguration.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DocumentSearchResultSetConfiguration.Constants.TYPE_NAME, propOrder = {
    DocumentSearchResultSetConfiguration.Elements.OVERRIDE_SEARCHABLE_ATTRIBUTES,
    DocumentSearchResultSetConfiguration.Elements.CUSTOM_FIELD_NAMES_TO_ADD,
    DocumentSearchResultSetConfiguration.Elements.STANDARD_RESULT_FIELDS_TO_REMOVE,
    DocumentSearchResultSetConfiguration.Elements.ADDITIONAL_ATTRIBUTE_FIELDS,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class DocumentSearchResultSetConfiguration extends AbstractDataTransferObject
        implements DocumentSearchResultSetConfigurationContract {

    @XmlElement(name = Elements.OVERRIDE_SEARCHABLE_ATTRIBUTES, required = true)
    private final boolean overrideSearchableAttributes;

    @XmlElementWrapper(name = Elements.CUSTOM_FIELD_NAMES_TO_ADD, required = false)
    @XmlElement(name = Elements.CUSTOM_FIELD_NAME_TO_ADD, required = false)
    private final List<String> customFieldNamesToAdd;

    @XmlElementWrapper(name = Elements.STANDARD_RESULT_FIELDS_TO_REMOVE, required = false)
    @XmlElement(name = Elements.STANDARD_RESULT_FIELD_TO_REMOVE, required = false)
    private final List<StandardResultField> standardResultFieldsToRemove;

    @XmlElementWrapper(name = Elements.ADDITIONAL_ATTRIBUTE_FIELDS, required = false)
    @XmlElement(name = Elements.ADDITIONAL_ATTRIBUTE_FIELD, required = false)
    private final List<RemotableAttributeField> additionalAttributeFields;
    
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    @SuppressWarnings("unused")
    private DocumentSearchResultSetConfiguration() {
        this.overrideSearchableAttributes = false;
        this.customFieldNamesToAdd = null;
        this.standardResultFieldsToRemove = null;
        this.additionalAttributeFields = null;
    }

    private DocumentSearchResultSetConfiguration(Builder builder) {
        this.overrideSearchableAttributes = builder.isOverrideSearchableAttributes();
        this.customFieldNamesToAdd = ModelObjectUtils.createImmutableCopy(builder.getCustomFieldNamesToAdd());
        this.standardResultFieldsToRemove =
                ModelObjectUtils.createImmutableCopy(builder.getStandardResultFieldsToRemove());
        this.additionalAttributeFields = ModelObjectUtils.buildImmutableCopy(builder.getAdditionalAttributeFields());
    }

    @Override
    public boolean isOverrideSearchableAttributes() {
        return this.overrideSearchableAttributes;
    }

    @Override
    public List<String> getCustomFieldNamesToAdd() {
        return this.customFieldNamesToAdd;
    }

    @Override
    public List<StandardResultField> getStandardResultFieldsToRemove() {
        return this.standardResultFieldsToRemove;
    }

    @Override
    public List<RemotableAttributeField> getAdditionalAttributeFields() {
        return this.additionalAttributeFields;
    }

    /**
     * A builder which can be used to construct {@link DocumentSearchResultSetConfiguration} instances.  Enforces the
     * constraints of the {@link DocumentSearchResultSetConfigurationContract}.
     */
    public final static class Builder implements Serializable, ModelBuilder, DocumentSearchResultSetConfigurationContract {

        private boolean overrideSearchableAttributes;
        private List<String> customFieldNamesToAdd;
        private List<StandardResultField> standardResultFieldsToRemove;
        private List<RemotableAttributeField.Builder> additionalAttributeFields;

        private Builder() {
            setOverrideSearchableAttributes(false);
            setCustomFieldNamesToAdd(new ArrayList<String>());
            setStandardResultFieldsToRemove(new ArrayList<StandardResultField>());
            setAdditionalAttributeFields(new ArrayList<RemotableAttributeField.Builder>());
        }

        /**
         * Creates new empty builder instance.  The various lists on this builder are initialized to empty lists.  The
         * {@code overrideSearchableAttribute} boolean property is initialized to "false".
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
        public static Builder create(DocumentSearchResultSetConfigurationContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            builder.setOverrideSearchableAttributes(contract.isOverrideSearchableAttributes());
            if (CollectionUtils.isNotEmpty(contract.getCustomFieldNamesToAdd())) {
                builder.setCustomFieldNamesToAdd(new ArrayList<String>(contract.getCustomFieldNamesToAdd()));
            }
            if (CollectionUtils.isNotEmpty(contract.getStandardResultFieldsToRemove())) {
                builder.setStandardResultFieldsToRemove(
                        new ArrayList<StandardResultField>(contract.getStandardResultFieldsToRemove()));
            }
            if (CollectionUtils.isNotEmpty(contract.getAdditionalAttributeFields())) {
                for (RemotableAttributeFieldContract attributeField : contract.getAdditionalAttributeFields()) {
                    builder.getAdditionalAttributeFields().add(RemotableAttributeField.Builder.create(attributeField));
                }
            }
            return builder;
        }

        @Override
        public DocumentSearchResultSetConfiguration build() {
            return new DocumentSearchResultSetConfiguration(this);
        }

        @Override
        public boolean isOverrideSearchableAttributes() {
            return this.overrideSearchableAttributes;
        }

        @Override
        public List<String> getCustomFieldNamesToAdd() {
            return this.customFieldNamesToAdd;
        }

        @Override
        public List<StandardResultField> getStandardResultFieldsToRemove() {
            return this.standardResultFieldsToRemove;
        }

        @Override
        public List<RemotableAttributeField.Builder> getAdditionalAttributeFields() {
            return this.additionalAttributeFields;
        }

        public void setOverrideSearchableAttributes(boolean overrideSearchableAttributes) {
            this.overrideSearchableAttributes = overrideSearchableAttributes;
        }

        public void setCustomFieldNamesToAdd(List<String> customFieldNamesToAdd) {
            this.customFieldNamesToAdd = customFieldNamesToAdd;
        }

        public void setStandardResultFieldsToRemove(List<StandardResultField> standardResultFieldsToRemove) {
            this.standardResultFieldsToRemove = standardResultFieldsToRemove;
        }

        public void setAdditionalAttributeFields(List<RemotableAttributeField.Builder> additionalAttributeFields) {
            this.additionalAttributeFields = additionalAttributeFields;
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "documentSearchResultSetConfiguration";
        final static String TYPE_NAME = "DocumentSearchResultSetConfigurationType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled
     * to XML.
     */
    static class Elements {
        final static String OVERRIDE_SEARCHABLE_ATTRIBUTES = "overrideSearchableAttributes";
        final static String CUSTOM_FIELD_NAMES_TO_ADD = "customFieldNamesToAdd";
        final static String CUSTOM_FIELD_NAME_TO_ADD = "customFieldNameToAdd";
        final static String STANDARD_RESULT_FIELDS_TO_REMOVE = "standardResultFieldsToRemove";
        final static String STANDARD_RESULT_FIELD_TO_REMOVE = "standardResultFieldToRemove";
        final static String ADDITIONAL_ATTRIBUTE_FIELDS = "additionalAttributeFields";
        final static String ADDITIONAL_ATTRIBUTE_FIELD = "additionalAttributeField";
    }

}
