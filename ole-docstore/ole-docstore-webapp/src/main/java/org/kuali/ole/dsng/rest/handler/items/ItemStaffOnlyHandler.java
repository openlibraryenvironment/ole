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
 * Created by SheikS on 2/3/2016.
 */
public class ItemStaffOnlyHandler extends ItemHandler {


    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(OleNGConstants.BatchProcess.STAFF_ONLY);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
        String staffOnly = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.STAFF_ONLY);
        List<String> parsedValues = parseCommaSeperatedValues(staffOnly);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String staffOnlyString = iterator.next();
            boolean staffOnlyflag = (StringUtils.isNotBlank(staffOnlyString) && StringUtils.equalsIgnoreCase(staffOnlyString,"Y") ? true : false);
            if (itemRecord.getStaffOnlyFlag() == staffOnlyflag) {
                exchange.add(OleNGConstants.MATCHED_ITEM, Boolean.TRUE);
                exchange.add(OleNGConstants.MATCHED_VALUE, staffOnlyString);
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.STAFF_ONLY);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
            String staffOnlyString = listFromJSONArray.get(0);
            ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
            boolean staffOnly = (StringUtils.isNotBlank(staffOnlyString) && StringUtils.equalsIgnoreCase(staffOnlyString,"Y") ? true : false);
            itemRecord.setStaffOnlyFlag(staffOnly);
        }
    }
}