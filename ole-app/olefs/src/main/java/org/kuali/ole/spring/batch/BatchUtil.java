package org.kuali.ole.spring.batch;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.Exchange;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.docstore.common.response.*;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.process.model.BatchProcessJob;
import org.kuali.ole.oleng.batch.process.model.ValueByPriority;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileDataMapping;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileMatchPoint;
import org.kuali.ole.oleng.dao.DescribeDAO;
import org.kuali.ole.oleng.dao.impl.DescribeDAOImpl;
import org.kuali.ole.utility.OleNgUtil;
import org.kuali.ole.utility.MarcRecordUtil;
import org.kuali.ole.utility.OleDsNgRestClient;
import org.kuali.rice.krad.util.GlobalVariables;
import org.marc4j.marc.Record;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by SheikS on 12/8/2015.
 */
public class BatchUtil extends OleNgUtil {
    private OleDsNgRestClient oleDsNgRestClient;
    SolrRequestReponseHandler solrRequestReponseHandler;
    private MarcRecordUtil marcRecordUtil;
    public static Map<String, BatchJobDetails> BATCH_JOB_EXECUTION_DETAILS_MAP = new HashMap<>();
    private MarcXMLConverter marcXMLConverter;

    public OleDsNgRestClient getOleDsNgRestClient() {
        if(null == oleDsNgRestClient) {
            oleDsNgRestClient = new OleDsNgRestClient();
        }
        return oleDsNgRestClient;
    }

    public void setOleDsNgRestClient(OleDsNgRestClient oleDsNgRestClient) {
        this.oleDsNgRestClient = oleDsNgRestClient;
    }

    public SolrRequestReponseHandler getSolrRequestReponseHandler() {
        if(null == solrRequestReponseHandler) {
            solrRequestReponseHandler  = new SolrRequestReponseHandler();
        }
        return solrRequestReponseHandler;
    }

    public void setSolrRequestReponseHandler(SolrRequestReponseHandler solrRequestReponseHandler) {
        this.solrRequestReponseHandler = solrRequestReponseHandler;
    }

    public MarcRecordUtil getMarcRecordUtil() {
        if(null == marcRecordUtil) {
            marcRecordUtil = new MarcRecordUtil();
        }
        return marcRecordUtil;
    }

    public void setMarcRecordUtil(MarcRecordUtil marcRecordUtil) {
        this.marcRecordUtil = marcRecordUtil;
    }

    public MarcXMLConverter getMarcXMLConverter() {
        if(null == marcXMLConverter) {
            marcXMLConverter = new MarcXMLConverter();
        }
        return marcXMLConverter;
    }

    public void setMarcXMLConverter(MarcXMLConverter marcXMLConverter) {
        this.marcXMLConverter = marcXMLConverter;
    }

    public Map<String, List<ValueByPriority>> getvalueByPriorityMapForDataMapping(Record marcRecord, List<BatchProfileDataMapping> batchProfileDataMappingList, List<String> mappingTypes) {
        sortDataMappings(batchProfileDataMappingList);
        Map<String, List<ValueByPriority>> valueByPriorityMap = new HashMap<>();
        for (Iterator<BatchProfileDataMapping> iterator = batchProfileDataMappingList.iterator(); iterator.hasNext(); ) {
            BatchProfileDataMapping batchProfileDataMapping = iterator.next();
            if (mappingTypes.contains(batchProfileDataMapping.getDataType())) {
                String destinationField = batchProfileDataMapping.getField();
                boolean multiValue = batchProfileDataMapping.isMultiValue();
                List<String> fieldValues = getFieldValues(marcRecord, batchProfileDataMapping, multiValue);

                if (CollectionUtils.isNotEmpty(fieldValues)) {
                    int priority = batchProfileDataMapping.getPriority();

                    buildingValuesForDestinationBasedOnPriority(valueByPriorityMap, destinationField, multiValue, fieldValues, priority);
                }
            }
        }
        return valueByPriorityMap;
    }

