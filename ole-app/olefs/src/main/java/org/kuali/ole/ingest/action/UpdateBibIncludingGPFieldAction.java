package org.kuali.ole.ingest.action;

import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.ingest.pojo.OverlayOption;
import org.kuali.ole.ingest.pojo.ProfileAttributeBo;
import org.kuali.ole.pojo.edi.LineItemOrder;
import org.kuali.ole.service.OleOverlayActionService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.framework.engine.Action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: premkb
 * Date: 12/11/12
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */

public class UpdateBibIncludingGPFieldAction implements Action {
    private DataCarrierService dataCarrierService;


    private OleOverlayActionService oleOverlayActionService;


    private DocstoreClientLocator docstoreClientLocator;

    /**
     *  This will update the existing Bib and Instance record ignoring the Globally Protected field.
     * @param executionEnvironment
     */
    @Override
    public void execute(ExecutionEnvironment executionEnvironment) {
        dataCarrierService = getDataCarrierService();
        oleOverlayActionService = getOleOverlayActionService();
        List<ProfileAttributeBo> profileAttributesList = (List<ProfileAttributeBo>) dataCarrierService.getData(OLEConstants.PROFILE_ATTRIBUTE_LIST);
        List<OverlayOption> overlayOptionList = (List<OverlayOption>) dataCarrierService.getData(OLEConstants.OVERLAY_OPTION_LIST);
       // List<OverlayLookupAction> overlayLookupActionList = (List<OverlayLookupAction>) dataCarrierService.getData(OLEConstants.OVERLAY_LOOKUP_ACTION_LIST);
        LineItemOrder lineItemOrder = (LineItemOrder) dataCarrierService.getData(OLEConstants.REQUEST_LINE_ITEM_ORDER_RECORD);
        String profileName = (String)dataCarrierService.getData(OLEConstants.PROFILE_NM);
        HashMap<String,String> uuids = new HashMap<String, String>();
        BibMarcRecord newBibMarcRecord = (BibMarcRecord) dataCarrierService.getData(OLEConstants.REQUEST_BIB_RECORD);
        try {
            List bibInfoList = (List) dataCarrierService.getData(OLEConstants.BIB_INFO_LIST_FROM_SOLR_RESPONSE);
            String existingBibUUID = null;
            for (Iterator iterator = bibInfoList.iterator(); iterator.hasNext(); ) {
                Map map = (Map) iterator.next();
                if (map.containsKey(OLEConstants.BIB_UNIQUE_ID)) {
                    existingBibUUID = (String) map.get(OLEConstants.BIB_UNIQUE_ID);
                    uuids.put(OLEConstants.BIBlIOGRAPHICUUID,existingBibUUID);
                    break;
                }
            }

            SearchParams searchParams=new  SearchParams();
            searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.BIB.getCode(),"id",existingBibUUID), "AND"));
           /* searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.BIB.getCode(),"bibIdentifier",existingBibUUID), "AND"));*/
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "holdingsIdentifier"));
            try {
                SearchResponse searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                List<SearchResult>  searchResults=searchResponse.getSearchResults();
                if (searchResults.size()>0 ){
                    for(SearchResultField searchResultField:searchResults.get(0).getSearchResultFields()){
                        if(searchResultField.getFieldName()!=null && !searchResultField.getFieldName().isEmpty() && searchResultField.getFieldName().equalsIgnoreCase("holdingsIdentifier") ){
                            uuids.put(OLEConstants.OVERLAY_HOLDINGUUID, searchResultField.getFieldValue());
                        }
                        break;
                    }
                }
            }
            catch(Exception ex){
                throw new RuntimeException(ex);
            }
            Bib bib=getDocstoreClientLocator().getDocstoreClient().retrieveBib(existingBibUUID);
            String bibContent=bib.getContent();
            BibMarcRecordProcessor recordProcessor = new BibMarcRecordProcessor();
            BibMarcRecords bibMarcRecords = recordProcessor.fromXML(bibContent);
            BibMarcRecord oldBibMarcRecord=bibMarcRecords.getRecords().get(0);
            oleOverlayActionService.updateRecordIncludingGPF(uuids, oldBibMarcRecord, newBibMarcRecord, lineItemOrder,null, overlayOptionList,profileName);
            executionEnvironment.getEngineResults().setAttribute(OLEConstants.UPDATE_BIB_INCLUDING_GPF, true);
        } catch (Exception e) {
            e.printStackTrace();
            executionEnvironment.getEngineResults().setAttribute(OLEConstants.UPDATE_BIB_INCLUDING_GPF, null);
        }
    }
    /**
     *   This method simulate the executionEnvironment.
     * @param executionEnvironment
     */

    @Override
    public void executeSimulation(ExecutionEnvironment executionEnvironment) {
        execute(executionEnvironment);
    }

    protected DataCarrierService getDataCarrierService() {
        if(dataCarrierService == null){
            return GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        }
        return dataCarrierService;
    }


    public OleOverlayActionService getOleOverlayActionService() {
        if(null==oleOverlayActionService){
            oleOverlayActionService = GlobalResourceLoader.getService(OLEConstants.OVERLAY_ACTION_SERVICE);
        }
        return oleOverlayActionService;
    }
    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return  SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }


}
