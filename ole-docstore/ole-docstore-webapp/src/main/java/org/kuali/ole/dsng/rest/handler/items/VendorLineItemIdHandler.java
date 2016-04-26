package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;

import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/20/2015.
 */
public class VendorLineItemIdHandler extends ItemHandler {

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(OleNGConstants.BatchProcess.VENDOR_LINE_ITEM_IDENTIFIER);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
        String vendorLineItemIdentifier = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.VENDOR_LINE_ITEM_IDENTIFIER);
        List<String> parsedValues = parseCommaSeperatedValues(vendorLineItemIdentifier);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String vendorLineItemIdentifierValue = iterator.next();
            if (StringUtils.equals(itemRecord.getVendorLineItemId(), vendorLineItemIdentifierValue)) {
                exchange.add(OleNGConstants.MATCHED_ITEM, Boolean.TRUE);
                exchange.add(OleNGConstants.MATCHED_VALUE, vendorLineItemIdentifierValue);
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.VENDOR_LINE_ITEM_IDENTIFIER);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
            String vendorLineItemIdentifier = listFromJSONArray.get(0);
            ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
            itemRecord.setVendorLineItemId(vendorLineItemIdentifier);
            exchange.add(OleNGConstants.ITEM_RECORD, itemRecord);
        }
    }
}
