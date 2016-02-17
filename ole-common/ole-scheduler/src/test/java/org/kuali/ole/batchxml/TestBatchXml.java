package org.kuali.ole.batchxml;

import org.kuali.ole.scheduler.BatchJobScheduler;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestBatchXml {

    String[] springConfig = {"spring/batch/jobs/*.xml"};
    // kann springconfig auch aus Datenbank gelesen werden?
    ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
    JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");

    public static void main(String[] args) {
        TestBatchXml testBatchXml = new TestBatchXml();
        testBatchXml.runAllJobsOnce();
        testBatchXml.scheduleOneJob();
    }

    public void runAllJobsOnce() {
        System.out.println("--- Running all batch Jobs once ---");
        String[] jobNames = context.getBeanNamesForType(Job.class);
        for (String jobName : jobNames) {
            try {
                System.out.println("Starting job " + jobName);
                Job job = (Job) context.getBean(jobName);
                JobExecution execution = jobLauncher.run(job, new JobParameters());
                System.out.println("Exit Status: " + execution.getStatus());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("--- Done ---");
    }

    public void scheduleOneJob() {
        System.out.println("--- Scheduling one job and letting scheduler run for some time ---");
        String[] jobNames = context.getBeanNamesForType(Job.class);
        Job jobToBeScheduled = (Job) context.getBean(jobNames[0]);
        try {
            BatchJobScheduler batchJobScheduler = new BatchJobScheduler();
            batchJobScheduler.scheduleSpringBatchJob(jobToBeScheduled, jobLauncher, "0/10 * * * * ?", "myTestJob", "myTestTrigger");
            batchJobScheduler.startScheduler();
            Thread.sleep(60000);
            batchJobScheduler.stopScheduler();
            System.out.println("--- Scheduler stopped ---");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}