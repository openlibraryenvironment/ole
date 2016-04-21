package org.kuali.ole.oleng.scheduler;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 * Created by SheikS on 4/18/2016.
 */
public class OleNGBatchJobListener implements JobListener {

    public static final String LISTENER_NAME = "OleNGBatchJobListener";
    
    private static final Logger LOG = Logger.getLogger(OleNGBatchJobListener.class);

    @Override
    public String getName() {
        return LISTENER_NAME; //must return a name
    }

    // Run this if job is about to be executed.
    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        String jobName = context.getJobDetail().getKey().toString();
        LOG.info("Job : " + jobName + " is going to start...");

    }

    // No idea when will run this?
    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        String jobName = context.getJobDetail().getKey().toString();
        LOG.info("Job : " + jobName + " execution is Vetoed");
    }

    //Run this after job has been executed
    @Override
    public void jobWasExecuted(JobExecutionContext context,
                               JobExecutionException jobException) {
        String jobName = context.getJobDetail().getKey().toString();
        LOG.info("Job : " + jobName + " is finished...");

        if (null != jobException && StringUtils.isNotBlank(jobException.getMessage())) {
            LOG.info("Exception thrown by: " + jobName
                    + " Exception: " + jobException.getMessage());
        }

    }
}
