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
import org.kuali.ole.module.purap.PurapParameterConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.document.InvoiceDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.service.AccountsPayableService;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.module.purap.exception.PurError;
import org.kuali.ole.module.purap.util.ExpiredOrClosedAccountEntry;
import org.kuali.ole.module.purap.util.PurApItemUtils;
import org.kuali.ole.module.purap.util.PurApObjectUtils;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.document.service.OleInvoiceService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.impl.OleParameterConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Payment Request Item Business Object.
 */
public class InvoiceItem extends AccountsPayableItemBase {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(InvoiceItem.class);

    private BigDecimal purchaseOrderItemUnitPrice;
    private KualiDecimal itemOutstandingInvoiceQuantity;
    private KualiDecimal itemOutstandingInvoiceAmount;
    private Integer invoiceIdentifier;
    private Integer purchaseOrderIdentifier;
    protected Integer accountsPayablePurchasingDocumentLinkIdentifier;
    protected Integer poLineNumber;
    protected boolean closePurchaseOrderIndicator;
    protected boolean reopenPurchaseOrderIndicator;
    protected Integer postingYear;
    protected String recurringPaymentTypeCode;
    protected RecurringPaymentType recurringPaymentType;
    protected boolean receivingDocumentRequiredIndicator;
    protected PurchaseOrderDocument purchaseOrderDocument;
    private PurchaseOrderItem poItem;

    /**
     * Default constructor.
     */
    public InvoiceItem() {

    }

    /**
     * inv item constructor - Delegate
     *
     * @param poi - purchase order item
     * @param inv - payment request document
     */
    public InvoiceItem(PurchaseOrderItem poi, InvoiceDocument inv) {
        this(poi, inv, new HashMap<String, ExpiredOrClosedAccountEntry>());
    }

