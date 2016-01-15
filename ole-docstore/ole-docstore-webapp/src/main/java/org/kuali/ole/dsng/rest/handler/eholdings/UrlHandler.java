package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.document.content.bib.marc.Collection;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsUriRecord;
import org.kuali.ole.dsng.rest.Exchange;
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
        List<HoldingsUriRecord> holdingsUriRecords = holdingRecord.getHoldingsUriRecords();
        if(CollectionUtils.isNotEmpty(holdingsUriRecords)) {
            for (Iterator<HoldingsUriRecord> iterator = holdingsUriRecords.iterator(); iterator.hasNext(); ) {
                HoldingsUriRecord holdingsUriRecord = iterator.next();
                if(StringUtils.equals(holdingsUriRecord.getUri(),url)) {
                    exchange.add(OleNGConstants.MATCHED_HOLDINGS, holdingRecord);
                }
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        String url = getStringValueFromJsonObject(requestJsonObject, TYPE);
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        List<HoldingsUriRecord> holdingsUriRecords = holdingRecord.getHoldingsUriRecords();
        if(CollectionUtils.isNotEmpty(holdingsUriRecords)) {
            for (Iterator<HoldingsUriRecord> iterator = holdingsUriRecords.iterator(); iterator.hasNext(); ) {
                HoldingsUriRecord holdingsUriRecord = iterator.next();
                holdingsUriRecord.setUri(url);
            }
        } else{
            holdingsUriRecords = new ArrayList<HoldingsUriRecord>();
            HoldingsUriRecord holdingsUriRecord = new HoldingsUriRecord();
            holdingsUriRecord.setUri(url);
            holdingsUriRecord.setHoldingsId(holdingRecord.getHoldingsId());
            holdingsUriRecords.add(holdingsUriRecord);
            holdingRecord.setHoldingsUriRecords(holdingsUriRecords);
        }
        exchange.add(OleNGConstants.HOLDINGS_RECORD, holdingRecord);
    }
}
