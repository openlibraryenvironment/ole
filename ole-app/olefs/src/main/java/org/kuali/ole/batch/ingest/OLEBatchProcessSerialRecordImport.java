package org.kuali.ole.batch.ingest;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.impl.AbstractBatchProcess;
import org.kuali.ole.select.bo.*;
import org.kuali.ole.service.OLESerialReceivingImportProcessor;
import org.kuali.rice.coreservice.impl.parameter.ParameterBo;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 2/6/14
 * Time: 12:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessSerialRecordImport extends AbstractBatchProcess {
    private static final Logger LOG = LoggerFactory.getLogger(OLEBatchProcessClaimReport.class);
    private OLESerialReceivingImportProcessor oleSerialReceivingConverterService;
    private String fileContent = null;
    private String documentFileContent = null;
    private String typeFileContent = null;
    private String historyFileContent = null;
    private String documentFileName =null;
    private String historyFileName =null;
    private String typeFileName =null;

    @Override
    protected void prepareForRead() throws Exception {
        oleSerialReceivingConverterService = new OLESerialReceivingImportProcessor();
        if (processDef.getOutputFormat().equalsIgnoreCase("xml")) {
            fileContent = getBatchProcessFileContent(job.getUploadFileName());
        } else {
            String[] fileNames = job.getUploadFileName().split(",");
          //  if(fileNames.length == 3) {
                for(String fileName : fileNames) {
                    if (fileName.endsWith(getParameter(OLEConstants.OLEBatchProcess.SERIAL_RECORD_NAME)+".csv")) {
                        documentFileName = getBatchProcessFilePath(processDef.getBatchProcessType()) + job.getJobId() +FileSystems.getDefault().getSeparator()+ job.getJobId() + OLEConstants.OLEBatchProcess.PROFILE_JOB + "_" + fileName;
                                documentFileContent = getBatchProcessFileContent(fileName);
                    } else if (fileName.endsWith(getParameter(OLEConstants.OLEBatchProcess.SERIAL_HISTORY_NAME)+".csv")) {
                        historyFileName = getBatchProcessFilePath(processDef.getBatchProcessType()) +job.getJobId()+FileSystems.getDefault().getSeparator() +job.getJobId() + OLEConstants.OLEBatchProcess.PROFILE_JOB + "_" + fileName;
                        historyFileContent = getBatchProcessFileContent(fileName);
                    } else if (fileName.endsWith(getParameter(OLEConstants.OLEBatchProcess.SERIAL_TYPE_NAME)+".csv")) {
                        typeFileName = getBatchProcessFilePath(processDef.getBatchProcessType()) + job.getJobId()+FileSystems.getDefault().getSeparator()+job.getJobId() + OLEConstants.OLEBatchProcess.PROFILE_JOB + "_" + fileName;
                        typeFileContent = getBatchProcessFileContent(fileName);
                    }
               // }
            }/* else if (fileNames.length == 2) {
                if (fileNames[0].endsWith("recv_rec.csv")) {
                    documentFileContent = getBatchProcessFileContent(fileNames[0]);
                } else if (fileNames[0].endsWith("recv_his_rec.csv")) {
                    historyFileContent = getBatchProcessFileContent(fileNames[0]);
                } else if (fileNames[0].endsWith("recv_rec_type.csv")) {
                    typeFileContent = getBatchProcessFileContent(fileNames[0]);
                }

                if (fileNames[1].endsWith("recv_rec.csv")) {
                    documentFileContent = getBatchProcessFileContent(fileNames[1]);
                } else if (fileNames[1].endsWith("recv_his_rec.csv")) {
                    historyFileContent = getBatchProcessFileContent(fileNames[1]);
                } else if (fileNames[1].endsWith("recv_rec_type.csv")) {
                    typeFileContent = getBatchProcessFileContent(fileNames[1]);
                }
            } else {
                if (fileNames[0].endsWith("recv_rec.csv")) {
                    documentFileContent = getBatchProcessFileContent(fileNames[0]);
                } else if (fileNames[0].endsWith("recv_his_rec.csv")) {
                    historyFileContent = getBatchProcessFileContent(fileNames[0]);
                } else if (fileNames[0].endsWith("recv_rec_type.csv")) {
                    typeFileContent = getBatchProcessFileContent(fileNames[0]);
                }
            }*/
        }
        job.setNoOfSuccessRecords("0");
        job.setNoOfFailureRecords("0");
        job.setNoOfRecordsProcessed("0");
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void prepareForWrite() throws Exception {
        LOG.debug("Inside prepareForWrite for the file : "+job.getUploadFileName());
        OLESerialReceivingRecordSummary oleSerialReceivingRecordSummary= null;
        if(job.getUploadFileName().contains(".xml")){
            oleSerialReceivingRecordSummary=oleSerialReceivingConverterService.createOLESerialReceivingDocumentFromXml(fileContent);
            deleteBatchFile();
            job.setTotalNoOfRecords(String.valueOf(oleSerialReceivingRecordSummary.getTotalRecordSize()));
            job.setNoOfRecordsProcessed(String.valueOf(oleSerialReceivingRecordSummary.getSuccessRecordSize()));
            job.setNoOfSuccessRecords(String.valueOf(oleSerialReceivingRecordSummary.getSuccessRecordSize()));
            job.setNoOfFailureRecords(String.valueOf(oleSerialReceivingRecordSummary.getFailureRecordSize()));
            job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
            if (oleSerialReceivingRecordSummary.getFailureDocuments()!=null){
                OLESerialReceivingDocuments oleSerialReceivingDocuments=new OLESerialReceivingDocuments();
                oleSerialReceivingDocuments.setOleSerialReceivingDocuments(oleSerialReceivingRecordSummary.getFailureDocuments());
                String content=oleSerialReceivingConverterService.getSerialReceivingXMLContent(oleSerialReceivingDocuments);
                createBatchFailureFile(content);
            }
        } else {
            oleSerialReceivingRecordSummary = oleSerialReceivingConverterService.createOLESerialReceivingDocumentFromCsv(documentFileName,typeFileName,historyFileName);
            String[] fileNames = job.getUploadFileName().split(",");
            for(String fileName : fileNames){
                deleteBatchFile(fileName);
            }
            int totalSize=oleSerialReceivingRecordSummary.getDocSuccessCount()+oleSerialReceivingRecordSummary.getDocFailureCount()+oleSerialReceivingRecordSummary.getHstrySucceesCount()+oleSerialReceivingRecordSummary.getHstryFailureCount()+oleSerialReceivingRecordSummary.getTypeSuccessCount()+oleSerialReceivingRecordSummary.getTypeFailureCount();
            job.setTotalNoOfRecords(String.valueOf(totalSize));
            job.setNoOfRecordsProcessed(String.valueOf(totalSize));
            job.setNoOfSuccessRecords(String.valueOf(oleSerialReceivingRecordSummary.getDocSuccessCount()));
            job.setNoOfFailureRecords(String.valueOf(oleSerialReceivingRecordSummary.getDocFailureCount()));
            job.setHstrySucceesCount(String.valueOf(oleSerialReceivingRecordSummary.getHstrySucceesCount()));
            job.setHstryFailureCount(String.valueOf(oleSerialReceivingRecordSummary.getHstryFailureCount()));
            job.setTypeSuccessCount(String.valueOf(oleSerialReceivingRecordSummary.getTypeSuccessCount()));
            job.setTypeFailureCount(String.valueOf(oleSerialReceivingRecordSummary.getTypeFailureCount()));
            job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
            if (oleSerialReceivingRecordSummary.getDocFailureList()!=null && oleSerialReceivingRecordSummary.getDocFailureList().size()>0){
                OLESerialReceivingFailureDocuments oleSerialReceivingFailureDocuments = createOLESerialReceivingFailureDocuments(oleSerialReceivingRecordSummary.getDocFailureList());
/*                OLESerialReceivingDocuments oleSerialReceivingDocuments=new OLESerialReceivingDocuments();
                oleSerialReceivingDocuments.setOleSerialReceivingDocuments(oleSerialReceivingRecordSummary.getDocFailureList());
                String content=oleSerialReceivingConverterService.getSerialReceivingXMLContent(oleSerialReceivingDocuments);*/
                String content = oleSerialReceivingConverterService.getSerialReceivingFailureDocumentsXmlContent(oleSerialReceivingFailureDocuments);
                createBatchFailureFile(content, StringUtils.substringAfter(documentFileName, getBatchProcessFilePath(processDef.getBatchProcessType()) + job.getJobId() + FileSystems.getDefault().getSeparator() + job.getJobId() + OLEConstants.OLEBatchProcess.PROFILE_JOB + "_").replace("csv","xml"));
            }
            if (oleSerialReceivingRecordSummary.getHstryFailureList()!=null && oleSerialReceivingRecordSummary.getHstryFailureList().size()>0){
                OLESerialReceivingFailureHistories oleSerialReceivingFailureHistories = createOleSerialReceivingFailureHistories(oleSerialReceivingRecordSummary.getHstryFailureList());
/*                OLESerialReceivingDocument oleSerialReceivingDocument=new OLESerialReceivingDocument();
                oleSerialReceivingDocument.setOleSerialReceivingHistoryList(oleSerialReceivingRecordSummary.getHstryFailureList());
                String content=oleSerialReceivingConverterService.getSerialReceivingDocumentXMLContent(oleSerialReceivingDocument);*/
                String content =  oleSerialReceivingConverterService.getSerialReceivingFailureHistoriesXmlContent(oleSerialReceivingFailureHistories);
                createBatchFailureFile(content,StringUtils.substringAfter(historyFileName, getBatchProcessFilePath(processDef.getBatchProcessType()) + job.getJobId() + FileSystems.getDefault().getSeparator() + job.getJobId() + OLEConstants.OLEBatchProcess.PROFILE_JOB + "_").replace("csv","xml"));
            }
            if (oleSerialReceivingRecordSummary.getTypeFailureList()!=null && oleSerialReceivingRecordSummary.getTypeFailureList().size()>0){
                OLESerialReceivingFailureTypes oleSerialReceivingFailureTypes = createOleSerialReceivingFailureTypes(oleSerialReceivingRecordSummary.getTypeFailureList());
/*                OLESerialReceivingDocument oleSerialReceivingDocument=new OLESerialReceivingDocument();
                oleSerialReceivingDocument.setOleSerialReceivingTypes(oleSerialReceivingRecordSummary.getTypeFailureList());
                String content=oleSerialReceivingConverterService.getSerialReceivingDocumentXMLContent(oleSerialReceivingDocument);*/
                String content =  oleSerialReceivingConverterService.getSerialReceivingFailureTypesXmlContent(oleSerialReceivingFailureTypes);
                createBatchFailureFile(content,StringUtils.substringAfter(typeFileName, getBatchProcessFilePath(processDef.getBatchProcessType()) + job.getJobId() + FileSystems.getDefault().getSeparator() + job.getJobId() + OLEConstants.OLEBatchProcess.PROFILE_JOB + "_").replace("csv","xml"));
            }
        }

        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void getNextBatch() throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void processBatch() throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This method is used to get the file name from the system parameter
     * @param name
     * @return  parameter
     */
    private String getParameter(String name) {
        LOG.info("Parameter Name : "+ name);
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
        LOG.info("Parameter Value : " + parameter);
        return parameter;
    }

    /**
     * This method is used to create OLESerialReceivingFailureDocuments from list of OLESerialReceivingDocument
     * @param oleSerialReceivingDocuments
     * @return  OLESerialReceivingFailureDocuments
     */
    private OLESerialReceivingFailureDocuments createOLESerialReceivingFailureDocuments(List<OLESerialReceivingDocument> oleSerialReceivingDocuments){
        LOG.info("Inside createOLESerialReceivingFailureDocuments for creating  OLESerialReceivingFailureDocuments for the " +oleSerialReceivingDocuments.size() + "of OLESerialReceivingDocument");
        OLESerialReceivingFailureDocuments oleSerialReceivingFailureDocuments = new OLESerialReceivingFailureDocuments();
        List<OLESerialReceivingFailureDocument> oleSerialReceivingFailureDocumentList = new ArrayList<OLESerialReceivingFailureDocument>();
          OLESerialReceivingFailureDocument oleSerialReceivingFailureDocument ;
        for(OLESerialReceivingDocument oleSerialReceivingDocument : oleSerialReceivingDocuments){
            oleSerialReceivingFailureDocument = new OLESerialReceivingFailureDocument();
            oleSerialReceivingFailureDocument.setOleSerialReceivingDocument(oleSerialReceivingDocument);
            if(oleSerialReceivingDocument.isRecordAlreadyExist()){
                oleSerialReceivingFailureDocument.setErrorMessage(OLEConstants.SERIAL_DOC_ALRDY_EXIST);
            }else if(!oleSerialReceivingDocument.isValidPo()){
                oleSerialReceivingFailureDocument.setErrorMessage(OLEConstants.INVLD_PO);
            }else if(!oleSerialReceivingDocument.isValidBibAndInstance()){
                oleSerialReceivingFailureDocument.setErrorMessage(OLEConstants.INVLD_BIB_INS);
            }else if(!oleSerialReceivingDocument.isAvailableSerialReceiptLocation()){
                oleSerialReceivingFailureDocument.setErrorMessage(OLEConstants.NO_SRL_RCPT_LOCN);
            }else if(!oleSerialReceivingDocument.isValidSubscriptionStatus()){
                oleSerialReceivingFailureDocument.setErrorMessage(OLEConstants.INVLD_SUBS_STAT);
            }else if(!oleSerialReceivingDocument.isValidRecordType()){
                oleSerialReceivingFailureDocument.setErrorMessage(OLEConstants.INVALID_RECORD_TYPE);
            }else if(!oleSerialReceivingDocument.isValidChildRecordType()){
                oleSerialReceivingFailureDocument.setErrorMessage(OLEConstants.LINKED_TYP_FAILED);
            }else if(!oleSerialReceivingDocument.isValidChildHistoryRecordType()){
                oleSerialReceivingFailureDocument.setErrorMessage(OLEConstants.LINKED_HSTRY_FAILED);
            }
            oleSerialReceivingFailureDocumentList.add(oleSerialReceivingFailureDocument);
        }
        oleSerialReceivingFailureDocuments.setOleSerialReceivingFailureDocuments(oleSerialReceivingFailureDocumentList);
        if(oleSerialReceivingFailureDocumentList!=null && oleSerialReceivingFailureDocumentList.size()>0){
            LOG.info(oleSerialReceivingFailureDocumentList.size()+"number of OLESerialReceivingFailureDocument created ");
        } else{
            LOG.info("No OLESerialReceivingFailureDocument created ");
        }
       return oleSerialReceivingFailureDocuments;
    }

    /**
     *   This method is used to create OLESerialReceivingFailureHistories from list of OLESerialReceivingHistory record
     * @param oleSerialReceivingHistories
     * @return OLESerialReceivingFailureHistories
     */
    private OLESerialReceivingFailureHistories createOleSerialReceivingFailureHistories(List<OLESerialReceivingHistory> oleSerialReceivingHistories){
        LOG.info("Inside createOleSerialReceivingFailureHistories for creating  OLESerialReceivingFailureHistories for the " +oleSerialReceivingHistories.size() + "of OLESerialReceivingHistory");

        OLESerialReceivingFailureHistories oleSerialReceivingFailureHistories = new OLESerialReceivingFailureHistories();
        List<OLESerialReceivingFailureHistory> oleSerialReceivingFailureHistoryList =  new ArrayList<OLESerialReceivingFailureHistory>();
        OLESerialReceivingFailureHistory oleSerialReceivingFailureHistory ;
        for(OLESerialReceivingHistory oleSerialReceivingHistory : oleSerialReceivingHistories){
            oleSerialReceivingFailureHistory = new OLESerialReceivingFailureHistory();
            oleSerialReceivingFailureHistory.setOleSerialReceivingHistory(oleSerialReceivingHistory);
            if(!oleSerialReceivingHistory.isDocumentExist()){
                oleSerialReceivingFailureHistory.setErrorMessage(OLEConstants.LINK_DOC_MISS);
            }else if(!oleSerialReceivingHistory.isValidRecordType()){
                oleSerialReceivingFailureHistory.setErrorMessage(OLEConstants.INVALID_RECORD_TYPE);
            }/*else if(oleSerialReceivingHistory.isValidRecordType() && !oleSerialReceivingDocument.isValidChildRecordType()){
                oleSerialReceivingFailureHistory.setErrorMessage("One or more of the corresponding Serial Receiving Document's assosiated Serial Receiving Record Type Fails");
            }else if(oleSerialReceivingHistory.isValidRecordType() && !oleSerialReceivingDocument.isValidChildHistoryRecordType()){
                oleSerialReceivingFailureHistory.setErrorMessage("One or more of the corresponding Serial Receiving Document's assosiated Serial Receiving Record History Fails");
            }*/else{
                oleSerialReceivingFailureHistory.setErrorMessage(OLEConstants.LINK_DOC_FAILURE);
            }
            oleSerialReceivingFailureHistoryList.add(oleSerialReceivingFailureHistory);
        }
        oleSerialReceivingFailureHistories.setOleSerialReceivingFailureHistories(oleSerialReceivingFailureHistoryList);
        if(oleSerialReceivingFailureHistoryList!=null && oleSerialReceivingFailureHistoryList.size()>0){
            LOG.info(oleSerialReceivingFailureHistoryList.size()+"number of OLESerialReceivingFailureHistory created ");
        } else{
            LOG.info("No OLESerialReceivingFailureHistory created ");
        }
        return oleSerialReceivingFailureHistories;
    }

    /**
     * This method is used to create OLESerialReceivingFailureTypes from list of OLESerialReceivingType
     * @param oleSerialReceivingTypes
     * @return OLESerialReceivingFailureTypes
     */
    private OLESerialReceivingFailureTypes createOleSerialReceivingFailureTypes(List<OLESerialReceivingType> oleSerialReceivingTypes){
        LOG.info("Inside createOleSerialReceivingFailureTypes for creating  OLESerialReceivingFailureTypes for the " +oleSerialReceivingTypes.size() + "of OLESerialReceivingType");
        OLESerialReceivingFailureTypes oleSerialReceivingFailureTypes = new OLESerialReceivingFailureTypes();
        List<OLESerialReceivingFailureType> oleSerialReceivingFailureTypeList =  new ArrayList<OLESerialReceivingFailureType>();
        OLESerialReceivingFailureType oleSerialReceivingFailureType ;
        for(OLESerialReceivingType oleSerialReceivingType : oleSerialReceivingTypes){
            oleSerialReceivingFailureType = new OLESerialReceivingFailureType();
            oleSerialReceivingFailureType.setOleSerialReceivingType(oleSerialReceivingType);
            if(!oleSerialReceivingType.isDocumentExist()){
                oleSerialReceivingFailureType.setErrorMessage(OLEConstants.LINK_DOC_MISS);
            }else if(!oleSerialReceivingType.isValidRecordType()){
                 oleSerialReceivingFailureType.setErrorMessage(OLEConstants.INVALID_RECORD_TYPE);
            }/*else if(oleSerialReceivingType.isValidRecordType() && !oleSerialReceivingDocument.isValidChildRecordType()){
                oleSerialReceivingFailureType.setErrorMessage("One or more of the corresponding Serial Receiving Document's assosiated Serial Receiving Record Type Fails");
            }else if(oleSerialReceivingType.isValidRecordType() && !oleSerialReceivingDocument.isValidChildHistoryRecordType()){
                oleSerialReceivingFailureType.setErrorMessage("One or more of the corresponding Serial Receiving Document's assosiated Serial Receiving Record History Fails");
            }*/else{
                oleSerialReceivingFailureType.setErrorMessage(OLEConstants.LINK_DOC_FAILURE);
            }
            oleSerialReceivingFailureTypeList.add(oleSerialReceivingFailureType);
        }
        oleSerialReceivingFailureTypes.setOleSerialReceivingFailureTypes(oleSerialReceivingFailureTypeList);
        if(oleSerialReceivingFailureTypeList!=null && oleSerialReceivingFailureTypeList.size()>0){
            LOG.info(oleSerialReceivingFailureTypeList.size()+"number of OLESerialReceivingFailureType created ");
        } else{
            LOG.info("No OLESerialReceivingFailureType created ");
        }
        return oleSerialReceivingFailureTypes;
    }
}
