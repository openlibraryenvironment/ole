package org.kuali.ole.select.controller;


import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.coa.businessobject.OleFundCode;
import org.kuali.ole.coa.businessobject.OleFundCodeAccountingLine;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.module.purap.PurapParameterConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.module.purap.document.PurchasingAccountsPayableDocument;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.module.purap.document.validation.event.AttributedCalculateAccountsPayableEvent;
import org.kuali.ole.module.purap.service.PurapAccountingService;
import org.kuali.ole.module.purap.util.PurApRelatedViews;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.document.service.OleInvoiceFundCheckService;
import org.kuali.ole.select.document.service.OleInvoiceItemService;
import org.kuali.ole.select.document.service.OleInvoiceService;
import org.kuali.ole.select.document.service.OlePurapAccountingService;
import org.kuali.ole.select.document.service.impl.OlePurapAccountingServiceImpl;
import org.kuali.ole.select.document.validation.event.OLEInvoiceSubscriptionOverlayValidationEvent;
import org.kuali.ole.select.document.validation.event.OleDiscountInvoiceEvent;
import org.kuali.ole.select.document.validation.event.OleForeignCurrencyInvoiceEvent;
import org.kuali.ole.select.form.OLEInvoiceForm;
import org.kuali.ole.select.service.impl.OLEInvoiceDaoOjb;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.AccountingLine;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.event.AddAccountingLineEvent;
import org.kuali.ole.vnd.businessobject.VendorAddress;
import org.kuali.ole.vnd.businessobject.VendorAlias;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.api.util.type.AbstractKualiDecimal;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.service.*;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.web.controller.TransactionalDocumentControllerBase;
import org.kuali.rice.krad.web.form.DocumentFormBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: anithaa
 * Date: 7/8/13
 * Time: 7:27 PM
 * To change this template use File | Settings | File Templates.
 */


@Controller
@RequestMapping(value = "/OLEInvoice")
public class OLEInvoiceController extends TransactionalDocumentControllerBase {

    private static transient OleInvoiceService invoiceService;
    private KualiRuleService kualiRuleService;
    private OleInvoiceItemService oleInvoiceItemService;

    public OleInvoiceItemService getOleInvoiceItemService() {
        if (null == oleInvoiceItemService){
            oleInvoiceItemService = (OleInvoiceItemService) SpringContext.getBean("oleInvoiceItemService");
        }
        return oleInvoiceItemService;
    }

    public void setOleInvoiceItemService(OleInvoiceItemService oleInvoiceItemService) {
        this.oleInvoiceItemService = oleInvoiceItemService;
    }

    @Override
    protected OLEInvoiceForm createInitialForm(HttpServletRequest request) {
        OLEInvoiceForm oleInvoiceForm = new OLEInvoiceForm();
        return oleInvoiceForm;
    }

    /**
     * Creates a new document of the type specified by the docTypeName property of the given form.
     * This has been abstracted out so that it can be overridden in children if the need arises.
     *
     * @param form - form instance that contains the doc type parameter and where
     *             the new document instance should be set
     */
    @Override
    protected void createDocument(DocumentFormBase form) throws WorkflowException {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        super.createDocument(oleInvoiceForm);
        ((OleInvoiceDocument) oleInvoiceForm.getDocument()).initiateDocument();

    }

    @RequestMapping(params = "methodToCall=docHandler")
    public ModelAndView docHandler(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        String itemLimit = getInvoiceService().getParameter(org.kuali.ole.OLEConstants.SERIAL_SINGLE_SEC_LIMIT);
        oleInvoiceForm.setItemLimit(itemLimit!=null?itemLimit:"0");
        ModelAndView mv = super.docHandler(oleInvoiceForm, result, request, response);
        String command = form.getCommand();
        String[] DOCUMENT_LOAD_COMMANDS =
                {KewApiConstants.ACTIONLIST_COMMAND, KewApiConstants.DOCSEARCH_COMMAND, KewApiConstants.SUPERUSER_COMMAND,
                        KewApiConstants.HELPDESK_ACTIONLIST_COMMAND};
        if (ArrayUtils.contains(DOCUMENT_LOAD_COMMANDS, command) && form.getDocId() != null) {
            OleInvoiceDocument  oleInvoiceDocument = (OleInvoiceDocument) form.getDocument();
            oleInvoiceDocument.loadInvoiceDocument();
        }
        return mv;
    }
    @RequestMapping(params = "methodToCall=addProcessItem")
    public ModelAndView addItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the add process item method");
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument invoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        invoiceDocument.setDbRetrieval(false);

        if(invoiceDocument.isAddImmediately() && StringUtils.isBlank(invoiceDocument.getPaymentMethodIdentifier())){
            GlobalVariables.getMessageMap().putError(OleSelectConstant.PROCESS_ITEM_SECTION_ID, OLEKeyConstants.ERROR_NO_PAYMENT_MTHD);
            return getUIFModelAndView(oleInvoiceForm);
        }
        if (invoiceDocument.getPurchaseOrderDocuments().size() > 0) {
            GlobalVariables.getMessageMap().putError(OleSelectConstant.PO_ITEM_SECTION_ID,
                    PurapKeyConstants.ERROR_PO_ADD);
        } else {
            invoiceDocument.setPurchaseOrderDocuments(new ArrayList<OlePurchaseOrderDocument>());

            String invoiceCurrencyType = getInvoiceService().getCurrencyType(invoiceDocument.getInvoiceCurrencyType());
            BigDecimal invoiceCurrencyExchangeRate = BigDecimal.ZERO;
            if (StringUtils.isNotBlank(invoiceCurrencyType) && !invoiceCurrencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                if (StringUtils.isBlank(invoiceDocument.getInvoiceCurrencyExchangeRate())) {
                    GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_EXCHANGE_RATE_EMPTY, invoiceCurrencyType);
                    return getUIFModelAndView(oleInvoiceForm);
                } else {
                    try {
                        Double.parseDouble(invoiceDocument.getInvoiceCurrencyExchangeRate());
                        BigDecimal exchangeRate = new BigDecimal(invoiceDocument.getInvoiceCurrencyExchangeRate());
                        if (new KualiDecimal(exchangeRate).isZero()) {
                            GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_ENTER_VALID_EXCHANGE_RATE);
                            return getUIFModelAndView(oleInvoiceForm);
                        }
                        invoiceCurrencyExchangeRate = exchangeRate;
                    } catch (NumberFormatException nfe) {
                        GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_ENTER_VALID_EXCHANGE_RATE);
                        return getUIFModelAndView(oleInvoiceForm);
                    }
                }
            }

            String[] poIds = invoiceDocument.getPoId().split(",");
            for (String poId : poIds) {
                if (!poId.isEmpty() && isNumeric(poId) && isValidInteger(poId)) {
                    OlePurchaseOrderDocument olePurchaseOrderDocument = getOlePurchaseOrderDocument(poId);
                    if (olePurchaseOrderDocument != null && (olePurchaseOrderDocument.getApplicationDocumentStatus().equalsIgnoreCase("OPEN"))) {
                        if (invoiceDocument.getVendorHeaderGeneratedIdentifier() != null && invoiceDocument.getVendorDetailAssignedIdentifier() != null &&
                                (olePurchaseOrderDocument.getVendorDetailAssignedIdentifier().compareTo(invoiceDocument.getVendorDetailAssignedIdentifier()) == 0) &&
                                olePurchaseOrderDocument.getVendorHeaderGeneratedIdentifier().compareTo(invoiceDocument.getVendorHeaderGeneratedIdentifier()) == 0) {
                            List<OlePurchaseOrderItem> items = (List<OlePurchaseOrderItem>) olePurchaseOrderDocument.getItems();
                            List<OlePurchaseOrderItem> activeItems = new ArrayList<>();
                            List<OlePurchaseOrderItem> finalItem = new ArrayList<>();
                            olePurchaseOrderDocument.setItems(finalItem);
                            for (OlePurchaseOrderItem item : items) {
                                if (item.isItemActiveIndicator() &&
                                        item.getItemType().isQuantityBasedGeneralLedgerIndicator() &&
                                        item.getItemType().isLineItemIndicator()) {
                                    activeItems.add(item);
                                }
                            }
                            if (CollectionUtils.isEmpty(activeItems)) {
                                GlobalVariables.getMessageMap().putError(OleSelectConstant.PO_ITEM_SECTION_ID, OLEKeyConstants.NO_ACTIVE_ITEMS, poId);

                            } else {
                                processPurchaseOrderDocument(invoiceDocument, invoiceCurrencyType, invoiceCurrencyExchangeRate, olePurchaseOrderDocument, activeItems);
                            }
                        } else if (invoiceDocument.getVendorHeaderGeneratedIdentifier() == null && invoiceDocument.getVendorDetailAssignedIdentifier() == null && olePurchaseOrderDocument.getVendorNumber() != null) {
                            GlobalVariables.getMessageMap().putError(OleSelectConstant.PO_ITEM_SECTION_ID, OLEKeyConstants.ERROR_NO_MATCHING_PO_VND_NM, new String[]{poId, olePurchaseOrderDocument.getVendorName()});
                        } else {
                            GlobalVariables.getMessageMap().putError(OleSelectConstant.PO_ITEM_SECTION_ID, OLEKeyConstants.ERROR_NO_MATCHING_PO_VND, poId);
                        }
                    } else if (olePurchaseOrderDocument != null && olePurchaseOrderDocument.getDocumentHeader().getWorkflowDocument().isFinal() && (olePurchaseOrderDocument.getApplicationDocumentStatus().equalsIgnoreCase("VOID"))) {
                        if (invoiceDocument.getVendorHeaderGeneratedIdentifier() == null && invoiceDocument.getVendorDetailAssignedIdentifier() == null && olePurchaseOrderDocument.getVendorName() != null) {
                            GlobalVariables.getMessageMap().putError(OleSelectConstant.PO_ITEM_SECTION_ID,
                                    PurapKeyConstants.ERROR_PURCHASE_ORDER_FINAL_VOID, poId);
                        } else if (invoiceDocument.getVendorName().equalsIgnoreCase(olePurchaseOrderDocument.getVendorName())) {
                            GlobalVariables.getMessageMap().putError(OleSelectConstant.PO_ITEM_SECTION_ID,
                                    PurapKeyConstants.ERROR_PURCHASE_ORDER_FINAL_VOID, poId);
                        } else {
                            GlobalVariables.getMessageMap().putError(OleSelectConstant.PO_ITEM_SECTION_ID, OLEKeyConstants.ERROR_NO_MATCHING_PO_VND, poId);
                        }
                    } else if (olePurchaseOrderDocument != null && olePurchaseOrderDocument.getDocumentHeader().getWorkflowDocument().isFinal() && (olePurchaseOrderDocument.getApplicationDocumentStatus().equalsIgnoreCase("PENDING PRINT"))) {
                        if (invoiceDocument.getVendorHeaderGeneratedIdentifier() == null && invoiceDocument.getVendorDetailAssignedIdentifier() == null && olePurchaseOrderDocument.getVendorName() != null) {
                            GlobalVariables.getMessageMap().putError(OleSelectConstant.PO_ITEM_SECTION_ID,
                                    PurapKeyConstants.ERROR_PURCHASE_ORDER_PENDING_PRINT, poId);
                        } else if (invoiceDocument.getVendorName().equalsIgnoreCase(olePurchaseOrderDocument.getVendorName())) {
                            GlobalVariables.getMessageMap().putError(OleSelectConstant.PO_ITEM_SECTION_ID,
                                    PurapKeyConstants.ERROR_PURCHASE_ORDER_PENDING_PRINT, poId);
                        } else {
                            GlobalVariables.getMessageMap().putError(OleSelectConstant.PO_ITEM_SECTION_ID, OLEKeyConstants.ERROR_NO_MATCHING_PO_VND, poId);
                        }
                    } else {
                        GlobalVariables.getMessageMap().putError(OleSelectConstant.PO_ITEM_SECTION_ID,
                                PurapKeyConstants.ERROR_PURCHASE_ORDER_NOT_FOUND, poId);
                    }
                } else {
                    GlobalVariables.getMessageMap().putError(OleSelectConstant.PO_ITEM_SECTION_ID,
                            OLEKeyConstants.ERROR_NO_PO_EXIST, poId);
                }
            }
            if (invoiceDocument.isAddImmediately()){
                getInvoiceService().populateInvoiceItems(invoiceDocument, invoiceDocument.getPurchaseOrderDocuments());
                try {
                    calculate(oleInvoiceForm, result, request, response);
                } catch (Exception e) {
                    LOG.error("Exception while calculating the document" + e);
                    throw new RuntimeException(e);
                }
                if (invoiceDocument.isProrateDollar() || invoiceDocument.isProrateQty()) {
                    SpringContext.getBean(OleInvoiceService.class).calculateInvoice(invoiceDocument, true);
                }
                processInvoiceItems(invoiceDocument);
            }
            invoiceDocument.setPoId("");
            processCollapseSections(invoiceDocument);
        }
        return getUIFModelAndView(oleInvoiceForm);
    }

    private void processPurchaseOrderDocument(OleInvoiceDocument invoiceDocument, String invoiceCurrencyType, BigDecimal invoiceCurrencyExchangeRate, OlePurchaseOrderDocument olePurchaseOrderDocument, List<OlePurchaseOrderItem> activeItems) {
        for (OlePurchaseOrderItem item : activeItems) {
            if (item.getItemTypeCode().equalsIgnoreCase(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE)) {
                //item.setPoOutstandingQuantity(item.getItemQuantity().subtract(item.getItemInvoicedTotalQuantity()));
                calculatePOOutstandingQuantity(item, olePurchaseOrderDocument.getOrderType().getPurchaseOrderType());
                item.setNoOfCopiesInvoiced(new KualiInteger(item.getItemQuantity().bigDecimalValue()));
                item.setNoOfPartsInvoiced(item.getItemNoOfParts());
                item.setInvoiceItemListPrice(item.getItemListPrice().toString());
                if (StringUtils.isNotBlank(invoiceDocument.getInvoiceCurrencyType())) {
                    invoiceDocument.setInvoiceCurrencyTypeId(new Long(invoiceDocument.getInvoiceCurrencyType()));
                    if (StringUtils.isNotBlank(invoiceCurrencyType)) {
                        if (!invoiceCurrencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                            item.setInvoiceForeignCurrency(invoiceCurrencyType);
                            invoiceDocument.setForeignCurrencyFlag(true);
                            item.setItemDiscount(new KualiDecimal(0.0));
                            item.setInvoiceExchangeRate(invoiceCurrencyExchangeRate.toString());
                            // if the PO has Foreign Currency
                            if (item.getItemForeignListPrice() != null) {
                                item.setInvoiceForeignItemListPrice(item.getItemForeignListPrice().toString());
                                item.setInvoiceForeignDiscount(item.getItemForeignDiscount() != null ? item.getItemForeignDiscount().toString() : new KualiDecimal("0.0").toString());
                                item.setInvoiceForeignUnitCost(item.getItemForeignUnitCost().toString());
                                item.setInvoiceForeignCurrency(invoiceCurrencyType);

                                if (item.getInvoiceExchangeRate() != null && item.getInvoiceForeignUnitCost() != null) {
                                    item.setItemUnitCostUSD(new KualiDecimal(new BigDecimal(item.getInvoiceForeignUnitCost()).divide(new BigDecimal(item.getInvoiceExchangeRate()), 4, RoundingMode.HALF_UP)));
                                    item.setItemUnitPrice(new BigDecimal(item.getInvoiceForeignUnitCost()).divide(new BigDecimal(item.getInvoiceExchangeRate()), 4, RoundingMode.HALF_UP));
                                    item.setItemListPrice(item.getItemUnitCostUSD());
                                    item.setInvoiceItemListPrice(item.getItemListPrice().toString());
                                }
                            } else {
                                item.setItemForeignUnitCost(new KualiDecimal(item.getItemUnitPrice().multiply(new BigDecimal(item.getInvoiceExchangeRate()))));
                                item.setItemForeignListPrice(item.getItemForeignUnitCost());
                                item.setInvoiceForeignItemListPrice(item.getItemForeignListPrice().toString());
                                item.setInvoiceForeignDiscount(new KualiDecimal(0.0).toString());
                                item.setInvoiceForeignUnitCost(item.getItemForeignUnitCost().toString());
                            }
                            getInvoiceService().calculateAccount(item);
                        } else {
                            invoiceDocument.setForeignCurrencyFlag(false);
                            invoiceDocument.setInvoiceCurrencyExchangeRate(null);
                            item.setItemDiscount(item.getItemDiscount() != null ? item.getItemDiscount() : new KualiDecimal(0.0));
                        }
                    }
                } else {
                    invoiceDocument.setForeignCurrencyFlag(false);
                    invoiceDocument.setInvoiceCurrencyType(invoiceDocument.getVendorDetail().getCurrencyType().getCurrencyTypeId().toString());
                    invoiceDocument.setInvoiceCurrencyTypeId(invoiceDocument.getVendorDetail().getCurrencyType().getCurrencyTypeId());
                    invoiceDocument.setInvoiceCurrencyExchangeRate(null);
                    item.setInvoiceExchangeRate(null);
                    item.setItemExchangeRate(null);
                }
                if (item.getItemTitleId() != null) {
                    item.setItemDescription(SpringContext.getBean(OlePurapService.class).getItemDescription(item));
                }
                olePurchaseOrderDocument.getItems().add(item);
            }
        }
        if (olePurchaseOrderDocument.getPurchaseOrderEndDate() != null) {
            olePurchaseOrderDocument.setPoEndDate(olePurchaseOrderDocument.getPurchaseOrderEndDate());
        }
        List<OlePurchaseOrderLineForInvoice> olePurchaseOrderLineForInvoiceList = getOleInvoiceItemService().getOlePurchaseOrderLineForInvoiceForAddItem(olePurchaseOrderDocument);
        olePurchaseOrderDocument.setOlePurchaseOrderLineForInvoiceList(olePurchaseOrderLineForInvoiceList);
        List<OlePurchaseOrderTotal> olePurchaseOrderTotalList = getOleInvoiceItemService().getOlePurchaseOrderTotalForAddItem(olePurchaseOrderDocument);
        olePurchaseOrderDocument.setPurchaseOrderTotalList(olePurchaseOrderTotalList);
        invoiceDocument.getPurchaseOrderDocuments().add(olePurchaseOrderDocument);
    }

    private void processCollapseSections(OleInvoiceDocument invoiceDocument) {
        String[] collapseSections = getInvoiceService().getCollapseSections();
        invoiceDocument.setOverviewFlag(getInvoiceService().canCollapse(OLEConstants.OVERVIEW_SECTION, collapseSections));
        invoiceDocument.setVendorInfoFlag(getInvoiceService().canCollapse(OLEConstants.VENDOR_INFO_SECTION, collapseSections));
        invoiceDocument.setInvoiceInfoFlag(getInvoiceService().canCollapse(OLEConstants.INVOICE_INFO_SECTION, collapseSections));
        invoiceDocument.setProcessTitlesFlag(getInvoiceService().canCollapse(OLEConstants.PROCESS_TITLES_SECTION, collapseSections));
        invoiceDocument.setCurrentItemsFlag(getInvoiceService().canCollapse(OLEConstants.CURRENT_ITEM_SECTION, collapseSections));
        invoiceDocument.setAdditionalChargesFlag(getInvoiceService().canCollapse(OLEConstants.ADDITIONAL_CHARGES_SECTION, collapseSections));
        invoiceDocument.setAccountSummaryFlag(getInvoiceService().canCollapse(OLEConstants.ACCOUNT_SUMMARY_SECTION, collapseSections));
        invoiceDocument.setAdHocRecipientsFlag(getInvoiceService().canCollapse(OLEConstants.ADHOC_RECIPIENT_SECTION, collapseSections));
        invoiceDocument.setRouteLogFlag(getInvoiceService().canCollapse(OLEConstants.ROUTE_LOG_SECTION, collapseSections));
        invoiceDocument.setNotesAndAttachmentFlag(getInvoiceService().canCollapse(OLEConstants.NOTES_AND_ATTACH_SECTION, collapseSections));
    }

    private OlePurchaseOrderDocument getOlePurchaseOrderDocument(String poId) {
        OlePurchaseOrderDocument olePurchaseOrderDocument = null;
        if (StringUtils.isNotBlank(poId)) {
            Map<String, String> searchCriteria = new HashMap<>();
            searchCriteria.put(OLEConstants.InvoiceDocument.INVOICE_PURAP_DOCUMENT_IDENTIFIER, poId);
            List<OlePurchaseOrderDocument> purchaseOrderDocumentList = (List<OlePurchaseOrderDocument>) getBusinessObjectService().findMatching(OlePurchaseOrderDocument.class, searchCriteria);
            if (purchaseOrderDocumentList.size() > 0) {
                for (OlePurchaseOrderDocument purchaseOrderDocument : purchaseOrderDocumentList) {
                    if (purchaseOrderDocument.getPurchaseOrderCurrentIndicatorForSearching()) {
                        olePurchaseOrderDocument = purchaseOrderDocument;
                    }
                }
            }
        }
        return olePurchaseOrderDocument;
    }

    private void calculatePOOutstandingQuantity(OlePurchaseOrderItem item,String poOrderType ){
        if (poOrderType!=null && !getInvoiceService().getRecurringOrderTypes().contains(poOrderType)) {
            item.setPoOutstandingQuantity(item.getItemQuantity().subtract(item.getItemInvoicedTotalQuantity()));
        }else{
            item.setPoOutstandingQuantity(KualiDecimal.ZERO);
        }
    }

    @RequestMapping(params = "methodToCall=searchVendor")
    public ModelAndView searchVendor(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        Map<String, String> criteria = new HashMap<String, String>();
        VendorDetail vendorDetail = null;
        String[] vendorIds = oleInvoiceDocument.getVendorId().split("-");
        if (vendorIds.length > 1) {
            criteria.put(OLEConstants.InvoiceDocument.VENDOR_HEADER_IDENTIFIER, vendorIds[0]);
            criteria.put(OLEConstants.InvoiceDocument.VENDOR_DETAIL_IDENTIFIER, vendorIds[1]);
            vendorDetail = (VendorDetail) getBusinessObjectService().findByPrimaryKey(VendorDetail.class, criteria);
        }
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
            if (vendorDetail.getPaymentMethodId() != null) {
                oleInvoiceDocument.setPaymentMethodIdentifier(vendorDetail.getPaymentMethodId().toString());
            }
            if (oleInvoiceDocument.getVendorDetail() != null ) {
                if (oleInvoiceDocument.getVendorDetail().getCurrencyType() != null) {
                    oleInvoiceDocument.setInvoiceCurrencyType(vendorDetail.getCurrencyType().getCurrencyTypeId().toString());
                    oleInvoiceDocument.setInvoiceCurrencyTypeId(vendorDetail.getCurrencyType().getCurrencyTypeId());
                    if (vendorDetail.getCurrencyType().getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                        oleInvoiceDocument.setForeignCurrencyFlag(false);
                        oleInvoiceDocument.setForeignInvoiceAmount(null);
                        oleInvoiceDocument.setInvoiceCurrencyExchangeRate(null);
                    } else {
                        oleInvoiceDocument.setForeignCurrencyFlag(true);
                        oleInvoiceDocument.setInvoiceAmount(null);
                        BigDecimal exchangeRate = getInvoiceService().getExchangeRate(oleInvoiceDocument.getInvoiceCurrencyType()).getExchangeRate();
                        oleInvoiceDocument.setInvoiceCurrencyExchangeRate(exchangeRate.toString());
                    }
                }
            }
            else {
                oleInvoiceDocument.setPaymentMethodIdentifier("");
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
        else {
            vendorDetail = new VendorDetail();
            oleInvoiceDocument.setVendorDetail(vendorDetail);
            oleInvoiceDocument.setVendorName(vendorDetail.getVendorName());
            oleInvoiceDocument.setVendorHeaderGeneratedIdentifier(vendorDetail.getVendorHeaderGeneratedIdentifier());
            oleInvoiceDocument.setVendorDetailAssignedIdentifier(vendorDetail.getVendorDetailAssignedIdentifier());
            oleInvoiceDocument.setVendorNumber(vendorDetail.getVendorNumber());
            oleInvoiceDocument.setVendorHeaderGeneratedIdentifier(vendorDetail.getVendorHeaderGeneratedIdentifier());
            oleInvoiceDocument.setVendorDetailAssignedIdentifier(vendorDetail.getVendorDetailAssignedIdentifier());
            oleInvoiceDocument.setVendorFaxNumber(vendorDetail.getDefaultFaxNumber());
            oleInvoiceDocument.setVendorPaymentTerms(vendorDetail.getVendorPaymentTerms());
            oleInvoiceDocument.setVendorPaymentTermsCode("");
            oleInvoiceDocument.setVendorShippingPaymentTerms(vendorDetail.getVendorShippingPaymentTerms());
            oleInvoiceDocument.setPaymentMethodIdentifier("");
            oleInvoiceDocument.setVendorCityName("");
            oleInvoiceDocument.setVendorLine1Address("");
            oleInvoiceDocument.setVendorLine2Address("");
            oleInvoiceDocument.setVendorAttentionName("");
            oleInvoiceDocument.setVendorPostalCode("");
            oleInvoiceDocument.setVendorStateCode("");
            oleInvoiceDocument.setVendorAttentionName("");
            oleInvoiceDocument.setVendorAddressInternationalProvinceName("");
            oleInvoiceDocument.setVendorCountryCode("");
        }
        return getUIFModelAndView(oleInvoiceForm);
    }

    @RequestMapping(params = {"methodToCall=calculate"})
    public ModelAndView calculate(@ModelAttribute("KualiForm") org.kuali.rice.krad.web.form.DocumentFormBase form,
                                  BindingResult result, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // ActionForward forward = super.calculate(mapping, form, request, response);
        /* calculateCurrency(mapping, form, request, response); */
        OLEInvoiceForm paymentForm = (OLEInvoiceForm) form;
        OleInvoiceDocument payDoc = (OleInvoiceDocument) paymentForm.getDocument();

        List<PurApItem> purApItems = ((PurchasingAccountsPayableDocument) payDoc).getItems();
        for(PurApItem purApItem:purApItems){
            List<KualiDecimal> existingAmount=new ArrayList<>();
            for(PurApAccountingLine oldSourceAccountingLine:purApItem.getSourceAccountingLines()) {
                if(oldSourceAccountingLine instanceof InvoiceAccount) {
                    if(((InvoiceAccount)oldSourceAccountingLine).getExistingAmount()!=null){
                        existingAmount.add(((InvoiceAccount)oldSourceAccountingLine).getExistingAmount());
                    }
                }
            }
            int count=0;
            KualiDecimal accountingLinePercentage = KualiDecimal.ZERO;
            for(PurApAccountingLine account:purApItem.getSourceAccountingLines()){
                int accountingLineSize = purApItem.getSourceAccountingLines().size();
                OleInvoiceItem oleInvoiceItem = (OleInvoiceItem)purApItem;
                KualiDecimal totalAmount = KualiDecimal.ZERO;
                BigDecimal discount = BigDecimal.ZERO;
                if(oleInvoiceItem.getItemDiscount() != null) {
                    if((oleInvoiceItem.getItemDiscountType() != null && oleInvoiceItem.getItemDiscountType().equals("%")) || (oleInvoiceItem.getItemForeignDiscountType() != null && oleInvoiceItem.getItemForeignDiscountType().equals("%"))) {
                        discount = ((oleInvoiceItem.getItemListPrice().bigDecimalValue().multiply(oleInvoiceItem.getItemDiscount().bigDecimalValue()))).divide(new BigDecimal(100));
                        totalAmount = new KualiDecimal(purApItem.getItemQuantity().bigDecimalValue().multiply(oleInvoiceItem.getItemListPrice().bigDecimalValue().subtract(discount)));
                    }  else {
                        discount = ((oleInvoiceItem.getItemListPrice().bigDecimalValue().subtract(oleInvoiceItem.getItemDiscount().bigDecimalValue())));
                        totalAmount = new KualiDecimal(purApItem.getItemQuantity().bigDecimalValue().multiply(oleInvoiceItem.getItemListPrice().bigDecimalValue().subtract(discount)));
                    }

                }
                else{
                    totalAmount = new KualiDecimal(purApItem.getItemQuantity().bigDecimalValue().multiply(oleInvoiceItem.getItemListPrice().bigDecimalValue()));
                }
                if(oleInvoiceItem.isUseTaxIndicator() && oleInvoiceItem.getItemSalesTaxAmount()!=null){
                    totalAmount = new KualiDecimal(totalAmount.bigDecimalValue().add(oleInvoiceItem.getItemSalesTaxAmount().bigDecimalValue()));
                    ((OleInvoiceItem) purApItem).setUseTaxIndicator(true);
                }
                if (purApItem != null && purApItem.getExtendedPrice() != null && purApItem.getExtendedPrice().isZero()) {
                    account.setAmount(KualiDecimal.ZERO);
                }
                else if (ObjectUtils.isNotNull(account.getAccountLinePercent()) || ObjectUtils.isNotNull(account.getAmount())) {
                    if (account.getAmount() != null && count < existingAmount.size() && existingAmount.size() != 0 && !existingAmount.get(count).toString().equals(account.getAmount().toString())) {
                        if (count == accountingLineSize - 1) {
                            KualiDecimal calculatedPercent = new KualiDecimal(100).subtract(accountingLinePercentage);
                            account.setAccountLinePercent(calculatedPercent.bigDecimalValue());
                        } else {
                            KualiDecimal calculatedPercent = totalAmount.isGreaterThan(AbstractKualiDecimal.ZERO) ? new KualiDecimal(account.getAmount().multiply(new KualiDecimal(100)).divide(totalAmount).toString()) : KualiDecimal.ZERO;
                            accountingLinePercentage = accountingLinePercentage.add(calculatedPercent);
                            account.setAccountLinePercent(calculatedPercent.bigDecimalValue().setScale(OLEConstants.BIG_DECIMAL_SCALE, BigDecimal.ROUND_CEILING));
                        }

                    } else {
                        KualiDecimal calculatedAmount = new KualiDecimal((account.getAccountLinePercent().multiply(purApItem.getItemQuantity().bigDecimalValue().multiply(oleInvoiceItem.getItemListPrice().bigDecimalValue().subtract(discount))).divide(new BigDecimal(100))).toString());
                        account.setAmount(calculatedAmount);
                        if (count == accountingLineSize - 1) {
                            KualiDecimal calculatedPercent = new KualiDecimal(100).subtract(accountingLinePercentage);
                            account.setAccountLinePercent(calculatedPercent.bigDecimalValue());
                        } else {
                            KualiDecimal calculatedPercent = totalAmount.isGreaterThan(AbstractKualiDecimal.ZERO) ? new KualiDecimal(account.getAmount().multiply(new KualiDecimal(100)).divide(totalAmount).toString()) : KualiDecimal.ZERO;
                            accountingLinePercentage = accountingLinePercentage.add(calculatedPercent);
                            account.setAccountLinePercent(calculatedPercent.bigDecimalValue().setScale(OLEConstants.BIG_DECIMAL_SCALE, BigDecimal.ROUND_CEILING));
                        }

                    }
                }
                count++;
            }
            for(PurApAccountingLine oldSourceAccountingLine:purApItem.getSourceAccountingLines()) {
                if(oldSourceAccountingLine instanceof InvoiceAccount) {
                    ((InvoiceAccount)oldSourceAccountingLine).setExistingAmount(oldSourceAccountingLine.getAmount());
                }
            }
        }

        payDoc.setProrateBy(payDoc.isProrateQty() ? OLEConstants.PRORATE_BY_QTY : payDoc.isProrateManual() ? OLEConstants.MANUAL_PRORATE : payDoc.isProrateDollar() ? OLEConstants.PRORATE_BY_DOLLAR : payDoc.isNoProrate() ? OLEConstants.NO_PRORATE : null);

        if ( StringUtils.isNotBlank(payDoc.getInvoiceCurrencyType()) ) {
            List<OleInvoiceItem> item = payDoc.getItems();
            String currencyType = getInvoiceService().getCurrencyType(payDoc.getInvoiceCurrencyType());
            if (StringUtils.isNotBlank(currencyType)) {
                if (currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                    payDoc.setInvoiceCurrencyExchangeRate(null);
                    payDoc.setForeignCurrencyFlag(false);
                    payDoc.setForeignVendorInvoiceAmount(null);
                    for (int i = 0; item.size() > i; i++) {
                        OleInvoiceItem items = (OleInvoiceItem) payDoc.getItem(i);
                        if (items.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
                            boolean rulePassed = SpringContext.getBean(KualiRuleService.class).applyRules(
                                    new OleDiscountInvoiceEvent(payDoc, items));
                            if (rulePassed) {
                                items.setItemUnitPrice(SpringContext.getBean(OlePurapService.class).calculateDiscount(items).setScale(4, BigDecimal.ROUND_HALF_UP));
                            }
                        }
                    }
                } else {
                    LOG.debug("###########Foreign Currency Field Calculation###########");
                    for (int i = 0; item.size() > i; i++) {
                        OleInvoiceItem items = (OleInvoiceItem) payDoc.getItem(i);
                        BigDecimal exchangeRate = null;
                        if (StringUtils.isNotBlank(payDoc.getInvoiceCurrencyExchangeRate())) {
                            try {
                                Double.parseDouble(payDoc.getInvoiceCurrencyExchangeRate());
                                exchangeRate = new BigDecimal(payDoc.getInvoiceCurrencyExchangeRate());
                                if (new KualiDecimal(exchangeRate).isZero()) {
                                    GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_ENTER_VALID_EXCHANGE_RATE);
                                    return getUIFModelAndView(paymentForm);
                                }
                                items.setItemExchangeRate(exchangeRate);
                                items.setExchangeRate(exchangeRate.toString());
                            }
                            catch (NumberFormatException nfe) {
                                GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_ENTER_VALID_EXCHANGE_RATE);
                                return getUIFModelAndView(paymentForm);
                            }
                        } else {
                                /*exchangeRate = new KualiDecimal(getInvoiceService().getExchangeRate(payDoc.getInvoiceCurrencyType()).getExchangeRate());
                                items.setItemExchangeRate(exchangeRate);
                                items.setExchangeRate(exchangeRate.toString());
                                payDoc.setInvoiceCurrencyExchangeRate(exchangeRate.toString());
                                payDoc.setForeignCurrencyFlag(true);*/
                            GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_EXCHANGE_RATE_EMPTY, currencyType);
                            return getUIFModelAndView(paymentForm);
                        }
                            /*payDoc.setForeignVendorInvoiceAmount(payDoc.getVendorInvoiceAmount()!=null ? payDoc.getVendorInvoiceAmount().bigDecimalValue()
                                    .multiply(tempOleExchangeRate.getExchangeRate()) : payDoc.getForeignVendorInvoiceAmount());*/

                        if ((items.getItemType().isQuantityBasedGeneralLedgerIndicator())) {
                            boolean rulePassed = SpringContext.getBean(KualiRuleService.class).applyRules(
                                    new OleForeignCurrencyInvoiceEvent(payDoc, items));
                            if (rulePassed) {
                                SpringContext.getBean(OlePurapService.class).calculateForeignCurrency(items);
                                if (items.getItemExchangeRate() != null && items.getItemForeignUnitCost() != null) {
                                    if(!items.getItemForeignUnitCost().equals(new KualiDecimal("0.00"))) {
                                        items.setItemUnitCostUSD(new KualiDecimal(items.getItemForeignUnitCost().bigDecimalValue().divide(exchangeRate, 4, BigDecimal.ROUND_HALF_UP)));
                                        items.setItemUnitPrice(items.getItemForeignUnitCost().bigDecimalValue().divide(exchangeRate, 4, BigDecimal.ROUND_HALF_UP));
                                        items.setItemListPrice(items.getItemUnitCostUSD());
                                    }
                                    if (!items.isDebitItem()) {
                                        items.setListPrice("-" + items.getItemListPrice().toString());
                                    } else {
                                        items.setListPrice(items.getItemListPrice().toString());
                                    }
                                    items.setItemDiscount(new KualiDecimal(0.0));
                                    items.setDiscountItem(new KualiDecimal(0.0).toString());
                                }
                                getInvoiceService().calculateAccount(items);
                            }

                        } else {
                            if (items.getItemExchangeRate() != null && items.getForeignCurrencyExtendedPrice() != null) {
                                // added for jira - OLE-2203
                                if (items.isAdditionalChargeUsd()) {
                                    items.setItemUnitPrice(items.getForeignCurrencyExtendedPrice().bigDecimalValue());
                                } else {
                                    items.setItemUnitPrice(items.getForeignCurrencyExtendedPrice().bigDecimalValue().divide(exchangeRate, 4, RoundingMode.HALF_UP));
                                }
                            }
                        }
                    }
                }
            }
        }
        else{
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, org.kuali.ole.OLEConstants.NO_CURENCYTYPE_FOUND);
        }

        //   SpringContext.getBean(OleInvoiceService.class).populateInvoiceFromPurchaseOrders(payDoc,null);
        //SpringContext.getBean(PurapAccountingService.class).updateAccountAmounts(payDoc);

        if ((payDoc.getProrateBy() != null) && (payDoc.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY) || payDoc.getProrateBy().equals(OLEConstants.PRORATE_BY_DOLLAR) || payDoc.getProrateBy().equals(OLEConstants.MANUAL_PRORATE))) {
            // set amounts on any empty
            payDoc.updateExtendedPriceOnItems();

            // calculation just for the tax area, only at tax review stage
            // by now, the general calculation shall have been done.
            if (payDoc.getApplicationDocumentStatus().equals(PurapConstants.PaymentRequestStatuses.APPDOC_AWAITING_TAX_REVIEW)) {
                SpringContext.getBean(OleInvoiceService.class).calculateTaxArea(payDoc);
            }

            // notice we're ignoring whether the boolean, because these are just warnings they shouldn't halt anything
            // Calculate Payment request before rules since the rule check totalAmount.
            SpringContext.getBean(OleInvoiceService.class).calculateInvoice(payDoc, true);
            SpringContext.getBean(KualiRuleService.class).applyRules(
                    new AttributedCalculateAccountsPayableEvent(payDoc));
        } else {
            // set amounts on any empty
            payDoc.updateExtendedPriceOnItems();

            // calculation just for the tax area, only at tax review stage
            // by now, the general calculation shall have been done.
            if (StringUtils.equals(payDoc.getApplicationDocumentStatus(), PurapConstants.PaymentRequestStatuses.APPDOC_AWAITING_TAX_REVIEW)) {
                SpringContext.getBean(OleInvoiceService.class).calculateTaxArea(payDoc);

            }

            // notice we're ignoring whether the boolean, because these are just warnings they shouldn't halt anything
            //Calculate Payment request before rules since the rule check totalAmount.
            //SpringContext.getBean(OleInvoiceService.class).calculateInvoice(payDoc, true);
            SpringContext.getBean(KualiRuleService.class).applyRules(new AttributedCalculateAccountsPayableEvent(payDoc));
        }

        payDoc.setCalculated(true);
