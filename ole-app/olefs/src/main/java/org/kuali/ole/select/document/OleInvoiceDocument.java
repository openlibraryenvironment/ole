/*
 * Copyright 2011 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.document;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.BibMarc;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibInfoRecord;
import org.kuali.ole.gl.service.SufficientFundsService;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapConstants.InvoiceStatuses;
import org.kuali.ole.module.purap.PurapParameterConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.PurapWorkflowConstants;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.module.purap.document.InvoiceDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.PurchasingAccountsPayableDocument;
import org.kuali.ole.module.purap.document.service.AccountsPayableService;
import org.kuali.ole.module.purap.document.service.InvoiceService;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.module.purap.document.validation.event.AttributedContinuePurapEvent;
import org.kuali.ole.module.purap.service.PurapAccountingService;
import org.kuali.ole.module.purap.util.ExpiredOrClosedAccountEntry;
import org.kuali.ole.module.purap.util.PurApItemUtils;
import org.kuali.ole.module.purap.util.SummaryAccount;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.service.OleInvoiceService;
import org.kuali.ole.select.document.service.impl.OleInvoiceFundCheckServiceImpl;
import org.kuali.ole.select.form.OLEInvoiceForm;
import org.kuali.ole.select.service.BibInfoService;
import org.kuali.ole.select.service.BibInfoWrapperService;
import org.kuali.ole.select.service.FileProcessingService;
import org.kuali.ole.select.service.impl.BibInfoServiceImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.Bank;
import org.kuali.ole.sys.businessobject.GeneralLedgerPendingEntry;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.businessobject.SufficientFundsItem;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.BankService;
import org.kuali.ole.sys.service.GeneralLedgerPendingEntryService;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.ole.vnd.VendorConstants;
import org.kuali.ole.vnd.businessobject.OleCurrencyType;
import org.kuali.ole.vnd.businessobject.OleExchangeRate;
import org.kuali.ole.vnd.businessobject.VendorAddress;
import org.kuali.ole.vnd.document.service.VendorService;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.web.format.CurrencyFormatter;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.api.action.ActionTaken;
import org.kuali.rice.kew.api.action.RoutingReportCriteria;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.IdentityManagementService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.document.Copyable;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.workflow.service.WorkflowDocumentService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * This class is the document class for Ole Payment Request
 */

