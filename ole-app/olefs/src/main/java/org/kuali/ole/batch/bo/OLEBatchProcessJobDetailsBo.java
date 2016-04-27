package org.kuali.ole.batch.bo;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.document.OLEBatchProcessDefinitionDocument;
import org.kuali.ole.batch.form.OLEBatchProcessJobDetailsForm;
import org.kuali.ole.batch.helper.OLEBatchProcessDataHelper;
import org.kuali.rice.coreservice.impl.parameter.ParameterBo;
import org.kuali.rice.krad.document.TransactionalDocumentBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.io.File;
import java.nio.file.FileSystems;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: krishnamohanv
 * Date: 7/11/13
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessJobDetailsBo extends TransactionalDocumentBase {


    private String sNo;
    private String jobId;
    private String jobIdNum;
    private String jobName;
    private String userName;
    private Timestamp startTime = new Timestamp(new Date().getTime()); //default value
    private String timeSpent="0:0:0";
    private String recordsExported;
    private String perCompleted = "0.0%"; //default value
    private String status;
    private String batchProfileName;
    private String uploadFileName;
    private Timestamp createTime = new Timestamp(new Date().getTime()); //default value
    private Timestamp endTime;
    private String statusDesc;
    private String oleBatchPrcsScheduleId;
    private String batchProcessId;
    private String batchProcessType;
    private String totalNoOfRecords="0";
    private String noOfRecordsProcessed="0";
    private String noOfSuccessRecords="0";
    private String noOfFailureRecords="0";
    private String noOfDeletedRecords="0";
    private boolean failureAttachmentFlag;
    private boolean errorAttachmentFlag;

    private boolean fileCreatedWithOutLinkFlag;
    private boolean fileCreatedWithMoreThanOneLinkFlag;

    private boolean failureCSVAttachmentFlag;
    private String bibErrorPath;
    private String batchDeletePath;

    private String serialCSVErrorPath;
    private String hstrySucceesCount="0";
    private String hstryFailureCount="0";
    private String typeSuccessCount="0";
    private String typeFailureCount="0";
    private boolean documentFlag;
    private boolean historyFlag;
    private boolean typeFlag;
    private boolean inputCSVFormatFlag;
    private Integer orderImportSuccessCount;
    private Integer orderImportFailureCount;
    private Integer createBibCount;
    private Integer updateBibCount;
    private Integer createHoldingsCount;
    private Integer updateHoldingsCount;

    private String fundCodeCSVErrorPath;
    private String fndAcclnSuccessCount="0";
    private String fndAcclnFailureCount="0";

    private Integer noOfEinstanceAdded;
    private Integer noOfEinstanceDeleted;
    private Integer noOfEinstanceCreatedWithOutLink;
    private Integer noOfbibsHaveMoreThanOneEinstance;
    private boolean bibsDeletedForExportFlag;
    private int noOfEHoldingsCreatedWithOutPlatfom =0;
    private int noOfEHoldingsCreatedWithOutEResource =0;

    private OLEBatchProcessScheduleBo oleBatchProcessScheduleBo;
    private List<OLEBatchProcessScheduleBo> oleBatchProcessScheduleBoList;

    private OLEBatchProcessDefinitionDocument oLEBatchProcessDefinitionDocument;
    public OLEBatchProcessJobDetailsForm form;

    private OrderImportHelperBo orderImportHelperBo = new OrderImportHelperBo();

    private static final Logger LOG = Logger.getLogger(OLEBatchProcessJobDetailsBo.class);


    public boolean isFailureAttachmentFlag() {
        File file = new File(getBatchProcessFilePath(this.getBatchProcessType(), this.getJobId()) + this.getJobId() + "_FailureRecord" + "_" + this.getUploadFileName());
        if (file.exists()) {
            return true;
        } else if ((this.getBatchProcessType() != null && !this.getBatchProcessType().equals(OLEConstants.OLEBatchProcess.ORDER_RECORD_IMPORT)) && isFileCreatedWithMoreThanOneLinkFlag()) {
            return true;
        } else if ((this.getBatchProcessType() != null && !this.getBatchProcessType().equals(OLEConstants.OLEBatchProcess.ORDER_RECORD_IMPORT)) && isFileCreatedWithOutLinkFlag()) {
            return true;
        }
        return false;
    }

    public boolean isErrorAttachmentFlag() {
        String uploadFileName = this.getUploadFileName();
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
        File file = new File(getBatchProcessFilePath(this.getBatchProcessType() , this.getJobId())+this.getJobId()+ "_FailureRecord" + "_" + errorFileName);
        if(file.exists())
            return true;
        return false;
    }

    public void setErrorAttachmentFlag(boolean errorAttachmentFlag) {
        this.errorAttachmentFlag= errorAttachmentFlag;
    }

    public boolean isFileCreatedWithOutLinkFlag() {
        File file = new File(getBatchProcessFilePath(this.getBatchProcessType() , this.getJobId())+ this.getJobId() + OLEConstants.OLEBatchProcess.RECORDS_CREATED_WITHOUT_LINK  + this.getUploadFileName());
        if(file.exists())
            return true;
        return false;
    }

    public void setFileCreatedWithOutLinkFlag(boolean fileCreatedWithOutLinkFlag) {
        this.fileCreatedWithOutLinkFlag = fileCreatedWithOutLinkFlag;
    }

    public boolean isFileCreatedWithMoreThanOneLinkFlag() {
        File file = new File(getBatchProcessFilePath(this.getBatchProcessType() , this.getJobId()) +  this.getJobId()  + OLEConstants.OLEBatchProcess.RECORDS_CREATED_WITH_MORE_THAN_ONE_LINK + this.getUploadFileName());
        if(file.exists())
            return true;
        return false;
    }

    public void setFileCreatedWithMoreThanOneLinkFlag(boolean fileCreatedWithMoreThanOneLinkFlag) {
        this.fileCreatedWithMoreThanOneLinkFlag = fileCreatedWithMoreThanOneLinkFlag;
    }

    public boolean isBibsDeletedForExportFlag() {
        File file = new File(getBatchProcessFilePath(this.getBatchProcessType(), this.getJobId()) + this.getJobId() + OLEConstants.OLEBatchProcess.DELETED_BIB_IDS_FILE_NAME);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    public void setBibsDeletedForExportFlag(boolean bibsDeletedForExportFlag) {
        this.bibsDeletedForExportFlag = bibsDeletedForExportFlag;
    }

    public Integer getNoOfEinstanceAdded() {
        return noOfEinstanceAdded;
    }

    public void setNoOfEinstanceAdded(Integer noOfEinstanceAdded) {
        this.noOfEinstanceAdded = noOfEinstanceAdded;
    }

    public Integer getNoOfEinstanceDeleted() {
        return noOfEinstanceDeleted;
    }

    public void setNoOfEinstanceDeleted(Integer noOfEinstanceDeleted) {
        this.noOfEinstanceDeleted = noOfEinstanceDeleted;
    }

    public Integer getNoOfEinstanceCreatedWithOutLink() {
        return noOfEinstanceCreatedWithOutLink;
    }

    public void setNoOfEinstanceCreatedWithOutLink(Integer noOfEinstanceCreatedWithOutLink) {
        this.noOfEinstanceCreatedWithOutLink = noOfEinstanceCreatedWithOutLink;
    }

    public Integer getNoOfbibsHaveMoreThanOneEinstance() {
        return noOfbibsHaveMoreThanOneEinstance;
    }

    public void setNoOfbibsHaveMoreThanOneEinstance(Integer noOfbibsHaveMoreThanOneEinstance) {
        this.noOfbibsHaveMoreThanOneEinstance = noOfbibsHaveMoreThanOneEinstance;
    }

    public void setFailureAttachmentFlag(boolean failureAttachmentFlag) {
        this.failureAttachmentFlag = failureAttachmentFlag;
    }

    public int getNoOfEHoldingsCreatedWithOutEResource() {
        return noOfEHoldingsCreatedWithOutEResource;
    }

    public void setNoOfEHoldingsCreatedWithOutEResource(int noOfEHoldingsCreatedWithOutEResource) {
        this.noOfEHoldingsCreatedWithOutEResource = noOfEHoldingsCreatedWithOutEResource;
    }

    public int getNoOfEHoldingsCreatedWithOutPlatfom() {
        return noOfEHoldingsCreatedWithOutPlatfom;
    }

    public void setNoOfEHoldingsCreatedWithOutPlatfom(int noOfEHoldingsCreatedWithOutPlatfom) {
        this.noOfEHoldingsCreatedWithOutPlatfom = noOfEHoldingsCreatedWithOutPlatfom;
    }

    public boolean isFailureCSVAttachmentFlag() {
        String[] fileNames=this.getUploadFileName().split(",");
        for (String fileName:fileNames){
            if (fileName.contains(getParameter(OLEConstants.OLEBatchProcess.SERIAL_RECORD_NAME))){
                File file = new File(getBatchProcessFilePath(this.getBatchProcessType(), jobId) + this.getJobId() + "_FailureRecord" + "_" + fileName.replace("csv","xml"));
                if(file.exists())
                    return true;
            }
            else if (fileName.contains(getParameter(OLEConstants.OLEBatchProcess.SERIAL_HISTORY_NAME))){
                File file = new File(getBatchProcessFilePath(this.getBatchProcessType(),jobId) + this.getJobId() + "_FailureRecord" + "_" + fileName.replace("csv","xml"));
                if(file.exists())
                    return true;
            } else if (fileName.contains(getParameter(OLEConstants.OLEBatchProcess.SERIAL_TYPE_NAME))){
                File file = new File(getBatchProcessFilePath(this.getBatchProcessType(),jobId) + this.getJobId() + "_FailureRecord" + "_" + fileName.replace("csv","xml"));
                if(file.exists())
                    return true;
            }
            else if (fileName.contains(getParameter(OLEConstants.OLEBatchProcess.FUND_RECORD_NAME))){
                File file = new File(getBatchProcessFilePath(this.getBatchProcessType(),jobId) + this.getJobId() + "_FailureRecord" + "_" + fileName.replace("csv","xml"));
                if(file.exists()) {
                    return true;
                }
            }
            else if (fileName.contains(getParameter(OLEConstants.OLEBatchProcess.FUND_ACCOUNTING_LINE_RECORD_NAME))){
                File file = new File(getBatchProcessFilePath(this.getBatchProcessType(),jobId) + this.getJobId() + "_FailureRecord" + "_" + fileName.replace("csv","xml"));
                if(file.exists()) {
                    return true;
                }
            }

        }
        return false;
    }

    public void setFailureCSVAttachmentFlag(boolean failureCSVAttachmentFlag) {
        this.failureCSVAttachmentFlag = failureCSVAttachmentFlag;
    }

    public void getJobDisplay(){
        LOG.info("get Report.....");
    }


    public String getTotalNoOfRecords() {
        return totalNoOfRecords;
    }

    public void setTotalNoOfRecords(String totalNoOfRecords) {
        this.totalNoOfRecords = totalNoOfRecords;
    }

    public String getNoOfRecordsProcessed() {
        return noOfRecordsProcessed;
    }

    public void setNoOfRecordsProcessed(String noOfRecordsProcessed) {
        this.noOfRecordsProcessed = noOfRecordsProcessed;
    }

    public OLEBatchProcessJobDetailsForm getForm() {
        return form;
    }

    public void setForm(OLEBatchProcessJobDetailsForm form) {
        this.form = form;
    }

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRecordsExported() {
        return recordsExported;
    }

    public void setRecordsExported(String recordsExported) {
        this.recordsExported = recordsExported;
    }

    public String getPerCompleted() {
        return perCompleted;
    }

    public void setPerCompleted(String perCompleted) {
        this.perCompleted = perCompleted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBatchProfileName() {
        return batchProfileName;
    }

    public void setBatchProfileName(String batchProfileName) {
        this.batchProfileName = batchProfileName;
    }


    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }


    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }


    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getOleBatchPrcsScheduleId() {
        return oleBatchPrcsScheduleId;
    }

    public void setOleBatchPrcsScheduleId(String oleBatchPrcsScheduleId) {
        this.oleBatchPrcsScheduleId = oleBatchPrcsScheduleId;
    }

    public String getBatchProcessId() {
        return batchProcessId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public void setBatchProcessId(String batchProcessId) {
        this.batchProcessId = batchProcessId;
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

    public String getBatchProcessType() {
        return batchProcessType;
    }

    public void setBatchProcessType(String batchProcessType) {
        this.batchProcessType = batchProcessType;
    }

    public OLEBatchProcessDefinitionDocument getOLEBatchProcessDefinitionDocument() {
        return oLEBatchProcessDefinitionDocument;
    }

    public void setOLEBatchProcessDefinitionDocument(OLEBatchProcessDefinitionDocument oLEBatchProcessDefinitionDocument) {
        this.oLEBatchProcessDefinitionDocument = oLEBatchProcessDefinitionDocument;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getNoOfSuccessRecords() {
        return noOfSuccessRecords;
    }

    public void setNoOfSuccessRecords(String noOfSuccessRecords) {
        this.noOfSuccessRecords = noOfSuccessRecords;
    }

    public String getNoOfFailureRecords() {
        return noOfFailureRecords;
    }

    public void setNoOfFailureRecords(String noOfFailureRecords) {
        this.noOfFailureRecords = noOfFailureRecords;
    }

    public String getNoOfDeletedRecords() {
        return noOfDeletedRecords;
    }

    public void setNoOfDeletedRecords(String noOfDeletedRecords) {
        this.noOfDeletedRecords = noOfDeletedRecords;
    }

    private String getBatchProcessFilePath(String batchProceesType) {
        String batchProcessLocation =  OLEBatchProcessDataHelper.getInstance().getBatchProcessFilePath(batchProceesType);
        return batchProcessLocation;
    }
    private String getBatchProcessFilePath(String batchProceesType,String jobId) {
        String batchProcessLocation =  OLEBatchProcessDataHelper.getInstance().getBatchProcessFilePath(batchProceesType,jobId);
        return batchProcessLocation;
    }

    public String getBibErrorPath() {
        return bibErrorPath;
    }

    public void setBibErrorPath(String bibErrorPath) {
        this.bibErrorPath = bibErrorPath;
    }

    public String getBatchDeletePath() {
        return batchDeletePath;
    }

    public void setBatchDeletePath(String batchDeletePath) {
        this.batchDeletePath = batchDeletePath;
    }

    public void setJobIdNum(String jobIdNum) {
        this.jobIdNum = jobIdNum;
    }

    public String getJobIdNum() {
        return String.format("%08d", Integer.parseInt(jobId)) ;
    }

    public String getHstrySucceesCount() {
        return hstrySucceesCount;
    }

    public void setHstrySucceesCount(String hstrySucceesCount) {
        this.hstrySucceesCount = hstrySucceesCount;
    }

    public String getHstryFailureCount() {
        return hstryFailureCount;
    }

    public void setHstryFailureCount(String hstryFailureCount) {
        this.hstryFailureCount = hstryFailureCount;
    }

    public String getTypeSuccessCount() {
        return typeSuccessCount;
    }

    public void setTypeSuccessCount(String typeSuccessCount) {
        this.typeSuccessCount = typeSuccessCount;
    }

    public String getTypeFailureCount() {
        return typeFailureCount;
    }

    public void setTypeFailureCount(String typeFailureCount) {
        this.typeFailureCount = typeFailureCount;
    }

    public boolean isDocumentFlag() {
        if (uploadFileName.contains(getParameter(OLEConstants.OLEBatchProcess.SERIAL_RECORD_NAME)+".csv")){
            return true;
        }
        return false;
    }

    public void setDocumentFlag(boolean documentFlag) {
        this.documentFlag = documentFlag;
    }

    public boolean isHistoryFlag() {
        if (uploadFileName.contains(getParameter(OLEConstants.OLEBatchProcess.SERIAL_HISTORY_NAME)+".csv")){
            return true;
        }
        return false;
    }

    public void setHistoryFlag(boolean historyFlag) {
        this.historyFlag = historyFlag;
    }

    public boolean isTypeFlag() {
        if (uploadFileName.contains(getParameter(OLEConstants.OLEBatchProcess.SERIAL_TYPE_NAME)+".csv")){
            return true;
        }
        return false;
    }

    public void setTypeFlag(boolean typeFlag) {
        this.typeFlag = typeFlag;
    }
    private String getParameter(String name) {
        String parameter = "";
        try {
            Map<String, String> criteriaMap = new HashMap<String, String>();
            criteriaMap.put("namespaceCode", OLEConstants.SYS_NMSPC);
            criteriaMap.put("componentCode", OLEConstants.BATCH_CMPNT);
            criteriaMap.put("name", name);
            List<ParameterBo> parametersList = (List<ParameterBo>) KRADServiceLocator.getBusinessObjectService().findMatching(ParameterBo.class, criteriaMap);
            for (ParameterBo parameterBo : parametersList) {
                parameter = parameterBo.getValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parameter;
    }

    public String getFundCodeCSVErrorPath() {
        return fundCodeCSVErrorPath;
    }

    public void setFundCodeCSVErrorPath(String fundCodeCSVErrorPath) {
        this.fundCodeCSVErrorPath = fundCodeCSVErrorPath;
    }

    public String getFndAcclnSuccessCount() {
        return fndAcclnSuccessCount;
    }

    public void setFndAcclnSuccessCount(String fndAcclnSuccessCount) {
        this.fndAcclnSuccessCount = fndAcclnSuccessCount;
    }

    public String getFndAcclnFailureCount() {
        return fndAcclnFailureCount;
    }

    public void setFndAcclnFailureCount(String fndAcclnFailureCount) {
        this.fndAcclnFailureCount = fndAcclnFailureCount;
    }

    public String getSerialCSVErrorPath() {
        return serialCSVErrorPath;
    }

    public void setSerialCSVErrorPath(String serialCSVErrorPath) {
        this.serialCSVErrorPath = serialCSVErrorPath;
    }

    public boolean isInputCSVFormatFlag() {
        if (uploadFileName.contains(".csv")) {
            return true;
        }
        return false;
    }

    public void setInputCSVFormatFlag(boolean inputCSVFormatFlag) {
        this.inputCSVFormatFlag = inputCSVFormatFlag;
    }

    public Integer getOrderImportSuccessCount() {
        return orderImportSuccessCount;
    }

    public void setOrderImportSuccessCount(Integer orderImportSuccessCount) {
        this.orderImportSuccessCount = orderImportSuccessCount;
    }

    public Integer getOrderImportFailureCount() {
        return orderImportFailureCount;
    }

    public void setOrderImportFailureCount(Integer orderImportFailureCount) {
        this.orderImportFailureCount = orderImportFailureCount;
    }

    public Integer getCreateBibCount() {
        return createBibCount;
    }

    public void setCreateBibCount(Integer createBibCount) {
        this.createBibCount = createBibCount;
    }

    public Integer getUpdateBibCount() {
        return updateBibCount;
    }

    public void setUpdateBibCount(Integer updateBibCount) {
        this.updateBibCount = updateBibCount;
    }

    public Integer getCreateHoldingsCount() {
        return createHoldingsCount;
    }

    public void setCreateHoldingsCount(Integer createHoldingsCount) {
        this.createHoldingsCount = createHoldingsCount;
    }

    public OrderImportHelperBo getOrderImportHelperBo() {
        return orderImportHelperBo;
    }

    public void setOrderImportHelperBo(OrderImportHelperBo orderImportHelperBo) {
        this.orderImportHelperBo = orderImportHelperBo;
    }

    public Integer getUpdateHoldingsCount() {
        return updateHoldingsCount;
    }

    public void setUpdateHoldingsCount(Integer updateHoldingsCount) {
        this.updateHoldingsCount = updateHoldingsCount;
    }

    public void setJobstatistics(OLEBatchBibImportStatistics bibImportStatistics) {
        setNoOfRecordsProcessed(Integer.parseInt(noOfRecordsProcessed) + bibImportStatistics.getTotalCount() + "");
        setNoOfSuccessRecords(bibImportStatistics.getSuccessRecord() + "");
        setNoOfFailureRecords((bibImportStatistics.getMismatchRecordList().size()) + "");
        setNoOfEinstanceAdded(bibImportStatistics.getNoOfEinstanceAdded());
        setNoOfEinstanceDeleted(bibImportStatistics.getNoOfEinstanceDeleted());
        setNoOfEinstanceCreatedWithOutLink(bibImportStatistics.getNoOfEinstanceCreatedWithOutLink());
        setNoOfbibsHaveMoreThanOneEinstance(bibImportStatistics.getNoOfbibsHaveMoreThanOneEinstance());
        setNoOfEHoldingsCreatedWithOutPlatfom(bibImportStatistics.getNoOfEHoldingsCreatedWithOutPlatfom());
        setNoOfEHoldingsCreatedWithOutEResource(bibImportStatistics.getNoOfEHoldingsCreatedWithOutEResource());

    }
    
    
    public void setIntailJob(OLEBatchBibImportStatistics bibImportStatistics){
       setNoOfSuccessRecords("0");
       setNoOfFailureRecords("0");
       setNoOfRecordsProcessed("0");
       setNoOfEinstanceAdded(0);
       setNoOfEinstanceDeleted(0);
       setNoOfEinstanceCreatedWithOutLink(0);
       setNoOfbibsHaveMoreThanOneEinstance(0);
       setTotalNoOfRecords(bibImportStatistics.getBibMarcRecordList().size()+"");
        setNoOfEHoldingsCreatedWithOutPlatfom(0);
        setNoOfEHoldingsCreatedWithOutEResource(0);
    }

}
