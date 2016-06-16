package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsNoteRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.handler.HoldingsAndItemsGeneralHandler;

import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 12/24/15.
 */
public abstract class HoldingsHandler extends HoldingsAndItemsGeneralHandler {
    public abstract void processDataMappings(JSONObject requestJsonObject, Exchange exchange);

    public  boolean doesAlreadyNoteExist(String noteType, String publicNote, HoldingsRecord holdingRecord) {
        List<HoldingsNoteRecord> holdingsNoteRecords = holdingRecord.getHoldingsNoteRecords();
        if(CollectionUtils.isNotEmpty(holdingsNoteRecords)) {
            for (Iterator<HoldingsNoteRecord> iterator = holdingsNoteRecords.iterator(); iterator.hasNext(); ) {
                HoldingsNoteRecord holdingsNoteRecord = iterator.next();
                if(null != holdingsNoteRecord && StringUtils.isNotBlank(holdingsNoteRecord.getType()) &&
                        holdingsNoteRecord.getType().equalsIgnoreCase(noteType)) {
                    if(StringUtils.equals(holdingsNoteRecord.getNote(),publicNote)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public HoldingsNoteRecord createPublicOrNonPublicNote(String noteType, String publicNote, String holdingsId) {
        HoldingsNoteRecord holdingsNoteRecord = new HoldingsNoteRecord();
        holdingsNoteRecord.setNote(publicNote);
        holdingsNoteRecord.setType(noteType);
        holdingsNoteRecord.setHoldingsId(holdingsId);
        return holdingsNoteRecord;
    }
}
