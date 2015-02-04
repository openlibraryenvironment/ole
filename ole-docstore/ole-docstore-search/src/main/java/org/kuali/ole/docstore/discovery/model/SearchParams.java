/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.docstore.discovery.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchParams {
    private String searchType;
    private String docCategory;
    private String docType;
    private String docFormat;
    private String searchTerm;
    private String fieldQuery;
    private List<String> facetFieldList;
    private String resultFromIndex;
    private String resultPageSize;
    private String resultPageIndex;
    private List<String> fieldList;
    private List<String> highlightFieldList;
    private String sortBy;
    private String sortField;
    private String sortOrder;
    private List<SearchCondition> searchFieldsList;
    private String facetValue;
    private String facetField;
    private String solrSearchQueryString;
    private Map<String, String> facetTermsMap;
    private String facetTerms;
    private String operator;
    private int facetMinCount;
    private int facetLimit;
    private int rows;
    private int start;
    private int totalRecCount;
    private String searchTerms;
    private String sortByTerms;
    private String linkValue;
    private String searchQuery;
    private String facetPageSize;
    private String facetPrefix;
    private String facetSort;
    private String facetOffset;

    public int getTotalRecCount() {
        return totalRecCount;
    }

    public void setTotalRecCount(int totalRecCount) {
        this.totalRecCount = totalRecCount;
    }

    public String getFacetOffset() {
        return facetOffset;
    }

    public void setFacetOffset(String facetOffset) {
        this.facetOffset = facetOffset;
    }

    public String getFacetSort() {
        return facetSort;
    }

    public void setFacetSort(String facetSort) {
        this.facetSort = facetSort;
    }

    public String getFacetPrefix() {
        return facetPrefix;
    }

    public void setFacetPrefix(String facetPrefix) {
        this.facetPrefix = facetPrefix;
    }

    public String getFacetPageSize() {

        return facetPageSize;
    }

    public void setFacetPageSize(String facetPageSize) {
        this.facetPageSize = facetPageSize;
    }

    public String getSearchTerms() {
        return searchTerms;
    }


    public void setSearchTerms(String searchTerms) {
        this.searchTerms = searchTerms;
    }


    public String getSolrSearchQueryString() {
        return solrSearchQueryString;
    }

    public void setSolrSearchQueryString(String solrSearchQueryString) {
        this.solrSearchQueryString = solrSearchQueryString;
    }

    public String getFacetValue() {
        return facetValue;
    }

    public void setFacetValue(String facetValue) {
        this.facetValue = facetValue;
    }

    public List<SearchCondition> getSearchFieldsList() {
        if (null == searchFieldsList) {
            searchFieldsList = new ArrayList<SearchCondition>();
        }
        return searchFieldsList;
    }

    public void setSearchFieldsList(List<SearchCondition> searchFieldsList) {
        this.searchFieldsList = searchFieldsList;
    }

    public List<String> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<String> fieldList) {
        this.fieldList = fieldList;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public int getFacetMinCount() {
        return facetMinCount;
    }

    public void setFacetMinCount(int facetMinCount) {
        this.facetMinCount = facetMinCount;
    }

    public int getFacetLimit() {
        return facetLimit;
    }

    public void setFacetLimit(int facetLimit) {
        this.facetLimit = facetLimit;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * Initialize with suitable starting values.
     */
    public void init() {

    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getDocCategory() {
        return docCategory;
    }

    public void setDocCategory(String docCategory) {
        this.docCategory = docCategory;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocFormat() {
        return docFormat;
    }

    public void setDocFormat(String docFormat) {
        this.docFormat = docFormat;
    }

    public String getFieldQuery() {
        return fieldQuery;
    }

    public void setFieldQuery(String fieldQuery) {
        this.fieldQuery = fieldQuery;
    }

    public List<String> getFacetFieldList() {
        return facetFieldList;
    }

    public void setFacetFieldList(List<String> facetFieldList) {
        this.facetFieldList = facetFieldList;
    }

    public String getResultFromIndex() {
        return resultFromIndex;
    }

    public void setResultFromIndex(String resultFromIndex) {
        this.resultFromIndex = resultFromIndex;
    }

    public String getResultPageSize() {
        return resultPageSize;
    }

    public void setResultPageSize(String resultPageSize) {
        this.resultPageSize = resultPageSize;
    }

    public List<String> getHighlightFieldList() {
        return highlightFieldList;
    }

    public void setHighlightFieldList(List<String> highlightFieldList) {
        this.highlightFieldList = highlightFieldList;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getFacetTerms() {
        return facetTerms;
    }

    public void setFacetTerms(String facetTerms) {
        this.facetTerms = facetTerms;
    }

    public Map<String, String> getFacetTermsMap() {
        return facetTermsMap;
    }

    public void setFacetTermsMap(Map<String, String> facetTermsMap) {
        this.facetTermsMap = facetTermsMap;
    }

    public String getFacetField() {
        return facetField;
    }

    public void setFacetField(String facetField) {
        this.facetField = facetField;
    }

    public String getResultPageIndex() {
        return resultPageIndex;
    }

    public void setResultPageIndex(String resultPageIndex) {
        this.resultPageIndex = resultPageIndex;
    }


    public String getSortByTerms() {
        return sortByTerms;
    }


    public void setSortByTerms(String sortByTerms) {
        this.sortByTerms = sortByTerms;
    }


    public String getLinkValue() {
        return linkValue;
    }


    public void setLinkValue(String linkValue) {
        this.linkValue = linkValue;
    }


    public String getSearchQuery() {
        return searchQuery;
    }


    public void setSearchQuery(String query) {
        this.searchQuery = query;
    }

}
