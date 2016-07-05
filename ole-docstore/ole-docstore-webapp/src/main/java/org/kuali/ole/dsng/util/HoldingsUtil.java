package org.kuali.ole.dsng.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.util.BusinessObjectServiceHelperUtil;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 2/25/2016.
 */
public class HoldingsUtil extends OleDsHelperUtil{

    private static HoldingsUtil holdingsUtil;

    private HoldingsUtil() {
    }

    public static HoldingsUtil getInstance() {
        if(null == holdingsUtil) {
            holdingsUtil = new HoldingsUtil();
        }
        return holdingsUtil;
    }

    public void processIfDeleteAllExistOpsFound(HoldingsRecord holdingsRecord, JSONObject requestJsonObject) {
        ArrayList<ItemRecord> itemListToDelete = getListOfItemsToDelete(holdingsRecord, requestJsonObject);

        if (CollectionUtils.isNotEmpty(itemListToDelete)) {

            getBusinessObjectService().delete(itemListToDelete);

            StringBuilder itemIdsString = new StringBuilder();
            for (Iterator<ItemRecord> iterator = itemListToDelete.iterator(); iterator.hasNext(); ) {
                ItemRecord itemRecord = iterator.next();
                String itemId = itemRecord.getItemId();
                itemIdsString.append(DocumentUniqueIDPrefix.PREFIX_WORK_ITEM_OLEML + "-" + itemId);
                if(iterator.hasNext()) {
                    itemIdsString.append(" OR ");
                }
            }
            if(StringUtils.isNotBlank(itemIdsString.toString())) {
                String deleteQuery = "id:(" + itemIdsString + ")";
                getSolrRequestReponseHandler().deleteFromSolr(deleteQuery);
            }
        }
    }

    private ArrayList<ItemRecord> getListOfItemsToDelete(HoldingsRecord holdingsRecord, JSONObject requestJsonObject) {
        ArrayList<ItemRecord> itemListToDelete = new ArrayList<ItemRecord>();
        String addedOpsValue = getAddedOpsValue(requestJsonObject, OleNGConstants.ITEM);
        if(StringUtils.isNotBlank(addedOpsValue) && (addedOpsValue.equalsIgnoreCase(OleNGConstants.DELETE_ALL_EXISTING_AND_ADD) ||
                addedOpsValue.equalsIgnoreCase(OleNGConstants.DELETE_ALL))) {
            List<ItemRecord> itemRecords = holdingsRecord.getItemRecords();
            if (CollectionUtils.isNotEmpty(itemRecords)) {
                itemListToDelete.addAll(itemRecords);
            }
            holdingsRecord.setItemRecords(new ArrayList<ItemRecord>());
        }
        return itemListToDelete;
    }


    private String getAddedOpsValue(JSONObject jsonObject, String docType) {
        JSONObject addedOps = getJSONObjectFromJSONObject(jsonObject, OleNGConstants.ADDED_OPS);
        return getStringValueFromJsonObject(addedOps,docType);
    }
}
