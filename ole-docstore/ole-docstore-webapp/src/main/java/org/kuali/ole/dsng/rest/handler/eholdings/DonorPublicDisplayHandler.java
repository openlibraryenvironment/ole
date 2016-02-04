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
public class DonorPublicDisplayHandler extends HoldingsHandler {

    private final String TYPE = "Donor Public Display";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String donorPublicDisplay = getStringValueFromJsonObject(requestJsonObject, TYPE);
        List<String> parsedValues = parseCommaSeperatedValues(donorPublicDisplay);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String donorDisplayValue = iterator.next();
            List<OLEHoldingsDonorRecord> donorList = holdingRecord.getDonorList();
            if(CollectionUtils.isNotEmpty(donorList)) {
                for (Iterator<OLEHoldingsDonorRecord> oleHoldingsDonorRecordIterator = donorList.iterator(); oleHoldingsDonorRecordIterator.hasNext(); ) {
                    OLEHoldingsDonorRecord oleHoldingsDonorRecord = oleHoldingsDonorRecordIterator.next();
                    if(StringUtils.equals(oleHoldingsDonorRecord.getDonorPublicDisplay(),donorDisplayValue)) {
                        exchange.add(OleNGConstants.MATCHED_HOLDINGS, Boolean.TRUE);
                        exchange.add(OleNGConstants.MATCHED_VALUE, donorDisplayValue);
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
                    String donorPublicDisplay = iterator.next();
                    for (Iterator<OLEHoldingsDonorRecord> iterator1 = donorList.iterator(); iterator1.hasNext(); ) {
                        OLEHoldingsDonorRecord oleHoldingsDonorRecord = iterator1.next();
                        oleHoldingsDonorRecord.setDonorPublicDisplay(donorPublicDisplay);
                    }
                }
            } else {
                donorList = new ArrayList<OLEHoldingsDonorRecord>();
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String donorPublicDisplay = iterator.next();
                    OLEHoldingsDonorRecord oleHoldingsDonorRecord = new OLEHoldingsDonorRecord();
                    oleHoldingsDonorRecord.setDonorPublicDisplay(donorPublicDisplay);
                    oleHoldingsDonorRecord.setHoldingsId(holdingRecord.getHoldingsId());
                    oleHoldingsDonorRecord.setHoldingsRecord(holdingRecord);
                }
                holdingRecord.setDonorList(donorList);
            }

        }
    }
}
