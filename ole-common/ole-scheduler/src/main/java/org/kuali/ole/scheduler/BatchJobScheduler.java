package org.kuali.ole.scheduler;

import org.quartz.CronScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

public class BatchJobScheduler {

    // We might want to look for a scheduler already in use...
    SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    Scheduler scheduler = schedulerFactory.getScheduler();

    public BatchJobScheduler() throws SchedulerException {
    }

    public void scheduleJob(String jobName, String triggerName, String profileName, String batchType,
            String cronExpression) throws Exception {
        BatchJobLauncher batchJobLauncher = new BatchJobLauncher(profileName, batchType);
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        jobDetail.setTargetObject(batchJobLauncher);
        jobDetail.setTargetMethod("start");
        jobDetail.setName(jobName);
        jobDetail.setConcurrent(false);
        jobDetail.afterPropertiesSet();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName, "group1")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();
        scheduler.scheduleJob(jobDetail.getObject(), trigger);

    }

    public void startScheduler() throws SchedulerException {
        scheduler.start();
    }

    public void stopScheduler() throws SchedulerException {
        scheduler.shutdown();
    }

}