/*

        try {
            SpringContext.getBean(PurapAccountingService.class).updateAccountAmounts(payDoc);
            payDoc.refreshAccountSummmary();
        } catch (Exception e) {
            LOG.error("Exception while refreshing the document"+e);
            throw new RuntimeException(e);
        }
*/


        //return mapping.findForward(OLEConstants.MAPPING_BASIC);
        //AccountsPayableActionBase accountsPayableActionBase = new AccountsPayableActionBase();
        //return calculate(mapping, form, request, response);
        return getUIFModelAndView(paymentForm);
    }




    /*@RequestMapping(params = "methodToCall=view")
    public ModelAndView view(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {

        String ckecking;
        OLEInvoiceForm  invform = (OLEInvoiceForm) form;
        InvoiceDocument invoiceDoc = (InvoiceDocument)invform.getDocument();
        List<InvoiceAccount> l=new ArrayList<InvoiceAccount>();
        //l= invoiceDoc.getInvoiceAccount();
        if (l.isEmpty())
        {
            ckecking=new String("SELECT THE ACCOUNT NUMBER");
        }
        else
        {
           // invoiceDoc.setAccountSummary(l);
        }
        Integer c=l.size();
        //invoiceDoc.setValue2(c.toString());
        //invoiceDoc.setValue1("   ");
        return  getUIFModelAndView(invform);
    }*/

    /**
     * Saves the document instance contained on the form
     *
     * @param form - document form base containing the document instance that will be saved
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=save")
    public ModelAndView save(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {

        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setDbRetrieval(false);
        OleInvoiceFundCheckService oleInvoiceFundCheckService = (OleInvoiceFundCheckService) SpringContext
                .getBean("oleInvoiceFundCheckService");
        oleInvoiceDocument.setNoteLine1Text(oleInvoiceDocument.getInvoiceNumber());
        boolean fundCheckFlag = false;
        if (!validateRequiredFields(oleInvoiceDocument,false)) {
            return getUIFModelAndView(oleInvoiceForm);
        }
        if(oleInvoiceDocument.getItems().size() > 4) {
            try {
                calculate(oleInvoiceForm, result, request, response);
            }
            catch (Exception e) {
                LOG.error("Exception while calculating the document"+e);
                throw new RuntimeException(e);
            }
        }
       /* if (SpringContext.getBean(OleInvoiceService.class).getParameterBoolean(OLEConstants.CoreModuleNamespaces.SELECT, OLEConstants.OperationType.SELECT, PurapParameterConstants.ALLOW_INVOICE_SUFF_FUND_CHECK)) {
            if (oleInvoiceDocument.getSourceAccountingLines().size() > 0) {
                fundCheckFlag = oleInvoiceFundCheckService.isBudgetReviewRequired(oleInvoiceDocument);
                if (fundCheckFlag) {
                    return getUIFModelAndView(form);
                }
            }
        }*/
        List<OlePurchaseOrderDocument> olePurchaseOrderDocumentList = oleInvoiceDocument.getPurchaseOrderDocuments();
        for (OlePurchaseOrderDocument olePurchaseOrderDocument : olePurchaseOrderDocumentList) {
            List<OlePurchaseOrderLineForInvoice> olePurchaseOrderLineForInvoiceList = getOleInvoiceItemService().getOlePurchaseOrderLineForInvoice(olePurchaseOrderDocument);
            olePurchaseOrderDocument.setOlePurchaseOrderLineForInvoiceList(olePurchaseOrderLineForInvoiceList);
            List<OlePurchaseOrderTotal> olePurchaseOrderTotalList = getOleInvoiceItemService().getOlePurchaseOrderTotal(olePurchaseOrderDocument);
            olePurchaseOrderDocument.setPurchaseOrderTotalList(olePurchaseOrderTotalList);
        }
        Long nextLinkIdentifier = SpringContext.getBean(SequenceAccessorService.class).getNextAvailableSequenceNumber("AP_PUR_DOC_LNK_ID");
        oleInvoiceDocument.setAccountsPayablePurchasingDocumentLinkIdentifier(nextLinkIdentifier.intValue());
        SpringContext.getBean(OleInvoiceService.class).populateInvoiceDocument(oleInvoiceDocument);
        //oleInvoiceDocument = getInvoiceService().populateInvoiceFromPurchaseOrders(oleInvoiceDocument, null);
        //oleInvoiceForm.setDocument(oleInvoiceDocument);
        boolean duplicationExists = false;
        duplicationExists = getInvoiceService().isDuplicationExists(oleInvoiceDocument,oleInvoiceForm,"save");
        if (duplicationExists) {
            oleInvoiceDocument.setDuplicateSaveFlag(true);
            return getUIFModelAndView(form);
        }
        oleInvoiceDocument.setDuplicateSaveFlag(false);
        getInvoiceService().deleteInvoiceItem(oleInvoiceDocument);
        ModelAndView mv = super.save(form, result, request, response);
        oleInvoiceDocument.loadInvoiceDocument();
        return mv;
    }

    @RequestMapping(params = "methodToCall=continueSave")
    public ModelAndView continueSave(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {

        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setDuplicateSaveFlag(false);
        getInvoiceService().deleteInvoiceItem(oleInvoiceDocument);
        ModelAndView mv = super.save(form, result, request, response);
        oleInvoiceDocument.loadInvoiceDocument();
        return mv;
    }


    /**
     * Routes the document instance contained on the form
     *
     * @param form - document form base containing the document instance that will be routed
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=route")
    public ModelAndView route(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response)  {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setDbRetrieval(false);
        oleInvoiceDocument.setValidationFlag(false);
        List<OlePurchaseOrderDocument> olePurchaseOrderDocumentList = oleInvoiceDocument.getPurchaseOrderDocuments();
        OleInvoiceFundCheckService oleInvoiceFundCheckService = (OleInvoiceFundCheckService) SpringContext
                .getBean("oleInvoiceFundCheckService");
        if(!(SpringContext.getBean(OleInvoiceService.class).validateDepositAccount(oleInvoiceDocument))){
            GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID,
                    PurapKeyConstants.ERROR_INVALID_DEPOSIT_ACCT);
            return getUIFModelAndView(oleInvoiceForm);
        }

        if(! (oleInvoiceDocument.getItems().size() > 0 )){
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectConstant.ERROR_ATLEAST_ONE_ITEM_QTY_REQUIRED);
            return getUIFModelAndView(oleInvoiceForm);
        }
        /*if(olePurchaseOrderDocumentList.size()==0){
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectConstant.ERROR_ATLEAST_ONE_ITEM_QTY_REQUIRED);
            return getUIFModelAndView(oleInvoiceForm);
        }*/
        /*if(oleInvoiceDocument.getPoId().equalsIgnoreCase(null)){
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectConstant.ERROR_ATLEAST_ONE_ITEM_QTY_REQUIRED);
            return getUIFModelAndView(oleInvoiceForm);
        }*/
       /* for (OleInvoiceItem invoiceItem : (List<OleInvoiceItem>) oleInvoiceDocument.getItems()) {
             if (invoiceItem == null ){
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectConstant.ERROR_ATLEAST_ONE_ITEM_QTY_REQUIRED);
                    return getUIFModelAndView(oleInvoiceForm);
             }
        }*/
        boolean fundCheckFlag = false;
        if (!validateRequiredFields(oleInvoiceDocument,true)) {
            return getUIFModelAndView(oleInvoiceForm);
        }
        try {
            SpringContext.getBean(OleInvoiceService.class).populateInvoiceDocument(oleInvoiceDocument);
            calculate(oleInvoiceForm, result, request, response);
        } catch (Exception e) {
            LOG.error("Exception while calculating the document"+e);
            throw new RuntimeException(e);
        }
        oleInvoiceDocument.setNoteLine1Text(oleInvoiceDocument.getInvoiceNumber());

        for (OlePurchaseOrderDocument olePurchaseOrderDocument : olePurchaseOrderDocumentList) {
            List<OlePurchaseOrderLineForInvoice> olePurchaseOrderLineForInvoiceList = getOleInvoiceItemService().getOlePurchaseOrderLineForInvoice(olePurchaseOrderDocument);
            olePurchaseOrderDocument.setOlePurchaseOrderLineForInvoiceList(olePurchaseOrderLineForInvoiceList);
            List<OlePurchaseOrderTotal> olePurchaseOrderTotalList = getOleInvoiceItemService().getOlePurchaseOrderTotal(olePurchaseOrderDocument);
            olePurchaseOrderDocument.setPurchaseOrderTotalList(olePurchaseOrderTotalList);
           /* Added for Grand total*/
            oleInvoiceDocument.setGrandTotal(oleInvoiceDocument.getGrandTotal());
        }
        /*if (SpringContext.getBean(OleInvoiceService.class).getParameterBoolean(OLEConstants.CoreModuleNamespaces.SELECT, OLEConstants.OperationType.SELECT, PurapParameterConstants.ALLOW_INVOICE_SUFF_FUND_CHECK)) {
            if (oleInvoiceDocument.getSourceAccountingLines().size() > 0) {
                fundCheckFlag = oleInvoiceFundCheckService.isBudgetReviewRequired(oleInvoiceDocument);
                if (fundCheckFlag) {
                    return getUIFModelAndView(form);
                }
            }
        }*/
            String subscriptionValidationMessage = getInvoiceService().createSubscriptionDateOverlapQuestionText(oleInvoiceDocument);
            if (!subscriptionValidationMessage.isEmpty() && subscriptionValidationMessage != null){
                oleInvoiceForm.setSubscriptionDateValidationMessage(subscriptionValidationMessage);
                oleInvoiceDocument.setSubscriptionDateValidationFlag(true);
            }

        if (oleInvoiceDocument.getItems() != null && oleInvoiceDocument.getItems().size() > 4 ) {
            String validationMessage = getInvoiceService().createInvoiceNoMatchQuestionText(oleInvoiceDocument);
            if (!validationMessage.isEmpty() && validationMessage != null){
                oleInvoiceForm.setValidationMessage(validationMessage);
                oleInvoiceDocument.setValidationFlag(true);
            }
        }
        if(oleInvoiceDocument.getSourceAccountingLines().size() > 0) {
            if (SpringContext.getBean(OleInvoiceService.class).getParameterBoolean(OLEConstants.CoreModuleNamespaces.SELECT, OLEConstants.OperationType.SELECT, PurapParameterConstants.ALLOW_INVOICE_SUFF_FUND_CHECK)) {
                if (oleInvoiceDocument.getPaymentMethodIdentifier() != null && (SpringContext.getBean(OleInvoiceService.class).getPaymentMethodType(oleInvoiceDocument.getPaymentMethodIdentifier())).equals(OLEConstants.DEPOSIT)) {
                } else {
                    List<SourceAccountingLine> sourceAccountingLineList = oleInvoiceDocument.getSourceAccountingLines();
                    for (SourceAccountingLine accLine : sourceAccountingLineList) {
                        Map searchMap = new HashMap();
                        String notificationOption = null;
                        boolean sufficientFundCheck;
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
                        if (notificationOption != null && notificationOption.equals(OLEPropertyConstants.BLOCK_USE)) {
                            sufficientFundCheck = oleInvoiceFundCheckService.hasSufficientFundCheckRequired(accLine);
                            if (sufficientFundCheck) {
                                GlobalVariables.getMessageMap().putError(
                                        OLEConstants.SufficientFundCheck.ERROR_MSG_FOR_INSUFF_FUND, RiceKeyConstants.ERROR_CUSTOM,
                                        OLEConstants.SufficientFundCheck.INSUFF_FUND_INV + accLine.getAccountNumber());
                                return getUIFModelAndView(oleInvoiceForm);
                            }
                        } else if (notificationOption != null && notificationOption.equals(OLEPropertyConstants.WARNING_MSG)) {
                            sufficientFundCheck = oleInvoiceFundCheckService.hasSufficientFundCheckRequired(accLine);
                            oleInvoiceDocument.setSfcFlag(sufficientFundCheck);
                            if (sufficientFundCheck) {
                                oleInvoiceForm.setSubscriptionDateValidationMessage(null);
                                oleInvoiceDocument.setSubscriptionDateValidationFlag(false);
                                oleInvoiceDocument.setSfcFlag(sufficientFundCheck);
                                oleInvoiceForm.setSfcFailRouteMsg(OLEConstants.INV_INSUFF_FUND + accLine.getAccountNumber());
                                return getUIFModelAndView(oleInvoiceForm);
                            }
                        }
                    }
                }
            }
        }
        boolean duplicationExists = false;
        duplicationExists = getInvoiceService().isDuplicationExists(oleInvoiceDocument, oleInvoiceForm, "route");
        if (duplicationExists) {
            oleInvoiceDocument.setDuplicateRouteFlag(true);
            return getUIFModelAndView(form);
        }
        oleInvoiceDocument.setDuplicateRouteFlag(false);
        if (oleInvoiceDocument.isValidationFlag() || oleInvoiceDocument.isSubscriptionDateValidationFlag()) {
            return getUIFModelAndView(oleInvoiceForm);
        }

        getInvoiceService().deleteInvoiceItem(oleInvoiceDocument);
        ModelAndView mv = super.route(oleInvoiceForm, result, request, response);
        oleInvoiceDocument.loadInvoiceDocument();
        if (GlobalVariables.getMessageMap().getErrorMessages().size() > 0) {
            return mv;
        }
        return closeDocument(oleInvoiceForm, result, request, response);

        /*
        if (oleInvoiceDocument.getDocumentHeader() != null && oleInvoiceDocument.getDocumentHeader().getWorkflowDocument() != null &&
                oleInvoiceDocument.getDocumentHeader().getWorkflowDocument().isSaved()) {
            if (oleInvoiceDocument.getSourceAccountingLines().size() > 0) {
                fundCheckFlag = oleInvoiceFundCheckService.isBudgetReviewRequired(oleInvoiceDocument);
                if (fundCheckFlag) {
                    return getUIFModelAndView(form);
                }
            }
            performWorkflowAction(form, UifConstants.WorkflowAction.ROUTE, true);
            return getUIFModelAndView(oleInvoiceForm);
        } else {
            SpringContext.getBean(OleInvoiceService.class).populateInvoiceFromPurchaseOrders(oleInvoiceDocument, null);
            if (oleInvoiceDocument.getSourceAccountingLines().size() > 0) {
                fundCheckFlag = oleInvoiceFundCheckService.isBudgetReviewRequired(oleInvoiceDocument);
                if (fundCheckFlag) {
                    return getUIFModelAndView(form);
                }
            }
            performWorkflowAction(form, UifConstants.WorkflowAction.ROUTE, true);
        }
        return getUIFModelAndView(oleInvoiceForm);*/
    }


    @RequestMapping(params = "methodToCall=continueDuplicateRoute")
    public ModelAndView continueDuplicateRoute(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceForm.setDuplicationMessage(null);
        oleInvoiceDocument.setDuplicateRouteFlag(false);
        String validationMessage = getInvoiceService().createInvoiceNoMatchQuestionText(oleInvoiceDocument);
        if (!validationMessage.isEmpty() && validationMessage != null) {
            oleInvoiceForm.setValidationMessage(validationMessage);
            oleInvoiceDocument.setValidationFlag(true);
            return getUIFModelAndView(oleInvoiceForm);
        }
        else {
            oleInvoiceDocument.setValidationFlag(false);
            getInvoiceService().deleteInvoiceItem(oleInvoiceDocument);
            ModelAndView mv = super.route(oleInvoiceForm, result, request, response);
            if (GlobalVariables.getMessageMap().getErrorMessages().size() > 0) {
                return mv;
            }
            oleInvoiceDocument.setUnsaved(false);
            return closeDocument(oleInvoiceForm, result, request, response);
        }
    }

    @RequestMapping(params = "methodToCall=continueRoute")
    public ModelAndView continueRoute(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setSfcFlag(false);
        oleInvoiceForm.setValidationMessage(null);
        oleInvoiceDocument.setValidationFlag(false);
        String subscriptionValidationMessage = getInvoiceService().createSubscriptionDateOverlapQuestionText(oleInvoiceDocument);
        if (!subscriptionValidationMessage.isEmpty() && subscriptionValidationMessage != null){
            oleInvoiceForm.setSubscriptionDateValidationMessage(subscriptionValidationMessage);
            oleInvoiceDocument.setSubscriptionDateValidationFlag(true);
        }
        if(oleInvoiceDocument.isSubscriptionDateValidationFlag()) {
            return getUIFModelAndView(form);
        }
        String validationMessage = getInvoiceService().createInvoiceNoMatchQuestionText(oleInvoiceDocument);
        if (!validationMessage.isEmpty() && validationMessage != null){
            oleInvoiceForm.setValidationMessage(validationMessage);
            oleInvoiceDocument.setValidationFlag(true);
        }
        else {
            oleInvoiceDocument.setValidationFlag(false);
            performWorkflowAction(form, UifConstants.WorkflowAction.ROUTE, true);
        }
        if(GlobalVariables.getMessageMap().getErrorMessages().size() > 0){
            return getUIFModelAndView(form);
        }
        return closeDocument(oleInvoiceForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=cancelRoute")
    public ModelAndView cancelRoute(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setSfcFlag(false);
        oleInvoiceForm.setSfcFailRouteMsg(null);
        return getUIFModelAndView(form);
    }


    public static OleInvoiceService getInvoiceService() {
        if (invoiceService == null) {
            invoiceService = SpringContext.getBean(OleInvoiceService.class);
        }
        return invoiceService;
    }

    /**
     * Performs the blanket approve workflow action on the form document instance
     *
     * @param form - document form base containing the document instance that will be blanket approved
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=blanketApprove")
    public ModelAndView blanketApprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;

        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setBlanketApproveValidationFlag(false);
        oleInvoiceDocument.setBlanketApproveSubscriptionDateValidationFlag(false);
        oleInvoiceDocument.setDbRetrieval(false);
        OleInvoiceFundCheckService oleInvoiceFundCheckService = (OleInvoiceFundCheckService) SpringContext
                .getBean("oleInvoiceFundCheckService");
        if(!(SpringContext.getBean(OleInvoiceService.class).validateDepositAccount(oleInvoiceDocument))){
            GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID,
                    PurapKeyConstants.ERROR_INVALID_DEPOSIT_ACCT);
            return getUIFModelAndView(oleInvoiceForm);
        }
        if(! (oleInvoiceDocument.getItems().size() > 0 )){
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OleSelectConstant.ERROR_ATLEAST_ONE_ITEM_QTY_REQUIRED);
            return getUIFModelAndView(oleInvoiceForm);
        }
        if (!validateRequiredFields(oleInvoiceDocument,true)) {
            return getUIFModelAndView(oleInvoiceForm);
        }
        try {
            SpringContext.getBean(OleInvoiceService.class).populateInvoiceDocument(oleInvoiceDocument);
            calculate(oleInvoiceForm, result, request, response);
        } catch (Exception e) {
            LOG.error("Exception while calculating the document"+e);
            throw new RuntimeException(e);
        }
        oleInvoiceDocument.setNoteLine1Text(oleInvoiceDocument.getInvoiceNumber());
        List<OlePurchaseOrderDocument> olePurchaseOrderDocumentList = oleInvoiceDocument.getPurchaseOrderDocuments();
        for (OlePurchaseOrderDocument olePurchaseOrderDocument : olePurchaseOrderDocumentList) {
            List<OlePurchaseOrderLineForInvoice> olePurchaseOrderLineForInvoiceList = getOleInvoiceItemService().getOlePurchaseOrderLineForInvoice(olePurchaseOrderDocument);
            olePurchaseOrderDocument.setOlePurchaseOrderLineForInvoiceList(olePurchaseOrderLineForInvoiceList);
            List<OlePurchaseOrderTotal> olePurchaseOrderTotalList = getOleInvoiceItemService().getOlePurchaseOrderTotal(olePurchaseOrderDocument);
            olePurchaseOrderDocument.setPurchaseOrderTotalList(olePurchaseOrderTotalList);
        }
        boolean duplicationExists = false;
        duplicationExists = getInvoiceService().isDuplicationExists(oleInvoiceDocument,oleInvoiceForm,"approve");
        if (duplicationExists) {
            oleInvoiceDocument.setDuplicateApproveFlag(true);
            return getUIFModelAndView(form);
        }
        oleInvoiceDocument.setDuplicateApproveFlag(false);
        boolean amountExceedsForBlanketApprove = false;
        amountExceedsForBlanketApprove = getInvoiceService().isNotificationRequired(oleInvoiceDocument);
        if (amountExceedsForBlanketApprove) {
            oleInvoiceForm.setAmountExceedsMesgForBlankApp(getInvoiceService().createInvoiceAmountExceedsThresholdText(oleInvoiceDocument));
            oleInvoiceDocument.setAmountExceedsForBlanketApprove(true);
            return getUIFModelAndView(form);
        }
        /*boolean fundCheckFlag = false;
        if (SpringContext.getBean(OleInvoiceService.class).getParameterBoolean(OLEConstants.CoreModuleNamespaces.SELECT, OLEConstants.OperationType.SELECT, PurapParameterConstants.ALLOW_INVOICE_SUFF_FUND_CHECK)) {
            if (oleInvoiceDocument.getSourceAccountingLines().size() > 0) {
                fundCheckFlag = oleInvoiceFundCheckService.isBudgetReviewRequired(oleInvoiceDocument);
                if (fundCheckFlag || oleInvoiceDocument.isBlanketApproveValidationFlag() || oleInvoiceDocument.isBlanketApproveSubscriptionDateValidationFlag()) {
                    return getUIFModelAndView(form);
                }
            }
        }*/
            String subscriptionValidationMessage = getInvoiceService().createSubscriptionDateOverlapQuestionText(oleInvoiceDocument);
            if (!subscriptionValidationMessage.isEmpty() && subscriptionValidationMessage != null){
                oleInvoiceForm.setSubscriptionValidationMessage(subscriptionValidationMessage);
                oleInvoiceDocument.setBlanketApproveSubscriptionDateValidationFlag(true);
            }
        if (oleInvoiceDocument.getItems() != null && oleInvoiceDocument.getItems().size() > 4 ) {
            String validationMessage = getInvoiceService().createInvoiceNoMatchQuestionText(oleInvoiceDocument);
            if (!validationMessage.isEmpty() && validationMessage != null){
                oleInvoiceForm.setValidationMessage(validationMessage);
                oleInvoiceDocument.setBlanketApproveValidationFlag(true);
            }
        }
        if (oleInvoiceDocument.getSourceAccountingLines().size() > 0) {
            if (SpringContext.getBean(OleInvoiceService.class).getParameterBoolean(OLEConstants.CoreModuleNamespaces.SELECT, OLEConstants.OperationType.SELECT, PurapParameterConstants.ALLOW_INVOICE_SUFF_FUND_CHECK)) {
                if (oleInvoiceDocument.getPaymentMethodIdentifier() != null && (SpringContext.getBean(OleInvoiceService.class).getPaymentMethodType(oleInvoiceDocument.getPaymentMethodIdentifier())).equals(OLEConstants.DEPOSIT)) {
                } else {
                    List<SourceAccountingLine> sourceAccountingLineList = oleInvoiceDocument.getSourceAccountingLines();
                    for (SourceAccountingLine accLine : sourceAccountingLineList) {
                        Map searchMap = new HashMap();
                        String notificationOption = null;
                        boolean sufficientFundCheck;
                        Map<String, Object> key = new HashMap<String, Object>();
                        String chartCode = accLine.getChartOfAccountsCode();
                        String accNo = accLine.getAccountNumber();
                        key.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
                        key.put(OLEPropertyConstants.ACCOUNT_NUMBER, accNo);
                        OleSufficientFundCheck account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(
                                OleSufficientFundCheck.class, key);
                        if (account != null) {
                            notificationOption = account.getNotificationOption();
                        }
                        if (notificationOption != null && notificationOption.equals(OLEPropertyConstants.BLOCK_USE)) {
                            sufficientFundCheck = oleInvoiceFundCheckService.hasSufficientFundCheckRequired(accLine);
                            if (sufficientFundCheck) {
                                GlobalVariables.getMessageMap().putError(
                                        OLEConstants.SufficientFundCheck.ERROR_MSG_FOR_INSUFF_FUND, RiceKeyConstants.ERROR_CUSTOM,
                                        OLEConstants.SufficientFundCheck.INSUFF_FUND_INV + accLine.getAccountNumber());
                                return getUIFModelAndView(oleInvoiceForm);
                            }
                        } else if (notificationOption != null && notificationOption.equals(OLEPropertyConstants.WARNING_MSG)) {
                            sufficientFundCheck = oleInvoiceFundCheckService.hasSufficientFundCheckRequired(accLine);
                            if (sufficientFundCheck) {
                                oleInvoiceDocument.setBaSfcFlag(sufficientFundCheck);
                                oleInvoiceForm.setSfcFailApproveMsg(OLEConstants.INV_INSUFF_FUND + accLine.getAccountNumber());
                                return getUIFModelAndView(oleInvoiceForm);
                            }
                        }
                    }
                }
            }
        }

        /*if (oleInvoiceDocument.getDocumentHeader() != null && oleInvoiceDocument.getDocumentHeader().getWorkflowDocument() != null &&
                !oleInvoiceDocument.getDocumentHeader().getWorkflowDocument().isSaved()) {
            SpringContext.getBean(OleInvoiceService.class).populateInvoiceFromPurchaseOrders(oleInvoiceDocument, null);

            boolean fundCheckFlag = false;
            if (oleInvoiceDocument.getSourceAccountingLines().size() > 0) {
                fundCheckFlag = oleInvoiceFundCheckService.isBudgetReviewRequired(oleInvoiceDocument);
                if (fundCheckFlag) {
                    return getUIFModelAndView(form);
                }
            }
        }*/



        if(oleInvoiceDocument.isBlanketApproveValidationFlag() || oleInvoiceDocument.isBlanketApproveSubscriptionDateValidationFlag()){
            return getUIFModelAndView(oleInvoiceForm);
        }

       /* try {
            calculate(oleInvoiceForm,result,request,response);
        }   catch(Exception e) {
            LOG.error("Error while calculating Invoice Document");
            throw new RuntimeException(e);
        }*/
        getInvoiceService().deleteInvoiceItem(oleInvoiceDocument);
        super.blanketApprove(oleInvoiceForm, result, request, response);
        oleInvoiceDocument.loadInvoiceDocument();
        if (GlobalVariables.getMessageMap().getErrorCount() > 0) {
            oleInvoiceDocument.setUnsaved(false);
            return getUIFModelAndView(oleInvoiceForm);
        }
        GlobalVariables.getMessageMap().clearErrorMessages();
        oleInvoiceDocument.setUnsaved(false);
        //return getUIFModelAndView(oleInvoiceForm);
        return closeDocument(oleInvoiceForm,result,request,response);
    }


    @RequestMapping(params = "methodToCall=continueBlanketApprove")
    public ModelAndView continueBlanketApprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                               HttpServletRequest request, HttpServletResponse response)  throws Exception{
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setDuplicateApproveFlag(false);
        oleInvoiceDocument.setBaSfcFlag(false);
        oleInvoiceDocument.setBlanketApproveValidationFlag(false);
        String subscriptionValidationMessage = getInvoiceService().createSubscriptionDateOverlapQuestionText(oleInvoiceDocument);
        if (!subscriptionValidationMessage.isEmpty() && subscriptionValidationMessage != null){
            oleInvoiceForm.setSubscriptionValidationMessage(subscriptionValidationMessage);
            oleInvoiceDocument.setBlanketApproveSubscriptionDateValidationFlag(true);
        }
        if(oleInvoiceDocument.isValidationFlag() || oleInvoiceDocument.isBlanketApproveValidationFlag() || oleInvoiceDocument.isBlanketApproveSubscriptionDateValidationFlag()){
            return getUIFModelAndView(oleInvoiceForm);
        }
       String validationMessage = getInvoiceService().createInvoiceNoMatchQuestionText(oleInvoiceDocument);
        if (!validationMessage.isEmpty() && validationMessage != null){
            oleInvoiceForm.setValidationMessage(validationMessage);
            oleInvoiceDocument.setBlanketApproveValidationFlag(true);
        }
        else {
            oleInvoiceDocument.setBlanketApproveValidationFlag(false);
            getInvoiceService().deleteInvoiceItem(oleInvoiceDocument);
            super.blanketApprove(oleInvoiceForm, result, request, response);
            if (GlobalVariables.getMessageMap().getErrorCount() > 0) {
                return getUIFModelAndView(oleInvoiceForm);
            }
            oleInvoiceDocument.setUnsaved(false);
            //GlobalVariables.getMessageMap().clearErrorMessages();
            return closeDocument(form,result,request,response);
        }
        if(oleInvoiceDocument.isValidationFlag() || oleInvoiceDocument.isBlanketApproveValidationFlag() || oleInvoiceDocument.isBlanketApproveSubscriptionDateValidationFlag()){
            return getUIFModelAndView(oleInvoiceForm);
        }
        return closeDocument(oleInvoiceForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=cancelBlanketApprove")
    public ModelAndView cancelBlanketApprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response) {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setBaSfcFlag(false);
        oleInvoiceForm.setSfcFailApproveMsg(null);
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=continueInvoiceRoute")
    public ModelAndView continueInvoiceRoute(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response)  throws Exception{
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setValidationFlag(false);
        oleInvoiceDocument.setValidationFlag(false);
        performWorkflowAction(form, UifConstants.WorkflowAction.ROUTE, true);
        if(GlobalVariables.getMessageMap().getErrorMessages().size() > 0){
            return getUIFModelAndView(form);
        }
        return closeDocument(oleInvoiceForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=cancelInvoiceRoute")
    public ModelAndView cancelInvoiceRoute(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setValidationFlag(false);
        oleInvoiceForm.setValidationMessage(null);
        oleInvoiceDocument.setBlanketApproveSubscriptionDateValidationFlag(false);
        oleInvoiceForm.setSubscriptionValidationMessage(null);

        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=continueInvoiceSubscriptionApprove")
    public ModelAndView continueInvoiceSubscriptionApprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setSfcFlag(false);
        oleInvoiceDocument.setSubscriptionDateValidationFlag(false);

        String validationMessage = getInvoiceService().createInvoiceNoMatchQuestionText(oleInvoiceDocument);
        if (!validationMessage.isEmpty() && validationMessage != null){
            oleInvoiceForm.setValidationMessage(validationMessage);
            oleInvoiceDocument.setValidationFlag(true);
        }
        else {
            oleInvoiceDocument.setValidationFlag(false);
            performWorkflowAction(form, UifConstants.WorkflowAction.ROUTE, true);
        }
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=cancelInvoiceSubscriptionApprove")
    public ModelAndView cancelInvoiceSubscriptionApprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setSfcFlag(false);
        oleInvoiceForm.setSfcFailRouteMsg(null);
        return getUIFModelAndView(form);
    }


    @RequestMapping(params = "methodToCall=continueInvoiceBlanketApprove")
    public ModelAndView continueInvoiceBlanketApprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                                      HttpServletRequest request, HttpServletResponse response)  throws Exception{
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setValidationFlag(false);
        getInvoiceService().deleteInvoiceItem(oleInvoiceDocument);
        super.blanketApprove(oleInvoiceForm, result, request, response);
        if (GlobalVariables.getMessageMap().getErrorCount() > 0) {
            return getUIFModelAndView(oleInvoiceForm);
        }
        GlobalVariables.getMessageMap().clearErrorMessages();
        return closeDocument(oleInvoiceForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=cancelInvoiceBlanketApprove")
    public ModelAndView cancelInvoiceBlanketApprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                                    HttpServletRequest request, HttpServletResponse response) {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setValidationFlag(false);
        oleInvoiceForm.setValidationMessage(null);
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=continueInvoiceSubscriptionBlanketApprove")
    public ModelAndView continueInvoiceSubscriptionBlanketApprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                                      HttpServletRequest request, HttpServletResponse response)  throws Exception{
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setBlanketApproveSubscriptionDateValidationFlag(false);
        String validationMessage = getInvoiceService().createInvoiceNoMatchQuestionText(oleInvoiceDocument);
        if (!validationMessage.isEmpty() && validationMessage != null){
            oleInvoiceForm.setValidationMessage(validationMessage);
            oleInvoiceDocument.setValidationFlag(true);
            oleInvoiceDocument.setBlanketApproveValidationFlag(true);
        }
        else {
            oleInvoiceDocument.setBlanketApproveValidationFlag(false);
            getInvoiceService().deleteInvoiceItem(oleInvoiceDocument);
            super.blanketApprove(oleInvoiceForm, result, request, response);
            if (GlobalVariables.getMessageMap().getErrorCount() > 0) {
                return getUIFModelAndView(oleInvoiceForm);
            }
            oleInvoiceDocument.setUnsaved(false);
            //GlobalVariables.getMessageMap().clearErrorMessages();
            return closeDocument(form,result,request,response);
        }
        if(oleInvoiceDocument.isValidationFlag() || oleInvoiceDocument.isBlanketApproveSubscriptionDateValidationFlag()){
            return getUIFModelAndView(oleInvoiceForm);
        }
        getInvoiceService().deleteInvoiceItem(oleInvoiceDocument);
        super.blanketApprove(oleInvoiceForm, result, request, response);
        if (GlobalVariables.getMessageMap().getErrorCount() > 0) {
            oleInvoiceDocument.setUnsaved(false);
            return getUIFModelAndView(oleInvoiceForm);
        }
        GlobalVariables.getMessageMap().clearErrorMessages();
        oleInvoiceDocument.setUnsaved(false);
        return closeDocument(oleInvoiceForm,result,request,response);
    }

    @RequestMapping(params = "methodToCall=cancelInvoiceSubscriptionBlanketApprove")
    public ModelAndView cancelInvoiceSubscriptionBlanketApprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                                    HttpServletRequest request, HttpServletResponse response) {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setBlanketApproveSubscriptionDateValidationFlag(false);
        oleInvoiceForm.setSubscriptionValidationMessage(null);
        return getUIFModelAndView(form);
    }

    /**
     * @see org.kuali.rice.kns.web.struts.action.KualiDocumentActionBase#loadDocument(org.kuali.rice.kns.web.struts.form.KualiDocumentFormBase)
     */
    @Override
    protected void loadDocument(DocumentFormBase formBase) throws WorkflowException {
        OLEInvoiceForm invoiceForm = (OLEInvoiceForm) formBase;
        super.loadDocument(invoiceForm);
        OleInvoiceDocument invoiceDocument = (OleInvoiceDocument) invoiceForm.getDocument();
        //invoiceDocument.setItems(new ArrayList());
        OLEInvoiceDaoOjb oleInvoiceDaoOjb = (OLEInvoiceDaoOjb)SpringContext.getBean("oleInvoiceDao");
        invoiceDocument.setDbRetrieval(true);
        invoiceDocument.setGrantTotal(oleInvoiceDaoOjb.getInvoiceTotal(invoiceDocument.getPurapDocumentIdentifier(),null ).toString());
        invoiceDocument.setItemTotal(oleInvoiceDaoOjb.getInvoiceTotal(invoiceDocument.getPurapDocumentIdentifier(), "ITEM").toString());
        if(invoiceDocument.getForeignVendorInvoiceAmount() != null) {
            if (invoiceDocument.getForeignVendorInvoiceAmount().equals(new BigDecimal("0.00")) && invoiceDocument.getVendorInvoiceAmount().equals(new KualiDecimal("0.00"))
                    && !new KualiDecimal(invoiceDocument.getGrantTotal()).equals(new KualiDecimal("0.00"))) {
                invoiceDocument.setVendorInvoiceAmount(new KualiDecimal(oleInvoiceDaoOjb.getInvoiceTotal(invoiceDocument.getPurapDocumentIdentifier(), null).toString()));
            }
        }
        if (invoiceDocument.getInvoiceCurrencyTypeId()!=null) {
            String currencyType = getInvoiceService().getCurrencyType(invoiceDocument.getInvoiceCurrencyTypeId().toString());
            if (StringUtils.isNotBlank(currencyType)) {
                if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                    invoiceDocument.setForeignGrandTotal(oleInvoiceDaoOjb.getForeignInvoiceTotal(invoiceDocument.getPurapDocumentIdentifier(),null ).toString());
                    invoiceDocument.setForeignItemTotal(oleInvoiceDaoOjb.getForeignInvoiceTotal(invoiceDocument.getPurapDocumentIdentifier(), "ITEM").toString());
                }   else {
                    invoiceDocument.setForeignGrandTotal(OLEConstants.EMPTY_STRING);
                    invoiceDocument.setForeignItemTotal(OLEConstants.EMPTY_STRING);
                }
            }
        }
        invoiceDocument.setDocumentTotalAmount(invoiceDocument.getGrantTotal());
        // refresh the account summary (note this also updates the account amounts)
        invoiceDocument.refreshAccountSummmary();

        // Sorting the purchase order item base on item identifier. Modified for Jira OLE-5297
        Collections.sort(invoiceDocument.getItems(),new Comparator<OleInvoiceItem>(){
            public int compare(OleInvoiceItem item1,OleInvoiceItem item2){
                if(item1.getSequenceNumber() != null && item2.getSequenceNumber() != null) {
                    return item1.getSequenceNumber().compareTo(item2.getSequenceNumber());
                }
                return 0;
            }
        });


        for (org.kuali.rice.krad.bo.Note note : (java.util.List<org.kuali.rice.krad.bo.Note>) invoiceDocument.getNotes()) {
            note.refreshReferenceObject("attachment");
        }

        // sort the below the line
        SpringContext.getBean(PurapService.class).sortBelowTheLine(invoiceDocument);
    }

    /**
     * This method to refresh the account summary.
     */
    @RequestMapping(params = "methodToCall=refreshAccountSummary")
    public ModelAndView refreshAccountSummary(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument document = (OleInvoiceDocument) form.getDocument();
        SpringContext.getBean(PurapAccountingService.class).updateAccountAmounts(document);
        document.refreshAccountSummmary();
        document.setUnsaved(false);
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=proratedSurchargeRefresh")
    public ModelAndView proratedSurchargeRefresh(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                 HttpServletRequest request, HttpServletResponse response) {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setDbRetrieval(false);
        OlePurapAccountingService olePurapAccountingService = new OlePurapAccountingServiceImpl();
        boolean canProrate = false;
        KualiDecimal totalAmt = oleInvoiceDocument.getInvoicedItemTotal() != null ?
                new KualiDecimal(oleInvoiceDocument.getInvoicedItemTotal()) : KualiDecimal.ZERO;
        if(SpringContext.getBean(OleInvoiceService.class).getPaymentMethodType(oleInvoiceDocument.getPaymentMethodIdentifier()).equals(OLEConstants.DEPOSIT))       {
            for(OleInvoiceItem item : (List<OleInvoiceItem>)oleInvoiceDocument.getItems()){
                if(item.getItemTypeCode().equals("ITEM")){
                    if(item.getSourceAccountingLineList().size() <=0){
                        GlobalVariables.getMessageMap().putError(PurapPropertyConstants.OFFSET_ACCT_LINE, OLEKeyConstants.ERROR_REQUIRED, PurapConstants.PRQSDocumentsStrings.OFFSET_ACCT_LINE);
                        return getUIFModelAndView(oleInvoiceForm);
                    }

                }
            }
        }
        for (InvoiceItem item : (List<InvoiceItem>) oleInvoiceDocument.getItems()) {
            if (item.getItemType().isAdditionalChargeIndicator()) {
                List<PurApItem> items = new ArrayList<>();
                BigDecimal exchangeRate;
                if (items.size() == 0) {
                    for (OleInvoiceItem invoiceItem : (List<OleInvoiceItem>) oleInvoiceDocument.getItems()) {
                        if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyType())) {
                            String currencyType = getInvoiceService().getCurrencyType(oleInvoiceDocument.getInvoiceCurrencyType());
                            if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                                if (StringUtils.isNotBlank(invoiceItem.getAdditionalForeignUnitCost()) && invoiceItem.getItemType().isAdditionalChargeIndicator()) {
                                    if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyExchangeRate())) {
                                        try {
                                            Double.parseDouble(oleInvoiceDocument.getInvoiceCurrencyExchangeRate());
                                            exchangeRate = new BigDecimal(oleInvoiceDocument.getInvoiceCurrencyExchangeRate());
                                            if (new KualiDecimal(exchangeRate).isZero()) {
                                                GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_ENTER_VALID_EXCHANGE_RATE);
                                                return getUIFModelAndView(oleInvoiceForm);
                                            }
                                            oleInvoiceDocument.setInvoiceCurrencyExchangeRate(exchangeRate.toString());
                                        }
                                        catch (NumberFormatException nfe) {
                                            GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_ENTER_VALID_EXCHANGE_RATE);
                                            return getUIFModelAndView(oleInvoiceForm);
                                        }
                                    }  else {
                                        GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_EXCHANGE_RATE_EMPTY, currencyType);
                                        return getUIFModelAndView(oleInvoiceForm);
                                    }
                                    KualiDecimal itemUnitPrice = new KualiDecimal((new BigDecimal(invoiceItem.getAdditionalForeignUnitCost()).
                                            divide(new BigDecimal(oleInvoiceDocument.getInvoiceCurrencyExchangeRate()), 4, RoundingMode.HALF_UP)));
                                    if (!itemUnitPrice.bigDecimalValue().equals(invoiceItem.getItemUnitPrice())) {
                                        invoiceItem.setItemUnitPrice((new BigDecimal(invoiceItem.getAdditionalForeignUnitCost()).
                                                divide(new BigDecimal(oleInvoiceDocument.getInvoiceCurrencyExchangeRate()), 4, RoundingMode.HALF_UP)).abs());
                                    }
                                }   else if (StringUtils.isBlank(invoiceItem.getAdditionalForeignUnitCost()) && invoiceItem.getItemType().isAdditionalChargeIndicator()) {
                                    invoiceItem.setItemUnitPrice(null);
                                }
                            }
                        }
                        if (invoiceItem.getItemType().isQuantityBasedGeneralLedgerIndicator() && invoiceItem.getItemUnitPrice().compareTo(BigDecimal.ZERO) != 0 ) {
                            canProrate = true;
                        }
                        items.add(invoiceItem);
                    }
                }
                else {
                    for (OleInvoiceItem invoiceItem : (List<OleInvoiceItem>) oleInvoiceDocument.getItems()) {
                        if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyType())) {
                            String currencyType = getInvoiceService().getCurrencyType(oleInvoiceDocument.getInvoiceCurrencyType());
                            if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                                if (StringUtils.isNotBlank(invoiceItem.getAdditionalForeignUnitCost()) && invoiceItem.getItemType().isAdditionalChargeIndicator()) {
                                    if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyExchangeRate())) {
                                        try {
                                            Double.parseDouble(oleInvoiceDocument.getInvoiceCurrencyExchangeRate());
                                            exchangeRate = new BigDecimal(oleInvoiceDocument.getInvoiceCurrencyExchangeRate());
                                            if (new KualiDecimal(exchangeRate).isZero()) {
                                                GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_ENTER_VALID_EXCHANGE_RATE);
                                                return getUIFModelAndView(oleInvoiceForm);
                                            }
                                            oleInvoiceDocument.setInvoiceCurrencyExchangeRate(exchangeRate.toString());
                                        }
                                        catch (NumberFormatException nfe) {
                                            GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_ENTER_VALID_EXCHANGE_RATE);
                                            return getUIFModelAndView(oleInvoiceForm);
                                        }
                                    }  else {
                                        GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_EXCHANGE_RATE_EMPTY, currencyType);
                                        return getUIFModelAndView(oleInvoiceForm);
                                    }
                                    invoiceItem.setItemUnitPrice(invoiceItem.getItemForeignUnitCost().bigDecimalValue().divide(exchangeRate.setScale(2, RoundingMode.HALF_UP)));

                                }
                            }
                        }
                        if (!(invoiceItem.getItemType().isQuantityBasedGeneralLedgerIndicator()) && invoiceItem.getItemUnitPrice().compareTo(BigDecimal.ZERO) != 0 ) {
                            canProrate = true;
                            items.add(invoiceItem);
                        }
                    }
                }
                if ((totalAmt.isZero() || !canProrate)&& oleInvoiceDocument.isProrateDollar() ) {
                    GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_ADDITIONAL_CHARGE_SECTION_ID,
                            OLEKeyConstants.ERROR_PRORATE_DOLLAR_ZERO_ITEM_TOTAL);
                    oleInvoiceDocument.setProrateDollar(false);
                    oleInvoiceDocument.setProrateManual(false);
                    oleInvoiceDocument.setProrateQty(false);
                    oleInvoiceDocument.setNoProrate(false);
                    oleInvoiceDocument.setProrateBy(null);
                    for (OleInvoiceItem invoiceItem : (List<OleInvoiceItem>) oleInvoiceDocument.getItems()) {
                        if (invoiceItem.getItemType().isAdditionalChargeIndicator()) {
                            invoiceItem.setSourceAccountingLines(new ArrayList<PurApAccountingLine>());
                        }
                    }
                    return getUIFModelAndView(oleInvoiceForm);
                }
                List<PurApAccountingLine> distributedAccounts = null;
                List<SourceAccountingLine> summaryAccounts = null;
                summaryAccounts = olePurapAccountingService.generateSummaryForManual(items);
                distributedAccounts = olePurapAccountingService.generateAccountDistributionForProrationByManual(summaryAccounts,InvoiceAccount.class);
                if (CollectionUtils.isNotEmpty(distributedAccounts)) {
                    item.setSourceAccountingLines(distributedAccounts);
                }
                if(oleInvoiceDocument.isNoProrate() && item.getItemType().isAdditionalChargeIndicator()){
                    item.setSourceAccountingLines(new ArrayList<PurApAccountingLine>());
                }
                else if (oleInvoiceDocument.isProrateDollar() || oleInvoiceDocument.isProrateQty()) {
                    SpringContext.getBean(OleInvoiceService.class).calculateInvoice(oleInvoiceDocument, true);
                }
                for(PurApAccountingLine oldSourceAccountingLine:item.getSourceAccountingLines()) {
                    if(oldSourceAccountingLine instanceof InvoiceAccount) {
                        ((InvoiceAccount)oldSourceAccountingLine).setExistingAmount(oldSourceAccountingLine.getAmount());
                    }
                }
            }

        }
        return getUIFModelAndView(oleInvoiceForm);   /*,"OLEInvoiceView-MainPage"*/
    }

    @RequestMapping(params = "methodToCall=deletePO")
    public ModelAndView deletePurchaseOrder(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                            HttpServletRequest request, HttpServletResponse response) {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setDbRetrieval(false);
        String focusId = oleInvoiceForm.getFocusId();
        String s = focusId.substring(focusId.length() - 1, focusId.length());
        int deleteDocument = Integer.parseInt(s);
        List<OlePurchaseOrderDocument> olePurchaseOrderDocument = oleInvoiceDocument.getPurchaseOrderDocuments();
        olePurchaseOrderDocument.remove(deleteDocument);
        oleInvoiceDocument.setPurchaseOrderDocuments(olePurchaseOrderDocument);
        return getUIFModelAndView(oleInvoiceForm);
    }


    @RequestMapping(params = "methodToCall=fillInvoiceNumber")
    public ModelAndView fillInvoiceNumber(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setNoteLine1Text(oleInvoiceDocument.getInvoiceNumber());
        return getUIFModelAndView(oleInvoiceForm);
    }

    @RequestMapping(params = "methodToCall=refreshCurrentItem")
    public ModelAndView refreshCurrentItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        return navigate(form, result, request, response);
    }


    /**
     * Called by the add line action for a new collection line. Method
     * determines which collection the add action was selected for and invokes
     * the view helper service to add the line
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addSourceAccountingLine")
    public ModelAndView addSourceAccountingLine(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                                HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside addSourceAccountingLine()");
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) uifForm;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        boolean rulePassed = true;
        boolean flag = false;
        PurApItem item = null;
        String errorPrefix = null;
        String selectedCollectionPath = uifForm.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        if (StringUtils.isBlank(selectedCollectionPath)) {
            throw new RuntimeException("Selected collection was not set for add line action, cannot add new line");
        }
        CollectionGroup collectionGroup = oleInvoiceForm.getPostedView().getViewIndex().getCollectionGroupByPath(
                selectedCollectionPath);
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();
        Object eventObject = ObjectPropertyUtils.getPropertyValue(oleInvoiceForm, addLinePath);
        InvoiceAccount invoiceAccount = (InvoiceAccount)eventObject;
        if (StringUtils.isEmpty(invoiceAccount.getChartOfAccountsCode())) {
            flag = true;
        }
        if (StringUtils.isEmpty(invoiceAccount.getAccountNumber())) {
            flag = true;
        }
        if (StringUtils.isEmpty(invoiceAccount.getFinancialObjectCode())) {
            flag = true;
        }
        int selectedLine = 0;
        if (StringUtils.isNotBlank(selectedCollectionPath)) {
            String lineNumber = StringUtils.substringBetween(selectedCollectionPath, ".items[", ".");
            String itemIndex = StringUtils.substringBefore(lineNumber,"]");
            if (!StringUtils.isEmpty(lineNumber)) {
                selectedLine = Integer.parseInt(itemIndex);
            }
            errorPrefix = OLEPropertyConstants.DOCUMENT + "." + PurapPropertyConstants.ITEM + "[" + Integer.toString(selectedLine) + "]." + OLEConstants.NEW_SOURCE_ACCT_LINE_PROPERTY_NAME;
        }
        rulePassed = SpringContext.getBean(KualiRuleService.class).applyRules(new AddAccountingLineEvent(errorPrefix, oleInvoiceDocument, (AccountingLine)eventObject));
        if (!rulePassed || flag) {
            return getUIFModelAndView(uifForm);
        }
        View view = uifForm.getPostedView();
        view.getViewHelperService().processCollectionAddLine(view, uifForm, selectedCollectionPath);
        if (rulePassed) {
            item = oleInvoiceDocument.getItem((selectedLine));
            PurApAccountingLine lineItem = invoiceAccount;
            if (item.getTotalAmount() != null && !item.getTotalAmount().equals(KualiDecimal.ZERO)) {
                if (lineItem.getAccountLinePercent() != null && (lineItem.getAmount() == null || lineItem.getAmount().equals(KualiDecimal.ZERO))) {
                    BigDecimal percent = lineItem.getAccountLinePercent().divide(new BigDecimal(100));
                    lineItem.setAmount((item.getTotalAmount().multiply(new KualiDecimal(percent))));
                } else if (lineItem.getAmount() != null && lineItem.getAmount().isNonZero() && lineItem.getAccountLinePercent() == null) {
                    KualiDecimal dollar = lineItem.getAmount().multiply(new KualiDecimal(100));
                    BigDecimal dollarToPercent = dollar.bigDecimalValue().divide((item.getTotalAmount().bigDecimalValue()), 2, RoundingMode.FLOOR);
                    lineItem.setAccountLinePercent(dollarToPercent);
                } else if (lineItem.getAmount() != null && lineItem.getAmount().isZero() && lineItem.getAccountLinePercent() == null) {
                    lineItem.setAccountLinePercent(new BigDecimal(0));
                }
                else if(lineItem.getAmount()!=null&& lineItem.getAccountLinePercent().intValue()== 100){
                    KualiDecimal dollar = lineItem.getAmount().multiply(new KualiDecimal(100));
                    BigDecimal dollarToPercent = dollar.bigDecimalValue().divide((item.getTotalAmount().bigDecimalValue()),2,RoundingMode.FLOOR);
                    lineItem.setAccountLinePercent(dollarToPercent);
                }
                else if(lineItem.getAmount()!=null&&lineItem.getAccountLinePercent() != null){
                    BigDecimal percent = lineItem.getAccountLinePercent().divide(new BigDecimal(100));
                    lineItem.setAmount((item.getTotalAmount().multiply(new KualiDecimal(percent))));
                }
            } else {
                lineItem.setAmount(new KualiDecimal(0));
            }
            if(item.getItemType().isAdditionalChargeIndicator()) {
                oleInvoiceDocument.setProrateBy(OLEConstants.NO_PRORATE);
                oleInvoiceDocument.setNoProrate(true);
            }

        }
        LOG.debug("Leaving addSourceAccountingLine()");
        return getUIFModelAndView(uifForm);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addPOAccountingLine")
    public ModelAndView addPOAccountingLine(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                            HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside addPOAccountingLine()");
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) uifForm;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        boolean rulePassed = true;
        boolean flag = false;
        PurApItem item = null;
        String errorPrefix = null;
        String selectedCollectionPath = uifForm.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        if (StringUtils.isBlank(selectedCollectionPath)) {
            throw new RuntimeException("Selected collection was not set for add line action, cannot add new line");
        }
        CollectionGroup collectionGroup = oleInvoiceForm.getPostedView().getViewIndex().getCollectionGroupByPath(
                selectedCollectionPath);
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();
        Object eventObject = ObjectPropertyUtils.getPropertyValue(oleInvoiceForm, addLinePath);
        OlePurchaseOrderAccount purchaseOrderAccount = (OlePurchaseOrderAccount) eventObject;
        if (StringUtils.isEmpty(purchaseOrderAccount.getChartOfAccountsCode())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLEInvoiceView_ProcessItems_AccountingLines, OLEConstants.ERROR_CHART_CODE_REQ,OLEConstants.OrderQueue.CHART_CODE);
            flag = true;
        }
        if (StringUtils.isEmpty(purchaseOrderAccount.getAccountNumber())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLEInvoiceView_ProcessItems_AccountingLines, OLEConstants.ERROR_ACC_NUMB_REQ,OLEConstants.ACC_NUM);
            flag = true;
        }
        if (StringUtils.isEmpty(purchaseOrderAccount.getFinancialObjectCode())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLEInvoiceView_ProcessItems_AccountingLines, OLEConstants.ERROR_OBJECT_CODE_REQ,OLEConstants.OrderQueue.OBJECT_CODE);
            flag = true;
        }
        int selectedLine = 0;
        if (StringUtils.isNotBlank(selectedCollectionPath)) {
            String lineNumber = StringUtils.substringBetween(selectedCollectionPath, ".items[", ".");
            String itemIndex = StringUtils.substringBefore(lineNumber, "]");
            if (!StringUtils.isEmpty(lineNumber)) {
                selectedLine = Integer.parseInt(itemIndex);
            }
            errorPrefix = OLEPropertyConstants.DOCUMENT + "." + PurapPropertyConstants.ITEM + "[" + Integer.toString(selectedLine) + "]." + OLEConstants.NEW_SOURCE_ACCT_LINE_PROPERTY_NAME;
        }
        rulePassed = SpringContext.getBean(KualiRuleService.class).applyRules(new AddAccountingLineEvent(errorPrefix, oleInvoiceDocument, (AccountingLine) eventObject));
        if (!rulePassed || flag) {
            return getUIFModelAndView(uifForm);
        }
        View view = uifForm.getPostedView();
        view.getViewHelperService().processCollectionAddLine(view, uifForm, selectedCollectionPath);
        if (rulePassed) {
            item = oleInvoiceDocument.getItem((selectedLine));
            PurApAccountingLine lineItem = purchaseOrderAccount;
            if (item.getTotalAmount() != null && !item.getTotalAmount().equals(KualiDecimal.ZERO)) {
                if (lineItem.getAccountLinePercent() != null && (lineItem.getAmount() == null || lineItem.getAmount().equals(KualiDecimal.ZERO))) {
                    BigDecimal percent = lineItem.getAccountLinePercent().divide(new BigDecimal(100));
                    lineItem.setAmount((item.getTotalAmount().multiply(new KualiDecimal(percent))));
                } else if (lineItem.getAmount() != null && lineItem.getAmount().isNonZero() && lineItem.getAccountLinePercent() == null) {
                    KualiDecimal dollar = lineItem.getAmount().multiply(new KualiDecimal(100));
                    BigDecimal dollarToPercent = dollar.bigDecimalValue().divide((item.getTotalAmount().bigDecimalValue()), 2, RoundingMode.FLOOR);
                    lineItem.setAccountLinePercent(dollarToPercent);
                } else if (lineItem.getAmount() != null && lineItem.getAmount().isZero() && lineItem.getAccountLinePercent() == null) {
                    lineItem.setAccountLinePercent(new BigDecimal(0));
                } else if (lineItem.getAmount() != null && lineItem.getAccountLinePercent().intValue() == 100) {
                    KualiDecimal dollar = lineItem.getAmount().multiply(new KualiDecimal(100));
                    BigDecimal dollarToPercent = dollar.bigDecimalValue().divide((item.getTotalAmount().bigDecimalValue()), 2, RoundingMode.FLOOR);
                    lineItem.setAccountLinePercent(dollarToPercent);
                } else if (lineItem.getAmount() != null && lineItem.getAccountLinePercent() != null) {
                    BigDecimal percent = lineItem.getAccountLinePercent().divide(new BigDecimal(100));
                    lineItem.setAmount((item.getTotalAmount().multiply(new KualiDecimal(percent))));
                }
            } else {
                lineItem.setAmount(new KualiDecimal(0));
            }
            oleInvoiceDocument.setProrateBy(OLEConstants.NO_PRORATE);
            oleInvoiceDocument.setNoProrate(true);
        }
        LOG.debug("Leaving addPOAccountingLine()");
        return getUIFModelAndView(uifForm);
    }

    /**
     * Performs the disapprove workflow action on the form document instance
     *
     * @param form - document form base containing the document instance that will be disapproved
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=disapprove")
    public ModelAndView disapprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();

        Note noteObj = getDocumentService().createNoteFromDocument(oleInvoiceDocument, "Dispproved at Budget Node By : " + GlobalVariables.getUserSession().getPerson().getName());
        PersistableBusinessObject noteParent = oleInvoiceDocument.getNoteTarget();
        List<Note> noteList = getNoteService().getByRemoteObjectId(noteParent.getObjectId());
        noteList.add(noteObj);
        getNoteService().saveNoteList(noteList);
        getNoteService().save(noteObj);
        oleInvoiceDocument.setNotes(noteList);
        getDocumentService().saveDocument(oleInvoiceDocument);

        performWorkflowAction(oleInvoiceForm, UifConstants.WorkflowAction.DISAPPROVE, true);
        return closeDocument(oleInvoiceForm,result,request,response);

    }


    /**
     * Called by the add line action for a new collection line. Method
     * determines which collection the add action was selected for and invokes
     * the view helper service to add the line
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addPoItems")
    public ModelAndView addPoItems(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) uifForm;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setCurrentItemsFlag(true);
        if(oleInvoiceDocument.getPaymentMethodIdentifier().equals("")){
            GlobalVariables.getMessageMap().putError(OleSelectConstant.PROCESS_ITEM_SECTION_ID, OLEKeyConstants.ERROR_NO_PAYMENT_MTHD);
            return getUIFModelAndView(oleInvoiceForm);
        }
        if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyType())) {
            String currencyType = getInvoiceService().getCurrencyType(oleInvoiceDocument.getInvoiceCurrencyType());
            BigDecimal exchangeRate = null;
            if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyExchangeRate())) {
                    try {
                        Double.parseDouble(oleInvoiceDocument.getInvoiceCurrencyExchangeRate());
                        exchangeRate = new BigDecimal(oleInvoiceDocument.getInvoiceCurrencyExchangeRate());
                        if (new KualiDecimal(exchangeRate).isZero()) {
                            GlobalVariables.getMessageMap().putError(OleSelectConstant.PROCESS_ITEM_SECTION_ID, OLEKeyConstants.ERROR_ENTER_VALID_EXCHANGE_RATE);
                            return getUIFModelAndView(oleInvoiceForm);
                        }
                    }
                    catch (NumberFormatException nfe) {
                        GlobalVariables.getMessageMap().putError(OleSelectConstant.PROCESS_ITEM_SECTION_ID, OLEKeyConstants.ERROR_ENTER_VALID_EXCHANGE_RATE);
                        return getUIFModelAndView(oleInvoiceForm);
                    }
                } else {
                    GlobalVariables.getMessageMap().putError(OleSelectConstant.PROCESS_ITEM_SECTION_ID, OLEKeyConstants.ERROR_EXCHANGE_RATE_EMPTY, currencyType);
                    return getUIFModelAndView(oleInvoiceForm);
                }
            }  else {
                oleInvoiceDocument.setInvoiceCurrencyExchangeRate(null);
                oleInvoiceDocument.setForeignCurrencyFlag(false);
                oleInvoiceDocument.setForeignVendorInvoiceAmount(null);
            }
        }
        String focusId = oleInvoiceForm.getFocusId();
        String s = focusId.substring(focusId.length() - 1, focusId.length());
        int index = Integer.parseInt(s);
        OlePurchaseOrderDocument olePurchaseOrderDocument = oleInvoiceDocument.getPurchaseOrderDocuments().get(index);
        getInvoiceService().convertPOItemToInvoiceItem(oleInvoiceDocument, olePurchaseOrderDocument);
        try {
            calculate(oleInvoiceForm, result, request, response);
        } catch (Exception e) {
            LOG.error("Exception while calculating the document" + e);
            throw new RuntimeException(e);
        }
        if (oleInvoiceDocument.isProrateDollar() || oleInvoiceDocument.isProrateQty()) {
            SpringContext.getBean(OleInvoiceService.class).calculateInvoice(oleInvoiceDocument, true);
        }
        processInvoiceItems(oleInvoiceDocument);
        OLEInvoiceOffsetAccountingLineVendor vendor = new OLEInvoiceOffsetAccountingLineVendor();
        vendor.setVendorName(oleInvoiceDocument.getVendorName());
        oleInvoiceDocument.setPoId("");
        return getUIFModelAndView(oleInvoiceForm);
    }

    private void processInvoiceItems(OleInvoiceDocument oleInvoiceDocument) {
        List<OleInvoiceItem> oleInvoiceItems = new ArrayList<>();
        List<OleInvoiceItem> oleInvoiceAdditionalItems = new ArrayList<>();
        for (OleInvoiceItem oleInvoiceItem : (List<OleInvoiceItem>) oleInvoiceDocument.getItems()) {
            if (oleInvoiceItem.getItemLineNumber() != null) {
                if (oleInvoiceItem.getSequenceNumber() == null || oleInvoiceItem.getSequenceNumber() == 0) {
                    int sequenceNumber = oleInvoiceDocument.getLineOrderSequenceNumber();
                    oleInvoiceDocument.setLineOrderSequenceNumber(++sequenceNumber);
                    oleInvoiceItem.setSequenceNumber(sequenceNumber);
                }
                oleInvoiceItems.add(oleInvoiceItem);
            } else {
                oleInvoiceAdditionalItems.add(oleInvoiceItem);
                //  oleInvoiceItem.setAdditionalUnitPrice(oleInvoiceItem.getExtendedPrice() != null ? oleInvoiceItem.getExtendedPrice().toString() : "");
            }
        }
        oleInvoiceItems.addAll(oleInvoiceAdditionalItems);
        oleInvoiceDocument.setItems(oleInvoiceItems);
    }


    public static boolean isNumeric(String value) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(value, pos);
        return value.length() == pos.getIndex();
    }

    private static boolean isValidInteger(String value){
        try{
            Integer.parseInt(value);
        }catch(Exception e){
            return false;
        }
        return true;
    }

    @RequestMapping(params = "methodToCall=acknowledge")
    public ModelAndView acknowledge(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        Note noteObj = getDocumentService().createNoteFromDocument(oleInvoiceDocument, "Acknowledged at Budget Node By : " + GlobalVariables.getUserSession().getPerson().getName());
        PersistableBusinessObject noteParent = oleInvoiceDocument.getNoteTarget();
        List<Note> noteList = getNoteService().getByRemoteObjectId(noteParent.getObjectId());
        noteList.add(noteObj);
        getNoteService().saveNoteList(noteList);
        getNoteService().save(noteObj);
        oleInvoiceDocument.setNotes(noteList);
        getDocumentService().saveDocument(oleInvoiceDocument);
        performWorkflowAction(oleInvoiceForm, UifConstants.WorkflowAction.ACKNOWLEDGE, true);
        return returnToPrevious(oleInvoiceForm);
    }

    /**
     * This method updates the Invoice account based on the Invoice Price on the Item.
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=updatePrice")
    public ModelAndView updatePrice(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) uifForm;
        //boolean invoicePriceFlag = false;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        for (OleInvoiceItem item : (List<OleInvoiceItem>)oleInvoiceDocument.getItems()) {
            /*if(new KualiDecimal(item.getInvoiceListPrice()).isLessThan(KualiDecimal.ZERO)){
                invoicePriceFlag = true;
            }*/
            if (item.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
                if(StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyType())){
                    String currencyType = getInvoiceService().getCurrencyType(oleInvoiceDocument.getInvoiceCurrencyType());
                    if (StringUtils.isNotBlank(currencyType)) {
                        if (currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                            if (item.getItemDiscount() != null && item.getItemDiscountType() != null) {
                                if(item.getItemDiscountType().equals(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)){
                                    BigDecimal discount = ((item.getItemListPrice().bigDecimalValue().multiply(item.getItemDiscount().bigDecimalValue()))).divide(new BigDecimal(100));
                                    item.setItemUnitPrice(item.getItemListPrice().bigDecimalValue().subtract(discount));
                                }else{
                                    item.setItemUnitPrice(((OleInvoiceItem)item).getItemListPrice().bigDecimalValue().subtract(item.getItemDiscount().bigDecimalValue()));
                                }
                            }
                            else {
                                item.setItemUnitPrice(((OleInvoiceItem)item).getItemListPrice().bigDecimalValue());
                            }
                        } else {
                            BigDecimal exchangeRate = null;
                            if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyExchangeRate())) {
                                try {
                                    Double.parseDouble(oleInvoiceDocument.getInvoiceCurrencyExchangeRate());
                                    exchangeRate = new BigDecimal(oleInvoiceDocument.getInvoiceCurrencyExchangeRate());
                                    if (new KualiDecimal(exchangeRate).isZero()) {
                                        GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_ENTER_VALID_EXCHANGE_RATE);
                                        return getUIFModelAndView(oleInvoiceForm);
                                    }
                                    oleInvoiceDocument.setInvoiceCurrencyExchangeRate(exchangeRate.toString());
                                }
                                catch (NumberFormatException nfe) {
                                    GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_ENTER_VALID_EXCHANGE_RATE);
                                    return getUIFModelAndView(oleInvoiceForm);
                                }
                            }  else {
                                GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_EXCHANGE_RATE_EMPTY, currencyType);
                                return getUIFModelAndView(oleInvoiceForm);
                            }
                            if (item.getForeignDiscount() != null) {
                                if(item.getItemForeignDiscountType().equals(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)){
                                    item.setItemForeignDiscount(new KualiDecimal(item.getForeignDiscount()));
                                    //item.setItemForeignListPrice(new KualiDecimal(new BigDecimal(item.getForeignListPrice())));
                                    BigDecimal discount = ((item.getItemForeignListPrice().bigDecimalValue().multiply(new BigDecimal(item.getForeignDiscount())))).divide(new BigDecimal(100));
                                    item.setItemForeignUnitCost(new KualiDecimal(item.getItemForeignListPrice().bigDecimalValue().subtract(discount)));
                                    item.setForeignUnitCost(item.getItemForeignUnitCost().toString());
                                }else{
                                    item.setItemForeignDiscount(new KualiDecimal(item.getForeignDiscount()));
                                    item.setItemForeignListPrice(new KualiDecimal(item.getForeignListPrice()));
                                    item.setItemForeignUnitCost(new KualiDecimal(((OleInvoiceItem) item).getItemForeignListPrice().bigDecimalValue().subtract(item.getItemForeignDiscount().bigDecimalValue())));
                                    item.setForeignUnitCost(item.getItemForeignUnitCost().toString());
                                }
                            }
                            else {
                                item.setItemForeignListPrice(new KualiDecimal(item.getForeignListPrice()));
                                item.setItemForeignUnitCost(new KualiDecimal(item.getItemForeignListPrice().bigDecimalValue()));
                            }
                        }
                    }
                }
                getInvoiceService().calculateAccount(item);
            }
        }

        try {
            calculate(oleInvoiceForm,result,request,response);
            if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyType())) {
                String currencyType = getInvoiceService().getCurrencyType(oleInvoiceDocument.getInvoiceCurrencyType());
                if (StringUtils.isNotBlank(currencyType)) {
                  //  if(invoicePriceFlag){
                        oleInvoiceDocument.setDbRetrieval(false);
                        oleInvoiceDocument.setInvoicedGrandTotal(oleInvoiceDocument.getInvoicedGrandTotal());
                        oleInvoiceDocument.setInvoiceItemTotal(oleInvoiceDocument.getInvoiceItemTotal());
                        if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                            oleInvoiceDocument.setInvoicedForeignGrandTotal(oleInvoiceDocument.getInvoicedForeignGrandTotal());
                            oleInvoiceDocument.setInvoicedForeignItemTotal(oleInvoiceDocument.getInvoicedForeignItemTotal());
                        }
                   /* }else{
                        oleInvoiceDocument.setDbRetrieval(false);
                        oleInvoiceDocument.setInvoicedGrandTotal(oleInvoiceDocument.getInvoicedGrandTotal());
                        oleInvoiceDocument.setInvoiceItemTotal(oleInvoiceDocument.getInvoiceItemTotal());
                        if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                            oleInvoiceDocument.setInvoicedForeignGrandTotal(oleInvoiceDocument.getInvoicedForeignGrandTotal());
                            oleInvoiceDocument.setInvoicedForeignItemTotal(oleInvoiceDocument.getInvoicedForeignItemTotal());
                        }
                        oleInvoiceDocument.setItemSign(true);
                    }*/
                }
            }
        }
        catch (Exception e) {
            LOG.error("Exception while updating price"+e);
            throw new RuntimeException(e);
        }
        return getUIFModelAndView(oleInvoiceForm);
    }


    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=updatePOPrice")
    public ModelAndView updatePOPrice(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) uifForm;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        for (OlePurchaseOrderItem item : (List<OlePurchaseOrderItem>) oleInvoiceDocument.getPurchaseOrderDocuments().get(0).getItems()) {
            if (item.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
                if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyType())) {
                    String currencyType = getInvoiceService().getCurrencyType(oleInvoiceDocument.getInvoiceCurrencyType());
                    if (StringUtils.isNotBlank(currencyType)) {
                        if (currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                            if (item.getItemDiscount() != null && item.getItemDiscountType() != null) {
                                if (item.getItemDiscountType().equals(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                                    BigDecimal discount = ((new BigDecimal(item.getInvoiceItemListPrice()).multiply(item.getItemDiscount().bigDecimalValue()))).divide(new BigDecimal(100));
                                    item.setItemUnitPrice(new BigDecimal(item.getInvoiceItemListPrice()).subtract(discount));
                                } else {
                                    item.setInvoiceItemListPrice(item.getInvoiceItemListPrice());
                                    item.setItemUnitPrice(new BigDecimal(((OlePurchaseOrderItem) item).getInvoiceItemListPrice()).subtract(item.getItemDiscount().bigDecimalValue()));
                                }
                            } else {
                                item.setItemUnitPrice(new BigDecimal(((OlePurchaseOrderItem) item).getInvoiceItemListPrice()));
                            }
                        } else {
                            BigDecimal exchangeRate = null;
                            if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyExchangeRate())) {
                                try {
                                    Double.parseDouble(oleInvoiceDocument.getInvoiceCurrencyExchangeRate());
                                    exchangeRate = new BigDecimal(oleInvoiceDocument.getInvoiceCurrencyExchangeRate());
                                    if (new KualiDecimal(exchangeRate).isZero()) {
                                        GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_ENTER_VALID_EXCHANGE_RATE);
                                        return getUIFModelAndView(oleInvoiceForm);
                                    }
                                    oleInvoiceDocument.setInvoiceCurrencyExchangeRate(exchangeRate.toString());
                                } catch (NumberFormatException nfe) {
                                    GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_ENTER_VALID_EXCHANGE_RATE);
                                    return getUIFModelAndView(oleInvoiceForm);
                                }
                            } else {
                                GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_EXCHANGE_RATE_EMPTY, currencyType);
                                return getUIFModelAndView(oleInvoiceForm);
                            }
                            if (item.getInvoiceForeignDiscount() != null && !item.getInvoiceForeignDiscount().equals("0.00")) {
                                if (item.getInvoiceForeignDiscountType().equals(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                                    item.setInvoiceForeignDiscount((item.getInvoiceForeignDiscount()));
                                    BigDecimal discount = (new BigDecimal(((OlePurchaseOrderItem) item).getInvoiceForeignItemListPrice())).multiply((new BigDecimal(item.getInvoiceForeignDiscount()))).divide(new BigDecimal(100));
                                    item.setInvoiceForeignUnitCost(new BigDecimal(item.getInvoiceForeignItemListPrice()).subtract(discount).toString());
                                    BigDecimal listPrice = new BigDecimal(item.getInvoiceForeignItemListPrice()).divide(exchangeRate, 2, RoundingMode.HALF_UP);
                                    item.setInvoiceItemListPrice(listPrice.subtract(listPrice.multiply(new BigDecimal(item.getInvoiceForeignDiscount()).divide(new BigDecimal(100)))).toString());
                                    item.setItemUnitPrice(new BigDecimal(item.getInvoiceItemListPrice()));

                                } else {
                                    item.setInvoiceForeignDiscount((item.getInvoiceForeignDiscount()));
                                    BigDecimal discount = new BigDecimal(item.getInvoiceForeignDiscount());
                                    item.setInvoiceForeignUnitCost(new BigDecimal(item.getInvoiceForeignItemListPrice()).subtract(discount).toString());
                                    BigDecimal listPrice = new BigDecimal(item.getInvoiceForeignItemListPrice()).subtract(discount).divide(exchangeRate, 2, RoundingMode.HALF_UP);
                                    item.setInvoiceItemListPrice(listPrice.toString());
                                    item.setItemUnitPrice(listPrice);
                                }
                            } else {
                                item.setInvoiceForeignUnitCost((item.getInvoiceForeignItemListPrice()));
                                BigDecimal listPrice = new BigDecimal(item.getInvoiceForeignItemListPrice()).divide(exchangeRate, 2, RoundingMode.HALF_UP);
                                item.setInvoiceItemListPrice(listPrice.toString());
                                item.setItemUnitPrice(listPrice);
                            }
                        }
                    }
                }
                getInvoiceService().calculateAccount(item);
            }
        }

        return getUIFModelAndView(oleInvoiceForm);
    }

    /**
     * To refresh POItems after lookup.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    @RequestMapping(params = "methodToCall=refresh")
    public ModelAndView refresh(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEInvoiceForm invoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument invoiceDocument = (OleInvoiceDocument)invoiceForm.getDocument();

       for(OleInvoiceItem item :( List<OleInvoiceItem>)invoiceDocument.getItems()){
           if(item.getTempItemIdentifier()!=null){
               Integer id = item.getTempItemIdentifier();
               Map map = new HashMap();
               map.put("itemIdentifier", id);
               List<OlePurchaseOrderAccount> list  = (List<OlePurchaseOrderAccount> )getBusinessObjectService().findMatching(OlePurchaseOrderAccount.class, map);
               List<InvoiceAccount> newList = new ArrayList<>();
               for(OlePurchaseOrderAccount olePurchaseOrderAccount : list){
                   InvoiceAccount invoiceAccount = new InvoiceAccount(item, olePurchaseOrderAccount) ;
                   newList.add(invoiceAccount);
               }
               item.setSourceAccountingLines((List)newList);
               item.setTempItemIdentifier(null);
           }
       }
        if (invoiceDocument.getPoId() != null && StringUtils.isNotBlank(invoiceDocument.getPoId().toString())) {
            super.refresh(invoiceForm, result, request, response);
            invoiceDocument.setUnsaved(true);
            return addItem(invoiceForm, result, request, response);
        }
        return super.refresh(invoiceForm, result, request, response);
    }

    /**
     * This method validates the Invoice Amount.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=validateInvoiceAmount")
    public ModelAndView validateInvoiceAmount(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEInvoiceForm invoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument invoiceDocument = (OleInvoiceDocument)invoiceForm.getDocument();
        String subscriptionValidationMessage = getInvoiceService().createSubscriptionDateOverlapQuestionText(invoiceDocument);
        if (!subscriptionValidationMessage.isEmpty() && subscriptionValidationMessage != null){
            invoiceForm.setSubscriptionValidationMessage(subscriptionValidationMessage);
            invoiceDocument.setBlanketApproveSubscriptionDateValidationFlag(true);
        }
        else {
            invoiceDocument.setValidationFlag(false);
        }

        String validationMessage = getInvoiceService().createInvoiceNoMatchQuestionText(invoiceDocument);
        if (!validationMessage.isEmpty() && validationMessage != null){
            invoiceForm.setValidationMessage(validationMessage);
            invoiceDocument.setValidationFlag(true);
        }
        else {
            invoiceDocument.setValidationFlag(false);
        }
        return getUIFModelAndView(invoiceForm);
    }

    @RequestMapping(params = "methodToCall=deleteInvoice")
    public ModelAndView deleteInvoice(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setDbRetrieval(false);
        String focusId = oleInvoiceForm.getFocusId();
        String s = focusId.substring(focusId.indexOf("_line"),focusId.length()).replace("_line","");
        int deleteDocument = Integer.parseInt(s);
        List<OleInvoiceItem> oleInvoiceItems = oleInvoiceDocument.getItems();
        oleInvoiceDocument.getDeletedInvoiceItems().add((OleInvoiceItem) oleInvoiceDocument.getItems().get(deleteDocument));
        oleInvoiceItems.remove(deleteDocument);
        int sequenceNumber = 0;
        for(OleInvoiceItem item : oleInvoiceItems) {
            if(item.getItemLineNumber() != null) {
                item.setSequenceNumber(++sequenceNumber);
            }
        }
        oleInvoiceDocument.setItems(oleInvoiceItems);
        if (oleInvoiceDocument.isProrateDollar() || oleInvoiceDocument.isProrateQty()) {
            SpringContext.getBean(OleInvoiceService.class).calculateInvoice(oleInvoiceDocument, true);
        }
        return getUIFModelAndView(oleInvoiceForm);
    }

    @RequestMapping(params = "methodToCall=unlinkInvoice")
    public ModelAndView unlinkInvoice(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        String focusId = oleInvoiceForm.getFocusId();
        String s = focusId.substring(focusId.indexOf("_line"),focusId.length()).replace("_line","");
        int unlinkDocument = Integer.parseInt(s);
        List<OleInvoiceItem> oleInvoiceItems = oleInvoiceDocument.getItems();
        OleInvoiceItem oleInvoiceItem = oleInvoiceItems.get(unlinkDocument);
        oleInvoiceItem.setItemDescription("Unlinked - previously attached to PO ["+oleInvoiceItem.getPurchaseOrderIdentifier()+"]");
        oleInvoiceItem.setPoItemIdentifier(null);
        Long nextLinkIdentifier = SpringContext.getBean(SequenceAccessorService.class).getNextAvailableSequenceNumber("AP_PUR_DOC_LNK_ID");
        oleInvoiceItem.setAccountsPayablePurchasingDocumentLinkIdentifier(nextLinkIdentifier.intValue());
        oleInvoiceItem.setItemTitleId(null);
        return getUIFModelAndView(oleInvoiceForm);
    }

    private boolean validateRequiredFields(OleInvoiceDocument invoiceDocument, boolean validateVendorInvoiceAmount) {
        boolean isValid = true;
        for (Object invoiceItem : invoiceDocument.getItems()) {
            isValid &= getKualiRuleService().applyRules(new OLEInvoiceSubscriptionOverlayValidationEvent(invoiceDocument, (OleInvoiceItem)invoiceItem));
        }
        if (invoiceDocument.getInvoiceDate() == null || invoiceDocument.getInvoiceDate().equals("")) {
            GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_NO_INVOICE_DATE);
            isValid &= false;
        }
        if (StringUtils.isNotBlank(invoiceDocument.getInvoiceCurrencyType())) {
            String currencyType = getInvoiceService().getCurrencyType(invoiceDocument.getInvoiceCurrencyType());
            BigDecimal exchangeRate = null;
            if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                if (StringUtils.isNotBlank(invoiceDocument.getInvoiceCurrencyExchangeRate())) {
                    try {
                        Double.parseDouble(invoiceDocument.getInvoiceCurrencyExchangeRate());
                        exchangeRate = new BigDecimal(invoiceDocument.getInvoiceCurrencyExchangeRate());
                        if (new KualiDecimal(exchangeRate).isZero()) {
                            GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_ENTER_VALID_EXCHANGE_RATE);
                            isValid &= false;
                        }
                    }
                    catch (NumberFormatException nfe) {
                        GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_ENTER_VALID_EXCHANGE_RATE);
                        isValid &= false;
                    }
                }   else {
                    GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_EXCHANGE_RATE_EMPTY, currencyType);
                    isValid &= false;
                }
            }  else {
                invoiceDocument.setInvoiceCurrencyExchangeRate(null);
                invoiceDocument.setForeignCurrencyFlag(false);
                invoiceDocument.setForeignVendorInvoiceAmount(null);
            }
        }
        if (validateVendorInvoiceAmount) {
            if (StringUtils.isNotBlank(invoiceDocument.getInvoiceCurrencyType())) {
                String currencyType = getInvoiceService().getCurrencyType(invoiceDocument.getInvoiceCurrencyType());
                if (StringUtils.isNotBlank(currencyType)) {
                    if (currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                        if (invoiceDocument.getInvoiceAmount() == null || invoiceDocument.getInvoiceAmount().equals("")) {
                            GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_NO_INVOICE_AMOUNT);
                            isValid &= false;
                        }
                    } else {
                        if (invoiceDocument.getForeignInvoiceAmount() == null || invoiceDocument.getForeignInvoiceAmount().equals("")) {
                            GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_NO_FOREIGN_INVOICE_AMOUNT);
                            isValid &= false;
                        }
                    }
                }
            }
            try {
                if (StringUtils.isNotBlank(invoiceDocument.getInvoiceAmount())) {
                    Double.parseDouble(invoiceDocument.getInvoiceAmount());
                }
            }
            catch (NumberFormatException nfe) {
                GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_VALID_INVOICE_AMOUNT);
                isValid &= false;
            }
            try {
                if (StringUtils.isNotBlank(invoiceDocument.getForeignInvoiceAmount())) {
                    Double.parseDouble(invoiceDocument.getForeignInvoiceAmount());
                }
            }
            catch (NumberFormatException nfe) {
                GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_VALID_FOREIGN_INVOICE_AMOUNT);
                isValid &= false;
            }
        }
        if (invoiceDocument.getPaymentMethodIdentifier() == null || invoiceDocument.getPaymentMethodIdentifier().equals("")) {
            GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_NO_PAYMENT_MTHD);
            isValid &= false;
        }
        return isValid;
    }

    protected KualiRuleService getKualiRuleService() {
        if (kualiRuleService == null) {
            kualiRuleService = KRADServiceLocatorWeb.getKualiRuleService();
        }
        return this.kualiRuleService;
    }
    @RequestMapping(params = "methodToCall=" + "closeDocument")
    public ModelAndView closeDocument(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response)  {

        String baseUrl = ConfigContext.getCurrentContextConfig().getProperty(org.kuali.ole.OLEPropertyConstants.OLE_URL_BASE);
        String url = baseUrl + "/portal.do";
        Properties props = new Properties();
        props.put(UifParameters.METHOD_TO_CALL, UifConstants.MethodToCallNames.REFRESH);
        if (StringUtils.isNotBlank(form.getReturnFormKey())) {
            props.put(UifParameters.FORM_KEY, form.getReturnFormKey());
        }
        GlobalVariables.getUifFormManager().removeSessionForm(form);
        return performRedirect(form, url, props);
    }

    @RequestMapping(params = "methodToCall=" + "clone")
    public ModelAndView clone(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        //oleInvoiceDocument.setCloneDebitInvoice("true");
        oleInvoiceDocument.setCloneFlag(true);
        return getUIFModelAndView(form);
    }

    /**
     * This method creates the copy of the Invoice Document based on the user input
     * on credit or debit Invoice Document.
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=" + "copyInvoice")
    public ModelAndView copyInvoice(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        //oleInvoiceDocument.setCloneFlag(true);
        /*for (OleInvoiceItem item : (List<OleInvoiceItem>)oleInvoiceDocument.getItems()) {
            OleInvoiceItem invoiceItem = (OleInvoiceItem) item;
            invoiceItem.setReceiptStatusId(null);
            invoiceItem.setOleReceiptStatus(new OleReceiptStatus());
        }*/

        super.copy(oleInvoiceForm,result,request,response);

        return getUIFModelAndView(form);
    }

    /**
     * Performs the approve workflow action on the form document instance
     *
     * @param form - document form base containing the document instance that will be approved
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=approveInvoiceDocument")
    public ModelAndView approveInvoiceDocument(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        Boolean isAmountExceeds = getInvoiceService().isNotificationRequired(oleInvoiceDocument);
        oleInvoiceDocument.setBlanketApproveValidationFlag(false);
        OleInvoiceFundCheckService oleInvoiceFundCheckService = (OleInvoiceFundCheckService) SpringContext
                .getBean("oleInvoiceFundCheckService");
        String subscriptionValidationMessage = getInvoiceService().createSubscriptionDateOverlapQuestionText(oleInvoiceDocument);
        if (!subscriptionValidationMessage.isEmpty() && subscriptionValidationMessage != null) {
            oleInvoiceForm.setSubscriptionValidationMessage(subscriptionValidationMessage);
            oleInvoiceDocument.setBlanketApproveSubscriptionDateValidationFlag(true);
        }
        if (oleInvoiceDocument.getInvoiceAmount() != null && oleInvoiceDocument.getInvoicedGrandTotal() != null ) {
            String validationMessage = getInvoiceService().createInvoiceNoMatchQuestionText(oleInvoiceDocument);
            if (!validationMessage.isEmpty() && validationMessage != null) {
                oleInvoiceForm.setValidationMessage(validationMessage);
                oleInvoiceDocument.setBlanketApproveValidationFlag(true);
            }
        }
        boolean duplicationExists = false;
        duplicationExists = getInvoiceService().isDuplicationExists(oleInvoiceDocument,oleInvoiceForm,"approve");
        if (duplicationExists) {
            //oleInvoiceForm.setDuplicationMessage(OleSelectConstant.DUPLICATE_INVOICE);
            oleInvoiceDocument.setDuplicateFlag(true);
            return getUIFModelAndView(form);
        }
        boolean sfcFlag = false;
        if (SpringContext.getBean(OleInvoiceService.class).getParameterBoolean(OLEConstants.CoreModuleNamespaces.SELECT, OLEConstants.OperationType.SELECT, PurapParameterConstants.ALLOW_INVOICE_SUFF_FUND_CHECK)) {
            if (oleInvoiceDocument.getSourceAccountingLines().size() > 0) {
                sfcFlag = oleInvoiceFundCheckService.isBudgetReviewRequired(oleInvoiceDocument);
                if (sfcFlag || oleInvoiceDocument.isBlanketApproveValidationFlag() || oleInvoiceDocument.isBlanketApproveSubscriptionDateValidationFlag()) {
                    return getUIFModelAndView(form);
                }
            }
        }
        if(isAmountExceeds){
            oleInvoiceForm.setAmountExceedsMessage(getInvoiceService().createInvoiceAmountExceedsThresholdText(oleInvoiceDocument));
            oleInvoiceDocument.setAmountExceeds(true);
            return getUIFModelAndView(form);
        }
        oleInvoiceDocument.setDuplicateFlag(false);
        oleInvoiceDocument.setBlanketApproveValidationFlag(false);
        super.approve(oleInvoiceForm,result,request,response);
        return closeDocument(oleInvoiceForm,result,request,response);
    }
    /**
     * close the popup in instance tab
     *
     * @param form - document form base containing the document instance that will be routed
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=closePopup")
    public ModelAndView closePopup(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        return getUIFModelAndView(form);
    }

    /**
     * Performs the approve workflow action on the form document instance
     *
     * @param form - document form base containing the document instance that will be approved
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=continueDuplicateBlanketApprove")
    public ModelAndView continueDuplicateBlanketApprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                                        HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceForm.setDuplicationApproveMessage(null);
        oleInvoiceDocument.setDuplicateApproveFlag(false);

        String validationMessage = getInvoiceService().createInvoiceNoMatchQuestionText(oleInvoiceDocument);
        if (!validationMessage.isEmpty() && validationMessage != null){
            oleInvoiceForm.setValidationMessage(validationMessage);
            oleInvoiceDocument.setBlanketApproveValidationFlag(true);
        }
        else {
            oleInvoiceDocument.setBlanketApproveValidationFlag(false);
            getInvoiceService().deleteInvoiceItem(oleInvoiceDocument);
            super.blanketApprove(oleInvoiceForm, result, request, response);
            if (GlobalVariables.getMessageMap().getErrorCount() > 0) {
                return getUIFModelAndView(oleInvoiceForm);
            }
            oleInvoiceDocument.setUnsaved(false);
            return closeDocument(form,result,request,response);
        }
        return  getUIFModelAndView(oleInvoiceForm);
    }



    /**
     * Called by the add line action for a new collection line. Method
     * determines which collection the add action was selected for and invokes
     * the view helper service to add the line
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addOffsetSourceAccountingLine")
    public ModelAndView addOffsetSourceAccountingLine(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                                      HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside offset addSourceAccountingLine()");
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) uifForm;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        boolean rulePassed = true;
        boolean flag = false;
        PurApItem item = null;
        String errorPrefix = null;
        String selectedCollectionPath = uifForm.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        if (StringUtils.isBlank(selectedCollectionPath)) {
            throw new RuntimeException("Selected collection was not set for offset add line action, cannot add new line");
        }
        CollectionGroup collectionGroup = oleInvoiceForm.getPostedView().getViewIndex().getCollectionGroupByPath(
                selectedCollectionPath);
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();
        Object eventObject = ObjectPropertyUtils.getPropertyValue(oleInvoiceForm, addLinePath);
        OLEInvoiceOffsetAccountingLine invoiceAccount = (OLEInvoiceOffsetAccountingLine)eventObject;
        if (StringUtils.isEmpty(invoiceAccount.getChartOfAccountsCode())) {
            flag = true;
        }
        if (StringUtils.isEmpty(invoiceAccount.getAccountNumber())) {
            flag = true;
        }
        if (StringUtils.isEmpty(invoiceAccount.getFinancialObjectCode())) {
            flag = true;
        }
        int selectedLine = 0;
        if (StringUtils.isNotBlank(selectedCollectionPath)) {
            String lineNumber = StringUtils.substringBetween(selectedCollectionPath, ".items[", ".");
            String itemIndex = StringUtils.substringBefore(lineNumber,"]");
            if (!StringUtils.isEmpty(lineNumber)) {
                selectedLine = Integer.parseInt(itemIndex);
            }
            errorPrefix = OLEPropertyConstants.DOCUMENT + "." + PurapPropertyConstants.ITEM + "[" + Integer.toString(selectedLine) + "]." + OLEConstants.NEW_SOURCE_ACCT_LINE_PROPERTY_NAME;
        }
        rulePassed = SpringContext.getBean(KualiRuleService.class).applyRules(new AddAccountingLineEvent(errorPrefix, oleInvoiceDocument, (AccountingLine)eventObject));
        if (!rulePassed || flag) {
            return getUIFModelAndView(uifForm);
        }
        View view = uifForm.getPostedView();
        view.getViewHelperService().processCollectionAddLine(view, uifForm, selectedCollectionPath);
        if (rulePassed) {
            item = oleInvoiceDocument.getItem((selectedLine));
            OLEInvoiceOffsetAccountingLine lineItem = invoiceAccount;
            if (item.getTotalAmount() != null && !item.getTotalAmount().equals(KualiDecimal.ZERO)) {
                if (lineItem.getAccountLinePercent() != null && (lineItem.getAmount() == null || lineItem.getAmount().equals(KualiDecimal.ZERO))) {
                    BigDecimal percent = lineItem.getAccountLinePercent().divide(new BigDecimal(100));
                    lineItem.setAmount((item.getTotalAmount().multiply(new KualiDecimal(percent))));
                } else if (lineItem.getAmount() != null && lineItem.getAmount().isNonZero() && lineItem.getAccountLinePercent() == null) {
                    KualiDecimal dollar = lineItem.getAmount().multiply(new KualiDecimal(100));
                    BigDecimal dollarToPercent = dollar.bigDecimalValue().divide((item.getTotalAmount().bigDecimalValue()), 2, RoundingMode.FLOOR);
                    lineItem.setAccountLinePercent(dollarToPercent);
                } else if (lineItem.getAmount() != null && lineItem.getAmount().isZero() && lineItem.getAccountLinePercent() == null) {
                    lineItem.setAccountLinePercent(new BigDecimal(0));
                }
                else if(lineItem.getAmount()!=null&& lineItem.getAccountLinePercent().intValue()== 100){
                    KualiDecimal dollar = lineItem.getAmount().multiply(new KualiDecimal(100));
                    BigDecimal dollarToPercent = dollar.bigDecimalValue().divide((item.getTotalAmount().bigDecimalValue()),2,RoundingMode.FLOOR);
                    lineItem.setAccountLinePercent(dollarToPercent);
                }
                else if(lineItem.getAmount()!=null&&lineItem.getAccountLinePercent() != null){
                    BigDecimal percent = lineItem.getAccountLinePercent().divide(new BigDecimal(100));
                    lineItem.setAmount((item.getTotalAmount().multiply(new KualiDecimal(percent))));
                }
            } else {
                lineItem.setAmount(null);
            }
            //oleInvoiceDocument.setProrateBy(OLEConstants.NO_PRORATE);
            //oleInvoiceDocument.setNoProrate(true);
        }
        LOG.debug("Leaving addSourceAccountingLine()");
        return getUIFModelAndView(uifForm);
    }

    @RequestMapping(params = "methodToCall=refreshBeanId")
    public ModelAndView refreshBeanId(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        String paymentType = SpringContext.getBean(OleInvoiceService.class).getPaymentMethodType(oleInvoiceDocument.getPaymentMethodIdentifier());
        if(paymentType.equals(OLEConstants.DEPOSIT)) {
            oleInvoiceDocument.setOffsetAccountIndicator(true);
        }else{
            oleInvoiceDocument.setOffsetAccountIndicator(false);
        }
        return navigate(form, result, request, response);
    }

    @RequestMapping(params = "methodToCall=approveCheckAmountExceeds")
    public ModelAndView approveCheckAmountExceeds(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        Boolean isAmountExceeds = getInvoiceService().isNotificationRequired(oleInvoiceDocument);
        OleInvoiceFundCheckService oleInvoiceFundCheckService = (OleInvoiceFundCheckService) SpringContext
                .getBean("oleInvoiceFundCheckService");
        oleInvoiceDocument.setSfcFlag(false);
        if(isAmountExceeds){
            oleInvoiceForm.setAmountExceedsMessage(getInvoiceService().createInvoiceAmountExceedsThresholdText(oleInvoiceDocument));
            oleInvoiceDocument.setAmountExceeds(true);
            return getUIFModelAndView(form);
        }
        String subscriptionValidationMessage = getInvoiceService().createSubscriptionDateOverlapQuestionText(oleInvoiceDocument);
        if (!subscriptionValidationMessage.isEmpty() && subscriptionValidationMessage != null) {
            oleInvoiceForm.setSubscriptionValidationMessage(subscriptionValidationMessage);
            oleInvoiceDocument.setBlanketApproveSubscriptionDateValidationFlag(true);
            return getUIFModelAndView(form);
        }
        if (oleInvoiceDocument.getInvoiceAmount() != null && oleInvoiceDocument.getInvoicedGrandTotal() != null ) {
            String validationMessage = getInvoiceService().createInvoiceNoMatchQuestionText(oleInvoiceDocument);
            if (!validationMessage.isEmpty() && validationMessage != null) {
                oleInvoiceForm.setValidationMessage(validationMessage);
                oleInvoiceDocument.setBlanketApproveValidationFlag(true);
                return getUIFModelAndView(form);
            }
        }
        super.approve(oleInvoiceForm,result,request,response);
        return closeDocument(oleInvoiceForm,result,request,response);
    }

    @RequestMapping(params = "methodToCall=continueInvoiceApproval")
    public ModelAndView continueInvoiceApproval(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                                HttpServletRequest request, HttpServletResponse response)  throws Exception{
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setAmountExceeds(false);
        super.approve(oleInvoiceForm, result, request, response);
        return closeDocument(oleInvoiceForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=cancelInvoiceApproval")
    public ModelAndView cancelInvoiceApproval(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setAmountExceeds(false);
        oleInvoiceForm.setAmountExceedsMessage(null);
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=continueInvoiceBlankApproval")
    public ModelAndView continueInvoiceBlankApproval(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                                     HttpServletRequest request, HttpServletResponse response)  throws Exception{
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceFundCheckService oleInvoiceFundCheckService = (OleInvoiceFundCheckService) SpringContext
                .getBean("oleInvoiceFundCheckService");
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setAmountExceedsForBlanketApprove(false);
        oleInvoiceDocument.setBlanketApproveSubscriptionDateValidationFlag(false);
        oleInvoiceDocument.setValidationFlag(false);
        oleInvoiceDocument.setBlanketApproveValidationFlag(false);
        if(oleInvoiceDocument.getSourceAccountingLines().size() > 0) {
            if (SpringContext.getBean(OleInvoiceService.class).getParameterBoolean(OLEConstants.CoreModuleNamespaces.SELECT, OLEConstants.OperationType.SELECT, PurapParameterConstants.ALLOW_INVOICE_SUFF_FUND_CHECK)) {
                List<SourceAccountingLine> sourceAccountingLineList = oleInvoiceDocument.getSourceAccountingLines();
                for (SourceAccountingLine accLine : sourceAccountingLineList) {
                    Map searchMap = new HashMap();
                    String notificationOption = null;
                    boolean sufficientFundCheck;
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
                    if (notificationOption != null && notificationOption.equals(OLEPropertyConstants.WARNING_MSG)) {
                        sufficientFundCheck = oleInvoiceFundCheckService.hasSufficientFundCheckRequired(accLine);
                        if (sufficientFundCheck) {
                            oleInvoiceDocument.setBlanketApproveFlag(false);
                            oleInvoiceDocument.setBaSfcFlag(sufficientFundCheck);
                            oleInvoiceForm.setSfcFailApproveMsg(OLEConstants.INV_INSUFF_FUND + accLine.getAccountNumber());
                            return getUIFModelAndView(oleInvoiceForm);
                        }
                    }
                }
            }
        }

        String subscriptionValidationMessage = getInvoiceService().createSubscriptionDateOverlapQuestionText(oleInvoiceDocument);
        if (!subscriptionValidationMessage.isEmpty() && subscriptionValidationMessage != null){
            oleInvoiceForm.setSubscriptionValidationMessage(subscriptionValidationMessage);
            oleInvoiceDocument.setBlanketApproveSubscriptionDateValidationFlag(true);
            oleInvoiceDocument.setBlanketApproveFlag(false);
        }
        if(oleInvoiceDocument.isValidationFlag() || oleInvoiceDocument.isBlanketApproveValidationFlag() || oleInvoiceDocument.isBlanketApproveSubscriptionDateValidationFlag()){
            return getUIFModelAndView(oleInvoiceForm);
        }

        String validationMessage = getInvoiceService().createInvoiceNoMatchQuestionText(oleInvoiceDocument);
        if (!validationMessage.isEmpty() && validationMessage != null){
            oleInvoiceForm.setValidationMessage(validationMessage);
            oleInvoiceDocument.setBlanketApproveValidationFlag(true);
            oleInvoiceDocument.setBlanketApproveFlag(false);
        }
        else {
            oleInvoiceDocument.setBlanketApproveValidationFlag(false);
            super.blanketApprove(oleInvoiceForm, result, request, response);
            if (GlobalVariables.getMessageMap().getErrorCount() > 0) {
                return getUIFModelAndView(oleInvoiceForm);
            }
            oleInvoiceDocument.setUnsaved(false);
            //GlobalVariables.getMessageMap().clearErrorMessages();
            return closeDocument(oleInvoiceForm,result,request,response);
        }
        return  getUIFModelAndView(oleInvoiceForm);
    }

    @RequestMapping(params = "methodToCall=cancelInvoiceBlankApproval")
    public ModelAndView cancelInvoiceBlankApproval(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                                   HttpServletRequest request, HttpServletResponse response) {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setAmountExceedsForBlanketApprove(false);
        oleInvoiceForm.setAmountExceedsMessage(null);
        oleInvoiceDocument.setUnsaved(true);
        return getUIFModelAndView(form);
    }

    @Override
    protected void performWorkflowAction(DocumentFormBase form, UifConstants.WorkflowAction action, boolean checkSensitiveData) {
        Document document = form.getDocument();

        LOG.debug("Performing workflow action " + action.name() + "for document: " + document.getDocumentNumber());

        // TODO: need question and prompt framework
        if (checkSensitiveData) {
            //        String viewName = checkAndWarnAboutSensitiveData(form, request, response,
            //                KRADPropertyConstants.DOCUMENT_EXPLANATION, document.getDocumentHeader().getExplanation(), "route", "");
            //        if (viewName != null) {
            //            return new ModelAndView(viewName);
            //        }
        }

        try {
            String successMessageKey = null;
            switch (action) {
                case SAVE:
                    getDocumentService().saveDocument(document);
                    successMessageKey = RiceKeyConstants.MESSAGE_SAVED;
                    break;
                case ROUTE:
                    getDocumentService().routeDocument(document, form.getAnnotation(), combineAdHocRecipients(form));
                    successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_SUCCESSFUL;
                    break;
                case BLANKETAPPROVE:
                    getDocumentService().blanketApproveDocument(document, form.getAnnotation(), combineAdHocRecipients(
                            form));
                    successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_SUCCESSFUL;
                    break;
                case APPROVE:
                    getDocumentService().approveDocument(document, form.getAnnotation(), combineAdHocRecipients(form));
                    successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_APPROVED;
                    break;
                case DISAPPROVE:
                    // TODO: need to get disapprove note from user
                    String disapprovalNoteText = "";
                    getDocumentService().disapproveDocument(document, disapprovalNoteText);
                    successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_DISAPPROVED;
                    break;
                case FYI:
                    getDocumentService().clearDocumentFyi(document, combineAdHocRecipients(form));
                    successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_FYIED;
                    break;
                case ACKNOWLEDGE:
                    getDocumentService().acknowledgeDocument(document, form.getAnnotation(), combineAdHocRecipients(
                            form));
                    successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_ACKNOWLEDGED;
                    break;
                case CANCEL:
                    if (getDocumentService().documentExists(document.getDocumentNumber())) {
                        getDocumentService().cancelDocument(document, form.getAnnotation());
                        successMessageKey = RiceKeyConstants.MESSAGE_CANCELLED;
                    }
                    break;
                case COMPLETE:
                    if (getDocumentService().documentExists(document.getDocumentNumber())) {
                        getDocumentService().completeDocument(document, form.getAnnotation(), combineAdHocRecipients(form));
                        successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_SUCCESSFUL;
                    }
                    break;
                case SENDADHOCREQUESTS:
                    getDocumentService().sendAdHocRequests(document, form.getAnnotation(), combineAdHocRecipients(form));
                    successMessageKey = RiceKeyConstants.MESSAGE_ROUTE_SUCCESSFUL;
                    break;
            }

            if (successMessageKey != null) {
                GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_MESSAGES, successMessageKey);
            }
        } catch (ValidationException e) {
            // if errors in map, swallow exception so screen will draw with errors
            // if not then throw runtime because something bad happened
            if (GlobalVariables.getMessageMap().hasNoErrors()) {
                throw new RiceRuntimeException("Validation Exception with no error message.", e);
            }
        } catch (Exception e) {
            throw new RiceRuntimeException(
                    "Exception trying to invoke action " + action.name() + "for document: " + document
                            .getDocumentNumber(), e);
        }

        form.setAnnotation("");
    }

    /**
     *  Updates the Currency type of the document.
     */

    @RequestMapping(params = "methodToCall=updateCurrencyTypeChange")
    public ModelAndView updateCurrencyTypeChange(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        List<OleInvoiceItem> oleInvoiceItemList = oleInvoiceDocument.getItems();
       // boolean invoicePriceFlag = false;
        String currencyType = null;
        BigDecimal exchangeRate = null;
        BigDecimal previousExchangeRate = null;
        Long previousCurrencyTypeId = null;
        String itemForeignListPrice;
        String previousCurrencyType = null;
        oleInvoiceDocument.setDbRetrieval(false);
        if (oleInvoiceDocument.getInvoiceCurrencyTypeId() != null) {
            previousCurrencyType = getInvoiceService().getCurrencyType(oleInvoiceDocument.getInvoiceCurrencyTypeId().toString());
            previousCurrencyTypeId = oleInvoiceDocument.getInvoiceCurrencyTypeId();
        }
        if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyType())) {

            currencyType = getInvoiceService().getCurrencyType(oleInvoiceDocument.getInvoiceCurrencyType());

            if (StringUtils.isNotBlank(currencyType)) {
                // local vendor
                if (currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                    oleInvoiceDocument.setForeignCurrencyFlag(false);
                    oleInvoiceDocument.setForeignVendorInvoiceAmount(null);
                    oleInvoiceDocument.setForeignVendorAmount(OLEConstants.EMPTY_STRING);
                    oleInvoiceDocument.setInvoiceCurrencyTypeId(new Long(oleInvoiceDocument.getInvoiceCurrencyType()));
                    oleInvoiceDocument.setInvoiceCurrencyExchangeRate(null);
                    oleInvoiceDocument.setForeignInvoiceAmount(null);
                    oleInvoiceDocument.setForeignItemTotal(OLEConstants.EMPTY_STRING);
                    oleInvoiceDocument.setForeignGrandTotal(OLEConstants.EMPTY_STRING);

                    if (oleInvoiceDocument.getItems().size()>0 || oleInvoiceDocument.getPurchaseOrderDocuments().size()>0) {
                        for (OlePurchaseOrderDocument olePurchaseOrderDocument : oleInvoiceDocument.getPurchaseOrderDocuments()) {
                            for (OlePurchaseOrderItem olePurchaseOrderItem : (List<OlePurchaseOrderItem>) olePurchaseOrderDocument.getItems()) {
                                if (olePurchaseOrderItem.getItemTypeCode().equalsIgnoreCase(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE)) {
                                    if (olePurchaseOrderItem.getItemDiscount() != null && olePurchaseOrderItem.getItemDiscountType() != null) {
                                        if(olePurchaseOrderItem.getItemDiscountType().equals(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)){
                                            BigDecimal discount = ((olePurchaseOrderItem.getItemListPrice().bigDecimalValue().multiply(olePurchaseOrderItem.getItemDiscount().bigDecimalValue()))).divide(new BigDecimal(100));
                                            olePurchaseOrderItem.setItemUnitPrice(olePurchaseOrderItem.getItemListPrice().bigDecimalValue().subtract(discount));
                                            olePurchaseOrderItem.setInvoiceItemListPrice(olePurchaseOrderItem.getItemListPrice().toString());
                                        }else{
                                            olePurchaseOrderItem.setItemUnitPrice((olePurchaseOrderItem.getItemListPrice().bigDecimalValue().subtract(olePurchaseOrderItem.getItemDiscount().bigDecimalValue())));
                                            olePurchaseOrderItem.setInvoiceItemListPrice(olePurchaseOrderItem.getItemListPrice().toString());
                                        }
                                    }   else {
                                        olePurchaseOrderItem.setInvoiceItemListPrice(olePurchaseOrderItem.getItemListPrice().toString());
                                        olePurchaseOrderItem.setItemUnitPrice(olePurchaseOrderItem.getItemListPrice().bigDecimalValue());
                                        olePurchaseOrderItem.setItemDiscount(new KualiDecimal(0.0));
                                    }
                                }
                                getInvoiceService().calculateAccount(olePurchaseOrderItem);
                            }
                        }

                        if (oleInvoiceItemList.size()>0) {
                            for (OleInvoiceItem oleInvoiceItem : oleInvoiceItemList) {
                                if (oleInvoiceItem.getItemTypeCode().equalsIgnoreCase(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE)) {
                                    if (StringUtils.isNotBlank(previousCurrencyType) && previousCurrencyTypeId != null) {
                                        if (!previousCurrencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                                            previousExchangeRate = getInvoiceService().getExchangeRate(previousCurrencyTypeId.toString()).getExchangeRate();
                                            oleInvoiceItem.setItemUnitCostUSD(new KualiDecimal((oleInvoiceItem.getItemForeignUnitCost().bigDecimalValue()).divide(previousExchangeRate, 4, RoundingMode.HALF_UP)));
                                            oleInvoiceItem.setItemUnitPrice(oleInvoiceItem.getItemForeignUnitCost().bigDecimalValue().divide(previousExchangeRate, 4, RoundingMode.HALF_UP));
                                            oleInvoiceItem.setItemListPrice(new KualiDecimal(oleInvoiceItem.getItemUnitPrice()));
                                            if (!oleInvoiceItem.isDebitItem()) {
                                                oleInvoiceItem.setListPrice("-" + oleInvoiceItem.getItemListPrice().toString());
                                            } else {
                                                oleInvoiceItem.setListPrice(oleInvoiceItem.getItemListPrice().toString());
                                            }
                                            oleInvoiceItem.setItemDiscount(new KualiDecimal(0.0));
                                            oleInvoiceItem.setDiscountItem(new KualiDecimal(0.0).toString());
                                        }
                                    }
                                    oleInvoiceItem.setForeignListPrice(null);
                                    oleInvoiceItem.setForeignUnitCost(null);
                                    oleInvoiceItem.setForeignDiscount(null);
                                }
                                getInvoiceService().calculateAccount(oleInvoiceItem);
                            }
                        }
                    }
                }
                //foreign currency
                else {
                    oleInvoiceDocument.setForeignCurrencyFlag(true);
                    oleInvoiceDocument.setInvoiceCurrencyTypeId(new Long(oleInvoiceDocument.getInvoiceCurrencyType()));
                    exchangeRate = getInvoiceService().getExchangeRate(oleInvoiceDocument.getInvoiceCurrencyType()).getExchangeRate();
                    oleInvoiceDocument.setInvoiceCurrencyExchangeRate(exchangeRate.toString());
                    if (StringUtils.isNotBlank(oleInvoiceDocument.getForeignInvoiceAmount()) ) {
                        oleInvoiceDocument.setForeignVendorInvoiceAmount(new BigDecimal(oleInvoiceDocument.getForeignInvoiceAmount()));
                        oleInvoiceDocument.setVendorInvoiceAmount(new KualiDecimal(new BigDecimal(oleInvoiceDocument.getForeignInvoiceAmount()).divide(exchangeRate, 2, RoundingMode.HALF_UP)));
                        oleInvoiceDocument.setInvoiceAmount(oleInvoiceDocument.getVendorInvoiceAmount().toString());
                        oleInvoiceDocument.setVendorAmount(oleInvoiceDocument.getVendorInvoiceAmount().toString());
                    }
                    else {
                        if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceAmount())) {
                            oleInvoiceDocument.setVendorInvoiceAmount(new KualiDecimal(oleInvoiceDocument.getInvoiceAmount()));
                            oleInvoiceDocument.setForeignVendorInvoiceAmount(new BigDecimal(oleInvoiceDocument.getInvoiceAmount()).multiply(exchangeRate));
                            oleInvoiceDocument.setForeignInvoiceAmount(new KualiDecimal(oleInvoiceDocument.getForeignVendorInvoiceAmount()).toString());
                            oleInvoiceDocument.setForeignVendorAmount(new KualiDecimal(oleInvoiceDocument.getForeignVendorInvoiceAmount()).toString());
                        }
                    }

                    if (oleInvoiceDocument.getItems().size()>0 || oleInvoiceDocument.getPurchaseOrderDocuments().size()>0) {
                        for (OlePurchaseOrderDocument olePurchaseOrderDocument : oleInvoiceDocument.getPurchaseOrderDocuments()) {
                            for (OlePurchaseOrderItem olePurchaseOrderItem : (List<OlePurchaseOrderItem>) olePurchaseOrderDocument.getItems()) {
                                if (olePurchaseOrderItem.getItemTypeCode().equalsIgnoreCase(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE)) {
                                    olePurchaseOrderItem.setInvoiceForeignCurrency(currencyType);
                                    olePurchaseOrderItem.setInvoiceExchangeRate(exchangeRate.toString());

                                    if (StringUtils.isNotBlank(olePurchaseOrderItem.getInvoiceItemListPrice())) {
                                        if (!olePurchaseOrderItem.getItemListPrice().equals(new KualiDecimal(olePurchaseOrderItem.getInvoiceItemListPrice()))) {
                                            if (olePurchaseOrderItem.getItemDiscount() != null && olePurchaseOrderItem.getItemDiscountType() != null) {
                                                if(olePurchaseOrderItem.getItemDiscountType().equals(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)){
                                                    BigDecimal discount = ((new BigDecimal(olePurchaseOrderItem.getInvoiceItemListPrice()).multiply(olePurchaseOrderItem.getItemDiscount().bigDecimalValue()))).divide(new BigDecimal(100));
                                                    olePurchaseOrderItem.setItemUnitPrice(new BigDecimal(olePurchaseOrderItem.getInvoiceItemListPrice()).subtract(discount));
                                                    olePurchaseOrderItem.setItemListPrice(new KualiDecimal(olePurchaseOrderItem.getInvoiceItemListPrice()));
                                                }else{
                                                    olePurchaseOrderItem.setItemUnitPrice(new BigDecimal(olePurchaseOrderItem.getInvoiceItemListPrice()).subtract(olePurchaseOrderItem.getItemDiscount().bigDecimalValue()));
                                                    olePurchaseOrderItem.setItemListPrice(new KualiDecimal(olePurchaseOrderItem.getInvoiceItemListPrice()));
                                                }
                                            }   else {
                                                olePurchaseOrderItem.setItemListPrice(new KualiDecimal(olePurchaseOrderItem.getInvoiceItemListPrice()));
                                                olePurchaseOrderItem.setItemUnitPrice(new BigDecimal(olePurchaseOrderItem.getInvoiceItemListPrice()));
                                            }
                                        }
                                    }

                                    if (StringUtils.isNotBlank(previousCurrencyType)) {
                                        if (!previousCurrencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                                            if (olePurchaseOrderItem.getInvoiceForeignDiscount() != null && olePurchaseOrderItem.getInvoiceForeignDiscountType() != null) {
                                                if(olePurchaseOrderItem.getInvoiceForeignDiscountType().equals("%")){
                                                    if (new KualiDecimal(olePurchaseOrderItem.getInvoiceForeignDiscount()).isNonZero()) {
                                                        BigDecimal discount = ((new BigDecimal(olePurchaseOrderItem.getInvoiceForeignItemListPrice()).multiply(new BigDecimal(olePurchaseOrderItem.getInvoiceForeignDiscount())))).divide(new BigDecimal(100));
                                                        olePurchaseOrderItem.setInvoiceForeignUnitCost(new KualiDecimal(new BigDecimal(olePurchaseOrderItem.getInvoiceForeignItemListPrice()).subtract(discount)).toString());
                                                    } else {
                                                        olePurchaseOrderItem.setInvoiceForeignUnitCost(olePurchaseOrderItem.getInvoiceForeignItemListPrice());
                                                    }
                                                }else {
                                                    olePurchaseOrderItem.setInvoiceForeignUnitCost(new BigDecimal(olePurchaseOrderItem.getInvoiceForeignItemListPrice()).subtract(new BigDecimal(olePurchaseOrderItem.getInvoiceForeignDiscount())).toString());
                                                }
                                                if (olePurchaseOrderItem.getInvoiceExchangeRate() != null && olePurchaseOrderItem.getInvoiceForeignUnitCost() != null) {
                                                    olePurchaseOrderItem.setItemUnitCostUSD(new KualiDecimal(new BigDecimal(olePurchaseOrderItem.getInvoiceForeignUnitCost()).divide(new BigDecimal(olePurchaseOrderItem.getInvoiceExchangeRate()), 4, RoundingMode.HALF_UP)));
                                                    olePurchaseOrderItem.setItemUnitPrice(olePurchaseOrderItem.getItemUnitCostUSD().bigDecimalValue());
                                                    olePurchaseOrderItem.setItemListPrice(olePurchaseOrderItem.getItemUnitCostUSD());
                                                    olePurchaseOrderItem.setInvoiceItemListPrice(olePurchaseOrderItem.getItemListPrice().toString());
                                                }
                                            }
                                        }
                                        else {
                                            olePurchaseOrderItem.setInvoiceForeignUnitCost(new KualiDecimal((olePurchaseOrderItem.getItemUnitPrice().multiply(new BigDecimal(olePurchaseOrderItem.getInvoiceExchangeRate())))).toString());
                                            olePurchaseOrderItem.setInvoiceForeignItemListPrice(olePurchaseOrderItem.getInvoiceForeignUnitCost());
                                            olePurchaseOrderItem.setInvoiceForeignDiscount(new KualiDecimal(0.0).toString());
                                            olePurchaseOrderItem.setItemDiscount(new KualiDecimal(0.0));
                                        }
                                    }
                                    else {
                                        olePurchaseOrderItem.setInvoiceForeignUnitCost(new KualiDecimal((olePurchaseOrderItem.getItemUnitPrice().multiply(new BigDecimal(olePurchaseOrderItem.getInvoiceExchangeRate())))).toString());
                                        olePurchaseOrderItem.setInvoiceForeignItemListPrice(olePurchaseOrderItem.getInvoiceForeignUnitCost());
                                        olePurchaseOrderItem.setInvoiceForeignDiscount(new KualiDecimal(0.0).toString());
                                        olePurchaseOrderItem.setItemDiscount(new KualiDecimal(0.0));
                                    }
                                    getInvoiceService().calculateAccount(olePurchaseOrderItem);
                                }
                            }
                        }
                        if (oleInvoiceItemList.size()>0) {
                            for (OleInvoiceItem oleInvoiceItem : oleInvoiceItemList) {
                               /* if(new KualiDecimal(oleInvoiceItem.getInvoiceListPrice()).isLessThan(KualiDecimal.ZERO)){
                                    invoicePriceFlag = true;
                                }*/
                                if (oleInvoiceItem.getItemTypeCode().equalsIgnoreCase(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE)) {
                                    oleInvoiceItem.setItemCurrencyType(currencyType);
                                    oleInvoiceItem.setInvoicedCurrency(currencyType);
                                    oleInvoiceItem.setExchangeRate(exchangeRate.toString());
                                    oleInvoiceItem.setItemExchangeRate(exchangeRate);

                                    if (StringUtils.isNotBlank(previousCurrencyType)) {
                                        if (!previousCurrencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                                            if (oleInvoiceItem.getItemForeignDiscount() != null && oleInvoiceItem.getItemForeignDiscountType() != null) {
                                                if (!oleInvoiceItem.isDebitItem()) {
                                                    itemForeignListPrice = oleInvoiceItem.getForeignListPrice().toString().replace("(", "");
                                                    itemForeignListPrice = itemForeignListPrice.replace(")","");
                                                } else {
                                                    itemForeignListPrice = oleInvoiceItem.getForeignListPrice();
                                                }
                                                if(oleInvoiceItem.getItemForeignDiscountType().equals("%")){
                                                    if (new KualiDecimal(oleInvoiceItem.getForeignDiscount()).isNonZero()) {
                                                        BigDecimal discount = ((new BigDecimal(itemForeignListPrice).multiply(new BigDecimal(oleInvoiceItem.getForeignDiscount())))).divide(new BigDecimal(100));
                                                        oleInvoiceItem.setItemForeignUnitCost(new KualiDecimal(new BigDecimal(itemForeignListPrice).subtract(discount)));
                                                        oleInvoiceItem.setForeignUnitCost(oleInvoiceItem.getItemForeignUnitCost().toString());
                                                        oleInvoiceItem.setItemForeignListPrice(new KualiDecimal(itemForeignListPrice));
                                                        oleInvoiceItem.setItemForeignDiscount(new KualiDecimal(oleInvoiceItem.getForeignDiscount()));
                                                    } else {
                                                        oleInvoiceItem.setItemForeignListPrice(new KualiDecimal(itemForeignListPrice));
                                                        oleInvoiceItem.setItemForeignDiscount(new KualiDecimal(0.0));
                                                        oleInvoiceItem.setItemForeignUnitCost(oleInvoiceItem.getItemForeignListPrice());
                                                        oleInvoiceItem.setForeignUnitCost(oleInvoiceItem.getItemForeignUnitCost().toString());
                                                    }
                                                }else {
                                                    oleInvoiceItem.setItemForeignUnitCost(new KualiDecimal(new BigDecimal(itemForeignListPrice).subtract(new BigDecimal(oleInvoiceItem.getForeignDiscount()))));
                                                    oleInvoiceItem.setForeignUnitCost(oleInvoiceItem.getItemForeignUnitCost().toString());
                                                    oleInvoiceItem.setItemForeignListPrice(new KualiDecimal(itemForeignListPrice));
                                                    oleInvoiceItem.setItemForeignDiscount(new KualiDecimal(oleInvoiceItem.getForeignDiscount()));
                                                }
                                                if (oleInvoiceItem.getExchangeRate() != null && oleInvoiceItem.getItemForeignUnitCost() != null) {
                                                    oleInvoiceItem.setItemUnitCostUSD(new KualiDecimal(oleInvoiceItem.getItemForeignUnitCost().bigDecimalValue().divide(exchangeRate, 4, RoundingMode.HALF_UP)));
                                                    oleInvoiceItem.setItemUnitPrice(oleInvoiceItem.getItemForeignUnitCost().bigDecimalValue().divide(exchangeRate, 4, RoundingMode.HALF_UP));
                                                    oleInvoiceItem.setItemListPrice(oleInvoiceItem.getItemUnitCostUSD());
                                                    if (!oleInvoiceItem.isDebitItem()) {
                                                        oleInvoiceItem.setListPrice("-" + oleInvoiceItem.getItemListPrice().toString());
                                                    } else {
                                                        oleInvoiceItem.setListPrice(oleInvoiceItem.getItemListPrice().toString());
                                                    }
                                                    oleInvoiceItem.setItemDiscount(new KualiDecimal(0.0));
                                                    oleInvoiceItem.setDiscountItem(new KualiDecimal(0.0).toString());
                                                }
                                            }
                                        }
                                        else {
                                            oleInvoiceItem.setItemForeignUnitCost(new KualiDecimal(oleInvoiceItem.getItemUnitPrice().multiply(exchangeRate)));
                                            oleInvoiceItem.setForeignUnitCost(oleInvoiceItem.getItemForeignUnitCost().toString());
                                            oleInvoiceItem.setItemForeignListPrice(oleInvoiceItem.getItemForeignUnitCost());
                                            //oleInvoiceItem.setForeignListPrice(oleInvoiceItem.getItemForeignListPrice().toString());
                                            oleInvoiceItem.setItemForeignDiscount(new KualiDecimal(0.0));
                                            oleInvoiceItem.setForeignDiscount(oleInvoiceItem.getItemForeignDiscount().toString());
                                            oleInvoiceItem.setItemForeignDiscountType(oleInvoiceItem.getItemDiscountType());
                                            oleInvoiceItem.setItemDiscount(new KualiDecimal(0.0));
                                        }
                                    } else {
                                        oleInvoiceItem.setItemForeignUnitCost(new KualiDecimal(oleInvoiceItem.getItemUnitPrice().multiply(exchangeRate)));
                                        oleInvoiceItem.setForeignUnitCost(oleInvoiceItem.getItemForeignUnitCost().toString());
                                        oleInvoiceItem.setItemForeignListPrice(oleInvoiceItem.getItemForeignUnitCost());
                                        //oleInvoiceItem.setForeignListPrice(oleInvoiceItem.getItemForeignListPrice().toString());
                                        oleInvoiceItem.setItemForeignDiscount(new KualiDecimal(0.0));
                                        oleInvoiceItem.setForeignDiscount(oleInvoiceItem.getItemForeignDiscount().toString());
                                        oleInvoiceItem.setItemForeignDiscountType(oleInvoiceItem.getItemDiscountType());
                                        oleInvoiceItem.setItemDiscount(new KualiDecimal(0.0));
                                    }
                                }   else {
                                    if (StringUtils.isNotBlank(previousCurrencyType)) {
                                        if (previousCurrencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                                            if (oleInvoiceItem.getItemUnitPrice() != null) {
                                                oleInvoiceItem.setAdditionalForeignUnitCost(oleInvoiceItem.getItemUnitPrice().multiply(new BigDecimal(oleInvoiceDocument.getInvoiceCurrencyExchangeRate())).toString());
                                            }
                                        }
                                    }
                                }
                                getInvoiceService().calculateAccount(oleInvoiceItem);
                            }
                            proratedSurchargeRefresh(oleInvoiceForm, result, request, response);
                        }
                    }
                }
            }
         //   if(invoicePriceFlag){
                oleInvoiceDocument.setDbRetrieval(false);
                oleInvoiceDocument.setInvoicedGrandTotal(oleInvoiceDocument.getInvoicedGrandTotal());
                oleInvoiceDocument.setInvoiceItemTotal(oleInvoiceDocument.getInvoiceItemTotal());
                if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                    oleInvoiceDocument.setInvoicedForeignGrandTotal(oleInvoiceDocument.getInvoicedForeignGrandTotal());
                    oleInvoiceDocument.setInvoicedForeignItemTotal(oleInvoiceDocument.getInvoicedForeignItemTotal());
                }
          /*  }else{
                oleInvoiceDocument.setDbRetrieval(false);
                oleInvoiceDocument.setInvoicedGrandTotal(oleInvoiceDocument.getInvoicedGrandTotal());
                oleInvoiceDocument.setInvoiceItemTotal(oleInvoiceDocument.getInvoiceItemTotal());
                if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                    oleInvoiceDocument.setInvoicedForeignGrandTotal(oleInvoiceDocument.getInvoicedForeignGrandTotal());
                    oleInvoiceDocument.setInvoicedForeignItemTotal(oleInvoiceDocument.getInvoicedForeignItemTotal());
                }
                oleInvoiceDocument.setItemSign(true);
            }*/
        }
        return getUIFModelAndView(form);
    }

    /**
     * Updates the prices to Zero if Current Currency is Overrided.
     */
