package org.kuali.ole.dsng.rest.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.document.EHoldings;
import org.kuali.ole.docstore.common.document.PHoldings;
import org.kuali.ole.docstore.common.response.*;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.model.HoldingsRecordAndDataMapping;
import org.kuali.ole.dsng.model.ItemRecordAndDataMapping;
import org.kuali.ole.dsng.rest.handler.Handler;
import org.kuali.ole.dsng.rest.handler.holdings.HoldingsHandler;
import org.kuali.ole.dsng.rest.handler.items.ItemHandler;
import org.kuali.ole.dsng.service.OleDsNGMemorizeService;
import org.kuali.ole.oleng.exception.ValidationException;
import org.marc4j.marc.Record;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

/**
 * Created by SheikS on 12/8/2015.
 */
public class OleDsNgOverlayProcessor extends OleDsNgOverlayProcessorHelper implements DocstoreConstants {
    private static final Logger LOG = Logger.getLogger(OleDsNgOverlayProcessor.class);

    @Autowired
    OleDsNGMemorizeService oleDsNGMemorizeService;

    public String processBibAndHoldingsAndItems(String jsonBody) {
        String response = "{}";

        List<BibResponse> bibResponses = new ArrayList<BibResponse>();

        Map<String, SolrInputDocument> solrInputDocumentMap = new HashedMap();

        OleNGBibImportResponse oleNGBibImportResponse = new OleNGBibImportResponse();

        try {
            JSONArray requestJsonArray = new JSONArray(jsonBody);
            Exchange exchange = new Exchange();

            for (int index = 0; index < requestJsonArray.length(); index++) {
                JSONObject bibJSONDataObject = requestJsonArray.getJSONObject(index);
                BibResponse bibResponse = new BibResponse();
                BibFailureResponse failureResponse = new BibFailureResponse();

                exchange.add(OleNGConstants.FAILURE_RESPONSE, failureResponse);
                exchange.add("validationErrorMessages", new ArrayList<String>());
                try {

                    String recordIndex = getStringValueFromJsonObject(bibJSONDataObject, OleNGConstants.RECORD_INDEX);
                    if(null != recordIndex) {
                        bibResponse.setRecordIndex(Integer.valueOf(recordIndex));
                    }

                    String ops = bibJSONDataObject.getString(OleNGConstants.OPS);
                    List<String> operationsList = getListFromJSONArray(ops);

                    BibRecord bibRecord = prepareBib(bibJSONDataObject, exchange);
                    Boolean bibNotAvailable = (Boolean) exchange.get(OleNGConstants.BIB_NOT_AVAILABLE);
                    if ((bibNotAvailable == null || bibNotAvailable == Boolean.FALSE)) {
                        exchange.add(OleNGConstants.BIB, bibRecord);

                        List<HoldingsRecordAndDataMapping> holdingsForUpdateOrCreate = new ArrayList<HoldingsRecordAndDataMapping>();

                        preparePHoldingsRecord(bibRecord, bibJSONDataObject, exchange);

                        prepareEHoldingsRecord(bibRecord, bibJSONDataObject, exchange);

                        List<HoldingsRecordAndDataMapping> createHoldingsRecordAndDataMappings = (List<HoldingsRecordAndDataMapping>) exchange.get(OleNGConstants.HOLDINGS_FOR_CREATE);
                        List<HoldingsRecordAndDataMapping> updateHoldingsRecordAndDataMappings = (List<HoldingsRecordAndDataMapping>) exchange.get(OleNGConstants.HOLDINGS_FOR_UPDATE);
                        List<HoldingsRecordAndDataMapping> createEHoldingsRecordAndDataMappings = (List<HoldingsRecordAndDataMapping>) exchange.get(OleNGConstants.EHOLDINGS_FOR_CREATE);
                        List<HoldingsRecordAndDataMapping> updateEHoldingsRecordAndDataMappings = (List<HoldingsRecordAndDataMapping>) exchange.get(OleNGConstants.EHOLDINGS_FOR_UPDATE);


                        if (CollectionUtils.isNotEmpty(createHoldingsRecordAndDataMappings)) {
                            holdingsForUpdateOrCreate.addAll(createHoldingsRecordAndDataMappings);
                        }
                        if (CollectionUtils.isNotEmpty(updateHoldingsRecordAndDataMappings)) {
                            holdingsForUpdateOrCreate.addAll(updateHoldingsRecordAndDataMappings);
                        }
                        prepareItemsRecord(holdingsForUpdateOrCreate, bibJSONDataObject, exchange);

                        List<ItemRecordAndDataMapping> createItemRecordAndDataMappings = (List<ItemRecordAndDataMapping>) exchange.get(OleNGConstants.ITEMS_FOR_CREATE);
                        List<ItemRecordAndDataMapping> updateItemRecordAndDataMappings = (List<ItemRecordAndDataMapping>) exchange.get(OleNGConstants.ITEMS_FOR_UPDATE);
                        LOG.info("processBib started");
                        processBib(solrInputDocumentMap, exchange, bibJSONDataObject, ops, bibRecord);
                        LOG.info("processBib completed");
                        LOG.info("processHoldings started");
                        processHoldings(solrInputDocumentMap, exchange, bibJSONDataObject, ops, holdingsForUpdateOrCreate);
                        LOG.info("processHoldings completed");
                        LOG.info("processEHoldings started");
                        processEHoldings(solrInputDocumentMap, exchange, bibJSONDataObject, ops);
                        LOG.info("processEHoldings completed");
                        LOG.info("processItems started");
                        processItems(solrInputDocumentMap, exchange, bibJSONDataObject, ops);
                        LOG.info("processItems completed");

                        bibRecord = (BibRecord) exchange.get(OleNGConstants.BIB);
                        buildBibResponses(bibResponse, bibRecord, exchange, operationsList);

                        if (CollectionUtils.isNotEmpty(createHoldingsRecordAndDataMappings)) {
                            for (Iterator<HoldingsRecordAndDataMapping> iterator = createHoldingsRecordAndDataMappings.iterator(); iterator.hasNext(); ) {
                                HoldingsRecordAndDataMapping holdingsRecordAndDataMapping = iterator.next();
                                HoldingsRecord holdingsRecord = holdingsRecordAndDataMapping.getHoldingsRecord();
                                if(StringUtils.isNotBlank(holdingsRecord.getHoldingsId())) {
                                    oleNGBibImportResponse.setUnmatchedHoldingsCount(oleNGBibImportResponse.getUnmatchedHoldingsCount() + 1);
                                }
                            }
                        }
                        if (CollectionUtils.isNotEmpty(updateHoldingsRecordAndDataMappings)) {
                            for (Iterator<HoldingsRecordAndDataMapping> iterator = updateHoldingsRecordAndDataMappings.iterator(); iterator.hasNext(); ) {
                                HoldingsRecordAndDataMapping holdingsRecordAndDataMapping = iterator.next();
                                HoldingsRecord holdingsRecord = holdingsRecordAndDataMapping.getHoldingsRecord();
                                if(StringUtils.isNotBlank(holdingsRecord.getHoldingsId())) {
                                    oleNGBibImportResponse.setMatchedHoldingsCount(oleNGBibImportResponse.getMatchedHoldingsCount() + 1);
                                }
                            }
                        }
                        if (CollectionUtils.isNotEmpty(createEHoldingsRecordAndDataMappings)) {
                            for (Iterator<HoldingsRecordAndDataMapping> iterator = createEHoldingsRecordAndDataMappings.iterator(); iterator.hasNext(); ) {
                                HoldingsRecordAndDataMapping holdingsRecordAndDataMapping = iterator.next();
                                HoldingsRecord holdingsRecord = holdingsRecordAndDataMapping.getHoldingsRecord();
                                if(StringUtils.isNotBlank(holdingsRecord.getHoldingsId())) {
                                    oleNGBibImportResponse.setUnmatchedEHoldingsCount(oleNGBibImportResponse.getUnmatchedEHoldingsCount() + 1);
                                }
                            }
                        }
                        if (CollectionUtils.isNotEmpty(updateEHoldingsRecordAndDataMappings)) {
                            for (Iterator<HoldingsRecordAndDataMapping> iterator = updateEHoldingsRecordAndDataMappings.iterator(); iterator.hasNext(); ) {
                                HoldingsRecordAndDataMapping holdingsRecordAndDataMapping = iterator.next();
                                HoldingsRecord holdingsRecord = holdingsRecordAndDataMapping.getHoldingsRecord();
                                if(StringUtils.isNotBlank(holdingsRecord.getHoldingsId())) {
                                    oleNGBibImportResponse.setMatchedEHoldingsCount(oleNGBibImportResponse.getMatchedEHoldingsCount() + 1);
                                }
                            }
                        }

                        if (CollectionUtils.isNotEmpty(createItemRecordAndDataMappings)) {
                            for (Iterator<ItemRecordAndDataMapping> iterator = createItemRecordAndDataMappings.iterator(); iterator.hasNext(); ) {
                                ItemRecordAndDataMapping itemRecordAndDataMapping = iterator.next();
                                ItemRecord itemRecord = itemRecordAndDataMapping.getItemRecord();
                                if(StringUtils.isNotBlank(itemRecord.getItemId())) {
                                    oleNGBibImportResponse.setUnmatchedItemsCount(oleNGBibImportResponse.getUnmatchedItemsCount() + 1);
                                }
                            }
                        }
                        if (CollectionUtils.isNotEmpty(updateItemRecordAndDataMappings)) {
                            for (Iterator<ItemRecordAndDataMapping> iterator = updateItemRecordAndDataMappings.iterator(); iterator.hasNext(); ) {
                                ItemRecordAndDataMapping itemRecordAndDataMapping = iterator.next();
                                ItemRecord itemRecord = itemRecordAndDataMapping.getItemRecord();
                                if(StringUtils.isNotBlank(itemRecord.getItemId())) {
                                    oleNGBibImportResponse.setMatchedItemsCount(oleNGBibImportResponse.getMatchedItemsCount() + 1);
                                }
                            }
                        }

                        Integer multipleMatchedHoldings = (Integer) exchange.get("multipleMatchedHoldings");
                        if (null != multipleMatchedHoldings) {
                            oleNGBibImportResponse.setMultipleMatchedHoldingsCount(multipleMatchedHoldings);
                        }

                        Integer multipleMatchedEHoldings = (Integer) exchange.get("multipleMatchedEHoldings");
                        if (null != multipleMatchedEHoldings) {
                            oleNGBibImportResponse.setMultipleMatchedEHoldingsCount(multipleMatchedEHoldings);
                        }

                        Integer multipleMatchedItems = (Integer) exchange.get("multipleMatchedItems");
                        if (null != multipleMatchedItems) {
                            oleNGBibImportResponse.setMultipleMatchedItemsCount(multipleMatchedItems);
                        }
                    }
                    ArrayList validationErrorMessages = (ArrayList) exchange.get("validationErrorMessages");
                    bibResponse.setValidationErrorMessages(validationErrorMessages);
                    bibResponses.add(bibResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    addFailureReportToExchange(bibJSONDataObject, exchange, "Bib", e , null);
                }
                failureResponse = (BibFailureResponse) exchange.get(OleNGConstants.FAILURE_RESPONSE);
                if(StringUtils.isNotBlank(failureResponse.getFailureMessage())) {
                    oleNGBibImportResponse.addFailureResponse(failureResponse);
                }
            }

            List<SolrInputDocument> solrInputDocuments = getBibIndexer().getSolrInputDocumentListFromMap(solrInputDocumentMap);
            if (CollectionUtils.isNotEmpty(solrInputDocuments)) {
                getBibIndexer().commitDocumentToSolr(solrInputDocuments);
            }

        } catch (Exception e) {
            e.printStackTrace();
            BibFailureResponse failureResponse = new BibFailureResponse();
            failureResponse.setFailureMessage(e.toString());
            oleNGBibImportResponse.addFailureResponse(failureResponse);
        }

        oleNGBibImportResponse.setBibResponses(bibResponses);

        try {
            response = getObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(oleNGBibImportResponse);
        } catch (IOException e) {
            e.printStackTrace();
            BibFailureResponse failureResponse = new BibFailureResponse();
            failureResponse.setFailureMessage(e.toString());
            oleNGBibImportResponse.addFailureResponse(failureResponse);
        }
        return response;
    }

    public void buildBibResponses(BibResponse bibResponse, BibRecord bibRecord, Exchange exchange, List<String> options) {
        updateBibOperation(bibResponse, bibRecord);
        buildHoldingResponses(bibResponse, exchange, options);
        buildEHoldingResponses(bibResponse, exchange, options);
    }

    private void updateBibOperation(BibResponse bibResponse, BibRecord bibRecord) {
        bibResponse.setOperation(OleNGConstants.DISCARDED);
        if(StringUtils.isNotBlank(bibRecord.getBibId())) {
            bibResponse.setBibId(bibRecord.getBibId());
        } else {
            bibResponse.setBibId(" ");
        }
        if(StringUtils.isNotBlank(bibRecord.getOperationType())) {
            if(bibRecord.getOperationType().equalsIgnoreCase(OleNGConstants.CREATED)) {
                bibResponse.setOperation(OleNGConstants.CREATED);
            } else if(bibRecord.getOperationType().equalsIgnoreCase(OleNGConstants.UPDATED)) {
                bibResponse.setOperation(OleNGConstants.UPDATED);
            }
        }
    }

    public void buildEHoldingResponses(BibResponse bibResponse, Exchange exchange, List<String> options) {
        List<HoldingsRecordAndDataMapping> createEHoldingsRecordAndDataMappings = (List<HoldingsRecordAndDataMapping>) exchange.get(OleNGConstants.EHOLDINGS_FOR_CREATE);
        List<HoldingsRecordAndDataMapping> updateEHoldingsRecordAndDataMappings = (List<HoldingsRecordAndDataMapping>) exchange.get(OleNGConstants.EHOLDINGS_FOR_UPDATE);
        List<HoldingsResponse> holdingsResponses = new ArrayList<HoldingsResponse>();
        if (CollectionUtils.isNotEmpty(createEHoldingsRecordAndDataMappings)) {
            for (HoldingsRecordAndDataMapping holdingsRecordAndDataMapping : createEHoldingsRecordAndDataMappings) {
                HoldingsRecord holdingsRecord = holdingsRecordAndDataMapping.getHoldingsRecord();
                if (null != holdingsRecord && StringUtils.isNotBlank(holdingsRecord.getHoldingsId())) {
                    HoldingsResponse holdingsResponse = new HoldingsResponse();
                    holdingsResponse.setHoldingsId(holdingsRecord.getHoldingsId());
                    holdingsResponse.setOperation(OleNGConstants.CREATED);
                    holdingsResponse.setHoldingsType(EHoldings.ELECTRONIC);
                    holdingsResponses.add(holdingsResponse);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(updateEHoldingsRecordAndDataMappings)) {
            for (HoldingsRecordAndDataMapping holdingsRecordAndDataMapping : updateEHoldingsRecordAndDataMappings) {
                HoldingsRecord holdingsRecord = holdingsRecordAndDataMapping.getHoldingsRecord();
                if (null != holdingsRecord && StringUtils.isNotBlank(holdingsRecord.getHoldingsId())) {
                    String operationType = holdingsRecord.getOperationType();
                    String status = OleNGConstants.DISCARDED;
                    if (options.contains("142") && StringUtils.isNotBlank(operationType) && operationType.equalsIgnoreCase(OleNGConstants.UPDATED)) {
                        status = OleNGConstants.UPDATED;
                    }
                    HoldingsResponse holdingsResponse = new HoldingsResponse();
                    holdingsResponse.setHoldingsId(holdingsRecord.getHoldingsId());
                    holdingsResponse.setOperation(status);
                    holdingsResponse.setHoldingsType(EHoldings.ELECTRONIC);
                    holdingsResponses.add(holdingsResponse);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(holdingsResponses)) {
            if (CollectionUtils.isNotEmpty(bibResponse.getHoldingsResponses())) {
                bibResponse.getHoldingsResponses().addAll(holdingsResponses);
            } else {
                bibResponse.setHoldingsResponses(holdingsResponses);
            }
        }
    }

    public void buildHoldingResponses(BibResponse bibResponse, Exchange exchange, List<String> options) {
        List<HoldingsRecordAndDataMapping> createHoldingsRecordAndDataMappings = (List<HoldingsRecordAndDataMapping>) exchange.get(OleNGConstants.HOLDINGS_FOR_CREATE);
        List<HoldingsRecordAndDataMapping> updateHoldingsRecordAndDataMappings = (List<HoldingsRecordAndDataMapping>) exchange.get(OleNGConstants.HOLDINGS_FOR_UPDATE);
        List<HoldingsResponse> holdingsResponses = new ArrayList<HoldingsResponse>();
        if (CollectionUtils.isNotEmpty(createHoldingsRecordAndDataMappings)) {
            for (HoldingsRecordAndDataMapping holdingsRecordAndDataMapping : createHoldingsRecordAndDataMappings) {
                HoldingsRecord holdingsRecord = holdingsRecordAndDataMapping.getHoldingsRecord();
                if (null != holdingsRecord && StringUtils.isNotBlank(holdingsRecord.getHoldingsId())) {
                    HoldingsResponse holdingsResponse = new HoldingsResponse();
                    holdingsResponse.setHoldingsId(holdingsRecord.getHoldingsId());
                    holdingsResponse.setOperation(OleNGConstants.CREATED);
                    holdingsResponse.setHoldingsType(PHoldings.PRINT);
                    buildItemResponse(holdingsResponse, exchange, holdingsRecord, options);
                    holdingsResponses.add(holdingsResponse);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(updateHoldingsRecordAndDataMappings)) {
            for (HoldingsRecordAndDataMapping holdingsRecordAndDataMapping : updateHoldingsRecordAndDataMappings) {
                HoldingsRecord holdingsRecord = holdingsRecordAndDataMapping.getHoldingsRecord();
                if (null != holdingsRecord && StringUtils.isNotBlank(holdingsRecord.getHoldingsId())) {
                    String operationType = holdingsRecord.getOperationType();
                    String status = OleNGConstants.DISCARDED;
                    if (options.contains("122") && StringUtils.isNotBlank(operationType) && operationType.equalsIgnoreCase(OleNGConstants.UPDATED)) {
                        status = OleNGConstants.UPDATED;
                    }
                    HoldingsResponse holdingsResponse = new HoldingsResponse();
                    holdingsResponse.setHoldingsId(holdingsRecord.getHoldingsId());
                    holdingsResponse.setOperation(status);
                    holdingsResponse.setHoldingsType(PHoldings.PRINT);
                    buildItemResponse(holdingsResponse, exchange, holdingsRecord, options);
                    holdingsResponses.add(holdingsResponse);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(holdingsResponses)) {
            if (CollectionUtils.isNotEmpty(bibResponse.getHoldingsResponses())) {
                bibResponse.getHoldingsResponses().addAll(holdingsResponses);
            } else {
                bibResponse.setHoldingsResponses(holdingsResponses);
            }
        }
    }

    public void buildItemResponse(HoldingsResponse holdingsResponse, Exchange exchange, HoldingsRecord holdingsRecord, List<String> options) {
        List<ItemRecordAndDataMapping> createItemRecordAndDataMappings = (List<ItemRecordAndDataMapping>) exchange.get(OleNGConstants.ITEMS_FOR_CREATE);
        List<ItemRecordAndDataMapping> updateItemRecordAndDataMappings = (List<ItemRecordAndDataMapping>) exchange.get(OleNGConstants.ITEMS_FOR_UPDATE);
        List<ItemResponse> itemResponses = new ArrayList<ItemResponse>();
        if (CollectionUtils.isNotEmpty(createItemRecordAndDataMappings)) {
            for (ItemRecordAndDataMapping itemRecordAndDataMapping : createItemRecordAndDataMappings) {
                ItemRecord itemRecord = itemRecordAndDataMapping.getItemRecord();
                if (null != itemRecord && StringUtils.isNotBlank(itemRecord.getItemId()) && holdingsRecord.getHoldingsId().equals(itemRecord.getHoldingsId())) {
                    ItemResponse itemResponse = new ItemResponse();
                    itemResponse.setItemId(itemRecord.getItemId());
                    itemResponse.setOperation(OleNGConstants.CREATED);
                    itemResponses.add(itemResponse);
                }
            }
        }
        if (!(OleNGConstants.CREATED.equalsIgnoreCase(holdingsResponse.getOperation()))) {
            if (CollectionUtils.isNotEmpty(updateItemRecordAndDataMappings)) {
                for (ItemRecordAndDataMapping itemRecordAndDataMapping : updateItemRecordAndDataMappings) {
                    ItemRecord itemRecord = itemRecordAndDataMapping.getItemRecord();
                    if (null != itemRecord && StringUtils.isNotBlank(itemRecord.getItemId()) && holdingsRecord.getHoldingsId().equals(itemRecord.getHoldingsId())) {
                        String operationType = itemRecord.getOperationType();
                        String status = OleNGConstants.DISCARDED;
                        if (options.contains("122")  && StringUtils.isNotBlank(operationType) && operationType.equalsIgnoreCase(OleNGConstants.UPDATED)) {
                            status = OleNGConstants.UPDATED;
                        }
                        ItemResponse itemResponse = new ItemResponse();
                        itemResponse.setItemId(itemRecord.getItemId());
                        itemResponse.setOperation(status);
                        itemResponses.add(itemResponse);
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(itemResponses)) {
            if (CollectionUtils.isNotEmpty(holdingsResponse.getItemResponses())) {
                holdingsResponse.getItemResponses().addAll(itemResponses);
            } else {
                holdingsResponse.setItemResponses(itemResponses);
            }
        }
    }

    private void processItems(Map<String, SolrInputDocument> solrInputDocumentMap, Exchange exchange, JSONObject bibJSONDataObject, String ops) {
        boolean doIndex = false;
        for (Iterator<Handler> iterator = getItemHandlers().iterator(); iterator.hasNext(); ) {
            Handler itemHandler = iterator.next();
            if (itemHandler.isInterested(ops)) {
                doIndex = true;
                itemHandler.setOleDsNGMemorizeService(oleDsNGMemorizeService);
                itemHandler.process(bibJSONDataObject, exchange);
            }
        }
        if (doIndex) {
            List<ItemRecordAndDataMapping> createItemRecordAndDataMappings = (List<ItemRecordAndDataMapping>) exchange.get(OleNGConstants.ITEMS_FOR_CREATE);
            List<ItemRecordAndDataMapping> updateItemRecordAndDataMappings = (List<ItemRecordAndDataMapping>) exchange.get(OleNGConstants.ITEMS_FOR_UPDATE);
            List<ItemRecordAndDataMapping> itemForUpdateOrCreate = new ArrayList<ItemRecordAndDataMapping>();
            if (CollectionUtils.isNotEmpty(createItemRecordAndDataMappings)) {
                itemForUpdateOrCreate.addAll(createItemRecordAndDataMappings);
            }
            if (CollectionUtils.isNotEmpty(updateItemRecordAndDataMappings)) {
                itemForUpdateOrCreate.addAll(updateItemRecordAndDataMappings);
            }
            for (Iterator<ItemRecordAndDataMapping> itemRecordIterator = itemForUpdateOrCreate.iterator(); itemRecordIterator.hasNext(); ) {
                ItemRecordAndDataMapping itemRecordAndDataMapping = itemRecordIterator.next();
                ItemRecord itemRecord = itemRecordAndDataMapping.getItemRecord();
                if (StringUtils.isNotBlank(itemRecord.getItemId())) {
                    getItemIndexer().getInputDocumentForItem(itemRecord, solrInputDocumentMap);
                }
            }
        }
    }

    private void processEHoldings(Map<String, SolrInputDocument> solrInputDocumentMap, Exchange exchange, JSONObject bibJSONDataObject, String ops) {
        boolean doIndex = false;
        for (Iterator<Handler> iterator = getEHoldingHandlers().iterator(); iterator.hasNext(); ) {
            Handler holdingsHandler = iterator.next();
            if (holdingsHandler.isInterested(ops)) {
                doIndex = true;
                holdingsHandler.setOleDsNGMemorizeService(oleDsNGMemorizeService);
                holdingsHandler.process(bibJSONDataObject, exchange);
            }
        }

        if (doIndex) {
            List<HoldingsRecordAndDataMapping> createHoldingsRecordAndDataMappings = (List<HoldingsRecordAndDataMapping>) exchange.get(OleNGConstants.EHOLDINGS_FOR_CREATE);
            List<HoldingsRecordAndDataMapping> updateHoldingsRecordAndDataMappings = (List<HoldingsRecordAndDataMapping>) exchange.get(OleNGConstants.EHOLDINGS_FOR_UPDATE);
            List<HoldingsRecordAndDataMapping> eholdingsForUpdateOrCreate = new ArrayList<HoldingsRecordAndDataMapping>();
            if (CollectionUtils.isNotEmpty(createHoldingsRecordAndDataMappings)) {
                eholdingsForUpdateOrCreate.addAll(createHoldingsRecordAndDataMappings);
            }
            if (CollectionUtils.isNotEmpty(updateHoldingsRecordAndDataMappings)) {
                eholdingsForUpdateOrCreate.addAll(updateHoldingsRecordAndDataMappings);
            }
            for (Iterator<HoldingsRecordAndDataMapping> holdingsRecordIterator = eholdingsForUpdateOrCreate.iterator(); holdingsRecordIterator.hasNext(); ) {
                HoldingsRecordAndDataMapping holdingsRecordAndDataMapping = holdingsRecordIterator.next();
                HoldingsRecord holdingsRecord = holdingsRecordAndDataMapping.getHoldingsRecord();
                if (StringUtils.isNotBlank(holdingsRecord.getHoldingsId())) {
                    getHoldingIndexer().getInputDocumentForHoldings(holdingsRecord, solrInputDocumentMap);
                }
            }
        }
    }

    private void processHoldings(Map<String, SolrInputDocument> solrInputDocumentMap, Exchange exchange, JSONObject bibJSONDataObject, String ops, List<HoldingsRecordAndDataMapping> holdingsForUpdateOrCreate) {
        boolean doIndex = false;
        for (Iterator<Handler> iterator = getHoldingHandlers().iterator(); iterator.hasNext(); ) {
            Handler holdingsHandler = iterator.next();
            if (holdingsHandler.isInterested(ops)) {
                doIndex = true;
                holdingsHandler.setOleDsNGMemorizeService(oleDsNGMemorizeService);
                holdingsHandler.process(bibJSONDataObject, exchange);
            }
        }

        if (doIndex) {
            for (Iterator<HoldingsRecordAndDataMapping> holdingsRecordIterator = holdingsForUpdateOrCreate.iterator(); holdingsRecordIterator.hasNext(); ) {
                HoldingsRecordAndDataMapping holdingsRecordAndDataMapping = holdingsRecordIterator.next();
                HoldingsRecord holdingsRecord = holdingsRecordAndDataMapping.getHoldingsRecord();
                if (StringUtils.isNotBlank(holdingsRecord.getHoldingsId())) {
                    getHoldingIndexer().getInputDocumentForHoldings(holdingsRecord, solrInputDocumentMap);
                }
            }
        }
    }

    private void processBib(Map<String, SolrInputDocument> solrInputDocumentMap, Exchange exchange, JSONObject bibJSONDataObject, String ops, BibRecord bibRecord) {
        boolean doIndex = false;
        for (Iterator<Handler> iterator = getBibHandlers().iterator(); iterator.hasNext(); ) {
            Handler bibHandler = iterator.next();
            if (bibHandler.isInterested(ops)) {
                doIndex = true;
                bibHandler.setOleDsNGMemorizeService(oleDsNGMemorizeService);
                bibHandler.process(bibJSONDataObject, exchange);
            }
        }

        bibRecord = (BibRecord) exchange.get(OleNGConstants.BIB);
        if (doIndex && StringUtils.isNotBlank(bibRecord.getBibId())) {
            getBibIndexer().getInputDocumentForBib(bibRecord, solrInputDocumentMap);
        }
    }

    public JSONObject getJSONObject(JSONArray jsonArray, int index) {
        JSONObject jsonObject = null;
        try {
            jsonObject = jsonArray.getJSONObject(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public void prepareItemsRecord(List<HoldingsRecordAndDataMapping> holdingsRecordAndDataMappings, JSONObject bibJSON, Exchange exchange) {
        List<ItemRecordAndDataMapping> updateItemRecordAndDataMappings = new ArrayList<ItemRecordAndDataMapping>();
        List<ItemRecordAndDataMapping> createItemRecordAndDataMappings = new ArrayList<ItemRecordAndDataMapping>();
        List<ItemRecordAndDataMapping> itemRecordAndDataMappings = new ArrayList<ItemRecordAndDataMapping>();
        List<ItemRecord> itemRecords = new ArrayList<ItemRecord>();

        JSONObject itemJSON = null;
        Map<String, Integer> numOccurances = new TreeMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);
        try {
            itemJSON = bibJSON.getJSONObject(OleNGConstants.ITEM);
            numOccurances = getNumOccurrences(bibJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONArray actionOps = getActionOps(bibJSON);
        String addedOps = getAddedOpsValue(bibJSON, OleNGConstants.ITEM);
        JSONArray itemDataMappings = getDataMappingsJSONArray(itemJSON);

        for (Iterator<HoldingsRecordAndDataMapping> iterator = holdingsRecordAndDataMappings.iterator(); iterator.hasNext(); ) {

            HoldingsRecordAndDataMapping holdingsRecordAndDataMapping = iterator.next();
            HoldingsRecord holdingsRecord = holdingsRecordAndDataMapping.getHoldingsRecord();

            List<JSONObject> dataMappingForItemMatchedWithHoldingsMapping = getDataMappingForItemMatchedWithHoldingsMapping(holdingsRecordAndDataMapping.getDataMapping(), itemDataMappings, actionOps);

            if (null == holdingsRecord.getHoldingsId()) {
                if (!dataMappingForItemMatchedWithHoldingsMapping.isEmpty()) {
                    for (int i = 0; i < dataMappingForItemMatchedWithHoldingsMapping.size(); i++) {
                        ItemRecordAndDataMapping itemRecordAndDataMapping = getNewItemRecord(holdingsRecord);
                        itemRecords.add(itemRecordAndDataMapping.getItemRecord());
                        itemRecordAndDataMapping.setDataMapping(dataMappingForItemMatchedWithHoldingsMapping.get(i));
                        itemRecordAndDataMappings.add(itemRecordAndDataMapping);
                    }
                } else {
                    ItemRecordAndDataMapping itemRecordAndDataMapping = getNewItemRecord(holdingsRecord);
                    itemRecords.add(itemRecordAndDataMapping.getItemRecord());
                    itemRecordAndDataMapping.setDataMapping(getJSONObject(itemDataMappings, 0));
                    itemRecordAndDataMappings.add(itemRecordAndDataMapping);
                }
                holdingsRecord.setItemRecords(itemRecords);
            } else if (StringUtils.isBlank(addedOps)) {
                List<ItemRecordAndDataMapping> preparedItemAndDataMappings = createItemAndDataMappingForUnassignedMappings(holdingsRecord, itemDataMappings);
                if (CollectionUtils.isNotEmpty(preparedItemAndDataMappings)) {
                    itemRecordAndDataMappings.addAll(preparedItemAndDataMappings);
                }
            }else if (addedOps.equalsIgnoreCase(OleNGConstants.OVERLAY) || addedOps.equalsIgnoreCase(OleNGConstants.DISCARD)) {
                List<ItemRecordAndDataMapping> preparedItemAndDataMappings = determineItemsAndDataMappingByMatchPoints(holdingsRecord, exchange, itemJSON);
                if (CollectionUtils.isNotEmpty(preparedItemAndDataMappings)) {
                    itemRecordAndDataMappings.addAll(preparedItemAndDataMappings);
                }
            } else if (addedOps.equalsIgnoreCase(OleNGConstants.DELETE_ALL_EXISTING_AND_ADD) ||
                    addedOps.equalsIgnoreCase(OleNGConstants.KEEP_ALL_EXISTING_AND_ADD)) {
                List<ItemRecordAndDataMapping> preparedItemAndDataMappings = createItemAndDataMappingForUnassignedMappings(holdingsRecord, itemDataMappings);
                if (CollectionUtils.isNotEmpty(preparedItemAndDataMappings)) {
                    itemRecordAndDataMappings.addAll(preparedItemAndDataMappings);
                }
            }
        }

        for (Iterator<ItemRecordAndDataMapping> iterator = itemRecordAndDataMappings.iterator(); iterator.hasNext(); ) {
            ItemRecordAndDataMapping itemRecordAndDataMapping = iterator.next();
            if (null == itemRecordAndDataMapping.getItemRecord().getItemId()) {
                createItemRecordAndDataMappings.add(itemRecordAndDataMapping);
            } else {
                updateItemRecordAndDataMappings.add(itemRecordAndDataMapping);
            }
        }
        exchange.add(OleNGConstants.ITEMS_FOR_CREATE, createItemRecordAndDataMappings);
        exchange.add(OleNGConstants.ITEMS_FOR_UPDATE, updateItemRecordAndDataMappings);
    }

    private JSONArray getActionOps(JSONObject bibJSON) {
        JSONArray actionOps = new JSONArray();
        if (bibJSON.has(OleNGConstants.ACTION_OPS)) {
            try {
                actionOps = bibJSON.getJSONArray(OleNGConstants.ACTION_OPS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return actionOps;
    }

    private ItemRecordAndDataMapping getNewItemRecord(HoldingsRecord holdingsRecord) {
        ItemRecordAndDataMapping itemRecordAndDataMapping = new ItemRecordAndDataMapping();
        ItemRecord itemRecord = new ItemRecord();
        itemRecord.setHoldingsId(holdingsRecord.getHoldingsId());
        itemRecord.setHoldingsRecord(holdingsRecord);
        itemRecordAndDataMapping.setItemRecord(itemRecord);
        return itemRecordAndDataMapping;
    }

    private List<ItemRecordAndDataMapping> determineItemsAndDataMappingByMatchPoints(HoldingsRecord holdingsRecord, Exchange exchange, JSONObject itemJSON) {
        List<ItemRecordAndDataMapping> itemRecordAndDataMappings = new ArrayList<ItemRecordAndDataMapping>();
        List<ItemRecord> itemRecordsForHoldings = holdingsRecord.getItemRecords();
        JSONArray clonedDataMappings = getClonedDataMappingsJSONArray(itemJSON);
        JSONObject matchPoints = null;
        HashMap map = null;

        Map<String, Map<String, List<ItemRecordAndDataMapping>>> matchedItemsRecordByMatchPoint = new HashMap<String, Map<String, List<ItemRecordAndDataMapping>>>();
        try {
            matchPoints = itemJSON.getJSONObject(OleNGConstants.MATCH_POINT);
            map = getObjectMapper().readValue(matchPoints.toString(), new TypeReference<Map<String, String>>() {
            });
            if (CollectionUtils.isNotEmpty(itemRecordsForHoldings)) {
                for (Iterator<ItemRecord> itemRecordIterator = itemRecordsForHoldings.iterator(); itemRecordIterator.hasNext(); ) {
                    ItemRecord itemRecord = itemRecordIterator.next();
                    for (Iterator matchPointIterator = map.keySet().iterator(); matchPointIterator.hasNext(); ) {
                        String key = (String) matchPointIterator.next();
                        for (Iterator<ItemHandler> metadataHandlerIterator = getItemMetaDataHandlers().iterator(); metadataHandlerIterator.hasNext(); ) {
                            ItemHandler itemHandler = metadataHandlerIterator.next();
                            if (itemHandler.isInterested(key)) {
                                exchange.add(OleNGConstants.ITEM_RECORD, itemRecord);
                                itemHandler.setOleDsNGMemorizeService(oleDsNGMemorizeService);
                                itemHandler.process(matchPoints, exchange);
                                Object match = exchange.get(OleNGConstants.MATCHED_ITEM);
                                if (null != match && match.equals(Boolean.TRUE)) {
                                    ItemRecordAndDataMapping itemRecordAndDataMapping = new ItemRecordAndDataMapping();
                                    itemRecordAndDataMapping.setItemRecord(itemRecord);
                                    String matchedValue = (String) exchange.get(OleNGConstants.MATCHED_VALUE);
                                    JSONObject filterdDataMapping = findDataMappingByValue(clonedDataMappings, key, matchedValue);
                                    itemRecordAndDataMapping.setDataMapping(filterdDataMapping);

                                    if(matchedItemsRecordByMatchPoint.containsKey(key)) {
                                        Map<String, List<ItemRecordAndDataMapping>> matchPointValueAndItems = matchedItemsRecordByMatchPoint.get(key);
                                        if (!matchPointValueAndItems.containsKey(matchedValue)) {
                                            ArrayList matchedItemsForValue = new ArrayList();
                                            matchedItemsForValue.add(itemRecordAndDataMapping);
                                            matchPointValueAndItems.put(matchedValue, matchedItemsForValue);
                                        } else {
                                            List<ItemRecordAndDataMapping> itemRecordAndDataMappings1 = matchPointValueAndItems.get(matchedValue);
                                            itemRecordAndDataMappings1.add(itemRecordAndDataMapping);
                                        }
                                    } else {
                                        Map<String, List<ItemRecordAndDataMapping>> matchPointValueAndItems = new HashMap<String, List<ItemRecordAndDataMapping>>();
                                        ArrayList matchedItemsForValue = new ArrayList();
                                        matchedItemsForValue.add(itemRecordAndDataMapping);
                                        matchPointValueAndItems.put(matchedValue, matchedItemsForValue);
                                        matchedItemsRecordByMatchPoint.put(key, matchPointValueAndItems);
                                    }


                                    exchange.remove(OleNGConstants.MATCHED_ITEM);
                                    exchange.remove(OleNGConstants.MATCHED_VALUE);
                                }
                            }
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

        for (Iterator<String> itemRecordIterator = matchedItemsRecordByMatchPoint.keySet().iterator(); itemRecordIterator.hasNext(); ) {
            String key = itemRecordIterator.next();
            Map<String, List<ItemRecordAndDataMapping>> matchPointValueAndItems = matchedItemsRecordByMatchPoint.get(key);
            for (Iterator<String> iterator = matchPointValueAndItems.keySet().iterator(); iterator.hasNext(); ) {
                String valueKey = iterator.next();
                if (matchPointValueAndItems.get(valueKey).size() == 1) {
                    itemRecordAndDataMappings.add(matchPointValueAndItems.get(valueKey).get(0));
                } else {
                    Integer multipleMatchedItems = (Integer) exchange.get("multipleMatchedItems");
                    if (null != multipleMatchedItems) {
                        multipleMatchedItems = multipleMatchedItems + matchPointValueAndItems.size();
                    } else {
                        multipleMatchedItems = matchPointValueAndItems.size();
                    }
                    exchange.add("multipleMatchedItems", multipleMatchedItems);
                }
            }
        }

        JSONArray unAssignedDataMappings = filterUnAssignedDataMappings(clonedDataMappings);
        List<ItemRecordAndDataMapping> itemAndDataMappingForUnassignedMappings = createItemAndDataMappingForUnassignedMappings(holdingsRecord, unAssignedDataMappings);
        if(CollectionUtils.isNotEmpty(itemAndDataMappingForUnassignedMappings)) {
            itemRecordAndDataMappings.addAll(itemAndDataMappingForUnassignedMappings);
        }
        exchange.remove(OleNGConstants.ITEM_RECORD);
        return itemRecordAndDataMappings;
    }

    private List<ItemRecordAndDataMapping> createItemAndDataMappingForUnassignedMappings(HoldingsRecord holdingsRecord, JSONArray unAssignedDataMappings) {
        List<ItemRecordAndDataMapping> itemRecordAndDataMappings = new ArrayList<ItemRecordAndDataMapping>();
        if (unAssignedDataMappings.length() > 0) {
            for (int index=0; index < unAssignedDataMappings.length() ; index++) {
                JSONObject dataMapping = getJSONObjectFromJsonArray(unAssignedDataMappings,index);
                ItemRecordAndDataMapping itemRecordAndDataMapping = getNewItemRecord(holdingsRecord);
                itemRecordAndDataMapping.setDataMapping(dataMapping);
                itemRecordAndDataMappings.add(itemRecordAndDataMapping);
            }
        }
        return itemRecordAndDataMappings;
    }

    private List<JSONObject> getDataMappingForItemMatchedWithHoldingsMapping(JSONObject holdingsDataMapping, JSONArray itemDataMappings, JSONArray actionOps) {
        List<JSONObject> matchedMappings = new ArrayList<JSONObject>();
        try {
            String itemLinkField = getLinkFieldForItem(actionOps);
            if (StringUtils.isNotBlank(itemLinkField)) {
                for (int index = 0; index < itemDataMappings.length(); index++) {
                    JSONObject dataMapping = itemDataMappings.getJSONObject(index);
                    if (dataMapping.has(itemLinkField)) {
                        JSONArray valueArray = dataMapping.getJSONArray(itemLinkField);
                        for (int valueIndex = 0; valueIndex < valueArray.length(); valueIndex++) {
                            String itemLinkFieldValue = valueArray.getString(valueIndex);
                            JSONArray holdingsValueArray = holdingsDataMapping.getJSONArray(itemLinkField);
                            for (int holdingValueIndex = 0; holdingValueIndex < holdingsValueArray.length(); holdingValueIndex++) {
                                String holdingLinkFieldValue = holdingsValueArray.getString(holdingValueIndex);
                                if (itemLinkFieldValue.equalsIgnoreCase(holdingLinkFieldValue)) {
                                    dataMapping.put("assigned", Boolean.TRUE);
                                    matchedMappings.add(dataMapping);
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return matchedMappings;
    }

    private String getLinkFieldForItem(JSONArray actionOps) throws JSONException {
        String itemLinkField = null;
        for (int index = 0; index < actionOps.length(); index++) {
            JSONObject jsonObject = actionOps.getJSONObject(index);
            if (jsonObject.getString(OleNGConstants.DOC_TYPE).equalsIgnoreCase(OleNGConstants.ITEM)) {
                if (jsonObject.has(OleNGConstants.LINKFIELD)) {
                    itemLinkField = jsonObject.getString(OleNGConstants.LINKFIELD);
                    break;
                }
            }

        }
        return itemLinkField;
    }

    /**
     *
     * @param bibRecord
     * @param bibJSON
     * @param exchange
     *
     * This method prepares a list of holdings to be either created or updated.
     */
    private void preparePHoldingsRecord(BibRecord bibRecord, JSONObject bibJSON, Exchange exchange) {
        List<HoldingsRecordAndDataMapping> updatePHoldingsRecordAndDataMappings = new ArrayList<HoldingsRecordAndDataMapping>();

        List<HoldingsRecordAndDataMapping> createPHoldingsRecordAndDataMappings = new ArrayList<HoldingsRecordAndDataMapping>();

        JSONObject holdingsJSON = null;


        Map<String, Integer> numOccurances = new TreeMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);
        try {
            holdingsJSON = bibJSON.getJSONObject(OleNGConstants.HOLDINGS);
            numOccurances = getNumOccurrences(bibJSON);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String addedOps = getAddedOpsValue(bibJSON, OleNGConstants.HOLDINGS);
        JSONArray dataMappings = getDataMappingsJSONArray(holdingsJSON);

        List<HoldingsRecordAndDataMapping> holdingsRecordAndDataMappings = new ArrayList<HoldingsRecordAndDataMapping>();
        if (null == bibRecord.getBibId()) {
            for (int count = 0; count < numOccurances.get(OleNGConstants.HOLDINGS); count++) {
                HoldingsRecordAndDataMapping newHoldings = getNewHoldings(bibRecord, PHoldings.PRINT);
                try {
                    newHoldings.setDataMapping(dataMappings.getJSONObject(count));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                holdingsRecordAndDataMappings.add(newHoldings);
            }

        } else if (StringUtils.isBlank(addedOps)) {
            holdingsRecordAndDataMappings  = createHoldingAndDataMapping(bibRecord, PHoldings.PRINT, dataMappings);
        } else if (addedOps.equalsIgnoreCase(OleNGConstants.OVERLAY) || addedOps.equalsIgnoreCase(OleNGConstants.DISCARD)) {
            holdingsRecordAndDataMappings = determineHoldingsAndDataMappingsByMatchPoints(bibRecord, exchange, holdingsJSON, PHoldings.PRINT);
        } else if (addedOps.equalsIgnoreCase(OleNGConstants.DELETE_ALL_EXISTING_AND_ADD) ||
                addedOps.equalsIgnoreCase(OleNGConstants.KEEP_ALL_EXISTING_AND_ADD)) {
            holdingsRecordAndDataMappings  = createHoldingAndDataMapping(bibRecord, PHoldings.PRINT, dataMappings);
        }

        for (Iterator<HoldingsRecordAndDataMapping> iterator = holdingsRecordAndDataMappings.iterator(); iterator.hasNext(); ) {
            HoldingsRecordAndDataMapping holdingsRecordAndDataMapping = iterator.next();
            if (null == holdingsRecordAndDataMapping.getHoldingsRecord().getHoldingsId()) {
                createPHoldingsRecordAndDataMappings.add(holdingsRecordAndDataMapping);
            } else {
                updatePHoldingsRecordAndDataMappings.add(holdingsRecordAndDataMapping);
            }
        }
        exchange.add(OleNGConstants.HOLDINGS_FOR_CREATE, createPHoldingsRecordAndDataMappings);
        exchange.add(OleNGConstants.HOLDINGS_FOR_UPDATE, updatePHoldingsRecordAndDataMappings);
    }

    public void prepareEHoldingsRecord(BibRecord bibRecord, JSONObject bibJSON, Exchange exchange) {
        List<HoldingsRecordAndDataMapping> updateEHoldingsRecordAndDataMappings = new ArrayList<HoldingsRecordAndDataMapping>();

        List<HoldingsRecordAndDataMapping> createEHoldingsRecordAndDataMappings = new ArrayList<HoldingsRecordAndDataMapping>();

        JSONObject eholdingsJSON = null;

        Map<String, Integer> numOccurances = new TreeMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);
        try {
            eholdingsJSON = bibJSON.getJSONObject(OleNGConstants.EHOLDINGS);
            numOccurances = getNumOccurrences(bibJSON);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String addedOps = getAddedOpsValue(bibJSON, OleNGConstants.EHOLDINGS);
        JSONArray dataMappings = getDataMappingsJSONArray(eholdingsJSON);

        List<HoldingsRecordAndDataMapping> eholdingsRecordAndDataMappings = new ArrayList<HoldingsRecordAndDataMapping>();
        if (null == bibRecord.getBibId()) {
            for (int count = 0; count < numOccurances.get(OleNGConstants.EHOLDINGS); count++) {
                HoldingsRecordAndDataMapping newEHoldings = getNewHoldings(bibRecord, EHoldings.ELECTRONIC);

                try {
                    newEHoldings.setDataMapping(dataMappings.getJSONObject(count));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                eholdingsRecordAndDataMappings.add(newEHoldings);
            }

        } else if (StringUtils.isBlank(addedOps)) {
            eholdingsRecordAndDataMappings  = createHoldingAndDataMapping(bibRecord, EHoldings.ELECTRONIC, dataMappings);
        }  else if (addedOps.equalsIgnoreCase(OleNGConstants.OVERLAY) || addedOps.equalsIgnoreCase(OleNGConstants.DISCARD)) {
            eholdingsRecordAndDataMappings = determineHoldingsAndDataMappingsByMatchPoints(bibRecord, exchange, eholdingsJSON, EHoldings.ELECTRONIC);
        } else if (addedOps.equalsIgnoreCase(OleNGConstants.DELETE_ALL_EXISTING_AND_ADD) ||
                addedOps.equalsIgnoreCase(OleNGConstants.KEEP_ALL_EXISTING_AND_ADD)) {
            eholdingsRecordAndDataMappings  = createHoldingAndDataMapping(bibRecord, EHoldings.ELECTRONIC, dataMappings);
        }

        for (Iterator<HoldingsRecordAndDataMapping> iterator = eholdingsRecordAndDataMappings.iterator(); iterator.hasNext(); ) {
            HoldingsRecordAndDataMapping holdingsRecordAndDataMapping = iterator.next();
            if (null == holdingsRecordAndDataMapping.getHoldingsRecord().getHoldingsId()) {
                createEHoldingsRecordAndDataMappings.add(holdingsRecordAndDataMapping);
            } else {
                updateEHoldingsRecordAndDataMappings.add(holdingsRecordAndDataMapping);
            }
        }
        exchange.add(OleNGConstants.EHOLDINGS_FOR_CREATE, createEHoldingsRecordAndDataMappings);
        exchange.add(OleNGConstants.EHOLDINGS_FOR_UPDATE, updateEHoldingsRecordAndDataMappings);
    }

    private HoldingsRecordAndDataMapping getNewHoldings(BibRecord bibRecord, String holdingsType) {
        HoldingsRecordAndDataMapping holdingsRecordAndDataMapping = new HoldingsRecordAndDataMapping();

        HoldingsRecord holdingsRecord = new HoldingsRecord();
        holdingsRecord.setHoldingsType(holdingsType);
        holdingsRecord.setBibId(bibRecord.getBibId());
        holdingsRecord.setBibRecords(Collections.singletonList(bibRecord));
        holdingsRecordAndDataMapping.setHoldingsRecord(holdingsRecord);
        return holdingsRecordAndDataMapping;
    }

    public List<HoldingsRecordAndDataMapping> determineHoldingsAndDataMappingsByMatchPoints(BibRecord bibRecord, Exchange exchange, JSONObject holdingsJSON, String docType) {
        List<HoldingsRecordAndDataMapping> holdingsRecordAndDataMappings = new ArrayList<HoldingsRecordAndDataMapping>();

        List<HoldingsRecord> holdingsForBib = bibRecord.getHoldingsRecords();

        JSONArray clonedDataMapping = getClonedDataMappingsJSONArray(holdingsJSON);

        Map<String, Map<String, List<HoldingsRecordAndDataMapping>>> matchedHoldingsByMatchPoint = new HashMap<String, Map<String, List<HoldingsRecordAndDataMapping>>>();

        for (Iterator<HoldingsRecord> iterator = holdingsForBib.iterator(); iterator.hasNext(); ) {
            HoldingsRecord holdingsRecord = iterator.next();
            if (holdingsRecord.getHoldingsType().equals(docType)) {

                if (holdingsJSON.has(OleNGConstants.MATCH_POINT)) {
                    JSONObject matchPoints = getJsonObject(holdingsJSON, OleNGConstants.MATCH_POINT);

                    HashMap map = getHashMapFromObjectMapper(matchPoints);

                    for (Iterator matchPointIterator = map.keySet().iterator(); matchPointIterator.hasNext(); ) {
                        String key = (String) matchPointIterator.next();

                        for (Iterator<HoldingsHandler> holdingsMetaDataHandlerIterator = getHoldingMetaDataHandlersIterator(docType); holdingsMetaDataHandlerIterator.hasNext(); ) {
                            HoldingsHandler holdingsMetadataHandler = holdingsMetaDataHandlerIterator.next();

                            if (holdingsMetadataHandler.isInterested(key)) {
                                exchange.add(OleNGConstants.HOLDINGS_RECORD, holdingsRecord);
                                holdingsMetadataHandler.setOleDsNGMemorizeService(oleDsNGMemorizeService);
                                holdingsMetadataHandler.process(matchPoints, exchange);

                                Object match = exchange.get(OleNGConstants.MATCHED_HOLDINGS);

                                if (null != match && match.equals(Boolean.TRUE)) {
                                    HoldingsRecordAndDataMapping holdingsRecordAndDataMapping = null;
                                    holdingsRecordAndDataMapping = new HoldingsRecordAndDataMapping();
                                    holdingsRecordAndDataMapping.setHoldingsRecord(holdingsRecord);
                                    String matchedValue = (String) exchange.get(OleNGConstants.MATCHED_VALUE);
                                    JSONObject filterdDataMapping = findDataMappingByValue(clonedDataMapping, key, matchedValue);
                                    holdingsRecordAndDataMapping.setDataMapping(filterdDataMapping);

                                    if(matchedHoldingsByMatchPoint.containsKey(key)) {
                                        Map<String, List<HoldingsRecordAndDataMapping>> matchPointValueAndHoldings = matchedHoldingsByMatchPoint.get(key);
                                        if (!matchPointValueAndHoldings.containsKey(matchedValue)) {
                                            ArrayList matchedHoldingsForKey = new ArrayList();
                                            matchedHoldingsForKey.add(holdingsRecordAndDataMapping);
                                            matchPointValueAndHoldings.put(matchedValue, matchedHoldingsForKey);
                                        } else {
                                            List<HoldingsRecordAndDataMapping> holdingsRecordAndDataMappings1 = matchPointValueAndHoldings.get(matchedValue);
                                            holdingsRecordAndDataMappings1.add(holdingsRecordAndDataMapping);
                                        }
                                    } else {
                                        Map<String, List<HoldingsRecordAndDataMapping>> matchPointValueAndHoldings = new HashMap<String, List<HoldingsRecordAndDataMapping>>();
                                        ArrayList matchedHoldingsForKey = new ArrayList();
                                        matchedHoldingsForKey.add(holdingsRecordAndDataMapping);
                                        matchPointValueAndHoldings.put(matchedValue, matchedHoldingsForKey);
                                        matchedHoldingsByMatchPoint.put(key, matchPointValueAndHoldings);
                                    }

                                    exchange.remove(OleNGConstants.MATCHED_HOLDINGS);
                                    exchange.remove(OleNGConstants.MATCHED_VALUE);
                                }
                            }
                        }
                    }
                }
            }

            exchange.remove(OleNGConstants.HOLDINGS_RECORD);
        }

        for (Iterator<String> holdingsRecordIterator = matchedHoldingsByMatchPoint.keySet().iterator(); holdingsRecordIterator.hasNext(); ) {
            String key = holdingsRecordIterator.next();
            Map<String, List<HoldingsRecordAndDataMapping>> matchPointValueAndHoldings = matchedHoldingsByMatchPoint.get(key);
            for (Iterator<String> iterator = matchPointValueAndHoldings.keySet().iterator(); iterator.hasNext(); ) {
                String valueKey = iterator.next();
                if (matchPointValueAndHoldings.get(valueKey).size() == 1) {
                    holdingsRecordAndDataMappings.add(matchPointValueAndHoldings.get(valueKey).get(0));
                } else {
                    if (docType.equalsIgnoreCase(PHoldings.PRINT)) {
                        Integer multipleMatchedHoldings = (Integer) exchange.get("multipleMatchedHoldings");
                        if (null != multipleMatchedHoldings) {
                            multipleMatchedHoldings = multipleMatchedHoldings + matchPointValueAndHoldings.size();
                        } else {
                            multipleMatchedHoldings = matchPointValueAndHoldings.size();
                        }
                        exchange.add("multipleMatchedHoldings", multipleMatchedHoldings);
                    } else if (docType.equalsIgnoreCase(EHoldings.ELECTRONIC)) {
                        Integer multipleMatchedEHoldings = (Integer) exchange.get("multipleMatchedEHoldings");
                        if (null != multipleMatchedEHoldings) {
                            multipleMatchedEHoldings = multipleMatchedEHoldings + matchPointValueAndHoldings.size();
                        } else {
                            multipleMatchedEHoldings = matchPointValueAndHoldings.size();
                        }
                        exchange.add("multipleMatchedEHoldings", multipleMatchedEHoldings);
                    }
                }
            }

        }

        JSONArray unAssignedDataMappings = filterUnAssignedDataMappings(clonedDataMapping);
        List<HoldingsRecordAndDataMapping> holdingAndDataMappingForUnassignedDataMapping = createHoldingAndDataMapping(bibRecord, docType, unAssignedDataMappings);
        if(CollectionUtils.isNotEmpty(holdingAndDataMappingForUnassignedDataMapping)) {
            holdingsRecordAndDataMappings.addAll(holdingAndDataMappingForUnassignedDataMapping);
        }

        return holdingsRecordAndDataMappings;
    }

    private List<HoldingsRecordAndDataMapping> createHoldingAndDataMapping(BibRecord bibRecord, String docType, JSONArray datamappings) {
        List<HoldingsRecordAndDataMapping> holdingsRecordAndDataMappings = new ArrayList<HoldingsRecordAndDataMapping>();

        for(int index = 0; index < datamappings.length(); index++){
            JSONObject dataMapping = getJSONObjectFromJsonArray(datamappings,index);
            HoldingsRecordAndDataMapping newEHoldings = getNewHoldings(bibRecord, docType);
            newEHoldings.setDataMapping(dataMapping);
            holdingsRecordAndDataMappings.add(newEHoldings);
        }
        return holdingsRecordAndDataMappings;
    }

    private HashMap getHashMapFromObjectMapper(JSONObject jsonObject) {
        if (null != jsonObject) {
            try {
                return getObjectMapper().readValue(jsonObject.toString(), new TypeReference<Map<String, String>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new HashMap();
    }

    private JSONObject getJsonObject(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getJSONObject(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Iterator<HoldingsHandler> getHoldingMetaDataHandlersIterator(String docType) {
        if (docType.equalsIgnoreCase(PHoldings.PRINT)) {
            return getHoldingMetaDataHandlers().iterator();
        } else if (docType.equalsIgnoreCase(EHoldings.ELECTRONIC)) {
            return getEholdingMetaDataHandlers().iterator();
        }
        return null;
    }

    private JSONArray getDataMappingsJSONArray(JSONObject jsonObject) {
        JSONArray dataMappings = new JSONArray();
        if (jsonObject.has(OleNGConstants.DATAMAPPING)) {
            try {
                dataMappings = jsonObject.getJSONArray(OleNGConstants.DATAMAPPING);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dataMappings;
    }


    private JSONArray getClonedDataMappingsJSONArray(JSONObject jsonObject) {
        JSONArray dataMappings = null;
        JSONArray clonedDataMappings = null;
        if (jsonObject.has(OleNGConstants.DATAMAPPING)) {
            try {
                dataMappings = jsonObject.getJSONArray(OleNGConstants.DATAMAPPING);
                if (null != dataMappings) {
                    clonedDataMappings = new JSONArray(dataMappings.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return clonedDataMappings;
    }


    private JSONArray filterUnAssignedDataMappings(JSONArray dataMappings) {
        JSONArray unAssignedDataMappings = new JSONArray();
        if (null != dataMappings) {
            for (int index = 0; index < dataMappings.length(); index++) {
                try {
                    JSONObject datamapping = dataMappings.getJSONObject(index);
                    if (!datamapping.has("assigned")) {
                        unAssignedDataMappings.put(datamapping);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return unAssignedDataMappings;
    }

    /**
     * @param bibJSON
     * @return
     * @throws JSONException The method calculates the num occurrences of a tag in the given bib record. i.e. how many times
     *                       has 856 tag appears. Primarily for setting up how many holdings/e-holdings/items to create.
     */
    private Map<String, Integer> getNumOccurrences(JSONObject bibJSON) throws JSONException {
        Map numOccurances = new TreeMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);
        numOccurances.put(OleNGConstants.HOLDINGS, 1);
        numOccurances.put(OleNGConstants.EHOLDINGS, 1);
        numOccurances.put(OleNGConstants.ITEM, 1);

        JSONArray actionOps;
        List<Record> marcRecords;
        if (bibJSON.has(OleNGConstants.ACTION_OPS)) {
            actionOps = bibJSON.getJSONArray(OleNGConstants.ACTION_OPS);
        } else {
            return numOccurances;
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

                    if (StringUtils.isNotBlank(dataField)) {
                        Integer count = getMarcRecordUtil().getNumOccurances(marcRecords.get(0), dataField, ind1, ind2, subField);
                        if(count != null && count > 0) {
                            numOccurances.put(docType, count);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return numOccurances;
    }

    private BibRecord prepareBib(JSONObject bibJSONDataObject, Exchange exchange) {
        BibRecord bibRecord = new BibRecord();
        if (bibJSONDataObject.has(OleNGConstants.ID)) {
            try {
                bibRecord = oleDsNGMemorizeService.getBibDAO().retrieveBibById(bibJSONDataObject.getString(OleNGConstants.ID));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(null == bibRecord) {
                exchange.add(OleNGConstants.BIB_NOT_AVAILABLE, Boolean.TRUE);
                addFailureReportToExchange(bibJSONDataObject, exchange,"bib", new ValidationException("Bib record is not available in database. " +
                        "Solr and databasse are not in sync."), null);
            }
        }

        return bibRecord;
    }

    private JSONObject findDataMappingByValue(JSONArray dataMappings, String type, String value) {
        if (null != dataMappings) {
            for (int index = 0; index < dataMappings.length(); index++) {
                JSONObject dataMapping = null;
                try {
                    dataMapping = dataMappings.getJSONObject(index);
                    if (dataMapping.has(OleNGConstants.MATCHPOINT_FOR_DATAMAPPING)) {
                        JSONObject matchpointForDataMapping = dataMapping.getJSONObject(OleNGConstants.MATCHPOINT_FOR_DATAMAPPING);
                        if (matchpointForDataMapping.has(type)) {
                            JSONArray dataMappingValues = matchpointForDataMapping.getJSONArray(type);
                            for (int innerIndex = 0; innerIndex < dataMappingValues.length(); innerIndex++) {
                                String dataMappingValue = dataMappingValues.getString(innerIndex);
                                if (dataMappingValue.equalsIgnoreCase(value)) {
                                    dataMapping.put("assigned", Boolean.TRUE);
                                    return dataMapping;
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private String getAddedOpsValue(JSONObject jsonObject, String docType) {
        JSONObject addedOps = getJSONObjectFromJSONObject(jsonObject, OleNGConstants.ADDED_OPS);
        return getStringValueFromJsonObject(addedOps,docType);
    }
}
