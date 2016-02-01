package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.CallNumberTypeRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.util.CallNumberUtil;

import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/20/2015.
 */
public class CallNumberTypeHandler extends ItemHandler {

    private final String TYPE = "Call Number Type";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
        String callNumberType = getStringValueFromJsonObject(requestJsonObject, TYPE);
        List<String> parsedValues = parseCommaSeperatedValues(callNumberType);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String callNumberTypeValue = iterator.next();
            if (null != itemRecord.getCallNumberTypeRecord() &&
                    StringUtils.equals(itemRecord.getCallNumberTypeRecord().getCallNumberTypeId(),callNumberTypeValue)) {
                exchange.add(OleNGConstants.MATCHED_ITEM, Boolean.TRUE);
                exchange.add(OleNGConstants.MATCHED_VALUE, callNumberTypeValue);
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, TYPE);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
            String callNumberType = listFromJSONArray.get(0);
            ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
            CallNumberTypeRecord callNumberTypeRecord = new CallNumberUtil().fetchCallNumberTypeRecordById(callNumberType);
            if (null != callNumberTypeRecord) {
                itemRecord.setCallNumberTypeId(callNumberTypeRecord.getCallNumberTypeId());
                itemRecord.setCallNumberTypeRecord(callNumberTypeRecord);
            }
        }
    }
}
