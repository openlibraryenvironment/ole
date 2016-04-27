package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.EInstanceCoverageRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.handler.holdings.HoldingsHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/31/2015.
 */
public class CoverageStartVolumeHandler extends HoldingsHandler {

    private final String TYPE = "Coverage Start Volume";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String coverageStartVolume = getStringValueFromJsonObject(requestJsonObject,TYPE);
        List<String> parsedValues = parseCommaSeperatedValues(coverageStartVolume);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String coverageStartVolumeValue = iterator.next();
            List<EInstanceCoverageRecord> eInstanceCoverageRecords = holdingsRecord.geteInstanceCoverageRecordList();
            if(CollectionUtils.isNotEmpty(eInstanceCoverageRecords)) {
                for (Iterator<EInstanceCoverageRecord> eInstanceCoverageRecordIterator = eInstanceCoverageRecords.iterator();
                     eInstanceCoverageRecordIterator.hasNext(); ) {
                    EInstanceCoverageRecord eInstanceCoverageRecord = eInstanceCoverageRecordIterator.next();
                    if(StringUtils.equals(eInstanceCoverageRecord.getCoverageStartVolume(),coverageStartVolumeValue)) {
                        exchange.add(OleNGConstants.MATCHED_HOLDINGS, Boolean.TRUE);
                        exchange.add(OleNGConstants.MATCHED_VALUE, coverageStartVolumeValue);
                    }
                }
            }
        }
    }


    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, TYPE);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
            HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
            List<EInstanceCoverageRecord> eInstanceCoverageRecords = holdingsRecord.geteInstanceCoverageRecordList();
            if(CollectionUtils.isNotEmpty(eInstanceCoverageRecords))  {
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String coverageStartVolume = iterator.next();
                    for (Iterator<EInstanceCoverageRecord> iterator1 = eInstanceCoverageRecords.iterator(); iterator.hasNext(); ) {
                        EInstanceCoverageRecord eInstanceCoverageRecord = iterator1.next();
                        eInstanceCoverageRecord.setCoverageStartVolume(coverageStartVolume);
                    }
                }
            } else {
                eInstanceCoverageRecords = new ArrayList<EInstanceCoverageRecord>();
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String coverageStartVolume = iterator.next();
                    EInstanceCoverageRecord eInstanceCoverageRecord = new EInstanceCoverageRecord();
                    eInstanceCoverageRecord.setCoverageStartVolume(coverageStartVolume);
                    eInstanceCoverageRecord.setHoldingsId(holdingsRecord.getHoldingsId());
                    eInstanceCoverageRecord.setHoldingsRecord(holdingsRecord);
                    eInstanceCoverageRecords.add(eInstanceCoverageRecord);
                }
                holdingsRecord.seteInstanceCoverageRecordList(eInstanceCoverageRecords);
            }
        }
    }
}
