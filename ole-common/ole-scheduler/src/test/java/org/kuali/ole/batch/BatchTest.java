package org.kuali.ole.batch;

import org.junit.Test;
import org.quartz.CronScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

public class BatchTest {

    // @Test
    public void testBatch() throws Exception {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(BatchConfiguration.class);
        Job job = ctx.getBean("dummyJob", Job.class);
        JobLauncher jobLauncher = ctx.getBean("jobLauncher", JobLauncher.class);
        JobExecution execution = jobLauncher.run(job, new JobParameters());
        System.out.println(execution.getEndTime());
    }

    //  @Test
    public void testScheduler() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(BatchConfiguration.class);
        Scheduler scheduler = ctx.getBean("quartzScheduler", Scheduler.class);
        Job job = ctx.getBean("dummyJob", Job.class);
        JobLauncher jobLauncher = ctx.getBean("jobLauncher", JobLauncher.class);

        try {
            MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
            jobDetail.setTargetObject(jobLauncher);
            jobDetail.setTargetMethod("run");
            jobDetail.setName("DummyJobDetail");
            jobDetail.setArguments(new Object[]{job, new JobParameters()});
            jobDetail.setConcurrent(false);
            jobDetail.afterPropertiesSet();            
          
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("DummyTrigger", "group1")
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
                    .build();
            
            scheduler.clear();
            scheduler.scheduleJob(jobDetail.getObject(), trigger);
            scheduler.start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        BatchTest bt = new BatchTest();
        bt.testScheduler();
    }

}
