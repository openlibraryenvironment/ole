package org.kuali.ole.dsng.rest.processor;

import org.aspectj.util.FileUtil;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.DocstoreTestCaseBase;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.dsng.rest.handler.Handler;
import org.kuali.ole.utility.MarcRecordUtil;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

import static org.junit.Assert.assertNotNull;

/**
 * Created by pvsubrah on 1/21/16.
 */
public class OleDsNgOverlayProcessorTest extends DocstoreTestCaseBase {

    @Autowired
    OleDsNgOverlayProcessor oleDsNgOverlayProcessor;

    private JSONObject dataMappingJSONForHoldings = new JSONObject();

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
        matchpointsForHoldings.put(Handler.LOCATION_LEVEL_1, "UC");
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



    public String getMarcXMLContent() throws URISyntaxException, IOException {
        URL resource = getClass().getResource("sample.mrc");
        File marcFile = new File(resource.toURI());

        String rawMarcToXML = new MarcRecordUtil().getMarcXMLConverter().convert(FileUtil.readAsString(marcFile));
        return rawMarcToXML;
    }
}