package org.kuali.ole.dsng.rest.processor;

import org.apache.commons.collections.CollectionUtils;
import org.aspectj.util.FileUtil;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.DocstoreTestCaseBase;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.document.EHoldings;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsUriRecord;
import org.kuali.ole.dsng.model.HoldingsRecordAndDataMapping;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;
import org.kuali.ole.utility.MarcRecordUtil;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by angelind on 1/29/16.
 */
public class OleDsNgOverlayProcessor_IT extends DocstoreTestCaseBase {

    @Autowired
    OleDsNgOverlayProcessor oleDsNgOverlayProcessor;

    private JSONObject dataMappingJSONForHoldings = new JSONObject();

    private JSONObject dataMappingJSONForEHoldings = new JSONObject();

    private JSONObject dataMappingJSONForEHoldings1 = new JSONObject();
    private JSONObject dataMappingJSONForEHoldings2 = new JSONObject();

    private JSONObject dataMappingJSONForItems = new JSONObject();

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createBibHoldingsItems() throws Exception {
        JSONArray request = new JSONArray();
        JSONObject bibData = new JSONObject();


        JSONObject holdingsDta = new JSONObject();
        JSONArray dataMappingsForHoldings = new JSONArray();
        JSONArray callNumbers = new JSONArray();
        callNumbers.put("1231");
        dataMappingJSONForHoldings.put("Call Number", callNumbers);
        dataMappingsForHoldings.put(dataMappingJSONForHoldings);
        holdingsDta.put(OleNGConstants.DATAMAPPING, dataMappingsForHoldings);

        JSONObject itemData = new JSONObject();
        JSONArray dataMappingsForItems = new JSONArray();
        JSONArray barcodes = new JSONArray();
        barcodes.put("12311");
        dataMappingJSONForItems.put("Item Barcode", barcodes);
        dataMappingsForItems.put(dataMappingJSONForItems);
        itemData.put(OleNGConstants.DATAMAPPING, dataMappingsForItems);

        bibData.put(OleNGConstants.HOLDINGS, holdingsDta);
        bibData.put(OleNGConstants.ITEM, itemData);
        bibData.put(OleNGConstants.UPDATED_BY, "ole-quickstart");
        bibData.put(OleNGConstants.UPDATED_DATE, DocstoreConstants.DOCSTORE_DATE_FORMAT.format(new Date()));
        bibData.put(OleNGConstants.MODIFIED_CONTENT, getMarcXMLContent());

        JSONArray ops = new JSONArray();
        ops.put("111");
        ops.put("121");
        ops.put("131");

        bibData.put(OleNGConstants.OPS, ops);
        bibData.put(OleNGConstants.TAG_001, "1231421");

        request.put(bibData);

        String response = oleDsNgOverlayProcessor.processBibAndHoldingsAndItems(request.toString());
        assertNotNull(response);
    }


    @Test
    public void overlayBibHoldingsAndItems() throws Exception {
        JSONArray request = new JSONArray();
        JSONObject bibData = new JSONObject();


        JSONObject holdingsDta = new JSONObject();
        JSONObject matchpointsForHoldings = new JSONObject();
        matchpointsForHoldings.put(OleNGConstants.BatchProcess.LOCATION_LEVEL_1, "UC");
        holdingsDta.put(OleNGConstants.MATCH_POINT, matchpointsForHoldings);
        JSONArray dataMappingsForHoldings = new JSONArray();
        JSONArray callNumbers = new JSONArray();
        callNumbers.put("1231");
        dataMappingJSONForHoldings.put("Call Number", callNumbers);
        dataMappingsForHoldings.put(dataMappingJSONForHoldings);
        holdingsDta.put(OleNGConstants.DATAMAPPING, dataMappingsForHoldings);

        JSONObject itemData = new JSONObject();
        JSONObject matchpointsForItems = new JSONObject();
        matchpointsForItems.put("Item Barcode", "28423654");
        itemData.put(OleNGConstants.MATCH_POINT, matchpointsForItems);
        JSONArray dataMappingsForItems = new JSONArray();
        JSONArray callNumber = new JSONArray();
        callNumber.put("12311 2015");
        dataMappingJSONForItems.put("Call Number", callNumber);
        dataMappingsForItems.put(dataMappingJSONForItems);
        itemData.put(OleNGConstants.DATAMAPPING, dataMappingsForItems);

        bibData.put(OleNGConstants.ID, "2");
        bibData.put(OleNGConstants.HOLDINGS, holdingsDta);
        bibData.put(OleNGConstants.ITEM, itemData);
        bibData.put(OleNGConstants.UPDATED_BY, "ole-quickstart");
        bibData.put(OleNGConstants.UPDATED_DATE, DocstoreConstants.DOCSTORE_DATE_FORMAT.format(new Date()));
        bibData.put(OleNGConstants.MODIFIED_CONTENT, getMarcXMLContent());

        JSONArray ops = new JSONArray();
        ops.put("112");
        ops.put("122");
        ops.put("132");

        bibData.put(OleNGConstants.OPS, ops);
        bibData.put(OleNGConstants.TAG_001, "1231421");

        request.put(bibData);

    }

