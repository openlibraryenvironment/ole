package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsStatisticalSearchRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.StatisticalSearchRecord;
import org.kuali.ole.dsng.rest.handler.holdings.HoldingsHandler;
import org.kuali.ole.dsng.util.StatisticalSearchCodeUtil;
import org.kuali.ole.utility.OleNgUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/31/2015.
 */
public class StatisticalSearchCodeHandler extends HoldingsHandler {

    private final String TYPE = "Statistical Code";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String statisticalSearchCode = getStringValueFromJsonObject(requestJsonObject,TYPE);
        List<String> parsedValues = parseCommaSeperatedValues(statisticalSearchCode);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String statisticalSearchCodeValue = iterator.next();
            List<HoldingsStatisticalSearchRecord> holdingsStatisticalSearchRecords = holdingsRecord.getHoldingsStatisticalSearchRecords();
            if(CollectionUtils.isNotEmpty(holdingsStatisticalSearchRecords)) {
                for (Iterator<HoldingsStatisticalSearchRecord> holdingsStatisticalSearchRecordIterator = holdingsStatisticalSearchRecords.iterator(); holdingsStatisticalSearchRecordIterator.hasNext(); ) {
                    HoldingsStatisticalSearchRecord holdingsStatisticalSearchRecord = holdingsStatisticalSearchRecordIterator.next();
                    if(null != holdingsStatisticalSearchRecord.getStatisticalSearchRecord() &&
                            StringUtils.equals(holdingsStatisticalSearchRecord.getStatisticalSearchRecord().getCode(),statisticalSearchCodeValue)) {
                        exchange.add(OleNGConstants.MATCHED_HOLDINGS, Boolean.TRUE);
                        exchange.add(OleNGConstants.MATCHED_VALUE, statisticalSearchCodeValue);
                    }
                }
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, TYPE);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
            HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
            List<HoldingsStatisticalSearchRecord> holdingsStatisticalSearchRecords = holdingsRecord.getHoldingsStatisticalSearchRecords();
            OleNgUtil oleNgUtil = new OleNgUtil();
            if(CollectionUtils.isNotEmpty(holdingsStatisticalSearchRecords)) {
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String statisticalSearchCode = iterator.next();
                    StatisticalSearchRecord statisticalSearchRecord = getOleDsNGMemorizeService().fetchStatisticalSearchRecordByCode(statisticalSearchCode);
                    if (null != statisticalSearchRecord) {
                        for (Iterator<HoldingsStatisticalSearchRecord> holdingsStatisticalSearchRecordIterator = holdingsStatisticalSearchRecords.iterator(); holdingsStatisticalSearchRecordIterator.hasNext(); ) {
                            HoldingsStatisticalSearchRecord holdingsStatisticalSearchRecord = holdingsStatisticalSearchRecordIterator.next();
                            holdingsStatisticalSearchRecord.setStatisticalSearchId(statisticalSearchRecord.getStatisticalSearchId());
                            holdingsStatisticalSearchRecord.setStatisticalSearchRecord(statisticalSearchRecord);

                        }
                    } else {
                        oleNgUtil.addValidationErrorMessageToExchange(exchange, "Invalid Statistical Code : " + statisticalSearchCode);
                    }
                }
            } else {
                holdingsStatisticalSearchRecords = new ArrayList<HoldingsStatisticalSearchRecord>();
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String statisticalSearchCode = iterator.next();
                    StatisticalSearchRecord statisticalSearchRecord = getOleDsNGMemorizeService().fetchStatisticalSearchRecordByCode(statisticalSearchCode);
                    if (null != statisticalSearchRecord) {
                        HoldingsStatisticalSearchRecord holdingsStatisticalSearchRecord = new HoldingsStatisticalSearchRecord();
                        holdingsStatisticalSearchRecord.setStatisticalSearchId(statisticalSearchRecord.getStatisticalSearchId());
                        holdingsStatisticalSearchRecord.setStatisticalSearchRecord(statisticalSearchRecord);
                        holdingsStatisticalSearchRecord.setHoldingsId(holdingsRecord.getHoldingsId());
                        holdingsStatisticalSearchRecord.setHoldingsRecord(holdingsRecord);
                        holdingsStatisticalSearchRecords.add(holdingsStatisticalSearchRecord);
                    } else {
                        oleNgUtil.addValidationErrorMessageToExchange(exchange, "Invalid Statistical Code : " +statisticalSearchCode);
                    }
                }
                holdingsRecord.setHoldingsStatisticalSearchRecords(holdingsStatisticalSearchRecords);
            }
            exchange.add(OleNGConstants.HOLDINGS_RECORD, holdingsRecord);
        }

    }
}
