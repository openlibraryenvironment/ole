package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.rest.Exchange;

/**
 * Created by SheikS on 12/20/2015.
 */
public class ChronologyHandler extends ItemHandler {
    private final String TYPE = "Chronology";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        ItemRecord itemRecord = (ItemRecord) exchange.get("itemRecord");
        String chronology = getStringValueFromJsonObject(requestJsonObject, TYPE);
        if (StringUtils.equals(itemRecord.getChronology(), chronology)) {
            exchange.add("matchedItem", itemRecord);
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        String chronology = getStringValueFromJsonObject(requestJsonObject, TYPE);
        ItemRecord itemRecord = (ItemRecord) exchange.get("itemRecord");
        itemRecord.setChronology(chronology);
        exchange.add("itemRecord", itemRecord);
    }
}
