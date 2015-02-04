package org.kuali.ole.batch.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.OLEBatchProcessScheduleBo;
import org.kuali.ole.batch.service.OLEBatchSchedulerService;
import org.kuali.ole.sys.batch.Job;
import org.kuali.ole.sys.batch.JobDescriptor;
import org.kuali.ole.sys.batch.Step;
import org.kuali.ole.sys.batch.service.impl.SchedulerServiceImpl;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.quartz.CronExpression;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.UnableToInterruptJobException;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.CronTriggerBean;

import java.text.ParseException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 7/23/13
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchSchedulerServiceImpl extends SchedulerServiceImpl implements OLEBatchSchedulerService {
    private static final Logger LOG = Logger.getLogger(OLEBatchSchedulerServiceImpl.class);
    private static volatile Set<String> initJobs = new HashSet<String>();
    private static final String TRIGGER_SFX = "_Trigger";

    @Override
    public void initialize() {
        //read from scheduler
        jobListener.setSchedulerService(this);
        Collection<OLEBatchProcessScheduleBo> oleBatchProcessScheduleBoList = KRADServiceLocator.getBusinessObjectService().findAll(OLEBatchProcessScheduleBo.class);
        Step step = new OLEBatchProcessStep();
        for (OLEBatchProcessScheduleBo scheduleBo : oleBatchProcessScheduleBoList) {
            try {
                CronExpression exp = new CronExpression(scheduleBo.getCronExpression());
                Date date = exp.getNextValidTimeAfter(new Date());
                if (date == null)
                    continue;
            } catch (Exception e) {
                LOG.error("Error while validating cron exp::" + scheduleBo.getCronExpression(), e);

            }
            scheduleBo.getCronExpression();
            initializeJobs(OLEConstants.OLEBatchProcess.BATCH_JOB + scheduleBo.getScheduleId(), step);
            try {
                initializeTriggersForModule(scheduleBo.getScheduleId(), scheduleBo.getCronExpression());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            initJobs.add(OLEConstants.OLEBatchProcess.BATCH_JOB + scheduleBo.getScheduleId());
        }
    }

    @Override
    public void initializeJob(String jobName, Job job) {
        if (initJobs.contains(jobName)) {
            job.setSchedulerService(this);
            job.setParameterService(parameterService);
            List<Step> steps = new ArrayList<Step>();
            steps.add(new OLEBatchProcessStep());
            steps.add(new OLEBatchProcessAdhocStep());
            steps.add(new OLEBatchProcessEmailStep());
            job.setSteps(steps);
            job.setDateTimeService(dateTimeService);
        } else {
            super.initializeJob(jobName, job);
        }
    }

    /**
     * This method loads the jobs with the given jobName - Job name here is the schedule id
     * the name will always be suffixed with "BATCH_JOB_"
     *
     * @param jobName
     */
    public void initializeJobsForModule(String jobName) {
        jobListener.setSchedulerService(this);
        if (initJobs.contains(OLEConstants.OLEBatchProcess.BATCH_JOB + jobName)) return;
        initJobs.add(OLEConstants.OLEBatchProcess.BATCH_JOB + jobName);
        initializeJobs(OLEConstants.OLEBatchProcess.BATCH_JOB + jobName, new OLEBatchProcessStep());
    }

    /**
     * method initializes triggers for the given jobName - Job name here is the schedule id
     * the name will always be suffixed with "BATCH_JOB_"
     *
     * @param jobName
     * @param cronExpression
     */
    public void initializeTriggersForModule(String jobName, String cronExpression) throws Exception {
        try {
            CronExpression exp = new CronExpression(cronExpression);
            Date date = exp.getNextValidTimeAfter(new Date());
            if (date == null) {
                throw new RuntimeException("given cron expression already past its valid time::" + cronExpression);
            } else {
                LOG.info("Next valid run time is:: " + date.toString() + " for the schedule job :: " + jobName);
                addTrigger(getCronTriggerBean(jobName, cronExpression));
            }
        } catch (Exception e) {
            LOG.info("given cron expression already past its valid time::" + cronExpression, e);
            throw e;
        }

    }

    private CronTriggerBean getCronTriggerBean(String jobName, String cronExpression) {
        CronTriggerBean cronTriggerBean = new CronTriggerBean();
        cronTriggerBean.setName(jobName + TRIGGER_SFX);
        try {
            cronTriggerBean.setCronExpression(cronExpression);
        } catch (ParseException e) {
            LOG.error("Error while parsing cron expression :: " + cronExpression, e);
            throw new RuntimeException("Error while parsing cron expression", e);
        }
        cronTriggerBean.setJobName(OLEConstants.OLEBatchProcess.BATCH_JOB + jobName);
        cronTriggerBean.setJobGroup(SCHEDULED_GROUP);
        return cronTriggerBean;
    }

    private void initializeJobs(String jobName, Step step) {
        JobDescriptor jobDescriptor = new JobDescriptor();
        jobDescriptor.setBeanName(jobName);
        jobDescriptor.setGroup(SCHEDULED_GROUP);
        JobDetail jobDetail = jobDescriptor.getJobDetail();
        jobDescriptor.getSteps().add(step);
        loadJob(jobDescriptor);
    }

    private void initializeAdhocJobs(String jobName, Step step) {
        JobDescriptor jobDescriptor = new JobDescriptor();
        jobDescriptor.setBeanName(jobName);
        jobDescriptor.setGroup(UNSCHEDULED_GROUP);
        JobDetail jobDetail = jobDescriptor.getJobDetail();
        jobDescriptor.getSteps().add(step);
        loadJob(jobDescriptor);
    }

    public void startJob(String jobName) throws Exception {
        try {
            jobListener.setSchedulerService(this);
            if (initJobs.contains(OLEConstants.OLEBatchProcess.ADHOC_BATCH_JOB + jobName)) return;
            initJobs.add(OLEConstants.OLEBatchProcess.ADHOC_BATCH_JOB + jobName);
            initializeAdhocJobs(OLEConstants.OLEBatchProcess.ADHOC_BATCH_JOB + jobName, new OLEBatchProcessAdhocStep());
            scheduler.triggerJob(OLEConstants.OLEBatchProcess.ADHOC_BATCH_JOB + jobName, UNSCHEDULED_GROUP);
        } catch (SchedulerException e) {
            LOG.error("Error while starting job :: " + OLEConstants.OLEBatchProcess.ADHOC_BATCH_JOB + jobName, e);
            throw e;
        }
    }

    public void pauseJob(String jobName) throws Exception {
        try {
            scheduler.pauseJob(OLEConstants.OLEBatchProcess.ADHOC_BATCH_JOB + jobName, SCHEDULED_GROUP);
        } catch (SchedulerException e) {
            LOG.error("Error while pausing job :: " + OLEConstants.OLEBatchProcess.ADHOC_BATCH_JOB + jobName, e);
            throw e;
        }
    }

    public void resumeJob(String jobName) throws Exception {
        try {
            scheduler.resumeJob(OLEConstants.OLEBatchProcess.ADHOC_BATCH_JOB + jobName, SCHEDULED_GROUP);
        } catch (SchedulerException e) {
            LOG.error("Error while resuming job :: " + OLEConstants.OLEBatchProcess.ADHOC_BATCH_JOB + jobName, e);
            throw e;
        }
    }

    @Override
    public void deleteJob(String jobName) throws Exception {
        removeScheduled(OLEConstants.OLEBatchProcess.BATCH_JOB + jobName);
    }

    @Override
    public void rescheduleJob(String jobName, String cronExp) throws Exception {
        scheduler.rescheduleJob(jobName + TRIGGER_SFX, OLEConstants.OLEBatchProcess.BATCH_JOB + jobName, getCronTriggerBean(jobName, cronExp));
    }

    public void unScheduleOneTimeJob(String jobName, String jobGroup) {
        Map map = new HashMap();
        map.put("scheduleId", StringUtils.substringAfter(jobName, "BATCH_JOB_"));
        OLEBatchProcessScheduleBo scheduleBo = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEBatchProcessScheduleBo.class, map);
        if (scheduleBo != null && StringUtils.isNotBlank(scheduleBo.getOneTimeOrRecurring())) {
            if (scheduleBo.getOneTimeOrRecurring().equalsIgnoreCase("onetime")) {
                removeScheduled(jobName);
                LOG.info("Removed" + jobName + "from Scheduler");
            }
        }
    }
}
