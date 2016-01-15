package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.AccessLocation;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsAccessLocation;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.holdings.HoldingsHandler;
import org.kuali.ole.dsng.util.AccessLocationUtil;

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
        List<HoldingsAccessLocation> holdingsAccessLocations = holdingRecord.getHoldingsAccessLocations();
        if(CollectionUtils.isNotEmpty(holdingsAccessLocations)) {
            for (Iterator<HoldingsAccessLocation> iterator = holdingsAccessLocations.iterator(); iterator.hasNext(); ) {
                HoldingsAccessLocation holdingsAccessLocation = iterator.next();
                if(null != holdingsAccessLocation.getAccessLocation() && StringUtils.equals(holdingsAccessLocation.getAccessLocation().getCode(),accessLocation)) {
                    exchange.add(OleNGConstants.MATCHED_HOLDINGS, holdingRecord);
                    break;
                }
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String accessLocationCode = getStringValueFromJsonObject(requestJsonObject, TYPE);
        AccessLocation accessLocation = new AccessLocationUtil().fetchAccessLocationByCode(accessLocationCode);
        if(null !=  accessLocation) {
            List<HoldingsAccessLocation> holdingsAccessLocations = holdingRecord.getHoldingsAccessLocations();
            if(CollectionUtils.isNotEmpty(holdingsAccessLocations)) {
                for (Iterator<HoldingsAccessLocation> iterator = holdingsAccessLocations.iterator(); iterator.hasNext(); ) {
                    HoldingsAccessLocation holdingsAccessLocation = iterator.next();
                    holdingsAccessLocation.setAccessLocation(accessLocation);
                }
            } else {
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
