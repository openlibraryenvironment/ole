package org.kuali.ole.spring.batch.processor;

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
        BatchFileProcessor batchFileProcessor = new BatchFileProcessor();
        URL resource = getClass().getResource("InvYBP_Test_1207_2rec.mrc");
        File file = new File(resource.toURI());
        batchFileProcessor.processBatch(file);
    }

    @Test
    public void testOleDsNgRestClient() throws Exception {
        OleDsNgRestClient oleDsNgRestClient = new MockOleDsNgRestClient();
        String requestContent = "[{\"LocalId_display\":\"10000034\"}]";
        String responseData = oleDsNgRestClient.postData("processBibOverlay", requestContent, "json");
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