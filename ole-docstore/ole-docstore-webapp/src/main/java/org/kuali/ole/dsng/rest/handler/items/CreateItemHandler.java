package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.model.ItemRecordAndDataMapping;
import org.kuali.ole.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/26/2015.
 */
public class CreateItemHandler extends Handler {

    @Override
    public Boolean isInterested(String operation) {
        List<String> operationsList = getListFromJSONArray(operation);
        for (Iterator iterator = operationsList.iterator(); iterator.hasNext(); ) {
            String op = (String) iterator.next();
            if (op.equals("131") || op.equals("231")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        List<ItemRecordAndDataMapping> itemRecordAndDataMappings = (List<ItemRecordAndDataMapping>) exchange.get(OleNGConstants.ITEMS_FOR_CREATE);
        List<ItemRecord> itemRecords = new ArrayList<ItemRecord>();

        if (CollectionUtils.isNotEmpty(itemRecordAndDataMappings)) {
            JSONObject itemJSONObject = getJSONObjectFromJSONObject(requestJsonObject, OleNGConstants.ITEM);
                for (Iterator<ItemRecordAndDataMapping> iterator = itemRecordAndDataMappings.iterator(); iterator.hasNext(); ) {
                    try {
                        ItemRecordAndDataMapping itemRecordAndDataMapping = iterator.next();
                        ItemRecord itemRecord = itemRecordAndDataMapping.getItemRecord();
                        JSONObject dataMapping = itemRecordAndDataMapping.getDataMapping();
                        exchange.add(OleNGConstants.DATAMAPPING,dataMapping);
                        itemRecord.setHoldingsId(itemRecord.getHoldingsRecord().getHoldingsId());
                        exchange.add(OleNGConstants.ITEM_RECORD, itemRecord);
                        processDataMappings(itemJSONObject, exchange);
                        setCommonValuesToItemRecord(requestJsonObject, itemRecord);
                        itemRecords.add(itemRecord);
                    } catch (Exception e) {
                        e.printStackTrace();
                        addFailureReportToExchange(requestJsonObject, exchange, OleNGConstants.NO_OF_FAILURE_ITEM, e.toString(),
                                "Problem while create item.", 1);
                    }
                }

            exchange.remove(OleNGConstants.ITEM_RECORD);
            exchange.remove(OleNGConstants.DATAMAPPING);
            try {
                getItemDAO().saveAll(itemRecords);
            } catch (Exception e) {
                e.printStackTrace();
                addFailureReportToExchange(requestJsonObject, exchange, OleNGConstants.NO_OF_FAILURE_ITEM, e.toString(),
                        "Problem while create item.", itemRecords.size());
            }
        }

    }

    private void setCommonValuesToItemRecord(JSONObject requestJsonObject, ItemRecord itemRecord) {
        String createdDateString = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.UPDATED_DATE);
        Timestamp createdDate = getDateTimeStamp(createdDateString);
        String createdBy = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.UPDATED_BY);
        itemRecord.setCreatedBy(createdBy);
        itemRecord.setCreatedDate(createdDate);
        itemRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_ITEM_OLEML);
    }

    @Override
    public List<Handler> getMetaDataHandlers() {
        if (null == metaDataHandlers) {
            metaDataHandlers = new ArrayList<Handler>();
            metaDataHandlers.add(new CallNumberHandler());
            metaDataHandlers.add(new CallNumberPrefixHandler());
            metaDataHandlers.add(new CallNumberTypeHandler());
            metaDataHandlers.add(new ChronologyHandler());
            metaDataHandlers.add(new CopyNumberHandler());
            metaDataHandlers.add(new DonorCodeHandler());
            metaDataHandlers.add(new EnumerationHandler());
            metaDataHandlers.add(new ItemHoldingLocationHandler());
            metaDataHandlers.add(new ItemBarcodeHandler());
            metaDataHandlers.add(new ItemStatusHandler());
            metaDataHandlers.add(new ItemTypeHandler());
            metaDataHandlers.add(new NoOfPiecesHandler());
            metaDataHandlers.add(new ItemLocationHandler());
            metaDataHandlers.add(new StatisticalSearchCodeHandler());
            metaDataHandlers.add(new VendorLineItemIdHandler());
            metaDataHandlers.add(new ItemStaffOnlyHandler());
        }
        return metaDataHandlers;
    }
}
