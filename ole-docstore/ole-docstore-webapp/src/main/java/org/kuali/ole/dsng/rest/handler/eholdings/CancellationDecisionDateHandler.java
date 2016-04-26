package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.handler.holdings.HoldingsHandler;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/31/2015.
 */
public class CancellationDecisionDateHandler extends HoldingsHandler {

    private final String TYPE = "Cancellation Decision Date";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String cancellationDecisionDate = getStringValueFromJsonObject(requestJsonObject, TYPE);
        List<String> parsedValues = parseCommaSeperatedValues(cancellationDecisionDate);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String cancellationDecisionDateValue =  iterator.next();
            try{
                Date parsedDate = DOCSTORE_DATE_FORMAT.parse(cancellationDecisionDateValue);
                if (holdingRecord.getCancellationDecisionDate().compareTo(new Timestamp(parsedDate.getTime())) == 0) {
                    exchange.add(OleNGConstants.MATCHED_HOLDINGS, Boolean.TRUE);
                    exchange.add(OleNGConstants.MATCHED_VALUE, cancellationDecisionDateValue);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, TYPE);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
            String cancellationDecisionDate  = listFromJSONArray.get(0);
            HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
            try{
                Date parsedDate = DOCSTORE_DATE_FORMAT.parse(cancellationDecisionDate);
                holdingRecord.setCancellationDecisionDate(new Timestamp(parsedDate.getTime()));
            } catch(Exception e) {
                e.printStackTrace();
            }
            exchange.add(OleNGConstants.HOLDINGS_RECORD, holdingRecord);
        }
    }
}
