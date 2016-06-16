package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;

import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 5/27/2016.
 */
public class ItemIdHandler extends ItemHandler {

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(OleNGConstants.BatchProcess.LOCAL_IDENTIFIER);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
        String localIdentifier = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.LOCAL_IDENTIFIER);
        List<String> parsedValues = parseCommaSeperatedValues(localIdentifier);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String itemId = iterator.next();
            if (StringUtils.equals(itemRecord.getItemId(), itemId)) {
                exchange.add(OleNGConstants.MATCHED_ITEM, Boolean.TRUE);
                exchange.add(OleNGConstants.MATCHED_VALUE, itemId);
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {

    }
}