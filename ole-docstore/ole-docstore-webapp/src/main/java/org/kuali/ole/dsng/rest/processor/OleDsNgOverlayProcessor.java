package org.kuali.ole.dsng.rest.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.response.BibResponse;
import org.kuali.ole.docstore.common.response.HoldingsResponse;
import org.kuali.ole.docstore.common.response.ItemResponse;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.dao.BibDAO;
import org.kuali.ole.dsng.dao.HoldingDAO;
import org.kuali.ole.dsng.dao.ItemDAO;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;
import org.kuali.ole.dsng.rest.handler.bib.CreateBibHandler;
import org.kuali.ole.dsng.rest.handler.bib.UpdateBibHandler;
import org.kuali.ole.dsng.rest.handler.holdings.CreateHoldingsHandler;
import org.kuali.ole.dsng.rest.handler.holdings.UpdateHoldingsHandler;
import org.kuali.ole.dsng.rest.handler.items.CreateItemHandler;
import org.kuali.ole.dsng.rest.handler.items.UpdateItemHandler;
import org.kuali.ole.dsng.util.OleDsHelperUtil;
import org.marc4j.marc.Record;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 12/8/2015.
 */
public class OleDsNgOverlayProcessor extends OleDsHelperUtil implements DocstoreConstants {
    @Autowired
    BibDAO bibDAO;

    @Autowired
    HoldingDAO holdingDAO;

    @Autowired
    ItemDAO itemDAO;

    private List<Handler> bibHandlers;
    private List<Handler> holdingHandlers;
    private List<Handler> itemHandlers;

    public List<Handler> getBibHandlers() {
        if (null == bibHandlers) {
            bibHandlers = new ArrayList<Handler>();
            bibHandlers.add(new CreateBibHandler());
            bibHandlers.add(new UpdateBibHandler());
        }
        return bibHandlers;
    }

    public void setBibHandlers(List<Handler> bibHandlers) {
        this.bibHandlers = bibHandlers;
    }

    public List<Handler> getHoldingHandlers() {
        if (null == holdingHandlers) {
            holdingHandlers = new ArrayList<Handler>();
            holdingHandlers.add(new CreateHoldingsHandler());
            holdingHandlers.add(new UpdateHoldingsHandler());
        }
        return holdingHandlers;
    }

    public void setHoldingHandlers(List<Handler> holdingHandlers) {
        this.holdingHandlers = holdingHandlers;
    }

    public List<Handler> getItemHandlers() {
        if(null == itemHandlers) {
            itemHandlers = new ArrayList<Handler>();
            itemHandlers.add(new CreateItemHandler());
            itemHandlers.add(new UpdateItemHandler());
        }
        return itemHandlers;
    }

    public void setItemHandlers(List<Handler> itemHandlers) {
        this.itemHandlers = itemHandlers;
    }

