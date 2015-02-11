package org.kuali.ole.batch.ingest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.OrderImportHelperBo;
import org.kuali.ole.batch.impl.AbstractBatchProcess;
import org.kuali.ole.ingest.IngestProcessor;
import org.kuali.ole.ingest.pojo.IngestRecord;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 7/15/13
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class BatchProcessOrderIngest extends AbstractBatchProcess {
    private static final Logger LOG = Logger.getLogger(BatchProcessOrderIngest.class);
    private String marcFileContent;
    private String ediFileContent;
    private String mrcFileName;
    private String ediFileName;
    private IngestProcessor ingestProcessor;

    @Override
    public void prepareForRead() throws Exception {
        String[] fileNames = job.getUploadFileName().split(",");
        if(fileNames.length == 2) {
            mrcFileName = fileNames[0];
            ediFileName = fileNames[1];
            ediFileContent=getBatchProcessFileContent(ediFileName);
        }
        else {
            mrcFileName = job.getUploadFileName();
        }
        marcFileContent=getBatchProcessFileContent(mrcFileName);

    }

    @Override
    public void prepareForWrite() throws Exception {

        if(!StringUtils.isBlank(processDef.getUser())){
            GlobalVariables.setUserSession(new UserSession(processDef.getUser()));
        }
        String agendaName = OLEConstants.PROFILE_AGENDA_NM;
        String agendaDescription = processDef.getBatchProcessName();
        int recordCount=0;
        if (null != agendaName && !agendaName.equals("")) {
            IngestRecord ingestRecord = new IngestRecord();
            ingestRecord.setOriginalMarcFileName(mrcFileName);
            ingestRecord.setMarcFileContent(marcFileContent);
            ingestRecord.setAgendaName(agendaName);
            ingestRecord.setAgendaDescription(agendaDescription);
            ingestRecord.setByPassPreProcessing(false);
            //ingestRecord.setUser(principalName);
            ingestRecord.setOriginalEdiFileName(ediFileName);
            ingestRecord.setEdiFileContent(ediFileContent);
            ingestRecord.setByPassPreProcessing(isPreProcessingRequired(mrcFileName, ediFileName));
            try {
                boolean failure_flag=true;
                recordCount = getIngestProcessor().start(ingestRecord, failure_flag, processDef, job);
                if (recordCount!=0) {
                    job.setStatusDesc(OLEConstants.BATCH_ORDER_IMPORT_SUCCESS);
                } else if (ingestRecord.isUpdate()) {
                    job.setStatusDesc(OLEConstants.BATCH_ORDER_IMPORT_SUCCESS);
                } else {
                    job.setStatusDesc(OLEConstants.BATCH_ORDER_IMPORT_FAILURE);
                }
            } catch (Exception orderRecordException) {
                LOG.error("Failed to perform Staff Upload:", orderRecordException);
            }
        } else {
            job.setStatusDesc(OLEConstants.ERROR_AGENDA_NAME);
        }
        job.setTotalNoOfRecords(recordCount+"");
        job.setNoOfRecordsProcessed(recordCount+"");
        job.setNoOfSuccessRecords("N/A");
        job.setNoOfFailureRecords("N/A");
        job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
        checkForFailureReport();
    }

    @Override
    public void getNextBatch() {

    }

    @Override
    public void processBatch() {

    }

    private IngestProcessor getIngestProcessor() {
        if(ingestProcessor == null)
         ingestProcessor = new IngestProcessor();
        return ingestProcessor;
    }

    private boolean isPreProcessingRequired(String marcFile, String ediFile) {
        if(ediFile != null) {
            return (marcFile.contains(".mrc") && ediFile.contains(".edi") ? true : false);
        }
        else {
            return (marcFile.contains(".mrc")? true : false);
        }
    }


    private void checkForFailureReport() throws Exception {
        OrderImportHelperBo orderImportHelperBo = job.getOrderImportHelperBo();
        Integer createBibCount = orderImportHelperBo.getCreateBibCount();
        Integer updateBibCount = orderImportHelperBo.getUpdateBibCount();
        Integer createHoldingsCount = orderImportHelperBo.getCreateHoldingsCount();
        Integer updateHoldingsCount = orderImportHelperBo.getUpdateHoldingsCount();
        Integer orderImportSuccessCount = orderImportHelperBo.getOrderImportSuccessCount();
        Integer orderImportFailureCount = orderImportHelperBo.getOrderImportFailureCount();
        List<String> reasonForFailure = orderImportHelperBo.getFailureReason();
        job.setCreateBibCount(createBibCount);
        job.setUpdateBibCount(updateBibCount);
        job.setCreateHoldingsCount(createHoldingsCount);
        job.setUpdateHoldingsCount(updateHoldingsCount);
        job.setOrderImportSuccessCount(orderImportSuccessCount);
        job.setOrderImportFailureCount(orderImportFailureCount);
        StringBuffer failureBuffer = new StringBuffer();
        if(reasonForFailure != null && reasonForFailure.size() > 0){
            for(int failureCount = 0;failureCount < reasonForFailure.size();failureCount++){
                failureBuffer.append(reasonForFailure.get(failureCount) + "\n");
            }
            createBatchErrorAttachmentFile(failureBuffer.toString());
        }
    }

}
