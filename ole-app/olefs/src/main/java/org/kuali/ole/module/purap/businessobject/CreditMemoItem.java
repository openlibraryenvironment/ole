/*
 * Copyright 2006 The Kuali Foundation
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

package org.kuali.ole.module.purap.businessobject;

import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.document.PaymentRequestDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.VendorCreditMemoDocument;
import org.kuali.ole.module.purap.document.service.AccountsPayableService;
import org.kuali.ole.module.purap.document.service.PaymentRequestService;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.module.purap.document.service.PurchaseOrderService;
import org.kuali.ole.module.purap.exception.PurError;
import org.kuali.ole.module.purap.util.ExpiredOrClosedAccountEntry;
import org.kuali.ole.module.purap.util.PurApObjectUtils;
import org.kuali.ole.select.businessobject.OleInvoiceItem;
import org.kuali.ole.select.document.OleVendorCreditMemoDocument;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Item line Business Object for Credit Memo Document.
 */
public class CreditMemoItem extends AccountsPayableItemBase {
    private KualiDecimal poInvoicedTotalQuantity;
    private BigDecimal poUnitPrice;
    private KualiDecimal poTotalAmount;
    private KualiDecimal preqInvoicedTotalQuantity;
    private BigDecimal preqUnitPrice;
    private KualiDecimal preqTotalAmount;

    /**
     * Default constructor.
     */
    public CreditMemoItem() {
    }

    /**
     * Constructs a CreditMemoItem object from an existing Purchase Order Item. - Delegate
     *
     * @param cmDocument the Credit Memo Document this item belongs to.
     * @param poItem     the Purchase Order Item to copy from.
     */
    public CreditMemoItem(VendorCreditMemoDocument cmDocument, PurchaseOrderItem poItem) {
        this(cmDocument, poItem, new HashMap<String, ExpiredOrClosedAccountEntry>());
    }

    /**
     * Constructs a CreditMemoItem object from an existing Purchase Order Item, and check and process expired or closed accounts
     * item might contain.
     *
     * @param cmDocument                 the Credit Memo Document this item belongs to.
     * @param poItem                     the Purchase Order Item to copy from.
     * @param expiredOrClosedAccountList the list of expired or closed accounts to check against.
     */
    public CreditMemoItem(VendorCreditMemoDocument cmDocument, PurchaseOrderItem poItem, HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList) {
        super();

        setPurapDocumentIdentifier(cmDocument.getPurapDocumentIdentifier());
        setPurapDocument(cmDocument);
        setItemLineNumber(poItem.getItemLineNumber());
        setPoInvoicedTotalQuantity(poItem.getItemInvoicedTotalQuantity());
        setPoUnitPrice(poItem.getItemUnitPrice());
        setPoTotalAmount(poItem.getItemInvoicedTotalAmount());
        setItemTypeCode(poItem.getItemTypeCode());

        //recalculate tax
        calculateTax(cmDocument);

        if ((ObjectUtils.isNotNull(this.getItemType()) && this.getItemType().isAmountBasedGeneralLedgerIndicator())) {
            // setting unit price to be null to be more consistent with other below the line
            this.setItemUnitPrice(null);
        } else {
            setItemUnitPrice(poItem.getItemUnitPrice());
        }

        setItemCatalogNumber(poItem.getItemCatalogNumber());

        setItemDescription(poItem.getItemDescription());

        if (getPoInvoicedTotalQuantity() == null) {
            setPoInvoicedTotalQuantity(KualiDecimal.ZERO);
        }
        if (getPoUnitPrice() == null) {
            setPoUnitPrice(BigDecimal.ZERO);
        }
        if (getPoTotalAmount() == null) {
            setPoTotalAmount(KualiDecimal.ZERO);
        }

        for (Iterator iter = poItem.getSourceAccountingLines().iterator(); iter.hasNext(); ) {
            PurchaseOrderAccount account = (PurchaseOrderAccount) iter.next();

            // check if this account is expired/closed and replace as needed
            SpringContext.getBean(AccountsPayableService.class).processExpiredOrClosedAccount(account, expiredOrClosedAccountList);

            getSourceAccountingLines().add(new CreditMemoAccount(account));
        }
    }

    public void calculateTax(VendorCreditMemoDocument purapDocument) {
        PurchaseOrderDocument pDoc = purapDocument.getPurchaseOrderDocument();
        String deliveryState = pDoc.getDeliveryStateCode();
        String deliveryPostalCode = pDoc.getBillingPostalCode();
        SpringContext.getBean(PurapService.class).calculateTaxForPREQ(purapDocument,deliveryState,deliveryPostalCode);
    }

