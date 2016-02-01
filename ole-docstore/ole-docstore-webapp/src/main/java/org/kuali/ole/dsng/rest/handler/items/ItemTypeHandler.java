
package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemTypeRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.util.ItemUtil;

import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/20/2015.
 */
public class ItemTypeHandler extends ItemHandler {
    private final String TYPE = "Item Type";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
        String itemTypeName = getStringValueFromJsonObject(requestJsonObject, TYPE);
        List<String> parsedValues = parseCommaSeperatedValues(itemTypeName);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String itemTypeNameValue = iterator.next();
            if (null != itemRecord.getItemTypeRecord() &&
                    StringUtils.equals(itemRecord.getItemTypeRecord().getName(),itemTypeNameValue)) {
                exchange.add(OleNGConstants.MATCHED_ITEM, Boolean.TRUE);
                exchange.add(OleNGConstants.MATCHED_VALUE, itemTypeNameValue);
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, TYPE);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
            String itemTypeName = listFromJSONArray.get(0);
            ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
            ItemTypeRecord itemTypeRecord = new ItemUtil().fetchItemTypeByName(itemTypeName);
            if(null != itemTypeRecord) {
                itemRecord.setItemTypeId(itemTypeRecord.getItemTypeId());
                itemRecord.setItemTypeRecord(itemTypeRecord);
            }
            exchange.add(OleNGConstants.ITEM_RECORD, itemRecord);
        }
    }
}
