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

import org.kuali.ole.coa.businessobject.Chart;
import org.kuali.ole.coa.businessobject.Organization;
import org.kuali.ole.module.purap.businessobject.CapitalAssetSystemState;
import org.kuali.ole.module.purap.businessobject.CapitalAssetSystemType;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderTransmissionMethod;
import org.kuali.ole.module.purap.businessobject.RecurringPaymentType;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;
import java.util.LinkedHashMap;

public class RequisitionLookup extends PersistableBusinessObjectBase {

    private String documentNumber;

    private String vendorCustomerNumber;

    private String statusCode;

    private String chartOfAccountsCode;

    private String organizationCode;

    private String capitalAssetSystemStateCode;

    private String capitalAssetSystemTypeCode;

    private String vendorCountryCode;

    private String vendorCityName;

    private String vendorLine1Address;

    private String vendorName;

    private String recurringPaymentTypeCode;

    private String purchaseOrderTransmissionMethodCode;

    private Integer postingYear;

    private Integer purapDocumentIdentifier;

    private Timestamp purchaseOrderBeginDate;

    private Timestamp purchaseOrderEndDate;

    private float purchaseOrderTotalLimit;

    private Integer vendorHeaderGeneratedIdentifier;

    private RecurringPaymentType recurringPaymentType;

    private PurchaseOrderTransmissionMethod purchaseOrderTransmissionMethod;

    private Organization organization;

    /**
     * Gets the organization attribute.
     *
     * @return Returns the organization.
     */
    public Organization getOrganization() {
        return organization;
    }

