package org.kuali.ole.scheduler;

import java.util.ArrayList;

public class BatchJobSchedulerTest {

    public static void main(String[] args) {
	try {
	    BatchJobScheduler batchJobScheduler = new BatchJobScheduler();

	    // Schedule a job
	    batchJobScheduler.scheduleJob("job1", "trigger1", "1", "Bib Import",
		    "http://localhost:8080/olefs/rest/batch/upload", ("0/5 * * * * ?"));

	    batchJobScheduler.startScheduler();
	    Thread.sleep(10000);

	    // Now change the trigger to new cron expression
	    batchJobScheduler.rescheduleJob("job1", "trigger1", ("0/15 * * * * ?"));

	    Thread.sleep(30000);

	    // Print all jobs
	    System.out.println(batchJobScheduler.getAllJobs());
	    
	    // Print all triggers for a job -- do we want to allow multiple triggers for one job?
	    System.out.println(batchJobScheduler.getTriggersForJob("job1"));
	    
	    // delete a job
	    batchJobScheduler.deleteJob("job1");
	    
	    batchJobScheduler.stopScheduler();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}