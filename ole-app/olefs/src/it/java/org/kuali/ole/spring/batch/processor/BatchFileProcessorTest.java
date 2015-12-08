package org.kuali.ole.spring.batch.processor;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.kuali.ole.utility.OleDsNgRestClient;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Created by pvsubrah on 12/7/15.
 */
public class BatchFileProcessorTest {

    @Test
    public void testProcessBatch() throws Exception {
        OleDsNgRestClient oleDsNgRestClient = new MockOleDsNgRestClient();
        BatchFileProcessor batchFileProcessor = new BatchFileProcessor();
        URL resource = getClass().getResource("InvYBP_Test_1207_2rec.mrc");
        File file = new File(resource.toURI());
        batchFileProcessor.setOleDsNgRestClient(oleDsNgRestClient);
        batchFileProcessor.processBatch(file);
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

    class MockOleDsNgRestClient extends OleDsNgRestClient {
        @Override
        public String getDsNgBaseUrl() {;
            return "http://localhost:8080/oledocstore";
        }
    }

}