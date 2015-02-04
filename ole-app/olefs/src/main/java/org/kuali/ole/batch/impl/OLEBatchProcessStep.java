package org.kuali.ole.batch.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo;
import org.kuali.ole.batch.bo.OLEBatchProcessScheduleBo;
import org.kuali.ole.batch.document.OLEBatchProcessDefinitionDocument;
import org.kuali.ole.batch.helper.OLEBatchProcessDataHelper;
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
public class OLEBatchProcessStep extends AbstractStep {
    private static final Logger LOG = Logger.getLogger(OLEBatchProcessStep.class);
    private OLEBatchProcessDataHelper oleBatchProcessDataHelper;

    @Override
    public boolean execute(String jobName, Date jobRunDate) throws InterruptedException {
        String scheduleId = StringUtils.substringAfter(jobName, SchedulerService.SCHEDULED_GROUP + "." + OLEConstants.OLEBatchProcess.BATCH_JOB);
        if (StringUtils.isEmpty(scheduleId)) return true;
        return executeBatch(scheduleId);
    }

    /**
     * performs job operation
     * 1. reads the scheduled record in using the scheduleId which is in the job name
     * 2. Reads the process definition from the process def table
     * 3. Creates a job record for the given schedule
     * 4. Creates the process based on process type and runs the batch process
     *
     * @param scheduleId
     * @return
     */
    private boolean executeBatch(String scheduleId) {
        try {
            OLEBatchProcessScheduleBo scheduleBo = readScheduleRecord(scheduleId);
            String prcsId = scheduleBo.getBatchProcessId();
            Map<String, String> prcsMap = new HashMap<String, String>();
            prcsMap.put("bat_prcs_id", prcsId);
            OLEBatchProcessDefinitionDocument processDef = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEBatchProcessDefinitionDocument.class, prcsMap);
            OLEBatchProcessJobDetailsBo jobBo = createJobRecord(processDef, scheduleBo);
            createBatchProcessJobFile(processDef.getBatchProcessType(), jobBo);
            LOG.info("Executing Batch process type :: " + jobBo.getBatchProcessType());
            OLEBatchProcess process = BatchProcessFactory.createProcess(jobBo.getBatchProcessType());
            process.process(processDef, jobBo);
        } catch (Exception e) {
            LOG.error("Error while running Batch Process Step:: OLEBatchProcessStep", e);
            //  return false;
        }
        return true;
    }

    /**
     * Creates the job record for the given process def
     *
     * @param processDef
     * @return
     */
    private OLEBatchProcessJobDetailsBo createJobRecord(OLEBatchProcessDefinitionDocument processDef, OLEBatchProcessScheduleBo scheduleBo) {
        OLEBatchProcessJobDetailsBo jobBo = new OLEBatchProcessJobDetailsBo();
        jobBo.setBatchProcessId(processDef.getBatchProcessId());
        jobBo.setBatchProcessType(processDef.getBatchProcessType());
        jobBo.setBatchProfileName(processDef.getBatchProcessProfileName());
        jobBo.setUserName(processDef.getUser());
        OLEBatchProcessJobDetailsBo jobDetailsBo = KRADServiceLocator.getBusinessObjectService().save(jobBo);
        jobDetailsBo.setJobName(processDef.getBatchProcessName());
        jobDetailsBo.setUserName(processDef.getUser());
        jobDetailsBo.setUploadFileName(scheduleBo.getUploadFileName());
        jobDetailsBo.setOleBatchPrcsScheduleId(scheduleBo.getScheduleId());
        return KRADServiceLocator.getBusinessObjectService().save(jobDetailsBo);
    }

    /**
     * Reads the schedule record for the given schedule id
     *
     * @param scheduleId
     * @return
     */
    private OLEBatchProcessScheduleBo readScheduleRecord(String scheduleId) {
        Map<String, String> schMap = new HashMap<String, String>();
        schMap.put("ole_bat_prcs_schdule_id", scheduleId);
        OLEBatchProcessScheduleBo scheduleBo = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEBatchProcessScheduleBo.class, schMap);
        return scheduleBo;
    }


    /**
     * This method is using for creating the job file for each schedule job running time
     * @param batchProcessType
     * @param job
     * @throws Exception
     */
    private void createBatchProcessJobFile(String batchProcessType, OLEBatchProcessJobDetailsBo job) throws Exception {
        if (!batchProcessType.equals(OLEConstants.OLEBatchProcess.BATCH_EXPORT)) {
            String mrcFileContent = null;
            String ediFileContent = null;
            String ingestFileContent = null;
            String mrcFileName = null;
            String ediFileName = null;
            if (batchProcessType.equals(OLEConstants.OLEBatchProcess.ORDER_RECORD_IMPORT)) {
                String[] fileNames = job.getUploadFileName().split(",");
                if (fileNames.length == 2) {
                    mrcFileName = fileNames[0];
                    ediFileName = fileNames[1];
                    mrcFileContent = getOLEBatchProcessDataHelper().getBatchProcessFileContent(batchProcessType, job.getOleBatchPrcsScheduleId() + OLEConstants.OLEBatchProcess.PROFILE_SCHEDULE + "_" + mrcFileName, job.getOleBatchPrcsScheduleId());
                    ediFileContent = getOLEBatchProcessDataHelper().getBatchProcessFileContent(batchProcessType, job.getOleBatchPrcsScheduleId() + OLEConstants.OLEBatchProcess.PROFILE_SCHEDULE + "_" + ediFileName, job.getOleBatchPrcsScheduleId());
                    createBatchProcessJobFile(batchProcessType, job.getJobId() + OLEConstants.OLEBatchProcess.PROFILE_JOB + "_" + mrcFileName, job.getJobId() + OLEConstants.OLEBatchProcess.PROFILE_JOB + "_" + ediFileName, mrcFileContent, ediFileContent , job.getJobId());
                }
                else {
                    ingestFileContent = getOLEBatchProcessDataHelper().getBatchProcessFileContent(batchProcessType, job.getOleBatchPrcsScheduleId() + OLEConstants.OLEBatchProcess.PROFILE_SCHEDULE + "_" + job.getUploadFileName(), job.getOleBatchPrcsScheduleId());
                    createBatchProcessJobFile(batchProcessType, job.getJobId() + OLEConstants.OLEBatchProcess.PROFILE_JOB + "_" + job.getUploadFileName(), ingestFileContent, job.getJobId());
                }
            } else {
                ingestFileContent = getOLEBatchProcessDataHelper().getBatchProcessFileContent(batchProcessType, job.getOleBatchPrcsScheduleId() + OLEConstants.OLEBatchProcess.PROFILE_SCHEDULE + "_" + job.getUploadFileName(), job.getOleBatchPrcsScheduleId());
                createBatchProcessJobFile(batchProcessType, job.getJobId() + OLEConstants.OLEBatchProcess.PROFILE_JOB + "_" + job.getUploadFileName(), ingestFileContent, job.getJobId());
            }


        }
    }

    private void createBatchProcessJobFile(String batchProcessType, String mrcFileName, String ediFileName, String mrcFileContent, String ediFileContent , String jobId) throws Exception {
        getOLEBatchProcessDataHelper().createBatchProcessFile(batchProcessType, mrcFileName, ediFileName, mrcFileContent, ediFileContent , jobId);
    }

    private void createBatchProcessJobFile(String batchProcessType, String fileName, String fileContent , String jobId) throws Exception {
        getOLEBatchProcessDataHelper().createBatchProcessFile(batchProcessType, fileName, fileContent , jobId);
    }

    private OLEBatchProcessDataHelper getOLEBatchProcessDataHelper() {

        if (oleBatchProcessDataHelper == null) {
            oleBatchProcessDataHelper = OLEBatchProcessDataHelper.getInstance();
        }
        return oleBatchProcessDataHelper;
    }
}
