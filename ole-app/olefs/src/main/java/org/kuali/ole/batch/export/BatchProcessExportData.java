package org.kuali.ole.batch.export;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.*;
import org.kuali.ole.batch.document.OLEBatchProcessDefinitionDocument;
import org.kuali.ole.batch.helper.InstanceMappingHelper;
import org.kuali.ole.batch.impl.*;
import org.kuali.ole.batch.marc.OLEMarcReader;
import org.kuali.ole.batch.marc.OLEMarcXmlReader;
import org.kuali.ole.batch.service.ExportDataService;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.docstore.common.util.BatchBibTreeDBUtil;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.marc4j.MarcSplitStreamWriter;
import org.marc4j.MarcStreamWriter;
import org.marc4j.MarcWriter;
import org.marc4j.marc.ControlField;
import org.marc4j.marc.Record;
import org.marc4j.marc.VariableField;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import static org.kuali.ole.OLEConstants.OLEBatchProcess.*;

/**
 * Created with IntelliJ IDEA.
 * User: meenrajd
 * Date: 7/5/13
 * Time: 5:34 PM
 * <p/>
 * OLE Batch export process performs the export process for the given profile. Writes the exported data to marcxml or mrc file based on file type
 * in the process definition
 */
public class BatchProcessExportData extends AbstractBatchProcess {

    private static final Logger LOG = Logger.getLogger(BatchProcessExportData.class);

