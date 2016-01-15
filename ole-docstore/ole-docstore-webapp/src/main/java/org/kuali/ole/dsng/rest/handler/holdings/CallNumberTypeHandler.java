package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.CallNumberTypeRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.util.CallNumberUtil;

/**
 * Created by SheikS on 12/20/2015.
 */
public class CallNumberTypeHandler extends HoldingsHandler {

    private final String TYPE = "Call Number Type";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String callNumberTypeCode = getStringValueFromJsonObject(requestJsonObject, TYPE);
        if ((null != holdingsRecord.getCallNumberTypeRecord() &&
                StringUtils.equals(holdingsRecord.getCallNumberTypeRecord().getCode(),callNumberTypeCode))) {
            exchange.add(OleNGConstants.MATCHED_HOLDINGS, holdingsRecord);
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        String callNumberTypeCode = getStringValueFromJsonObject(requestJsonObject, TYPE);
        HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        CallNumberTypeRecord callNumberTypeRecord = new CallNumberUtil().fetchCallNumberTypeRecordById(callNumberTypeCode);
        if (null != callNumberTypeRecord) {
            holdingsRecord.setCallNumberTypeId(callNumberTypeRecord.getCallNumberTypeId());
            holdingsRecord.setCallNumberTypeRecord(callNumberTypeRecord);
        }
    }
}
