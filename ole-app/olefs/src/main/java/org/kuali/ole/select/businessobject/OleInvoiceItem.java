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
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibInfoRecord;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.businessobject.InvoiceItem;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.module.purap.businessobject.PurApItemUseTax;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderItem;
import org.kuali.ole.module.purap.document.InvoiceDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.module.purap.exception.PurError;
import org.kuali.ole.module.purap.util.ExpiredOrClosedAccountEntry;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.lookup.DocData;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.businessobject.UnitOfMeasure;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.type.AbstractKualiDecimal;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.doctype.DocumentType;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OleInvoiceItem extends InvoiceItem {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleInvoiceItem.class);
    // Foreign Currency Conversion
    private BigDecimal itemExtendedPrice;
    protected String itemCurrencyType;
    protected KualiDecimal itemForeignListPrice;
    protected KualiDecimal itemForeignDiscount;
    protected String itemForeignDiscountType;
    protected KualiDecimal itemForeignDiscountAmt;
    protected KualiDecimal itemForeignUnitCost;
    protected BigDecimal itemExchangeRate;
    protected KualiDecimal itemUnitCostUSD;
    protected KualiDecimal foreignCurrencyExtendedPrice;

    protected boolean itemRouteToRequestorIndicator;
    protected boolean itemPublicViewIndicator;

    protected boolean debitItem = true;
    protected String invoiceListPrice;
    protected String invoiceForeignListPrice;
    protected String additionalUnitPrice;
    protected String localTitleId;


    protected Integer formatTypeId;
    protected KualiInteger itemNoOfParts;
    protected KualiDecimal itemListPrice;
    protected KualiDecimal itemDiscount;
    private String discountItem;
    protected String itemDiscountType;
    protected BigDecimal itemSurcharge;
    protected String note;
    protected List<OleInvoiceNote> notes;
    protected Integer poItemIdentifier;

   // private Boolean relatedViewExist;
   // private transient PurApRelatedViews relatedViews;
    private UnitOfMeasure itemUnitOfMeasure;

    private String vendorItemIdentifier;

    //BibInfo Details
    private BibInfoBean bibInfoBean;
    protected String itemTitleId;
    protected String bibUUID;
    private DocData docData;
    private InvoiceDocument invoiceDocument;
    private OleOrderRecord oleOrderRecord;
    protected Integer receiptStatusId;
    private OleReceiptStatus oleReceiptStatus;
    private boolean useTaxIndicator;
    /**
     * For Quantity As Integer
     */
    private KualiInteger oleItemQuantity;
    private String oleCopiesOrdered;
    private KualiInteger olePoOutstandingQuantity;
    private String oleOpenQuantity;
    private OleFormatType formatType = new OleFormatType();
    private String poItemLink;
    private Date purchaseOrderEndDate;
    private String docFormat;
    //Subscription Date
    private Date subscriptionFromDate;
    private Date subscriptionToDate;
    private boolean subscriptionOverlap;
    private List<SourceAccountingLine> sourceAccountingLineList = new ArrayList<SourceAccountingLine>();
    private OLEInvoiceOffsetAccountingLine oleInvoiceOffsetAccountingLine;

    private String donorCode;
    private String donorId;
    private List<OLELinkPurapDonor> oleDonors = new ArrayList<>();
    private List<OLEPaidCopy> paidCopies = new ArrayList<>();
    private Integer tempPurchaseOrderIdentifier;
    private String listPrice;
    private String foreignListPrice;
    private String foreignUnitCost;
    private String foreignDiscount;
    private String exchangeRate;
    private String invoicedCurrency;
    protected String additionalForeignUnitCost;

    private Integer reLinkPO;
    private Integer requisitionItemIdentifier;
    private Integer tempItemIdentifier;
    public String itemTitle;
    private boolean enableDetailsSection=false;
    private Integer sequenceNumber;

    private BibInfoRecord bibInfoRecord;
    private String fundCode;
    private String subscriptionPeriod;

    public BibInfoRecord getBibInfoRecord() {
        return bibInfoRecord;
    }

    public void setBibInfoRecord(BibInfoRecord bibInfoRecord) {
        this.bibInfoRecord = bibInfoRecord;
    }

    public Integer getTempItemIdentifier() {
        return tempItemIdentifier;
    }

    public void setTempItemIdentifier(Integer tempItemIdentifier) {
        this.tempItemIdentifier = tempItemIdentifier;
    }

    public String getDocFormat() {
        return docFormat;
    }

    public Integer getTempPurchaseOrderIdentifier() {
        if (poItemIdentifier != null) {
            tempPurchaseOrderIdentifier = getPurchaseOrderIdentifier();
        } else {
            tempPurchaseOrderIdentifier = null;
        }
        return tempPurchaseOrderIdentifier;
    }

    public void setTempPurchaseOrderIdentifier(Integer tempPurchaseOrderIdentifier) {
        this.tempPurchaseOrderIdentifier = tempPurchaseOrderIdentifier;
    }

    public void setDocFormat(String docFormat) {
        this.docFormat = docFormat;
    }

    public String getOleOpenQuantity() {
     /*   if (getPoOutstandingQuantity() != null) {
            return String.valueOf(super.getPoOutstandingQuantity().intValue());
        }*/
        return oleOpenQuantity;
    }

    public void setOleOpenQuantity(String oleOpenQuantity) {
        this.oleOpenQuantity = oleOpenQuantity;
    }

    public String getVendorItemIdentifier() {
        return vendorItemIdentifier;
    }

    public void setVendorItemIdentifier(String vendorItemIdentifier) {
        this.vendorItemIdentifier = vendorItemIdentifier;
    }

    public String getLocalTitleId() {
        return localTitleId;
    }

    public String getDonorCode() {
        return donorCode;
    }

    public void setDonorCode(String donorCode) {
        this.donorCode = donorCode;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public List<OLELinkPurapDonor> getOleDonors() {
        return oleDonors;
    }

    public void setOleDonors(List<OLELinkPurapDonor> oleDonors) {
        this.oleDonors = oleDonors;
    }

    public void setLocalTitleId(String localTitleId) {
        this.localTitleId = localTitleId;
    }

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

    // added for jira - OLE-2203
    protected boolean additionalChargeUsd;

    /**
     * Constructs a OleInvoiceItem.java.
     */
    public OleInvoiceItem() {
        this.setItemNoOfParts(new KualiInteger(1));
        this.setItemUnitOfMeasureCode(PurapConstants.InvoiceDocumentStrings.CUSTOMER_INVOICE_DETAIL_UOM_DEFAULT);
        this.setOleItemQuantity(new KualiInteger(1));
        this.setItemListPrice(new KualiDecimal(0.00));
        notes = new ArrayList<OleInvoiceNote>();
    }

    /**
     * Constructs a OleInvoiceItem.java.
     *
     * @param poi
     * @param prqs
     * @param expiredOrClosedAccountList
     */
    public OleInvoiceItem(PurchaseOrderItem poi, InvoiceDocument prqs, HashMap<String, ExpiredOrClosedAccountEntry> expiredOrClosedAccountList) {
        super(poi, prqs, expiredOrClosedAccountList);
        this.setInvoiceDocument(prqs);
        LOG.debug("Inside OleInvoiceItem Constructor");
        LOG.debug("Setting the Format,Discount and Price Details");
        notes = new ArrayList<OleInvoiceNote>();

        OlePurchaseOrderItem olePoi = (OlePurchaseOrderItem) poi;
        olePoi.getItemTypeCode();
        notes.addAll(olePoi.getInvoiceNotes());
        this.setItemForeignDiscount(olePoi.getInvoiceForeignDiscount() != null ? new KualiDecimal(olePoi.getInvoiceForeignDiscount()) : new KualiDecimal(0.00));
        this.setForeignDiscount(olePoi.getInvoiceForeignDiscount());
        this.setItemForeignDiscountType(olePoi.getInvoiceForeignDiscountType());
        this.setItemForeignDiscountAmt(olePoi.getItemForeignDiscountAmt());
        this.setItemCurrencyType(olePoi.getInvoiceForeignCurrency() != null ? olePoi.getInvoiceForeignCurrency() : olePoi.getItemCurrencyType());
        this.setInvoicedCurrency(olePoi.getInvoiceForeignCurrency() != null ? olePoi.getInvoiceForeignCurrency() : olePoi.getItemCurrencyType());
        this.setItemExchangeRate(olePoi.getInvoiceExchangeRate() != null ? new BigDecimal(olePoi.getInvoiceExchangeRate()) : new BigDecimal(0.00));
        this.setExchangeRate(olePoi.getInvoiceExchangeRate() != null ? olePoi.getInvoiceExchangeRate() : null);
        this.setItemUnitCostUSD(olePoi.getItemUnitCostUSD());
        this.setFormatTypeId(olePoi.getFormatTypeId());
        this.setItemNoOfParts(olePoi.getNoOfPartsInvoiced());
        this.setOleDonors(olePoi.getOleDonors());
        this.setItemListPrice(olePoi.getItemListPrice());
        this.setItemDiscount(olePoi.getItemDiscount() != null ? olePoi.getItemDiscount() : new KualiDecimal(0.00));
        this.setItemDiscountType(olePoi.getItemDiscountType());
        this.setItemCatalogNumber(olePoi.getItemCatalogNumber());
        this.setPoItemIdentifier(olePoi.getItemIdentifier());
        this.setExtendedPrice(olePoi.getExtendedPrice());
        this.setItemQuantity(olePoi.getItemQuantity());
        this.setBibInfoBean(olePoi.getBibInfoBean());
        if (olePoi.getItemTitleId() != null) {
            this.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(olePoi.getItemTitleId()));
        }
        this.setItemTitleId(olePoi.getItemTitleId());
        if (olePoi.getNoOfCopiesInvoiced() != null) {
            this.setItemQuantity(olePoi.getNoOfCopiesInvoiced().kualiDecimalValue());
        }
        if (new KualiDecimal(olePoi.getInvoiceItemListPrice()).isLessThan(AbstractKualiDecimal.ZERO)) {
            this.setInvoiceListPrice(olePoi.getInvoiceItemListPrice());
            this.setListPrice(olePoi.getInvoiceItemListPrice());

        } else {
            this.setItemListPrice(new KualiDecimal(olePoi.getInvoiceItemListPrice()));
        }
        if (new KualiDecimal(olePoi.getInvoiceForeignItemListPrice()).isLessThan(AbstractKualiDecimal.ZERO)) {
            this.setInvoiceForeignListPrice(olePoi.getInvoiceForeignItemListPrice());
            this.setForeignListPrice(olePoi.getInvoiceForeignItemListPrice());
        }   else {
            this.setItemForeignListPrice(new KualiDecimal(olePoi.getInvoiceForeignItemListPrice()));
        }
        //this.setInvoiceListPrice(olePoi.getInvoiceItemListPrice());
        this.setItemUnitPrice(SpringContext.getBean(OlePurapService.class).calculateDiscount(this).setScale(4, BigDecimal.ROUND_HALF_UP));
        this.setItemForeignUnitCost(SpringContext.getBean(OlePurapService.class).calculateForeignCurrency(this).itemForeignUnitCost);
        this.setForeignUnitCost(this.getItemForeignUnitCost().toString());
        this.setExtendedPrice(this.calculateExtendedPrice());
        this.setReceiptStatusId(olePoi.getReceiptStatusId());
        this.setItemSurcharge(olePoi.getItemSurcharge());
        this.setPurapDocumentIdentifier(prqs.getPurapDocumentIdentifier());

        this.setSubscriptionOverlap(olePoi.isSubscriptionOverlap());
        this.setSubscriptionFromDate(olePoi.getSubscriptionFromDate());
        this.setSubscriptionToDate(olePoi.getSubscriptionToDate());
        this.setItemRouteToRequestorIndicator(olePoi.isItemRouteToRequestorIndicator());
        this.setItemPublicViewIndicator(olePoi.isItemPublicViewIndicator());
    }

    @Override
    public KualiDecimal calculateExtendedPrice() {
        KualiDecimal extendedPrice = super.calculateExtendedPrice();
        KualiDecimal taxAmount = getItemTaxAmount();
        if(taxAmount!=null && taxAmount.equals(KualiDecimal.ZERO) && getItemSalesTaxAmount()!=null && this.getItemTypeCode().equals("ITEM")){
            taxAmount = getItemSalesTaxAmount();
            extendedPrice = extendedPrice.add(taxAmount);
        }
        return extendedPrice;
    }

    /**
     * Constructs a OleInvoiceItem.java.
     *
     * @param poi
     * @param prqs
     */
    public OleInvoiceItem(PurchaseOrderItem poi, InvoiceDocument prqs) {
        super(poi, prqs);
        this.setInvoiceDocument(prqs);
        LOG.debug("Inside OleInvoiceItem Constructor");
        LOG.debug("Setting the Format,Discount and Price Details");
        notes = new ArrayList<OleInvoiceNote>();
        OlePurchaseOrderItem olePoi = (OlePurchaseOrderItem) poi;
        notes.addAll(olePoi.getInvoiceNotes());
        this.setItemForeignDiscount(olePoi.getInvoiceForeignDiscount() != null? new KualiDecimal(olePoi.getInvoiceForeignDiscount()) : new KualiDecimal(0.00));
        this.setForeignDiscount(olePoi.getInvoiceForeignDiscount());
        this.setItemForeignDiscountType(olePoi.getItemForeignDiscountType());
        this.setItemForeignDiscountAmt(olePoi.getItemForeignDiscountAmt());
        this.setItemForeignUnitCost(olePoi.getItemForeignUnitCost());
        this.setItemTitleId(olePoi.getItemTitleId());
        this.setItemCurrencyType(olePoi.getInvoiceForeignCurrency()!=null ? olePoi.getInvoiceForeignCurrency() : olePoi.getItemCurrencyType());
        this.setInvoicedCurrency(olePoi.getInvoiceForeignCurrency()!=null? olePoi.getInvoiceForeignCurrency() : olePoi.getItemCurrencyType());
        this.setItemExchangeRate(olePoi.getInvoiceExchangeRate()!= null ? new BigDecimal(olePoi.getInvoiceExchangeRate()) : new BigDecimal(0.00));
        this.setExchangeRate(olePoi.getInvoiceExchangeRate()!= null? olePoi.getInvoiceExchangeRate() : null);
        this.setItemUnitCostUSD(olePoi.getItemUnitCostUSD());
        this.setOleDonors(olePoi.getOleDonors());
        this.setFormatTypeId(olePoi.getFormatTypeId());
        this.setItemNoOfParts(olePoi.getItemNoOfParts());
        this.setItemListPrice(olePoi.getItemListPrice());
        this.setItemDiscount(olePoi.getItemDiscount() != null ? olePoi.getItemDiscount() : new KualiDecimal(0.00));
        this.setItemDiscountType(olePoi.getItemDiscountType());
        this.setItemCatalogNumber(olePoi.getItemCatalogNumber());
        if (olePoi.getItemTitleId() != null) {
            this.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(olePoi.getItemTitleId()));
        }
        this.setPoItemIdentifier(olePoi.getItemIdentifier());
        if (olePoi.getNoOfCopiesInvoiced() != null) {
            this.setItemQuantity(olePoi.getNoOfCopiesInvoiced().kualiDecimalValue());
        }
        if (new KualiDecimal(olePoi.getInvoiceItemListPrice()).isLessThan(AbstractKualiDecimal.ZERO)) {
            this.setInvoiceListPrice(olePoi.getInvoiceItemListPrice());
            this.setListPrice(olePoi.getInvoiceItemListPrice());

        } else {
            this.setItemListPrice(new KualiDecimal(olePoi.getInvoiceItemListPrice()));
        }
        if (new KualiDecimal(olePoi.getInvoiceForeignItemListPrice()).isLessThan(AbstractKualiDecimal.ZERO)) {
            this.setInvoiceForeignListPrice(olePoi.getInvoiceForeignItemListPrice());
            this.setForeignListPrice(olePoi.getInvoiceForeignItemListPrice());
        }   else {
            this.setItemForeignListPrice(new KualiDecimal(olePoi.getInvoiceForeignItemListPrice()));
        }
        //this.setInvoiceListPrice(olePoi.getInvoiceItemListPrice());
        this.setItemUnitPrice(SpringContext.getBean(OlePurapService.class).calculateDiscount(this).setScale(4, BigDecimal.ROUND_HALF_UP));
        this.setItemForeignUnitCost(SpringContext.getBean(OlePurapService.class).calculateForeignCurrency(this).itemForeignUnitCost);
        this.setForeignUnitCost(this.getItemForeignUnitCost().toString());
        this.setExtendedPrice(this.calculateExtendedPrice());
        this.setPurapDocumentIdentifier(prqs.getPurapDocumentIdentifier());
        this.setItemRouteToRequestorIndicator(olePoi.isItemRouteToRequestorIndicator());
        this.setItemPublicViewIndicator(olePoi.isItemPublicViewIndicator());
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

    public String getInvoicedCurrency() {
        return invoicedCurrency;
    }

    public void setInvoicedCurrency(String invoicedCurrency) {
        this.invoicedCurrency = invoicedCurrency;
    }

    public String getExchangeRate() {
        if (exchangeRate != null && !exchangeRate.isEmpty()) {
            return exchangeRate;
        } else {
            return itemExchangeRate!= null? itemExchangeRate.toString() :null;
        }
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getForeignDiscount() {
        if (foreignDiscount != null && !foreignDiscount.isEmpty()) {
            return foreignDiscount;
        } else {
            return itemForeignDiscount!= null? itemForeignDiscount.toString() :null;
        }
    }

    public void setForeignDiscount(String foreignDiscount) {
        this.foreignDiscount = foreignDiscount;
    }

    public String getForeignUnitCost() {
        if (foreignUnitCost != null && !foreignUnitCost.isEmpty()) {
            return foreignUnitCost;
        } else {
            if (!debitItem) {
                return itemForeignUnitCost != null ?  "(" + itemForeignUnitCost.toString().replace("-", "") + ")" : null;
            } else {
                return itemForeignUnitCost.toString();
            }
        }
    }

    public void setForeignUnitCost(String foreignUnitCost) {
        this.foreignUnitCost = foreignUnitCost;
        if (foreignUnitCost != null) {
            if (foreignUnitCost.contains("(") || foreignUnitCost.contains(")")) {
                foreignUnitCost = foreignUnitCost.replace("(", "");
                foreignUnitCost = foreignUnitCost.replace(")", "");
                foreignUnitCost = "-" + foreignUnitCost;
                this.foreignUnitCost = foreignUnitCost;
            }
            if (new KualiDecimal(foreignUnitCost).isLessThan(KualiDecimal.ZERO)) {
                foreignUnitCost = foreignUnitCost.replace("-", "");
                foreignUnitCost = "(" + foreignUnitCost + ")";
                this.foreignUnitCost = foreignUnitCost;
            } else {
                this.foreignUnitCost = foreignUnitCost;
            }
        }

    }

    public String getAdditionalForeignUnitCost() {
        if (!isDebitItem()) {
            additionalForeignUnitCost =  this.getItemForeignUnitCost() != null ? this.getItemForeignUnitCost().negated().toString() : null;
        } else{
            additionalForeignUnitCost =  this.getItemForeignUnitCost() != null ? this.getItemForeignUnitCost().toString() : null;
        }
        return this.additionalForeignUnitCost;
    }

    public void setAdditionalForeignUnitCost(String additionalForeignUnitCost) {
        if (additionalForeignUnitCost != null && !additionalForeignUnitCost.isEmpty() &&
                (new KualiDecimal(additionalForeignUnitCost)).isLessThan(AbstractKualiDecimal.ZERO)) {
            //  this.setDebitItem(false);
            this.setItemForeignUnitCost(new KualiDecimal(additionalForeignUnitCost).abs());
            this.additionalForeignUnitCost = additionalForeignUnitCost;
        } else if (additionalForeignUnitCost != null && !additionalForeignUnitCost.isEmpty() &&
                (new KualiDecimal(additionalForeignUnitCost)).isGreaterEqual(AbstractKualiDecimal.ZERO)) {
            //  this.setDebitItem(true);
            this.setItemForeignUnitCost((new KualiDecimal(additionalForeignUnitCost)));
            this.additionalForeignUnitCost = additionalForeignUnitCost;
        }
        else {
            this.setItemForeignUnitCost(null);
            this.additionalForeignUnitCost = additionalForeignUnitCost;
        }
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

    public String getDiscountItem() {
        return String.valueOf(itemDiscount) != null ? String.valueOf(itemDiscount) : "0";
    }

    public void setDiscountItem(String discountItem) {
        this.itemDiscount = new KualiDecimal(discountItem);
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

    public List<OleInvoiceNote> getNotes() {
        return notes;
    }

    public void setNotes(List<OleInvoiceNote> notes) {
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

    public InvoiceDocument getInvoiceDocument() {
        return invoiceDocument;
    }

    public void setInvoiceDocument(InvoiceDocument invoiceDocument) {
        setPurapDocument(invoiceDocument);
        this.invoiceDocument = invoiceDocument;
    }

    public boolean isAdditionalChargeUsd() {
        return additionalChargeUsd;
    }

    public void setAdditionalChargeUsd(boolean additionalChargeUsd) {
        this.additionalChargeUsd = additionalChargeUsd;
    }

    public String getOleCopiesOrdered() {
        return String.valueOf(super.getItemQuantity().intValue());
    }

    public void setOleCopiesOrdered(String oleCopiesOrdered) {
        super.setItemQuantity(new KualiDecimal(oleCopiesOrdered));
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
        if (getPoOutstandingQuantity() != null) {
            return new KualiInteger(super.getPoOutstandingQuantity().intValue());
        }
        return new KualiInteger(0);
    }

    /**
     * Sets the olePoOutstandingQuantity attribute value.
     *
     * @param olePoOutstandingQuantity The olePoOutstandingQuantity to set.
     */
    public void setOlePoOutstandingQuantity(KualiInteger olePoOutstandingQuantity) {
        if (olePoOutstandingQuantity != null) {
            super.setPoOutstandingQuantity(new KualiDecimal(olePoOutstandingQuantity.intValue()));
        }
    }

    public boolean isUseTaxIndicator() {
        return useTaxIndicator;
    }

    public void setUseTaxIndicator(boolean useTaxIndicator) {
        this.useTaxIndicator = useTaxIndicator;
    }

    @Override
    public KualiDecimal getItemTaxAmount() {
        KualiDecimal taxAmount = KualiDecimal.ZERO;

        /*if (ObjectUtils.isNull(purapDocument)) {
            this.refreshReferenceObject("purapDocument");
        }*/

        if (this.isUseTaxIndicator() == false) {
            taxAmount = this.getItemSalesTaxAmount();
        } else {
            // sum use tax item tax amounts
            for (PurApItemUseTax useTaxItem : this.getUseTaxItems()) {
                taxAmount = taxAmount.add(useTaxItem.getTaxAmount());
            }
        }

        return taxAmount;
    }

    public OleFormatType getFormatType() {
        return formatType;
    }

    public void setFormatType(OleFormatType formatType) {
        this.formatType = formatType;
    }

   /* public PurApRelatedViews getRelatedViews() {
        if (relatedViews == null) {
            relatedViews = new PurApRelatedViews(
                    this.getInvoiceDocument() != null ? this.getInvoiceDocument().getDocumentNumber()
                            : null,
                    this.getAccountsPayablePurchasingDocumentLinkIdentifier() != null ?
                            this.getAccountsPayablePurchasingDocumentLinkIdentifier() : null);
        }
        return relatedViews;
    }*/

   /* public void setRelatedViews(PurApRelatedViews relatedViews) {
        this.relatedViews = relatedViews;
    }*/

   /* public Boolean getRelatedViewExist() {
        if (this.relatedViews != null) {
            this.relatedViewExist = Boolean.TRUE;
        } else {
            this.relatedViewExist = Boolean.FALSE;
        }
        return relatedViewExist;
    }*/

    /*public void setRelatedViewExist(Boolean relatedViewExist) {
        this.relatedViewExist = relatedViewExist;
    }*/

    public boolean isDebitItem() {
        return debitItem;
    }

    public void setDebitItem(boolean debitItem) {
        this.debitItem = debitItem;
    }

    public String getInvoiceListPrice() {
        if (!isDebitItem()) {
            return this.getItemListPrice() != null ? this.getItemListPrice().negated().toString() : null;
        }
        return itemListPrice.toString();
    }

    public void setInvoiceListPrice(String invoiceListPrice) {
        if (invoiceListPrice != null && !invoiceListPrice.isEmpty() &&
                (new KualiDecimal(invoiceListPrice)).isLessThan(AbstractKualiDecimal.ZERO)) {
            this.setDebitItem(false);
        } else {
            this.setDebitItem(true);
        }
        this.setItemListPrice((new KualiDecimal(invoiceListPrice)).abs());
        this.invoiceListPrice = invoiceListPrice;
    }

    public String getInvoiceForeignListPrice() {
        if (!isDebitItem()) {
            return this.getItemForeignListPrice() != null ? this.getItemForeignListPrice().negated().toString() : null;
        }
        return invoiceForeignListPrice;
    }

    public void setInvoiceForeignListPrice(String invoiceForeignListPrice) {
        if (invoiceForeignListPrice != null && !invoiceForeignListPrice.isEmpty() &&
                (new KualiDecimal(invoiceForeignListPrice)).isLessThan(AbstractKualiDecimal.ZERO)) {
            this.setDebitItem(false);
        } else {
            this.setDebitItem(true);
        }
        this.setItemForeignListPrice((new KualiDecimal(invoiceForeignListPrice)).abs());
        this.invoiceForeignListPrice = invoiceForeignListPrice;
    }

    public String getAdditionalUnitPrice() {
        if (!isDebitItem()) {
            additionalUnitPrice =  this.getItemUnitPrice() != null ? this.getItemUnitPrice().negate().toString() : null;
        } else{
            additionalUnitPrice =  this.getItemUnitPrice() != null ? this.getItemUnitPrice().toString() : null;
        }
        return this.additionalUnitPrice;
    }

    public String getListPrice() {
        if (listPrice != null && !listPrice.isEmpty()) {
            return listPrice;
        } else if (invoiceListPrice != null && new KualiDecimal(invoiceListPrice).isLessThan(KualiDecimal.ZERO)) {
            return "(" + invoiceListPrice.replace("-", "") + ")";
        } else {
            if (!debitItem) {
                return "(" + itemListPrice.toString().replace("-", "") + ")";
            } else {
                return itemListPrice.toString();
            }
        }
    }

    public void setListPrice(String listPrice) {
        this.listPrice = listPrice;
        if (listPrice.contains("(") || listPrice.contains(")")) {
            listPrice = listPrice.replace("(", "");
            listPrice = listPrice.replace(")", "");
            listPrice = "-" + listPrice;
            this.listPrice = listPrice;
        }
        if (new KualiDecimal(listPrice).isLessThan(KualiDecimal.ZERO)) {
            this.setInvoiceListPrice(listPrice);
            listPrice = listPrice.replace("-", "");
            listPrice = "(" + listPrice + ")";
            this.listPrice = listPrice;
        } else {
            this.setInvoiceListPrice(listPrice);
            this.listPrice = listPrice;
        }

    }

    public String getForeignListPrice() {
        if (foreignListPrice != null && !foreignListPrice.isEmpty()) {
            return foreignListPrice;
        } else if (invoiceForeignListPrice != null && new KualiDecimal(invoiceForeignListPrice).isLessThan(KualiDecimal.ZERO)) {
            return "(" + invoiceForeignListPrice.replace("-", "") + ")";
        } else {
            if (!debitItem) {
                return "(" + itemForeignListPrice.toString().replace("-", "") + ")";
            } else {
                return itemForeignListPrice.toString();
            }
        }
    }

    public void setForeignListPrice(String foreignListPrice) {
        this.foreignListPrice = foreignListPrice;
        if(foreignListPrice != null) {
            if (foreignListPrice.contains("(") || foreignListPrice.contains(")")) {
                foreignListPrice = foreignListPrice.replace("(", "");
                foreignListPrice = foreignListPrice.replace(")", "");
                foreignListPrice = "-" + foreignListPrice;
                this.foreignListPrice = foreignListPrice;
            }
            if (new KualiDecimal(foreignListPrice).isLessThan(KualiDecimal.ZERO)) {
                this.setInvoiceForeignListPrice(foreignListPrice);
                foreignListPrice = foreignListPrice.replace("-", "");
                foreignListPrice = "(" + foreignListPrice + ")";
                this.foreignListPrice = foreignListPrice;
            } else {
                this.setInvoiceForeignListPrice(foreignListPrice);
                this.foreignListPrice = foreignListPrice;
            }
        }
    }

    public void setAdditionalUnitPrice(String additionalUnitPrice) {
        if (additionalUnitPrice != null && !additionalUnitPrice.isEmpty() &&
                (new KualiDecimal(additionalUnitPrice)).isLessThan(AbstractKualiDecimal.ZERO)) {
            this.setDebitItem(false);
            this.setItemUnitPrice((new BigDecimal(additionalUnitPrice)).abs());
            this.additionalUnitPrice = additionalUnitPrice;
        } else if (additionalUnitPrice != null && !additionalUnitPrice.isEmpty() &&
                (new KualiDecimal(additionalUnitPrice)).isGreaterEqual(AbstractKualiDecimal.ZERO)) {
            this.setDebitItem(true);
            this.setItemUnitPrice((new BigDecimal(additionalUnitPrice)));
            this.additionalUnitPrice = additionalUnitPrice;
        }
        else {
            this.setItemUnitPrice(null);
            this.additionalUnitPrice = additionalUnitPrice;
        }

    }

    public String getPoItemLink() {
        if (poItemIdentifier != null) {
            String documentTypeName = PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_DOCUMENT;
            DocumentType docType = KewApiServiceLocator.getDocumentTypeService().getDocumentTypeByName(documentTypeName);
            String docHandlerUrl = docType.getResolvedDocumentHandlerUrl();
            int endSubString = docHandlerUrl.lastIndexOf("/");
            String serverName = docHandlerUrl.substring(0, endSubString);
            String handler = docHandlerUrl.substring(endSubString + 1, docHandlerUrl.lastIndexOf("?"));
            poItemLink = serverName + "/" + KRADConstants.PORTAL_ACTION + "?channelTitle=" + docType.getName() + "&channelUrl=" +
                    handler + "?" + KRADConstants.DISPATCH_REQUEST_PARAMETER + "=" + KRADConstants.DOC_HANDLER_METHOD + "&" +
                    KRADConstants.PARAMETER_DOC_ID + "=" + this.getPurchaseOrderDocument().getDocumentNumber() + "&" + KRADConstants.PARAMETER_COMMAND + "=" +
                    KewApiConstants.DOCSEARCH_COMMAND;

        } else {
            poItemLink = "";
        }
        return poItemLink;
    }

    public void setPoItemLink(String poItemLink) {
        this.poItemLink = poItemLink;
    }

    public Date getSubscriptionFromDate() {
        return subscriptionFromDate;
    }

    public void setSubscriptionFromDate(Date subscriptionFromDate) {
        this.subscriptionFromDate = subscriptionFromDate;
    }

    public Date getSubscriptionToDate() {
        return subscriptionToDate;
    }

    public void setSubscriptionToDate(Date subscriptionToDate) {
        this.subscriptionToDate = subscriptionToDate;
    }

    public boolean isSubscriptionOverlap() {
        return subscriptionOverlap;
    }

    public void setSubscriptionOverlap(boolean subscriptionOverlap) {
        this.subscriptionOverlap = subscriptionOverlap;
    }

    public Date getPurchaseOrderEndDate() {
        return purchaseOrderEndDate;
    }

    public void setPurchaseOrderEndDate(Date purchaseOrderEndDate) {
        this.purchaseOrderEndDate = purchaseOrderEndDate;
    }


    public void setPaidCopies(List<OLEPaidCopy> paidCopies) {
        this.paidCopies = paidCopies;
    }

    public List<OLEPaidCopy> getPaidCopies() {
        return paidCopies;
    }

    public void setOleInvoiceOffsetAccountingLine(OLEInvoiceOffsetAccountingLine oleInvoiceOffsetAccountingLine) {
        this.oleInvoiceOffsetAccountingLine = oleInvoiceOffsetAccountingLine;
    }

    public List<SourceAccountingLine> getSourceAccountingLineList() {
        return sourceAccountingLineList;
    }

    public void setSourceAccountingLineList(List<SourceAccountingLine> sourceAccountingLineList) {
        this.sourceAccountingLineList = sourceAccountingLineList;
    }

    public OLEInvoiceOffsetAccountingLine getOleInvoiceOffsetAccountingLine() {
        return oleInvoiceOffsetAccountingLine;
    }

    public Integer getReLinkPO() {
        return reLinkPO;
    }

    public void setReLinkPO(Integer reLinkPO) {
        this.reLinkPO = reLinkPO;
    }

    @Override
    public PurchaseOrderItem getPurchaseOrderItem() {
        InvoiceDocument invDoc = getInvoice();
       /* if (ObjectUtils.isNotNull(this.getPurapDocumentIdentifier())) {
            if (ObjectUtils.isNull(this.getInvoice())) {
                this.refreshReferenceObject(PurapPropertyConstants.PURAP_DOC);
            }
        }*/
        PurchaseOrderItem poi = null;
        // ideally we should do this a different way - maybe move it all into the service or save this info somehow (make sure and
        // update though)
        if (invDoc!= null) {
            PurchaseOrderDocument po = invDoc.getPurchaseOrderDocument(this.getPurchaseOrderIdentifier());
            if (po != null) {
                this.setPostingYear(po.getPostingYear());
                if (po.getRecurringPaymentType() != null) {
                    this.setRecurringPaymentType(po.getRecurringPaymentType());
                    this.setRecurringPaymentTypeCode(po.getRecurringPaymentTypeCode());

                }

                if (this.getItemType().isLineItemIndicator() && this.getItemLineNumber() != null && this.getItemLineNumber()< 0) {
                    List<PurchaseOrderItem> items = po.getItems();
                    poi = items.get(this.getItemLineNumber().intValue() - 1);
                    // throw error if line numbers don't match
                    // MSU Contribution DTT-3014 OLEMI-8483 OLECNTRB-974
                    /*
                    * List items = po.getItems(); if (items != null) { for (Object object : items) { PurchaseOrderItem item =
                    * (PurchaseOrderItem) object; if (item != null && item.getItemLineNumber().equals(this.getItemLineNumber())) { poi
                    * = item; break; } } }
                    */
                } else if (this.getItemType().isLineItemIndicator()) {
                    List<PurchaseOrderItem> items = po.getItems();
                    for (int count = 0; count < items.size(); count++) {
                        if (this.getPoItemIdentifier() != null) {
                            if (this.getPoItemIdentifier().compareTo((items.get(count)).getItemIdentifier()) == 0) {
                                this.setItemLineNumber(items.get(count).getItemLineNumber());
                                poi = items.get(this.getItemLineNumber().intValue() - 1);
                                break;
                            }
                        }
                        //items.get(count).getItemLineNumber();
                    }
                } else {
                    poi = (PurchaseOrderItem) SpringContext.getBean(PurapService.class).getBelowTheLineByType(po, this.getItemType());
                    if (poi != null) {
                        this.setItemLineNumber(poi.getItemLineNumber());
                    }
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

    public Integer getRequisitionItemIdentifier() {
        return requisitionItemIdentifier;
    }

    public void setRequisitionItemIdentifier(Integer requisitionItemIdentifier) {
        this.requisitionItemIdentifier = requisitionItemIdentifier;
    }

    //Added for Jira OLE-5370
    public String getItemTitle() {
        itemTitle = getItemDescription();
        if(itemTitle.contains(OLEConstants.ANGLE_BRACKET_LESS_THAN)){
            itemTitle = itemTitle.replace(OLEConstants.ANGLE_BRACKET_LESS_THAN,"&#60;");
        }
        if(itemTitle.contains(OLEConstants.ANGLE_BRACKET_GREATER_THAN)){
            itemTitle = itemTitle.replace(OLEConstants.ANGLE_BRACKET_GREATER_THAN,"&#62;");
        }
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public boolean isItemPublicViewIndicator() {
        return itemPublicViewIndicator;
    }

    public void setItemPublicViewIndicator(boolean itemPublicViewIndicator) {
        this.itemPublicViewIndicator = itemPublicViewIndicator;
    }

    public boolean isItemRouteToRequestorIndicator() {
        return itemRouteToRequestorIndicator;
    }

    public void setItemRouteToRequestorIndicator(boolean itemRouteToRequestorIndicator) {
        this.itemRouteToRequestorIndicator = itemRouteToRequestorIndicator;
    }

    public boolean isEnableDetailsSection() {
        return enableDetailsSection;
    }

    public void setEnableDetailsSection(boolean enableDetailsSection) {
        this.enableDetailsSection = enableDetailsSection;
    }

    public BigDecimal getItemExtendedPrice() {
        return calculateItemExtendedPrice();
    }


    public BigDecimal calculateItemExtendedPrice() {
        BigDecimal extendedPrice = BigDecimal.ZERO;
        BigDecimal itemUnitPrice = getItemUnitPrice();
        if (ObjectUtils.isNotNull(itemUnitPrice)) {
            if (this.getItemType().isAmountBasedGeneralLedgerIndicator()) {
                // SERVICE ITEM: return unit price as extended price
                extendedPrice = itemUnitPrice;
            } else if (ObjectUtils.isNotNull(this.getItemQuantity())) {
                BigDecimal calcExtendedPrice = itemUnitPrice.multiply(this.getItemQuantity().bigDecimalValue());
                // ITEM TYPE (qty driven): return (unitPrice x qty)
                extendedPrice = calcExtendedPrice.setScale(4, KualiDecimal.ROUND_BEHAVIOR);
            }
        }
        return extendedPrice;
    }

    @Override
    public KualiDecimal getTotalAmount() {
        KualiDecimal totalAmount = getExtendedPrice();
        if (ObjectUtils.isNull(totalAmount)) {
            totalAmount = KualiDecimal.ZERO;
        }

        KualiDecimal taxAmount = getItemTaxAmount();
        if (ObjectUtils.isNull(taxAmount)) {
            taxAmount = KualiDecimal.ZERO;
        }

        totalAmount = totalAmount.add(taxAmount);

        return totalAmount;
    }

    public BigDecimal getInvoiceTotalAmount() {
        BigDecimal totalAmount = getItemExtendedPrice();
        if (ObjectUtils.isNull(totalAmount)) {
            totalAmount = BigDecimal.ZERO;
        }

        KualiDecimal taxAmount = getItemTaxAmount();
        if (ObjectUtils.isNull(taxAmount)) {
            taxAmount = KualiDecimal.ZERO;
        }

        totalAmount = totalAmount.add(taxAmount.bigDecimalValue());

        return totalAmount;
    }


    public KualiDecimal getForeignTotalAmount() {
        KualiDecimal totalAmount = calculateForeignExtendedPrice();
        if (ObjectUtils.isNull(totalAmount)) {
            totalAmount = KualiDecimal.ZERO;
        }

        KualiDecimal taxAmount = getItemTaxAmount();
        if (ObjectUtils.isNull(taxAmount)) {
            taxAmount = KualiDecimal.ZERO;
        }

        totalAmount = totalAmount.add(taxAmount);

        return totalAmount;
    }

    public KualiDecimal calculateForeignExtendedPrice() {
        KualiDecimal extendedPrice = KualiDecimal.ZERO;
        if (this.getItemTypeCode().equalsIgnoreCase(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE)) {
            if (ObjectUtils.isNotNull(itemForeignUnitCost)) {
                if (this.getItemType().isAmountBasedGeneralLedgerIndicator()) {
                    extendedPrice = new KualiDecimal(this.itemForeignUnitCost.toString());
                } else if (ObjectUtils.isNotNull(this.getItemQuantity())) {
                    BigDecimal calcExtendedPrice = this.itemForeignUnitCost.bigDecimalValue().multiply(this.getItemQuantity().bigDecimalValue());
                    extendedPrice = new KualiDecimal(calcExtendedPrice.setScale(KualiDecimal.SCALE, KualiDecimal.ROUND_BEHAVIOR));
                }
            }
        }
        return extendedPrice;
    }

    public String getFundCode() {
        return fundCode;
    }
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }
    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public void setItemExtendedPrice(BigDecimal itemExtendedPrice) {
        this.itemExtendedPrice = itemExtendedPrice;
    }

    public String getSubscriptionPeriod() {
        return subscriptionPeriod;
    }

    public void setSubscriptionPeriod(String subscriptionPeriod) {
        this.subscriptionPeriod = subscriptionPeriod;
    }
}