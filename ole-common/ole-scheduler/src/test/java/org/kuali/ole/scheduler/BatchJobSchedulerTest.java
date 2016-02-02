package org.kuali.ole.scheduler;

public class BatchJobSchedulerTest {

    public static void main(String[] args) {
        try {
            BatchJobScheduler batchJobScheduler = new BatchJobScheduler();
            batchJobScheduler.scheduleJob("job1", "trigger1", "1", "Bib Import", 
                    "http://localhost:8080/olefs/rest/batch/upload", ("0/5 * * * * ?"));
            batchJobScheduler.startScheduler();
            Thread.sleep(20000);
            batchJobScheduler.stopScheduler();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}