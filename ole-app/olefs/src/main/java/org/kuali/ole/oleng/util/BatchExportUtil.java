package org.kuali.ole.oleng.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.response.BatchExportFileResponse;
import org.kuali.ole.docstore.common.response.OleNGBatchExportResponse;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.process.model.BatchProcessJob;
import org.kuali.ole.oleng.batch.process.model.BatchProcessTxObject;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileFilterCriteria;
import org.kuali.ole.oleng.batch.reports.BatchExportFileLogHandler;
import org.kuali.ole.spring.batch.BatchUtil;
import org.marc4j.marc.Record;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by rajeshbabuk on 4/21/16.
 */
public class BatchExportUtil extends BatchUtil {

    public Date getLastExportDateForProfile(BatchProcessTxObject batchProcessTxObject) {
        Calendar calendar = Calendar.getInstance();
        try {
            BatchProcessJob batchProcessJob = getBusinessObjectService().findBySinglePrimaryKey(BatchProcessJob.class, batchProcessTxObject.getBatchJobDetails().getJobId());
            Map map = new HashMap();
            map.put(OleNGConstants.JOB_ID, batchProcessTxObject.getBatchJobDetails().getJobId());
            map.put(OleNGConstants.STATUS, OleNGConstants.COMPLETED);
            // Get all jobs for the job id and status completed
            List<BatchJobDetails> batchJobDetailsList = (List<BatchJobDetails>) getBusinessObjectService().findMatchingOrderBy(BatchJobDetails.class, map, OleNGConstants.START_TIME, false);
            if (CollectionUtils.isNotEmpty(batchJobDetailsList)) {
                if (null != batchProcessJob && OleNGConstants.SCHEDULED.equalsIgnoreCase(batchProcessJob.getJobType())) {
                    // Compare scheduled job creation time and last executed job start time and set the greater date to last export date.
                    int compareTime = batchProcessJob.getCreatedOn().compareTo(batchJobDetailsList.get(0).getStartTime());
                    if (compareTime > 0) {
                        calendar.setTime(batchProcessJob.getCreatedOn());
                    } else if (compareTime < 0) {
                        calendar.setTime(batchJobDetailsList.get(0).getStartTime());
                    } else {
                        calendar.setTime(batchJobDetailsList.get(0).getStartTime());
                    }
                } else {
                    // Last executed job start time is set as last export date of all jobs for the job id.
                    calendar.setTime(batchJobDetailsList.get(0).getStartTime());
                }
            } else {
                if (OleNGConstants.SCHEDULED.equalsIgnoreCase(batchProcessJob.getJobType())) {
                    // Get scheduled job running for the first time to set the creation time as last export date.
                    calendar.setTime(batchProcessJob.getCreatedOn());
                } else {
                    // Job start time is set as last export date for the adhoc jobs that are running for the first time.
                    calendar.setTime(batchProcessTxObject.getBatchJobDetails().getStartTime());
                }
            }
            return getUTCTime(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            addBatchExportFailureResponseToExchange(e, null, batchProcessTxObject.getExchangeObjectForBatchExport());
        }
        return null;
    }

    private static Date getUTCTime(Date date) throws ParseException {
        DateFormat format = new SimpleDateFormat(OleNGConstants.SOLR_DATE_FORMAT);
        format.setTimeZone(TimeZone.getTimeZone(OleNGConstants.UTC_TIME_ZONE));
        String utcStr = format.format(date);
        DateFormat format2 = new SimpleDateFormat(OleNGConstants.SOLR_DATE_FORMAT);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(OleNGConstants.UTC_TIME_ZONE));
        cal.setTime(format2.parse(utcStr));
        return cal.getTime();
    }

    public String getIncrementalSolrQuery(Date lastExportDate) {
        SimpleDateFormat format = new SimpleDateFormat(OleNGConstants.SOLR_DATE_FORMAT);
        String fromDate = format.format(lastExportDate);
        return "(dateUpdated" + OleNGConstants.COLON + "[" + fromDate + " TO NOW])";
    }