    private int start;
    private Date lastExportDate;
    private List<String> bibDocList = new ArrayList<String>();
    private File filePath;
    private String fileName;
    private int fileCount = 1;
    private static final String FULL_EXPORT = "(DocType:bibliographic) AND (DocFormat:marc)";
    private ExportDataService service;
    private int processedRec;
    private static final String RANGE = "range";
    private static final String AND = "AND";
    private static final String NONE = "none";
    private static final String PHRASE = "phrase";
    private static final String OR = "OR";
    private StringBuilder errBuilder;
    private String errCnt = "0";
    private static final String applicationUrl = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.OLEBatchProcess.BATCH_EXPORT_PATH_APP_URL);
    private static final String homeDirectory = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.USER_HOME_DIRECTORY);
    private SearchParams searchParams;
    private SearchResponse response;
    private OLEBatchProcessProfileFilterCriteriaBo staffOnlyCriteriaBo = null;
    private DocstoreClientLocator docstoreClientLocator;
    private SimpleDateFormat dateTimeFormat=new SimpleDateFormat("yyyy-MM-dd'T'HHmm");





    private int totalRecordsExported;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }


    /**
     * The method receives the profile information and performs the solr query to retrieve the
     * solr document list and calls the ExportService to get the export data and writes the
     * formatted data to the file system
     *
     * @return List<String> docList
     * @throws Exception
     */
    public Object[] batchExport(OLEBatchProcessProfileBo profileBo) throws Exception {
        return service.getExportDataBySolr(response.getSearchResults(), profileBo);
    }


    /**
     * Gets the filter criteria which is used to create the solr query
     *
     * @return solrQuery
     */
    public void getSolrQuery() throws Exception {
        searchParams = new SearchParams();
        searchParams.setStartIndex(start);
        searchParams.setPageSize(this.processDef.getChunkSize());
        searchParams.getSearchResultFields().addAll(getSearchResultFeilds());
        SortCondition sortCondition = new SortCondition();
        sortCondition.setSortField("dateEntered");
        sortCondition.setSortOrder("asc");
        searchParams.getSortConditions().add(sortCondition);
        getCriteria();
    }

    private List<SearchResultField> getSearchResultFeilds() {
        List<SearchResultField> searchResultFields = new ArrayList<>();
        List<String> resultFields = new ArrayList();
        //resultFields.add("id");
        resultFields.add("bibIdentifier");
        resultFields.add("holdingsIdentifier");
        resultFields.add("DocType");
        resultFields.add("LocalId_display");
        for (String resultField : resultFields) {
            SearchResultField searchResultField = new SearchResultField();
            searchResultField.setDocType("bibliographic");
            searchResultField.setFieldName(resultField);
            searchResultFields.add(searchResultField);
        }
        return searchResultFields;
    }

    public SearchResponse getDeleteSolrQuery() throws Exception {
        searchParams = new SearchParams();
        searchParams.setStartIndex(start);
        searchParams.setPageSize(processDef.getChunkSize());
        SearchResultField searchResultField = new SearchResultField();
        searchResultField.setDocType("bibliographic_delete");
        searchResultField.setFieldName("LocalId_display");
        searchParams.getSearchResultFields().add(searchResultField);
        SortCondition sortCondition = new SortCondition();
        sortCondition.setSortField("dateEntered");
        sortCondition.setSortOrder("asc");
        searchParams.getSortConditions().add(sortCondition);
        getDeleteCriteria();
        response = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
        return response;
    }

    public SearchResponse getIncrementalSolrQuery() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat(SOLR_DT_FORMAT);
        String fromDate = format.format(lastExportDate);
        searchParams = new SearchParams();
        SearchField searchField = searchParams.buildSearchField("", "dateUpdated", "[" + fromDate + " TO NOW]");
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition(NONE, searchField, "AND"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(null, "bibIdentifier"));
        searchField = searchParams.buildSearchField("", "staffOnlyFlag", Boolean.FALSE.toString());
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition(NONE, searchField, "AND"));
        response = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
        return response;
    }

    public SearchResponse getDeleteIncrementalSolrQuery() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat(SOLR_DT_FORMAT);
        String fromDate = format.format(lastExportDate);
        searchParams = new SearchParams();
        SearchField searchField = searchParams.buildSearchField("", "dateUpdated", "[" + fromDate + " TO NOW]");
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition(NONE, searchField, "AND"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(null, "bibIdentifier"));
        searchField = searchParams.buildSearchField("", "staffOnlyFlag", Boolean.TRUE.toString());
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition(NONE, searchField, "AND"));
        response = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
        return response;
    }

    /**
     * adds the filter criteria from the profile to search conditions as field value pair
     */
    private String getCriteria() throws ParseException {
        List<OLEBatchProcessProfileFilterCriteriaBo> criteriaBos = processDef.getOleBatchProcessProfileBo().getOleBatchProcessProfileFilterCriteriaList();
        if (processDef.getOleBatchProcessProfileBo().getExportScope().equalsIgnoreCase(EXPORT_INC)
                || processDef.getOleBatchProcessProfileBo().getExportScope().equalsIgnoreCase(INCREMENTAL_EXPORT_EX_STAFF)) {
            SearchCondition condition = getDefaultCondition();
            SearchField searchField = new SearchField();
            SearchCondition conditionStaffOnly = getDefaultCondition();
            SearchField searchFieldStaffOnly = new SearchField();

            SimpleDateFormat format = new SimpleDateFormat(SOLR_DT_FORMAT);
            String fromDate = format.format(lastExportDate);
            if (StringUtils.isNotEmpty(processDef.getBatchProcessProfileBo().getDataToExport())
                    && processDef.getBatchProcessProfileBo().getDataToExport().equalsIgnoreCase(EXPORT_BIB_ONLY)) {
                searchFieldStaffOnly.setDocType(DocType.BIB.getDescription());
                searchField.setDocType(DocType.BIB.getDescription());
                searchField.setFieldName("dateUpdated");
                searchField.setFieldValue("[" + fromDate + " TO NOW]");
                condition.setSearchScope(NONE);
                condition.setSearchField(searchField);
                searchParams.getSearchConditions().add(condition);
            }

            if (processDef.getOleBatchProcessProfileBo().getExportScope().equalsIgnoreCase(INCREMENTAL_EXPORT_EX_STAFF)) {
                searchFieldStaffOnly.setFieldName("staffOnlyFlag");
                searchFieldStaffOnly.setFieldValue(Boolean.FALSE.toString());
                conditionStaffOnly.setSearchScope(NONE);
                conditionStaffOnly.setSearchField(searchFieldStaffOnly);
                searchParams.getSearchConditions().add(conditionStaffOnly);
            }
            return "";
        } else if (processDef.getOleBatchProcessProfileBo().getExportScope().equalsIgnoreCase(EXPORT_FILTER)) {
            return getFilterCriteria(criteriaBos);
        } else {
            return "";
        }
    }

    private String getDeleteCriteria() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(SOLR_DT_FORMAT);
        String fromDate = format.format(lastExportDate);
        SearchCondition condition = getDefaultCondition();
        SearchField searchField = new SearchField();
        searchField.setDocType("bibliographic_delete");
        searchField.setFieldName("dateUpdated");
        searchField.setFieldValue("[" + fromDate + " TO NOW]");
        condition.setSearchScope(NONE);
        //condition.setOperator(RANGE);
        condition.setSearchField(searchField);
        searchParams.getSearchConditions().add(condition);
        return "";
    }

    @Override
    protected void prepareForRead() throws Exception {
        try {
            errBuilder = new StringBuilder();
            OLEBatchProcessProfileBo profileBo = processDef.getBatchProcessProfileBo();
            profileBo.setFileType(processDef.getOutputFormat());
            if (job.getStatus().equals(OLEConstants.OLEBatchProcess.JOB_STATUS_PAUSED)) {
                start = start + Integer.valueOf(job.getNoOfRecordsProcessed());
            }
          performSolrQuery();

            if(!profileBo.getExportScope().equalsIgnoreCase(EXPORT_FULL) && !profileBo.getExportScope().equalsIgnoreCase(EXPORT_EX_STAFF)) {
                updateJobProgress();
                incrementalFilterExport(profileBo);
            } else{
                batchProcessExportFetch();
                if(!job.getStatus().equalsIgnoreCase(OLEConstants.OLEBatchProcess.JOB_STATUS_CANCELLED)
                        && !job.getStatus().equalsIgnoreCase(OLEConstants.OLEBatchProcess.JOB_STATUS_PAUSED)
                        && !job.getStatus().equalsIgnoreCase(OLEConstants.OLEBatchProcess.JOB_STATUS_STOPPED)){
                    job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
                }
                updateJobProgress();
            }
        } catch (Exception ex) {
            LOG.error("Error while processing data :::", ex);
            job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_STOPPED);
            throw ex;
        }
    }

    /**
     * gets the next batch of records for export
     *
     * @throws Exception
     */
    @Override
    protected void getNextBatch() throws Exception {
        try {
            errBuilder = new StringBuilder();
            bibDocList.clear();
            start += processDef.getChunkSize();
            if (start > response.getTotalRecordCount()) {//no more next batch
                job.setStatus(JOB_STATUS_COMPLETED);
                return;
            }
            performSolrQuery();
            Object[] resultMap = null;
            OLEBatchProcessProfileBo profileBo = processDef.getBatchProcessProfileBo();
            profileBo.setFileType(processDef.getOutputFormat());
            if (response.getSearchResults().size() > 0) resultMap = batchExport(profileBo);
            if (resultMap == null || resultMap[0].equals("0")) {
                if (start < response.getTotalRecordCount()) {
                    getNextBatch();
                }
                job.setStatus(JOB_STATUS_COMPLETED);
            } else {
                fileCount++;
                if(StringUtils.isNotBlank(processDef.getDestinationDirectoryPath()))
                    fileName = processDef.getDestinationDirectoryPath() + "-" + dateTimeFormat.format(new Date()) + "-" + OLEConstants.OLEBatchProcess.PART  + fileCount;
                else if(StringUtils.isNotBlank(processDef.getBatchProcessName()))
                    fileName = processDef.getBatchProcessName() + "-" + dateTimeFormat.format(new Date()) + "-" + OLEConstants.OLEBatchProcess.PART  + fileCount;
                else
                    fileName = job.getJobId() + "-" + dateTimeFormat.format(new Date()) + "-" + OLEConstants.OLEBatchProcess.PART  + fileCount;
                //fileName = processDef.getBatchProcessProfileName() + "_" + fileCount;
                //bibDocList.add(XML_DEC);
                bibDocList.addAll((List<String>) resultMap[1]);
                processedRec = Integer.valueOf(resultMap[0].toString());
            }
            if (!job.getStatus().equals(JOB_STATUS_RUNNING) && errBuilder.length() != 0) {
                job.setStatusDesc("Batch Completed with Errors :: See Error file for details");
            }
            if (resultMap != null) {
                if (resultMap[2] != null)
                    errBuilder.append(resultMap[2].toString());
                if (resultMap[3] != null)
                    errCnt = resultMap[3].toString();
            }
        } catch (Exception ex) {
            LOG.error("Error while getNextBatch operation", ex);
            job.setStatus(JOB_STATUS_STOPPED);
            throw ex;
        }

    }

    /**
     * methods creates the directories to write file to if they do not exist
     * if the user provided folder is not valid (cannot be created) then the default location is chosen
     *
     * @throws Exception
     */
    @Override
    protected void prepareForWrite() throws Exception {
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            if(StringUtils.isNotBlank(processDef.getDestinationDirectoryPath()))
                fileName = processDef.getDestinationDirectoryPath()+ "-" + dateTimeFormat.format(new Date());
            else if(StringUtils.isNotBlank(processDef.getBatchProcessName()))
                fileName = processDef.getBatchProcessName()+ "-" + dateTimeFormat.format(new Date());
            else
                fileName = job.getJobId()+ "-" + dateTimeFormat.format(new Date());
            if (response.getSearchResults().size() >= processDef.getChunkSize()) {
                fileCount += (start / processDef.getChunkSize());
                //fileName = processDef.getBatchProcessProfileName() + "_" + fileCount;
                if(StringUtils.isNotBlank(processDef.getDestinationDirectoryPath()))
                    fileName = processDef.getDestinationDirectoryPath()+ "-" + dateTimeFormat.format(new Date()) + "-" + OLEConstants.OLEBatchProcess.PART  + fileCount;
                else if(StringUtils.isNotBlank(processDef.getBatchProcessName()))
                    fileName = processDef.getBatchProcessName() + "-" + dateTimeFormat.format(new Date()) + "-" + OLEConstants.OLEBatchProcess.PART  + fileCount;
                else
                    fileName = job.getJobId() + "-" + dateTimeFormat.format(new Date()) + "-" + OLEConstants.OLEBatchProcess.PART  + fileCount;
            }
            String homeDirectory = getBatchProcessFilePath(processDef.getBatchProcessType());
            filePath = new File(homeDirectory + FileSystems.getDefault().getSeparator() + job.getBatchProfileName());
            /*if (StringUtils.isNotEmpty(processDef.getDestinationDirectoryPath())) {
                filePath = new File(homeDirectory + FileSystems.getDefault().getSeparator() + processDef.getDestinationDirectoryPath());
            } else if (filePath == null || !filePath.isDirectory()) {
                filePath = new File(homeDirectory + FileSystems.getDefault().getSeparator() + job.getBatchProfileName());
            }*/
            if (filePath.isDirectory()) {
                //in case of paused and stopped status of job
                //job has already run and directory exists
                //LOG.info("filePath :: " + filePath.getPath() + " ::already exists");
            } else {
                if (filePath.mkdirs()) {
                    // able to create directory for the given file path
                    LOG.info("user given filePath :: " + filePath.getPath() + " ::created successfully");
                } else {
                    filePath = new File(homeDirectory + FileSystems.getDefault().getSeparator() + job.getBatchProfileName());
                    if (filePath.mkdirs())
                        LOG.info("default filePath :: " + filePath.getPath() + " ::created");
                    else {
                        LOG.error("Cannot create output directory for the given file path:: " + filePath.getPath());
                        job.setStatus(JOB_STATUS_STOPPED);
                        throw new RuntimeException("Cannot create output directory for the given file path:: " + filePath.getPath());
                    }
                }
            }
            job.setUploadFileName(filePath.getPath());
        } catch (Exception ex) {
            LOG.error("Error while prepareForWrite operation", ex);
            job.setStatus(JOB_STATUS_STOPPED);
            throw ex;
        }

    }


    public void prepareForWrite(String fileName) throws Exception {
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            processDef.setBatchProcessProfileName(fileName);
            if (response.getSearchResults().size() > processDef.getChunkSize()) {
                fileCount += (start / processDef.getChunkSize());
                fileName = processDef.getBatchProcessProfileName() + "_" + fileCount;
            }
            String homeDirectory = getBatchProcessFilePath(processDef.getBatchProcessType());
            filePath = new File(homeDirectory + FileSystems.getDefault().getSeparator() + job.getBatchProfileName());
            /*if (StringUtils.isNotEmpty(processDef.getDestinationDirectoryPath())) {
                filePath = new File(homeDirectory + FileSystems.getDefault().getSeparator() + job.getBatchProfileName());
            } else if (filePath == null || !filePath.isDirectory()) {
                filePath = new File(homeDirectory + FileSystems.getDefault().getSeparator() + job.getBatchProfileName());
            }*/
            if (filePath.isDirectory()) {
                //in case of paused and stopped status of job
                //job has already run and directory exists
                //LOG.info("filePath :: " + filePath.getPath() + " ::already exists");
            } else {
                if (filePath.mkdirs()) {
                    // able to create directory for the given file path
                    LOG.info("user given filePath :: " + filePath.getPath() + " ::created successfully");
                } else {
                    filePath = new File(homeDirectory + FileSystems.getDefault().getSeparator() + job.getBatchProfileName());
                    if (filePath.mkdirs())
                        LOG.info("default filePath :: " + filePath.getPath() + " ::created");
                    else {
                        LOG.error("Cannot create output directory for the given file path:: " + filePath.getPath());
                        job.setStatus(JOB_STATUS_STOPPED);
                        throw new RuntimeException("Cannot create output directory for the given file path:: " + filePath.getPath());
                    }
                }
            }
            job.setUploadFileName(filePath.getPath());
        } catch (Exception ex) {
            LOG.error("Error while prepareForWrite operation", ex);
            job.setStatus(JOB_STATUS_STOPPED);
            throw ex;
        }

    }

    /**
     * Performs the batch write operation
     * Writes the data to marcxml or mrc format based on the output format specified in the process
     *
     * @throws Exception
     */
    @Override
    protected void processBatch() throws Exception {
        int currSuccessRec = 0;
        int successRec = Integer.valueOf(job.getNoOfSuccessRecords());
        int recordProcessed = Integer.valueOf(job.getNoOfRecordsProcessed());
        int errRecords = Integer.valueOf(job.getNoOfFailureRecords());
        int currErrCnt = Integer.valueOf(errCnt);
        if (processDef.getOutputFormat().equalsIgnoreCase(MARCXML)) {
            try {
                if (processedRec > 0)
                    writeFileToLocation();
                currSuccessRec = processedRec;
            } catch (Exception e) {
                LOG.error("Error while writing to file:: marcxml ::", e);
                job.setStatus(JOB_STATUS_STOPPED);
                job.setStatusDesc("Error while writing to marcxml file::" + fileName + EXT_MARCXML);
                currSuccessRec = 0;
                currErrCnt += processedRec;
            }
        } else if (processDef.getOutputFormat().equalsIgnoreCase(MARC)) {
            try {
                currSuccessRec = generateMarcFromXml();
                currErrCnt += processedRec - currSuccessRec;
            } catch (Exception e) {
                LOG.error("Error while writing to file:: mrc ::", e);
            }
        }
        try {
            writeErrorFile();
        } catch (Exception ex) {
            LOG.error("Error while writing to error file", ex);
            job.setStatus(JOB_STATUS_STOPPED);
        }

        job.setNoOfRecordsProcessed(String.valueOf(recordProcessed + currSuccessRec + currErrCnt));
        //job.setTotalNoOfRecords(String.valueOf(response.getTotalRecordCount()));
        job.setNoOfFailureRecords(String.valueOf(errRecords + currErrCnt));
        job.setNoOfSuccessRecords(String.valueOf(successRec + currSuccessRec));
        LOG.debug(job.getNoOfRecordsProcessed() + " ::records processed");
        if (currErrCnt > 0)
            LOG.debug(job.getNoOfFailureRecords() + " ::records failed");
    }

    /**
     * Write the content read to mrcxml file
     *
     * @throws Exception
     */
    public void writeFileToLocation() throws Exception {
        File file = new File(filePath + FileSystems.getDefault().getSeparator() + fileName + EXT_MARCXML);
        FileUtils.writeLines(file, "UTF-8", bibDocList, true);
    }

    public void writeFileToLocation(String fileName) throws Exception {
        File file = new File(filePath + FileSystems.getDefault().getSeparator() + fileName + EXT_MARCXML);
        FileUtils.writeLines(file, "UTF-8", bibDocList, true);
    }

    /**
     * Writes the content read into a mrc file
     *
     * @throws Exception
     */
    public int generateMarcFromXml() throws Exception {
        StopWatch timer = new StopWatch();
        timer.start();
        int successRec = 0;
        File fileToWrite = new File(filePath + FileSystems.getDefault().getSeparator() + fileName + EXT_MARC);
        FileOutputStream fileOutputStream = new FileOutputStream(fileToWrite, true);
        //String bibContent = StringUtils.join(bibDocList, "");
        if (!fileToWrite.exists()) {
            if (fileToWrite.getParentFile().mkdirs() && fileToWrite.createNewFile()) {
                //do nothing
            } else {
                LOG.error("Cannot create mrc file in the given file path :: " + fileToWrite.getPath());
                job.setStatus(JOB_STATUS_STOPPED);
                throw new RuntimeException("Cannot create mrc file in the given file path :: " + fileToWrite.getPath());
            }
        }
        MarcSplitStreamWriter writer = new MarcSplitStreamWriter(fileOutputStream, OleNGConstants.UTF_8,300000,"880");
        int errorCount = 0;
        for (String bibContent : bibDocList) {
            InputStream input = new ByteArrayInputStream(bibContent.getBytes());
            Record record = null;
            OLEMarcReader marcXmlReader = new OLEMarcXmlReader(input);
            try {
                while (marcXmlReader.hasNext()) {
                    if (marcXmlReader.hasErrors()) {
                        marcXmlReader.next();
                        errBuilder.append(marcXmlReader.getError().toString()).append(lineSeparator);
                        errorCount++;
                        marcXmlReader.clearErrors();
                        continue;
                    }
                    record = marcXmlReader.next();
                    writer.write(record);
                    successRec++;
                }

            } catch (Exception ex) {
                String recordId = getRecordId(record);
                LOG.error("Error while parsing MARCXML to mrc data:: " + (recordId == null ? "NULL_RECORD" : "record id:: " + recordId), ex);
                errBuilder.append(ERR_BIB).append(recordId == null ? "ERROR_RECORD" : recordId).append(TIME_STAMP)
                        .append(new Date()).append(ERR_CAUSE).append(ex.getMessage()).append(" ::At:: ").append("generateMarcFromXml() For Record ::"+bibContent).append(lineSeparator);
            }
        }
        writer.close();
        timer.stop();
        return successRec;
    }

    /**
     * Converts the given date string to solr date format
     * // convert the format to yyyy-MM-dd'T'HH:mm:ss'Z'
     *
     * @param dateStr
     * @param isFrom
     * @return
     */
    private String getSolrDate(String dateStr, boolean isFrom) throws ParseException {
        SimpleDateFormat solrDtFormat = new SimpleDateFormat(SOLR_DT_FORMAT);
        SimpleDateFormat userFormat = new SimpleDateFormat(FILTER_DT_FORMAT);
        try {
            if (isFrom) {
                Date date = userFormat.parse(dateStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
                return solrDtFormat.format(cal.getTime());
            } else {
                Date date = userFormat.parse(dateStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 23, 59, 59);
                return solrDtFormat.format(cal.getTime());
            }
        } catch (ParseException e) {
            LOG.error("Error while parsing user entered date::" + dateStr, e);
            throw e;
        }
    }

    /**
     * loads the profile and checks for incremental export
     *
     * @param processDef
     */
    @Override
    protected void loadProfile(OLEBatchProcessDefinitionDocument processDef) throws Exception {
        super.loadProfile(processDef);
        if (processDef.getBatchProcessType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_EXPORT) && processDef.getLoadIdFromFile().equalsIgnoreCase("true")) {
            String batchProcessFileContent = getBatchProcessFileContent();
            if (processDef.getOleBatchProcessProfileBo().getOleBatchProcessProfileFilterCriteriaList().size() == 1 && processDef.getOleBatchProcessProfileBo().getOleBatchProcessProfileFilterCriteriaList().get(0).getFilterFieldName().equalsIgnoreCase(LOCAL_ID_DISPLAY)) {
                processDef.getOleBatchProcessProfileBo().getOleBatchProcessProfileFilterCriteriaList().get(0).setFilterFieldValue(batchProcessFileContent);
            }
        }
        List<OLEBatchProcessProfileMappingOptionsBo> optionsBoList = processDef.getOleBatchProcessProfileBo().getOleBatchProcessProfileMappingOptionsList();
        for (OLEBatchProcessProfileMappingOptionsBo bo : optionsBoList) {
            processDef.getOleBatchProcessProfileBo().getOleBatchProcessProfileDataMappingOptionsBoList().addAll(bo.getOleBatchProcessProfileDataMappingOptionsBoList());
        }
        try {
            if (processDef.getOleBatchProcessProfileBo().getExportScope().equalsIgnoreCase(EXPORT_INC)  || processDef.getOleBatchProcessProfileBo().getExportScope().equalsIgnoreCase(INCREMENTAL_EXPORT_EX_STAFF) ) {
                Calendar cal = Calendar.getInstance();
                String jobId_FL = "start_time";
                Map<String, String> jobBoMap = new HashMap<>();
                jobBoMap.put("bat_prfle_nm", processDef.getBatchProcessProfileName());
                jobBoMap.put("status", JOB_STATUS_COMPLETED);
                // Gets adhoc jobs and scheduled jobs that ran with the profile name and status completed.
                List<OLEBatchProcessJobDetailsBo> jobDetailsBos = (List<OLEBatchProcessJobDetailsBo>) KRADServiceLocator.getBusinessObjectService().findMatchingOrderBy(OLEBatchProcessJobDetailsBo.class, jobBoMap, jobId_FL, true);
                if (jobDetailsBos == null || CollectionUtils.isEmpty(jobDetailsBos)) {
                    jobId_FL = "CRTE_TIME";
                    jobBoMap.clear();
                    jobBoMap.put("batchProcessId", processDef.getBatchProcessId());
                    // Gets scheduled job running for the first time to set the creation time as last export date.
                    List<OLEBatchProcessScheduleBo> scheduleBos = (List<OLEBatchProcessScheduleBo>) KRADServiceLocator.getBusinessObjectService().findMatchingOrderBy(OLEBatchProcessScheduleBo.class, jobBoMap, jobId_FL, true);
                    if (scheduleBos == null || CollectionUtils.isEmpty(scheduleBos)) {
                        // Job start time is set as last export date for the adhoc jobs that are running for the first time.
                        cal.setTime(job.getStartTime());
                    } else {
                        cal.setTime(scheduleBos.get(scheduleBos.size() - 1).getCreateTime());
                    }
                } else {
                    cal.setTime(jobDetailsBos.get(jobDetailsBos.size() - 1).getStartTime());
                }
                lastExportDate = getUTCTime(cal.getTime());

                LOG.info("Incremental export running for batch export profile :: " + processDef.getBatchProcessProfileName() + " :: with date ::" + lastExportDate);
            }
        } catch (Exception ex) {
            LOG.error("Error while retrieving job details for incremental export::");
            throw ex;
        }
    }

    /**
     * gets the solr query based on the filter criteria and executed to retrieve the results
     *
     * @throws Exception
     */
    public void performSolrQuery() throws Exception {
        try {
            if (searchParams == null) {
                getSolrQuery();
                if (!(processDef.getOleBatchProcessProfileBo().getExportScope().equalsIgnoreCase(EXPORT_FULL)
                        || processDef.getOleBatchProcessProfileBo().getExportScope().equalsIgnoreCase(EXPORT_EX_STAFF))) {
                    processResults();
                    if (response.getSearchResults().size() == 0) {
                        job.setTotalNoOfRecords("0");
                    } else {
                        List<SearchResult> searchResults = response.getSearchResults();
                        int totalRecordCount = getTotalRecordCount(searchResults);
                        job.setTotalNoOfRecords(String.valueOf(totalRecordCount));
                    }
                } else {
                    response = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                    job.setTotalNoOfRecords(String.valueOf(response.getTotalRecordCount()));
                }
                LOG.info("Total number of records to be exported :: " + job.getTotalNoOfRecords());

            } else {
                searchParams.setStartIndex(start);
                response = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                job.setTotalNoOfRecords(String.valueOf(response.getTotalRecordCount()));
            }


        } catch (Exception e) {
            LOG.error("Error while performing solr query :: ", e);
            throw e;
        }
    }

    private int getTotalRecordCount(List<SearchResult> searchResults){
        Set<String> bibIds = new TreeSet<>();
        if (searchResults.size() > 0) {
            for (SearchResult searchResult : searchResults) {
                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                    if (searchResultField.getDocType().equalsIgnoreCase("bibliographic") && searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                        String bibId = searchResultField.getFieldValue();
                        bibIds.add(bibId);
                    }

                }
            }
        }
        return bibIds.size();
    }

    /**
     * converts the given date to UTC time
     *
     * @param date
     * @return
     * @throws ParseException
     */
    private static Date getUTCTime(Date date) throws ParseException {
        DateFormat format = new SimpleDateFormat(SOLR_DT_FORMAT);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String utcStr = format.format(date);
        DateFormat format2 = new SimpleDateFormat(SOLR_DT_FORMAT);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(format2.parse(utcStr));
        return cal.getTime();
    }

    /**
     * gets the default search condition
     *
     * @return
     */
    private SearchCondition getDefaultCondition() {
        SearchCondition condition = new SearchCondition();
        condition.setSearchScope(AND);
        condition.setOperator(AND);
        return condition;
    }

    /**
     * Writes the error records to the path
     *
     * @throws Exception
     */
    public void writeErrorFile() throws Exception {
        if (errBuilder != null && StringUtils.isNotEmpty(errBuilder.toString())) {
            File file = new File(filePath + FileSystems.getDefault().getSeparator() + processDef.getBatchProcessProfileName() + EXT_ERR_TXT);
            FileUtils.writeStringToFile(file, errBuilder.toString(), "UTF-8", true);
        }
    }

    /**
     * reads the filter criterias from the profile and sets the search params for solr query
     *
     * @param criteriaBos
     * @return
     * @throws ParseException
     */
    private String getFilterCriteria(List<OLEBatchProcessProfileFilterCriteriaBo> criteriaBos) throws ParseException {
        //OLEBatchProcessProfileFilterCriteriaBo staffOnlyCriteriaBo = null;
        for (OLEBatchProcessProfileFilterCriteriaBo bo : criteriaBos) {
            if (processDef.getOleBatchProcessProfileBo().getExportScope().equalsIgnoreCase(EXPORT_INC)
                    && bo.getFilterFieldName().equalsIgnoreCase((OLEConstants.OLEBatchProcess.DATE_UPDATED))) {
                continue;// do not add dateUpdated in params even if its present in the filter as it will be taken from last job run of the same profile
            }
            if (bo.getFilterFieldName().equalsIgnoreCase(OLEConstants.OLEBatchProcess.STAFF_ONLY_FLAG)) {
                staffOnlyCriteriaBo = bo;
                //continue;
            }
            SearchCondition condition = getDefaultCondition();
            SearchField searchField = new SearchField();
            searchField.setFieldName(bo.getFilterFieldName());
            //condition.setDocField(bo.getFilterFieldName());
            if (StringUtils.isNotEmpty(bo.getFilterFieldValue())) { // one value
                Map<String, String> filterMap = new HashMap<>();
                filterMap.put("ole_bat_field_nm", bo.getFilterFieldName());
                Collection<OLEBatchProcessFilterCriteriaBo> filterBo = KRADServiceLocator.getBusinessObjectService().findMatching(OLEBatchProcessFilterCriteriaBo.class, filterMap);
                if (filterBo.iterator().hasNext()) {
                    OLEBatchProcessFilterCriteriaBo oleBatchProcessFilterCriteriaBo = filterBo.iterator().next();
                    if (oleBatchProcessFilterCriteriaBo.getFieldType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DATE)) {
                        searchField.setFieldValue("[" + getSolrDate(bo.getFilterFieldValue(), true) + " TO " + getSolrDate(bo.getFilterFieldValue(), false) + "]");
                        //condition.setSearchText("[" + getSolrDate(bo.getFilterFieldValue(), true) + " TO " + getSolrDate(bo.getFilterFieldValue(), false) + "]");
                        condition.setSearchScope(NONE);
                        //condition.setOperator(RANGE);
                        if (filterBo.iterator().next().getFieldName().equalsIgnoreCase(OLEConstants.OLEBatchProcess.STATUS_UPDATED_ON)) {
                            searchField.setDocType(DocType.BIB.getDescription());
                        }
                    } else if(oleBatchProcessFilterCriteriaBo.getFieldDisplayName().equalsIgnoreCase(OLEConstants.OLEBatchProcess.OLE_BATCH_FLTR_CRITERIA_LOAD_FROM_FILE)){
                         buildSearchConditions(bo.getFilterFieldValue());
                    } else if (oleBatchProcessFilterCriteriaBo.getFieldDisplayName().equalsIgnoreCase(OLEConstants.OLEBatchProcess.OLE_BATCH_FLTR_CRITERIA_BIB_STATUS)) {
                        buildSearchConditionsForStatus(bo.getFilterFieldValue());
                    } else {
                        searchField.setDocType(DocType.BIB.getDescription());
                        searchField.setFieldValue(bo.getFilterFieldValue());
                        /*if (bo.getFilterFieldName().equalsIgnoreCase(OLEConstants.OLEBatchProcess.STATUS_SEARCH)) {
                            //To set bib status values as 'Catalogued' or 'Cataloguing' or 'None' in case sensitive.
                            searchField.setFieldValue(StringUtils.capitalize(StringUtils.lowerCase(bo.getFilterFieldValue())));
                        } else {
                            searchField.setFieldValue(bo.getFilterFieldValue());
                        }*/
                        //condition.setSearchText(bo.getFilterFieldValue());
                    }
                } else {
                    try {
                        InstanceMappingHelper instanceMappingHelper = new InstanceMappingHelper();
                        String filterFieldNameTag = instanceMappingHelper.getTagForExportFilter(bo.getFilterFieldName());
                        String filterFieldNameCode = instanceMappingHelper.getCodeForExportFilter(bo.getFilterFieldName());
                        if (StringUtils.isEmpty(filterFieldNameTag) || StringUtils.isEmpty(filterFieldNameCode)) {
                            searchField.setFieldName(bo.getFilterFieldName());
                        } else {
                            // Convert marc data field tag into its corresponding solr field
                            filterFieldNameTag = OLEConstants.OLEBatchProcess.DYNAMIC_FIELD_PREFIX + filterFieldNameTag;
                            String docField = filterFieldNameTag + filterFieldNameCode;
                            searchField.setFieldName(docField);
                        }
                        condition.setSearchScope(NONE);
                    } catch (StringIndexOutOfBoundsException e) {
                        searchField.setFieldName(bo.getFilterFieldName());
                    }
                    searchField.setDocType(DocType.BIB.getDescription());
                    searchField.setFieldValue(bo.getFilterFieldValue());
                }
            } else if (StringUtils.isNotEmpty(bo.getFilterRangeFrom()) && StringUtils.isNotEmpty(bo.getFilterRangeTo())) {
                // range values
                //condition.setOperator(RANGE);
                condition.setSearchScope(NONE);
                Map<String, String> filterMap = new HashMap<>();
                filterMap.put("ole_bat_field_nm", bo.getFilterFieldName());
                Collection<OLEBatchProcessFilterCriteriaBo> filterBo = KRADServiceLocator.getBusinessObjectService().findMatching(OLEBatchProcessFilterCriteriaBo.class, filterMap);
                if (!filterBo.iterator().hasNext()) return "";
                if (filterBo.iterator().next().getFieldType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DATE)) {
                    searchField.setFieldValue("[" + getSolrDate(bo.getFilterRangeFrom(), true) + " TO " + getSolrDate(bo.getFilterRangeTo(), false) + "]");
                    if (filterBo.iterator().next().getFieldName().equalsIgnoreCase(OLEConstants.OLEBatchProcess.STATUS_UPDATED_ON)) {
                        searchField.setDocType(DocType.BIB.getDescription());
                    }
                } else {
                    searchField.setFieldValue("[" + bo.getFilterRangeFrom() + " TO " + bo.getFilterRangeTo() + "]");
                }
            } else if (StringUtils.isNotEmpty(bo.getFilterRangeFrom()) && StringUtils.isEmpty(bo.getFilterRangeTo())) {   // range values
                //condition.setOperator(RANGE);
                condition.setSearchScope(NONE);
                Map<String, String> filterMap = new HashMap<>();
                filterMap.put("ole_bat_field_nm", bo.getFilterFieldName());
                Collection<OLEBatchProcessFilterCriteriaBo> filterBo = KRADServiceLocator.getBusinessObjectService().findMatching(OLEBatchProcessFilterCriteriaBo.class, filterMap);
                if (!filterBo.iterator().hasNext()) return "";
                if (filterBo.iterator().next().getFieldType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DATE)) {
                    searchField.setFieldValue("[" + getSolrDate(bo.getFilterRangeFrom(), true) + " TO NOW]");
                } else {
                    searchField.setFieldValue("[" + bo.getFilterRangeFrom() + " TO *]");
                }
            }
            //to check if bib status or local id is present in the filter criteria, then select only the bib records by setting export type as full
            if (bo.getFilterFieldName().equalsIgnoreCase(OLEConstants.OLEBatchProcess.LOCAL_ID_SEARCH) || bo.getFilterFieldName().equalsIgnoreCase(OLEConstants.OLEBatchProcess.STATUS_SEARCH)
                    || (StringUtils.isNotEmpty(processDef.getBatchProcessProfileBo().getDataToExport()) && processDef.getBatchProcessProfileBo().getDataToExport().equalsIgnoreCase(EXPORT_BIB_ONLY) && !bo.getFilterFieldName().equalsIgnoreCase(LOCAL_ID_DISPLAY))) {
                processDef.getOleBatchProcessProfileBo().setExportScope(EXPORT_FULL);
                //searchParams.setDocFormat(DocFormat.MARC.getDescription());
                searchField.setDocType(DocType.BIB.getDescription());
                /*if (bo.getFilterFieldName().equalsIgnoreCase(OLEConstants.OLEBatchProcess.STATUS_SEARCH))
                    condition.setSearchScope(PHRASE);*/
            }
            if (!bo.getFilterFieldName().equalsIgnoreCase(LOCAL_ID_DISPLAY) && !bo.getFilterFieldName().equalsIgnoreCase(STATUS_SEARCH)) {
                condition.setSearchField(searchField);
                searchParams.getSearchConditions().add(condition);
            }
        }
        return "";
    }

    /**
     * returns the record id - local identifier of the record
     *
     * @param record
     * @return
     */
    private String getRecordId(Record record) {
        if (record == null || record.getControlFields() == null || record.getControlFields().isEmpty() || record.getControlFields().size() < 2)
            return null;
        if (record.getControlFields().get(1) == null) return null;
        VariableField field = (VariableField) record.getControlFields().get(1);
        if (field instanceof ControlField) {
            return ((ControlField) field).getData();
        } else {
            return null;
        }

    }

    private void incrementalFilterExport(OLEBatchProcessProfileBo profileBo) throws Exception {
        Object[] resultMap = null;

        //Write deleted bibid's  to a file
        if (profileBo.getExportScope().equals(EXPORT_INC)
                || profileBo.getExportScope().equalsIgnoreCase(INCREMENTAL_EXPORT_EX_STAFF)) {
            if (lastExportDate != null) {
                StringBuilder deleteId = new StringBuilder();
                if ( profileBo.getExportScope().equalsIgnoreCase(INCREMENTAL_EXPORT_EX_STAFF)) {
                     response = getIncrementalSolrQuery();
                }

                if (response.getSearchResults().size() > 0) {
                    service = new ExportDataServiceImpl();
                    resultMap = batchExport(profileBo);
                }
                SearchResponse responseDelete= getDeleteIncrementalSolrQuery();
                Set<String> incrementalBibIds = new HashSet<>();
                for (SearchResult searchResult : responseDelete.getSearchResults()) {
                    for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                        if (searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                            if (incrementalBibIds.add(searchResultField.getFieldValue())) {
                                deleteId.append(DocumentUniqueIDPrefix.getDocumentId(searchResultField.getFieldValue())).append(COMMA);
                            }
                        }
                    }
                }
                responseDelete = getDeleteSolrQuery();
                if (responseDelete.getSearchResults().size() > 0 || deleteId != null) {
                    if (responseDelete.getSearchResults().size() > 0) {
                        Iterator<SearchResult> iterator = responseDelete.getSearchResults().iterator();
                        while (iterator.hasNext()) {
                            SearchResult searchresult = iterator.next();
                            if (null != searchresult && searchresult.getSearchResultFields() != null) {
                                for (SearchResultField searchResultField : searchresult.getSearchResultFields()) {
                                    if (searchResultField.getFieldName().equalsIgnoreCase("LocalId_display")) {
                                        String id = searchResultField.getFieldValue();
                                        if (id != null) {
                                            deleteId.append(id);
                                            deleteId.append(",");
                                        }
                                    }
                                }
                            }
                        }
                    }
                    String deleted = "";
                    if (deleteId.length() > 0) {
                        deleted = deleteId.substring(0, deleteId.length());
                    }
                    if (deleted.length() > 0 && deleted != "") {
                        String ids[] = deleted.split(",");
                        createFile(ids,OLEConstants.OLEBatchProcess.DELETED_BIB_IDS_FILE_NAME);
                    }
                    if (job.getTotalNoOfRecords() != null) {
                        job.setTotalNoOfRecords(String.valueOf(Integer.valueOf(job.getTotalNoOfRecords()) + response.getTotalRecordCount()));
                    } else {
                        job.setTotalNoOfRecords(String.valueOf(response.getTotalRecordCount()));
                    }
                    if (job.getNoOfRecordsProcessed() != null) {
                        job.setNoOfRecordsProcessed(String.valueOf(Integer.valueOf(job.getNoOfRecordsProcessed()) + response.getSearchResults().size()));
                    } else {
                        job.setNoOfRecordsProcessed(String.valueOf(response.getSearchResults().size()));
                    }
                    if (job.getNoOfSuccessRecords() != null) {
                        job.setNoOfSuccessRecords(String.valueOf(Integer.valueOf(job.getNoOfSuccessRecords()) + response.getSearchResults().size()));
                    } else {
                        job.setNoOfSuccessRecords(String.valueOf(response.getSearchResults().size()));
                    }
                }
            }
        }else{
            if (response.getSearchResults().size() > 0) {
                service = new ExportDataServiceImpl();
                resultMap = batchExport(profileBo);
            }
        }

        String homeDirectory = getBatchProcessFilePath(processDef.getBatchProcessType());
        if (StringUtils.isNotEmpty(processDef.getDestinationDirectoryPath())) {
            filePath = new File(processDef.getDestinationDirectoryPath() + FileSystems.getDefault().getSeparator() + job.getJobId());
        } else if (filePath == null || !filePath.isDirectory()) {
            filePath = new File(homeDirectory + FileSystems.getDefault().getSeparator() + job.getJobId());
        }
        job.setUploadFileName(filePath.getPath());
        if (resultMap == null || resultMap[0].equals("0")) {
            job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
        } else {
            //bibDocList.add(XML_DEC);
            bibDocList.addAll((List<String>) resultMap[1]);
            processedRec = Integer.valueOf(resultMap[0].toString());
        }
        if (resultMap != null) {
            if (resultMap[2] != null)
                errBuilder.append(resultMap[2].toString());
            if (resultMap[3] != null)
                errCnt = resultMap[3].toString();
        }
    }


    public void batchProcessExportFetch() throws Exception {
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        int totalRecords = 0;
        int remainingRecords = 0;
        int recordsToBeExportedToFile = 0;
        int recProcessed = 0;
        Boolean isBibOnly=true;

        if (!processDef.getBatchProcessProfileBo().getOleBatchProcessProfileMappingOptionsList().isEmpty()
                && StringUtils.isNotEmpty(processDef.getBatchProcessProfileBo().getDataToExport()) && (processDef.getBatchProcessProfileBo().getDataToExport().equalsIgnoreCase(OLEBatchProcess.EXPORT_BIB_AND_INSTANCE) || processDef.getBatchProcessProfileBo().getDataToExport().equalsIgnoreCase(OLEBatchProcess.EXPORT_BIB_INSTANCE_AND_EINSTANCE))) {
            isBibOnly=false;
        }
        StopWatch timer = new StopWatch();
        timer.start();

        Date date =new Date();
        LOG.info("Batch Export Started : " +date.toString());

        BatchBibTreeDBUtil bibTreeDBUtil = null;
        if(processDef.getBatchProcessProfileBo().getExportScope().equalsIgnoreCase(EXPORT_EX_STAFF)){
            bibTreeDBUtil= new BatchBibTreeDBUtil(false);
        }else{

            bibTreeDBUtil= new BatchBibTreeDBUtil();
        }


        job.setTotalNoOfRecords(bibTreeDBUtil.getTotalNoOfRecords());
        updateJobProgress();


        bibTreeDBUtil.init(0, 0,null);

        recProcessed = Integer.parseInt(job.getNoOfRecordsProcessed());
        totalRecords = Integer.parseInt(job.getTotalNoOfRecords());


        int fileNumber = 1;
        while (recProcessed < totalRecords) {
            remainingRecords = totalRecords - recProcessed;
            recordsToBeExportedToFile = Math.min(processDef.getMaxRecordsInFile(), remainingRecords);
            if(totalRecords >= processDef.getMaxRecordsInFile()){
                if (StringUtils.isNotBlank(processDef.getDestinationDirectoryPath())) {
                    fileName = processDef.getDestinationDirectoryPath() + "-" + dateTimeFormat.format(new Date())+ "-" + OLEConstants.OLEBatchProcess.PART + fileNumber;
                }else if (StringUtils.isNotBlank(processDef.getBatchProcessName())) {
                    fileName = processDef.getBatchProcessName() + "-" + dateTimeFormat.format(new Date()) + "-" + OLEConstants.OLEBatchProcess.PART + fileNumber;
                } else {
                    fileName = job.getJobId()+ "-" +  dateTimeFormat.format(new Date()) + "-" + OLEConstants.OLEBatchProcess.PART + fileNumber;
                }
            }else{
                if (StringUtils.isNotBlank(processDef.getDestinationDirectoryPath())) {
                    fileName = processDef.getDestinationDirectoryPath() + "-" + dateTimeFormat.format(new Date());
                }else if (StringUtils.isNotBlank(processDef.getBatchProcessName())) {
                    fileName = processDef.getBatchProcessName() + "-" + dateTimeFormat.format(new Date());
                } else {
                    fileName = job.getJobId()+ "-" +  dateTimeFormat.format(new Date());
                }
            }
            fileNumber++;
            if (!job.getStatus().equalsIgnoreCase(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED)
                    && !job.getStatus().equalsIgnoreCase(OLEConstants.OLEBatchProcess.JOB_STATUS_CANCELLED)
                    && !job.getStatus().equalsIgnoreCase(OLEConstants.OLEBatchProcess.JOB_STATUS_PAUSED)
                    && !job.getStatus().equalsIgnoreCase(OLEConstants.OLEBatchProcess.JOB_STATUS_STOPPED)) {

                BatchExportFetch batchExportFetch = new BatchExportFetch(bibTreeDBUtil, recordsToBeExportedToFile, fileName, this, processDef, isBibOnly);
                batchExportFetch.call();
                recProcessed += recordsToBeExportedToFile;

            } else {

                break;
            }

        }

        timer.stop();
        date =new Date();
        LOG.info("Batch Export Ended : " + date.toString() + " Time Taken : " + timer.toString());
        bibTreeDBUtil.closeConnections();
        try {
            writeErrorFile();
        } catch (Exception ex) {
            job.setStatus(JOB_STATUS_STOPPED);
        }
    }

    private void buildSearchConditions(String ids){
        String[] filterFieldValues = ids.split("\n");
        for(String id : filterFieldValues){
            SearchCondition searchCondition = new SearchCondition();
            searchCondition.setOperator(OR);
            searchCondition.setSearchScope(NONE);
            SearchField searchField = new SearchField();
            searchField.setDocType(DocType.BIB.getDescription());
            searchField.setFieldName(LOCAL_ID_SEARCH);
            searchField.setFieldValue(id);
            searchCondition.setSearchField(searchField);
            searchParams.getSearchConditions().add(searchCondition);
        }
    }

    /**
     * This method will build the search condition with the bib statuses and adds it to search params.
     * @param fieldValue
     */
    private void buildSearchConditionsForStatus(String fieldValue) {
        String[] filterFieldValues = fieldValue.split(",");
        StringBuilder bibStatusBuilder = new StringBuilder();
        // building the search condition field value
        String bibStatus = "";
        for (int i = 0; i < filterFieldValues.length; i++) {
            bibStatus = filterFieldValues[i];
            if (StringUtils.isNotBlank(bibStatus)) {
                bibStatusBuilder.append("\"" + bibStatus + "\"");
                if (i != filterFieldValues.length - 1) {
                    bibStatusBuilder.append(OR);
                }
            }
        }
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setOperator(AND);
        searchCondition.setSearchScope(NONE);
        SearchField searchField = new SearchField();
        searchField.setDocType(DocType.BIB.getDescription());
        searchField.setFieldName(STATUS_SEARCH);
        searchField.setFieldValue(bibStatusBuilder.toString());
        searchCondition.setSearchField(searchField);
        searchParams.getSearchConditions().add(searchCondition);
    }

    private void processResults() throws Exception {
        List<SearchResult> searchResultList = new ArrayList<>();

        if (processDef.getLoadIdFromFile().equalsIgnoreCase("true")) {
            List<SearchCondition> searchConditions = new ArrayList<>();
            searchConditions.addAll(searchParams.getSearchConditions());
            List<List<SearchCondition>> searchConditionLists = Lists.partition(searchConditions, 300);
            for(List<SearchCondition> searchConditionList:searchConditionLists){
                searchParams.getSearchConditions().clear();
                searchParams.getSearchConditions().addAll(searchConditionList);
                SearchResponse responseLocal = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                searchResultList.addAll(responseLocal.getSearchResults());
            }
        } else {
            if (StringUtils.isNotEmpty(processDef.getBatchProcessProfileBo().getDataToExport())
                    && (processDef.getBatchProcessProfileBo().getDataToExport().equalsIgnoreCase("BIBANDHOLDINGS") || processDef.getBatchProcessProfileBo().getDataToExport().equalsIgnoreCase("BIBHOLDINGSEHOLDINGS"))) {
                getQueryForDocument(DocType.BIB.getDescription());
                SearchResponse responseBib = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                searchResultList.addAll(responseBib.getSearchResults());

                getQueryForDocument(DocType.HOLDINGS.getDescription());
                SearchResponse responsHoldings = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                searchResultList.addAll(responsHoldings.getSearchResults());

                getQueryForDocument(DocType.ITEM.getDescription());
                SearchResponse responsItem = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                searchResultList.addAll(responsItem.getSearchResults());
            } else {
                getQueryForDocument(DocType.BIB.getDescription());
                SearchResponse responseBib = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                searchResultList.addAll(responseBib.getSearchResults());

            }

        }

        response = new SearchResponse();
        response.getSearchResults().addAll(searchResultList);
    }

    private void getQueryForDocument( String documnet) {
        SimpleDateFormat format = new SimpleDateFormat(SOLR_DT_FORMAT);
        String fromDate = format.format(lastExportDate);

        SearchCondition condition = getDefaultCondition();
        SearchField searchField = new SearchField();
        searchField.setDocType(documnet);
        searchField.setFieldName("dateUpdated");
        searchField.setFieldValue("[" + fromDate + " TO NOW]");
        condition.setSearchScope(NONE);
        condition.setSearchField(searchField);
        searchParams.getSearchConditions().clear();
        searchParams.getSearchConditions().add(condition);
    }

    public StringBuilder getErrBuilder() {
        return errBuilder;
    }

    public String getErrCnt() {
        return errCnt;
    }

    public void setErrCnt(String errCnt) {
        this.errCnt = errCnt;
    }

    public List<String> getBibDocList() {
        if (bibDocList == null) {
            bibDocList = new ArrayList<>();
        }
        return this.bibDocList;
    }

    public int getProcessedRec() {
        return processedRec;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getTotalRecordsExported() {
        return totalRecordsExported;
    }

    public void setTotalRecordsExported(int totalRecordsExported) {
        this.totalRecordsExported = totalRecordsExported;
    }

    public SearchResponse getResponse() {
        return response;
    }

    public void setResponse(SearchResponse response) {
        this.response = response;
    }

    public File getFilePath() {
        return filePath;
    }
}