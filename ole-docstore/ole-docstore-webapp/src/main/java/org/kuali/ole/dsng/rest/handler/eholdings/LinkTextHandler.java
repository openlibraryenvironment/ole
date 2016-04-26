package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsUriRecord;
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

        HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);

        String linkText = getStringValueFromJsonObject(requestJsonObject, TYPE);
        List<String> parsedValues = parseCommaSeperatedValues(linkText);
        List<HoldingsUriRecord> holdingsUriRecords = holdingsRecord.getHoldingsUriRecords();
        for (Iterator<String> iterator1 = parsedValues.iterator(); iterator1.hasNext(); ) {
            String linkTextValue = iterator1.next();
            if (CollectionUtils.isNotEmpty(holdingsUriRecords)) {
                for (Iterator<HoldingsUriRecord> iterator2 = holdingsUriRecords.iterator(); iterator2.hasNext(); ) {
                    HoldingsUriRecord holdingsUriRecord = iterator2.next();
                    if (StringUtils.equals(holdingsUriRecord.getText(), linkTextValue)) {
                        exchange.add(OleNGConstants.MATCHED_HOLDINGS, Boolean.TRUE);
                        exchange.add(OleNGConstants.MATCHED_VALUE, linkTextValue);
                    }
                }
            }
        }

    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, TYPE);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        if (CollectionUtils.isNotEmpty(listFromJSONArray)) {
            HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
            List<HoldingsUriRecord> holdingsUriRecords = holdingRecord.getHoldingsUriRecords();
            if (CollectionUtils.isNotEmpty(holdingsUriRecords)) {
                for (Iterator<String> uriRecordIterator = listFromJSONArray.iterator(); uriRecordIterator.hasNext(); ) {
                    String linkText = uriRecordIterator.next();
                    for (Iterator<HoldingsUriRecord> iterator = holdingsUriRecords.iterator(); iterator.hasNext(); ) {
                        HoldingsUriRecord holdingsUriRecord = iterator.next();
                        holdingsUriRecord.setText(linkText);
                    }
                }
            } else {
                holdingsUriRecords = new ArrayList<HoldingsUriRecord>();
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String linkText = iterator.next();
                    HoldingsUriRecord holdingsUriRecord = new HoldingsUriRecord();
                    holdingsUriRecord.setText(linkText);
                    holdingsUriRecord.setHoldingsId(holdingRecord.getHoldingsId());
                    holdingsUriRecords.add(holdingsUriRecord);
                }
                holdingRecord.setHoldingsUriRecords(holdingsUriRecords);

            }
        }
    }
}
