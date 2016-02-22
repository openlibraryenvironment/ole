package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.response.InvoiceResponse;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;
import org.kuali.ole.docstore.common.response.OleNGInvoiceImportResponse;
import org.kuali.ole.oleng.batch.process.model.ValueByPriority;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileDataMapping;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileMatchPoint;
import org.kuali.ole.oleng.batch.reports.InvoiceImportLoghandler;
import org.kuali.ole.oleng.dao.DescribeDAO;
import org.kuali.ole.oleng.resolvers.invoiceimport.*;
import org.kuali.ole.oleng.service.OleNGInvoiceService;
import org.kuali.ole.pojo.OleInvoiceRecord;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.marc4j.marc.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by SheikS on 1/27/2016.
 */
@Service("batchInvoiceImportProcessor")
public class BatchInvoiceImportProcessor extends BatchFileProcessor {

    @Autowired
    private DescribeDAO describeDAO;

    @Autowired
    private BatchBibFileProcessor batchBibFileProcessor;

    @Autowired
    private OleNGInvoiceService oleNGInvoiceService;

    private List<InvoiceRecordResolver> invoiceRecordResolvers;

    @Override
    public String processRecords(List<Record> records, BatchProcessProfile batchProcessProfile, String reportDirectoryName) throws JSONException {
        JSONObject response = new JSONObject();
        OleNGInvoiceImportResponse oleNGInvoiceImportResponse = new OleNGInvoiceImportResponse();
        if(CollectionUtils.isNotEmpty(records)) {
            BatchProcessProfile bibImportProfile = getBibImportProfile(batchProcessProfile.getBibImportProfileForOrderImport());
            if(null != bibImportProfile) {
                OleNGBibImportResponse oleNGBibImportResponse = processBibImport(records, bibImportProfile, reportDirectoryName);

                Map<String, List<OleInvoiceRecord>> oleinvoiceRecordMap = new TreeMap<>();

                for (Iterator<Record> iterator = records.iterator(); iterator.hasNext(); ) {
                    Record record = iterator.next();
                    OleInvoiceRecord oleInvoiceRecord = prepareInvoiceOrderRercordFromProfile(record,batchProcessProfile);

                    //Match Point process start
                    if(CollectionUtils.isNotEmpty(batchProcessProfile.getBatchProfileMatchPointList())) {
                        List<OlePurchaseOrderItem> olePurchaseOrderItems = processMatchpoint(record, batchProcessProfile,oleInvoiceRecord);
                        if(CollectionUtils.isNotEmpty(olePurchaseOrderItems)) {
                            // Todo : validate the purchaseOrder status if open or not (PurapConstants.PurchaseOrderStatuses.APPDOC_OPEN). If not open should be unlink
                            oleInvoiceRecord.setOlePurchaseOrderItems(olePurchaseOrderItems);
                            if(oleInvoiceRecord.getMatchPointType().equalsIgnoreCase(OleNGConstants.BatchProcess.VENDOR_ITEM_IDENTIFIER)) {
                                oleInvoiceRecord.setLink((olePurchaseOrderItems.size() ==1 ? true : false));
                            } else {
                                oleInvoiceRecord.setLink(true);
                            }
                        }
                    }

                    oleInvoiceRecord.setUnitPrice(oleInvoiceRecord.getListPrice());

                    String invoiceNumber = (oleInvoiceRecord.getInvoiceNumber() != null && !oleInvoiceRecord.getInvoiceNumber().isEmpty())
                            ? oleInvoiceRecord.getInvoiceNumber() : "0";

                    List oleInvoiceRecords = oleinvoiceRecordMap.get(invoiceNumber);
                    if (oleInvoiceRecords == null) {
                        oleInvoiceRecords = new ArrayList<OleInvoiceRecord>();
                    }
                    oleInvoiceRecords.add(oleInvoiceRecord);
                    oleinvoiceRecordMap.put(invoiceNumber, oleInvoiceRecords);
                }


                for (Iterator<String> iterator = oleinvoiceRecordMap.keySet().iterator(); iterator.hasNext(); ) {
                    String invoiceNumber = iterator.next();
                    List<OleInvoiceRecord> oleInvoiceRecords = oleinvoiceRecordMap.get(invoiceNumber);

                    try {
                        OleInvoiceDocument oleInvoiceDocument = oleNGInvoiceService.createNewInvoiceDocument();
                        oleNGInvoiceService.populateInvoiceDocWithOrderInformation(oleInvoiceDocument, oleInvoiceRecords);
                        oleNGInvoiceService.saveInvoiceDocument(oleInvoiceDocument);
                        String documentNumber = oleInvoiceDocument.getDocumentNumber();
                        if(null != documentNumber) {
                            InvoiceResponse invoiceResponse = new InvoiceResponse();
                            invoiceResponse.setDocumentNumber(documentNumber);
                            for (Iterator<OleInvoiceRecord> oleInvoiceRecordIterator = oleInvoiceRecords.iterator(); oleInvoiceRecordIterator.hasNext(); ) {
                                OleInvoiceRecord invoiceRecord = oleInvoiceRecordIterator.next();
                                if(invoiceRecord.isLink()) {
                                    invoiceResponse.addLinkCount();
                                } else {
                                    invoiceResponse.addUnLinkCount();
                                }
                            }
                            oleNGInvoiceImportResponse.getInvoiceResponses().add(invoiceResponse);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                try {
                    String successResponse = getObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(oleNGInvoiceImportResponse);
                    System.out.println("Invoice Import Response : " + successResponse);
                    InvoiceImportLoghandler invoiceImportLoghandler = InvoiceImportLoghandler.getInstance();
                    invoiceImportLoghandler.logMessage(oleNGInvoiceImportResponse,reportDirectoryName);
                    return successResponse;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        response.put(OleNGConstants.STATUS, OleNGConstants.FAILURE);
        return response.toString();
    }

    private List<OlePurchaseOrderItem> processMatchpoint(Record record, BatchProcessProfile batchProcessProfile, OleInvoiceRecord oleInvoiceRecord) {
        Map<String, List<String>> criteriaMapForMatchPoint = getCriteriaMapForMatchPoint(record, batchProcessProfile.getBatchProfileMatchPointList());
        for (Iterator<String> iterator = criteriaMapForMatchPoint.keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            Map dbCriteria = new HashMap();
            String dbCriteriaKey = "";
            if(key.equalsIgnoreCase(OleNGConstants.BatchProcess.VENDOR_ITEM_IDENTIFIER)) {
                dbCriteriaKey = "vendorItemPoNumber";
            } else if(key.equalsIgnoreCase(OleNGConstants.BatchProcess.PO_NUMBER)) {
                dbCriteriaKey = "purchaseOrder.purapDocumentIdentifier";
            }

            List<String> values = criteriaMapForMatchPoint.get(key);
            for (Iterator<String> matchPointIterator = values.iterator(); matchPointIterator.hasNext(); ) {
                String value = matchPointIterator.next();
                if (StringUtils.isNotBlank(value)) {
                    dbCriteria.put(dbCriteriaKey,value);
                    List<OlePurchaseOrderItem> matching = (List<OlePurchaseOrderItem>) getBusinessObjectService().findMatching(OlePurchaseOrderItem.class, dbCriteria);
                    if(CollectionUtils.isNotEmpty(matching)) {
                        oleInvoiceRecord.setMatchPointType(key);
                        return matching;
                    }
                }
            }

        }
        return null;
    }

    private Map<String, List<String>> getCriteriaMapForMatchPoint(Record marcRecord, List<BatchProfileMatchPoint> batchProfileMatchPointList) {
        Map criteriaMap = new HashMap();
        for (Iterator<BatchProfileMatchPoint> iterator = batchProfileMatchPointList.iterator(); iterator.hasNext(); ) {
            BatchProfileMatchPoint batchProfileMatchPoint = iterator.next();
            criteriaMap.put(batchProfileMatchPoint.getMatchPointType(),getMatchPointValue(marcRecord, batchProfileMatchPoint));
        }
        return criteriaMap;
    }

    private List<String> getMatchPointValue(Record marcRecord, BatchProfileMatchPoint batchProfileMatchPoint) {
        List<String> values = new ArrayList<>();
        if(batchProfileMatchPoint.getDataType().equalsIgnoreCase(OleNGConstants.BIB_MARC)) {
            List<String> multiDataFieldValues = getMarcRecordUtil().getMultiDataFieldValues(marcRecord, batchProfileMatchPoint.getDataField(), batchProfileMatchPoint.getInd1(),
                    batchProfileMatchPoint.getInd2(), batchProfileMatchPoint.getSubField());
            if(!batchProfileMatchPoint.isMultiValue()) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Iterator<String> iterator = multiDataFieldValues.iterator(); iterator.hasNext(); ) {
                    String next = iterator.next();
                    stringBuilder.append(next);
                    if(iterator.hasNext()){
                        stringBuilder.append(",");
                    }
                }
                values.add(stringBuilder.toString());
            } else {
                values.addAll(multiDataFieldValues);
            }
        } else {
            values.add(batchProfileMatchPoint.getMatchPointValue());
        }
        return values;
    }

    @Override
    public String getReportingFilePath() {
        return ConfigContext.getCurrentContextConfig().getProperty("batch.invoice.directory");
    }

    private BatchProcessProfile getBibImportProfile(String profileName) {
        BatchProcessProfile batchProcessProfile = null;
        try {
            List<BatchProcessProfile> batchProcessProfiles = describeDAO.fetchProfileByNameAndType(profileName, "Bib Import");
            if (CollectionUtils.isNotEmpty(batchProcessProfiles)) {
                batchProcessProfile = batchProcessProfiles.get(0);
                getObjectMapper().setVisibilityChecker(getObjectMapper().getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                batchProcessProfile = getObjectMapper().readValue(IOUtils.toString(batchProcessProfile.getContent()), BatchProcessProfile.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return batchProcessProfile;
    }

    private OleNGBibImportResponse processBibImport(List<Record> records, BatchProcessProfile bibImportProfile, String reportDirectoryName) {
        OleNGBibImportResponse oleNGBibImportResponse = new OleNGBibImportResponse();
        try {
            String response = batchBibFileProcessor.processRecords(records, bibImportProfile,reportDirectoryName);
            oleNGBibImportResponse = getObjectMapper().readValue(response, OleNGBibImportResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oleNGBibImportResponse;
    }

    private OleInvoiceRecord prepareInvoiceOrderRercordFromProfile(Record marcRecord, BatchProcessProfile batchProcessProfile) {
        OleInvoiceRecord oleInvoiceRecord = new OleInvoiceRecord();

        List<BatchProfileDataMapping> batchProfileDataMappingList = batchProcessProfile.getBatchProfileDataMappingList();
        Map<String, List<ValueByPriority>> valueByPriorityMap = getvalueByPriorityMapForDataMapping(marcRecord, batchProfileDataMappingList);

        for (Iterator<String> iterator = valueByPriorityMap.keySet().iterator(); iterator.hasNext(); ) {
            String destinationField = iterator.next();
            for (Iterator<InvoiceRecordResolver> invoiceRecordResolverIterator = getInvoiceRecordResolvers().iterator(); invoiceRecordResolverIterator.hasNext(); ) {
                InvoiceRecordResolver invoiceRecordResolver = invoiceRecordResolverIterator.next();
                if(invoiceRecordResolver.isInterested(destinationField)) {
                    String value = getDestinationValue(valueByPriorityMap, destinationField);
                    invoiceRecordResolver.setAttributeValue(oleInvoiceRecord,value);
                }
            }
        }

        checkForForeignCurrency(oleInvoiceRecord);
        oleInvoiceRecord.setUnitPrice(oleInvoiceRecord.getListPrice());

        return oleInvoiceRecord;

    }

    private void checkForForeignCurrency(OleInvoiceRecord oleInvoiceRecord){
        if(!StringUtils.isBlank(oleInvoiceRecord.getCurrencyType())){
            if(!oleInvoiceRecord.getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
                if (oleInvoiceRecord.getForeignListPrice() != null && !oleInvoiceRecord.getForeignListPrice().isEmpty() &&
                        oleInvoiceRecord.getInvoiceCurrencyExchangeRate()!= null &&  !oleInvoiceRecord.getInvoiceCurrencyExchangeRate().isEmpty()) {
                    oleInvoiceRecord.setListPrice((new BigDecimal(oleInvoiceRecord.getForeignListPrice()).
                            divide(new BigDecimal(oleInvoiceRecord.getInvoiceCurrencyExchangeRate()), 4, RoundingMode.HALF_UP)).toString());
                }
            }
        }
    }

    public List<InvoiceRecordResolver> getInvoiceRecordResolvers() {
        if(CollectionUtils.isEmpty(invoiceRecordResolvers)) {
            invoiceRecordResolvers = new ArrayList<>();
            invoiceRecordResolvers.add(new AccountNumberValueResolver());
            invoiceRecordResolvers.add(new CurrencyTypeValueResolver());
            invoiceRecordResolvers.add(new ExchangeRateValueResolver());
            invoiceRecordResolvers.add(new FundCodeValueResolver());
            invoiceRecordResolvers.add(new InvoiceDateValueResolver());
            invoiceRecordResolvers.add(new InvoicedForeignPriceValueResolver());
            invoiceRecordResolvers.add(new InvoicedPriceValueResolver());
            invoiceRecordResolvers.add(new InvoiceNumberValueResolver());
            invoiceRecordResolvers.add(new ItemDescriptionValueResolver());
            invoiceRecordResolvers.add(new ObjectCodeValueResolver());
            invoiceRecordResolvers.add(new QuantityValueResolver());
            invoiceRecordResolvers.add(new VendorItemIdentifierValueResolver());
            invoiceRecordResolvers.add(new VendorNumberValueResolver());
        }
        return invoiceRecordResolvers;
    }

    public void setInvoiceRecordResolvers(List<InvoiceRecordResolver> invoiceRecordResolvers) {
        this.invoiceRecordResolvers = invoiceRecordResolvers;
    }
}
