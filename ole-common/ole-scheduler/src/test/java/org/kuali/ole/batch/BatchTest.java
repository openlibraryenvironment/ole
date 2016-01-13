package org.kuali.ole.batch;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BatchTest {
    
//    if you want to test whether scheduling works, run main method, stop it manually
//    public static void main(String[] args) {
//        ApplicationContext ctx = new AnnotationConfigApplicationContext(BatchConfiguration.class);
//    }
    
    @Test
    public void testBatch() throws Exception {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(BatchConfiguration.class);
        Job job = ctx.getBean("dummyJob", Job.class);
        JobLauncher jobLauncher = ctx.getBean("jobLauncher", JobLauncher.class);        
        JobExecution execution = jobLauncher.run(job, new JobParameters());
        System.out.println(execution.getEndTime());
    }
}
