package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsUriRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.holdings.HoldingsHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 1/13/2016.
 */
public class LinkTextHandler extends HoldingsHandler {

    private final String TYPE = "Link Text";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get("holdingsRecord");
        String linkText = getStringValueFromJsonObject(requestJsonObject, TYPE);
        List<HoldingsUriRecord> holdingsUriRecords = holdingRecord.getHoldingsUriRecords();
        if(CollectionUtils.isNotEmpty(holdingsUriRecords)) {
            for (Iterator<HoldingsUriRecord> iterator = holdingsUriRecords.iterator(); iterator.hasNext(); ) {
                HoldingsUriRecord holdingsUriRecord = iterator.next();
                if(StringUtils.equals(holdingsUriRecord.getText(),linkText)) {
                    exchange.add("matchedHoldings", holdingRecord);
                }
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        String linkText = getStringValueFromJsonObject(requestJsonObject, TYPE);
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get("holdingsRecord");
        List<HoldingsUriRecord> holdingsUriRecords = holdingRecord.getHoldingsUriRecords();
        if(CollectionUtils.isNotEmpty(holdingsUriRecords)) {
            for (Iterator<HoldingsUriRecord> iterator = holdingsUriRecords.iterator(); iterator.hasNext(); ) {
                HoldingsUriRecord holdingsUriRecord = iterator.next();
                holdingsUriRecord.setText(linkText);
            }
        } else{
            holdingsUriRecords = new ArrayList<HoldingsUriRecord>();
            HoldingsUriRecord holdingsUriRecord = new HoldingsUriRecord();
            holdingsUriRecord.setText(linkText);
            holdingsUriRecord.setHoldingsId(holdingRecord.getHoldingsId());
            holdingsUriRecords.add(holdingsUriRecord);
            holdingRecord.setHoldingsUriRecords(holdingsUriRecords);
        }
        exchange.add("holdingsRecord", holdingRecord);
    }
}
