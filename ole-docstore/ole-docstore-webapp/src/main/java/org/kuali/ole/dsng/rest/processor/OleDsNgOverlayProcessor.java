package org.kuali.ole.dsng.rest.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
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
import org.kuali.ole.dsng.rest.handler.holdings.CallNumberHandler;
import org.kuali.ole.dsng.rest.handler.holdings.CallNumberPrefixHandler;
import org.kuali.ole.dsng.rest.handler.holdings.CallNumberTypeHandler;
import org.kuali.ole.dsng.rest.handler.holdings.CopyNumberHandler;
import org.kuali.ole.dsng.rest.handler.holdings.*;
import org.kuali.ole.dsng.rest.handler.holdings.LocationHandler;
import org.kuali.ole.dsng.rest.handler.items.*;
import org.kuali.ole.dsng.util.OleDsHelperUtil;
import org.marc4j.marc.Record;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.text.ParseException;
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

    private List<HoldingsOverlayHandler> holdingsMatchPointHandlers;
    private List<ItemOverlayHandler> itemOverlayMatchPointHandlers;

    private List<Handler> bibHandlers;

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

    public String processBibAndHoldingsAndItems(String jsonBody) {
        JSONArray responseJsonArray = null;
        Map<String,SolrInputDocument> solrInputDocumentMap = new HashedMap();
        try {
            JSONArray requestJsonArray = new JSONArray(jsonBody);
            responseJsonArray = new JSONArray();
            for (int index = 0; index < requestJsonArray.length(); index++) {
                JSONObject requestJsonObject = requestJsonArray.getJSONObject(index);
                Exchange exchange = new Exchange();

                String overlayOps = requestJsonObject.getString("overlayOps");

                for (Iterator<Handler> iterator = bibHandlers.iterator(); iterator.hasNext(); ) {
                    Handler handler = iterator.next();
                    if(handler.isInterested(overlayOps)){
                        handler.process(requestJsonObject, exchange);
                    }
                }
                solrInputDocumentMap = getBibIndexer().getInputDocumentForBib((BibRecord) exchange.get("bib"), solrInputDocumentMap);
            }


            List<SolrInputDocument> solrInputDocuments = getBibIndexer().getSolrInputDocumentListFromMap(solrInputDocumentMap);
            getBibIndexer().commitDocumentToSolr(solrInputDocuments);

        }catch (JSONException e) {
            e.printStackTrace();
        }
        return responseJsonArray.toString();
    }

    private Map<String, SolrInputDocument> processHoldings(Map<String, SolrInputDocument> solrInputDocumentMap, JSONObject jsonObject,
                                                           String updatedBy, Timestamp updatedDate, List<HoldingsRecord> holdingsRecords) throws JSONException {
        for (Iterator<HoldingsRecord> iterator = holdingsRecords.iterator(); iterator.hasNext(); ) {
            HoldingsRecord holdingsRecord = iterator.next();
            JSONObject holdingJsonObject = jsonObject.getJSONObject("holdings");
            if (holdingJsonObject.has("matchPoints")) {
                JSONObject matchPoints = holdingJsonObject.getJSONObject("matchPoints");
                boolean isMatchingHolding = false;
                for (Iterator<HoldingsOverlayHandler> holdingsRecordIterator = getHoldingsMatchPointHandlers().iterator(); holdingsRecordIterator.hasNext(); ) {
                    HoldingsOverlayHandler holdingsOverlayHandler = holdingsRecordIterator.next();
                    if(holdingsOverlayHandler.isInterested(matchPoints)){
                        isMatchingHolding = true;
                        boolean matching = holdingsOverlayHandler.isMatching(holdingsRecord, matchPoints);
                        if (!matching) {
                            isMatchingHolding = false;
                            break;
                        }
                    }
                }
                if(isMatchingHolding){
                    if (holdingJsonObject.has("dataMapping")) {
                        JSONObject dataMapping = holdingJsonObject.getJSONObject("dataMapping");
                        boolean isUpdated = false;
                        for (Iterator<HoldingsOverlayHandler> holdingsRecordIterator = getHoldingsMatchPointHandlers().iterator(); holdingsRecordIterator.hasNext(); ) {
                            HoldingsOverlayHandler holdingsOverlayHandler = holdingsRecordIterator.next();
                            if(holdingsOverlayHandler.isInterested(dataMapping)){
                                holdingsRecord = holdingsOverlayHandler.process(holdingsRecord, dataMapping);
                                isUpdated = true;
                            }
                        }
                        if (isUpdated) {
                            holdingsRecord.setUpdatedBy(updatedBy);
                            holdingsRecord.setUpdatedDate(updatedDate);
                            HoldingsRecord updatedHoldingsRecord = holdingDAO.save(holdingsRecord);
                            solrInputDocumentMap = getHoldingIndexer().getInputDocumentForHoldings(holdingsRecord, solrInputDocumentMap);
                        }
                    }

                    // Processing Items
                    List<ItemRecord> itemRecords = holdingsRecord.getItemRecords();
                    if(CollectionUtils.isNotEmpty(itemRecords) && jsonObject.has("items")){
                        solrInputDocumentMap = processItems(solrInputDocumentMap, jsonObject, updatedBy, updatedDate, itemRecords);
                    }
                }
            }
        }
        return solrInputDocumentMap;
    }

    private Map<String, SolrInputDocument> processItems(Map<String, SolrInputDocument> solrInputDocumentMap, JSONObject jsonObject, String updatedBy, Timestamp updatedDate, List<ItemRecord> itemRecords) throws JSONException {
        JSONObject itemJsonObject = jsonObject.getJSONObject("items");
        if (itemJsonObject.has("matchPoints")) {
            JSONObject matchPoints = itemJsonObject.getJSONObject("matchPoints");
            for (Iterator<ItemRecord> itemRecordIterator = itemRecords.iterator(); itemRecordIterator.hasNext(); ) {
                ItemRecord itemRecord = itemRecordIterator.next();
                boolean isMatchingItem = false;
                for (Iterator<ItemOverlayHandler> iterator = getItemOverlayMatchPointHandlers().iterator(); iterator.hasNext(); ) {
                    ItemOverlayHandler itemOverlayHandler = iterator.next();
                    if(itemOverlayHandler.isInterested(matchPoints)){
                        isMatchingItem = true;
                        boolean matching = itemOverlayHandler.isMatching(itemRecord, matchPoints);
                        if(!matching){
                            isMatchingItem = false;
                            break;
                        }
                    }
                }
                if(isMatchingItem) {
                    if (itemJsonObject.has("dataMapping")) {
                        JSONObject dataMapping = itemJsonObject.getJSONObject("dataMapping");
                        boolean isUpdated = false;
                        for (Iterator<ItemOverlayHandler> iterator = getItemOverlayMatchPointHandlers().iterator(); iterator.hasNext(); ) {
                            ItemOverlayHandler itemOverlayHandler = iterator.next();
                            if(itemOverlayHandler.isInterested(dataMapping)){
                                itemRecord = itemOverlayHandler.process(itemRecord, dataMapping);
                                isUpdated = true;
                            }
                        }
                        if (isUpdated) {
                            itemRecord.setUpdatedBy(updatedBy);
                            itemRecord.setUpdatedDate(updatedDate);
                            ItemRecord updatedItemRecord = itemDAO.save(itemRecord);
                            solrInputDocumentMap = getItemIndexer().getInputDocumentForItem(itemRecord, solrInputDocumentMap);
                        }
                    }
                }
            }
        }
        return solrInputDocumentMap;
    }

    private String doCustomProcessForProfile(String marcContent,String bibId) {
        MarcXMLConverter marcXMLConverter = new MarcXMLConverter();
        List<Record> records = marcXMLConverter.convertMarcXmlToRecord(marcContent);
        if(CollectionUtils.isNotEmpty(records)) {
            for (Iterator<Record> iterator = records.iterator(); iterator.hasNext(); ) {
                Record record = iterator.next();
                //update 001 value by bibId
                getMarcRecordUtil().updateControlFieldValue(record,"001", bibId);
                return marcXMLConverter.generateMARCXMLContent(record);
            }
        }
        return marcContent;
    }

    private Timestamp getDateTimeStamp(String updatedDateString) {
        Timestamp timeStamp = null;
        try {
            Date parse = DocstoreConstants.DOCSTORE_DATE_FORMAT.parse(updatedDateString);
            if (null != parse) {
                timeStamp = new Timestamp(parse.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
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

    public List<HoldingsOverlayHandler> getHoldingsMatchPointHandlers() {
        if(CollectionUtils.isEmpty(holdingsMatchPointHandlers)) {
            holdingsMatchPointHandlers = new ArrayList<HoldingsOverlayHandler>();
            holdingsMatchPointHandlers.add(new CallNumberHandler());
            holdingsMatchPointHandlers.add(new CallNumberPrefixHandler());
            holdingsMatchPointHandlers.add(new CallNumberTypeHandler());
            holdingsMatchPointHandlers.add(new CopyNumberHandler());
            holdingsMatchPointHandlers.add(new LocationHandler());
        }
        return holdingsMatchPointHandlers;
    }

    public List<ItemOverlayHandler> getItemOverlayMatchPointHandlers() {
        if(CollectionUtils.isEmpty(itemOverlayMatchPointHandlers)) {
            itemOverlayMatchPointHandlers = new ArrayList<ItemOverlayHandler>();
            itemOverlayMatchPointHandlers.add(new org.kuali.ole.dsng.rest.handler.items.CallNumberHandler());
            itemOverlayMatchPointHandlers.add(new org.kuali.ole.dsng.rest.handler.items.CallNumberPrefixHandler());
            itemOverlayMatchPointHandlers.add(new org.kuali.ole.dsng.rest.handler.items.CallNumberTypeHandler());
            itemOverlayMatchPointHandlers.add(new ChronologyHandler());
            itemOverlayMatchPointHandlers.add(new org.kuali.ole.dsng.rest.handler.items.CopyNumberHandler());
            itemOverlayMatchPointHandlers.add(new DonorCodeHandler());
            itemOverlayMatchPointHandlers.add(new DonorNoteHandler());
            itemOverlayMatchPointHandlers.add(new DonorPublicDisplayHandler());
            itemOverlayMatchPointHandlers.add(new EnumerationHandler());
            itemOverlayMatchPointHandlers.add(new ItemBarcodeHandler());
            itemOverlayMatchPointHandlers.add(new ItemStatusHandler());
            itemOverlayMatchPointHandlers.add(new ItemTypeHandler());
            itemOverlayMatchPointHandlers.add(new org.kuali.ole.dsng.rest.handler.items.LocationHandler());
            itemOverlayMatchPointHandlers.add(new StatisticalSearchCodeHandler());
            itemOverlayMatchPointHandlers.add(new VendorLineItemIdHandler());
        }
        return itemOverlayMatchPointHandlers;
    }
}
