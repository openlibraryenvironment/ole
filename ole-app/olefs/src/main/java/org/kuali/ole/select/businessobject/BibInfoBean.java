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

import org.apache.cxf.common.util.StringUtils;
import org.kuali.rice.krad.bo.BusinessObjectBase;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class BibInfoBean extends BusinessObjectBase {

    private String leader;

    private String controlField;

    // private String requestorSourceType;

    private String titleId;

    private String title;

    private String mainTitle;

    private String subTitle;

    private ArrayList<String> authors;

    private String author;

    private String edition;

    private String series;

    private String publisher;

    private String localIdentifier;

    private String placeOfPublication;

    private String yearOfPublication;

    private String standardNumber;

    private String typeOfStandardNumber;

    private String routeRequesterReceipt;

    private String requestSource;

    private String requisitionSource;

    private String requestSourceUrl;

    private Long startPage;

    private Long endPage;

    private String category;

    private String isbn;

    private String accountNumber;

    private String librarynote;

    private Double listprice;

    private Long quantity;

    private String ybp;

    private String subAccountNumber;

    private String binding;

    private String initials;

    private String dateOrdered;

    private String vendorCode;

    private String ybpuid;

    private String volumeNumber;

    private String location;

    private String requisitionDescription;

    private String financialYear;

    private String fundingSource;

    private String deliveryCampusCode;

    private String deliveryToName;

    private String deliveryBuildingCode;

    private String deliveryBuildingLine1Address;

    private String deliveryBuildingRoomNumber;

    private String deliveryCityName;

    private String deliveryStateCode;

    private String deliveryPostalCode;

    private String deliveryCountryCode;

    private String vendorCustomerNumber;

    private String purchaseOrderTransmissionMethodCode;

    private String purchaseOrderCostSourceCode;

    private String requestorPersonName;

    private String requestorPersonEmailAddress;

    private String requestorPersonPhoneNumber;

    private String dateOfPublication;

    private String physicalDescription;

    private String format;

    private String subjects;

    private String price;

    private String noOfCopies;

    private String requestorContact;

    private String requestorsFirstName;

    private String requestorsLastName;

    private String requestorsAddress1;

    private String requestorsAddress2;

    private String requestorsCity;

    private String requestorsState;

    private String requestorsZipCode;

    private String requestorsCountryCode;

    private String requestorsPhone;

    private String requestorsEmail;

    private String requestorsSMS;

    private String requestorType;

    private String requestersNotes;

    private String selector;

    private String selectorNotes;

    private String uom;

    private String chart;

    private String objectCode;

    private Long percent;

    private String chartOfAccountsCode;

    private String organizationCode;

    private String documentFundingSourceCode;

    private boolean useTaxIndicator;

    private boolean deliveryBuildingOtherIndicator;

    private String organizationAutomaticPurchaseOrderLimit;

    private boolean purchaseOrderAutomaticIndicator;

    private boolean receivingDocumentRequiredIndicator;

    private boolean paymentRequestPositiveApprovalIndicator;

    private String itemTypeCode;

    private String requestor;

    private String seriesOfStatement;

    private String fundCode;

    private String purchaseOrderType;

    private String operatorInitials;

    private String billingName;

    private String billingLine1Address;

    private String billingCityName;

    private String billingStateCode;

    private String billingPostalCode;

    private String billingPhoneNumber;

    private String billingCountryCode;

    private String docStoreOperation;

    private String docCategoryType;

    private String docCategoryUUID;

    private String failure;

    private String docStoreIngestionId;
    
    /*private boolean licensingRequirementIndicator;*/
    
    /*private String licensingRequirementCode;*/

    private String requestorId;

    public String getFailure() {
        return failure;
    }

    public void setFailure(String failure) {
        this.failure = failure;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }


    public String getAuthor() {
        return author;
    }

    public Long getStartPage() {
        return startPage;
    }

    public void setStartPage(Long startPage) {
        this.startPage = startPage;
    }

    public Long getEndPage() {
        return endPage;
    }

    public void setEndPage(Long endPage) {
        this.endPage = endPage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLocalIdentifier() {
        return localIdentifier;
    }

    public void setLocalIdentifier(String localIdentifier) {
        this.localIdentifier = localIdentifier;
    }

    public String getPlaceOfPublication() {
        return placeOfPublication;
    }

    public void setPlaceOfPublication(String placeOfPublication) {
        this.placeOfPublication = placeOfPublication;
    }

    public String getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(String yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public String getStandardNumber() {
        return standardNumber;
    }

    public void setStandardNumber(String standardNumber) {
        this.standardNumber = standardNumber;
    }

    public String getTypeOfStandardNumber() {
        return typeOfStandardNumber;
    }

    public void setTypeOfStandardNumber(String typeOfStandardNumber) {
        this.typeOfStandardNumber = typeOfStandardNumber;
    }

    public String getRouteRequesterReceipt() {
        return routeRequesterReceipt;
    }

    public void setRouteRequesterReceipt(String routeRequesterReceipt) {
        this.routeRequesterReceipt = routeRequesterReceipt;
    }

    public String getRequestSource() {
        return requestSource;
    }

    public void setRequestSource(String requestSource) {
        this.requestSource = requestSource;
    }

    public String getRequestSourceUrl() {
        return requestSourceUrl;
    }

    public void setRequestSourceUrl(String requestSourceUrl) {
        this.requestSourceUrl = requestSourceUrl;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getRequisitionDescription() {
        return requisitionDescription;
    }

    public void setRequisitionDescription(String requisitionDescription) {
        this.requisitionDescription = requisitionDescription;
    }

    public String getFinantialYear() {
        return financialYear;
    }

    public void setFinantialYear(String finantialYear) {
        this.financialYear = finantialYear;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLibrarynote() {
        return librarynote;
    }

    public void setLibrarynote(String librarynote) {
        this.librarynote = librarynote;
    }

    public Double getListprice() {
        return listprice;
    }

    public void setListprice(Double listprice) {
        this.listprice = listprice;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getYbp() {
        return ybp;
    }

    public void setYbp(String ybp) {
        this.ybp = ybp;
    }

    public String getSubAccountNumber() {
        return subAccountNumber;
    }

    public void setSubAccountNumber(String subAccountNumber) {
        this.subAccountNumber = subAccountNumber;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(String dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getYbpuid() {
        return ybpuid;
    }

    public void setYbpuid(String ybpuid) {
        this.ybpuid = ybpuid;
    }

    public String getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(String volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    public String getFundingSource() {
        return fundingSource;
    }

    public void setFundingSource(String fundingSource) {
        this.fundingSource = fundingSource;
    }

    public String getDeliveryCampusCode() {
        return deliveryCampusCode;
    }

    public void setDeliveryCampusCode(String deliveryCampusCode) {
        this.deliveryCampusCode = deliveryCampusCode;
    }

    public String getDeliveryToName() {
        return deliveryToName;
    }

    public void setDeliveryToName(String deliveryToName) {
        this.deliveryToName = deliveryToName;
    }

    public String getDeliveryBuildingCode() {
        return deliveryBuildingCode;
    }

    public void setDeliveryBuildingCode(String deliveryBuildingCode) {
        this.deliveryBuildingCode = deliveryBuildingCode;
    }

    public String getDeliveryBuildingLine1Address() {
        return deliveryBuildingLine1Address;
    }

    public void setDeliveryBuildingLine1Address(String deliveryBuildingLine1Address) {
        this.deliveryBuildingLine1Address = deliveryBuildingLine1Address;
    }

    public String getDeliveryBuildingRoomNumber() {
        return deliveryBuildingRoomNumber;
    }

    public void setDeliveryBuildingRoomNumber(String deliveryBuildingRoomNumber) {
        this.deliveryBuildingRoomNumber = deliveryBuildingRoomNumber;
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

    public String getVendorCustomerNumber() {
        return vendorCustomerNumber;
    }

    public void setVendorCustomerNumber(String vendorCustomerNumber) {
        this.vendorCustomerNumber = vendorCustomerNumber;
    }

    public String getPurchaseOrderTransmissionMethodCode() {
        return purchaseOrderTransmissionMethodCode;
    }

    public void setPurchaseOrderTransmissionMethodCode(String purchaseOrderTransmissionMethodCode) {
        this.purchaseOrderTransmissionMethodCode = purchaseOrderTransmissionMethodCode;
    }

    public String getPurchaseOrderCostSourceCode() {
        return purchaseOrderCostSourceCode;
    }

    public void setPurchaseOrderCostSourceCode(String purchaseOrderCostSourceCode) {
        this.purchaseOrderCostSourceCode = purchaseOrderCostSourceCode;
    }

    public String getRequestorPersonName() {
        return requestorPersonName;
    }

    public void setRequestorPersonName(String requestorPersonName) {
        this.requestorPersonName = requestorPersonName;
    }

    public String getRequestorPersonEmailAddress() {
        return requestorPersonEmailAddress;
    }

    public void setRequestorPersonEmailAddress(String requestorPersonEmailAddress) {
        this.requestorPersonEmailAddress = requestorPersonEmailAddress;
    }

    public String getRequestorPersonPhoneNumber() {
        return requestorPersonPhoneNumber;
    }

    public void setRequestorPersonPhoneNumber(String requestorPersonPhoneNumber) {
        this.requestorPersonPhoneNumber = requestorPersonPhoneNumber;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public String getDateOfPublication() {
        return dateOfPublication;
    }

    public void setDateOfPublication(String dateOfPublication) {
        this.dateOfPublication = dateOfPublication;
    }

    public String getPhysicalDescription() {
        return physicalDescription;
    }

    public void setPhysicalDescription(String physicalDescription) {
        this.physicalDescription = physicalDescription;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNoOfCopies() {
        return noOfCopies;
    }

    public void setNoOfCopies(String noOfCopies) {
        this.noOfCopies = noOfCopies;
    }

    public String getRequestorContact() {
        return requestorContact;
    }

    public void setRequestorContact(String requestorContact) {
        this.requestorContact = requestorContact;
    }

    public String getRequestorType() {
        return requestorType;
    }

    public void setRequestorType(String requestorType) {
        this.requestorType = requestorType;
    }

    public String getRequestersNotes() {
        return requestersNotes;
    }

    public void setRequestersNotes(String requestersNotes) {
        this.requestersNotes = requestersNotes;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getSelectorNotes() {
        return selectorNotes;
    }

    public void setSelectorNotes(String selectorNotes) {
        this.selectorNotes = selectorNotes;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getChart() {
        return chart;
    }

    public void setChart(String chart) {
        this.chart = chart;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public Long getPercent() {
        return percent;
    }

    public void setPercent(Long percent) {
        this.percent = percent;
    }

    public String getChartOfAccountsCode() {
        return chartOfAccountsCode;
    }

    public void setChartOfAccountsCode(String chartOfAccountsCode) {
        this.chartOfAccountsCode = chartOfAccountsCode;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getDocumentFundingSourceCode() {
        return documentFundingSourceCode;
    }

    public void setDocumentFundingSourceCode(String documentFundingSourceCode) {
        this.documentFundingSourceCode = documentFundingSourceCode;
    }

    public boolean isUseTaxIndicator() {
        return useTaxIndicator;
    }

    public void setUseTaxIndicator(boolean useTaxIndicator) {
        this.useTaxIndicator = useTaxIndicator;
    }

    public boolean isDeliveryBuildingOtherIndicator() {
        return deliveryBuildingOtherIndicator;
    }

    public void setDeliveryBuildingOtherIndicator(boolean deliveryBuildingOtherIndicator) {
        this.deliveryBuildingOtherIndicator = deliveryBuildingOtherIndicator;
    }

    public String getOrganizationAutomaticPurchaseOrderLimit() {
        return organizationAutomaticPurchaseOrderLimit;
    }

    public void setOrganizationAutomaticPurchaseOrderLimit(String organizationAutomaticPurchaseOrderLimit) {
        this.organizationAutomaticPurchaseOrderLimit = organizationAutomaticPurchaseOrderLimit;
    }

    public boolean isPurchaseOrderAutomaticIndicator() {
        return purchaseOrderAutomaticIndicator;
    }

    public void setPurchaseOrderAutomaticIndicator(boolean purchaseOrderAutomaticIndicator) {
        this.purchaseOrderAutomaticIndicator = purchaseOrderAutomaticIndicator;
    }

    public boolean isReceivingDocumentRequiredIndicator() {
        return receivingDocumentRequiredIndicator;
    }

    public void setReceivingDocumentRequiredIndicator(boolean receivingDocumentRequiredIndicator) {
        this.receivingDocumentRequiredIndicator = receivingDocumentRequiredIndicator;
    }

    public boolean isPaymentRequestPositiveApprovalIndicator() {
        return paymentRequestPositiveApprovalIndicator;
    }

    public void setPaymentRequestPositiveApprovalIndicator(boolean paymentRequestPositiveApprovalIndicator) {
        this.paymentRequestPositiveApprovalIndicator = paymentRequestPositiveApprovalIndicator;
    }

    public String getItemTypeCode() {
        return itemTypeCode;
    }

    public void setItemTypeCode(String itemTypeCode) {
        this.itemTypeCode = itemTypeCode;
    }

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }

    public String getRequestorsFirstName() {
        return requestorsFirstName;
    }

    public void setRequestorsFirstName(String requestorsFirstName) {
        this.requestorsFirstName = requestorsFirstName;
    }

    public String getRequestorsLastName() {
        return requestorsLastName;
    }

    public void setRequestorsLastName(String requestorsLastName) {
        this.requestorsLastName = requestorsLastName;
    }

    public String getRequestorsAddress1() {
        return requestorsAddress1;
    }

    public void setRequestorsAddress1(String requestorsAddress1) {
        this.requestorsAddress1 = requestorsAddress1;
    }

    public String getRequestorsAddress2() {
        return requestorsAddress2;
    }

    public void setRequestorsAddress2(String requestorsAddress2) {
        this.requestorsAddress2 = requestorsAddress2;
    }

    public String getRequestorsCity() {
        return requestorsCity;
    }

    public void setRequestorsCity(String requestorsCity) {
        this.requestorsCity = requestorsCity;
    }

    public String getRequestorsState() {
        return requestorsState;
    }

    public void setRequestorsState(String requestorsState) {
        this.requestorsState = requestorsState;
    }

    public String getRequestorsZipCode() {
        return requestorsZipCode;
    }

    public void setRequestorsZipCode(String requestorsZipCode) {
        this.requestorsZipCode = requestorsZipCode;
    }

    public String getRequestorsCountryCode() {
        return requestorsCountryCode;
    }

    public void setRequestorsCountryCode(String requestorsCountryCode) {
        this.requestorsCountryCode = requestorsCountryCode;
    }

    public String getRequestorsPhone() {
        return requestorsPhone;
    }

    public void setRequestorsPhone(String requestorsPhone) {
        this.requestorsPhone = requestorsPhone;
    }

    public String getRequestorsEmail() {
        return requestorsEmail;
    }

    public void setRequestorsEmail(String requestorsEmail) {
        this.requestorsEmail = requestorsEmail;
    }

    public String getRequestorsSMS() {
        return requestorsSMS;
    }

    public void setRequestorsSMS(String requestorsSMS) {
        this.requestorsSMS = requestorsSMS;
    }

    public String getSeriesOfStatement() {
        return seriesOfStatement;
    }

    public void setSeriesOfStatement(String seriesOfStatement) {
        this.seriesOfStatement = seriesOfStatement;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }


    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        // TODO Auto-generated method stub
        return null;
    }

    public void refresh() {
        // TODO Auto-generated method stub

    }


    public String getPurchaseOrderType() {
        return purchaseOrderType;
    }

    public void setPurchaseOrderType(String purchaseOrderType) {
        this.purchaseOrderType = purchaseOrderType;
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

    public String getDocStoreOperation() {
        return docStoreOperation;
    }

    public void setDocStoreOperation(String docStoreOperation) {
        this.docStoreOperation = docStoreOperation;
    }

    public String getDocCategoryType() {
        return docCategoryType;
    }

    public void setDocCategoryType(String docCategoryType) {
        this.docCategoryType = docCategoryType;
    }

    public String getDocCategoryUUID() {
        return docCategoryUUID;
    }

    public void setDocCategoryUUID(String docCategoryUUID) {
        this.docCategoryUUID = docCategoryUUID;
    }

    /**
     * Returns Requestors firstname, lastname initials in lowercase
     *
     * @return operatorInitials
     */
    public String getOperatorInitials() {
        StringBuffer operatorInitials = new StringBuffer();
        if (!StringUtils.isEmpty(this.requestorsFirstName))
            operatorInitials.append(this.requestorsFirstName.toLowerCase().charAt(0));
        if (!StringUtils.isEmpty(this.requestorsLastName))
            operatorInitials.append(this.requestorsLastName.toLowerCase().charAt(0));
        return operatorInitials.toString();
    }

    public void setOperatorInitials(String operatorInitials) {
        this.operatorInitials = operatorInitials;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getControlField() {
        return controlField;
    }

    public void setControlField(String controlField) {
        this.controlField = controlField;
    }

    public String getRequisitionSource() {
        return requisitionSource;
    }

    public void setRequisitionSource(String requisitionSource) {
        this.requisitionSource = requisitionSource;
    }

    public String getDocStoreIngestionId() {
        return docStoreIngestionId;
    }

    public void setDocStoreIngestionId(String docStoreIngestionId) {
        this.docStoreIngestionId = docStoreIngestionId;
    }

    /*public boolean isLicensingRequirementIndicator() {
        return licensingRequirementIndicator;
    }

    public void setLicensingRequirementIndicator(boolean licensingRequirementIndicator) {
        this.licensingRequirementIndicator = licensingRequirementIndicator;
    }*/

   /* public String getLicensingRequirementCode() {
        return licensingRequirementCode;
    }

    public void setLicensingRequirementCode(String licensingRequirementCode) {
        this.licensingRequirementCode = licensingRequirementCode;
    }   */

    /**
     * Gets the requestorId attribute.
     *
     * @return Returns the requestorId.
     */
    public String getRequestorId() {
        return requestorId;
    }

    /**
     * Sets the requestorId attribute value.
     *
     * @param requestorId The requestorId to set.
     */
    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
    }

}