    public String getDestinationValue(Map<String, List<ValueByPriority>> valueByPriorityMap, String fieldType) {
        List<ValueByPriority> valueByPriorities = valueByPriorityMap.get(fieldType);
        for (Iterator<ValueByPriority> iterator1 = valueByPriorities.iterator(); iterator1.hasNext(); ) {
            ValueByPriority valueByPriority = iterator1.next();
            List<String> values = valueByPriority.getValues();
            if (CollectionUtils.isNotEmpty(values)) {
                StringBuilder stringBuilder = new StringBuilder();
                if(valueByPriority.isMultiValue()) {
                    for (Iterator<String> iterator = values.iterator(); iterator.hasNext(); ) {
                        String value = iterator.next();
                        if(StringUtils.isNotBlank(stringBuilder.toString())) {
                            stringBuilder.append(",");
                        }
                        stringBuilder.append(value);
                    }
                    return stringBuilder.toString();
                }
                return values.get(0);
            }
        }

        return null;
    }

    public void sortDataMappings(List<BatchProfileDataMapping> filteredDataMappings) {
        Collections.sort(filteredDataMappings, new Comparator<BatchProfileDataMapping>() {
            public int compare(BatchProfileDataMapping dataMapping1, BatchProfileDataMapping dataMapping2) {
                int priorityForDataMapping1 = dataMapping1.getPriority();
                int priorityForDataMapping2 = dataMapping2.getPriority();
                return new Integer(priorityForDataMapping1).compareTo(new Integer(priorityForDataMapping2));
            }
        });
    }


    public List<String> getFieldValues(Record marcRecord, BatchProfileDataMapping batchProfileDataMapping, boolean multiValue) {
        List<String> marcValues = new ArrayList<>();
        if (batchProfileDataMapping.getDataType().equalsIgnoreCase(OleNGConstants.BIB_MARC)) {
            String marcValue;
            String dataField = batchProfileDataMapping.getDataField();
            if (StringUtils.isNotBlank(dataField)) {
                if (getMarcRecordUtil().isControlField(dataField)) {
                    marcValue = getMarcRecordUtil().getControlFieldValue(marcRecord, dataField);
                    marcValues.add(marcValue);
                } else {
                    String subField = batchProfileDataMapping.getSubField();
                    if (multiValue) {
                        marcValues = getMarcRecordUtil().getMultiDataFieldValues(marcRecord, dataField, batchProfileDataMapping.getInd1(), batchProfileDataMapping.getInd2(), subField);
                    } else {
                        marcValue = getMarcRecordUtil().getDataFieldValueWithIndicators(marcRecord, dataField, batchProfileDataMapping.getInd1(), batchProfileDataMapping.getInd2(), subField);
                        if (StringUtils.isNotBlank(marcValue)) {
                            marcValues.add(marcValue);
                        }
                    }
                }
            }
        } else {
            String constantValue = batchProfileDataMapping.getConstant();
            if (StringUtils.isNotBlank(constantValue)) {
                marcValues.add(constantValue);
            }
        }
        return marcValues;
    }

