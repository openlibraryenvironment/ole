package org.kuali.ole.batch.ingest;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.impl.AbstractBatchProcess;
import org.kuali.ole.ingest.OleLocationXMLSchemaValidator;
import org.kuali.ole.service.OleLocationConverterService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class BatchProcessLocationIngest extends AbstractBatchProcess {
    private static final Logger LOG = Logger.getLogger(BatchProcessLocationIngest.class);
    OleLocationConverterService oleLocationRecordService;
    OleLocationXMLSchemaValidator oleLocationXMLSchemaValidator;
    private String fileContent;
    private String failedRecords;
    private Integer totalCount = 0;
    private Integer successCount = 0;
    private Integer failureCount = 0;

    @Override
    public void prepareForRead() throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
        oleLocationRecordService = GlobalResourceLoader.getService("oleLocationConverterService");
        oleLocationXMLSchemaValidator = new OleLocationXMLSchemaValidator();
        fileContent = getBatchProcessFileContent();
        job.setNoOfSuccessRecords("0");
        job.setNoOfFailureRecords("0");
        job.setNoOfRecordsProcessed("0");
        job.setTotalNoOfRecords("0");
    }

    @Override
    public void prepareForWrite() throws Exception {
        try {
            InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
            boolean schemaFlag = oleLocationXMLSchemaValidator.validateContentsAgainstSchema(inputStream);
            if (schemaFlag) {
                String summary = oleLocationRecordService.persistLocationFromFileContent(fileContent, job.getUploadFileName());
                totalCount = oleLocationRecordService.getSuccessCount() + oleLocationRecordService.getFailureCount();
                successCount = oleLocationRecordService.getSuccessCount();
                failureCount = oleLocationRecordService.getFailureCount();
                job.setTotalNoOfRecords(totalCount.toString());
                job.setNoOfRecordsProcessed(totalCount.toString());
                job.setNoOfSuccessRecords(successCount.toString());
                job.setNoOfFailureRecords(failureCount.toString());
                job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
                //job.setStatusDesc(OLEConstants.LOCATION_RECORD_SUCCESS);
                processDef.setMessage(OLEConstants.LOCATION_RECORD_SUCCESS + " " + summary);
                deleteBatchFile();
                if (failureCount > 0) {
                    failedRecords = oleLocationRecordService.getIngestFailureRecord();
                    if (!"".equals(failedRecords) && failedRecords != null) {
                        createBatchFailureFile(failedRecords);
                    }
                }
            } else {
                job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
                job.setStatusDesc(OLEConstants.LOCATION_RECORD_INVALID_SCHEMA);
                deleteBatchFile();
                createBatchFailureFile(fileContent);
            }

        } catch (Exception locationIngestException) {
            job.setStatusDesc(OLEConstants.LOCATION_RECORD_FAILURE);
            LOG.error("Failed to upload location.", locationIngestException);
            throw locationIngestException;
        }
    }

    @Override
    public void getNextBatch() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void processBatch() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