    /**
     * Constructs a new payment request item, but also merges expired accounts.
     *
     * @param poi                        - purchase order item
     * @param inv                        - payment request document
     * @param expiredOrClosedAccountList - list of expired or closed accounts to merge
     */
    public InvoiceItem(PurchaseOrderItem poi, InvoiceDocument inv, HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList) {

        // copy base attributes w/ extra array of fields not to be copied
        PurApObjectUtils.populateFromBaseClass(PurApItemBase.class, poi, this, PurapConstants.PREQ_ITEM_UNCOPYABLE_FIELDS);

        setItemDescription(poi.getItemDescription());

        //New Source Line should be set for InvoiceItem
        resetAccount();

        // set up accounts
        List accounts = new ArrayList();
        for (PurApAccountingLine account : poi.getSourceAccountingLines()) {
            PurchaseOrderAccount poa = (PurchaseOrderAccount) account;

            // check if this account is expired/closed and replace as needed
            SpringContext.getBean(AccountsPayableService.class).processExpiredOrClosedAccount(poa, expiredOrClosedAccountList);

            //KFSMI-4522 copy an accounting line with zero dollar amount if system parameter allows
            if (poa.getAmount()!=null && poa.getAmount().isZero()) {
                if (SpringContext.getBean(AccountsPayableService.class).canCopyAccountingLinesWithZeroAmount()) {
                    accounts.add(new InvoiceAccount(this, poa));
                }
            } else {
                accounts.add(new InvoiceAccount(this, poa));
            }
        }

        this.setSourceAccountingLines(accounts);
        this.getUseTaxItems().clear();
        //List<PurApItemUseTax> newUseTaxItems = new ArrayList<PurApItemUseTax>();
        /// this.setUseTaxItems(newUseTaxItems);
        //copy use tax items over, and blank out keys (useTaxId and itemIdentifier)
        /*
        this.getUseTaxItems().clear();
        for (PurApItemUseTax useTaxItem : poi.getUseTaxItems()) {
            InvoiceItemUseTax newItemUseTax = new InvoiceItemUseTax(useTaxItem);
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
        this.purchaseOrderItemUnitPrice = poi.getItemUnitPrice();
//        this.purchaseOrderCommodityCode = poi.getPurchaseOrderCommodityCd();

        // set doc fields
        this.setPurapDocumentIdentifier(inv.getPurapDocumentIdentifier());
        this.setPurapDocument(inv);
    }

    /**
     * Retrieves a purchase order item by inspecting the item type to see if its above the line or below the line and returns the
     * appropriate type.
     *
     * @return - purchase order item
     */
    @Override
    public PurchaseOrderItem getPurchaseOrderItem() {
        if (ObjectUtils.isNotNull(this.getPurapDocumentIdentifier())) {
            if (ObjectUtils.isNull(this.getInvoice())) {
                this.refreshReferenceObject(PurapPropertyConstants.PURAP_DOC);
            }
        }
        PurchaseOrderItem poi = null;
        // ideally we should do this a different way - maybe move it all into the service or save this info somehow (make sure and
        // update though)
        if (getInvoice() != null) {
            PurchaseOrderDocument po = this.getInvoice().getPurchaseOrderDocument(this.getPurchaseOrderIdentifier());
            if (po != null) {
                this.setPostingYear(po.getPostingYear());
                if (po.getRecurringPaymentType() != null) {
                    this.setRecurringPaymentType(po.getRecurringPaymentType());
                    this.setRecurringPaymentTypeCode(po.getRecurringPaymentTypeCode());

                }

                if (this.getItemType().isLineItemIndicator() && this.getItemLineNumber() != null ) {
                    List<PurchaseOrderItem> items = po.getItems();
                    poi = items.get(this.getItemLineNumber().intValue() - 1);
                    // throw error if line numbers don't match
                    // MSU Contribution DTT-3014 OLEMI-8483 OLECNTRB-974
                    /*
                    * List items = po.getItems(); if (items != null) { for (Object object : items) { PurchaseOrderItem item =
                    * (PurchaseOrderItem) object; if (item != null && item.getItemLineNumber().equals(this.getItemLineNumber())) { poi
                    * = item; break; } } }
                    */
                } else {
                    poi = (PurchaseOrderItem) SpringContext.getBean(PurapService.class).getBelowTheLineByType(po, this.getItemType());
                }
            }

            if (poi != null) {
                this.setPoItem(poi);
                return poi;
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("getPurchaseOrderItem() Returning null because PurchaseOrderItem object for line number" + getItemLineNumber() + "or itemType " + getItemTypeCode() + " is null");
                }
                return null;
            }
        } else {

            LOG.error("getPurchaseOrderItem() Returning null because invoice object is null");
            throw new PurError("Payment Request Object in Purchase Order item line number " + getItemLineNumber() + "or itemType " + getItemTypeCode() + " is null");
        }
    }

    public KualiDecimal getPoOutstandingAmount() {
        PurchaseOrderItem poi = this.getPoItem()!=null ? this.getPoItem() : getPurchaseOrderItem();
        if (ObjectUtils.isNull(this.getPurchaseOrderItemUnitPrice()) || KualiDecimal.ZERO.equals(this.getPurchaseOrderItemUnitPrice())) {
            return null;
        } else {
            return this.getPoOutstandingAmount(poi);
        }
    }

    private KualiDecimal getPoOutstandingAmount(PurchaseOrderItem poi) {
        if (poi == null) {
            return KualiDecimal.ZERO;
        } else {
            return poi.getItemOutstandingEncumberedAmount();
        }
    }

    public KualiDecimal getPoOriginalAmount() {
        PurchaseOrderItem poi = this.getPoItem()!=null ? this.getPoItem() : getPurchaseOrderItem();
        if (poi == null) {
            return null;
        } else {
            return poi.getExtendedPrice();
        }
    }

    /**
     * Exists due to a setter requirement by the htmlControlAttribute
     *
     * @param amount - po outstanding amount
     * @deprecated
     */
    @Deprecated
    public void setPoOutstandingAmount(KualiDecimal amount) {
        // do nothing
    }

    private List<String> getRecurringOrderTypes(){
        List<String> continuingOrderType=new ArrayList<>();
        continuingOrderType.add(PurapConstants.ORDER_TYPE_STANDING);
        continuingOrderType.add(PurapConstants.ORDER_TYPE_SUBSCRIPTION);
        continuingOrderType.add(PurapConstants.ORDER_TYPE_MEMBERSHIP);
        continuingOrderType.add(PurapConstants.ORDER_TYPE_BLANKET);
        continuingOrderType.add(PurapConstants.ORDER_TYPE_INTEGRATING_RESOURCE);
        continuingOrderType.add(PurapConstants.ORDER_TYPE_CONTINUING);
        return continuingOrderType;
    }