/*
    @RequestMapping(params = "methodToCall=continueCurrencyOverride")
    public ModelAndView continueCurrencyOverride(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                                HttpServletRequest request, HttpServletResponse response)  throws Exception{
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setCurrencyOverrideFlag(false);
        oleInvoiceDocument.setGrandTotal(new KualiDecimal(0.0));
        oleInvoiceDocument.setItemTotal(new KualiDecimal(0.0).toString());
        KualiDecimal exchangeRate = null;
        List<OleInvoiceItem> oleInvoiceItems = oleInvoiceDocument.getItems();
        for (OleInvoiceItem oleInvoiceItem : oleInvoiceItems) {
            //if (oleInvoiceItem.getItemType().getItemTypeCode().equalsIgnoreCase(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE)) {
            oleInvoiceItem.setTotalAmount(new KualiDecimal(0.0));
            if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyType())) {
                String currencyType = getInvoiceService().getCurrencyType(oleInvoiceDocument.getInvoiceCurrencyType());
                if (StringUtils.isNotBlank(currencyType)) {
                    oleInvoiceItem.setInvoicedCurrency(currencyType);
                    oleInvoiceDocument.setInvoiceCurrencyTypeId(new Long(oleInvoiceDocument.getInvoiceCurrencyType()));
                    if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                        oleInvoiceDocument.setForeignCurrencyFlag(true);
                        exchangeRate = new KualiDecimal(getInvoiceService().getExchangeRate(oleInvoiceDocument.getInvoiceCurrencyType()).getExchangeRate());
                        oleInvoiceDocument.setInvoiceCurrencyExchangeRate(exchangeRate.toString());
                        oleInvoiceItem.setExchangeRate(exchangeRate.toString());
                        oleInvoiceItem.setItemExchangeRate(exchangeRate);
                    } else {
                        oleInvoiceDocument.setForeignCurrencyFlag(false);
                        oleInvoiceDocument.setInvoiceCurrencyExchangeRate(null);
                        oleInvoiceItem.setItemExchangeRate(null);
                        oleInvoiceItem.setExchangeRate(null);
                    }
                    oleInvoiceDocument.setVendorAmount(null);
                    oleInvoiceDocument.setForeignVendorInvoiceAmount(null);
                    oleInvoiceItem.setListPrice(new KualiDecimal(0.0).toString());
                    oleInvoiceItem.setDiscountItem(new KualiDecimal(0.0).toString());
                    oleInvoiceItem.setItemListPrice(new KualiDecimal(0.0));
                    oleInvoiceItem.setItemUnitCostUSD(new KualiDecimal(0.0));
                    oleInvoiceItem.setInvoiceListPrice(new KualiDecimal(0.0).toString());
                    oleInvoiceItem.setAdditionalUnitPrice(new KualiDecimal(0.0).toString());
                    oleInvoiceItem.setItemUnitPrice(new BigDecimal(0));
                    oleInvoiceItem.setExtendedPrice(new KualiDecimal(0.0));
                    oleInvoiceItem.setItemForeignListPrice(new KualiDecimal(0.0));
                    oleInvoiceItem.setForeignListPrice(new KualiDecimal(0.0).toString());
                    oleInvoiceItem.setItemForeignUnitCost(new KualiDecimal(0.0));
                    oleInvoiceItem.setForeignUnitCost(new KualiDecimal(0.0).toString());
                    oleInvoiceItem.setItemForeignDiscount(new KualiDecimal(0.0));
                    oleInvoiceItem.setForeignDiscount(new KualiDecimal(0.0).toString());
                    oleInvoiceItem.setAdditionalForeignUnitCost(new KualiDecimal(0.0).toString());
                    oleInvoiceItem.setForeignCurrencyExtendedPrice(new KualiDecimal(0.0));
                    oleInvoiceItem.setInvoiceForeignListPrice(new KualiDecimal(0.0).toString());
                }
            }
            //}
            getInvoiceService().calculateAccount(oleInvoiceItem);
        }

        if (oleInvoiceDocument.getPurchaseOrderDocuments().size()>0) {
            for (OlePurchaseOrderDocument olePurchaseOrderDocument : oleInvoiceDocument.getPurchaseOrderDocuments()) {
                for (OlePurchaseOrderItem olePurchaseOrderItem : (List<OlePurchaseOrderItem>) olePurchaseOrderDocument.getItems()) {

                    if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyType())) {
                        String currencyType = getInvoiceService().getCurrencyType(oleInvoiceDocument.getInvoiceCurrencyType());
                        if (StringUtils.isNotBlank(currencyType)) {
                            olePurchaseOrderItem.setInvoiceForeignCurrency(currencyType);
                            olePurchaseOrderItem.setItemCurrencyType(currencyType);
                            oleInvoiceDocument.setInvoiceCurrencyTypeId(new Long(oleInvoiceDocument.getInvoiceCurrencyType()));
                            if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                                oleInvoiceDocument.setForeignCurrencyFlag(true);
                                exchangeRate = new KualiDecimal(getInvoiceService().getExchangeRate(oleInvoiceDocument.getInvoiceCurrencyType()).getExchangeRate());
                                oleInvoiceDocument.setInvoiceCurrencyExchangeRate(exchangeRate.toString());
                                olePurchaseOrderItem.setInvoiceExchangeRate(exchangeRate.toString());
                                olePurchaseOrderItem.setItemExchangeRate(exchangeRate);
                            } else {
                                oleInvoiceDocument.setForeignCurrencyFlag(false);
                                olePurchaseOrderItem.setInvoiceExchangeRate(null);
                                olePurchaseOrderItem.setItemExchangeRate(null);
                                oleInvoiceDocument.setInvoiceCurrencyExchangeRate(null);
                            }
                            oleInvoiceDocument.setVendorAmount(null);
                            oleInvoiceDocument.setForeignVendorInvoiceAmount(null);
                            olePurchaseOrderItem.setItemListPrice(new KualiDecimal(0.0));
                            olePurchaseOrderItem.setItemDiscount(new KualiDecimal(0.0));
                            olePurchaseOrderItem.setItemUnitPrice(new BigDecimal(0.0));
                            olePurchaseOrderItem.setInvoiceItemListPrice(new KualiDecimal(0.0).toString());
                            olePurchaseOrderItem.setItemDiscount(new KualiDecimal(0.0));
                            olePurchaseOrderItem.setExtendedPrice(new KualiDecimal(0.0));
                            olePurchaseOrderItem.setInvoicePrice(new KualiDecimal(0.0));
                            olePurchaseOrderItem.setItemForeignListPrice(new KualiDecimal(0.0));
                            olePurchaseOrderItem.setInvoiceForeignItemListPrice(new KualiDecimal(0.0).toString());
                            olePurchaseOrderItem.setItemForeignDiscount(new KualiDecimal(0.0));
                            olePurchaseOrderItem.setInvoiceForeignDiscount(new KualiDecimal(0.0).toString());
                            olePurchaseOrderItem.setItemForeignUnitCost(new KualiDecimal(0.0));
                            olePurchaseOrderItem.setInvoiceForeignUnitCost(new KualiDecimal(0.0).toString());
                            olePurchaseOrderItem.setItemForiegnExtendedPrice(new KualiDecimal(0.0));
                            olePurchaseOrderItem.setItemForeignDiscountAmt(new KualiDecimal(0.0));
                        }
                    }
                    getInvoiceService().calculateAccount(olePurchaseOrderItem);
                }
            }
        }
        return getUIFModelAndView(form);
    }

    /**
     * Currency type of the document is retained, if Currency Type is not overrided.
     */

