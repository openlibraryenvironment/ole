package org.kuali.ole.dsng.rest.handler.bib;

import org.apache.commons.collections.CollectionUtils;
import org.apache.cxf.common.i18n.Exception;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.document.PHoldings;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by SheikS on 2/1/2016.
 */
public class UpdateBibHandlerTest {

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
        holdingsRecord.setHoldingsId("101");
        holdingsRecord.setHoldingsType(PHoldings.PRINT);
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
        BibRecord bibRecord = new BibRecord();
        bibRecord.setBibId("1");
        ArrayList<HoldingsRecord> holdingsRecords = new ArrayList<HoldingsRecord>();
        holdingsRecords.add(holdingsRecord);
        bibRecord.setHoldingsRecords(holdingsRecords);

        JSONObject bibData = new JSONObject();
        JSONObject addedops = new JSONObject();
        addedops.put(OleNGConstants.ITEM,OleNGConstants.DELETE_ALL_EXISTING_AND_ADD);
        addedops.put(OleNGConstants.HOLDINGS,OleNGConstants.DELETE_ALL_EXISTING_AND_ADD);
        bibData.put(OleNGConstants.ADDED_OPS, addedops);

        UpdateBibHandler updateBibHandler = new UpdateBibHandler();
        updateBibHandler.setBusinessObjectService(mockBusinessObjectService);
        updateBibHandler.setSolrRequestReponseHandler(mockSolrRequestReponseHandler);
        updateBibHandler.processIfDeleteAllExistOpsFound(bibRecord,bibData);
        assertTrue(CollectionUtils.isEmpty(bibRecord.getHoldingsRecords()));
    }

}