    public KualiDecimal getPoOutstandingQuantity() {
        PurchaseOrderItem poi = this.getPoItem()!=null ? this.getPoItem() : getPurchaseOrderItem();
        if (this.getInvoice().getPurchaseOrderDocument() != null && this.getInvoice().getPurchaseOrderDocument().getOrderType()!=null && !getRecurringOrderTypes().contains(this.getInvoice().getPurchaseOrderDocument().getOrderType().getPurchaseOrderType())) {
            if (poi == null) {
                return KualiDecimal.ZERO;
            } else {
                if (PurapConstants.ItemTypeCodes.ITEM_TYPE_SERVICE_CODE.equals(this.getItemTypeCode())) {
                    return KualiDecimal.ZERO;
                } else {
                    return poi.getOutstandingQuantity();
                }
            }
        }else{
            return KualiDecimal.ZERO;
        }
    }

    /**
     * Exists due to a setter requirement by the htmlControlAttribute
     *
     * @param qty - po outstanding quantity
     * @deprecated
     */
    @Deprecated
    public void setPoOutstandingQuantity(KualiDecimal qty) {
        // do nothing
    }

    public BigDecimal getPurchaseOrderItemUnitPrice() {
        return purchaseOrderItemUnitPrice;
    }

    public Integer getAccountsPayablePurchasingDocumentLinkIdentifier() {
        return accountsPayablePurchasingDocumentLinkIdentifier;
    }

    public void setAccountsPayablePurchasingDocumentLinkIdentifier(Integer accountsPayablePurchasingDocumentLinkIdentifier) {
        this.accountsPayablePurchasingDocumentLinkIdentifier = accountsPayablePurchasingDocumentLinkIdentifier;
    }

    public Integer getPoLineNumber() {
        return poLineNumber;
    }

    public void setPoLineNumber(Integer poLineNumber) {
        this.poLineNumber = poLineNumber;
    }


    public Integer getPurchaseOrderIdentifier() {
        return purchaseOrderIdentifier;
    }

    public void setPurchaseOrderIdentifier(Integer purchaseOrderIdentifier) {
        this.purchaseOrderIdentifier = purchaseOrderIdentifier;
    }

    public Integer getInvoiceIdentifier() {
        return invoiceIdentifier;
    }

    public void setInvoiceIdentifier(Integer invoiceIdentifier) {
        this.invoiceIdentifier = invoiceIdentifier;
    }

    public BigDecimal getOriginalAmountfromPO() {
        return purchaseOrderItemUnitPrice;
    }

    public void setOriginalAmountfromPO(BigDecimal purchaseOrderItemUnitPrice) {
        // Do nothing
    }

    public Integer getPostingYear() {
        return postingYear;
    }

    /**
     * @see org.kuali.ole.sys.document.LedgerPostingDocument#setPostingYear(Integer)
     */
    public void setPostingYear(Integer postingYear) {
        this.postingYear = postingYear;
    }

    public void setPurchaseOrderItemUnitPrice(BigDecimal purchaseOrderItemUnitPrice) {
        this.purchaseOrderItemUnitPrice = purchaseOrderItemUnitPrice;
    }

    public KualiDecimal getItemOutstandingInvoiceAmount() {
        return itemOutstandingInvoiceAmount;
    }

    public void setItemOutstandingInvoiceAmount(KualiDecimal itemOutstandingInvoiceAmount) {
        this.itemOutstandingInvoiceAmount = itemOutstandingInvoiceAmount;
    }

    public KualiDecimal getItemOutstandingInvoiceQuantity() {
        return itemOutstandingInvoiceQuantity;
    }

    public void setItemOutstandingInvoiceQuantity(KualiDecimal itemOutstandingInvoiceQuantity) {
        this.itemOutstandingInvoiceQuantity = itemOutstandingInvoiceQuantity;
    }

