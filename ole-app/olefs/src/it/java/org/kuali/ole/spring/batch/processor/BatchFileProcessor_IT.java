package org.kuali.ole.spring.batch.processor;

import org.apache.commons.io.FileUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileDataMapping;
import org.kuali.ole.utility.OleDsNgRestClient;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by pvsubrah on 12/7/15.
 */
public class BatchFileProcessor_IT {

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
    public void testProcessBatchForHoldings() throws Exception {
        OleDsNgRestClient oleDsNgRestClient = new MockOleDsNgRestClient();
        BatchFileProcessor batchFileProcessor = new MockBatchHoldingFileProcessor();
        URL resource = getClass().getResource("InvYBP_Test_1207_2rec.mrc");
        File file = new File(resource.toURI());
        batchFileProcessor.setOleDsNgRestClient(oleDsNgRestClient);
        String rawMarc = FileUtils.readFileToString(file);
        batchFileProcessor.processBatch(rawMarc,"BibForInvoiceCasalini");
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
    }

    class MockBatchHoldingFileProcessor extends BatchHoldingFileProcessor {
        @Override
        public String getUpdatedUserName() {
            return "dev2";
        }
    }

}