    /**
     * Sets the organization attribute value.
     *
     * @param organization The organization to set.
     */
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }


    private Chart chartOfAccounts;

    private CapitalAssetSystemType capitalAssetSystemType;

    /**
     * Gets the recurringPaymentType attribute.
     *
     * @return Returns the recurringPaymentType.
     */
    public RecurringPaymentType getRecurringPaymentType() {
        return recurringPaymentType;
    }

    /**
     * Sets the recurringPaymentType attribute value.
     *
     * @param recurringPaymentType The recurringPaymentType to set.
     */
    public void setRecurringPaymentType(RecurringPaymentType recurringPaymentType) {
        this.recurringPaymentType = recurringPaymentType;
    }

    /**
     * Gets the purchaseOrderTransmissionMethod attribute.
     *
     * @return Returns the purchaseOrderTransmissionMethod.
     */
    public PurchaseOrderTransmissionMethod getPurchaseOrderTransmissionMethod() {
        return purchaseOrderTransmissionMethod;
    }

    /**
     * Sets the purchaseOrderTransmissionMethod attribute value.
     *
     * @param purchaseOrderTransmissionMethod
     *         The purchaseOrderTransmissionMethod to set.
     */
    public void setPurchaseOrderTransmissionMethod(PurchaseOrderTransmissionMethod purchaseOrderTransmissionMethod) {
        this.purchaseOrderTransmissionMethod = purchaseOrderTransmissionMethod;
    }


    /**
     * Gets the chartOfAccounts attribute.
     *
     * @return Returns the chartOfAccounts.
     */
    public Chart getChartOfAccounts() {
        return chartOfAccounts;
    }

    /**
     * Sets the chartOfAccounts attribute value.
     *
     * @param chartOfAccounts The chartOfAccounts to set.
     */
    public void setChartOfAccounts(Chart chartOfAccounts) {
        this.chartOfAccounts = chartOfAccounts;
    }

    /**
     * Gets the capitalAssetSystemType attribute.
     *
     * @return Returns the capitalAssetSystemType.
     */
    public CapitalAssetSystemType getCapitalAssetSystemType() {
        return capitalAssetSystemType;
    }

    /**
     * Sets the capitalAssetSystemType attribute value.
     *
     * @param capitalAssetSystemType The capitalAssetSystemType to set.
     */
    public void setCapitalAssetSystemType(CapitalAssetSystemType capitalAssetSystemType) {
        this.capitalAssetSystemType = capitalAssetSystemType;
    }

    /**
     * Gets the capitalAssetSystemState attribute.
     *
     * @return Returns the capitalAssetSystemState.
     */
    public CapitalAssetSystemState getCapitalAssetSystemState() {
        return capitalAssetSystemState;
    }

    /**
     * Sets the capitalAssetSystemState attribute value.
     *
     * @param capitalAssetSystemState The capitalAssetSystemState to set.
     */
    public void setCapitalAssetSystemState(CapitalAssetSystemState capitalAssetSystemState) {
        this.capitalAssetSystemState = capitalAssetSystemState;
    }


    private CapitalAssetSystemState capitalAssetSystemState;

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getVendorCustomerNumber() {
        return vendorCustomerNumber;
    }

    public void setVendorCustomerNumber(String vendorCustomerNumber) {
        this.vendorCustomerNumber = vendorCustomerNumber;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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

    public String getCapitalAssetSystemStateCode() {
        return capitalAssetSystemStateCode;
    }

    public void setCapitalAssetSystemStateCode(String capitalAssetSystemStateCode) {
        this.capitalAssetSystemStateCode = capitalAssetSystemStateCode;
    }

    public String getCapitalAssetSystemTypeCode() {
        return capitalAssetSystemTypeCode;
    }

    public void setCapitalAssetSystemTypeCode(String capitalAssetSystemTypeCode) {
        this.capitalAssetSystemTypeCode = capitalAssetSystemTypeCode;
    }

    public String getVendorCountryCode() {
        return vendorCountryCode;
    }

    public void setVendorCountryCode(String vendorCountryCode) {
        this.vendorCountryCode = vendorCountryCode;
    }

    public String getVendorCityName() {
        return vendorCityName;
    }

    public void setVendorCityName(String vendorCityName) {
        this.vendorCityName = vendorCityName;
    }

    public String getVendorLine1Address() {
        return vendorLine1Address;
    }

    public void setVendorLine1Address(String vendorLine1Address) {
        this.vendorLine1Address = vendorLine1Address;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getRecurringPaymentTypeCode() {
        return recurringPaymentTypeCode;
    }

    public void setRecurringPaymentTypeCode(String recurringPaymentTypeCode) {
        this.recurringPaymentTypeCode = recurringPaymentTypeCode;
    }

    public String getPurchaseOrderTransmissionMethodCode() {
        return purchaseOrderTransmissionMethodCode;
    }

    public void setPurchaseOrderTransmissionMethodCode(String purchaseOrderTransmissionMethodCode) {
        this.purchaseOrderTransmissionMethodCode = purchaseOrderTransmissionMethodCode;
    }

    public Integer getPostingYear() {
        return postingYear;
    }

    public void setPostingYear(Integer postingYear) {
        this.postingYear = postingYear;
    }

    public Integer getPurapDocumentIdentifier() {
        return purapDocumentIdentifier;
    }

    public void setPurapDocumentIdentifier(Integer purapDocumentIdentifier) {
        this.purapDocumentIdentifier = purapDocumentIdentifier;
    }

    public Timestamp getPurchaseOrderBeginDate() {
        return purchaseOrderBeginDate;
    }

    public void setPurchaseOrderBeginDate(Timestamp purchaseOrderBeginDate) {
        this.purchaseOrderBeginDate = purchaseOrderBeginDate;
    }

    public Timestamp getPurchaseOrderEndDate() {
        return purchaseOrderEndDate;
    }

    public void setPurchaseOrderEndDate(Timestamp purchaseOrderEndDate) {
        this.purchaseOrderEndDate = purchaseOrderEndDate;
    }

    public float getPurchaseOrderTotalLimit() {
        return purchaseOrderTotalLimit;
    }

    public void setPurchaseOrderTotalLimit(float purchaseOrderTotalLimit) {
        this.purchaseOrderTotalLimit = purchaseOrderTotalLimit;
    }

    public Integer getVendorHeaderGeneratedIdentifier() {
        return vendorHeaderGeneratedIdentifier;
    }

    public void setVendorHeaderGeneratedIdentifier(Integer vendorHeaderGeneratedIdentifier) {
        this.vendorHeaderGeneratedIdentifier = vendorHeaderGeneratedIdentifier;
    }


    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        return map;
    }

}
