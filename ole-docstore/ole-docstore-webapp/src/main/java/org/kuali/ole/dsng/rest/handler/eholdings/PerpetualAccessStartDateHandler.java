package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.EInstancePerpetualAccessRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.holdings.HoldingsHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/31/2015.
 */
public class PerpetualAccessStartDateHandler extends HoldingsHandler {

    private final String TYPE = "Perpetual Access Start Date";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String perpetualAccessStartDate = getStringValueFromJsonObject(requestJsonObject,TYPE);
        List<String> parsedValues = parseCommaSeperatedValues(perpetualAccessStartDate);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String perpectualAccessStartDateValue = iterator.next();
            List<EInstancePerpetualAccessRecord> eInstanceCoverageRecords = holdingsRecord.geteInstancePerpetualAccessRecordList();
            if(CollectionUtils.isNotEmpty(eInstanceCoverageRecords)) {
                for (Iterator<EInstancePerpetualAccessRecord> eInstancePerpetualAccessRecordIterator = eInstanceCoverageRecords.iterator(); eInstancePerpetualAccessRecordIterator.hasNext(); ) {
                    EInstancePerpetualAccessRecord eInstancePerpetualAccessRecord = eInstancePerpetualAccessRecordIterator.next();
                    if(StringUtils.equals(eInstancePerpetualAccessRecord.getPerpetualAccessStartDate(),perpectualAccessStartDateValue)) {
                        exchange.add(OleNGConstants.MATCHED_HOLDINGS, Boolean.TRUE);
                        exchange.add(OleNGConstants.MATCHED_VALUE, perpectualAccessStartDateValue);
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
            List<EInstancePerpetualAccessRecord> eInstancePerpetualAccessRecords = holdingsRecord.geteInstancePerpetualAccessRecordList();
            if(CollectionUtils.isNotEmpty(eInstancePerpetualAccessRecords)) {
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String perpetualAccessStartDate = iterator.next();
                    for (Iterator<EInstancePerpetualAccessRecord> iterator1 = eInstancePerpetualAccessRecords.iterator(); iterator1.hasNext(); ) {
                        EInstancePerpetualAccessRecord eInstancePerpetualAccessRecord = iterator1.next();
                        eInstancePerpetualAccessRecord.setPerpetualAccessStartDate(perpetualAccessStartDate);
                    }
                }
            } else {
                eInstancePerpetualAccessRecords = new ArrayList<EInstancePerpetualAccessRecord>();
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String perpetualAccessStartDate = iterator.next();
                    EInstancePerpetualAccessRecord eInstancePerpetualAccessRecord = new EInstancePerpetualAccessRecord();
                    eInstancePerpetualAccessRecord.setPerpetualAccessStartDate(perpetualAccessStartDate);
                    eInstancePerpetualAccessRecord.setHoldingsId(holdingsRecord.getHoldingsId());
                    eInstancePerpetualAccessRecord.setHoldingsRecord(holdingsRecord);
                    eInstancePerpetualAccessRecords.add(eInstancePerpetualAccessRecord);
                }
                holdingsRecord.seteInstancePerpetualAccessRecordList(eInstancePerpetualAccessRecords);
            }
        }

    }
}
