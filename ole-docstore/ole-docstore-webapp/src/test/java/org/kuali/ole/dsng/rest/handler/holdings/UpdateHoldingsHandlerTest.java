package org.kuali.ole.dsng.rest.handler.holdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.cxf.common.i18n.Exception;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.util.HoldingsUtil;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Created by SheikS on 2/2/2016.
 */
public class UpdateHoldingsHandlerTest {

    @Mock
    BusinessObjectService mockBusinessObjectService;

    @Mock
    SolrRequestReponseHandler mockSolrRequestReponseHandler;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testProcessIfDeleteAllExistOpsFound() throws JSONException {
        HoldingsRecord holdingsRecord = new HoldingsRecord();
        ArrayList<ItemRecord> itemRecords = new ArrayList<ItemRecord>();
        ItemRecord itemRecord1 = new ItemRecord();
        itemRecord1.setItemId("10001");
        itemRecords.add(itemRecord1);

        ItemRecord itemRecord2 = new ItemRecord();
        itemRecord2.setItemId("10001");
        itemRecords.add(itemRecord2);

        ItemRecord itemRecord3 = new ItemRecord();
        itemRecord3.setItemId("10001");
        itemRecords.add(itemRecord3);

        holdingsRecord.setItemRecords(itemRecords);

        JSONObject bibData = new JSONObject();
        JSONObject addedops = new JSONObject();
        addedops.put(OleNGConstants.ITEM, OleNGConstants.DELETE_ALL_EXISTING_AND_ADD);
        bibData.put(OleNGConstants.ADDED_OPS, addedops);

        UpdateHoldingsHandler updateHoldingsHandler = new UpdateHoldingsHandler();
        updateHoldingsHandler.setBusinessObjectService(mockBusinessObjectService);
        HoldingsUtil.getInstance().setBusinessObjectService(mockBusinessObjectService);
        updateHoldingsHandler.setSolrRequestReponseHandler(mockSolrRequestReponseHandler);
        HoldingsUtil.getInstance().setSolrRequestReponseHandler(mockSolrRequestReponseHandler);
        HoldingsUtil.getInstance().processIfDeleteAllExistOpsFound(holdingsRecord,bibData, new Exchange());
        assertTrue(CollectionUtils.isEmpty(holdingsRecord.getItemRecords()));
    }

    @Test
    public void testProcessIfDeleteAll() throws JSONException {
        HoldingsRecord holdingsRecord = new HoldingsRecord();
        ArrayList<ItemRecord> itemRecords = new ArrayList<ItemRecord>();
        ItemRecord itemRecord1 = new ItemRecord();
        itemRecord1.setItemId("10001");
        itemRecords.add(itemRecord1);

        ItemRecord itemRecord2 = new ItemRecord();
        itemRecord2.setItemId("10001");
        itemRecords.add(itemRecord2);

        ItemRecord itemRecord3 = new ItemRecord();
        itemRecord3.setItemId("10001");
        itemRecords.add(itemRecord3);

        holdingsRecord.setItemRecords(itemRecords);

        JSONObject bibData = new JSONObject();
        JSONObject addedops = new JSONObject();
        addedops.put(OleNGConstants.ITEM, OleNGConstants.DELETE_ALL);
        bibData.put(OleNGConstants.ADDED_OPS, addedops);

        UpdateHoldingsHandler updateHoldingsHandler = new UpdateHoldingsHandler();
        updateHoldingsHandler.setBusinessObjectService(mockBusinessObjectService);
        HoldingsUtil.getInstance().setBusinessObjectService(mockBusinessObjectService);
        updateHoldingsHandler.setSolrRequestReponseHandler(mockSolrRequestReponseHandler);
        HoldingsUtil.getInstance().setSolrRequestReponseHandler(mockSolrRequestReponseHandler);
        HoldingsUtil.getInstance().processIfDeleteAllExistOpsFound(holdingsRecord,bibData, new Exchange());
        assertTrue(CollectionUtils.isEmpty(holdingsRecord.getItemRecords()));
    }

}