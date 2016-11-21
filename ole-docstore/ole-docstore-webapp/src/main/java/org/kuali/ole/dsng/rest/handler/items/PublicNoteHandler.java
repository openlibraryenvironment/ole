package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemNoteRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.OLEItemDonorRecord;
import org.kuali.ole.select.bo.OLEDonor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by rajeshgp on 18/11/16.
 */
public class PublicNoteHandler extends ItemHandler {

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.PUBLIC_NOTE);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        List<ItemNoteRecord> noteRecordList=new ArrayList<ItemNoteRecord>();
        if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
            ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
            for(String note:listFromJSONArray){
                ItemNoteRecord itemNoteRecord=new ItemNoteRecord();
                itemNoteRecord.setNote(note);
                itemNoteRecord.setType(OleNGConstants.PUBLIC);
                itemNoteRecord.setItemId(itemRecord.getItemId());
                noteRecordList.add(itemNoteRecord);
            }
            itemRecord.setItemNoteRecords(noteRecordList);
        }

    }


    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(OleNGConstants.BatchProcess.PUBLIC_NOTE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        String publicNote = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.PUBLIC_NOTE);
        List<String> parsedValues = parseCommaSeperatedValues(publicNote);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String publicNoteValue = iterator.next();
            ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
            List<ItemNoteRecord> noteList = itemRecord.getItemNoteRecords();
            if(CollectionUtils.isNotEmpty(noteList)) {
                for (Iterator<ItemNoteRecord> olePublicNoteRecordIterator = noteList.iterator(); olePublicNoteRecordIterator.hasNext(); ) {
                    ItemNoteRecord oleItemNoteRecord = olePublicNoteRecordIterator.next();
                    if(StringUtils.equals(oleItemNoteRecord.getNote(),publicNoteValue)) {
                        exchange.add(OleNGConstants.MATCHED_ITEM, Boolean.TRUE);
                        exchange.add(OleNGConstants.MATCHED_VALUE, publicNoteValue);
                        break;
                    }
                }
            }
        }
    }



}
