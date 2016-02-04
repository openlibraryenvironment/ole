package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileDataMapping;
import org.kuali.ole.oleng.dao.DescribeDAO;
import org.kuali.ole.oleng.resolvers.invoiceimport.*;
import org.kuali.ole.oleng.service.OleNGInvoiceService;
import org.kuali.ole.pojo.OleInvoiceRecord;
import org.kuali.ole.select.OleSelectConstant;
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
    public String processRecords(List<Record> records, BatchProcessProfile batchProcessProfile) throws JSONException {
        JSONObject response = new JSONObject();
        if(CollectionUtils.isNotEmpty(records)) {
            BatchProcessProfile bibImportProfile = getBibImportProfile(batchProcessProfile.getBibImportProfileForOrderImport());
            if(null != bibImportProfile) {
                //Todo : Need to Enable bib import process
                OleNGBibImportResponse oleNGBibImportResponse = processBibImport(records, bibImportProfile);

                Map<String, List<OleInvoiceRecord>> oleinvoiceRecordMap = new TreeMap<>();

                for (Iterator<Record> iterator = records.iterator(); iterator.hasNext(); ) {
                    Record record = iterator.next();
                    OleInvoiceRecord oleInvoiceRecord = prepareInvoiceOrderRercordFromProfile(record,batchProcessProfile);

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
                    List oleInvoiceRecords = oleinvoiceRecordMap.get(invoiceNumber);

                    try {
                        OleInvoiceDocument oleInvoiceDocument = oleNGInvoiceService.createNewInvoiceDocument();
                        oleNGInvoiceService.populateInvoiceDocWithOrderInformation(oleInvoiceDocument, oleInvoiceRecords);
                        oleNGInvoiceService.saveInvoiceDocument(oleInvoiceDocument);
                        if(null != oleInvoiceDocument.getDocumentNumber()) {
                            response.put(OleNGConstants.STATUS,OleNGConstants.SUCCESS);
                            response.put(OleNGConstants.DOCUMENT_NUMBER,oleInvoiceDocument.getDocumentNumber());
                            return response.toString();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        }
        response.put(OleNGConstants.STATUS, OleNGConstants.FAILURE);
        return response.toString();
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

    private OleNGBibImportResponse processBibImport(List<Record> records, BatchProcessProfile bibImportProfile) {
        OleNGBibImportResponse oleNGBibImportResponse = new OleNGBibImportResponse();
        try {
            String response = batchBibFileProcessor.processRecords(records, bibImportProfile);
            oleNGBibImportResponse = getObjectMapper().readValue(response, OleNGBibImportResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oleNGBibImportResponse;
    }

    private OleInvoiceRecord prepareInvoiceOrderRercordFromProfile(Record marcRecord, BatchProcessProfile batchProcessProfile) {
        OleInvoiceRecord oleInvoiceRecord = new OleInvoiceRecord();

        List<BatchProfileDataMapping> batchProfileDataMappingList = batchProcessProfile.getBatchProfileDataMappingList();
        if(CollectionUtils.isNotEmpty(batchProfileDataMappingList)) {
            for (Iterator<BatchProfileDataMapping> iterator = batchProfileDataMappingList.iterator(); iterator.hasNext(); ) {
                BatchProfileDataMapping batchProfileDataMapping = iterator.next();
                String field = batchProfileDataMapping.getField();
                for (Iterator<InvoiceRecordResolver> invoiceRecordResolverIterator = getInvoiceRecordResolvers().iterator(); invoiceRecordResolverIterator.hasNext(); ) {
                    InvoiceRecordResolver invoiceRecordResolver = invoiceRecordResolverIterator.next();
                    if(invoiceRecordResolver.isInterested(field)) {
                        String value = getDestinationValue(marcRecord, batchProfileDataMapping);
                        invoiceRecordResolver.setAttributeValue(oleInvoiceRecord,value);
                    }
                }
            }
        }

        checkForForeignCurrency(oleInvoiceRecord);
        oleInvoiceRecord.setUnitPrice(oleInvoiceRecord.getListPrice());

        return oleInvoiceRecord;

    }

    private String getDestinationValue(Record marcRecord, BatchProfileDataMapping batchProfileDataMapping) {
        String destValue = null;
        if (batchProfileDataMapping.getDataType().equalsIgnoreCase(OleNGConstants.BIB_MARC)) {
            String dataField = batchProfileDataMapping.getDataField();
            String subField = batchProfileDataMapping.getSubField();
            if (getMarcRecordUtil().isControlField(dataField)) {
                destValue = getMarcRecordUtil().getControlFieldValue(marcRecord, dataField);
            } else {
                destValue = getMarcRecordUtil().getDataFieldValue(marcRecord, dataField, subField);
            }
        } else if (batchProfileDataMapping.getDataType().equalsIgnoreCase(OleNGConstants.CONSTANT)) {
            destValue = batchProfileDataMapping.getConstant();
        }

        return destValue;
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