    public InvoiceDocument getInvoice() {
        if (ObjectUtils.isNotNull(getPurapDocumentIdentifier())) {
            if (ObjectUtils.isNull(getPurapDocument())) {
                this.refreshReferenceObject(PurapPropertyConstants.PURAP_DOC);
            }
        }
        return super.getPurapDocument();
    }

    public void setInvoice(InvoiceDocument invoice) {
        this.setPurapDocument(invoice);
    }

    public boolean isClosePurchaseOrderIndicator() {
        return closePurchaseOrderIndicator;
    }

    public void setClosePurchaseOrderIndicator(boolean closePurchaseOrderIndicator) {
        this.closePurchaseOrderIndicator = closePurchaseOrderIndicator;
    }

    public boolean isReopenPurchaseOrderIndicator() {
        return reopenPurchaseOrderIndicator;
    }

    public void setReopenPurchaseOrderIndicator(boolean reopenPurchaseOrderIndicator) {
        this.reopenPurchaseOrderIndicator = reopenPurchaseOrderIndicator;
    }

    public String getRecurringPaymentTypeCode() {
        return recurringPaymentTypeCode;
    }

    public void setRecurringPaymentTypeCode(String recurringPaymentTypeCode) {
        this.recurringPaymentTypeCode = recurringPaymentTypeCode;
    }

    public RecurringPaymentType getRecurringPaymentType() {
        if (ObjectUtils.isNull(recurringPaymentType)) {
            refreshReferenceObject(PurapPropertyConstants.RECURRING_PAYMENT_TYPE);
        }
        return recurringPaymentType;
    }

    public void setRecurringPaymentType(RecurringPaymentType recurringPaymentType) {
        this.recurringPaymentType = recurringPaymentType;
    }

    public void generateAccountListFromPoItemAccounts(List<PurApAccountingLine> accounts) {
        for (PurApAccountingLine line : accounts) {
            PurchaseOrderAccount poa = (PurchaseOrderAccount) line;
            if (!line.isEmpty()) {
                getSourceAccountingLines().add(new InvoiceAccount(this, poa));
            }
        }
    }

    /**
     * @see org.kuali.ole.module.purap.businessobject.PurApItem#getAccountingLineClass()
     */
    @Override
    public Class getAccountingLineClass() {
        return InvoiceAccount.class;
    }

    public boolean isDisplayOnPreq() {
        PurchaseOrderItem poi = this.getPoItem()!=null ? this.getPoItem() : getPurchaseOrderItem();
        if (ObjectUtils.isNull(poi)) {
            LOG.debug("poi was null");
            return false;
        }

        // if the po item is not active... skip it
        if (!poi.isItemActiveIndicator()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("poi was not active: " + poi.toString());
            }
            return false;
        }

        ItemType poiType = poi.getItemType();

