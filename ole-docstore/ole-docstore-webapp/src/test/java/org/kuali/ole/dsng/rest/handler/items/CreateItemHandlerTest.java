package org.kuali.ole.dsng.rest.handler.items;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.dao.ItemDAO;
import org.kuali.ole.dsng.model.HoldingsRecordAndDataMapping;
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
public class CreateItemHandlerTest {

    private JSONObject dataMappingJSONForEHoldings1 = new JSONObject();
    private JSONObject dataMappingJSONForEHoldings2 = new JSONObject();

    @Mock
    ItemDAO mockItemDAO;
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

        List<HoldingsRecordAndDataMapping> holdingsRecordAndDataMappings = new ArrayList<HoldingsRecordAndDataMapping>();

        HoldingsRecordAndDataMapping holdingsRecordAndDataMapping1 = new HoldingsRecordAndDataMapping();
        HoldingsRecord holdingsRecord1 = new HoldingsRecord();
        holdingsRecord1.setHoldingsId("1");
        holdingsRecordAndDataMapping1.setHoldingsRecord(holdingsRecord1);
        holdingsRecordAndDataMappings.add(holdingsRecordAndDataMapping1);


        HoldingsRecordAndDataMapping holdingsRecordAndDataMapping2 = new HoldingsRecordAndDataMapping();
        HoldingsRecord holdingsRecord2 = new HoldingsRecord();
        holdingsRecord2.setHoldingsId("1");
        holdingsRecordAndDataMapping2.setHoldingsRecord(holdingsRecord2);
        holdingsRecordAndDataMappings.add(holdingsRecordAndDataMapping2);


        JSONObject itemData = new JSONObject();
        JSONObject matchpointsForItems = new JSONObject();
        matchpointsForItems.put("Item Barcode", "123456");
        itemData.put(OleNGConstants.MATCH_POINT, matchpointsForItems);

        JSONArray dataMappingsForEHoldings = new JSONArray();
        JSONArray itemBarcode = new JSONArray();
        itemBarcode.put("123456");
        dataMappingJSONForEHoldings1.put("Item Barcode", itemBarcode);
        JSONArray callNumber = new JSONArray();
        callNumber.put("44566678");
        dataMappingJSONForEHoldings1.put("Call Number", callNumber);

        JSONArray dataMappingsForEHoldings2 = new JSONArray();
        JSONArray itemBarcode2 = new JSONArray();
        itemBarcode2.put("10000");
        dataMappingJSONForEHoldings2.put("Item Barcode", itemBarcode2);
        JSONArray callNumber2 = new JSONArray();
        callNumber2.put("200000");
        dataMappingJSONForEHoldings2.put("Call Number", callNumber2);
        dataMappingsForEHoldings.put(dataMappingJSONForEHoldings2);
        dataMappingsForEHoldings.put(dataMappingJSONForEHoldings1);
        itemData.put(OleNGConstants.DATAMAPPING, dataMappingsForEHoldings);


        JSONObject bibData = new JSONObject();
        JSONObject holdingsData = new JSONObject();
        JSONObject eholdingsData = new JSONObject();

        bibData.put(OleNGConstants.EHOLDINGS, eholdingsData);
        bibData.put(OleNGConstants.HOLDINGS, holdingsData);
        bibData.put(OleNGConstants.ITEM, itemData);
        bibData.put(OleNGConstants.UPDATED_BY, "ole-quickstart");
        bibData.put(OleNGConstants.UPDATED_DATE, DocstoreConstants.DOCSTORE_DATE_FORMAT.format(new Date()));
        JSONObject addedOps = new JSONObject();
        addedOps.put(OleNGConstants.ITEM, OleNGConstants.OVERLAY);
        bibData.put(OleNGConstants.ADDED_OPS, addedOps);

        Exchange exchange = new Exchange();
        new OleDsNgOverlayProcessor().prepareItemsRecord(holdingsRecordAndDataMappings, bibData, exchange);
        List itemsForCreate = (List) exchange.get(OleNGConstants.ITEMS_FOR_CREATE);
        assertNotNull(itemsForCreate);
        assertTrue(itemsForCreate.size() == 4);

        List itemsForUpdate = (List) exchange.get(OleNGConstants.ITEMS_FOR_UPDATE);
        assertNotNull(itemsForUpdate);
        assertTrue(itemsForUpdate.size() == 0);

