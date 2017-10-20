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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.common.client.DocstoreClient;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.client.DocstoreClientLocatorService;
import org.kuali.ole.docstore.common.client.DocstoreRestClient;
import org.kuali.ole.docstore.common.client.impl.DocstoreClientLocatorServiceImpl;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.BibMarc;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.gl.service.SufficientFundsService;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapConstants.PaymentRequestStatuses;
import org.kuali.ole.module.purap.PurapParameterConstants;
import org.kuali.ole.module.purap.PurapWorkflowConstants;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.module.purap.document.PaymentRequestDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.service.AccountsPayableService;
import org.kuali.ole.module.purap.document.service.PaymentRequestService;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.module.purap.service.PurapAccountingService;
import org.kuali.ole.module.purap.util.ExpiredOrClosedAccountEntry;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.service.OleInvoiceService;
import org.kuali.ole.select.document.service.OlePaymentRequestService;
import org.kuali.ole.select.document.service.OlePurchaseOrderDocumentHelperService;
import org.kuali.ole.select.document.service.OleRequisitionDocumentService;
import org.kuali.ole.select.document.service.impl.OlePaymentRequestFundCheckServiceImpl;
import org.kuali.ole.select.service.BibInfoService;
import org.kuali.ole.select.service.BibInfoWrapperService;
import org.kuali.ole.select.service.FileProcessingService;
import org.kuali.ole.select.service.impl.BibInfoServiceImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.*;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.GeneralLedgerPendingEntryService;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.ole.vnd.VendorConstants;
import org.kuali.ole.vnd.businessobject.OleExchangeRate;
import org.kuali.ole.vnd.businessobject.VendorAddress;
import org.kuali.ole.vnd.businessobject.VendorAlias;
import org.kuali.ole.vnd.document.service.VendorService;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
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
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.workflow.service.WorkflowDocumentService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


/**
 * This class is the document class for Ole Payment Request
 */

