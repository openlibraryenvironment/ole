package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.model.ItemRecordAndDataMapping;
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
        List<String> operationsList = getListFromJSONArray(operation);
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
        List<ItemRecordAndDataMapping> itemRecordAndDataMappings = (List<ItemRecordAndDataMapping>) exchange.get(OleNGConstants.ITEMS_FOR_UPDATE);
        Set<String> discardedHoldingsForAdditionalOverlayOps = (Set<String>) exchange.get(OleNGConstants.DISCARDED_HOLDINGS_FOR_ADDITIONAL_OVERLAY_OPS);
        List<ItemRecord> itemRecords = new ArrayList<ItemRecord>();
        if (CollectionUtils.isNotEmpty(itemRecordAndDataMappings)) {

                String updatedDateString = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.UPDATED_DATE);
                Timestamp updatedDate = getDateTimeStamp(updatedDateString);
                String updatedBy = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.UPDATED_BY);

                for (Iterator<ItemRecordAndDataMapping> iterator = itemRecordAndDataMappings.iterator(); iterator.hasNext(); ) {
                    try {
                        ItemRecordAndDataMapping itemRecordAndDataMapping = iterator.next();
                        ItemRecord itemRecord = itemRecordAndDataMapping.getItemRecord();
                        String holdingsId = itemRecord.getHoldingsId();
                        if (!isDiscardedByAdditionalOverlayOps(discardedHoldingsForAdditionalOverlayOps, holdingsId)) {
                            JSONObject dataMapping = itemRecordAndDataMapping.getDataMapping();
                            itemRecord.setUpdatedBy(updatedBy);
                            itemRecord.setUpdatedDate(updatedDate);
                            if(itemRecord.getStaffOnlyFlag()==null){
                                itemRecord.setStaffOnlyFlag(false);
                            }
                            exchange.add(OleNGConstants.ITEM_RECORD, itemRecord);
                            if (null != dataMapping) {
                                processOverlay(exchange, dataMapping, itemRecord);
                            }
                            itemRecords.add(itemRecord);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        addFailureReportToExchange(requestJsonObject, exchange, OleNGConstants.NO_OF_FAILURE_ITEM, e, 1);
                    }
                }

            exchange.remove(OleNGConstants.ITEM_RECORD);
            try {
                getOleDsNGMemorizeService().getItemDAO().saveAll(itemRecords);
                for (Iterator<ItemRecord> iterator = itemRecords.iterator(); iterator.hasNext(); ) {
                    ItemRecord itemRecord = iterator.next();
                    itemRecord.setOperationType(OleNGConstants.UPDATED);
                }
            } catch (Exception e) {
                e.printStackTrace();
                addFailureReportToExchange(requestJsonObject, exchange, OleNGConstants.NO_OF_FAILURE_ITEM, e , itemRecords.size());
            }
        }
    }

    private ItemRecord processOverlay(Exchange exchange, JSONObject dataMapping, ItemRecord itemRecord) throws JSONException, IOException {
        Map<String, Object> dataMappingsMap = new ObjectMapper().readValue(dataMapping.toString(), new TypeReference<Map<String, Object>>() {});
        for (Iterator dataMappingsIterator = dataMappingsMap.keySet().iterator(); dataMappingsIterator.hasNext(); ) {
            String key1 = (String) dataMappingsIterator.next();
            for (Iterator<ItemHandler> itemMetaDataHandlerIterator = getItemMetaDataHandlers().iterator(); itemMetaDataHandlerIterator.hasNext(); ) {
                ItemHandler itemMetaDataHandlelr = itemMetaDataHandlerIterator.next();
                if (itemMetaDataHandlelr.isInterested(key1)) {
                    itemMetaDataHandlelr.setBusinessObjectService(getBusinessObjectService());
                    itemMetaDataHandlelr.processDataMappings(dataMapping, exchange);
                }
            }
        }
        itemRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_ITEM_OLEML);
        return itemRecord;
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
            itemMetaDataHandlers.add(new EnumerationHandler());
            itemMetaDataHandlers.add(new ItemBarcodeHandler());
            itemMetaDataHandlers.add(new ItemStatusHandler());
            itemMetaDataHandlers.add(new ItemTypeHandler());
            itemMetaDataHandlers.add(new NoOfPiecesHandler());
            itemMetaDataHandlers.add(new ItemLocationHandler());
            itemMetaDataHandlers.add(new ItemHoldingLocationHandler());
            itemMetaDataHandlers.add(new StatisticalSearchCodeHandler());
            itemMetaDataHandlers.add(new VendorLineItemIdHandler());
            itemMetaDataHandlers.add(new ItemStaffOnlyHandler());
            itemMetaDataHandlers.add(new ShelvingOrderHandler());
            itemMetaDataHandlers.add(new PublicNoteHandler());
        }
        return itemMetaDataHandlers;
    }

    public void setItemMetaDataHandlers(List<ItemHandler> itemMetaDataHandlers) {
        this.itemMetaDataHandlers = itemMetaDataHandlers;
    }
}
