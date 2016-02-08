package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.OLERestBaseTestCase;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.oleng.batch.profile.model.*;
import org.kuali.ole.oleng.describe.processor.bibimport.MatchPointProcessor;
import org.kuali.ole.utility.OleDsNgRestClient;
import org.marc4j.marc.Record;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by pvsubrah on 12/7/15.
 */
public class BatchFileProcessor_IT extends OLERestBaseTestCase{

    @Test
    public void testProcessBatchForBib() throws Exception {
        processBatch("InvYBP_Test_1207_2rec.mrc","BibForInvoiceYBP");
    }

    @Test
    public void testProcessBatchForBibForYBP() throws Exception {
        processBatch("InvYBP_1124.mrc", "BibForInvoiceYBP");
    }

    private void processBatch(String fielName, String profileName) throws URISyntaxException, IOException {
        OleDsNgRestClient oleDsNgRestClient = new MockOleDsNgRestClient();
        BatchFileProcessor batchFileProcessor = new MockBatchBibFileProcessor();
        URL resource = getClass().getResource(fielName);
        File file = new File(resource.toURI());
        batchFileProcessor.setOleDsNgRestClient(oleDsNgRestClient);
        String rawMarc = FileUtils.readFileToString(file);
        batchFileProcessor.processBatch(rawMarc,profileName);
    }

