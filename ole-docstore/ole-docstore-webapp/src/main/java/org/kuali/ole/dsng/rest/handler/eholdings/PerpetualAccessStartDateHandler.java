package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
        List<EInstancePerpetualAccessRecord> eInstanceCoverageRecords = holdingsRecord.geteInstancePerpetualAccessRecordList();
        if(CollectionUtils.isNotEmpty(eInstanceCoverageRecords)) {
            for (Iterator<EInstancePerpetualAccessRecord> iterator = eInstanceCoverageRecords.iterator(); iterator.hasNext(); ) {
                EInstancePerpetualAccessRecord eInstancePerpetualAccessRecord = iterator.next();
                if(StringUtils.equals(eInstancePerpetualAccessRecord.getPerpetualAccessStartDate(),perpetualAccessStartDate)) {
                    exchange.add(OleNGConstants.MATCHED_HOLDINGS, holdingsRecord);
                }
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String perpetualAccessStartDate = getStringValueFromJsonObject(requestJsonObject,TYPE);
        if(StringUtils.isNotBlank(perpetualAccessStartDate)) {
            try {
                Date parsedDate = DOCSTORE_DATE_FORMAT.parse(perpetualAccessStartDate);
                List<EInstancePerpetualAccessRecord> eInstancePerpetualAccessRecords = holdingsRecord.geteInstancePerpetualAccessRecordList();
                if(CollectionUtils.isNotEmpty(eInstancePerpetualAccessRecords)) {
                    for (Iterator<EInstancePerpetualAccessRecord> iterator = eInstancePerpetualAccessRecords.iterator(); iterator.hasNext(); ) {
                        EInstancePerpetualAccessRecord eInstancePerpetualAccessRecord = iterator.next();
                        eInstancePerpetualAccessRecord.setPerpetualAccessStartDate(perpetualAccessStartDate);
                    }
                } else {
                    eInstancePerpetualAccessRecords = new ArrayList<EInstancePerpetualAccessRecord>();
                    EInstancePerpetualAccessRecord eInstancePerpetualAccessRecord = new EInstancePerpetualAccessRecord();
                    eInstancePerpetualAccessRecord.setPerpetualAccessStartDate(perpetualAccessStartDate);
                    eInstancePerpetualAccessRecord.setHoldingsId(holdingsRecord.getHoldingsId());
                    eInstancePerpetualAccessRecord.setHoldingsRecord(holdingsRecord);
                    eInstancePerpetualAccessRecords.add(eInstancePerpetualAccessRecord);
                    holdingsRecord.seteInstancePerpetualAccessRecordList(eInstancePerpetualAccessRecords);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
