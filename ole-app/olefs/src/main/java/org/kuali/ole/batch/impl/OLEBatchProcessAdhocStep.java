package org.kuali.ole.batch.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo;
import org.kuali.ole.batch.document.OLEBatchProcessDefinitionDocument;
import org.kuali.ole.sys.batch.AbstractStep;
import org.kuali.ole.sys.batch.service.SchedulerService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: meenrajd
 * Date: 7/25/13
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessAdhocStep extends AbstractStep {
    private static final Logger LOG = Logger.getLogger(OLEBatchProcessAdhocStep.class);

    @Override
    public boolean execute(String jobName, Date jobRunDate) throws InterruptedException {
        String jobId = StringUtils.substringAfter(jobName, SchedulerService.SCHEDULED_GROUP + "." + OLEConstants.OLEBatchProcess.ADHOC_BATCH_JOB);
        if (StringUtils.isEmpty(jobId)) return true;
        return executeBatch(jobId);
    }

    /**
     * performs the  job operation
     * 1. Reads the job record
     * 2. gets the process type from the job record, creates the specific process and call the process batch
     * jobName will be of the fomat scheduled.ADHOC_BATCH_JOB{1} where 1 is the job record id
     *
     * @param jobName
     * @return
     */
    private boolean executeBatch(String jobName) {
        try {
            OLEBatchProcessJobDetailsBo jobBo = readJobRecord(jobName);
            Map<String, String> prcsMap = new HashMap<String, String>();
            prcsMap.put("bat_prcs_id", jobBo.getBatchProcessId());
            OLEBatchProcessDefinitionDocument processDef = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEBatchProcessDefinitionDocument.class, prcsMap);
            LOG.info("Executing Batch process type :: " + jobBo.getBatchProcessType());
            OLEBatchProcess process = BatchProcessFactory.createProcess(jobBo.getBatchProcessType());
            process.process(processDef, jobBo);
        } catch (Exception e) {
            LOG.error("Error while running Batch Process Step::OLEBatchProcessAdhocStep", e);
            // return false;
        }
        return true;
    }

    /**
     * reads the job record which is being run
     *
     * @param jobId
     * @return
     */
    private OLEBatchProcessJobDetailsBo readJobRecord(String jobId) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOG.error("Error while running Batch Process Step::", e);
        }
        Map<String, String> jobMap = new HashMap<String, String>();
        jobMap.put("job_id", jobId);
        OLEBatchProcessJobDetailsBo jobDetailsBo = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEBatchProcessJobDetailsBo.class, jobMap);
        return jobDetailsBo;
        //jobDetailsBo.setJobName(jobDetailsBo.getBatchProfileName() + "_" + jobDetailsBo.getJobId());
        //return KRADServiceLocator.getBusinessObjectService().save(jobDetailsBo);

    }
}