public class OlePaymentRequestDocument extends PaymentRequestDocument {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePaymentRequestDocument.class);

    private Integer invoiceTypeId;
    private Integer invoiceSubTypeId;

    // NOT PERSISTED IN DB
    private String invoiceType;
    private String invoiceSubType;

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
    private Integer paymentMethodId;
    private OlePaymentMethod paymentMethod;
    private String vendorAliasName;

    private boolean currencyTypeIndicator= true;

    public String getVendorAliasName() {
        return vendorAliasName;
    }

    public void setVendorAliasName(String vendorAliasName) {
        this.vendorAliasName = vendorAliasName;
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
    public void setOrderType(PurchaseOrderType orderType) {
        this.orderType = orderType;
    }

    public Integer getInvoiceTypeId() {
        return invoiceTypeId;
    }

    public void setInvoiceTypeId(Integer invoiceTypeId) {
        this.invoiceTypeId = invoiceTypeId;
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

    public boolean isNoProrate() {
        return noProrate;
    }

    public void setNoProrate(boolean noProrate) {
        this.noProrate = noProrate;
    }

    private static transient ConfigurationService kualiConfigurationService;
    private static transient BibInfoWrapperService bibInfoWrapperService;
    private static transient FileProcessingService fileProcessingService;
    private static transient BusinessObjectService businessObjectService;
    private static transient VendorService vendorService;
    private static transient PaymentRequestService paymentRequestService;
    private static transient OlePaymentRequestService olePaymentRequestService;
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
        OlePaymentRequestDocument.workflowDocumentService = workflowDocumentService;
    }

    public static PaymentRequestService getPaymentRequestService() {
        if (paymentRequestService == null) {
            paymentRequestService = SpringContext.getBean(PaymentRequestService.class);
        }
        return paymentRequestService;
    }

    public static void setPaymentRequestService(PaymentRequestService paymentRequestService) {
        OlePaymentRequestDocument.paymentRequestService = paymentRequestService;
    }

    public static VendorService getVendorService() {
        if (vendorService == null) {
            vendorService = SpringContext.getBean(VendorService.class);
        }
        return vendorService;
    }

    public static void setVendorService(VendorService vendorService) {
        OlePaymentRequestDocument.vendorService = vendorService;
    }

    public static PurapService getPurapService() {
        if (purapService == null) {
            purapService = SpringContext.getBean(PurapService.class);
        }
        return purapService;
    }

    public static void setPurapService(PurapService purapService) {
        OlePaymentRequestDocument.purapService = purapService;
    }

    public static OlePaymentRequestService getOlePaymentRequestService() {
        if (olePaymentRequestService == null) {
            olePaymentRequestService = SpringContext.getBean(OlePaymentRequestService.class);
        }
        return olePaymentRequestService;
    }

    public static void setOlePaymentRequestService(OlePaymentRequestService olePaymentRequestService) {
        OlePaymentRequestDocument.olePaymentRequestService = olePaymentRequestService;
    }

    public static IdentityManagementService getIdentityManagementService() {
        if (identityManagementService == null) {
            identityManagementService = SpringContext.getBean(IdentityManagementService.class);
        }
        return identityManagementService;
    }

    public static void setIdentityManagementService(IdentityManagementService identityManagementService) {
        OlePaymentRequestDocument.identityManagementService = identityManagementService;
    }

    public static AccountsPayableService getAccountsPayableService() {
        if (accountsPayableService == null) {
            accountsPayableService = SpringContext.getBean(AccountsPayableService.class);
        }
        return accountsPayableService;
    }

    public static void setAccountsPayableService(AccountsPayableService accountsPayableService) {
        OlePaymentRequestDocument.accountsPayableService = accountsPayableService;
    }

    public static ConfigurationService getConfigurationService() {
        if (kualiConfigurationService == null) {
            kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
        }
        return kualiConfigurationService;
    }

    public static void setConfigurationService(ConfigurationService kualiConfigurationService) {
        OlePaymentRequestDocument.kualiConfigurationService = kualiConfigurationService;
    }

    public static BibInfoWrapperService getBibInfoWrapperService() {
        if (bibInfoWrapperService == null) {
            bibInfoWrapperService = SpringContext.getBean(BibInfoWrapperService.class);
        }
        return bibInfoWrapperService;
    }

    public static void setBibInfoWrapperService(BibInfoWrapperService bibInfoWrapperService) {
        OlePaymentRequestDocument.bibInfoWrapperService = bibInfoWrapperService;
    }


    public static FileProcessingService getFileProcessingService() {
        if (fileProcessingService == null) {
            fileProcessingService = SpringContext.getBean(FileProcessingService.class);
        }
        return fileProcessingService;
    }

    public static void setFileProcessingService(FileProcessingService fileProcessingService) {
        OlePaymentRequestDocument.fileProcessingService = fileProcessingService;
    }

    public static DateTimeService getDateTimeService() {
        if (dateTimeService == null) {
            dateTimeService = SpringContext.getBean(DateTimeService.class);
        }
        return dateTimeService;
    }

    public static void setDateTimeService(DateTimeService dateTimeService) {
        OlePaymentRequestDocument.dateTimeService = dateTimeService;
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
    public OlePaymentRequestDocument() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * This method is overridden to populate Ole PaymentRequestDocument from PurchaseOrder Document
     *
     * @see org.kuali.ole.module.purap.document.PaymentRequestDocument#populatePaymentRequestFromPurchaseOrder(org.kuali.ole.module.purap.document.PurchaseOrderDocument, java.util.HashMap)
     */
    @Override
    public void populatePaymentRequestFromPurchaseOrder(PurchaseOrderDocument po, HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList) {
        LOG.debug("Inside populatePaymentRequestFromPurchaseOrder method of OlePaymentRequest Document");
        this.setPaymentMethodId(po.getVendorDetail().getPaymentMethodId());
        this.setPurchaseOrderIdentifier(po.getPurapDocumentIdentifier());
        this.getDocumentHeader().setOrganizationDocumentNumber(po.getDocumentHeader().getOrganizationDocumentNumber());
        this.setPostingYear(po.getPostingYear());
        this.setReceivingDocumentRequiredIndicator(po.isReceivingDocumentRequiredIndicator());
        this.setUseTaxIndicator(po.isUseTaxIndicator());
        this.setPaymentRequestPositiveApprovalIndicator(po.isPaymentRequestPositiveApprovalIndicator());
        this.setVendorCustomerNumber(po.getVendorCustomerNumber());

        if (po.getPurchaseOrderCostSource() != null) {
            this.setPaymentRequestCostSource(po.getPurchaseOrderCostSource());
            this.setPaymentRequestCostSourceCode(po.getPurchaseOrderCostSourceCode());
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

        this.setVendorHeaderGeneratedIdentifier(po.getVendorHeaderGeneratedIdentifier());
        this.setVendorDetailAssignedIdentifier(po.getVendorDetailAssignedIdentifier());
        this.setVendorCustomerNumber(po.getVendorCustomerNumber());
        this.setVendorName(po.getVendorName());
        /*this.setVendorAliasName(((OlePurchaseOrderDocument) po).getVendorAliasName());*/

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

        this.setPaymentRequestPayDate(getPaymentRequestService().calculatePayDate(this.getInvoiceDate(), this.getVendorPaymentTerms()));

        if (getPaymentRequestService().encumberedItemExistsForInvoicing(po)) {
            for (OlePurchaseOrderItem poi : (List<OlePurchaseOrderItem>) po.getItems()) {
                // check to make sure it's eligible for payment (i.e. active and has encumbrance available
                if (getDocumentSpecificService().poItemEligibleForAp(this, poi)) {
                    OlePaymentRequestItem paymentRequestItem = new OlePaymentRequestItem(poi, this, expiredOrClosedAccountList);
                    this.getItems().add(paymentRequestItem);
                    PurchasingCapitalAssetItem purchasingCAMSItem = po.getPurchasingCapitalAssetItemByItemIdentifier(poi.getItemIdentifier());
                    if (purchasingCAMSItem != null) {
                        paymentRequestItem.setCapitalAssetTransactionTypeCode(purchasingCAMSItem.getCapitalAssetTransactionTypeCode());
                    }

                    /*
                    // copy usetaxitems over
                    paymentRequestItem.getUseTaxItems().clear();
                    for (PurApItemUseTax useTax : poi.getUseTaxItems()) {
                        paymentRequestItem.getUseTaxItems().add(useTax);
                    }
                    */
                }
            }
        }

        // add missing below the line
        getPurapService().addBelowLineItems(this);
        this.setAccountsPayablePurchasingDocumentLinkIdentifier(po.getAccountsPayablePurchasingDocumentLinkIdentifier());

        //fix up below the line items
        getPaymentRequestService().removeIneligibleAdditionalCharges(this);

        this.fixItemReferences();
        this.refreshNonUpdateableReferences();
    }

    @Override
    public Class getItemClass() {
        return OlePaymentRequestItem.class;
    }

    @Override
    public PurApAccountingLine getFirstAccount() {
        // loop through items, and pick the first item
        if ((getItems() != null) && (!getItems().isEmpty())) {
            OlePaymentRequestItem itemToUse = null;
            for (Iterator iter = getItems().iterator(); iter.hasNext(); ) {
                OlePaymentRequestItem item = (OlePaymentRequestItem) iter.next();
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
               PaymentRequestAccount accountLine = (PaymentRequestAccount)accts.get(0);
                    return accountLine.getFinancialChartOfAccountsCode() + "-" + accountLine.getAccountNumber();
                }
                */
            }
        }
        return null;

    }

    @Override
    public PurApItem getItem(int pos) {
        OlePaymentRequestItem item = (OlePaymentRequestItem) super.getItem(pos);
        if (item.getPaymentRequest() == null) {
            item.setPaymentRequest(this);
        }
        return item;
    }

    @Override
    public void processAfterRetrieve() {
        super.processAfterRetrieve();
        try {
            LOG.debug("###########inside OlePaymentRequestDocument processAfterRetrieve###########");
            if (this.getVendorAliasName() == null) {
                populateVendorAliasName();
            }
            if (this.getPaymentMethodId() != null) {
                OlePaymentMethod olePaymentMethod = SpringContext.getBean(BusinessObjectService.class)
                        .findBySinglePrimaryKey(OlePaymentMethod.class, this.getPaymentMethodId());
                this.setPaymentMethod(olePaymentMethod);
            }
            List<BigDecimal> newUnitPriceList = new ArrayList<BigDecimal>();
            BigDecimal newUnitPrice = new BigDecimal(0);
            BigDecimal hundred = new BigDecimal(100);
            List<OlePaymentRequestItem> item = this.getItems();

            for (int i = 0; item.size() > i; i++) {
                OlePaymentRequestItem items = (OlePaymentRequestItem) this.getItem(i);
                if ((items.getItemType().isQuantityBasedGeneralLedgerIndicator())) {
                    if (items.getItemDiscount() == null) {
                        items.setItemDiscount(KualiDecimal.ZERO);
                    }

                    if (items.getItemListPrice() == null) {
                        items.setItemListPrice(KualiDecimal.ZERO);
                    }

                    if (items.getItemDiscountType() != null && items.getItemDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                        newUnitPrice = (hundred.subtract(items.getItemDiscount().bigDecimalValue())).divide(hundred).multiply(items.getItemListPrice().bigDecimalValue());
                    } else {
                        newUnitPrice = items.getItemListPrice().bigDecimalValue().subtract(items.getItemDiscount().bigDecimalValue());
                    }
                    items.setItemSurcharge(items.getItemUnitPrice().subtract(newUnitPrice).setScale(4, RoundingMode.HALF_UP));
                }
            }
            if (this.getVendorDetail().getCurrencyType()!=null){
                if(this.getVendorDetail().getCurrencyType().getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
                    currencyTypeIndicator=true;
                }
                else{
                    currencyTypeIndicator=false;
                }
            }

            OleInvoiceDocument oleInvoiceDocument = SpringContext.getBean(BusinessObjectService.class)
                    .findBySinglePrimaryKey(OleInvoiceDocument.class, this.getInvoiceIdentifier());

            if(oleInvoiceDocument.getInvoiceCurrencyTypeId() != null && oleInvoiceDocument.getForeignVendorInvoiceAmount() != null && oleInvoiceDocument.getInvoiceCurrencyExchangeRate() != null) {
                        this.setForeignVendorInvoiceAmount(this.getVendorInvoiceAmount().bigDecimalValue().multiply(new BigDecimal(oleInvoiceDocument.getInvoiceCurrencyExchangeRate())));
            }
            else {
                if (this.getVendorDetail() != null && (!currencyTypeIndicator)) {
                    Long currencyTypeId = this.getVendorDetail().getCurrencyType().getCurrencyTypeId();
                    Map documentNumberMap = new HashMap();
                    documentNumberMap.put(OleSelectConstant.CURRENCY_TYPE_ID, currencyTypeId);
                    List<OleExchangeRate> exchangeRateList = (List) getBusinessObjectService().findMatchingOrderBy(OleExchangeRate.class, documentNumberMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
                    Iterator iterator = exchangeRateList.iterator();
                    for (OlePaymentRequestItem items : item) {
                        iterator = exchangeRateList.iterator();
                        if (iterator.hasNext()) {
                            OleExchangeRate tempOleExchangeRate = (OleExchangeRate) iterator.next();
                            items.setItemExchangeRate(tempOleExchangeRate.getExchangeRate());
                            this.setForeignVendorInvoiceAmount(this.getVendorInvoiceAmount().bigDecimalValue().multiply(tempOleExchangeRate.getExchangeRate()));
                        }
                    }
                }

            }
            String itemDescription = "";
            for (OlePaymentRequestItem singleItem : item) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Title id while retriving ------>" + singleItem.getItemTitleId());
                }
                if (singleItem.getItemTitleId() != null) {
                    LOG.debug("###########inside processAfterRetrieve ole requisition item###########");
                    Bib bib =  new BibMarc();
                    DocstoreClientLocator docstoreClientLocator=new DocstoreClientLocator();
                  if(singleItem.getItemTitleId()!=null && singleItem.getItemTitleId()!=""){
                        bib= docstoreClientLocator.getDocstoreClient().retrieveBib(singleItem.getItemTitleId());
                        singleItem.setBibUUID(bib.getId());
                        singleItem.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(singleItem.getItemTitleId()));
                  }
                    if(singleItem.getItemUnitPrice()!=null){
                        singleItem.setItemUnitPrice(singleItem.getItemUnitPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    itemDescription = ((bib.getTitle() != null && !bib
                            .getTitle().isEmpty()) ? bib.getTitle().trim() + ", " : "")
                            + ((bib.getAuthor() != null && !bib
                            .getAuthor().isEmpty()) ? bib.getAuthor().trim() + ", "
                            : "")
                            + ((bib.getPublisher() != null && !bib
                            .getPublisher().isEmpty()) ? bib.getPublisher().trim()
                            + ", " : "")
                            + ((bib.getIsbn() != null && !bib.getIsbn()
                            .isEmpty()) ? bib.getIsbn().trim() + ", " : "");
                    if (itemDescription != null && !(itemDescription.equals(""))) {
                        itemDescription = itemDescription.lastIndexOf(",") < 0 ? itemDescription :
                                itemDescription.substring(0, itemDescription.lastIndexOf(","));
                        StringEscapeUtils stringEscapeUtils = new StringEscapeUtils();
                        itemDescription = stringEscapeUtils.unescapeXml(itemDescription);
                        singleItem.setItemDescription(itemDescription);
                    }
                    HashMap<String, String> queryMap = new HashMap<String, String>();
                    if (singleItem.getPoItemIdentifier() != null) {
                        queryMap.put(OLEConstants.PO_ITEM_ID, singleItem.getPoItemIdentifier().toString());
                        OleInvoiceItem oleInvoiceItem = getBusinessObjectService().findByPrimaryKey(OleInvoiceItem.class, queryMap);
                        if (oleInvoiceItem != null && oleInvoiceItem.getPoItemIdentifier() != null) {
                            queryMap.clear();
                            queryMap.put(OLEConstants.OleCopy.PO_ITM_ID, oleInvoiceItem.getPoItemIdentifier().toString());
                            List<OLELinkPurapDonor> oleLinkPurapDonorList = (List<OLELinkPurapDonor>) getBusinessObjectService().findMatching(OLELinkPurapDonor.class, queryMap);
                            if (oleLinkPurapDonorList != null) {
                                singleItem.setOleDonors(oleLinkPurapDonorList);
                            }
                        }
                    }
                }
                for(OLEPaidCopy olePaidCopy : singleItem.getPaidCopies()){
                    if(olePaidCopy.getPaymentRequestItemId()==null && olePaidCopy.getPaymentRequestIdentifier()==null){
                        olePaidCopy.setPaymentRequestItemId(singleItem.getItemIdentifier());
                        olePaidCopy.setPaymentRequestIdentifier(this.getPurapDocumentIdentifier());
                        getBusinessObjectService().save(olePaidCopy);
                    }
                }
            }
            if (this.getProrateBy() != null) {
                this.setProrateQty(this.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY));
                this.setProrateManual(this.getProrateBy().equals(OLEConstants.MANUAL_PRORATE));
                this.setProrateDollar(this.getProrateBy().equals(OLEConstants.PRORATE_BY_DOLLAR));
                this.setNoProrate(this.getProrateBy().equals(OLEConstants.NO_PRORATE));
            }
        } catch (Exception e) {
            LOG.error("Exception during processAfterRetrieve() in OlePaymentRequestDocument", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is overrided to create POA from new Line Item of PaymentRequest
     *
     * @see org.kuali.ole.module.purap.document.PaymentRequestDocument#doRouteStatusChange(org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange)
     */
    @Override
    public void doRouteStatusChange(DocumentRouteStatusChange statusChangeEvent) {
        LOG.debug("doRouteStatusChange() started");

        super.doRouteStatusChange(statusChangeEvent);
        try {
            // DOCUMENT PROCESSED
            if (this.getDocumentHeader().getWorkflowDocument().isProcessed()) {
                if (!PaymentRequestStatuses.APPDOC_AUTO_APPROVED.equals(getApplicationDocumentStatus())) {
                    //delete unentered items and update po totals and save po
                    getOlePaymentRequestService().completePaymentDocument(this);
                    if(!(SpringContext.getBean(OlePaymentRequestService.class).getPaymentMethod(this.getPaymentMethodId()).equals(OLEConstants.DEPOSIT)))   {

                        this.setApplicationDocumentStatus(PurapConstants.PaymentRequestStatuses.APPDOC_DEPARTMENT_APPROVED);
                    }
                    else {
                        this.setApplicationDocumentStatus(PaymentRequestStatuses.APPDOC_DO_NOT_EXTRACT);
                    }
                    populateDocumentForRouting();
                    getPurapService().saveDocumentNoValidation(this);
                    return;
                }
            }
            // DOCUMENT DISAPPROVED
            else if (this.getDocumentHeader().getWorkflowDocument().isDisapproved()) {
                // String nodeName =
                // getWorkflowDocumentService().getCurrentRouteLevelName(getDocumentHeader().getWorkflowDocument());
                String nodeName = getDocumentHeader().getWorkflowDocument().getCurrentNodeNames().iterator().next();
                HashMap<String, String> disApprovedStatusMap = PurapConstants.PaymentRequestStatuses
                        .getPaymentRequestAppDocDisapproveStatuses();
                // NodeDetails currentNode = NodeDetailEnum.getNodeDetailEnumByName(nodeName);
                // STATUS_ORDER currentNode = STATUS_ORDER.getByStatusCode(nodeName);
                if (ObjectUtils.isNotNull(nodeName)) {
                    String newStatusCode = disApprovedStatusMap.get(nodeName);
                    // currentNode.getDisapprovedStatusCode();
                    if ((StringUtils.isBlank(newStatusCode))
                            && ((PaymentRequestStatuses.APPDOC_INITIATE.equals(getApplicationDocumentStatus())) || (PaymentRequestStatuses.APPDOC_IN_PROCESS
                            .equals(getApplicationDocumentStatus())))) {
                        newStatusCode = PaymentRequestStatuses.APPDOC_CANCELLED_IN_PROCESS;
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
                HashMap<String, String> disApprovedStatusMap = PurapConstants.PaymentRequestStatuses
                        .getPaymentRequestAppDocDisapproveStatuses();
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
     * @throws WorkflowException
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
     * @throws WorkflowException
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

    public String getBibSearchURL() {
        String bibSearchURL = getConfigurationService().getPropertyValueAsString(OLEConstants.BIBEDITOR_SEARCH_URL_KEY);
        return bibSearchURL;
    }
    /**
     * This method is used to get the dublinedtior edit url from propertie file
     *
     * @return Dublineditor edit url string
     */
    public String getDublinEditorEditURL() {
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getDublinEditorEditURL();

    }

    /**
     * This method is used to get the dublinedtior view url from propertie file
     *
     * @return dublineditor view url string
     */
    public String getDublinEditorViewURL() {
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getDublinEditorViewURL();
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
        super.prepareForSave(event);
        try {
            if (this.proformaIndicator && !this.immediatePaymentIndicator) {
                this.setImmediatePaymentIndicator(true);
            }
            LOG.debug("###########Inside OlePaymentRequestDocument " + "repareForSave###########");
            List<OlePaymentRequestItem> items = new ArrayList<OlePaymentRequestItem>();
            items = this.getItems();
            Iterator iterator = items.iterator();
            HashMap dataMap = new HashMap();
            String titleId;
            while (iterator.hasNext()) {
                LOG.debug("###########inside prepareForSave item loop###########");
                Object object = iterator.next();
                if (object instanceof OlePaymentRequestItem) {
                    LOG.debug("###########inside prepareForSave ole payment request item###########");
                    OlePaymentRequestItem singleItem = (OlePaymentRequestItem) object;
                    setItemDescription(singleItem);
                    for(OLEPaidCopy olePaidCopy : singleItem.getPaidCopies()){
                        if(olePaidCopy.getPaymentRequestItemId()==null && olePaidCopy.getPaymentRequestIdentifier()==null){
                            olePaidCopy.setPaymentRequestItemId(singleItem.getItemIdentifier());
                            olePaidCopy.setPaymentRequestIdentifier(this.getPurapDocumentIdentifier());
                            getBusinessObjectService().save(olePaidCopy);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Exception during prepareForSave() in OlePaymentRequestDocument", e);
            throw new RuntimeException(e);
        }
    }

    @Override
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
    public KualiDecimal getTotalPreTaxDollarAmountAboveLineItems() {
        if ((this.prorateBy != null) && (this.prorateBy.equals(OLEConstants.PRORATE_BY_QTY) || this.prorateBy.equals(OLEConstants.PRORATE_BY_DOLLAR) || this.prorateBy.equals(OLEConstants.MANUAL_PRORATE))) {
            KualiDecimal addChargeItem = KualiDecimal.ZERO;
            KualiDecimal lineItemPreTaxTotal = KualiDecimal.ZERO;
            KualiDecimal prorateSurcharge = KualiDecimal.ZERO;
            List<OlePaymentRequestItem> item = this.getItems();
            for (OlePaymentRequestItem items : item) {
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

    private void setItemDescription(OlePaymentRequestItem singleItem) throws Exception {
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
        if (isReceivingDocumentRequiredIndicator() && !this.proformaIndicator) {
            return !isReceivingRequirementMet();
        }

        //receiving is not required or has already been fulfilled, no need to stop for routing
        return false;
    }

    /**
     * Provides answers to the following splits: PurchaseWasReceived VendorIsEmployeeOrNonResidentAlien
     *
     * @see org.kuali.ole.sys.document.FinancialSystemTransactionalDocumentBase#answerSplitNodeQuestion(java.lang.String)
     */
    @Override
    public boolean answerSplitNodeQuestion(String nodeName) throws UnsupportedOperationException {
        if (nodeName.equals(OLEConstants.HAS_VENDOR_DEPOSIT_ACCOUNT)) {
            return hasVendorDepositAccount();
        }
        else if (nodeName.equals(OLEConstants.OlePaymentRequest.HAS_INVOICE_TYPE)) {
            return hasInvoiceType();
        }
        else if (nodeName.equals(OLEConstants.OlePaymentRequest.HAS_PREPAID_INVOICE_TYPE)) {
            return hasPrepaidInvoiceType();
        }
        else if (nodeName.equals(OLEConstants.OlePaymentRequest.HAS_PAYMENT_METHOD)) {
            return hasPaymentMethod();
        }
        else if (nodeName.equals(PurapWorkflowConstants.BUDGET_REVIEW_REQUIRED)) {
            return isBudgetReviewRequired();
        }
        else if (nodeName.equals(PurapWorkflowConstants.REQUIRES_IMAGE_ATTACHMENT)) {
            return requiresAccountsPayableReviewRouting();
        }
        else if (nodeName.equals(PurapWorkflowConstants.PURCHASE_WAS_RECEIVED)) {
            return shouldWaitForReceiving();
        }
        else if (nodeName.equals(PurapWorkflowConstants.VENDOR_IS_EMPLOYEE_OR_NON_RESIDENT_ALIEN)) {
            return isVendorEmployeeOrNonResidentAlien();
        }
        else if (nodeName.equals(OLEConstants.REQUIRES_SEPARATION_OF_DUTIES)) {
            return isSeparationOfDutiesReviewRequired();
        }

        else if (nodeName.equals(PurapWorkflowConstants.NOTIFY_BUDGET_REVIEW)) {
            return isNotificationRequired();
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
        List<PaymentRequestAccount> sourceAccounts = this.getSourceAccountingLines();
        for (PaymentRequestAccount sourceAccount : sourceAccounts) {
            if (sourceAccount.getAccount().getSubFundGroupCode().equalsIgnoreCase(OLEConstants.CLEARING_ACCOUNT_CODE)) {
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

        OlePaymentRequestFundCheckServiceImpl olePaymentRequestFundCheckServiceImpl = new OlePaymentRequestFundCheckServiceImpl();
        boolean required = false;
        if((SpringContext.getBean(OlePaymentRequestService.class).getPaymentMethod(this.getPaymentMethodId())).equals(OLEConstants.DEPOSIT)) {
            return false;
        }
        // if document's fiscal year is less than or equal to the current fiscal year
        else if (SpringContext.getBean(OleInvoiceService.class).getParameterBoolean(OLEConstants.CoreModuleNamespaces.SELECT, OLEConstants.OperationType.SELECT, PurapParameterConstants.ALLOW_INVOICE_SUFF_FUND_CHECK)) {
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
                    if (oleSufficientFundCheck != null) {
                        String option = oleSufficientFundCheck.getNotificationOption() != null ? oleSufficientFundCheck
                                .getNotificationOption() : "";
                        if (option.equals(OLEPropertyConstants.BUD_REVIEW)) {
                            required = olePaymentRequestFundCheckServiceImpl.hasSufficientFundCheckRequired(accLine);
                            return required;
                        }
                    }
                }
            }
        }
        return required;
        // get list of sufficientfundItems

        // delete and recreate the GL entries for this document so they do not get included in the SF check
        // This is *NOT* ideal. The SF service needs to be updated to allow it to provide the current
        // document number so that it can be exlcuded from pending entry checks.
        // List<GeneralLedgerPendingEntry> pendingEntries = getPendingLedgerEntriesForSufficientFundsChecking();
        // dumb loop to just force OJB to load the objects. Otherwise, the proxy object above
        // only gets resolved *after* the delete below and no SF check happens.
        // for (GeneralLedgerPendingEntry glpe : pendingEntries) {
        // glpe.getChartOfAccountsCode();
        // }
        // SpringContext.getBean(GeneralLedgerPendingEntryService.class).delete(getDocumentNumber());
        // List<SufficientFundsItem> fundsItems = SpringContext.getBean(SufficientFundsService.class)
        // .checkSufficientFundsForPREQ(pendingEntries);
        // SpringContext.getBean(GeneralLedgerPendingEntryService.class).generateGeneralLedgerPendingEntries(this);
        // SpringContext.getBean(BusinessObjectService.class).save(getGeneralLedgerPendingEntries());
        // if (fundsItems.size() > 0) {
        // return true;
        // }
        // }
        //
        // return false;
    }

    private boolean isNotificationRequired() {
        OleRequisitionDocumentService oleRequisitionDocumentService = (OleRequisitionDocumentService) SpringContext
                .getBean("oleRequisitionDocumentService");
        List<SourceAccountingLine> sourceAccountingLineList = this.getSourceAccountingLines();
        boolean sufficientFundCheck = false;
        if((SpringContext.getBean(OlePaymentRequestService.class).getPaymentMethod(this.getPaymentMethodId())).equals(OLEConstants.DEPOSIT)) {
            return false;
        }
        else if (SpringContext.getBean(OleInvoiceService.class).getParameterBoolean(OLEConstants.CoreModuleNamespaces.SELECT, OLEConstants.OperationType.SELECT, PurapParameterConstants.ALLOW_INVOICE_SUFF_FUND_CHECK)) {
            for (SourceAccountingLine accLine : sourceAccountingLineList) {
                Map searchMap = new HashMap();
                String notificationOption = null;
                Map<String, Object> key = new HashMap<String, Object>();
                String chartCode = accLine.getChartOfAccountsCode();
                String accNo = accLine.getAccountNumber();
                String objectCd = accLine.getFinancialObjectCode();
                key.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
                key.put(OLEPropertyConstants.ACCOUNT_NUMBER, accNo);
                OleSufficientFundCheck account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(
                        OleSufficientFundCheck.class, key);
                if (account != null) {
                    notificationOption = account.getNotificationOption();
                }
                if (notificationOption != null && notificationOption.equals(OLEPropertyConstants.NOTIFICATION)) {
                    sufficientFundCheck = true;
                }
            }
        }
        return sufficientFundCheck;
    }

    @Override
    protected void populateAccountsForRouting() {

        // if(this.getDocumentHeader().ge)
        List<SufficientFundsItem> fundsItems = new ArrayList<SufficientFundsItem>();
        try {
            // String nodeName =
            // SpringContext.getBean(WorkflowDocumentService.class).getCurrentRouteLevelName(getFinancialSystemDocumentHeader().getWorkflowDocument());
            if(getFinancialSystemDocumentHeader() != null){
                String documentNumber = this.getDocumentNumber();
                if(documentNumber != null){
                WorkflowDocument workflowDocument = null;
                workflowDocument = KRADServiceLocatorWeb.getWorkflowDocumentService().loadWorkflowDocument(this.getDocumentNumber(), SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId()));
                getFinancialSystemDocumentHeader().setWorkflowDocument(workflowDocument);
                }
            }
            String nodeName = getFinancialSystemDocumentHeader().getWorkflowDocument().getCurrentNodeNames().iterator()
                    .next();
            if (nodeName != null
                    && (nodeName.equalsIgnoreCase(PurapWorkflowConstants.BUDGET_NODE) || nodeName
                    .equalsIgnoreCase(PurapWorkflowConstants.BUDGET_REVIEW_REQUIRED))) {
                /*if (SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear()
                        .compareTo(getPostingYear()) >= 0) {
                    List<GeneralLedgerPendingEntry> pendingEntries = getPendingLedgerEntriesForSufficientFundsChecking();
                    for (GeneralLedgerPendingEntry glpe : pendingEntries) {
                        glpe.getChartOfAccountsCode();
                    }
                    // SpringContext.getBean(GeneralLedgerPendingEntryService.class).delete(getDocumentNumber());
                    fundsItems = SpringContext.getBean(SufficientFundsService.class).checkSufficientFundsForPREQ(
                            pendingEntries);
                    // SpringContext.getBean(GeneralLedgerPendingEntryService.class).generateGeneralLedgerPendingEntries(
                    // this);
                    // SpringContext.getBean(BusinessObjectService.class).save(getGeneralLedgerPendingEntries());
                }*/
                SpringContext.getBean(PurapAccountingService.class).updateAccountAmounts(this);
                accountsForRouting = (SpringContext.getBean(PurapAccountingService.class).generateSummary(getItems()));
                /*String documentFiscalYearString = this.getPostingYear().toString();
                List<String> fundsItemList = new ArrayList<String>();
                List<String> accountsList = new ArrayList<String>();
                for (SufficientFundsItem fundsItem : fundsItems) {
                    fundsItemList.add(fundsItem.getAccount().getChartOfAccountsCode());
                }
                for (Iterator accountsForRoutingIter = accountsForRouting.iterator(); accountsForRoutingIter.hasNext(); ) {
                    if (!(fundsItemList.contains(((SourceAccountingLine) accountsForRoutingIter.next())
                            .getChartOfAccountsCode()))) {
                        accountsForRoutingIter.remove();
                    }
                }*/
                setAccountsForRouting(accountsForRouting);
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
            desiredActions.add(ActionRequestType.COMPLETE.getCode());
            if (KewApiServiceLocator.getWorkflowDocumentActionsService().documentWillHaveAtLeastOneActionRequest(
                    reportCriteria.build(), desiredActions, false)) {
                if (StringUtils.isNotBlank(newNodeName)) {
                    if (StringUtils.isNotBlank(newNodeName)) {
                        if (newNodeName.equalsIgnoreCase(PurapWorkflowConstants.BUDGET_NODE)
                                || newNodeName.equalsIgnoreCase(PurapWorkflowConstants.FYI_BUDGET)) {
                            String note = OLEConstants.SufficientFundCheck.PREQ_NOTE;
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

    private void populateVendorAliasName() {
        Map vendorDetailMap = new HashMap();
        vendorDetailMap.put(OLEConstants.VENDOR_HEADER_IDENTIFIER, this.getVendorHeaderGeneratedIdentifier());
        vendorDetailMap.put(OLEConstants.VENDOR_DETAIL_IDENTIFIER, this.getVendorDetailAssignedIdentifier());
        List<VendorAlias> vendorDetailList = (List) getBusinessObjectService().findMatching(VendorAlias.class, vendorDetailMap);
        if (vendorDetailList != null && vendorDetailList.size() > 0) {
            this.setVendorAliasName(vendorDetailList.get(0).getVendorAliasName());
        }
    }
    
    @Override
    public boolean generateGeneralLedgerPendingEntries(GeneralLedgerPendingEntrySourceDetail glpeSourceDetail, GeneralLedgerPendingEntrySequenceHelper sequenceHelper) {
        LOG.debug("processGenerateGeneralLedgerPendingEntries(AccountingDocument, AccountingLine, GeneralLedgerPendingEntrySequenceHelper) - start");

        // handle the explicit entry
        // create a reference to the explicitEntry to be populated, so we can pass to the offset method later
        GeneralLedgerPendingEntry explicitEntry = new GeneralLedgerPendingEntry();
        processExplicitGeneralLedgerPendingEntry(sequenceHelper, glpeSourceDetail, explicitEntry);
        // increment the sequence counter
        sequenceHelper.increment();

        // handle the offset entry

        GeneralLedgerPendingEntry offsetEntry = new GeneralLedgerPendingEntry(explicitEntry);
        boolean success = processOffsetGeneralLedgerPendingEntry(sequenceHelper, glpeSourceDetail, explicitEntry, offsetEntry);

        LOG.debug("processGenerateGeneralLedgerPendingEntries(AccountingDocument, AccountingLine, GeneralLedgerPendingEntrySequenceHelper) - end");
        return success;
    }

    /**
     * This method processes an accounting line's information to build an offset entry, and then adds that to the document.
     *
     * @param sequenceHelper
     * @param explicitEntry
     * @param offsetEntry
     * @return boolean True if the offset generation is successful.
     */
    protected boolean processOffsetGeneralLedgerPendingEntry(GeneralLedgerPendingEntrySequenceHelper sequenceHelper, GeneralLedgerPendingEntrySourceDetail postable, GeneralLedgerPendingEntry explicitEntry, GeneralLedgerPendingEntry offsetEntry) {
        LOG.debug("processOffsetGeneralLedgerPendingEntry(AccountingDocument, GeneralLedgerPendingEntrySequenceHelper, AccountingLine, GeneralLedgerPendingEntry, GeneralLedgerPendingEntry) - start");

        // populate the offset entry
        boolean success = SpringContext.getBean(GeneralLedgerPendingEntryService.class).populateOffsetGeneralLedgerPendingEntry(getPostingYear(), explicitEntry, sequenceHelper, offsetEntry);

        // hook for children documents to implement document specific field mappings for the GLPE
        success &= customizeOffsetGeneralLedgerPendingEntry(postable, explicitEntry, offsetEntry);

        if (SpringContext.getBean(OlePaymentRequestService.class).getPaymentMethod(this.getPaymentMethodId()).equals(OLEConstants.DEPOSIT)) {
            if (offsetEntry.getFinancialBalanceTypeCode().equals(OLEConstants.BALANCE_TYPE_ACTUAL) && offsetEntry.isTransactionEntryOffsetIndicator()) {
                Integer purapDocumentIdentifier = this.getPurapDocumentIdentifier();
                Map itemMap = new HashMap();
                itemMap.put("purapDocumentIdentifier", purapDocumentIdentifier);
                List<OlePaymentRequestItem> paymentRequestItems = (List<OlePaymentRequestItem>) getBusinessObjectService().findMatching(OlePaymentRequestItem.class, itemMap);
                if (paymentRequestItems.size() > 0) {
                    for (OlePaymentRequestItem item : paymentRequestItems) {
                        for (int i = 0; i < item.getSourceAccountingLines().size(); i++) {
                            if (item.getSourceAccountingLine(i).getAccountNumber().equals(explicitEntry.getAccountNumber())) {
                                Integer poItemIdentifier = item.getPoItemIdentifier();
                                Map offMap = new HashMap();
                                offMap.put("itemIdentifier", poItemIdentifier);
                                List<OLEInvoiceOffsetAccountingLine> offsetAccountingLines = (List<OLEInvoiceOffsetAccountingLine>) getBusinessObjectService().findMatching(OLEInvoiceOffsetAccountingLine.class, offMap);
                                if (offsetAccountingLines.size() > 0) {
                                    int size = offsetAccountingLines.size();
                                    for (OLEInvoiceOffsetAccountingLine accLine : offsetAccountingLines) {
                                        offsetEntry.setChartOfAccountsCode(accLine.getChartOfAccountsCode());
                                        offsetEntry.setAccountNumber(accLine.getAccountNumber());
                                        offsetEntry.setFinancialObjectCode(accLine.getFinancialObjectCode());
                                        offsetEntry.setFinancialBalanceTypeCode("AC");
                                        offsetEntry.setFinancialObjectTypeCode("AS");
                                        offsetEntry.setTransactionDebitCreditCode(OLEConstants.GL_CREDIT_CODE);
                                        offsetEntry.setTransactionLedgerEntryAmount(accLine.getAmount());
                                        offsetEntry.setAcctSufficientFundsFinObjCd(accLine.getFinancialObjectCode());
                                        addPendingEntry(offsetEntry);
                                    }
                                }
                            }
                        }
                    }
                }

            } else {
                addPendingEntry(offsetEntry);
            }
        } else {
            addPendingEntry(offsetEntry);
        }

        LOG.debug("processOffsetGeneralLedgerPendingEntry(AccountingDocument, GeneralLedgerPendingEntrySequenceHelper, AccountingLine, GeneralLedgerPendingEntry, GeneralLedgerPendingEntry) - end");
        return success;
    }


    /**
     * This method processes all necessary information to build an explicit general ledger entry, and then adds that to the
     * document.
     *
     * @param sequenceHelper
     * @param explicitEntry
     * @return boolean True if the explicit entry generation was successful, false otherwise.
     */
    protected void processExplicitGeneralLedgerPendingEntry(GeneralLedgerPendingEntrySequenceHelper sequenceHelper, GeneralLedgerPendingEntrySourceDetail glpeSourceDetail, GeneralLedgerPendingEntry explicitEntry) {
        LOG.debug("processExplicitGeneralLedgerPendingEntry(AccountingDocument, GeneralLedgerPendingEntrySequenceHelper, AccountingLine, GeneralLedgerPendingEntry) - start");

        // populate the explicit entry
        SpringContext.getBean(GeneralLedgerPendingEntryService.class).populateExplicitGeneralLedgerPendingEntry(this, glpeSourceDetail, sequenceHelper, explicitEntry);

        // hook for children documents to implement document specific GLPE field mappings
        customizeExplicitGeneralLedgerPendingEntry(glpeSourceDetail, explicitEntry);

        if ((SpringContext.getBean(OlePaymentRequestService.class).getPaymentMethod(this.getPaymentMethodId()).equals(OLEConstants.DEPOSIT)) && explicitEntry.getFinancialBalanceTypeCode().equals(OLEConstants.BALANCE_TYPE_ACTUAL)) {
            explicitEntry.setTransactionDebitCreditCode(OLEConstants.GL_DEBIT_CODE);
            addPendingEntry(explicitEntry);
        } else {
            addPendingEntry(explicitEntry);
        }

        LOG.debug("processExplicitGeneralLedgerPendingEntry(AccountingDocument, GeneralLedgerPendingEntrySequenceHelper, AccountingLine, GeneralLedgerPendingEntry) - end");
    }

    public boolean isCurrencyTypeIndicator() {
        return currencyTypeIndicator;
    }

    public void setCurrencyTypeIndicator(boolean currencyTypeIndicator) {
        this.currencyTypeIndicator = currencyTypeIndicator;
    }
}

