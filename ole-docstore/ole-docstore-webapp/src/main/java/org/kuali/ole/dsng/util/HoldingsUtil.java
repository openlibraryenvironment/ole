package org.kuali.ole.dsng.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibDeletionRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.dao.BibValidationDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 2/25/2016.
 */
public class HoldingsUtil extends OleDsHelperUtil{

    private static HoldingsUtil holdingsUtil;
    private BibValidationDao bibValidationDao;

    private HoldingsUtil() {
    }

    public static HoldingsUtil getInstance() {
        if(null == holdingsUtil) {
            holdingsUtil = new HoldingsUtil();
        }
        return holdingsUtil;
    }

    public void processIfDeleteAllExistOpsFound(HoldingsRecord holdingsRecord, JSONObject requestJsonObject, Exchange exchange) {
        ArrayList<ItemRecord> itemListToDelete = getListOfItemsToDelete(holdingsRecord, requestJsonObject);

        if (CollectionUtils.isNotEmpty(itemListToDelete)) {
            List<ItemRecord> finalListToDelete = new ArrayList<ItemRecord>();
            for (Iterator<ItemRecord> iterator = itemListToDelete.iterator(); iterator.hasNext(); ) {
                ItemRecord itemRecord = iterator.next();
                if (getBibValidationDao().isItemAttachedToPO(itemRecord.getItemId())) {
                    Exception e = new Exception(OleNGConstants.ERR_ITEM_HAS_REQ_OR_PO + DocumentUniqueIDPrefix.PREFIX_WORK_ITEM_OLEML + "-" + itemRecord.getItemId());
                    addFailureReportToExchange(requestJsonObject, exchange,OleNGConstants.ITEM,e,null);
                } else {
                    finalListToDelete.add(itemRecord);
                }
            }


            getBusinessObjectService().delete(finalListToDelete);
            saveDeletedItem(finalListToDelete);

            List<String> itemIdsToDeleteFromSolr = new ArrayList<String>();
            for (Iterator<ItemRecord> iterator = finalListToDelete.iterator(); iterator.hasNext(); ) {
                ItemRecord itemRecord = iterator.next();
                String itemId = itemRecord.getItemId();
                itemIdsToDeleteFromSolr.add(DocumentUniqueIDPrefix.PREFIX_WORK_ITEM_OLEML + "-" + itemId);
            }
            String itemIdsString = StringUtils.join(itemIdsToDeleteFromSolr, " OR ");
            if(StringUtils.isNotBlank(itemIdsString)) {
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



    public BibValidationDao getBibValidationDao() {
        if(null == bibValidationDao) {
            bibValidationDao = (BibValidationDao) org.kuali.ole.dsng.service.SpringContext.getBean("bibValidationDao");
        }

        return bibValidationDao;
    }

    public void setBibValidationDao(BibValidationDao bibValidationDao) {
        this.bibValidationDao = bibValidationDao;
    }

    public void saveDeletedItem(List<ItemRecord> itemRecords) {
        List<BibDeletionRecord> bibDeletionRecords= new ArrayList<BibDeletionRecord>();
        for(ItemRecord itemRecord : itemRecords){
            BibDeletionRecord bibDeletionRecord= new BibDeletionRecord();
            if(itemRecord.getHoldingsRecord() != null){
                bibDeletionRecord.setBibId(DocumentUniqueIDPrefix.getDocumentId(itemRecord.getHoldingsRecord().getBibId()));
            }
            if(StringUtils.isNotBlank(itemRecord.getHoldingsId())){
                bibDeletionRecord.setHoldingId(DocumentUniqueIDPrefix.getDocumentId(itemRecord.getHoldingsId()));
            }
            bibDeletionRecord.setItemId(DocumentUniqueIDPrefix.getDocumentId(itemRecord.getItemId()));
            bibDeletionRecord.setBibIdIndicator("N");
            bibDeletionRecord.setHoldingIdIndicator("N");
            bibDeletionRecord.setItemIdIndicator("Y");
            bibDeletionRecord.setDateUpdated(new Timestamp(new Date().getTime()));
            bibDeletionRecords.add(bibDeletionRecord);
        }
        getBusinessObjectService().save(bibDeletionRecords);
    }
}
