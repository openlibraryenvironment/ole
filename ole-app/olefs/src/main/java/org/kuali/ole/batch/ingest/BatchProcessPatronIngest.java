package org.kuali.ole.batch.ingest;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.impl.AbstractBatchProcess;
import org.kuali.ole.ingest.OlePatronXMLSchemaValidator;
import org.kuali.ole.deliver.bo.OlePatronIngestSummaryRecord;
import org.kuali.ole.service.OlePatronConverterService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.util.GlobalVariables;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 7/15/13
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class BatchProcessPatronIngest extends AbstractBatchProcess {
    private static final Logger LOG = Logger.getLogger(BatchProcessPatronIngest.class);
    private OlePatronConverterService olePatronRecordService;
    private String principalName = GlobalVariables.getUserSession().getPrincipalName();
    private OlePatronIngestSummaryRecord olePatronIngestSummaryRecord;
    private OlePatronXMLSchemaValidator olePatronXMLSchemaValidator;
    private String fileContent = null;

    @Override
    public void prepareForRead() throws Exception {
        olePatronRecordService = GlobalResourceLoader.getService(OLEConstants.PATRON_CONVERTER_SERVICE);
        olePatronIngestSummaryRecord = new OlePatronIngestSummaryRecord();
        olePatronXMLSchemaValidator = new OlePatronXMLSchemaValidator();
        fileContent = getBatchProcessFileContent();
        job.setNoOfSuccessRecords("0");
        job.setNoOfFailureRecords("0");
        job.setNoOfRecordsProcessed("0");
    }


    @Override
    public void prepareForWrite() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        //boolean schemaFlag = olePatronXMLSchemaValidator.validateContentsAgainstSchema(inputStream);
        Map validateResultMap = olePatronXMLSchemaValidator.validateContentsAgainstSchema(inputStream);
        boolean schemaFlag = (boolean)validateResultMap.get(OLEConstants.OlePatron.PATRON_XML_ISVALID);
        if (!schemaFlag) {
            job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
            deleteBatchFile();
            createBatchFailureFile(fileContent);
            throw new Exception("patron ingest schema Failed");
        }

        olePatronRecordService.persistPatronFromFileContent(fileContent, processDef.isAddUnmatchedPatron(), job.getUploadFileName(), olePatronIngestSummaryRecord, "", principalName);
        String failedRecords = olePatronIngestSummaryRecord.getFailureRecords();
        if (!"".equals(failedRecords) && failedRecords != null) {
            createBatchFailureFile(failedRecords);
        }
        deleteBatchFile();
        int totalCount = olePatronIngestSummaryRecord.getPatronCreateCount() + olePatronIngestSummaryRecord.getPatronUpdateCount() + olePatronIngestSummaryRecord.getPatronFailedCount();
        int faiCount = olePatronIngestSummaryRecord.getPatronFailedCount();
        job.setTotalNoOfRecords(totalCount + "");
        job.setNoOfRecordsProcessed(totalCount + "");
        job.setNoOfSuccessRecords(totalCount - faiCount + "");
        job.setNoOfFailureRecords(faiCount + "");
        job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
        //job.setStatusDesc(OLEConstants.PATRON_RECORD_SUCCESS);
    }

    @Override
    public void getNextBatch() {

    }

    @Override
    public void processBatch() {

    }
}
