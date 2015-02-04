package org.kuali.ole.batch.service;

import org.kuali.ole.sys.batch.service.SchedulerService;

/**
 * Created with IntelliJ IDEA.
 * User: meenrajd
 * Date: 7/26/13
 * Time: 12:09 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OLEBatchSchedulerService extends SchedulerService {
    public void initializeJobsForModule(String jobName);

    public void initializeTriggersForModule(String jobName, String cronExpression) throws Exception;

    public void startJob(String jobName) throws Exception;

    public void pauseJob(String jobName) throws Exception;

    public void resumeJob(String jobName) throws Exception;

    public void deleteJob(String jobName) throws Exception;

    public void rescheduleJob(String jobName, String cronExp) throws Exception;

    public void unScheduleOneTimeJob(String jobName, String jobGroup);
}
