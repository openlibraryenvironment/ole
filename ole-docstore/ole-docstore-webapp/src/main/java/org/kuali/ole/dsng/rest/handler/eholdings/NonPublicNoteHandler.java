package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.document.content.bib.marc.Collection;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsNoteRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.OLEHoldingsDonorRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.holdings.HoldingsHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 1/7/2016.
 */
public class NonPublicNoteHandler extends HoldingsHandler {

    private final String TYPE = "Non Public Note";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String publicNote = getStringValueFromJsonObject(requestJsonObject, TYPE);
        List<String> parsedValues = parseCommaSeperatedValues(publicNote);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String nonPublicNoteValue =  iterator.next();
            List<HoldingsNoteRecord> holdingsNoteRecords = holdingRecord.getHoldingsNoteRecords();
            if(CollectionUtils.isNotEmpty(holdingsNoteRecords)) {
                for (Iterator<HoldingsNoteRecord> holdingsNoteRecordIterator = holdingsNoteRecords.iterator(); holdingsNoteRecordIterator.hasNext(); ) {
                    HoldingsNoteRecord holdingsNoteRecord = holdingsNoteRecordIterator.next();
                    if(StringUtils.equals(holdingsNoteRecord.getType(),OleNGConstants.NON_PUBLIC) &&
                            StringUtils.equals(holdingsNoteRecord.getNote(),nonPublicNoteValue)){
                        exchange.add(OleNGConstants.MATCHED_HOLDINGS, Boolean.TRUE);
                        exchange.add(OleNGConstants.MATCHED_VALUE, nonPublicNoteValue);
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
            boolean isNoteFound = false;
            List<HoldingsNoteRecord> holdingsNoteRecords = holdingRecord.getHoldingsNoteRecords();
            if(CollectionUtils.isNotEmpty(holdingsNoteRecords)) {
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String nonPublicNote = iterator.next();
                    for (Iterator<HoldingsNoteRecord> iterator1 = holdingsNoteRecords.iterator(); iterator1.hasNext(); ) {
                        HoldingsNoteRecord holdingsNoteRecord = iterator1.next();
                        if(StringUtils.equals(holdingsNoteRecord.getType(),OleNGConstants.NON_PUBLIC)){
                            holdingsNoteRecord.setNote(nonPublicNote);
                            isNoteFound = true;
                        }
                    }
                }
            }
            if(CollectionUtils.isEmpty(holdingsNoteRecords) || isNoteFound) {
                holdingsNoteRecords = new ArrayList<HoldingsNoteRecord>();
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String nonPublicNote = iterator.next();
                    HoldingsNoteRecord holdingsNoteRecord = new HoldingsNoteRecord();
                    holdingsNoteRecord.setNote(nonPublicNote);
                    holdingsNoteRecord.setType(OleNGConstants.NON_PUBLIC);
                    holdingsNoteRecord.setHoldingsId(holdingRecord.getHoldingsId());
                    holdingsNoteRecords.add(holdingsNoteRecord);

                }
                holdingRecord.setHoldingsNoteRecords(holdingsNoteRecords);

            }
        }
        exchange.add(OleNGConstants.HOLDINGS_RECORD, holdingRecord);
    }
}
