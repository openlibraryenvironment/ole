package org.kuali.ole.batchannotation;

import org.springframework.batch.core.JobExecution;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DummyScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;
  
    //@Scheduled(cron="*/5 * * * * *")
    // Every day at 6 o'clock: @Scheduled(cron="0 0 6 * * *")
    public void run() {
        try {
            JobExecution execution = jobLauncher.run(job, new JobParameters());
            System.out.println(execution.getEndTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}