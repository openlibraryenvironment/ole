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
public class DonorPublicDisplayHandler extends ItemHandler {
    private final String TYPE = "Donor Public Display";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
        String donorPublicDisplay = getStringValueFromJsonObject(requestJsonObject,TYPE);
        List<String> parsedValues = parseCommaSeperatedValues(donorPublicDisplay);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String donorPublicDisplayValue = iterator.next();
            List<OLEItemDonorRecord> donorList = itemRecord.getDonorList();
            if(CollectionUtils.isNotEmpty(donorList)) {
                for (Iterator<OLEItemDonorRecord> oleItemDonorRecordIterator = donorList.iterator(); oleItemDonorRecordIterator.hasNext(); ) {
                    OLEItemDonorRecord oleItemDonorRecord = oleItemDonorRecordIterator.next();
                    if(StringUtils.equals(oleItemDonorRecord.getDonorPublicDisplay(),donorPublicDisplayValue)) {
                        exchange.add(OleNGConstants.MATCHED_ITEM, Boolean.TRUE);
                        exchange.add(OleNGConstants.MATCHED_VALUE, donorPublicDisplayValue);
                        break;
                    }
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
                    String donorPublicDisplay = iterator.next();
                    for (Iterator<OLEItemDonorRecord> iterator1 = donorList.iterator(); iterator1.hasNext(); ) {
                        OLEItemDonorRecord oleItemDonorRecord = iterator1.next();
                        oleItemDonorRecord.setDonorPublicDisplay(donorPublicDisplay);
                    }
                }
            } else {
                donorList = new ArrayList<OLEItemDonorRecord>();
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String donorPublicDisplay = iterator.next();
                    OLEItemDonorRecord oleItemDonorRecord = new OLEItemDonorRecord();
                    oleItemDonorRecord.setDonorPublicDisplay(donorPublicDisplay);
                    oleItemDonorRecord.setItemId(itemRecord.getItemId());
                }
                itemRecord.setDonorList(donorList);
            }

            exchange.add(OleNGConstants.ITEM_RECORD, itemRecord);
        }
    }
}
