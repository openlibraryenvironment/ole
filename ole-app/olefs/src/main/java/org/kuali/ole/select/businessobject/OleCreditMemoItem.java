/*
 * Copyright 2012 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.businessobject;

import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.module.purap.businessobject.CreditMemoItem;
import org.kuali.ole.module.purap.businessobject.PaymentRequestAccount;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLineBase;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.VendorCreditMemoDocument;
import org.kuali.ole.module.purap.document.service.AccountsPayableService;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.module.purap.util.ExpiredOrClosedAccountEntry;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.document.OleVendorCreditMemoDocument;
import org.kuali.ole.select.lookup.DocData;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.HashMap;

public class OleCreditMemoItem extends CreditMemoItem {

    // Foreign Currency Conversion
    protected String itemCurrencyType;
    protected KualiDecimal itemForeignListPrice;
    protected KualiDecimal itemForeignDiscount;
    protected String itemForeignDiscountType;
    protected KualiDecimal itemForeignDiscountAmt;
    protected KualiDecimal itemForeignUnitCost;
    protected BigDecimal itemExchangeRate;
    protected KualiDecimal itemUnitCostUSD;
    //Bib details
    private BibInfoBean bibInfoBean;
    private DocData docData;
    private OleOrderRecord oleOrderRecord;
    protected String itemTitleId;
    protected String bibUUID;
    private String docFormat;
    protected BigDecimal itemSurcharge;

    protected KualiDecimal foreignCurrencyExtendedPrice;
    protected boolean additionalChargeUsd;

    public BibInfoBean getBibInfoBean() {
        return bibInfoBean;
    }

    public void setBibInfoBean(BibInfoBean bibInfoBean) {
        this.bibInfoBean = bibInfoBean;
    }

    public DocData getDocData() {
        return docData;
    }

    public void setDocData(DocData docData) {
        this.docData = docData;
    }

    public OleOrderRecord getOleOrderRecord() {
        return oleOrderRecord;
    }

    public void setOleOrderRecord(OleOrderRecord oleOrderRecord) {
        this.oleOrderRecord = oleOrderRecord;
    }

    public String getItemTitleId() {
        return itemTitleId;
    }

    public void setItemTitleId(String itemTitleId) {
        this.itemTitleId = itemTitleId;
    }

    public String getBibUUID() {
        return bibUUID;
    }

    public void setBibUUID(String bibUUID) {
        this.bibUUID = bibUUID;
    }

    public KualiDecimal getForeignCurrencyExtendedPrice() {
        return foreignCurrencyExtendedPrice;
    }

    public void setForeignCurrencyExtendedPrice(KualiDecimal foreignCurrencyExtendedPrice) {
        this.foreignCurrencyExtendedPrice = foreignCurrencyExtendedPrice;
    }

    public boolean isAdditionalChargeUsd() {
        return additionalChargeUsd;
    }

    public void setAdditionalChargeUsd(boolean additionalChargeUsd) {
        this.additionalChargeUsd = additionalChargeUsd;
    }

    /**
     * Default constructor.
     */
    public OleCreditMemoItem() {
        this.setPoInvoicedTotalQuantity(new KualiDecimal(1));
        this.setItemQuantity(new KualiDecimal(1.0));
        this.setPoUnitPrice(new BigDecimal(0.0));
    }

    public String getDocFormat() {
        return docFormat;
    }

    public void setDocFormat(String docFormat) {
        this.docFormat = docFormat;
    }

    /**
     * Constructs a CreditMemoItem object from an existing Purchase Order Item. - Delegate
     *
     * @param cmDocument the Credit Memo Document this item belongs to.
     * @param poItem     the Purchase Order Item to copy from.
     */
    public OleCreditMemoItem(VendorCreditMemoDocument cmDocument, OlePurchaseOrderItem poItem) {
        super(cmDocument, poItem, new HashMap<String, ExpiredOrClosedAccountEntry>());
    }

    /**
     * Constructs a CreditMemoItem object from an existing Purchase Order Item, and check and process expired or closed accounts
     * item might contain.
     *
     * @param cmDocument                 the Credit Memo Document this item belongs to.
     * @param poItem                     the Purchase Order Item to copy from.
     * @param expiredOrClosedAccountList the list of expired or closed accounts to check against.
     */
    public OleCreditMemoItem(VendorCreditMemoDocument cmDocument, OlePurchaseOrderItem poItem, HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList) {

        setPurapDocumentIdentifier(cmDocument.getPurapDocumentIdentifier());
        setPurapDocument(cmDocument);
        setItemLineNumber(poItem.getItemLineNumber());
        setPoInvoicedTotalQuantity(poItem.getItemInvoicedTotalQuantity());
        setPoUnitPrice(poItem.getItemUnitPrice());
        setPoTotalAmount(poItem.getItemInvoicedTotalAmount());
        setItemTypeCode(poItem.getItemTypeCode());
        setBibInfoBean(poItem.getBibInfoBean());
        this.setBibUUID(poItem.getItemTitleId());
        setItemTitleId(poItem.getItemTitleId());
        setItemQuantity(poItem.getItemQuantity());
        if(poItem.getItemTitleId()!=null){
            this.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(poItem.getItemTitleId()));
        }
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

        for (Object element : poItem.getSourceAccountingLines()) {
            PurApAccountingLineBase account = (PurApAccountingLineBase) element;
            account.getAmount();
            // check if this account is expired/closed and replace as needed
            SpringContext.getBean(AccountsPayableService.class).processExpiredOrClosedAccount(account, expiredOrClosedAccountList);

            getSourceAccountingLines().add(new OleCreditMemoAccount(account));
        }

        // Foreign Currency Conversion
        setItemCurrencyType(poItem.getItemCurrencyType());
        setItemForeignListPrice(poItem.getItemForeignListPrice());
        setItemForeignDiscount(poItem.getItemForeignDiscount());
        setItemForeignDiscountAmt(poItem.getItemForeignDiscountAmt());
        setItemForeignDiscountType(poItem.getItemForeignDiscountType());
        setItemForeignUnitCost(poItem.getItemForeignUnitCost());
        setItemExchangeRate(poItem.getItemExchangeRate());
        setItemUnitCostUSD(poItem.getItemUnitCostUSD());

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
    public OleCreditMemoItem(VendorCreditMemoDocument cmDocument, OlePaymentRequestItem preqItem, OlePurchaseOrderItem poItem, HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList) {

        super();

        setPurapDocumentIdentifier(cmDocument.getPurapDocumentIdentifier());
        setItemLineNumber(preqItem.getItemLineNumber());
        this.setPurapDocument(cmDocument);
        setItemQuantity(preqItem.getItemQuantity());
        this.setBibUUID(preqItem.getItemTitleId());
        /*
         * // take invoiced quantities from the lower of the preq and po if different if (poItem.getItemInvoicedTotalQuantity() !=
         * null && preqItem.getItemQuantity() != null &&
         * poItem.getItemInvoicedTotalQuantity().isLessThan(preqItem.getItemQuantity())) {
         * setPreqInvoicedTotalQuantity(poItem.getItemInvoicedTotalQuantity());
         * setPreqTotalAmount(poItem.getItemInvoicedTotalAmount()); } else {
         */
        setPreqInvoicedTotalQuantity(preqItem.getItemQuantity());
        setPreqTotalAmount(preqItem.getTotalAmount());
        /* } */

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

        for (Object element : preqItem.getSourceAccountingLines()) {
            PaymentRequestAccount account = (PaymentRequestAccount) element;

            // check if this account is expired/closed and replace as needed
            SpringContext.getBean(AccountsPayableService.class).processExpiredOrClosedAccount(account, expiredOrClosedAccountList);

            getSourceAccountingLines().add(new OleCreditMemoAccount(account));
        }

        // Foreign Currency Conversion
        setItemCurrencyType(preqItem.getItemCurrencyType());
        setItemForeignListPrice(preqItem.getItemForeignListPrice());
        setItemForeignDiscount(preqItem.getItemForeignDiscount());
        setItemForeignDiscountAmt(preqItem.getItemForeignDiscountAmt());
        setItemForeignDiscountType(preqItem.getItemForeignDiscountType());
        setItemForeignUnitCost(preqItem.getItemForeignUnitCost());
        setItemExchangeRate(preqItem.getItemExchangeRate());
        setItemUnitCostUSD(preqItem.getItemUnitCostUSD());

    }

    public String getItemCurrencyType() {
        return itemCurrencyType;
    }

    public void setItemCurrencyType(String itemCurrencyType) {
        this.itemCurrencyType = itemCurrencyType;
    }

    public KualiDecimal getItemForeignListPrice() {
        return itemForeignListPrice;
    }

    public void setItemForeignListPrice(KualiDecimal itemForeignListPrice) {
        this.itemForeignListPrice = itemForeignListPrice;
    }

    public KualiDecimal getItemForeignDiscount() {
        return itemForeignDiscount;
    }

    public void setItemForeignDiscount(KualiDecimal itemForeignDiscount) {
        this.itemForeignDiscount = itemForeignDiscount;
    }

    public String getItemForeignDiscountType() {
        return itemForeignDiscountType;
    }

    public void setItemForeignDiscountType(String itemForeignDiscountType) {
        this.itemForeignDiscountType = itemForeignDiscountType;
    }

    public KualiDecimal getItemForeignDiscountAmt() {
        return itemForeignDiscountAmt;
    }

    public void setItemForeignDiscountAmt(KualiDecimal itemForeignDiscountAmt) {
        this.itemForeignDiscountAmt = itemForeignDiscountAmt;
    }

    public KualiDecimal getItemForeignUnitCost() {
        return itemForeignUnitCost;
    }

    public void setItemForeignUnitCost(KualiDecimal itemForeignUnitCost) {
        this.itemForeignUnitCost = itemForeignUnitCost;
    }

    public BigDecimal getItemExchangeRate() {
        return itemExchangeRate;
    }

    public void setItemExchangeRate(BigDecimal itemExchangeRate) {
        this.itemExchangeRate = itemExchangeRate;
    }

    public KualiDecimal getItemUnitCostUSD() {
        return itemUnitCostUSD;
    }

    public void setItemUnitCostUSD(KualiDecimal itemUnitCostUSD) {
        this.itemUnitCostUSD = itemUnitCostUSD;
    }

    public BigDecimal getItemSurcharge() {
        return itemSurcharge;
    }

    public void setItemSurcharge(BigDecimal itemSurcharge) {
        this.itemSurcharge = itemSurcharge;
    }

    /**
     * @see org.kuali.ole.module.purap.businessobject.PurchasingItemBase#getAccountingLineClass()
     */
    @Override
    public Class getAccountingLineClass() {
        return OleCreditMemoAccount.class;
    }

    /**
     * Constructs a OlePaymentRequestItem.java.
     *
     * @param olePoi
     * @param vendorCreditMemoDoc
     * @param expiredOrClosedAccountList
     */
    public OleCreditMemoItem(OleInvoiceItem olePoi, OleVendorCreditMemoDocument vendorCreditMemoDoc, HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList) {
        super(olePoi, vendorCreditMemoDoc, expiredOrClosedAccountList);

        this.setPurapDocument(vendorCreditMemoDoc);
        //LOG.debug("Inside OlePaymentRequestItem Constructor");
        //LOG.debug("Setting the Format,Discount and Price Details");
        //notes = new ArrayList<OlePaymentRequestNote>();
        // OleInvoiceItem olePoi = (OleInvoiceItem)poi;
        olePoi.getItemTypeCode();
        this.setItemForeignListPrice(olePoi.getItemForeignListPrice());
        this.setItemForeignDiscount(olePoi.getItemForeignDiscount());
        this.setItemForeignDiscountType(olePoi.getItemForeignDiscountType());
        this.setItemForeignDiscountAmt(olePoi.getItemForeignDiscountAmt());
        this.setItemForeignUnitCost(olePoi.getItemForeignUnitCost());
        this.setItemExchangeRate(olePoi.getItemExchangeRate());
        this.setItemUnitCostUSD(olePoi.getItemUnitCostUSD());
        this.setTotalAmount(olePoi.getTotalAmount());
        this.setItemTypeCode(olePoi.getItemTypeCode());
        this.setExtendedPrice(olePoi.getExtendedPrice());
        /*this.setFormatTypeId(olePoi.getFormatTypeId());
        this.setItemNoOfParts(olePoi.getItemNoOfParts());
        this.setItemListPrice(olePoi.getItemListPrice());
        this.setItemDiscount(olePoi.getItemDiscount());
        this.setItemDiscountType(olePoi.getItemDiscountType());*/
        this.setItemCatalogNumber(olePoi.getItemCatalogNumber());
        //this.set(olePoi.getItemIdentifier());
        this.setExtendedPrice(olePoi.getExtendedPrice());
        this.setBibInfoBean(olePoi.getBibInfoBean());
        this.setItemTitleId(olePoi.getItemTitleId());
        this.setItemQuantity(olePoi.getItemQuantity());
        this.setItemUnitPrice(olePoi.getItemUnitPrice());
        this.setExtendedPrice(olePoi.getExtendedPrice());
        if (vendorCreditMemoDoc.getPaymentMethodId() != null) {
            OlePaymentMethod olePaymentMethod = SpringContext.getBean(BusinessObjectService.class)
                    .findBySinglePrimaryKey(OlePaymentMethod.class, vendorCreditMemoDoc.getPaymentMethodId());
            vendorCreditMemoDoc.setPaymentMethod(olePaymentMethod.getPaymentMethod());
            vendorCreditMemoDoc.setPaymentMethodId(olePaymentMethod.getPaymentMethodId());
        }

        // added for OLE-2203
        this.setItemCurrencyType(OleSelectConstant.USD);
        //this.setReceiptStatusId(olePoi.getReceiptStatusId());
    }
}