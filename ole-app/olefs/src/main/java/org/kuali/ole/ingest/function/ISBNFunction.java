package org.kuali.ole.ingest.function;

//import org.kuali.incubator.SolrRequestReponseHandler;

import org.apache.log4j.Logger;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;

import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.ingest.ISBNUtil;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.framework.engine.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ISBNFunction takes the request,normalize tha isbn and sets the bibRecordFromSOLRResponse into dataCarrierService
 */
public class ISBNFunction implements Function {

    private DocstoreClientLocator docstoreClientLocator;

    private static final Logger LOG = Logger.getLogger(ISBNFunction.class);

    /**
     *  This method takes the request and normalize the isbn and  sets the bibRecordFromSOLRResponse into dataCarrierService.
     * @param arguments
     * @return  Object
     */
    @Override
    public Object invoke(Object... arguments) {
        LOG.info(" -----------------> inside isbn function ------------> ");
        DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        String existingDocstoreField = (String)(arguments[0]);
        String isbn = (String)(arguments[1]);
        String normalizedISBN = null;
        try {
            normalizedISBN = new ISBNUtil().normalizeISBN(isbn);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //  List list = getDocstoreClientLocator().getResponseFromSOLR(existingDocstoreField, normalizedISBN);
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        SearchParams searchParams=new  SearchParams();
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.BIB.getCode(),"ISBN",normalizedISBN), "AND"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "bibIdentifier"));
        try {
            SearchResponse searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
            List<SearchResult>  searchResults=searchResponse.getSearchResults();
            LOG.info(" ---------------> list.size ------------> " + list.size());
            if(searchResults.size() >=1){
                LOG.info(" inside if condition of list --------------------> ");
                for (SearchResult searchResult:searchResults){
                    HashMap<String, Object> bibMap=new HashMap<>();
                    for(SearchResultField searchResultField:searchResult.getSearchResultFields()){
                        if(searchResultField.getFieldValue()!=null && !searchResultField.getFieldValue().isEmpty() && searchResultField.getFieldName().equalsIgnoreCase("bibidentifier") ){
                        bibMap.put(OLEConstants.BIB_UNIQUE_ID,searchResultField.getFieldValue());
                        }
                    }
                    list.add(bibMap);
            }
            dataCarrierService.addData(OLEConstants.BIB_INFO_LIST_FROM_SOLR_RESPONSE, list);
             return true;
            }
            return false;

        }
        catch(Exception ex){
        throw new RuntimeException(ex);
        }

        }


    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
        return  SpringContext.getBean(DocstoreClientLocator.class);
       }
        return docstoreClientLocator;
    }

    public void setDocstoreClientLocator(DocstoreClientLocator docstoreClientLocator) {
                this.docstoreClientLocator = docstoreClientLocator;
    }

}