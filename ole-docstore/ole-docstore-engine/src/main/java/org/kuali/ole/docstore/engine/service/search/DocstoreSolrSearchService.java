package org.kuali.ole.docstore.engine.service.search;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.*;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.exception.DocstoreIndexException;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.docstore.common.search.FacetResultField;
import org.kuali.ole.docstore.discovery.service.SolrServerManager;
import org.kuali.ole.docstore.model.enums.DocType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.util.CollectionUtils;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/13/13
 * Time: 6:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocstoreSolrSearchService implements DocstoreSearchService, DocstoreConstants {

    private static final Logger LOG = LoggerFactory.getLogger(DocstoreSolrSearchService.class);


    private static Map<String, String> searchResultFieldsMap = new HashMap<>();

    private static Map<String, String> searchFieldsMap = new HashMap<>();
    private static Map<String, String> joinQueryMap = new HashMap<>();
    private static final String BIB_HOLDINGS = "bibliographicholdings";
    private static final String BIB_EHOLDINGS = "bibliographiceHoldings";
    private static final String BIB_ITEM="bibliographicitem";
    private static final String HOLDINGS_BIB = "holdingsbibliographic";
    private static final String EHOLDINGS_BIB = "eHoldingsbibliographic";
    private static final String HOLDINGS_ITEM = "holdingsitem";
    private static final String ITEM_HOLDINGS = "itemholdings";
    private static final String ITEM_BIB = "itembibliographic";
    static {
        searchFieldsMap.put(Bib.ID, "id");
        searchFieldsMap.put(Bib.AUTHOR, "Author_search");
        searchFieldsMap.put(Bib.TITLE, "Title_search");
        searchFieldsMap.put(Bib.PUBLICATIONDATE, "PublicationDate_search");
        searchFieldsMap.put(Bib.PUBLISHER, "Publisher_search");
        searchFieldsMap.put(Bib.ISBN, "ISBN_search");
        searchFieldsMap.put(Bib.ISSN, "ISSN_search");
        searchFieldsMap.put(Bib.SUBJECT, "Subject_search");
        searchFieldsMap.put(Bib.EDITION, "Edition_search");
        searchFieldsMap.put(Bib.DESCRIPTION, "Description_search");
        searchFieldsMap.put(Bib.FORMAT, "Format_search");
        searchFieldsMap.put(Bib.LANGUAGE, "Language_search");
        searchFieldsMap.put(Bib.BIBIDENTIFIER, "bibIdentifier");
        searchFieldsMap.put(Holdings.HOLDINGSIDENTIFIER, "holdingsIdentifier");
        searchFieldsMap.put(Holdings.LOCATION_NAME, "Location_search");
        searchFieldsMap.put(Holdings.LOCATION_LEVEL1, "Location_search");
        searchFieldsMap.put(Holdings.LOCATION_LEVEL2, "Location_search");
        searchFieldsMap.put(Holdings.LOCATION_LEVEL3, "Location_search");
        searchFieldsMap.put(Holdings.LOCATION_LEVEL4, "Location_search");
        searchFieldsMap.put(Holdings.LOCATION_LEVEL5, "Location_search");
        searchFieldsMap.put(Holdings.CALL_NUMBER, "CallNumber_search");
        searchFieldsMap.put(Holdings.CALLNUMBER_PREFIX, "CallNumberPrefix_search");
        searchFieldsMap.put(Holdings.CALLNUMBER_TYPE, "CallNumberType_search");
        searchFieldsMap.put(Holdings.COPY_NUMBER, "CopyNumber_search");
        searchFieldsMap.put(Item.ITEMIDENTIFIER, "ItemIdentifier_search");
        searchFieldsMap.put(Item.SHELVING_ORDER, "ShelvingOrder_search");
        searchFieldsMap.put(Item.ITEM_BARCODE, "ItemBarcode_search");
        searchFieldsMap.put(Item.ENUMERATION, "Enumeration_search");
        searchFieldsMap.put(Item.CHRONOLOGY, "Chronology_search");
        searchFieldsMap.put(Item.ITEM_TYPE, "ItemTypeCodeValue_search");
        searchFieldsMap.put(Item.VOLUME_NUMBER, "VolumeNumber_search");
        searchFieldsMap.put(Item.ITEM_STATUS, "ItemStatus_search");
        searchFieldsMap.put(Item.ITEMIDENTIFIER, "ItemIdentifier_search");
        searchFieldsMap.put(Item.DONOR_CODE, "DonorCode_search");
        searchFieldsMap.put(Item.DONOR_NOTE, "DonorNote_display");
        searchFieldsMap.put(Item.DONOR_PUBLIC, "DonorPublic_display");
        searchFieldsMap.put(Item.HOLDINGS_CALL_NUMBER, "HoldingsCallNumber_search");
        searchFieldsMap.put(Item.HOLDINGS_CALL_NUMBER_PREFIX, "CallNumberPrefix_search");
        searchFieldsMap.put(Item.HOLDINGS_CALL_NUMBER_TYPE, "CallNumberType_search");
        searchFieldsMap.put(Item.HOLDINGS_COPY_NUMBER, "HoldingsCopyNumber_search");
        searchFieldsMap.put(Item.HOLDINGS_LOCATION_LEVEL1, "HoldingsLocation_search");
        searchFieldsMap.put(Item.HOLDINGS_LOCATION_LEVEL2, "HoldingsLocation_search");
        searchFieldsMap.put(Item.HOLDINGS_LOCATION_LEVEL3, "HoldingsLocation_search");
        searchFieldsMap.put(Item.HOLDINGS_LOCATION_LEVEL4, "HoldingsLocation_search");
        searchFieldsMap.put(Item.HOLDINGS_LOCATION_LEVEL5, "HoldingsLocation_search");
        searchFieldsMap.put(Item.VENDOR_LINE_ITEM_IDENTIFIER, "VendorLineItemIdentifier_search");
        searchFieldsMap.put(EHoldings.ACCESS_STATUS, "AccessStatus_search");
        searchFieldsMap.put(EHoldings.URL, "URL_search");
        searchFieldsMap.put(EHoldings.PERSISTENT_LINK, "Persist_Link_search");
        searchFieldsMap.put(EHoldings.LINK_TEXT, "Link_Text_search");
        searchFieldsMap.put(EHoldings.PUBLIC_DISPLAY_NOTE, "Public_Note_display");
        searchFieldsMap.put(EHoldings.COVERAGE_START_DATE, "CoverageDate_display");
        searchFieldsMap.put(EHoldings.COVERAGE_END_DATE, "CoverageDate_display");
        searchFieldsMap.put(EHoldings.STATISTICAL_CODE, "StatisticalSearchingCodeValue_display");
        searchFieldsMap.put(EHoldings.PLATFORM, "Platform_display");

        searchResultFieldsMap.put(Bib.ID, "id");
        searchResultFieldsMap.put(Bib.AUTHOR, "Author_display");
        searchResultFieldsMap.put(Bib.TITLE, "Title_display");
        searchResultFieldsMap.put(Bib.PUBLICATIONDATE, "PublicationDate_display");
        searchResultFieldsMap.put(Bib.PUBLISHER, "Publisher_display");
        searchResultFieldsMap.put(Bib.ISBN, "ISBN_display");
        searchResultFieldsMap.put(Bib.ISSN, "ISSN_display");
        searchResultFieldsMap.put(Bib.SUBJECT, "Subject_display");
        searchResultFieldsMap.put(Bib.EDITION, "Edition_display");
        searchResultFieldsMap.put(Bib.DESCRIPTION, "Description_display");
        searchResultFieldsMap.put(Bib.FORMAT, "Format_display");
        searchResultFieldsMap.put(Bib.LANGUAGE, "Language_display");
        searchResultFieldsMap.put(Bib.BIBIDENTIFIER, "bibIdentifier");
        searchResultFieldsMap.put(Holdings.HOLDINGSIDENTIFIER, "holdingsIdentifier");
        searchResultFieldsMap.put(Holdings.LOCATION_NAME, "Location_display");
        searchResultFieldsMap.put(Holdings.LOCATION_LEVEL1, "Location_display");
        searchResultFieldsMap.put(Holdings.LOCATION_LEVEL2, "Location_display");
        searchResultFieldsMap.put(Holdings.LOCATION_LEVEL3, "Location_display");
        searchResultFieldsMap.put(Holdings.LOCATION_LEVEL4, "Location_display");
        searchResultFieldsMap.put(Holdings.LOCATION_LEVEL5, "Location_display");
        searchResultFieldsMap.put(Holdings.CALL_NUMBER, "CallNumber_display");
        searchResultFieldsMap.put(Holdings.CALLNUMBER_PREFIX, "CallNumberPrefix_display");
        searchResultFieldsMap.put(Holdings.CALLNUMBER_TYPE, "CallNumberType_display");
        searchResultFieldsMap.put(Holdings.COPY_NUMBER, "CopyNumber_display");
        searchResultFieldsMap.put(Item.ITEMIDENTIFIER, "itemIdentifier");
        searchResultFieldsMap.put(Item.SHELVING_ORDER, "ShelvingOrder_display");
        searchResultFieldsMap.put(Item.ITEM_BARCODE, "ItemBarcode_display");
        searchResultFieldsMap.put(Item.ENUMERATION, "Enumeration_display");
        searchResultFieldsMap.put(Item.CHRONOLOGY, "Chronology_display");
        searchResultFieldsMap.put(Item.ITEM_TYPE, "ItemTypeCodeValue_display");
        searchResultFieldsMap.put(Item.VOLUME_NUMBER, "VolumeNumber_display");
        searchResultFieldsMap.put(Item.ITEM_STATUS, "ItemStatus_display");
        searchResultFieldsMap.put(Item.DONOR_CODE, "DonorCode_display");
        searchResultFieldsMap.put(Item.DONOR_NOTE, "DonorNote_display");
        searchResultFieldsMap.put(Item.DONOR_PUBLIC, "DonorPublic_display");
        searchResultFieldsMap.put(Item.HOLDINGS_CALL_NUMBER, "HoldingsCallNumber_display");
        searchResultFieldsMap.put(Item.HOLDINGS_CALL_NUMBER_PREFIX, "CallNumberPrefix_display");
        searchResultFieldsMap.put(Item.HOLDINGS_CALL_NUMBER_TYPE, "CallNumberType_display");
        searchResultFieldsMap.put(Item.HOLDINGS_COPY_NUMBER, "HoldingsCopyNumber_display");
        searchResultFieldsMap.put(Item.HOLDINGS_LOCATION_LEVEL1, "HoldingsLocation_display");
        searchResultFieldsMap.put(Item.HOLDINGS_LOCATION_LEVEL2, "HoldingsLocation_display");
        searchResultFieldsMap.put(Item.HOLDINGS_LOCATION_LEVEL3, "HoldingsLocation_display");
        searchResultFieldsMap.put(Item.HOLDINGS_LOCATION_LEVEL4, "HoldingsLocation_display");
        searchResultFieldsMap.put(Item.HOLDINGS_LOCATION_LEVEL5, "HoldingsLocation_display");
        searchResultFieldsMap.put(Item.VENDOR_LINE_ITEM_IDENTIFIER, "VendorLineItemIdentifier_display");
        searchResultFieldsMap.put(EHoldings.ACCESS_STATUS, "AccessStatus_display");
        searchResultFieldsMap.put(EHoldings.URL, "URL_display");
        searchResultFieldsMap.put(EHoldings.PERSISTENT_LINK, "Persist_Link_display");
        searchResultFieldsMap.put(EHoldings.LINK_TEXT, "Link_Text_display");
        searchResultFieldsMap.put(EHoldings.PUBLIC_DISPLAY_NOTE, "Public_Note_display");
        searchResultFieldsMap.put(EHoldings.PUBLIC_DISPLAY_NOTE, "CoverageDate_display");
        searchResultFieldsMap.put(EHoldings.STATISTICAL_CODE, "StatisticalSearchingCodeValue_display");
        searchResultFieldsMap.put(EHoldings.PLATFORM, "Platform_display");
        searchResultFieldsMap.put(Bib.BIBIDENTIFIER, "bibIdentifier");
        searchResultFieldsMap.put(Holdings.HOLDINGSIDENTIFIER, "holdingsIdentifier");
        searchResultFieldsMap.put(Item.ITEMIDENTIFIER, "itemIdentifier");
        searchResultFieldsMap.put(Item.DUE_DATE_TIME, "dueDateTime");

        joinQueryMap.put(BIB_HOLDINGS, "{!join from=id to=holdingsIdentifier}");
        joinQueryMap.put(HOLDINGS_BIB, "{!join from=id to=bibIdentifier}");
        joinQueryMap.put(BIB_EHOLDINGS, "{!join from=id to=holdingsIdentifier}");
        joinQueryMap.put(EHOLDINGS_BIB, "{!join from=id to=bibIdentifier}");
        joinQueryMap.put(HOLDINGS_ITEM, "{!join from=id to=itemIdentifier}");
        joinQueryMap.put(ITEM_BIB, "{!join from=id to=bibIdentifier}");
        joinQueryMap.put(ITEM_HOLDINGS, "{!join from=id to=holdingsIdentifier}");
        joinQueryMap.put(BIB_ITEM, "{!join from=id to=itemIdentifier}");
    }

    @Override
    public SearchResponse search(SearchParams searchParams) {
        try {
            SolrQuery solrQuery = new SolrQuery();
            SolrServer server = SolrServerManager.getInstance().getSolrServer();
            buildQueryWithSearchConditions(searchParams,solrQuery);
            if (!CollectionUtils.isEmpty(searchParams.getSortConditions())) {
                buildQueryWithSortConditions(solrQuery, searchParams.getSortConditions());
            }
            if (searchParams.getPageSize() != 0) {
                solrQuery.setRows(searchParams.getPageSize());
            }
            solrQuery.setStart(searchParams.getStartIndex());
            if (!CollectionUtils.isEmpty(searchParams.getSearchResultFields())) {
                solrQuery.setFields(buildQueryWithResultFieldsLists(searchParams));
            }
            if (!CollectionUtils.isEmpty(searchParams.getFacetFields())) {
                for(String facetField : searchParams.getFacetFields()) {
                    solrQuery.addFacetField(facetField);
                }
                solrQuery.setFacet(true);
                if(StringUtils.isNotEmpty(searchParams.getFacetPrefix())) {
                    if(!searchParams.getFacetPrefix().equalsIgnoreCase("all")) {
                        solrQuery.setFacetPrefix(searchParams.getFacetPrefix().toLowerCase());
                    }
                }
//                else {
                    solrQuery.setFacetLimit(searchParams.getFacetLimit());
//                }
                solrQuery.setFacetMinCount(1);
                solrQuery.setFacetSort(searchParams.getFacetSort());
                solrQuery.set("facet.offset", searchParams.getFacetOffset());
            }

            if (!CollectionUtils.isEmpty(searchParams.getFacetConditions())) {
               buildQueryWithFacetConditions(solrQuery, searchParams.getFacetConditions());
            }
            LOG.info("Executing solr query :" +solrQuery.toString());
            QueryResponse response = server.query(solrQuery, SolrRequest.METHOD.POST);
            return buildSearchResponse(response, searchParams);
        } catch (SolrServerException ex) {
            LOG.info("Exception :", ex);
            throw new DocstoreIndexException(ex.getMessage());
        }
    }

    private void buildQueryWithFacetConditions(SolrQuery solrQuery, List<FacetCondition> facetConditions) {

        StringBuffer facetTerms = new StringBuffer();
        StringBuffer filterQuery = new StringBuffer();
        int facetConditionsSize = facetConditions.size();
        for (int i=0; i < facetConditionsSize; i++) {
            FacetCondition facetCondition = facetConditions.get(i);
            if(StringUtils.isNotEmpty(facetCondition.getFieldValue())) {
                facetTerms.append(facetCondition.getFieldValue());
                if(i != (facetConditionsSize-1)) {
                    facetTerms.append("|");
                }
            }
            if(StringUtils.isNotEmpty(facetCondition.getFieldValue())) {
                filterQuery.append(facetCondition.getFieldName());
                filterQuery.append(":");
                filterQuery.append("\"" + facetCondition.getFieldValue() + "\"");

                if(i != (facetConditionsSize-1)) {
                    filterQuery.append(" AND ");
                }
            }
        }

        solrQuery.setTermsRegex(facetTerms.toString());
        solrQuery.setFilterQueries(filterQuery.toString());
    }


    private SearchResponse buildSearchResponse(QueryResponse response, SearchParams searchParams) {
        SolrDocumentList results = response.getResults();
        List<org.apache.solr.client.solrj.response.FacetField> facetFields = response.getFacetFields();
        String searchConditionDocType = searchParams.getDocType();
        SearchResponse searchResponse = new SearchResponse();
        List<SearchResult> searchResults = new ArrayList<>();
        if (StringUtils.isEmpty(searchConditionDocType) && !CollectionUtils.isEmpty(searchParams.getSearchConditions())) {
            for(SearchCondition searchCondition:searchParams.getSearchConditions()){
                if(searchCondition.getSearchField()!=null){
                    searchConditionDocType = searchCondition.getSearchField().getDocType();
                    break;
                }
            }
        }
        List<SearchResultField> searchResultFields = new ArrayList<SearchResultField>();
        if (CollectionUtils.isEmpty(searchParams.getSearchResultFields())) {
            searchResultFields = getDefaultSearchResultFields(searchParams);
        } else {
            searchResultFields = searchParams.getSearchResultFields();
        }
        for (SolrDocument solrDocument : results) {
            SolrDocument bibSolrDocument = null;
            //SolrDocument holdingsSolrDocument = null;
            //SolrDocument itemSolrDocument = null;
            SearchResult searchResult = new SearchResult();
            List<SearchResultField> newSearchResultFields = new ArrayList<SearchResultField>();
            for (SearchResultField searchResultField : searchResultFields) {
                SearchResultField newSearchResultField = new SearchResultField();
                if (StringUtils.isBlank(searchConditionDocType) || searchResultField.getDocType().equalsIgnoreCase(searchConditionDocType)) {
                    String fieldValue = searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) != null ? searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) : searchResultField.getFieldName();
                    if (fieldValue.equalsIgnoreCase("id") || fieldValue.equalsIgnoreCase("bibIdentifier")
                            || fieldValue.equalsIgnoreCase("holdingsIdentifier")
                            || fieldValue.equalsIgnoreCase("itemIdentifier")
                            || fieldValue.equalsIgnoreCase("ItemIdentifier_display")
                            || fieldValue.equalsIgnoreCase("DonorCode_search")
                            || fieldValue.equalsIgnoreCase("DonorCode_display")
                            || fieldValue.equalsIgnoreCase("ISBN_search")
                            || fieldValue.equalsIgnoreCase("ISBN_display")
                            || fieldValue.equalsIgnoreCase("ISSN_search")
                            || fieldValue.equalsIgnoreCase("ISSN_display")) {
                        newSearchResultFields.addAll(buildSearchResultsForIds(solrDocument, searchResultField));
                        continue;
                    }

                    String resultFieldValue = getSolrFieldValue(solrDocument, fieldValue);
                    if (StringUtils.isNotBlank(resultFieldValue)) {
                        newSearchResultField.setFieldValue(resultFieldValue);
                    }
                } else {
                    if (searchResultField.getDocType().equalsIgnoreCase(HOLDINGS)) {
                        if (searchConditionDocType.equalsIgnoreCase(ITEM) || searchConditionDocType.equalsIgnoreCase(BIBLIOGRAPHIC)) {
                            newSearchResultFields.addAll(buildHoldingsSearchResults(solrDocument, searchResultField));
                            continue;
                        }
                    } else if (searchResultField.getDocType().equalsIgnoreCase(BIBLIOGRAPHIC)) {
                        if (searchConditionDocType.equalsIgnoreCase(ITEM) || searchConditionDocType.equalsIgnoreCase(HOLDINGS) || searchConditionDocType.equalsIgnoreCase(EHOLDINGS) ) {
                             newSearchResultFields.addAll(buildBibSearchResults(solrDocument, searchResultField));
                            continue;
                        }
                    } else if (searchResultField.getDocType().equalsIgnoreCase(ITEM)) {
                        if (searchConditionDocType.equalsIgnoreCase(HOLDINGS)) {
                            newSearchResultFields.addAll(buildItemSearchResultsForHoldings(solrDocument, searchResultField));
                            continue;
                        } else if (searchConditionDocType.equalsIgnoreCase(BIBLIOGRAPHIC)) {
                            newSearchResultFields.addAll(buildItemSearchResultsForBib(solrDocument, searchResultField));
                            continue;
                        }
                    } else if (searchConditionDocType.equalsIgnoreCase(EHOLDINGS)) {
                        if (bibSolrDocument == null) {
                            bibSolrDocument = getBibRecord((String) solrDocument.getFieldValue("bibIdentifier"));
                        }
                        String fieldValue = searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) != null ? searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) : searchResultField.getFieldName();
                        String resultFieldValue = getSolrFieldValue(bibSolrDocument, fieldValue);
                        if (StringUtils.isNotBlank(resultFieldValue)) {
                            newSearchResultField.setFieldValue(resultFieldValue);
                        }
                    }
                }
                newSearchResultField.setDocType(searchResultField.getDocType());
                newSearchResultField.setFieldName(searchResultField.getFieldName());
                //newSearchResultField.setFieldValue(searchResultField.getFieldValue());
                newSearchResultFields.add(newSearchResultField);
            }
            searchResult.getSearchResultFields().addAll(newSearchResultFields);
            searchResults.add(searchResult);
        }
        searchResponse.getSearchResults().addAll(searchResults);
        searchResponse.setTotalRecordCount((int) results.getNumFound());
        if (searchParams.getPageSize() == 0) {
            searchResponse.setPageSize(results.size());
        } else {
            searchResponse.setPageSize(searchParams.getPageSize());
        }
        searchResponse.setStartIndex(searchParams.getStartIndex());
        searchResponse.setEndIndex(searchParams.getStartIndex() + searchParams.getPageSize());
        FacetResult facetResult = new FacetResult();
        if (!CollectionUtils.isEmpty(facetFields)) {

            for (org.apache.solr.client.solrj.response.FacetField facetField : facetFields) {


                FacetResultField facetFieldResult = new FacetResultField();
                facetFieldResult.setFieldName(facetField.getName());

                if(facetField.getValues() != null) {
                    for (org.apache.solr.client.solrj.response.FacetField.Count count : facetField.getValues()) {
                        String valCount = String.valueOf(count.getCount());
                        if(!valCount.equalsIgnoreCase("0")) {

                            ValueCount valueCount = new ValueCount();
                            String fullValue = count.getName();
                            String value = "";
                            if(fullValue.contains("/r/n!@#$")) {
                                int index = fullValue.indexOf("/r/n!@#$");
                                value = fullValue.substring(index + 8, fullValue.length());
                            }
                            else {
                                value = fullValue;
                            }
                            valueCount.setValue(value);
                            valueCount.setFullValue(count.getName());
                            valueCount.setCount(valCount);
                            facetFieldResult.getValueCounts().add(valueCount);
                        }
                    }
                }

                facetResult.getFacetResultFields().add(facetFieldResult);
            }
        }

        searchResponse.setFacetResult(facetResult);
        searchResponse.setTime(response.getQTime());
        return searchResponse;
    }

    private List<SearchResultField> buildSearchResultsForIds(SolrDocument solrDocument, SearchResultField searchResultField) {
        List<SearchResultField> newSearchResultFields = new ArrayList<SearchResultField>();
        Object object = solrDocument.getFieldValue(searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) != null ? searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) : searchResultField.getFieldName());
        if (object instanceof String) {
            SearchResultField newSearchResultField = new SearchResultField();
            newSearchResultField.setDocType(searchResultField.getDocType());
            newSearchResultField.setFieldName(searchResultField.getFieldName());
            newSearchResultField.setFieldValue((String) object);
            newSearchResultFields.add(newSearchResultField);
        } else {
            if (((List) object) != null) {
                for (Object identifier : (List) object) {
                    SearchResultField newSearchResultField = new SearchResultField();
                    newSearchResultField.setDocType(searchResultField.getDocType());
                    newSearchResultField.setFieldName(searchResultField.getFieldName());
                    newSearchResultField.setFieldValue((String) identifier);
                    newSearchResultFields.add(newSearchResultField);
                }
            }
        }
        return newSearchResultFields;
    }

    private List<SearchResultField> buildBibSearchResults(SolrDocument solrDocument, SearchResultField searchResultField) {
        List<SearchResultField> newSearchResultFields = new ArrayList<SearchResultField>();
         Object object = solrDocument.getFieldValue("bibIdentifier");
        if (object instanceof String) {
             SearchResultField newSearchResultField = new SearchResultField();
             SolrDocument bibSolrDocument = getBibRecord((String) object);
            if (bibSolrDocument != null) {
                String fieldValue = searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) != null ? searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) : searchResultField.getFieldName();
                String resultFieldValue = getSolrFieldValue(bibSolrDocument, fieldValue);
                if (StringUtils.isNotBlank(resultFieldValue)) {
                    newSearchResultField.setFieldValue(resultFieldValue);
                }
                newSearchResultField.setDocType(searchResultField.getDocType());
                newSearchResultField.setFieldName(searchResultField.getFieldName());
                //newSearchResultField.setFieldValue(searchResultField.getFieldValue());
                newSearchResultFields.add(newSearchResultField);
            }
        } else {
            if (((List) object) != null) {
                for (Object bibIdentifier : (List) object) {
                    SearchResultField newSearchResultField = new SearchResultField();
                    SolrDocument bibSolrDocument = getBibRecord((String) bibIdentifier);
                    if (bibSolrDocument != null) {
                        String fieldValue = searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) != null ? searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) : searchResultField.getFieldName();
                        String resultFieldValue = getSolrFieldValue(bibSolrDocument, fieldValue);
                        if (StringUtils.isNotBlank(resultFieldValue)) {
                            newSearchResultField.setFieldValue(resultFieldValue);
                        }
                        newSearchResultField.setDocType(searchResultField.getDocType());
                        newSearchResultField.setFieldName(searchResultField.getFieldName());
                        //newSearchResultField.setFieldValue(searchResultField.getFieldValue());
                        newSearchResultFields.add(newSearchResultField);
                    }
                }
            }
        }
        return newSearchResultFields;
    }

    private List<SearchResultField> buildItemSearchResultsForHoldings(SolrDocument solrDocument, SearchResultField searchResultField) {
        List<SearchResultField> newSearchResultFields = new ArrayList<SearchResultField>();
        Object object = solrDocument.getFieldValue("itemIdentifier");
        if (object instanceof String) {
            SearchResultField newSearchResultField = new SearchResultField();
            SolrDocument itemSolrDocument = getItemRecord((String) object);
            String fieldValue = searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) != null ? searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) : searchResultField.getFieldName();
            String resultFieldValue = getSolrFieldValue(itemSolrDocument, fieldValue);
            if (StringUtils.isNotBlank(resultFieldValue)) {
                newSearchResultField.setFieldValue(resultFieldValue);
            }
            newSearchResultField.setDocType(searchResultField.getDocType());
            newSearchResultField.setFieldName(searchResultField.getFieldName());
            //newSearchResultField.setFieldValue(searchResultField.getFieldValue());
            newSearchResultFields.add(newSearchResultField);
        } else {
            if (((List) object) != null) {
                for (Object identifier : (List) object) {
                    SearchResultField newSearchResultField = new SearchResultField();
                    SolrDocument itemSolrDocument = getItemRecord((String) identifier);
                    String fieldValue = searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) != null ? searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) : searchResultField.getFieldName();
                    String resultFieldValue = getSolrFieldValue(itemSolrDocument, fieldValue);
                    if (StringUtils.isNotBlank(resultFieldValue)) {
                        newSearchResultField.setFieldValue(resultFieldValue);
                    }
                    newSearchResultField.setDocType(searchResultField.getDocType());
                    newSearchResultField.setFieldName(searchResultField.getFieldName());
                    //newSearchResultField.setFieldValue(searchResultField.getFieldValue());
                    newSearchResultFields.add(newSearchResultField);
                }
            }
        }
        return newSearchResultFields;
    }

    private List<SearchResultField> buildItemSearchResultsForBib(SolrDocument solrDocument, SearchResultField searchResultField) {
        List<SearchResultField> newSearchResultFields = new ArrayList<SearchResultField>();
        Object holdingsObject = solrDocument.getFieldValue("holdingsIdentifier");
        if (holdingsObject instanceof String) {
            SearchResultField newSearchResultField = new SearchResultField();
            SolrDocument holdingsSolrDocument = getHoldingsRecord((String) holdingsObject);
            if (holdingsSolrDocument != null) {
                Object itemObject = holdingsSolrDocument.getFieldValue("itemIdentifier");
                if (itemObject instanceof String) {
                    SolrDocument itemSolrDocument = getItemRecord((String) itemObject);
                    String fieldValue = searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) != null ? searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) : searchResultField.getFieldName();
                    String resultFieldValue = getSolrFieldValue(itemSolrDocument, fieldValue);
                    if (StringUtils.isNotBlank(resultFieldValue)) {
                        newSearchResultField.setFieldValue(resultFieldValue);
                    }
                    newSearchResultField.setDocType(searchResultField.getDocType());
                    newSearchResultField.setFieldName(searchResultField.getFieldName());
                    //newSearchResultField.setFieldValue(searchResultField.getFieldValue());
                    newSearchResultFields.add(newSearchResultField);
                } else {
                    if (((List) itemObject) != null) {
                        for (Object identifier : (List) itemObject) {
                            SearchResultField newSearchResultField1 = new SearchResultField();
                            SolrDocument itemSolrDocument = getItemRecord((String) identifier);
                            String fieldValue = searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) != null ? searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) : searchResultField.getFieldName();
                            String resultFieldValue = getSolrFieldValue(itemSolrDocument, fieldValue);
                            if (StringUtils.isNotBlank(resultFieldValue)) {
                                newSearchResultField.setFieldValue(resultFieldValue);
                            }
                            newSearchResultField1.setDocType(searchResultField.getDocType());
                            newSearchResultField1.setFieldName(searchResultField.getFieldName());
                            //newSearchResultField1.setFieldValue(searchResultField.getFieldValue());
                            newSearchResultFields.add(newSearchResultField1);
                        }
                    }
                }
            }
        } else {
            if (((List) holdingsObject) != null) {
                for (Object identifier : (List) holdingsObject) {
                    SearchResultField newSearchResultField = new SearchResultField();
                    SolrDocument holdingsSolrDocument = getHoldingsRecord((String) identifier);
                    if (holdingsSolrDocument != null) {
                        Object itemObject = holdingsSolrDocument.getFieldValue("itemIdentifier");
                        if (itemObject instanceof String) {
                            SolrDocument itemSolrDocument = getItemRecord((String) itemObject);
                            String fieldValue = searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) != null ? searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) : searchResultField.getFieldName();
                            String resultFieldValue = getSolrFieldValue(itemSolrDocument, fieldValue);
                            if (StringUtils.isNotBlank(resultFieldValue)) {
                                searchResultField.setFieldValue(resultFieldValue);
                            }
                            newSearchResultField.setDocType(searchResultField.getDocType());
                            newSearchResultField.setFieldName(searchResultField.getFieldName());
                            newSearchResultField.setFieldValue(searchResultField.getFieldValue());
                            newSearchResultFields.add(newSearchResultField);
                        } else {
                            if (((List) itemObject) != null) {
                                for (Object itemIdentifier : (List) itemObject) {
                                    SearchResultField newSearchResultField1 = new SearchResultField();
                                    SolrDocument itemSolrDocument = getItemRecord((String) itemIdentifier);
                                    String fieldValue = searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) != null ? searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) : searchResultField.getFieldName();
                                    String resultFieldValue = getSolrFieldValue(itemSolrDocument, fieldValue);
                                    if (StringUtils.isNotBlank(resultFieldValue)) {
                                        searchResultField.setFieldValue(resultFieldValue);
                                    }
                                    newSearchResultField1.setDocType(searchResultField.getDocType());
                                    newSearchResultField1.setFieldName(searchResultField.getFieldName());
                                    newSearchResultField1.setFieldValue(searchResultField.getFieldValue());
                                    newSearchResultFields.add(newSearchResultField1);
                                }
                            }
                        }
                    }
                }
            }
        }
        return newSearchResultFields;
    }

    private List<SearchResultField> buildHoldingsSearchResults(SolrDocument solrDocument, SearchResultField searchResultField) {
        List<SearchResultField> newSearchResultFields = new ArrayList<SearchResultField>();
        Object object = solrDocument.getFieldValue("holdingsIdentifier");
        if (object instanceof String) {
            SearchResultField newSearchResultField = new SearchResultField();
            SolrDocument holdingsSolrDocument = getHoldingsRecord((String) object);
            if (holdingsSolrDocument != null) {
                String fieldValue = searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) != null ? searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) : searchResultField.getFieldName();
                String resultFieldValue = getSolrFieldValue(holdingsSolrDocument, fieldValue);
                if (StringUtils.isNotBlank(resultFieldValue)) {
                    newSearchResultField.setFieldValue(resultFieldValue);
                }
                newSearchResultField.setDocType(searchResultField.getDocType());
                newSearchResultField.setFieldName(searchResultField.getFieldName());
                //newSearchResultField.setFieldValue(searchResultField.getFieldValue());
                newSearchResultFields.add(newSearchResultField);
            }
        } else {
            if (((List) object) != null) {
                for (Object identifier : (List) object) {
                    SearchResultField newSearchResultField = new SearchResultField();
                    SolrDocument holdingsSolrDocument = getHoldingsRecord((String) identifier);
                    if (holdingsSolrDocument != null) {
                        String fieldValue = searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) != null ? searchResultFieldsMap.get(searchResultField.getFieldName().toUpperCase()) : searchResultField.getFieldName();
                        String resultFieldValue = getSolrFieldValue(holdingsSolrDocument, fieldValue);
                        if (StringUtils.isNotBlank(resultFieldValue)) {
                            newSearchResultField.setFieldValue(resultFieldValue);
                        }
                        newSearchResultField.setDocType(searchResultField.getDocType());
                        newSearchResultField.setFieldName(searchResultField.getFieldName());
                        //newSearchResultField.setFieldValue(searchResultField.getFieldValue());
                        newSearchResultFields.add(newSearchResultField);
                    }
                }
            }
        }
        return newSearchResultFields;
    }

    private String getSolrFieldValue(SolrDocument solrDocument, String fieldValue) {
        Object obj = solrDocument.getFieldValue(fieldValue);
        String resultFieldValue = null;
        if (obj instanceof String) {
            resultFieldValue = (String) obj;
        } else if (obj instanceof Boolean) {
            resultFieldValue = String.valueOf(obj);
        } else if (obj instanceof Date) {
            resultFieldValue = String.valueOf(obj);
        } else if (obj instanceof List) {
            if(fieldValue.endsWith("_display")) {
                if (((List) obj) != null) {
                    StringBuilder resultFieldList = new StringBuilder();
                    ArrayList<String> fields = (ArrayList<String>) obj;
                    for(String s : fields) {
                        resultFieldList.append(s);
                        resultFieldList.append(", ");
                    }
                    resultFieldValue = resultFieldList.substring(0, (resultFieldList.length() -1));
                }
            }
            else {
                resultFieldValue = (String) ((List) obj).get(0);
            }
        }
        return resultFieldValue;
    }

    private List<SearchResultField> getDefaultSearchResultFields(SearchParams searchParams) {
        String docType = null;
        if (!CollectionUtils.isEmpty(searchParams.getSearchConditions())) {
            docType = searchParams.getSearchConditions().get(0).getSearchField().getDocType();
        }
        List<SearchResultField> searchResultFields = new ArrayList<>();
        if (StringUtils.isBlank(docType)) {
            String allFields = BIB_FIELDS + "," + HOLDINGS_FIELDS + "," + ITEM_FIELDS;
            for (String defaultField : getDefaultFields(allFields)) {
                SearchResultField searchResultField = new SearchResultField();
                searchResultField.setDocType(docType);
                searchResultField.setFieldName(defaultField);
                searchResultFields.add(searchResultField);
            }
        } else if (docType.equalsIgnoreCase(BIBLIOGRAPHIC)) {
            for (String bibField : getDefaultFields(BIB_FIELDS)) {
                SearchResultField searchResultField = new SearchResultField();
                searchResultField.setDocType(docType);
                searchResultField.setFieldName(bibField);
                searchResultFields.add(searchResultField);
            }
        } else if (docType.equalsIgnoreCase(HOLDINGS)) {
            for (String holdingsField : getDefaultFields(HOLDINGS_FIELDS)) {
                SearchResultField searchResultField = new SearchResultField();
                searchResultField.setDocType(docType);
                searchResultField.setFieldName(holdingsField);
                searchResultFields.add(searchResultField);
            }
        } else if (docType.equalsIgnoreCase(ITEM)) {
            for (String itemField : getDefaultFields(ITEM_FIELDS)) {
                SearchResultField searchResultField = new SearchResultField();
                searchResultField.setDocType(docType);
                searchResultField.setFieldName(itemField);
                searchResultFields.add(searchResultField);
            }
        } else if (docType.equalsIgnoreCase(EHOLDINGS)) {
            for (String itemField : getDefaultFields(EHOLDINGS_FIELDS)) {
                SearchResultField searchResultField = new SearchResultField();
                searchResultField.setDocType(docType);
                searchResultField.setFieldName(itemField);
                searchResultFields.add(searchResultField);
            }
        }
        return searchResultFields;
    }

    private List<String> getDefaultFields(String bibFields) {
        return Arrays.asList(bibFields.split(","));
    }


    public String buildQueryWithSearchParams(SearchParams searchParams) {
        return buildQueryWithSearchConditions(searchParams.getSearchConditions()) /*+ buildQueryWithSortConditions(searchParams.getSortConditions())*/;
    }

    private SolrDocument getItemRecord(String itemIdentifier) {
        try {
            SolrDocument itemSolrDocument = null;
            SearchParams searchParams = new SearchParams();
            SearchField searchField = searchParams.buildSearchField("item", "itemIdentifier", itemIdentifier);
            searchParams.getSearchConditions().add(searchParams.buildSearchCondition("none", searchField, null));
            SolrQuery solrQuery = new SolrQuery();
            SolrServer server = SolrServerManager.getInstance().getSolrServer();
            buildQueryWithSearchConditions(searchParams,solrQuery);
            QueryResponse response = server.query(solrQuery);
            if (response.getResults().size() > 0) {
                itemSolrDocument = response.getResults().get(0);
            }
            return itemSolrDocument;
        } catch (SolrServerException ex) {
            LOG.info("Exception :", ex);
            throw new DocstoreIndexException(ex.getMessage());
        }
    }

    private SolrDocument getBibRecord(String bibIdentifier) {
        try {
            SolrDocument bibSolrDocument = null;
            SearchParams searchParams = new SearchParams();
            SearchField searchField = searchParams.buildSearchField("bibliographic", "LocalId_search", DocumentUniqueIDPrefix.getDocumentId(bibIdentifier));
            searchParams.getSearchConditions().add(searchParams.buildSearchCondition("none", searchField, null));
            SolrQuery solrQuery = new SolrQuery();
            SolrServer server = SolrServerManager.getInstance().getSolrServer();
            buildQueryWithSearchConditions(searchParams,solrQuery);
            QueryResponse response = server.query(solrQuery);
            if (response.getResults().size() > 0) {
                bibSolrDocument = response.getResults().get(0);
            }
            return bibSolrDocument;
        } catch (SolrServerException ex) {
            LOG.info("Exception :", ex);
            throw new DocstoreIndexException(ex.getMessage());
        }
    }

    private SolrDocument getHoldingsRecord(String holdingsIdentifier) {
        try {
            SolrDocument holdingsSolrDocument = null;
            SearchParams searchParams = new SearchParams();
            SearchField searchField = searchParams.buildSearchField("holdings", "holdingsIdentifier", holdingsIdentifier);
            searchParams.getSearchConditions().add(searchParams.buildSearchCondition("none", searchField, null));
            SolrQuery solrQuery = new SolrQuery();
            SolrServer server = SolrServerManager.getInstance().getSolrServer();
            buildQueryWithSearchConditions(searchParams,solrQuery);
            QueryResponse response = server.query(solrQuery);
            if (response.getResults().size() > 0) {
                holdingsSolrDocument = response.getResults().get(0);
            }
            return holdingsSolrDocument;
        } catch (SolrServerException ex) {
            LOG.info("Exception :", ex);
            throw new DocstoreIndexException(ex.getMessage());
        }
    }

    private String buildQueryWithResultFieldsLists(SearchParams searchParams) {
        String docType = searchParams.getDocType();
        List<SearchResultField> searchResultFields = searchParams.getSearchResultFields();
        if (StringUtils.isEmpty(docType) && !CollectionUtils.isEmpty(searchParams.getSearchConditions())) {
            for(SearchCondition searchCondition:searchParams.getSearchConditions()){
                if(searchCondition.getSearchField()!=null){
                    docType = searchCondition.getSearchField().getDocType();
                    break;
                }
            }
        }
        StringBuffer resultFieldsBuffer = new StringBuffer();
        //resultFieldsBuffer.append("&");
        //resultFieldsBuffer.append("fl=");
        if (StringUtils.isBlank(docType)) {
            if (!CollectionUtils.isEmpty(searchResultFields)) {
                for (int i = 0; i < searchResultFields.size(); i++) {
                    resultFieldsBuffer.append(searchResultFieldsMap.get(searchResultFields.get(i).getFieldName().toUpperCase()) != null ? searchResultFieldsMap.get(searchResultFields.get(i).getFieldName().toUpperCase()) : searchResultFields.get(i).getFieldName());
                    if (i != searchResultFields.size() - 1) {
                        resultFieldsBuffer.append(",");
                    }
                }
            } /*else {
                resultFieldsBuffer.append(BIB_FIELDS + "," + HOLDINGS_FIELDS + "," + ITEM_FIELDS);
            }*/
        } else if (docType.equalsIgnoreCase(BIBLIOGRAPHIC)) {
            if (!CollectionUtils.isEmpty(searchResultFields)) {
                for (int i = 0; i < searchResultFields.size(); i++) {
                    if (searchResultFields.get(i).getDocType().equalsIgnoreCase(BIBLIOGRAPHIC)) {
                        resultFieldsBuffer.append(searchResultFieldsMap.get(searchResultFields.get(i).getFieldName().toUpperCase()) != null ? searchResultFieldsMap.get(searchResultFields.get(i).getFieldName().toUpperCase()) : searchResultFields.get(i).getFieldName());
                        resultFieldsBuffer.append(",");
                    }
                }
                resultFieldsBuffer.append(BIB_LINK_FIELDS);
            } /*else {
                resultFieldsBuffer.append(BIB_FIELDS);
            }*/
        } else if (docType.equalsIgnoreCase(HOLDINGS)) {
            if (!CollectionUtils.isEmpty(searchResultFields)) {
                for (int i = 0; i < searchResultFields.size(); i++) {
                    if (searchResultFields.get(i).getDocType().equalsIgnoreCase(HOLDINGS)) {
                        resultFieldsBuffer.append(searchResultFieldsMap.get(searchResultFields.get(i).getFieldName().toUpperCase()) != null ? searchResultFieldsMap.get(searchResultFields.get(i).getFieldName().toUpperCase()) : searchResultFields.get(i).getFieldName());
                        resultFieldsBuffer.append(",");
                    }
                }
                resultFieldsBuffer.append(HOLDINGS_LINK_FIELDS);
            } /*else {
                resultFieldsBuffer.append(HOLDINGS_FIELDS);
            }*/

        } else if (docType.equalsIgnoreCase(ITEM)) {
            if (!CollectionUtils.isEmpty(searchResultFields)) {
                for (int i = 0; i < searchResultFields.size(); i++) {
                    if (searchResultFields.get(i).getDocType().equalsIgnoreCase(ITEM)) {
                        resultFieldsBuffer.append(searchResultFieldsMap.get(searchResultFields.get(i).getFieldName().toUpperCase()) != null ? searchResultFieldsMap.get(searchResultFields.get(i).getFieldName().toUpperCase()) : searchResultFields.get(i).getFieldName());
                        resultFieldsBuffer.append(",");
                    }
                }
                resultFieldsBuffer.append(ITEM_LINK_FIELDS);
            } /*else {
                resultFieldsBuffer.append(ITEM_FIELDS);
            }*/
        } else if (docType.equalsIgnoreCase(EHOLDINGS)) {
            if (!CollectionUtils.isEmpty(searchResultFields)) {
                for (int i = 0; i < searchResultFields.size(); i++) {
                    if (searchResultFields.get(i).getDocType().equalsIgnoreCase(EHOLDINGS)) {
                        resultFieldsBuffer.append(searchResultFieldsMap.get(searchResultFields.get(i).getFieldName().toUpperCase()) != null ? searchResultFieldsMap.get(searchResultFields.get(i).getFieldName().toUpperCase()) : searchResultFields.get(i).getFieldName());
                        resultFieldsBuffer.append(",");
                    }
                }
                resultFieldsBuffer.append(EHOLDINGS_FIELDS);
            } /*else {
                resultFieldsBuffer.append(ITEM_FIELDS);
            }*/
        }
        return resultFieldsBuffer.toString();
    }

    private List<String> getIds(List<SolrDocument> solrDocumentList) {
        List<String> ids = new ArrayList<>();
        for (SolrDocument solrDocument : solrDocumentList) {
            String docType = (String) solrDocument.getFieldValue("DocType");
            if (docType.equalsIgnoreCase(DocType.BIB.getCode())) {
                ids.add((String) solrDocument.getFieldValue("id"));
            } else if (docType.equalsIgnoreCase(DocType.HOLDINGS.getCode())) {
                ids.add((String) solrDocument.getFieldValue("id"));
            } else if (docType.equalsIgnoreCase(DocType.ITEM.getCode())) {
                ids.add((String) solrDocument.getFieldValue("id"));
            }
        }
        return ids;
    }

    public String buildQueryWithSearchConditions(List<SearchCondition> searchConditions) {
        StringBuffer query = new StringBuffer();
        StringBuffer addDocType = new StringBuffer();
        if (searchConditions != null && searchConditions.size() > 0) {
            for (SearchCondition searchCondition : searchConditions) {
                if (searchCondition.getSearchField() != null && StringUtils.isNotBlank(searchCondition.getSearchField().getDocType())) {
                    addDocType.append("(DocType:");
                    addDocType.append(searchCondition.getSearchField().getDocType());
                    addDocType.append(")");
                    break;
                }
            }
        } else {
            return "(*:*)";
        }

        modifySearchCondition(searchConditions);
        //    Collections.reverse(searchConditions);
        int size = searchConditions.size();
        String previousOperator = "";
        String docOperator ="AND";
        for (int i = size - 1; i >= 0; i--) {
            boolean isInsert = false;
            SearchCondition searchCondition = null;
            String searchText = null;
            String searchScope = null;
            if (searchConditions.get(i) != null && searchConditions.get(i).getSearchField() != null) {
                if ((size - 1) != i) {
                    query.insert(0, "(");
                    isInsert = true;
                }
                searchCondition = searchConditions.get(i);
                searchText = searchCondition.getSearchField().getFieldValue();
                if(searchFieldsMap.get(Bib.AUTHOR).equalsIgnoreCase(searchCondition.getSearchField().getFieldName())){
                    searchText = searchText.replaceAll("-"," ");
                }
                searchScope = searchCondition.getSearchScope();
                if (StringUtils.isNotEmpty(searchCondition.getOperator()) && searchCondition.getOperator().equalsIgnoreCase("NOT")) {
                    previousOperator = searchCondition.getOperator();
                    if(size == 1){
                        docOperator = searchCondition.getOperator();
                    }
                }

            } else {
                continue;
            }

            if (isInsert) {
                query.append(previousOperator);
            }

            query.append("(");
            if (StringUtils.isNotBlank(searchCondition.getSearchField().getFieldName())) {
                if (searchCondition.getSearchField().getFieldName().equalsIgnoreCase("all") || searchCondition.getSearchField().getFieldName().equalsIgnoreCase("any")) {
                    query.append("all_text");
                } else {
                    query.append(searchFieldsMap.get(searchCondition.getSearchField().getFieldName().toUpperCase()) != null ? searchFieldsMap.get(searchCondition.getSearchField().getFieldName().toUpperCase()) : searchCondition.getSearchField().getFieldName());
                }
                query.append(":");
            }

            if (StringUtils.isNotBlank(searchText)) {
                String searchTextVal = getModifiedText(searchText);
                if (searchText.length() > 0) {
                    query.append("(");
                    if (StringUtils.isNotBlank(searchScope)) {
                        if (searchScope.equalsIgnoreCase("AND")) {
                            searchTextVal = searchTextVal.trim().replaceAll("\\s+", "+ AND +");
                        } else if (searchScope.equalsIgnoreCase("OR")) {
                            searchTextVal = searchTextVal.trim().replaceAll("\\s+", "+OR+");
                        } else if (searchScope.equalsIgnoreCase("phrase")) {
                            searchTextVal = "\"" + searchText + "\"";
                        } else if (searchScope.equalsIgnoreCase("none")) {
                            searchTextVal = searchText;
                        }
                    }
                    query.append(searchTextVal);
                    query.append(")");
                }
                query.append(")");
            } else {
                query.append("(*:*)");
                query.append(")");
            }

            previousOperator = searchCondition.getOperator();


            if (isInsert) {
                query.append(")");
            }
        }
        if (addDocType != null && addDocType.length() > 0) {
            return addDocType.append(docOperator).append(query).toString();
        } else {
            return query.toString();
        }
    }

    private void modifySearchCondition(List<SearchCondition> searchConditions) {
        List<SearchCondition> tempSearchConditions = new ArrayList<>();
        List<SearchCondition> notSearchConditions = new ArrayList<>();

        for(SearchCondition searchCondition:searchConditions){
            if(StringUtils.isNotEmpty(searchCondition.getOperator()) && searchCondition.getOperator().equalsIgnoreCase("NOT")){
                notSearchConditions.add(searchCondition);
            }else{
                tempSearchConditions.add(searchCondition);
            }
        }

        if(tempSearchConditions.size() >0){
            notSearchConditions.addAll(tempSearchConditions);
        }

        searchConditions.clear();
        searchConditions.addAll(notSearchConditions);
    }


    public void buildQueryWithSearchConditions(SearchParams searchParams, SolrQuery solrQuery) {
        String docType = searchParams.getDocType(); // second
        String key = "";
        String firstDocType = "";
        String secondDocType = searchParams.getDocType();
        String query = "";
        List<SearchCondition> filterSearchConditions = new ArrayList<>();
        List<SearchCondition> querySearchConditions = new ArrayList<>();
        int size = searchParams.getSearchConditions().size();
        for (int i = size - 1; i >= 0; i--) {
            SearchCondition searchCondition = searchParams.getSearchConditions().get(i);
            if (StringUtils.isNotEmpty(searchCondition.getSearchField().getDocType()) && !searchCondition.getSearchField().getDocType().equalsIgnoreCase(secondDocType)) {
                firstDocType = searchCondition.getSearchField().getDocType();
            }
            if (searchCondition.getSearchField() != null && searchCondition.getSearchField().getDocType() != null && docType.equals(searchCondition.getSearchField().getDocType())) {
                filterSearchConditions.add(searchCondition);
            } else {
                querySearchConditions.add(searchCondition);
            }
        }
        if (StringUtils.isEmpty(secondDocType)) {
            secondDocType = docType;
        }

        if (StringUtils.isEmpty(secondDocType)) {
            secondDocType = firstDocType;
        }
        if (StringUtils.isEmpty(docType)) {
            docType = firstDocType;
        }
        if (StringUtils.isNotEmpty(firstDocType) && StringUtils.isNotEmpty(secondDocType)) {
            key = secondDocType + firstDocType;
            String crossDocumentJoinQuery = joinQueryMap.get(key);
            if (StringUtils.isNotEmpty(crossDocumentJoinQuery)) {
                query = joinQueryMap.get(key) + buildQueryWithSearchConditions(querySearchConditions);
            } else {
                query = buildQueryWithSearchConditions(querySearchConditions);
            }

            String filterQuery = "";
            if (filterSearchConditions != null && filterSearchConditions.size() > 0) {
                filterQuery = buildQueryWithSearchConditions(filterSearchConditions);
            }
            if (StringUtils.isEmpty(filterQuery)) {
                filterQuery = "DocType:" + docType;
            }
            solrQuery.setQuery(query);
            solrQuery.setFilterQueries(filterQuery);
            LOG.info("join query = " + solrQuery.getQuery() + "\tfilter query = " + solrQuery.getFilterQueries()[0]);
        } else {
            String filterQuery = buildQueryWithSearchConditions(filterSearchConditions);
            solrQuery.setQuery(filterQuery);
            LOG.info("query = " + solrQuery.getQuery());
        }

    }


