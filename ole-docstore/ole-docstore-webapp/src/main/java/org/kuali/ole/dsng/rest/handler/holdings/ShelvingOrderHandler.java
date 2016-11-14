package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;

import java.util.Iterator;
import java.util.List;

/**
 * Created by govindarajank on 11/11/16.
 */
public class ShelvingOrderHandler extends HoldingsHandler  {

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.SHELVING_ORDER);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
            String shelvingOrder = listFromJSONArray.get(0);
            HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
            holdingRecord.setShelvingOrder(shelvingOrder);
        }
    }

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(OleNGConstants.BatchProcess.SHELVING_ORDER);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String shelvingOrder = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.SHELVING_ORDER);
        List<String> parsedValues = parseCommaSeperatedValues(shelvingOrder);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String shelvingOrderValue = iterator.next();
            if(StringUtils.equals(holdingRecord.getCallNumberPrefix(), shelvingOrderValue)) {
                exchange.add(OleNGConstants.MATCHED_HOLDINGS, Boolean.TRUE);
                exchange.add(OleNGConstants.MATCHED_VALUE, shelvingOrderValue);
            }
        }
    }
}
