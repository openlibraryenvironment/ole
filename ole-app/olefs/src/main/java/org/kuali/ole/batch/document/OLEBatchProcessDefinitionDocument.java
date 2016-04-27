package org.kuali.ole.batch.document;

import org.kuali.ole.batch.bo.*;
import org.kuali.ole.batch.form.OLEBatchProcessDefinitionForm;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 7/11/13
 * Time: 6:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessDefinitionDocument extends PersistableBusinessObjectBase {

    private String batchProcessId;
    private String documentNumber;
    private String batchProcessName;
    private String batchProcessProfileName;
    private String batchProcessType;
    private String batchProcessKRMSProfile;
    private String batchProcessProfileId;
    private OLEBatchProcessProfileBo oleBatchProcessProfileBo;
    private MultipartFile ingestedFile;
    private List<OLEBatchProcessIngestFile> oleBatchProcessIngestFiles = new ArrayList<OLEBatchProcessIngestFile>();
    private String outputFormat="marcxml";
    private String outputFile;
    private boolean scheduleCheck;
    private boolean oneTimeCheck;
    //private Date oneTimeDate;
    //private String oneTimeStartTime;
    private boolean recurringCheck;
    private boolean scheduleTypeDailyCheck;
    private boolean scheduleTypeWeeklyCheck;
    private boolean scheduleTypeMonthlyCheck;
    //private String startTime;
    private OLEBatchProcessDefinitionForm form;
    private String sourceFilePath;
    private String sourceDirectoryPath;
    private String sourceFileMask;
    private String fileFormatId;
    private String destinationDirectoryPath;
    private boolean scheduleFlag;
    private String message;
    private int chunkSize;
    private String scheduleType;
    private String exportType;
    private boolean linkToJob;
    private String enteredCronExp;
    private String user;
    private String cronOrSchedule;
    private String emailIds;
    private String uploadFileName;
    private boolean rescheduleFlag = true;
    private boolean fileFlag=false;
    private boolean continueImportFlag=false;
    private int maxRecordsInFile;
    private int maxNumberOfThreads;
    private boolean runNowFlag = false;
    private String runNowOrSchedule;
    private boolean afterSubmitFlag;
    private String loadIdFromFile = "false";
    private String processNameHidden;
    private String processTypeHidden;

    public String getProcessNameHidden() {
        return processNameHidden;
    }

    public void setProcessNameHidden(String processNameHidden) {
        this.processNameHidden = processNameHidden;
    }

    public String getProcessTypeHidden() {
        return processTypeHidden;
    }

    public void setProcessTypeHidden(String processTypeHidden) {
        this.processTypeHidden = processTypeHidden;
    }

    public boolean isAfterSubmitFlag() {
        return afterSubmitFlag;
    }

    public void setAfterSubmitFlag(boolean afterSubmitFlag) {
        this.afterSubmitFlag = afterSubmitFlag;
    }

    public String getRunNowOrSchedule() {
        return runNowOrSchedule;
    }

    public void setRunNowOrSchedule(String runNowOrSchedule) {
        this.runNowOrSchedule = runNowOrSchedule;
    }

    public int getMaxNumberOfThreads() {
        return maxNumberOfThreads;
    }

    public void setMaxNumberOfThreads(int maxNumberOfThreads) {
        this.maxNumberOfThreads = maxNumberOfThreads;
    }

    public int getMaxRecordsInFile() {
        return maxRecordsInFile;
    }

    public void setMaxRecordsInFile(int maxRecordsInFile) {
        this.maxRecordsInFile = maxRecordsInFile;
    }

    public boolean isContinueImportFlag() {
        return continueImportFlag;
    }

    public void setContinueImportFlag(boolean continueImportFlag) {
        this.continueImportFlag = continueImportFlag;
    }


    public boolean isFileFlag() {
        return fileFlag;
    }

    public void setFileFlag(boolean fileFlag) {
        this.fileFlag = fileFlag;
    }
    private boolean addUnmatchedPatron;

    public boolean isRescheduleFlag() {
        return rescheduleFlag;
    }

    public void setRescheduleFlag(boolean rescheduleFlag) {
        this.rescheduleFlag = rescheduleFlag;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public String getCronOrSchedule() {
        return cronOrSchedule;
    }

    public void setCronOrSchedule(String cronOrSchedule) {
        this.cronOrSchedule = cronOrSchedule;
    }

    public String getExportType() {
        return exportType;
    }

    public void setExportType(String exportType) {
        this.exportType = exportType;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }
    /*private String scheduleType;
    private List<String> weekDays;
    private String weekNumber;
    private String weekDay;
    private String monthNumber;*/

    private MultipartFile marcFile;
    private MultipartFile ediFile;
    private String inputFormat;
    private MultipartFile serialRecordDocumentFile;
    private MultipartFile serialRecordHistoryFile;
    private MultipartFile serialRecordTypeFile;
    private String serialRecordDocumentFileName;
    private String serialRecordHistoryFileName;
    private String serialRecordTypeFileName;
    private String marcFileName;
    private String ediFileName;
    private String oneTimeOrRecurring;
    private MultipartFile fundRecordDocumentFile;
    private String fundRecordDocumentFileName;
    private MultipartFile fundAcctlnDocumentFile;
    private String fundAcclnFileName;
    private OLEBatchProcessScheduleBo oleBatchProcessScheduleBo;
    private List<OLEBatchProcessScheduleBo> oleBatchProcessScheduleBoList;
    //private OLEBatchProcessJobDetailsBo oleBatchProcessJobDetailsBo;
    private List<OLEBatchProcessJobDetailsBo> oleBatchProcessJobDetailsBoList;
    private OLEBatchProcessProfileBo  batchProcessProfileBo;

    private Boolean marcOnly;

    public Boolean getMarcOnly() {
        return marcOnly;
    }

    public void setMarcOnly(Boolean marcOnly) {
        this.marcOnly = marcOnly;
    }

    public String getMarcFileName() {
        return marcFileName;
    }

    public void setMarcFileName(String marcFileName) {
        this.marcFileName = marcFileName;
    }

    public String getEdiFileName() {
        return ediFileName;
    }

    public void setEdiFileName(String ediFileName) {
        this.ediFileName = ediFileName;
    }

    public MultipartFile getIngestedFile() {
        return ingestedFile;
    }

    public void setIngestedFile(MultipartFile ingestedFile) {
        this.ingestedFile = ingestedFile;
    }

    public String getSourceFilePath() {
        return sourceFilePath;
    }

    public void setSourceFilePath(String sourceFilePath) {
        this.sourceFilePath = sourceFilePath;
    }

    public String getSourceDirectoryPath() {
        return sourceDirectoryPath;
    }

    public void setSourceDirectoryPath(String sourceDirectoryPath) {
        this.sourceDirectoryPath = sourceDirectoryPath;
    }

    public String getSourceFileMask() {
        return sourceFileMask;
    }

    public void setSourceFileMask(String sourceFileMask) {
        this.sourceFileMask = sourceFileMask;
    }

    public String getFileFormatId() {
        return fileFormatId;
    }

    public void setFileFormatId(String fileFormatId) {
        this.fileFormatId = fileFormatId;
    }

    public String getDestinationDirectoryPath() {
        return destinationDirectoryPath;
    }

    public void setDestinationDirectoryPath(String destinationDirectoryPath) {
        this.destinationDirectoryPath = destinationDirectoryPath;
    }

    public String getBatchProcessId() {
        return batchProcessId;
    }

    public void setBatchProcessId(String batchProcessId) {
        this.batchProcessId = batchProcessId;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getBatchProcessProfileId() {
        return batchProcessProfileId;
    }

    public void setBatchProcessProfileId(String batchProcessProfileId) {
        this.batchProcessProfileId = batchProcessProfileId;
    }

    public OLEBatchProcessDefinitionForm getForm() {
        return form;
    }

    public void setForm(OLEBatchProcessDefinitionForm form) {
        this.form = form;
    }

    public String getBatchProcessName() {
        return batchProcessName;
    }

    public void setBatchProcessName(String batchProcessName) {
        this.batchProcessName = batchProcessName;
    }

    public String getBatchProcessProfileName() {
        return batchProcessProfileName;
    }

    public void setBatchProcessProfileName(String batchProcessProfileName) {
        this.batchProcessProfileName = batchProcessProfileName;
    }

    public String getBatchProcessType() {
        return batchProcessType;
    }

    public void setBatchProcessType(String batchProcessType) {
        this.batchProcessType = batchProcessType;
    }

    public String getBatchProcessKRMSProfile() {
        return batchProcessKRMSProfile;
    }

    public void setBatchProcessKRMSProfile(String batchProcessKRMSProfile) {
        this.batchProcessKRMSProfile = batchProcessKRMSProfile;
    }

    public OLEBatchProcessProfileBo getOleBatchProcessProfileBo() {
        return oleBatchProcessProfileBo;
    }

    public void setOleBatchProcessProfileBo(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        this.oleBatchProcessProfileBo = oleBatchProcessProfileBo;
    }

    public List<OLEBatchProcessIngestFile> getOleBatchProcessIngestFiles() {
        return oleBatchProcessIngestFiles;
    }

    public void setOleBatchProcessIngestFiles(List<OLEBatchProcessIngestFile> oleBatchProcessIngestFiles) {
        this.oleBatchProcessIngestFiles = oleBatchProcessIngestFiles;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public boolean isScheduleCheck() {
        return scheduleCheck;
    }

    public void setScheduleCheck(boolean scheduleCheck) {
        this.scheduleCheck = scheduleCheck;
    }

    public boolean isOneTimeCheck() {
        return oneTimeCheck;
    }

    public void setOneTimeCheck(boolean oneTimeCheck) {
        this.oneTimeCheck = oneTimeCheck;
    }

    public boolean isRecurringCheck() {
        return recurringCheck;
    }

    public void setRecurringCheck(boolean recurringCheck) {
        this.recurringCheck = recurringCheck;
    }

    public boolean isScheduleTypeDailyCheck() {
        return scheduleTypeDailyCheck;
    }

    public void setScheduleTypeDailyCheck(boolean scheduleTypeDailyCheck) {
        this.scheduleTypeDailyCheck = scheduleTypeDailyCheck;
    }

    public boolean isScheduleTypeWeeklyCheck() {
        return scheduleTypeWeeklyCheck;
    }

    public void setScheduleTypeWeeklyCheck(boolean scheduleTypeWeeklyCheck) {
        this.scheduleTypeWeeklyCheck = scheduleTypeWeeklyCheck;
    }

    public boolean isScheduleTypeMonthlyCheck() {
        return scheduleTypeMonthlyCheck;
    }

    public void setScheduleTypeMonthlyCheck(boolean scheduleTypeMonthlyCheck) {
        this.scheduleTypeMonthlyCheck = scheduleTypeMonthlyCheck;
    }

    public boolean isScheduleFlag() {
        return scheduleFlag;
    }

    public void setScheduleFlag(boolean scheduleFlag) {
        this.scheduleFlag = scheduleFlag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MultipartFile getMarcFile() {
        return marcFile;
    }

    public void setMarcFile(MultipartFile marcFile) {
        this.marcFile = marcFile;
    }

    public MultipartFile getEdiFile() {
        return ediFile;
    }

    public void setEdiFile(MultipartFile ediFile) {
        this.ediFile = ediFile;
    }

    public OLEBatchProcessScheduleBo getOleBatchProcessScheduleBo() {
        return oleBatchProcessScheduleBo;
    }

    public void setOleBatchProcessScheduleBo(OLEBatchProcessScheduleBo oleBatchProcessScheduleBo) {
        this.oleBatchProcessScheduleBo = oleBatchProcessScheduleBo;
    }

    public List<OLEBatchProcessScheduleBo> getOleBatchProcessScheduleBoList() {
        return oleBatchProcessScheduleBoList;
    }

    public void setOleBatchProcessScheduleBoList(List<OLEBatchProcessScheduleBo> oleBatchProcessScheduleBoList) {
        this.oleBatchProcessScheduleBoList = oleBatchProcessScheduleBoList;
    }

    public OLEBatchProcessProfileBo getBatchProcessProfileBo() {
        return batchProcessProfileBo;
    }

    public void setBatchProcessProfileBo(OLEBatchProcessProfileBo batchProcessProfileBo) {
        this.batchProcessProfileBo = batchProcessProfileBo;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getOneTimeOrRecurring() {
        return oneTimeOrRecurring;
    }

    public void setOneTimeOrRecurring(String oneTimeOrRecurring) {
        this.oneTimeOrRecurring = oneTimeOrRecurring;
    }

    public List<OLEBatchProcessJobDetailsBo> getOleBatchProcessJobDetailsBoList() {
        return oleBatchProcessJobDetailsBoList;
    }

    public void setOleBatchProcessJobDetailsBoList(List<OLEBatchProcessJobDetailsBo> oleBatchProcessJobDetailsBoList) {
        this.oleBatchProcessJobDetailsBoList = oleBatchProcessJobDetailsBoList;
    }

    public boolean isLinkToJob() {
        return linkToJob;
    }

    public void setLinkToJob(boolean linkToJob) {
        this.linkToJob = linkToJob;
    }

    public String getEnteredCronExp() {
        return enteredCronExp;
    }

    public void setEnteredCronExp(String enteredCronExp) {
        this.enteredCronExp = enteredCronExp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmailIds() {
        return emailIds;
    }

    public void setEmailIds(String emailIds) {
        this.emailIds = emailIds;
    }

    public MultipartFile getSerialRecordDocumentFile() {
        return serialRecordDocumentFile;
    }

    public void setSerialRecordDocumentFile(MultipartFile serialRecordDocumentFile) {
        this.serialRecordDocumentFile = serialRecordDocumentFile;
    }

    public MultipartFile getSerialRecordHistoryFile() {
        return serialRecordHistoryFile;
    }

    public void setSerialRecordHistoryFile(MultipartFile serialRecordHistoryFile) {
        this.serialRecordHistoryFile = serialRecordHistoryFile;
    }

    public MultipartFile getSerialRecordTypeFile() {
        return serialRecordTypeFile;
    }

    public void setSerialRecordTypeFile(MultipartFile serialRecordTypeFile) {
        this.serialRecordTypeFile = serialRecordTypeFile;
    }

    public String getInputFormat() {
        return inputFormat;
    }

    public void setInputFormat(String inputFormat) {
        this.inputFormat = inputFormat;
    }

    public String getSerialRecordDocumentFileName() {
        return serialRecordDocumentFileName;
    }

    public void setSerialRecordDocumentFileName(String serialRecordDocumentFileName) {
        this.serialRecordDocumentFileName = serialRecordDocumentFileName;
    }

    public String getSerialRecordHistoryFileName() {
        return serialRecordHistoryFileName;
    }

    public void setSerialRecordHistoryFileName(String serialRecordHistoryFileName) {
        this.serialRecordHistoryFileName = serialRecordHistoryFileName;
    }

    public String getSerialRecordTypeFileName() {
        return serialRecordTypeFileName;
    }

    public void setSerialRecordTypeFileName(String serialRecordTypeFileName) {
        this.serialRecordTypeFileName = serialRecordTypeFileName;
    }

    public MultipartFile getFundRecordDocumentFile() {
        return fundRecordDocumentFile;
    }

    public void setFundRecordDocumentFile(MultipartFile fundRecordDocumentFile) {
        this.fundRecordDocumentFile = fundRecordDocumentFile;
    }

    public String getFundRecordDocumentFileName() {
        return fundRecordDocumentFileName;
    }

    public void setFundRecordDocumentFileName(String fundRecordDocumentFileName) {
        this.fundRecordDocumentFileName = fundRecordDocumentFileName;
    }

    public MultipartFile getFundAcctlnDocumentFile() {
        return fundAcctlnDocumentFile;
    }

    public void setFundAcctlnDocumentFile(MultipartFile fundAcctlnDocumentFile) {
        this.fundAcctlnDocumentFile = fundAcctlnDocumentFile;
    }

    public String getFundAcclnFileName() {
        return fundAcclnFileName;
    }

    public void setFundAcclnFileName(String fundAcclnFileName) {
        this.fundAcclnFileName = fundAcclnFileName;
    }

    public boolean isAddUnmatchedPatron() {
        return addUnmatchedPatron;
    }

    public void setAddUnmatchedPatron(boolean addUnmatchedPatron) {
        this.addUnmatchedPatron = addUnmatchedPatron;
    }

    public boolean isRunNowFlag() {
        return runNowFlag;
    }

    public void setRunNowFlag(boolean runNowFlag) {
        this.runNowFlag = runNowFlag;
    }

    public String getLoadIdFromFile() {
        if (batchProcessProfileId != null) {
            Map ids = new HashMap();
            ids.put("batchProcessProfileId", batchProcessProfileId);
            OLEBatchProcessProfileFilterCriteriaBo oleBatchProcessProfileFilterCriteriaBo = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEBatchProcessProfileFilterCriteriaBo.class, ids);
            if (oleBatchProcessProfileFilterCriteriaBo != null) {
                if (("LocalId_display").equalsIgnoreCase(oleBatchProcessProfileFilterCriteriaBo.getFilterFieldName())) {
                    loadIdFromFile = "true";
                }
            } else {
                loadIdFromFile = "false";
            }
        }
        return loadIdFromFile;
    }

    public void setLoadIdFromFile(String loadIdFromFile) {
        this.loadIdFromFile = loadIdFromFile;
    }
}
