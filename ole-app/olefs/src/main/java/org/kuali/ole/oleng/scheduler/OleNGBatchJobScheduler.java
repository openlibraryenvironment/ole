package org.kuali.ole.oleng.scheduler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.oleng.batch.process.model.BatchProcessJob;
import org.kuali.ole.oleng.dao.DescribeDAO;
import org.kuali.ole.oleng.scheduler.job.BatchSchedulerJob;
import org.kuali.ole.oleng.util.OleNGSchedulerHelperUtil;
import org.kuali.ole.spring.batch.processor.*;
import org.kuali.ole.utility.OleHttpRestGet;
import org.kuali.ole.utility.OleHttpRestGetImpl;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 4/18/2016.
 */
@Service("oleNGBatchJobScheduler")
public class OleNGBatchJobScheduler extends OleNGSchedulerHelperUtil {

    private static final Logger LOG = Logger.getLogger(OleNGBatchJobScheduler.class);

    private OleHttpRestGet oleHttpRestGet;
    private static Scheduler scheduler = null;

    @Autowired
    private BatchBibFileProcessor batchBibFileProcessor;

    @Autowired
    private BatchOrderImportProcessor batchOrderImportProcessor;

    @Autowired
    private BatchInvoiceImportProcessor batchInvoiceImportProcessor;

    @Autowired
    private BatchDeleteFileProcessor batchDeleteFileProcessor;

    @Autowired
    private BatchExportFileProcessor batchExportFileProcessor;


    @Autowired
    private DescribeDAO describeDAO;
    
    public void initializeAllJobs() {
        LOG.info("-- initializing batch jobs --");

        String defaultUserForRestCall = getDefaultUserForRestCall();

        List<BatchProcessJob> batchProcessJobs = getDescribeDAO().fetchAllBatchProcessJobs();
        if(CollectionUtils.isNotEmpty(batchProcessJobs)) {
            for (Iterator<BatchProcessJob> iterator = batchProcessJobs.iterator(); iterator.hasNext(); ) {
                BatchProcessJob batchProcessJob = iterator.next();
                try {
                    long id      = batchProcessJob.getJobId();
                    String type = batchProcessJob.getProfileType();
                    String cron = batchProcessJob.getCronExpression();
                    long profileId = batchProcessJob.getBatchProfileId();

                    if (StringUtils.isNotBlank(cron)) {
                        Date nextValidTimeAfter = getNextValidTimeAfter(cron);
                        if(null != nextValidTimeAfter) {
                            batchProcessJob.setNextRunTime(new Timestamp(nextValidTimeAfter.getTime()));
                            batchProcessJob.setJobType(OleNGConstants.SCHEDULED);
                            getBusinessObjectService().save(batchProcessJob);
                            scheduleOrRescheduleJob(id, profileId, type, cron, defaultUserForRestCall);
                        }
                    }
                } catch (Exception e) {
                    LOG.info("Failed to Schedule Job: " + batchProcessJob.getJobName());
                    e.printStackTrace();
                }
            }
        }
    }

    public void scheduleOrRescheduleJob(long id, long profileId, String jobType, String cron, String principalName) {
        try {
            String jobId = OleNGConstants.JOB_NAME_PFX + id;
            JobDetail jobDetail = getScheduler().getJobDetail(jobId, OleNGConstants.GROUP);
            String triggerName = jobId + OleNGConstants.TRIGGER_SFX;
            if(null != jobDetail) {
                unScheduleJob(jobId, false);
            }
            getScheduler().start();
            JobDetail job =  getJobDetail(jobType, jobId, OleNGConstants.GROUP);

            if (null != job && job.getJobClass() != null) {
                try {
                    job.getJobDataMap().put(OleNGConstants.JOB_ID, id);
                    job.getJobDataMap().put(OleNGConstants.PROFILE_ID, profileId);
                    job.getJobDataMap().put(OleNGConstants.JOB_TYPE, jobType);
                    job.getJobDataMap().put(OleNGConstants.PRINCIPAL_NAME, principalName);
                    CronTrigger trigger = new CronTrigger();
                    trigger.setName(triggerName);
                    trigger.setCronExpression(cron);
                    getScheduler().scheduleJob(job, trigger);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public boolean pauseJob(String jobId){
        boolean paused = false;
        String jobName = OleNGConstants.JOB_NAME_PFX + jobId;
        try {
            getScheduler().pauseJob(jobName, OleNGConstants.GROUP);
            paused = true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return paused;
    }

    public boolean unScheduleJob(String jobName, boolean addPrefix){
        boolean unscheduled = false;
        if(addPrefix) {
            jobName = OleNGConstants.JOB_NAME_PFX + jobName;
        }
        try {
            getScheduler().deleteJob(jobName, OleNGConstants.GROUP);
            unscheduled = true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return unscheduled;
    }

    public boolean resumeJob(String jobId){
        boolean unscheduled = false;
        String jobName = OleNGConstants.JOB_NAME_PFX + jobId;
        try {
            scheduler.resumeJob(jobName, OleNGConstants.GROUP);
            unscheduled = true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return unscheduled;
    }

    private JobDetail getJobDetail(String jobType, String jobName, String group) {
        JobDetail job = new JobDetail();
        job.setName(jobName);
        job.setGroup(group);
        job.setJobClass(BatchSchedulerJob.class);
        if(OleNGConstants.BIB_IMPORT.equalsIgnoreCase(jobType)) {
            job.getJobDataMap().put(OleNGConstants.PROCESSOR, batchBibFileProcessor);
        } else if(OleNGConstants.ORDER_RECORD_IMPORT.equalsIgnoreCase(jobType)) {
            job.getJobDataMap().put(OleNGConstants.PROCESSOR, batchOrderImportProcessor);
        } else if(OleNGConstants.INVOICE_IMPORT.equalsIgnoreCase(jobType)) {
            job.getJobDataMap().put(OleNGConstants.PROCESSOR, batchInvoiceImportProcessor);
        } else if(OleNGConstants.BATCH_DELETE.equalsIgnoreCase(jobType)) {
            job.getJobDataMap().put(OleNGConstants.PROCESSOR, batchDeleteFileProcessor);
        } else if(OleNGConstants.BATCH_EXPORT.equalsIgnoreCase(jobType)) {
            job.getJobDataMap().put(OleNGConstants.PROCESSOR, batchExportFileProcessor);
        }
        return job;
    }

    public static Scheduler getScheduler() {
        if(null == scheduler) {
            try {
                scheduler = new StdSchedulerFactory().getScheduler();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }

        try {
            if(null !=  scheduler && scheduler.isShutdown()) {
                scheduler.start();
                scheduler.addJobListener(new org.kuali.ole.oleng.scheduler.OleNGBatchJobListener());
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public OleHttpRestGet getOleHttpRestGet() {
        if(null == oleHttpRestGet) {
            oleHttpRestGet = new OleHttpRestGetImpl();
        }
        return oleHttpRestGet;
    }

    public void setOleHttpRestGet(OleHttpRestGet oleHttpRestGet) {
        this.oleHttpRestGet = oleHttpRestGet;
    }

    public DescribeDAO getDescribeDAO() {
        return describeDAO;
    }

    public void setDescribeDAO(DescribeDAO describeDAO) {
        this.describeDAO = describeDAO;
    }

    private String getDefaultUserForRestCall() {
        return ParameterValueResolver.getInstance().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OleNGConstants.DEFAULT_USER_FOR_REST_CALLS);
    }
}
