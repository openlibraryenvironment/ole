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

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.mo.ModelObjectUtils;
import org.kuali.rice.core.api.util.jaxb.DateTimeAdapter;
import org.kuali.rice.core.api.util.jaxb.MultiValuedStringMapAdapter;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.document.DocumentStatusCategory;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An immutable data transfer object implementation of the {@link DocumentSearchCriteriaContract}.  Instances of this
 * class should be constructed using the nested {@link Builder} class.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = DocumentSearchCriteria.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DocumentSearchCriteria.Constants.TYPE_NAME, propOrder = {
    DocumentSearchCriteria.Elements.DOCUMENT_ID,
    DocumentSearchCriteria.Elements.DOCUMENT_STATUSES,
    DocumentSearchCriteria.Elements.DOCUMENT_STATUS_CATEGORIES,
    DocumentSearchCriteria.Elements.TITLE,
    DocumentSearchCriteria.Elements.APPLICATION_DOCUMENT_ID,
    DocumentSearchCriteria.Elements.APPLICATION_DOCUMENT_STATUS,
    DocumentSearchCriteria.Elements.INITIATOR_PRINCIPAL_NAME,
    DocumentSearchCriteria.Elements.VIEWER_PRINCIPAL_NAME,
    DocumentSearchCriteria.Elements.GROUP_VIEWER_ID,
    DocumentSearchCriteria.Elements.GROUP_VIEWER_NAME,
    DocumentSearchCriteria.Elements.APPROVER_PRINCIPAL_NAME,
    DocumentSearchCriteria.Elements.ROUTE_NODE_NAME,
    DocumentSearchCriteria.Elements.ROUTE_NODE_LOOKUP_LOGIC,
    DocumentSearchCriteria.Elements.DOCUMENT_TYPE_NAME,
    DocumentSearchCriteria.Elements.ADDITIONAL_DOCUMENT_TYPE_NAMES,
    DocumentSearchCriteria.Elements.DATE_CREATED_FROM,
    DocumentSearchCriteria.Elements.DATE_CREATED_TO,
    DocumentSearchCriteria.Elements.DATE_LAST_MODIFIED_FROM,
    DocumentSearchCriteria.Elements.DATE_LAST_MODIFIED_TO,
    DocumentSearchCriteria.Elements.DATE_APPROVED_FROM,
    DocumentSearchCriteria.Elements.DATE_APPROVED_TO,
    DocumentSearchCriteria.Elements.DATE_FINALIZED_FROM,
    DocumentSearchCriteria.Elements.DATE_FINALIZED_TO,
    DocumentSearchCriteria.Elements.DATE_APPLICATION_DOCUMENT_STATUS_CHANGED_FROM,
    DocumentSearchCriteria.Elements.DATE_APPLICATION_DOCUMENT_STATUS_CHANGED_TO,
    DocumentSearchCriteria.Elements.DOCUMENT_ATTRIBUTE_VALUES,
    DocumentSearchCriteria.Elements.SAVE_NAME,
    DocumentSearchCriteria.Elements.START_AT_INDEX,
    DocumentSearchCriteria.Elements.MAX_RESULTS,
    DocumentSearchCriteria.Elements.IS_ADVANCED_SEARCH,
    DocumentSearchCriteria.Elements.SEARCH_OPTIONS,
    DocumentSearchCriteria.Elements.APPLICATION_DOCUMENT_STATUSES,
    DocumentSearchCriteria.Elements.DOC_SEARCH_USER_ID,
    DocumentSearchCriteria.Elements.INITIATOR_PRINCIPAL_ID,
    DocumentSearchCriteria.Elements.VIEWER_PRINCIPAL_ID,
    DocumentSearchCriteria.Elements.APPROVER_PRINCIPAL_ID,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class DocumentSearchCriteria extends AbstractDataTransferObject implements DocumentSearchCriteriaContract {

    private static final long serialVersionUID = -221440103480740497L;
    
    @XmlElement(name = Elements.DOCUMENT_ID, required = false)
    private final String documentId;

    @XmlElementWrapper(name = Elements.DOCUMENT_STATUSES, required = false)
    @XmlElement(name = Elements.DOCUMENT_STATUS, required = false)
    private final List<DocumentStatus> documentStatuses;

    @XmlElementWrapper(name = Elements.DOCUMENT_STATUS_CATEGORIES, required = false)
    @XmlElement(name = Elements.DOCUMENT_STATUS_CATEGORY, required = false)
    private final List<DocumentStatusCategory> documentStatusCategories;

    @XmlElement(name = Elements.TITLE, required = false)
    private final String title;

    @XmlElement(name = Elements.APPLICATION_DOCUMENT_ID, required = false)
    private final String applicationDocumentId;

    @XmlElement(name = Elements.APPLICATION_DOCUMENT_STATUS, required = false)
    private final String applicationDocumentStatus;

    @XmlElement(name = Elements.INITIATOR_PRINCIPAL_NAME, required = false)
    private final String initiatorPrincipalName;

    @XmlElement(name = Elements.INITIATOR_PRINCIPAL_ID, required = false)
    private final String initiatorPrincipalId;

    @XmlElement(name = Elements.VIEWER_PRINCIPAL_NAME, required = false)
    private final String viewerPrincipalName;

    @XmlElement(name = Elements.VIEWER_PRINCIPAL_ID, required = false)
    private final String viewerPrincipalId;

    @XmlElement(name = Elements.GROUP_VIEWER_ID, required = false)
    private final String groupViewerId;

    @XmlElement(name = Elements.GROUP_VIEWER_NAME, required = false)
    private final String groupViewerName;
    
    @XmlElement(name = Elements.APPROVER_PRINCIPAL_NAME, required = false)
    private final String approverPrincipalName;

    @XmlElement(name = Elements.APPROVER_PRINCIPAL_ID, required = false)
    private final String approverPrincipalId;

    @XmlElement(name = Elements.ROUTE_NODE_NAME, required = false)
    private final String routeNodeName;

    @XmlElement(name = Elements.ROUTE_NODE_LOOKUP_LOGIC, required = false)
    private final RouteNodeLookupLogic routeNodeLookupLogic;

    @XmlElement(name = Elements.DOCUMENT_TYPE_NAME, required = false)
    private final String documentTypeName;

    @XmlElementWrapper(name = Elements.ADDITIONAL_DOCUMENT_TYPE_NAMES, required = false)
    @XmlElement(name = Elements.ADDITIONAL_DOCUMENT_TYPE_NAME, required = false)
    private final List<String> additionalDocumentTypeNames;

    @XmlElement(name = Elements.DATE_CREATED_FROM, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime dateCreatedFrom;

    @XmlElement(name = Elements.DATE_CREATED_TO, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime dateCreatedTo;

    @XmlElement(name = Elements.DATE_LAST_MODIFIED_FROM, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime dateLastModifiedFrom;

    @XmlElement(name = Elements.DATE_LAST_MODIFIED_TO, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime dateLastModifiedTo;

    @XmlElement(name = Elements.DATE_APPROVED_FROM, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime dateApprovedFrom;

    @XmlElement(name = Elements.DATE_APPROVED_TO, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime dateApprovedTo;

    @XmlElement(name = Elements.DATE_FINALIZED_FROM, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime dateFinalizedFrom;

    @XmlElement(name = Elements.DATE_FINALIZED_TO, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime dateFinalizedTo;

    @XmlElement(name = Elements.DATE_APPLICATION_DOCUMENT_STATUS_CHANGED_FROM, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime dateApplicationDocumentStatusChangedFrom;

    @XmlElement(name = Elements.DATE_APPLICATION_DOCUMENT_STATUS_CHANGED_TO, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime dateApplicationDocumentStatusChangedTo;

    @XmlElement(name = Elements.DOCUMENT_ATTRIBUTE_VALUES, required = false)
    @XmlJavaTypeAdapter(MultiValuedStringMapAdapter.class)
    private final Map<String, List<String>> documentAttributeValues;

    @XmlElement(name = Elements.SAVE_NAME, required = false)
    private final String saveName;

    @XmlElement(name = Elements.START_AT_INDEX, required = false)
    private final Integer startAtIndex;

    @XmlElement(name = Elements.MAX_RESULTS, required = false)
    private final Integer maxResults;

    @XmlElement(name = Elements.IS_ADVANCED_SEARCH, required = false)
    private final String isAdvancedSearch;

    @XmlElement(name = Elements.SEARCH_OPTIONS, required = false)
    @XmlJavaTypeAdapter(MultiValuedStringMapAdapter.class)
    private final Map<String, List<String>> searchOptions;

    /**
     * @since 2.1.2
     */
    @XmlElement(name = Elements.APPLICATION_DOCUMENT_STATUS, required = false)
    @XmlElementWrapper(name = Elements.APPLICATION_DOCUMENT_STATUSES, required = false)
    private final List<String> applicationDocumentStatuses;

    @XmlElement(name = Elements.DOC_SEARCH_USER_ID, required = false)
    private final String docSearchUserId;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    private DocumentSearchCriteria() {
        this.documentId = null;
        this.documentStatuses = null;
        this.documentStatusCategories = null;
        this.title = null;
        this.applicationDocumentId = null;
        this.applicationDocumentStatus = null;
        this.initiatorPrincipalName = null;
        this.initiatorPrincipalId = null;
        this.viewerPrincipalName = null;
        this.viewerPrincipalId = null;
        this.groupViewerId = null;
        this.groupViewerName = null;
        this.approverPrincipalName = null;
        this.approverPrincipalId = null;
        this.routeNodeName = null;
        this.routeNodeLookupLogic = null;
        this.documentTypeName = null;
        this.additionalDocumentTypeNames = null;
        this.dateCreatedFrom = null;
        this.dateCreatedTo = null;
        this.dateLastModifiedFrom = null;
        this.dateLastModifiedTo = null;
        this.dateApprovedFrom = null;
        this.dateApprovedTo = null;
        this.dateFinalizedFrom = null;
        this.dateFinalizedTo = null;
        this.dateApplicationDocumentStatusChangedFrom = null;
        this.dateApplicationDocumentStatusChangedTo = null;
        this.documentAttributeValues = null;
        this.searchOptions = null;
        this.saveName = null;
        this.startAtIndex = null;
        this.maxResults = null;
        this.isAdvancedSearch = null;
        this.docSearchUserId = null;
        this.applicationDocumentStatuses = null;
    }

    private DocumentSearchCriteria(Builder builder) {
        this.documentId = builder.getDocumentId();
        this.documentStatuses = ModelObjectUtils.createImmutableCopy(builder.getDocumentStatuses());
        this.documentStatusCategories = ModelObjectUtils.createImmutableCopy(builder.getDocumentStatusCategories());
        this.title = builder.getTitle();
        this.applicationDocumentId = builder.getApplicationDocumentId();
        this.applicationDocumentStatus = builder.getApplicationDocumentStatus();
        this.initiatorPrincipalName = builder.getInitiatorPrincipalName();
        this.initiatorPrincipalId = builder.getInitiatorPrincipalId();
        this.viewerPrincipalName = builder.getViewerPrincipalName();
        this.viewerPrincipalId = builder.getViewerPrincipalId();
        this.groupViewerId = builder.getGroupViewerId();
        this.groupViewerName = builder.getGroupViewerName();
        this.approverPrincipalName = builder.getApproverPrincipalName();
        this.approverPrincipalId = builder.getApproverPrincipalId();
        this.routeNodeName = builder.getRouteNodeName();
        this.routeNodeLookupLogic = builder.getRouteNodeLookupLogic();
        this.documentTypeName = builder.getDocumentTypeName();
        this.additionalDocumentTypeNames = ModelObjectUtils.createImmutableCopy(
                builder.getAdditionalDocumentTypeNames());
        this.dateCreatedFrom = builder.getDateCreatedFrom();
        this.dateCreatedTo = builder.getDateCreatedTo();
        this.dateLastModifiedFrom = builder.getDateLastModifiedFrom();
        this.dateLastModifiedTo = builder.getDateLastModifiedTo();
        this.dateApprovedFrom = builder.getDateApprovedFrom();
        this.dateApprovedTo = builder.getDateApprovedTo();
        this.dateFinalizedFrom = builder.getDateFinalizedFrom();
        this.dateFinalizedTo = builder.getDateFinalizedTo();
        this.dateApplicationDocumentStatusChangedFrom = builder.getDateApplicationDocumentStatusChangedFrom();
        this.dateApplicationDocumentStatusChangedTo = builder.getDateApplicationDocumentStatusChangedTo();
        this.documentAttributeValues = ModelObjectUtils.createImmutableCopy(builder.getDocumentAttributeValues());
        this.searchOptions = ModelObjectUtils.createImmutableCopy(builder.getSearchOptions());
        this.saveName = builder.getSaveName();
        this.startAtIndex = builder.getStartAtIndex();
        this.maxResults = builder.getMaxResults();
        this.isAdvancedSearch = builder.getIsAdvancedSearch();
        this.docSearchUserId = builder.getDocSearchUserId();
        this.applicationDocumentStatuses = builder.getApplicationDocumentStatuses();
    }

    @Override
    public String getDocumentId() {
        return this.documentId;
    }

    @Override
    public List<DocumentStatus> getDocumentStatuses() {
        return this.documentStatuses;
    }

    @Override
    public List<DocumentStatusCategory> getDocumentStatusCategories() {
        return this.documentStatusCategories;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getApplicationDocumentId() {
        return this.applicationDocumentId;
    }

    /**
     * @deprecated use {@link #getApplicationDocumentStatuses()} instead
     * @return
     */
    @Deprecated
    @Override
    public String getApplicationDocumentStatus() {
        return this.applicationDocumentStatus;
    }

    /**
     * @deprecated use {@link #getInitiatorPrincipalId()} instead
     */
    @Deprecated
    @Override
    public String getInitiatorPrincipalName() {
        return this.initiatorPrincipalName;
    }

    @Override
    public String getInitiatorPrincipalId() {
        return this.initiatorPrincipalId;
    }

    /**
     * @deprecated use {@link #getViewerPrincipalId()} instead
     */
    @Deprecated
    @Override
    public String getViewerPrincipalName() {
        return this.viewerPrincipalName;
    }

    @Override
    public String getViewerPrincipalId() {
        return this.viewerPrincipalId;
    }

    @Override
    public String getGroupViewerId() {
        return this.groupViewerId;
    }
    
    @Override
    public String getGroupViewerName() {
        return this.groupViewerName;
    }

    /**
     * @deprecated use {@link #getApproverPrincipalId()} instead
     */
    @Deprecated
    @Override
    public String getApproverPrincipalName() {
        return this.approverPrincipalName;
    }

    @Override
    public String getApproverPrincipalId() {
        return this.approverPrincipalId;
    }

    @Override
    public String getRouteNodeName() {
        return this.routeNodeName;
    }

    @Override
    public RouteNodeLookupLogic getRouteNodeLookupLogic() {
        return this.routeNodeLookupLogic;
    }

    @Override
    public String getDocumentTypeName() {
        return this.documentTypeName;
    }

    @Override
    public List<String> getAdditionalDocumentTypeNames() {
        return this.additionalDocumentTypeNames;
    }

    @Override
    public DateTime getDateCreatedFrom() {
        return this.dateCreatedFrom;
    }

    @Override
    public DateTime getDateCreatedTo() {
        return this.dateCreatedTo;
    }

    @Override
    public DateTime getDateLastModifiedFrom() {
        return this.dateLastModifiedFrom;
    }

    @Override
    public DateTime getDateLastModifiedTo() {
        return this.dateLastModifiedTo;
    }

    @Override
    public DateTime getDateApprovedFrom() {
        return this.dateApprovedFrom;
    }

    @Override
    public DateTime getDateApprovedTo() {
        return this.dateApprovedTo;
    }

    @Override
    public DateTime getDateFinalizedFrom() {
        return this.dateFinalizedFrom;
    }

    @Override
    public DateTime getDateFinalizedTo() {
        return this.dateFinalizedTo;
    }

    @Override
    public DateTime getDateApplicationDocumentStatusChangedFrom() {
        return dateApplicationDocumentStatusChangedFrom;
    }

    @Override
    public DateTime getDateApplicationDocumentStatusChangedTo() {
        return dateApplicationDocumentStatusChangedTo;
    }

    @Override
    public Map<String, List<String>> getDocumentAttributeValues() {
        return this.documentAttributeValues;
    }

    @Override
    public Map<String, List<String>> getSearchOptions() {
        return this.searchOptions;
    }

    @Override
    public String getSaveName() {
        return saveName;
    }

    @Override
    public Integer getStartAtIndex() {
        return this.startAtIndex;
    }

    @Override
    public Integer getMaxResults() {
        return this.maxResults;
    }

    @Override
    public String getIsAdvancedSearch() {
        return this.isAdvancedSearch;
    }

    /**
     * @since 2.1.2
     */
    @Override
    public List<String> getApplicationDocumentStatuses() {
        return applicationDocumentStatuses;
    }

    @Override
    public String getDocSearchUserId(){
        return docSearchUserId;
    }
    /**
     * A builder which can be used to construct {@link DocumentSearchCriteria} instances.  Enforces the constraints of
     * the {@link DocumentSearchCriteriaContract}.
     */
    public final static class Builder implements Serializable, ModelBuilder, DocumentSearchCriteriaContract {

        private String documentId;
        private List<DocumentStatus> documentStatuses;
        private List<DocumentStatusCategory> documentStatusCategories;
        private String title;
        private String applicationDocumentId;
        private String applicationDocumentStatus;
        private String initiatorPrincipalName;
        private String initiatorPrincipalId;
        private String viewerPrincipalName;
        private String viewerPrincipalId;
        private String groupViewerId;
        private String groupViewerName;
        private String approverPrincipalName;
        private String approverPrincipalId;
        private String routeNodeName;
        private RouteNodeLookupLogic routeNodeLookupLogic;
        private String documentTypeName;
        private List<String> additionalDocumentTypeNames;
        private DateTime dateCreatedFrom;
        private DateTime dateCreatedTo;
        private DateTime dateLastModifiedFrom;
        private DateTime dateLastModifiedTo;
        private DateTime dateApprovedFrom;
        private DateTime dateApprovedTo;
        private DateTime dateFinalizedFrom;
        private DateTime dateFinalizedTo;
        private DateTime dateApplicationDocumentStatusChangedFrom;
        private DateTime dateApplicationDocumentStatusChangedTo;
        private Map<String, List<String>> documentAttributeValues;
        private Map<String, List<String>> searchOptions;
        private String saveName;
        private Integer startAtIndex;
        private Integer maxResults;
        private String isAdvancedSearch;
        private String docSearchUserId;
        private List<String> applicationDocumentStatuses;

        private Builder() {
            setDocumentStatuses(new ArrayList<DocumentStatus>());
            setDocumentStatusCategories(new ArrayList<DocumentStatusCategory>());
            setAdditionalDocumentTypeNames(new ArrayList<String>());
            setDocumentAttributeValues(new HashMap<String, List<String>>());
            setSearchOptions(new HashMap<String, List<String>>());
            setApplicationDocumentStatuses(new ArrayList<String>());
        }

        /**
         * Creates an empty builder instance.  All collections on the new instance are initialized to empty collections.
         *
         * @return a new builder instance
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
        public static Builder create(DocumentSearchCriteriaContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            builder.setDocumentId(contract.getDocumentId());
            if (contract.getDocumentStatuses() != null) {
                builder.setDocumentStatuses(new ArrayList<DocumentStatus>(contract.getDocumentStatuses()));
            }
            if (contract.getDocumentStatusCategories() != null) {
                builder.setDocumentStatusCategories(new ArrayList<DocumentStatusCategory>(contract.getDocumentStatusCategories()));
            }
            builder.setTitle(contract.getTitle());
            builder.setApplicationDocumentId(contract.getApplicationDocumentId());
            builder.setInitiatorPrincipalName(contract.getInitiatorPrincipalName());
            builder.setInitiatorPrincipalId(contract.getInitiatorPrincipalId());
            builder.setViewerPrincipalName(contract.getViewerPrincipalName());
            builder.setViewerPrincipalId(contract.getViewerPrincipalId());
            builder.setGroupViewerId(contract.getGroupViewerId());
            builder.setGroupViewerName(contract.getGroupViewerName());
            builder.setApproverPrincipalName(contract.getApproverPrincipalName());
            builder.setApproverPrincipalId(contract.getApproverPrincipalId());
            builder.setRouteNodeName(contract.getRouteNodeName());
            builder.setRouteNodeLookupLogic(contract.getRouteNodeLookupLogic());
            builder.setDocumentTypeName(contract.getDocumentTypeName());
            if (contract.getAdditionalDocumentTypeNames() != null) {
                builder.setAdditionalDocumentTypeNames(new ArrayList<String>(contract.getAdditionalDocumentTypeNames()));
            }
            builder.setDateCreatedFrom(contract.getDateCreatedFrom());
            builder.setDateCreatedTo(contract.getDateCreatedTo());
            builder.setDateLastModifiedFrom(contract.getDateLastModifiedFrom());
            builder.setDateLastModifiedTo(contract.getDateLastModifiedTo());
            builder.setDateApprovedFrom(contract.getDateApprovedFrom());
            builder.setDateApprovedTo(contract.getDateApprovedTo());
            builder.setDateFinalizedFrom(contract.getDateFinalizedFrom());
            builder.setDateFinalizedTo(contract.getDateFinalizedTo());
            builder.setDateApplicationDocumentStatusChangedFrom(contract.getDateApplicationDocumentStatusChangedFrom());
            builder.setDateApplicationDocumentStatusChangedTo(contract.getDateApplicationDocumentStatusChangedTo());
            if (contract.getDocumentAttributeValues() != null) {
                builder.setDocumentAttributeValues(new HashMap<String, List<String>>(contract.getDocumentAttributeValues()));
            }
            if (contract.getSearchOptions() != null) {
                builder.setSearchOptions(new HashMap<String, List<String>>(contract.getSearchOptions()));
            }
            builder.setSaveName(contract.getSaveName());
            builder.setStartAtIndex(contract.getStartAtIndex());
            builder.setMaxResults(contract.getMaxResults());
            builder.setIsAdvancedSearch(contract.getIsAdvancedSearch());

            // Set applicationDocumentStatuses (plural!)
            builder.setApplicationDocumentStatuses(contract.getApplicationDocumentStatuses());
            // Set applicationDocumentStatus (singular!)
            builder.setApplicationDocumentStatus(contract.getApplicationDocumentStatus());
            builder.setDocSearchUserId(contract.getDocSearchUserId());

            return builder;
        }

        @Override
        public DocumentSearchCriteria build() {
            if (StringUtils.isNotBlank(routeNodeName) && StringUtils.isBlank(documentTypeName)) {
                throw new IllegalStateException("documentTypeName must be set if routeNodeName is set.");
            }
            return new DocumentSearchCriteria(this);
        }

        @Override
        public String getDocumentId() {
            return this.documentId;
        }

        @Override
        public List<DocumentStatus> getDocumentStatuses() {
            return this.documentStatuses;
        }

        @Override
        public List<DocumentStatusCategory> getDocumentStatusCategories() {
            return this.documentStatusCategories;
        }

        @Override
        public String getTitle() {
            return this.title;
        }

        @Override
        public String getApplicationDocumentId() {
            return this.applicationDocumentId;
        }

        /**
         * @deprecated use {@link #getApplicationDocumentStatuses()} instead
         */
        @Deprecated
        @Override
        public String getApplicationDocumentStatus() {
            return this.applicationDocumentStatus;
        }

        /**
         *  @deprecated use {@link #getInitiatorPrincipalId()} instead
         */
        @Deprecated
        @Override
        public String getInitiatorPrincipalName() {
            return this.initiatorPrincipalName;
        }

        @Override
        public String getInitiatorPrincipalId() {
            return this.initiatorPrincipalId;
        }

        /**
         * @deprecated use {@link #getViewerPrincipalId()} instead
         */
        @Deprecated
        @Override
        public String getViewerPrincipalName() {
            return this.viewerPrincipalName;
        }

        @Override
        public String getViewerPrincipalId() {
            return this.viewerPrincipalId;
        }

        @Override
        public String getGroupViewerId() {
            return this.groupViewerId;
        }

        @Override
        public String getGroupViewerName() {
            return this.groupViewerName;
        }

        /**
         * @deprecated use {@link #getApproverPrincipalId()} instead
         */
        @Deprecated
        @Override
        public String getApproverPrincipalName() {
            return this.approverPrincipalName;
        }

        @Override
        public String getApproverPrincipalId() {
            return this.approverPrincipalId;
        }

        @Override
        public String getRouteNodeName() {
            return this.routeNodeName;
        }

        @Override
        public RouteNodeLookupLogic getRouteNodeLookupLogic() {
            return this.routeNodeLookupLogic;
        }

        @Override
        public String getDocumentTypeName() {
            return this.documentTypeName;
        }

        @Override
        public List<String> getAdditionalDocumentTypeNames() {
            return additionalDocumentTypeNames;
        }

        @Override
        public DateTime getDateCreatedFrom() {
            return this.dateCreatedFrom;
        }

        @Override
        public DateTime getDateCreatedTo() {
            return this.dateCreatedTo;
        }

        @Override
        public DateTime getDateLastModifiedFrom() {
            return this.dateLastModifiedFrom;
        }

        @Override
        public DateTime getDateLastModifiedTo() {
            return this.dateLastModifiedTo;
        }

        @Override
        public DateTime getDateApprovedFrom() {
            return this.dateApprovedFrom;
        }

        @Override
        public DateTime getDateApprovedTo() {
            return this.dateApprovedTo;
        }

        @Override
        public DateTime getDateFinalizedFrom() {
            return this.dateFinalizedFrom;
        }

        @Override
        public DateTime getDateFinalizedTo() {
            return this.dateFinalizedTo;
        }

        @Override
        public DateTime getDateApplicationDocumentStatusChangedFrom() {
            return dateApplicationDocumentStatusChangedFrom;
        }

        @Override
        public DateTime getDateApplicationDocumentStatusChangedTo() {
            return dateApplicationDocumentStatusChangedTo;
        }

        @Override
        public Map<String, List<String>> getDocumentAttributeValues() {
            return this.documentAttributeValues;
        }

        @Override
        public Map<String, List<String>> getSearchOptions() {
            return this.searchOptions;
        }

        @Override
        public String getSaveName() {
            return this.saveName;
        }

        @Override
        public Integer getStartAtIndex() {
            return this.startAtIndex;
        }

        @Override
        public Integer getMaxResults() {
            return this.maxResults;
        }

        @Override
        public String getIsAdvancedSearch() {
            return this.isAdvancedSearch;
        }

        public List<String> getApplicationDocumentStatuses() {
            return applicationDocumentStatuses;
        }

        public String getDocSearchUserId(){
          return docSearchUserId;
        }

        public void setDocumentId(String documentId) {
            this.documentId = documentId;
        }

        public void setDocumentStatuses(List<DocumentStatus> documentStatuses) {
            this.documentStatuses = documentStatuses;
        }

        public void setDocumentStatusCategories(List<DocumentStatusCategory> documentStatusCategories) {
            this.documentStatusCategories = documentStatusCategories;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setApplicationDocumentId(String applicationDocumentId) {
            this.applicationDocumentId = applicationDocumentId;
        }

        /**
         * @deprecated use {@link #setApplicationDocumentStatuses(java.util.List)} instead
         */
        @Deprecated
        public void setApplicationDocumentStatus(String applicationDocumentStatus) {
            this.applicationDocumentStatus = applicationDocumentStatus;
        }

        /**
         * @deprecated use {@link #setInitiatorPrincipalId(String)} instead
         * @param initiatorPrincipalName
         */
        public void setInitiatorPrincipalName(String initiatorPrincipalName) {
            this.initiatorPrincipalName = initiatorPrincipalName;
        }

        public void setInitiatorPrincipalId(String initiatorPrincipalId) {
            this.initiatorPrincipalId = initiatorPrincipalId;
        }

        /**
         * @deprecated use {@link #setViewerPrincipalId(String)} instead
         * @param viewerPrincipalName
         */
        @Deprecated
        public void setViewerPrincipalName(String viewerPrincipalName) {
            this.viewerPrincipalName = viewerPrincipalName;
        }

        public void setViewerPrincipalId(String viewerPrincipalId) {
            this.viewerPrincipalId = viewerPrincipalId;
        }

        public void setGroupViewerId(String groupViewerId) {
            this.groupViewerId = groupViewerId;
        }
        
        public void setGroupViewerName (String groupViewerName) {
            this.groupViewerName = groupViewerName;
        }

        /**
         * @deprecated use {@link #setApproverPrincipalId(String)} instead
         * @param approverPrincipalName
         */
        @Deprecated
        public void setApproverPrincipalName(String approverPrincipalName) {
            this.approverPrincipalName = approverPrincipalName;
        }

        public void setApproverPrincipalId(String approverPrincipalId) {
            this.approverPrincipalId = approverPrincipalId;
        }

        public void setRouteNodeName(String routeNodeName) {
            this.routeNodeName = routeNodeName;
        }

        public void setRouteNodeLookupLogic(RouteNodeLookupLogic routeNodeLookupLogic) {
            this.routeNodeLookupLogic = routeNodeLookupLogic;
        }

        public void setDocumentTypeName(String documentTypeName) {
            this.documentTypeName = documentTypeName;
        }

        public void setAdditionalDocumentTypeNames(List<String> additionalDocumentTypeNames) {
            this.additionalDocumentTypeNames = additionalDocumentTypeNames;
        }

        public void setDateCreatedFrom(DateTime dateCreatedFrom) {
            this.dateCreatedFrom = dateCreatedFrom;
        }

        public void setDateCreatedTo(DateTime dateCreatedTo) {
            this.dateCreatedTo = dateCreatedTo;
        }

        public void setDateLastModifiedFrom(DateTime dateLastModifiedFrom) {
            this.dateLastModifiedFrom = dateLastModifiedFrom;
        }

        public void setDateLastModifiedTo(DateTime dateLastModifiedTo) {
            this.dateLastModifiedTo = dateLastModifiedTo;
        }

        public void setDateApprovedFrom(DateTime dateApprovedFrom) {
            this.dateApprovedFrom = dateApprovedFrom;
        }

        public void setDateApprovedTo(DateTime dateApprovedTo) {
            this.dateApprovedTo = dateApprovedTo;
        }

        public void setDateFinalizedFrom(DateTime dateFinalizedFrom) {
            this.dateFinalizedFrom = dateFinalizedFrom;
        }

        public void setDateFinalizedTo(DateTime dateFinalizedTo) {
            this.dateFinalizedTo = dateFinalizedTo;
        }

        public void setDateApplicationDocumentStatusChangedFrom(DateTime dateApplicationDocumentStatusChangedFrom) {
            this.dateApplicationDocumentStatusChangedFrom = dateApplicationDocumentStatusChangedFrom;
        }

        public void setDateApplicationDocumentStatusChangedTo(DateTime dateApplicationDocumentStatusChangedTo) {
            this.dateApplicationDocumentStatusChangedTo = dateApplicationDocumentStatusChangedTo;
        }

        public void setDocumentAttributeValues(Map<String, List<String>> documentAttributeValues) {
            this.documentAttributeValues = documentAttributeValues;
        }

        /**
         * add document attribute value to an internal map
         * @param name - the attribute name
         * @param value - the attribute value
         */
        public void addDocumentAttributeValue(String name, String value) {
            if (StringUtils.isBlank(value)) {
                throw new IllegalArgumentException("value was null or blank");
            }
            List<String> values = getDocumentAttributeValues().get(name);
            if (values == null) {
                values = new ArrayList<String>();
                getDocumentAttributeValues().put(name, values);
            }
            values.add(value);
        }

        public void setSearchOptions(Map<String, List<String>> searchOptions) {
            this.searchOptions = searchOptions;
        }

        public void setSaveName(String saveName) {
            this.saveName = saveName;
        }

        public void setStartAtIndex(Integer startAtIndex) {
            this.startAtIndex = startAtIndex;
        }

        public void setMaxResults(Integer maxResults) {
            this.maxResults = maxResults;
        }

        public void setIsAdvancedSearch(String isAdvancedSearch) {
            this.isAdvancedSearch = isAdvancedSearch;
        }

        /**
         * @since 2.1.2
         */
        public void setApplicationDocumentStatuses(List<String> applicationDocumentStatuses) {
            this.applicationDocumentStatuses = applicationDocumentStatuses;
        }

        public void setDocSearchUserId(String docSearchUserId){
            this.docSearchUserId = docSearchUserId;
        }
        /**
         * Resets DateTimes to local TimeZone (preserving absolute time)
         *
         * @see <a href="http://jira.codehaus.org/browse/JACKSON-279">Modify Joda DateTime</a>
         */
        public void normalizeDateTimes() {
            if (dateCreatedFrom != null) dateCreatedFrom = dateCreatedFrom.withZone(DateTimeZone.getDefault());
            if (dateCreatedTo != null) dateCreatedTo = dateCreatedTo.withZone(DateTimeZone.getDefault());
            if (dateLastModifiedFrom != null) dateLastModifiedFrom = dateLastModifiedFrom.withZone(DateTimeZone.getDefault());
            if (dateLastModifiedTo != null) dateLastModifiedTo = dateLastModifiedTo.withZone(DateTimeZone.getDefault());
            if (dateApprovedFrom != null) dateApprovedFrom = dateApprovedFrom.withZone(DateTimeZone.getDefault());
            if (dateApprovedTo != null) dateApprovedTo = dateApprovedTo.withZone(DateTimeZone.getDefault());
            if (dateFinalizedFrom != null) dateFinalizedFrom = dateFinalizedFrom.withZone(DateTimeZone.getDefault());
            if (dateFinalizedTo != null) dateFinalizedTo = dateFinalizedTo.withZone(DateTimeZone.getDefault());
            if (dateApplicationDocumentStatusChangedFrom != null) dateApplicationDocumentStatusChangedFrom = dateApplicationDocumentStatusChangedFrom.withZone(DateTimeZone.getDefault());
            if (dateApplicationDocumentStatusChangedTo != null) dateApplicationDocumentStatusChangedTo = dateApplicationDocumentStatusChangedTo.withZone(DateTimeZone.getDefault());
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "documentSearchCriteria";
        final static String TYPE_NAME = "DocumentSearchCriteriaType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String DOCUMENT_ID = "documentId";
        final static String DOCUMENT_STATUSES = "documentStatuses";
        final static String DOCUMENT_STATUS = "documentStatus";
        final static String DOCUMENT_STATUS_CATEGORIES = "documentStatusCategories";
        final static String DOCUMENT_STATUS_CATEGORY = "documentStatusCategory";
        final static String TITLE = "title";
        final static String APPLICATION_DOCUMENT_ID = "applicationDocumentId";
        final static String APPLICATION_DOCUMENT_STATUS = "applicationDocumentStatus";
        final static String INITIATOR_PRINCIPAL_NAME = "initiatorPrincipalName";
        final static String INITIATOR_PRINCIPAL_ID = "initiatorPrincipalId";
        final static String VIEWER_PRINCIPAL_NAME = "viewerPrincipalName";
        final static String VIEWER_PRINCIPAL_ID = "viewerPrincipalId";
        final static String GROUP_VIEWER_ID = "groupViewerId";
        final static String GROUP_VIEWER_NAME = "groupViewerName";
        final static String APPROVER_PRINCIPAL_NAME = "approverPrincipalName";
        final static String APPROVER_PRINCIPAL_ID = "approverPrincipalId";
        final static String ROUTE_NODE_NAME = "routeNodeName";
        final static String ROUTE_NODE_LOOKUP_LOGIC = "routeNodeLookupLogic";
        final static String DOCUMENT_TYPE_NAME = "documentTypeName";
        final static String ADDITIONAL_DOCUMENT_TYPE_NAMES = "additionalDocumentTypeNames";
        final static String ADDITIONAL_DOCUMENT_TYPE_NAME = "additionalDocumentTypeName";
        final static String DATE_CREATED_FROM = "dateCreatedFrom";
        final static String DATE_CREATED_TO = "dateCreatedTo";
        final static String DATE_LAST_MODIFIED_FROM = "dateLastModifiedFrom";
        final static String DATE_LAST_MODIFIED_TO = "dateLastModifiedTo";
        final static String DATE_APPROVED_FROM = "dateApprovedFrom";
        final static String DATE_APPROVED_TO = "dateApprovedTo";
        final static String DATE_FINALIZED_FROM = "dateFinalizedFrom";
        final static String DATE_FINALIZED_TO = "dateFinalizedTo";
        final static String DATE_APPLICATION_DOCUMENT_STATUS_CHANGED_FROM = "dateApplicationDocumentStatusChangedFrom";
        final static String DATE_APPLICATION_DOCUMENT_STATUS_CHANGED_TO = "dateApplicationDocumentStatusChangedTo";
        final static String DOCUMENT_ATTRIBUTE_VALUES = "documentAttributeValues";
        final static String SAVE_NAME = "saveName";
        final static String START_AT_INDEX = "startAtIndex";
        final static String MAX_RESULTS = "maxResults";
        final static String IS_ADVANCED_SEARCH = "isAdvancedSearch";
        final static String SEARCH_OPTIONS = "searchOptions";
        final static String APPLICATION_DOCUMENT_STATUSES = "applicationDocumentStatuses";
        final static String DOC_SEARCH_USER_ID = "docSearchUserId";
    }

}