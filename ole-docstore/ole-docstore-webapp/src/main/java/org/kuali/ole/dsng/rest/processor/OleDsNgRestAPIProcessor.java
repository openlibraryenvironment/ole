package org.kuali.ole.dsng.rest.processor;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.common.exception.DocstoreResources;
import org.kuali.ole.docstore.common.exception.DocstoreValidationException;
import org.kuali.ole.docstore.common.response.DeleteFailureResponse;
import org.kuali.ole.docstore.common.response.OleNGBatchDeleteResponse;
import org.kuali.ole.docstore.engine.service.storage.rdbms.RdbmsBibDocumentManager;
import org.kuali.ole.docstore.engine.service.storage.rdbms.RdbmsHoldingsDocumentManager;
import org.kuali.ole.docstore.engine.service.storage.rdbms.RdbmsItemDocumentManager;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.indexer.BibIndexer;
import org.kuali.ole.dsng.indexer.HoldingIndexer;
import org.kuali.ole.dsng.indexer.ItemIndexer;
import org.kuali.ole.dsng.indexer.OleDsNgIndexer;
import org.kuali.ole.dsng.rest.handler.eholdings.CreateEHoldingsHandler;
import org.kuali.ole.dsng.rest.handler.holdings.CreateHoldingsHanlder;
import org.kuali.ole.dsng.rest.handler.items.CreateItemHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by SheikS on 11/25/2015.
 */
@Service("oleDsNgRestAPIProcessor")
public class OleDsNgRestAPIProcessor extends OleDsNgOverlayProcessor {

    public String createBib(String jsonBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        BibRecord bibRecord = objectMapper.readValue(jsonBody, BibRecord.class);
        oleDsNGMemorizeService.getBibDAO().save(bibRecord);
        OleDsNgIndexer bibIndexer = new BibIndexer();
        bibIndexer.indexDocument(bibRecord);
        return objectMapper.writeValueAsString(bibRecord);
    }


