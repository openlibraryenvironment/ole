package org.kuali.ole.batch;

import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BatchTest {

    @Test
    public void runDummyBatch() throws Exception {
        
        ApplicationContext ctx = new AnnotationConfigApplicationContext(BatchConfiguration.class);
        Job job = ctx.getBean("dummyJob", Job.class);
        JobLauncher jobLauncher = ctx.getBean("jobLauncher", JobLauncher.class);
        JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
        JobExecution execution = jobLauncher.run(job, jobParameters);
        System.out.println(execution.getEndTime());
        ListItemWriter writer = ctx.getBean("writer", ListItemWriter.class);
        for(Object o : writer.getWrittenItems()) {
            System.out.println(((Dummy) o).toString());
        }
        

    }
    
}
