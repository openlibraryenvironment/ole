package org.kuali.ole.batch.ingest;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileMatchPoint;
import org.kuali.ole.batch.impl.AbstractBatchProcess;
import org.kuali.ole.batch.service.BatchProcessDeleteService;
import org.kuali.ole.batch.util.BatchBibImportUtil;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.marc4j.*;
import org.marc4j.marc.ControlField;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 7/27/13
 * Time: 9:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class BatchProcessDeleteData extends AbstractBatchProcess {

    private static final Logger LOG = Logger.getLogger(BatchProcessDeleteData.class);
    private BatchProcessDeleteService batchProcessDeleteService;
    //private List bibliographicRecords;
    private List docBibIdsList;
    private List deleteChunkBibIdsList = new ArrayList(0);
    private int chunkCount = 0;
    private int totalCount = 0;
    private int failureCount = 0;
    private StringBuffer misMatchMarcRecords = new StringBuffer("");
    private StringBuffer matchMarcRecords = new StringBuffer("");
    private StringBuffer failureReport = new StringBuffer("");
    private String fileType = "mrc";

    private BatchProcessDeleteService getBatchProcessDeleteService() {
        if (batchProcessDeleteService == null)
            batchProcessDeleteService = GlobalResourceLoader.getService("batchDeleteServiceImpl");
        return batchProcessDeleteService;
    }

    @Override
    protected void prepareForRead() throws Exception {
        // read the import file content from the batch delete folder
        String fileContent = getBatchProcessFileContent();
        job.setNoOfSuccessRecords("0");
        job.setNoOfFailureRecords("0");
        job.setNoOfRecordsProcessed("0");
        if (job.getUploadFileName() != null && job.getUploadFileName().contains(".txt")) {
            fileType = "txt";
        }
        docBibIdsList = getBatchDeleteBibIdsList(fileContent);
        job.setTotalNoOfRecords(totalCount + "");
        // processDef.setChunkSize(4);
    }

    @Override
    protected void prepareForWrite() throws Exception {
        if (docBibIdsList != null && docBibIdsList.size() > 0) {
            if (processDef.getChunkSize() == 1000) {
                String profileField = getProfileField();
                if (profileField != null) {
                    int sucRecordCount = getBatchProcessDeleteService().performBatchDelete(docBibIdsList, profileField);
                    deleteBatchFile();
                    createBatchFailureFile();
                    job.setNoOfRecordsProcessed(totalCount + "");
                    job.setNoOfSuccessRecords((totalCount - failureCount) + "");
                    job.setNoOfFailureRecords(failureCount + "");
                    job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
                }
            }
        } else {
            job.setNoOfRecordsProcessed(totalCount + "");
            job.setNoOfSuccessRecords((totalCount - failureCount) + "");
            job.setNoOfFailureRecords(failureCount + "");
            deleteBatchFile();
            createBatchFailureFile();
            job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
        }


    }

    @Override
    protected void getNextBatch() throws Exception {
        if (chunkCount < docBibIdsList.size()) {
            if (chunkCount + processDef.getChunkSize() < docBibIdsList.size()) {
                deleteChunkBibIdsList = docBibIdsList.subList(chunkCount, chunkCount + processDef.getChunkSize());
                chunkCount += processDef.getChunkSize();
            } else {
                deleteChunkBibIdsList = docBibIdsList.subList(chunkCount, docBibIdsList.size());
                performBatchDelete();
                chunkCount += processDef.getChunkSize();
                deleteBatchFile();
                createBatchFailureFile();
                job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
            }

        }

    }

    @Override
    protected void processBatch() throws Exception {

        if (deleteChunkBibIdsList != null && deleteChunkBibIdsList.size() > 0) {

            performBatchDelete();

        }
    }

    /**
     * this method return the match point profile field value
     *
     * @return
     */
    private String getProfileField() {
        if (processDef.getBatchProcessProfileBo() != null) {
            List<OLEBatchProcessProfileMatchPoint> bibMatchPointList = BatchBibImportUtil.buildMatchPointListByDataType(processDef.getBatchProcessProfileBo().getOleBatchProcessProfileMatchPointList(), org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode());
            if (bibMatchPointList != null && bibMatchPointList.size() > 0) {
                String profileField = bibMatchPointList.get(0).getMatchPoint();
                return profileField;
            }
        }
        return null;
    }

    /**
     * This method returns the list of bib ids which are not used in requisition , loan and does not contain multiple instances and boundwith
     * @param fileContent
     * @return
     * @throws Exception
     */
    public List getBatchDeleteBibIdsList(String fileContent) throws Exception {

        String profileField = getProfileField();
        List matchBibIdsList = new ArrayList(0);
        totalCount = 0;
        if (profileField != null) {
            String profileDataFiled = profileField.substring(0, 3);
            char profileSubField = ' ';
            if(profileField!=null && profileField.contains("$")) {
                profileSubField = profileField.substring(profileField.lastIndexOf('$') + 1).toCharArray()[0];
            } else if (profileField != null && profileField.length() > 3) {
                profileSubField = profileField.substring(3).toCharArray()[0];
            }
            String profileFieldQuery = profileDataFiled;
            if (profileField != null && profileField.length() > 3) {
                profileFieldQuery = profileFieldQuery + profileSubField;
                profileFieldQuery = profileFieldQuery.trim();
            }
            try {
                // if import file type is mrc
                if ("mrc".equals(fileType)) {
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileContent.getBytes());
                    ByteArrayOutputStream failureByteArrayOutputStream = new ByteArrayOutputStream();
                    ByteArrayOutputStream successByteArrayOutputStream = new ByteArrayOutputStream();
                    MarcReader reader = new MarcStreamReader(byteArrayInputStream);
                    MarcWriter failureWriter = new MarcStreamWriter(failureByteArrayOutputStream);
                    MarcWriter successWriter = new MarcStreamWriter(successByteArrayOutputStream);
                    while (reader.hasNext()) {
                        Record record = reader.next();
                        totalCount++;
                        boolean recExistFlag = true;
                        for (DataField dataFieldImpl : (List<DataField>) record.getDataFields()) {
                            String searchMrcFieldData = "";
                            if (profileDataFiled.equals(dataFieldImpl.getTag())) {
                                searchMrcFieldData = dataFieldImpl.getSubfield(profileSubField).getData().replaceAll(":", "");
                                Map batchDeleteMap = getBatchProcessDeleteService().getBibIdsForBatchDelete(searchMrcFieldData, profileFieldQuery);
                                List bibIdList = (List) batchDeleteMap.get(OLEConstants.BIB_SEARCH);
                                if (bibIdList == null || (bibIdList != null && bibIdList.size() > 1)) {
                                    failureCount++;
                                    failureWriter.write(record);
                                } else {
                                    matchBibIdsList.addAll(bibIdList);
                                    successWriter.write(record);
                                }
                                String failureInfo = (String) batchDeleteMap.get(OLEConstants.OLEBatchProcess.FAILURE_INFO);
                                if (failureInfo != null) {
                                    failureReport.append(searchMrcFieldData + "  (" + profileField + ")   :: " + failureInfo + "\n");
                                }
                                recExistFlag = false;
                                break;
                            }
                        }

                        if (OLEConstants.OLEBatchProcess.CONTROL_FIELD_001.equals(profileDataFiled)) {
                            for (ControlField controlField : (List<ControlField>) record.getControlFields()) {
                                if (!OLEConstants.OLEBatchProcess.CONTROL_FIELD_001.equalsIgnoreCase(controlField.getTag()))
                                    continue;
                                Map batchDeleteMap = getBatchProcessDeleteService().getBibIdsForBatchDelete(controlField.getData(), profileFieldQuery);
                                List bibIdList = (List) batchDeleteMap.get(OLEConstants.BIB_SEARCH);
                                if (bibIdList == null || (bibIdList != null && bibIdList.size() > 1)) {
                                    failureCount++;
                                    failureWriter.write(record);
                                } else {
                                    matchBibIdsList.addAll(bibIdList);
                                    successWriter.write(record);
                                }
                                String failureInfo = (String) batchDeleteMap.get(OLEConstants.OLEBatchProcess.FAILURE_INFO);
                                if (failureInfo != null) {
                                    failureReport.append(controlField.getData() + "  (" + profileField + ")   :: " + failureInfo + "\n");
                                }
                                recExistFlag = false;
                                break;


                            }
                        }

                        // provide matchpoint record data is not available in mrc record
                        if (recExistFlag) {
                            failureCount++;
                            failureWriter.write(record);
                        }

                    }
                    misMatchMarcRecords.append(failureByteArrayOutputStream.toString());
                    matchMarcRecords.append(successByteArrayOutputStream.toString());
                    failureWriter.close();
                    successWriter.close();
                }
                // if import file type is txt
                else if ("txt".equals(fileType)) {
                    List txtFileDataList = new ArrayList();
                    BufferedReader br = new BufferedReader(new StringReader(fileContent));
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();
                    while (line != null) {
                        totalCount++;
                        if (!txtFileDataList.contains(line)) {
                            txtFileDataList.add(line);
                            Map batchDeleteMap = getBatchProcessDeleteService().getBibIdsForBatchDelete(line, profileFieldQuery);
                            List bibIdList = (List) batchDeleteMap.get("bibIdentifier");
                            if (bibIdList == null || (bibIdList != null && bibIdList.size() > 1)) {
                                failureCount++;
                                misMatchMarcRecords.append(line.toString() + "\n");
                            } else {
                                matchBibIdsList.addAll(bibIdList);
                                matchMarcRecords.append(line.toString() + "\n");
                            }
                            String failureInfo = (String) batchDeleteMap.get("failureInfo");
                            if (failureInfo != null) {
                                failureReport.append(line + "  (" + profileField + ")   :: " + failureInfo + "\n");
                            }
                        } else {
                            failureCount++;
                            misMatchMarcRecords.append(line.toString() + "\n");
                        }
                        line = br.readLine();
                    }
                }
            } catch (Exception e) {
                LOG.error("exception :  " + e.getMessage());
            }
        }
        return matchBibIdsList;
    }

    /**
     * This method perform the delete the records by using list bib ids
     ** @throws Exception
     */
    public void performBatchDelete() throws Exception {
        String profileField = getProfileField();
        if (profileField != null) {
            // String failureRecords = "";
            int sucRecordCount = getBatchProcessDeleteService().performBatchDelete(deleteChunkBibIdsList, profileField);
            //  misMatchMarcRecords.append(failureRecords);
            job.setNoOfSuccessRecords((Integer.parseInt(job.getNoOfSuccessRecords()) + sucRecordCount) + "");
            job.setNoOfFailureRecords(totalCount - (Integer.parseInt(job.getNoOfSuccessRecords())) + "");
            job.setNoOfRecordsProcessed((Integer.parseInt(job.getNoOfRecordsProcessed()) + deleteChunkBibIdsList.size()) + "");
        }
    }

    /**
     * Create the failure and success and failure Report files
     * @throws Exception
     */
    public void createBatchFailureFile() throws Exception {

        if (!"".equals(misMatchMarcRecords.toString())) {
            createBatchFailureFile(misMatchMarcRecords.toString());
        }
        if (!"".equals(matchMarcRecords.toString())) {
            createBatchSuccessFile(matchMarcRecords.toString());
        }
        if (!"".equals(failureReport.toString())) {
            createBatchDeleteFailureReportFile(failureReport.toString());
        }

    }


}
