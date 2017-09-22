package org.kuali.ole.batch.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.document.OLEBatchProcessDefinitionDocument;
import org.kuali.ole.batch.export.BatchProcessExportData;
import org.kuali.ole.batch.helper.EInstanceMappingHelper;
import org.kuali.ole.batch.helper.InstanceMappingHelper;
import org.kuali.ole.batch.helper.OLEBatchProcessDataHelper;
import org.kuali.ole.batch.marc.OLEMarcReader;
import org.kuali.ole.batch.marc.OLEMarcXmlReader;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.common.document.BibTrees;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.util.BatchBibTreeDBUtil;
import org.kuali.ole.docstore.common.util.BatchExportStatistics;
import org.marc4j.MarcSplitStreamWriter;
import org.marc4j.marc.Record;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.util.*;

import static org.kuali.ole.OLEConstants.OLEBatchProcess.*;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 5/28/14
 * Time: 12:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class BatchExportFetch {


    private String fileName;
    private File filePath;
    private StringBuilder errBuilder = new StringBuilder();

    private BatchProcessExportData batchProcessExportData;
    private OLEBatchProcessProfileBo batchProcessProfileBo;
    private OLEBatchProcessDefinitionDocument processDef;
    private int errCnt = 0;

    private BatchBibTreeDBUtil bibTreesUtil = null;
    private int recordsToBeExportedToFile;
    private int batchSize;
    private int remainingRecords;
    private int recordsToBeExported;
    private Boolean isBibOnly = false;
    private List<BibMarcRecord> failureList = new ArrayList<>();
    private static final Logger LOG = Logger.getLogger(BatchExportFetch.class);

    public BatchExportFetch(BatchBibTreeDBUtil bibTreesUtil, int recordsToBeExportedToFile, String fileName, BatchProcessExportData batchProcessExportData, OLEBatchProcessDefinitionDocument processDef, Boolean isBibOnly) {
        this.fileName = fileName;
        this.batchProcessExportData = batchProcessExportData;
        this.batchProcessProfileBo = processDef.getBatchProcessProfileBo();
        this.processDef = processDef;
        this.bibTreesUtil = bibTreesUtil;
        this.recordsToBeExportedToFile = recordsToBeExportedToFile;
        this.isBibOnly = isBibOnly;
    }


    public Long call() throws Exception {
        BatchExportStatistics batchExportStatistics = new BatchExportStatistics();
        StopWatch timer = new StopWatch();
        Boolean isFirstBatch = true;
        Boolean isLastBatch = false;

        batchExportStatistics.setFileName(fileName);
        int exportedRecords = 0;
        batchSize = processDef.getChunkSize();
        timer.start();

        while (exportedRecords < recordsToBeExportedToFile) {
            remainingRecords = recordsToBeExportedToFile - exportedRecords;
            recordsToBeExported = Math.min(batchSize, remainingRecords);

            if (remainingRecords <= batchSize) {
                isLastBatch = true;
            }
            if (!batchProcessExportData.job.getStatus().equalsIgnoreCase(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED)
                    && !batchProcessExportData.job.getStatus().equalsIgnoreCase(OLEConstants.OLEBatchProcess.JOB_STATUS_CANCELLED)
                    && !batchProcessExportData.job.getStatus().equalsIgnoreCase(OLEConstants.OLEBatchProcess.JOB_STATUS_PAUSED)
                    && !batchProcessExportData.job.getStatus().equalsIgnoreCase(OLEConstants.OLEBatchProcess.JOB_STATUS_STOPPED)) {

                BibTrees bibTrees = bibTreesUtil.fetchNextBatch(recordsToBeExported, batchExportStatistics, isBibOnly);

                // Building Marc Records    and Creating Data Fileds
                List<BibMarcRecord> bibMarcRecords = buildBibMarcRecords(bibTrees.getBibTrees(), batchExportStatistics);

                int size = bibMarcRecords.size();
                batchProcessExportData.setTotalRecordsExported(size + batchProcessExportData.getTotalRecordsExported());
                // Converting Bib Marc records as MARCXML
                Object[] resultMap = processBibMarcRecord(bibMarcRecords, batchExportStatistics, isFirstBatch, isLastBatch);

                exportedRecords += recordsToBeExported;
                prepareForWrite(resultMap, bibMarcRecords, batchExportStatistics, isFirstBatch, isLastBatch);
                isFirstBatch = false;
            } else {
                break;
            }
        }
        timer.stop();
        batchExportStatistics.setTotalTimeTaken(timer.getTime());
        batchExportStatistics.printExportStatistics();

        batchProcessExportData.job.setNoOfRecordsProcessed(String.valueOf(batchProcessExportData.getTotalRecordsExported()));
        batchProcessExportData.job.setNoOfFailureRecords(String.valueOf(Integer.valueOf(batchProcessExportData.job.getNoOfFailureRecords()) + failureList.size()));
        batchProcessExportData.job.setNoOfSuccessRecords(String.valueOf(Integer.valueOf(batchProcessExportData.job.getNoOfRecordsProcessed()) - Integer.valueOf(batchProcessExportData.job.getNoOfFailureRecords())));
        return Long.valueOf(batchProcessExportData.job.getNoOfRecordsProcessed());
    }


    private void prepareForWrite(Object[] resultMap, List<BibMarcRecord> bibMarcRecords, BatchExportStatistics batchExportStatistics, Boolean isFirstBatch, Boolean isLastBatch) throws Exception {
        StopWatch timer = new StopWatch();
        timer.start();
        if (resultMap != null && !resultMap[0].equals("0")) {
            batchProcessExportData.getBibDocList().clear();
            batchProcessExportData.getBibDocList().addAll((List<String>) resultMap[1]);
            processBatch(bibMarcRecords, batchExportStatistics, isFirstBatch, isLastBatch);
            batchProcessExportData.updateJobProgress();
            if (resultMap[2] != null)
                batchProcessExportData.getErrBuilder().append(resultMap[2].toString());
            if (resultMap[3] != null)
                batchProcessExportData.setErrCnt(resultMap[3].toString());
        }
        timer.stop();
        batchExportStatistics.addTimeTakenForWritingRecords(timer.getTime());
    }


    private List<BibMarcRecord> buildBibMarcRecords(List<BibTree> bibTrees, BatchExportStatistics batchExportStatistics) {

        StopWatch timer = new StopWatch();
        timer.start();

        List<BibMarcRecord> bibMarcRecords = new ArrayList<>();

        for (BibTree bibTree : bibTrees) {
            BibMarcRecord bibMarcRecord = null;
            try {
                bibMarcRecord = buildBibMarcRecord(bibTree.getBib());

                if (!batchProcessProfileBo.getOleBatchProcessProfileMappingOptionsList().isEmpty()
                        && StringUtils.isNotEmpty(batchProcessProfileBo.getDataToExport()) && (batchProcessProfileBo.getDataToExport().equalsIgnoreCase(OLEBatchProcess.EXPORT_BIB_AND_INSTANCE) || batchProcessProfileBo.getDataToExport().equalsIgnoreCase(OLEBatchProcess.EXPORT_BIB_INSTANCE_AND_EINSTANCE))) {
                    try {
                        getInstanceDetails(bibMarcRecord, batchProcessProfileBo, errBuilder, bibTree);
                        LOG.debug("Instance data mapping completed");
                    } catch (Exception ex) {
                        LOG.error("Instance data mapping Error for Bib record id::" + bibMarcRecord.getRecordId(), ex);
                        buildError(errBuilder, ERR_BIB, bibMarcRecord.getRecordId(), "", "",
                                ERR_CAUSE, ex.getMessage(), TIME_STAMP, new java.util.Date().toString());
                    }
                }
                //Marc record rename
                if (!batchProcessProfileBo.getOleBatchProcessProfileRenameFieldsList().isEmpty()) {
                    try {
                        OLEBatchProcessDataHelper.getInstance().renameMarcFieldsSubFields(batchProcessProfileBo, bibMarcRecord);
                        LOG.debug("Rename of bib marc records completed");
                    } catch (Exception ex) {
                        LOG.error("Marc Record Rename error for Bib record id::" + bibMarcRecord.getRecordId(), ex);
                        buildError(errBuilder, ERR_BIB, bibMarcRecord.getRecordId(), ERR_CAUSE, ex.getMessage(), " ::At:: ", "renameMarcFieldsSubFields", TIME_STAMP, new Date().toString());
                        errCnt++;
                    }
                }
                //Marc record delete
                if (!batchProcessProfileBo.getOleBatchProcessProfileDeleteFieldsList().isEmpty()) {
                    try {
                        OLEBatchProcessDataHelper.getInstance().deleteFieldsSubfields(batchProcessProfileBo, bibMarcRecord);
                        LOG.debug("Deletion of bib marc records completed");
                    } catch (Exception ex) {
                        LOG.error("Marc record delete Error for Bib record id::" + bibMarcRecord.getRecordId(), ex);
                        buildError(errBuilder, ERR_BIB, bibMarcRecord.getRecordId(), ERR_CAUSE, ex.getMessage(), " ::At:: ", "deleteFieldsSubfields", TIME_STAMP, new Date().toString());
                        errCnt++;
                    }
                }
                bibMarcRecords.add(bibMarcRecord);
            } catch (Exception ex) {
                LOG.error("Error while Exporting bibs :: No of bibs processed while error occured :: " + bibMarcRecords.size(), ex);

                if (!bibMarcRecords.isEmpty()) {
                    LOG.error("Bib record where error occured: " + bibMarcRecords.get(bibMarcRecords.size() - 1).getRecordId(), ex);
                    buildError(errBuilder, ERR_BIB, bibMarcRecords.get(bibMarcRecords.size() - 1).getRecordId(), ERR_CAUSE, ex.getMessage(), " ::At:: ", "getBibliographicRecord-P", TIME_STAMP, new Date().toString());
                    errCnt++;
                }
            }


        }
        timer.stop();
        batchExportStatistics.addTimeTakenForBibMarcRecords(timer.getTime());
        return bibMarcRecords;
    }


    private Object[] processBibMarcRecord(List<BibMarcRecord> bibMarcRecords, BatchExportStatistics batchExportStatistics, Boolean firstBatch, Boolean lastBatch) {
        StopWatch timer = new StopWatch();
        timer.start();
        BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor(errBuilder);
        List<String> bibMarcRecordList = new ArrayList<String>();
        try {
            getResult(bibMarcRecordProcessor, bibMarcRecords, bibMarcRecordList, firstBatch, lastBatch);
        } catch (Exception ex) {
            LOG.error("Error while Exporting bibs :: No of bibs processed while error occured :: " + bibMarcRecords.size(), ex);
            buildError(errBuilder, ERR_CAUSE, "Error while getting bib data::" + ex.getMessage(), TIME_STAMP, new Date().toString());
        }
        timer.stop();
        batchExportStatistics.addTimeTakenForProcessing(timer.getTime());
        return new Object[]{String.valueOf(bibMarcRecordProcessor.getSuccessCnt()), bibMarcRecordList, errBuilder.toString(), String.valueOf((errCnt + bibMarcRecordProcessor.getErrCnt()))};
    }

    private void getResult(BibMarcRecordProcessor bibMarcRecordProcessor, List<BibMarcRecord> bibRecords, List<String> bibMarcRecordList, Boolean firstBatch, Boolean lastBatch) {
        String bibMarcRecord = null;
        if (processDef.getOutputFormat().equalsIgnoreCase(BatchProcessExportData.MARC)) {
            bibMarcRecord = bibMarcRecordProcessor.generateXML(bibRecords);
        } else {
            bibMarcRecord = bibMarcRecordProcessor.generateXML(bibRecords, firstBatch, lastBatch);
        }

        bibMarcRecordList.add(bibMarcRecord);
    }


    protected void processBatch(List<BibMarcRecord> bibMarcRecords, BatchExportStatistics batchExportStatistics, Boolean isFirstBatch, Boolean isLastBatch) throws Exception {
        int recordsSize = bibMarcRecords.size();
        batchProcessExportData.prepareForWrite(fileName);
        int currSuccessRec = 0;
        int currErrCnt = Integer.valueOf(batchProcessExportData.getErrCnt());
        if (processDef.getOutputFormat().equalsIgnoreCase(BatchProcessExportData.MARCXML)) {
            try {
                if (batchProcessExportData.getProcessedRec() > 0)
                    batchProcessExportData.setFileName(fileName);
                batchProcessExportData.writeFileToLocation(fileName);
                currSuccessRec = recordsSize;
            } catch (Exception e) {
                batchProcessExportData.job.setStatus(JOB_STATUS_STOPPED);
                batchProcessExportData.job.setStatusDesc("Error while writing to marcxml file::" + fileName + BatchProcessExportData.EXT_MARCXML);
                currSuccessRec = 0;
                currErrCnt += recordsSize - currSuccessRec;
            }
        } else if (processDef.getOutputFormat().equalsIgnoreCase(BatchProcessExportData.MARC)) {
            List<BibMarcRecord> bibMarcRecordsProcess = new ArrayList<>();
            bibMarcRecordsProcess.addAll(bibMarcRecords);
            batchProcessExportData.setFileName(fileName);
            generateMarcFromXml(bibMarcRecordsProcess, batchExportStatistics, isFirstBatch, isLastBatch);
        }
    }


    private BibMarcRecord buildBibMarcRecord(Bib bib) throws Exception {
        BibMarcRecord bibMarcRecord = null;
        BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();
        BibMarcRecords marcRecords = bibMarcRecordProcessor.fromXML(bib.getContent());
        List<BibMarcRecord> bibMarcRecordList = marcRecords.getRecords();
        Iterator<BibMarcRecord> bibMarcRecordListIterator = bibMarcRecordList.iterator();
        if (bibMarcRecordListIterator.hasNext()) {
            bibMarcRecord = bibMarcRecordListIterator.next();
        }
        return bibMarcRecord;
    }


    private void getInstanceDetails(BibMarcRecord bibMarcRecord, OLEBatchProcessProfileBo profile, StringBuilder errBuilder, BibTree bibTree) throws Exception {
        List<DataField> dataFields = bibMarcRecord.getDataFields();

        try {
            List<DataField> holdingsItemDataField = Collections.emptyList();

            if (bibTree != null && bibTree.getHoldingsTrees() != null && bibTree.getHoldingsTrees().size() > 0) {
                for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
                    if (holdingsTree.getHoldings() != null) {
                        if (holdingsTree.getHoldings().getHoldingsType().equalsIgnoreCase("print")) {
                            holdingsItemDataField = new InstanceMappingHelper().generateDataFieldForHolding(holdingsTree, profile, errBuilder);
                        } else {
                            holdingsItemDataField = new EInstanceMappingHelper().generateDataFieldForEHolding(holdingsTree, profile, errBuilder);
                        }
                        dataFields.addAll(holdingsItemDataField);
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("Error while getting instance details for instanceID :: " + bibMarcRecord.getRecordId(), ex);
            errBuilder.append("-----");
            buildError(errBuilder, ERR_INSTANCE, bibMarcRecord.getRecordId(), ERR_CAUSE, ex.getMessage(), " ::At:: ", "getInstanceDetails", TIME_STAMP, new java.util.Date().toString());

        }
    }


    private void buildError(StringBuilder errBuilder, String... errorString) {
        for (String str : errorString) {
            errBuilder.append(str).append(COMMA);
        }
        errBuilder.append(lineSeparator);
    }


    /**
     * Writes the content read into a mrc file
     *
     * @param bibMarcRecords
     * @param batchExportStatistics
     * @param isFirstBatch
     * @param isLastBatch
     * @throws Exception
     */
    public int generateMarcFromXml(List<BibMarcRecord> bibMarcRecords, BatchExportStatistics batchExportStatistics, Boolean isFirstBatch, Boolean isLastBatch) throws Exception {
        StopWatch timer = new StopWatch();
        timer.start();
        int successRec = 0;
        File fileToWrite = new File(batchProcessExportData.getFilePath() + FileSystems.getDefault().getSeparator() + fileName + BatchProcessExportData.EXT_MARC);
        FileOutputStream fileOutputStream = new FileOutputStream(fileToWrite, true);
        //String bibContent = StringUtils.join(bibDocList, "");
        if (!fileToWrite.exists()) {
            if (fileToWrite.getParentFile().mkdirs() && fileToWrite.createNewFile()) {
                //do nothing
            } else {
                LOG.error("Cannot create mrc file in the given file path :: " + fileToWrite.getPath());
                batchProcessExportData.job.setStatus(JOB_STATUS_STOPPED);
                throw new RuntimeException("Cannot create mrc file in the given file path :: " + fileToWrite.getPath());
            }
        }
        BibMarcRecordProcessor recordProcessor = new BibMarcRecordProcessor();
        MarcSplitStreamWriter writer = new MarcSplitStreamWriter(fileOutputStream, OleNGConstants.UTF_8,300000,"880");

        for (String bibContent : batchProcessExportData.getBibDocList()) {
            InputStream input = new ByteArrayInputStream(bibContent.getBytes());
            List<BibMarcRecord> successList = new ArrayList<>();
            Record record = null;
            OLEMarcReader marcXmlReader = new OLEMarcXmlReader(input);
            try {
                while (marcXmlReader.hasNext()) {
                    if (marcXmlReader.hasErrors()) {
                        marcXmlReader.next();
                        errBuilder.append(marcXmlReader.getError().toString()).append(lineSeparator);
                        failureList.add(bibMarcRecords.get(successRec));
                        marcXmlReader.clearErrors();
                        continue;
                    }
                    record = marcXmlReader.next();
                    writer.write(record);
                    successList.add(bibMarcRecords.get(successRec));
                    successRec++;
                }

            } catch (Exception ex) {
                BibMarcRecord failureRecord = bibMarcRecords.get(successRec);
                bibMarcRecords.removeAll(successList);

                // add to error list
                failureList.add(failureRecord);
                bibMarcRecords.remove(failureRecord);

                // Building Error file with reason
                String recordId = failureRecord.getRecordId();
                LOG.error("Error while parsing MARCXML to mrc data:: " + (recordId == null ? "NULL_RECORD" : "record id:: " + recordId), ex);
                batchProcessExportData.getErrBuilder().append(ERR_BIB).append(recordId == null ? "ERROR_RECORD" : recordId).append(TIME_STAMP)
                    .append(new Date()).append(ERR_CAUSE).append(ex.getMessage()).append("::").append(ex.getCause().getMessage()).append(" ::For Record::").append(lineSeparator);
                batchProcessExportData.getErrBuilder().append("--------------------------------------------------------------------------------------------------").append(lineSeparator);
                batchProcessExportData.getErrBuilder().append(recordProcessor.generateXML(failureRecord)).append(lineSeparator);
                batchProcessExportData.getErrBuilder().append("--------------------------------------------------------------------------------------------------").append(lineSeparator).append(lineSeparator);

                // Converting Bib Marc records as MARCXML
                Object[] resultMap = processBibMarcRecord(bibMarcRecords, batchExportStatistics, isFirstBatch, isLastBatch);
                prepareForWrite(resultMap, bibMarcRecords, batchExportStatistics, isFirstBatch, isLastBatch);
            }
        }
        writer.close();
        timer.stop();
        return successRec;
    }


}
