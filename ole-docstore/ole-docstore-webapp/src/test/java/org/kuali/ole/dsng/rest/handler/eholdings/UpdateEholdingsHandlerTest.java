package org.kuali.ole.dsng.rest.handler.eholdings;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.document.EHoldings;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsUriRecord;
import org.kuali.ole.dsng.dao.HoldingDAO;
import org.kuali.ole.dsng.rest.processor.OleDsNgOverlayProcessor;
import org.kuali.ole.dsng.service.OleDsNGMemorizeService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by SheikS on 1/25/2016.
 */
public class UpdateEholdingsHandlerTest {

    private JSONObject dataMappingJSONForEHoldings1 = new JSONObject();
    private JSONObject dataMappingJSONForEHoldings2 = new JSONObject();

    @Mock
    HoldingDAO mockHoldingDAO;

    @Mock
    OleDsNGMemorizeService mockOleDsNGMemorizeService;

    @Mock
    BusinessObjectService mockBusinessObjectService;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcess() throws Exception {
        BibRecord bibRecord = new BibRecord();
        bibRecord.setBibId("1001");
        ArrayList<HoldingsRecord> holdingsRecords = new ArrayList<HoldingsRecord>();

        HoldingsRecord holdingsRecord1 = new HoldingsRecord();
        holdingsRecord1.setHoldingsId("1");
        ArrayList<HoldingsUriRecord> holdingsUriRecords1 = new ArrayList<HoldingsUriRecord>();
        HoldingsUriRecord holdingsUriRecord1 = new HoldingsUriRecord();
        holdingsUriRecord1.setUri("https://www.google.com");
        holdingsUriRecord1.setText("google");
        holdingsUriRecords1.add(holdingsUriRecord1);
        holdingsRecord1.setHoldingsUriRecords(holdingsUriRecords1);
        holdingsRecord1.setHoldingsType(EHoldings.ELECTRONIC);
        holdingsRecords.add(holdingsRecord1);


        HoldingsRecord holdingsRecord2 = new HoldingsRecord();
        holdingsRecord2.setHoldingsId("2");
        ArrayList<HoldingsUriRecord> holdingsUriRecords2 = new ArrayList<HoldingsUriRecord>();
        HoldingsUriRecord holdingsUriRecord2 = new HoldingsUriRecord();
        holdingsUriRecord2.setUri("https://www.gmail.com");
        holdingsUriRecord2.setText("gmail");
        holdingsUriRecords2.add(holdingsUriRecord2);
        holdingsRecord2.setHoldingsUriRecords(holdingsUriRecords2);
        holdingsRecord2.setHoldingsType(EHoldings.ELECTRONIC);
        holdingsRecords.add(holdingsRecord2);

        bibRecord.setHoldingsRecords(holdingsRecords);

        JSONObject eholdingsData = new JSONObject();
        JSONObject matchpointsForEHoldings = new JSONObject();
        matchpointsForEHoldings.put("Link Text", "google");
        eholdingsData.put(OleNGConstants.MATCH_POINT, matchpointsForEHoldings);

        JSONArray dataMappingsForEHoldings = new JSONArray();
        JSONArray linkText = new JSONArray();
        linkText.put("google");
        dataMappingJSONForEHoldings1.put("Link Text", linkText);
        JSONArray url = new JSONArray();
        url.put("https://drive.google.com");
        dataMappingJSONForEHoldings1.put("URL", url);

        JSONArray linkText2 = new JSONArray();
        linkText2.put("gmail");
        dataMappingJSONForEHoldings2.put("Link Text", linkText2);
        JSONArray ur12 = new JSONArray();
        ur12.put("https://www.gmail.com/mail");
        dataMappingJSONForEHoldings2.put("URL", ur12);
        dataMappingsForEHoldings.put(dataMappingJSONForEHoldings2);
        dataMappingsForEHoldings.put(dataMappingJSONForEHoldings1);
        eholdingsData.put(OleNGConstants.DATAMAPPING, dataMappingsForEHoldings);


        JSONObject bibData = new JSONObject();
        JSONObject holdingsData = new JSONObject();
        JSONObject itemData = new JSONObject();

        bibData.put(OleNGConstants.EHOLDINGS, eholdingsData);
        bibData.put(OleNGConstants.HOLDINGS, holdingsData);
        bibData.put(OleNGConstants.ITEM, itemData);
        bibData.put(OleNGConstants.UPDATED_BY, "ole-quickstart");
        bibData.put(OleNGConstants.UPDATED_DATE, DocstoreConstants.DOCSTORE_DATE_FORMAT.format(new Date()));
        JSONObject addedOps = new JSONObject();
        addedOps.put(OleNGConstants.EHOLDINGS, OleNGConstants.OVERLAY);
        bibData.put(OleNGConstants.ADDED_OPS, addedOps);

        Exchange exchange = new Exchange();
        new OleDsNgOverlayProcessor().prepareEHoldingsRecord(bibRecord, bibData, exchange);
        List eholdingsForCreate = (List) exchange.get(OleNGConstants.EHOLDINGS_FOR_CREATE);
        assertNotNull(eholdingsForCreate);
        assertTrue(eholdingsForCreate.size() == 1);

        List eholdingsForUpdate = (List) exchange.get(OleNGConstants.EHOLDINGS_FOR_UPDATE);
        assertNotNull(eholdingsForUpdate);
        assertTrue(eholdingsForUpdate.size() == 1);

        Mockito.when(mockOleDsNGMemorizeService.getHoldingDAO()).thenReturn(mockHoldingDAO);

        UpdateEholdingsHandler updateEholdingsHandler = new UpdateEholdingsHandler();
        updateEholdingsHandler.setOleDsNGMemorizeService(mockOleDsNGMemorizeService);
        updateEholdingsHandler.setBusinessObjectService(mockBusinessObjectService);
        updateEholdingsHandler.process(bibData, exchange);
    }

