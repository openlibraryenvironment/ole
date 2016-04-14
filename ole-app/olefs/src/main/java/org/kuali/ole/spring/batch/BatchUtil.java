package org.kuali.ole.spring.batch;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.response.BibFailureResponse;
import org.kuali.ole.docstore.common.response.InvoiceFailureResponse;
import org.kuali.ole.docstore.common.response.OrderFailureResponse;
import org.kuali.ole.oleng.batch.process.model.ValueByPriority;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileDataMapping;
import org.kuali.ole.oleng.dao.DescribeDAO;
import org.kuali.ole.oleng.dao.impl.DescribeDAOImpl;
import org.kuali.ole.oleng.util.OleNgUtil;
import org.kuali.ole.utility.MarcRecordUtil;
import org.kuali.ole.utility.OleDsNgRestClient;
import org.marc4j.marc.Record;

import java.io.IOException;
import java.util.*;

/**
 * Created by SheikS on 12/8/2015.
 */
public class BatchUtil extends OleNgUtil{
    private OleDsNgRestClient oleDsNgRestClient;
    SolrRequestReponseHandler solrRequestReponseHandler;
    private MarcRecordUtil marcRecordUtil;

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
        exchange.add(OleNGConstants.FAILURE_RESPONSE,invoiceFailureResponses);

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
        exchange.add(OleNGConstants.FAILURE_RESPONSE,bibFailureResponses);

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

}
