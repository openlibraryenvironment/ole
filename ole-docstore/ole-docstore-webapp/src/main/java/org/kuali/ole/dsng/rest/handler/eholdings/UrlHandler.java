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
 * Created by SheikS on 12/31/2015.
 */
public class UrlHandler extends HoldingsHandler {

    private final String TYPE = "URL";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String url = getStringValueFromJsonObject(requestJsonObject, TYPE);
        List<String> parsedValues = parseCommaSeperatedValues(url);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String urlValue = iterator.next();
            List<HoldingsUriRecord> holdingsUriRecords = holdingRecord.getHoldingsUriRecords();
            if(CollectionUtils.isNotEmpty(holdingsUriRecords)) {
                for (Iterator<HoldingsUriRecord> holdingsUriRecordIterator = holdingsUriRecords.iterator(); holdingsUriRecordIterator.hasNext(); ) {
                    HoldingsUriRecord holdingsUriRecord = holdingsUriRecordIterator.next();
                    if(StringUtils.equals(holdingsUriRecord.getUri(),urlValue)) {
                        exchange.add(OleNGConstants.MATCHED_HOLDINGS, Boolean.TRUE);
                        exchange.add(OleNGConstants.MATCHED_VALUE, urlValue);
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
            HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
            List<HoldingsUriRecord> holdingsUriRecords = holdingRecord.getHoldingsUriRecords();
            if(CollectionUtils.isNotEmpty(holdingsUriRecords)) {
                for (Iterator<String> uriRecordIterator = listFromJSONArray.iterator(); uriRecordIterator.hasNext(); ) {
                    String url = uriRecordIterator.next();
                    for (Iterator<HoldingsUriRecord> iterator = holdingsUriRecords.iterator(); iterator.hasNext(); ) {
                        HoldingsUriRecord holdingsUriRecord = iterator.next();
                        holdingsUriRecord.setUri(url);
                    }
                }
            } else {
                holdingsUriRecords = new ArrayList<HoldingsUriRecord>();
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String url = iterator.next();
                    HoldingsUriRecord holdingsUriRecord = new HoldingsUriRecord();
                    holdingsUriRecord.setUri(url);
                    holdingsUriRecord.setHoldingsId(holdingRecord.getHoldingsId());
                    holdingsUriRecords.add(holdingsUriRecord);
                }
                holdingRecord.setHoldingsUriRecords(holdingsUriRecords);

            }

            exchange.add(OleNGConstants.HOLDINGS_RECORD, holdingRecord);
        }
    }
}