    /**
     * Constructs a CreditMemoItem object from an existing Payment Request Item, and check and process expired or closed accounts
     * item might contain.
     *
     * @param cmDocument                 the Credit Memo Document this item belongs to.
     * @param preqItem                   the Payment Request Item to copy from.
     * @param poItem                     the Purchase Order Item to copy from.
     * @param expiredOrClosedAccountList the list of expired or closed accounts to check against.
     */
    public CreditMemoItem(VendorCreditMemoDocument cmDocument, PaymentRequestItem preqItem, PurchaseOrderItem poItem, HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList) {
        super();

        setPurapDocumentIdentifier(cmDocument.getPurapDocumentIdentifier());
        setItemLineNumber(preqItem.getItemLineNumber());
        this.setPurapDocument(cmDocument);

        // take invoiced quantities from the lower of the preq and po if different
        if (poItem.getItemInvoicedTotalQuantity() != null && preqItem.getItemQuantity() != null && poItem.getItemInvoicedTotalQuantity().isLessThan(preqItem.getItemQuantity())) {
            setPreqInvoicedTotalQuantity(poItem.getItemInvoicedTotalQuantity());
            setPreqTotalAmount(poItem.getItemInvoicedTotalAmount());
        } else {
            setPreqInvoicedTotalQuantity(preqItem.getItemQuantity());
            setPreqTotalAmount(preqItem.getTotalAmount());
        }

        setPreqUnitPrice(preqItem.getItemUnitPrice());
        setItemTypeCode(preqItem.getItemTypeCode());

        if ((ObjectUtils.isNotNull(this.getItemType()) && this.getItemType().isAmountBasedGeneralLedgerIndicator())) {
            // setting unit price to be null to be more consistent with other below the line
            this.setItemUnitPrice(null);
        } else {
            setItemUnitPrice(preqItem.getItemUnitPrice());
        }

        setItemCatalogNumber(preqItem.getItemCatalogNumber());
        setItemDescription(preqItem.getItemDescription());

        setCapitalAssetTransactionTypeCode(preqItem.getCapitalAssetTransactionTypeCode());

        if (getPreqInvoicedTotalQuantity() == null) {
            setPreqInvoicedTotalQuantity(KualiDecimal.ZERO);
        }
        if (getPreqUnitPrice() == null) {
            setPreqUnitPrice(BigDecimal.ZERO);
        }
        if (getPreqTotalAmount() == null) {
            setPreqTotalAmount(KualiDecimal.ZERO);
        }

        for (Iterator iter = preqItem.getSourceAccountingLines().iterator(); iter.hasNext(); ) {
            PaymentRequestAccount account = (PaymentRequestAccount) iter.next();

            // check if this account is expired/closed and replace as needed
            SpringContext.getBean(AccountsPayableService.class).processExpiredOrClosedAccount(account, expiredOrClosedAccountList);

            getSourceAccountingLines().add(new CreditMemoAccount(account));
        }
    }

    /**
     * @see org.kuali.ole.module.purap.businessobject.PurApItemBase#getAccountingLineClass()
     */
    @Override
    public Class<CreditMemoAccount> getAccountingLineClass() {
        return CreditMemoAccount.class;
    }

    public KualiDecimal getPoTotalAmount() {
        return poTotalAmount;
    }

    public void setPoTotalAmount(KualiDecimal poTotalAmount) {
        this.poTotalAmount = poTotalAmount;
    }

    public KualiDecimal getPoInvoicedTotalQuantity() {
        return poInvoicedTotalQuantity;
    }

    public void setPoInvoicedTotalQuantity(KualiDecimal poInvoicedTotalQuantity) {
        this.poInvoicedTotalQuantity = poInvoicedTotalQuantity;
    }

    public BigDecimal getPoUnitPrice() {
        return poUnitPrice;
    }

    public void setPoUnitPrice(BigDecimal poUnitPrice) {
        this.poUnitPrice = poUnitPrice;
    }

    public KualiDecimal getPreqTotalAmount() {
        return preqTotalAmount;
    }

    public void setPreqTotalAmount(KualiDecimal preqTotalAmount) {
        this.preqTotalAmount = preqTotalAmount;
    }

    public KualiDecimal getPreqInvoicedTotalQuantity() {
        return preqInvoicedTotalQuantity;
    }

    public void setPreqInvoicedTotalQuantity(KualiDecimal preqInvoicedTotalQuantity) {
        this.preqInvoicedTotalQuantity = preqInvoicedTotalQuantity;
    }

    public BigDecimal getPreqUnitPrice() {
        return preqUnitPrice;
    }

    public void setPreqUnitPrice(BigDecimal preqUnitPrice) {
        this.preqUnitPrice = preqUnitPrice;
    }

    @Override
    public Class getUseTaxClass() {
        return CreditMemoItemUseTax.class;
    }