    public String updateBib(String jsonBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        BibRecord bibRecord = objectMapper.readValue(jsonBody, BibRecord.class);

        if(StringUtils.isNotBlank(bibRecord.getBibId())){
            oleDsNGMemorizeService.getBibDAO().save(bibRecord);
            BibIndexer bibIndexer = new BibIndexer();
            bibIndexer.updateDocument(bibRecord);
            return objectMapper.writeValueAsString(bibRecord);
        } else {
            DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.BIB_ID_NOT_FOUND, DocstoreResources.BIB_ID_NOT_FOUND);
            docstoreException.addErrorParams("bibId", bibRecord.getBibId());
            throw docstoreException;
        }

    }

    public String createHolding(String jsonBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HoldingsRecord holdingsRecord = objectMapper.readValue(jsonBody, HoldingsRecord.class);
        oleDsNGMemorizeService.getHoldingDAO().save(holdingsRecord);
        OleDsNgIndexer bibIndexer = new HoldingIndexer();
        bibIndexer.indexDocument(holdingsRecord);
        return objectMapper.writeValueAsString(holdingsRecord);
    }


    public String updateHolding(String jsonBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HoldingsRecord holdingsRecord = objectMapper.readValue(jsonBody, HoldingsRecord.class);

        if(StringUtils.isNotBlank(holdingsRecord.getHoldingsId())){
            oleDsNGMemorizeService.getHoldingDAO().save(holdingsRecord);
            OleDsNgIndexer holdingIndexer = new HoldingIndexer();
            holdingIndexer.updateDocument(holdingsRecord);
            return objectMapper.writeValueAsString(holdingsRecord);
        } else {
            DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.HOLDING_ID_NOT_FOUND, DocstoreResources.HOLDING_ID_NOT_FOUND);
            docstoreException.addErrorParams("holdingId", holdingsRecord.getHoldingsId());
            throw docstoreException;
        }

    }

    public String createItem(String jsonBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ItemRecord itemRecord = objectMapper.readValue(jsonBody, ItemRecord.class);
        oleDsNGMemorizeService.getItemDAO().save(itemRecord);
        OleDsNgIndexer itemIndexer = new ItemIndexer();
        itemIndexer.indexDocument(itemRecord);
        return objectMapper.writeValueAsString(itemRecord);
    }


    public String updateItem(String jsonBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ItemRecord itemRecord = objectMapper.readValue(jsonBody, ItemRecord.class);

        if(StringUtils.isNotBlank(itemRecord.getItemId())){
            oleDsNGMemorizeService.getItemDAO().save(itemRecord);
            OleDsNgIndexer itemIndexer = new ItemIndexer();
            itemIndexer.updateDocument(itemRecord);
            return objectMapper.writeValueAsString(itemRecord);
        } else {
            DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.ITEM_ID_NOT_FOUND, DocstoreResources.ITEM_ID_NOT_FOUND);
            docstoreException.addErrorParams("itemId", itemRecord.getItemId());
            throw docstoreException;
        }

    }

    public String createDummyHoldings(String body) {
        String serializedContent = null;
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJson = new JSONObject(body);
            String holdingsType = getStringValueFromJsonObject(requestJson, OleNGConstants.HOLDINGS_TYPE);
            JSONObject dataMappings = getJSONObjectFromJSONObject(requestJson, OleNGConstants.DATAMAPPING);
            if(StringUtils.isNotBlank(holdingsType)) {
                if(holdingsType.equalsIgnoreCase(EHoldings.ELECTRONIC)) {
                    serializedContent = createEHoldings(dataMappings);
                } else if(holdingsType.equalsIgnoreCase(PHoldings.PRINT)) {
                    serializedContent = createHoldings(dataMappings);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            responseObject.put(OleNGConstants.CONTENT, serializedContent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseObject.toString();
    }

    private String createEHoldings(JSONObject dataMappings) {
        HoldingsRecord holdingsRecord = new HoldingsRecord();
        holdingsRecord.setHoldingsType(EHoldings.ELECTRONIC);
        CreateEHoldingsHandler createEHoldingsHandler = new CreateEHoldingsHandler();
        holdingsRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML);

        Exchange exchange = new Exchange();
        exchange.add(OleNGConstants.HOLDINGS_RECORD, holdingsRecord);
        exchange.add(OleNGConstants.DATAMAPPING, dataMappings);
        try {
            createEHoldingsHandler.setOleDsNGMemorizeService(oleDsNGMemorizeService);
            createEHoldingsHandler.processDataMappings(dataMappings, exchange);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holdingsRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        RdbmsHoldingsDocumentManager rdbmsHoldingsDocumentManager = new RdbmsHoldingsDocumentManager();
        Holdings holdings = rdbmsHoldingsDocumentManager.buildHoldingsFromHoldingsRecord(holdingsRecord);
        holdings.serializeContent();
        String serializedContent = new EHoldings().serialize(holdings);
        return serializedContent;
    }

    private String createHoldings(JSONObject dataMappings) {
        HoldingsRecord holdingsRecord = new HoldingsRecord();
        holdingsRecord.setHoldingsType(PHoldings.PRINT);
        CreateHoldingsHanlder createHoldingsHandler = new CreateHoldingsHanlder();
        holdingsRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML);

        Exchange exchange = new Exchange();
        exchange.add(OleNGConstants.HOLDINGS_RECORD, holdingsRecord);
        exchange.add(OleNGConstants.DATAMAPPING, dataMappings);
        try {
            createHoldingsHandler.setOleDsNGMemorizeService(oleDsNGMemorizeService);
            createHoldingsHandler.processDataMappings(dataMappings, exchange);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holdingsRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        RdbmsHoldingsDocumentManager rdbmsHoldingsDocumentManager = new RdbmsHoldingsDocumentManager();
        Holdings holdings = rdbmsHoldingsDocumentManager.buildHoldingsFromHoldingsRecord(holdingsRecord);
        holdings.serializeContent();
        String serializedContent = new PHoldings().serialize(holdings);
        return serializedContent;
    }

    public String createDummyItem(String body) {
        String serializedContent = null;
        JSONObject responseObject = new JSONObject();
        try {
            JSONObject requestJson = new JSONObject(body);
            JSONObject dataMappings = getJSONObjectFromJSONObject(requestJson, OleNGConstants.DATAMAPPING);
            serializedContent = createItem(dataMappings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            responseObject.put(OleNGConstants.CONTENT, serializedContent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseObject.toString();
    }

    private String createItem(JSONObject dataMappings) {
        ItemRecord itemRecord = new ItemRecord();
        CreateItemHandler createItemHandler = new CreateItemHandler();
        itemRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_ITEM_OLEML);

        Exchange exchange = new Exchange();
        exchange.add(OleNGConstants.ITEM_RECORD, itemRecord);
        exchange.add(OleNGConstants.DATAMAPPING, dataMappings);
        try {
            createItemHandler.setOleDsNGMemorizeService(oleDsNGMemorizeService);
            createItemHandler.processDataMappings(dataMappings, exchange);
        } catch (Exception e) {
            e.printStackTrace();
        }

        itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
        RdbmsItemDocumentManager rdbmsHoldingsDocumentManager = new RdbmsItemDocumentManager();
        Item item = rdbmsHoldingsDocumentManager.buildItemContent(itemRecord);
        item.serializeContent();
        String serializedContent = new ItemOleml().serialize(item);
        return serializedContent;
    }

    public String processDeleteBibs(String jsonBody) throws IOException, JSONException {
        ObjectMapper objectMapper = new ObjectMapper();
        OleNGBatchDeleteResponse oleNGBatchDeleteResponse = new OleNGBatchDeleteResponse();
        JSONArray jsonArray = new JSONArray(jsonBody);
        RdbmsBibDocumentManager rdbmsBibDocumentManager = new RdbmsBibDocumentManager();
        Bib bib =new Bib();
        for (int index = 0; index < jsonArray.length(); index++) {
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            String bibId = (String) jsonObject.get(OleNGConstants.ID);
            bib = (Bib) rdbmsBibDocumentManager.retrieve(bibId);
            try {
                oleDsNGMemorizeService.getBibDAO().deleteBibTreeRecord(bibId);
            } catch (Exception e) {
                e.printStackTrace();
                oleNGBatchDeleteResponse.addFailureRecord(null, null, bibId, OleNGConstants.ERR_DELETING_FROM_DB + " :: " + e.toString());
                oleNGBatchDeleteResponse.getDeleteFailureResponseList().add(new DeleteFailureResponse(e.toString(), getDetailedMessage(e), null, bibId));
                continue;
            }
            try {
                OleDsNgIndexer bibIndexer = new BibIndexer();
                bibIndexer.deleteDocument(bibId);
                getSolrRequestReponseHandler().commitToServer();
                bibIndexer.saveDeletedBibInfo(bib);
                oleNGBatchDeleteResponse.addSuccessRecord(null, null, bibId, OleNGConstants.DELETED);
            } catch (Exception e) {
                e.printStackTrace();
                oleNGBatchDeleteResponse.addFailureRecord(null, null, bibId, OleNGConstants.ERR_DELETING_FROM_SOLR + " :: " + e.toString());
                oleNGBatchDeleteResponse.getDeleteFailureResponseList().add(new DeleteFailureResponse(e.toString(), getDetailedMessage(e), null, bibId));
            }
        }
        return objectMapper.writeValueAsString(oleNGBatchDeleteResponse);
    }
}

