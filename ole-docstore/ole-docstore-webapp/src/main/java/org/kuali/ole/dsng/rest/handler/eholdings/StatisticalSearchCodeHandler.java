package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.EInstancePerpetualAccessRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsStatisticalSearchRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.StatisticalSearchRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.holdings.HoldingsHandler;
import org.kuali.ole.dsng.util.StatisticalSearchCodeUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/31/2015.
 */
public class StatisticalSearchCodeHandler extends HoldingsHandler {

    private final String TYPE = "Statistical Code";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get("holdingsRecord");
        String statisticalSearchCode = getStringValueFromJsonObject(requestJsonObject,TYPE);
        List<HoldingsStatisticalSearchRecord> holdingsStatisticalSearchRecords = holdingsRecord.getHoldingsStatisticalSearchRecords();
        if(CollectionUtils.isNotEmpty(holdingsStatisticalSearchRecords)) {
            for (Iterator<HoldingsStatisticalSearchRecord> iterator = holdingsStatisticalSearchRecords.iterator(); iterator.hasNext(); ) {
                HoldingsStatisticalSearchRecord holdingsStatisticalSearchRecord = iterator.next();
                if(null != holdingsStatisticalSearchRecord.getStatisticalSearchRecord() &&
                        StringUtils.equals(holdingsStatisticalSearchRecord.getStatisticalSearchRecord().getCode(),statisticalSearchCode)) {
                    exchange.add("matchedItem", holdingsRecord);
                }
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        String statisticalSearchCode = getStringValueFromJsonObject(requestJsonObject,TYPE);
        HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get("holdingsRecord");

        StatisticalSearchRecord statisticalSearchRecord = new StatisticalSearchCodeUtil().fetchStatisticalSearchRecordByCode(statisticalSearchCode);
        if (null != statisticalSearchRecord) {
            List<HoldingsStatisticalSearchRecord> holdingsStatisticalSearchRecords = holdingsRecord.getHoldingsStatisticalSearchRecords();
            if(CollectionUtils.isNotEmpty(holdingsStatisticalSearchRecords)) {
                for (Iterator<HoldingsStatisticalSearchRecord> iterator = holdingsStatisticalSearchRecords.iterator(); iterator.hasNext(); ) {
                    HoldingsStatisticalSearchRecord holdingsStatisticalSearchRecord = iterator.next();
                    holdingsStatisticalSearchRecord.setStatisticalSearchRecord(statisticalSearchRecord);
                }
            } else {
                holdingsStatisticalSearchRecords = new ArrayList<HoldingsStatisticalSearchRecord>();
                HoldingsStatisticalSearchRecord holdingsStatisticalSearchRecord = new HoldingsStatisticalSearchRecord();
                holdingsStatisticalSearchRecord.setStatisticalSearchId(statisticalSearchRecord.getStatisticalSearchId());
                holdingsStatisticalSearchRecord.setStatisticalSearchRecord(statisticalSearchRecord);
                holdingsStatisticalSearchRecord.setHoldingsId(holdingsRecord.getHoldingsId());
                holdingsStatisticalSearchRecord.setHoldingsRecord(holdingsRecord);
                holdingsStatisticalSearchRecords.add(holdingsStatisticalSearchRecord);
                holdingsRecord.setHoldingsStatisticalSearchRecords(holdingsStatisticalSearchRecords);
            }
        }
        exchange.add("holdingsRecord", holdingsRecord);

    }
}