//    public String buildQueryWithSearchConditions(List<SearchCondition> searchConditions) {
//        boolean searchConditionCheck = false;
//        SearchCondition searchCondition = null;
//        String docType = null;
//        StringBuffer queryStringBuffer = new StringBuffer();
//        if (searchConditions != null && searchConditions.size() > 0) {
//            for (int i = 0; i < searchConditions.size(); i++) {
//                int searchScopeAddLimit = i;
//                searchCondition = searchConditions.get(i);
//                if (searchCondition.getSearchField() != null) {
//                    if (StringUtils.isBlank(docType) && searchScopeAddLimit == 0) {
//                        docType = searchCondition.getSearchField().getDocType();
//                        if (StringUtils.isNotBlank(docType)) {
//                            queryStringBuffer.append("(");
//                            queryStringBuffer.append("DocType:" + docType);
//                            queryStringBuffer.append(")");
//                        }
//                    }
//                    String searchScope = searchCondition.getSearchScope();
//                    String searchOperator = searchCondition.getOperator();
//                    String searchText = searchCondition.getSearchField().getFieldValue();
//                    if (StringUtils.isBlank(docType) || docType.equalsIgnoreCase(searchCondition.getSearchField().getDocType())) {
//                        if (StringUtils.isNotBlank(searchCondition.getSearchField().getFieldName()) || StringUtils.isNotBlank(searchCondition.getSearchField().getFieldValue())) {
//                            if (StringUtils.isNotBlank(docType) && searchScopeAddLimit == 0) {
//                                queryStringBuffer.append("AND");
//                                queryStringBuffer.append("(");
//                            }
//                            queryStringBuffer.append("(");
//                            if (StringUtils.isNotBlank(searchCondition.getSearchField().getFieldName())) {
//                                if (searchCondition.getSearchField().getFieldName().equalsIgnoreCase("all")) {
//                                    queryStringBuffer.append("all_text");
//                                } else {
//                                    queryStringBuffer.append(searchFieldsMap.get(searchCondition.getSearchField().getFieldName().toUpperCase()) != null ? searchFieldsMap.get(searchCondition.getSearchField().getFieldName().toUpperCase()) : searchCondition.getSearchField().getFieldName());
//                                }
//                                queryStringBuffer.append(":");
//                            }
//
//                            if (StringUtils.isNotBlank(searchText)) {
//                                String searchTextVal = getModifiedText(searchText);
//                                if (searchText.length() > 0) {
//                                    queryStringBuffer.append("(");
//                                    if (StringUtils.isNotBlank(searchScope)) {
//                                        if (searchScope.equalsIgnoreCase("AND")) {
//                                            searchTextVal = searchTextVal.trim().replaceAll("\\s+", "+ AND +");
//                                        } else if (searchScope.equalsIgnoreCase("OR")) {
//                                            searchTextVal = searchTextVal.trim().replaceAll("\\s+", "+ OR +");
//                                        } else if (searchScope.equalsIgnoreCase("phrase")) {
//                                            searchTextVal = "\"" + searchText + "\"";
//                                        } else if (searchScope.equalsIgnoreCase("none")) {
//                                            searchTextVal = searchText;
//                                        }
//                                    }
//                                    queryStringBuffer.append(searchTextVal);
//                                    queryStringBuffer.append(")");
//                                }
//                                queryStringBuffer.append(")");
//                                searchConditionCheck = true;
//                            } else {
//                                queryStringBuffer.append("(*:*)");
//                                queryStringBuffer.append(")");
//                                searchConditionCheck = true;
//                            }
//                        }
//                    }
//                    ++searchScopeAddLimit;
//                    if (StringUtils.isBlank(searchOperator)) {
//                        break;
//                    }
//                    if (searchScopeAddLimit != searchConditions.size()) {
//                        if (StringUtils.isBlank(docType) || StringUtils.isNotBlank(docType) && docType.equalsIgnoreCase(searchConditions.get(searchScopeAddLimit).getSearchField().getDocType())) {
//                            queryStringBuffer.append(searchOperator);
//                        }
//                    }
//                }
//                if (StringUtils.isBlank(docType) && StringUtils.isBlank(searchCondition.getSearchField().getFieldName()) && StringUtils.isBlank(searchCondition.getSearchField().getFieldValue())) {
//                    queryStringBuffer.append("*:*");
//                }
//            }
//        } else {
//            queryStringBuffer.append("*:*");
//        }
//        if (searchConditionCheck && StringUtils.isNotBlank(docType)) {
//            queryStringBuffer.append(")");
//        }
//        return queryStringBuffer.toString();
//    }

    private String getModifiedText(String searchText) {
        StringBuffer modifiedText = new StringBuffer();
        boolean isHashReplaced=true;
        if(searchText.contains(DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC) || searchText.contains(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML) || searchText.contains(DocumentUniqueIDPrefix.PREFIX_WORK_ITEM_OLEML)  || searchText.equalsIgnoreCase("RECENTLY-RETURNED")){
            isHashReplaced=false;
        }
        StringCharacterIterator stringCharacterIterator = new StringCharacterIterator(searchText);
        char character = stringCharacterIterator.current();
        while (character != CharacterIterator.DONE) {

            if (character == '\\') {
                modifiedText.append("\\\\");
            } else if (character == '?') {
                modifiedText.append("\\?");
            } else if (character == '*' && StringUtils.isEmpty(modifiedText.toString())) {
                modifiedText.append("\\*");
            } else if (character == '+') {
                modifiedText.append("\\+");
            } else if (character == ':') {
                modifiedText.append("\\:");
            } else if (character == '{') {
                modifiedText.append("\\{");
            } else if (character == '}') {
                modifiedText.append("\\}");
            } else if (character == '[') {
                modifiedText.append("\\[");
            } else if (character == ']') {
                modifiedText.append("\\]");
            } else if (character == '(') {
                modifiedText.append("\\(");
            } else if (character == ')') {
                modifiedText.append("\\)");
            } else if (character == '^') {
                modifiedText.append("\\^");
            } else if (character == '~') {
                modifiedText.append("\\~");
            } else if (character == '-') {
                if (isHashReplaced) {
                    modifiedText.append("");
                } else {
                    modifiedText.append("\\-");
                }
            } else if (character == '!') {
                modifiedText.append("\\!");
            } else if (character == '\'') {
                modifiedText.append("\\'");
            } else if (character == '@') {
                modifiedText.append("\\@");
            } else if (character == '#') {
                modifiedText.append("\\#");
            } else if (character == '$') {
                modifiedText.append("\\$");
            } else if (character == '%') {
                modifiedText.append("\\%");
            } else {
                // the char is not a special one
                // add it to the result as is
                modifiedText.append(character);
            }
            character = stringCharacterIterator.next();
        }
        return modifiedText.toString();
    }

    /*public String buildQueryWithSortConditions(List<SortCondition> sortConditions) {
        StringBuffer sortFieldsQuery = new StringBuffer();
        for (SortCondition sortCondition : sortConditions) {
            String sortField = sortCondition.getSortField();
            String sortOrder = sortCondition.getSortOrder();
            if (StringUtils.isNotBlank(sortField) && StringUtils.isNotBlank(getSolrSortField(sortField.toUpperCase())) && StringUtils.isNotBlank(sortOrder)) {
                sortFieldsQuery.append("&");
                sortFieldsQuery.append("sort=");
                sortFieldsQuery.append(getSolrSortField(sortField.toUpperCase()));
                sortFieldsQuery.append(" ");
                sortFieldsQuery.append(sortOrder);
            }
        }
        return sortFieldsQuery.toString();
    }*/

    private void buildQueryWithSortConditions(SolrQuery solrQuery, List<SortCondition> sortConditions) {
        for (SortCondition sortCondition : sortConditions) {
            String sortField = sortCondition.getSortField();
            String sortOrder = sortCondition.getSortOrder();
            if (StringUtils.isNotBlank(sortField) && StringUtils.isNotBlank(sortOrder)) {
                solrQuery.addSortField(getSolrSortField(sortField.toUpperCase()) != null ? getSolrSortField(sortField.toUpperCase()) : sortField, SolrQuery.ORDER.valueOf(sortOrder));
            }
        }
    }

    private String getSolrSortField(String sortField) {

        if ("TITLE".equals(sortField)) {
            return "Title_sort";
        } else if ("AUTHOR".equals(sortField)) {
            return "Author_sort";
        } else if ("PUBLICATION DATE".equals(sortField)) {
            return "PublicationDate_sort";
        } else if ("LOCATION".equals(sortField)) {
            return "Location_sort";
        } else if ("CALL NUMBER".equals(sortField)) {
            return "CallNumber_sort";
        } else if ("LOCAL IDENTIFIER".equalsIgnoreCase(sortField)){
            return "LocalId_search";
        } else if ("Journal Title".equalsIgnoreCase(sortField)){
            return "JournalTitle_sort";
        }
        return null;
    }

    @Override
    public List<String> callNumberBrowse(BrowseParams browseParams) {
        try {
            SolrQuery solrQuery = new SolrQuery();
            SolrServer server = SolrServerManager.getInstance().getSolrServer();
            solrQuery.setQuery(buildQueryWithSearchParams(browseParams));
            QueryResponse response = server.query(solrQuery);
            browseParams.setTotalCount(response.getResults().getNumFound());
            return getIds(response.getResults());
        } catch (SolrServerException ex) {
            LOG.info("Exception :", ex);
            throw new DocstoreIndexException(ex.getMessage());
        }
    }

    @Override
    public String findBib(Map<String, String> map) {
        List<String> bibIds = new ArrayList<>();
        SearchParams searchParams = buildSearchParamsFromMap(map);
        SearchResponse searchResponse = search(searchParams);
        for (SearchResult searchResult : searchResponse.getSearchResults()) {
            for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                bibIds.add(searchResultField.getFieldValue());
            }
        }
        if (!CollectionUtils.isEmpty(bibIds)) {
            return bibIds.get(0);
        }
        return null;
    }

    private SearchParams buildSearchParamsFromMap(Map<String, String> map) {
        SearchParams searchParams = new SearchParams();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            if (!DOC_TYPE.equalsIgnoreCase(key)) {
                SearchCondition searchCondition = new SearchCondition();
                SearchField searchField = searchParams.buildSearchField(map.get(DOC_TYPE), searchFieldsMap.get(key), map.get(key));
                searchCondition.setSearchField(searchField);
                searchParams.getSearchConditions().add(searchCondition);
            }
        }
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(map.get(DOC_TYPE), "id"));
        return searchParams;
    }

    @Override
    public String findHoldings(Map<String, String> map) {
        List<String> holdingIds = new ArrayList<>();
        map.put(DOC_TYPE, "holdings");
        SearchParams searchParams = buildSearchParamsFromMap(map);
        SearchResponse searchResponse = search(searchParams);
        for (SearchResult searchResult : searchResponse.getSearchResults()) {
            for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                holdingIds.add(searchResultField.getFieldValue());
            }
        }
        if (!CollectionUtils.isEmpty(holdingIds)) {
            return holdingIds.get(0);
        }
        return null;
    }

    @Override
    public String findItem(Map<String, String> map) {
        List<String> itemIds = new ArrayList<>();
        map.put(DOC_TYPE, "item");
        SearchParams searchParams = buildSearchParamsFromMap(map);
        SearchResponse searchResponse = search(searchParams);
        for (SearchResult searchResult : searchResponse.getSearchResults()) {
            for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                itemIds.add(searchResultField.getFieldValue());
            }
        }
        if (!CollectionUtils.isEmpty(itemIds)) {
            return itemIds.get(0);
        }
        return null;
    }

    @Override
    public String findHoldingsTree(Map<String, String> map) {
        return findHoldings(map);
    }

    @Override
    public String findBibTree(Map<String, String> map) {
        return findBib(map);
    }

}
