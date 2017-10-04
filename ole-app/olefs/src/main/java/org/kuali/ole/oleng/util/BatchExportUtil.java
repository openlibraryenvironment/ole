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
import org.kuali.ole.oleng.dao.export.ExportDao;
import org.kuali.ole.spring.batch.BatchUtil;
import org.kuali.ole.sys.context.SpringContext;
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

    private ExportDao exportDao=(ExportDao) SpringContext.getBean("exportDao");

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
            if(!batchProcessTxObject.getBatchProcessProfile().getExportScope().equalsIgnoreCase(OleNGConstants.INCREMENTAL_EXCEPT_STAFF_ONLY) &&
                    !batchProcessTxObject.getBatchProcessProfile().getExportScope().equalsIgnoreCase(OleNGConstants.INCREMENTAL)) {
                return getUTCTime(calendar.getTime());
            }else {
                return calendar.getTime();
            }
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fromDate = format.format(lastExportDate);
        StringBuilder queryString=new StringBuilder();
        queryString.append("SELECT BIB_ID FROM OLE_DS_BIB_T WHERE DATE_UPDATED BETWEEN '"+ fromDate +"' AND NOW()");
        queryString.append("|");
        queryString.append("SELECT DISTINCT H.BIB_ID FROM OLE_DS_HOLDINGS_T H LEFT OUTER JOIN OLE_DS_ITEM_T I ON H.HOLDINGS_ID = I.HOLDINGS_ID WHERE (H.DATE_UPDATED BETWEEN '"+ fromDate +"' AND NOW()) ");
        queryString.append("OR (I.DATE_UPDATED BETWEEN '"+ fromDate +"' AND NOW())");
        queryString.append("|");
        queryString.append("SELECT DISTINCT BIB_ID FROM OLE_DS_BIB_T WHERE BIB_ID IN (SELECT DISTINCT BIB_ID FROM OLE_DS_HOLDINGS_T WHERE HOLDINGS_ID IN (SELECT DISTINCT HOLDINGS_ID FROM OLE_DS_HOLDINGS_DONOR_T WHERE DATE_UPDATED BETWEEN '" + fromDate + "' AND NOW())) OR " +
                " BIB_ID IN (SELECT DISTINCT BIB_ID FROM OLE_DS_HOLDINGS_T WHERE HOLDINGS_ID IN (SELECT DISTINCT HOLDINGS_ID FROM OLE_DS_ITEM_T WHERE ITEM_ID IN " +
                " (SELECT ITEM_ID FROM OLE_DS_ITEM_DONOR_T WHERE DATE_UPDATED BETWEEN '" + fromDate +"' AND NOW())))");
        queryString.append("|");
        queryString.append("SELECT DISTINCT DELETED_BIB_ID FROM OLE_DS_DELETED_BIB_T WHERE IS_BIB_DELETED = 'N' AND (DATE_UPDATED BETWEEN '"+fromDate+"' AND NOW())");
        return queryString.toString();
    }

    public String getDeletedAndStaffOnlyBibsSolrQuery(Date lastExportDate) {
        SimpleDateFormat format = new SimpleDateFormat(OleNGConstants.SOLR_DATE_FORMAT);
        String fromDate = format.format(lastExportDate);
        return "(DocType:bibliographic_delete OR (DocType:bibliographic AND staffOnlyFlag:true))AND(dateUpdated" + OleNGConstants.COLON + "[" + fromDate + " TO NOW])";
    }

    public String getDeletedAndStaffOnlyBibsSqlQuery(Date lastExportDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fromDate = format.format(lastExportDate);
        StringBuilder queryString=new StringBuilder();
        queryString.append("SELECT DELETED_BIB_ID FROM OLE_DS_DELETED_BIB_T WHERE IS_BIB_DELETED = 'Y' AND (DATE_UPDATED BETWEEN '"+fromDate+"' AND NOW())");
        queryString.append("|");
        queryString.append("SELECT BIB_ID FROM OLE_DS_BIB_T WHERE (DATE_UPDATED BETWEEN '"+fromDate+"' AND NOW()) AND STAFF_ONLY='Y'");
        return queryString.toString();
    }

    public String getIncrementalExceptStaffOnlySolrQuery(Date lastExportDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fromDate = format.format(lastExportDate);
        StringBuilder queryString=new StringBuilder();
        queryString.append("SELECT BIB_ID FROM OLE_DS_BIB_T WHERE (DATE_UPDATED BETWEEN '"+ fromDate +"' AND NOW()) AND STAFF_ONLY='N'");
        queryString.append("|");
        queryString.append("SELECT DISTINCT BIB_ID FROM OLE_DS_BIB_T WHERE BIB_ID IN (SELECT DISTINCT BIB_ID FROM OLE_DS_HOLDINGS_T WHERE DATE_UPDATED BETWEEN '"+fromDate+"' AND NOW() AND STAFF_ONLY = 'N') and STAFF_ONLY='N' OR " +
                "BIB_ID IN (SELECT DISTINCT BIB_ID FROM OLE_DS_HOLDINGS_T WHERE HOLDINGS_ID IN (SELECT DISTINCT HOLDINGS_ID FROM OLE_DS_ITEM_T WHERE DATE_UPDATED BETWEEN '"+fromDate+"' AND NOW() AND STAFF_ONLY = 'N') " +
                "AND STAFF_ONLY='N') AND STAFF_ONLY ='N'");
        queryString.append("|");
        queryString.append("SELECT DISTINCT BIB_ID FROM OLE_DS_BIB_T WHERE BIB_ID IN (SELECT DISTINCT BIB_ID FROM OLE_DS_HOLDINGS_T WHERE STAFF_ONLY = 'N' AND " +
                "HOLDINGS_ID IN (SELECT DISTINCT HOLDINGS_ID FROM OLE_DS_HOLDINGS_DONOR_T WHERE DATE_UPDATED BETWEEN '" + fromDate + "' AND NOW())) AND STAFF_ONLY='N' OR " +
                "BIB_ID IN (SELECT DISTINCT BIB_ID FROM OLE_DS_HOLDINGS_T WHERE HOLDINGS_ID IN (SELECT DISTINCT HOLDINGS_ID FROM OLE_DS_ITEM_T WHERE STAFF_ONLY = 'N' AND " +
                "ITEM_ID IN (SELECT ITEM_ID FROM OLE_DS_ITEM_DONOR_T WHERE DATE_UPDATED BETWEEN '" + fromDate +"' AND NOW())) AND STAFF_ONLY='N') AND STAFF_ONLY ='N'");
        queryString.append("|");
        queryString.append("SELECT DISTINCT DELETED_BIB_ID FROM OLE_DS_DELETED_BIB_T WHERE IS_BIB_DELETED = 'N' AND (DATE_UPDATED BETWEEN '"+fromDate+"' AND NOW())");
        return queryString.toString();
    }

    public List<String> getFilterSolrQuery(BatchProcessTxObject batchProcessTxObject, OleNGBatchExportResponse oleNGBatchExportResponse) {
        List<String> queryList = new ArrayList<>();
        List<BatchProfileFilterCriteria> filterCriteriaList = batchProcessTxObject.getBatchProcessProfile().getBatchProfileFilterCriteriaList();
        if (CollectionUtils.isNotEmpty(filterCriteriaList)) {
            if (StringUtils.isNotBlank(batchProcessTxObject.getFileExtension()) && OleNGConstants.TXT.equalsIgnoreCase(batchProcessTxObject.getFileExtension())) {
                if (filterCriteriaList.get(0).getFieldName().equalsIgnoreCase("Bib Local Id From File")) {
                    return buildFilterQueryForInputFile(batchProcessTxObject, oleNGBatchExportResponse);
                }
            } else {
                queryList.add(buildFilterQuery(filterCriteriaList));
                return queryList;
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
                                queryBuilder.append("(updatedBy:" + filterCriteriaList.get(i).getFieldValue() + ")");
                            }
                            break;
                        case "Staff Only(of Bib, Holdings or Item)":
                            if (StringUtils.isNotBlank(filterCriteriaList.get(i).getFieldValue())) {
                                queryBuilder.append("(staffOnlyFlag:" + filterCriteriaList.get(i).getFieldValue() + ")");
                            }
                            break;
                    }
                } else if (StringUtils.isNotBlank(filterCriteriaList.get(i).getFieldNameText())) {
                    addDocType = true;
                    String filterFieldList[]=filterCriteriaList.get(i).getFieldNameText().split("\\$");
                    if(filterFieldList.length==2) {
                        if (getMarcRecordUtil().isControlField(filterFieldList[0].trim())) {
                            queryBuilder.append("(" + OleNGConstants.CONTROL_FIELD_ + filterFieldList[0].trim() + ":" + filterCriteriaList.get(i).getFieldValue() + ")");
                        } else {
                            queryBuilder.append("(" + OleNGConstants.MDF_ + filterFieldList[0].trim() + filterFieldList[1] + ":" + filterCriteriaList.get(i).getFieldValue() + ")");
                        }
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

    public List<String> buildFilterQueryForInputFile(BatchProcessTxObject batchProcessTxObject, OleNGBatchExportResponse oleNGBatchExportResponse) {
        String fileContent = null;
        try {
            fileContent = FileUtils.readFileToString(new File(batchProcessTxObject.getIncomingFileDirectoryPath() + File.separator + batchProcessTxObject.getBatchJobDetails().getFileName()));
            List<String> bibLocalIds = IOUtils.readLines(IOUtils.toInputStream(fileContent));
            oleNGBatchExportResponse.setTotalNumberOfRecords(bibLocalIds.size());
            batchProcessTxObject.getBatchJobDetails().setTotalRecords(String.valueOf(bibLocalIds.size()));
            updateBatchJob(batchProcessTxObject.getBatchJobDetails());
            removeDuplicates(bibLocalIds, oleNGBatchExportResponse);
            return getLocalIds(bibLocalIds);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getSolrQueryForLocalIds(List<String> bibLocalIds) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> queryList = new ArrayList<>();
        stringBuilder.append("(DocType:bibliographic)AND(");
        if (CollectionUtils.isNotEmpty(bibLocalIds)) {
            for (int i = 0; i < bibLocalIds.size(); i++) {
                stringBuilder.append("(LocalId_search:" + bibLocalIds.get(i) + ")");
                if( (i+1) % 10000==0  || i == bibLocalIds.size() - 1){
                    stringBuilder.append(")");
                    queryList.add(stringBuilder.toString());
                    stringBuilder.setLength(0);
                    stringBuilder.append("(DocType:bibliographic)AND(");
                }else{
                    stringBuilder.append("OR");
                }
            }
        }
        return queryList;
    }

    public List<String> getLocalIds(List<String> bibLocalIds) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> queryList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(bibLocalIds)) {
            for (int i = 0; i < bibLocalIds.size(); i++) {
                stringBuilder.append(bibLocalIds.get(i));
                if( (i+1) % 10000==0  || i == bibLocalIds.size() - 1){
                    queryList.add(stringBuilder.toString());
                    stringBuilder.setLength(0);
                }else{
                    stringBuilder.append(",");
                }
            }
        }
        return queryList;
    }

    public void removeDuplicates(List<String> bibLocalIds, OleNGBatchExportResponse oleNGBatchExportResponse) {
        List<String> bibLocalIdData = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(bibLocalIds)) {
            for (String bibLocalId : bibLocalIds) {
                bibLocalId=bibLocalId.trim();
                if(StringUtils.isNumeric(bibLocalId)) {
                    if (StringUtils.isNotEmpty(bibLocalId) && !bibLocalIdData.contains(bibLocalId)) {
                        bibLocalIdData.add(DocumentUniqueIDPrefix.getDocumentId(bibLocalId));
                    } else {
                        oleNGBatchExportResponse.addNoOfFailureRecords(1);
                        oleNGBatchExportResponse.addFailureRecord(bibLocalId, bibLocalId, OleNGConstants.ERR_DUPLICATE_LOCAL_ID);
                    }
                } else{
                    oleNGBatchExportResponse.addNoOfFailureRecords(1);
                    oleNGBatchExportResponse.addFailureRecord(bibLocalId, bibLocalId, OleNGConstants.ERR_NON_NUMERIC_VALUE);
                }
            }
        }
        bibLocalIds.clear();
        bibLocalIds.addAll(bibLocalIdData);
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

    public void processDeletedAndStaffOnlyBibs(Date lastExportDate, BatchProcessTxObject batchProcessTxObject) {
        SortedSet<String> deletedBibIds = new TreeSet<>();
        String query=null;
        try {
            query = getDeletedAndStaffOnlyBibsSqlQuery(lastExportDate);
            //  String[] queryList = query.split("\\|");
            List<String> bibIdList = exportDao.getBibIdFromSqlQuery(query,batchProcessTxObject.getBatchProcessProfile().getExportScope());
            //  bibIdList.addAll(exportDao.getBibIdFromSqlQuery(queryList[1],batchProcessTxObject.getBatchProcessProfile().getExportScope()));
            if(bibIdList.size()>0){
                for(String bibId:bibIdList){
                    deletedBibIds.add(bibId);
                }
            }
        }catch (Exception e){
                e.printStackTrace();
                addBatchExportFailureResponseToExchange(e, null, batchProcessTxObject.getExchangeObjectForBatchExport());
        }
        generateFileForBibIds(deletedBibIds, batchProcessTxObject);
    }

    public Set<String> getBibIdentifiersForQuery(String query, int start, int chunkSize) {
        Set<String> bibIdentifiers = new HashSet<>();
        SolrDocumentList solrDocumentList = getSolrRequestReponseHandler().getSolrDocumentList(query, start, chunkSize, OleNGConstants.BIB_IDENTIFIER);
        if (solrDocumentList.size() > 0) {
            for (SolrDocument solrDocument : solrDocumentList) {
                if (solrDocument.containsKey(OleNGConstants.BIB_IDENTIFIER)) {
                    List<String> bibIds = (List) solrDocument.getFieldValue(OleNGConstants.BIB_IDENTIFIER);
                    for (String bibId : bibIds) {
                        bibIdentifiers.add(DocumentUniqueIDPrefix.getDocumentId(bibId));
                    }
                }
            }
        }
        return bibIdentifiers;
    }

}
