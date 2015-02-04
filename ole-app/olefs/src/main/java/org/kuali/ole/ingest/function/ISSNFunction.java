package org.kuali.ole.ingest.function;

import org.apache.log4j.Logger;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.ingest.util.ISSNUtil;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.framework.engine.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/6/12
 * Time: 6:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class ISSNFunction implements Function {

    private DocstoreClientLocator docstoreClientLocator;
    private static final Logger LOG = Logger.getLogger(ISSNFunction.class);

    @Override
    public Object invoke(Object... arguments) {
        DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        String existingDocstoreField = (String)(arguments[0]);
        String issn = (String)(arguments[1]);
        LOG.info(" ------------------> issn ------------------> " + issn);
        if(issn != null) {
            boolean normalizedISSN = false;
            try {
                ISSNUtil validIssn = new ISSNUtil(issn);
                normalizedISSN = validIssn.hasCorrectChecksum();
            } catch (Exception e) {
                e.printStackTrace();
            }
            List list = new ArrayList();
            if(normalizedISSN) {
                //list = getDiscoveryHelperService().getResponseFromSOLR(existingDocstoreField, issn);
               // List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
                SearchParams searchParams=new  SearchParams();
                searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.BIB.getCode(),"ISSN",issn), "AND"));
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
                                if(searchResultField.getFieldValue()!=null && !searchResultField.getFieldValue().isEmpty() && searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")){
                                    bibMap.put(OLEConstants.BIB_UNIQUE_ID,searchResultField.getFieldValue());
                                }
                            }
                            list.add(bibMap);
                        }
                    }
                }catch(Exception ex){
                    throw new RuntimeException(ex);

                }

            }
            if(list != null && list.size() >= 1){
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
