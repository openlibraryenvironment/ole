package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsNoteRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by SheikS on 1/18/2016.
 */
public class PublicNoteHandlerTest {

    @Test
    public void testProcessDataMappings() throws Exception {

        JSONObject jsonObject = new JSONObject();
        JSONArray notes = new JSONArray();
        notes.put("Note 1");
        notes.put("Note 2");
        notes.put("Note 3");
        notes.put("Note 4");
        notes.put("Note 5");
        jsonObject.put("Public Note", notes);

        PublicNoteHandler publicNoteHandler = new PublicNoteHandler();
        Exchange exchange = new Exchange();
        exchange.add(OleNGConstants.HOLDINGS_RECORD, new HoldingsRecord());
        publicNoteHandler.processDataMappings(jsonObject, exchange);
        HoldingsRecord holdingsRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        List<HoldingsNoteRecord> updatedList = holdingsRecord.getHoldingsNoteRecords();
        assertTrue(CollectionUtils.isNotEmpty(updatedList));
        System.out.println(updatedList);

    }
}