    /**
     * @param valueByPriorityMap
     * @param destinationField
     * @param multiValue
     * @param fieldValues
     * @param priority
     * @return The valueByPriorityMap contains valueByPriority pojo for each destination field.
     */
    public Map<String, List<ValueByPriority>> buildingValuesForDestinationBasedOnPriority(Map<String, List<ValueByPriority>> valueByPriorityMap, String destinationField, boolean multiValue, List<String> fieldValues, int priority) {
        List<ValueByPriority> valueByPriorities;

        ValueByPriority valueByPriority = new ValueByPriority();
        valueByPriority.setField(destinationField);
        valueByPriority.setPriority(priority);
        valueByPriority.setMultiValue(multiValue);

        valueByPriority.setValues(fieldValues);

        if (valueByPriorityMap.containsKey(destinationField)) {
            valueByPriorities = valueByPriorityMap.get(destinationField);

            if (valueByPriorities.contains(valueByPriority)) {
                ValueByPriority existingValuePriority = valueByPriorities.get(valueByPriorities.indexOf(valueByPriority));
                List<String> values = existingValuePriority.getValues();
                values.addAll(fieldValues);
                StringBuilder stringBuilder = new StringBuilder();
                if (!multiValue) {
                    for (Iterator<String> iterator = values.iterator(); iterator.hasNext(); ) {
                        String value = iterator.next();
                        stringBuilder.append(value);
                        if (iterator.hasNext()) {
                            stringBuilder.append(" ");
                        }
                    }
                    values.clear();
                    values.add(stringBuilder.toString());
                }
            } else {
                valueByPriorities.add(valueByPriority);
            }
        } else {
            valueByPriorities = new ArrayList<>();
            valueByPriorities.add(valueByPriority);
        }
        valueByPriorityMap.put(destinationField, valueByPriorities);

        return valueByPriorityMap;
    }

