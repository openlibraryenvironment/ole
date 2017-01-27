package org.kuali.ole.oleng.scheduler;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.oleng.batch.process.model.BatchProcessJob;
import org.kuali.ole.oleng.dao.DescribeDAO;
import org.kuali.ole.utility.OleHttpRestGet;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by SheikS on 4/19/2016.
 */
public class OleNGBatchJobSchedulerTest {

    @Mock
    private DescribeDAO describeDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSchedule() {

        OleNGBatchJobScheduler oleNGBatchJobScheduler = new OleNGBatchJobScheduler();
        List<BatchProcessJob> batchProcessJobs = new ArrayList<>();
        int index = 0;
        BatchProcessJob batchProcessJob = new BatchProcessJob();
        batchProcessJob.setJobId(index);
        batchProcessJob.setJobName("Job "+index);
        batchProcessJob.setCronExpression(index + "/5 * * * * ?");
        batchProcessJob.setJobType(OleNGConstants.BIB_IMPORT);
        batchProcessJobs.add(batchProcessJob);

        index = 1;
        BatchProcessJob batchProcessJob1 = new BatchProcessJob();
        batchProcessJob1.setJobId(index);
        batchProcessJob1.setJobName("Job "+index);
        batchProcessJob1.setCronExpression(index + "/5 * * * * ?");
        batchProcessJob1.setJobType(OleNGConstants.ORDER_RECORD_IMPORT);
        batchProcessJobs.add(batchProcessJob1);

        index = 2;
        BatchProcessJob batchProcessJob2 = new BatchProcessJob();
        batchProcessJob2.setJobId(index);
        batchProcessJob2.setJobName("Job "+index);
        batchProcessJob2.setCronExpression(index + "/5 * * * * ?");
        batchProcessJob2.setJobType(OleNGConstants.INVOICE_IMPORT);
        batchProcessJobs.add(batchProcessJob2);
        Mockito.when(describeDAO.fetchAllBatchProcessJobs()).thenReturn(batchProcessJobs);
        oleNGBatchJobScheduler.setDescribeDAO(describeDAO);

        oleNGBatchJobScheduler.initializeAllJobs();

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Done");

    }

    @Test
    public void testReschedule() {
        OleNGBatchJobScheduler oleNGBatchJobScheduler = new OleNGBatchJobScheduler();
        oleNGBatchJobScheduler.scheduleOrRescheduleJob(1,2,"", "0/5 * * * * ?","ole-quickstart");
        oleNGBatchJobScheduler.scheduleOrRescheduleJob(1,2,"", "1/5 * * * * ?","ole-quickstart");
        oleNGBatchJobScheduler.scheduleOrRescheduleJob(1,2,"", "3/5 * * * * ?","ole-quickstart");
    }

}