public class OleInvoiceDocument extends InvoiceDocument implements Copyable {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleInvoiceDocument.class);

    private Integer invoiceTypeId;
    private Integer invoiceSubTypeId;
    private String vendorId;
    private String vendorAlias;
    private String invoiceTypeHdnId;
    private String invoiceSubTypeHdnId;
    private String acquisitionUnit;
    private String empty;
    protected transient List<SummaryAccount> summaryAccounts;
    private boolean sfcFlag;
    private String invoiceItemTotal;
    private String invoiceForeignItemTotal;
    private String invoiceAmount;
    private boolean baSfcFlag;
    private boolean validationFlag;
    private boolean blanketApproveValidationFlag;
    private boolean blanketApproveSubscriptionDateValidationFlag;
    private boolean subscriptionDateValidationFlag;
    private String purchaseOrderDocumentNums;
    private boolean duplicateApproveFlag = false;
    private boolean blanketApproveFlag=true;
    private boolean overviewFlag = true;
    private boolean vendorInfoFlag = true;
    private boolean invoiceInfoFlag = true;
    private boolean processItemFlag = false;
    private boolean processTitlesFlag = false;
    private boolean currentItemsFlag = false;


    private boolean additionalChargesFlag = false;
    private boolean accountSummaryFlag = false;
    private boolean notesAndAttachmentFlag = false;
    private boolean adHocRecipientsFlag = false;
    private boolean routeLogFlag = false;
    private boolean routeLogDisplayFlag = false;
    private boolean unsaved = false;
    private boolean canClose = false;

    private String cloneDebitInvoice;
    private boolean cloneFlag = false;
    private boolean amountExceeds = false;
    private String exceedsEncumbranceTitle;
    private String exceedsEncumbranceExtendedCost;
    private String exceedsEncumbranceListPrice;
    private boolean amountExceedsForBlanketApprove;
    private boolean itemSign;

    private String vendorLink;
    private boolean foreignCurrencyFlag=false;
    private Long invoiceCurrencyTypeId;
    private String invoiceCurrencyType;
    private OleCurrencyType oleCurrencyType;
    private boolean currencyOverrideFlag = false;
    private String currencyOverrideMessage;
    private String invoiceCurrencyExchangeRate;
    private boolean dbRetrieval= false;
    private boolean enableCurrentItems = false;
    private String foreignVendorAmount;
    private String foreignInvoiceAmount;
    private List<OleInvoiceItem> deletedInvoiceItems =  new ArrayList<>();
    private boolean duplicateRouteFlag;
    private boolean duplicateSaveFlag;
	private boolean duplicateValidationFlag;
    private String invoiceInfo;
    private String currencyFormat;
    private Integer lineOrderSequenceNumber = 0;

    public boolean isBlanketApproveFlag() {
        return blanketApproveFlag;
    }
    private boolean currencyTypeIndicator = true;
    private boolean addImmediately;

    public void setBlanketApproveFlag(boolean blanketApproveFlag) {
        this.blanketApproveFlag = blanketApproveFlag;
    }

    private List<OlePaymentRequestDocument> paymentRequestDocuments = new ArrayList<OlePaymentRequestDocument>();

    public boolean isCanClose() {
        return canClose;
    }

    public void setCanClose(boolean canClose) {
        this.canClose = canClose;
    }

    public boolean isUnsaved() {
        return unsaved;
    }

    public void setUnsaved(boolean unsaved) {
        this.unsaved = unsaved;
    }

    private boolean duplicateFlag = false;


    private BigDecimal priorTotal = new BigDecimal(0);

    private List<OleVendorCreditMemoDocument> creditMemoDocuments = new ArrayList<OleVendorCreditMemoDocument>();

    public String getAcquisitionUnit() {
        return acquisitionUnit;
    }

    public void setAcquisitionUnit(String acquisitionUnit) {
        this.acquisitionUnit = acquisitionUnit;
    }

    public BigDecimal getPriorTotal() {
        return priorTotal;
    }

    public void setPriorTotal(BigDecimal priorTotal) {
        this.priorTotal = priorTotal;
    }

    public List<OleVendorCreditMemoDocument> getCreditMemoDocuments() {
        return creditMemoDocuments;
    }

    public void setCreditMemoDocuments(List<OleVendorCreditMemoDocument> creditMemoDocuments) {
        this.creditMemoDocuments = creditMemoDocuments;
    }

    public String getEmpty() {
        return empty;
    }

    public void setEmpty(String empty) {
        this.empty = empty;
    }

    // NOT PERSISTED IN DB
    //private String invoiceType;
    // private String invoiceSubType;

    // REFERENCE OBJECTS
    private OleInvoiceSubType oleInvoiceSubType;
    private OleInvoiceType oleInvoiceType;

    // Prorating Additional charges
    private boolean prorateQty;
    private boolean prorateDollar;
    private boolean prorateManual;
    private boolean noProrate;

    private String prorateBy;

    private BigDecimal foreignVendorInvoiceAmount;

    private BigDecimal purchaseOrderTypeId;
    private PurchaseOrderType orderType;
    private static transient BibInfoService bibInfoService;
    private String paymentMethodIdentifier;
    private Integer paymentMethodId;
    private OlePaymentMethod paymentMethod;
    private String vendorFaxNumber;

    protected Integer accountsPayablePurchasingDocumentLinkIdentifier;
    protected List<PurchasingAccountsPayableDocument> purchaseOrders;


    private String invoicedGrandTotal;
    private String invoicedItemTotal;
    //private boolean invoiceModified;
    private String searchResultInvoiceDate;
    private String searchResultInvoicePayDate;
    private boolean offsetAccountIndicator;

    public boolean isNoProrate() {
        return noProrate;
    }

    public void setNoProrate(boolean noProrate) {
        this.noProrate = noProrate;
    }

    @SuppressWarnings("rawtypes")
    public List getPurchaseOrders() {
        return purchaseOrders;
    }

    @SuppressWarnings("rawtypes")
    public void setPurchaseOrders(List purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    public String getVendorFaxNumber() {
        return vendorFaxNumber;
    }

    public void setVendorFaxNumber(String vendorFaxNumber) {
        this.vendorFaxNumber = vendorFaxNumber;
    }

    public String getPaymentMethodIdentifier() {
        return paymentMethodIdentifier;
    }

    public void setPaymentMethodIdentifier(String paymentMethodIdentifier) {
        this.paymentMethodIdentifier = paymentMethodIdentifier;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getInvoiceSubTypeHdnId() {
        return invoiceSubTypeHdnId;
    }

    public void setInvoiceSubTypeHdnId(String invoiceSubTypeHdnId) {
        this.invoiceSubTypeHdnId = invoiceSubTypeHdnId;
    }

    //Overview
    private String documentYear;
    private String documentTotalAmount;

    //Additional Items
    /*List<OLEInvoiceAdditionalItem> additionalItems = new ArrayList<OLEInvoiceAdditionalItem>();*/

    public String getDocumentYear() {
        return documentYear;
    }

    public void setDocumentYear(String documentYear) {
        this.documentYear = documentYear;
    }

    public String getDocumentTotalAmount() {
        if (!isDbRetrieval()) {
            this.documentTotalAmount = this.getTotalDollarAmount().toString();
        }
        return documentTotalAmount;
        //return this.getTotalDollarAmount().toString();
    }

    public void setDocumentTotalAmount(String documentTotalAmount) {
        this.documentTotalAmount = documentTotalAmount;
    }

    /*public List<OLEInvoiceAdditionalItem> getAdditionalItems() {
        return additionalItems;
    }

    public void setAdditionalItems(List<OLEInvoiceAdditionalItem> additionalItems) {
        this.additionalItems = additionalItems;
    }*/

    private List<OlePurchaseOrderItem> purchaseOrderItems = new ArrayList<OlePurchaseOrderItem>();


    public List<OlePurchaseOrderItem> getPurchaseOrderItems() {
        return purchaseOrderItems;
    }

    public void setPurchaseOrderItems(List<OlePurchaseOrderItem> purchaseOrderItems) {
        this.purchaseOrderItems = purchaseOrderItems;
    }

    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public OlePaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(OlePaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public static BibInfoService getBibInfoService() {
        if (bibInfoService == null) {
            bibInfoService = SpringContext.getBean(BibInfoServiceImpl.class);
        }
        return bibInfoService;
    }

    @Override
    public BigDecimal getPurchaseOrderTypeId() {
        return purchaseOrderTypeId;
    }

    @Override
    public void setPurchaseOrderTypeId(BigDecimal purchaseOrderTypeId) {
        this.purchaseOrderTypeId = purchaseOrderTypeId;
    }

    @Override
    public PurchaseOrderType getOrderType() {
        return orderType;
    }

    @Override
    public Integer getAccountsPayablePurchasingDocumentLinkIdentifier() {
        return accountsPayablePurchasingDocumentLinkIdentifier;
    }

    @Override
    public void setAccountsPayablePurchasingDocumentLinkIdentifier(Integer accountsPayablePurchasingDocumentLinkIdentifier) {
        this.accountsPayablePurchasingDocumentLinkIdentifier = accountsPayablePurchasingDocumentLinkIdentifier;
    }

    @Override
    public void setOrderType(PurchaseOrderType orderType) {
        this.orderType = orderType;
    }

    public Integer getInvoiceTypeId() {
        return invoiceTypeId;
    }

    public void setInvoiceTypeId(Integer invoiceTypeId) {
        this.invoiceTypeId = invoiceTypeId;
    }

    public String getInvoiceTypeHdnId() {
        return invoiceTypeHdnId;
    }

    public void setInvoiceTypeHdnId(String invoiceTypeHdnId) {
        this.invoiceTypeHdnId = invoiceTypeHdnId;
    }

    public Integer getInvoiceSubTypeId() {
        return invoiceSubTypeId;
    }

    public void setInvoiceSubTypeId(Integer invoiceSubTypeId) {
        this.invoiceSubTypeId = invoiceSubTypeId;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }


    public String getInvoiceSubType() {
        return invoiceSubType;
    }

    public void setInvoiceSubType(String invoiceSubType) {
        this.invoiceSubType = invoiceSubType;
    }

    public OleInvoiceSubType getOleInvoiceSubType() {
        return oleInvoiceSubType;
    }

    public void setOleInvoiceSubType(OleInvoiceSubType oleInvoiceSubType) {
        this.oleInvoiceSubType = oleInvoiceSubType;
    }

    public OleInvoiceType getOleInvoiceType() {
        return oleInvoiceType;
    }

    public void setOleInvoiceType(OleInvoiceType oleInvoiceType) {
        this.oleInvoiceType = oleInvoiceType;
    }

    public boolean isProrateQty() {
        return prorateQty;
    }

    public void setProrateQty(boolean prorateQty) {
        this.prorateQty = prorateQty;
    }

    public boolean isProrateDollar() {
        return prorateDollar;
    }

    public void setProrateDollar(boolean prorateDollar) {
        this.prorateDollar = prorateDollar;
    }

    public boolean isProrateManual() {
        return prorateManual;
    }

    public void setProrateManual(boolean prorateManual) {
        this.prorateManual = prorateManual;
    }

    public boolean isAmountExceeds() {
        return amountExceeds;
    }

    public void setAmountExceeds(boolean amountExceeds) {
        this.amountExceeds = amountExceeds;
    }

    public String getExceedsEncumbranceTitle() {
        return exceedsEncumbranceTitle;
    }

    public void setExceedsEncumbranceTitle(String exceedsEncumbranceTitle) {
        this.exceedsEncumbranceTitle = exceedsEncumbranceTitle;
    }

    public String getExceedsEncumbranceExtendedCost() {
        return exceedsEncumbranceExtendedCost;
    }

    public void setExceedsEncumbranceExtendedCost(String exceedsEncumbranceExtendedCost) {
        this.exceedsEncumbranceExtendedCost = exceedsEncumbranceExtendedCost;
    }

    public String getExceedsEncumbranceListPrice() {
        return exceedsEncumbranceListPrice;
    }

    public void setExceedsEncumbranceListPrice(String exceedsEncumbranceListPrice) {
        this.exceedsEncumbranceListPrice = exceedsEncumbranceListPrice;
    }

    public boolean isAmountExceedsForBlanketApprove() {
        return amountExceedsForBlanketApprove;
    }

    public void setAmountExceedsForBlanketApprove(boolean amountExceedsForBlanketApprove) {
        this.amountExceedsForBlanketApprove = amountExceedsForBlanketApprove;
    }

    private static transient ConfigurationService kualiConfigurationService;
    private static transient BibInfoWrapperService bibInfoWrapperService;
    private static transient FileProcessingService fileProcessingService;
    private static transient BusinessObjectService businessObjectService;
    private static transient VendorService vendorService;
    private static transient InvoiceService invoiceService;
    private static transient OleInvoiceService oleInvoiceService;
    private static transient PurapService purapService;
    private static transient AccountsPayableService accountsPayableService;
    private static transient IdentityManagementService identityManagementService;
    private static transient WorkflowDocumentService workflowDocumentService;

    //Proforma
    private boolean proformaIndicator;

    public static WorkflowDocumentService getWorkflowDocumentService() {
        if (workflowDocumentService == null) {
            workflowDocumentService = SpringContext.getBean(WorkflowDocumentService.class);
        }
        return workflowDocumentService;
    }

    public static void setWorkflowDocumentService(WorkflowDocumentService workflowDocumentService) {
        OleInvoiceDocument.workflowDocumentService = workflowDocumentService;
    }

    public static InvoiceService getInvoiceService() {
        if (invoiceService == null) {
            invoiceService = SpringContext.getBean(InvoiceService.class);
        }
        return invoiceService;
    }

    public static void setInvoiceService(InvoiceService invoiceService) {
        OleInvoiceDocument.invoiceService = invoiceService;
    }

    public static VendorService getVendorService() {
        if (vendorService == null) {
            vendorService = SpringContext.getBean(VendorService.class);
        }
        return vendorService;
    }

    public static void setVendorService(VendorService vendorService) {
        OleInvoiceDocument.vendorService = vendorService;
    }

    public static PurapService getPurapService() {
        if (purapService == null) {
            purapService = SpringContext.getBean(PurapService.class);
        }
        return purapService;
    }

    public static void setPurapService(PurapService purapService) {
        OleInvoiceDocument.purapService = purapService;
    }

    public static OleInvoiceService getOleInvoiceService() {
        if (oleInvoiceService == null) {
            oleInvoiceService = SpringContext.getBean(OleInvoiceService.class);
        }
        return oleInvoiceService;
    }

    public static void setOleInvoiceService(OleInvoiceService oleInvoiceService) {
        OleInvoiceDocument.oleInvoiceService = oleInvoiceService;
    }

    public static IdentityManagementService getIdentityManagementService() {
        if (identityManagementService == null) {
            identityManagementService = SpringContext.getBean(IdentityManagementService.class);
        }
        return identityManagementService;
    }

    public static void setIdentityManagementService(IdentityManagementService identityManagementService) {
        OleInvoiceDocument.identityManagementService = identityManagementService;
    }

    public static AccountsPayableService getAccountsPayableService() {
        if (accountsPayableService == null) {
            accountsPayableService = SpringContext.getBean(AccountsPayableService.class);
        }
        return accountsPayableService;
    }

    public static void setAccountsPayableService(AccountsPayableService accountsPayableService) {
        OleInvoiceDocument.accountsPayableService = accountsPayableService;
    }

    public static ConfigurationService getConfigurationService() {
        if (kualiConfigurationService == null) {
            kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
        }
        return kualiConfigurationService;
    }

    public static void setConfigurationService(ConfigurationService kualiConfigurationService) {
        OleInvoiceDocument.kualiConfigurationService = kualiConfigurationService;
    }

    public static BibInfoWrapperService getBibInfoWrapperService() {
        if (bibInfoWrapperService == null) {
            bibInfoWrapperService = SpringContext.getBean(BibInfoWrapperService.class);
        }
        return bibInfoWrapperService;
    }

    public static void setBibInfoWrapperService(BibInfoWrapperService bibInfoWrapperService) {
        OleInvoiceDocument.bibInfoWrapperService = bibInfoWrapperService;
    }


    public static FileProcessingService getFileProcessingService() {
        if (fileProcessingService == null) {
            fileProcessingService = SpringContext.getBean(FileProcessingService.class);
        }
        return fileProcessingService;
    }

    public static void setFileProcessingService(FileProcessingService fileProcessingService) {
        OleInvoiceDocument.fileProcessingService = fileProcessingService;
    }

    public static DateTimeService getDateTimeService() {
        if (dateTimeService == null) {
            dateTimeService = SpringContext.getBean(DateTimeService.class);
        }
        return dateTimeService;
    }

    public static void setDateTimeService(DateTimeService dateTimeService) {
        OleInvoiceDocument.dateTimeService = dateTimeService;
    }

    @Override
    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = SpringContext.getBean(BusinessObjectService.class);
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    /**
     * Default constructor.
     */
    public OleInvoiceDocument() {
        super();
        /*this.setProrateBy(OLEConstants.PRORATE_BY_DOLLAR);
        this.setProrateDollar(true);*/
    }

    public void initiateDocument() throws WorkflowException {
        super.initiateDocument();
        OleInvoiceItem item;
        this.setUseTaxIndicator(false);
        String[] collapseSections = getOleInvoiceService().getDefaultCollapseSections();
        this.setOverviewFlag(getOleInvoiceService().canCollapse(OLEConstants.OVERVIEW_SECTION, collapseSections));
        this.setVendorInfoFlag(getOleInvoiceService().canCollapse(OLEConstants.VENDOR_INFO_SECTION, collapseSections));
        this.setInvoiceInfoFlag(getOleInvoiceService().canCollapse(OLEConstants.INVOICE_INFO_SECTION, collapseSections));
        this.setProcessItemFlag(getOleInvoiceService().canCollapse(OLEConstants.PROCESS_ITEMS_SECTION, collapseSections));
        this.setProcessTitlesFlag(getOleInvoiceService().canCollapse(OLEConstants.PROCESS_TITLES_SECTION, collapseSections));
        this.setCurrentItemsFlag(getOleInvoiceService().canCollapse(OLEConstants.CURRENT_ITEM_SECTION, collapseSections));
        this.setAdditionalChargesFlag(getOleInvoiceService().canCollapse(OLEConstants.ADDITIONAL_CHARGES_SECTION, collapseSections));
        this.setAccountSummaryFlag(getOleInvoiceService().canCollapse(OLEConstants.ACCOUNT_SUMMARY_SECTION, collapseSections));
        this.setAdHocRecipientsFlag(getOleInvoiceService().canCollapse(OLEConstants.ADHOC_RECIPIENT_SECTION, collapseSections));
        this.setRouteLogFlag(getOleInvoiceService().canCollapse(OLEConstants.ROUTE_LOG_SECTION, collapseSections));
        this.setNotesAndAttachmentFlag(getOleInvoiceService().canCollapse(OLEConstants.NOTES_AND_ATTACH_SECTION, collapseSections));

        if (this.getItems() == null || this.getItems().size() == 0) {
            this.items = new ArrayList<>();
            SpringContext.getBean(PurapService.class).addBelowLineItems(this);
            List<OleInvoiceItem> oleInvItems = this.getItems();
            for (OleInvoiceItem oleInvoiceItem : oleInvItems) {
                oleInvoiceItem.setPostingYear(SpringContext.getBean(UniversityDateService.class).getCurrentUniversityDate().getUniversityFiscalYear());
            }
        }
    }

    /**
     * This method is overridden to populate Ole InvoiceDocument from PurchaseOrder Document
     *
     * @see org.kuali.ole.module.purap.document.InvoiceDocument#populateInvoiceFromPurchaseOrder(org.kuali.ole.module.purap.document.PurchaseOrderDocument, java.util.HashMap)
     */
    @Override
    public void populateInvoiceFromPurchaseOrder(PurchaseOrderDocument po, HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList) {
        LOG.debug("Inside populateInvoiceFromPurchaseOrder method of OleInvoice Document");
        this.setPaymentMethodId(po.getVendorDetail().getPaymentMethodId());
        this.setPurchaseOrderIdentifier(po.getPurapDocumentIdentifier());
        this.getDocumentHeader().setOrganizationDocumentNumber(po.getDocumentHeader().getOrganizationDocumentNumber());
        this.setPostingYear(po.getPostingYear());
        this.setReceivingDocumentRequiredIndicator(po.isReceivingDocumentRequiredIndicator());
        this.setUseTaxIndicator(po.isUseTaxIndicator());
        this.setInvoicePositiveApprovalIndicator(po.isPaymentRequestPositiveApprovalIndicator());
        this.setVendorCustomerNumber(po.getVendorCustomerNumber());

        if (po.getPurchaseOrderCostSource() != null) {
            this.setInvoiceCostSource(po.getPurchaseOrderCostSource());
            this.setInvoiceCostSourceCode(po.getPurchaseOrderCostSourceCode());
        }

        if (po.getVendorShippingPaymentTerms() != null) {
            this.setVendorShippingPaymentTerms(po.getVendorShippingPaymentTerms());
            this.setVendorShippingPaymentTermsCode(po.getVendorShippingPaymentTermsCode());
        }

        if (po.getVendorPaymentTerms() != null) {
            this.setVendorPaymentTermsCode(po.getVendorPaymentTermsCode());
            this.setVendorPaymentTerms(po.getVendorPaymentTerms());
        }

        if (po.getRecurringPaymentType() != null) {
            this.setRecurringPaymentType(po.getRecurringPaymentType());
            this.setRecurringPaymentTypeCode(po.getRecurringPaymentTypeCode());
        }

        Bank defaultBank = SpringContext.getBean(BankService.class).getDefaultBankByDocType(this.getClass());
        if (defaultBank != null) {
            this.setBankCode(defaultBank.getBankCode());
            this.setBank(defaultBank);
        }
        this.setVendorHeaderGeneratedIdentifier(po.getVendorHeaderGeneratedIdentifier());
        this.setVendorDetailAssignedIdentifier(po.getVendorDetailAssignedIdentifier());
        this.setVendorCustomerNumber(po.getVendorCustomerNumber());
        this.setVendorName(po.getVendorName());

        // set original vendor
        this.setOriginalVendorHeaderGeneratedIdentifier(po.getVendorHeaderGeneratedIdentifier());
        this.setOriginalVendorDetailAssignedIdentifier(po.getVendorDetailAssignedIdentifier());

        // set alternate vendor info as well
        this.setAlternateVendorHeaderGeneratedIdentifier(po.getAlternateVendorHeaderGeneratedIdentifier());
        this.setAlternateVendorDetailAssignedIdentifier(po.getAlternateVendorDetailAssignedIdentifier());

        // populate preq vendor address with the default remit address type for the vendor if found
        String userCampus = GlobalVariables.getUserSession().getPerson().getCampusCode();
        VendorAddress vendorAddress = getVendorService().getVendorDefaultAddress(po.getVendorHeaderGeneratedIdentifier(), po.getVendorDetailAssignedIdentifier(), VendorConstants.AddressTypes.REMIT, userCampus);
        if (vendorAddress != null) {
            this.templateVendorAddress(vendorAddress);
            this.setVendorAddressGeneratedIdentifier(vendorAddress.getVendorAddressGeneratedIdentifier());
            setVendorAttentionName(StringUtils.defaultString(vendorAddress.getVendorAttentionName()));
        } else {
            // set address from PO
            this.setVendorAddressGeneratedIdentifier(po.getVendorAddressGeneratedIdentifier());
            this.setVendorLine1Address(po.getVendorLine1Address());
            this.setVendorLine2Address(po.getVendorLine2Address());
            this.setVendorCityName(po.getVendorCityName());
            this.setVendorAddressInternationalProvinceName(po.getVendorAddressInternationalProvinceName());
            this.setVendorStateCode(po.getVendorStateCode());
            this.setVendorPostalCode(po.getVendorPostalCode());
            this.setVendorCountryCode(po.getVendorCountryCode());

            boolean blankAttentionLine = StringUtils.equalsIgnoreCase(
                    "Y",
                    SpringContext.getBean(ParameterService.class).getParameterValueAsString(
                            PurapConstants.PURAP_NAMESPACE, "Document",
                            PurapParameterConstants.BLANK_ATTENTION_LINE_FOR_PO_TYPE_ADDRESS));

            if (blankAttentionLine) {
                setVendorAttentionName(StringUtils.EMPTY);
            } else {
                setVendorAttentionName(StringUtils.defaultString(po.getVendorAttentionName()));
            }
        }

        this.setInvoicePayDate(getInvoiceService().calculatePayDate(this.getInvoiceDate(), this.getVendorPaymentTerms()));

        if (getInvoiceService().encumberedItemExistsForInvoicing(po)) {
            for (OlePurchaseOrderItem poi : (List<OlePurchaseOrderItem>) po.getItems()) {
                // check to make sure it's eligible for payment (i.e. active and has encumbrance available
                if (getDocumentSpecificService().poItemEligibleForAp(this, poi)) {
                    OleInvoiceItem invoiceItem = new OleInvoiceItem(poi, this, expiredOrClosedAccountList);
                    invoiceItem.setReceivingDocumentRequiredIndicator(po.isReceivingDocumentRequiredIndicator());
                    this.getItems().add(invoiceItem);
                    PurchasingCapitalAssetItem purchasingCAMSItem = po.getPurchasingCapitalAssetItemByItemIdentifier(poi.getItemIdentifier());
                    if (purchasingCAMSItem != null) {
                        invoiceItem.setCapitalAssetTransactionTypeCode(purchasingCAMSItem.getCapitalAssetTransactionTypeCode());
                    }

                    /*
                    // copy usetaxitems over
                    invoiceItem.getUseTaxItems().clear();
                    for (PurApItemUseTax useTax : poi.getUseTaxItems()) {
                        invoiceItem.getUseTaxItems().add(useTax);
                    }
                    */
                }
            }
        }

        // add missing below the line
        getPurapService().addBelowLineItems(this);
        this.setAccountsPayablePurchasingDocumentLinkIdentifier(po.getAccountsPayablePurchasingDocumentLinkIdentifier());

        //fix up below the line items
        getInvoiceService().removeIneligibleAdditionalCharges(this);

        this.fixItemReferences();
        this.refreshNonUpdateableReferences();
    }

    @Override
    public Class getItemClass() {
        return OleInvoiceItem.class;
    }

    @Override
    public PurApAccountingLine getFirstAccount() {
        // loop through items, and pick the first item
        if ((getItems() != null) && (!getItems().isEmpty())) {
            OleInvoiceItem itemToUse = null;
            for (Iterator iter = getItems().iterator(); iter.hasNext(); ) {
                OleInvoiceItem item = (OleInvoiceItem) iter.next();
                if ((item.isConsideredEntered()) && ((item.getSourceAccountingLines() != null) && (!item.getSourceAccountingLines().isEmpty()))) {
                    // accounting lines are not empty so pick the first account
                    PurApAccountingLine accountLine = item.getSourceAccountingLine(0);
                    accountLine.refreshNonUpdateableReferences();
                    return accountLine;
                }
                /*
                if (((item.getExtendedPrice() != null) && item.getExtendedPrice().compareTo(BigDecimal.ZERO) > 0) && ((item.getAccounts() != null) && (!item.getAccounts().isEmpty()))) {
                    // accounting lines are not empty so pick the first account
               List accts = (List)item.getAccounts();
               InvoiceAccount accountLine = (InvoiceAccount)accts.get(0);
                    return accountLine.getFinancialChartOfAccountsCode() + "-" + accountLine.getAccountNumber();
                }
                */
            }
        }
        return null;

    }

    @Override
    public PurApItem getItem(int pos) {
        OleInvoiceItem item = (OleInvoiceItem) super.getItem(pos);
        if (item.getInvoice() == null) {
            item.setInvoice(this);
        }
        return item;
    }

   /* @Override
    public void processAfterRetrieve() {
        super.processAfterRetrieve();
    }*/

    public void loadInvoiceDocument() {
    try {
        long b1 = System.currentTimeMillis();
     //   super.processAfterRetrieve();
        LOG.debug("###########inside OleInvoiceDocument processAfterRetrieve###########");

        if (this.getPaymentMethodId() != null) {
            OlePaymentMethod olePaymentMethod = SpringContext.getBean(BusinessObjectService.class)
                    .findBySinglePrimaryKey(OlePaymentMethod.class, this.getPaymentMethodId());
            this.setPaymentMethod(olePaymentMethod);
            this.setPaymentMethodIdentifier(this.getPaymentMethodId().toString());
        }
        if (this.getInvoiceTypeId() != null) {
            this.setInvoiceTypeHdnId(this.getInvoiceTypeId().toString());
        }
        if (this.getInvoiceSubTypeId() != null) {
            this.setInvoiceSubTypeHdnId(this.getInvoiceSubTypeId().toString());
        }
        if (this.getVendorInvoiceAmount() != null) {
            this.setInvoiceAmount(this.getVendorInvoiceAmount().toString());
        }
        if (this.getForeignVendorInvoiceAmount() != null) {
            this.setForeignInvoiceAmount(this.getForeignVendorInvoiceAmount().toString());
        }
        List<BigDecimal> newUnitPriceList = new ArrayList<BigDecimal>();
        BigDecimal newUnitPrice = new BigDecimal(0);
        BigDecimal hundred = new BigDecimal(100);
        List<OleInvoiceItem> item = this.getItems();
        String titleId = null;
        //int itemCount = 0;
         /*   List<String> itemIds = new ArrayList<>();
            for(OleInvoiceItem invoiceItem : (List<OleInvoiceItem>)this.getItems()){
                if(invoiceItem.getItemTitleId()!=null){
                    itemIds.add(invoiceItem.getItemTitleId());
                }
            }
            DocstoreClientLocator docstoreClientLocator = new DocstoreClientLocator();
            List<Bib> bibs = new ArrayList<>();
            if(itemIds.size()>0){
                bibs = docstoreClientLocator.getDocstoreClient().retrieveBibs(itemIds);
            }*/
        String itemDescription = "";
        BigDecimal exchangeRate = new BigDecimal("0");
        if (this.getInvoiceCurrencyTypeId() != null) {
            OleCurrencyType oleCurrencyType = SpringContext.getBean(BusinessObjectService.class)
                    .findBySinglePrimaryKey(OleCurrencyType.class, this.getInvoiceCurrencyTypeId());
            this.setOleCurrencyType(oleCurrencyType);
            this.setInvoiceCurrencyType(this.getInvoiceCurrencyTypeId().toString());
            if ( oleCurrencyType.getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME) ){
                currencyTypeIndicator=true;
                this.setForeignCurrencyFlag(false);
            } else {
                currencyTypeIndicator=false;
                this.setForeignCurrencyFlag(true);
            }
        } else {
            if ( this.getVendorDetail() !=null && this.getVendorDetail().getCurrencyType() != null ){
                if(this.getVendorDetail().getCurrencyType().getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
                    currencyTypeIndicator=true;
                    this.setForeignCurrencyFlag(false);
                    this.setInvoiceCurrencyType(this.getVendorDetail().getCurrencyType().getCurrencyTypeId().toString());
                }
                else{
                    currencyTypeIndicator=false;
                    this.setForeignCurrencyFlag(true);
                    this.setInvoiceCurrencyType(this.getVendorDetail().getCurrencyType().getCurrencyTypeId().toString());
                }
            }
        }

        if (this.getInvoiceCurrencyTypeId() != null && (!currencyTypeIndicator) ){
            if (StringUtils.isBlank(this.getInvoiceCurrencyExchangeRate())) {
                exchangeRate = SpringContext.getBean(OleInvoiceService.class).getExchangeRate(this.getInvoiceCurrencyType()).getExchangeRate();
                this.setInvoiceCurrencyExchangeRate(exchangeRate.toString());
                //    items.setItemExchangeRate(new KualiDecimal(exchangeRate));
                //    items.setExchangeRate(exchangeRate.toString());
            }
        }
        int count=0;
        for (int i = 0; item.size() > i; i++) {
            OleInvoiceItem items = (OleInvoiceItem) this.getItem(i);
            if(items.getSequenceNumber() == null) {
                items.setSequenceNumber(++count);
            }
            if (this.getInvoiceCurrencyTypeId() != null) {
                items.setItemCurrencyType(oleCurrencyType.getCurrencyType());
                items.setInvoicedCurrency(oleCurrencyType.getCurrencyType());
            } else {
                if (this.getVendorDetail() !=null && this.getVendorDetail().getCurrencyType() != null ){
                    items.setItemCurrencyType(this.getVendorDetail().getCurrencyType().getCurrencyType());
                    items.setInvoicedCurrency(this.getVendorDetail().getCurrencyType().getCurrencyType());
                }
            }

            if (this.getInvoiceCurrencyTypeId() != null && (!currencyTypeIndicator) ){
                if (StringUtils.isNotBlank(this.getInvoiceCurrencyExchangeRate())) {
                    try {
                        //          Double.parseDouble(this.getInvoiceCurrencyExchangeRate());
                        items.setItemExchangeRate(new BigDecimal(this.getInvoiceCurrencyExchangeRate()));
                        items.setExchangeRate(this.getInvoiceCurrencyExchangeRate());
                    }
                    catch (NumberFormatException nfe) {
                        throw new RuntimeException("Invalid Exchange Rate", nfe);
                    }
                }   else {
                    items.setItemExchangeRate(exchangeRate);
                    items.setExchangeRate(exchangeRate.toString());
                }
                if (items.getItemExchangeRate() != null && items.getItemForeignUnitCost() != null   && !this.getApplicationDocumentStatus().equals("Department-Approved")) {
                    if(!items.getItemForeignUnitCost().equals(new KualiDecimal("0.00"))) {
                        items.setItemUnitCostUSD(new KualiDecimal(items.getItemForeignUnitCost().bigDecimalValue().divide(new BigDecimal(items.getExchangeRate()), 4, BigDecimal.ROUND_HALF_UP)));
                        items.setItemUnitPrice(items.getItemForeignUnitCost().bigDecimalValue().divide(new BigDecimal(items.getExchangeRate()), 4, BigDecimal.ROUND_HALF_UP));
                        items.setItemListPrice(items.getItemUnitCostUSD());
                        items.setExtendedPrice(items.calculateExtendedPrice());
                    }
                    else  {
                        items.setExtendedPrice(items.calculateExtendedPrice());
                    }
                }
                //this.setForeignVendorInvoiceAmount(this.getVendorInvoiceAmount().bigDecimalValue().multiply(tempOleExchangeRate.getExchangeRate()));
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Title id while retriving ------>" + items.getItemTitleId());
            }
            if (items.getItemTitleId() != null) {
                BibInfoRecord  bibInfoRecord = items.getBibInfoRecord();

                if(bibInfoRecord !=null){
                    items.setBibUUID(bibInfoRecord.getBibIdStr());
                    items.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(items.getItemTitleId()));

                    itemDescription = ((bibInfoRecord.getTitle() != null && !bibInfoRecord.getTitle().isEmpty()) ? bibInfoRecord.getTitle().trim() + ", " : "")
                            + ((bibInfoRecord.getAuthor() != null && !bibInfoRecord
                            .getAuthor().isEmpty()) ? bibInfoRecord.getAuthor().trim() + ", "
                            : "")
                            + ((bibInfoRecord.getPublisher() != null && !bibInfoRecord
                            .getPublisher().isEmpty()) ? bibInfoRecord.getPublisher().trim()
                            + ", " : "")
                            + ((bibInfoRecord.getIsxn() != null && !bibInfoRecord.getIsxn()
                            .isEmpty()) ? bibInfoRecord.getIsxn().trim() + ", " : "");
                }
                if (itemDescription != null && !(itemDescription.equals(""))) {
                    itemDescription = itemDescription.lastIndexOf(",") < 0 ? itemDescription :
                            itemDescription.substring(0, itemDescription.lastIndexOf(","));
                    StringEscapeUtils stringEscapeUtils = new StringEscapeUtils();
                    itemDescription = stringEscapeUtils.unescapeXml(itemDescription);
                    items.setItemDescription(itemDescription);
                }
                HashMap<String, String> queryMap = new HashMap<String, String>();
                if (items.getPoItemIdentifier() != null) {
                    queryMap.put(OLEConstants.OleCopy.PO_ITM_ID, items.getPoItemIdentifier().toString());
                    List<OLELinkPurapDonor> oleLinkPurapDonorList = (List<OLELinkPurapDonor>) getBusinessObjectService().findMatching(OLELinkPurapDonor.class, queryMap);
                    if (oleLinkPurapDonorList != null) {
                        items.setOleDonors(oleLinkPurapDonorList);
                        oleLinkPurapDonorList.clear();
                    }
                }
            }
            if ((items.getItemType().isQuantityBasedGeneralLedgerIndicator())) {
                if (items.getItemDiscount() == null) {
                    items.setItemDiscount(KualiDecimal.ZERO);
                }

                if (items.getItemListPrice() == null) {
                    items.setItemListPrice(KualiDecimal.ZERO);
                }

                    /*   if (items.getItemDiscountType() != null && items.getItemDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                       newUnitPrice = (hundred.subtract(items.getItemDiscount().bigDecimalValue())).divide(hundred).multiply(items.getItemListPrice().bigDecimalValue());
                   } else {
                       newUnitPrice = items.getItemListPrice().bigDecimalValue().subtract(items.getItemDiscount().bigDecimalValue());
                   }
                   items.setItemSurcharge(items.getItemUnitPrice().subtract(newUnitPrice).setScale(4, RoundingMode.HALF_UP));*/
            }
                /*else if (items.getItemType().isAdditionalChargeIndicator() && (!currencyTypeIndicator)) {
                    if (items.getItemForeignUnitCost() != null) {
                        items.setAdditionalForeignUnitCost(items.getItemForeignUnitCost().toString());
                    }
                }*/
            items.setOleOpenQuantity(String.valueOf(items.getPoOutstandingQuantity()));
        }
        if (this.getVendorHeaderGeneratedIdentifier() != null && this.getVendorDetailAssignedIdentifier() != null) {
            this.setVendorId(this.getVendorHeaderGeneratedIdentifier().toString() + "-" + this.getVendorDetailAssignedIdentifier().toString());
        }

        if (this.getProrateBy() != null) {
            this.setProrateQty(this.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY));
            this.setProrateManual(this.getProrateBy().equals(OLEConstants.MANUAL_PRORATE));
            this.setProrateDollar(this.getProrateBy().equals(OLEConstants.PRORATE_BY_DOLLAR));
            this.setNoProrate(this.getProrateBy().equals(OLEConstants.NO_PRORATE));
        }
        long b2 = System.currentTimeMillis();
        long tot = b2-b1;
        LOG.info("loadInvoiceDocument"+tot);
    } catch (Exception e) {
        LOG.error("Exception during processAfterRetrieve in OleInvoiceDocument "+e);
        throw new RuntimeException(e);
    }

    }
    /**
     * This method is overrided to create POA from new Line Item of Invoice
     *
     * @see org.kuali.ole.module.purap.document.InvoiceDocument#doRouteStatusChange(org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange)
     */
    @Override
    public void doRouteStatusChange(DocumentRouteStatusChange statusChangeEvent) {
        LOG.debug("doRouteStatusChange() started");

        super.doRouteStatusChange(statusChangeEvent);
        try {
            // DOCUMENT PROCESSED
            if (this.getDocumentHeader().getWorkflowDocument().isProcessed()) {
                if (!InvoiceStatuses.APPDOC_AUTO_APPROVED.equals(getApplicationDocumentStatus())) {
                    //delete unentered items and update po totals and save po
                    getOleInvoiceService().completePaymentDocument(this);
                    this.setApplicationDocumentStatus(PurapConstants.InvoiceStatuses.APPDOC_DEPARTMENT_APPROVED);
                    if (this.getImmediatePaymentIndicator()) {
                        this.setInvoicePayDate(new java.sql.Date(this.getAccountsPayableApprovalTimestamp().getTime()));
                    }
                    /*if(this.getImmediatePaymentIndicator()) {
                        this.setInvoicePayDate(new java.sql.Date(this.getAccountsPayableApprovalTimestamp().getTime()));
                    }*/
                    populateDocumentForRouting();
                    getPurapService().saveDocumentNoValidation(this);
                    SpringContext.getBean(OleInvoiceService.class).createPaymentRequestOrCreditMemoDocument(this);
                    //SpringContext.getBean(OleInvoiceService.class).createPaymentRequest(this);
                    return;
                }
            }
            /* else if (this.getFinancialSystemDocumentHeader().getWorkflowDocument().isFinal())  {
                if(this.getImmediatePaymentIndicator()) {
                    this.setInvoicePayDate(new java.sql.Date(this.getAccountsPayableApprovalTimestamp().getTime()));
                }
                SpringContext.getBean(OleInvoiceService.class).createPaymentRequestOrCreditMemoDocument(this);
            }*/

            // DOCUMENT DISAPPROVED
            else if (this.getDocumentHeader().getWorkflowDocument().isDisapproved()) {
                // String nodeName =
                // getWorkflowDocumentService().getCurrentRouteLevelName(getDocumentHeader().getWorkflowDocument());
                String nodeName = getDocumentHeader().getWorkflowDocument().getCurrentNodeNames().iterator().next();
                HashMap<String, String> disApprovedStatusMap = PurapConstants.InvoiceStatuses
                        .getInvoiceAppDocDisapproveStatuses();
                // NodeDetails currentNode = NodeDetailEnum.getNodeDetailEnumByName(nodeName);
                // STATUS_ORDER currentNode = STATUS_ORDER.getByStatusCode(nodeName);
                if (ObjectUtils.isNotNull(nodeName)) {
                    String newStatusCode = disApprovedStatusMap.get(nodeName);
                    // currentNode.getDisapprovedStatusCode();
                    if ((StringUtils.isBlank(newStatusCode))
                            && ((InvoiceStatuses.APPDOC_INITIATE.equals(getApplicationDocumentStatus())) || (InvoiceStatuses.APPDOC_IN_PROCESS
                            .equals(getApplicationDocumentStatus())))) {
                        newStatusCode = InvoiceStatuses.APPDOC_CANCELLED_POST_AP_APPROVE;
                    }
                    if (StringUtils.isNotBlank(newStatusCode)) {
                        getAccountsPayableService().cancelAccountsPayableDocument(this, nodeName);
                        return;
                    }
                }
                logAndThrowRuntimeException("No status found to set for document being disapproved in node '" + nodeName + "'");
            }
            // DOCUMENT CANCELED
            else if (this.getDocumentHeader().getWorkflowDocument().isCanceled()) {
                // String currentNodeName =
                // getWorkflowDocumentService().getCurrentRouteLevelName(this.getDocumentHeader().getWorkflowDocument());
                // NodeDetails currentNode = NodeDetailEnum.getNodeDetailEnumByName(currentNodeName);
                String nodeName = getDocumentHeader().getWorkflowDocument().getCurrentNodeNames().iterator().next();
                HashMap<String, String> disApprovedStatusMap = PurapConstants.InvoiceStatuses
                        .getInvoiceAppDocDisapproveStatuses();
                if (ObjectUtils.isNotNull(nodeName)) {
                    // String cancelledStatusCode = currentNode.getDisapprovedStatusCode();
                    String cancelledStatusCode = disApprovedStatusMap.get(nodeName);
                    if (StringUtils.isNotBlank(cancelledStatusCode)) {
                        this.setApplicationDocumentStatus(cancelledStatusCode);
                        getPurapService().saveDocumentNoValidation(this);
                        return;
                    }
                }
                logAndThrowRuntimeException("No status found to set for document being canceled in node '" + nodeName
                        + "'");
            }
        } catch (Exception e) {
            logAndThrowRuntimeException("Error saving routing data while saving document with id " + getDocumentNumber(), e);
        }
    }

    /**
     * Sends FYI workflow request to the given user on this document.
     *
     * @param workflowDocument the associated workflow document.
     * @param userNetworkId    the network ID of the user to be sent to.
     * @param annotation       the annotation notes contained in this document.
     * @param responsibility   the responsibility specified in the request.
     * @throws org.kuali.rice.kew.api.exception.WorkflowException
     *
     */
    public void appSpecificRouteDocumentToUser(WorkflowDocument workflowDocument, String userNetworkId,
                                               String annotation, String responsibility) throws WorkflowException {
        if (ObjectUtils.isNotNull(workflowDocument)) {
            String annotationNote = (ObjectUtils.isNull(annotation)) ? "" : annotation;
            String responsibilityNote = (ObjectUtils.isNull(responsibility)) ? "" : responsibility;
            String currentNodeName = getCurrentRouteNodeName(workflowDocument);
            Principal principal = getIdentityManagementService().getPrincipalByPrincipalName(userNetworkId);
            workflowDocument.adHocToPrincipal(ActionRequestType.FYI, currentNodeName, annotationNote,
                    principal.getPrincipalId(), responsibilityNote, true);
        }
    }

    /**
     * Returns the name of the current route node.
     *
     * @param wd the current workflow document.
     * @return the name of the current route node.
     * @throws org.kuali.rice.kew.api.exception.WorkflowException
     *
     */
    protected String getCurrentRouteNodeName(WorkflowDocument wd) throws WorkflowException {
        // String[] nodeNames = wd.getNodeNames();
        Set<String> nodeNameSet = wd.getNodeNames();
        String[] nodeNames = (String[]) nodeNameSet.toArray(); // Here it fails.

        if ((nodeNames == null) || (nodeNames.length == 0)) {
            return null;
        } else {
            return nodeNames[0];
        }
    }

    public BigDecimal getForeignVendorInvoiceAmount() {
        return foreignVendorInvoiceAmount;
    }

    public void setForeignVendorInvoiceAmount(BigDecimal foreignVendorInvoiceAmount) {
        this.foreignVendorInvoiceAmount = foreignVendorInvoiceAmount;
    }

    /**
     * This method is used to get the bibedtior creat url from propertie file
     *
     * @return Bibeditor creat url string
     */
    public String getBibeditorCreateURL() {
        String bibeditorCreateURL = getConfigurationService().getPropertyValueAsString(
                OLEConstants.BIBEDITOR_CREATE_URL_KEY);
        return bibeditorCreateURL;
    }

    /**
     * This method is used to get the bibedtior edit url from propertie file
     *
     * @return Bibeditor edit url string
     */
    public String getBibeditorEditURL() {
        String bibeditorEditURL = getConfigurationService().getPropertyValueAsString(OLEConstants.BIBEDITOR_URL_KEY);
        return bibeditorEditURL;
    }

    /**
     * This method is used to get the bibedtior view url from propertie file
     *
     * @return Bibeditor view url string
     */
    public String getBibeditorViewURL() {
        String bibeditorViewURL = getConfigurationService().getPropertyValueAsString(OLEConstants.DOCSTORE_APP_URL_KEY);
        return bibeditorViewURL;
    }

    /**
     * This method is used to get the directory path where the marc xml files need to be created
     *
     * @return Directory path string
     */
    public String getMarcXMLFileDirLocation() throws Exception {
        String externaleDirectory = getFileProcessingService().getMarcXMLFileDirLocation();
        return externaleDirectory;
    }

    @Override
    public void prepareForSave(KualiDocumentEvent event) {
        // TODO Auto-generated method stub
        // first populate, then call super
        if (event instanceof AttributedContinuePurapEvent) {
            SpringContext.getBean(OleInvoiceService.class).populateInvoice(this);
        }
        if(this.getVendorPaymentTermsCode() != null && this.getVendorPaymentTermsCode().isEmpty()) {
            this.setVendorPaymentTermsCode(null);
        }
        super.prepareForSave(event);
        try {
            if (this.proformaIndicator && !this.immediatePaymentIndicator) {
                this.setImmediatePaymentIndicator(true);
            }
            LOG.debug("###########Inside OleInvoiceDocument " + "repareForSave###########");
            List<OleInvoiceItem> items = new ArrayList<OleInvoiceItem>();
            items = this.getItems();
            Iterator iterator = items.iterator();
            HashMap dataMap = new HashMap();
            String titleId;
            while (iterator.hasNext()) {
                LOG.debug("###########inside prepareForSave item loop###########");
                Object object = iterator.next();
                if (object instanceof OleInvoiceItem) {
                    LOG.debug("###########inside prepareForSave ole payment request item###########");
                    OleInvoiceItem singleItem = (OleInvoiceItem) object;
                    if (StringUtils.isNotBlank(this.invoiceCurrencyType)) {
                        this.setInvoiceCurrencyTypeId(new Long(this.getInvoiceCurrencyType()));
                        String currencyType = SpringContext.getBean(OleInvoiceService.class).getCurrencyType(this.getInvoiceCurrencyType());
                        if (StringUtils.isNotBlank(currencyType)) {
                            if(!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                                if (StringUtils.isNotBlank(this.getInvoiceCurrencyExchangeRate())) {
                                    try {
                                        Double.parseDouble(this.getInvoiceCurrencyExchangeRate());
                                        singleItem.setItemExchangeRate(new BigDecimal(this.getInvoiceCurrencyExchangeRate()));
                                        singleItem.setExchangeRate(this.getInvoiceCurrencyExchangeRate());
                                    }
                                    catch (NumberFormatException nfe) {
                                        throw new RuntimeException("Invalid Exchange Rate", nfe);
                                    }
                                }   else {
                                    BigDecimal exchangeRate = SpringContext.getBean(OleInvoiceService.class).getExchangeRate(this.getInvoiceCurrencyType()).getExchangeRate();
                                    this.setInvoiceCurrencyExchangeRate(exchangeRate.toString());
                                    singleItem.setItemExchangeRate(exchangeRate);
                                    singleItem.setExchangeRate(exchangeRate.toString());
                                }
                                this.setVendorInvoiceAmount(this.getForeignVendorInvoiceAmount() != null ?
                                        new KualiDecimal(this.getForeignVendorInvoiceAmount().divide(new BigDecimal(singleItem.getExchangeRate()), 4, RoundingMode.HALF_UP)) : null);
                            }
                        }
                    }
                    setItemDescription(singleItem);
                    Map<String, String> copyCriteria = new HashMap<String, String>();
                    if (singleItem.getPaidCopies().size() <= 0 && singleItem.getPoItemIdentifier() != null && (this.getPurapDocumentIdentifier() != null && singleItem.getItemIdentifier() != null)) {
                        copyCriteria.put("poItemId", singleItem.getPoItemIdentifier().toString());
                        List<OleCopy> copies = (List<OleCopy>) getBusinessObjectService().findMatching(OleCopy.class, copyCriteria);
                        if (copies.size() > 0) {
                            List<OLEPaidCopy> paidCopies = new ArrayList<OLEPaidCopy>();
                            for (OleCopy copy : copies) {
                                OLEPaidCopy paidCopy = new OLEPaidCopy();
                                paidCopy.setCopyId(copy.getCopyId());
                                paidCopy.setInvoiceItemId(this.getPurapDocumentIdentifier());
                                paidCopy.setInvoiceIdentifier(singleItem.getItemIdentifier());
                                //copy.getOlePaidCopies().add(paidCopy);
                                paidCopies.add(paidCopy);
                            }
                            getBusinessObjectService().save(paidCopies);
                            singleItem.setPaidCopies(paidCopies);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Exception during prepareForSave() in OleInvoiceDocument", e);
            throw new RuntimeException(e);
        }
    }

    /* @Override
        public KualiDecimal getGrandTotal() {
            if ((this.prorateBy != null) && (this.prorateBy.equals(OLEConstants.PRORATE_BY_QTY) || this.prorateBy.equals(OLEConstants.PRORATE_BY_DOLLAR) || this.prorateBy.equals(OLEConstants.MANUAL_PRORATE))) {
                return this.getGrandPreTaxTotal().add(this.getGrandTaxAmount());
            } else {
                return super.getGrandTotal();
            }
        }

        @Override
        public KualiDecimal getGrandTotalExcludingDiscount() {
            String[] discountCode = new String[]{PurapConstants.ItemTypeCodes.ITEM_TYPE_PMT_TERMS_DISCOUNT_CODE};
            if ((this.prorateBy != null)
                    && (this.prorateBy.equals(OLEConstants.PRORATE_BY_QTY)
                    || this.prorateBy.equals(OLEConstants.PRORATE_BY_DOLLAR) || this.prorateBy
                    .equals(OLEConstants.MANUAL_PRORATE))) {
                return this.getTotalDollarAmountWithExclusions(discountCode, false);
            } else {
                return this.getTotalDollarAmountWithExclusions(discountCode, true);
            }
        }

        @Override
        public KualiDecimal getTotalDollarAmountAllItems(String[] excludedTypes) {
            if ((this.prorateBy != null)
                    && (this.prorateBy.equals(OLEConstants.PRORATE_BY_QTY)
                    || this.prorateBy.equals(OLEConstants.PRORATE_BY_DOLLAR) || this.prorateBy
                    .equals(OLEConstants.MANUAL_PRORATE))) {
                return getTotalDollarAmountWithExclusions(excludedTypes, false);
            } else {
                return getTotalDollarAmountWithExclusions(excludedTypes, true);
            }
        }

        @Override
        public KualiDecimal getGrandPreTaxTotalExcludingDiscount() {
            String[] discountCode = new String[]{PurapConstants.ItemTypeCodes.ITEM_TYPE_PMT_TERMS_DISCOUNT_CODE};
            if ((this.prorateBy != null)
                    && (this.prorateBy.equals(OLEConstants.PRORATE_BY_QTY)
                    || this.prorateBy.equals(OLEConstants.PRORATE_BY_DOLLAR) || this.prorateBy
                    .equals(OLEConstants.MANUAL_PRORATE))) {
                return this.getTotalPreTaxDollarAmountWithExclusions(discountCode, false);
            } else {
                return this.getTotalPreTaxDollarAmountWithExclusions(discountCode, true);
            }
        }

        @Override
        public KualiDecimal getGrandTaxAmountExcludingDiscount() {
            String[] discountCode = new String[]{PurapConstants.ItemTypeCodes.ITEM_TYPE_PMT_TERMS_DISCOUNT_CODE};
            if ((this.prorateBy != null)
                    && (this.prorateBy.equals(OLEConstants.PRORATE_BY_QTY)
                    || this.prorateBy.equals(OLEConstants.PRORATE_BY_DOLLAR) || this.prorateBy
                    .equals(OLEConstants.MANUAL_PRORATE))) {
                return this.getTotalTaxAmountWithExclusions(discountCode, false);
            } else {
                return this.getTotalTaxAmountWithExclusions(discountCode, true);
            }
        }

        @Override
        public KualiDecimal getTotalPreTaxDollarAmountAllItems(String[] excludedTypes) {
            if ((this.prorateBy != null)
                    && (this.prorateBy.equals(OLEConstants.PRORATE_BY_QTY)
                    || this.prorateBy.equals(OLEConstants.PRORATE_BY_DOLLAR) || this.prorateBy
                    .equals(OLEConstants.MANUAL_PRORATE))) {
                return getTotalPreTaxDollarAmountWithExclusions(excludedTypes, false);
            } else {
                return getTotalPreTaxDollarAmountWithExclusions(excludedTypes, true);
            }
        }

        @Override
        public KualiDecimal getTotalPreTaxDollarAmountAboveLineItems() {
            if ((this.prorateBy != null) && (this.prorateBy.equals(OLEConstants.PRORATE_BY_QTY) || this.prorateBy.equals(OLEConstants.PRORATE_BY_DOLLAR) || this.prorateBy.equals(OLEConstants.MANUAL_PRORATE))) {
                KualiDecimal addChargeItem = KualiDecimal.ZERO;
                KualiDecimal lineItemPreTaxTotal = KualiDecimal.ZERO;
                KualiDecimal prorateSurcharge = KualiDecimal.ZERO;
                List<OleInvoiceItem> item = this.getItems();
                for (OleInvoiceItem items : item) {
                    if (items.getItemType().isQuantityBasedGeneralLedgerIndicator() && items.getExtendedPrice() != null && items.getExtendedPrice().compareTo(KualiDecimal.ZERO) != 0) {
                        if (items.getItemSurcharge() != null) {
                            prorateSurcharge = new KualiDecimal(items.getItemSurcharge());
                        }
                        addChargeItem = addChargeItem.add(items.getExtendedPrice().subtract(prorateSurcharge.multiply(items.getItemQuantity())));
                    }
                }
                lineItemPreTaxTotal = addChargeItem;
                return lineItemPreTaxTotal;
            } else {
                return super.getTotalPreTaxDollarAmountAboveLineItems();
            }
        }
    */

    /**
     * This method is used to check the status of the document for displaying view and edit buttons in line item
     *
     * @return boolean
     */
    public boolean getIsSaved() {
        if (this.getDocumentHeader().getWorkflowDocument().isSaved()
                || this.getDocumentHeader().getWorkflowDocument().isInitiated()) {
            return true;
        }
        return false;
    }

    private void setItemDescription(OleInvoiceItem singleItem) throws Exception {
        if (singleItem.getOleOrderRecord() != null) {
            Map<String, ?> bibAssociatedFieldValuesMap = singleItem.getOleOrderRecord().getOleBibRecord().getBibAssociatedFieldsValueMap();
            List titleList = (List) bibAssociatedFieldValuesMap.get("Title_search");
            String title = titleList != null && !titleList.isEmpty() ? (String) (titleList).get(0) : null;
            List authorList = (List) bibAssociatedFieldValuesMap.get("Author_search");
            String author = authorList != null && !authorList.isEmpty() ? (String) (authorList).get(0) : null;
            List publisherList = (List) bibAssociatedFieldValuesMap.get("Publisher_search");
            String publisher = publisherList != null && !publisherList.isEmpty() ? (String) (publisherList).get(0) : null;
            List isbnList = (List) bibAssociatedFieldValuesMap.get("020a");
            String isbn = isbnList != null && !isbnList.isEmpty() ? (String) (isbnList).get(0) : null;

            singleItem.setBibUUID(singleItem.getOleOrderRecord().getOleBibRecord().getBibUUID());
            singleItem.setItemDescription(title + "," + author + "," + publisher + "," + isbn);
        }
    }

    public String getProrateBy() {
        return prorateBy;
    }

    public void setProrateBy(String prorateBy) {
        this.prorateBy = prorateBy;
    }

    /**
     * This method returns the boolean if the proforma indicator is selected
     *
     * @return proformaIndicator
     */
    public boolean isProformaIndicator() {
        return proformaIndicator;
    }

    /**
     * This method sets the proforma Indicator
     *
     * @param proformaIndicator
     */

    public void setProformaIndicator(boolean proformaIndicator) {
        this.proformaIndicator = proformaIndicator;
    }

    /**
     * Payment Request needs to wait for receiving if the receiving requirements have NOT been met.
     *
     * @return
     */
    @Override
    protected boolean shouldWaitForReceiving() {
        // only require if PO was marked to require receiving
        Boolean invReceiveInd = false;
        //List<OleInvoiceItem> invItem = (List<OleInvoiceItem>) this.getItems();
        for (OleInvoiceItem invoiceItem : (List<OleInvoiceItem>) this.getItems()) {
            if (invoiceItem.isReceivingDocumentRequiredIndicator()) {
                invReceiveInd = true;
                break;
            }
        }


        if (invReceiveInd && !this.proformaIndicator) {
            return !isReceivingRequirementMet();
        }

        //receiving is not required or has already been fulfilled, no need to stop for routing
        return false;
    }

    /**
     * Provides answers to the following splits: PurchaseWasReceived VendorIsEmployeeOrNonResidentAlien
     *
     * @see org.kuali.ole.sys.document.FinancialSystemTransactionalDocumentBase#answerSplitNodeQuestion(String)
     */
    @Override
    public boolean answerSplitNodeQuestion(String nodeName) throws UnsupportedOperationException {
        if (nodeName.equals(OLEConstants.HAS_VENDOR_DEPOSIT_ACCOUNT)) {
            return hasVendorDepositAccount();
        }
        if (nodeName.equals(OLEConstants.OleInvoice.HAS_INVOICE_TYPE)) {
            return hasInvoiceType();
        }
        if (nodeName.equals(OLEConstants.OleInvoice.HAS_PREPAID_INVOICE_TYPE)) {
            return hasPrepaidInvoiceType();
        }
        if (nodeName.equals(OLEConstants.OleInvoice.HAS_PAYMENT_METHOD)) {
            return hasPaymentMethod();
        }
        if (nodeName.equals(PurapWorkflowConstants.BUDGET_REVIEW_REQUIRED)) {
            if (SpringContext.getBean(OleInvoiceService.class).getParameterBoolean(OLEConstants.CoreModuleNamespaces.SELECT, OLEConstants.OperationType.SELECT, PurapParameterConstants.ALLOW_INVOICE_SUFF_FUND_CHECK)) {
                return isBudgetReviewRequired();
            } else {
                return Boolean.FALSE;
            }
        }
        if (nodeName.equals(PurapWorkflowConstants.REQUIRES_IMAGE_ATTACHMENT)) {
            return requiresAccountsPayableReviewRouting();
        }
        if (nodeName.equals(PurapWorkflowConstants.PURCHASE_WAS_RECEIVED)) {
            return shouldWaitForReceiving();
        }
        if (nodeName.equals(PurapWorkflowConstants.VENDOR_IS_EMPLOYEE_OR_NON_RESIDENT_ALIEN)) {
            return isVendorEmployeeOrNonResidentAlien();
        }
        if (nodeName.equals(OLEConstants.REQUIRES_SEPARATION_OF_DUTIES)) {
            return isSeparationOfDutiesReviewRequired();
        }

        if (nodeName.equals(PurapWorkflowConstants.NOTIFY_BUDGET_REVIEW)) {
            if (SpringContext.getBean(OleInvoiceService.class).getParameterBoolean(OLEConstants.CoreModuleNamespaces.SELECT, OLEConstants.OperationType.SELECT, PurapParameterConstants.ALLOW_INVOICE_SUFF_FUND_CHECK)) {
                return isNotificationRequired();
            } else {
                return Boolean.FALSE;
            }
        }
        throw new UnsupportedOperationException("Cannot answer split question for this node you call \"" + nodeName + "\"");
    }

    private boolean hasInvoiceType() {
        if (this.getInvoiceTypeId() != null) {
            return true;
        }
        return false;
    }

    private boolean hasVendorDepositAccount() {
        if (this.getPaymentMethodIdentifier() != null) {
            String paymentType = SpringContext.getBean(OleInvoiceService.class).getPaymentMethodType(this.getPaymentMethodIdentifier());
            if (paymentType.equals("Deposit")) {
                return true;
            }
        }
        return false;
    }

    private boolean hasPrepaidInvoiceType() {
        if (this.getInvoiceTypeId() != null) {
            Map<String, String> invoiceMap = new HashMap<String, String>();
            invoiceMap.put("invoiceTypeId", this.getInvoiceTypeId().toString());
            OleInvoiceType invoiceType = this.getBusinessObjectService().findByPrimaryKey(OleInvoiceType.class,
                    invoiceMap);
            if (invoiceType != null &&
                    invoiceType.getInvoiceType().equals("Prepay") ||
                    invoiceType.getInvoiceType().equals("Deposit")) {
                return true;
            }
        }
        return false;
    }

    private boolean hasPaymentMethod() {
        if (this.getPaymentMethodId() != null) {
            return true;
        }
        return false;
    }

    public Set<Person> getAllPriorApprovers() throws WorkflowException {
        PersonService personService = KimApiServiceLocator.getPersonService();
        List<ActionTaken> actionsTaken = getDocumentHeader().getWorkflowDocument().getActionsTaken();
        Set<String> principalIds = new HashSet<String>();
        Set<Person> persons = new HashSet<Person>();

        for (ActionTaken actionTaken : actionsTaken) {
            if (KewApiConstants.ACTION_TAKEN_APPROVED_CD.equals(actionTaken.getActionTaken())) {
                String principalId = actionTaken.getPrincipalId();
                if (!principalIds.contains(principalId)) {
                    principalIds.add(principalId);
                    persons.add(personService.getPerson(principalId));
                }
            }
        }
        return persons;
    }

    protected boolean isSeparationOfDutiesReviewRequired() {
        try {
            Set<Person> priorApprovers = getAllPriorApprovers();

            // The initiator cannot be the approver
            String initiatorPrincipalId = getDocumentHeader().getWorkflowDocument().getInitiatorPrincipalId();
            Person initiator = SpringContext.getBean(PersonService.class).getPerson(initiatorPrincipalId);
            // If there is only one approver, and that approver is also the initiator, then Separation of Duties is required.
            boolean priorApproverIsInitiator = priorApprovers.contains(initiator);
            boolean onlyOneApprover = (priorApprovers.size() == 1);
            if (priorApproverIsInitiator && onlyOneApprover) {
                return true;
            }

            // if there are more than 0 prior approvers which means there had been at least another approver than the current approver
            // then no need for separation of duties
            if (priorApprovers.size() > 0) {
                return false;
            }
        } catch (WorkflowException we) {
            LOG.error("Exception while attempting to retrieve all prior approvers from workflow: " + we);
        }
        return false;
    }

    public boolean isBudgetReviewRequired() {

        OleInvoiceFundCheckServiceImpl oleInvoiceFundCheckServiceImpl = new OleInvoiceFundCheckServiceImpl();
        boolean required = false;
        if ((SpringContext.getBean(OleInvoiceService.class).getPaymentMethodType(this.getPaymentMethodIdentifier())).equals(OLEConstants.DEPOSIT)) {
            return false;
        }
        // if document's fiscal year is less than or equal to the current fiscal year
        if (SpringContext.getBean(OleInvoiceService.class).getParameterBoolean(OLEConstants.CoreModuleNamespaces.SELECT, OLEConstants.OperationType.SELECT, PurapParameterConstants.ALLOW_INVOICE_SUFF_FUND_CHECK)) {
            if (SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear().compareTo(getPostingYear()) >= 0) {
                List<SourceAccountingLine> sourceAccountingLineList = this.getSourceAccountingLines();
                for (SourceAccountingLine accLine : sourceAccountingLineList) {
                    String chart = accLine.getAccount().getChartOfAccountsCode();
                    String account = accLine.getAccount().getAccountNumber();
                    String sfCode = accLine.getAccount().getAccountSufficientFundsCode();
                    Map<String, Object> key = new HashMap<String, Object>();
                    key.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chart);
                    key.put(OLEPropertyConstants.ACCOUNT_NUMBER, account);
                    OleSufficientFundCheck oleSufficientFundCheck = businessObjectService.findByPrimaryKey(
                            OleSufficientFundCheck.class, key);
               /* List<GeneralLedgerPendingEntry> pendingEntries = getPendingLedgerEntriesForSufficientFundsChecking();
                for (GeneralLedgerPendingEntry glpe : pendingEntries) {
                    glpe.getChartOfAccountsCode();
                }*/
               /* SpringContext.getBean(GeneralLedgerPendingEntryService.class).generateGeneralLedgerPendingEntries(this);
                SpringContext.getBean(BusinessObjectService.class).save(getGeneralLedgerPendingEntries());
*/
                    if (oleSufficientFundCheck != null) {
                        String option = oleSufficientFundCheck.getNotificationOption() != null ? oleSufficientFundCheck
                                .getNotificationOption() : "";
                        if (option.equals(OLEPropertyConstants.BUD_REVIEW)) {
                        /*List<GeneralLedgerPendingEntry> pendingEntries = getPendingLedgerEntriesForSufficientFundsChecking();
                        for (GeneralLedgerPendingEntry glpe : pendingEntries) {
                            glpe.getChartOfAccountsCode();
                        }
                        SpringContext.getBean(GeneralLedgerPendingEntryService.class).generateGeneralLedgerPendingEntries(this);
                        SpringContext.getBean(BusinessObjectService.class).save(getGeneralLedgerPendingEntries());*/

                            required = oleInvoiceFundCheckServiceImpl.hasSufficientFundCheckRequired(accLine);
                      /*  SpringContext.getBean(GeneralLedgerPendingEntryService.class).delete(getDocumentNumber());*/
/*                        SpringContext.getBean(GeneralLedgerPendingEntryService.class).delete(getDocumentNumber());*/
                            return required;
                        }
                    }
                }
            }
        }
      //  SpringContext.getBean(GeneralLedgerPendingEntryService.class).delete(getDocumentNumber());
        /*SpringContext.getBean(GeneralLedgerPendingEntryService.class).delete(getDocumentNumber());*/
        return required;
        // get list of sufficientfundItems

        // delete and recreate the GL entries for this document so they do not get included in the SF check
        // This is *NOT* ideal. The SF service needs to be updated to allow it to provide the current
        // document number so that it can be exlcuded from pending entry checks.
        //   List<GeneralLedgerPendingEntry> pendingEntries = getPendingLedgerEntriesForSufficientFundsChecking();
        // dumb loop to just force OJB to load the objects. Otherwise, the proxy object above
        // only gets resolved *after* the delete below and no SF check happens.
        // for (GeneralLedgerPendingEntry glpe : pendingEntries) {
        // glpe.getChartOfAccountsCode();
        // }
        // SpringContext.getBean(GeneralLedgerPendingEntryService.class).delete(getDocumentNumber());
        // List<SufficientFundsItem> fundsItems = SpringContext.getBean(SufficientFundsService.class)
        //.checkSufficientFundsForPREQ(pendingEntries);

        //if (fundsItems.size() > 0) {
        //return true;
        //}


        //return false;
    }

    private boolean isNotificationRequired() {
        OleInvoiceFundCheckServiceImpl oleInvoiceFundCheckServiceImpl = new OleInvoiceFundCheckServiceImpl();
        if ((SpringContext.getBean(OleInvoiceService.class).getPaymentMethodType(this.getPaymentMethodIdentifier())).equals(OLEConstants.DEPOSIT)) {
            return false;
        }
        if (SpringContext.getBean(OleInvoiceService.class).getParameterBoolean(OLEConstants.CoreModuleNamespaces.SELECT, OLEConstants.OperationType.SELECT, PurapParameterConstants.ALLOW_INVOICE_SUFF_FUND_CHECK)) {
            Map searchMap = new HashMap();
            String notificationOption = null;
            Map<String, Object> key = new HashMap<String, Object>();
            List<SourceAccountingLine> sourceAccountingLineList = this.getSourceAccountingLines();
            boolean sufficientFundCheck = false;
            for (SourceAccountingLine accLine : sourceAccountingLineList) {
                String chartCode = accLine.getChartOfAccountsCode();
                String accNo = accLine.getAccountNumber();
                key.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
                key.put(OLEPropertyConstants.ACCOUNT_NUMBER, accNo);
                OleSufficientFundCheck account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(
                        OleSufficientFundCheck.class, key);
                if (account != null) {
                    notificationOption = account.getNotificationOption();
                }
                if (notificationOption != null && notificationOption.equals(OLEPropertyConstants.NOTIFICATION)) {
                    sufficientFundCheck = oleInvoiceFundCheckServiceImpl.hasSufficientFundCheckRequired(accLine);
                    return sufficientFundCheck;
                }
                searchMap.clear();
            }
            return sufficientFundCheck;
        }
        else {
            return false;
        }
    }

    @Override
    protected void populateAccountsForRouting() {

        // if(this.getDocumentHeader().ge)
        List<SufficientFundsItem> fundsItems = new ArrayList<SufficientFundsItem>();
        try {
            // String nodeName =
            // SpringContext.getBean(WorkflowDocumentService.class).getCurrentRouteLevelName(getFinancialSystemDocumentHeader().getWorkflowDocument());
            String nodeName = getFinancialSystemDocumentHeader().getWorkflowDocument().getCurrentNodeNames().iterator()
                    .next();
            if (nodeName != null
                    && (nodeName.equalsIgnoreCase(PurapWorkflowConstants.BUDGET_NODE) || nodeName
                    .equalsIgnoreCase(PurapWorkflowConstants.BUDGET_REVIEW_REQUIRED))) {
                /*if (SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear()
                        .compareTo(getPostingYear()) >= 0) {

                    SpringContext.getBean(GeneralLedgerPendingEntryService.class).generateGeneralLedgerPendingEntries(
                            this);
                    SpringContext.getBean(BusinessObjectService.class).save(getGeneralLedgerPendingEntries());

                    List<GeneralLedgerPendingEntry> pendingEntries = getPendingLedgerEntriesForSufficientFundsChecking();
                    for (GeneralLedgerPendingEntry glpe : pendingEntries) {
                        glpe.getChartOfAccountsCode();
                    }

                    fundsItems = SpringContext.getBean(SufficientFundsService.class).checkSufficientFundsForInvoice(pendingEntries);

*//*
                    SpringContext.getBean(GeneralLedgerPendingEntryService.class).generateGeneralLedgerPendingEntries(
                            this);
                    SpringContext.getBean(BusinessObjectService.class).save(getGeneralLedgerPendingEntries());

                    fundsItems = SpringContext.getBean(SufficientFundsService.class).checkSufficientFundsForInvoice(
                            pendingEntries);
*//*

                }*/
                SpringContext.getBean(PurapAccountingService.class).updateAccountAmounts(this);
                if (accountsForRouting == null) {
                    accountsForRouting = (SpringContext.getBean(PurapAccountingService.class).generateSummary(getItems()));
                }
                /*String documentFiscalYearString = this.getPostingYear().toString();
                List<String> fundsItemList = new ArrayList<String>();
                for (SufficientFundsItem fundsItem : fundsItems) {
                    fundsItemList.add(fundsItem.getAccount().getChartOfAccountsCode());
                }*/
                if (accountsForRouting != null) {
                    /*for (Iterator accountsForRoutingIter = accountsForRouting.iterator(); accountsForRoutingIter.hasNext(); ) {
                        if (!(fundsItemList.contains(((SourceAccountingLine) accountsForRoutingIter.next())
                                .getChartOfAccountsCode()))) {
                            accountsForRoutingIter.remove();
                        }
                    }*/

/*                SpringContext.getBean(GeneralLedgerPendingEntryService.class).delete(getDocumentNumber());*/
                    setAccountsForRouting(accountsForRouting);
                }
                // need to refresh to get the references for the searchable attributes (ie status) and for invoking route levels (ie
                // account
                // objects) -hjs
                refreshNonUpdateableReferences();
                for (SourceAccountingLine sourceLine : getAccountsForRouting()) {
                    sourceLine.refreshNonUpdateableReferences();
                }
            } else {
                super.populateAccountsForRouting();
            }
            SpringContext.getBean(GeneralLedgerPendingEntryService.class).delete(getDocumentNumber());
        } catch (Exception e) {
            logAndThrowRuntimeException("Error in populateAccountsForRouting while submitting document with id "
                    + getDocumentNumber(), e);
        }

    }

    /**
     * @see org.kuali.rice.krad.document.DocumentBase#doRouteLevelChange(org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange)
     */
    @Override
    public void doRouteLevelChange(DocumentRouteLevelChange levelChangeEvent) {

        super.doRouteLevelChange(levelChangeEvent);
        try {
            String newNodeName = levelChangeEvent.getNewNodeName();
            List<String> desiredActions = new ArrayList<String>(2);
            RoutingReportCriteria.Builder reportCriteria = RoutingReportCriteria.Builder
                    .createByDocumentIdAndTargetNodeName(getDocumentNumber(), newNodeName);
            desiredActions.add(ActionRequestType.APPROVE.getCode());
            desiredActions.add(ActionRequestType.FYI.getCode());
            desiredActions.add(ActionRequestType.COMPLETE.getCode());
            if (KewApiServiceLocator.getWorkflowDocumentActionsService().documentWillHaveAtLeastOneActionRequest(
                    reportCriteria.build(), desiredActions, false)) {
                if (StringUtils.isNotBlank(newNodeName)) {
                    if (StringUtils.isNotBlank(newNodeName)) {
                        if (newNodeName.equalsIgnoreCase(PurapWorkflowConstants.BUDGET_NODE)
                                || newNodeName.equalsIgnoreCase(PurapWorkflowConstants.FYI_BUDGET)) {
                            String note = "";
                            if (newNodeName.equalsIgnoreCase(PurapWorkflowConstants.BUDGET_NODE)) {
                                note = OLEConstants.SufficientFundCheck.INV_NOTE;
                            }
                            if (newNodeName.equalsIgnoreCase(PurapWorkflowConstants.FYI_BUDGET)) {
                                note = OLEConstants.SufficientFundCheck.FYI_NOTE;
                            }
                            DocumentService documentService = SpringContext.getBean(DocumentService.class);
                            Note apoNote = documentService.createNoteFromDocument(this, note);
                            this.addNote(apoNote);
                            documentService.saveDocumentNotes(this);
                        }
                    }
                }
            }
        } catch (Exception e) {
            String errorMsg = "Workflow Error found checking actions requests on document with id "
                    + getDocumentNumber() + ". *** WILL NOT UPDATE PURAP STATUS ***";
            LOG.error(errorMsg, e);
        }
    }

    private OleInvoiceDocument invoiceDocumentObject;

    public OleInvoiceDocument getInvoiceDocumentObject() {
        return invoiceDocumentObject;
    }

    public void setInvoiceDocumentObject(OleInvoiceDocument invoiceDocumentObject) {
        this.invoiceDocumentObject = invoiceDocumentObject;
    }


    private String invoiceNumber;
    //private String paymentAttachmentIndicator;
    private String invoicePaymentClassification;
    private String invoiceVendorAmount;
    private String invoiceExtractDate;
    private String invoiceProcessedDate;
    private String invoiceType;
    private String invoiceSubType;
    private String invoiceBankCode;
    //private Date invoicePayDate;
    private String invoicePayDateCheck;
    private String vendorAmount;
    private String itemTotal;
    private String foreignItemTotal;
    private String grantTotal;
    private String foreignGrandTotal;

    private String invoicedForeignItemTotal;
    private String invoicedForeignGrandTotal;

    public String getInvoicePayDateCheck() {
        return invoicePayDateCheck;
    }

    public void setInvoicePayDateCheck(String invoicePayDateCheck) {
        this.invoicePayDateCheck = invoicePayDateCheck;
    }


    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }


    public String getInvoicePaymentClassification() {
        return invoicePaymentClassification;
    }

    public void setInvoicePaymentClassification(String invoicePaymentClassification) {
        this.invoicePaymentClassification = invoicePaymentClassification;
    }

    public String getInvoiceVendorAmount() {
        return invoiceVendorAmount;
    }

    public void setInvoiceVendorAmount(String invoiceVendorAmount) {
        this.invoiceVendorAmount = invoiceVendorAmount;
    }

    public String getInvoiceExtractDate() {
        return invoiceExtractDate;
    }

    public void setInvoiceExtractDate(String invoiceExtractDate) {
        this.invoiceExtractDate = invoiceExtractDate;
    }

    public String getInvoiceProcessedDate() {
        return invoiceProcessedDate;
    }

    public void setInvoiceProcessedDate(String invoiceProcessedDate) {
        this.invoiceProcessedDate = invoiceProcessedDate;
    }

    public String getInvoiceBankCode() {
        return invoiceBankCode;
    }

    public void setInvoiceBankCode(String invoiceBankCode) {
        this.invoiceBankCode = invoiceBankCode;
    }


    private String poId;

    public String getPoId() {
        return poId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
    }


    public String getInvoiceCostSourceCode() {
        return invoiceCostSourceCode;
    }

    public void setInvoiceCostSourceCode(String invoiceCostSourceCode) {
        this.invoiceCostSourceCode = invoiceCostSourceCode;
    }

    public Integer getVendorHeaderGeneratedIdentifier() {
        return vendorHeaderGeneratedIdentifier;
    }

    public void setVendorHeaderGeneratedIdentifier(Integer vendorHeaderGeneratedIdentifier) {
        this.vendorHeaderGeneratedIdentifier = vendorHeaderGeneratedIdentifier;
    }

    public List<SummaryAccount> getSummaryAccounts() {
        if (summaryAccounts == null) {
            refreshAccountSummmary();
        }
        return summaryAccounts;
    }

    public void setSummaryAccounts(List<SummaryAccount> summaryAccounts) {
        this.summaryAccounts = summaryAccounts;
    }

    public void clearSummaryAccounts() {
        summaryAccounts = new ArrayList<SummaryAccount>();
    }

    /**
     * Updates the summaryAccounts that are contained in the form. Currently we are only calling this on load and when
     * refreshAccountSummary is called.
     */
    public void refreshAccountSummmary() {
        clearSummaryAccounts();
        summaryAccounts.addAll(SpringContext.getBean(PurapAccountingService.class).generateSummaryAccounts(this));
    }

    public boolean isSfcFlag() {
        return sfcFlag;
    }

    public void setSfcFlag(boolean sfcFlag) {
        this.sfcFlag = sfcFlag;
    }

    public String getInvoiceItemTotal() {
        // if (this.invoiceItemTotal == null || this.invoiceItemTotal.isEmpty() || this.invoiceItemTotal.equalsIgnoreCase("0.00")) {
        BigDecimal addChargeItem = BigDecimal.ZERO;
        List<OleInvoiceItem> item = this.getItems();
        for (OleInvoiceItem invoiceditem : item) {
            if (invoiceditem.getItemType().isAdditionalChargeIndicator() && invoiceditem.getExtendedPrice() != null) {
                addChargeItem = addChargeItem.add(invoiceditem.getExtendedPrice().bigDecimalValue());
            }
        }
        return (this.getTotalDollarAmount().subtract(new KualiDecimal(addChargeItem)).toString());

        /* }
       return invoiceItemTotal;*/
    }

    public void setInvoiceItemTotal(String invoiceItemTotal) {
        this.invoiceItemTotal = invoiceItemTotal;
    }

    public String getInvoiceForeignItemTotal() {
        BigDecimal addChargeItem = BigDecimal.ZERO;
        List<OleInvoiceItem> item = this.getItems();
        for (OleInvoiceItem invoicedItem : item) {
            if (invoicedItem.getItemType().isAdditionalChargeIndicator() && invoicedItem.getItemUnitPrice() != null) {
                addChargeItem = addChargeItem.add(invoicedItem.getItemUnitPrice());
            }
        }
        return (this.getTotalDollarAmount().subtract(new KualiDecimal(addChargeItem)).toString());
    }

    public void setInvoiceForeignItemTotal(String invoiceForeignItemTotal) {
        this.invoiceForeignItemTotal = invoiceForeignItemTotal;
    }

    public String getVendorAmount() {
        if (vendorAmount != null && !vendorAmount.isEmpty()) {
            return vendorAmount;
        } else {
            vendorAmount = this.getInvoiceAmount();
            if (vendorAmount != null && vendorAmount.contains("-")) {
                vendorAmount = vendorAmount.replace("-", "");
                vendorAmount = "(" + vendorAmount + ")";
            }
            return vendorAmount;
        }
    }

    public void setVendorAmount(String vendorAmount) {
        this.vendorAmount = vendorAmount;
        if (vendorAmount.contains("(") || vendorAmount.contains(")")) {
            vendorAmount = vendorAmount.replace("(", "");
            vendorAmount = vendorAmount.replace(")", "");
            vendorAmount = "-" + vendorAmount;
            this.vendorAmount = vendorAmount;
        }
        if (vendorAmount != null && new KualiDecimal(vendorAmount).isLessThan(KualiDecimal.ZERO)) {
            this.setInvoiceAmount(vendorAmount);
            vendorAmount = vendorAmount.replace("-", "");
            vendorAmount = "(" + vendorAmount + ")";
            this.vendorAmount = vendorAmount;
        } else {
            this.setInvoiceAmount(vendorAmount);
            this.vendorAmount = vendorAmount;
        }
    }

    public String getForeignVendorAmount() {
        if (foreignVendorAmount != null && !foreignVendorAmount.isEmpty()) {
            return foreignVendorAmount;
        } else {
            foreignVendorAmount = this.getForeignVendorInvoiceAmount() != null ? this.getForeignVendorInvoiceAmount().toString() : null;
            if (foreignVendorAmount != null && foreignVendorAmount.contains("-")) {
                foreignVendorAmount = foreignVendorAmount.replace("-", "");
                foreignVendorAmount = "(" + foreignVendorAmount + ")";
            }
            return foreignVendorAmount;
        }
    }

    public void setForeignVendorAmount(String foreignVendorAmount) {
        this.foreignVendorAmount = foreignVendorAmount;
        if (foreignVendorAmount.contains("(") || foreignVendorAmount.contains(")")) {
            foreignVendorAmount = foreignVendorAmount.replace("(", "");
            foreignVendorAmount = foreignVendorAmount.replace(")", "");
            foreignVendorAmount = "-" + foreignVendorAmount;
            this.foreignVendorAmount = foreignVendorAmount;
        }
        if (foreignVendorAmount != null && new KualiDecimal(foreignVendorAmount).isLessThan(KualiDecimal.ZERO)) {
            this.setForeignInvoiceAmount(foreignVendorAmount);
            foreignVendorAmount = foreignVendorAmount.replace("-", "");
            foreignVendorAmount = "(" + foreignVendorAmount + ")";
            this.foreignVendorAmount = foreignVendorAmount;
        } else {
            this.setForeignInvoiceAmount(foreignVendorAmount);
            this.foreignVendorAmount = foreignVendorAmount;
        }
    }

    public String getForeignInvoiceAmount() {
        return foreignInvoiceAmount;
    }

    public void setForeignInvoiceAmount(String foreignInvoiceAmount) {
        this.foreignInvoiceAmount = foreignInvoiceAmount;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public boolean isBaSfcFlag() {
        return baSfcFlag;
    }

    public void setBaSfcFlag(boolean baSfcFlag) {
        this.baSfcFlag = baSfcFlag;
    }

    /**
     * This method calculates the grand Total
     *
     * @param includeBelowTheLine
     * @param itemsForTotal
     * @return
     */
    protected KualiDecimal getInvoicedTotalWithAllItems(boolean includeBelowTheLine, List<OleInvoiceItem> itemsForTotal) {

        BigDecimal total = BigDecimal.ZERO;
        for (OleInvoiceItem item : itemsForTotal) {
            item.refreshReferenceObject(PurapPropertyConstants.ITEM_TYPE);
            ItemType it = item.getItemType();
            if (includeBelowTheLine || it.isLineItemIndicator()) {
                BigDecimal totalAmount = item.getTotalAmount().bigDecimalValue();
                BigDecimal itemTotal = (totalAmount != null) ? totalAmount : BigDecimal.ZERO;
                if (item.isDebitItem()) {
                    total = total.add(itemTotal);
                } else {
                    total = total.subtract(itemTotal);
                }
                /*if (item.getItemDiscount() != null) {
                    total = total.subtract(item.getItemDiscount());
                }*/
            }
        }
        return new KualiDecimal(total);
    }

    protected KualiDecimal getInvoicedForeignTotalWithAllItems(boolean includeBelowTheLine, List<OleInvoiceItem> itemsForTotal) {

        KualiDecimal total = new KualiDecimal(BigDecimal.ZERO);
        for (OleInvoiceItem item : itemsForTotal) {
            item.refreshReferenceObject(PurapPropertyConstants.ITEM_TYPE);
            ItemType it = item.getItemType();
            if (includeBelowTheLine || it.isLineItemIndicator()) {
                KualiDecimal totalAmount = item.getForeignTotalAmount();
                KualiDecimal itemTotal = (totalAmount != null) ? totalAmount : KualiDecimal.ZERO;
                if (item.isDebitItem()) {
                    total = total.add(itemTotal);
                } else {
                    total = total.subtract(itemTotal);
                }
            }
        }
        return total;
    }

    public String getInvoicedItemTotal() {
        KualiDecimal total = getInvoicedTotalWithAllItems(false, this.getItems());
        if (this.isItemSign()) {
            if (total.isLessThan(KualiDecimal.ZERO)) {
                total = total;
            }
        }
        return total != null ? total.toString() : "0";
    }

    public void setInvoicedItemTotal(String invoicedItemTotal) {
        this.invoicedItemTotal = invoicedItemTotal;
    }

    public String getInvoicedForeignItemTotal() {
        KualiDecimal foreignItemTotal = getInvoicedForeignTotalWithAllItems(false, this.getItems());
       /* if (this.isItemSign()) {
            if (foreignItemTotal.isLessThan(KualiDecimal.ZERO)) {
                foreignItemTotal = foreignItemTotal;
            }
        } */
        return foreignItemTotal != null ? foreignItemTotal.toString() : "0";
    }

    public void setInvoicedForeignItemTotal(String invoicedForeignItemTotal) {
        this.invoicedForeignItemTotal = invoicedForeignItemTotal;
    }

    public String getItemTotal() {
        if(!this.isDbRetrieval()){
            itemTotal = this.getInvoicedItemTotal();
        }
        if (itemTotal != null && !itemTotal.contains("(")
                && new KualiDecimal(itemTotal).isLessThan(KualiDecimal.ZERO)) {
            itemTotal = itemTotal.replace("-", "");
            itemTotal = "(" + itemTotal + ")";
        }
        return itemTotal;
    }

    public void setItemTotal(String itemTotal) {
        this.itemTotal = itemTotal;
    }

    public String getForeignItemTotal() {
        if(!this.isDbRetrieval()){
            foreignItemTotal = this.getInvoicedForeignItemTotal();
        }
        if (foreignItemTotal != null && !foreignItemTotal.contains("(")
                && new KualiDecimal(foreignItemTotal).isLessThan(KualiDecimal.ZERO)) {
            foreignItemTotal = foreignItemTotal.replace("-", "");
            foreignItemTotal = "(" + foreignItemTotal + ")";
        }
        return foreignItemTotal;
    }

    public void setForeignItemTotal(String foreignItemTotal) {
        this.foreignItemTotal = foreignItemTotal;
    }

    public String getGrantTotal() {
        if(!this.isDbRetrieval()){
            grantTotal = this.getInvoicedGrandTotal();
        }
        if (grantTotal != null &&  !grantTotal.contains("(")
                && new KualiDecimal(grantTotal).isLessThan(KualiDecimal.ZERO)) {
            grantTotal = grantTotal.replace("-", "");
            grantTotal = "(" + grantTotal + ")";
        }
        return grantTotal;
    }

    public void setGrantTotal(String grantTotal) {
        this.grantTotal = grantTotal;
    }

    public String getForeignGrandTotal() {
        if(!this.isDbRetrieval()){
            foreignGrandTotal = this.getInvoicedForeignGrandTotal();
        }
        if (foreignGrandTotal != null &&  !foreignGrandTotal.contains("(")
                && new KualiDecimal(foreignGrandTotal).isLessThan(KualiDecimal.ZERO)) {
            foreignGrandTotal = foreignGrandTotal.replace("-", "");
            foreignGrandTotal = "(" + foreignGrandTotal + ")";
        }
        return foreignGrandTotal;
    }

    public void setForeignGrandTotal(String foreignGrandTotal) {
        this.foreignGrandTotal = foreignGrandTotal;
    }

    public String getInvoicedGrandTotal() {
        KualiDecimal total = getInvoicedTotalWithAllItems(true, this.getItems());
      /*  if (this.isItemSign()) {
            if (total.isLessThan(KualiDecimal.ZERO)) {
                total = total;
            }
        } */
        return total != null ? total.toString() : "0";
    }

    public void setInvoicedGrandTotal(String invoicedGrandTotal) {
        this.invoicedGrandTotal = invoicedGrandTotal;
    }

    public String getInvoicedForeignGrandTotal() {
        KualiDecimal total = getInvoicedForeignTotalWithAllItems(true, this.getItems());
       /* if (this.isItemSign()) {
            if (total.isLessThan(KualiDecimal.ZERO)) {
                total = total;
            }
        } */
        for (OleInvoiceItem item : (List<OleInvoiceItem>)this.getItems()) {
            if (!item.getItemType().getItemTypeCode().equalsIgnoreCase(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE)
                    && item.getAdditionalForeignUnitCost() != null) {
                total = total.add(new KualiDecimal(item.getAdditionalForeignUnitCost()));
            }
            else if (!item.getItemType().getItemTypeCode().equalsIgnoreCase(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE)
                    && item.getItemForeignUnitCost() != null) {
                total = total.add(item.getItemForeignUnitCost());
            }
        }

        return total != null ? total.toString() : "0";
    }
    public void setInvoicedForeignGrandTotal(String invoicedForeignGrandTotal) {
        this.invoicedForeignGrandTotal = invoicedForeignGrandTotal;
    }

    public boolean isValidationFlag() {
        return validationFlag;
    }

    public void setValidationFlag(boolean validationFlag) {
        this.validationFlag = validationFlag;
    }

    public boolean isBlanketApproveValidationFlag() {
        return blanketApproveValidationFlag;
    }

    public void setBlanketApproveValidationFlag(boolean blanketApproveValidationFlag) {
        this.blanketApproveValidationFlag = blanketApproveValidationFlag;
    }

    public String getPurchaseOrderDocumentNums() {
        return purchaseOrderDocumentNums;
    }

    public void setPurchaseOrderDocumentNums(String purchaseOrderDocumentNums) {
        this.purchaseOrderDocumentNums = purchaseOrderDocumentNums;
    }

    /*public boolean isInvoiceModified() {
        return invoiceModified;
    }

    public void setInvoiceModified(boolean invoiceModified) {
        this.invoiceModified = invoiceModified;
    }*/

    public boolean isOverviewFlag() {
        return overviewFlag;
    }

    public void setOverviewFlag(boolean overviewFlag) {
        this.overviewFlag = overviewFlag;
    }

    public boolean isVendorInfoFlag() {
        return vendorInfoFlag;
    }

    public void setVendorInfoFlag(boolean vendorInfoFlag) {
        this.vendorInfoFlag = vendorInfoFlag;
    }

    public boolean isInvoiceInfoFlag() {
        return invoiceInfoFlag;
    }

    public void setInvoiceInfoFlag(boolean invoiceInfoFlag) {
        this.invoiceInfoFlag = invoiceInfoFlag;
    }

    public boolean isProcessItemFlag() {
        return processItemFlag;
    }

    public void setProcessItemFlag(boolean processItemFlag) {
        this.processItemFlag = processItemFlag;
    }

    public boolean isProcessTitlesFlag() {
        return processTitlesFlag;
    }

    public void setProcessTitlesFlag(boolean processTitlesFlag) {
        this.processTitlesFlag = processTitlesFlag;
    }

    public boolean isCurrentItemsFlag() {
        return currentItemsFlag;
    }

    public void setCurrentItemsFlag(boolean currentItemsFlag) {
        this.currentItemsFlag = currentItemsFlag;
    }

    public boolean isAdditionalChargesFlag() {
        return additionalChargesFlag;
    }

    public void setAdditionalChargesFlag(boolean additionalChargesFlag) {
        this.additionalChargesFlag = additionalChargesFlag;
    }

    public boolean isAccountSummaryFlag() {
        return accountSummaryFlag;
    }

    public void setAccountSummaryFlag(boolean accountSummaryFlag) {
        this.accountSummaryFlag = accountSummaryFlag;
    }

    public boolean isNotesAndAttachmentFlag() {
        return notesAndAttachmentFlag;
    }

    public void setNotesAndAttachmentFlag(boolean notesAndAttachmentFlag) {
        this.notesAndAttachmentFlag = notesAndAttachmentFlag;
    }

    public boolean isAdHocRecipientsFlag() {
        return adHocRecipientsFlag;
    }

    public void setAdHocRecipientsFlag(boolean adHocRecipientsFlag) {
        this.adHocRecipientsFlag = adHocRecipientsFlag;
    }

    public boolean isRouteLogFlag() {
        return routeLogFlag;
    }

    public void setRouteLogFlag(boolean routeLogFlag) {
        this.routeLogFlag = routeLogFlag;
    }

    public boolean isRouteLogDisplayFlag() {
        return routeLogDisplayFlag;
    }

    public void setRouteLogDisplayFlag(boolean routeLogDisplayFlag) {
        this.routeLogDisplayFlag = routeLogDisplayFlag;
    }


    /**
     * Performs logic needed to copy Invoice Document.
     */
    @Override
    public void toCopy() throws WorkflowException, ValidationException {
        super.toCopy();
        String[] collapseSections = getOleInvoiceService().getDefaultCollapseSections();
        this.setOverviewFlag(getOleInvoiceService().canCollapse(OLEConstants.OVERVIEW_SECTION, collapseSections));
        this.setVendorInfoFlag(getOleInvoiceService().canCollapse(OLEConstants.VENDOR_INFO_SECTION, collapseSections));
        this.setInvoiceInfoFlag(getOleInvoiceService().canCollapse(OLEConstants.INVOICE_INFO_SECTION, collapseSections));
        this.setProcessTitlesFlag(getOleInvoiceService().canCollapse(OLEConstants.PROCESS_TITLES_SECTION, collapseSections));
        this.setCurrentItemsFlag(getOleInvoiceService().canCollapse(OLEConstants.CURRENT_ITEM_SECTION, collapseSections));
        this.setAdditionalChargesFlag(getOleInvoiceService().canCollapse(OLEConstants.ADDITIONAL_CHARGES_SECTION, collapseSections));
        this.setAccountSummaryFlag(getOleInvoiceService().canCollapse(OLEConstants.ACCOUNT_SUMMARY_SECTION, collapseSections));
        this.setAdHocRecipientsFlag(getOleInvoiceService().canCollapse(OLEConstants.ADHOC_RECIPIENT_SECTION, collapseSections));
        this.setRouteLogFlag(getOleInvoiceService().canCollapse(OLEConstants.ROUTE_LOG_SECTION, collapseSections));
        this.setNotesAndAttachmentFlag(getOleInvoiceService().canCollapse(OLEConstants.NOTES_AND_ATTACH_SECTION, collapseSections));
        // Clear related views
        this.setAccountsPayablePurchasingDocumentLinkIdentifier(null);
       // this.setRelatedViews(null);
        this.setInvoiceNumber("");
        this.setNoteLine1Text("");

        Person currentUser = GlobalVariables.getUserSession().getPerson();
        this.setPurapDocumentIdentifier(null);

        // Set req status to INPR.
        //for app doc status
        updateAndSaveAppDocStatus(InvoiceStatuses.APPDOC_INITIATE);
        this.setPostingYear(SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear());

        for (Iterator iter = this.getItems().iterator(); iter.hasNext(); ) {
            OleInvoiceItem item = (OleInvoiceItem) iter.next();
            item.setPurapDocumentIdentifier(null);
            item.setItemIdentifier(null);
            if (this.getCloneDebitInvoice().equalsIgnoreCase("true")) {
                item.setDebitItem(true);
                item.setInvoiceListPrice(item.getItemListPrice().abs().toString());
            } else {
                item.setDebitItem(false);
                item.setInvoiceListPrice(item.getItemListPrice().negated().toString());
            }
            for (Iterator acctIter = item.getSourceAccountingLines().iterator(); acctIter.hasNext(); ) {
                InvoiceAccount account = (InvoiceAccount) acctIter.next();
                account.setAccountIdentifier(null);
                account.setItemIdentifier(null);
                account.setObjectId(null);
                account.setVersionNumber(null);
            }
            item.setPaidCopies(new ArrayList<OLEPaidCopy>());
        }
        SpringContext.getBean(PurapService.class).addBelowLineItems(this);
        this.refreshNonUpdateableReferences();
    }

    /**
     * Checks whether copying of this document should be allowed. Copying is not allowed if this is a B2B requistion, and more than
     * a set number of days have passed since the document's creation.
     *
     * @return True if copying of this requisition is allowed.
     */
    @Override
    public boolean getAllowsCopy() {
        //boolean allowsCopy = super.getAllowsCopy();
        boolean allowsCopy = true;
        return allowsCopy;
    }

    /**
     * This method returns the duplicateFlag indicator
     *
     * @return duplicateFlag
     */
    public boolean isDuplicateFlag() {
        return duplicateFlag;
    }

    /**
     * This method sets the duplicateFlag
     *
     * @param duplicateFlag
     */
    public void setDuplicateFlag(boolean duplicateFlag) {
        this.duplicateFlag = duplicateFlag;
    }

    public String getSearchResultInvoiceDate() {
        return searchResultInvoiceDate;
    }

    public void setSearchResultInvoiceDate(String searchResultInvoiceDate) {
        this.searchResultInvoiceDate = searchResultInvoiceDate;
    }

    public String getSearchResultInvoicePayDate() {
        return searchResultInvoicePayDate;
    }

    public void setSearchResultInvoicePayDate(String searchResultInvoicePayDate) {
        this.searchResultInvoicePayDate = searchResultInvoicePayDate;
    }

    public boolean isDuplicateApproveFlag() {
        return duplicateApproveFlag;
    }

    public void setDuplicateApproveFlag(boolean duplicateApproveFlag) {
        this.duplicateApproveFlag = duplicateApproveFlag;
    }

    public String getCloneDebitInvoice() {
        return cloneDebitInvoice;
    }

    public void setCloneDebitInvoice(String cloneDebitInvoice) {
        this.cloneDebitInvoice = cloneDebitInvoice;
    }

    public boolean isCloneFlag() {
        return cloneFlag;
    }

    public void setCloneFlag(boolean cloneFlag) {
        this.cloneFlag = cloneFlag;
    }

    public List<OlePaymentRequestDocument> getPaymentRequestDocuments() {
        return paymentRequestDocuments;
    }

    public void setPaymentRequestDocuments(List<OlePaymentRequestDocument> paymentRequestDocuments) {
        this.paymentRequestDocuments = paymentRequestDocuments;
    }

    public boolean isOffsetAccountIndicator() {
        return offsetAccountIndicator;
    }

    public void setOffsetAccountIndicator(boolean offsetAccountIndicator) {
        this.offsetAccountIndicator = offsetAccountIndicator;
    }

    public boolean isItemSign() {
        return itemSign;
    }

    public void setItemSign(boolean itemSign) {
        this.itemSign = itemSign;
    }

    public boolean isBlanketApproveSubscriptionDateValidationFlag() {
        return blanketApproveSubscriptionDateValidationFlag;
    }

    public void setBlanketApproveSubscriptionDateValidationFlag(boolean blanketApproveSubscriptionDateValidationFlag) {
        this.blanketApproveSubscriptionDateValidationFlag = blanketApproveSubscriptionDateValidationFlag;
    }

    public boolean isSubscriptionDateValidationFlag() {
        return subscriptionDateValidationFlag;
    }

    public void setSubscriptionDateValidationFlag(boolean subscriptionDateValidationFlag) {
        this.subscriptionDateValidationFlag = subscriptionDateValidationFlag;
    }

    public String getVendorLink() {
        String oleurl = ConfigContext.getCurrentContextConfig().getProperty("ole.url");
        String url = oleurl+ OLEConstants.VENDOR_LINK +vendorHeaderGeneratedIdentifier + "&amp;vendorDetailAssignedIdentifier="
                +vendorDetailAssignedIdentifier;
        return url;
    }

    public void setVendorLink(String vendorLink) {
        this.vendorLink = vendorLink;
    }

    public boolean isForeignCurrencyFlag() {
        return foreignCurrencyFlag;
    }

    public void setForeignCurrencyFlag(boolean foreignCurrencyFlag) {
        this.foreignCurrencyFlag = foreignCurrencyFlag;
    }

    public Long getInvoiceCurrencyTypeId() {
        return invoiceCurrencyTypeId;
    }

    public void setInvoiceCurrencyTypeId(Long invoiceCurrencyTypeId) {
        this.invoiceCurrencyTypeId = invoiceCurrencyTypeId;
    }

    public String getInvoiceCurrencyType() {
        return invoiceCurrencyType;
    }

    public void setInvoiceCurrencyType(String invoiceCurrencyType) {
        this.invoiceCurrencyType = invoiceCurrencyType;
    }

    public OleCurrencyType getOleCurrencyType() {
        return oleCurrencyType;
    }

    public void setOleCurrencyType(OleCurrencyType oleCurrencyType) {
        this.oleCurrencyType = oleCurrencyType;
    }

    public boolean isCurrencyOverrideFlag() {
        return currencyOverrideFlag;
    }

    public void setCurrencyOverrideFlag(boolean currencyOverrideFlag) {
        this.currencyOverrideFlag = currencyOverrideFlag;
    }

    public String getCurrencyOverrideMessage() {
        return currencyOverrideMessage;
    }

    public void setCurrencyOverrideMessage(String currencyOverrideMessage) {
        this.currencyOverrideMessage = currencyOverrideMessage;
    }

    public String getInvoiceCurrencyExchangeRate() {
        if (invoiceCurrencyExchangeRate != null && !invoiceCurrencyExchangeRate.isEmpty()) {
            if (invoiceCurrencyExchangeRate != null && invoiceCurrencyExchangeRate.contains("-")) {
                invoiceCurrencyExchangeRate = invoiceCurrencyExchangeRate.replace("-", "");
                invoiceCurrencyExchangeRate = "(" + invoiceCurrencyExchangeRate + ")";
            }
            return invoiceCurrencyExchangeRate;
        }
        return invoiceCurrencyExchangeRate;
    }

    public void setInvoiceCurrencyExchangeRate(String invoiceCurrencyExchangeRate) {
        this.invoiceCurrencyExchangeRate = invoiceCurrencyExchangeRate;
        if (invoiceCurrencyExchangeRate != null) {
            if (invoiceCurrencyExchangeRate.contains("(") || invoiceCurrencyExchangeRate.contains(")")) {
                invoiceCurrencyExchangeRate = invoiceCurrencyExchangeRate.replace("(", "");
                invoiceCurrencyExchangeRate = invoiceCurrencyExchangeRate.replace(")", "");
                invoiceCurrencyExchangeRate = "-" + invoiceCurrencyExchangeRate;
                this.invoiceCurrencyExchangeRate = invoiceCurrencyExchangeRate;
            }
            if (new KualiDecimal(invoiceCurrencyExchangeRate).isLessThan(KualiDecimal.ZERO)) {
                invoiceCurrencyExchangeRate = invoiceCurrencyExchangeRate.replace("-", "");
                invoiceCurrencyExchangeRate = "(" + invoiceCurrencyExchangeRate + ")";
                this.invoiceCurrencyExchangeRate = invoiceCurrencyExchangeRate;
            }
        }
    }

    public boolean isDbRetrieval() {
        return dbRetrieval;
    }

    public void setDbRetrieval(boolean dbRetrieval) {
        this.dbRetrieval = dbRetrieval;
    }

    public boolean isEnableCurrentItems() {
        return enableCurrentItems;
    }

    public void setEnableCurrentItems(boolean enableCurrentItems) {
        this.enableCurrentItems = enableCurrentItems;
    }

    public List<OleInvoiceItem> getDeletedInvoiceItems() {
        return deletedInvoiceItems;
    }

    public void setDeletedInvoiceItems(List<OleInvoiceItem> deletedInvoiceItems) {
        this.deletedInvoiceItems = deletedInvoiceItems;
    }

    public boolean isDuplicateRouteFlag() {
        return duplicateRouteFlag;
    }

    public void setDuplicateRouteFlag(boolean duplicateRouteFlag) {
        this.duplicateRouteFlag = duplicateRouteFlag;
    }

    public boolean isDuplicateSaveFlag() {
        return duplicateSaveFlag;
    }

    public void setDuplicateSaveFlag(boolean duplicateSaveFlag) {
        this.duplicateSaveFlag = duplicateSaveFlag;
    }

    public String getCurrencyFormat() {
        if(org.apache.commons.lang3.StringUtils.isBlank(currencyFormat)){
            currencyFormat = CurrencyFormatter.getSymbolForCurrencyPattern();
        }
        return currencyFormat;
    }

    public Integer getLineOrderSequenceNumber() {
        return lineOrderSequenceNumber;
    }

    public void setLineOrderSequenceNumber(Integer lineOrderSequenceNumber) {
        this.lineOrderSequenceNumber = lineOrderSequenceNumber;
    }

    public Integer getIndexNumberFromJsonObject(String sequenceObject) {
        Integer returnValue = null;
        try {
            JSONObject jsonObject = new JSONObject(sequenceObject);
            returnValue = jsonObject.getInt(OLEConstants.INDEX_NBR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnValue;
    }
	
	public String getVendorAlias() {
        return vendorAlias;
    }

    public void setVendorAlias(String vendorAlias) {
        this.vendorAlias = vendorAlias;
    }

    public boolean isDuplicateValidationFlag() {
        return duplicateValidationFlag;
    }

    public void setDuplicateValidationFlag(boolean duplicateValidationFlag) {
        this.duplicateValidationFlag = duplicateValidationFlag;
    }

    public String getInvoiceInfo() {
        if(StringUtils.isNotEmpty(invoiceNumber)){
            invoiceInfo = "";
            invoiceInfo = "Invoice (" + invoiceNumber + ")";
        }else{
            invoiceInfo = "Invoice";
        }
        return invoiceInfo;
    }

    public void setInvoiceInfo(String invoiceInfo) {
        this.invoiceInfo = invoiceInfo;
    }

    public boolean isCurrencyTypeIndicator() {
        return currencyTypeIndicator;
    }

    public void setCurrencyTypeIndicator(boolean currencyTypeIndicator) {
        this.currencyTypeIndicator = currencyTypeIndicator;
    }

    public void setCurrencyFormat(String currencyFormat) {
        this.currencyFormat = currencyFormat;
    }

    public boolean isAddImmediately() {
        return addImmediately;
    }

    public void setAddImmediately(boolean addImmediately) {
        this.addImmediately = addImmediately;
    }

    @Override
    public KualiDecimal getTotalDollarAmount() {
        // return total without inactive and with below the line
        return getTotalDollarAmount(false, true);
    }

    public KualiDecimal getTotalDollarAmount(boolean includeInactive, boolean includeBelowTheLine) {
        KualiDecimal total = new KualiDecimal(BigDecimal.ZERO);
        for (OleInvoiceItem item : (List<OleInvoiceItem>) getItems()) {

            if (item.getPurapDocument() == null) {
                item.setPurapDocument(this);
            }
            ItemType it = item.getItemType();
            if ((includeBelowTheLine) && (includeInactive || PurApItemUtils.checkItemActive(item))) {
                KualiDecimal totalAmount = item.getTotalAmount();
                KualiDecimal itemTotal = (totalAmount != null) ? totalAmount : KualiDecimal.ZERO;
                if(item.isDebitItem()) {
                    total = total.add(itemTotal);
                }
                else {
                    total = total.subtract(itemTotal);
                }
            }
        }
        return total;
    }
}
