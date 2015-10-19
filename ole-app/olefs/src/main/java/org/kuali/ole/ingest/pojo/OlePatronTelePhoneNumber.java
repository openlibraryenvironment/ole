package org.kuali.ole.ingest.pojo;

/**
 * OlePatronTelePhoneNumber is a business object class for Ole Patron Telephone Number Document
 */
public class OlePatronTelePhoneNumber {

    private String telephoneNumberType;
    private String telephoneNumber;
    private String extension;
    private String country;
    private String phoneSource;
    private boolean defaults;
    private boolean active;
    /**
     * Gets the telephoneNumberType attribute.
     * @return  Returns the telephoneNumberType.
     */
    public String getTelephoneNumberType() {
        return telephoneNumberType;
    }
    /**
     * Sets the telephoneNumberType attribute value.
     * @param telephoneNumberType The telephoneNumberType to set.
     */
    public void setTelephoneNumberType(String telephoneNumberType) {
        this.telephoneNumberType = telephoneNumberType;
    }
    /**
     * Gets the extension attribute.
     * @return  Returns the extension.
     */
    public String getExtension() {
        return extension;
    }
    /**
     * Sets the extension attribute value.
     * @param extension The extension to set.
     */
    public void setExtension(String extension) {
        this.extension = extension;
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

    public String getPhoneSource() {
        return phoneSource;
    }

    public void setPhoneSource(String phoneSource) {
        this.phoneSource = phoneSource;
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
     * Gets the telephoneNumber attribute.
     * @return  Returns the telephoneNumber.
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }
    /**
     * Sets the telephoneNumber attribute value.
     * @param telephoneNumber The telephoneNumber to set.
     */
    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
}
