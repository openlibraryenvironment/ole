package org.kuali.ole.oleng.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.Exchange;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.coa.businessobject.OleFundCode;
import org.kuali.ole.coa.businessobject.OleFundCodeAccountingLine;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.businessobject.InvoiceAccount;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderAccount;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.module.purap.document.validation.event.AttributedCalculateAccountsPayableEvent;
import org.kuali.ole.module.purap.service.PurapAccountingService;
import org.kuali.ole.oleng.service.OleNGInvoiceService;
import org.kuali.ole.oleng.service.OleNGMemorizeService;
import org.kuali.ole.pojo.OleInvoiceRecord;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.bo.OleVendorAccountInfo;
import org.kuali.ole.select.businessobject.OleInvoiceItem;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.document.service.OleInvoiceService;
import org.kuali.ole.spring.batch.BatchUtil;
import org.kuali.ole.sys.businessobject.Bank;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.event.AttributedSaveDocumentEvent;
import org.kuali.ole.sys.service.BankService;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.ole.vnd.businessobject.OleCurrencyType;
import org.kuali.ole.vnd.businessobject.OleExchangeRate;
import org.kuali.ole.vnd.businessobject.VendorAddress;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KualiRuleService;
import org.kuali.rice.krad.service.SequenceAccessorService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by SheikS on 12/17/2015.
 */
@Service("oleNGInvoiceService")
public class OleNGInvoiceServiceImpl implements OleNGInvoiceService {

    private static final Logger LOG = LoggerFactory.getLogger(OleNGInvoiceServiceImpl.class);
    private BusinessObjectService businessObjectService;
    private OlePurapService olePurapService;
    private OleInvoiceService oleInvoiceService;
    private BatchUtil batchUtil;
    @Autowired
    private OleNGMemorizeService oleNGMemorizeService;

    @Override
    public OleInvoiceDocument createNewInvoiceDocument() throws Exception {
        OleInvoiceDocument invoiceDocument = null;
        try {
            invoiceDocument = (OleInvoiceDocument) SpringContext.getBean(DocumentService.class).getNewDocument("OLE_PRQS");
        } catch (WorkflowException e) {
            LOG.error(e.getMessage());
        }
        return invoiceDocument;
    }

