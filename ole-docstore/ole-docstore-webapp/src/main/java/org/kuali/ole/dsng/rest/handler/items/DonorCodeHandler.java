package org.kuali.ole.dsng.rest.handler.items;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.OLEItemDonorRecord;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.utility.OleNgUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/20/2015.
 */
public class DonorCodeHandler extends ItemHandler {

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(OleNGConstants.BatchProcess.DONOR_CODE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        String donorCode = getStringValueFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.DONOR_CODE);
        List<String> parsedValues = parseCommaSeperatedValues(donorCode);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String donorCodeValue = iterator.next();
            ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
            List<OLEItemDonorRecord> donorList = itemRecord.getDonorList();
            if(CollectionUtils.isNotEmpty(donorList)) {
                for (Iterator<OLEItemDonorRecord> oleItemDonorRecordIterator = donorList.iterator(); oleItemDonorRecordIterator.hasNext(); ) {
                    OLEItemDonorRecord oleItemDonorRecord = oleItemDonorRecordIterator.next();
                    if(StringUtils.equals(oleItemDonorRecord.getDonorCode(),donorCodeValue)) {
                        exchange.add(OleNGConstants.MATCHED_ITEM, Boolean.TRUE);
                        exchange.add(OleNGConstants.MATCHED_VALUE, donorCodeValue);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        ItemRecord itemRecord = (ItemRecord) exchange.get(OleNGConstants.ITEM_RECORD);
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, OleNGConstants.BatchProcess.DONOR_CODE);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
            List<OLEItemDonorRecord> donorList = itemRecord.getDonorList();
            OleNgUtil oleNgUtil = new OleNgUtil();
            if(CollectionUtils.isNotEmpty(donorList)) {
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String donorCode = iterator.next();
                    OLEDonor donor = getOleDsNGMemorizeService().getDonorCode(donorCode);
                    if(null != donor) {
                        for (Iterator<OLEItemDonorRecord> iterator1 = donorList.iterator(); iterator1.hasNext(); ) {
                            OLEItemDonorRecord oleItemDonorRecord = iterator1.next();
                            oleItemDonorRecord.setDonorCode(donor.getDonorCode());
                            oleItemDonorRecord.setDonorNote(donor.getDonorNote());
                            oleItemDonorRecord.setDonorPublicDisplay(donor.getDonorPublicDisplay());
                        }
                    } else {
                        oleNgUtil.addValidationErrorMessageToExchange(exchange, "Invalid Donor Code : " + donorCode);
                    }
                }
            } else {
                donorList = new ArrayList<OLEItemDonorRecord>();
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String donorCode = iterator.next();
                    OLEDonor donor = getOleDsNGMemorizeService().getDonorCode(donorCode);
                    if(null != donor) {
                        OLEItemDonorRecord oleItemDonorRecord = new OLEItemDonorRecord();
                        oleItemDonorRecord.setDonorCode(donor.getDonorCode());
                        oleItemDonorRecord.setDonorNote(donor.getDonorNote());
                        oleItemDonorRecord.setDonorPublicDisplay(donor.getDonorPublicDisplay());
                        oleItemDonorRecord.setItemId(itemRecord.getItemId());
                        donorList.add(oleItemDonorRecord);
                    } else {
                        oleNgUtil.addValidationErrorMessageToExchange(exchange, "Invalid Donor Code : " + donorCode);
                    }
                }
                itemRecord.setDonorList(donorList);
            }

            exchange.add(OleNGConstants.ITEM_RECORD, itemRecord);
        }
    }
}