    public String getDeleteSolrQuery(Date lastExportDate) {
        SimpleDateFormat format = new SimpleDateFormat(OleNGConstants.SOLR_DATE_FORMAT);
        String fromDate = format.format(lastExportDate);
        return "(DocType:bibliographic_delete)AND(dateUpdated" + OleNGConstants.COLON + "[" + fromDate + " TO NOW])";
    }

    public String getIncrementalExceptStaffOnlySolrQuery(Date lastExportDate) {
        SimpleDateFormat format = new SimpleDateFormat(OleNGConstants.SOLR_DATE_FORMAT);
        String fromDate = format.format(lastExportDate);
        return "(dateUpdated" + OleNGConstants.COLON + "[" + fromDate + " TO NOW])AND(staffOnlyFlag:false)";
    }

    public String getFilterSolrQuery(BatchProcessTxObject batchProcessTxObject, OleNGBatchExportResponse oleNGBatchExportResponse) {
        List<BatchProfileFilterCriteria> filterCriteriaList = batchProcessTxObject.getBatchProcessProfile().getBatchProfileFilterCriteriaList();
        if (CollectionUtils.isNotEmpty(filterCriteriaList)) {
            if (StringUtils.isNotBlank(batchProcessTxObject.getFileExtension()) && OleNGConstants.TXT.equalsIgnoreCase(batchProcessTxObject.getFileExtension())) {
                if (filterCriteriaList.get(0).getFieldName().equalsIgnoreCase("Bib Local Id From File")) {
                    return buildFilterQueryForInputFile(batchProcessTxObject, oleNGBatchExportResponse);
                }
            } else {
                return buildFilterQuery(filterCriteriaList);
            }
        }
        return null;
    }

