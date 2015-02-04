package org.kuali.ole.ingest.function;

import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.ingest.util.OCLCUtil;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.framework.engine.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/10/12
 * Time: 1:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class OCLCFunction implements Function {

    private DocstoreClientLocator docstoreClientLocator;

    @Override
    public Object invoke(Object... arguments) {
        DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        String existingDocstoreField = (String)(arguments[0]);
        String oclc = (String)(arguments[1]);
        if(oclc != null){
            String normalizedOclc = null;
            try{
                normalizedOclc = new OCLCUtil().normalizedOclc(oclc);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
          //  List list = getDiscoveryHelperService().getResponseFromSOLR(existingDocstoreField, normalizedOclc);

            List list=new ArrayList<>();
            SearchParams searchParams=new  SearchParams();
            searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.BIB.getCode(),"035a",normalizedOclc), "AND"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "bibIdentifier"));
            try {
                SearchResponse searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                List<SearchResult>  searchResults=searchResponse.getSearchResults();
                if(searchResults.size() >=1){
                    for (SearchResult searchResult:searchResults){
                        HashMap<String, Object> bibMap=new HashMap<>();
                        for(SearchResultField searchResultField:searchResult.getSearchResultFields()){
                            if(searchResultField.getFieldValue()!=null && !searchResultField.getFieldValue().isEmpty() && searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier") ){
                                bibMap.put(OLEConstants.BIB_UNIQUE_ID,searchResultField.getFieldValue());
                            }
                        }
                        list.add(bibMap);
                    }
                }
            }catch(Exception ex){
                throw new RuntimeException(ex);

            }
            if(list.size() >= 1){
                dataCarrierService.addData(OLEConstants.BIB_INFO_LIST_FROM_SOLR_RESPONSE, list);
                return true;
            }
        }
        return false;
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
