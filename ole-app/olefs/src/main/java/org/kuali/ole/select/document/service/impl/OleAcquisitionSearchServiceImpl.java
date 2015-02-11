/*
 * Copyright 2012 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.document.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OleAcquisitionSearchResult;
import org.kuali.ole.select.document.service.OleAcquisitionSearchService;
import org.kuali.ole.select.lookup.DocData;
import org.kuali.ole.select.service.impl.OleDocStoreSearchService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kew.api.document.attribute.DocumentAttribute;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResult;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.exception.WorkflowServiceError;
import org.kuali.rice.kew.exception.WorkflowServiceErrorException;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.*;


public class OleAcquisitionSearchServiceImpl implements OleAcquisitionSearchService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleAcquisitionSearchServiceImpl.class);

    /**
     * This method sets the docData details for searched results
     * by querying to the docStore
     *
     * @param documentSearchResults
     */
    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }
   /* protected Collection<DocData> getDocData(List<DocumentSearchResult> documentSearchResults) {
        List<Object> titleIds = new ArrayList<Object>();
        for (DocumentSearchResult documentSearchResult : documentSearchResults) {
            for (DocumentAttribute documentAttribute : documentSearchResult.getDocumentAttributes()) {
                if (documentAttribute.getName().equalsIgnoreCase(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID) &&
                        documentAttribute.getValue() != null &&
                        StringUtils.isNotBlank(documentAttribute.getValue().toString())) {
                    titleIds.add(documentAttribute.getValue());
                }
            }
        }
        if (!titleIds.isEmpty()) {
            try {
                return SpringContext.getBean(OleDocStoreSearchService.class).getResult(DocData.class, "", titleIds);
            } catch (Exception ex) {
                GlobalVariables.getMessageMap().putError(OleSelectConstant.AcquisitionsSearch.REQUISITIONS, "Error While connecting to DocStore",
                        new String[]{});
                LOG.error("Exception access document store for lookup by titleIds: " + titleIds, ex);
            }
        }
        return Collections.emptyList();
    }*/
    protected Collection<Bib> getBib(List<DocumentSearchResult> documentSearchResults) {
        List<String> titleIds = new ArrayList<>();
        for (DocumentSearchResult documentSearchResult : documentSearchResults) {
            for (DocumentAttribute documentAttribute : documentSearchResult.getDocumentAttributes()) {
                if (documentAttribute.getName().equalsIgnoreCase(OleSelectConstant.AcquisitionsSearch.ITEM_TITLE_ID) &&
                        documentAttribute.getValue() != null &&
                        StringUtils.isNotBlank(documentAttribute.getValue().toString())) {
                    titleIds.add((String)documentAttribute.getValue());
                }
            }
        }
        if (!titleIds.isEmpty()) {
            try {
                List<Bib> bibs=new ArrayList<>();
                bibs.addAll(getDocstoreClientLocator().getDocstoreClient().acquisitionSearchRetrieveBibs(titleIds));
                return bibs;
                //return SpringContext.getBean(OleDocStoreSearchService.class).getResult(DocData.class, "", titleIds);
            } catch (Exception ex) {
                GlobalVariables.getMessageMap().putError(OleSelectConstant.AcquisitionsSearch.REQUISITIONS, "Error While connecting to DocStore",
                        new String[]{});
                LOG.error("Exception access document store for lookup by titleIds: " + titleIds, ex);
            }
        }
        return Collections.emptyList();
    }

    /**
     * This method populates the criteria for the fields in the map
     * using searchable attributes
     *
     * @param criteria
     * @param propertyFields
     * @return criteria
     */
    private DocumentSearchCriteria addDocumentAttributesToCriteria(DocumentSearchCriteria.Builder criteria, Map<String, List<String>> propertyFields) {
        Map<String, List<String>> attributes = new HashMap<String, List<String>>();
        if (criteria != null) {
            if (!propertyFields.isEmpty()) {
                for (String propertyField : propertyFields.keySet()) {
                    if (propertyFields.get(propertyField) != null) {
                        attributes.put(propertyField, propertyFields.get(propertyField));
                    }
                }
            }
        }
        criteria.setDocumentAttributeValues(attributes);
        return criteria.build();
    }

    /**
     * return a string array of titleIds from a List of docdata class
     *
     * @param docDataList
     * @return titleId
     */
    @Override
    public List<String> getTitleIds(List<SearchResult> searchResults) {
        List<String> titleIds = new ArrayList<String>();
        int i = 0;
        for (SearchResult searchResult : searchResults) {
            for(SearchResultField searchResultField:searchResult.getSearchResultFields()){
                if(searchResultField.getFieldName().equalsIgnoreCase("id")){
                titleIds.add(searchResultField.getFieldValue());
                }
            }
        }
        //String[] titleId = titleIds.toArray(new String[titleIds.size()]);
        return titleIds;
    }

    /**
     * returns a collection of resultRow for the search type document
     * from the documentsearchresult components
     *
     * @param components
     * @return resultTable
     */
    private List<OleAcquisitionSearchResult> getFinalDocumentTypeResult(List<DocumentSearchResult> componentResults) {
        List<OleAcquisitionSearchResult> docResult = new ArrayList<OleAcquisitionSearchResult>();
        OleAcquisitionSearchResult acqSearchResult;
        if (!componentResults.isEmpty()) {
            for (DocumentSearchResult searchResult : componentResults) {
                acqSearchResult = new OleAcquisitionSearchResult();
                acqSearchResult.setResultDetails(false, searchResult, new ArrayList());
                if (acqSearchResult != null) {
                    docResult.add(acqSearchResult);
                }
            }
        }
        return docResult;

    }

    /**
     * This method filters other search criteria's based on create from/to date.
     *
     * @param fixedParameters Map containing created date search criteria.
     * @return Map containing document numbers of searched Results
     */
    @Override
    public List<OleAcquisitionSearchResult> performDocumentSearch(DocumentSearchCriteria docSearchCriteria, Map<String, List<String>> fixedParameters) {
        DocumentSearchCriteria docSearchCriteriaDTO = addDocumentAttributesToCriteria(DocumentSearchCriteria.Builder.create(docSearchCriteria), fixedParameters);
        LOG.debug("Inside filterWorkflowStatusDate of OleAcquisitionsSearchDocument");
        List result = new ArrayList();
        List<OleAcquisitionSearchResult> finalResult = new ArrayList<OleAcquisitionSearchResult>();
        DocumentSearchResults components = null;
        boolean isDateSpecified = true;
        try {
            components = KEWServiceLocator.getDocumentSearchService().lookupDocuments(GlobalVariables.getUserSession().getPrincipalId(),
                    docSearchCriteriaDTO);

            List<DocumentSearchResult> docSearchResults = components.getSearchResults();
            if (!fixedParameters.containsKey("displayType")) {
                finalResult = getFinalDocumentTypeResult(docSearchResults);
            } else {
                finalResult = getFinalBibTypeResult(docSearchResults);
            }
        } catch (WorkflowServiceErrorException wsee) {
            for (WorkflowServiceError workflowServiceError : (List<WorkflowServiceError>) wsee.getServiceErrors()) {
                if (workflowServiceError.getMessageMap() != null && workflowServiceError.getMessageMap().hasErrors()) {
                    GlobalVariables.getMessageMap().merge(workflowServiceError.getMessageMap());
                } else {
                    GlobalVariables.getMessageMap().putError(workflowServiceError.getMessage(), RiceKeyConstants.ERROR_CUSTOM,
                            workflowServiceError.getMessage());
                }
            }
            ;
        }
        return finalResult;
    }

    /**
     * returns a collection of resultRow for the search type bib
     * from the documentsearchresult components
     *
     * @param components
     * @return resultTable
     */
    protected List<OleAcquisitionSearchResult> getFinalBibTypeResult(List<DocumentSearchResult> componentResults) {
        List<OleAcquisitionSearchResult> bibResult = new ArrayList<OleAcquisitionSearchResult>();
        List<Bib> bibs= (List<Bib>) getBib(componentResults);
        OleAcquisitionSearchResult acqSearchResult;
        if (!componentResults.isEmpty()) {
            for (DocumentSearchResult searchResult : componentResults) {
                acqSearchResult = new OleAcquisitionSearchResult();
                acqSearchResult.setResultDetails(true, searchResult, bibs);
                if (acqSearchResult != null & StringUtils.isNotBlank(acqSearchResult.getLocalIdentifier())) {
                    bibResult.add(acqSearchResult);
                }
            }
        }
        return bibResult;
    }

}