    @Override
    public OleInvoiceDocument populateInvoiceDocWithOrderInformation(OleInvoiceDocument oleInvoiceDocument, List<OleInvoiceRecord> oleInvoiceRecords, Exchange exchange) throws Exception {
        SimpleDateFormat invoiceDateFormat = new SimpleDateFormat("yyyyMMdd");
        UserSession userSession = GlobalVariables.getUserSession();
        Person person = userSession.getPerson();

        initiateInvoiceDocument(oleInvoiceDocument,person);

        OleInvoiceRecord oleInvoiceRecord = oleInvoiceRecords.get(0);

        oleInvoiceDocument.setInvoiceNumber(oleInvoiceRecord.getInvoiceNumber());

        String vendorNumber = oleInvoiceRecord.getVendorNumber();
        populateVendorDetail(vendorNumber, oleInvoiceDocument);
        oleInvoiceDocument.setVendorCustomerNumber(oleInvoiceRecord.getBillToCustomerID());

        if(!StringUtils.isBlank(oleInvoiceRecord.getCurrencyTypeId())){
            setDocumentForeignDetails(oleInvoiceDocument,oleInvoiceRecord);
        }
        else {
            VendorDetail vendorDetail = oleInvoiceDocument.getVendorDetail();
            if(vendorDetail != null){
                oleInvoiceRecord.setCurrencyTypeId(vendorDetail.getCurrencyTypeId().toString());
                setDocumentForeignDetails(oleInvoiceDocument,oleInvoiceRecord);
            }
        }

        oleInvoiceDocument.setInvoiceCurrencyTypeId(new Long(oleInvoiceRecord.getCurrencyTypeId()));
        oleInvoiceDocument.setVendorInvoiceAmount(null);
        Date invoiceDate = null;
        try {
            invoiceDate =  invoiceDateFormat.parse(oleInvoiceRecord.getInvoiceDate());
        } catch (Exception e) {
            LOG.error(e.getMessage());

        }

        if(null == invoiceDate) {
            getBatchUtil().addInvoiceFaiureResponseToExchange(new RuntimeException("Invoice date is Null."), null, exchange);
            return null;
        }

        oleInvoiceDocument.setInvoiceDate(new java.sql.Date(invoiceDate.getTime()));

        Map<Integer, List<OleInvoiceItem>> invoiceItemMap = new TreeMap<>();

        for (Iterator<OleInvoiceRecord> iterator = oleInvoiceRecords.iterator(); iterator.hasNext(); ) {
            OleInvoiceRecord invoiceRecord = iterator.next();
            Integer recordIndex = invoiceRecord.getRecordIndex();
            try {
                List<OleInvoiceItem> oleInvoiceItems = new ArrayList<>();

                List<OlePurchaseOrderItem> olePurchaseOrderItems = invoiceRecord.getOlePurchaseOrderItems();

                oleInvoiceItems = createInvoiceItems(olePurchaseOrderItems, invoiceRecord);

                if (CollectionUtils.isNotEmpty(oleInvoiceItems)) {
                    for (OleInvoiceItem item : oleInvoiceItems) {
                        if (invoiceRecord.getAdditionalChargeCode() != null && invoiceRecord.getAdditionalChargeCode().equalsIgnoreCase("SVC") && item.getItemTypeCode().equalsIgnoreCase("SPHD")) {
                            item.setItemUnitPrice(new BigDecimal(invoiceRecord.getAdditionalCharge() != null ? invoiceRecord.getAdditionalCharge() : invoiceRecord.getAdditionalCharge()));
                        } else if (invoiceRecord.getLineItemAdditionalChargeCode() != null && invoiceRecord.getLineItemAdditionalChargeCode().equalsIgnoreCase("LD") && item.getItemTypeCode().equalsIgnoreCase("ITEM")) {

                            if ((item.getItemDescription().contains(invoiceRecord.getISBN())
                                    || (invoiceRecord.getVendorItemIdentifier() != null ? invoiceRecord.getVendorItemIdentifier().equalsIgnoreCase(olePurchaseOrderItems != null && olePurchaseOrderItems.size() > 0 ? item.getVendorItemIdentifier() : "") : false)
                                    || (invoiceRecord.getPurchaseOrderNumber() != null ? invoiceRecord.getPurchaseOrderNumber().equals(item.getPurchaseOrderDocument() != null ? item.getPurchaseOrderDocument().getPurapDocumentIdentifier() : null) : false))) {
                                item.setItemDiscountType("%"); // % or #
                                item.setItemDiscount(new KualiDecimal(invoiceRecord.getLineItemAdditionalCharge() != null ? invoiceRecord.getLineItemAdditionalCharge() : invoiceRecord.getLineItemAdditionalCharge()));
                            }
                        }
                    }
                    invoiceItemMap.put(recordIndex, oleInvoiceItems);
                }
            } catch (Exception e) {
                e.printStackTrace();
                getBatchUtil().addInvoiceFaiureResponseToExchange(e, recordIndex, exchange);
            }
        }

        addInvoiceItemsToInvoiceDocument(oleInvoiceDocument, invoiceItemMap);

        if (oleInvoiceDocument.getPaymentMethodId() == null) {
            oleInvoiceDocument.setPaymentMethodId(Integer.parseInt(OLEConstants.OleInvoiceImport.PAY_METHOD));
        }
        oleInvoiceDocument.setPaymentMethodIdentifier(String.valueOf(oleInvoiceDocument.getPaymentMethodId()));
        SpringContext.getBean(PurapAccountingService.class).updateAccountAmounts(oleInvoiceDocument);
        Long nextLinkIdentifier = SpringContext.getBean(SequenceAccessorService.class).getNextAvailableSequenceNumber("AP_PUR_DOC_LNK_ID");
        oleInvoiceDocument.setAccountsPayablePurchasingDocumentLinkIdentifier(nextLinkIdentifier.intValue());

        oleInvoiceDocument.updateExtendedPriceOnItems();

        // calculation just for the tax area, only at tax review stage
        // by now, the general calculation shall have been done.
        if (StringUtils.equals(oleInvoiceDocument.getApplicationDocumentStatus(), PurapConstants.PaymentRequestStatuses.APPDOC_AWAITING_TAX_REVIEW)) {
            SpringContext.getBean(OleInvoiceService.class).calculateTaxArea(oleInvoiceDocument);
        }

        updatePrice(oleInvoiceDocument);

        // notice we're ignoring whether the boolean, because these are just warnings they shouldn't halt anything
        //Calculate Payment request before rules since the rule check totalAmount.
        SpringContext.getBean(OleInvoiceService.class).calculateInvoice(oleInvoiceDocument, true);
        SpringContext.getBean(KualiRuleService.class).applyRules(new AttributedCalculateAccountsPayableEvent(oleInvoiceDocument));

        if (!SpringContext.getBean(KualiRuleService.class).applyRules(new AttributedSaveDocumentEvent(oleInvoiceDocument)) ||
                !SpringContext.getBean(KualiRuleService.class).applyRules(new AttributedCalculateAccountsPayableEvent(oleInvoiceDocument))) {
            // TOdo : failure report need to generate.
        }

        //TODO : Need to process for the Success and Failure Report

        //todo : nextprocess.
        return oleInvoiceDocument;
    }

