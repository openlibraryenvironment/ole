package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.EInstanceCoverageRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.EInstancePerpetualAccessRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.holdings.HoldingsHandler;

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
        HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get("holdingsRecord");
        String perpetualAccessStartDate = getStringValueFromJsonObject(requestJsonObject,TYPE);
        List<EInstancePerpetualAccessRecord> eInstanceCoverageRecords = holdingsRecord.geteInstancePerpetualAccessRecordList();
        if(CollectionUtils.isNotEmpty(eInstanceCoverageRecords)) {
            for (Iterator<EInstancePerpetualAccessRecord> iterator = eInstanceCoverageRecords.iterator(); iterator.hasNext(); ) {
                EInstancePerpetualAccessRecord eInstancePerpetualAccessRecord = iterator.next();
                if(StringUtils.equals(eInstancePerpetualAccessRecord.getPerpetualAccessStartDate(),perpetualAccessStartDate)) {
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
