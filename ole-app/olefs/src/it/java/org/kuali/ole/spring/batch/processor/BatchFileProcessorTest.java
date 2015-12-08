package org.kuali.ole.spring.batch.processor;

import org.junit.Test;

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
}