    private void addInvoiceItemsToInvoiceDocument(OleInvoiceDocument oleInvoiceDocument, Map<Integer, List<OleInvoiceItem>> invoiceItemMap) {
        List<InvoiceItemDetail> invoiceItemDetailsUnlinked = new ArrayList<>();
        Set<InvoiceItemDetail> invoiceItemDetailsLinked = new HashSet<>();
        int lineOrderNo = 1;
        for (Iterator<Integer> iterator = invoiceItemMap.keySet().iterator(); iterator.hasNext(); ) {
            Integer lineNumber = iterator.next();
            List<OleInvoiceItem> oleInvoiceItems = invoiceItemMap.get(lineNumber);
            if (CollectionUtils.isNotEmpty(oleInvoiceItems)) {
                for (Iterator<OleInvoiceItem> oleInvoiceItemIterator = oleInvoiceItems.iterator(); oleInvoiceItemIterator.hasNext(); ) {
                    OleInvoiceItem oleInvoiceItem = oleInvoiceItemIterator.next();
                    InvoiceItemDetail invoiceItemDetail = new InvoiceItemDetail();
                    invoiceItemDetail.setSubscriptionPeriod(oleInvoiceItem.getSubscriptionPeriod());
                    invoiceItemDetail.setPoNumber(oleInvoiceItem.getPurchaseOrderIdentifier());
                    invoiceItemDetail.setOleInvoiceItem(oleInvoiceItem);
                    invoiceItemDetail.setLineNo(lineOrderNo);
                    lineOrderNo++;
                    if(null == invoiceItemDetail.getPoNumber() || invoiceItemDetail.getPoNumber() == 0) {
                        invoiceItemDetailsUnlinked.add(invoiceItemDetail);
                    }else {
                        if(!invoiceItemDetailsLinked.contains(invoiceItemDetail)) {
                            invoiceItemDetailsLinked.add(invoiceItemDetail);
                        }
                    }

                }
            }
        }

        List<InvoiceItemDetail> finalInvoiceItemsDetails = new ArrayList<>();
        finalInvoiceItemsDetails.addAll(invoiceItemDetailsLinked);
        finalInvoiceItemsDetails.addAll(invoiceItemDetailsUnlinked);
        sortByLineOrderNumber(finalInvoiceItemsDetails);

        lineOrderNo = 1;
        for (Iterator<InvoiceItemDetail> iterator = finalInvoiceItemsDetails.iterator(); iterator.hasNext(); ) {
            InvoiceItemDetail invoiceItemDetail = iterator.next();
            OleInvoiceItem oleInvoiceItem = invoiceItemDetail.getOleInvoiceItem();
            oleInvoiceItem.setSequenceNumber(lineOrderNo);
            oleInvoiceDocument.getItems().add(oleInvoiceItem);
            lineOrderNo++;
        }
    }

    private void sortByLineOrderNumber(List<InvoiceItemDetail> finalInvoiceItemsDetails) {
        Collections.sort(finalInvoiceItemsDetails, new Comparator<InvoiceItemDetail>() {
            public int compare(InvoiceItemDetail invoiceItemDetail1, InvoiceItemDetail invoiceItemDetail2) {
                int lineNo1 = invoiceItemDetail1.getLineNo();
                int lineNo2 = invoiceItemDetail2.getLineNo();
                return new Integer(lineNo1).compareTo(new Integer(lineNo2));
            }
        });
    }

    @Override
    public OleInvoiceDocument saveInvoiceDocument(OleInvoiceDocument oleInvoiceDocument) {
        getInvoiceService().autoApprovePaymentRequest(oleInvoiceDocument);
        return oleInvoiceDocument;
    }

