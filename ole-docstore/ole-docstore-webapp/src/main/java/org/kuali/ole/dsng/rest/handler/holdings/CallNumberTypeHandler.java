package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.CallNumberTypeRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.util.CallNumberUtil;
import org.kuali.ole.utility.OleNgUtil;

import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/20/2015.
 */
public class CallNumberTypeHandler extends HoldingsHandler {


    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(OleNGConstants.BatchProcess.CALL_NUMBER_TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String callNumberTypeCode = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.CALL_NUMBER_TYPE);
        List<String> parsedValues = parseCommaSeperatedValues(callNumberTypeCode);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String callNumberTypeCodeValue = iterator.next();
            if ((null != holdingsRecord.getCallNumberTypeRecord() &&
                    StringUtils.equals(holdingsRecord.getCallNumberTypeRecord().getCode(),callNumberTypeCodeValue))) {
                exchange.add(OleNGConstants.MATCHED_HOLDINGS, Boolean.TRUE);
                exchange.add(OleNGConstants.MATCHED_VALUE, callNumberTypeCodeValue);
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.CALL_NUMBER_TYPE);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        if (CollectionUtils.isNotEmpty(listFromJSONArray)) {
            String callNumberTypeCode = listFromJSONArray.get(0);
            HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
            CallNumberTypeRecord callNumberTypeRecord = getOleDsNGMemorizeService().fetchCallNumberTypeRecordById(callNumberTypeCode);
            if (null != callNumberTypeRecord) {
                holdingsRecord.setCallNumberTypeId(callNumberTypeRecord.getCallNumberTypeId());
                holdingsRecord.setCallNumberTypeRecord(callNumberTypeRecord);
            } else {
                new OleNgUtil().addValidationErrorMessageToExchange(exchange, "Invalid Call Number Type : " + callNumberTypeCode);
            }
        }
    }
}
