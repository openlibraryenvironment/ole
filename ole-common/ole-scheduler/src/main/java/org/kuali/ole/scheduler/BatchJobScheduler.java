package org.kuali.ole.scheduler;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;

import static org.quartz.JobKey.*;
import static org.quartz.TriggerKey.*;
import static org.quartz.impl.matchers.GroupMatcher.*;

import java.util.ArrayList;
import java.util.List;

public class BatchJobScheduler {

    // We might want to look for a scheduler already in use...
    SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    Scheduler scheduler = schedulerFactory.getScheduler();

    public BatchJobScheduler() throws SchedulerException {
    }

    /**
     * Create a new Quartz job and register it in the scheduler.
     * By using this method the batch job is triggered via a rest call.
     * The url for this rest call has to be passed on to this method.
     *
     * @param jobName        the name of the Quartz Job
     * @param triggerName    the name of its Quartz Trigger
     * @param profileId      the ole batch profile id
     * @param batchType      the ole batch job type
     * @param url            the url of the rest controller to call, this is where the
     *                       actual batch job starts
     * @param cronExpression the cron expression to specify when the job should run
     * @throws Exception
     */
    public void scheduleRestJob(String jobName, String triggerName, String profileId, String batchType, String url,
                                String cronExpression) throws Exception {
        BatchJobLauncher batchJobLauncher = new BatchJobLauncher(profileId, batchType, url);
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        jobDetail.setTargetObject(batchJobLauncher);
        jobDetail.setTargetMethod("startJobByRestCall");
        jobDetail.setName(jobName);
        jobDetail.setConcurrent(false);
        jobDetail.afterPropertiesSet();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName)
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();
        scheduler.scheduleJob(jobDetail.getObject(), trigger);
    }

    /**
     * Create a new Quartz job and register it in the scheduler.
     * By using this method the batch job gets executed directly as a Spring Batch Job.
     * That Spring Batch Job has to be passed on to this method.
     *
     * @param job               the Spring Batch Job to be scheduled
     * @param jobLauncher       the Spring Batch Job Launcher
     * @param cronExpression    the cron expression to specify when the job should run
     * @param quartzJobName     the name of the Quartz Job
     * @param quartzTriggerName the name of its Quartz Trigger
     * @throws Exception
     */
    public void scheduleSpringBatchJob(Job job, JobLauncher jobLauncher, String cronExpression,
                                       String quartzJobName, String quartzTriggerName) throws Exception {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        BatchJobLauncher batchJobLauncher = new BatchJobLauncher();
        jobDetail.setTargetObject(batchJobLauncher);
        jobDetail.setTargetMethod("startSpringBatchJob");
        jobDetail.setArguments(new Object[]{job, jobLauncher});
        jobDetail.setName(quartzJobName);
        jobDetail.setConcurrent(false);
        jobDetail.afterPropertiesSet();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(quartzTriggerName)
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();
        scheduler.scheduleJob(jobDetail.getObject(), trigger);
    }

    /**
     * Delete a job from the scheduler, will not be executed or listed anymore
     *
     * @param jobName the job to be removed
     * @throws SchedulerException
     */
    public void deleteJob(String jobName) throws SchedulerException {
        scheduler.deleteJob(jobKey(jobName));
    }

    /**
     * Change scheduling for a given job with a given trigger.
     *
     * @param jobName        the job to be rescheduled
     * @param triggerName    the name of the new trigger
     * @param cronExpression the cron expression to specify when the job should run
     * @throws SchedulerException
     */
    public void rescheduleJob(String jobName, String triggerName, String cronExpression) throws SchedulerException {
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(triggerName)
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();
        scheduler.rescheduleJob(triggerKey(triggerName), trigger);
    }

    /**
     * List all existing jobs
     *
     * @return a list of all job names
     * @throws SchedulerException
     */
    public ArrayList<String> getAllJobs() throws SchedulerException {
        ArrayList<String> jobs = new ArrayList<String>();
        for (String group : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(jobGroupEquals(group))) {
                jobs.add(jobKey.getName());
            }
        }
        return jobs;
    }

    /**
     * List all triggers for one given job
     *
     * @param jobName the job you want to find the triggers for
     * @return
     * @throws SchedulerException
     */
    public ArrayList<String> getTriggersForJob(String jobName) throws SchedulerException {
        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey(jobName));
        ArrayList<String> triggerNames = new ArrayList<String>();
        for (Trigger trigger : triggers) {
            triggerNames.add(trigger.getKey().getName());
        }
        return triggerNames;
    }

    /**
     * Start scheduler with all scheduled jobs.
     *
     * @throws SchedulerException
     */
    public void startScheduler() throws SchedulerException {
        scheduler.start();
    }

    /**
     * Stop scheduler. No job will be executed.
     *
     * @throws SchedulerException
     */
    public void stopScheduler() throws SchedulerException {
        scheduler.shutdown();
    }
}