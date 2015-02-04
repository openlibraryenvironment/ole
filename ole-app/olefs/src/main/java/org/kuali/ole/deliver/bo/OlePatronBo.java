package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.Date;

/**
 * OlePatronBo provides OlePatronBo information through getter and setter.
 */
public class OlePatronBo extends PersistableBusinessObjectBase {
    private Integer olePatronId;
    private String entityId;
    private String barcode;
    private String borrowerType;
    private String activeIndicator;
    private String generalBlock;
    private String pagingPrivilege;
    private String courtesyNotice;
    private String deliveryPrivilege;
    private Date expirationDate;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;

    /**
     * Gets the value of olePatronId property
     *
     * @return olePatronId
     */
    public Integer getOlePatronId() {
        return olePatronId;
    }

    /**
     * Sets the value for olePatronId property
     *
     * @param OlePatronImplId
     */
    public void setOlePatronId(Integer OlePatronImplId) {
        this.olePatronId = olePatronId;
    }

    /**
     * Gets the value of entityId property
     *
     * @return entityId
     */
    public String getEntityId() {
        return entityId;
    }

    /**
     * Sets the value for entityId property
     *
     * @param entityId
     */
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    /**
     * Gets the value of barcode property
     *
     * @return barcode
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * Sets the value for barcode property
     *
     * @param barcode
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * Gets the value of borrowerType property
     *
     * @return borrowerType
     */
    public String getBorrowerType() {
        return borrowerType;
    }

    /**
     * Sets the value for borrowerType property
     *
     * @param borrowerType
     */
    public void setBorrowerType(String borrowerType) {
        this.borrowerType = borrowerType;
    }

    /**
     * Gets the value of activeIndicator property
     *
     * @return activeIndicator
     */
    public String getActiveIndicator() {
        return activeIndicator;
    }

    /**
     * Sets the value for activeIndicator property
     *
     * @param activeIndicator
     */
    public void setActiveIndicator(String activeIndicator) {
        this.activeIndicator = activeIndicator;
    }

    /**
     * Gets the value of generalBlock property
     *
     * @return generalBlock
     */
    public String getGeneralBlock() {
        return generalBlock;
    }

    /**
     * Sets the value for generalBlock property
     *
     * @param generalBlock
     */
    public void setGeneralBlock(String generalBlock) {
        this.generalBlock = generalBlock;
    }

    /**
     * Gets the value of pagingPrivilege property
     *
     * @return pagingPrivilege
     */
    public String getPagingPrivilege() {
        return pagingPrivilege;
    }

    /**
     * Sets the value for pagingPrivilege property
     *
     * @param pagingPrivilege
     */
    public void setPagingPrivilege(String pagingPrivilege) {
        this.pagingPrivilege = pagingPrivilege;
    }

    /**
     * Gets the value of courtesyNotice property
     *
     * @return courtesyNotice
     */
    public String getCourtesyNotice() {
        return courtesyNotice;
    }

    /**
     * Sets the value for courtesyNotice property
     *
     * @param courtesyNotice
     */
    public void setCourtesyNotice(String courtesyNotice) {
        this.courtesyNotice = courtesyNotice;
    }

    /**
     * Gets the value of deliveryPrivilege property
     *
     * @return deliveryPrivilege
     */
    public String getDeliveryPrivilege() {
        return deliveryPrivilege;
    }

    /**
     * Sets the value for deliveryPrivilege property
     *
     * @param deliveryPrivilege
     */
    public void setDeliveryPrivilege(String deliveryPrivilege) {
        this.deliveryPrivilege = deliveryPrivilege;
    }

    /**
     * Gets the value of expirationDate of type Date
     *
     * @return expirationDate(Date)
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the value for expirationDate of type Date
     *
     * @param expirationDate(Date)
     */
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Gets the value of emailAddress property
     *
     * @return emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the value for emailAddress property
     *
     * @param emailAddress
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Gets the value of firstName property
     *
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value for firstName property
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the value of lastName property
     *
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value for lastName property
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the value of phoneNumber property
     *
     * @return phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the value for phoneNumber property
     *
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