    @Test
    public void testOleDsNgRestClient() throws Exception {
        OleDsNgRestClient oleDsNgRestClient = new MockOleDsNgRestClient();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "10000034");
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("id", "10000035");

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);
        jsonArray.put(jsonObject1);

        String requestBody = jsonArray.toString();
        String responseData = oleDsNgRestClient.postData("processBibOverlay", requestBody, "json");
        assertNotNull(responseData);
        System.out.println(responseData);
    }

    @Test
    public void testGetDataMappingMap(){
        BatchBibFileProcessor batchBibFileProcessor = new BatchBibFileProcessor();
        ArrayList<BatchProfileDataMapping> batchProfileDataMappingList = new ArrayList<>();
        BatchProfileDataMapping batchProfileDataMapping1 = new BatchProfileDataMapping();

        batchProfileDataMapping1.setDataType("bibliographic");
        batchProfileDataMapping1.setDestination("holdings");
        batchProfileDataMapping1.setField("Call Number");
        batchProfileDataMapping1.setDataField("050");
        batchProfileDataMapping1.setSubField("a");

        BatchProfileDataMapping batchProfileDataMapping2 = new BatchProfileDataMapping();
        batchProfileDataMapping2.setDataType("bibliographic");
        batchProfileDataMapping2.setDestination("holdings");
        batchProfileDataMapping2.setField("Call Number");
        batchProfileDataMapping2.setDataField("050");
        batchProfileDataMapping2.setSubField("b");

        BatchProfileDataMapping batchProfileDataMapping3 = new BatchProfileDataMapping();
        batchProfileDataMapping3.setDataType("bibliographic");
        batchProfileDataMapping3.setDestination("item");
        batchProfileDataMapping3.setField("Copy Number");
        batchProfileDataMapping3.setDataField("070");
        batchProfileDataMapping3.setSubField("a");

        BatchProfileDataMapping batchProfileDataMapping4 = new BatchProfileDataMapping();
        batchProfileDataMapping4.setDataType("bibliographic");
        batchProfileDataMapping4.setDestination("holdings");
        batchProfileDataMapping4.setField("Location Level1");
        batchProfileDataMapping4.setDataField("065");
        batchProfileDataMapping4.setSubField("b");


        BatchProfileDataMapping batchProfileDataMapping5 = new BatchProfileDataMapping();
        batchProfileDataMapping5.setDataType("bibliographic");
        batchProfileDataMapping5.setDestination("holdings");
        batchProfileDataMapping5.setField("Copy Number");
        batchProfileDataMapping5.setDataField("080");
        batchProfileDataMapping5.setSubField("c");


        BatchProfileDataMapping batchProfileDataMapping6 = new BatchProfileDataMapping();
        batchProfileDataMapping6.setDataType("bibliographic");
        batchProfileDataMapping6.setDestination("holdings");
        batchProfileDataMapping6.setField("Copy Number");
        batchProfileDataMapping6.setDataField("080");
        batchProfileDataMapping6.setSubField("f");

        batchProfileDataMappingList.add(batchProfileDataMapping1);
        batchProfileDataMappingList.add(batchProfileDataMapping2);
        batchProfileDataMappingList.add(batchProfileDataMapping3);
        batchProfileDataMappingList.add(batchProfileDataMapping4);
        batchProfileDataMappingList.add(batchProfileDataMapping5);
        batchProfileDataMappingList.add(batchProfileDataMapping6);
        Map<String, String> dataMappingMap = batchBibFileProcessor.getDataMappingMap(batchProfileDataMappingList);
        assertNotNull(dataMappingMap);
        assertTrue(dataMappingMap.size() > 0);
        System.out.println(dataMappingMap);
    }



    @Test
    public void testProcessBibHoldingsAndItem() throws IOException, JSONException {

        BatchProcessProfile batchProcessProfile = new BatchProcessProfile();

        String rawMarcContent = org.kuali.ole.loaders.common.FileUtils.readFileContent("org/kuali/ole/spring/batch/processor/InvYBP_Test_1207_2rec.mrc");
        List<Record> records = new MarcXMLConverter().convertRawMarchToMarc(rawMarcContent);
        assertTrue(CollectionUtils.isNotEmpty(records));

        ArrayList<BatchProfileMatchPoint> batchProfileMatchPoints = new ArrayList<>();

        BatchProfileMatchPoint batchProfileMatchPoint1 = new BatchProfileMatchPoint();
        batchProfileMatchPoint1.setDataType("bibliographic");
        batchProfileMatchPoint1.setDataField("980");
        batchProfileMatchPoint1.setSubField("a");

        BatchProfileMatchPoint batchProfileMatchPoint2 = new BatchProfileMatchPoint();
        batchProfileMatchPoint2.setDataType("holdings");
        batchProfileMatchPoint2.setMatchPointType("Location Level1");
        batchProfileMatchPoint2.setMatchPointValue("UC");

        BatchProfileMatchPoint batchProfileMatchPoint3 = new BatchProfileMatchPoint();
        batchProfileMatchPoint3.setDataType("Item");
        batchProfileMatchPoint3.setMatchPointType("Copy Number");
        batchProfileMatchPoint3.setConstant("c.11");

        batchProfileMatchPoints.add(batchProfileMatchPoint1);
        batchProfileMatchPoints.add(batchProfileMatchPoint2);
        batchProfileMatchPoints.add(batchProfileMatchPoint3);


        //Transformers
        ArrayList<BatchProfileDataTransformer> batchProfileDataTransformers = new ArrayList<>();

        BatchProfileDataTransformer batchProfileDataTransformer1 = new BatchProfileDataTransformer();
        batchProfileDataTransformer1.setOperation("delete");
        batchProfileDataTransformer1.setConstant("ocm,ocn");
        batchProfileDataTransformer1.setSourceField("001");
        batchProfileDataTransformer1.setStep(1);
        batchProfileDataTransformers.add(batchProfileDataTransformer1);

        BatchProfileDataTransformer batchProfileDataTransformer2 = new BatchProfileDataTransformer();
        batchProfileDataTransformer2.setOperation("Prepend with Prefix");
        batchProfileDataTransformer2.setDestinationField("001");
        batchProfileDataTransformer2.setSourceField("003");
        batchProfileDataTransformer2.setStep(2);
        batchProfileDataTransformers.add(batchProfileDataTransformer2);

        BatchProfileDataTransformer batchProfileDataTransformer3 = new BatchProfileDataTransformer();
        batchProfileDataTransformer3.setOperation("add");
        batchProfileDataTransformer3.setDestinationField("035 $a");
        batchProfileDataTransformer3.setSourceField("001");
        batchProfileDataTransformer3.setStep(3);
        batchProfileDataTransformers.add(batchProfileDataTransformer3);

        BatchProfileDataTransformer batchProfileDataTransformer4 = new BatchProfileDataTransformer();
        batchProfileDataTransformer4.setOperation("delete");
        batchProfileDataTransformer4.setSourceField("001");
        batchProfileDataTransformer4.setStep(4);
        batchProfileDataTransformers.add(batchProfileDataTransformer4);

        BatchProfileDataTransformer batchProfileDataTransformer5 = new BatchProfileDataTransformer();
        batchProfileDataTransformer5.setOperation("delete");
        batchProfileDataTransformer5.setSourceField("003");
        batchProfileDataTransformer5.setStep(5);
        batchProfileDataTransformers.add(batchProfileDataTransformer5);



        // Overlay opts
        ArrayList<BatchProfileAddOrOverlay> batchProfileAddOrOverlays = new ArrayList<>();

        BatchProfileAddOrOverlay batchProfileAddOrOverlay = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay.setDataType("bibliographic");
        batchProfileAddOrOverlay.setMatchOption("If Match Found");
        batchProfileAddOrOverlay.setOperation("overlay");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay);

        BatchProfileAddOrOverlay batchProfileAddOrOverlay2 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay2.setDataType("holdings");
        batchProfileAddOrOverlay2.setMatchOption("If Match Found");
        batchProfileAddOrOverlay2.setOperation("overlay");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay2);

        BatchProfileAddOrOverlay batchProfileAddOrOverlay3 = new BatchProfileAddOrOverlay();
        batchProfileAddOrOverlay3.setDataType("items");
        batchProfileAddOrOverlay3.setMatchOption("If Match Found");
        batchProfileAddOrOverlay3.setOperation("overlay");
        batchProfileAddOrOverlays.add(batchProfileAddOrOverlay3);


        //Data Mapping
        ArrayList<BatchProfileDataMapping> batchProfileDataMappingList = new ArrayList<>();
        BatchProfileDataMapping batchProfileDataMapping1 = new BatchProfileDataMapping();

        batchProfileDataMapping1.setDataType("bibliographic");
        batchProfileDataMapping1.setDestination("holdings");
        batchProfileDataMapping1.setField("Call Number");
        batchProfileDataMapping1.setDataField("050");
        batchProfileDataMapping1.setSubField("a");
        batchProfileDataMapping1.setPriority(1);
        batchProfileDataMappingList.add(batchProfileDataMapping1);

        BatchProfileDataMapping batchProfileDataMapping2 = new BatchProfileDataMapping();
        batchProfileDataMapping2.setDataType("bibliographic");
        batchProfileDataMapping2.setDestination("holdings");
        batchProfileDataMapping2.setField("Call Number");
        batchProfileDataMapping2.setDataField("050");
        batchProfileDataMapping2.setSubField("b");
        batchProfileDataMapping2.setPriority(2);
        batchProfileDataMappingList.add(batchProfileDataMapping2);

        batchProcessProfile.setBatchProfileAddOrOverlayList(batchProfileAddOrOverlays);
        batchProcessProfile.setBatchProfileDataTransformerList(batchProfileDataTransformers);
        batchProcessProfile.setBatchProfileMatchPointList(batchProfileMatchPoints);
        batchProcessProfile.setBatchProfileDataMappingList(batchProfileDataMappingList);
        System.out.println(batchProcessProfile);

        MockBatchBibFileProcessor mockBatchBibFileProcessor = new MockBatchBibFileProcessor();

        OleDsNgRestClient oleDsNgRestClient = new MockOleDsNgRestClient();
        mockBatchBibFileProcessor.setOleDsNgRestClient(oleDsNgRestClient);

        MockSolrRequestReponseHandler mockSolrRequestReponseHandler = new MockSolrRequestReponseHandler();
        mockBatchBibFileProcessor.setSolrRequestReponseHandler(mockSolrRequestReponseHandler);
        String response = mockBatchBibFileProcessor.processRecords(records, batchProcessProfile);
        assertTrue(StringUtils.isNotBlank(response));
        System.out.println(response);
    }

    class MockOleDsNgRestClient extends OleDsNgRestClient {
        @Override
        public String getDsNgBaseUrl() {;
            return "http://localhost:8080/oledocstore";
        }
    }

    class MockBatchBibFileProcessor extends BatchBibFileProcessor {
        @Override
        public String getUpdatedUserName() {
            return "dev2";
        }

        @Override
        public MatchPointProcessor getMatchPointProcessor() {
            return new MatchPointProcessor();
        }
    }

    class MockSolrRequestReponseHandler extends SolrRequestReponseHandler {
        @Override
        public String getSolrUrl() {
            return "http://localhost:8080/oledocstore/bib";
        }
    }

    @Test
    public void testProcessRecord() throws JSONException, URISyntaxException, IOException {
         JSONObject jsonObject = new JSONObject();
        MockBatchBibFileProcessor mockBatchBibFileProcessor = new MockBatchBibFileProcessor();
        OleDsNgRestClient oleDsNgRestClient = new MockOleDsNgRestClient();
        jsonObject.put("profileName","Test Bib Import");
        jsonObject.put("batchType","Bib Import");
        URL resource = getClass().getResource("InvYBP_Test_1207_2rec.mrc");
        File file = new File(resource.toURI());
        mockBatchBibFileProcessor.setOleDsNgRestClient(oleDsNgRestClient);
        String rawMarc = FileUtils.readFileToString(file);
        jsonObject.put("marcContent",rawMarc);
        System.out.println(jsonObject.toString());
        String URL = "http://localhost:8080/olefs/rest/batch/submit/api";
        String jsonString = jsonObject.toString();
        String responseContent = sendPostRequest(URL, jsonString,"json");
        assertTrue(StringUtils.isNotBlank(responseContent));
        System.out.println(responseContent);
    }

}