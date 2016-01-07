package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.AccessLocation;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsAccessLocation;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.holdings.HoldingsHandler;
import org.kuali.ole.dsng.util.AccessLocationUtil;

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
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get("holdingsRecord");
        String accessLocation = getStringValueFromJsonObject(requestJsonObject, TYPE);
        List<HoldingsAccessLocation> holdingsAccessLocations = holdingRecord.getHoldingsAccessLocations();
        if(CollectionUtils.isNotEmpty(holdingsAccessLocations)) {
            for (Iterator<HoldingsAccessLocation> iterator = holdingsAccessLocations.iterator(); iterator.hasNext(); ) {
                HoldingsAccessLocation holdingsAccessLocation = iterator.next();
                if(null != holdingsAccessLocation.getAccessLocation() && StringUtils.equals(holdingsAccessLocation.getAccessLocation().getCode(),accessLocation)) {
                    exchange.add("matchedItem", holdingRecord);
                    break;
                }
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get("holdingsRecord");
        String accessLocationCode = getStringValueFromJsonObject(requestJsonObject, TYPE);
        AccessLocation accessLocation = new AccessLocationUtil().fetchAccessLocationByCode(accessLocationCode);
        if(null !=  accessLocation) {
            List<HoldingsAccessLocation> holdingsAccessLocations = holdingRecord.getHoldingsAccessLocations();
            if(CollectionUtils.isNotEmpty(holdingsAccessLocations)) {
                for (Iterator<HoldingsAccessLocation> iterator = holdingsAccessLocations.iterator(); iterator.hasNext(); ) {
                    HoldingsAccessLocation holdingsAccessLocation = iterator.next();
                    holdingsAccessLocation.setAccessLocation(accessLocation);
                }
            }
        }
        exchange.add("holdingsRecord", holdingRecord);
    }
}
