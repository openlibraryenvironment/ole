package org.kuali.ole.ingest.pojo;

import java.sql.Date;
import java.util.List;

/**
 * OlePatron is a business object class for Ole Patron Document
 */
public class OlePatron {
    private String errorMessage;
    private String patronID;
    private Date expirationDate;
    private Date activationDate;
    private String source;
    private String statisticalCategory;
    private boolean active;
    private OleNameTypes name;
    private String borrowerType;
    private String barcode;
    private List<OlePatronAffiliations> affiliations;
    private List<OlePatronPostalAddress> postalAddresses;
    private List<OlePatronEmailAddress> emailAddresses;
    private List<OlePatronTelePhoneNumber> telephoneNumbers;
    private OlePatronLevelPolicies patronLevelPolicies;
    private List<OlePatronNote> notes;
    private List<OlePatronLocalIdentification> localIdentifications;
    /**
     * Gets the patronID attribute.
     * @return  Returns the patronID.
     */
    public String getPatronID() {
        return patronID;
    }
    /**
     * Sets the patronID attribute value.
     * @param patronID The patronID to set.
     */
    public void setPatronID(String patronID) {
        this.patronID = patronID;
    }
    /**
     * Gets the expirationDate attribute.
     * @return  Returns the expirationDate.
     */
    public Date getExpirationDate() {
        return expirationDate;
    }
    /**
     * Sets the expirationDate attribute value.
     * @param  expirationDate The expirationDate to set.
     */
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
    /**
     * Gets the active attribute.
     * @return  Returns the active.
     */
    public boolean isActive() {
        return active;
    }
    /**
     * Sets the active attribute value.
     * @param active The active to set.
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    /**
     * Gets the name attribute.
     * @return  Returns the name.
     */
    public OleNameTypes getName() {
        return name;
    }
    /**
     * Sets the name attribute value.
     * @param name The name to set.
     */
    public void setName(OleNameTypes name) {
        this.name = name;
    }
    /**
     * Gets the borrowerType attribute.
     * @return  Returns the borrowerType.
     */
    public String getBorrowerType() {
        return borrowerType;
    }
    /**
     * Sets the borrowerType attribute value.
     * @param borrowerType The borrowerType to set.
     */
    public void setBorrowerType(String borrowerType) {
        this.borrowerType = borrowerType;
    }
    /**
     * Gets the barcode attribute.
     * @return  Returns the barcode.
     */
    public String getBarcode() {
        return barcode;
    }
    /**
     * Sets the barcode attribute value.
     * @param barcode The barcode to set.
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    /**
     * Gets the postalAddresses attribute.
     * @return  Returns the postalAddresses.
     */
    public List<OlePatronPostalAddress> getPostalAddresses() {
        return postalAddresses;
    }
    /**
     * Sets the postalAddresses attribute value.
     * @param postalAddress The postalAddresses to set.
     */
    public void setPostalAddresses(List<OlePatronPostalAddress> postalAddress) {
        this.postalAddresses = postalAddress;
    }
    /**
     * Gets the telephoneNumbers attribute.
     * @return  Returns the telephoneNumbers.
     */
    public List<OlePatronTelePhoneNumber> getTelephoneNumbers() {
        return telephoneNumbers;
    }
    /**
     * Sets the telephoneNumbers attribute value.
     * @param telephoneNumbers The telephoneNumbers to set.
     */
    public void setTelephoneNumbers(List<OlePatronTelePhoneNumber> telephoneNumbers) {
        this.telephoneNumbers = telephoneNumbers;
    }
    /**
     * Gets the emailAddresses attribute.
     * @return  Returns the emailAddresses.
     */
    public List<OlePatronEmailAddress> getEmailAddresses() {
        return emailAddresses;
    }
    /**
     * Sets the  emailAddresses attribute value.
     * @param emailAddresses The emailAddresses to set.
     */
    public void setEmailAddresses(List<OlePatronEmailAddress> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }
    /**
     * Gets the patronLevelPolicies attribute.
     * @return  Returns the patronLevelPolicies.
     */
    public OlePatronLevelPolicies getPatronLevelPolicies() {
        return patronLevelPolicies;
    }
    /**
     * Sets the patronLevelPolicies attribute value.
     * @param patronLevelPolicies The patronLevelPolicies to set.
     */
    public void setPatronLevelPolicies(OlePatronLevelPolicies patronLevelPolicies) {
        this.patronLevelPolicies = patronLevelPolicies;
    }
    /**
     * Gets the notes attribute.
     * @return  Returns the notes.
     */
    public List<OlePatronNote> getNotes() {
        return notes;
    }
    /**
     * Sets the notes attribute value.
     * @param notes The notes to set.
     */
    public void setNotes(List<OlePatronNote> notes) {
        this.notes = notes;
    }
    /**
     * Gets the errorMessage attribute.
     * @return  returns the errorMessage.
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    /**
     * Sets the errorMessage attribute value.
     * @param errorMessage The errorMessage to set.
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    /**
     * Gets the affiliations attribute.
     * @return  returns the affiliations
     */
    public List<OlePatronAffiliations> getAffiliations() {
        return affiliations;
    }
    /**
     * Sets the affiliations attribute value.
     * @param affiliations The affiliations to set.
     */
    public void setAffiliations(List<OlePatronAffiliations> affiliations) {
        this.affiliations = affiliations;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatisticalCategory() {
        return statisticalCategory;
    }

    public void setStatisticalCategory(String statisticalCategory) {
        this.statisticalCategory = statisticalCategory;
    }

    public List<OlePatronLocalIdentification> getLocalIdentifications() {
        return localIdentifications;
    }

    public void setLocalIdentifications(List<OlePatronLocalIdentification> localIdentifications) {
        this.localIdentifications = localIdentifications;
    }
}
