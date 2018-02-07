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
package org.kuali.rice.kew.api.document.search;

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
 * An immutable data transfer object implementation of the {@link DocumentSearchResultsContract}.  Instances of this
 * class should be constructed using the nested {@link Builder} class.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = DocumentSearchResults.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DocumentSearchResults.Constants.TYPE_NAME, propOrder = {
    DocumentSearchResults.Elements.SEARCH_RESULTS,
    DocumentSearchResults.Elements.CRITERIA,
    DocumentSearchResults.Elements.CRITERIA_MODIFIED,
    DocumentSearchResults.Elements.OVER_THRESHOLD,
    DocumentSearchResults.Elements.NUMBER_OF_SECURITY_FILTERED_RESULTS,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class DocumentSearchResults extends AbstractDataTransferObject implements DocumentSearchResultsContract {

    @XmlElementWrapper(name = Elements.SEARCH_RESULTS, required = true)
    @XmlElement(name = Elements.SEARCH_RESULT, required = false)
    private final List<DocumentSearchResult> searchResults;

    @XmlElement(name = Elements.CRITERIA, required = true)
    private final DocumentSearchCriteria criteria;

    @XmlElement(name = Elements.CRITERIA_MODIFIED, required = true)
    private final boolean criteriaModified;

    @XmlElement(name = Elements.OVER_THRESHOLD, required = true)
    private final boolean overThreshold;

    @XmlElement(name = Elements.NUMBER_OF_SECURITY_FILTERED_RESULTS, required = true)
    private final int numberOfSecurityFilteredResults;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    @SuppressWarnings("unused")
    private DocumentSearchResults() {
        this.searchResults = null;
        this.criteria = null;
        this.criteriaModified = false;
        this.overThreshold = false;
        this.numberOfSecurityFilteredResults = 0;
    }

    private DocumentSearchResults(Builder builder) {
        this.searchResults = ModelObjectUtils.buildImmutableCopy(builder.getSearchResults());
        this.criteria = builder.getCriteria().build();
        this.criteriaModified = builder.isCriteriaModified();
        this.overThreshold = builder.isOverThreshold();
        this.numberOfSecurityFilteredResults = builder.getNumberOfSecurityFilteredResults();
    }

    @Override
    public List<DocumentSearchResult> getSearchResults() {
        return this.searchResults;
    }

    @Override
    public DocumentSearchCriteria getCriteria() {
        return this.criteria;
    }

    @Override
    public boolean isCriteriaModified() {
        return this.criteriaModified;
    }

    @Override
    public boolean isOverThreshold() {
        return this.overThreshold;
    }

    @Override
    public int getNumberOfSecurityFilteredResults() {
        return this.numberOfSecurityFilteredResults;
    }

    /**
     * A builder which can be used to construct {@link DocumentSearchResults} instances.  Enforces the constraints of
     * the {@link DocumentSearchResultsContract}.
     */
    public final static class Builder implements Serializable, ModelBuilder, DocumentSearchResultsContract {

        private List<DocumentSearchResult.Builder> searchResults;
        private DocumentSearchCriteria.Builder criteria;
        private boolean criteriaModified;
        private boolean overThreshold;
        private int numberOfSecurityFilteredResults;

        private Builder(DocumentSearchCriteria.Builder criteria) {
            setSearchResults(new ArrayList<DocumentSearchResult.Builder>());
            setCriteria(criteria);
            setCriteriaModified(false);
            setOverThreshold(false);
            setNumberOfSecurityFilteredResults(0);

        }

        /**
         * Create a builder for the document search result and initialize it with the given document search criteria
         * builder.  Additionally initializes {@code criteriaModified} to "false", {@code overThreshold} to "false",
         * and {@code numberOfSecurityFilteredResults} to 0.
         *
         * @param criteria the document search criteria builder with which to initialize the returned builder instance
         *
         * @return a builder instance initialized with the given document search criteria builder
         *
         * @throws IllegalArgumentException if the given document search criteria builder is null
         */
        public static Builder create(DocumentSearchCriteria.Builder criteria) {
            return new Builder(criteria);
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
        public static Builder create(DocumentSearchResultsContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(DocumentSearchCriteria.Builder.create(contract.getCriteria()));
            if (!CollectionUtils.isEmpty(contract.getSearchResults())) {
                for (DocumentSearchResultContract searchResultContract : contract.getSearchResults()) {
                    builder.getSearchResults().add(DocumentSearchResult.Builder.create(searchResultContract));
                }
            }
            builder.setCriteriaModified(contract.isCriteriaModified());
            builder.setOverThreshold(contract.isOverThreshold());
            builder.setNumberOfSecurityFilteredResults(contract.getNumberOfSecurityFilteredResults());
            return builder;
        }

        public DocumentSearchResults build() {
            return new DocumentSearchResults(this);
        }

        @Override
        public List<DocumentSearchResult.Builder> getSearchResults() {
            return this.searchResults;
        }

        @Override
        public DocumentSearchCriteria.Builder getCriteria() {
            return this.criteria;
        }

        @Override
        public boolean isCriteriaModified() {
            return this.criteriaModified;
        }

        @Override
        public boolean isOverThreshold() {
            return this.overThreshold;
        }

        @Override
        public int getNumberOfSecurityFilteredResults() {
            return this.numberOfSecurityFilteredResults;
        }

        public void setSearchResults(List<DocumentSearchResult.Builder> searchResults) {
            this.searchResults = searchResults;
        }

        /**
         * Sets the criteria builder on this builder to the given value.
         *
         * @param criteria the criteria builder to set, must not be null
         *
         * @throws IllegalArgumentException if criteria is null
         */
        public void setCriteria(DocumentSearchCriteria.Builder criteria) {
            if (criteria == null) {
                throw new IllegalArgumentException("criteria was null");
            }
            this.criteria = criteria;
        }

        public void setCriteriaModified(boolean criteriaModified) {
            this.criteriaModified = criteriaModified;
        }

        public void setOverThreshold(boolean overThreshold) {
            this.overThreshold = overThreshold;
        }

        public void setNumberOfSecurityFilteredResults(int numberOfSecurityFilteredResults) {
            this.numberOfSecurityFilteredResults = numberOfSecurityFilteredResults;
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "documentSearchResults";
        final static String TYPE_NAME = "DocumentSearchResultsType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String SEARCH_RESULTS = "searchResults";
        final static String SEARCH_RESULT = "searchResult";
        final static String CRITERIA = "criteria";
        final static String CRITERIA_MODIFIED = "criteriaModified";
        final static String OVER_THRESHOLD = "overThreshold";
        final static String NUMBER_OF_SECURITY_FILTERED_RESULTS = "numberOfSecurityFilteredResults";
    }

}
