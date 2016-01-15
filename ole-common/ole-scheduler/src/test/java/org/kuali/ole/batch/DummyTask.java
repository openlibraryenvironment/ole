package org.kuali.ole.batch;

import org.springframework.batch.core.JobExecution;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DummyTask implements Runnable {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;
  
    public void run() {
        try {
            JobExecution execution = jobLauncher.run(job, new JobParameters());
            System.out.println(execution.getEndTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}