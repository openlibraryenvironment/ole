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
package org.kuali.ole.docstore.discovery.service;

import org.kuali.ole.docstore.discovery.model.SearchCondition;
import org.kuali.ole.docstore.discovery.model.SearchParams;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;

public class DiscoveryServiceImpl
        implements DiscoveryService {
    private static final Logger LOG = LoggerFactory.getLogger(DiscoveryServiceImpl.class);
    private static String docSearchUrl = null;
    private static DiscoveryService discoveryService = null;

    private DiscoveryServiceImpl() {
        LOG.debug("DiscoveryServiceImpl ");
        init();
    }

    public static DiscoveryService getInstance() {
        if (null == discoveryService) {
            discoveryService = new DiscoveryServiceImpl();
        }
        return discoveryService;
    }

    protected void init() {
        LOG.debug("DiscoveryServiceImpl init ");
        if(ConfigContext.getCurrentContextConfig()!=null){
            setDocSearchUrl(ConfigContext.getCurrentContextConfig().getProperty("docSearchURL"));
        }
    }

    public String search(SearchParams searchParams) {
        String response = "";
        LOG.debug("in search1");
        String searchType = searchParams.getSearchType();
        LOG.debug("searchType " + searchType);
        StringBuffer query = new StringBuffer();
        query.append(SolrServerManager.getInstance().getSolrCoreURL() + "/select");
        if (searchType.equals(SEARCH_TYPE_MORE_FACET)) {
            String facetPrefix = searchParams.getFacetPrefix();
            String facetPageSize = searchParams.getFacetPageSize();
            String facetSort = searchParams.getFacetSort();
            String facetOffset = searchParams.getFacetOffset();
            buildInitialQuery(query, searchParams);
            query.append("&facet=true");

            query.append("&facet.mincount=1");

            query.append("&facet.prefix=" + facetPrefix);

            query.append("&facet.offset=" + facetOffset);

            query.append("&facet.limit=" + facetPageSize);

            query.append("&facet.sort=" + facetSort);

            query.append("&facet.field=" + searchParams.getFacetField());

            Map<String, String> facetTermsMap = searchParams.getFacetTermsMap();
            query.append(buildFilterQuery(facetTermsMap));
        } else {
            if (searchParams.getSearchFieldsList() != null && searchParams.getSearchFieldsList().size() <= 0 && (
                    searchType != null && !searchType.equalsIgnoreCase(SEARCH_TYPE_LINK))) {
                buildInitialQuery(query, searchParams);
                searchParams.setSearchTerms("");
            } else if (searchType != null && (searchType.equalsIgnoreCase(SEARCH_TYPE_ADVANCED)
                    || searchType.equalsIgnoreCase(SEARCH_TYPE_FACET) || searchType
                    .equalsIgnoreCase(SEARCH_TYPE_FACET_DELETE))) {
                buildInitialQuery(query, searchParams);
                if (searchParams.getSearchFieldsList().size() > 0) {
                    query.append("AND(");
                }
                query.append(buildQueryWithSearchParameters(searchParams.getSearchFieldsList()));
                LOG.debug("query for search terms............." + query.toString());
                String searchTerms = buildQueryWithSearchParameters(searchParams.getSearchFieldsList())
                        .replaceAll("_search", "");
                try {
                    searchTerms = URLDecoder.decode(searchTerms, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                searchParams.setSearchTerms(searchTerms.substring(0, searchTerms.lastIndexOf(")")));
            }
            if (SEARCH_TYPE_LINK.equals(searchType)) {
                query.append("?q=id:");
                query.append(searchParams.getLinkValue());
            }
            query.append("&rows=" + searchParams.getResultPageSize());
            String docType = searchParams.getDocType();
            LOG.info("docType-->" + docType);

            query.append("&start=" + searchParams.getResultFromIndex());
            List<String> facetList = new ArrayList<String>();
            facetList.add(AUTHOR_FACET);
            facetList.add(SUBJECT_FACET);
            facetList.add(FORMAT_FACET);
            facetList.add(LANGUAGE_FACET);
            facetList.add(PUBLICATION_DATE_FACET);
            facetList.add(GENRE_FACET);
            query.append(buildQueryWithFacetParameters(facetList, 1, 10));
            LOG.debug("sort terms buildQueryWithSortFields" + searchParams.getSortByTerms());
            LOG.debug("sort Field buildQueryWithSortFields" + searchParams.getSortField());
            LOG.debug("sort Order buildQueryWithSortFields" + searchParams.getSortOrder());
            query.append(buildQueryWithSortFields(searchParams.getSortField(), searchParams.getSortOrder()));
            query.append(buildQueryWithFieldListParameters(searchParams.getFieldList()));
            Map<String, String> facetTermsMap = searchParams.getFacetTermsMap();
            query.append(buildFilterQuery(facetTermsMap));
            String holdingFields
                    = "LocalId_display,Uri_display,HoldingsNote_display,ReceiptStatus_display,CallNumber_display,CallNumberPrefix_display,CallNumberType_display,ClassificationPart_display,Location_display,ShelvingSchemeCode_display";
            String itemFields
                    = "LocalId_display,ItemBarcode_display,ItemTypeFullValue_display,VendorLineItemIdentifier_display,ShelvingOrderValue_display,ShelvingSchemeValue_display,PurchaseOrderLineItemIdentifier_display,CopyNumber_display,Enumeration_display,Chronology_display,VolumeNumber_display,ItemStatus_display";
            String instanceFields = "LocalId_display,Source_display";
            String patronFields
                    = "RecordNumber_display,BeginDate_display,Name_display,BorrowerType_display,BarCodeNumber_display,BarCodeStatus_display,";
            String onixplFields
                    = "ContractNumber_display,Title_display,Method_display,Status_display,Type_display,Licensor_display,Licensee_display";
            String licenseBinaryFields
                    = "Name_display,FileName_display,DateUploaded_display,Owner_display,Notes_display";
            String eInstanceFields = "AccessStatus_display,Imprint_display,Platform_display,StatisticalSearchingCodeValue_display,EResource_name_display";
            //query.append("&fl=" + fieldList);
            query.append(
                    "&fl=LocalId_display,Title_display,Author_display,Publisher_display,Description_display,Subject_display,Location_display,PublicationDate_display,Format_display,DocType,DocFormat,id,ItemLinks,BibliographicLinks,Barcode_display,instanceIdentifier,holdingsIdentifier,itemIdentifier,bibIdentifier,staffOnlyFlag"
                    /*
                                    + "," + "245a,245b"
                                    + "," + "100a,110a,111a,700a,710a,711a,800a,810a,811a,400a,410a,411a"
                                    + "," + "600a,610a,611a,630a,650a,651a,653a"
                                    + "," + "505a"
                                    + "," + "856u"
                                    + "," + "260b"
                    */ + "," + holdingFields + "," + itemFields + "," + instanceFields + "," + patronFields
                            + "," + onixplFields + "," + licenseBinaryFields + "," + eInstanceFields);
        }
        LOG.debug("query---> " + query);
        try {
            String queryStr = query.toString().replaceAll(" ", "+");

            searchParams.setSearchQuery(queryStr);
            URL url = new URL(queryStr);
            URLConnection urlc = null;
            urlc = url.openConnection();
            urlc.setDoOutput(true);
            urlc.setAllowUserInteraction(false);
            BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            br.close();
            response = sb.toString();
        } catch (Exception e) {
            LOG.error("Exception:" + e.getMessage(), e);
        }
        return response;
    }

    public String getFieldList(String docType) {
        String fieldList = "";
        String holdingFields = HOLDINGS_FIELDS;
        String itemFields = ITEM_FIELDS;
        String instanceFields = INSTANCE_FIELDS;
        String bibFields = BIB_FIELDS;

        if (docType.equalsIgnoreCase(BIBLIOGRAPHIC)) {
            fieldList = bibFields;

        } else if (docType.equalsIgnoreCase(INSTANCE)) {
            fieldList = instanceFields;

        } else if (docType.equalsIgnoreCase(HOLDINGS)) {
            fieldList = holdingFields;

        } else if (docType.equalsIgnoreCase(ITEM)) {
            fieldList = itemFields;
        }
        return fieldList;
    }


    public static void setDocSearchUrl(String docSearchUrl) {
    }

    public static String getDocSearchUrl() {
        return docSearchUrl;
    }

    public String buildQuery(SearchParams searchParams) {
        StringBuffer query = new StringBuffer();
        String searchType = searchParams.getSearchType();
        query.append(SolrServerManager.getInstance().getSolrCoreURL() + "/select");
        buildInitialQuery(query, searchParams);
        if (searchParams.getSearchFieldsList().size() > 0) {
            query.append("AND(");
        }
        query.append(buildQueryWithSearchParameters(searchParams.getSearchFieldsList()));
        if (searchParams.getResultPageSize() != null) {
            query.append("&rows=" + searchParams.getResultPageSize());
        }
        if (searchParams.getResultFromIndex() != null) {
            query.append("&start=" + searchParams.getResultFromIndex());
        }
        List<String> facetFieldList = searchParams.getFacetFieldList();
        query.append(buildQueryWithFacetParameters(facetFieldList, 1, 10));
        query.append(buildQueryWithSortFields(searchParams.getSortField(), searchParams.getSortOrder()));
        query.append(buildQueryWithFieldListParameters(searchParams.getFieldList()));
        Map<String, String> facetTermsMap = searchParams.getFacetTermsMap();
        query.append(buildFilterQuery(facetTermsMap));

        return query.toString();
    }


    public String buildQueryWithSearchParameters(List<SearchCondition> searchFieldsList) {
        SearchCondition docSearchFieldsDTO = null;
        StringBuffer queryStringbuffer = new StringBuffer();
        StringBuffer highlightBuffer = new StringBuffer("&hl.fl=");
        if (searchFieldsList != null && searchFieldsList.size() > 0) {

            for (int i = 0; i < searchFieldsList.size(); i++) {
                int searchScopeAddLimit = i;
                docSearchFieldsDTO = searchFieldsList.get(i);
                if (docSearchFieldsDTO.getOperator() != null) {
                    //queryStringbuffer.append(docSearchFieldsDTO.getOperator());
                }
                queryStringbuffer.append("(");
                if (docSearchFieldsDTO.getDocField().equalsIgnoreCase("all")) {
                    queryStringbuffer.append("all_text");
                    highlightBuffer.append("*");

                } else {
                    queryStringbuffer.append(docSearchFieldsDTO.getDocField());
                    highlightBuffer.append(docSearchFieldsDTO.getDocField());


                    if (i != searchFieldsList.size() - 1) {
                        highlightBuffer.append(",");
                    }
                }
                queryStringbuffer.append(":");
                String searchScope = docSearchFieldsDTO.getSearchScope();
                String searchText = docSearchFieldsDTO.getSearchText();
                String searchOperator = docSearchFieldsDTO.getOperator();
//                searchText = searchText.toLowerCase();
                LOG.debug("searchText-->" + searchText);
//                searchText = searchText.replaceAll("[~!(){}\\[\\]':-]+"," ");
//                String modifiedSearchText = searchText.replaceAll("[~!(){}<>\\[\\]':\\-\\\\^]+", " ").toLowerCase();
                String modifiedSearchText = getModifiedText(searchText);
                String searchTextVal = null;
                if (modifiedSearchText.length() > 0) {
                    queryStringbuffer.append("(");
                    if (searchScope.equalsIgnoreCase("AND")) {
                        modifiedSearchText = modifiedSearchText.replaceAll("\\s+", " ");
                        searchTextVal = modifiedSearchText.trim().replace(" ", " AND ");
                    } else if (searchScope.equalsIgnoreCase("OR")) {
                        modifiedSearchText = modifiedSearchText.replaceAll("\\s+", " ");
                        searchTextVal = modifiedSearchText.trim().replace(" ", " OR ");
                    } else if (searchScope.equalsIgnoreCase("phrase")) {
                        searchTextVal = "\"" + searchText + "\"";
                    }
                    try {
                        searchTextVal = URLEncoder.encode(searchTextVal, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    queryStringbuffer.append(searchTextVal);
                    LOG.debug("searchTextVal............" + searchTextVal + "........" + queryStringbuffer.toString());
                    queryStringbuffer.append(")");
                }
                queryStringbuffer.append(")");
                ++searchScopeAddLimit;
                if (searchScopeAddLimit != searchFieldsList.size()) {
                    queryStringbuffer.append(searchOperator);
                }
            }
            queryStringbuffer.append(")");
            String highLight = highlightBuffer.toString().replace("LocalId_search", "LocalId_display");
            queryStringbuffer.append(highLight);

            queryStringbuffer.append("&hl=true");
        }


        return queryStringbuffer.toString();
    }

    public String buildQueryWithFacetParameters(List<String> facetsParametersList, int facetMinCount, int facetLimit) {
        String facetFieldName = "";
        String queryWithFacetParameters = "";
        if (facetsParametersList != null) {
            StringBuffer facetsQueryStringbuffer = new StringBuffer();

            facetsQueryStringbuffer.append("&facet=true");

            facetsQueryStringbuffer.append("&facet.mincount=" + facetMinCount);

            facetsQueryStringbuffer.append("&");

            for (int i = 0; i < facetsParametersList.size(); i++) {
                facetFieldName = facetsParametersList.get(i);
                facetsQueryStringbuffer.append("facet.field=" + facetFieldName);
                facetsQueryStringbuffer.append("&");

            }
            queryWithFacetParameters = facetsQueryStringbuffer.substring(0, facetsQueryStringbuffer.length() - 1);
        }
        return queryWithFacetParameters;
    }


    public String buildQueryWithSortFields(String sortField, String sortOrder) {
        StringBuffer sortFieldsQuery = new StringBuffer();
        if (null != sortField) {
            sortFieldsQuery.append("&");
            sortFieldsQuery.append("sort=");
            sortFieldsQuery.append(sortField);
            if (null != sortOrder) {
                sortFieldsQuery.append(" ");
                sortFieldsQuery.append(sortOrder);
            }
        }
        return sortFieldsQuery.toString();
    }

    public String buildQueryWithFieldListParameters(List<String> fieldsList) {
        String queryWithFieldListParameters = "";
        if (fieldsList != null) {
            StringBuffer fieldsListQueryStringbuffer = new StringBuffer();
            fieldsListQueryStringbuffer.append("&");
            fieldsListQueryStringbuffer.append("fl=");
            for (int i = 0; i < fieldsList.size(); i++) {
                fieldsListQueryStringbuffer.append(fieldsList.get(i));
                fieldsListQueryStringbuffer.append(",");
            }
            queryWithFieldListParameters = fieldsListQueryStringbuffer
                    .substring(0, fieldsListQueryStringbuffer.length() - 1);
        }
        return queryWithFieldListParameters;
    }

    public static String convertListToStringFieldValues(Map<String, String> map) {
        StringBuffer sb = new StringBuffer();
        Set set = map.keySet();
        Iterator<String> ite = set.iterator();
        while (ite.hasNext()) {
            sb.append(ite.next());
            sb.append("|");
        }
        String str = sb.toString();
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public String buildFilterQuery(Map<String, String> facetTermsMap) {
        String filterQuery = "";
        int temp = 0;
        int tokenInt = 0;
        if ((null != facetTermsMap) && (facetTermsMap.size() > 0)) {

            String facetTerms = convertListToStringFieldValues(facetTermsMap);
            StringBuffer facetQueryTemp = new StringBuffer();
            facetQueryTemp.append("&terms=" + facetTerms);
            facetQueryTemp.append("&fq=");
            StringTokenizer sttoken = new StringTokenizer(facetTerms, "|");
            String token;
            while (sttoken.hasMoreElements()) {
                token = sttoken.nextToken();
                facetQueryTemp.append("(");
                facetQueryTemp.append(facetTermsMap.get(token));
                facetQueryTemp.append(":\"");
                facetQueryTemp.append(token);
                facetQueryTemp.append("\")");
                facetQueryTemp.append("AND");
            }
            if (facetQueryTemp.length() > 0) {
                filterQuery = (facetQueryTemp.toString().substring(0, facetQueryTemp.toString().length() - 3));
            }
        }
        return filterQuery;
    }


    /**
     * @param query
     * @param searchParams Usage: This method builds initial SOLR query with DocType and DocFormat as SolrParams
     */
    private void buildInitialQuery(StringBuffer query, SearchParams searchParams) {
        query.append("?q=");
        //query.append("(DocType:" + searchParams.getDocType() + ")");
        if (searchParams.getDocFormat().equalsIgnoreCase("marc")) {
            query.append("((DocType:" + searchParams.getDocType() + ")" + "OR(DocType:item))");
        } else {
            query.append("(DocType:" + searchParams.getDocType() + ")");
        }

        if (searchParams.getDocFormat() != null && !searchParams.getDocFormat().equalsIgnoreCase("all")) {
            if ("dublin".equals(searchParams.getDocFormat())) {
                searchParams.setDocFormat("dublin");
            }
            query.append("AND(DocFormat:" + searchParams.getDocFormat() + ")");
        }
    }

    private String getModifiedText(String searchText) {
        StringBuffer modifiedText = new StringBuffer();
        StringCharacterIterator stringCharacterIterator = new StringCharacterIterator(searchText);
        char character = stringCharacterIterator.current();
        while (character != CharacterIterator.DONE) {

            if (character == '\\') {
                modifiedText.append("\\\\");
            } else if (character == '?') {
                modifiedText.append("\\?");
            } else if (character == '*'  && StringUtils.isEmpty(modifiedText.toString())) {
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
                modifiedText.append("\\-");
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
                //the char is not a special one
                //add it to the result as is
                modifiedText.append(character);
            }
            character = stringCharacterIterator.next();
        }

        return modifiedText.toString().toLowerCase();
    }

}
