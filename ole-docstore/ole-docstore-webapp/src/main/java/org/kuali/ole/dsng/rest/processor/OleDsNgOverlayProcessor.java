package org.kuali.ole.dsng.rest.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;
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
import org.kuali.ole.dsng.rest.handler.bib.DiscardBibHandler;
import org.kuali.ole.dsng.rest.handler.bib.UpdateBibHandler;
import org.kuali.ole.dsng.rest.handler.holdings.*;
import org.kuali.ole.dsng.rest.handler.holdings.CallNumberHandler;
import org.kuali.ole.dsng.rest.handler.holdings.CallNumberPrefixHandler;
import org.kuali.ole.dsng.rest.handler.holdings.CallNumberTypeHandler;
import org.kuali.ole.dsng.rest.handler.holdings.CopyNumberHandler;
import org.kuali.ole.dsng.rest.handler.items.*;
import org.kuali.ole.dsng.util.OleDsHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

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
    private List<HoldingsHandler> holdingMetaDataHandlers;
    private List<ItemHandler> itemMetaDataHandlers;

    public List<Handler> getBibHandlers() {
        if (null == bibHandlers) {
            bibHandlers = new ArrayList<Handler>();
            bibHandlers.add(new CreateBibHandler());
            bibHandlers.add(new UpdateBibHandler());
            bibHandlers.add(new DiscardBibHandler());
        }
        return bibHandlers;
    }

    public List<HoldingsHandler> getHoldingMetaDataHandlers() {
        if (null == holdingMetaDataHandlers) {
            holdingMetaDataHandlers = new ArrayList<HoldingsHandler>();
            holdingMetaDataHandlers.add(new HoldingsLocationHandler());
            holdingMetaDataHandlers.add(new CallNumberHandler());
            holdingMetaDataHandlers.add(new CallNumberTypeHandler());
            holdingMetaDataHandlers.add(new CallNumberPrefixHandler());
            holdingMetaDataHandlers.add(new CopyNumberHandler());
        }
        return holdingMetaDataHandlers;
    }

    public List<ItemHandler> getItemMetaDataHandlers() {
        if (null == itemMetaDataHandlers) {
            itemMetaDataHandlers = new ArrayList<ItemHandler>();
            itemMetaDataHandlers.add(new org.kuali.ole.dsng.rest.handler.items.CallNumberHandler());
            itemMetaDataHandlers.add(new org.kuali.ole.dsng.rest.handler.items.CallNumberPrefixHandler());
            itemMetaDataHandlers.add(new org.kuali.ole.dsng.rest.handler.items.CallNumberTypeHandler());
            itemMetaDataHandlers.add(new ChronologyHandler());
            itemMetaDataHandlers.add(new org.kuali.ole.dsng.rest.handler.items.CopyNumberHandler());
            itemMetaDataHandlers.add(new DonorCodeHandler());
            itemMetaDataHandlers.add(new DonorNoteHandler());
            itemMetaDataHandlers.add(new DonorPublicDisplayHandler());
            itemMetaDataHandlers.add(new EnumerationHandler());
            itemMetaDataHandlers.add(new ItemHoldingLocationHandler());
            itemMetaDataHandlers.add(new ItemBarcodeHandler());
            itemMetaDataHandlers.add(new ItemStatusHandler());
            itemMetaDataHandlers.add(new ItemTypeHandler());
            itemMetaDataHandlers.add(new ItemLocationHandler());
            itemMetaDataHandlers.add(new StatisticalSearchCodeHandler());
            itemMetaDataHandlers.add(new VendorLineItemIdHandler());
        }
        return itemMetaDataHandlers;
    }

    public void setBibHandlers(List<Handler> bibHandlers) {
        this.bibHandlers = bibHandlers;
    }

    public List<Handler> getHoldingHandlers() {
        if (null == holdingHandlers) {
            holdingHandlers = new ArrayList<Handler>();
            holdingHandlers.add(new CreateHoldingsHanlder());
            holdingHandlers.add(new UpdateHoldingsHandler());
        }
        return holdingHandlers;
    }

    public void setHoldingHandlers(List<Handler> holdingHandlers) {
        this.holdingHandlers = holdingHandlers;
    }

    public List<Handler> getItemHandlers() {
        if (null == itemHandlers) {
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
                JSONObject bibJSONDataObject = requestJsonArray.getJSONObject(index);

                BibResponse bibResponse = new BibResponse();
                String valueOf001 = bibJSONDataObject.getString(OleNGConstants.TAG_001);
                bibResponse.setValueOf001(valueOf001);

                String ops = bibJSONDataObject.getString(OleNGConstants.OPS);

                BibRecord bibRecord = prepareBib(bibJSONDataObject);
                exchange.add(OleNGConstants.BIB, bibRecord);

                List<HoldingsRecord> holdingsForUpdateOrCreate = prepareHoldingsRecord(bibRecord, bibJSONDataObject.getJSONObject(OleNGConstants.HOLDINGS), exchange);
                exchange.add(OleNGConstants.HOLDINGS, holdingsForUpdateOrCreate);

                List<ItemRecord> itemsForUpdateOrCreate = prepareItemsRecord(holdingsForUpdateOrCreate, bibJSONDataObject.getJSONObject(OleNGConstants.ITEM), exchange);
                exchange.add(OleNGConstants.ITEM, itemsForUpdateOrCreate);

                for (Iterator<Handler> iterator = getBibHandlers().iterator(); iterator.hasNext(); ) {
                    Handler bibHandler = iterator.next();
                    if (bibHandler.isInterested(ops)) {
                        bibHandler.setBibDAO(bibDAO);
                        bibHandler.process(bibJSONDataObject, exchange);
                    }
                }

                getBibIndexer().getInputDocumentForBib(bibRecord, solrInputDocumentMap);

                for (Iterator<Handler> iterator = getHoldingHandlers().iterator(); iterator.hasNext(); ) {
                    Handler holdingsHandler = iterator.next();
                    if (holdingsHandler.isInterested(ops)) {
                        holdingsHandler.setHoldingDAO(holdingDAO);
                        holdingsHandler.process(bibJSONDataObject, exchange);
                    }
                }

                for (Iterator<HoldingsRecord> holdingsRecordIterator = holdingsForUpdateOrCreate.iterator(); holdingsRecordIterator.hasNext(); ) {
                    HoldingsRecord holdingsRecord = holdingsRecordIterator.next();
                    getHoldingIndexer().getInputDocumentForHoldings(holdingsRecord, solrInputDocumentMap);
                }

                for (Iterator<Handler> iterator = getItemHandlers().iterator(); iterator.hasNext(); ) {
                    Handler itemHandler = iterator.next();
                    if (itemHandler.isInterested(ops)) {
                        itemHandler.setItemDAO(itemDAO);
                        itemHandler.process(bibJSONDataObject, exchange);
                    }
                }

                for (Iterator<ItemRecord> itemRecordIterator = itemsForUpdateOrCreate.iterator(); itemRecordIterator.hasNext(); ) {
                    ItemRecord itemRecord = itemRecordIterator.next();
                    getItemIndexer().getInputDocumentForItem(itemRecord, solrInputDocumentMap);
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

    private List<ItemRecord> prepareItemsRecord(List<HoldingsRecord> holdingsRecords, JSONObject itemJSON, Exchange exchange) {
        List<ItemRecord> itemRecords = new ArrayList<ItemRecord>();

        for (Iterator<HoldingsRecord> iterator = holdingsRecords.iterator(); iterator.hasNext(); ) {
            HoldingsRecord holdingsRecord = iterator.next();
            if (null == holdingsRecord.getHoldingsId()) {
                ItemRecord itemRecord = new ItemRecord();
                itemRecord.setHoldingsRecord(holdingsRecord);
                itemRecords.add(itemRecord);
                holdingsRecord.setItemRecords(itemRecords);
            } else {

                JSONObject matchPoints = null;
                HashMap map = null;
                try {
                    matchPoints = itemJSON.getJSONObject(OleNGConstants.MATCH_POINT);
                    map = getObjectMapper().readValue(matchPoints.toString(), new TypeReference<Map<String, String>>() {
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (Iterator<HoldingsRecord> holdingsRecordIterator = holdingsRecords.iterator(); holdingsRecordIterator.hasNext(); ) {
                    HoldingsRecord holdings = holdingsRecordIterator.next();
                    List<ItemRecord> itemRecordsForHoldings = holdings.getItemRecords();

                    for (Iterator<ItemRecord> itemRecordIterator = itemRecordsForHoldings.iterator(); itemRecordIterator.hasNext(); ) {
                        ItemRecord itemRecord = itemRecordIterator.next();
                        for (Iterator matchPointIterator = map.keySet().iterator(); matchPointIterator.hasNext(); ) {
                            String key = (String) matchPointIterator.next();
                            for (Iterator<ItemHandler> metadataHandlerIterator = getItemMetaDataHandlers().iterator(); metadataHandlerIterator.hasNext(); ) {
                                ItemHandler itemHandler = metadataHandlerIterator.next();
                                if (itemHandler.isInterested(key)) {
                                    exchange.add(OleNGConstants.ITEM_RECORD, itemRecord);
                                    itemHandler.process(matchPoints, exchange);
                                    Object match = exchange.get(OleNGConstants.MATCHED_ITEM);
                                    if (null != match && match.equals(Boolean.TRUE)) {
                                        itemRecords.add(itemRecord);
                                    }
                                }
                            }
                        }
                    }

                    exchange.remove(OleNGConstants.ITEM_RECORD);
                }
            }


        }

        return itemRecords;
    }

    private List<HoldingsRecord> prepareHoldingsRecord(BibRecord bibRecord, JSONObject holdingsJSON, Exchange exchange) {
        List<HoldingsRecord> holdingsRecords = new ArrayList<HoldingsRecord>();
        if (null == bibRecord.getBibId()) {
            HoldingsRecord holdingsRecord = new HoldingsRecord();
            holdingsRecord.setBibRecords(Collections.singletonList(bibRecord));
            holdingsRecords.add(holdingsRecord);
        } else if (holdingsJSON.has(OleNGConstants.MATCH_POINT)) {
            List<HoldingsRecord> holdingsForBib = bibRecord.getHoldingsRecords();

            for (Iterator<HoldingsRecord> iterator = holdingsForBib.iterator(); iterator.hasNext(); ) {
                HoldingsRecord holdingsRecord = iterator.next();

                JSONObject matchPoints = null;
                try {
                    matchPoints = holdingsJSON.getJSONObject(OleNGConstants.MATCH_POINT);

                    HashMap map = getObjectMapper().readValue(matchPoints.toString(), new TypeReference<Map<String, String>>() {
                    });

                    for (Iterator matchPointIterator = map.keySet().iterator(); matchPointIterator.hasNext(); ) {
                        String key = (String) matchPointIterator.next();
                        for (Iterator<HoldingsHandler> holdingsRecordIterator = getHoldingMetaDataHandlers().iterator(); holdingsRecordIterator.hasNext(); ) {
                            HoldingsHandler holdingsHandler = holdingsRecordIterator.next();
                            if (holdingsHandler.isInterested(key)) {
                                exchange.add(OleNGConstants.HOLDINGS_RECORD, holdingsRecord);
                                holdingsHandler.process(matchPoints, exchange);
                                Object match = exchange.get(OleNGConstants.MATCHED_HOLDINGS);
                                if (null != match && match.equals(Boolean.TRUE)) {
                                    holdingsRecords.add(holdingsRecord);
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            exchange.remove(OleNGConstants.HOLDINGS_RECORD);
        }

        return holdingsRecords;
    }

    private BibRecord prepareBib(JSONObject bibJSONDataObject) {
        BibRecord bibRecord = new BibRecord();
        if (bibJSONDataObject.has(OleNGConstants.ID)) {
            try {
                bibRecord = bibDAO.retrieveBibById(bibJSONDataObject.getString(OleNGConstants.ID));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return bibRecord;
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
        if (CollectionUtils.isNotEmpty(holdingRecords)) {
            for (Iterator iterator = holdingRecords.iterator(); iterator.hasNext(); ) {
                HoldingsResponse holdingsResponse = new HoldingsResponse();
                HoldingsRecord holdingsRecord = (HoldingsRecord) iterator.next();
                holdingsResponse.setOperation(operation);
                holdingsResponse.setHoldingsId(holdingsRecord.getUniqueIdPrefix() + "-" + holdingsRecord.getHoldingsId());
                holdingsResponse.setItemResponses(prepareItemsResponse(holdingsRecord, exchange));
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
        if (CollectionUtils.isNotEmpty(holdingsRecordsToUpdate)) {
            finalHoldingsForSolr.addAll(holdingsRecordsToUpdate);
        }
        if (CollectionUtils.isNotEmpty(holdingsRecordsToCreate)) {
            finalHoldingsForSolr.addAll(holdingsRecordsToCreate);
        }

        solrInputDocumentMap = prepareSolrInputDocumentsForHoldings(solrInputDocumentMap, finalHoldingsForSolr);
        return solrInputDocumentMap;
    }

    private Map<String, SolrInputDocument> prepareItemsForSolr(Map<String, SolrInputDocument> solrInputDocumentMap, Exchange exchange) {
        List<ItemRecord> itemRecordsToUpdate = (List<ItemRecord>) exchange.get(OleNGConstants.ITEMS_UPDATED);
        List<ItemRecord> itemRecordsToCreate = (List<ItemRecord>) exchange.get(OleNGConstants.ITEMS_CREATED);

        List<ItemRecord> finalItemsForSolr = new ArrayList<ItemRecord>();
        if (CollectionUtils.isNotEmpty(itemRecordsToUpdate)) {
            finalItemsForSolr.addAll(itemRecordsToUpdate);
        }
        if (CollectionUtils.isNotEmpty(itemRecordsToCreate)) {
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
