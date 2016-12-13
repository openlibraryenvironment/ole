/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.ole.select.document.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapParameterConstants;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.module.purap.document.PaymentRequestDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.VendorCreditMemoDocument;
import org.kuali.ole.module.purap.document.service.AccountsPayableService;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.module.purap.document.service.impl.CreditMemoServiceImpl;
import org.kuali.ole.module.purap.service.PurapAccountingService;
import org.kuali.ole.module.purap.util.ExpiredOrClosedAccountEntry;
import org.kuali.ole.select.businessobject.OleCreditMemoItem;
import org.kuali.ole.select.businessobject.OleInvoiceItem;
import org.kuali.ole.select.businessobject.OlePaymentRequestItem;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.document.OlePaymentRequestDocument;
import org.kuali.ole.select.document.OleVendorCreditMemoDocument;
import org.kuali.ole.select.document.service.*;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.vnd.VendorConstants;
import org.kuali.ole.vnd.VendorUtils;
import org.kuali.ole.vnd.businessobject.VendorAddress;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.ole.vnd.document.service.VendorService;
import org.kuali.rice.core.api.util.type.AbstractKualiDecimal;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Provides services to support the creation of a Credit Memo Document.
 */
@Transactional
public class OleCreditMemoServiceImpl extends CreditMemoServiceImpl implements OleCreditMemoService {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleCreditMemoServiceImpl.class);

    private PurapAccountingService purapAccountingService;
    private PurapService purapService;
    private OlePaymentRequestService olePaymentRequestService;
    private VendorService vendorService;
    private OlePurchaseOrderService olePurchaseOrderService;
    private OleInvoiceService oleInvoiceService;
    private AccountsPayableService accountsPayableService;
    private DocumentService documentService;
    private OlePurapAccountingService olePurapAccountingService;



    public void setOlePurchaseOrderService(OlePurchaseOrderService olePurchaseOrderService) {
        this.olePurchaseOrderService = olePurchaseOrderService;
    }

    @Override
    public void setVendorService(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    public void setOlePaymentRequestService(OlePaymentRequestService olePaymentRequestService) {
        this.olePaymentRequestService = olePaymentRequestService;
    }

    @Override
    public void setPurapAccountingService(PurapAccountingService purapAccountingService) {
        this.purapAccountingService = purapAccountingService;
    }

    @Override
    public void setPurapService(PurapService purapService) {
        this.purapService = purapService;
    }

    public void setOleInvoiceService(OleInvoiceService oleInvoiceService) {
        this.oleInvoiceService = oleInvoiceService;
    }

    public void setAccountsPayableService(AccountsPayableService accountsPayableService) {
        this.accountsPayableService = accountsPayableService;
    }

    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    public void setOlePurapAccountingService(OlePurapAccountingService olePurapAccountingService) {
        this.olePurapAccountingService = olePurapAccountingService;
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.CreditMemoService# calculateCreditMemo(org.kuali.ole.module.purap.document.VendorCreditMemoDocument)
     */
    @Override
    public void calculateCreditMemo(VendorCreditMemoDocument cmDocument) {
        OleVendorCreditMemoDocument oleVendorCreditMemoDocument = (OleVendorCreditMemoDocument) cmDocument;
        cmDocument.updateExtendedPriceOnItems();
        KualiDecimal totalAmount = KualiDecimal.ZERO;
        KualiDecimal totalQty = KualiDecimal.ZERO;
        KualiDecimal totalSurcharge = KualiDecimal.ZERO;
        List<PurApItem> items = new ArrayList<>();
        //purapAccountingService.updateAccountAmounts(cmDocument);
        for (OleCreditMemoItem item : (List<OleCreditMemoItem>) cmDocument.getItems()) {
            // make sure restocking fee is negative
            if (StringUtils.equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_RESTCK_FEE_CODE, item.getItemTypeCode())) {
                if (item.getItemUnitPrice() != null) {
                    item.setExtendedPrice(item.getExtendedPrice().abs().negated());
                    item.setItemUnitPrice(item.getItemUnitPrice().abs().negate());
                }
            }
            if (StringUtils.equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_MISC_CRDT_CODE, item.getItemTypeCode()) ||
                    StringUtils.equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_RESTCK_FEE_CODE, item.getItemTypeCode()) ||
                    StringUtils.equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_FREIGHT_CODE, item.getItemTypeCode()) ||
                    StringUtils.equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_SHIP_AND_HAND_CODE, item.getItemTypeCode()) ||
                    StringUtils.equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_MISC_CODE, item.getItemTypeCode()) ||
                    StringUtils.equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_MIN_ORDER_CODE, item.getItemTypeCode())) {
                if (item.getItemUnitPrice() != null && ((OleVendorCreditMemoDocument)cmDocument).getInvoiceIdentifier() == null) {
                    calculateProrateItemSurcharge((OleVendorCreditMemoDocument) cmDocument);
                }
            }if(item.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
                //     invoiceItem.setRelatedViews(null);
                if (item.getItemQuantity() != null) {
                    totalQty = totalQty.add(item.getItemQuantity());
                }
            }

            if (!(item.getItemType().isQuantityBasedGeneralLedgerIndicator()) && item.getExtendedPrice()!=null) {
                totalSurcharge = totalSurcharge.add(item.getExtendedPrice());
            }
            items.add(item);
        }

        //calculate tax if cm not based on vendor
        if (cmDocument.isSourceVendor() == false) {
            calculateTax(cmDocument);
        }

        // proration
        if (cmDocument.isSourceVendor()) {
            // no proration on vendor
            return;
        }

        oleVendorCreditMemoDocument.setProrateBy(oleVendorCreditMemoDocument.isProrateQty() ?
                OLEConstants.PRORATE_BY_QTY : oleVendorCreditMemoDocument.isProrateManual() ?
                OLEConstants.MANUAL_PRORATE : oleVendorCreditMemoDocument.isProrateDollar() ?
                OLEConstants.PRORATE_BY_DOLLAR : oleVendorCreditMemoDocument.isNoProrate() ?
                OLEConstants.NO_PRORATE : null);
        for (OleCreditMemoItem item : (List<OleCreditMemoItem>) cmDocument.getItems()) {

            // skip above the line
            if (item.getItemType().isLineItemIndicator()) {
                continue;
            }
            totalAmount = cmDocument.getTotalDollarAmountAboveLineItems();
            totalAmount = totalAmount.subtract(totalSurcharge);
            // purapAccountingService.updateAccountAmounts(invoiceDocument);

            if ((ObjectUtils.isNotNull(item.getExtendedPrice())) && (KualiDecimal.ZERO.compareTo(item.getExtendedPrice()) != 0)) {

                List<PurApAccountingLine> distributedAccounts = null;
                List<SourceAccountingLine> summaryAccounts = null;

                totalAmount = cmDocument.getLineItemTotal();
                // this should do nothing on preq which is fine
                if(oleVendorCreditMemoDocument.isProrateDollar() || oleVendorCreditMemoDocument.isProrateQty()){
                    summaryAccounts = olePurapAccountingService.generateSummary(items);
                }
                if(oleVendorCreditMemoDocument.isProrateDollar()){
                    distributedAccounts = olePurapAccountingService.generateAccountDistributionForProration(summaryAccounts, totalAmount, PurapConstants.PRORATION_SCALE, CreditMemoAccount.class);
                }else if(oleVendorCreditMemoDocument.isProrateQty()) {
                    distributedAccounts = olePurapAccountingService.generateAccountDistributionForProrationByQty(summaryAccounts, totalQty, PurapConstants.PRORATION_SCALE, CreditMemoAccount.class);
                }

                if (CollectionUtils.isNotEmpty(distributedAccounts) ) {
                    item.setSourceAccountingLines(distributedAccounts);
                }
            }
            purapAccountingService.updateItemAccountAmounts(item);

        }
        purapAccountingService.updateAccountAmounts(cmDocument);
        // end proration          
    }


    public void calculateProrateItemSurcharge(OleVendorCreditMemoDocument vendorCreditMemoDocument) {
        LOG.debug("Inside Calculation for ProrateItemSurcharge");
        vendorCreditMemoDocument.setProrateBy(vendorCreditMemoDocument.isProrateQty() ? OLEConstants.PRORATE_BY_QTY :
                vendorCreditMemoDocument.isProrateManual() ? OLEConstants.MANUAL_PRORATE :
                        vendorCreditMemoDocument.isProrateDollar() ? OLEConstants.PRORATE_BY_DOLLAR : vendorCreditMemoDocument.isNoProrate() ? OLEConstants.NO_PRORATE : null);
        BigDecimal addChargeItem = BigDecimal.ZERO;
        List<OleCreditMemoItem> item = (List<OleCreditMemoItem>) vendorCreditMemoDocument.getItems();
        for (OleCreditMemoItem items : item) {
            if (!items.getItemType().isQuantityBasedGeneralLedgerIndicator() && !items.getItemTypeCode().equalsIgnoreCase(PurapConstants.ItemTypeCodes.ITEM_TYPE_PMT_TERMS_DISCOUNT_CODE) && items.getItemUnitPrice() != null) {
                addChargeItem = addChargeItem.add(items.getItemUnitPrice());
            }
        }
        List<BigDecimal> newUnitPriceList = new ArrayList<BigDecimal>();
        BigDecimal totalExtPrice = new BigDecimal(0);
        BigDecimal newUnitPrice = new BigDecimal(0);
        BigDecimal extPrice = new BigDecimal(0);
        BigDecimal unitPricePercent = new BigDecimal(0);
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal one = new BigDecimal(1);
        BigDecimal totalSurCharge = new BigDecimal(0);
        BigDecimal totalItemQuantity = new BigDecimal(0);
        BigDecimal itemSurchargeCons = new BigDecimal(0);
        for (int i = 0; item.size() > i; i++) {
            OleCreditMemoItem items = (OleCreditMemoItem) vendorCreditMemoDocument.getItem(i);
            if ((items.getItemType().isQuantityBasedGeneralLedgerIndicator()) && !ObjectUtils.isNull(items.getItemQuantity())) {
                if (vendorCreditMemoDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY)) {
                    totalItemQuantity = totalItemQuantity.add(items.getItemQuantity().bigDecimalValue());
                }
                if (vendorCreditMemoDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_DOLLAR) || vendorCreditMemoDocument.getProrateBy().equals(OLEConstants.MANUAL_PRORATE) || vendorCreditMemoDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY)) {
                    /*if (items.getItemDiscount() == null) {
                        items.setItemDiscount(KualiDecimal.ZERO);
                    }
                    if (items.getItemDiscountType() != null && items.getItemDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                        newUnitPrice = (hundred.subtract(items.getItemDiscount().bigDecimalValue())).divide(hundred).multiply(items.getItemListPrice().bigDecimalValue());
                    }
                    else {
                        newUnitPrice = items.getItemListPrice().bigDecimalValue().subtract(items.getItemDiscount().bigDecimalValue());
                    }*/
                    newUnitPrice = items.getItemUnitPrice();
                    newUnitPriceList.add(newUnitPrice);
                    extPrice = newUnitPrice.multiply(items.getItemQuantity().bigDecimalValue());
                    totalExtPrice = totalExtPrice.add(extPrice);
                }
                if (vendorCreditMemoDocument.getProrateBy().equals(OLEConstants.MANUAL_PRORATE)) {
                    if (items.getItemSurcharge() == null) {
                        items.setItemSurcharge(BigDecimal.ZERO);
                    }
                    totalSurCharge = totalSurCharge.add(items.getItemQuantity().bigDecimalValue().multiply(items.getItemSurcharge()));
                }
            }

        }
        if (vendorCreditMemoDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY)) {
            if (totalItemQuantity.compareTo(BigDecimal.ZERO) != 0) {
                itemSurchargeCons = one.divide(totalItemQuantity, 8, RoundingMode.HALF_UP);
            }
        }
        for (int i = 0, j = 0; item.size() > i; i++) {
            OleCreditMemoItem items = (OleCreditMemoItem) vendorCreditMemoDocument.getItem(i);
            if (items.getItemType().isQuantityBasedGeneralLedgerIndicator() && newUnitPriceList.size() > j && !ObjectUtils.isNull(items.getItemQuantity())) {
                if (vendorCreditMemoDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_DOLLAR)) {
                    if (totalExtPrice.compareTo(BigDecimal.ZERO) != 0) {
                        unitPricePercent = newUnitPriceList.get(j).divide(totalExtPrice, 8, RoundingMode.HALF_UP);
                    }
                    items.setItemSurcharge(unitPricePercent.multiply(addChargeItem).setScale(4, RoundingMode.HALF_UP));
                    items.setExtendedPrice((new KualiDecimal((newUnitPriceList.get(j).add(items.getItemSurcharge()).toString()))).multiply(items.getItemQuantity()));
                }
                if (vendorCreditMemoDocument.getProrateBy().equals(OLEConstants.PRORATE_BY_QTY)) {
                    items.setItemSurcharge(itemSurchargeCons.multiply(addChargeItem).setScale(4, RoundingMode.HALF_UP));
                    items.setExtendedPrice(new KualiDecimal(((newUnitPriceList.get(j).add(items.getItemSurcharge()).toString()))).multiply(items.getItemQuantity()));
                }
                if (vendorCreditMemoDocument.getProrateBy().equals(OLEConstants.MANUAL_PRORATE) && items.getItemSurcharge() != null) {
                    items.setExtendedPrice(new KualiDecimal(((newUnitPriceList.get(j).add(items.getItemSurcharge()).toString()))).multiply(items.getItemQuantity()));
                }
                j++;
            }
        }
        if (vendorCreditMemoDocument.getProrateBy().equals(OLEConstants.MANUAL_PRORATE)) {
            if (totalSurCharge.compareTo(addChargeItem) != 0) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY, OLEKeyConstants.ERROR_PAYMENT_REQUEST_TOTAL_MISMATCH);
            }
        }
        LOG.debug("Leaving Calculation for ProrateItemSurcharge");
    }


    /**
     * Populates the credit memo items from the payment request items.
     *
     * @param cmDocument - Credit Memo Document to Populate
     */
    @Override
    protected void populateItemLinesFromPreq(VendorCreditMemoDocument cmDocument, HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList) {
        OlePaymentRequestDocument preqDocument = (OlePaymentRequestDocument) cmDocument.getPaymentRequestDocument();

        for (OlePaymentRequestItem preqItemToTemplate : (List<OlePaymentRequestItem>) preqDocument.getItems()) {
            if (preqItemToTemplate.getItemType().isLineItemIndicator() && ((preqItemToTemplate.getItemType().isQuantityBasedGeneralLedgerIndicator() && preqItemToTemplate.getItemQuantity().isNonZero())
                    || (preqItemToTemplate.getItemType().isAmountBasedGeneralLedgerIndicator() && preqItemToTemplate.getTotalAmount().isNonZero()))) {
                cmDocument.getItems().add(new OleCreditMemoItem(cmDocument, preqItemToTemplate, (OlePurchaseOrderItem) preqItemToTemplate.getPurchaseOrderItem(), expiredOrClosedAccountList));
            }
        }

        // add below the line items
        purapService.addBelowLineItems(cmDocument);

        cmDocument.fixItemReferences();
    }

    /**
     * Populate Credit Memo of type Payment Request.
     *
     * @param cmDocument - Credit Memo Document to Populate
     */
    @Override
    protected void populateDocumentFromPreq(VendorCreditMemoDocument cmDocument, HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList) {
        PaymentRequestDocument paymentRequestDocument = olePaymentRequestService.getPaymentRequestById(cmDocument.getPaymentRequestIdentifier());
        cmDocument.getDocumentHeader().setOrganizationDocumentNumber(paymentRequestDocument.getDocumentHeader().getOrganizationDocumentNumber());
        cmDocument.setPaymentRequestDocument(paymentRequestDocument);
        cmDocument.setPurchaseOrderDocument(paymentRequestDocument.getPurchaseOrderDocument());
        cmDocument.setUseTaxIndicator(paymentRequestDocument.isUseTaxIndicator());

        // credit memo address taken directly from payment request
        cmDocument.setVendorHeaderGeneratedIdentifier(paymentRequestDocument.getVendorHeaderGeneratedIdentifier());
        cmDocument.setVendorDetailAssignedIdentifier(paymentRequestDocument.getVendorDetailAssignedIdentifier());
        cmDocument.setVendorAddressGeneratedIdentifier(paymentRequestDocument.getVendorAddressGeneratedIdentifier());
        cmDocument.setVendorCustomerNumber(paymentRequestDocument.getVendorCustomerNumber());
        cmDocument.setVendorName(paymentRequestDocument.getVendorName());
        cmDocument.setVendorLine1Address(paymentRequestDocument.getVendorLine1Address());
        cmDocument.setVendorLine2Address(paymentRequestDocument.getVendorLine2Address());
        cmDocument.setVendorCityName(paymentRequestDocument.getVendorCityName());
        cmDocument.setVendorStateCode(paymentRequestDocument.getVendorStateCode());
        cmDocument.setVendorPostalCode(paymentRequestDocument.getVendorPostalCode());
        cmDocument.setVendorCountryCode(paymentRequestDocument.getVendorCountryCode());
        cmDocument.setVendorAttentionName(paymentRequestDocument.getVendorAttentionName());
        cmDocument.setAccountsPayablePurchasingDocumentLinkIdentifier(paymentRequestDocument.getAccountsPayablePurchasingDocumentLinkIdentifier());
        cmDocument.setPaymentMethodId(paymentRequestDocument.getVendorDetail().getPaymentMethodId());
        cmDocument.setPurchaseOrderTypeId(paymentRequestDocument.getPurchaseOrderTypeId());
        // prep the item lines (also collect warnings for later display) this is only done on paymentRequest
        purapAccountingService.convertMoneyToPercent(paymentRequestDocument);
        populateItemLinesFromPreq(cmDocument, expiredOrClosedAccountList);
    }


    @Override
    protected void populateDocumentFromPO(VendorCreditMemoDocument cmDocument, HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList) {
        PurchaseOrderDocument purchaseOrderDocument = olePurchaseOrderService.getCurrentPurchaseOrder(cmDocument.getPurchaseOrderIdentifier());
        cmDocument.setPurchaseOrderDocument(purchaseOrderDocument);
        cmDocument.getDocumentHeader().setOrganizationDocumentNumber(purchaseOrderDocument.getDocumentHeader().getOrganizationDocumentNumber());
        cmDocument.setUseTaxIndicator(cmDocument.isUseTaxIndicator());

        cmDocument.setVendorHeaderGeneratedIdentifier(purchaseOrderDocument.getVendorHeaderGeneratedIdentifier());
        cmDocument.setVendorDetailAssignedIdentifier(purchaseOrderDocument.getVendorDetailAssignedIdentifier());
        cmDocument.setVendorCustomerNumber(purchaseOrderDocument.getVendorCustomerNumber());
        cmDocument.setVendorName(purchaseOrderDocument.getVendorName());
        cmDocument.setAccountsPayablePurchasingDocumentLinkIdentifier(purchaseOrderDocument.getAccountsPayablePurchasingDocumentLinkIdentifier());
        cmDocument.setPaymentMethodId(purchaseOrderDocument.getVendorDetail().getPaymentMethodId());
        cmDocument.setPurchaseOrderTypeId(purchaseOrderDocument.getPurchaseOrderTypeId());
        // populate cm vendor address with the default remit address type for the vendor if found
        String userCampus = GlobalVariables.getUserSession().getPerson().getCampusCode();
        VendorAddress vendorAddress = vendorService.getVendorDefaultAddress(purchaseOrderDocument.getVendorHeaderGeneratedIdentifier(), purchaseOrderDocument.getVendorDetailAssignedIdentifier(), VendorConstants.AddressTypes.REMIT, userCampus);
        if (vendorAddress != null) {
            cmDocument.templateVendorAddress(vendorAddress);
            cmDocument.setVendorAddressGeneratedIdentifier(vendorAddress.getVendorAddressGeneratedIdentifier());
            cmDocument.setVendorAttentionName(StringUtils.defaultString(vendorAddress.getVendorAttentionName()));
        } else {
            // set address from PO
            cmDocument.setVendorAddressGeneratedIdentifier(purchaseOrderDocument.getVendorAddressGeneratedIdentifier());
            cmDocument.setVendorLine1Address(purchaseOrderDocument.getVendorLine1Address());
            cmDocument.setVendorLine2Address(purchaseOrderDocument.getVendorLine2Address());
            cmDocument.setVendorCityName(purchaseOrderDocument.getVendorCityName());
            cmDocument.setVendorStateCode(purchaseOrderDocument.getVendorStateCode());
            cmDocument.setVendorPostalCode(purchaseOrderDocument.getVendorPostalCode());
            cmDocument.setVendorCountryCode(purchaseOrderDocument.getVendorCountryCode());


            boolean blankAttentionLine = CoreFrameworkServiceLocator.getParameterService().getParameterValueAsBoolean(PurapConstants.PURAP_NAMESPACE, "Document", PurapParameterConstants.BLANK_ATTENTION_LINE_FOR_PO_TYPE_ADDRESS, false);
            if (blankAttentionLine) {
                cmDocument.setVendorAttentionName(StringUtils.EMPTY);
            } else {
                cmDocument.setVendorAttentionName(StringUtils.defaultString(purchaseOrderDocument.getVendorAttentionName()));
            }
        }

        populateItemLinesFromPO(cmDocument, expiredOrClosedAccountList);
    }

    /**
     * Populates the credit memo items from the payment request items.
     *
     * @param cmDocument - Credit Memo Document to Populate
     */
    @Override
    protected void populateItemLinesFromPO(VendorCreditMemoDocument cmDocument, HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList) {
        List<PurchaseOrderItem> invoicedItems = getPOInvoicedItems(cmDocument.getPurchaseOrderDocument());
        for (PurchaseOrderItem poItem : invoicedItems) {
            if ((poItem.getItemType().isQuantityBasedGeneralLedgerIndicator() && poItem.getItemInvoicedTotalQuantity().isNonZero())
                    || (poItem.getItemType().isAmountBasedGeneralLedgerIndicator() && poItem.getItemInvoicedTotalAmount().isNonZero())) {
                OleCreditMemoItem olecreditMemoItem = new OleCreditMemoItem(cmDocument, (OlePurchaseOrderItem) poItem, expiredOrClosedAccountList);
                cmDocument.getItems().add(olecreditMemoItem);
                PurchasingCapitalAssetItem purchasingCAMSItem = cmDocument.getPurchaseOrderDocument().getPurchasingCapitalAssetItemByItemIdentifier(poItem.getItemIdentifier());
                if (purchasingCAMSItem != null) {
                    olecreditMemoItem.setCapitalAssetTransactionTypeCode(purchasingCAMSItem.getCapitalAssetTransactionTypeCode());
                }
            }
        }

        // add below the line items
        purapService.addBelowLineItems(cmDocument);

        cmDocument.fixItemReferences();
    }

    @Override
    protected void populateDocumentFromVendor(VendorCreditMemoDocument cmDocument) {
        Integer vendorHeaderId = VendorUtils.getVendorHeaderId(cmDocument.getVendorNumber());
        Integer vendorDetailId = VendorUtils.getVendorDetailId(cmDocument.getVendorNumber());

        VendorDetail vendorDetail = vendorService.getVendorDetail(vendorHeaderId, vendorDetailId);
        cmDocument.setVendorDetail(vendorDetail);

        cmDocument.setVendorHeaderGeneratedIdentifier(vendorDetail.getVendorHeaderGeneratedIdentifier());
        cmDocument.setVendorDetailAssignedIdentifier(vendorDetail.getVendorDetailAssignedIdentifier());
        cmDocument.setVendorCustomerNumber(vendorDetail.getVendorNumber());
        cmDocument.setVendorName(vendorDetail.getVendorName());
        cmDocument.setPaymentMethodId(vendorDetail.getPaymentMethodId());

        // credit memo type vendor uses the default remit type address for the vendor if found
        String userCampus = GlobalVariables.getUserSession().getPerson().getCampusCode();
        VendorAddress vendorAddress = vendorService.getVendorDefaultAddress(vendorHeaderId, vendorDetailId, VendorConstants.AddressTypes.REMIT, userCampus);
        if (vendorAddress == null) {
            // pick up the default vendor po address type
            vendorAddress = vendorService.getVendorDefaultAddress(vendorHeaderId, vendorDetailId, VendorConstants.AddressTypes.PURCHASE_ORDER, userCampus);
        }

        cmDocument.setVendorAddressGeneratedIdentifier(vendorAddress.getVendorAddressGeneratedIdentifier());
        cmDocument.templateVendorAddress(vendorAddress);

        // add below the line items
        purapService.addBelowLineItems(cmDocument);
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.CreditMemoCreateService# populateDocumentAfterInit(org.kuali.ole.module.purap.document.CreditMemoDocument)
     */
    @Override
    public void populateDocumentAfterInit(VendorCreditMemoDocument cmDocument) {

        OleVendorCreditMemoDocument vendorCreditMemoDocument = (OleVendorCreditMemoDocument) cmDocument;
        // make a call to search for expired/closed accounts
        HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList = accountsPayableService.getExpiredOrClosedAccountList(cmDocument);

        if (vendorCreditMemoDocument.isSourceDocumentPaymentRequest() && vendorCreditMemoDocument.getInvoiceIdentifier() == null) {
            populateDocumentFromPreq(vendorCreditMemoDocument, expiredOrClosedAccountList);
        } else if (vendorCreditMemoDocument.isSourceDocumentPurchaseOrder() && vendorCreditMemoDocument.getInvoiceIdentifier() == null) {
            populateDocumentFromPO(vendorCreditMemoDocument, expiredOrClosedAccountList);
        } else if (vendorCreditMemoDocument.getInvoiceIdentifier() != null) {
            populateDocumentFromInvoice(vendorCreditMemoDocument, expiredOrClosedAccountList);
        } else {
            populateDocumentFromVendor(vendorCreditMemoDocument);
        }

        populateDocumentDescription(vendorCreditMemoDocument);

        // write a note for expired/closed accounts if any exist and add a message stating there were expired/closed accounts at the
        // top of the document
        accountsPayableService.generateExpiredOrClosedAccountNote(cmDocument, expiredOrClosedAccountList);

        // set indicator so a message is displayed for accounts that were replaced due to expired/closed status
        if (ObjectUtils.isNotNull(expiredOrClosedAccountList) && !expiredOrClosedAccountList.isEmpty()) {
            cmDocument.setContinuationAccountIndicator(true);
        }

    }

    @Override
    public void populateDocumentFromInvoice(OleVendorCreditMemoDocument cmDocument, HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList) {
        OleInvoiceDocument invoiceDocument = oleInvoiceService.getInvoiceDocumentById(cmDocument.getInvoiceIdentifier());
        //cmDocument.setPurchaseOrderDocument();
        cmDocument.getDocumentHeader().setOrganizationDocumentNumber(invoiceDocument.getDocumentHeader().getOrganizationDocumentNumber());
        cmDocument.setUseTaxIndicator(cmDocument.isUseTaxIndicator());
        cmDocument.setPaymentMethodId(invoiceDocument.getPaymentMethodId());
        cmDocument.setOlePaymentMethod(invoiceDocument.getPaymentMethod());
        cmDocument.setVendorHeaderGeneratedIdentifier(invoiceDocument.getVendorHeaderGeneratedIdentifier());
        cmDocument.setVendorDetailAssignedIdentifier(invoiceDocument.getVendorDetailAssignedIdentifier());
        cmDocument.setVendorCustomerNumber(invoiceDocument.getVendorCustomerNumber());
        cmDocument.setVendorName(invoiceDocument.getVendorName());
       // cmDocument.setAccountsPayablePurchasingDocumentLinkIdentifier(invoiceDocument.getAccountsPayablePurchasingDocumentLinkIdentifier());
        cmDocument.setPaymentMethodId(invoiceDocument.getPaymentMethodId());
        cmDocument.setPurchaseOrderTypeId(invoiceDocument.getPurchaseOrderTypeId());
        // populate cm vendor address with the default remit address type for the vendor if found
        String userCampus = GlobalVariables.getUserSession().getPerson().getCampusCode();
        VendorAddress vendorAddress = vendorService.getVendorDefaultAddress(invoiceDocument.getVendorHeaderGeneratedIdentifier(), invoiceDocument.getVendorDetailAssignedIdentifier(), VendorConstants.AddressTypes.REMIT, userCampus);
        if (vendorAddress != null) {
            cmDocument.templateVendorAddress(vendorAddress);
            cmDocument.setVendorAddressGeneratedIdentifier(vendorAddress.getVendorAddressGeneratedIdentifier());
            cmDocument.setVendorAttentionName(StringUtils.defaultString(vendorAddress.getVendorAttentionName()));
        } else {
            // set address from PO
            cmDocument.setVendorAddressGeneratedIdentifier(invoiceDocument.getVendorAddressGeneratedIdentifier());
            cmDocument.setVendorLine1Address(invoiceDocument.getVendorLine1Address());
            cmDocument.setVendorLine2Address(invoiceDocument.getVendorLine2Address());
            cmDocument.setVendorCityName(invoiceDocument.getVendorCityName());
            cmDocument.setVendorStateCode(invoiceDocument.getVendorStateCode());
            cmDocument.setVendorPostalCode(invoiceDocument.getVendorPostalCode());
            cmDocument.setVendorCountryCode(invoiceDocument.getVendorCountryCode());


            boolean blankAttentionLine = CoreFrameworkServiceLocator.getParameterService().getParameterValueAsBoolean(PurapConstants.PURAP_NAMESPACE, "Document", PurapParameterConstants.BLANK_ATTENTION_LINE_FOR_PO_TYPE_ADDRESS, false);
            if (blankAttentionLine) {
                cmDocument.setVendorAttentionName(StringUtils.EMPTY);
            } else {
                cmDocument.setVendorAttentionName(StringUtils.defaultString(invoiceDocument.getVendorAttentionName()));
            }
        }

        //populateItemLinesFromInvoice(invoiceDocument, cmDocument, expiredOrClosedAccountList);
    }


    /**
     * Populates the credit memo items from the payment request items.
     *
     * @param cmDocument - Credit Memo Document to Populate
     */
    @Override
    public void populateItemLinesFromInvoice(OleInvoiceDocument invoiceDocument, OleVendorCreditMemoDocument cmDocument,
                                             HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList) {

        List<OleInvoiceItem> invoicedItems = invoiceDocument.getItems();
        for (OleInvoiceItem invoiceItem : invoicedItems) {
            if (invoiceItem.getExtendedPrice().isLessThan(AbstractKualiDecimal.ZERO)) {
                /*invoiceItem.setItemListPrice(invoiceItem.getItemListPrice().abs());
                KualiDecimal listPrice = invoiceItem.getItemListPrice().abs();
                BigDecimal unitPrice = invoiceItem.getItemUnitPrice();
                BigDecimal itemSurcharge = invoiceItem.getItemSurcharge();
                invoiceItem.setItemListPrice(listPrice);
                invoiceItem.setItemUnitPrice(unitPrice.abs());
                if (invoiceItem.getItemUnitPrice() != null) {
                    invoiceItem.setItemUnitPrice(invoiceItem.getItemUnitPrice().abs());
                }*/
                if (!invoiceItem.isDebitItem() && ((invoiceItem.getItemType().isQuantityBasedGeneralLedgerIndicator() && invoiceItem.getItemQuantity().isNonZero())
                        || (invoiceItem.getItemType().isAmountBasedGeneralLedgerIndicator() && invoiceItem.getTotalAmount().isNonZero()))) {
                    OleCreditMemoItem olecreditMemoItem = new OleCreditMemoItem(invoiceItem, cmDocument, expiredOrClosedAccountList);
                    cmDocument.getItems().add(olecreditMemoItem);
                    PurchasingCapitalAssetItem purchasingCAMSItem = cmDocument.getPurchaseOrderDocument().getPurchasingCapitalAssetItemByItemIdentifier(invoiceItem.getItemIdentifier());
                    if (purchasingCAMSItem != null) {
                        olecreditMemoItem.setCapitalAssetTransactionTypeCode(purchasingCAMSItem.getCapitalAssetTransactionTypeCode());
                    }
                }
            }

        }

        // add below the line items
        purapService.addBelowLineItems(cmDocument);

        cmDocument.fixItemReferences();
    }

    /**
     * NOTE: in the event of auto-approval failure, this method may throw a RuntimeException, indicating to Spring transactional
     * management that the transaction should be rolled back.
     *
     * @see org.kuali.ole.select.document.service.OleCreditMemoService# autoApproveCreditMemo(org.kuali.ole.module.purap.document.PaymentRequestDocument,
     *      org.kuali.rice.core.api.util.type.KualiDecimal)
     */
    @Override
    public boolean autoApproveCreditMemo(OleVendorCreditMemoDocument creditMemoDoc) {
        try {
            // Much of the rice frameworks assumes that document instances that are saved via DocumentService.saveDocument are
            // those
            // that were dynamically created by PojoFormBase (i.e., the Document instance wasn't created from OJB). We need to
            // make
            // a deep copy and materialize collections to fulfill that assumption so that collection elements will delete
            // properly

            // TODO: maybe rewriting PurapService.calculateItemTax could be rewritten so that the a deep copy doesn't need to be
            // made
            // by taking advantage of OJB's managed array lists
            try {
                ObjectUtils.materializeUpdateableCollections(creditMemoDoc);
                for (OleCreditMemoItem item : (List<OleCreditMemoItem>) creditMemoDoc.getItems()) {
                    ObjectUtils.materializeUpdateableCollections(item);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            creditMemoDoc = (OleVendorCreditMemoDocument) ObjectUtils.deepCopy(creditMemoDoc);
            //purapService.updateStatus(doc, PaymentRequestStatuses.AUTO_APPROVED);
            creditMemoDoc.updateAndSaveAppDocStatus(PurapConstants.CreditMemoStatuses.APPDOC_COMPLETE);

            documentService.blanketApproveDocument(creditMemoDoc, "auto-approving: Total is below threshold.", null);
        } catch (WorkflowException we) {
            LOG.error("Exception encountered when approving document number " + creditMemoDoc.getDocumentNumber() + ".", we);
            // throw a runtime exception up so that we can force a rollback
            throw new RuntimeException("Exception encountered when approving document number " + creditMemoDoc.getDocumentNumber() + ".", we);
        }
        return true;
    }

    public boolean validateVendorCreditMemo(OleVendorCreditMemoDocument oleVendorCreditMemoDocument) {
        for (OleCreditMemoItem item : (List<OleCreditMemoItem>) oleVendorCreditMemoDocument.getItems()) {
            if (StringUtils.equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_MISC_CRDT_CODE, item.getItemTypeCode()) ||
                    StringUtils.equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_RESTCK_FEE_CODE, item.getItemTypeCode()) ||
                    StringUtils.equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_FREIGHT_CODE, item.getItemTypeCode()) ||
                    StringUtils.equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_SHIP_AND_HAND_CODE, item.getItemTypeCode()) ||
                    StringUtils.equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_MISC_CODE, item.getItemTypeCode()) ||
                    StringUtils.equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_MIN_ORDER_CODE, item.getItemTypeCode())) {
                if (item.getItemUnitPrice() != null && ((OleVendorCreditMemoDocument) oleVendorCreditMemoDocument).getInvoiceIdentifier() == null && oleVendorCreditMemoDocument.getProrateBy() == null) {
                    return false;
                }
            }
        }
        return true;
    }

}