        Mockito.when(mockOleDsNGMemorizeService.getItemDAO()).thenReturn(mockItemDAO);
        CreateItemHandler createItemHandler = new CreateItemHandler();
        createItemHandler.setOleDsNGMemorizeService(mockOleDsNGMemorizeService);
        createItemHandler.setBusinessObjectService(mockBusinessObjectService);
        createItemHandler.process(bibData, exchange);
    }

    @Test
    public void testProcessCreateItem() throws Exception {

        List<HoldingsRecordAndDataMapping> holdingsRecordAndDataMappings = new ArrayList<HoldingsRecordAndDataMapping>();

        HoldingsRecordAndDataMapping holdingsRecordAndDataMapping1 = new HoldingsRecordAndDataMapping();
        HoldingsRecord holdingsRecord1 = new HoldingsRecord();
        holdingsRecord1.setHoldingsId("1");
        ArrayList<ItemRecord> itemRecords = new ArrayList<ItemRecord>();
        ItemRecord itemRecord = new ItemRecord();
        itemRecord.setBarCode("123456");
        itemRecord.setItemId("11");
        itemRecords.add(itemRecord);
        holdingsRecord1.setItemRecords(itemRecords);
        holdingsRecordAndDataMapping1.setHoldingsRecord(holdingsRecord1);
        holdingsRecordAndDataMappings.add(holdingsRecordAndDataMapping1);


        HoldingsRecordAndDataMapping holdingsRecordAndDataMapping2 = new HoldingsRecordAndDataMapping();
        HoldingsRecord holdingsRecord2 = new HoldingsRecord();
        holdingsRecord2.setHoldingsId("2");
        ArrayList<ItemRecord> itemRecords2 = new ArrayList<ItemRecord>();
        ItemRecord itemRecord2 = new ItemRecord();
        itemRecord2.setItemId("10");
        itemRecord2.setBarCode("2225");
        itemRecords2.add(itemRecord2);
        holdingsRecord2.setItemRecords(itemRecords2);
        holdingsRecordAndDataMapping2.setHoldingsRecord(holdingsRecord2);
        holdingsRecordAndDataMappings.add(holdingsRecordAndDataMapping2);


        JSONObject itemData = new JSONObject();
        JSONObject matchpointsForItems = new JSONObject();
        matchpointsForItems.put("Item Barcode", "123456");
        itemData.put(OleNGConstants.MATCH_POINT, matchpointsForItems);

        JSONArray dataMappingsForEHoldings = new JSONArray();
        JSONArray itemBarcode = new JSONArray();
        itemBarcode.put("123456");
        dataMappingJSONForEHoldings1.put("Item Barcode", itemBarcode);
        JSONArray callNumber = new JSONArray();
        callNumber.put("44566678");
        dataMappingJSONForEHoldings1.put("Call Number", callNumber);

        JSONArray dataMappingsForEHoldings2 = new JSONArray();
        JSONArray itemBarcode2 = new JSONArray();
        itemBarcode2.put("10000");
        dataMappingJSONForEHoldings2.put("Item Barcode", itemBarcode2);
        JSONArray callNumber2 = new JSONArray();
        callNumber2.put("200000");
        dataMappingJSONForEHoldings2.put("Call Number", callNumber2);
        dataMappingsForEHoldings.put(dataMappingJSONForEHoldings2);
        dataMappingsForEHoldings.put(dataMappingJSONForEHoldings1);
        itemData.put(OleNGConstants.DATAMAPPING, dataMappingsForEHoldings);


        JSONObject bibData = new JSONObject();
        JSONObject holdingsData = new JSONObject();
        JSONObject eholdingsData = new JSONObject();

        bibData.put(OleNGConstants.EHOLDINGS, eholdingsData);
        bibData.put(OleNGConstants.HOLDINGS, holdingsData);
        bibData.put(OleNGConstants.ITEM, itemData);
        bibData.put(OleNGConstants.UPDATED_BY, "ole-quickstart");
        bibData.put(OleNGConstants.UPDATED_DATE, DocstoreConstants.DOCSTORE_DATE_FORMAT.format(new Date()));
        JSONObject addedOps = new JSONObject();
        addedOps.put(OleNGConstants.ITEM, OleNGConstants.OVERLAY);
        bibData.put(OleNGConstants.ADDED_OPS, addedOps);

        Exchange exchange = new Exchange();
        new OleDsNgOverlayProcessor().prepareItemsRecord(holdingsRecordAndDataMappings, bibData, exchange);
        List itemsForCreate = (List) exchange.get(OleNGConstants.ITEMS_FOR_CREATE);
        assertNotNull(itemsForCreate);
        assertTrue(itemsForCreate.size() == 3);

        List itemsForUpdate = (List) exchange.get(OleNGConstants.ITEMS_FOR_UPDATE);
        assertNotNull(itemsForUpdate);
        assertTrue(itemsForUpdate.size() == 1);

        Mockito.when(mockOleDsNGMemorizeService.getItemDAO()).thenReturn(mockItemDAO);
        CreateItemHandler createItemHandler = new CreateItemHandler();
        createItemHandler.setOleDsNGMemorizeService(mockOleDsNGMemorizeService);
        createItemHandler.setBusinessObjectService(mockBusinessObjectService);
        createItemHandler.process(bibData, exchange);
    }


    @Test
    public void testProcessCreateItemWithDeleteAll() throws Exception {

        List<HoldingsRecordAndDataMapping> holdingsRecordAndDataMappings = new ArrayList<HoldingsRecordAndDataMapping>();

        HoldingsRecordAndDataMapping holdingsRecordAndDataMapping1 = new HoldingsRecordAndDataMapping();
        HoldingsRecord holdingsRecord1 = new HoldingsRecord();
        holdingsRecord1.setHoldingsId("1");
        ArrayList<ItemRecord> itemRecords = new ArrayList<ItemRecord>();
        ItemRecord itemRecord = new ItemRecord();
        itemRecord.setBarCode("123456");
        itemRecord.setItemId("11");
        itemRecords.add(itemRecord);
        holdingsRecord1.setItemRecords(itemRecords);
        holdingsRecordAndDataMapping1.setHoldingsRecord(holdingsRecord1);
        holdingsRecordAndDataMappings.add(holdingsRecordAndDataMapping1);


        HoldingsRecordAndDataMapping holdingsRecordAndDataMapping2 = new HoldingsRecordAndDataMapping();
        HoldingsRecord holdingsRecord2 = new HoldingsRecord();
        holdingsRecord2.setHoldingsId("2");
        ArrayList<ItemRecord> itemRecords2 = new ArrayList<ItemRecord>();
        ItemRecord itemRecord2 = new ItemRecord();
        itemRecord2.setItemId("10");
        itemRecord2.setBarCode("2225");
        itemRecords2.add(itemRecord2);
        holdingsRecord2.setItemRecords(itemRecords2);
        holdingsRecordAndDataMapping2.setHoldingsRecord(holdingsRecord2);
        holdingsRecordAndDataMappings.add(holdingsRecordAndDataMapping2);


        JSONObject itemData = new JSONObject();
        JSONObject matchpointsForItems = new JSONObject();
        matchpointsForItems.put("Item Barcode", "123456");
        itemData.put(OleNGConstants.MATCH_POINT, matchpointsForItems);

        JSONArray dataMappingsForEHoldings = new JSONArray();
        JSONArray itemBarcode = new JSONArray();
        itemBarcode.put("123456");
        dataMappingJSONForEHoldings1.put("Item Barcode", itemBarcode);
        JSONArray callNumber = new JSONArray();
        callNumber.put("44566678");
        dataMappingJSONForEHoldings1.put("Call Number", callNumber);

        JSONArray dataMappingsForEHoldings2 = new JSONArray();
        JSONArray itemBarcode2 = new JSONArray();
        itemBarcode2.put("10000");
        dataMappingJSONForEHoldings2.put("Item Barcode", itemBarcode2);
        JSONArray callNumber2 = new JSONArray();
        callNumber2.put("200000");
        dataMappingJSONForEHoldings2.put("Call Number", callNumber2);
        dataMappingsForEHoldings.put(dataMappingJSONForEHoldings2);
        dataMappingsForEHoldings.put(dataMappingJSONForEHoldings1);
        itemData.put(OleNGConstants.DATAMAPPING, dataMappingsForEHoldings);


        JSONObject bibData = new JSONObject();
        JSONObject holdingsData = new JSONObject();
        JSONObject eholdingsData = new JSONObject();

        bibData.put(OleNGConstants.EHOLDINGS, eholdingsData);
        bibData.put(OleNGConstants.HOLDINGS, holdingsData);
        bibData.put(OleNGConstants.ITEM, itemData);
        bibData.put(OleNGConstants.UPDATED_BY, "ole-quickstart");
        bibData.put(OleNGConstants.UPDATED_DATE, DocstoreConstants.DOCSTORE_DATE_FORMAT.format(new Date()));
        JSONObject addedOps = new JSONObject();
        addedOps.put(OleNGConstants.ITEM, OleNGConstants.DELETE_ALL_EXISTING_AND_ADD);
        bibData.put(OleNGConstants.ADDED_OPS, addedOps);

        Exchange exchange = new Exchange();
        new OleDsNgOverlayProcessor().prepareItemsRecord(holdingsRecordAndDataMappings, bibData, exchange);
        List itemsForCreate = (List) exchange.get(OleNGConstants.ITEMS_FOR_CREATE);
        assertNotNull(itemsForCreate);
        assertTrue(itemsForCreate.size() == 4);

        Mockito.when(mockOleDsNGMemorizeService.getItemDAO()).thenReturn(mockItemDAO);
        CreateItemHandler createItemHandler = new CreateItemHandler();
        createItemHandler.setOleDsNGMemorizeService(mockOleDsNGMemorizeService);
        createItemHandler.setBusinessObjectService(mockBusinessObjectService);
        createItemHandler.process(bibData, exchange);
    }
}