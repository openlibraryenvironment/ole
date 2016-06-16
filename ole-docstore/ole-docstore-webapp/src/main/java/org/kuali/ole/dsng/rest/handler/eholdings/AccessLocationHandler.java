package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.AccessLocation;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsAccessLocation;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.handler.holdings.HoldingsHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/31/2015.
 */
public class AccessLocationHandler extends HoldingsHandler {

    private final String TYPE = "Access Location";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String accessLocation = getStringValueFromJsonObject(requestJsonObject, TYPE);
        List<String> parsedValues = parseCommaSeperatedValues(accessLocation);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String accessLocationValue = iterator.next();
            List<HoldingsAccessLocation> holdingsAccessLocations = holdingRecord.getHoldingsAccessLocations();
            if(CollectionUtils.isNotEmpty(holdingsAccessLocations)) {
                for (Iterator<HoldingsAccessLocation> holdingsAccessLocationIterator = holdingsAccessLocations.iterator(); holdingsAccessLocationIterator.hasNext(); ) {
                    HoldingsAccessLocation holdingsAccessLocation = holdingsAccessLocationIterator.next();
                    if(null != holdingsAccessLocation.getAccessLocation() && StringUtils.equals(holdingsAccessLocation.getAccessLocation().getCode(),accessLocationValue)) {
                        exchange.add(OleNGConstants.MATCHED_HOLDINGS, Boolean.TRUE);
                        exchange.add(OleNGConstants.MATCHED_VALUE, accessLocationValue);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, TYPE);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
            List<HoldingsAccessLocation> holdingsAccessLocations = holdingRecord.getHoldingsAccessLocations();
            if (CollectionUtils.isNotEmpty(holdingsAccessLocations)) {
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String accessLocationCode = iterator.next();
                    AccessLocation accessLocation = getOleDsNGMemorizeService().fetchAccessLocationByCode(accessLocationCode);
                    if (null != accessLocation) {
                        for (Iterator<HoldingsAccessLocation> iterator1 = holdingsAccessLocations.iterator(); iterator1.hasNext(); ) {
                            HoldingsAccessLocation holdingsAccessLocation = iterator1.next();
                            holdingsAccessLocation.setAccessLocation(accessLocation);
                        }
                    }
                }
            } else {
                for (Iterator<String> iterator = listFromJSONArray.iterator(); iterator.hasNext(); ) {
                    String accessLocationCode = iterator.next();
                    AccessLocation accessLocation = getOleDsNGMemorizeService().fetchAccessLocationByCode(accessLocationCode);
                    holdingsAccessLocations = new ArrayList<HoldingsAccessLocation>();
                    HoldingsAccessLocation holdingsAccessLocation = new HoldingsAccessLocation();
                    holdingsAccessLocation.setAccessLocation(accessLocation);
                    holdingsAccessLocation.setHoldingsId(holdingRecord.getHoldingsId());
                    holdingsAccessLocation.setHoldingsRecord(holdingRecord);
                    holdingsAccessLocations.add(holdingsAccessLocation);
                    holdingRecord.setHoldingsAccessLocations(holdingsAccessLocations);
                }
            }
            exchange.add(OleNGConstants.HOLDINGS_RECORD, holdingRecord);
        }

    }
}