/*    @RequestMapping(params = "methodToCall=cancelCurrencyOverride")
    public ModelAndView cancelCurrencyOverride(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                                HttpServletRequest request, HttpServletResponse response)  throws Exception{
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setCurrencyOverrideFlag(false);
        oleInvoiceDocument.setInvoiceCurrencyType(oleInvoiceDocument.getInvoiceCurrencyTypeId().toString());
        return getUIFModelAndView(form);
    }
*/
    /**
     * Exchange rate of the current currency type is overrided and
     *
     * the prices are re-calculated based on the overrided exchange rate value.
     */

    @RequestMapping(params = "methodToCall=updateExchangeRate")
    public ModelAndView updateExchangeRate(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                               HttpServletRequest request, HttpServletResponse response)  throws Exception{
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        BigDecimal exchangeRate = null;
      //  boolean invoicePriceFlag = false;
        oleInvoiceDocument.setDbRetrieval(false);
        if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyExchangeRate())) {
            try {
                Double.parseDouble(oleInvoiceDocument.getInvoiceCurrencyExchangeRate());
                exchangeRate = new BigDecimal(oleInvoiceDocument.getInvoiceCurrencyExchangeRate());
            }
            catch (NumberFormatException nfe) {
                GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_ENTER_VALID_EXCHANGE_RATE);
                return getUIFModelAndView(oleInvoiceForm);
            }
        }  else {
            /*if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyType())) {
                exchangeRate = new KualiDecimal(getInvoiceService().getExchangeRate(oleInvoiceDocument.getInvoiceCurrencyType()).getExchangeRate());
            }*/
            String currencyType = getInvoiceService().getCurrencyType(oleInvoiceDocument.getInvoiceCurrencyType());
            GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_EXCHANGE_RATE_EMPTY, currencyType);
            return getUIFModelAndView(oleInvoiceForm);

        }
        if (exchangeRate != null ) {
            if (new KualiDecimal(exchangeRate).isZero()) {
                GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_ENTER_VALID_EXCHANGE_RATE);
                return getUIFModelAndView(oleInvoiceForm);
            }
            oleInvoiceDocument.setInvoiceCurrencyExchangeRate(exchangeRate.toString());
        } else {
            GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_INFO_SECTION_ID, OLEKeyConstants.ERROR_ENTER_VALID_EXCHANGE_RATE);
            return getUIFModelAndView(oleInvoiceForm);
        }
        if (StringUtils.isNotBlank(oleInvoiceDocument.getForeignInvoiceAmount())) {
            oleInvoiceDocument.setForeignVendorInvoiceAmount(new BigDecimal(oleInvoiceDocument.getForeignInvoiceAmount()));
            oleInvoiceDocument.setVendorInvoiceAmount(new KualiDecimal(new BigDecimal(oleInvoiceDocument.getForeignInvoiceAmount()).divide(exchangeRate, 4, RoundingMode.HALF_UP)));
            oleInvoiceDocument.setInvoiceAmount(oleInvoiceDocument.getVendorInvoiceAmount().toString());
            oleInvoiceDocument.setVendorAmount(oleInvoiceDocument.getVendorInvoiceAmount().toString());
        }
        else {
            if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceAmount())) {
                oleInvoiceDocument.setVendorInvoiceAmount(new KualiDecimal(oleInvoiceDocument.getInvoiceAmount()));
                oleInvoiceDocument.setForeignVendorInvoiceAmount(new KualiDecimal(new BigDecimal(oleInvoiceDocument.getInvoiceAmount()).multiply(exchangeRate)).bigDecimalValue());
                oleInvoiceDocument.setForeignInvoiceAmount(oleInvoiceDocument.getForeignVendorInvoiceAmount().toString());
                oleInvoiceDocument.setForeignVendorAmount(oleInvoiceDocument.getForeignVendorInvoiceAmount().toString());
            }
        }

        if (oleInvoiceDocument.getItems().size()>0) {
            for (OleInvoiceItem oleInvoiceItem : (List<OleInvoiceItem>) oleInvoiceDocument.getItems()) {
            /*    if(new KualiDecimal(oleInvoiceItem.getInvoiceListPrice()).isLessThan(KualiDecimal.ZERO)){
                    invoicePriceFlag = true;
                }*/
                if (oleInvoiceItem.getItemType().getItemTypeCode().equalsIgnoreCase(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE)) {
                        oleInvoiceItem.setExchangeRate(exchangeRate.toString());
                        oleInvoiceItem.setItemExchangeRate(exchangeRate);
                }
            }
            if(oleInvoiceDocument.getItems().size() > 4) {
                calculate(oleInvoiceForm, result, request, response);
                proratedSurchargeRefresh(oleInvoiceForm, result, request, response);
            }

            if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyType())) {
                String currencyType = getInvoiceService().getCurrencyType(oleInvoiceDocument.getInvoiceCurrencyType());
                if (StringUtils.isNotBlank(currencyType)) {
                  //  if(invoicePriceFlag){
                        oleInvoiceDocument.setDbRetrieval(false);
                        oleInvoiceDocument.setInvoicedGrandTotal(oleInvoiceDocument.getInvoicedGrandTotal());
                        oleInvoiceDocument.setInvoiceItemTotal(oleInvoiceDocument.getInvoiceItemTotal());
                        if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                            oleInvoiceDocument.setInvoicedForeignGrandTotal(oleInvoiceDocument.getInvoicedForeignGrandTotal());
                            oleInvoiceDocument.setInvoicedForeignItemTotal(oleInvoiceDocument.getInvoicedForeignItemTotal());
                        }
                   /* }else{
                        oleInvoiceDocument.setDbRetrieval(false);
                        oleInvoiceDocument.setInvoicedGrandTotal(oleInvoiceDocument.getInvoicedGrandTotal());
                        oleInvoiceDocument.setInvoiceItemTotal(oleInvoiceDocument.getInvoiceItemTotal());
                        if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                            oleInvoiceDocument.setInvoicedForeignGrandTotal(oleInvoiceDocument.getInvoicedForeignGrandTotal());
                            oleInvoiceDocument.setInvoicedForeignItemTotal(oleInvoiceDocument.getInvoicedForeignItemTotal());
                        }
                        oleInvoiceDocument.setItemSign(true);
                    }*/
                }
            }
        }
        if (oleInvoiceDocument.getPurchaseOrderDocuments().size()>0) {
            for (OlePurchaseOrderDocument olePurchaseOrderDocument : oleInvoiceDocument.getPurchaseOrderDocuments()) {
                for (OlePurchaseOrderItem olePurchaseOrderItem : (List<OlePurchaseOrderItem>) olePurchaseOrderDocument.getItems()) {

                    if (StringUtils.isNotBlank(oleInvoiceDocument.getInvoiceCurrencyType())) {
                        String currencyType = getInvoiceService().getCurrencyType(oleInvoiceDocument.getInvoiceCurrencyType());
                        if (StringUtils.isNotBlank(currencyType)) {
                            if (StringUtils.isNotBlank(currencyType)) {
                                if (!currencyType.equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                                    olePurchaseOrderItem.setInvoiceForeignCurrency(currencyType);
                                    oleInvoiceDocument.setForeignCurrencyFlag(true);
                                    olePurchaseOrderItem.setItemDiscount(new KualiDecimal(0.0));
                                    olePurchaseOrderItem.setInvoiceExchangeRate(exchangeRate.toString());

                                    // if the PO has Foreign Currency
                                    if (olePurchaseOrderItem.getItemForeignListPrice() != null) {
                                        olePurchaseOrderItem.setInvoiceForeignItemListPrice(olePurchaseOrderItem.getItemForeignListPrice().toString());
                                        olePurchaseOrderItem.setInvoiceForeignDiscount(olePurchaseOrderItem.getItemForeignDiscount()!=null? olePurchaseOrderItem.getItemForeignDiscount().toString(): new KualiDecimal("0.0").toString());
                                        olePurchaseOrderItem.setInvoiceForeignUnitCost(olePurchaseOrderItem.getItemForeignUnitCost().toString());
                                        olePurchaseOrderItem.setInvoiceForeignCurrency(currencyType);

                                        if (olePurchaseOrderItem.getInvoiceExchangeRate() != null && olePurchaseOrderItem.getInvoiceForeignUnitCost() != null) {
                                            olePurchaseOrderItem.setItemUnitCostUSD(new KualiDecimal(new BigDecimal(olePurchaseOrderItem.getInvoiceForeignUnitCost()).divide(new BigDecimal(olePurchaseOrderItem.getInvoiceExchangeRate()), 4, RoundingMode.HALF_UP)));
                                            olePurchaseOrderItem.setItemUnitPrice(olePurchaseOrderItem.getItemUnitCostUSD().bigDecimalValue());
                                            olePurchaseOrderItem.setItemListPrice(olePurchaseOrderItem.getItemUnitCostUSD());
                                            olePurchaseOrderItem.setInvoiceItemListPrice(olePurchaseOrderItem.getItemListPrice().toString());
                                            olePurchaseOrderItem.setExtendedPrice(olePurchaseOrderItem.calculateExtendedPrice());
                                        }
                                    } else {
                                        olePurchaseOrderItem.setItemForeignUnitCost(new KualiDecimal(olePurchaseOrderItem.getItemUnitPrice().multiply(new BigDecimal(olePurchaseOrderItem.getInvoiceExchangeRate()))));
                                        olePurchaseOrderItem.setItemForeignListPrice(olePurchaseOrderItem.getItemForeignUnitCost());
                                        olePurchaseOrderItem.setInvoiceForeignItemListPrice(olePurchaseOrderItem.getItemForeignListPrice().toString());
                                        olePurchaseOrderItem.setInvoiceForeignDiscount(new KualiDecimal(0.0).toString());
                                        olePurchaseOrderItem.setInvoiceForeignUnitCost(olePurchaseOrderItem.getItemForeignUnitCost().toString());
                                    }
                                }
                            }
                        }
                    }
                    getInvoiceService().calculateAccount(olePurchaseOrderItem);
                }
            }
        }
        return getUIFModelAndView(form);
    }

   /* @RequestMapping(params = "methodToCall=relatedViewBtn")
    public ModelAndView relatedViewBtn(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response)  throws Exception{
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        String focusId = oleInvoiceForm.getFocusId();
        String s = focusId.substring(focusId.indexOf("_line"),focusId.length()).replace("_line","");
        int unlinkDocument = Integer.parseInt(s);
        List<OleInvoiceItem> oleInvoiceItems = oleInvoiceDocument.getItems();
        OleInvoiceItem oleInvoiceItem = oleInvoiceItems.get(unlinkDocument);
        *//*PurApRelatedViews relatedViews = new PurApRelatedViews(oleInvoiceDocument != null
                ? oleInvoiceDocument.getDocumentNumber() : null,
                oleInvoiceItem.getAccountsPayablePurchasingDocumentLinkIdentifier() != null
                        ? oleInvoiceItem.getAccountsPayablePurchasingDocumentLinkIdentifier() : null);*//*
        //oleInvoiceItem.setRelatedViews(relatedViews);
       *//* oleInvoiceItem.getRelatedViews().getRelatedRequisitionViews();
        oleInvoiceItem.getRelatedViews().getRelatedLineItemReceivingViews();
        oleInvoiceItem.getRelatedViews().getRelatedCorrectionReceivingViews();
        oleInvoiceItem.getRelatedViews().getRelatedPaymentRequestViews();
        oleInvoiceItem.getRelatedViews().getRelatedCreditMemoViews();
        oleInvoiceItem.getRelatedViews().getPaymentHistoryPaymentRequestViews();
        oleInvoiceItem.getRelatedViews().getPaymentHistoryCreditMemoViews();*//*
        oleInvoiceItem.setEnableDetailsSection(true);
        return getUIFModelAndView(form);
    }*/

   /* @RequestMapping(params = "methodToCall=currentItemsBtn")
    public ModelAndView currentItemsBtn(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response)  throws Exception{
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setEnableCurrentItems(true);
        return getUIFModelAndView(form);
    }*/

    @RequestMapping(params = "methodToCall=enableRouteLogDisplay")
    public ModelAndView enableRouteLogDisplay(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response)  throws Exception{
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setRouteLogDisplayFlag(true);
        return getUIFModelAndView(form);
    }


    /**
     * Prompts user to confirm the cancel action then if confirmed cancels the document instance
     * contained on the form, Overridden to remove session from form.
     *
     * @param form - document form base containing the document instance that will be cancelled
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=cancel")
    @Override
    public ModelAndView cancel(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {

        String baseUrl = ConfigContext.getCurrentContextConfig().getProperty(org.kuali.ole.OLEPropertyConstants.OLE_URL_BASE);
        String url = baseUrl + "/portal.do";
        Properties props = new Properties();
        props.put(UifParameters.METHOD_TO_CALL, UifConstants.MethodToCallNames.REFRESH);
        if (StringUtils.isNotBlank(form.getReturnFormKey())) {
            props.put(UifParameters.FORM_KEY, form.getReturnFormKey());
        }
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setDbRetrieval(false);
        getInvoiceService().deleteInvoiceItem(oleInvoiceDocument);
        super.cancel(form,result,request,response);
        GlobalVariables.getUifFormManager().removeSessionForm(form);
        return performRedirect(form, url, props);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=deleteNotes")
    public ModelAndView deleteNotes(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside deleteNotes()");
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) uifForm;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        String focusId = oleInvoiceForm.getFocusId();
        String s = focusId.substring(focusId.indexOf("_line"),focusId.length()).replace("_line","");
        String lineArray[] = (focusId.substring(focusId.indexOf("_line")).split("_"));
        int itemLevel = Integer.parseInt(lineArray[1].replace("line",""));
        int noteLevel = Integer.parseInt(lineArray[2].replace("line",""));
        List<OleInvoiceItem> oleInvoiceItems = oleInvoiceDocument.getItems();
        OleInvoiceItem oleInvoiceItem = oleInvoiceItems.get(itemLevel);
        List<OleInvoiceNote> notes = oleInvoiceItem.getNotes();
        OleInvoiceNote oleInvoiceNote =  oleInvoiceItem.getNotes().remove(noteLevel);
        getBusinessObjectService().delete(oleInvoiceNote);
        return getUIFModelAndView(oleInvoiceForm);
    }

    @RequestMapping(params = "methodToCall=modifySequenceOrder")
    public ModelAndView modifySequenceOrder(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {

        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        int sequenceNumber = oleInvoiceDocument.getIndexNumberFromJsonObject(request.getParameter(OLEConstants.SEQ_NBR));
        int modifiedSequenceNumber = ((OleInvoiceItem) oleInvoiceDocument.getItems().get(sequenceNumber)).getSequenceNumber();
        if (modifiedSequenceNumber < 1) {
            String poId = ((OleInvoiceItem) oleInvoiceDocument.getItems().get(sequenceNumber)).getPurchaseOrderIdentifier().toString();
            GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_ITEM_SECTION_ID,
                    OLEKeyConstants.INV_SEQ_NBR,poId);
            return getUIFModelAndView(oleInvoiceForm);
        }
        int count = 0;
        for(OleInvoiceItem item : (List<OleInvoiceItem>)oleInvoiceDocument.getItems()) {
            if(item.getItemLineNumber() != null) {
               count++;
            }
        }
        if(modifiedSequenceNumber > count) {
            String poId = ((OleInvoiceItem) oleInvoiceDocument.getItems().get(sequenceNumber)).getPurchaseOrderIdentifier().toString();
            GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_ITEM_SECTION_ID,
                    OLEKeyConstants.INV_SEQ_NBR_ERR,poId);
            return getUIFModelAndView(oleInvoiceForm);
        }
        if(modifiedSequenceNumber <= sequenceNumber) {
            List<OleInvoiceItem> oleInvoiceItemList = new ArrayList<>();
            oleInvoiceItemList.addAll(oleInvoiceDocument.getItems());
            oleInvoiceItemList.add(modifiedSequenceNumber - 1, oleInvoiceItemList.get(sequenceNumber));
            oleInvoiceItemList.remove(sequenceNumber + 1);
            oleInvoiceDocument.getItems().clear();
            oleInvoiceDocument.getItems().addAll(oleInvoiceItemList);
        } else {
            List<OleInvoiceItem> oleInvoiceItemList = new ArrayList<>();
            oleInvoiceItemList.addAll(oleInvoiceDocument.getItems());
            oleInvoiceItemList.add(modifiedSequenceNumber , oleInvoiceItemList.get(sequenceNumber));
            oleInvoiceItemList.remove(sequenceNumber);
            oleInvoiceDocument.getItems().clear();
            oleInvoiceDocument.getItems().addAll(oleInvoiceItemList);

        }
        int seqNo = 0;
        for (OleInvoiceItem item : (List<OleInvoiceItem>) oleInvoiceDocument.getItems()) {
            if (item.getItemLineNumber() != null) {
                item.setSequenceNumber(++seqNo);
            }
        }
        return getUIFModelAndView(oleInvoiceForm);
    }

    @RequestMapping(params = "methodToCall=populateAccountingLines")
    public ModelAndView populateAccountingLines(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                           HttpServletRequest request, HttpServletResponse response) {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        String accountingLineIndex = request.getParameter("accountingLineIndex");
        OleInvoiceItem oleInvoiceItem = (OleInvoiceItem) oleInvoiceDocument.getItem(Integer.parseInt(accountingLineIndex));
        Map fundMap = new HashMap();
        String fundCode = oleInvoiceItem.getFundCode();
        if (!StringUtils.isNotBlank(fundCode) && oleInvoiceDocument.getPurchaseOrderDocuments().size() > 0) {
            OlePurchaseOrderItem olePurchaseOrderItem = ((OlePurchaseOrderItem) oleInvoiceDocument.getPurchaseOrderDocuments().get(0).getItem(Integer.parseInt(accountingLineIndex)));
            if (olePurchaseOrderItem != null) {
                fundCode = olePurchaseOrderItem.getFundCode();
                if (fundCode != null) {
                    fundMap.put(org.kuali.ole.OLEConstants.OLEEResourceRecord.FUND_CODE, fundCode);
                    OleFundCode oleFundCode = getBusinessObjectService().findByPrimaryKey(OleFundCode.class, fundMap);
                    if (oleFundCode == null) {
                        GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_ITEMS_FUNDCODE,
                                OleSelectConstant.ERROR_INVOICE_ITEMS_FUNDCODE_INVALID);
                        return getUIFModelAndView(oleInvoiceForm);
                    } else {
                        if (oleFundCode.getOleFundCodeAccountingLineList() != null) {
                            olePurchaseOrderItem.getSourceAccountingLines().clear();
                            for (OleFundCodeAccountingLine oleFundCodeAccountingLine : oleFundCode.getOleFundCodeAccountingLineList()) {
                                PurApAccountingLine purApAccountingLine = new OlePurchaseOrderAccount();
                                purApAccountingLine.setChartOfAccountsCode(oleFundCodeAccountingLine.getChartCode());
                                purApAccountingLine.setAccountNumber(oleFundCodeAccountingLine.getAccountNumber());
                                purApAccountingLine.setSubAccountNumber(oleFundCodeAccountingLine.getSubAccount());
                                purApAccountingLine.setFinancialObjectCode(oleFundCodeAccountingLine.getObjectCode());
                                purApAccountingLine.setFinancialSubObjectCode(oleFundCodeAccountingLine.getSubObject());
                                purApAccountingLine.setProjectCode(oleFundCodeAccountingLine.getProject());
                                purApAccountingLine.setOrganizationReferenceId(oleFundCodeAccountingLine.getOrgRefId());
                                purApAccountingLine.setAccountLinePercent(oleFundCodeAccountingLine.getPercentage());
                                olePurchaseOrderItem.getSourceAccountingLines().add(purApAccountingLine);
                            }
                            olePurchaseOrderItem.setFundCode(null);
                        }
                    }
                }

            }
            return getUIFModelAndView(oleInvoiceForm);
        }
        if (!StringUtils.isNotBlank(fundCode)) {
            GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_ITEMS_FUNDCODE,
                    OleSelectConstant.ERROR_INVOICE_ITEMS_FUNDCODE_REQUIRED);
            return getUIFModelAndView(oleInvoiceForm);
        } else {
            fundMap.clear();
            fundMap.put(org.kuali.ole.OLEConstants.OLEEResourceRecord.FUND_CODE, fundCode);
            OleFundCode oleFundCode = getBusinessObjectService().findByPrimaryKey(OleFundCode.class, fundMap);
            if (oleFundCode == null) {
                GlobalVariables.getMessageMap().putError(OleSelectConstant.INVOICE_ITEMS_FUNDCODE,
                        OleSelectConstant.ERROR_INVOICE_ITEMS_FUNDCODE_INVALID);
                return getUIFModelAndView(oleInvoiceForm);
            } else {
                if (oleFundCode.getOleFundCodeAccountingLineList() != null) {
                    oleInvoiceItem.getSourceAccountingLines().clear();
                    for (OleFundCodeAccountingLine oleFundCodeAccountingLine : oleFundCode.getOleFundCodeAccountingLineList()) {
                        PurApAccountingLine purApAccountingLine = new InvoiceAccount();
                        purApAccountingLine.setChartOfAccountsCode(oleFundCodeAccountingLine.getChartCode());
                        purApAccountingLine.setAccountNumber(oleFundCodeAccountingLine.getAccountNumber());
                        purApAccountingLine.setSubAccountNumber(oleFundCodeAccountingLine.getSubAccount());
                        purApAccountingLine.setFinancialObjectCode(oleFundCodeAccountingLine.getObjectCode());
                        purApAccountingLine.setFinancialSubObjectCode(oleFundCodeAccountingLine.getSubObject());
                        purApAccountingLine.setProjectCode(oleFundCodeAccountingLine.getProject());
                        purApAccountingLine.setOrganizationReferenceId(oleFundCodeAccountingLine.getOrgRefId());
                        purApAccountingLine.setAccountLinePercent(oleFundCodeAccountingLine.getPercentage());
                        oleInvoiceItem.getSourceAccountingLines().add(purApAccountingLine);
                    }
                    oleInvoiceItem.setFundCode(null);
                }
            }
        }
        return getUIFModelAndView(oleInvoiceForm);
    }

    @RequestMapping(params = "methodToCall=validateInvoiceNumber")
    public ModelAndView validateInvoiceNumber(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) throws Exception {

        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        oleInvoiceDocument.setDuplicateValidationFlag(false);
        boolean duplicationExists = false;
        duplicationExists = getInvoiceService().isDuplicationExists(oleInvoiceDocument,oleInvoiceForm,"");
        if (duplicationExists) {
            oleInvoiceDocument.setDuplicateValidationFlag(true);
            String message = oleInvoiceForm.getDuplicationMessage();
            if(message.length() > 0){
               /* if(message.contains("?")){
                    message = message.substring(1,message.lastIndexOf("D"));
                }*/
                oleInvoiceForm.setDuplicationMessage(message);
            }
            return getUIFModelAndView(form);
        }
        return getUIFModelAndView(oleInvoiceForm);
    }


    /*public Integer getIndexNumberFromJsonObject(String sequenceObject) {
        Integer returnValue = null;
        try {
            JSONObject jsonObject = new JSONObject(sequenceObject);
            returnValue = jsonObject.getInt(OLEConstants.INDEX_NBR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnValue;
    }*/

    private LookupService getLookupService() {
        return KRADServiceLocatorWeb.getLookupService();
    }

    @RequestMapping(params = "methodToCall=selectVendor")
    public ModelAndView selectVendor(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        OLEInvoiceForm oleInvoiceForm = (OLEInvoiceForm) form;
        OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceForm.getDocument();
        Map<String, String> criteria = new HashMap<String, String>();
        VendorDetail vendorDetail = null;
        String vendorAliasName = oleInvoiceDocument.getVendorAlias();
        if (StringUtils.isNotEmpty(vendorAliasName)) {
            criteria.put(OLEConstants.InvoiceDocument.VENDOR_ALIAS_NAME, vendorAliasName);
            List<VendorAlias> vendorAliasList = (List<VendorAlias>) getLookupService().findCollectionBySearch(VendorAlias.class, criteria);
            if (CollectionUtils.isNotEmpty(vendorAliasList)) {
                vendorDetail = vendorAliasList.get(0).getVendorDetail();
            }
        }
        if (vendorDetail != null) {
            oleInvoiceDocument.setVendorDetail(vendorDetail);
            oleInvoiceDocument.setVendorName(vendorDetail.getVendorName());
            oleInvoiceDocument.setVendorId(vendorDetail.getVendorHeaderGeneratedIdentifier() + "-" + vendorDetail.getVendorDetailAssignedIdentifier());
            oleInvoiceDocument.setVendorHeaderGeneratedIdentifier(vendorDetail.getVendorHeaderGeneratedIdentifier());
            oleInvoiceDocument.setVendorDetailAssignedIdentifier(vendorDetail.getVendorDetailAssignedIdentifier());
            oleInvoiceDocument.setVendorNumber(vendorDetail.getVendorNumber());
            oleInvoiceDocument.setVendorHeaderGeneratedIdentifier(vendorDetail.getVendorHeaderGeneratedIdentifier());
            oleInvoiceDocument.setVendorDetailAssignedIdentifier(vendorDetail.getVendorDetailAssignedIdentifier());
            oleInvoiceDocument.setVendorFaxNumber(vendorDetail.getDefaultFaxNumber());
            //oleInvoiceDocument.
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
            if (vendorDetail.getPaymentMethodId() != null) {
                oleInvoiceDocument.setPaymentMethodIdentifier(vendorDetail.getPaymentMethodId().toString());
            }
            if (oleInvoiceDocument.getVendorDetail() != null ) {
                if (oleInvoiceDocument.getVendorDetail().getCurrencyType() != null) {
                    oleInvoiceDocument.setInvoiceCurrencyType(vendorDetail.getCurrencyType().getCurrencyTypeId().toString());
                    oleInvoiceDocument.setInvoiceCurrencyTypeId(vendorDetail.getCurrencyType().getCurrencyTypeId());
                    if (vendorDetail.getCurrencyType().getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                        oleInvoiceDocument.setForeignCurrencyFlag(false);
                        oleInvoiceDocument.setForeignInvoiceAmount(null);
                        oleInvoiceDocument.setInvoiceCurrencyExchangeRate(null);
                    } else {
                        oleInvoiceDocument.setForeignCurrencyFlag(true);
                        oleInvoiceDocument.setInvoiceAmount(null);
                        BigDecimal exchangeRate = getInvoiceService().getExchangeRate(oleInvoiceDocument.getInvoiceCurrencyType()).getExchangeRate();
                        oleInvoiceDocument.setInvoiceCurrencyExchangeRate(exchangeRate.toString());
                    }
                }
            }
            else {
                oleInvoiceDocument.setPaymentMethodIdentifier("");
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
        else {
            vendorDetail = new VendorDetail();
            oleInvoiceDocument.setVendorDetail(vendorDetail);
            oleInvoiceDocument.setVendorName(vendorDetail.getVendorName());
            oleInvoiceDocument.setVendorHeaderGeneratedIdentifier(vendorDetail.getVendorHeaderGeneratedIdentifier());
            oleInvoiceDocument.setVendorDetailAssignedIdentifier(vendorDetail.getVendorDetailAssignedIdentifier());
            oleInvoiceDocument.setVendorNumber(vendorDetail.getVendorNumber());
            oleInvoiceDocument.setVendorHeaderGeneratedIdentifier(vendorDetail.getVendorHeaderGeneratedIdentifier());
            oleInvoiceDocument.setVendorDetailAssignedIdentifier(vendorDetail.getVendorDetailAssignedIdentifier());
            oleInvoiceDocument.setVendorFaxNumber(vendorDetail.getDefaultFaxNumber());
            oleInvoiceDocument.setVendorPaymentTerms(vendorDetail.getVendorPaymentTerms());
            oleInvoiceDocument.setVendorPaymentTermsCode("");
            oleInvoiceDocument.setVendorShippingPaymentTerms(vendorDetail.getVendorShippingPaymentTerms());
            oleInvoiceDocument.setPaymentMethodIdentifier("");
            oleInvoiceDocument.setVendorCityName("");
            oleInvoiceDocument.setVendorLine1Address("");
            oleInvoiceDocument.setVendorLine2Address("");
            oleInvoiceDocument.setVendorAttentionName("");
            oleInvoiceDocument.setVendorPostalCode("");
            oleInvoiceDocument.setVendorStateCode("");
            oleInvoiceDocument.setVendorAttentionName("");
            oleInvoiceDocument.setVendorAddressInternationalProvinceName("");
            oleInvoiceDocument.setVendorCountryCode("");
        }
        return getUIFModelAndView(oleInvoiceForm);
    }

}
