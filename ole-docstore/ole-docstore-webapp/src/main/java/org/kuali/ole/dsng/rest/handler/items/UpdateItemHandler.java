package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by SheikS on 12/26/2015.
 */
public class UpdateItemHandler extends Handler {

    List<ItemHandler> itemMetaDataHandlers;

    @Override
    public Boolean isInterested(String operation) {
        List<String> operationsList = getOperationsList(operation);
        for (Iterator iterator = operationsList.iterator(); iterator.hasNext(); ) {
            String op = (String) iterator.next();
            if (op.equals("132") || op.equals("232")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        List<ItemRecord> itemRecordsToUpdate = new ArrayList<ItemRecord>();
        try {
            String overlayOps = requestJsonObject.getString("overlayOps");
            String updatedDateString = getStringValueFromJsonObject(requestJsonObject, "updatedDate");
            Timestamp updatedDate = getDateTimeStamp(updatedDateString);
            String updatedBy = getStringValueFromJsonObject(requestJsonObject,"updatedBy");

            List<HoldingsRecord> holdingsRecordsToUpdate = (List<HoldingsRecord>) exchange.get("holdingRecordsToUpdate");
            if(CollectionUtils.isNotEmpty(holdingsRecordsToUpdate)) {
                for (Iterator<HoldingsRecord> holdingsRecordIterator = holdingsRecordsToUpdate.iterator(); holdingsRecordIterator.hasNext(); ) {
                    HoldingsRecord holdingsRecord = holdingsRecordIterator.next();
                    List<ItemRecord> itemRecords = holdingsRecord.getItemRecords();
                    boolean isItemMatched = false;
                    if (CollectionUtils.isNotEmpty(itemRecords)) {
                        for (Iterator<ItemRecord> iterator = itemRecords.iterator(); iterator.hasNext(); ) {
                            JSONObject itemJsonObject = requestJsonObject.getJSONObject("items");
                            ItemRecord itemRecord = iterator.next();
                            if (itemJsonObject.has("matchPoints")) {
                                JSONObject matchPoints = itemJsonObject.getJSONObject("matchPoints");
                                HashMap map = new ObjectMapper().readValue(matchPoints.toString(), new TypeReference<Map<String, String>>() {
                                });


                                exchange.add("itemRecord",itemRecord);

                                matchPointsLoop:
                                for (Iterator iterator1 = map.keySet().iterator(); iterator1.hasNext(); ) {
                                    String key = (String) iterator1.next();
                                    for (Iterator<ItemHandler> iterator2 = getItemMetaDataHandlers().iterator(); iterator2.hasNext(); ) {
                                        Handler itemMetaDataHandlelr = iterator2.next();
                                        if (itemMetaDataHandlelr.isInterested(key)) {
                                            itemMetaDataHandlelr.process(matchPoints, exchange);
                                            if (null != exchange.get("matchedItem")) {
                                                isItemMatched = true;
                                                itemRecord.setUpdatedBy(updatedBy);
                                                itemRecord.setUpdatedDate(updatedDate);
                                                ItemRecord record = processOverlay(exchange, itemJsonObject, itemRecord);
                                                itemRecordsToUpdate.add(record);
                                                break matchPointsLoop;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        getItemDAO().saveAll(itemRecordsToUpdate);
                        List itemsToUpdate = (List) exchange.get("itemRecordsToUpdate");
                        if(null == itemsToUpdate) {
                            itemsToUpdate = new ArrayList();
                        }
                        itemsToUpdate.addAll(itemRecordsToUpdate);

                        exchange.add("itemRecordsToUpdate",itemsToUpdate);
                    }
                    if(!isItemMatched) {
                        createItem(requestJsonObject, exchange, overlayOps, holdingsRecord);
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

    private void createItem(JSONObject requestJsonObject, Exchange exchange, String overlayOps, HoldingsRecord holdingsRecord) {
        CreateItemHandler createItemHandler = new CreateItemHandler();
        if(createItemHandler.isInterested(overlayOps)) {
            exchange.add("holdings",holdingsRecord);
            createItemHandler.setItemDAO(getItemDAO());
            createItemHandler.setBusinessObjectService(getBusinessObjectService());
            createItemHandler.process(requestJsonObject,exchange);
            exchange.remove("holdings");
        }
    }

    private ItemRecord processOverlay(Exchange exchange,JSONObject holdingJsonObject, ItemRecord itemRecord) throws JSONException, IOException {
        JSONObject dataMappings = holdingJsonObject.getJSONObject("dataMapping");

        HashMap dataMappingsMap = new ObjectMapper().readValue(dataMappings.toString(), new TypeReference<Map<String, String>>() {
        });
        for (Iterator iterator3 = dataMappingsMap.keySet().iterator(); iterator3.hasNext(); ) {
            String key1 = (String) iterator3.next();
            for (Iterator<ItemHandler> iterator4 = getItemMetaDataHandlers().iterator(); iterator4.hasNext(); ) {
                ItemHandler itemMetaDataHandlelr1 = iterator4.next();
                if (itemMetaDataHandlelr1.isInterested(key1)) {
                    itemMetaDataHandlelr1.setBusinessObjectService(getBusinessObjectService());
                    itemMetaDataHandlelr1.processDataMappings(dataMappings, exchange);
                }
            }
        }
        itemRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_ITEM_OLEML);
        exchange.remove("matchedItem");
        return itemRecord;
    }

    private Collection getCollletion(String values) {
        List queryValues = new ArrayList();

        StringTokenizer stringTokenizer = new StringTokenizer(values, ",");
        while (stringTokenizer.hasMoreTokens()) {
            queryValues.add(stringTokenizer.nextToken());
        }
        return queryValues;
    }



    public List<ItemHandler> getItemMetaDataHandlers() {
        if(null == itemMetaDataHandlers){
            itemMetaDataHandlers = new ArrayList<ItemHandler>();
            itemMetaDataHandlers.add(new CallNumberHandler());
            itemMetaDataHandlers.add(new CallNumberPrefixHandler());
            itemMetaDataHandlers.add(new CallNumberTypeHandler());
            itemMetaDataHandlers.add(new ChronologyHandler());
            itemMetaDataHandlers.add(new CopyNumberHandler());
            itemMetaDataHandlers.add(new DonorCodeHandler());
            itemMetaDataHandlers.add(new DonorNoteHandler());
            itemMetaDataHandlers.add(new DonorPublicDisplayHandler());
            itemMetaDataHandlers.add(new EnumerationHandler());
            itemMetaDataHandlers.add(new ItemBarcodeHandler());
            itemMetaDataHandlers.add(new ItemStatusHandler());
            itemMetaDataHandlers.add(new ItemTypeHandler());
            itemMetaDataHandlers.add(new ItemLocationHandler());
            itemMetaDataHandlers.add(new StatisticalSearchCodeHandler());
            itemMetaDataHandlers.add(new VendorLineItemIdHandler());
        }
        return itemMetaDataHandlers;
    }

    public void setItemMetaDataHandlers(List<ItemHandler> itemMetaDataHandlers) {
        this.itemMetaDataHandlers = itemMetaDataHandlers;
    }
}
