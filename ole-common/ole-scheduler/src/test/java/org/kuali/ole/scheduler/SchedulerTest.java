package org.kuali.ole.scheduler;

import org.junit.Test;

import javax.servlet.ServletException;

public class SchedulerTest {

    @Test
    public void threeProcesses() throws ServletException, InterruptedException {
        String [] restAnswer = {
                "[" +
                        "{'jobId':2,'batchProcessName':'exampleJobCron1',       'cronExpression':'0/3 * * * * ?'}," +
                        "{'jobId':3,'batchProcessName':'exampleJobCron1',       'cronExpression':'0/3 * * * * ?'}," +
                        "{'jobId':4,'batchProcessName':'exampleJobCron3',       'cronExpression':'0/3 * * * * ?'}" +
                "]"
        };
        OleBatchJobScheduler oleBatchJobScheduler = new OleBatchJobScheduler();
        oleBatchJobScheduler.setRestResult(restAnswer);
        oleBatchJobScheduler.init();
        Thread.sleep(7000);
        oleBatchJobScheduler.modifySchedule(3, null, null);
        Thread.sleep(7000);
        oleBatchJobScheduler.destroy();
    }
}
