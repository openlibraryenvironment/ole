package org.kuali.ole.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/27/13
 * Time: 6:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleInvoiceRecord {

    public OleInvoiceRecord(){
        this.validDoc=true;
    }
    //Vendor Address Section
    private String vendor;               // determined based on vendor number.
    private String vendorNumber;         // EDIFACT
    private String vendorAlias;         // EDIFACT
    private String vendorAddress1;       // determined based on vendor number.
    private String vendorAddress2;       // determined based on vendor number.
    private String attention;            // determined based on vendor number.
    private String vendorCity;           // determined based on vendor number.
    private String vendorState;          // determined based on vendor number.
    private String province;             // determined based on vendor number.
    private String vendorPostalCode;     // determined based on vendor number.
    private String vendorCountry;        // determined based on vendor number.

    //Vendor Info Section
    private String vendorNotes;           // blank or null (Discussion needed on stub notes)
    private String vendorPaymentTerms;   // determined based on vendor number.
    private String vendorShippingTitle;  // determined based on vendor number.
    private String vendorShippingPaymentTerms;  // determined based on vendor number.

    private String bfnNumber;             // EDIFACT

    // Invoice Information
    private String invoiceNumber;         // EDIFACT
    private String invoiceDate;           // EDIFACT
    private String vendorInvoiceAmount;   // EDIFACT
    private String paymentMethod;
    private String foreignVendorInvoiceAmount; // EDIFACT

    // Additional Charge
    private String additionalCharge;      // EDIFACT
    private String additionalChargeCode;  // EDIFACT
    private String lineItemAdditionalChargeCode; // EDIFACT
    private String lineItemAdditionalCharge; // EDIFACT
    // line item

    private Integer purchaseOrderNumber;  //Purchase Order   (same vnd should be for PO) (PO should be available for invoice)
    private String productId;                    // EDIFACT
    private String ISBN;                         // EDIFACT
    private String ISBNCode;                     // EDIFACT
    private String ISSN;                         // EDIFACT
    private String ISSNCode;                     // EDIFACT
    private String billToCustomerID;             // EDIFACT

    private String itemChartCode;                  // profile.xml
    private String itemType;                       // "QTY"
    private String quantity;                       // EDIFACT
    private String itemDescription;                // EDIFACT
    private List itemNote;                         // EDIFACT
    private String listPrice;                      // EDIFACT
    private String unitPrice;                      // EDIFACT
    private String subscriptionPeriodFrom;         // EDIFACT
    private String subscriptionPeriodTo;        // EDIFACT
    private String discount;                       // blank or null
    private String discountType;                   // blank or null
    private String currencyType;                   // based on vendor number.
    private String currencyTypeId;
    private String vendorItemIdentifier;   // EDIFACT (RFF)
    private String numberOfCopiesOrdered; // retrieve from quantity
    private String numberOfParts; // default "1"
    private String accountNumber;
    private String objectCode;
    private String fundCode;
    private String itemTitleIdForMRC;
    private String invoiceCurrencyExchangeRate;
    private String foreignListPrice;
    private String lineItemTaxAmount;
    private String summaryTaxAmount;
    private String summaryTaxableAmount;
    private String subscriptionPeriod;

    // Summary of all Charges
    private String summaryAmount;

    private boolean validDoc;

    private List olePurchaseOrderItems;
    private boolean link;
    private String matchPointType;
    private Integer recordIndex;

    public String getVendorAlias() {
        return vendorAlias;
    }

    public void setVendorAlias(String vendorAlias) {
        this.vendorAlias = vendorAlias;
    }

    public String getBillToCustomerID() {
        return billToCustomerID;
    }

    public void setBillToCustomerID(String billToCustomerID) {
        this.billToCustomerID = billToCustomerID;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getLineItemAdditionalChargeCode() {
        return lineItemAdditionalChargeCode;
    }

    public void setLineItemAdditionalChargeCode(String lineItemAdditionalChargeCode) {
        this.lineItemAdditionalChargeCode = lineItemAdditionalChargeCode;
    }

    public String getLineItemAdditionalCharge() {
        return lineItemAdditionalCharge;
    }

    public void setLineItemAdditionalCharge(String lineItemAdditionalCharge) {
        this.lineItemAdditionalCharge = lineItemAdditionalCharge;
    }


    public List getItemNote() {
        return itemNote;
    }

    public void setItemNote(List itemNote) {
        this.itemNote = itemNote;
    }

    public String getBfnNumber() {
        return bfnNumber;
    }

    public void setBfnNumber(String bfnNumber) {
        this.bfnNumber = bfnNumber;
    }

    public String getAdditionalChargeCode() {
        return additionalChargeCode;
    }

    public void setAdditionalChargeCode(String additionalChargeCode) {
        this.additionalChargeCode = additionalChargeCode;
    }

    public String getSubscriptionPeriodFrom() {
        return subscriptionPeriodFrom;
    }

    public void setSubscriptionPeriodFrom(String subscriptionPeriodFrom) {
        this.subscriptionPeriodFrom = subscriptionPeriodFrom;
    }

    public String getSubscriptionPeriodTo() {
        return subscriptionPeriodTo;
    }

    public void setSubscriptionPeriodTo(String subscriptionPeriodTo) {
        this.subscriptionPeriodTo = subscriptionPeriodTo;
    }

    public String getForeignVendorInvoiceAmount() {
        return foreignVendorInvoiceAmount;
    }

    public void setForeignVendorInvoiceAmount(String foreignVendorInvoiceAmount) {
        this.foreignVendorInvoiceAmount = foreignVendorInvoiceAmount;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getVendorNumber() {
        return vendorNumber;
    }

    public void setVendorNumber(String vendorNumber) {
        this.vendorNumber = vendorNumber;
    }

    public String getVendorAddress1() {
        return vendorAddress1;
    }

    public void setVendorAddress1(String vendorAddress1) {
        this.vendorAddress1 = vendorAddress1;
    }

    public String getVendorAddress2() {
        return vendorAddress2;
    }

    public void setVendorAddress2(String vendorAddress2) {
        this.vendorAddress2 = vendorAddress2;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getVendorCity() {
        return vendorCity;
    }

    public void setVendorCity(String vendorCity) {
        this.vendorCity = vendorCity;
    }

    public String getVendorState() {
        return vendorState;
    }

    public void setVendorState(String vendorState) {
        this.vendorState = vendorState;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getVendorPostalCode() {
        return vendorPostalCode;
    }

    public void setVendorPostalCode(String vendorPostalCode) {
        this.vendorPostalCode = vendorPostalCode;
    }

    public String getVendorCountry() {
        return vendorCountry;
    }

    public void setVendorCountry(String vendorCountry) {
        this.vendorCountry = vendorCountry;
    }

    public String getVendorNotes() {
        return vendorNotes;
    }

    public void setVendorNotes(String vendorNotes) {
        this.vendorNotes = vendorNotes;
    }

    public String getVendorPaymentTerms() {
        return vendorPaymentTerms;
    }

    public void setVendorPaymentTerms(String vendorPaymentTerms) {
        this.vendorPaymentTerms = vendorPaymentTerms;
    }

    public String getVendorShippingTitle() {
        return vendorShippingTitle;
    }

    public void setVendorShippingTitle(String vendorShippingTitle) {
        this.vendorShippingTitle = vendorShippingTitle;
    }

    public String getVendorShippingPaymentTerms() {
        return vendorShippingPaymentTerms;
    }

    public void setVendorShippingPaymentTerms(String vendorShippingPaymentTerms) {
        this.vendorShippingPaymentTerms = vendorShippingPaymentTerms;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getVendorInvoiceAmount() {
        return vendorInvoiceAmount;
    }

    public void setVendorInvoiceAmount(String vendorInvoiceAmount) {
        this.vendorInvoiceAmount = vendorInvoiceAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(Integer purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getISBNCode() {
        return ISBNCode;
    }

    public void setISBNCode(String ISBNCode) {
        this.ISBNCode = ISBNCode;
    }

    public String getISSN() {
        return ISSN;
    }

    public void setISSN(String ISSN) {
        this.ISSN = ISSN;
    }

    public String getISSNCode() {
        return ISSNCode;
    }

    public void setISSNCode(String ISSNCode) {
        this.ISSNCode = ISSNCode;
    }

    public String getItemChartCode() {
        return itemChartCode;
    }

    public void setItemChartCode(String itemChartCode) {
        this.itemChartCode = itemChartCode;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getListPrice() {
        return listPrice;
    }

    public void setListPrice(String listPrice) {
        this.listPrice = listPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getVendorItemIdentifier() {
        return vendorItemIdentifier;
    }

    public void setVendorItemIdentifier(String vendorItemIdentifier) {
        this.vendorItemIdentifier = vendorItemIdentifier;
    }

    public String getNumberOfCopiesOrdered() {
        return numberOfCopiesOrdered;
    }

    public void setNumberOfCopiesOrdered(String numberOfCopiesOrdered) {
        this.numberOfCopiesOrdered = numberOfCopiesOrdered;
    }

    public String getNumberOfParts() {
        return numberOfParts;
    }

    public void setNumberOfParts(String numberOfParts) {
        this.numberOfParts = numberOfParts;
    }

    public String getAdditionalCharge() {
        return additionalCharge;
    }

    public void setAdditionalCharge(String additionalCharge) {
        this.additionalCharge = additionalCharge;
    }

    public String getSummaryAmount() {
        return summaryAmount;
    }

    public void setSummaryAmount(String summaryAmount) {
        this.summaryAmount = summaryAmount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getItemTitleIdForMRC() {
        return itemTitleIdForMRC;
    }

    public void setItemTitleIdForMRC(String itemTitleIdForMRC) {
        this.itemTitleIdForMRC = itemTitleIdForMRC;
    }

    public String getCurrencyTypeId() {
        return currencyTypeId;
    }

    public void setCurrencyTypeId(String currencyTypeId) {
        this.currencyTypeId = currencyTypeId;
    }

    public String getInvoiceCurrencyExchangeRate() {
        return invoiceCurrencyExchangeRate;
    }

    public void setInvoiceCurrencyExchangeRate(String invoiceCurrencyExchangeRate) {
        this.invoiceCurrencyExchangeRate = invoiceCurrencyExchangeRate;
    }

    public String getForeignListPrice() {
        return foreignListPrice;
    }

    public void setForeignListPrice(String foreignListPrice) {
        this.foreignListPrice = foreignListPrice;
    }

    public boolean isValidDoc() {
        return validDoc;
    }

    public void setValidDoc(boolean validDoc) {
        this.validDoc = validDoc;
    }

    public String getLineItemTaxAmount() {
        return lineItemTaxAmount;
    }

    public void setLineItemTaxAmount(String lineItemTaxAmount) {
        this.lineItemTaxAmount = lineItemTaxAmount;
    }

    public String getSummaryTaxAmount() {
        return summaryTaxAmount;
    }

    public void setSummaryTaxAmount(String summaryTaxAmount) {
        this.summaryTaxAmount = summaryTaxAmount;
    }

    public String getSummaryTaxableAmount() {
        return summaryTaxableAmount;
    }

    public void setSummaryTaxableAmount(String summaryTaxableAmount) {
        this.summaryTaxableAmount = summaryTaxableAmount;
    }

    public boolean isLink() {
        return link;
    }

    public void setLink(boolean link) {
        this.link = link;
    }

    public List getOlePurchaseOrderItems() {
        if(null == olePurchaseOrderItems) {
            olePurchaseOrderItems = new ArrayList();
        }
        return olePurchaseOrderItems;
    }

    public void setOlePurchaseOrderItems(List olePurchaseOrderItems) {
        this.olePurchaseOrderItems = olePurchaseOrderItems;
    }

    public String getMatchPointType() {
        return matchPointType;
    }

    public void setMatchPointType(String matchPointType) {
        this.matchPointType = matchPointType;
    }

    public String getSubscriptionPeriod() {
        return subscriptionPeriod;
    }

    public void setSubscriptionPeriod(String subscriptionPeriod) {
        this.subscriptionPeriod = subscriptionPeriod;
    }

    public Integer getRecordIndex() {
        return recordIndex;
    }

    public void setRecordIndex(Integer recordIndex) {
        this.recordIndex = recordIndex;
    }
}
