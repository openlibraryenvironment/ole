package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.OLEItemDonorRecord;
import org.kuali.ole.dsng.rest.Exchange;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/20/2015.
 */
public class DonorNoteHandler extends ItemHandler {
    private final String TYPE = "Donor Note";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
        String donorNote = getStringValueFromJsonObject(requestJsonObject,TYPE);
        List<OLEItemDonorRecord> donorList = itemRecord.getDonorList();
        if(CollectionUtils.isNotEmpty(donorList)) {
            for (Iterator<OLEItemDonorRecord> iterator = donorList.iterator(); iterator.hasNext(); ) {
                OLEItemDonorRecord oleItemDonorRecord = iterator.next();
                if(StringUtils.equals(oleItemDonorRecord.getDonorNote(),donorNote)) {
                    exchange.add(OleNGConstants.MATCHED_ITEM, Boolean.TRUE);
                    break;
                }
            }
        }
    }


    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, TYPE);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
            List<OLEItemDonorRecord> donorList = itemRecord.getDonorList();
            if(CollectionUtils.isNotEmpty(donorList)) {
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String donorNote = iterator.next();
                    for (Iterator<OLEItemDonorRecord> iterator1 = donorList.iterator(); iterator1.hasNext(); ) {
                        OLEItemDonorRecord oleItemDonorRecord = iterator1.next();
                        oleItemDonorRecord.setDonorNote(donorNote);
                    }
                }
            } else {
                donorList = new ArrayList<OLEItemDonorRecord>();
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String donorNote = iterator.next();
                    OLEItemDonorRecord oleItemDonorRecord = new OLEItemDonorRecord();
                    oleItemDonorRecord.setDonorNote(donorNote);
                    oleItemDonorRecord.setItemId(itemRecord.getItemId());
                    donorList.add(oleItemDonorRecord);
                }
                itemRecord.setDonorList(donorList);
            }

            exchange.add(OleNGConstants.ITEM_RECORD, itemRecord);
        }
    }
}
