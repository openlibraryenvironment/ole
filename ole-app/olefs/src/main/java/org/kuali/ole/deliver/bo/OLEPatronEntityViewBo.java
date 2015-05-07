package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created by sheiksalahudeenm on 4/10/14.
 */
public class OLEPatronEntityViewBo extends PersistableBusinessObjectBase {
    private String patronId;
    private String patronBarcode;
    private String patronType;
    private String expirationDate;
    private String entityId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private String prefix;
    private String name;
    private boolean active;
    private String requestorPatronId;
    private String emailAddress;
    private String phoneNumber;
    private String viewBillUrl;
    private String createBillUrl;
    private boolean patronBillFlag;
    private int billCount;
    private String patronBillFileName;
    private String patronTypeId;
    private int loanCount;
    private int tempCirculationHistoryCount;
    private int requestedItemRecordsCount;

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public String getPatronBarcode() {
        return patronBarcode;
    }

    public void setPatronBarcode(String patronBarcode) {
        this.patronBarcode = patronBarcode;
    }

    public String getPatronType() {
        return patronType;
    }

    public void setPatronType(String patronType) {
        this.patronType = patronType;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequestorPatronId() {
        return requestorPatronId;
    }

    public void setRequestorPatronId(String requestorPatronId) {
        this.requestorPatronId = requestorPatronId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isPatronBillFlag() {
        return patronBillFlag;
    }

    public void setPatronBillFlag(boolean patronBillFlag) {
        this.patronBillFlag = patronBillFlag;
    }

    public int getBillCount() {
        return billCount;
    }

    public void setBillCount(int billCount) {
        this.billCount = billCount;
    }

    public String getViewBillUrl() {
        return viewBillUrl;
    }

    public void setViewBillUrl(String viewBillUrl) {
        this.viewBillUrl = viewBillUrl;
    }

    public String getCreateBillUrl() {
        return createBillUrl;
    }

    public void setCreateBillUrl(String createBillUrl) {
        this.createBillUrl = createBillUrl;
    }

    public String getPatronBillFileName() {
        return patronBillFileName;
    }

    public void setPatronBillFileName(String patronBillFileName) {
        this.patronBillFileName = patronBillFileName;
    }

    public String getPatronTypeId() {
        return patronTypeId;
    }

    public void setPatronTypeId(String patronTypeId) {
        this.patronTypeId = patronTypeId;
    }

    public int getLoanCount() {
        return loanCount;
    }

    public void setLoanCount(int loanCount) {
        this.loanCount = loanCount;
    }

    public int getTempCirculationHistoryCount() {
        return tempCirculationHistoryCount;
    }

    public void setTempCirculationHistoryCount(int tempCirculationHistoryCount) {
        this.tempCirculationHistoryCount = tempCirculationHistoryCount;
    }

    public int getRequestedItemRecordsCount() {
        return requestedItemRecordsCount;
    }

    public void setRequestedItemRecordsCount(int requestedItemRecordsCount) {
        this.requestedItemRecordsCount = requestedItemRecordsCount;
    }
}
