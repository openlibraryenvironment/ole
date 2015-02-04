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
package org.kuali.ole.docstore.discovery.web.struts.form;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.kuali.ole.docstore.discovery.model.SearchCondition;
import org.kuali.ole.docstore.discovery.model.SearchParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class DiscoveryForm extends ActionForm {

    private static final long serialVersionUID = 2785657877358788236L;
    private static final Logger LOG = LoggerFactory.getLogger(DiscoveryForm.class);


    private String searchType;
    private String docCategory;
    private String docType;
    private String docFormat;
    private String searchTerm;
    private String fieldQuery;
    private List<String> facetQueryList;
    private String resultFromIndex = "0";
    private String resultPageSize = "25";
    private String resultPageIndex = "1";
    private String resultFieldList;
    private List<String> highlightFieldList;
    private String sortBy;
    private String sortOrder;
    private String sortField;
    private String sortType;
    private String searchResult;
    private List<SearchCondition> searchFieldsList;
    private String facetValue;
    private String facetField;
    private String solrSearchQueryString;
    private Map<String, String> facetTermsMap;
    private String facetTerms;
    private String searchTerms;
    private String sortByTerms;
    private String linkValue;
    private String searchScope;
    private String searchQuery;
    private String facetPageSize = "25";
    private String facetPrefix = "";
    private String facetSort = "lex";
    private String facetOffset = "0";
    private String searchTime;

    public String getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(String searchTime) {
        this.searchTime = searchTime;
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
        return searchFieldsList;
    }

    public void setSearchFieldsList(List<SearchCondition> searchFieldsList) {
        this.searchFieldsList = searchFieldsList;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    /**
     * Initialize with suitable starting values.
     *
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void init() throws IllegalAccessException, InvocationTargetException {
        DiscoveryForm discoveryForm = new DiscoveryForm();
        discoveryForm.setSearchType(this.getSearchType());
        discoveryForm.setDocCategory(this.getDocCategory());
        discoveryForm.setDocType(this.getDocType());
        discoveryForm.setDocFormat(this.getDocFormat());
        discoveryForm.setSearchTerm(this.getSearchTerm());
        discoveryForm.setFieldQuery(this.getFieldQuery());
        discoveryForm.setResultFromIndex(this.getResultFromIndex());
        discoveryForm.setResultPageSize(this.getResultPageSize());
        discoveryForm.setResultPageIndex(this.getResultPageIndex());
        discoveryForm.setHighlightFieldList(this.getHighlightFieldList());
        discoveryForm.setSortBy(this.getSortBy());
        discoveryForm.setSortOrder(this.getSortOrder());
        discoveryForm.setSortField(this.getSortField());
        discoveryForm.setSearchFieldsList(this.getSearchFieldsList());
        discoveryForm.setFacetValue(this.getFacetValue());
        discoveryForm.setFacetField(this.getFacetField());
        discoveryForm.setSolrSearchQueryString(this.getSolrSearchQueryString());
        discoveryForm.setFacetTermsMap(this.getFacetTermsMap());
        discoveryForm.setFacetTerms(this.getFacetTerms());
        discoveryForm.setSearchTerms(this.getSearchTerms());
        discoveryForm.setSortByTerms(this.getSortByTerms());
        discoveryForm.setLinkValue(this.getLinkValue());
        discoveryForm.setSearchQuery(this.getSearchQuery());
        discoveryForm.setFacetPageSize(this.getFacetPageSize());
        discoveryForm.setFacetPrefix(this.getFacetPrefix());
        discoveryForm.setFacetSort(this.getFacetSort());
        discoveryForm.setFacetOffset(this.getFacetOffset());
//        BeanUtils.copyProperties(this, discoveryForm);
        setResultPageIndex("1");
        setResultPageSize("25");
        setResultFromIndex("0");

    }

    /**
     * Copy the request parameters, if available, into this form object.
     *
     * @param request
     */
    public void initFromRequest(HttpServletRequest request) {
        LOG.debug("initFromRequest");
        String fieldValue;
        List<SearchCondition> searchFieldsList = new ArrayList<SearchCondition>(0);
        Map parameterMap = request.getParameterMap();
        LOG.debug("parameterMap=" + parameterMap);
        String searchType = request.getParameter("searchType");
        String docCategory = request.getParameter("documentCategory");
        String docType = request.getParameter("documentType");
        String docFormat = request.getParameter("documentFormat");
        String searchField = request.getParameter("searchField");
        String searchField1 = request.getParameter("searchField1");
        String searchField2 = request.getParameter("searchField2");
        String searchField3 = request.getParameter("searchField3");
        String searchField4 = request.getParameter("searchField4");
        String searchFieldValue = request.getParameter("st");
        String searchFieldValue1 = request.getParameter("st1");
        String searchFieldValue2 = request.getParameter("st2");
        String searchFieldValue3 = request.getParameter("st3");
        String searchFieldValue4 = request.getParameter("st4");
        String facetValue = request.getParameter("facetvalue");
        String searchScope = request.getParameter("searchScope");
        String searchScope1 = request.getParameter("searchScope1");
        String searchScope2 = request.getParameter("searchScope2");
        String searchScope3 = request.getParameter("searchScope3");
        String searchScope4 = request.getParameter("searchScope4");
        String operator = request.getParameter("operator0");
        String operator1 = request.getParameter("operator1");
        String operator2 = request.getParameter("operator2");
        String operator3 = request.getParameter("operator3");
        LOG.debug("searchField " + searchField);
        LOG.debug("sortField " + sortField);
        LOG.debug("sortOrder " + sortOrder);
        LOG.debug("documentCategory " + docCategory);
        LOG.debug("documentType " + docType);
        LOG.debug("documentFormat " + docFormat);
        LOG.debug("searchType " + searchType);
        LOG.debug("facetValue " + facetValue);
        int x = 0;
        while (true) {
            SearchCondition docSearchFieldsDTO = new SearchCondition();
            String namePrefix = "advancedSearch[advancedSearch][" + x + "]";
            LOG.debug("DocumentServlet.getSearchResults().........." + namePrefix);

            String name = namePrefix + "[searchField]";
            LOG.debug("searchField........." + name);
            String value = request.getParameter(name);
            docSearchFieldsDTO.setDocField(value);
            LOG.debug("searchField........." + value);

            if (value == null)
                break;

            name = namePrefix + "[operator]";
            LOG.debug("operatorname.........." + name);
            value = request.getParameter(name);
            docSearchFieldsDTO.setOperator(value);
            LOG.debug("operatorvalue.........." + value);

            name = namePrefix + "[st]";
            LOG.debug("fieldvalue.........." + name);
            value = request.getParameter(name);
            fieldValue = value;
            LOG.debug("fieldvalue...." + value);
            docSearchFieldsDTO.setSearchText(value);
            if (fieldValue != null && fieldValue.length() > 0) {
                name = namePrefix + "[searchScope]";
                LOG.debug("searchScope.........." + name);
                value = request.getParameter(name);
                docSearchFieldsDTO.setSearchScope(value);
                LOG.debug("searchScopevalue.........." + value);
            }

            if (value != null && value.length() > 0) {
                LOG.debug("in if checking value");
                searchFieldsList.add(docSearchFieldsDTO);
            }
            x++;
        }
        LOG.debug("FieldNamaNar:Values");

        SearchCondition docSearchFieldsDTO = new SearchCondition();
        if (searchFieldValue != null && searchFieldValue.length() > 0) {
            LOG.debug("in if checking outside value");
            docSearchFieldsDTO = getSearchFieldList(searchField, searchFieldValue, searchScope, operator);
            searchFieldsList.add(docSearchFieldsDTO);
        }
        if (searchFieldValue1 != null && searchFieldValue1.length() > 0) {
            LOG.debug("in if checking outside value");
            docSearchFieldsDTO = getSearchFieldList(searchField1, searchFieldValue1, searchScope1, operator1);
            searchFieldsList.add(docSearchFieldsDTO);
        }
        if (searchFieldValue2 != null && searchFieldValue2.length() > 0) {
            LOG.debug("in if checking outside value");
            docSearchFieldsDTO = getSearchFieldList(searchField2, searchFieldValue2, searchScope2, operator2);
            searchFieldsList.add(docSearchFieldsDTO);
        }
        if (searchFieldValue3 != null && searchFieldValue3.length() > 0) {
            LOG.debug("in if checking outside value");
            docSearchFieldsDTO = getSearchFieldList(searchField3, searchFieldValue3, searchScope3, operator3);
            searchFieldsList.add(docSearchFieldsDTO);
        }
        if (searchFieldValue4 != null && searchFieldValue4.length() > 0) {
            LOG.debug("in if checking outside value");
            docSearchFieldsDTO = getSearchFieldList(searchField4, searchFieldValue4, searchScope4, null);
            searchFieldsList.add(docSearchFieldsDTO);
        }

        for (int i = 0; i < searchFieldsList.size(); i++) {
            LOG.debug("docField " + searchFieldsList.get(i).docField);
            LOG.debug("searchText " + searchFieldsList.get(i).searchText);
            LOG.debug("operator " + searchFieldsList.get(i).operator);
        }
        setSearchFieldsList(searchFieldsList);
        setDocCategory(docCategory);
        setDocFormat(docFormat);
        setDocType(docType);
        setSearchType(searchType);
        setFacetValue(facetValue);
        setSearchScope(searchScope);
        LOG.info("DiscoveryForm=" + this.toString());

    }

    public SearchCondition getSearchFieldList(String searchField, String searchFieldValue, String searchScope, String operator) {
        SearchCondition docSearchFieldsDTO = new SearchCondition();
        docSearchFieldsDTO.setDocField(searchField);
        docSearchFieldsDTO.setSearchText(searchFieldValue);
        docSearchFieldsDTO.setSearchScope(searchScope);
        docSearchFieldsDTO.setOperator(operator);
        return docSearchFieldsDTO;
    }

    public void updateFacetParams(String facetField, String facetValue) {
        setFacetField(facetField);
        setFacetValue(facetValue);
        StringBuffer query = new StringBuffer();
        String facetTerms = null;
        Map<String, String> facetTermsMap = getFacetTermsMap();
        if (facetTermsMap == null) {
            LOG.debug("in facetTermsMap null");
            facetTermsMap = new LinkedHashMap<String, String>();
            setFacetTermsMap(facetTermsMap);
        }
        facetTermsMap.put(facetValue, facetField);
        facetTerms = convertListToStringFieldValues(facetTermsMap);
        setFacetTerms(facetTerms);
    }

    public void removeFacet(String facetField, String facetValue) {
        setFacetField(null);
        setFacetValue(null);
        StringBuffer query = new StringBuffer();
        String facetTerms = null;
        Map<String, String> facetTermsMap = getFacetTermsMap();
        facetTermsMap.remove(facetValue);
        setFacetTermsMap(facetTermsMap);
    }

    public String convertListToStringFieldValues(Map<String, String> map) {
        StringBuffer sb = new StringBuffer();
        Set set = map.keySet();
        Iterator<String> ite = set.iterator();
        while (ite.hasNext()) {
            sb.append(ite.next());
            sb.append("|");
        }
        String str = sb.toString();
        if (str != null && str.length() > 0)
            str = str.substring(0, str.length() - 1);
        return str;
    }

    /**
     * Copy values from the form to SearchParams object.
     *
     * @param searchParams
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void initSearchParams(SearchParams searchParams) throws IllegalAccessException, InvocationTargetException {
        searchParams.setSearchType(this.getSearchType());
        searchParams.setDocCategory(this.getDocCategory());
        searchParams.setDocType(this.getDocType());
        searchParams.setDocFormat(this.getDocFormat());
        searchParams.setSearchTerm(this.getSearchTerm());
        searchParams.setFieldQuery(this.getFieldQuery());
        searchParams.setResultFromIndex(this.getResultFromIndex());
        searchParams.setResultPageSize(this.getResultPageSize());
        searchParams.setResultPageIndex(this.getResultPageIndex());
        searchParams.setHighlightFieldList(this.getHighlightFieldList());
        searchParams.setSortBy(this.getSortBy());
        searchParams.setSortOrder(this.getSortOrder());
        searchParams.setSortField(this.getSortField());
        searchParams.setSearchFieldsList(this.getSearchFieldsList());
        searchParams.setFacetValue(this.getFacetValue());
        searchParams.setFacetField(this.getFacetField());
        searchParams.setSolrSearchQueryString(this.getSolrSearchQueryString());
        searchParams.setFacetTermsMap(this.getFacetTermsMap());
        searchParams.setFacetTerms(this.getFacetTerms());
        searchParams.setSearchTerms(this.getSearchTerms());
        searchParams.setSortByTerms(this.getSortByTerms());
        searchParams.setLinkValue(this.getLinkValue());
        searchParams.setSearchQuery(this.getSearchQuery());
        searchParams.setFacetPageSize(this.getFacetPageSize());
        searchParams.setFacetPrefix(this.getFacetPrefix());
        searchParams.setFacetSort(this.getFacetSort());
        searchParams.setFacetOffset(this.getFacetOffset());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[documentCategory=").append(getDocCategory()).append("]");
        sb.append("documentType=").append(getDocType()).append("]");
        sb.append("documentFormat=").append(getDocFormat()).append("]");
        return sb.toString();
    }

    /**
     * Builds and returns an xml representation of the search params.
     *
     * @return
     */
    public String getSearchParamsXml() {
        // TODO: Implement this method.
        String paramsXml =
                "<searchParams>\n" +
                        "	<searchType>advancedSearch</searchType>\n" +
                        "	<documentCategory>bibliographic</documentCategory>\n" +
                        "	<documentType>bibliographic</documentType>\n" +
                        "	<documentFormat>marc</documentFormat>\n" +
                        "	<searchConditions>\n" +
                        "       <searchCondition fieldName=\"Title_search\" fieldValue=\"a*\" operator=\"\" searchScope=\"AND\"/>\n" +
                        "       <searchCondition fieldName=\"Author_search\" fieldValue=\"b*\" operator=\"OR\" searchScope=\"OR\"/>\n" +
                        "       <searchCondition fieldName=\"Publisher_search\" fieldValue=\"c*\" operator=\"OR\" searchScope=\"phrase\"/>\n" +
                        "	</searchConditions>\n" +
                        "</searchParams>\n";


        StringBuilder sb = new StringBuilder();
        sb.append("<searchParams>\n");
        sb.append("	<searchType>").append(getSearchType()).append("</searchType>\n");
        sb.append("	<documentCategory>").append(getDocCategory()).append("</documentCategory>\n");
        sb.append("	<documentType>").append(getDocType()).append("</documentType>\n");
        sb.append("	<documentFormat>").append(getDocFormat()).append("</documentFormat>\n");
        sb.append("		<searchConditions>\n");
        if ((null != searchFieldsList) && (searchFieldsList.size() > 0)) {
            for (SearchCondition sc : searchFieldsList) {
                sb.append("		<searchCondition fieldName=\"")
                        .append(sc.getDocField()).append("\" fieldValue=\"")
                        .append(sc.getSearchText()).append("\" operator=\"")
                        .append(sc.getOperator()).append("\" searchScope=\"")
                        .append(sc.getSearchScope()).append("\">")
                        .append(sc.getSearchScope()).append("</searchCondition>\n");
            }
        }
        sb.append("		</searchConditions>\n");
        sb.append("</searchParams>\n");

        return sb.toString();
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

    public List<String> getFacetQueryList() {
        return facetQueryList;
    }

    public void setFacetQueryList(List<String> facetQueryList) {
        this.facetQueryList = facetQueryList;
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

    public String getResultFieldList() {
        return resultFieldList;
    }

    public void setResultFieldList(String resultFieldList) {
        this.resultFieldList = resultFieldList;
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

    public String getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(String searchResult) {
        this.searchResult = searchResult;
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

    public String getSearchScope() {
        return searchScope;
    }

    public void setSearchScope(String searchScope) {
        this.searchScope = searchScope;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

}