    public String buildFilterQuery(List<BatchProfileFilterCriteria> filterCriteriaList) {
        StringBuilder queryBuilder = new StringBuilder();
        try {
            boolean addDocType = false;
            for (int i = 0; i < filterCriteriaList.size(); i++) {
                String filterFieldName = filterCriteriaList.get(i).getFieldName();
                if (StringUtils.isNotBlank(filterFieldName)) {
                    switch (filterFieldName) {
                        case "Bib Local Id":
                            addDocType = true;
                            if (StringUtils.isNotBlank(filterCriteriaList.get(i).getFieldValue())) {
                                queryBuilder.append("(LocalId_search:" + filterCriteriaList.get(i).getFieldValue() + ")");
                            } else if (StringUtils.isNotBlank(filterCriteriaList.get(i).getRangeFrom()) && StringUtils.isNotBlank(filterCriteriaList.get(i).getRangeTo())) {
                                queryBuilder.append(("(LocalId_search:" + "[" + filterCriteriaList.get(i).getRangeFrom() + " TO " + filterCriteriaList.get(i).getRangeTo() + "])"));
                            } else if (StringUtils.isNotBlank(filterCriteriaList.get(i).getRangeFrom()) && StringUtils.isBlank(filterCriteriaList.get(i).getRangeTo())) {
                                queryBuilder.append(("(LocalId_search:" + "[" + filterCriteriaList.get(i).getRangeFrom() + " TO *])"));
                            }
                            break;
                        case "Bib Status":
                            addDocType = true;
                            if (StringUtils.isNotBlank(filterCriteriaList.get(i).getFieldValue())) {
                                queryBuilder.append("(Status_search:" + buildFieldValueForStatus(filterCriteriaList.get(i).getFieldValue()) + ")");
                            }
                            break;
                        case "Bib Status Updated Date":
                            addDocType = true;
                            queryBuilder.append(buildFilterQueryForTypeDate("statusUpdatedOn",filterCriteriaList.get(i)));
                            break;
                        case "Date Updated":
                            queryBuilder.append(buildFilterQueryForTypeDate("dateUpdated",filterCriteriaList.get(i)));
                            break;
                        case "Date Entered":
                            queryBuilder.append(buildFilterQueryForTypeDate("dateEntered",filterCriteriaList.get(i)));
                            break;
                        case "Created By":
                            if (StringUtils.isNotBlank(filterCriteriaList.get(i).getFieldValue())) {
                                queryBuilder.append("(createdBy:" + filterCriteriaList.get(i).getFieldValue() + ")");
                            }
                            break;
                        case "Updated By":
                            if (StringUtils.isNotBlank(filterCriteriaList.get(i).getFieldValue())) {
                                queryBuilder.append("updatedBy:" + filterCriteriaList.get(i).getFieldValue() + ")");
                            }
                            break;
                        case "Staff Only":
                            if (StringUtils.isNotBlank(filterCriteriaList.get(i).getFieldValue())) {
                                queryBuilder.append("staffOnlyFlag:" + filterCriteriaList.get(i).getFieldValue() + ")");
                            }
                            break;
                    }
                } else if (StringUtils.isNotBlank(filterCriteriaList.get(i).getDataField())) {
                    addDocType = true;
                    if (getMarcRecordUtil().isControlField(filterCriteriaList.get(i).getDataField())) {
                        queryBuilder.append("(" + OleNGConstants.CONTROL_FIELD_ + filterCriteriaList.get(i).getDataField() + ":" + filterCriteriaList.get(i).getFieldValue() + ")");
                    } else {
                        queryBuilder.append("(" + OleNGConstants.MDF_ + filterCriteriaList.get(i).getDataField() + filterCriteriaList.get(i).getSubField() + ":" + filterCriteriaList.get(i).getFieldValue() + ")");
                    }
                }
                if (i != filterCriteriaList.size() - 1) {
                    queryBuilder.append("AND");
                }
            }
            if (addDocType) {
                return "(DocType:bibliographic) AND " + queryBuilder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryBuilder.toString();
    }

    public String buildFilterQueryForTypeDate(String fieldName, BatchProfileFilterCriteria filterCriteria) throws ParseException {
        StringBuilder queryBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(filterCriteria.getFieldValue())) {
            queryBuilder.append("(" + fieldName + ":" + "[" + getSolrDate(filterCriteria.getFieldValue(), true) + " TO " + getSolrDate(filterCriteria.getFieldValue(), false) + "])");
        } else if (StringUtils.isNotBlank(filterCriteria.getRangeFrom()) && StringUtils.isNotBlank(filterCriteria.getRangeTo())) {
            queryBuilder.append(("(" + fieldName + ":" + "[" + getSolrDate(filterCriteria.getRangeFrom(), true) + " TO " + getSolrDate(filterCriteria.getRangeTo(), false) + "])"));
        } else if (StringUtils.isNotBlank(filterCriteria.getRangeFrom()) && StringUtils.isBlank(filterCriteria.getRangeTo())) {
            queryBuilder.append(("(" + fieldName + ":" + "[" + getSolrDate(filterCriteria.getRangeFrom(), true) + " TO *])"));
        }
        return queryBuilder.toString();
    }

    public String buildFieldValueForStatus(String fieldValue) {
        StringBuilder bibStatusBuilder = new StringBuilder();
        String[] filterFieldValues = fieldValue.split(",");
        for (int i = 0; i < filterFieldValues.length; i++) {
            if (StringUtils.isNotBlank(filterFieldValues[i])) {
                bibStatusBuilder.append("\"" + filterFieldValues[i] + "\"");
                if (i != filterFieldValues.length - 1) {
                    bibStatusBuilder.append("OR");
                }
            }
        }
        return bibStatusBuilder.toString();
    }

    public String buildFilterQueryForInputFile(BatchProcessTxObject batchProcessTxObject, OleNGBatchExportResponse oleNGBatchExportResponse) {
        String fileContent = null;
        try {
            fileContent = FileUtils.readFileToString(new File(batchProcessTxObject.getIncomingFileDirectoryPath() + File.separator + batchProcessTxObject.getBatchJobDetails().getFileName()));
            List<String> bibLocalIds = IOUtils.readLines(IOUtils.toInputStream(fileContent));
            oleNGBatchExportResponse.setTotalNumberOfRecords(bibLocalIds.size());
            batchProcessTxObject.getBatchJobDetails().setTotalRecords(String.valueOf(bibLocalIds.size()));
            updateBatchJob(batchProcessTxObject.getBatchJobDetails());
            removeDuplicates(bibLocalIds, oleNGBatchExportResponse);
            return getSolrQueryForLocalIds(bibLocalIds);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSolrQueryForLocalIds(List<String> bibLocalIds) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(DocType:bibliographic)AND(");
        if (CollectionUtils.isNotEmpty(bibLocalIds)) {
            for (int i = 0; i < bibLocalIds.size(); i++) {
                stringBuilder.append("(LocalId_search:" + bibLocalIds.get(i) + ")");
                if (i != bibLocalIds.size() - 1) {
                    stringBuilder.append("OR");
                }
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public void removeDuplicates(List<String> bibLocalIds, OleNGBatchExportResponse oleNGBatchExportResponse) {
        List<String> bibLocalIdData = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(bibLocalIds)) {
            for (String bibLocalId : bibLocalIds) {
                if (StringUtils.isNotEmpty(bibLocalId) && !bibLocalIdData.contains(bibLocalId)) {
                    bibLocalIdData.add( DocumentUniqueIDPrefix.getDocumentId(bibLocalId));
                } else {
                    oleNGBatchExportResponse.addNoOfFailureRecords(1);
                    oleNGBatchExportResponse.addFailureRecord(bibLocalId, bibLocalId, OleNGConstants.ERR_DUPLICATE_LOCAL_ID);
                }
            }
        }
        bibLocalIds.clear();
        bibLocalIds.addAll(bibLocalIdData);
    }

    public void processDeletedBibs(Date lastExportDate, BatchProcessTxObject batchProcessTxObject, OleNGBatchExportResponse oleNGBatchExportResponse) {
        SolrDocumentList solrDocumentList = getSolrRequestReponseHandler().getSolrDocumentList(getDeleteSolrQuery(lastExportDate));
        SortedSet<String> deletedBibIds = new TreeSet<>();
        if (solrDocumentList.size() > 0) {
            for (SolrDocument solrDocument : solrDocumentList) {
                deletedBibIds.add((String) solrDocument.getFieldValue(OleNGConstants.LOCAL_ID_DISPLAY));
            }
        }
        generateFileForBibIds(deletedBibIds, batchProcessTxObject);
    }

    public void generateFileForMarcRecords(int fileNumber, List<Record> marcRecords, BatchProcessTxObject batchProcessTxObject) {
        BatchExportFileResponse batchExportFileResponse = new BatchExportFileResponse();
        batchExportFileResponse.setMarcRecords(marcRecords);
        batchExportFileResponse.setFileType(batchProcessTxObject.getBatchJobDetails().getOutputFileFormat());
        batchExportFileResponse.setFileName(batchProcessTxObject.getBatchProcessProfile().getBatchProcessProfileName() + "_" + batchProcessTxObject.getBatchJobDetails().getJobDetailId());
        batchExportFileResponse.setFileNumber(fileNumber);
        batchExportFileResponse.setProfileName(batchProcessTxObject.getBatchProcessProfile().getBatchProcessProfileName());
        batchExportFileResponse.setDirectoryName(batchProcessTxObject.getReportDirectoryName());
        BatchExportFileLogHandler batchExportFileLogHandler = BatchExportFileLogHandler.getInstance();
        batchExportFileLogHandler.logMessage(batchExportFileResponse, batchProcessTxObject.getReportDirectoryName());
    }

    public void generateFileForBibIds(SortedSet<String> bibIds, BatchProcessTxObject batchProcessTxObject) {
        BatchExportFileResponse batchExportFileResponse = new BatchExportFileResponse();
        batchExportFileResponse.setDeletedBibIds(bibIds);
        batchExportFileResponse.setFileType(OleNGConstants.TXT);
        batchExportFileResponse.setFileName(batchProcessTxObject.getBatchProcessProfile().getBatchProcessProfileName() + "_" + batchProcessTxObject.getBatchJobDetails().getJobDetailId());
        batchExportFileResponse.setProfileName(batchProcessTxObject.getBatchProcessProfile().getBatchProcessProfileName());
        batchExportFileResponse.setDirectoryName(batchProcessTxObject.getReportDirectoryName());
        BatchExportFileLogHandler batchExportFileLogHandler = BatchExportFileLogHandler.getInstance();
        batchExportFileLogHandler.logMessage(batchExportFileResponse, batchProcessTxObject.getReportDirectoryName());
    }

}
