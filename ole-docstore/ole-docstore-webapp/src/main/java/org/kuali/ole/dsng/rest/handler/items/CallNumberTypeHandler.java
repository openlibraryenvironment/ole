package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.CallNumberTypeRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.rest.Exchange;

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
        ItemRecord itemRecord = (ItemRecord) exchange.get("itemRecord");
        String callNumberType = getStringValueFromJsonObject(requestJsonObject, TYPE);
        if (null != itemRecord.getCallNumberTypeRecord() &&
                StringUtils.equals(itemRecord.getCallNumberTypeRecord().getCallNumberTypeId(),callNumberType)) {
            exchange.add("matchedItem", itemRecord);
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        String callNumberType = getStringValueFromJsonObject(requestJsonObject, TYPE);
        ItemRecord itemRecord = (ItemRecord) exchange.get("itemRecord");
        CallNumberTypeRecord callNumberTypeRecord = fetchCallNumberTypeRecordById(callNumberType);
        if (null != callNumberTypeRecord) {
            itemRecord.setCallNumberTypeId(callNumberTypeRecord.getCallNumberTypeId());
            itemRecord.setCallNumberTypeRecord(callNumberTypeRecord);
        }
        exchange.add("itemRecord", itemRecord);
    }
}
