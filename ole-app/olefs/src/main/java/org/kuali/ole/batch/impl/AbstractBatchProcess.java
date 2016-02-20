package org.kuali.ole.batch.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.controller.OLEBatchProcessJobDetailsController;
import org.kuali.ole.batch.document.OLEBatchProcessDefinitionDocument;
import org.kuali.ole.batch.helper.OLEBatchProcessDataHelper;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: meenrajd
 * Date: 7/8/13
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractBatchProcess implements OLEBatchProcess {

    protected OLEBatchProcessJobDetailsBo job;
    protected OLEBatchProcessDefinitionDocument processDef;
    private OLEBatchProcessDataHelper oleBatchProcessDataHelper;
    private static final Logger LOG = Logger.getLogger(OLEBatchProcessDataHelper.class);
    private BusinessObjectService businessObjectService;
    String jobStatus = null;
    private OLEBatchProcessDataHelper getOLEBatchProcessDataHelper() {

        if (oleBatchProcessDataHelper == null) {
            oleBatchProcessDataHelper = OLEBatchProcessDataHelper.getInstance();
        }
        return oleBatchProcessDataHelper;
    }

    /**
     * This method will be invoked for various processes implementing the OLEBatchProcess
     * The method will perform following operation in sequence
     * 1. loadProfile() - loads the profile of the process that has been invoked
     * 2. prepareForRead() - performs the initial read or read support operation as implemented by the invoked process
     * 3. prepareForWrite() - performs the initial write or write support operation as implemented by the invoked process
     * 4. writeData() - performs the actual write operation intended and implemented by the process invoked
     * 5. getNextBatch() - performs the read operation if there is more data to be read
     * <p/>
     * The writeData() and getNextBatch() will be executed till the job status is RUNNING. The implemented process will
     * have to set the job status to STOPPED / COMPLETED etc. as per its discretion
     * <p/>
     * updateBatchProgress() - performs the job update after each of the above method complete operation.
     *
     * @param processDef
     * @param jobBo
     * @throws Exception
     */
    @Override
    public final void process(OLEBatchProcessDefinitionDocument processDef, OLEBatchProcessJobDetailsBo jobBo) throws Exception {
        this.job = jobBo;
        if (job.getStatus() != null && job.getStatus().equals(OLEConstants.OLEBatchProcess.JOB_STATUS_PAUSED)) {
            job.setStatus(job.getStatus());
        } else {
            job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_RUNNING);//set job to running status
        }
        job.setStartTime(job.getStartTime() == null ? new Timestamp(new Date().getTime()) : job.getStartTime()); // set the start time of job
        try {
            updateJobProgress();   // update job
            checkJobPauseStatus();
            loadProfile(processDef); // load the profile for the given process id
            checkJobPauseStatus();
            updateJobProgress();  // update job
            checkJobPauseStatus();
            this.prepareForRead();  // read the data
            checkJobPauseStatus();
            updateJobProgress();  // update job
            checkJobPauseStatus();
            if (isJobRunning()) {     // check if the job is running
                checkJobPauseStatus();
                this.prepareForWrite(); // prepare for writing the data
                checkJobPauseStatus();
                updateJobProgress();  // update job
                while (isJobRunning()) {   // check if the job is running
                    checkJobPauseStatus();
                    this.processBatch();     // write data
                    checkJobPauseStatus();
                    updateJobProgress();  // update job
                    if (isJobRunning()) {    // check if the job is running
                        checkJobPauseStatus();
                        this.getNextBatch();  // get the next batch of records
                        checkJobPauseStatus();
                        updateJobProgress();  // update job
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("Error while performing batch process for profile :: " + this.processDef.getBatchProcessProfileName(), ex);
            job.setEndTime(new Timestamp(new Date().getTime())); // set the end time of the job
            job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_STOPPED);
            job.setStatusDesc("Batch process Failed for profile :: " + this.processDef.getBatchProcessProfileName());
            updateJobProgress();
            DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
            List<String> reasonForFailure = (List<String>) dataCarrierService.getData("reasonForBibImportFailure");
            StringBuffer failureBuffer = new StringBuffer();
            if(reasonForFailure != null && reasonForFailure.size() > 0){
                for(int failureCount = 0;failureCount < reasonForFailure.size();failureCount++){
                    failureBuffer.append(reasonForFailure.get(failureCount) + "\n");
                }
                createBatchErrorAttachmentFile(failureBuffer.toString());
            }
            dataCarrierService.addData("reasonForBibImportFailure",new ArrayList<>());
            List<String> reasonForInvoiceImportFailure = (List<String>) dataCarrierService.getData("invoiceIngestFailureReason");
            StringBuffer invoiceFailureBuffer = new StringBuffer();
            if(reasonForInvoiceImportFailure != null && reasonForInvoiceImportFailure.size() > 0){
                for(int failureCount = 0;failureCount < reasonForInvoiceImportFailure.size();failureCount++){
                    invoiceFailureBuffer.append(reasonForInvoiceImportFailure.get(failureCount) + "\n");
                }
                createBatchErrorAttachmentFile(invoiceFailureBuffer.toString());
            }
            dataCarrierService.addData("invoiceIngestFailureReason",new ArrayList<>());
            throw new Exception("Batch process Failed", ex);
        }

        if (job.getStatus().equals(OLEConstants.OLEBatchProcess.JOB_STATUS_STOPPED)) {
            if (jobBo != null) {
                jobBo.setStatus(job.getStatus());
                getBusinessObjectService().save(jobBo);
                job.setStatusDesc("Batch Operation Stopped");
                OLEBatchProcessJobDetailsController.removeStatusFromBatchProcess(jobBo.getJobId());
            }
        }
        job.setEndTime(new Timestamp(new Date().getTime())); // set the end time of the job
        if (StringUtils.isEmpty(job.getStatusDesc())) {
            job.setStatusDesc("Batch Operation Completed");
        }
        updateJobProgress();  // update job
    }

    protected BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null)
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        return businessObjectService;
    }

    /**
     * First method to be called in the batch process will load the profile for the process id in the process defn document
     *
     * @param processDef
     */
    protected void loadProfile(OLEBatchProcessDefinitionDocument processDef) throws Exception {
        this.processDef = processDef;
        //load profile
        try {
            OLEBatchProcessProfileBo profileBo = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OLEBatchProcessProfileBo.class, processDef.getBatchProcessProfileId());
            this.processDef.setOleBatchProcessProfileBo(profileBo);
        } catch (Exception ex) {
            LOG.error("Error while loading profile :: " + this.processDef.getBatchProcessProfileName(), ex);
            throw ex;
        }
    }

    /**
     * method to update the batch job progress will be called after every other method completes operation
     */
    public void updateJobProgress() throws Exception {
            updatePercentCompleted();
            updateTimeSpent();
            KRADServiceLocator.getBusinessObjectService().save(job);

    }

    /**
     * method to be implemented to read initial data for the various processes; will be called before prepareForWrite() method
     *
     * @throws Exception
     */
    protected abstract void prepareForRead() throws Exception;

    /**
     * method to be implemented to prepare for writing the data; will be called before writeData()
     *
     * @throws Exception
     */
    protected abstract void prepareForWrite() throws Exception;

    /**
     * method to be implemented to retreive the next batch of data to be processed
     *
     * @throws Exception
     */
    protected abstract void getNextBatch() throws Exception;

    /**
     * method to be implemented for various write operation
     */
    protected abstract void processBatch() throws Exception;

    /**
     * checks if the job is in the RUNNING state
     *
     * @return
     */
    protected boolean isJobRunning() {
        if(job.getStatus().equals(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED)) {
            return false;
        }
        else {
        if (OLEBatchProcessJobDetailsController.getBatchProcessJobStatusMap() != null && OLEBatchProcessJobDetailsController.getBatchProcessJobStatusMap().size() > 0) {
            jobStatus = OLEBatchProcessJobDetailsController.getBatchProcessJobStatus(job.getJobId());
            job.setStatus(jobStatus);
        }

        if (jobStatus == null) {
            Map<String, String> jobMap = new HashMap<String, String>();
            jobMap.put("job_id", job.getJobId());
            OLEBatchProcessJobDetailsBo jobDetailsBo = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEBatchProcessJobDetailsBo.class, jobMap);
            OLEBatchProcessJobDetailsController.setBatchProcessJobStatusMap(job.getJobId(),jobDetailsBo.getStatus());
            if (jobDetailsBo.getStatus().equalsIgnoreCase(OLEConstants.OLEBatchProcess.JOB_STATUS_RUNNING)) {
                return true;
            } else {
                return false;
            }
        } else if (jobStatus != null && OLEConstants.OLEBatchProcess.JOB_STATUS_RUNNING.equalsIgnoreCase(jobStatus)) {
            return true;
        } else {
            return false;
        }
        }
    }

    /**
     * checks if the job is in the Pause state
     *
     * @return
     */
    protected void checkJobPauseStatus() throws Exception{
        Map<String, String> jobMap = new HashMap<String, String>();
        jobMap.put("job_id", job.getJobId());
        OLEBatchProcessJobDetailsBo jobDetailsBo = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEBatchProcessJobDetailsBo.class, jobMap);
        while(jobDetailsBo.getStatus().equalsIgnoreCase(OLEConstants.OLEBatchProcess.JOB_STATUS_PAUSED)) {
            Thread.sleep(100);
            jobMap.put("job_id", job.getJobId());
            jobDetailsBo = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEBatchProcessJobDetailsBo.class, jobMap);
        }
    }


    /**
     * updates the time spent since the job started
     */
    private void updateTimeSpent() {
        Timestamp startTime = job.getStartTime();
        long diff = Calendar.getInstance().getTime().getTime() - startTime.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        StringBuffer sb = new StringBuffer();
        sb.append(diffHours + ":" + diffMinutes + ":" + diffSeconds);
        job.setTimeSpent(sb.toString());
    }

    /**
     * updates the percentage completed based on the number of records processed and no of records in process
     */
    private void updatePercentCompleted() {
        // String total = job.getNoOfRecords();
        String total = job.getTotalNoOfRecords();
        float totRec = Float.parseFloat(total == null ? "0" : total);
        if (totRec == 0.0) return;
        String noProcessed = job.getNoOfRecordsProcessed();
        if (StringUtils.isEmpty(noProcessed)) return;
        float perCompleted = (Float.valueOf(noProcessed) / Float.valueOf(total)) * 100;
        job.setPerCompleted(String.format("%.2f", perCompleted) + PERCENT);
    }

    /**
     * create and return the dirctory based on batch process type in the server file system
     */
    protected String getBatchProcessFilePath(String batchProceesType) {

        String batchProcessLocation = getOLEBatchProcessDataHelper().getBatchProcessFilePath(batchProceesType);
        return batchProcessLocation;
    }


    /**
     * Create the success record file in the batch process type directory  for batch delete
     * @param successRecordData
     * @throws Exception
     */
    protected void createBatchSuccessFile(String successRecordData) throws Exception {

        getOLEBatchProcessDataHelper().createBatchSuccessFile(successRecordData, processDef.getBatchProcessType(), job.getJobId() + "_SuccessRecord" + "_" + job.getUploadFileName(), job.getJobId());
    }


    /**
     * Create the failure report record file in the batch process type directory for batch delete
     * @param failureReportData
     * @throws Exception
     */
    protected void createBatchDeleteFailureReportFile(String failureReportData) throws Exception {

        getOLEBatchProcessDataHelper().createBatchDeleteFailureReportFile(failureReportData, processDef.getBatchProcessType(), job.getJobId() + "_FailureReport.txt", job.getJobId());
    }


    /**
     * Create the failure record file in the batch process type directory
     * @param failureRecordData
     * @throws Exception
     */
    protected void createBatchFailureFile(String failureRecordData) throws Exception {

        getOLEBatchProcessDataHelper().createBatchFailureFile(failureRecordData, processDef.getBatchProcessType(), job.getJobId() + "_FailureRecord" + "_" + job.getUploadFileName(), job.getJobId());
    }

    protected void createBatchErrorAttachmentFile(String failureRecordData) throws Exception {
        String uploadFileName = job.getUploadFileName();
        String errorFileName = null;
        String[] fileNames = uploadFileName.split(",");
        errorFileName = fileNames.length == 2 ? fileNames[0]:uploadFileName;
        if(errorFileName.endsWith(".mrc")){
            errorFileName = errorFileName.replace(".mrc",".txt");
        }
        else if(errorFileName.endsWith(".INV")){
            errorFileName = errorFileName.replace(".INV",".txt");
        }
        else if(errorFileName.endsWith(".edi")){
            errorFileName = errorFileName.replace(".edi",".txt");
        }
        getOLEBatchProcessDataHelper().createBatchFailureFile(failureRecordData, processDef.getBatchProcessType(), job.getJobId() + "_FailureRecord" + "_" + errorFileName, job.getJobId());
    }

    protected void createFile(String[] content, String fileName) throws Exception {
        getOLEBatchProcessDataHelper().createFile(content, processDef.getBatchProcessType(), job.getJobId() + fileName, job.getJobId());
    }

    /**
     * Delete the upload file in the batch process type dirctory
     */
    protected void deleteBatchFile() throws Exception {

        getOLEBatchProcessDataHelper().deleteBatchFile(processDef.getBatchProcessType(), job.getJobId() + OLEConstants.OLEBatchProcess.PROFILE_JOB + "_" + job.getUploadFileName(), job.getJobId());
    }

    /**
     * Get the upload file content from the batch process type dirctory by using job id an upload file name
     */
    protected String getBatchProcessFileContent() throws Exception {
        String fileContent = getOLEBatchProcessDataHelper().getBatchProcessFileContent(processDef.getBatchProcessType(), job.getJobId() + OLEConstants.OLEBatchProcess.PROFILE_JOB + "_" + job.getUploadFileName(), job.getJobId());
        return fileContent;
    }

    protected String getBatchProcessFileContent(String fileName) throws Exception {
        String fileContent = getOLEBatchProcessDataHelper().getBatchProcessFileContent(processDef.getBatchProcessType(), job.getJobId() + OLEConstants.OLEBatchProcess.PROFILE_JOB + "_" + fileName, job.getJobId());
        return fileContent;


    }

    /**
     * Delete the upload file in the batch process type dirctory
     */
    protected void deleteBatchFile(String fileName) throws Exception {

        getOLEBatchProcessDataHelper().deleteBatchFile(processDef.getBatchProcessType(), job.getJobId() + OLEConstants.OLEBatchProcess.PROFILE_JOB + "_" + fileName, job.getJobId());
    }

    /**
     * Create the failure record file in the batch process type directory
     * @param failureRecordData
     * @throws Exception
     */
    protected void createBatchFailureFile(String failureRecordData, String fileName) throws Exception {

        getOLEBatchProcessDataHelper().createBatchFailureFile(failureRecordData, processDef.getBatchProcessType(), job.getJobId() + "_FailureRecord" + "_" + fileName, job.getJobId());
    }

    public OLEBatchProcessDefinitionDocument getProcessDef() {
        return processDef;
    }

    public void setProcessDef(OLEBatchProcessDefinitionDocument processDef) {
        this.processDef = processDef;
    }
}