    public String processBibAndHoldingsAndItems(String jsonBody) {
        String response = "{}";
        OleNGBibImportResponse oleNGBibImportResponse = new OleNGBibImportResponse();
        List<BibResponse> bibResponses = new ArrayList<BibResponse>();
        Map<String, SolrInputDocument> solrInputDocumentMap = new HashedMap();
        try {
            JSONArray requestJsonArray = new JSONArray(jsonBody);
            Exchange exchange = new Exchange();
            for (int index = 0; index < requestJsonArray.length(); index++) {
                JSONObject requestJsonObject = requestJsonArray.getJSONObject(index);

                BibResponse bibResponse = new BibResponse();
                String valueOf001 = requestJsonObject.getString(OleNGConstants.TAG_001);
                bibResponse.setValueOf001(valueOf001);


                String ops = requestJsonObject.getString(OleNGConstants.OPS);

                processBib(requestJsonObject, exchange, ops);

                BibRecord bibRecord = (BibRecord) exchange.get(OleNGConstants.BIB);

                if (null != bibRecord) {
                    solrInputDocumentMap = getBibIndexer().getInputDocumentForBib(bibRecord, solrInputDocumentMap);
                    if(requestJsonObject.has(OleNGConstants.ID)){
                        bibResponse.setOperation(OleNGConstants.UPDATED);
                    } else {
                        bibResponse.setOperation(OleNGConstants.CREATED);
                    }
                    bibResponse.setBibId(bibRecord.getUniqueIdPrefix() + "-" + bibRecord.getBibId());

                    solrInputDocumentMap = prepareHoldingsForSolr(solrInputDocumentMap, exchange);
                    solrInputDocumentMap = prepareItemsForSolr(solrInputDocumentMap, exchange);

                    bibResponse.setHoldingsResponses(prepareHoldingsResponse(exchange));

                    exchange.remove(OleNGConstants.BIB_UPDATED);
                    exchange.remove(OleNGConstants.HOLDINGS_CREATED);
                    exchange.remove(OleNGConstants.HOLDINGS_UPDATED);
                    exchange.remove(OleNGConstants.ITEMS_CREATED);
                    exchange.remove(OleNGConstants.ITEMS_UPDATED);
                }
                bibResponses.add(bibResponse);
            }

            List<SolrInputDocument> solrInputDocuments = getBibIndexer().getSolrInputDocumentListFromMap(solrInputDocumentMap);
            getBibIndexer().commitDocumentToSolr(solrInputDocuments);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        oleNGBibImportResponse.setBibResponses(bibResponses);

        try {
            response = getObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(oleNGBibImportResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private List<HoldingsResponse> prepareHoldingsResponse(Exchange exchange) {
        List<HoldingsResponse> holdingsResponses = new ArrayList<HoldingsResponse>();
        List holdingRecordsToCreate = (List) exchange.get(OleNGConstants.HOLDINGS_CREATED);
        holdingsResponses.addAll(holdingsResponse(exchange, holdingRecordsToCreate, OleNGConstants.CREATED));

        List holdingRecordsToUpdate = (List) exchange.get(OleNGConstants.HOLDINGS_UPDATED);
        holdingsResponses.addAll(holdingsResponse(exchange, holdingRecordsToUpdate, OleNGConstants.UPDATED));

        return holdingsResponses;
    }

    private List<HoldingsResponse> holdingsResponse(Exchange exchange, List holdingRecords, String operation) {
        List<HoldingsResponse> holdingsResponses = new ArrayList<HoldingsResponse>();
        if(CollectionUtils.isNotEmpty(holdingRecords)) {
            for (Iterator iterator = holdingRecords.iterator(); iterator.hasNext(); ) {
                HoldingsResponse holdingsResponse = new HoldingsResponse();
                HoldingsRecord holdingsRecord = (HoldingsRecord) iterator.next();
                holdingsResponse.setOperation(operation);
                holdingsResponse.setHoldingsId(holdingsRecord.getUniqueIdPrefix() + "-" + holdingsRecord.getHoldingsId());
                holdingsResponse.setItemResponses(prepareItemsResponse(holdingsRecord,exchange));
                holdingsResponses.add(holdingsResponse);
            }
        }
        return holdingsResponses;
    }

    private List<ItemResponse> prepareItemsResponse(HoldingsRecord holdingsRecord, Exchange exchange) {
        List<ItemResponse> itemResponses = new ArrayList<ItemResponse>();

        List itemRecordsToCreate = (List) exchange.get(OleNGConstants.ITEMS_CREATED);
        itemResponses.addAll(itemResponse(holdingsRecord, itemRecordsToCreate, OleNGConstants.CREATED));

        List itemRecordsToUpdate = (List) exchange.get(OleNGConstants.ITEMS_UPDATED);
        itemResponses.addAll(itemResponse(holdingsRecord, itemRecordsToUpdate, OleNGConstants.UPDATED));

        return itemResponses;
    }

    private List<ItemResponse> itemResponse(HoldingsRecord holdingsRecord, List itemRecords, String operation) {
        List<ItemResponse> itemResponses = new ArrayList<ItemResponse>();
        if (CollectionUtils.isNotEmpty(itemRecords)) {
            for (Iterator iterator = itemRecords.iterator(); iterator.hasNext(); ) {
                ItemRecord itemRecord = (ItemRecord) iterator.next();
                if (itemRecord.getHoldingsId().equalsIgnoreCase(holdingsRecord.getHoldingsId())) {
                    ItemResponse itemResponse = new ItemResponse();
                    itemResponse.setOperation(operation);
                    itemResponse.setItemId(itemRecord.getUniqueIdPrefix() + "-" + itemRecord.getItemId());
                    itemResponses.add(itemResponse);
                }
            }
        }
        return itemResponses;
    }

    private Map<String, SolrInputDocument> prepareHoldingsForSolr(Map<String, SolrInputDocument> solrInputDocumentMap, Exchange exchange) {
        List<HoldingsRecord> holdingsRecordsToUpdate = (List<HoldingsRecord>) exchange.get(OleNGConstants.HOLDINGS_UPDATED);
        List<HoldingsRecord> holdingsRecordsToCreate = (List<HoldingsRecord>) exchange.get(OleNGConstants.HOLDINGS_CREATED);

        List<HoldingsRecord> finalHoldingsForSolr = new ArrayList<HoldingsRecord>();
        if(CollectionUtils.isNotEmpty(holdingsRecordsToUpdate)){
            finalHoldingsForSolr.addAll(holdingsRecordsToUpdate);
        }
        if(CollectionUtils.isNotEmpty(holdingsRecordsToCreate)){
            finalHoldingsForSolr.addAll(holdingsRecordsToCreate);
        }

        solrInputDocumentMap = prepareSolrInputDocumentsForHoldings(solrInputDocumentMap, finalHoldingsForSolr);
        return solrInputDocumentMap;
    }

    private Map<String, SolrInputDocument> prepareItemsForSolr(Map<String, SolrInputDocument> solrInputDocumentMap, Exchange exchange) {
        List<ItemRecord> itemRecordsToUpdate = (List<ItemRecord>) exchange.get(OleNGConstants.ITEMS_UPDATED);
        List<ItemRecord> itemRecordsToCreate = (List<ItemRecord>) exchange.get(OleNGConstants.ITEMS_CREATED);

        List<ItemRecord> finalItemsForSolr = new ArrayList<ItemRecord>();
        if(CollectionUtils.isNotEmpty(itemRecordsToUpdate)){
            finalItemsForSolr.addAll(itemRecordsToUpdate);
        }
        if(CollectionUtils.isNotEmpty(itemRecordsToCreate)){
            finalItemsForSolr.addAll(itemRecordsToCreate);
        }

        solrInputDocumentMap = prepareSolrInputDocumentsForItems(solrInputDocumentMap, finalItemsForSolr);
        return solrInputDocumentMap;
    }

    private Map<String, SolrInputDocument> prepareSolrInputDocumentsForHoldings(Map<String, SolrInputDocument> solrInputDocumentMap, List<HoldingsRecord> holdingsRecordsToUpdate) {
        if (CollectionUtils.isNotEmpty(holdingsRecordsToUpdate)) {
            for (Iterator<HoldingsRecord> iterator = holdingsRecordsToUpdate.iterator(); iterator.hasNext(); ) {
                HoldingsRecord holdingsRecord = iterator.next();
                solrInputDocumentMap = getHoldingIndexer().getInputDocumentForHoldings(holdingsRecord, solrInputDocumentMap);
            }
        }
        return solrInputDocumentMap;
    }

    private Map<String, SolrInputDocument> prepareSolrInputDocumentsForItems(Map<String, SolrInputDocument> solrInputDocumentMap, List<ItemRecord> itemRecordsToUpdate) {
        if (CollectionUtils.isNotEmpty(itemRecordsToUpdate)) {
            for (Iterator<ItemRecord> iterator = itemRecordsToUpdate.iterator(); iterator.hasNext(); ) {
                ItemRecord itemRecord = iterator.next();
                solrInputDocumentMap = getItemIndexer().getInputDocumentForItem(itemRecord, solrInputDocumentMap);
            }
        }
        return solrInputDocumentMap;
    }

    private void processBib(JSONObject requestJsonObject, Exchange exchange, String ops) {
        for (Iterator<Handler> iterator = getBibHandlers().iterator(); iterator.hasNext(); ) {
            Handler handler = iterator.next();
            if (handler.isInterested(ops)) {
                handler.setBibDAO(bibDAO);
                handler.setHoldingDAO(holdingDAO);
                handler.setItemDAO(itemDAO);
                handler.setBusinessObjectService(getBusinessObjectService());
                handler.process(requestJsonObject, exchange);
            }
        }
    }

    public String getStringValueFromJsonObject(JSONObject jsonObject, String key) {
        String returnValue = null;
        try {
            returnValue = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnValue;
    }
}
