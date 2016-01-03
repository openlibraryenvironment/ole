package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.EInstanceCoverageRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsStatisticalSearchRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.holdings.HoldingsHandler;

import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/31/2015.
 */
public class CoverageStartDateHandler extends HoldingsHandler {

    private final String TYPE = "Coverage Start Date";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get("holdingsRecord");
        String coverageStartDate = getStringValueFromJsonObject(requestJsonObject,TYPE);
        List<EInstanceCoverageRecord> eInstanceCoverageRecords = holdingsRecord.geteInstanceCoverageRecordList();
        if(CollectionUtils.isNotEmpty(eInstanceCoverageRecords)) {
            for (Iterator<EInstanceCoverageRecord> iterator = eInstanceCoverageRecords.iterator(); iterator.hasNext(); ) {
                EInstanceCoverageRecord eInstanceCoverageRecord = iterator.next();
                if(StringUtils.equals(eInstanceCoverageRecord.getCoverageStartDate(),coverageStartDate)) {
                    exchange.add("matchedItem", holdingsRecord);
                }
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {

        //Todo : Need to get the information about the process.

    }
}