    @Test
    public void testForUrlMatchPoint() throws Exception {
        BibRecord bibRecord = new BibRecord();
        bibRecord.setBibId("1001");
        ArrayList<HoldingsRecord> holdingsRecords = new ArrayList<HoldingsRecord>();

        HoldingsRecord holdingsRecord1 = new HoldingsRecord();
        holdingsRecord1.setHoldingsId("1");
        ArrayList<HoldingsUriRecord> holdingsUriRecords1 = new ArrayList<HoldingsUriRecord>();
        HoldingsUriRecord holdingsUriRecord1 = new HoldingsUriRecord();
        holdingsUriRecord1.setUri("https://www.google.com");
        holdingsUriRecord1.setText("google");
        holdingsUriRecords1.add(holdingsUriRecord1);
        holdingsRecord1.setHoldingsUriRecords(holdingsUriRecords1);
        holdingsRecord1.setHoldingsType(EHoldings.ELECTRONIC);
        holdingsRecords.add(holdingsRecord1);


        HoldingsRecord holdingsRecord2 = new HoldingsRecord();
        holdingsRecord2.setHoldingsId("2");
        ArrayList<HoldingsUriRecord> holdingsUriRecords2 = new ArrayList<HoldingsUriRecord>();
        HoldingsUriRecord holdingsUriRecord2 = new HoldingsUriRecord();
        holdingsUriRecord2.setUri("https://www.gmail.com");
        holdingsUriRecord2.setText("gmail");
        holdingsUriRecords2.add(holdingsUriRecord2);
        holdingsRecord2.setHoldingsUriRecords(holdingsUriRecords2);
        holdingsRecord2.setHoldingsType(EHoldings.ELECTRONIC);
        holdingsRecords.add(holdingsRecord2);

        bibRecord.setHoldingsRecords(holdingsRecords);

        JSONObject eholdingsData = new JSONObject();
        JSONObject matchpointsForEHoldings = new JSONObject();
        matchpointsForEHoldings.put("URL", "https://www.google.com");
        eholdingsData.put(OleNGConstants.MATCH_POINT, matchpointsForEHoldings);

        JSONArray dataMappingsForEHoldings = new JSONArray();
        JSONArray linkText = new JSONArray();
        linkText.put("google Account");
        dataMappingJSONForEHoldings1.put("Link Text", linkText);
        JSONArray url = new JSONArray();
        url.put("https://www.google.com");
        dataMappingJSONForEHoldings1.put("URL", url);

        JSONArray linkText2 = new JSONArray();
        linkText2.put("gmail");
        dataMappingJSONForEHoldings2.put("Link Text", linkText2);
        JSONArray ur12 = new JSONArray();
        ur12.put("https://www.gmail.com/mail");
        dataMappingJSONForEHoldings2.put("URL", ur12);
        dataMappingsForEHoldings.put(dataMappingJSONForEHoldings2);
        dataMappingsForEHoldings.put(dataMappingJSONForEHoldings1);
        eholdingsData.put(OleNGConstants.DATAMAPPING, dataMappingsForEHoldings);


        JSONObject bibData = new JSONObject();
        JSONObject holdingsData = new JSONObject();
        JSONObject itemData = new JSONObject();

        bibData.put(OleNGConstants.EHOLDINGS, eholdingsData);
        bibData.put(OleNGConstants.HOLDINGS, holdingsData);
        bibData.put(OleNGConstants.ITEM, itemData);
        bibData.put(OleNGConstants.UPDATED_BY, "ole-quickstart");
        bibData.put(OleNGConstants.UPDATED_DATE, DocstoreConstants.DOCSTORE_DATE_FORMAT.format(new Date()));
        JSONObject addedOps = new JSONObject();
        addedOps.put(OleNGConstants.EHOLDINGS, OleNGConstants.OVERLAY);
        bibData.put(OleNGConstants.ADDED_OPS, addedOps);

        Exchange exchange = new Exchange();
        new OleDsNgOverlayProcessor().prepareEHoldingsRecord(bibRecord, bibData, exchange);
        List eholdingsForCreate = (List) exchange.get(OleNGConstants.EHOLDINGS_FOR_CREATE);
        assertNotNull(eholdingsForCreate);
        assertTrue(eholdingsForCreate.size() == 1);

        List eholdingsForUpdate = (List) exchange.get(OleNGConstants.EHOLDINGS_FOR_UPDATE);
        assertNotNull(eholdingsForUpdate);
        assertTrue(eholdingsForUpdate.size() == 1);

        Mockito.when(mockOleDsNGMemorizeService.getHoldingDAO()).thenReturn(mockHoldingDAO);
        UpdateEholdingsHandler updateEholdingsHandler = new UpdateEholdingsHandler();
        updateEholdingsHandler.setOleDsNGMemorizeService(mockOleDsNGMemorizeService);
        updateEholdingsHandler.setBusinessObjectService(mockBusinessObjectService);
        updateEholdingsHandler.process(bibData, exchange);
    }
}