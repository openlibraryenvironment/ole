
package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.rest.Exchange;

import java.util.List;

/**
 * Created by SheikS on 12/20/2015.
 */
public class ItemBarcodeHandler extends ItemHandler {
    private final String TYPE = "Item Barcode";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
        String itemBarcode = getStringValueFromJsonObject(requestJsonObject, TYPE);
        if (StringUtils.equals(itemRecord.getBarCode(), itemBarcode)) {
            exchange.add(OleNGConstants.MATCHED_ITEM, Boolean.TRUE);
            exchange.add(OleNGConstants.MATCHED_VALUE, itemBarcode);
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, TYPE);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
            String itemBarcode = listFromJSONArray.get(0);
            ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
            itemRecord.setBarCode(itemBarcode);
        }
    }
}
