package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.rest.Exchange;

/**
 * Created by SheikS on 12/20/2015.
 */
public class VendorLineItemIdHandler extends ItemHandler {
    private final String TYPE = "Vendor Line Item Identifier";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
        String vendorLineItemIdentifier = getStringValueFromJsonObject(requestJsonObject, TYPE);
        if (StringUtils.equals(itemRecord.getVendorLineItemId(), vendorLineItemIdentifier)) {
            exchange.add(OleNGConstants.MATCHED_ITEM, itemRecord);
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        String vendorLineItemIdentifier = getStringValueFromJsonObject(requestJsonObject, TYPE);
        ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
        itemRecord.setVendorLineItemId(vendorLineItemIdentifier);
        exchange.add(OleNGConstants.ITEM_RECORD, itemRecord);
    }
}
