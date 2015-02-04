package org.kuali.ole.service;

import org.apache.commons.lang.StringUtils;

import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.select.bo.OleAgreementSearch;
import org.kuali.ole.sys.context.SpringContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *OleAgreementSearchService performs search operation and return list of agreement related information.
 */
public class OleAgreementSearchService {
    private final String queryString = "(DocType:license AND DocFormat:onixpl)";

    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }


//    /**
//     *  This method returns the responses from Solr.
//     * @param queryField
//     * @param value
//     * @return  List
//     */
//    public List getResponseFromSOLR(String queryField, String value) {
//        String queryString = queryField + ":" + value;
//        return QueryServiceImpl.getInstance().retriveResults(queryString);
//    }

    /**
     *  This method returns list of agreement information based on search criteria.
     * @param searchCriteria
     * @return   List<OleAgreementSearch>
     */
    public List<OleAgreementSearch> getAgreementInformation(Map searchCriteria) {
        List<OleAgreementSearch> agreementSearchResults = new ArrayList<OleAgreementSearch>();
        OleAgreementSearch agreemetSearch;
        SearchResponse solrResponse = getSolrResponse(searchCriteria);

        for(SearchResult searchResult : solrResponse.getSearchResults()) {
            agreemetSearch = new OleAgreementSearch();
            for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                if (searchResultField.getFieldName().equalsIgnoreCase("Title_search")) {
                    agreemetSearch.setAgreementTitle(searchResultField.getFieldValue());
                }
                else if (searchResultField.getFieldName().equalsIgnoreCase("ContractNumber_search")) {
                    agreemetSearch.setContractNumber(searchResultField.getFieldValue());
                }
                else if (searchResultField.getFieldName().equalsIgnoreCase("Method_search")) {
                    agreemetSearch.setMethodName(searchResultField.getFieldValue());
                }
                else if (searchResultField.getFieldName().equalsIgnoreCase("Type_search")) {
                    agreemetSearch.setType(searchResultField.getFieldValue());
                }
                else if (searchResultField.getFieldName().equalsIgnoreCase("Status_search")) {
                    agreemetSearch.setStatus(searchResultField.getFieldValue());
                }
                else if (searchResultField.getFieldName().equalsIgnoreCase("id")) {
                    agreemetSearch.setUniqueId(searchResultField.getFieldValue());
                }
            }

            agreementSearchResults.add(agreemetSearch);

        }
        return agreementSearchResults;
    }

    /**
     *  This method returns the SolrResponse as List based on searchCriteria.
     * @param searchCriteria
     * @return  List
     */
    private SearchResponse getSolrResponse(Map searchCriteria) {
        String query = queryString;
        String key = null;
        String value = null;
        SearchParams searchParams = new SearchParams();
        if(!searchCriteria.isEmpty()) {
            int count = 0;
            if ((searchCriteria.containsKey("agreementTitle")) &&
                    searchCriteria.get("agreementTitle") != null & !searchCriteria.get("agreementTitle").equals("")) {
                SearchField searchField = searchParams.buildSearchField("license", "Title_search", (String) searchCriteria.get("agreementTitle"));
                searchParams.getSearchConditions().add(searchParams.buildSearchCondition("OR", searchField, "AND"));
//                query = query + " AND Title_search:" +searchCriteria.get("agreementTitle");

            }
            else {
                count ++;
            }
            if ((searchCriteria.containsKey("contractNumber")) &&
                        searchCriteria.get("contractNumber") != null & !searchCriteria.get("contractNumber").equals("")) {
                SearchField searchField = searchParams.buildSearchField("license", "ContractNumber_search", (String) searchCriteria.get("contractNumber"));
                searchParams.getSearchConditions().add(searchParams.buildSearchCondition("OR", searchField, "AND"));
//                query = query + " AND ContractNumber_search:" +searchCriteria.get("contractNumber");
            }
            else {
                count ++;
            }

            /*if ((searchCriteria.containsKey("licensee")) &&
                    searchCriteria.get("licensee") != null & !searchCriteria.get("licensee").equals("")){
                query = query + " AND Licensee_search:" +searchCriteria.get("licensee");
            }
            if ((searchCriteria.containsKey("licensor")) &&
                    searchCriteria.get("licensor") != null & !searchCriteria.get("licensor").equals("")) {
                query = query + " AND Licensor_search:" +searchCriteria.get("licensor");
            }*/
            //query = query+"&fl=id,Title_search,ContractNumber_search,Licensee_search,Licensor_search";
            if(searchCriteria.containsKey("uuid")) {
                SearchField searchField = searchParams.buildSearchField("license", "id", (String) searchCriteria.get("uuid"));
                searchParams.getSearchConditions().add(searchParams.buildSearchCondition("OR", searchField, "AND"));

//                query = query + "id:"+ searchCriteria.get("uuid");
            }
            else {
                count ++;
            }
            if( count > 0) {
                SearchField searchField = searchParams.buildSearchField("license", "DocFormat", "onixpl");
                searchParams.getSearchConditions().add(searchParams.buildSearchCondition("OR", searchField , "AND"));
            }
        }

        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("license", "Title_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("license", "ContractNumber_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("license", "Method_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("license", "Type_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("license", "Status_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("license", "id"));
        SearchResponse searchResponse = null;

        try {
            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return searchResponse;
    }


}
