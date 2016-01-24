package org.kuali.ole.dsng.rest.processor;

import org.apache.commons.codec.language.bm.Rule;
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
import org.kuali.ole.docstore.common.document.EHoldings;
import org.kuali.ole.docstore.common.document.PHoldings;
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
import org.kuali.ole.dsng.rest.handler.eholdings.*;
import org.kuali.ole.dsng.rest.handler.holdings.*;
import org.kuali.ole.dsng.rest.handler.holdings.CallNumberHandler;
import org.kuali.ole.dsng.rest.handler.holdings.CallNumberPrefixHandler;
import org.kuali.ole.dsng.rest.handler.holdings.CallNumberTypeHandler;
import org.kuali.ole.dsng.rest.handler.holdings.CopyNumberHandler;
import org.kuali.ole.dsng.rest.handler.items.*;
import org.kuali.ole.dsng.rest.handler.items.DonorCodeHandler;
import org.kuali.ole.dsng.rest.handler.items.DonorNoteHandler;
import org.kuali.ole.dsng.rest.handler.items.DonorPublicDisplayHandler;
import org.kuali.ole.dsng.rest.handler.items.StatisticalSearchCodeHandler;
import org.kuali.ole.dsng.util.OleDsHelperUtil;
import org.marc4j.marc.Record;
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
    private List<Handler> eholdingHandlers;
    private List<Handler> itemHandlers;
    private List<HoldingsHandler> holdingMetaDataHandlers;
    private List<HoldingsHandler> eholdingMetaDataHandlers;
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

    public List<HoldingsHandler> getEholdingMetaDataHandlers() {

        if (null == eholdingMetaDataHandlers) {
            eholdingMetaDataHandlers = new ArrayList<HoldingsHandler>();
            eholdingMetaDataHandlers.add(new HoldingsLocationHandler());
            eholdingMetaDataHandlers.add(new CallNumberHandler());
            eholdingMetaDataHandlers.add(new CallNumberTypeHandler());
            eholdingMetaDataHandlers.add(new CallNumberPrefixHandler());
            eholdingMetaDataHandlers.add(new CopyNumberHandler());
            eholdingMetaDataHandlers.add(new AccessLocationHandler());
            eholdingMetaDataHandlers.add(new AccessPasswordHandler());
            eholdingMetaDataHandlers.add(new AccessStatusHandler());
            eholdingMetaDataHandlers.add(new AccessUserNameHandler());
            eholdingMetaDataHandlers.add(new AccessPasswordHandler());
            eholdingMetaDataHandlers.add(new AdminUrlHandler());
            eholdingMetaDataHandlers.add(new AdminUserNameHandler());
            eholdingMetaDataHandlers.add(new AuthenticationTypeHandler());
            eholdingMetaDataHandlers.add(new CancellationDecisionDateHandler());
            eholdingMetaDataHandlers.add(new CancellationEffectiveDateHandler());
            eholdingMetaDataHandlers.add(new CancellationReasonHandler());
            eholdingMetaDataHandlers.add(new CoverageStartDateHandler());
            eholdingMetaDataHandlers.add(new CoverageStartIssueHandler());
            eholdingMetaDataHandlers.add(new CoverageStartVolumeHandler());
            eholdingMetaDataHandlers.add(new CoverageEndDateHandler());
            eholdingMetaDataHandlers.add(new CoverageEndIssueHandler());
            eholdingMetaDataHandlers.add(new CoverageEndVolumeHandler());
            eholdingMetaDataHandlers.add(new CurrentSubscriptionEndDateHandler());
            eholdingMetaDataHandlers.add(new CurrentSubscriptionStartDateHandler());
            eholdingMetaDataHandlers.add(new org.kuali.ole.dsng.rest.handler.eholdings.DonorCodeHandler());
            eholdingMetaDataHandlers.add(new org.kuali.ole.dsng.rest.handler.eholdings.DonorNoteHandler());
            eholdingMetaDataHandlers.add(new org.kuali.ole.dsng.rest.handler.eholdings.DonorPublicDisplayHandler());
            eholdingMetaDataHandlers.add(new EResourceIdHandler());
            eholdingMetaDataHandlers.add(new NoOfSumultaneousUserHander());
            eholdingMetaDataHandlers.add(new InitialSubscriptionEndDateHandler());
            eholdingMetaDataHandlers.add(new PerpetualAccessStartDateHandler());
            eholdingMetaDataHandlers.add(new PerpetualAccessStartIssueHandler());
            eholdingMetaDataHandlers.add(new PerpetualAccessStartVolumeHandler());
            eholdingMetaDataHandlers.add(new PerpetualAccessEndDateHandler());
            eholdingMetaDataHandlers.add(new PerpetualAccessEndIssueHandler());
            eholdingMetaDataHandlers.add(new PerpetualAccessEndVolumeHandler());
            eholdingMetaDataHandlers.add(new PersistentLinkHandler());
            eholdingMetaDataHandlers.add(new PlatformHandler());
            eholdingMetaDataHandlers.add(new ProxiedHandler());
            eholdingMetaDataHandlers.add(new PublisherHandler());
            eholdingMetaDataHandlers.add(new org.kuali.ole.dsng.rest.handler.eholdings.StatisticalSearchCodeHandler());
            eholdingMetaDataHandlers.add(new SubscriptionStatusHandler());
            eholdingMetaDataHandlers.add(new UrlHandler());
            eholdingMetaDataHandlers.add(new LinkTextHandler());
            eholdingMetaDataHandlers.add(new ImprintHandler());
            eholdingMetaDataHandlers.add(new NonPublicNoteHandler());
            eholdingMetaDataHandlers.add(new PublicNoteHandler());
            eholdingMetaDataHandlers.add(new NoOfSumultaneousUserHander());
        }
        return eholdingMetaDataHandlers;
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

    public List<Handler> getEHoldingHandlers() {
        if (null == eholdingHandlers) {
            eholdingHandlers = new ArrayList<Handler>();
            eholdingHandlers.add(new CreateEHoldingsHandler());
            eholdingHandlers.add(new UpdateEholdingsHandler());
        }
        return eholdingHandlers;
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

                List<String> operationsList = getListFromJSONArray(ops);

                BibRecord bibRecord = prepareBib(bibJSONDataObject);
                exchange.add(OleNGConstants.BIB, bibRecord);

                List<HoldingsRecord> holdingsForUpdateOrCreate = new ArrayList<HoldingsRecord>();
                if (operationsList.contains("122") || operationsList.contains("221")) {
                    holdingsForUpdateOrCreate = preparePHoldingsRecord(bibRecord, bibJSONDataObject, exchange);
                    exchange.add(OleNGConstants.HOLDINGS, holdingsForUpdateOrCreate);
                }

                List<HoldingsRecord> eholdingsForUpdateOrCreate = new ArrayList<HoldingsRecord>();
                if (operationsList.contains("142") || operationsList.contains("241")) {
                    //TODO: prepare EHoldings list
                    eholdingsForUpdateOrCreate = prepareEHoldingsRecord(bibRecord, bibJSONDataObject, exchange);
                    exchange.add(OleNGConstants.EHOLDINGS, eholdingsForUpdateOrCreate);
                }

                List<ItemRecord> itemsForUpdateOrCreate = prepareItemsRecord(holdingsForUpdateOrCreate, bibJSONDataObject, exchange);
                exchange.add(OleNGConstants.ITEM, itemsForUpdateOrCreate);

                processBib(solrInputDocumentMap, exchange, bibJSONDataObject, ops, bibRecord);

                processHoldings(solrInputDocumentMap, exchange, bibJSONDataObject, ops, holdingsForUpdateOrCreate);

                processEHoldings(solrInputDocumentMap, exchange, bibJSONDataObject, ops, eholdingsForUpdateOrCreate);

                processItems(solrInputDocumentMap, exchange, bibJSONDataObject, ops, itemsForUpdateOrCreate);

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

    private void processItems(Map<String, SolrInputDocument> solrInputDocumentMap, Exchange exchange, JSONObject bibJSONDataObject, String ops, List<ItemRecord> itemsForUpdateOrCreate) {
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
    }

    private void processEHoldings(Map<String, SolrInputDocument> solrInputDocumentMap, Exchange exchange, JSONObject bibJSONDataObject, String ops, List<HoldingsRecord> eholdingsForUpdateOrCreate) {
        for (Iterator<Handler> iterator = getEHoldingHandlers().iterator(); iterator.hasNext(); ) {
            Handler holdingsHandler = iterator.next();
            if (holdingsHandler.isInterested(ops)) {
                holdingsHandler.setHoldingDAO(holdingDAO);
                holdingsHandler.process(bibJSONDataObject, exchange);
            }
        }

        for (Iterator<HoldingsRecord> holdingsRecordIterator = eholdingsForUpdateOrCreate.iterator(); holdingsRecordIterator.hasNext(); ) {
            HoldingsRecord holdingsRecord = holdingsRecordIterator.next();
            getHoldingIndexer().getInputDocumentForHoldings(holdingsRecord, solrInputDocumentMap);
        }
    }

    private void processHoldings(Map<String, SolrInputDocument> solrInputDocumentMap, Exchange exchange, JSONObject bibJSONDataObject, String ops, List<HoldingsRecord> holdingsForUpdateOrCreate) {
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
    }

    private void processBib(Map<String, SolrInputDocument> solrInputDocumentMap, Exchange exchange, JSONObject bibJSONDataObject, String ops, BibRecord bibRecord) {
        for (Iterator<Handler> iterator = getBibHandlers().iterator(); iterator.hasNext(); ) {
            Handler bibHandler = iterator.next();
            if (bibHandler.isInterested(ops)) {
                bibHandler.setBibDAO(bibDAO);
                bibHandler.process(bibJSONDataObject, exchange);
            }
        }

        getBibIndexer().getInputDocumentForBib(bibRecord, solrInputDocumentMap);
    }

    private List<ItemRecord> prepareItemsRecord(List<HoldingsRecord> holdingsRecords, JSONObject bibJSON, Exchange exchange) {
        List<ItemRecord> itemRecords = new ArrayList<ItemRecord>();

        JSONObject itemJSON = null;
        Integer numOccurances = 0;
        try {
            itemJSON = bibJSON.getJSONObject(OleNGConstants.ITEM);
            numOccurances = getNumOccurances(itemJSON);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        for (Iterator<HoldingsRecord> iterator = holdingsRecords.iterator(); iterator.hasNext(); ) {
            HoldingsRecord holdingsRecord = iterator.next();
            if (null == holdingsRecord.getHoldingsId()) {
                for (int i = 0; i < numOccurances; i++) {
                    ItemRecord itemRecord = new ItemRecord();
                    itemRecord.setHoldingsRecord(holdingsRecord);
                    itemRecords.add(itemRecord);
                    holdingsRecord.setItemRecords(itemRecords);
                }
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

    private List<HoldingsRecord> preparePHoldingsRecord(BibRecord bibRecord, JSONObject bibJSON, Exchange exchange) {
        List<HoldingsRecord> holdingsRecords = new ArrayList<HoldingsRecord>();

        JSONObject holdingsJSON = null;
        Integer numOccurances = 0;
        try {
            holdingsJSON = bibJSON.getJSONObject(OleNGConstants.HOLDINGS);
            numOccurances = getNumOccurances(holdingsJSON);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (null == bibRecord.getBibId()) {
            for (int i = 0; i < numOccurances; i++) {
                holdingsRecords.add(getNewHoldings(bibRecord, PHoldings.PRINT));
            }

        } else if (holdingsJSON.has(OleNGConstants.MATCH_POINT)) {
            holdingsRecords = determineHoldingsByMatchPoints(bibRecord, exchange, holdingsJSON, PHoldings.PRINT);
        }

        return holdingsRecords;
    }


    private List<HoldingsRecord> prepareEHoldingsRecord(BibRecord bibRecord, JSONObject bibJSON, Exchange exchange) {
        JSONObject eholdingsJSON = null;
        Integer numOccurances = 0;
        try {
            eholdingsJSON = bibJSON.getJSONObject(OleNGConstants.EHOLDINGS);
            numOccurances = getNumOccurances(bibJSON);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        List<HoldingsRecord> eholdingsRecords = new ArrayList<HoldingsRecord>();
        if (null == bibRecord.getBibId()) {
            for (int count = 0; count < numOccurances; count++) {
                eholdingsRecords.add(getNewHoldings(bibRecord, EHoldings.ELECTRONIC));
            }

        } else if (eholdingsJSON.has(OleNGConstants.MATCH_POINT)) {
            eholdingsRecords = determineHoldingsByMatchPoints(bibRecord, exchange, eholdingsJSON, EHoldings.ELECTRONIC);

        }

        return eholdingsRecords;
    }

    private HoldingsRecord getNewHoldings(BibRecord bibRecord, String holdingsType) {
        HoldingsRecord holdingsRecord = new HoldingsRecord();
        holdingsRecord.setHoldingsType(holdingsType);
        holdingsRecord.setBibRecords(Collections.singletonList(bibRecord));
        return holdingsRecord;
    }

    private List<HoldingsRecord> determineHoldingsByMatchPoints(BibRecord bibRecord, Exchange exchange, JSONObject holdingsJSON, String docType) {
        List<HoldingsRecord> eholdingsRecords = new ArrayList<HoldingsRecord>();
        List<HoldingsRecord> holdingsForBib = bibRecord.getHoldingsRecords();

        for (Iterator<HoldingsRecord> iterator = holdingsForBib.iterator(); iterator.hasNext(); ) {
            HoldingsRecord holdingsRecord = iterator.next();
            if (holdingsRecord.getHoldingsType().equals(docType)) {
                JSONObject matchPoints = null;
                try {
                    matchPoints = holdingsJSON.getJSONObject(OleNGConstants.MATCH_POINT);

                    HashMap map = getObjectMapper().readValue(matchPoints.toString(), new TypeReference<Map<String, String>>() {
                    });

                    for (Iterator matchPointIterator = map.keySet().iterator(); matchPointIterator.hasNext(); ) {
                        String key = (String) matchPointIterator.next();
                        for (Iterator<HoldingsHandler> holdingsRecordIterator = getEholdingMetaDataHandlers().iterator(); holdingsRecordIterator.hasNext(); ) {
                            HoldingsHandler holdingsHandler = holdingsRecordIterator.next();
                            if (holdingsHandler.isInterested(key)) {
                                exchange.add(OleNGConstants.HOLDINGS_RECORD, holdingsRecord);
                                holdingsHandler.process(matchPoints, exchange);
                                Object match = exchange.get(OleNGConstants.MATCHED_HOLDINGS);
                                if (null != match && match.equals(Boolean.TRUE)) {
                                    eholdingsRecords.add(holdingsRecord);
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

        return eholdingsRecords;
    }

    private Integer getNumOccurances(JSONObject bibJSON) throws JSONException {
        Integer numOccurances = null;
        JSONArray actionOps;
        List<Record> marcRecords;
        if (bibJSON.has(OleNGConstants.ACTION_OPS)) {
            actionOps = bibJSON.getJSONArray(OleNGConstants.ACTION_OPS);
        } else {
            return 1;
        }
        String marcContent = bibJSON.getString(OleNGConstants.MODIFIED_CONTENT);
        marcRecords = getMarcRecordUtil().getMarcXMLConverter().convertMarcXmlToRecord(marcContent);

        for (int i = 0; i < actionOps.length(); i++) {
            try {
                JSONObject actionOp = (JSONObject) actionOps.get(i);
                String docType = actionOp.getString(OleNGConstants.DOC_TYPE);
                if (docType.equalsIgnoreCase(OleNGConstants.EHOLDINGS) || docType.equalsIgnoreCase(OleNGConstants.HOLDINGS) || docType.equalsIgnoreCase(OleNGConstants.ITEM)) {
                    String dataField = actionOp.has(OleNGConstants.DATA_FIELD) ? actionOp.getString(OleNGConstants.DATA_FIELD) : null;
                    String ind1 = actionOp.has(OleNGConstants.IND1) ? actionOp.getString(OleNGConstants.IND1) : null;
                    String ind2 = actionOp.has(OleNGConstants.IND2) ? actionOp.getString(OleNGConstants.IND2) : null;
                    String subField = actionOp.has(OleNGConstants.SUBFIELD) ? actionOp.getString(OleNGConstants.SUBFIELD) : null;

                    numOccurances = getMarcRecordUtil().getNumOccurances(marcRecords.get(0), dataField, ind1, ind2, subField);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return numOccurances;
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