        if (poiType.isQuantityBasedGeneralLedgerIndicator()) {
            if (poi.getItemQuantity().isGreaterThan(poi.getItemInvoicedTotalQuantity())) {
                return true;
            } else {
                if (ObjectUtils.isNotNull(this.getItemQuantity()) && this.getItemQuantity().isGreaterThan(KualiDecimal.ZERO)) {
                    return true;
                }
            }

            return false;
        } else { // not quantity based
            if (poi.getItemOutstandingEncumberedAmount().isGreaterThan(KualiDecimal.ZERO)) {
                return true;
            } else {
                if (PurApItemUtils.isNonZeroExtended(this)) {
                    return true;
                }
                return false;
            }

        }
    }

    /**
     * sets account line percentage to zero.
     *
     * @see org.kuali.ole.module.purap.businessobject.PurApItem#resetAccount()
     */
    @Override
    public void resetAccount() {
        super.resetAccount();
        this.getNewSourceLine().setAmount(null);
        this.getNewSourceLine().setAccountLinePercent(null);
    }

    /**
     * Added for electronic invoice
     */
    public void addToUnitPrice(BigDecimal addThisValue) {
        if (getItemUnitPrice() == null) {
            setItemUnitPrice(BigDecimal.ZERO);
        }
        BigDecimal addedPrice = getItemUnitPrice().add(addThisValue);
        setItemUnitPrice(addedPrice);
    }

    public void addToExtendedPrice(KualiDecimal addThisValue) {
        if (getExtendedPrice() == null) {
            setExtendedPrice(KualiDecimal.ZERO);
        }
        KualiDecimal addedPrice = getExtendedPrice().add(addThisValue);
        setExtendedPrice(addedPrice);
    }

    @Override
    public Class getUseTaxClass() {
        return InvoiceItemUseTax.class;
    }

    /**
     * Gets the receivingDocumentRequiredIndicator attribute.
     *
     * @return Returns the receivingDocumentRequiredIndicator.
     */
    public boolean isReceivingDocumentRequiredIndicator() {
        return receivingDocumentRequiredIndicator;
    }

    /**
     * Sets the receivingDocumentRequiredIndicator attribute value.
     *
     * @param receivingDocumentRequiredIndicator
     *         The receivingDocumentRequiredIndicator to set.
     */
    public void setReceivingDocumentRequiredIndicator(boolean receivingDocumentRequiredIndicator) {
        // if receivingDocumentRequiredIndicator functionality is disabled, always set it to false, overriding the passed-in value
        if (!isEnableReceivingDocumentRequiredIndicator()) {
            this.receivingDocumentRequiredIndicator = false;
        } else {
            this.receivingDocumentRequiredIndicator = receivingDocumentRequiredIndicator;
        }
    }

    /**
     * Decides whether receivingDocumentRequiredIndicator functionality shall be enabled according to the controlling parameter.
     */
    public boolean isEnableReceivingDocumentRequiredIndicator() {
        return SpringContext.getBean(OleInvoiceService.class).getParameterBoolean(OLEConstants.OptionalModuleNamespaces.PURCHASING_ACCOUNTS_PAYABLE, OleParameterConstants.DOCUMENT_COMPONENT, PurapParameterConstants.RECEIVING_DOCUMENT_REQUIRED_IND);
        // return SpringContext.getBean(ParameterService.class).getParameterValueAsBoolean(OleParameterConstants.PURCHASING_DOCUMENT.class, PurapParameterConstants.RECEIVING_DOCUMENT_REQUIRED_IND);
    }

    /**
     * @see org.kuali.ole.module.purap.document.AccountsPayableDocument#getPurchaseOrderDocument()
     */
    public PurchaseOrderDocument getPurchaseOrderDocument() {
        if ((ObjectUtils.isNull(purchaseOrderDocument) || ObjectUtils.isNull(purchaseOrderDocument.getPurapDocumentIdentifier())) && (ObjectUtils.isNotNull(getPurchaseOrderIdentifier()))) {
            Map map = new HashMap();
            map.put("purapDocumentIdentifier",this.getPurchaseOrderIdentifier());
            List<OlePurchaseOrderDocument> purchaseOrderDocumentList = (List<OlePurchaseOrderDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePurchaseOrderDocument.class,map);
            if(purchaseOrderDocumentList!=null && purchaseOrderDocumentList.size()>0){
                for(OlePurchaseOrderDocument olePurchaseOrderDocument : purchaseOrderDocumentList){
                    if(olePurchaseOrderDocument.isPurchaseOrderCurrentIndicator()){
                        setPurchaseOrderDocument(olePurchaseOrderDocument);
                    }
                }

            }
        }
        return purchaseOrderDocument;
    }

    /**
     * @see org.kuali.ole.module.purap.document.AccountsPayableDocument#setPurchaseOrderDocument(org.kuali.ole.module.purap.document.PurchaseOrderDocument)
     */
    public void setPurchaseOrderDocument(PurchaseOrderDocument purchaseOrderDocument) {
        if (ObjectUtils.isNull(purchaseOrderDocument)) {
            // KUALI-PURAP 1185 PO Id not being set to null, instead throwing error on main screen that value is invalid.
            // setPurchaseOrderIdentifier(null);
            this.purchaseOrderDocument = null;
        } else {
            if (ObjectUtils.isNotNull(purchaseOrderDocument.getPurapDocumentIdentifier())) {
                setPurchaseOrderIdentifier(purchaseOrderDocument.getPurapDocumentIdentifier());
            }
            this.purchaseOrderDocument = purchaseOrderDocument;
        }
    }

    public PurchaseOrderItem getPoItem() {
        return poItem;
    }

    public void setPoItem(PurchaseOrderItem poItem) {
        this.poItem = poItem;
    }
}