    @Test
    public void createBibEHoldings() throws Exception {
        JSONArray request = new JSONArray();
        JSONObject bibData = new JSONObject();


        JSONObject eholdingsDta = new JSONObject();
        JSONArray dataMappingsForEHoldings = new JSONArray();
        JSONArray callNumbers = new JSONArray();
        callNumbers.put("1231");
        dataMappingJSONForEHoldings.put("Call Number", callNumbers);
        dataMappingsForEHoldings.put(dataMappingJSONForEHoldings);
        eholdingsDta.put(OleNGConstants.DATAMAPPING, dataMappingsForEHoldings);

        JSONObject holdingsData = new JSONObject();
        JSONObject itemData = new JSONObject();


        bibData.put(OleNGConstants.HOLDINGS, holdingsData);
        bibData.put(OleNGConstants.ITEM, itemData);
        bibData.put(OleNGConstants.EHOLDINGS, eholdingsDta);
        bibData.put(OleNGConstants.UPDATED_BY, "ole-quickstart");
        bibData.put(OleNGConstants.UPDATED_DATE, DocstoreConstants.DOCSTORE_DATE_FORMAT.format(new Date()));
        bibData.put(OleNGConstants.MODIFIED_CONTENT, getMarcXMLContent());

        JSONArray ops = new JSONArray();
        ops.put("111");
        ops.put("241");

        bibData.put(OleNGConstants.OPS, ops);
        bibData.put(OleNGConstants.TAG_001, "1231421");

        request.put(bibData);

        String response = oleDsNgOverlayProcessor.processBibAndHoldingsAndItems(request.toString());
        assertNotNull(response);
    }

    @Test
    public void overlayBibEHoldings() throws Exception {
        JSONArray request = new JSONArray();
        JSONObject bibData = new JSONObject();


        JSONObject eholdingsDta = new JSONObject();
        JSONObject matchpointsForEHoldings = new JSONObject();
        matchpointsForEHoldings.put(OleNGConstants.BatchProcess.LOCATION_LEVEL_1, "UC");
        eholdingsDta.put(OleNGConstants.MATCH_POINT, matchpointsForEHoldings);
        JSONArray dataMappingsForEHoldings = new JSONArray();
        JSONArray callNumbers = new JSONArray();
        callNumbers.put("1231");
        dataMappingJSONForEHoldings.put("Call Number", callNumbers);
        dataMappingsForEHoldings.put(dataMappingJSONForEHoldings);
        eholdingsDta.put(OleNGConstants.DATAMAPPING, dataMappingsForEHoldings);


        JSONObject holdingsData = new JSONObject();
        JSONObject itemData = new JSONObject();


        bibData.put(OleNGConstants.ID, "171771");
        bibData.put(OleNGConstants.HOLDINGS, holdingsData);
        bibData.put(OleNGConstants.EHOLDINGS, eholdingsDta);
        bibData.put(OleNGConstants.ITEM, itemData);
        bibData.put(OleNGConstants.UPDATED_BY, "ole-quickstart");
        bibData.put(OleNGConstants.UPDATED_DATE, DocstoreConstants.DOCSTORE_DATE_FORMAT.format(new Date()));
        bibData.put(OleNGConstants.MODIFIED_CONTENT, getMarcXMLContent());

        JSONArray ops = new JSONArray();
        ops.put("112");
        ops.put("142");

        bibData.put(OleNGConstants.OPS, ops);
        bibData.put(OleNGConstants.TAG_001, "1231421");

        request.put(bibData);

        String response = oleDsNgOverlayProcessor.processBibAndHoldingsAndItems(request.toString());
        assertNotNull(response);

    }

    @Test
    public void testDetermineHoldingsByMatchPoints() throws JSONException, IOException, URISyntaxException {

        BibRecord bibRecord = new BibRecord();
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
        linkText.put("google site");
        dataMappingJSONForEHoldings.put("Link Text", linkText);
        dataMappingsForEHoldings.put(dataMappingJSONForEHoldings);
        eholdingsData.put(OleNGConstants.DATAMAPPING, dataMappingsForEHoldings);


        Exchange exchange = new Exchange();
        List<HoldingsRecordAndDataMapping> holdingsRecordAndDataMappings = oleDsNgOverlayProcessor.determineHoldingsAndDataMappingsByMatchPoints(bibRecord, exchange, eholdingsData, EHoldings.ELECTRONIC);
        assertTrue(CollectionUtils.isNotEmpty(holdingsRecordAndDataMappings));
        assertTrue(holdingsRecordAndDataMappings.size() == 2);
    }

    @Test
    public void testPrepareEHoldingsRecord() throws JSONException, IOException, URISyntaxException {

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

        Exchange exchange = new Exchange();
        oleDsNgOverlayProcessor.prepareEHoldingsRecord(bibRecord, bibData, exchange);
        List eholdingsForCreate = (List) exchange.get(OleNGConstants.EHOLDINGS_FOR_CREATE);
        assertNotNull(eholdingsForCreate);
        assertTrue(eholdingsForCreate.size() == 1);

        List eholdingsForUpdate = (List) exchange.get(OleNGConstants.EHOLDINGS_FOR_UPDATE);
        assertNotNull(eholdingsForUpdate);
        assertTrue(eholdingsForUpdate.size() == 1);
    }



    public String getMarcXMLContent() throws URISyntaxException, IOException {
        URL resource = getClass().getResource("sample.mrc");
        File marcFile = new File(resource.toURI());

        String rawMarcToXML = new MarcRecordUtil().getMarcXMLConverter().convert(FileUtil.readAsString(marcFile));
        return rawMarcToXML;
    }

}