package org.kuali.ole.pojo;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/4/12
 * Time: 8:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleTxRecord {


    //Financial Document Detail Section
    private String year;
    private String amount;

    //Purchase Order Detail Section
    private String chartCode;         // profile xml
    private String orgCode;            // profile xml
    private boolean receivingRequired;   // profile xml
    private String contractManager;     // profile xml
    private String assignToUser;         // profile xml
    private boolean useTaxIndicator;      // profile xml
    private String orderType;            // profile xml
    private String fundingSource;        // "INSTITUTIONAL ACCOUNT"
    private boolean payReqPositiveApprovalReq; // profile xml
    private String previousPurchaseOrder;     // null or blank
    private boolean purchaseOrderConfirmationIndicator;  // y or true.
    private String requisitionSource;       //  "AUTO"

    //Status Changes Section
    private String additionalInfo;           // blank or null

    // Delivery Tab
    private String deliveryCampusCode;       // profile xml
    private String buildingCode;             // profile xml
    private String deliveryTo;               // determined based on deliveryCampusCode and buildingCode.
    private String deliveryAddress1;         // determined based on deliveryCampusCode and buildingCode.
    private String deliveryAddress2;         // determined based on deliveryCampusCode and buildingCode.
    private String deliveryBuildingRoomNumber;  // determined based on deliveryCampusCode and buildingCode.
    private String deliveryBuildingLine1Address; // determined based on deliveryCampusCode and buildingCode.
    private String deliveryCityName;   // determined based on deliveryCampusCode and buildingCode.
    private String deliveryStateCode;   // determined based on deliveryCampusCode and buildingCode.
    private String deliveryPostalCode;  // determined based on deliveryCampusCode and buildingCode.
    private String deliveryCountryCode;    // determined based on deliveryCampusCode and buildingCode.
    private Date deliveryDateRequired;     // blank or null
    private String deliveryDateRequiredReason; // blank or null
    private String deliveryInstruction;       // blank or null
    private String receivingAddress;       // blank or null
    private String receivingAddressToVendorIndicator;   // blank or null

    //Vendor Address Section
    private String vendor;               // determined based on vendor number.
    private String vendorNumber;          // EDIFACT
    private String vendorAddress1;       // determined based on vendor number.
    private String vendorAddress2;       // determined based on vendor number.
    private String attention;            // determined based on vendor number.
    private String vendorCity;           // determined based on vendor number.
    private String vendorState;          // determined based on vendor number.
    private String province;             // determined based on vendor number.
    private String vendorPostalCode;     // determined based on vendor number.
    private String vendorCountry;        // determined based on vendor number.
    private String vendorAliasName;

    //Vendor Info Section
    private String vendorChoice;         // profile xml
    private String vendorInfoCustomer;
    private String vendorNotes;           // blank or null
    private String alternativeNonPrimaryVendorPayment;  // blank or null
    private String contract;             // determined based on vendor number.
    private String vendorPhoneNumber;    // determined based on vendor number.
    private String vendorFaxNumber;      // determined based on vendor number.
    private String vendorPaymentTerms;   // determined based on vendor number.
    private String vendorShippingTitle;  // determined based on vendor number.
    private String vendorShippingPaymentTerms;  // determined based on vendor number.
    private String vendorContacts;          // determined based on vendor number.
    private String vendorSupplierDiversity;  // determined based on vendor number.

    //Stipulations tab
    private String note;

    //Items Section
    private String itemChartCode;                  // profile.xml
    private String itemType;                       // "QTY"
    private String quantity;                       // EDIFACT
    private String itemDescription;                // this information comes from the MARC record.Leave this as it is coded in OLE .3
    private String listPrice;                      // EDIFACT
    private String discount;                       // blank or null
    private String discountType;                   // blank or null
    private boolean routeToRequestor;               // "NO"
    private boolean publicView;                     // profile xml
    private String currencyType;                   // based on vendor number.

    private String vendorItemIdentifier;   // EDIFACT

    //Account Lines
    private String fundCode;
    private String accountNumber;    // EDIFACT
    private String objectCode;
    private String orgRefId;         // blank or null
    private String percent;          //100

    private String format;          // EDIFACT
    private String internalPurchasingLimit;    //Profile xml

    private String billingName;
    private String billingLine1Address;     // determined based on campus code on delivery section.
    private String billingCityName;        // determined based on campus code on delivery section.
    private String billingStateCode;       // determined based on campus code on delivery section.
    private String billingPostalCode;      // determined based on campus code on delivery section.
    private String billingPhoneNumber;     // determined based on campus code on delivery section.
    private String billingCountryCode;     // determined based on campus code on delivery section.

    //Free text note for line item.
    private String freeTextNote;                  // this information comes particularly from duke.edi

    private String methodOfPOTransmission; // "NOPR"

    private String costSource;

    private String defaultLocation;

    private String itemPriceSource;
    private String singleCopyNumber;

    private List<String> oleDonors;
    private String requestorName;
    private String itemStatus;
    private String itemNoOfParts;
    private String caption;
    private String volumeNumber;
    private String miscellaneousNote;
    private String receiptNote;
    private String requestorNote;
    private String selectorNote;
    private String splProcessInstrNote;
    private String vendorInstrNote;
    private String miscellaneousNoteValue;
    private String receiptNoteValue;
    private String requestorNoteValue;
    private String selectorNoteValue;
    private String splProcessInstrNoteValue;
    private String vendorInstrNoteValue;
    private String formatTypeId;
    private String requestSourceType;
    private List<String> miscellaneousNotes;
    private List<String> receiptNotes;
    private List<String> requestorNotes;
    private List<String> selectorNotes;
    private List<String> splProcessInstrNotes;
    private List<String> vendorInstrNotes;

    //Payment information
    private String recurringPaymentType;
    private String recurringPaymentBeginDate;
    private String recurringPaymentEndDate;
    private String bibId;


    public String getRecurringPaymentType() {
        return recurringPaymentType;
    }

    public void setRecurringPaymentType(String recurringPaymentType) {
        this.recurringPaymentType = recurringPaymentType;
    }

    public String getRecurringPaymentBeginDate() {
        return recurringPaymentBeginDate;
    }

    public void setRecurringPaymentBeginDate(String recurringPaymentBeginDate) {
        this.recurringPaymentBeginDate = recurringPaymentBeginDate;
    }

    public String getRecurringPaymentEndDate() {
        return recurringPaymentEndDate;
    }

    public void setRecurringPaymentEndDate(String recurringPaymentEndDate) {
        this.recurringPaymentEndDate = recurringPaymentEndDate;
    }

    public String getRequestorName() {
        return requestorName;
    }

    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getDefaultLocation() {
        return defaultLocation;
    }

    public void setDefaultLocation(String defaultLocation) {
        this.defaultLocation = defaultLocation;
    }

    public String getCostSource() {
        return costSource;
    }

    public void setCostSource(String costSource) {
        this.costSource = costSource;
    }


    public String getFreeTextNote() {
        return freeTextNote;
    }

    public void setFreeTextNote(String freeTextNote) {
        this.freeTextNote = freeTextNote;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getChartCode() {
        return chartCode;
    }

    public void setChartCode(String chartCode) {
        this.chartCode = chartCode;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getSingleCopyNumber() {
        return singleCopyNumber;
    }

    public void setSingleCopyNumber(String singleCopyNumber) {
        this.singleCopyNumber = singleCopyNumber;
    }

    public String getItemChartCode() {
        return itemChartCode;
    }

    public void setItemChartCode(String itemChartCode) {
        this.itemChartCode = itemChartCode;
    }

    public String getContractManager() {
        return contractManager;
    }

    public void setContractManager(String contractManager) {
        this.contractManager = contractManager;
    }

    public String getAssignToUser() {
        return assignToUser;
    }

    public void setAssignToUser(String assignToUser) {
        this.assignToUser = assignToUser;
    }

    public boolean isUseTaxIndicator() {
        return useTaxIndicator;
    }

    public void setUseTaxIndicator(boolean useTaxIndicator) {
        this.useTaxIndicator = useTaxIndicator;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getFundingSource() {
        return fundingSource;
    }

    public void setFundingSource(String fundingSource) {
        this.fundingSource = fundingSource;
    }


    public String getPreviousPurchaseOrder() {
        return previousPurchaseOrder;
    }

    public void setPreviousPurchaseOrder(String previousPurchaseOrder) {
        this.previousPurchaseOrder = previousPurchaseOrder;
    }


    public String getRequisitionSource() {
        return requisitionSource;
    }

    public void setRequisitionSource(String requisitionSource) {
        this.requisitionSource = requisitionSource;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getDeliveryCampusCode() {
        return deliveryCampusCode;
    }

    public void setDeliveryCampusCode(String deliveryCampusCode) {
        this.deliveryCampusCode = deliveryCampusCode;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getDeliveryTo() {
        return deliveryTo;
    }

    public void setDeliveryTo(String deliveryTo) {
        this.deliveryTo = deliveryTo;
    }

    public String getDeliveryAddress1() {
        return deliveryAddress1;
    }

    public void setDeliveryAddress1(String deliveryAddress1) {
        this.deliveryAddress1 = deliveryAddress1;
    }

    public String getDeliveryAddress2() {
        return deliveryAddress2;
    }

    public void setDeliveryAddress2(String deliveryAddress2) {
        this.deliveryAddress2 = deliveryAddress2;
    }

    public String getDeliveryBuildingRoomNumber() {
        return deliveryBuildingRoomNumber;
    }

    public void setDeliveryBuildingRoomNumber(String deliveryBuildingRoomNumber) {
        this.deliveryBuildingRoomNumber = deliveryBuildingRoomNumber;
    }

    public String getDeliveryBuildingLine1Address() {
        return deliveryBuildingLine1Address;
    }

    public void setDeliveryBuildingLine1Address(String deliveryBuildingLine1Address) {
        this.deliveryBuildingLine1Address = deliveryBuildingLine1Address;
    }

    public String getDeliveryCityName() {
        return deliveryCityName;
    }

    public void setDeliveryCityName(String deliveryCityName) {
        this.deliveryCityName = deliveryCityName;
    }

    public String getDeliveryStateCode() {
        return deliveryStateCode;
    }

    public void setDeliveryStateCode(String deliveryStateCode) {
        this.deliveryStateCode = deliveryStateCode;
    }

    public String getDeliveryPostalCode() {
        return deliveryPostalCode;
    }

    public void setDeliveryPostalCode(String deliveryPostalCode) {
        this.deliveryPostalCode = deliveryPostalCode;
    }

    public String getDeliveryCountryCode() {
        return deliveryCountryCode;
    }

    public void setDeliveryCountryCode(String deliveryCountryCode) {
        this.deliveryCountryCode = deliveryCountryCode;
    }

    public Date getDeliveryDateRequired() {
        return deliveryDateRequired;
    }

    public void setDeliveryDateRequired(Date deliveryDateRequired) {
        this.deliveryDateRequired = deliveryDateRequired;
    }

    public String getDeliveryDateRequiredReason() {
        return deliveryDateRequiredReason;
    }

    public void setDeliveryDateRequiredReason(String deliveryDateRequiredReason) {
        this.deliveryDateRequiredReason = deliveryDateRequiredReason;
    }

    public String getDeliveryInstruction() {
        return deliveryInstruction;
    }

    public void setDeliveryInstruction(String deliveryInstruction) {
        this.deliveryInstruction = deliveryInstruction;
    }

    public String getReceivingAddress() {
        return receivingAddress;
    }

    public void setReceivingAddress(String receivingAddress) {
        this.receivingAddress = receivingAddress;
    }

    public String getReceivingAddressToVendorIndicator() {
        return receivingAddressToVendorIndicator;
    }

    public void setReceivingAddressToVendorIndicator(String receivingAddressToVendorIndicator) {
        this.receivingAddressToVendorIndicator = receivingAddressToVendorIndicator;
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

    public String getVendorChoice() {
        return vendorChoice;
    }

    public void setVendorChoice(String vendorChoice) {
        this.vendorChoice = vendorChoice;
    }

    public String getVendorInfoCustomer() {
        return vendorInfoCustomer;
    }

    public void setVendorInfoCustomer(String vendorInfoCustomer) {
        this.vendorInfoCustomer = vendorInfoCustomer;
    }

    public String getVendorNotes() {
        return vendorNotes;
    }

    public void setVendorNotes(String vendorNotes) {
        this.vendorNotes = vendorNotes;
    }

    public String getAlternativeNonPrimaryVendorPayment() {
        return alternativeNonPrimaryVendorPayment;
    }

    public void setAlternativeNonPrimaryVendorPayment(String alternativeNonPrimaryVendorPayment) {
        this.alternativeNonPrimaryVendorPayment = alternativeNonPrimaryVendorPayment;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getVendorPhoneNumber() {
        return vendorPhoneNumber;
    }

    public void setVendorPhoneNumber(String vendorPhoneNumber) {
        this.vendorPhoneNumber = vendorPhoneNumber;
    }

    public String getVendorFaxNumber() {
        return vendorFaxNumber;
    }

    public void setVendorFaxNumber(String vendorFaxNumber) {
        this.vendorFaxNumber = vendorFaxNumber;
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

    public String getVendorContacts() {
        return vendorContacts;
    }

    public void setVendorContacts(String vendorContacts) {
        this.vendorContacts = vendorContacts;
    }

    public String getVendorSupplierDiversity() {
        return vendorSupplierDiversity;
    }

    public void setVendorSupplierDiversity(String vendorSupplierDiversity) {
        this.vendorSupplierDiversity = vendorSupplierDiversity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
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

    public String getOrgRefId() {
        return orgRefId;
    }

    public void setOrgRefId(String orgRefId) {
        this.orgRefId = orgRefId;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getInternalPurchasingLimit() {
        return internalPurchasingLimit;
    }

    public void setInternalPurchasingLimit(String internalPurchasingLimit) {
        this.internalPurchasingLimit = internalPurchasingLimit;
    }

    public String getBillingName() {
        return billingName;
    }

    public void setBillingName(String billingName) {
        this.billingName = billingName;
    }

    public String getBillingLine1Address() {
        return billingLine1Address;
    }

    public void setBillingLine1Address(String billingLine1Address) {
        this.billingLine1Address = billingLine1Address;
    }

    public String getBillingCityName() {
        return billingCityName;
    }

    public void setBillingCityName(String billingCityName) {
        this.billingCityName = billingCityName;
    }

    public String getBillingStateCode() {
        return billingStateCode;
    }

    public void setBillingStateCode(String billingStateCode) {
        this.billingStateCode = billingStateCode;
    }

    public String getBillingPostalCode() {
        return billingPostalCode;
    }

    public void setBillingPostalCode(String billingPostalCode) {
        this.billingPostalCode = billingPostalCode;
    }

    public String getBillingPhoneNumber() {
        return billingPhoneNumber;
    }

    public void setBillingPhoneNumber(String billingPhoneNumber) {
        this.billingPhoneNumber = billingPhoneNumber;
    }

    public String getBillingCountryCode() {
        return billingCountryCode;
    }

    public void setBillingCountryCode(String billingCountryCode) {
        this.billingCountryCode = billingCountryCode;
    }

    public String getMethodOfPOTransmission() {
        return methodOfPOTransmission;
    }

    public void setMethodOfPOTransmission(String methodOfPOTransmission) {
        this.methodOfPOTransmission = methodOfPOTransmission;
    }

    public boolean isReceivingRequired() {
        return receivingRequired;
    }

    public void setReceivingRequired(boolean receivingRequired) {
        this.receivingRequired = receivingRequired;
    }

    public boolean isPayReqPositiveApprovalReq() {
        return payReqPositiveApprovalReq;
    }

    public void setPayReqPositiveApprovalReq(boolean payReqPositiveApprovalReq) {
        this.payReqPositiveApprovalReq = payReqPositiveApprovalReq;
    }

    public boolean isPurchaseOrderConfirmationIndicator() {
        return purchaseOrderConfirmationIndicator;
    }

    public void setPurchaseOrderConfirmationIndicator(boolean purchaseOrderConfirmationIndicator) {
        this.purchaseOrderConfirmationIndicator = purchaseOrderConfirmationIndicator;
    }

    public boolean isRouteToRequestor() {
        return routeToRequestor;
    }

    public void setRouteToRequestor(boolean routeToRequestor) {
        this.routeToRequestor = routeToRequestor;
    }

    public String getItemNoOfParts() {
        return itemNoOfParts;
    }

    public void setItemNoOfParts(String itemNoOfParts) {
        this.itemNoOfParts = itemNoOfParts;
    }

    public boolean isPublicView() {
        return publicView;
    }

    public void setPublicView(boolean publicView) {
        this.publicView = publicView;
    }

    public List<String> getOleDonors() {
        return oleDonors;
    }

    public void setOleDonors(List<String> oleDonors) {
        this.oleDonors = oleDonors;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(String volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    public String getMiscellaneousNote() {
        return miscellaneousNote;
    }

    public void setMiscellaneousNote(String miscellaneousNote) {
        this.miscellaneousNote = miscellaneousNote;
    }

    public String getReceiptNote() {
        return receiptNote;
    }

    public void setReceiptNote(String receiptNote) {
        this.receiptNote = receiptNote;
    }

    public String getRequestorNote() {
        return requestorNote;
    }

    public void setRequestorNote(String requestorNote) {
        this.requestorNote = requestorNote;
    }

    public String getSelectorNote() {
        return selectorNote;
    }

    public void setSelectorNote(String selectorNote) {
        this.selectorNote = selectorNote;
    }

    public String getSplProcessInstrNote() {
        return splProcessInstrNote;
    }

    public void setSplProcessInstrNote(String splProcessInstrNote) {
        this.splProcessInstrNote = splProcessInstrNote;
    }

    public String getVendorInstrNote() {
        return vendorInstrNote;
    }

    public void setVendorInstrNote(String vendorInstrNote) {
        this.vendorInstrNote = vendorInstrNote;
    }

    public String getMiscellaneousNoteValue() {
        return miscellaneousNoteValue;
    }

    public void setMiscellaneousNoteValue(String miscellaneousNoteValue) {
        this.miscellaneousNoteValue = miscellaneousNoteValue;
    }

    public String getReceiptNoteValue() {
        return receiptNoteValue;
    }

    public void setReceiptNoteValue(String receiptNoteValue) {
        this.receiptNoteValue = receiptNoteValue;
    }

    public String getRequestorNoteValue() {
        return requestorNoteValue;
    }

    public void setRequestorNoteValue(String requestorNoteValue) {
        this.requestorNoteValue = requestorNoteValue;
    }

    public String getSelectorNoteValue() {
        return selectorNoteValue;
    }

    public void setSelectorNoteValue(String selectorNoteValue) {
        this.selectorNoteValue = selectorNoteValue;
    }

    public String getSplProcessInstrNoteValue() {
        return splProcessInstrNoteValue;
    }

    public void setSplProcessInstrNoteValue(String splProcessInstrNoteValue) {
        this.splProcessInstrNoteValue = splProcessInstrNoteValue;
    }

    public String getVendorInstrNoteValue() {
        return vendorInstrNoteValue;
    }

    public void setVendorInstrNoteValue(String vendorInstrNoteValue) {
        this.vendorInstrNoteValue = vendorInstrNoteValue;
    }

    public String getFormatTypeId() {
        return formatTypeId;
    }

    public void setFormatTypeId(String formatTypeId) {
        this.formatTypeId = formatTypeId;
    }

    public String getRequestSourceType() {
        return requestSourceType;
    }

    public void setRequestSourceType(String requestSourceType) {
        this.requestSourceType = requestSourceType;
    }

    public List<String> getMiscellaneousNotes() {
        return miscellaneousNotes;
    }

    public void setMiscellaneousNotes(List<String> miscellaneousNotes) {
        this.miscellaneousNotes = miscellaneousNotes;
    }

    public List<String> getReceiptNotes() {
        return receiptNotes;
    }

    public void setReceiptNotes(List<String> receiptNotes) {
        this.receiptNotes = receiptNotes;
    }

    public List<String> getRequestorNotes() {
        return requestorNotes;
    }

    public void setRequestorNotes(List<String> requestorNotes) {
        this.requestorNotes = requestorNotes;
    }

    public List<String> getSelectorNotes() {
        return selectorNotes;
    }

    public void setSelectorNotes(List<String> selectorNotes) {
        this.selectorNotes = selectorNotes;
    }

    public List<String> getSplProcessInstrNotes() {
        return splProcessInstrNotes;
    }

    public void setSplProcessInstrNotes(List<String> splProcessInstrNotes) {
        this.splProcessInstrNotes = splProcessInstrNotes;
    }

    public List<String> getVendorInstrNotes() {
        return vendorInstrNotes;
    }

    public void setVendorInstrNotes(List<String> vendorInstrNotes) {
        this.vendorInstrNotes = vendorInstrNotes;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public String getBibId() {
        return bibId;
    }

    public String getVendorAliasName() {
        return vendorAliasName;
    }

    public void setVendorAliasName(String vendorAliasName) {
        this.vendorAliasName = vendorAliasName;
    }

    public String getItemPriceSource() {
        return itemPriceSource;
    }

    public void setItemPriceSource(String itemPriceSource) {
        this.itemPriceSource = itemPriceSource;
    }
}