    public PurchaseOrderItem getPurchaseOrderItem() {

        PurchaseOrderItem poi = null;
        //refresh vendor document
        if (ObjectUtils.isNotNull(this.getPurapDocumentIdentifier())) {
            if (ObjectUtils.isNull(this.getVendorCreditMemo())) {
                this.refreshReferenceObject(PurapPropertyConstants.PURAP_DOC);
            }
        }

        //if vendor document not null, then attempt to pull PO off of it
        if (ObjectUtils.isNotNull(getVendorCreditMemo())) {
            PurchaseOrderDocument purchaseOrderDocument = null;
            Integer purchaseOrderDocumentId = getVendorCreditMemo().getPurchaseOrderIdentifier();

            if (getVendorCreditMemo().isSourceDocumentPaymentRequest() && ObjectUtils.isNull(purchaseOrderDocumentId)) {
                PaymentRequestDocument paymentRequestDocument = SpringContext.getBean(PaymentRequestService.class).getPaymentRequestById(getVendorCreditMemo().getPaymentRequestIdentifier());
                purchaseOrderDocumentId = paymentRequestDocument.getPurchaseOrderIdentifier();
            }

            // if we found a valid po id number then check it for reopening
            if (ObjectUtils.isNotNull(purchaseOrderDocumentId)) {
                purchaseOrderDocument = SpringContext.getBean(PurchaseOrderService.class).getCurrentPurchaseOrder(purchaseOrderDocumentId);
            }

            //if we have a PO document, get po item
            if (ObjectUtils.isNotNull(purchaseOrderDocument)) {
                if (this.getItemType().isLineItemIndicator()) {
                    List<PurchaseOrderItem> items = purchaseOrderDocument.getItems();
                    poi = items.get(this.getItemLineNumber().intValue() - 1);

                } else {
                    poi = (PurchaseOrderItem) SpringContext.getBean(PurapService.class).getBelowTheLineByType(purchaseOrderDocument, this.getItemType());
                }
            }
        } else {
            throw new PurError("Credit Memo Object in Purchase Order item line number " + getItemLineNumber() + "or itemType " + getItemTypeCode() + " is null");
        }

        return poi;
    }

    public VendorCreditMemoDocument getVendorCreditMemo() {
        return super.getPurapDocument();
    }

    /**
     * Constructs a new payment request item, but also merges expired accounts.
     *
     * @param poi                        - Ole invoice item
     * @param vendorCreditMemoDoc        - vendor credit memo document
     * @param expiredOrClosedAccountList - list of expired or closed accounts to merge
     */
    public CreditMemoItem(OleInvoiceItem poi, OleVendorCreditMemoDocument vendorCreditMemoDoc, HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList) {

        // copy base attributes w/ extra array of fields not to be copied
        PurApObjectUtils.populateFromBaseClass(PurApItemBase.class, poi, this, PurapConstants.PREQ_ITEM_UNCOPYABLE_FIELDS);

        setItemDescription(poi.getItemDescription());

        //New Source Line should be set for PaymentRequestItem
        resetAccount();

        // set up accounts
        List accounts = new ArrayList();
        for (PurApAccountingLine account : poi.getSourceAccountingLines()) {
            InvoiceAccount invoiceAccount = (InvoiceAccount) account;
            KualiDecimal amount = invoiceAccount.getAmount();
            invoiceAccount.setAmount(invoiceAccount.getAmount().abs());
            // check if this account is expired/closed and replace as needed
            SpringContext.getBean(AccountsPayableService.class).processExpiredOrClosedAccount(invoiceAccount, expiredOrClosedAccountList);
            if (invoiceAccount.getAmount().isZero()) {
                if (SpringContext.getBean(AccountsPayableService.class).canCopyAccountingLinesWithZeroAmount()) {
                    accounts.add(new CreditMemoAccount(this, invoiceAccount));
                }
            } else {
                accounts.add(new CreditMemoAccount(this, invoiceAccount));
            }
            invoiceAccount.setAmount(amount);
        }
        this.poInvoicedTotalQuantity = poi.getPoOutstandingQuantity();
        if (poi.getPurchaseOrderItemUnitPrice() != null) {
            this.poUnitPrice = poi.getPurchaseOrderItemUnitPrice().abs();
        }
        this.setSourceAccountingLines(accounts);
        this.getUseTaxItems().clear();
        //List<PurApItemUseTax> newUseTaxItems = new ArrayList<PurApItemUseTax>();
        /// this.setUseTaxItems(newUseTaxItems);
        //copy use tax items over, and blank out keys (useTaxId and itemIdentifier)
        /*
        this.getUseTaxItems().clear();
        for (PurApItemUseTax useTaxItem : poi.getUseTaxItems()) {
            PaymentRequestItemUseTax newItemUseTax = new PaymentRequestItemUseTax(useTaxItem);
            this.getUseTaxItems().add(newItemUseTax);

        }
        */

        // clear amount and desc on below the line - we probably don't need that null
        // itemType check but it's there just in case remove if it causes problems
        // also do this if of type service
        if ((ObjectUtils.isNotNull(this.getItemType()) && this.getItemType().isAmountBasedGeneralLedgerIndicator())) {
            // setting unit price to be null to be more consistent with other below the line
            this.setItemUnitPrice(null);
        }

        // copy custom


        // set doc fields
        // this.setPurapDocumentIdentifier(preq.getPurapDocumentIdentifier());
        // this.setPurapDocument(preq);
    }
}
