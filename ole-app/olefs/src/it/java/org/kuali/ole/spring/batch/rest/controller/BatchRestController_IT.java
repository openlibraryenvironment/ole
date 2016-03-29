package org.kuali.ole.spring.batch.rest.controller;

import org.apache.cxf.common.i18n.Exception;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.oleng.batch.process.model.BatchProcessJob;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by SheikS on 2/8/2016.
 */
public class BatchRestController_IT  extends OLETestCaseBase{
    @Test
    public void testLaunchJob() throws Exception {
        BatchRestController batchRestController = new BatchRestController();

        BatchProcessJob batchProcessJob = new BatchProcessJob();
        batchProcessJob.setJobName("Process Name");
        batchProcessJob.setJobType("Bib Import");
        batchProcessJob.setBatchProfileId(101);
        batchProcessJob.setBatchProfileName("Test Profile Name");

        String requestBody = null;
        try {
            GlobalVariables.setUserSession(new UserSession("ole-quickstart"));
            requestBody = new ObjectMapper().writeValueAsString(batchProcessJob);
            String responseBatchJob = batchRestController.createBatchJobDetailsEntry(requestBody);
            BatchProcessJob savedBatchProcessJob = new ObjectMapper().readValue(responseBatchJob, BatchProcessJob.class);
            assertNotNull(savedBatchProcessJob);
            assertTrue(savedBatchProcessJob.getJobId() != 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}