    public BatchProcessProfile getProfileByNameAndType(String profileName, String profileType) {
        try {
            DescribeDAO describeDAO =  new DescribeDAOImpl();
            List<BatchProcessProfile> batchProcessProfiles = describeDAO.fetchProfileByNameAndType(profileName, profileType);
            if(CollectionUtils.isNotEmpty(batchProcessProfiles)) {
                BatchProcessProfile batchProcessProfile = batchProcessProfiles.get(0);
                getObjectMapper().setVisibilityChecker(getObjectMapper().getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                batchProcessProfile = getObjectMapper().readValue(IOUtils.toString(batchProcessProfile.getContent()), BatchProcessProfile.class);
                return batchProcessProfile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addOrderFaiureResponseToExchange(Exception exception, Integer index, Exchange exchange) {
        String message = exception.toString();
        List<OrderFailureResponse> orderFailureResponses = (List<OrderFailureResponse>) exchange.get(OleNGConstants.FAILURE_RESPONSE);
        if(null == orderFailureResponses) {
            orderFailureResponses = new ArrayList<>();
        }
        OrderFailureResponse newOrderFailureResponse = getNewOrderFailureResponse(message, index);
        String detailedMessage = getDetailedMessage(exception);
        newOrderFailureResponse.setDetailedMessage(detailedMessage);
        orderFailureResponses.add(newOrderFailureResponse);
        exchange.add(OleNGConstants.FAILURE_RESPONSE,orderFailureResponses);

    }

    public OrderFailureResponse getNewOrderFailureResponse(String message, Integer index) {
        OrderFailureResponse orderFailureResponse = new OrderFailureResponse();
        orderFailureResponse.setFailureMessage(message);
        orderFailureResponse.setIndex(index);
        return orderFailureResponse;
    }

    public void addInvoiceFaiureResponseToExchange(Exception exception, Integer index, Exchange exchange) {
        String message = exception.toString();
        List<InvoiceFailureResponse> invoiceFailureResponses = (List<InvoiceFailureResponse>) exchange.get(OleNGConstants.FAILURE_RESPONSE);
        if(null == invoiceFailureResponses) {
            invoiceFailureResponses = new ArrayList<>();
        }
        InvoiceFailureResponse newInvoiceFailureResponse = getNewInvoiceFailureResponse(message, index);
        String detailedMessage = getDetailedMessage(exception);
        newInvoiceFailureResponse.setDetailedMessage(detailedMessage);
        invoiceFailureResponses.add(newInvoiceFailureResponse);
        exchange.add(OleNGConstants.FAILURE_RESPONSE, invoiceFailureResponses);

    }

    public String getDetailedMessage(Exception exception) {
        StackTraceElement[] stackTrace = exception.getStackTrace();
        StringBuilder detailedMessage = new StringBuilder();
        String className = null;
        String methodName = null;
        int lineNumber = 0;
        if (null != stackTrace && stackTrace.length >= 2) {
            for(int index = 0; index < 2; index++) {
                StackTraceElement stackTraceElement = stackTrace[index];
                String fullClassName = stackTraceElement.getClassName();
                className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
                methodName = stackTraceElement.getMethodName();
                lineNumber = stackTraceElement.getLineNumber();
                if(StringUtils.isNotBlank(detailedMessage.toString())){
                    detailedMessage.append("\n");
                }
                detailedMessage.append(className + "." + methodName + "():line#" + lineNumber);
            }
        }
        return detailedMessage.toString();
    }

    public InvoiceFailureResponse getNewInvoiceFailureResponse(String message, Integer index) {
        InvoiceFailureResponse invoiceFailureResponse = new InvoiceFailureResponse();
        invoiceFailureResponse.setFailureMessage(message);
        invoiceFailureResponse.setIndex(index);
        return invoiceFailureResponse;
    }



    public void addBibFaiureResponseToExchange(Exception exception, Integer index, Exchange exchange) {
        String message = exception.toString();
        List<BibFailureResponse> bibFailureResponses = (List<BibFailureResponse>) exchange.get(OleNGConstants.FAILURE_RESPONSE);
        if(null == bibFailureResponses) {
            bibFailureResponses = new ArrayList<>();
        }
        BibFailureResponse newBibFailureResponse = getNewBibFailureResponse(message, index);
        String detailedMessage = getDetailedMessage(exception);
        newBibFailureResponse.setDetailedMessage(detailedMessage);
        bibFailureResponses.add(newBibFailureResponse);
        exchange.add(OleNGConstants.FAILURE_RESPONSE, bibFailureResponses);

    }

    public BibFailureResponse getNewBibFailureResponse(String message, Integer index) {
        BibFailureResponse bibFailureResponse = new BibFailureResponse();
        bibFailureResponse.setFailureMessage(message);
        bibFailureResponse.setIndex(index);
        return bibFailureResponse;
    }

    public void appendQuery(StringBuilder queryBuilder, String query) {
        if (StringUtils.isNotBlank(query)) {
            if(queryBuilder.length() > 0) {
                queryBuilder.append(" OR ");
            }
            queryBuilder.append(query);
        }
    }

    public void updateBatchJob(BatchJobDetails batchJobDetails) {
        updatePercentCompleted(batchJobDetails);
        updateTimeSpent(batchJobDetails);
        if (batchJobDetails.getJobId() != 0 && batchJobDetails.getJobDetailId() != 0) {
            getBusinessObjectService().save(batchJobDetails);
        }
    }

    private void updatePercentCompleted(BatchJobDetails batchJobDetails) {
        String total = batchJobDetails.getTotalRecords();
        float totRec = Float.parseFloat(total == null ? "0" : total);
        if (totRec == 0.0) return;
        String noProcessed = batchJobDetails.getTotalRecordsProcessed();
        if (StringUtils.isEmpty(noProcessed)) return;
        float perCompleted = (Float.valueOf(noProcessed) / Float.valueOf(total)) * 100;
        batchJobDetails.setPerCompleted(String.format("%.2f", perCompleted) + OleNGConstants.PERCENT);
    }

    private void updateTimeSpent(BatchJobDetails batchJobDetails) {
        Timestamp startTime = batchJobDetails.getStartTime();
        long diff = Calendar.getInstance().getTime().getTime() - startTime.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        StringBuffer sb = new StringBuffer();
        sb.append(diffHours + ":" + diffMinutes + ":" + diffSeconds);
        batchJobDetails.setTimeSpent(sb.toString());
    }

    public BatchProfileMatchPoint getMatchPointFromProfile(BatchProcessProfile batchProcessProfile) {
        BatchProfileMatchPoint batchProfileMatchPoint = null;
        if (CollectionUtils.isNotEmpty(batchProcessProfile.getBatchProfileMatchPointList())) {
            batchProfileMatchPoint = batchProcessProfile.getBatchProfileMatchPointList().get(0);
        }
        return batchProfileMatchPoint;
    }

    public String getMatchPoint(BatchProfileMatchPoint batchProfileMatchPoint) {
        String matchPoint = null;
        if (StringUtils.isNotBlank(batchProfileMatchPoint.getDestDataField())) {
            matchPoint = batchProfileMatchPoint.getDestDataField() + (OleNGConstants.TAG_001.equalsIgnoreCase(batchProfileMatchPoint.getDestDataField()) ? "" : batchProfileMatchPoint.getDestSubField());
        } else if (StringUtils.isNotBlank(batchProfileMatchPoint.getDataField())) {
            matchPoint = batchProfileMatchPoint.getDataField() + (OleNGConstants.TAG_001.equalsIgnoreCase(batchProfileMatchPoint.getDataField()) ? "" : batchProfileMatchPoint.getSubField());
        }
        return matchPoint;
    }

    public String getSourceMatchPoint(BatchProfileMatchPoint batchProfileMatchPoint) {
        String sourceMatchPoint = null;
        if (StringUtils.isNotBlank(batchProfileMatchPoint.getDataField())) {
            sourceMatchPoint = batchProfileMatchPoint.getDataField() + (OleNGConstants.TAG_001.equalsIgnoreCase(batchProfileMatchPoint.getDataField()) ? "" : batchProfileMatchPoint.getSubField());
        }
        return sourceMatchPoint;
    }


    public void removeDuplicates(String matchPoint, List<String> matchPointValues, OleNGBatchDeleteResponse oleNGBatchDeleteResponse) {
        List<String> matchPointDatas = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(matchPointValues)) {
            for (String matchPointValue : matchPointValues) {
                if (!matchPointDatas.contains(matchPointValue)) {
                    matchPointDatas.add(matchPointValue);
                } else {
                    oleNGBatchDeleteResponse.addFailureRecord(matchPoint, matchPointValue, null, OleNGConstants.ERR_DUPLICATE_LINE_DATA);
                }
            }
        }
        matchPointValues.clear();
        matchPointValues.addAll(matchPointDatas);
    }

    public void addBatchDeleteFailureResponseToExchange(Exception exception, String matchPointData, String bibId, Exchange exchange) {
        String message = exception.toString();
        List<DeleteFailureResponse> deleteFailureResponses = (List<DeleteFailureResponse>) exchange.get(OleNGConstants.FAILURE_RESPONSE);
        if (null == deleteFailureResponses) {
            deleteFailureResponses = new ArrayList<>();
        }
        DeleteFailureResponse deleteFailureResponse = new DeleteFailureResponse();
        String detailedMessage = getDetailedMessage(exception);
        deleteFailureResponse.setDetailedMessage(detailedMessage);
        deleteFailureResponse.setFailureMessage(message);
        deleteFailureResponse.setFailedMatchPointData(matchPointData);
        deleteFailureResponse.setFailedBibId(bibId);
        deleteFailureResponses.add(deleteFailureResponse);
        exchange.add(OleNGConstants.FAILURE_RESPONSE, deleteFailureResponses);
    }

    public void addBatchExportFailureResponseToExchange(Exception exception, String bibId, Exchange exchange) {
        String message = exception.toString();
        List<ExportFailureResponse> exportFailureResponses = (List<ExportFailureResponse>) exchange.get(OleNGConstants.FAILURE_RESPONSE);
        if(null == exportFailureResponses) {
            exportFailureResponses = new ArrayList<>();
        }
        ExportFailureResponse exportFailureResponse = new ExportFailureResponse();
        String detailedMessage = getDetailedMessage(exception);
        exportFailureResponse.setDetailedMessage(detailedMessage);
        exportFailureResponse.setFailureMessage(message);
        exportFailureResponse.setFailedBibId(bibId);
        exportFailureResponses.add(exportFailureResponse);
        exchange.add(OleNGConstants.FAILURE_RESPONSE, exportFailureResponses);
    }

    public String writeBatchRunningStatusToFile(String directoryPath, String status, String totalTimeTaken) {
        File fileToWrite = new File(directoryPath, "status" + File.separator + "status.json");
        try {
            String statusContent = getStatusContent(status, totalTimeTaken);
            FileUtils.writeStringToFile(fileToWrite, statusContent);
            return fileToWrite.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getStatusContent(String status, String totalTimeTaken) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(OleNGConstants.STATUS , status);
            jsonObject.put(OleNGConstants.TIME_SPENT, totalTimeTaken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public BatchJobDetails createBatchJobDetailsEntry(BatchProcessJob batchProcessJob, String fileName) {
        BatchJobDetails batchJobDetails = new BatchJobDetails();
        batchJobDetails.setJobId(batchProcessJob.getJobId());
        batchJobDetails.setJobName(batchProcessJob.getJobName());
        batchJobDetails.setProfileType(batchProcessJob.getProfileType());
        batchJobDetails.setProfileName(batchProcessJob.getBatchProfileName());
        String loginUser = GlobalVariables.getUserSession().getPrincipalName();
        batchJobDetails.setCreatedBy(loginUser); // Job initiated by
        batchJobDetails.setStartTime(new Timestamp(new Date().getTime()));
        batchJobDetails.setStatus(OleNGConstants.RUNNING);
        batchJobDetails.setFileName(fileName);
        batchJobDetails.setStartTime(new Timestamp(System.currentTimeMillis()));
        batchJobDetails.setNumOfRecordsInFile(batchProcessJob.getNumOfRecordsInFile());
        batchJobDetails.setOutputFileFormat(batchProcessJob.getOutputFileFormat());
        return batchJobDetails;
    }

    public BatchProcessJob getBatchProcessJobById(Long jobId) {
        Map map = new HashedMap();
        map.put(OleNGConstants.JOB_ID, jobId);
        return getBusinessObjectService().findByPrimaryKey(BatchProcessJob.class, map);
    }

    public String getSolrDate(String dateStr, boolean isFrom) throws ParseException {
        SimpleDateFormat solrDtFormat = new SimpleDateFormat(OleNGConstants.SOLR_DATE_FORMAT);
        SimpleDateFormat userFormat = new SimpleDateFormat(OleNGConstants.FILTER_DATE_FORMAT);
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
            //LOG.error("Error while parsing user entered date::" + dateStr, e);
            throw e;
        }
    }

    public BatchJobDetails getJobDetailsById(Long jobDetailsId) {
        return getBusinessObjectService().findBySinglePrimaryKey(BatchJobDetails.class, jobDetailsId);
    }

    public void deleteJobDetailsById(Long jobDetailsId) {
        Map map = new HashedMap();
        map.put(OleNGConstants.JOB_DETAIL_ID, jobDetailsId);
        getBusinessObjectService().deleteMatching(BatchJobDetails.class, map);
    }

    public boolean isJobRunning(BatchJobDetails batchJobDetails) {
        boolean running = false;
        BatchJobDetails runningDetials = BATCH_JOB_EXECUTION_DETAILS_MAP.get(batchJobDetails.getJobId() + "_" + batchJobDetails.getJobDetailId());
        if(null != runningDetials) {
            running = true;
        }
        return running;

    }

    public int getBatchChunkSize() {
        String parameterValue = ParameterValueResolver.getInstance().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DESC_NMSPC, OLEConstants.DESCRIBE_COMPONENT, OleNGConstants.CHUNK_SIZE_FOR_BATCH_PROCESSING);
        if(NumberUtils.isDigits(parameterValue)) {
            return Integer.valueOf(parameterValue);
        }
        return 1000;
    }

}