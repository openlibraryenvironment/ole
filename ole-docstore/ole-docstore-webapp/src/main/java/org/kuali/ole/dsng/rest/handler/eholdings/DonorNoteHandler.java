package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.OLEHoldingsDonorRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.holdings.HoldingsHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/31/2015.
 */
public class DonorNoteHandler extends HoldingsHandler {

    private final String TYPE = "Donor Note";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String donorNote = getStringValueFromJsonObject(requestJsonObject, TYPE);
        List<String> parsedValues = parseCommaSeperatedValues(donorNote);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String donorNoteValue = iterator.next();
            List<OLEHoldingsDonorRecord> donorList = holdingRecord.getDonorList();
            if(CollectionUtils.isNotEmpty(donorList)) {
                for (Iterator<OLEHoldingsDonorRecord> oleHoldingsDonorRecordIterator = donorList.iterator(); oleHoldingsDonorRecordIterator.hasNext(); ) {
                    OLEHoldingsDonorRecord oleHoldingsDonorRecord = oleHoldingsDonorRecordIterator.next();
                    if(StringUtils.equals(oleHoldingsDonorRecord.getDonorPublicDisplay(),donorNoteValue)) {
                        exchange.add(OleNGConstants.MATCHED_HOLDINGS, Boolean.TRUE);
                        exchange.add(OleNGConstants.MATCHED_VALUE, donorNoteValue);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, TYPE);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
            List<OLEHoldingsDonorRecord> donorList = holdingRecord.getDonorList();
            if(CollectionUtils.isNotEmpty(donorList)) {
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String donorNote = iterator.next();
                    for (Iterator<OLEHoldingsDonorRecord> iterator1 = donorList.iterator(); iterator1.hasNext(); ) {
                        OLEHoldingsDonorRecord oleHoldingsDonorRecord = iterator1.next();
                        oleHoldingsDonorRecord.setDonorNote(donorNote);
                    }
                }
            } else {
                donorList = new ArrayList<OLEHoldingsDonorRecord>();
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String donorNote = iterator.next();
                    OLEHoldingsDonorRecord oleHoldingsDonorRecord = new OLEHoldingsDonorRecord();
                    oleHoldingsDonorRecord.setDonorNote(donorNote);
                    oleHoldingsDonorRecord.setHoldingsId(holdingRecord.getHoldingsId());
                    oleHoldingsDonorRecord.setHoldingsRecord(holdingRecord);
                }
                holdingRecord.setDonorList(donorList);
            }

        }
    }
}
