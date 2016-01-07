package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.OLEHoldingsDonorRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.holdings.HoldingsHandler;

import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/31/2015.
 */
public class DonorPublicDisplayHandler extends HoldingsHandler {

    private final String TYPE = "Donor Public Display";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get("holdingsRecord");
        String donorPublicDisplay = getStringValueFromJsonObject(requestJsonObject, TYPE);
        List<OLEHoldingsDonorRecord> donorList = holdingRecord.getDonorList();
        if(CollectionUtils.isNotEmpty(donorList)) {
            for (Iterator<OLEHoldingsDonorRecord> iterator = donorList.iterator(); iterator.hasNext(); ) {
                OLEHoldingsDonorRecord oleHoldingsDonorRecord = iterator.next();
                if(StringUtils.equals(oleHoldingsDonorRecord.getDonorPublicDisplay(),donorPublicDisplay)) {
                    exchange.add("matchedItem", holdingRecord);
                    break;
                }
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get("holdingsRecord");
        String donorPublicDisplay = getStringValueFromJsonObject(requestJsonObject, TYPE);
        List<OLEHoldingsDonorRecord> donorList = holdingRecord.getDonorList();
        if(CollectionUtils.isNotEmpty(donorList)) {
            for (Iterator<OLEHoldingsDonorRecord> iterator = donorList.iterator(); iterator.hasNext(); ) {
                OLEHoldingsDonorRecord oleHoldingsDonorRecord = iterator.next();
                oleHoldingsDonorRecord.setDonorPublicDisplay(donorPublicDisplay);
            }
        }

    }
}