    private OleInvoiceDocument initiateInvoiceDocument(OleInvoiceDocument invoiceDocument, Person currentUser) throws Exception {

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



    private void setDocumentForeignDetails(OleInvoiceDocument invoiceDocument, OleInvoiceRecord invoiceRecord){
        OleCurrencyType oleCurrencyType = getOleNGMemorizeService().getCurrencyType(invoiceRecord.getCurrencyTypeId());
        if (null != oleCurrencyType) {
            invoiceDocument.setInvoiceCurrencyType(oleCurrencyType.getCurrencyTypeId().toString());
            if(!oleCurrencyType.getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
                List<OleExchangeRate> exchangeRateList = getOleNGMemorizeService().getExchangeRate(invoiceRecord.getCurrencyTypeId());
                if (CollectionUtils.isNotEmpty(exchangeRateList)) {
                    Iterator iterator = exchangeRateList.iterator();
                    if (iterator.hasNext()) {
                        invoiceDocument.setForeignVendorInvoiceAmount(new BigDecimal(0.00));
                        invoiceDocument.setInvoiceCurrencyTypeId(new Long(invoiceRecord.getCurrencyTypeId()));
                        invoiceDocument.setInvoiceCurrencyExchangeRate(invoiceRecord.getInvoiceCurrencyExchangeRate());
                    }
                }
            }
        }
    }

    private List<OlePurchaseOrderItem> validateAndProcessPurchaseOrderItems(List<OlePurchaseOrderItem> dummyPurchaseOrderItems, HashMap itemMap) {
        List<OlePurchaseOrderItem> olePurchaseOrderItems = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dummyPurchaseOrderItems)) {
            Integer olePurchaseOrderItemsCount=0;
            Integer unlinkPurchaseOrderItemsCount=0;
            for(int itemCount = 0;itemCount < dummyPurchaseOrderItems.size();itemCount++){
                Map purchaseOrderDocNumberMap = new HashMap();
                purchaseOrderDocNumberMap.put(OleNGConstants.DOCUMENT_NUMBER, dummyPurchaseOrderItems.get(itemCount).getDocumentNumber());
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

    private List<OleInvoiceItem> createInvoiceItems(List<OlePurchaseOrderItem> olePurchaseOrderItems, OleInvoiceRecord invoiceRecord) throws Exception {
        List<OleInvoiceItem> oleInvoiceItems = new ArrayList<>();
        UniversityDateService universityDateService = SpringContext.getBean(UniversityDateService.class);
        Integer postingYear = universityDateService.getCurrentUniversityDate().getUniversityFiscalYear();
        if(CollectionUtils.isEmpty(olePurchaseOrderItems) || !invoiceRecord.isLink()) {
            oleInvoiceItems.add(invoiceItemForUnlink(invoiceRecord,postingYear));
        } else {
            for (OlePurchaseOrderItem poItem : olePurchaseOrderItems) {
                if (poItem.getItemTypeCode().equalsIgnoreCase("ITEM")) {
                    OleInvoiceItem oleInvoiceItem = createNewInvoiceItem(invoiceRecord);

                    PurchaseOrderDocument purchaseOrderDocument = olePurchaseOrderItems.get(0).getPurchaseOrder();
                    oleInvoiceItem.setItemTypeCode(poItem.getItemTypeCode());
                    oleInvoiceItem.setItemType(poItem.getItemType());

                    if (invoiceRecord.getLineItemAdditionalCharge() != null) {
                        oleInvoiceItem.setVendorItemIdentifier(poItem.getVendorItemPoNumber());
                    }
                    if(invoiceRecord.isLink()) {
                        oleInvoiceItem.setPurchaseOrderIdentifier(purchaseOrderDocument.getPurapDocumentIdentifier());
                        oleInvoiceItem.setAccountsPayablePurchasingDocumentLinkIdentifier(purchaseOrderDocument.getAccountsPayablePurchasingDocumentLinkIdentifier());
                    }

                    oleInvoiceItem.setItemDescription(poItem.getItemDescription());      // invoiceRecord.getItemDescription()
                    oleInvoiceItem.setItemTitleId(poItem.getItemTitleId());
                    oleInvoiceItem.setItemLineNumber(poItem.getItemLineNumber());
                    oleInvoiceItem.setItemNoOfParts(poItem.getItemNoOfParts());
                    oleInvoiceItem.setPoItemIdentifier(poItem.getItemIdentifier());
                    oleInvoiceItem.setOlePoOutstandingQuantity(new KualiInteger(poItem.getOutstandingQuantity().bigDecimalValue()));
                    List accountingLine = new ArrayList();

                    OleVendorAccountInfo oleVendorAccountInfo = populateBFN(invoiceRecord.getBfnNumber());
                    if (oleVendorAccountInfo != null && oleVendorAccountInfo.isActive()) {
                        InvoiceAccount invoiceAccount = createInvoiceAccountFromVendorAccountInfo(invoiceRecord, oleInvoiceItem, oleVendorAccountInfo);
                        accountingLine.add(invoiceAccount);

                    } else {
                        if (invoiceRecord.isLink()) {
                            if (StringUtils.isNotBlank(invoiceRecord.getFundCode())) {
                                accountingLine = getAccountingLinesFromFundCode(invoiceRecord, oleInvoiceItem);
                            } else {
                                for (PurApAccountingLine poa : poItem.getSourceAccountingLines()) {
                                    InvoiceAccount invoiceAccount = new InvoiceAccount(oleInvoiceItem, (PurchaseOrderAccount) poa);
                                    invoiceAccount.setAccountNumber(poa.getAccountNumber());
                                    invoiceAccount.setFinancialObjectCode(poa.getFinancialObjectCode());
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
                    oleInvoiceItem.setPostingYear(postingYear);
                    oleInvoiceItem.setSubscriptionPeriod(invoiceRecord.getSubscriptionPeriod());
                    oleInvoiceItems.add(oleInvoiceItem);
                }
            }
        }
        return oleInvoiceItems;
    }

    public OleInvoiceItem invoiceItemForUnlink(OleInvoiceRecord invoiceRecord, Integer postingYear) {
        OleInvoiceItem oleInvoiceItem = createNewInvoiceItem(invoiceRecord);
        oleInvoiceItem.setItemTypeCode("ITEM");
        //oleInvoiceItem.setItemType(poItem.getItemType()); // Todo: Need to set
        oleInvoiceItem.setItemQuantity(new KualiDecimal(invoiceRecord.getQuantity()));
        oleInvoiceItem.setItemListPrice(new KualiDecimal(invoiceRecord.getListPrice()));
        oleInvoiceItem.setItemDescription(invoiceRecord.getItemDescription());      // invoiceRecord.getItemDescription()
        oleInvoiceItem.setItemDiscount(invoiceRecord.getLineItemAdditionalCharge() != null ? new KualiDecimal(invoiceRecord.getLineItemAdditionalCharge()) : null);
        oleInvoiceItem.setItemDiscountType(invoiceRecord.getDiscountType());
        oleInvoiceItem.setItemUnitPrice(new BigDecimal(invoiceRecord.getUnitPrice()));
        oleInvoiceItem.setPostingYear(postingYear);
        oleInvoiceItem.setSubscriptionPeriod(invoiceRecord.getSubscriptionPeriod());
        // oleInvoiceItem.setItemTitleId(poItem.getItemTitleId()); // Todo: Need to set
        if (CollectionUtils.isNotEmpty(invoiceRecord.getItemNote())) {
            oleInvoiceItem.setNotes(invoiceRecord.getItemNote());
        }
        oleInvoiceItem.setVendorItemIdentifier(invoiceRecord.getVendorItemIdentifier());
        if (StringUtils.isNotBlank(invoiceRecord.getLineItemTaxAmount())) {
            oleInvoiceItem.setUseTaxIndicator(true);
            oleInvoiceItem.setItemSalesTaxAmount(new KualiDecimal(invoiceRecord.getLineItemTaxAmount()));
            oleInvoiceItem.addToExtendedPrice(oleInvoiceItem.getItemSalesTaxAmount());
        }
        //call populate VendorInfo
        OleVendorAccountInfo oleVendorAccountInfo = populateBFN(invoiceRecord.getBfnNumber());
        List accountingLine = new ArrayList();
        if (oleVendorAccountInfo != null && oleVendorAccountInfo.isActive()) {
            InvoiceAccount invoiceAccount = createInvoiceAccountFromVendorAccountInfo(invoiceRecord, oleInvoiceItem, oleVendorAccountInfo);
            accountingLine.add(invoiceAccount);

        }
        oleInvoiceItem.setSourceAccountingLines(accountingLine);
        try {
            oleInvoiceItem.setSubscriptionFromDate(invoiceRecord.getSubscriptionPeriodFrom() != null ? new java.sql.Date(OleNGConstants.DATE_FORMAT_WITHOUT_TIME.parse(invoiceRecord.getSubscriptionPeriodFrom()).getTime()) : null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            oleInvoiceItem.setSubscriptionToDate(invoiceRecord.getSubscriptionPeriodTo() != null ? new java.sql.Date(OleNGConstants.DATE_FORMAT_WITHOUT_TIME.parse(invoiceRecord.getSubscriptionPeriodTo()).getTime()) : null);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return oleInvoiceItem;
    }

    private InvoiceAccount createInvoiceAccountFromVendorAccountInfo(OleInvoiceRecord invoiceRecord, OleInvoiceItem oleInvoiceItem, OleVendorAccountInfo oleVendorAccountInfo) {
        InvoiceAccount invoiceAccount = new InvoiceAccount();
        invoiceAccount.setAccountNumber(oleVendorAccountInfo.getAccountNumber());
        invoiceAccount.setFinancialObjectCode(oleVendorAccountInfo.getObjectCode());
        invoiceAccount.setAmount(new KualiDecimal(invoiceRecord.getUnitPrice()));
        invoiceAccount.setAccountLinePercent(new BigDecimal("100"));  // TODO: Need to get from edifact.
        invoiceAccount.setPurapItem(oleInvoiceItem);
        invoiceAccount.setItemIdentifier(oleInvoiceItem.getItemIdentifier());
        String chartOfAccount = populateChartOfAccount(oleVendorAccountInfo.getAccountNumber());
        invoiceAccount.setChartOfAccountsCode(
                chartOfAccount != null ?
                        chartOfAccount : invoiceRecord.getItemChartCode());     // TODO: Need to get chart of Account based on account number and object code.
        return invoiceAccount;
    }

    private OleInvoiceItem createNewInvoiceItem(OleInvoiceRecord invoiceRecord) {
        OleInvoiceItem oleInvoiceItem = new OleInvoiceItem();
        oleInvoiceItem.setItemQuantity(new KualiDecimal(invoiceRecord.getQuantity()));
        oleInvoiceItem.setItemListPrice(new KualiDecimal(invoiceRecord.getListPrice()));
        oleInvoiceItem.setItemDiscount(invoiceRecord.getLineItemAdditionalCharge() != null ? new KualiDecimal(invoiceRecord.getLineItemAdditionalCharge()) : null);
        oleInvoiceItem.setItemDiscountType(invoiceRecord.getDiscountType());
        oleInvoiceItem.setItemUnitPrice(new BigDecimal(invoiceRecord.getUnitPrice()));
        if(CollectionUtils.isNotEmpty(invoiceRecord.getItemNote())) {
            oleInvoiceItem.setNotes(invoiceRecord.getItemNote());
        }
        if(StringUtils.isNotBlank(invoiceRecord.getLineItemTaxAmount())) {
            oleInvoiceItem.setUseTaxIndicator(true);
            oleInvoiceItem.setItemSalesTaxAmount(new KualiDecimal(invoiceRecord.getLineItemTaxAmount()));
            oleInvoiceItem.addToExtendedPrice(oleInvoiceItem.getItemSalesTaxAmount());
        }
        try {
            oleInvoiceItem.setSubscriptionFromDate(invoiceRecord.getSubscriptionPeriodFrom()!= null ? new java.sql.Date(OleNGConstants.DATE_FORMAT_WITHOUT_TIME.parse(invoiceRecord.getSubscriptionPeriodFrom()).getTime()):null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            oleInvoiceItem.setSubscriptionToDate(invoiceRecord.getSubscriptionPeriodTo()!= null ? new java.sql.Date(OleNGConstants.DATE_FORMAT_WITHOUT_TIME.parse(invoiceRecord.getSubscriptionPeriodTo()).getTime()):null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(!StringUtils.isBlank(invoiceRecord.getCurrencyTypeId())){
            String currencyType = getInvoiceService().getCurrencyType(invoiceRecord.getCurrencyTypeId());
            if (StringUtils.isNotBlank(currencyType)) {
                if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                    setItemForeignDetails(oleInvoiceItem,invoiceRecord.getCurrencyTypeId(),invoiceRecord);
                }
            }
        }
        return oleInvoiceItem;
    }


    private OleVendorAccountInfo populateBFN(String code) {
        List<OleVendorAccountInfo> oleVendorAccountInfo = getOleNGMemorizeService().getVendorAccountInfo(code);
        return CollectionUtils.isNotEmpty(oleVendorAccountInfo) ? oleVendorAccountInfo.get(0) : null;
    }

    private String populateChartOfAccount(String accountNumber) {
        List<Account> accountList = getOleNGMemorizeService().getAccount(accountNumber);
        return CollectionUtils.isNotEmpty(accountList)? accountList.get(0).getChartOfAccountsCode() : null;
    }

    private List getAccountingLinesFromFundCode(OleInvoiceRecord invoiceRecord, OleInvoiceItem oleInvoiceItem) {
        List accountingLine = new ArrayList();
        List<OleFundCode> fundCodeList = getOleNGMemorizeService().getFundCode(invoiceRecord.getFundCode());
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

    private void setItemForeignDetails(OleInvoiceItem oleInvoiceItem, String invoiceCurrencyType, OleInvoiceRecord invoiceRecord){
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
        oleInvoiceItem.setItemExchangeRate(getInvoiceService().getExchangeRate(invoiceCurrencyType).getExchangeRate());
        if(!(oleInvoiceItem.getItemForeignUnitCost().bigDecimalValue().equals(new BigDecimal("0.00")))) {
            if (StringUtils.isNotBlank(invoiceRecord.getInvoiceCurrencyExchangeRate())) {
                oleInvoiceItem.setItemUnitCostUSD(new KualiDecimal(oleInvoiceItem.getItemForeignUnitCost().bigDecimalValue().divide(new BigDecimal(invoiceRecord.getInvoiceCurrencyExchangeRate()), 4, BigDecimal.ROUND_HALF_UP)));
            } else {
                oleInvoiceItem.setItemUnitCostUSD(new KualiDecimal(oleInvoiceItem.getItemForeignUnitCost().bigDecimalValue().divide(oleInvoiceItem.getItemExchangeRate(), 4, BigDecimal.ROUND_HALF_UP)));
            }
            oleInvoiceItem.setItemUnitPrice(oleInvoiceItem.getItemUnitCostUSD().bigDecimalValue());
            oleInvoiceItem.setItemListPrice(oleInvoiceItem.getItemUnitCostUSD());
        }
        else {
            oleInvoiceItem.setItemUnitCostUSD(new KualiDecimal(oleInvoiceItem.getItemListPrice().bigDecimalValue()));
            oleInvoiceItem.setItemUnitPrice(oleInvoiceItem.getItemListPrice().bigDecimalValue());
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

    public OleInvoiceDocument populateVendorDetail(String vendorNumber, OleInvoiceDocument oleInvoiceDocument) {
        String[] vendorIds = vendorNumber != null ? vendorNumber.split("-") : new String[0];
        VendorDetail vendorDetail = getOleNGMemorizeService().getVendorDetail(Integer.valueOf(vendorIds[0]),Integer.valueOf(vendorIds[1]));
        if (vendorDetail != null) {
            oleInvoiceDocument.setVendorDetail(vendorDetail);
            oleInvoiceDocument.setVendorName(vendorDetail.getVendorName());
            oleInvoiceDocument.setVendorHeaderGeneratedIdentifier(vendorDetail.getVendorHeaderGeneratedIdentifier());
            oleInvoiceDocument.setVendorDetailAssignedIdentifier(vendorDetail.getVendorDetailAssignedIdentifier());
            oleInvoiceDocument.setVendorNumber(vendorDetail.getVendorNumber());
            oleInvoiceDocument.setVendorHeaderGeneratedIdentifier(vendorDetail.getVendorHeaderGeneratedIdentifier());
            oleInvoiceDocument.setVendorDetailAssignedIdentifier(vendorDetail.getVendorDetailAssignedIdentifier());
            oleInvoiceDocument.setVendorFaxNumber(vendorDetail.getDefaultFaxNumber());
            //oleInvoiceDocument.
            if (vendorDetail.getPaymentMethodId() != null) {
                oleInvoiceDocument.setPaymentMethodIdentifier(vendorDetail.getPaymentMethodId().toString());
                oleInvoiceDocument.setPaymentMethodId(vendorDetail.getPaymentMethodId());
            }

            if (vendorDetail.getVendorPaymentTerms() != null) {
                oleInvoiceDocument.setVendorPaymentTerms(vendorDetail.getVendorPaymentTerms());
                oleInvoiceDocument.setVendorPaymentTermsCode(vendorDetail.getVendorPaymentTerms().getVendorPaymentTermsCode());

            }
            if (vendorDetail.getVendorShippingTitle() != null) {
                oleInvoiceDocument.setVendorShippingTitleCode(vendorDetail.getVendorShippingTitle().getVendorShippingTitleCode());
            }
            if (vendorDetail.getVendorShippingPaymentTerms() != null) {
                oleInvoiceDocument.setVendorShippingPaymentTerms(vendorDetail.getVendorShippingPaymentTerms());
            }

            for (VendorAddress vendorAddress : vendorDetail.getVendorAddresses()) {
                if (vendorAddress.isVendorDefaultAddressIndicator()) {
                    oleInvoiceDocument.setVendorCityName(vendorAddress.getVendorCityName());
                    oleInvoiceDocument.setVendorLine1Address(vendorAddress.getVendorLine1Address());
                    oleInvoiceDocument.setVendorLine2Address(vendorAddress.getVendorLine2Address());
                    oleInvoiceDocument.setVendorAttentionName(vendorAddress.getVendorAttentionName());
                    oleInvoiceDocument.setVendorPostalCode(vendorAddress.getVendorZipCode());
                    oleInvoiceDocument.setVendorStateCode(vendorAddress.getVendorStateCode());
                    oleInvoiceDocument.setVendorAttentionName(vendorAddress.getVendorAttentionName());
                    oleInvoiceDocument.setVendorAddressInternationalProvinceName(vendorAddress.getVendorAddressInternationalProvinceName());
                    oleInvoiceDocument.setVendorCountryCode(vendorAddress.getVendorCountryCode());
                    oleInvoiceDocument.setVendorCountry(vendorAddress.getVendorCountry());
                    //oleInvoiceDocument.setNoteLine1Text(vendorAddress.getNoteLine2Text
                }
            }
        }

        return oleInvoiceDocument;
    }

    static class InvoiceItemDetail {
        private String subscriptionPeriod;
        private Integer poNumber;
        private int lineNo;
        private OleInvoiceItem oleInvoiceItem;

        public String getSubscriptionPeriod() {
            return subscriptionPeriod;
        }

        public void setSubscriptionPeriod(String subscriptionPeriod) {
            this.subscriptionPeriod = subscriptionPeriod;
        }

        public Integer getPoNumber() {
            return poNumber;
        }

        public void setPoNumber(Integer poNumber) {
            this.poNumber = poNumber;
        }

        public int getLineNo() {
            return lineNo;
        }

        public void setLineNo(int lineNo) {
            this.lineNo = lineNo;
        }

        public OleInvoiceItem getOleInvoiceItem() {
            return oleInvoiceItem;
        }

        public void setOleInvoiceItem(OleInvoiceItem oleInvoiceItem) {
            this.oleInvoiceItem = oleInvoiceItem;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            InvoiceItemDetail that = (InvoiceItemDetail) o;

            if (subscriptionPeriod != null ? !subscriptionPeriod.equals(that.subscriptionPeriod) : that.subscriptionPeriod != null) return false;
            return !(poNumber != null ? !poNumber.equals(that.poNumber) : that.poNumber != null);

        }

        @Override
        public int hashCode() {
            int result = subscriptionPeriod != null ? subscriptionPeriod.hashCode() : 0;
            result = 31 * result + (poNumber != null ? poNumber.hashCode() : 0);
            return result;
        }
    }

    public OlePurapService getOlePurapService() {
        if (olePurapService == null) {
            olePurapService = SpringContext.getBean(OlePurapService.class);
        }
        return olePurapService;
    }

    public BusinessObjectService getBusinessObjectService() {
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

    public BatchUtil getBatchUtil() {
        if(null == batchUtil) {
            batchUtil = new BatchUtil();
        }
        return batchUtil;
    }

    public void setBatchUtil(BatchUtil batchUtil) {
        this.batchUtil = batchUtil;
    }

    public OleNGMemorizeService getOleNGMemorizeService() {
        if(null == oleNGMemorizeService) {
            oleNGMemorizeService = new OleNGMemorizeServiceImpl();
        }
        return oleNGMemorizeService;
    }

    public void setOleNGMemorizeService(OleNGMemorizeService oleNGMemorizeService) {
        this.oleNGMemorizeService = oleNGMemorizeService;
    }
}
