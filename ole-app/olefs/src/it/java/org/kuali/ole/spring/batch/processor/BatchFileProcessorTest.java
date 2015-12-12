package org.kuali.ole.spring.batch.processor;

import org.apache.commons.io.FileUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.kuali.ole.utility.OleDsNgRestClient;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Created by pvsubrah on 12/7/15.
 */
public class BatchFileProcessorTest {

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
    public void testFormLocation() throws Exception {
        String locationLevel1 = "UC";
        String locationLevel2 = null;
        String locationLevel3 = "UCX";
        String locationLevel4 = null;
        String locationLevel5 = "InProc";

        BatchBibFileProcessor batchFileProcessor = new BatchBibFileProcessor();
        String fullPathLocation = batchFileProcessor.formLocation(locationLevel1, locationLevel2,
                locationLevel3, locationLevel4, locationLevel5);
        assertNotNull(fullPathLocation);
        System.out.println(fullPathLocation);
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