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

import static org.quartz.JobKey.*;
import static org.quartz.TriggerKey.*;
import static org.quartz.impl.matchers.GroupMatcher.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BatchJobScheduler {

    // We might want to look for a scheduler already in use...
    SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    Scheduler scheduler = schedulerFactory.getScheduler();

    public BatchJobScheduler() throws SchedulerException {
    }

    /**
     * Create a new batch job and register it in the scheduler
     * 
     * @param jobName
     *            the name of the job
     * @param triggerName
     *            the name of its trigger
     * @param profileId
     *            the ole batch profile id
     * @param batchType
     *            the ole batch job type
     * @param url
     *            the url of the rest controller to call, this is where the
     *            actual batch job starts
     * @param cronExpression
     *            the cron expression to specify when the job should run
     * @throws Exception
     */
    public void scheduleJob(String jobName, String triggerName, String profileId, String batchType, String url,
            String cronExpression) throws Exception {
        BatchJobLauncher batchJobLauncher = new BatchJobLauncher(profileId, batchType, url);
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        jobDetail.setTargetObject(batchJobLauncher);
        jobDetail.setTargetMethod("start");
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
     * Delete a job from the scheduler, will not be executed or listed anymore
     * 
     * @param jobName
     *            the job to be removed
     * @throws SchedulerException
     */
    public void deleteJob(String jobName) throws SchedulerException {
        scheduler.deleteJob(jobKey(jobName));
    }

    /**
     * Change scheduling for a given job with a given trigger.
     * 
     * @param jobName
     *            the job to be rescheduled
     * @param triggerName
     *            the name of the new trigger
     * @param cronExpression
     *            the cron expression to specify when the job should run
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
     * @param jobName
     *            the job you want to find the triggers for
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