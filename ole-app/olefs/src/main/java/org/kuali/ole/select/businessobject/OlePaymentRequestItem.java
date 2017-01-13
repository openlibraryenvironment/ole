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
package org.kuali.ole.select.businessobject;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.businessobject.PaymentRequestItem;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderItem;
import org.kuali.ole.module.purap.document.PaymentRequestDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.service.PaymentRequestService;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.module.purap.util.ExpiredOrClosedAccountEntry;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.document.OlePaymentRequestDocument;
import org.kuali.ole.select.document.service.OlePaymentRequestService;
import org.kuali.ole.select.lookup.DocData;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.businessobject.UnitOfMeasure;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OlePaymentRequestItem extends PaymentRequestItem {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePaymentRequestItem.class);
    // Foreign Currency Conversion
    protected String itemCurrencyType;
    protected KualiDecimal itemForeignListPrice;
    protected KualiDecimal itemForeignDiscount;
    protected String itemForeignDiscountType;
    protected KualiDecimal itemForeignDiscountAmt;
    protected KualiDecimal itemForeignUnitCost;
    protected BigDecimal itemExchangeRate;
    protected KualiDecimal itemUnitCostUSD;
    protected KualiDecimal foreignCurrencyExtendedPrice;
    protected String localTitleId;
    protected Integer formatTypeId;
    protected KualiInteger itemNoOfParts;
    protected KualiDecimal itemListPrice;
    protected KualiDecimal itemDiscount;
    protected String itemDiscountType;
    protected BigDecimal itemSurcharge;
    protected String note;
    protected List<OlePaymentRequestNote> notes;
    protected Integer poItemIdentifier;

    private UnitOfMeasure itemUnitOfMeasure;
    private String donorCode;
    private List<OLELinkPurapDonor> oleDonors=new ArrayList<>();
    //BibInfo Details
    private BibInfoBean bibInfoBean;
    protected String itemTitleId;
    protected String bibUUID;
    private DocData docData;
    private PaymentRequestDocument paymentRequestDocument;
    private OleOrderRecord oleOrderRecord;
    protected Integer receiptStatusId;
    private OleReceiptStatus oleReceiptStatus;

    private List<OLEPaidCopy> paidCopies = new ArrayList<>();
    private String docFormat;
    public String getDocFormat() {
        return docFormat;
    }

    public void setDocFormat(String docFormat) {
        this.docFormat = docFormat;
    }
    /**
     * For Quantity As Integer
     */
    private KualiInteger oleItemQuantity;
    private KualiInteger olePoOutstandingQuantity;
    private List<SourceAccountingLine> sourceAccountingLineList = new ArrayList<SourceAccountingLine>();

    private String purchaseOrderItemID;

    /**
     * Gets the value of the receiptStatusId property
     *
     * @return receiptStatusId
     */
    public Integer getReceiptStatusId() {
        return receiptStatusId;
    }

    /**
     * Sets the receiptStatusId value
     *
     * @param receiptStatusId
     */
    public void setReceiptStatusId(Integer receiptStatusId) {
        this.receiptStatusId = receiptStatusId;
    }

    /**
     * Gets the instance of OleReceiptStatus
     *
     * @return oleReceiptStatus
     */
    public OleReceiptStatus getOleReceiptStatus() {
        return oleReceiptStatus;
    }

    /**
     * Sets the instance value for OleReceiptStatus
     *
     * @param oleReceiptStatus
     */
    public void setOleReceiptStatus(OleReceiptStatus oleReceiptStatus) {
        this.oleReceiptStatus = oleReceiptStatus;
    }

    public OleOrderRecord getOleOrderRecord() {
        return oleOrderRecord;
    }

    public void setOleOrderRecord(OleOrderRecord oleOrderRecord) {
        this.oleOrderRecord = oleOrderRecord;
    }


    public String getDonorCode() {
        return donorCode;
    }

    public void setDonorCode(String donorCode) {
        this.donorCode = donorCode;
    }

    public List<OLELinkPurapDonor> getOleDonors() {
        return oleDonors;
    }

    public void setOleDonors(List<OLELinkPurapDonor> oleDonors) {
        this.oleDonors = oleDonors;
    }

    public String getLocalTitleId() {
        return localTitleId;
    }

    public void setLocalTitleId(String localTitleId) {
        this.localTitleId = localTitleId;
    }

    // added for jira - OLE-2203
    protected boolean additionalChargeUsd;

    /**
     * Constructs a OlePaymentRequestItem.java.
     */
    public OlePaymentRequestItem() {
        this.setItemNoOfParts(new KualiInteger(1));
        this.setItemUnitOfMeasureCode(PurapConstants.PaymentRequestDocumentStrings.CUSTOMER_INVOICE_DETAIL_UOM_DEFAULT);
        this.setOleItemQuantity(new KualiInteger(1));
        if (this.getItemListPrice() == null) {
            this.setItemListPrice(new KualiDecimal(0.00));
        }
        notes = new ArrayList<OlePaymentRequestNote>();
        // added for jira - OLE-2203
        this.setItemCurrencyType(OleSelectConstant.USD);
    }

    /**
     * Constructs a OlePaymentRequestItem.java.
     *
     * @param poi
     * @param preq
     * @param expiredOrClosedAccountList
     */
    public OlePaymentRequestItem(PurchaseOrderItem poi, PaymentRequestDocument preq, HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList) {
        super(poi, preq, expiredOrClosedAccountList);
        this.setPaymentRequestDocument(preq);
        LOG.debug("Inside OlePaymentRequestItem Constructor");
        LOG.debug("Setting the Format,Discount and Price Details");
        notes = new ArrayList<OlePaymentRequestNote>();
        OlePurchaseOrderItem olePoi = (OlePurchaseOrderItem) poi;
        olePoi.getItemTypeCode();
        this.setItemForeignListPrice(olePoi.getItemForeignListPrice());
        this.setItemForeignDiscount(olePoi.getItemForeignDiscount());
        this.setItemForeignDiscountType(olePoi.getItemForeignDiscountType());
        this.setItemForeignDiscountAmt(olePoi.getItemForeignDiscountAmt());
        this.setItemForeignUnitCost(olePoi.getItemForeignUnitCost());
        this.setItemExchangeRate(olePoi.getItemExchangeRate());
        this.setItemUnitCostUSD(olePoi.getItemUnitCostUSD());
        this.setFormatTypeId(olePoi.getFormatTypeId());
        this.setItemNoOfParts(olePoi.getItemNoOfParts());
        this.setItemListPrice(olePoi.getItemListPrice());
        this.setItemDiscount(olePoi.getItemDiscount());
        this.setItemDiscountType(olePoi.getItemDiscountType());
        this.setItemCatalogNumber(olePoi.getItemCatalogNumber());
        this.setPoItemIdentifier(olePoi.getItemIdentifier());
        this.setExtendedPrice(olePoi.getExtendedPrice());
        this.setBibInfoBean(olePoi.getBibInfoBean());
        this.setItemTitleId(olePoi.getItemTitleId());
        this.setOleDonors(olePoi.getOleDonors());
        if(olePoi.getItemTitleId()!=null){
            this.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(olePoi.getItemTitleId()));
        }
        this.setItemQuantity(olePoi.getItemQuantity());
        // added for OLE-2203
        this.setItemCurrencyType(OleSelectConstant.USD);
        this.setReceiptStatusId(olePoi.getReceiptStatusId());
    }

    /**
     * Constructs a OlePaymentRequestItem.java.
     *
     * @param poi
     * @param preq
     */
    public OlePaymentRequestItem(PurchaseOrderItem poi, PaymentRequestDocument preq) {
        super(poi, preq);
        this.setPaymentRequestDocument(preq);
        LOG.debug("Inside OlePaymentRequestItem Constructor");
        LOG.debug("Setting the Format,Discount and Price Details");
        notes = new ArrayList<OlePaymentRequestNote>();
        OlePurchaseOrderItem olePoi = (OlePurchaseOrderItem) poi;
        this.setItemForeignListPrice(olePoi.getItemForeignListPrice());
        this.setItemForeignDiscount(olePoi.getItemForeignDiscount());
        this.setItemForeignDiscountType(olePoi.getItemForeignDiscountType());
        this.setItemForeignDiscountAmt(olePoi.getItemForeignDiscountAmt());
        this.setItemForeignUnitCost(olePoi.getItemForeignUnitCost());
        this.setItemExchangeRate(olePoi.getItemExchangeRate());
        this.setItemUnitCostUSD(olePoi.getItemUnitCostUSD());
        this.setFormatTypeId(olePoi.getFormatTypeId());
        this.setItemNoOfParts(olePoi.getItemNoOfParts());
        this.setItemListPrice(olePoi.getItemListPrice());
        this.setItemDiscount(olePoi.getItemDiscount());
        this.setOleDonors(olePoi.getOleDonors());
        this.setItemDiscountType(olePoi.getItemDiscountType());
        this.setItemCatalogNumber(olePoi.getItemCatalogNumber());
        this.setPoItemIdentifier(olePoi.getItemIdentifier());
        // added for OLE-2203
        this.setItemCurrencyType(OleSelectConstant.USD);
    }

    @Override
    public boolean isConsideredEnteredWithZero() {
        return isConsideredEntered(true);
    }

    @Override
    public boolean isConsideredEnteredWithoutZero() {
        return isConsideredEntered(false);
    }

    private boolean isConsideredEntered(boolean allowsZero) {
        if (getItemType().isLineItemIndicator()) {
            if ((getItemType().isQuantityBasedGeneralLedgerIndicator())) {
                if ((ObjectUtils.isNull(getItemQuantity()) || getItemQuantity().isZero()) && (ObjectUtils.isNull(getExtendedPrice()) || (allowsZero && getExtendedPrice().isZero()))) {
                    return false;
                }
            } else {
                if (ObjectUtils.isNull(getExtendedPrice()) || (allowsZero && getExtendedPrice().isZero())) {
                    return false;
                }
            }
        } else {
            if ((ObjectUtils.isNull(getItemUnitPrice()) || (allowsZero && this.getItemUnitPrice().compareTo(new BigDecimal(0)) == 0)) && (StringUtils.isBlank(getItemDescription()))) {
                return false;
            }
        }

        return true;
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

    public Integer getFormatTypeId() {
        return formatTypeId;
    }

    public void setFormatTypeId(Integer formatTypeId) {
        this.formatTypeId = formatTypeId;
    }

    public KualiInteger getItemNoOfParts() {
        if (this.itemNoOfParts != null) {
            return itemNoOfParts;
        } else {
            return itemNoOfParts;
        }
    }

    public void setItemNoOfParts(KualiInteger itemNoOfParts) {
        this.itemNoOfParts = itemNoOfParts;
    }

    public KualiDecimal getItemListPrice() {
        return itemListPrice;
    }

    public void setItemListPrice(KualiDecimal itemListPrice) {
        this.itemListPrice = itemListPrice;
    }

    public KualiDecimal getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(KualiDecimal itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public String getItemDiscountType() {
        return itemDiscountType;
    }

    public void setItemDiscountType(String itemDiscountType) {
        this.itemDiscountType = itemDiscountType;
    }

    public BigDecimal getItemSurcharge() {
        return itemSurcharge;
    }

    public void setItemSurcharge(BigDecimal itemSurcharge) {
        this.itemSurcharge = itemSurcharge;
    }

    public Integer getPoItemIdentifier() {
        return poItemIdentifier;
    }

    public void setPoItemIdentifier(Integer poItemIdentifier) {
        this.poItemIdentifier = poItemIdentifier;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<OlePaymentRequestNote> getNotes() {
        return notes;
    }

    public void setNotes(List<OlePaymentRequestNote> notes) {
        this.notes = notes;
    }

    public UnitOfMeasure getItemUnitOfMeasure() {
        if (ObjectUtils.isNull(itemUnitOfMeasure) || !StringUtils.equalsIgnoreCase(itemUnitOfMeasure.getItemUnitOfMeasureCode(), getItemUnitOfMeasureCode())) {
            refreshReferenceObject(PurapPropertyConstants.ITEM_UNIT_OF_MEASURE);
        }
        return itemUnitOfMeasure;
    }

    public void setItemUnitOfMeasure(UnitOfMeasure itemUnitOfMeasure) {
        this.itemUnitOfMeasure = itemUnitOfMeasure;
    }

    public boolean getIsUnorderedItem() {
        if (this.getItemTypeCode().equalsIgnoreCase(PurapConstants.ItemTypeCodes.ITEM_TYPE_UNORDERED_ITEM_CODE)) {
            return false;
        }
        return true;
    }

    public KualiDecimal getForeignCurrencyExtendedPrice() {
        return foreignCurrencyExtendedPrice;
    }

    public void setForeignCurrencyExtendedPrice(KualiDecimal foreignCurrencyExtendedPrice) {
        this.foreignCurrencyExtendedPrice = foreignCurrencyExtendedPrice;
    }

    public String getItemTitleId() {
        return itemTitleId;
    }

    public void setItemTitleId(String itemTitleId) {
        this.itemTitleId = itemTitleId;
    }

    public BibInfoBean getBibInfoBean() {
        return bibInfoBean;
    }

    public void setBibInfoBean(BibInfoBean bibInfoBean) {
        this.bibInfoBean = bibInfoBean;
    }

    public String getBibUUID() {
        return bibUUID;
    }

    public void setBibUUID(String bibUUID) {
        this.bibUUID = bibUUID;
    }

    public DocData getDocData() {
        return docData;
    }

    public void setDocData(DocData docData) {
        this.docData = docData;
    }

    public PaymentRequestDocument getPaymentRequestDocument() {
        return paymentRequestDocument;
    }

    public void setPaymentRequestDocument(PaymentRequestDocument paymentRequestDocument) {
        setPurapDocument(paymentRequestDocument);
        this.paymentRequestDocument = paymentRequestDocument;
    }

    public boolean isAdditionalChargeUsd() {
        return additionalChargeUsd;
    }

    public void setAdditionalChargeUsd(boolean additionalChargeUsd) {
        this.additionalChargeUsd = additionalChargeUsd;
    }

    /**
     * Gets the oleItemQuantity attribute.
     *
     * @return Returns the oleItemQuantity.
     */
    public KualiInteger getOleItemQuantity() {
        return new KualiInteger(super.getItemQuantity().intValue());
    }

    /**
     * Sets the oleItemQuantity attribute value.
     *
     * @param oleItemQuantity The oleItemQuantity to set.
     */
    public void setOleItemQuantity(KualiInteger oleItemQuantity) {
        super.setItemQuantity(new KualiDecimal(oleItemQuantity.intValue()));
    }

    /**
     * Gets the olePoOutstandingQuantity attribute.
     *
     * @return Returns the olePoOutstandingQuantity.
     */
    public KualiInteger getOlePoOutstandingQuantity() {
        return new KualiInteger(super.getPoOutstandingQuantity().intValue());
    }

    /**
     * Sets the olePoOutstandingQuantity attribute value.
     *
     * @param olePoOutstandingQuantity The olePoOutstandingQuantity to set.
     */
    public void setOlePoOutstandingQuantity(KualiInteger olePoOutstandingQuantity) {
        super.setPoOutstandingQuantity(new KualiDecimal(olePoOutstandingQuantity.intValue()));
    }

    public List<SourceAccountingLine> getSourceAccountingLineList() {
        return sourceAccountingLineList;
    }

    public void setSourceAccountingLineList(List<SourceAccountingLine> sourceAccountingLineList) {
        this.sourceAccountingLineList = sourceAccountingLineList;
    }

    /**
     * Constructs a OlePaymentRequestItem.java.
     *
     * @param olePoi
     * @param preq
     * @param expiredOrClosedAccountList
     */
    public OlePaymentRequestItem(OleInvoiceItem olePoi, OlePaymentRequestDocument preq, HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList) {
        super(olePoi, preq, expiredOrClosedAccountList);

        this.setPaymentRequestDocument(preq);
        LOG.debug("Inside OlePaymentRequestItem Constructor");
        LOG.debug("Setting the Format,Discount and Price Details");
        notes = new ArrayList<OlePaymentRequestNote>();
        // OleInvoiceItem olePoi = (OleInvoiceItem)poi;
        olePoi.getItemTypeCode();
        this.setItemForeignListPrice(olePoi.getItemForeignListPrice());
        this.setItemForeignDiscount(olePoi.getItemForeignDiscount());
        this.setItemForeignDiscountType(olePoi.getItemForeignDiscountType());
        this.setOleDonors(olePoi.getOleDonors());
        this.setItemForeignDiscountAmt(olePoi.getItemForeignDiscountAmt());
        this.setItemForeignUnitCost(olePoi.getItemForeignUnitCost());
        this.setItemExchangeRate(olePoi.getItemExchangeRate());
        this.setItemUnitCostUSD(olePoi.getItemUnitCostUSD());
        this.setFormatTypeId(olePoi.getFormatTypeId());
        this.setItemNoOfParts(olePoi.getItemNoOfParts());
        this.setItemListPrice(olePoi.getItemListPrice());
        this.setItemUnitPrice(olePoi.getItemUnitPrice());
        this.setItemDiscount(olePoi.getItemDiscount());
        if(olePoi.getNotes() != null && olePoi.getNotes().size() > 0){
            List<OlePaymentRequestNote> paymentRequestNotes = new ArrayList<>();
            for(OleInvoiceNote invoiceNote : olePoi.getNotes()){
                OlePaymentRequestNote olePaymentRequestNote = new OlePaymentRequestNote();
                olePaymentRequestNote.setNote(invoiceNote.getNote());
                paymentRequestNotes.add(olePaymentRequestNote);
            }
            this.setNotes(paymentRequestNotes);
        }
        if(olePoi.getBibUUID()!=null){
            this.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(olePoi.getBibUUID()));
        }
        this.setItemDiscountType(olePoi.getItemDiscountType());
        this.setItemCatalogNumber(olePoi.getItemCatalogNumber());
        this.setPoItemIdentifier(olePoi.getItemIdentifier());
        this.setExtendedPrice(olePoi.getExtendedPrice());
        this.setBibInfoBean(olePoi.getBibInfoBean());
        this.setItemTitleId(olePoi.getItemTitleId());
        this.setItemQuantity(olePoi.getItemQuantity());
        if (preq.getPaymentMethodId() != null) {
            OlePaymentMethod olePaymentMethod = SpringContext.getBean(BusinessObjectService.class)
                    .findBySinglePrimaryKey(OlePaymentMethod.class, preq.getPaymentMethodId());
            preq.setPaymentMethod(olePaymentMethod);
            preq.getPaymentMethod().setPaymentMethodId(olePaymentMethod.getPaymentMethodId());
            preq.setPaymentMethodId(olePaymentMethod.getPaymentMethodId());
        }
        this.setPaidCopies(olePoi.getPaidCopies());

        // added for OLE-2203
        this.setItemCurrencyType(OleSelectConstant.USD);
        this.setReceiptStatusId(olePoi.getReceiptStatusId());
    }

    /**
     * Constructs a OlePaymentRequestItem.java.
     *
     * @param olePoi
     * @param preq
     */
    public OlePaymentRequestItem(OleInvoiceItem olePoi, OlePaymentRequestDocument preq) {
        super(olePoi, preq, new HashMap<String, ExpiredOrClosedAccountEntry>());
        this.setPaymentRequestDocument(preq);
        LOG.debug("Inside OlePaymentRequestItem Constructor");
        LOG.debug("Setting the Format,Discount and Price Details");
        notes = new ArrayList<OlePaymentRequestNote>();
        // OleInvoiceItem olePoi = (OleInvoiceItem)poi;
        this.setItemForeignListPrice(olePoi.getItemForeignListPrice());
        this.setItemForeignDiscount(olePoi.getItemForeignDiscount());
        this.setItemForeignDiscountType(olePoi.getItemForeignDiscountType());
        this.setItemForeignDiscountAmt(olePoi.getItemForeignDiscountAmt());
        this.setItemForeignUnitCost(olePoi.getItemForeignUnitCost());
        this.setItemExchangeRate(olePoi.getItemExchangeRate());
        this.setItemUnitCostUSD(olePoi.getItemUnitCostUSD());
        this.setFormatTypeId(olePoi.getFormatTypeId());
        this.setItemNoOfParts(olePoi.getItemNoOfParts());
        this.setOleDonors(olePoi.getOleDonors());
        this.setItemListPrice(olePoi.getItemListPrice());
        this.setItemUnitPrice(olePoi.getItemUnitPrice());
        if(olePoi.getItemTitleId()!=null){
            this.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(olePoi.getItemTitleId()));
        }
        this.setItemDiscount(olePoi.getItemDiscount());
        this.setItemDiscountType(olePoi.getItemDiscountType());
        this.setItemCatalogNumber(olePoi.getItemCatalogNumber());
        this.setPoItemIdentifier(olePoi.getItemIdentifier());
        this.setExtendedPrice(olePoi.getExtendedPrice());
        this.setBibInfoBean(olePoi.getBibInfoBean());
        this.setItemTitleId(olePoi.getItemTitleId());
        this.setItemQuantity(olePoi.getItemQuantity());
        // added for OLE-2203
        this.setItemCurrencyType(OleSelectConstant.USD);
        this.setReceiptStatusId(olePoi.getReceiptStatusId());
        this.setPaidCopies(olePoi.getPaidCopies());
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
            if (ObjectUtils.isNull(this.getPaymentRequest())) {
                this.refreshReferenceObject(PurapPropertyConstants.PURAP_DOC);
            }
        }
        // ideally we should do this a different way - maybe move it all into the service or save this info somehow (make sure and
        // update though)
        if (getPaymentRequest() != null) {
            PurchaseOrderDocument po = getPaymentRequest().getPurchaseOrderDocument();
            PurchaseOrderItem poi = null;
            if (this.getItemType().isLineItemIndicator()) {
                List<PurchaseOrderItem> items = po.getItems();
                poi = items.get(this.getItemLineNumber().intValue() - 1);
            } else {
                poi = (PurchaseOrderItem) SpringContext.getBean(PurapService.class).getBelowTheLineByType(po, this.getItemType());
            }
            if (poi != null) {
                return poi;
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("getPurchaseOrderItem() Returning null because PurchaseOrderItem object for line number" + getItemLineNumber() + "or itemType " + getItemTypeCode() + " is null");
                }
                return null;
            }
        }
        return null;
    }

    public List<OLEPaidCopy> getPaidCopies() {
        return paidCopies;
    }

    public void setPaidCopies(List<OLEPaidCopy> paidCopies) {
        this.paidCopies = paidCopies;
    }

    public String getPurchaseOrderItemID() {
        return purchaseOrderItemID;
    }

    public void setPurchaseOrderItemID(String purchaseOrderItemID) {
        this.purchaseOrderItemID = purchaseOrderItemID;
    }
}
