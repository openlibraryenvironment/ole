package org.kuali.ole.ingest.pojo;

import java.sql.Date;
import java.util.List;

/**
 * OlePatronPostalAddress is a business object class for Ole Patron Postal Address Document
 */
public class OlePatronPostalAddress {
    private String postalAddressType;
    private List<OleAddressLine> addressLinesList;
    private String country;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String addressSource;
    private Date addressValidTo;
    private Date addressValidFrom;
    private boolean addressVerified;
    private boolean defaults;
    private boolean active;
    /**
     * Gets the postalAddressType attribute.
     * @return  Returns the postalAddressType.
     */
    public String getPostalAddressType() {
        return postalAddressType;
    }
    /**
     * Sets the postalAddressType attribute value.
     * @param postalAddressType The postalAddressType to set.
     */
    public void setPostalAddressType(String postalAddressType) {
        this.postalAddressType = postalAddressType;
    }
    /**
     * Gets the addressLinesList attribute.
     * @return  Returns the addressLinesList.
     */
    public List<OleAddressLine> getAddressLinesList() {
        return addressLinesList;
    }
    /**
     * Sets the addressLinesList attribute value.
     * @param addressLinesList The addressLinesList to set.
     */
    public void setAddressLinesList(List<OleAddressLine> addressLinesList) {
        this.addressLinesList = addressLinesList;
    }
    /**
     * Gets the country attribute.
     * @return  Returns the country.
     */
    public String getCountry() {
        return country;
    }
    /**
     * Sets the country attribute value.
     * @param country The country to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }
    /**
     * Gets the city attribute.
     * @return  Returns the city.
     */
    public String getCity() {
        return city;
    }
    /**
     * Sets the city attribute value.
     * @param city The city to set.
     */
    public void setCity(String city) {
        this.city = city;
    }
    /**
     * Gets the stateProvince attribute.
     * @return  Returns the stateProvince.
     */
    public String getStateProvince() {
        return stateProvince;
    }
    /**
     * Sets the stateProvince attribute value.
     * @param stateProvince The stateProvince to set.
     */
    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }
    /**
     * Gets the postalCode attribute.
     * @return  Returns the postalCode.
     */
    public String getPostalCode() {
        return postalCode;
    }
    /**
     * Sets the postalCode attribute value.
     * @param postalCode The postalCode to set.
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
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
     * Gets the defaults attribute.
     * @return  Returns the defaults.
     */
    public boolean isDefaults() {
        return defaults;
    }
    /**
     * Sets the defaults attribute value.
     * @param defaults The defaults to set.
     */
    public void setDefaults(boolean defaults) {
        this.defaults = defaults;
    }
    /**
     * Gets the addressValidTo attribute.
     * @return  Returns the addressValidTo.
     */
    public Date getAddressValidTo() {
        return addressValidTo;
    }
    /**
     * Sets the addressValidTo attribute value.
     * @param addressValidTo The addressValidTo to set.
     */
    public void setAddressValidTo(Date addressValidTo) {
        this.addressValidTo = addressValidTo;
    }
    /**
     * Gets the addressValidFrom attribute.
     * @return  Returns the addressValidFrom.
     */
    public Date getAddressValidFrom() {
        return addressValidFrom;
    }
    /**
     * Sets the addressValidFrom attribute value.
     * @param addressValidFrom The addressValidFrom to set.
     */
    public void setAddressValidFrom(Date addressValidFrom) {
        this.addressValidFrom = addressValidFrom;
    }
    /**
     * Gets the addressVerified attribute.
     * @return  Returns the addressVerified.
     */
    public boolean isAddressVerified() {
        return addressVerified;
    }


    /**
     * Sets the addressVerified attribute value.
     * @param addressVerified The addressVerified to set.
     */
    public void setAddressVerified(boolean addressVerified) {
        this.addressVerified = addressVerified;
    }

    public String getAddressSource() {
        return addressSource;
    }

    public void setAddressSource(String addressSource) {
        this.addressSource = addressSource;
    }
}
