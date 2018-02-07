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

import org.apache.commons.collections.CollectionUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.mo.ModelObjectUtils;
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
import java.util.List;

/**
 * An immutable data transfer object implementation of the {@link DocumentSearchResultValuesContract}.
 * Instances of this class should be constructed using the nested {@link Builder} class.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = DocumentSearchResultValues.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DocumentSearchResultValues.Constants.TYPE_NAME, propOrder = {
    DocumentSearchResultValues.Elements.RESULT_VALUES,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class DocumentSearchResultValues extends AbstractDataTransferObject implements DocumentSearchResultValuesContract {

    @XmlElementWrapper(name = Elements.RESULT_VALUES, required = false)
    @XmlElement(name = Elements.RESULT_VALUE, required = false)
    private final List<DocumentSearchResultValue> resultValues;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    @SuppressWarnings("unused")
    private DocumentSearchResultValues() {
        this.resultValues = null;
    }

    private DocumentSearchResultValues(Builder builder) {
        this.resultValues = ModelObjectUtils.buildImmutableCopy(builder.getResultValues());
    }

    @Override
    public List<DocumentSearchResultValue> getResultValues() {
        return this.resultValues;
    }

    /**
     * A builder which can be used to construct {@link DocumentSearchResultValues} instances.  Enforces the
     * constraints of the {@link DocumentSearchResultValuesContract}.
     */
    public final static class Builder implements Serializable, ModelBuilder, DocumentSearchResultValuesContract {

        private List<DocumentSearchResultValue.Builder> resultValues;

        private Builder() {
            setResultValues(new ArrayList<DocumentSearchResultValue.Builder>());
        }

        /**
         * Creates new empty builder instance.  The various lists on this builder are initialized to empty lists.  The
         * internal list of result value builders is initialized to an empty list.
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
        public static Builder create(DocumentSearchResultValuesContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            if (!CollectionUtils.isEmpty(contract.getResultValues())) {
                for (DocumentSearchResultValueContract resultValueContract : contract.getResultValues()) {
                    //builder.getResultValues().add(DocumentSearchResultValue.Builder.create(resultValueContract));
                }
            }
            return builder;
        }

        @Override
        public DocumentSearchResultValues build() {
            return new DocumentSearchResultValues(this);
        }

        @Override
        public List<DocumentSearchResultValue.Builder> getResultValues() {
            return this.resultValues;
        }

        public void setResultValues(List<DocumentSearchResultValue.Builder> resultValues) {
            this.resultValues = resultValues;
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "documentSearchResultValues";
        final static String TYPE_NAME = "DocumentSearchResultValuesType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String RESULT_VALUES = "resultValues";
        final static String RESULT_VALUE = "resultValue";
    }

}
