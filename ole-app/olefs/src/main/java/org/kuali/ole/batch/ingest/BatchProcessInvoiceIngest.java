package org.kuali.ole.batch.ingest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLETranscationalRecordGenerator;
import org.kuali.ole.batch.bo.OLEBatchBibImportDataObjects;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.controller.OLEBatchProcessJobDetailsController;
import org.kuali.ole.batch.impl.AbstractBatchProcess;
import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.coa.businessobject.ObjectCode;
import org.kuali.ole.coa.businessobject.OleFundCode;
import org.kuali.ole.coa.businessobject.OleFundCodeAccountingLine;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.converter.OLEINVConverter;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.content.bib.marc.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.ingest.OleInvoiceRecordBuilder;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.document.service.InvoiceService;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.module.purap.document.service.PurchaseOrderService;
import org.kuali.ole.module.purap.businessobject.InvoiceAccount;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderAccount;
import org.kuali.ole.module.purap.document.validation.event.AttributedCalculateAccountsPayableEvent;
import org.kuali.ole.module.purap.service.PurapAccountingService;
import org.kuali.ole.pojo.OleInvoiceRecordHandler;
import org.kuali.ole.pojo.edi.INVOrders;
import org.kuali.ole.pojo.edi.INVOrder;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.bo.OleVendorAccountInfo;
import org.kuali.ole.select.document.OLEInvoiceIngestLoadReport;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.document.service.impl.OleInvoiceServiceImpl;
import org.kuali.ole.sys.document.validation.event.AttributedSaveDocumentEvent;
import org.kuali.ole.vnd.businessobject.OleCurrencyType;
import org.kuali.ole.vnd.businessobject.OleExchangeRate;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.service.*;
import org.kuali.rice.krad.util.ErrorMessage;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.BankService;
import org.kuali.ole.sys.businessobject.Bank;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.pojo.OleInvoiceRecord;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.kuali.ole.select.document.service.*;
import org.kuali.ole.select.businessobject.OleInvoiceItem;
import org.milyn.SmooksException;
import org.milyn.edisax.EDIParseException;
import org.springframework.util.AutoPopulatingList;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/27/13
 * Time: 3:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class BatchProcessInvoiceIngest extends AbstractBatchProcess {

    private static final Logger LOG = Logger.getLogger(BatchProcessLocationIngest.class);

    private static transient BusinessObjectService businessObjectService;
    private String xml;
    protected DocumentService documentService;
    public OleInvoiceService oleInvoiceService;
    OLETranscationalRecordGenerator oleTranscationalRecordGenerator = new OLETranscationalRecordGenerator();
    private StringBuffer failureRecords = new StringBuffer();
    private DocstoreClientLocator docstoreClientLocator;
    private String marcXMLContent;
    DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
    private static transient OlePurapService olePurapService;

    public static OlePurapService getOlePurapService() {
        if (olePurapService == null) {
            olePurapService = SpringContext.getBean(OlePurapService.class);
        }
        return olePurapService;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }

    List<INVOrder> invOrder;
    List<INVOrders> mismatchRecord = new ArrayList();

    protected BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = SpringContext.getBean(BusinessObjectService.class);
        }
        return businessObjectService;
    }


    private OleInvoiceService getInvoiceService() {
        if (oleInvoiceService == null) {
            oleInvoiceService = SpringContext.getBean(OleInvoiceService.class);
        }
        return oleInvoiceService;
    }

    public String readFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        return stringBuilder.toString();
    }


    @Override
    public void prepareForRead() throws Exception {
        try {
            convertRawDataToXML();
        }
        catch (Exception e) {
            StringBuilder errorSegment =fetchErrorSegment((EDIParseException)e.getCause());
            logAndThrowErrorSegment((EDIParseException)e.getCause(), errorSegment);
        }
    }


    private void logAndThrowErrorSegment(EDIParseException parse, StringBuilder errorSegment) throws Exception {
        failureRecords.append("Unable to map the Edifact file to segments. ");
        createBatchErrorAttachmentFile(failureRecords.append("Cannot parse edifact file. Ingested Edifact file has problem in EDIFACT segment --" + errorSegment).toString());
        LOG.error(failureRecords, parse);
        job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
        job.setStatusDesc(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
        throw parse;
    }

    private StringBuilder fetchErrorSegment(EDIParseException parse) {
        StringBuilder errorSegment = new StringBuilder();
        for(String segment:parse.getSegmentline()){
            errorSegment.append(segment+"+");
        }
        errorSegment.deleteCharAt(errorSegment.length()-1);
        errorSegment.append(". Line Number: " + parse.getSegmentNumber());
        return errorSegment;
    }

    public void byPassLogicForPreProcess(String rawMarcContent, boolean preProcessingReq) {
        if (preProcessingReq) {
            preProcess(rawMarcContent);
        } else {
            marcXMLContent = rawMarcContent;
        }
    }

    private void preProcess(String rawMarcContent) {
        marcXMLContent = preProcessMarc(rawMarcContent);
    }

    private OLEBatchProcessProfileBo getBibImportProfile() {
        String bibImportProfileForOrderRecord = this.processDef.getOleBatchProcessProfileBo().getBibImportProfileForOrderRecord();
        org.kuali.rice.krad.service.BusinessObjectService
                businessObjectService = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
        Map<String, String> bibImportProfileMap = new HashMap<>();
        bibImportProfileMap.put("batchProcessProfileName", bibImportProfileForOrderRecord);
        List<OLEBatchProcessProfileBo> oleBatchProcessProfileBoList = (List) businessObjectService.findMatching(OLEBatchProcessProfileBo.class, bibImportProfileMap);
        if (oleBatchProcessProfileBoList != null && oleBatchProcessProfileBoList.size() > 0) {
            return oleBatchProcessProfileBoList.get(0);
        }
        return null;
    }

    public String preProcessMarc(String marcFileContent) {
        String marcXMLContent = null;
        MarcXMLConverter marcXMLConverter = new MarcXMLConverter();
        marcXMLContent = marcXMLConverter.convert(marcFileContent);

        //TODO: hack to get rid of the extra xmlns entry. Not sure why the second entry gets generated when calling marc4J in ole-docstore-utility.
        //TODO: the duplicate entry does not get genereated if its run directly in the ole-docstore-utilty project.
        String modifiedXMLContent =
                marcXMLContent.
                        replace("collection xmlns=\"http://www.loc.gov/MARC21/slim\" xmlns=\"http://www.loc.gov/MARC21/slim",
                                "collection xmlns=\"http://www.loc.gov/MARC21/slim");
        return modifiedXMLContent;
    }

    private void convertRawDataToXML() throws Exception {
        String fileContent = getBatchProcessFileContent();
        if (this.processDef.getUploadFileName().endsWith(".mrc")) {
            byPassLogicForPreProcess(fileContent, true);
            ;
        } else {
            OLEINVConverter OLEEDITranslator = new OLEINVConverter();
            xml = OLEEDITranslator.convertToXML(fileContent);
        }
    }

    private void convertXMLToPojos(Map<String, List<OleInvoiceRecord>> map) throws Exception {
        LOG.info("--------------xml----------" + xml);
        INVOrders invOrders = null;
        if (xml != null) {
            invOrders = oleTranscationalRecordGenerator.fromInvoiceXml(xml);
            invOrder = invOrders.getInvOrder();
        }

        for (int i = 0; i < invOrder.size(); i++) {
            OleInvoiceRecordBuilder oleInvoiceRecordBuilder = OleInvoiceRecordBuilder.getInstance();
            OleInvoiceRecord oleInvoiceRecord = null;

            List<OleInvoiceRecord> oleInvoiceRecordList = new ArrayList<OleInvoiceRecord>();
            for (int j = 0; j < invOrder.get(i).getLineItemOrder().size(); j++) {
                try {
                    oleInvoiceRecord = oleInvoiceRecordBuilder.build(invOrder.get(i).getLineItemOrder().get(j), invOrder.get(i));
                    oleInvoiceService = getInvoiceService();
                    oleInvoiceService.setOleBatchProcessProfileBo(this.processDef.getOleBatchProcessProfileBo());
                    oleInvoiceService.setDefaultAndConstantValuesToInvoiceRecord(oleInvoiceRecord);
                    String invoiceNumber = (oleInvoiceRecord.getInvoiceNumber() != null && !oleInvoiceRecord.getInvoiceNumber().isEmpty())
                            ? oleInvoiceRecord.getInvoiceNumber() : "0";
                    oleInvoiceRecordList = map.get(invoiceNumber);
                    if (oleInvoiceRecordList == null) {
                        oleInvoiceRecordList = new ArrayList<OleInvoiceRecord>();
                    }
                    oleInvoiceRecordList.add(oleInvoiceRecord);
                    map.put(invoiceNumber, oleInvoiceRecordList);
                } catch (Exception e) {
                    failureRecords.append("Unable to create Invoice document");
                    failureRecords.append("\n");
                    failureRecords.append(e.getMessage());
                    failureRecords.append("\n");
                    job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
                    job.setStatusDesc(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
                    throw e;
                }
            }
        }
    }

    private void convertXMLToPojos(Map<String, List<OleInvoiceRecord>> map, String marcXMLContent) throws Exception {
        LOG.info("--------------xml----------" + marcXMLContent);
        List<OleInvoiceRecord> oleInvoiceRecordList = new ArrayList<OleInvoiceRecord>();
        try {
            BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();
            BibMarcRecords bibMarcRecords = bibMarcRecordProcessor.fromXML(marcXMLContent);
            oleInvoiceService = new OleInvoiceServiceImpl();
            oleInvoiceService.setOleBatchProcessProfileBo(this.processDef.getOleBatchProcessProfileBo());
            List<BibMarcRecord> records = bibMarcRecords.getRecords();
            dataCarrierService.addData("invoiceIngestFailureReason", new ArrayList<>());
            OLEBatchProcessProfileBo oleBatchProcessProfileBoForBibImport = getBibImportProfile();
            BatchProcessBibImport batchProcessBibImport =  new BatchProcessBibImport(processDef, job);
            batchProcessBibImport.setOleBatchProcessProfileBo(oleBatchProcessProfileBoForBibImport);
            OLEBatchBibImportDataObjects oleBatchBibImportDataObjects = new OLEBatchBibImportDataObjects();
            List<OrderBibMarcRecord> orderBibMarcRecords = oleBatchBibImportDataObjects.processBibImport(records,batchProcessBibImport);
            if(orderBibMarcRecords != null && orderBibMarcRecords.size() >0){
                for(int recCount = 0;recCount < orderBibMarcRecords.size();recCount ++){
                    // Check for errors in the output of bib import
                    if(!StringUtils.isBlank(orderBibMarcRecords.get(recCount).getFailureReason())){
                        failureRecords.append(OLEConstants.OLEBatchProcess.REC_POSITION + (recCount+1) + "  " + OLEConstants.OLEBatchProcess.BIB_IMP_FAILED);
                        getTitleInformationForFailureRecords(orderBibMarcRecords.get(recCount).getBibMarcRecord());
                        failureRecords.append(OLEConstants.OLEBatchProcess.REASON + orderBibMarcRecords.get(recCount).getFailureReason() + "\n");
                    }
                }
            }
            else {
                failureRecords.append(OLEConstants.OLEBatchProcess.BIB_IMP_FAILED);
            }
            for (int recordCount = 0; recordCount < records.size(); recordCount++) {
                OleInvoiceRecord oleInvoiceRecord = getInvoiceService().populateValuesFromProfile(records.get(recordCount));
                String invoiceNumber = (oleInvoiceRecord.getInvoiceNumber() != null && !oleInvoiceRecord.getInvoiceNumber().isEmpty())
                        ? oleInvoiceRecord.getInvoiceNumber() : "0";
                oleInvoiceRecordList = map.get(invoiceNumber);
                if (oleInvoiceRecordList == null) {
                    oleInvoiceRecordList = new ArrayList<OleInvoiceRecord>();
                }
                oleInvoiceRecordList.add(oleInvoiceRecord);
                map.put(invoiceNumber, oleInvoiceRecordList);
            }

        } catch (Exception e) {
            List invoiceIngestFailureReason = (List) dataCarrierService.getData("invoiceIngestFailureReason");
            if(invoiceIngestFailureReason != null && invoiceIngestFailureReason.size()>0){
                for(int failCount = 0;failCount < invoiceIngestFailureReason.size();failCount++){
                    failureRecords.append(invoiceIngestFailureReason.get(failCount) + "\n");
                }
            }
            failureRecords.append("Unable to create Invoice document");
            failureRecords.append("\n");
            failureRecords.append(e.getMessage());
            failureRecords.append("\n");
            job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
            job.setStatusDesc(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
            throw e;
        }
    }

    private void getTitleInformationForFailureRecords( BibMarcRecord bibMarcRecord){
        String title = null;
        for (DataField dataField : bibMarcRecord.getDataFields()){
            if (dataField.getTag().equals("245")){
                for (SubField subfield : dataField.getSubFields()){
                    if (subfield.getCode().equals("a")){
                        title = subfield.getValue();
                    }
                }
            }
        }
        if (title!=null){
            failureRecords.append("For Title : "+title);
        }
    }


    public void updatePrice(OleInvoiceDocument oleInvoiceDocument) {


        for (OleInvoiceItem item : (List<OleInvoiceItem>) oleInvoiceDocument.getItems()) {
            if (item.getItemDiscount() != null) {
                if (item.getItemDiscountType() != null && item.getItemDiscountType().equals("%")) {
                    BigDecimal discount = ((item.getItemListPrice().bigDecimalValue().multiply(item.getItemDiscount().bigDecimalValue()))).divide(new BigDecimal(100));
                    item.setItemUnitPrice(item.getItemListPrice().bigDecimalValue().subtract(discount));
                } else {
                    item.setItemUnitPrice(((OleInvoiceItem) item).getItemListPrice().bigDecimalValue().subtract(item.getItemDiscount().bigDecimalValue()));
                }
            } else {
                item.setItemUnitPrice(((OleInvoiceItem) item).getItemListPrice().bigDecimalValue());
            }
            getInvoiceService().calculateAccount(item);
        }
    }


    private OleInvoiceDocument initiateInvoiceDocument(OleInvoiceDocument invoiceDocument, Person currentUser) throws Exception {
        try {
            invoiceDocument = (OleInvoiceDocument) SpringContext.getBean(DocumentService.class).getNewDocument("OLE_PRQS");
        } catch (WorkflowException e) {
            LOG.error(e, e);
        }
        invoiceDocument.initiateDocument();

        UniversityDateService universityDateService = SpringContext.getBean(UniversityDateService.class);
        invoiceDocument.setPostingYear(universityDateService.getCurrentUniversityDate().getUniversityFiscalYear());
        Bank defaultBank = SpringContext.getBean(BankService.class).getDefaultBankByDocType(invoiceDocument.getClass());
        if (defaultBank != null) {
            invoiceDocument.setBankCode(defaultBank.getBankCode());
            invoiceDocument.setBank(defaultBank);

        }
        String description = getOlePurapService().getParameter(OLEConstants.INVOICE_IMPORT_INV_DESC);
        if (LOG.isDebugEnabled()){
            LOG.debug("Description for invoice import ingest is "+description);
        }
        description = getOlePurapService().setDocumentDescription(description,null);
        invoiceDocument.getDocumentHeader().setDocumentDescription(description);
        invoiceDocument.setAccountsPayableProcessorIdentifier(currentUser.getPrincipalId());
        invoiceDocument.setProcessingCampusCode(currentUser.getCampusCode());
        return invoiceDocument;
    }

    private OleVendorAccountInfo populateBFN(String code) {
        Map matchBFN = new HashMap();
        matchBFN.put("vendorRefNumber", code);
        List<OleVendorAccountInfo> oleVendorAccountInfo = (List<OleVendorAccountInfo>) getBusinessObjectService().findMatching(OleVendorAccountInfo.class, matchBFN);
        return oleVendorAccountInfo != null && oleVendorAccountInfo.size() > 0 ? oleVendorAccountInfo.get(0) : null;
    }

    private String populateChartOfAccount(String accountNumber) {
        Map matchChartCode = new HashMap();
        matchChartCode.put("accountNumber", accountNumber);
        List<Account> accountList = (List<Account>) getBusinessObjectService().findMatching(Account.class, matchChartCode);
        return accountList != null && accountList.size() > 0 ? accountList.get(0).getChartOfAccountsCode() : null;
    }

    private HashMap addInvoiceItem(List<OlePurchaseOrderItem> olePurchaseOrderItems, OleInvoiceRecord invoiceRecord, OleInvoiceDocument invoiceDocument, PurchaseOrderDocument purchaseOrderDocument, HashMap itemMap, Set<Integer> lineNumbers, Set<String> fdocNumbers,Set<BigDecimal> unitPrize,Set<String> version) throws Exception {

        for (OlePurchaseOrderItem poItem : olePurchaseOrderItems) {
            if (poItem.getItemTypeCode().equalsIgnoreCase("ITEM")) {
                OleInvoiceItem oleInvoiceItem = new OleInvoiceItem();
                oleInvoiceItem.setItemTypeCode(poItem.getItemTypeCode());
                oleInvoiceItem.setItemType(poItem.getItemType());
                oleInvoiceItem.setItemQuantity(new KualiDecimal(invoiceRecord.getQuantity()));
                oleInvoiceItem.setItemListPrice(new KualiDecimal(invoiceRecord.getListPrice()));
                oleInvoiceItem.setItemDescription(poItem.getItemDescription());      // invoiceRecord.getItemDescription()
                oleInvoiceItem.setItemDiscount(invoiceRecord.getLineItemAdditionalCharge() != null ? new KualiDecimal(invoiceRecord.getLineItemAdditionalCharge()) : null);
                oleInvoiceItem.setItemDiscountType(invoiceRecord.getDiscountType());
                oleInvoiceItem.setItemUnitPrice(new BigDecimal(invoiceRecord.getUnitPrice()));
                oleInvoiceItem.setItemTitleId(poItem.getItemTitleId());
                if(invoiceRecord.getItemNote()!=null && invoiceRecord.getItemNote().size()>0) {
                    oleInvoiceItem.setNotes(invoiceRecord.getItemNote());
                }
                if (invoiceRecord.getLineItemAdditionalCharge() != null) {
                    oleInvoiceItem.setVendorItemIdentifier(poItem.getVendorItemPoNumber());
                }
                PurchaseOrderService purchaseOrderService = (PurchaseOrderService) SpringContext.getBean("purchaseOrderService");
                purchaseOrderDocument = purchaseOrderService.getPurchaseOrderByDocumentNumber(poItem.getDocumentNumber());
                if (itemMap.containsKey("noOfItems") && (Integer)(itemMap.get("noOfItems"))==1) {
                    oleInvoiceItem.setPurchaseOrderIdentifier(purchaseOrderDocument.getPurapDocumentIdentifier());
                }
                oleInvoiceItem.setItemLineNumber(poItem.getItemLineNumber());
                oleInvoiceItem.setItemNoOfParts(poItem.getItemNoOfParts());
                oleInvoiceItem.setPoItemIdentifier(poItem.getItemIdentifier());
                oleInvoiceItem.setAccountsPayablePurchasingDocumentLinkIdentifier(purchaseOrderDocument.getAccountsPayablePurchasingDocumentLinkIdentifier());
                oleInvoiceItem.setOlePoOutstandingQuantity(new KualiInteger(poItem.getOutstandingQuantity().bigDecimalValue()));
                if(StringUtils.isNotBlank(invoiceRecord.getLineItemTaxAmount())) {
                    oleInvoiceItem.setUseTaxIndicator(true);
                    oleInvoiceItem.setItemSalesTaxAmount(new KualiDecimal(invoiceRecord.getLineItemTaxAmount()));
                    oleInvoiceItem.addToExtendedPrice(oleInvoiceItem.getItemSalesTaxAmount());
                }
                //call populate VendorInfo
                OleVendorAccountInfo oleVendorAccountInfo = populateBFN(invoiceRecord.getBfnNumber());
                List accountingLine = new ArrayList();
                if (oleVendorAccountInfo != null && oleVendorAccountInfo.isActive()) {

                    InvoiceAccount invoiceAccount = new InvoiceAccount();
                    invoiceAccount.setAccountNumber(oleVendorAccountInfo.getAccountNumber());
                    invoiceAccount.setFinancialObjectCode(oleVendorAccountInfo.getObjectCode());
                    invoiceAccount.setAmount(new KualiDecimal(invoiceRecord.getUnitPrice()));
                    invoiceAccount.setAccountLinePercent(new BigDecimal("100"));  // TODO: Need to get from edifact.
                    invoiceAccount.setPurapItem(oleInvoiceItem);
                    invoiceAccount.setItemIdentifier(oleInvoiceItem.getItemIdentifier());
                    invoiceAccount.setChartOfAccountsCode(populateChartOfAccount(oleVendorAccountInfo.getAccountNumber()) != null ? populateChartOfAccount(oleVendorAccountInfo.getAccountNumber()) : invoiceRecord.getItemChartCode());     // TODO: Need to get chart of Account based on account number and object code.
                    accountingLine.add(invoiceAccount);

                } else {
                    if (itemMap.containsKey("noOfItems") && (Integer)(itemMap.get("noOfItems"))==1) {
                        if (invoiceRecord.getFundCode() != null) {
                            accountingLine = getAccountingLinesFromFundCode(invoiceRecord, oleInvoiceItem);
                        } else {
                            for (PurApAccountingLine poa : poItem.getSourceAccountingLines()) {
                                InvoiceAccount invoiceAccount = new InvoiceAccount(oleInvoiceItem, (PurchaseOrderAccount) poa);
                                invoiceAccount.setAccountNumber(!StringUtils.isBlank(invoiceRecord.getAccountNumber()) ? invoiceRecord.getAccountNumber() : invoiceAccount.getAccountNumber());
                                invoiceAccount.setFinancialObjectCode(!StringUtils.isBlank(invoiceRecord.getObjectCode()) ? invoiceRecord.getObjectCode() : invoiceAccount.getFinancialObjectCode());
                                accountingLine.add(invoiceAccount);
                            }
                        }
                    }else{
                        for (PurApAccountingLine poa : poItem.getSourceAccountingLines()) {
                            InvoiceAccount invoiceAccount = new InvoiceAccount(oleInvoiceItem, (PurchaseOrderAccount) poa);
                            accountingLine.add(invoiceAccount);
                        }
                    }
                }
                oleInvoiceItem.setSourceAccountingLines(accountingLine);
                oleInvoiceItem.setPostingYear(poItem.getPurchaseOrder().getPostingYear());
                SimpleDateFormat dateFromRawFile = new SimpleDateFormat(org.kuali.ole.OLEConstants.DATE_FORMAT);
                oleInvoiceItem.setSubscriptionFromDate(invoiceRecord.getSubscriptionPeriodFrom()!= null ? new java.sql.Date(dateFromRawFile.parse(invoiceRecord.getSubscriptionPeriodFrom()).getTime()):null);
                oleInvoiceItem.setSubscriptionToDate(invoiceRecord.getSubscriptionPeriodTo()!= null ? new java.sql.Date(dateFromRawFile.parse(invoiceRecord.getSubscriptionPeriodTo()).getTime()):null);
                if(!StringUtils.isBlank(invoiceDocument.getInvoiceCurrencyType())){
                    invoiceDocument.setInvoiceCurrencyTypeId(new Long(invoiceDocument.getInvoiceCurrencyType()));
                    String currencyType = getInvoiceService().getCurrencyType(invoiceDocument.getInvoiceCurrencyType());
                    if (StringUtils.isNotBlank(currencyType)) {
                        if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                            setItemForeignDetails(oleInvoiceItem,invoiceDocument.getInvoiceCurrencyType(),invoiceRecord);
                        }
                    }
                }

                if (lineNumbers.add(oleInvoiceItem.getItemLineNumber())) {
                    invoiceDocument.getItems().add(oleInvoiceItem);
                    fdocNumbers.add(poItem.getDocumentNumber());
                    unitPrize.add(oleInvoiceItem.getItemUnitPrice());
                    version.add(invoiceRecord.getItemDescription());
                } else if (fdocNumbers.add(poItem.getDocumentNumber())) {
                    invoiceDocument.getItems().add(oleInvoiceItem);
                    unitPrize.add(oleInvoiceItem.getItemUnitPrice());
                    version.add(invoiceRecord.getItemDescription());
                } else if(unitPrize.add(oleInvoiceItem.getItemUnitPrice())){
                    //It's one title on one PO with multiple charges applicable to the one PO
                    invoiceDocument.getItems().add(oleInvoiceItem);
                    version.add(invoiceRecord.getItemDescription());
                } else if(version.add(invoiceRecord.getItemDescription())){
                    //It's one title on one PO with multiple charges applicable to the one PO.Since unitPrize is same,the PO are skipped.
                    invoiceDocument.getItems().add(oleInvoiceItem);
                }

            }
        }

        itemMap.put("invoiceDocument", invoiceDocument);
        itemMap.put("purchaseOrderDocument", purchaseOrderDocument);
        return itemMap;
    }

    private HashMap createInvoiceItem(int lineItemCount, OleInvoiceRecord invoiceRecord, OleInvoiceDocument invoiceDocument
            , HashMap itemMap) throws Exception {

        OleInvoiceItem oleInvoiceItem = new OleInvoiceItem();
        validateInvoiceRecordValues(invoiceRecord);
        oleInvoiceItem.setItemQuantity(new KualiDecimal(invoiceRecord.getQuantity()));
        oleInvoiceItem.setItemListPrice(new KualiDecimal(invoiceRecord.getListPrice()));
        oleInvoiceItem.setItemDescription(invoiceRecord.getItemDescription()); // invoiceRecord.getItemDescription()
        if(invoiceRecord.getItemNote()!=null && invoiceRecord.getItemNote().size()>0) {
            oleInvoiceItem.setNotes(invoiceRecord.getItemNote());
        }
        /*if (invoiceRecord.getAdditionalChargeCode() != null && invoiceRecord.getAdditionalChargeCode().equalsIgnoreCase("SVC")) {
            oleInvoiceItem.setItemUnitPrice(new BigDecimal(invoiceRecord.getAdditionalCharge() != null ? invoiceRecord.getAdditionalCharge() : invoiceRecord.getAdditionalCharge()));
            oleInvoiceItem.setItemTypeCode("SPHD");
        } *//*else if (invoiceRecord.getLineItemAdditionalChargeCode() != null && invoiceRecord.getLineItemAdditionalChargeCode().equalsIgnoreCase("LD") && item.getItemTypeCode().equalsIgnoreCase("ITEM")) {
            oleInvoiceItem.
            oleInvoiceItem.setItemDiscountType("#"); // % or #
            oleInvoiceItem.setItemDiscount(new KualiDecimal(invoiceRecord.getLineItemAdditionalCharge() != null ? invoiceRecord.getLineItemAdditionalCharge() : invoiceRecord.getLineItemAdditionalCharge()));
        }*/
        //else {
        if (invoiceRecord.getUnitPrice() != null) {
            oleInvoiceItem.setItemUnitPrice(new BigDecimal(invoiceRecord.getUnitPrice()));
        }
        oleInvoiceItem.setItemTypeCode("ITEM");
        oleInvoiceItem.setItemDiscountType("%"); // % or #
        oleInvoiceItem.setItemDiscount(new KualiDecimal(invoiceRecord.getLineItemAdditionalCharge() != null ? invoiceRecord.getLineItemAdditionalCharge() : invoiceRecord.getLineItemAdditionalCharge()));
        //}
        oleInvoiceItem.setItemTitleId(null);
        if (invoiceRecord.getLineItemAdditionalCharge() != null) {
            oleInvoiceItem.setVendorItemIdentifier(null);
        }
        if(StringUtils.isNotBlank(invoiceRecord.getLineItemTaxAmount())) {
            oleInvoiceItem.setUseTaxIndicator(true);
            oleInvoiceItem.setItemSalesTaxAmount(new KualiDecimal(invoiceRecord.getLineItemTaxAmount()));
            oleInvoiceItem.addToExtendedPrice(oleInvoiceItem.getItemSalesTaxAmount());
        }

        //PurchaseOrderService purchaseOrderService = (PurchaseOrderService) SpringContext.getBean("purchaseOrderService");
        // purchaseOrderDocument = purchaseOrderService.getPurchaseOrderByDocumentNumber(poItem.getDocumentNumber());
        oleInvoiceItem.setPurchaseOrderIdentifier(null);
        oleInvoiceItem.setItemLineNumber(lineItemCount);
        oleInvoiceItem.setItemNoOfParts(new KualiInteger("1"));
        oleInvoiceItem.setPoItemIdentifier(null);
        oleInvoiceItem.setAccountsPayablePurchasingDocumentLinkIdentifier((SpringContext.getBean(SequenceAccessorService.class).
                getNextAvailableSequenceNumber("AP_PUR_DOC_LNK_ID")).intValue());
        oleInvoiceItem.setOlePoOutstandingQuantity(KualiInteger.ZERO);
        //call populate VendorInfo
        OleVendorAccountInfo oleVendorAccountInfo = populateBFN(invoiceRecord.getBfnNumber());
        List accountingLine = new ArrayList();
        if (oleVendorAccountInfo != null && oleVendorAccountInfo.isActive()) {

            InvoiceAccount invoiceAccount = new InvoiceAccount();
            invoiceAccount.setAccountNumber(oleVendorAccountInfo.getAccountNumber());
            invoiceAccount.setFinancialObjectCode(oleVendorAccountInfo.getObjectCode());
            invoiceAccount.setAmount(new KualiDecimal(invoiceRecord.getUnitPrice()));
            invoiceAccount.setAccountLinePercent(new BigDecimal("100"));  // TODO: Need to get from edifact.
            invoiceAccount.setPurapItem(oleInvoiceItem);
            invoiceAccount.setItemIdentifier(oleInvoiceItem.getItemIdentifier());
            invoiceAccount.setChartOfAccountsCode(populateChartOfAccount(oleVendorAccountInfo.getAccountNumber()) != null ? populateChartOfAccount(oleVendorAccountInfo.getAccountNumber()) : invoiceRecord.getItemChartCode());     // TODO: Need to get chart of Account based on account number and object code.
            accountingLine.add(invoiceAccount);

        } else if (invoiceRecord.getFundCode() != null) {
            accountingLine = getAccountingLinesFromFundCode(invoiceRecord, oleInvoiceItem);
        }
        else if (invoiceRecord.getAccountNumber() != null && invoiceRecord.getObjectCode() != null) {
            InvoiceAccount invoiceAccount = new InvoiceAccount();
            invoiceAccount.setAccountNumber(invoiceRecord.getAccountNumber());
            invoiceAccount.setFinancialObjectCode(invoiceRecord.getObjectCode());
            invoiceAccount.setAmount(new KualiDecimal(invoiceRecord.getUnitPrice()));
            invoiceAccount.setAccountLinePercent(new BigDecimal("100"));  // TODO: Need to get from edifact.
            invoiceAccount.setPurapItem(oleInvoiceItem);
            invoiceAccount.setItemIdentifier(oleInvoiceItem.getItemIdentifier());
            invoiceAccount.setChartOfAccountsCode(populateChartOfAccount(invoiceRecord.getAccountNumber()));
            accountingLine.add(invoiceAccount);

        }

        oleInvoiceItem.setSourceAccountingLines(accountingLine);
        oleInvoiceItem.setPostingYear(SpringContext.getBean(UniversityDateService.class).getCurrentUniversityDate().getUniversityFiscalYear());
        SimpleDateFormat dateFromRawFile = new SimpleDateFormat(org.kuali.ole.OLEConstants.DATE_FORMAT);
        oleInvoiceItem.setSubscriptionFromDate(invoiceRecord.getSubscriptionPeriodFrom()!= null ? new java.sql.Date(dateFromRawFile.parse(invoiceRecord.getSubscriptionPeriodFrom()).getTime()):null);
        oleInvoiceItem.setSubscriptionToDate(invoiceRecord.getSubscriptionPeriodTo()!= null ? new java.sql.Date(dateFromRawFile.parse(invoiceRecord.getSubscriptionPeriodTo()).getTime()):null);
        if(!StringUtils.isBlank(invoiceDocument.getInvoiceCurrencyType())){
            invoiceDocument.setInvoiceCurrencyTypeId(new Long(invoiceDocument.getInvoiceCurrencyType()));
            String currencyType = getInvoiceService().getCurrencyType(invoiceDocument.getInvoiceCurrencyType());
            if (StringUtils.isNotBlank(currencyType)) {
                if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                    setItemForeignDetails(oleInvoiceItem,invoiceDocument.getInvoiceCurrencyType(),invoiceRecord);
                }
            }
        }
        invoiceDocument.getItems().add(oleInvoiceItem);

        /* if (invoiceRecord.getAdditionalChargeCode() != null && invoiceRecord.getAdditionalChargeCode().equalsIgnoreCase("SVC") && item.getItemTypeCode().equalsIgnoreCase("SPHD")) {
                    item.setItemUnitPrice(new BigDecimal(invoiceRecord.getAdditionalCharge() != null ? invoiceRecord.getAdditionalCharge() : invoiceRecord.getAdditionalCharge()));
                    item.setPurchaseOrderIdentifier(purchaseOrderDocument != null && purchaseOrderDocument.getPurapDocumentIdentifier() != null ?
                            purchaseOrderDocument.getPurapDocumentIdentifier() : null);
                } else if (invoiceRecord.getLineItemAdditionalChargeCode() != null && invoiceRecord.getLineItemAdditionalChargeCode().equalsIgnoreCase("LD") && item.getItemTypeCode().equalsIgnoreCase("ITEM")) {

                    if ((item.getItemDescription().contains(invoiceRecord.getISBN())
                            || (invoiceRecord.getVendorItemIdentifier() != null ? invoiceRecord.getVendorItemIdentifier().equalsIgnoreCase(olePurchaseOrderItems != null && olePurchaseOrderItems.size() > 0 ? item.getVendorItemIdentifier() : "") : false)
                            || (invoiceRecord.getPurchaseOrderNumber() != null ? invoiceRecord.getPurchaseOrderNumber().equals(item.getPurchaseOrderDocument() != null ? item.getPurchaseOrderDocument().getPurapDocumentIdentifier(): null):false))) {


                        item.setItemDiscountType("#"); // % or #
                        item.setItemDiscount(new KualiDecimal(invoiceRecord.getLineItemAdditionalCharge() != null ? invoiceRecord.getLineItemAdditionalCharge() : invoiceRecord.getLineItemAdditionalCharge()));
                    }
                }
        */

        itemMap.put("invoiceDocument", invoiceDocument);
        return itemMap;
    }

    private HashMap createInvoiceItem(OleInvoiceRecord invoiceRecord, OleInvoiceDocument invoiceDocument
            , HashMap itemMap) throws Exception {
        OleInvoiceItem oleInvoiceItem = new OleInvoiceItem();
        oleInvoiceItem.setItemQuantity(new KualiDecimal(invoiceRecord.getQuantity()));
        oleInvoiceItem.setItemListPrice(new KualiDecimal(invoiceRecord.getListPrice()));
        oleInvoiceItem.setItemUnitPrice(new BigDecimal(invoiceRecord.getUnitPrice()));
        oleInvoiceItem.setItemTypeCode("ITEM");
        oleInvoiceItem.setItemDiscountType("%");
        oleInvoiceItem.setItemDiscount(new KualiDecimal(invoiceRecord.getLineItemAdditionalCharge() != null ? invoiceRecord.getLineItemAdditionalCharge() : invoiceRecord.getLineItemAdditionalCharge()));
        oleInvoiceItem.setItemTitleId(invoiceRecord.getItemTitleIdForMRC());
        oleInvoiceItem.setItemDescription(invoiceRecord.getItemDescription());
        if (invoiceRecord.getLineItemAdditionalCharge() != null) {
            oleInvoiceItem.setVendorItemIdentifier(null);
        }
        oleInvoiceItem.setPurchaseOrderIdentifier(null);
        oleInvoiceItem.setItemNoOfParts(new KualiInteger("1"));
        oleInvoiceItem.setPoItemIdentifier(null);
        oleInvoiceItem.setAccountsPayablePurchasingDocumentLinkIdentifier((SpringContext.getBean(SequenceAccessorService.class).
                getNextAvailableSequenceNumber("AP_PUR_DOC_LNK_ID")).intValue());
        oleInvoiceItem.setOlePoOutstandingQuantity(KualiInteger.ZERO);
        List accountingLine = new ArrayList();
        if (invoiceRecord.getFundCode() != null) {
            accountingLine = getAccountingLinesFromFundCode(invoiceRecord, oleInvoiceItem);
        }
        else if (invoiceRecord.getAccountNumber() != null && invoiceRecord.getObjectCode() != null) {
            InvoiceAccount invoiceAccount = new InvoiceAccount();
            invoiceAccount.setAccountNumber(invoiceRecord.getAccountNumber());
            invoiceAccount.setFinancialObjectCode(invoiceRecord.getObjectCode());
            invoiceAccount.setAmount(new KualiDecimal(invoiceRecord.getUnitPrice()));
            invoiceAccount.setAccountLinePercent(new BigDecimal("100"));
            invoiceAccount.setPurapItem(oleInvoiceItem);
            invoiceAccount.setItemIdentifier(oleInvoiceItem.getItemIdentifier());
            invoiceAccount.setChartOfAccountsCode(populateChartOfAccount(invoiceRecord.getAccountNumber()));
            if(checkForValidObjectCode(invoiceRecord,invoiceAccount)){
                accountingLine.add(invoiceAccount);
            }
        }
        oleInvoiceItem.setSourceAccountingLines(accountingLine);
        oleInvoiceItem.setPostingYear(SpringContext.getBean(UniversityDateService.class).getCurrentUniversityDate().getUniversityFiscalYear());
        SimpleDateFormat dateFromRawFile = new SimpleDateFormat(org.kuali.ole.OLEConstants.DATE_FORMAT);
        oleInvoiceItem.setSubscriptionFromDate(invoiceRecord.getSubscriptionPeriodFrom()!= null ? new java.sql.Date(dateFromRawFile.parse(invoiceRecord.getSubscriptionPeriodFrom()).getTime()):null);
        oleInvoiceItem.setSubscriptionToDate(invoiceRecord.getSubscriptionPeriodTo()!= null ? new java.sql.Date(dateFromRawFile.parse(invoiceRecord.getSubscriptionPeriodTo()).getTime()):null);
        if(!StringUtils.isBlank(invoiceDocument.getInvoiceCurrencyType())){
            invoiceDocument.setInvoiceCurrencyTypeId(new Long(invoiceDocument.getInvoiceCurrencyType()));
            String currencyType = getInvoiceService().getCurrencyType(invoiceDocument.getInvoiceCurrencyType());
            if (StringUtils.isNotBlank(currencyType)) {
                if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                    setItemForeignDetails(oleInvoiceItem,invoiceDocument.getInvoiceCurrencyType(),invoiceRecord);
                }
            }
        }
        invoiceDocument.getItems().add(oleInvoiceItem);
        itemMap.put("invoiceDocument", invoiceDocument);
        return itemMap;
    }

    private List getAccountingLinesFromFundCode(OleInvoiceRecord invoiceRecord, OleInvoiceItem oleInvoiceItem) {
        List accountingLine = new ArrayList();
        Map fundCodeMap = new HashMap<>();
        fundCodeMap.put(OLEConstants.OLEEResourceRecord.FUND_CODE, invoiceRecord.getFundCode());
        List<OleFundCode> fundCodeList = (List) getBusinessObjectService().findMatching(OleFundCode.class, fundCodeMap);
        if (CollectionUtils.isNotEmpty(fundCodeList)) {
            OleFundCode oleFundCode = fundCodeList.get(0);
            List<OleFundCodeAccountingLine> fundCodeAccountingLineList = oleFundCode.getOleFundCodeAccountingLineList();
            for (OleFundCodeAccountingLine oleFundCodeAccountingLine : fundCodeAccountingLineList) {
                InvoiceAccount invoiceAccount = new InvoiceAccount();
                invoiceAccount.setChartOfAccountsCode(oleFundCodeAccountingLine.getChartCode());
                invoiceAccount.setAccountNumber(oleFundCodeAccountingLine.getAccountNumber());
                if (StringUtils.isNotBlank(oleFundCodeAccountingLine.getSubAccount())) {
                    invoiceAccount.setSubAccountNumber(oleFundCodeAccountingLine.getSubAccount());
                }
                invoiceAccount.setFinancialObjectCode(oleFundCodeAccountingLine.getObjectCode());
                if (StringUtils.isNotBlank(oleFundCodeAccountingLine.getSubObject())) {
                    invoiceAccount.setFinancialSubObjectCode(oleFundCodeAccountingLine.getSubObject());
                }
                if (StringUtils.isNotBlank(oleFundCodeAccountingLine.getProject())) {
                    invoiceAccount.setProjectCode(oleFundCodeAccountingLine.getProject());
                }
                if (StringUtils.isNotBlank(oleFundCodeAccountingLine.getOrgRefId())) {
                    invoiceAccount.setOrganizationReferenceId(oleFundCodeAccountingLine.getOrgRefId());
                }
                invoiceAccount.setAccountLinePercent(oleFundCodeAccountingLine.getPercentage());
                invoiceAccount.setPurapItem(oleInvoiceItem);
                invoiceAccount.setItemIdentifier(oleInvoiceItem.getItemIdentifier());
                accountingLine.add(invoiceAccount);
            }
        }
        return accountingLine;
    }

    private void setItemForeignDetails(OleInvoiceItem oleInvoiceItem,String invoiceCurrencyType, OleInvoiceRecord invoiceRecord){
        KualiDecimal foreignListPrice = new KualiDecimal(invoiceRecord.getForeignListPrice());
        oleInvoiceItem.setItemForeignListPrice(new KualiDecimal(invoiceRecord.getForeignListPrice()));
        oleInvoiceItem.setItemForeignDiscount(oleInvoiceItem.getItemDiscount() == null ? new KualiDecimal(0.0) : oleInvoiceItem.getItemDiscount());
        oleInvoiceItem.setItemForeignDiscountType(oleInvoiceItem.getItemDiscountType() != null ? oleInvoiceItem.getItemDiscountType() : "%");
        if(oleInvoiceItem.getItemForeignDiscountType().equalsIgnoreCase("%")){
            BigDecimal discount = ((new BigDecimal(String.valueOf(foreignListPrice)).multiply(new BigDecimal(oleInvoiceItem.getForeignDiscount())))).divide(new BigDecimal(100));
            oleInvoiceItem.setItemForeignUnitCost(new KualiDecimal(new BigDecimal(String.valueOf(foreignListPrice)).subtract(discount)));
        }
        else {
            oleInvoiceItem.setItemForeignUnitCost(new KualiDecimal(new BigDecimal(String.valueOf(foreignListPrice)).subtract(new BigDecimal(oleInvoiceItem.getForeignDiscount()))));
        }
        oleInvoiceItem.setItemExchangeRate((getInvoiceService().getExchangeRate(invoiceCurrencyType).getExchangeRate()));
        if(StringUtils.isNotBlank(invoiceRecord.getInvoiceCurrencyExchangeRate())){
            oleInvoiceItem.setItemUnitCostUSD(new KualiDecimal(oleInvoiceItem.getItemForeignUnitCost().bigDecimalValue().divide(new BigDecimal(invoiceRecord.getInvoiceCurrencyExchangeRate()), 4, BigDecimal.ROUND_HALF_UP)));
        }else{
            oleInvoiceItem.setItemUnitCostUSD(new KualiDecimal(oleInvoiceItem.getItemForeignUnitCost().bigDecimalValue().divide(oleInvoiceItem.getItemExchangeRate(), 4, BigDecimal.ROUND_HALF_UP)));
        }
        oleInvoiceItem.setItemUnitPrice(oleInvoiceItem.getItemUnitCostUSD().bigDecimalValue());
        oleInvoiceItem.setItemListPrice(oleInvoiceItem.getItemUnitCostUSD());
    }

    private boolean checkForValidObjectCode(OleInvoiceRecord oleInvoiceRecord,InvoiceAccount invoiceAccount){
        Map<String,String> chartCodeMap = new HashMap<>();
        chartCodeMap.put("chartOfAccountsCode",invoiceAccount.getChartOfAccountsCode());
        List<ObjectCode> objectCodeList = (List<ObjectCode>) getBusinessObjectService().findMatching(ObjectCode.class, chartCodeMap);
        if(objectCodeList != null && objectCodeList.size() > 0){
            for(int recCount = 0;recCount < objectCodeList.size();recCount++){
                if(oleInvoiceRecord.getObjectCode().equalsIgnoreCase(objectCodeList.get(recCount).getFinancialObjectCode())){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isUnlinkPO(Map itemMap) {
        if (itemMap.containsKey("noOfItems")) {
            int noOfItems = 0;
            if (itemMap.get("noOfItems") != null) {
                noOfItems = Integer.parseInt(itemMap.get("noOfItems").toString());
                if (noOfItems > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        if (!itemMap.containsKey("noOfItems") && !itemMap.containsKey("noOfUnlinkItems")){
            return true;
        }
        return false;
    }

    private void validateInvoiceRecordValues(OleInvoiceRecord invoiceRecord) throws Exception {
        boolean validQuantity = validateForNumber(invoiceRecord.getQuantity());
        if(!validQuantity){
            failureRecords.append(OLEConstants.INVALID_QTY + "  "+ invoiceRecord.getQuantity()+"\n" );
        }
        boolean validListPrice = validateDestinationFieldValues(invoiceRecord.getListPrice());
        if(!validListPrice){
            failureRecords.append(OLEConstants.INVALID_LIST_PRICE + "  " + invoiceRecord.getListPrice()+"\n");
        }
        boolean validVendorNumber = validateVendorNumber(invoiceRecord.getVendorNumber());
        if(!validVendorNumber){
            failureRecords.append(org.kuali.ole.OLEConstants.INVALID_VENDOR_NUMBER + "  "+ invoiceRecord.getVendorNumber() + "\n");
        }
        if(!StringUtils.isBlank(failureRecords.toString())){
            createBatchErrorAttachmentFile(failureRecords.toString());
        }
    }

    @Override
    public void prepareForWrite() throws Exception {
        ConfigurationService kualiConfiguration = SpringContext.getBean(ConfigurationService.class);
        GlobalVariables.setUserSession(new UserSession(processDef.getUser()));
        Set<Integer> lineNumbers = new TreeSet<>();
        Set<String> fdocNumbers = new TreeSet<>();
        Set<BigDecimal> unitPrize = new TreeSet<>();
        Set<String> version = new TreeSet<>();
        OleInvoiceDocument invoiceDocument = null;
        OleInvoiceRecord invoiceRecord = null;
        List<OleInvoiceDocument> allInvoiceDocument = new ArrayList<>();
        OleInvoiceRecordHandler oleInvoiceRecordHandler = new OleInvoiceRecordHandler();
        Map<String, List<OleInvoiceRecord>> map = new HashMap();
        try {
            if (this.processDef.getUploadFileName().endsWith(".mrc")) {
                convertXMLToPojos(map, marcXMLContent);
            } else {
                convertXMLToPojos(map);
            }
        } catch (Exception e) {
            createBatchErrorAttachmentFile(failureRecords.append(e.getMessage()).toString());
            LOG.error(e, e);
            job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
            job.setStatusDesc(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
            throw e;
        }
        oleInvoiceRecordHandler.setOleInvoiceLineItemRecords(map);
        int totalInvoiceFileCount = 0;
        // int totalInvoiceProcessedCount = 0;
        int successCount = 0;
        try {
            for (Map.Entry invoiceRecordList : oleInvoiceRecordHandler.getOleInvoiceLineItemRecords().entrySet()) {
                List<OleInvoiceRecord> oleInvoiceRecord = (List<OleInvoiceRecord>)invoiceRecordList.getValue();
                totalInvoiceFileCount = oleInvoiceRecordHandler.getOleInvoiceLineItemRecords().size();

                Person currentUser = GlobalVariables.getUserSession().getPerson();

                try {
                    invoiceDocument = initiateInvoiceDocument(invoiceDocument, currentUser);
                } catch (Exception e) {
                    LOG.error(e, e);
                    failureRecords.append("Unable to create Invoice document");
                    failureRecords.append("\n");
                    createBatchErrorAttachmentFile(failureRecords.append(e.getMessage()).toString());
                    //job.setStatusDesc(OLEConstants.OLEBatchProcess.JOB_STATUS_STOPPED);
                }
                //Map<Integer, List<OleInvoiceRecord>> oleInvoiceRecord = oleInvoiceRecordHandler.getOleInvoiceLineItemRecords();
                //List<OleInvoiceRecord> oleInvoiceRecordList = oleInvoiceRecord.get(0);
                SimpleDateFormat dateFromRawFile = new SimpleDateFormat("yyyyMMdd");
                OleInvoiceService oleInvoiceService = getInvoiceService();
                // if (oleInvoiceRecordList != null && !oleInvoiceRecordList.isEmpty()) {
                OleInvoiceRecord invoiceRecordHeader = oleInvoiceRecord.get(0);
                invoiceDocument.setInvoiceNumber(invoiceRecordHeader.getInvoiceNumber());
                //invoiceDocument.setVendorInvoiceAmount(new KualiDecimal(invoiceRecordHeader.getVendorInvoiceAmount()));
                invoiceDocument.setVendorInvoiceAmount(null);
                Date rawDate = null;
                try {
                    rawDate = dateFromRawFile.parse(invoiceRecordHeader.getInvoiceDate());
                } catch (Exception e) {
                    LOG.error(e, e);
                    failureRecords.append("Invoice Date: " + invoiceRecordHeader.getInvoiceDate());
                    failureRecords.append("\n");
                    failureRecords.append("Cannot parse Invoice Date" + e);
                    createBatchErrorAttachmentFile(failureRecords.toString());
                    throw e;

                }
               // try{
                if (rawDate != null) {
                    invoiceDocument.setInvoiceDate(new java.sql.Date(rawDate.getTime()));
                }
                //}
                int lineItemCount = 0;
                for (int j = 0; j < oleInvoiceRecord.size(); j++) {
                    invoiceRecord = oleInvoiceRecord.get(j);
                    String[] vendorIds = invoiceRecord.getVendorNumber() != null ? invoiceRecord.getVendorNumber().split("-") : new String[0];
                    if(!StringUtils.isBlank(invoiceRecord.getCurrencyTypeId())){
                        setDocumentForeignDetails(invoiceDocument,invoiceRecord);
                    }
                    else {
                        Map vendorDetailMap = new HashMap();
                        vendorDetailMap.put("vendorHeaderGeneratedIdentifier", vendorIds.length > 0 ? vendorIds[0] : "");
                        vendorDetailMap.put("vendorDetailAssignedIdentifier", vendorIds.length > 1 ? vendorIds[1] : "");
                        VendorDetail vendorDetail = (VendorDetail) getBusinessObjectService().findByPrimaryKey(VendorDetail.class, vendorDetailMap);
                        if(vendorDetail != null){
                            invoiceRecord.setCurrencyTypeId(vendorDetail.getCurrencyTypeId().toString());
                            setDocumentForeignDetails(invoiceDocument,invoiceRecord);
                        }
                    }
                    List<OlePurchaseOrderItem> olePurchaseOrderItems = null;
                    HashMap itemMap = new HashMap();
                    List<OlePurchaseOrderItem> dummyPurchaseOrderItemsWithPO = ruleOneScenarioWithPurchaseOrderNumber(invoiceRecord, vendorIds);
                    if(dummyPurchaseOrderItemsWithPO!=null && dummyPurchaseOrderItemsWithPO.size() > 0){
                        olePurchaseOrderItems = sortAndProcessPurchaseOrderItems(olePurchaseOrderItems, itemMap, dummyPurchaseOrderItemsWithPO);
                    }
                    if(olePurchaseOrderItems==null || olePurchaseOrderItems.size()<1) {
                        olePurchaseOrderItems = ruleOneScenarioWithOutPurchaseOrderNumber(invoiceRecord, vendorIds, olePurchaseOrderItems, itemMap);
                    }
                    PurchaseOrderDocument purchaseOrderDocument = null;
                    if (olePurchaseOrderItems != null && olePurchaseOrderItems.size() > 0 && isUnlinkPO(itemMap)) {

                        invoiceRecord.setPurchaseOrderNumber((olePurchaseOrderItems != null && olePurchaseOrderItems.size() > 0) ? olePurchaseOrderItems.get(0).getPurapDocumentIdentifier() : null);
                        if ((olePurchaseOrderItems != null && olePurchaseOrderItems.size() > 0 && olePurchaseOrderItems.get(0) != null && olePurchaseOrderItems.get(0).getPurchaseOrder() != null && olePurchaseOrderItems.get(0).getPurchaseOrder().getPostingYear() != null)) {
                            invoiceDocument.setPostingYear((olePurchaseOrderItems != null && olePurchaseOrderItems.size() > 0) ? olePurchaseOrderItems.get(0).getPurchaseOrder().getPostingYear() : null);
                        }
                        //invoiceDocument.setProrateBy(org.kuali.ole.sys.OLEConstants.MANUAL_PRORATE);
                        invoiceDocument.setInvoicePayDate(SpringContext.getBean(InvoiceService.class).calculatePayDate(invoiceDocument.getInvoiceDate(), invoiceDocument.getVendorPaymentTerms()));
                        itemMap = addInvoiceItem(olePurchaseOrderItems, invoiceRecord, invoiceDocument, purchaseOrderDocument, itemMap,lineNumbers,fdocNumbers,unitPrize,version);
                        if (itemMap != null && itemMap.containsKey("invoiceDocument") && itemMap.containsKey("purchaseOrderDocument")) {
                            invoiceDocument = (OleInvoiceDocument) itemMap.get("invoiceDocument");
                            purchaseOrderDocument = (PurchaseOrderDocument) itemMap.get("purchaseOrderDocument");
                        }
                    } else if ((olePurchaseOrderItems == null || olePurchaseOrderItems.size() < 1) && isUnlinkPO(itemMap)) {
                        if (invoiceRecord.getVendorNumber() != null && !invoiceRecord.getVendorNumber().isEmpty()) {
                            if ((invoiceRecord.getISBN() != null && !invoiceRecord.getISBN().isEmpty() ||
                                    (invoiceRecord.getISSN() != null && !invoiceRecord.getISSN().isEmpty()))) {
                                org.kuali.ole.docstore.common.search.SearchParams search_Params = new org.kuali.ole.docstore.common.search.SearchParams();
                                SearchResponse searchResponse = null;
                                // Retrieve bib id through solr query
                                if  (invoiceRecord.getISBN() != null && !invoiceRecord.getISBN().isEmpty()) {
                                    search_Params.getSearchConditions().add(search_Params.buildSearchCondition("", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(),"common_identifier_search", invoiceRecord.getISBN()), ""));
                                }
                                if (invoiceRecord.getISSN() != null && !invoiceRecord.getISSN().isEmpty()) {
                                    search_Params.getSearchConditions().add(search_Params.buildSearchCondition("", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(),"common_identifier_search", invoiceRecord.getISSN()), ""));
                                }
                                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), "id"));
                                String titleId = null;
                                searchResponse = getDocstoreClientLocator().getDocstoreClient().search(search_Params);
                                if (searchResponse.getSearchResults() != null && searchResponse.getSearchResults().size() > 0) {
                                    titleId = searchResponse.getSearchResults().get(0).getSearchResultFields().get(0).getFieldValue() != null ? searchResponse.getSearchResults().get(0).getSearchResultFields().get(0).getFieldValue() : "";
                                }
                                HashMap itemTitleMap = new HashMap();

                                List<OlePurchaseOrderItem> dummyTitlePurchaseOrderItemsWithPO = ruleTwoScenarioWithPONumber(invoiceRecord, vendorIds, olePurchaseOrderItems, titleId);
                                if(dummyTitlePurchaseOrderItemsWithPO!=null && dummyTitlePurchaseOrderItemsWithPO.size()>0){
                                    olePurchaseOrderItems = sortAndProcessPurchaseOrderItems(olePurchaseOrderItems, itemTitleMap, dummyTitlePurchaseOrderItemsWithPO);
                                }
                                if(olePurchaseOrderItems==null || olePurchaseOrderItems.size()<1) {
                                    olePurchaseOrderItems = ruleTwoWithOutPONumber(vendorIds, olePurchaseOrderItems, titleId,itemTitleMap);
                                }
                                if (titleId != null && olePurchaseOrderItems != null && olePurchaseOrderItems.size() > 0 && isUnlinkPO(itemMap)) {
                                    invoiceRecord.setPurchaseOrderNumber((olePurchaseOrderItems != null && olePurchaseOrderItems.size() > 0 && olePurchaseOrderItems.get(0) != null) ? olePurchaseOrderItems.get(0).getPurapDocumentIdentifier() : null);
                                    if ((olePurchaseOrderItems != null && olePurchaseOrderItems.size() > 0 && olePurchaseOrderItems.get(0) != null && olePurchaseOrderItems.get(0).getPurchaseOrder() != null && olePurchaseOrderItems.get(0).getPurchaseOrder().getPostingYear() != null)) {
                                        invoiceDocument.setPostingYear(olePurchaseOrderItems.get(0).getPurchaseOrder().getPostingYear());
                                    }
                                    //invoiceDocument.setProrateBy(org.kuali.ole.sys.OLEConstants.MANUAL_PRORATE);
                                    invoiceDocument.setInvoicePayDate(SpringContext.getBean(InvoiceService.class).calculatePayDate(invoiceDocument.getInvoiceDate(), invoiceDocument.getVendorPaymentTerms()));
                                    itemMap = addInvoiceItem(olePurchaseOrderItems, invoiceRecord, invoiceDocument, purchaseOrderDocument, itemMap,lineNumbers,fdocNumbers,unitPrize,version);
                                    if (itemMap != null && itemMap.containsKey("invoiceDocument") && itemMap.containsKey("purchaseOrderDocument")) {
                                        invoiceDocument = (OleInvoiceDocument) itemMap.get("invoiceDocument");
                                        purchaseOrderDocument = (PurchaseOrderDocument) itemMap.get("purchaseOrderDocument");
                                    }
                                } else if (olePurchaseOrderItems == null || olePurchaseOrderItems.size() < 1) {

                                    String[] vendorId = invoiceRecord.getVendorNumber() != null ? invoiceRecord.getVendorNumber().split("-") : new String[0];
                                    Map itemTitleId = new HashMap();
                                    itemTitleId.put("purchaseOrder.purapDocumentIdentifier", invoiceRecord.getPurchaseOrderNumber());
                                    itemTitleId.put("purchaseOrder.vendorHeaderGeneratedIdentifier", vendorId.length > 0 ? vendorId[0] : "");
                                    itemTitleId.put("purchaseOrder.vendorDetailAssignedIdentifier", vendorId.length > 1 ? vendorId[1] : "");
                                    if (invoiceRecord.getPurchaseOrderNumber() != null) {
                                        List<OlePurchaseOrderItem> dummyPurchaseOrderDocuments = (List<OlePurchaseOrderItem>) getBusinessObjectService().findMatching(OlePurchaseOrderItem.class, itemTitleId);
                                        if(dummyPurchaseOrderDocuments!=null && dummyPurchaseOrderDocuments.size()>0) {
                                            olePurchaseOrderItems = validateAndProcessPurchaseOrderItems(olePurchaseOrderItems, dummyPurchaseOrderDocuments, itemMap);
                                        }
                                        if (olePurchaseOrderItems != null && olePurchaseOrderItems.size() > 0) {
                                            invoiceRecord.setPurchaseOrderNumber((olePurchaseOrderItems != null && olePurchaseOrderItems.size() > 0 && olePurchaseOrderItems.get(0) != null) ? olePurchaseOrderItems.get(0).getPurapDocumentIdentifier() : null);
                                            if ((olePurchaseOrderItems != null && olePurchaseOrderItems.size() > 0 && olePurchaseOrderItems.get(0) != null && olePurchaseOrderItems.get(0).getPurchaseOrder() != null && olePurchaseOrderItems.get(0).getPurchaseOrder().getPostingYear() != null)) {
                                                invoiceDocument.setPostingYear(olePurchaseOrderItems.get(0).getPurchaseOrder().getPostingYear());
                                            }
                                            //invoiceDocument.setProrateBy(org.kuali.ole.sys.OLEConstants.MANUAL_PRORATE);
                                            invoiceDocument.setInvoicePayDate(SpringContext.getBean(InvoiceService.class).calculatePayDate(invoiceDocument.getInvoiceDate(), invoiceDocument.getVendorPaymentTerms()));
                                            Integer purchaseOrderCount=0;
                                            if(itemMap.containsKey("noOfItems")) {
                                                purchaseOrderCount = (Integer) itemMap.get("noOfItems");
                                            }
                                            if(purchaseOrderCount==1) {
                                                itemMap = addInvoiceItem(olePurchaseOrderItems, invoiceRecord, invoiceDocument, purchaseOrderDocument, itemMap, lineNumbers, fdocNumbers, unitPrize, version);
                                            }
                                            if (itemMap != null && itemMap.containsKey("invoiceDocument") && itemMap.containsKey("purchaseOrderDocument")) {
                                                invoiceDocument = (OleInvoiceDocument) itemMap.get("invoiceDocument");
                                                purchaseOrderDocument = (PurchaseOrderDocument) itemMap.get("purchaseOrderDocument");
                                            }
                                        }
                                    }
                                }
                            }
                            else if (invoiceRecord.getPurchaseOrderNumber() != null) {
                                String[] vendorId = invoiceRecord.getVendorNumber() != null ? invoiceRecord.getVendorNumber().split("-") : new String[0];
                                Map itemTitleId = new HashMap();
                                itemTitleId.put("purchaseOrder.purapDocumentIdentifier", invoiceRecord.getPurchaseOrderNumber());
                                itemTitleId.put("purchaseOrder.vendorHeaderGeneratedIdentifier", vendorId.length > 0 ? vendorId[0] : "");
                                itemTitleId.put("purchaseOrder.vendorDetailAssignedIdentifier", vendorId.length > 1 ? vendorId[1] : "");
                                if (invoiceRecord.getPurchaseOrderNumber() != null) {
                                    List<OlePurchaseOrderItem> dummyPurchaseOrderDocuments = (List<OlePurchaseOrderItem>) getBusinessObjectService().findMatching(OlePurchaseOrderItem.class, itemTitleId);
                                    if(dummyPurchaseOrderDocuments!=null && dummyPurchaseOrderDocuments.size()>0) {
                                        olePurchaseOrderItems = validateAndProcessPurchaseOrderItems(olePurchaseOrderItems, dummyPurchaseOrderDocuments, itemMap);
                                    }
                                    if (olePurchaseOrderItems != null && olePurchaseOrderItems.size() > 0) {
                                        invoiceRecord.setPurchaseOrderNumber((olePurchaseOrderItems != null && olePurchaseOrderItems.size() > 0 && olePurchaseOrderItems.get(0) != null) ? olePurchaseOrderItems.get(0).getPurapDocumentIdentifier() : null);
                                        if ((olePurchaseOrderItems != null && olePurchaseOrderItems.size() > 0 && olePurchaseOrderItems.get(0) != null && olePurchaseOrderItems.get(0).getPurchaseOrder() != null && olePurchaseOrderItems.get(0).getPurchaseOrder().getPostingYear() != null)) {
                                            invoiceDocument.setPostingYear(olePurchaseOrderItems.get(0).getPurchaseOrder().getPostingYear());
                                        }
                                        //invoiceDocument.setProrateBy(org.kuali.ole.sys.OLEConstants.MANUAL_PRORATE);
                                        invoiceDocument.setInvoicePayDate(SpringContext.getBean(InvoiceService.class).calculatePayDate(invoiceDocument.getInvoiceDate(), invoiceDocument.getVendorPaymentTerms()));
                                        Integer purchaseOrderCount=0;
                                        if(itemMap.containsKey("noOfItems")) {
                                            purchaseOrderCount = (Integer) itemMap.get("noOfItems");
                                        }
                                        if(purchaseOrderCount==1) {
                                            itemMap = addInvoiceItem(olePurchaseOrderItems, invoiceRecord, invoiceDocument, purchaseOrderDocument, itemMap, lineNumbers, fdocNumbers, unitPrize, version);
                                        }
                                        if (itemMap != null && itemMap.containsKey("invoiceDocument") && itemMap.containsKey("purchaseOrderDocument")) {
                                            invoiceDocument = (OleInvoiceDocument) itemMap.get("invoiceDocument");
                                            purchaseOrderDocument = (PurchaseOrderDocument) itemMap.get("purchaseOrderDocument");
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (purchaseOrderDocument != null) {
                        for (OleInvoiceItem item : (List<OleInvoiceItem>) invoiceDocument.getItems()) {
                            if (invoiceRecord.getAdditionalChargeCode() != null && invoiceRecord.getAdditionalChargeCode().equalsIgnoreCase("SVC") && item.getItemTypeCode().equalsIgnoreCase("SPHD")) {
                                item.setItemUnitPrice(new BigDecimal(invoiceRecord.getAdditionalCharge() != null ? invoiceRecord.getAdditionalCharge() : invoiceRecord.getAdditionalCharge()));
                                item.setPurchaseOrderIdentifier(purchaseOrderDocument.getPurapDocumentIdentifier());
                            } else if (invoiceRecord.getLineItemAdditionalChargeCode() != null && invoiceRecord.getLineItemAdditionalChargeCode().equalsIgnoreCase("LD") && item.getItemTypeCode().equalsIgnoreCase("ITEM")) {

                                if ((item.getItemDescription().contains(invoiceRecord.getISBN())
                                        || (invoiceRecord.getVendorItemIdentifier() != null ? invoiceRecord.getVendorItemIdentifier().equalsIgnoreCase(olePurchaseOrderItems != null && olePurchaseOrderItems.size() > 0 ? item.getVendorItemIdentifier() : "") : false)
                                        || (invoiceRecord.getPurchaseOrderNumber() != null ? invoiceRecord.getPurchaseOrderNumber().equals(item.getPurchaseOrderDocument() != null ? item.getPurchaseOrderDocument().getPurapDocumentIdentifier() : null) : false))) {


                                    item.setItemDiscountType("%"); // % or #
                                    item.setItemDiscount(new KualiDecimal(invoiceRecord.getLineItemAdditionalCharge() != null ? invoiceRecord.getLineItemAdditionalCharge() : invoiceRecord.getLineItemAdditionalCharge()));
                                }
                            }
                        }

                        String vendorNumber = purchaseOrderDocument.getVendorHeaderGeneratedIdentifier() + "-" + purchaseOrderDocument.getVendorDetailAssignedIdentifier();
                        oleInvoiceService.populateVendorDetail(vendorNumber, invoiceDocument);
                        invoiceDocument.setVendorCustomerNumber(invoiceRecord.getBillToCustomerID());
                        if (invoiceDocument.getPaymentMethodId() != null) {
                            invoiceDocument.setPaymentMethodIdentifier(String.valueOf(invoiceDocument.getPaymentMethodId()));
                        } else {
                            invoiceDocument.setPaymentMethodId(Integer.parseInt(OLEConstants.OleInvoiceImport.PAY_METHOD));
                            invoiceDocument.setPaymentMethodIdentifier(String.valueOf(invoiceDocument.getPaymentMethodId()));
                        }
                        //   SpringContext.getBean(OleInvoiceService.class).calculateProrateItemSurcharge(invoiceDocument);
                        SpringContext.getBean(PurapAccountingService.class).updateAccountAmounts(invoiceDocument);
                        Long nextLinkIdentifier = SpringContext.getBean(SequenceAccessorService.class).getNextAvailableSequenceNumber("AP_PUR_DOC_LNK_ID");
                        invoiceDocument.setAccountsPayablePurchasingDocumentLinkIdentifier(nextLinkIdentifier.intValue());


                        if ((invoiceDocument.getProrateBy() != null) && (invoiceDocument.getProrateBy().equals(org.kuali.ole.sys.OLEConstants.PRORATE_BY_QTY) || invoiceDocument.getProrateBy().equals(org.kuali.ole.sys.OLEConstants.PRORATE_BY_DOLLAR) || invoiceDocument.getProrateBy().equals(org.kuali.ole.sys.OLEConstants.MANUAL_PRORATE))) {
                            // set amounts on any empty
                            invoiceDocument.updateExtendedPriceOnItems();

                            // calculation just for the tax area, only at tax review stage
                            // by now, the general calculation shall have been done.
                            if (invoiceDocument.getApplicationDocumentStatus().equals(PurapConstants.PaymentRequestStatuses.APPDOC_AWAITING_TAX_REVIEW)) {
                                SpringContext.getBean(OleInvoiceService.class).calculateTaxArea(invoiceDocument);
                            }

                            updatePrice(invoiceDocument);

                            // notice we're ignoring whether the boolean, because these are just warnings they shouldn't halt anything
                            // Calculate Payment request before rules since the rule check totalAmount.
                            SpringContext.getBean(OleInvoiceService.class).calculateInvoice(invoiceDocument, true);
                            SpringContext.getBean(KualiRuleService.class).applyRules(
                                    new AttributedCalculateAccountsPayableEvent(invoiceDocument));
                        } else {
                            // set amounts on any empty
                            invoiceDocument.updateExtendedPriceOnItems();

                            // calculation just for the tax area, only at tax review stage
                            // by now, the general calculation shall have been done.
                            if (StringUtils.equals(invoiceDocument.getApplicationDocumentStatus(), PurapConstants.PaymentRequestStatuses.APPDOC_AWAITING_TAX_REVIEW)) {
                                SpringContext.getBean(OleInvoiceService.class).calculateTaxArea(invoiceDocument);
                            }

                            updatePrice(invoiceDocument);

                            // notice we're ignoring whether the boolean, because these are just warnings they shouldn't halt anything
                            //Calculate Payment request before rules since the rule check totalAmount.
                            SpringContext.getBean(OleInvoiceService.class).calculateInvoice(invoiceDocument, true);
                            SpringContext.getBean(KualiRuleService.class).applyRules(new AttributedCalculateAccountsPayableEvent(invoiceDocument));
                        }
                        if (!SpringContext.getBean(KualiRuleService.class).applyRules(new AttributedSaveDocumentEvent(invoiceDocument)) ||
                                !SpringContext.getBean(KualiRuleService.class).applyRules(new AttributedCalculateAccountsPayableEvent(invoiceDocument))) {


                            //}else {
                            List invoiceIngestFailureReason = (List) dataCarrierService.getData("invoiceIngestFailureReason");
                            if(invoiceIngestFailureReason != null && invoiceIngestFailureReason.size()>0){
                                for(int failCount = 0;failCount < invoiceIngestFailureReason.size();failCount++){
                                    failureRecords.append(invoiceIngestFailureReason.get(failCount) + "\n");
                                }
                            }
                            LOG.info("Invoice Error Message------------------->");
                            failureRecords.append("Unable to create Invoice document");
                            failureRecords.append("\n");
                            failureRecords.append("Invoice Number :" + invoiceRecord.getInvoiceNumber());
                            failureRecords.append("\n");
                            failureRecords.append("Invoice Date :" + invoiceDocument.getInvoiceDate());
                            failureRecords.append("\n");
                            if ((GlobalVariables.getMessageMap().getErrorMessages().get("GLOBAL_ERRORS")) != null) {
                                for (int error = 0; error < (GlobalVariables.getMessageMap().getErrorMessages().get("GLOBAL_ERRORS")).size(); error++) {
                                    failureRecords.append("Error Message:" + kualiConfiguration.getPropertyValueAsString((GlobalVariables.getMessageMap().getErrorMessages().get("GLOBAL_ERRORS")).get(error).getErrorKey()));
                                    failureRecords.append("\n");
                                }
                            }
                            failureRecords.append("\n");
                        }
                    } else if(itemMap.get("applicationStatus")==null || PurapConstants.PurchaseOrderStatuses.APPDOC_OPEN.equals(itemMap.get("applicationStatus"))) {
                        if(marcXMLContent != null && StringUtils.isNotBlank(marcXMLContent.toString()) ){
                            createInvoiceItem(invoiceRecord, invoiceDocument, itemMap);
                        }
                        else{
                            createInvoiceItem(lineItemCount, invoiceRecord, invoiceDocument, itemMap);
                        }
                        invoiceDocument.setAccountsPayablePurchasingDocumentLinkIdentifier((SpringContext.getBean(SequenceAccessorService.class).
                                getNextAvailableSequenceNumber("AP_PUR_DOC_LNK_ID")).intValue());
                        if (itemMap != null) {
                            invoiceDocument = (OleInvoiceDocument) itemMap.get("invoiceDocument");
                            purchaseOrderDocument = (PurchaseOrderDocument) itemMap.get("purchaseOrderDocument");
                        }
                        for (OleInvoiceItem item : (List<OleInvoiceItem>) invoiceDocument.getItems()) {
                            if (invoiceRecord.getAdditionalChargeCode() != null && invoiceRecord.getAdditionalChargeCode().equalsIgnoreCase("SVC") && item.getItemTypeCode().equalsIgnoreCase("SPHD")) {
                                item.setItemUnitPrice(new BigDecimal(invoiceRecord.getAdditionalCharge() != null ? invoiceRecord.getAdditionalCharge() : invoiceRecord.getAdditionalCharge()));
                                item.setPurchaseOrderIdentifier(purchaseOrderDocument != null && purchaseOrderDocument.getPurapDocumentIdentifier() != null ?
                                        purchaseOrderDocument.getPurapDocumentIdentifier() : null);
                            } else if (invoiceRecord.getLineItemAdditionalChargeCode() != null && invoiceRecord.getLineItemAdditionalChargeCode().equalsIgnoreCase("LD") && item.getItemTypeCode().equalsIgnoreCase("ITEM")) {

                                if ((item.getItemDescription().contains(invoiceRecord.getISBN())
                                        || (invoiceRecord.getVendorItemIdentifier() != null ? invoiceRecord.getVendorItemIdentifier().equalsIgnoreCase(olePurchaseOrderItems != null && olePurchaseOrderItems.size() > 0 ? item.getVendorItemIdentifier() : "") : false)
                                        || (invoiceRecord.getPurchaseOrderNumber() != null ? invoiceRecord.getPurchaseOrderNumber().equals(item.getPurchaseOrderDocument() != null ? item.getPurchaseOrderDocument().getPurapDocumentIdentifier(): null):false))) {


                                    item.setItemDiscountType("#"); // % or #
                                    item.setItemDiscount(new KualiDecimal(invoiceRecord.getLineItemAdditionalCharge() != null ? invoiceRecord.getLineItemAdditionalCharge() : invoiceRecord.getLineItemAdditionalCharge()));
                                }
                            }
                        }

                        String vendorNumber = invoiceRecord.getVendorNumber();
                        oleInvoiceService.populateVendorDetail(vendorNumber, invoiceDocument);
                        invoiceDocument.setVendorCustomerNumber(invoiceRecord.getBillToCustomerID());
                        if (invoiceDocument.getPaymentMethodId() != null) {
                            invoiceDocument.setPaymentMethodIdentifier(String.valueOf(invoiceDocument.getPaymentMethodId()));
                        } else {
                            invoiceDocument.setPaymentMethodId(Integer.parseInt(OLEConstants.OleInvoiceImport.PAY_METHOD));
                            invoiceDocument.setPaymentMethodIdentifier(String.valueOf(invoiceDocument.getPaymentMethodId()));
                        }
                        //   SpringContext.getBean(OleInvoiceService.class).calculateProrateItemSurcharge(invoiceDocument);
                        SpringContext.getBean(PurapAccountingService.class).updateAccountAmounts(invoiceDocument);
                        Long nextLinkIdentifier = SpringContext.getBean(SequenceAccessorService.class).getNextAvailableSequenceNumber("AP_PUR_DOC_LNK_ID");
                        // invoiceDocument.setAccountsPayablePurchasingDocumentLinkIdentifier(nextLinkIdentifier.intValue());


                        if ((invoiceDocument.getProrateBy() != null) && (invoiceDocument.getProrateBy().equals(org.kuali.ole.sys.OLEConstants.PRORATE_BY_QTY) || invoiceDocument.getProrateBy().equals(org.kuali.ole.sys.OLEConstants.PRORATE_BY_DOLLAR) || invoiceDocument.getProrateBy().equals(org.kuali.ole.sys.OLEConstants.MANUAL_PRORATE))) {
                            // set amounts on any empty
                            invoiceDocument.updateExtendedPriceOnItems();

                            // calculation just for the tax area, only at tax review stage
                            // by now, the general calculation shall have been done.
                            if (invoiceDocument.getApplicationDocumentStatus().equals(PurapConstants.PaymentRequestStatuses.APPDOC_AWAITING_TAX_REVIEW)) {
                                SpringContext.getBean(OleInvoiceService.class).calculateTaxArea(invoiceDocument);
                            }

                            updatePrice(invoiceDocument);

                            // notice we're ignoring whether the boolean, because these are just warnings they shouldn't halt anything
                            // Calculate Payment request before rules since the rule check totalAmount.
                            SpringContext.getBean(OleInvoiceService.class).calculateInvoice(invoiceDocument, true);
                            SpringContext.getBean(KualiRuleService.class).applyRules(
                                    new AttributedCalculateAccountsPayableEvent(invoiceDocument));
                        } else {
                            // set amounts on any empty
                            invoiceDocument.updateExtendedPriceOnItems();

                            // calculation just for the tax area, only at tax review stage
                            // by now, the general calculation shall have been done.
                            if (StringUtils.equals(invoiceDocument.getApplicationDocumentStatus(), PurapConstants.PaymentRequestStatuses.APPDOC_AWAITING_TAX_REVIEW)) {
                                SpringContext.getBean(OleInvoiceService.class).calculateTaxArea(invoiceDocument);

                            }

                            updatePrice(invoiceDocument);

                            // notice we're ignoring whether the boolean, because these are just warnings they shouldn't halt anything
                            //Calculate Payment request before rules since the rule check totalAmount.
                            SpringContext.getBean(OleInvoiceService.class).calculateInvoice(invoiceDocument, true);
                            SpringContext.getBean(KualiRuleService.class).applyRules(new AttributedCalculateAccountsPayableEvent(invoiceDocument));
                        }


                    }
                }
                if (!"".equals(failureRecords.toString())) {
                    createBatchErrorAttachmentFile(failureRecords.toString());
                }
                allInvoiceDocument.add(invoiceDocument);
            }
        } catch (Exception e) {
            createBatchErrorAttachmentFile(failureRecords.append(e.getMessage()).toString());
            LOG.error(e, e);
            job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_STOPPED);
            job.setStatusDesc(OLEConstants.OLEBatchProcess.JOB_STATUS_STOPPED);
            throw e;
        } finally {
            deleteBatchFile();
        }
     //   deleteBatchFile();
        if (allInvoiceDocument != null && !allInvoiceDocument.isEmpty()) {
            for (int process = 0; process < allInvoiceDocument.size(); process++) {
                invoiceDocument = allInvoiceDocument.get(process);
                if (invoiceDocument.getAccountsPayablePurchasingDocumentLinkIdentifier() != null) {
                    if (SpringContext.getBean(KualiRuleService.class).applyRules(new AttributedSaveDocumentEvent(invoiceDocument)) &&
                            SpringContext.getBean(KualiRuleService.class).applyRules(new AttributedCalculateAccountsPayableEvent(invoiceDocument))) {

                        try {
                            OLEInvoiceIngestLoadReport oleInvoiceIngestLoadReport =new OLEInvoiceIngestLoadReport();
                            oleInvoiceIngestLoadReport.setFileName(job.getUploadFileName());
                            java.sql.Date sqlDate = new java.sql.Date(new Date(System.currentTimeMillis()).getTime());
                            oleInvoiceIngestLoadReport.setDateUploaded(sqlDate);
                            oleInvoiceIngestLoadReport.setVendor(invoiceDocument.getVendorNumber());
                            getBusinessObjectService().save(oleInvoiceIngestLoadReport);
                            oleInvoiceService.autoApprovePaymentRequest(invoiceDocument);
                            successCount = process + 1;
                        } catch (Exception e) {
                            LOG.info("Invoice Error Message------------------->");
                            failureRecords.append("Unable to create Invoice document");
                            failureRecords.append("\n");
                            failureRecords.append("Invoice Number :" + invoiceRecord.getInvoiceNumber());
                            failureRecords.append("\n");
                            failureRecords.append("Invoice Date :" + invoiceDocument.getInvoiceDate());
                            failureRecords.append("\n");
                            if ((GlobalVariables.getMessageMap() != null && GlobalVariables.getMessageMap().getErrorMessages() != null) &&
                                    ((GlobalVariables.getMessageMap().getErrorMessages().size() > 0 ) ||
                                            (GlobalVariables.getMessageMap().getErrorMessages().get("GLOBAL_ERRORS")) != null)) {
                                for (int error = 0; error < (GlobalVariables.getMessageMap().getErrorMessages().get("GLOBAL_ERRORS")).size(); error++) {
                                    failureRecords.append("Error Message:" + kualiConfiguration.getPropertyValueAsString((GlobalVariables.getMessageMap().getErrorMessages().get("GLOBAL_ERRORS")).get(error).getErrorKey()));
                                    failureRecords.append("\n");
                                }
                            } else {
                                failureRecords.append("Error Message:" + e.initCause(new Throwable()));
                            }
                            failureRecords.append("\n");
                            failureRecords.append("\n");

                        }

                        job.setTotalNoOfRecords(totalInvoiceFileCount + "");
                        job.setNoOfRecordsProcessed(totalInvoiceFileCount + "");
                        job.setNoOfSuccessRecords(String.valueOf(successCount));
                        job.setNoOfFailureRecords(String.valueOf(totalInvoiceFileCount - successCount));
                        job.setStatusDesc(OLEConstants.INVOICE_UPLOAD_SUCCESS);
                        job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
                        deleteBatchFile();
                        if (!"".equals(failureRecords.toString())) {
                            createBatchErrorAttachmentFile(failureRecords.toString());
                        }
                    }
                    else {
                        if ((GlobalVariables.getMessageMap() != null &&
                                GlobalVariables.getMessageMap().getErrorMessages() != null &&
                                GlobalVariables.getMessageMap().hasErrors()) &&
                                (GlobalVariables.getMessageMap().getErrorMessages().size() > 0 )) {
                            Map<String, AutoPopulatingList<ErrorMessage>> errorMap = GlobalVariables.getMessageMap().getErrorMessages();
                            for (Map.Entry<String, AutoPopulatingList<ErrorMessage>> entry : errorMap.entrySet()) {
                                AutoPopulatingList<ErrorMessage> errors = entry.getValue();
                                    /*ErrorMessage error = errors.get(0);*/
                                    /*String[] params = error.getMessageParameters();
                                    String param = params[0];
                                    if (param.toUpperCase().contains("ITEM")) {
                                        param = error.getErrorKey();
                                    }*/
                                for(ErrorMessage error : errors){
                                    failureRecords.append("Error Message:" + MessageFormat.format(kualiConfiguration.getPropertyValueAsString(error.getErrorKey()), error.getMessageParameters()));
                                    failureRecords.append("\n");
                                }

                            }
                        }
                        job.setTotalNoOfRecords(totalInvoiceFileCount + "");
                        job.setNoOfRecordsProcessed(totalInvoiceFileCount + "");
                        job.setNoOfSuccessRecords(String.valueOf(successCount));
                        job.setNoOfFailureRecords(String.valueOf(totalInvoiceFileCount - successCount));
                        job.setStatusDesc(OLEConstants.INVOICE_UPLOAD_SUCCESS);
                        job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
                        deleteBatchFile();
                        if (!"".equals(failureRecords.toString())) {
                            createBatchErrorAttachmentFile(failureRecords.toString());
                        }
                    }
                    job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
                }
                job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
            }
            job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
        }
        if (successCount == 0) {
            OLEBatchProcessJobDetailsController.setBatchProcessJobStatusMap(job.getJobId(),OLEConstants.OLEBatchProcess.JOB_STATUS_STOPPED);
            job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_STOPPED);
        } else {
            OLEBatchProcessJobDetailsController.setBatchProcessJobStatusMap(job.getJobId(),OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
            job.setStatus(OLEConstants.OLEBatchProcess.JOB_STATUS_COMPLETED);
        }
    }

    private List<OlePurchaseOrderItem> ruleTwoWithOutPONumber(String[] vendorIds, List<OlePurchaseOrderItem> olePurchaseOrderItems, String titleId,HashMap itemTitleMap) {
        Map itemTitleIdWithOutPO = new HashMap();
        itemTitleIdWithOutPO.put("itemTitleId", titleId);
        itemTitleIdWithOutPO.put("purchaseOrder.vendorHeaderGeneratedIdentifier", vendorIds.length > 0 ? vendorIds[0] : "");
        itemTitleIdWithOutPO.put("purchaseOrder.vendorDetailAssignedIdentifier", vendorIds.length > 1 ? vendorIds[1] : "");
        List<OlePurchaseOrderItem> dummyTitlePurchaseOrderItemsWithPO = null;
        if (titleId != null) {
            dummyTitlePurchaseOrderItemsWithPO = (List<OlePurchaseOrderItem>) getBusinessObjectService().findMatching(OlePurchaseOrderItem.class, itemTitleIdWithOutPO);
            olePurchaseOrderItems = sortAndProcessPurchaseOrderItems(olePurchaseOrderItems, itemTitleMap, dummyTitlePurchaseOrderItemsWithPO);
        }
        return olePurchaseOrderItems;
    }

    private List<OlePurchaseOrderItem> ruleTwoScenarioWithPONumber(OleInvoiceRecord invoiceRecord, String[] vendorIds, List<OlePurchaseOrderItem> olePurchaseOrderItems, String titleId) {
        Map itemTitleIdWithPO = new HashMap();
        itemTitleIdWithPO.put("itemTitleId", titleId);
        itemTitleIdWithPO.put("purchaseOrder.vendorHeaderGeneratedIdentifier", vendorIds.length > 0 ? vendorIds[0] : "");
        itemTitleIdWithPO.put("purchaseOrder.vendorDetailAssignedIdentifier", vendorIds.length > 1 ? vendorIds[1] : "");
        if (invoiceRecord.getPurchaseOrderNumber()!=null) {
            itemTitleIdWithPO.put("purchaseOrder.purapDocumentIdentifier", invoiceRecord.getPurchaseOrderNumber());
        }
        List<OlePurchaseOrderItem> dummyTitlePurchaseOrderItemsWithPO = null;
        if (titleId != null) {
            dummyTitlePurchaseOrderItemsWithPO = (List<OlePurchaseOrderItem>) getBusinessObjectService().findMatching(OlePurchaseOrderItem.class, itemTitleIdWithPO);
        }
        return dummyTitlePurchaseOrderItemsWithPO;
    }

    private List<OlePurchaseOrderItem> ruleOneScenarioWithOutPurchaseOrderNumber(OleInvoiceRecord invoiceRecord, String[] vendorIds, List<OlePurchaseOrderItem> olePurchaseOrderItems, HashMap itemMap) {
        Map vendorItemIdentifier = new HashMap();
        vendorItemIdentifier.put("vendorItemPoNumber", invoiceRecord.getVendorItemIdentifier());
        vendorItemIdentifier.put("purchaseOrder.vendorHeaderGeneratedIdentifier", vendorIds.length > 0 ? vendorIds[0] : "");
        vendorItemIdentifier.put("purchaseOrder.vendorDetailAssignedIdentifier", vendorIds.length > 1 ? vendorIds[1] : "");
        List<OlePurchaseOrderItem> dummyPurchaseOrderItems = null;
        if (invoiceRecord.getVendorItemIdentifier() != null) {
            dummyPurchaseOrderItems = (List<OlePurchaseOrderItem>) getBusinessObjectService().findMatching(OlePurchaseOrderItem.class, vendorItemIdentifier);
            olePurchaseOrderItems = sortAndProcessPurchaseOrderItems(olePurchaseOrderItems, itemMap, dummyPurchaseOrderItems);
        }
        return olePurchaseOrderItems;
    }

    private List<OlePurchaseOrderItem> ruleOneScenarioWithPurchaseOrderNumber(OleInvoiceRecord invoiceRecord, String[] vendorIds) {
        Map vendorItemIdentifierWithPO = new HashMap();
        vendorItemIdentifierWithPO.put("vendorItemPoNumber", invoiceRecord.getVendorItemIdentifier());
        vendorItemIdentifierWithPO.put("purchaseOrder.vendorHeaderGeneratedIdentifier", vendorIds.length > 0 ? vendorIds[0] : "");
        vendorItemIdentifierWithPO.put("purchaseOrder.vendorDetailAssignedIdentifier", vendorIds.length > 1 ? vendorIds[1] : "");
        if(invoiceRecord.getPurchaseOrderNumber()!=null) {
            vendorItemIdentifierWithPO.put("purchaseOrder.purapDocumentIdentifier", invoiceRecord.getPurchaseOrderNumber());
        }
        List<OlePurchaseOrderItem> dummyPurchaseOrderItemsWithPO = null;
        if (invoiceRecord.getVendorItemIdentifier() != null) {
            dummyPurchaseOrderItemsWithPO = (List<OlePurchaseOrderItem>) getBusinessObjectService().findMatching(OlePurchaseOrderItem.class, vendorItemIdentifierWithPO);
        }
        return dummyPurchaseOrderItemsWithPO;
    }

    private List<OlePurchaseOrderItem> sortAndProcessPurchaseOrderItems(List<OlePurchaseOrderItem> olePurchaseOrderItems, HashMap itemMap, List<OlePurchaseOrderItem> dummyPurchaseOrderItems) {
        Collections.sort(dummyPurchaseOrderItems, new Comparator<OlePurchaseOrderItem>() {
            public int compare(OlePurchaseOrderItem dummyPurchaseOrderItems1, OlePurchaseOrderItem dummyPurchaseOrderItems2) {
                return dummyPurchaseOrderItems2.getDocumentNumber().compareTo(dummyPurchaseOrderItems1.getDocumentNumber());
            }
        });
        olePurchaseOrderItems = validateAndProcessPurchaseOrderItems(olePurchaseOrderItems, dummyPurchaseOrderItems, itemMap);
        return olePurchaseOrderItems;
    }

    private List<OlePurchaseOrderItem> validateAndProcessPurchaseOrderItems(List<OlePurchaseOrderItem> olePurchaseOrderItems, List<OlePurchaseOrderItem> dummyPurchaseOrderItems, HashMap itemMap) {
        if (dummyPurchaseOrderItems != null && dummyPurchaseOrderItems.size() > 0) {
            olePurchaseOrderItems = new ArrayList<>();
            Integer olePurchaseOrderItemsCount=0;
            Integer unlinkPurchaseOrderItemsCount=0;
            for(int itemCount = 0;itemCount < dummyPurchaseOrderItems.size();itemCount++){
                Map purchaseOrderDocNumberMap = new HashMap();
                purchaseOrderDocNumberMap.put("documentNumber", dummyPurchaseOrderItems.get(itemCount).getDocumentNumber());
                List<OlePurchaseOrderDocument> olePurchaseOrderDocumentList = (List<OlePurchaseOrderDocument>) getBusinessObjectService().findMatching(OlePurchaseOrderDocument.class, purchaseOrderDocNumberMap);
                String poAppDocStatus = olePurchaseOrderDocumentList.get(0).getApplicationDocumentStatus();
                if(PurapConstants.PurchaseOrderStatuses.APPDOC_OPEN.equals(poAppDocStatus)) {
                    olePurchaseOrderItems.add(dummyPurchaseOrderItems.get(itemCount));
                    if(("ITEM").equalsIgnoreCase(dummyPurchaseOrderItems.get(itemCount).getItemTypeCode())){
                        itemMap.put("purchaseOrderNumber",olePurchaseOrderDocumentList.get(0).getPurapDocumentIdentifier());
                        itemMap.put("applicationStatus",poAppDocStatus);
                        olePurchaseOrderItemsCount++;
                    }
                }else{
                    if(("ITEM").equalsIgnoreCase(dummyPurchaseOrderItems.get(itemCount).getItemTypeCode())) {
                        itemMap.put("purchaseOrderNumber",olePurchaseOrderDocumentList.get(0).getPurapDocumentIdentifier());
                        itemMap.put("applicationStatus",poAppDocStatus);
                        unlinkPurchaseOrderItemsCount++;
                    }
                }
            }
            itemMap.put("noOfItems",olePurchaseOrderItemsCount);
            itemMap.put("noOfUnlinkItems",unlinkPurchaseOrderItemsCount);
        } return olePurchaseOrderItems;
    }

    private void setDocumentForeignDetails(OleInvoiceDocument invoiceDocument,OleInvoiceRecord invoiceRecord){
        OleCurrencyType oleCurrencyType = getBusinessObjectService().findBySinglePrimaryKey(OleCurrencyType.class,invoiceRecord.getCurrencyTypeId());
        invoiceDocument.setInvoiceCurrencyType(oleCurrencyType.getCurrencyTypeId().toString());
        if(!oleCurrencyType.getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
            Map documentNumberMap = new HashMap();
            documentNumberMap.put(OleSelectConstant.CURRENCY_TYPE_ID, invoiceRecord.getCurrencyTypeId());
            List<OleExchangeRate> exchangeRateList = (List) getBusinessObjectService().findMatchingOrderBy(
                    OleExchangeRate.class, documentNumberMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
            Iterator iterator = exchangeRateList.iterator();
            if (iterator.hasNext()) {
                invoiceDocument.setForeignVendorInvoiceAmount(new BigDecimal(0.00));
                invoiceDocument.setInvoiceCurrencyTypeId(new Long(invoiceRecord.getCurrencyTypeId()));
                invoiceDocument.setInvoiceCurrencyExchangeRate(invoiceRecord.getInvoiceCurrencyExchangeRate());
            }
        }
    }

    public boolean validateVendorNumber(String vendorNumber){
        LOG.info("----Inside validateVendorNumber()------------------------------");
        boolean isValidVendor = false;
        if(vendorNumber != null){
            String[] vendorDetail = vendorNumber.split("-");
            if(vendorDetail.length == 2){
                String vendorHeaderGeneratedIdentifier = vendorDetail[0];
                String vendorDetailAssignedIdentifier = vendorDetail[1];
                try {
                    Map<String,Integer> vendorMap = new HashMap<>();
                    vendorMap.put(OLEConstants.VENDOR_HEADER_GENERATED_ID, Integer.parseInt(vendorHeaderGeneratedIdentifier));
                    vendorMap.put(OLEConstants.VENDOR_DETAILED_ASSIGNED_ID, Integer.parseInt(vendorDetailAssignedIdentifier));
                    List<VendorDetail> vendorDetailList = (List) getBusinessObjectService().findMatching(VendorDetail.class, vendorMap);
                    if(vendorDetail != null && vendorDetailList.size() > 0){
                        isValidVendor  = true;
                        return isValidVendor;
                    }
                }
                catch (NumberFormatException nfe) {
                    return isValidVendor;
                }
            }
        }
        return isValidVendor;
    }

    public boolean validateForNumber(String fieldValue){
        try {
            Integer quantity = Integer.parseInt(fieldValue);
            if(quantity <= 0){
                return false;
            }
        }
        catch(NumberFormatException nfe){
            return false;
        }
        return true;
    }

    public boolean validateDestinationFieldValues(String destinationFieldValue){
        try {
            Float fieldValue = Float.parseFloat(destinationFieldValue);
            if(fieldValue <= 0){
                return false;
            }
        }
        catch(NumberFormatException nfe){
            return false;
        }
        return true;
    }

    @Override
    public void getNextBatch() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void processBatch() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
