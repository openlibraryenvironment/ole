package org.kuali.ole.ingest.pojo;

/**
 * OlePatronEmailAddress is a business object class for Ole Patron Email Address Document
 */
public class OlePatronEmailAddress {

    private String emailAddressType;
    private String emailAddress;
    private boolean defaults;
    private boolean active;
    /**
     * Gets the emailAddressType attribute.
     * @return  Returns the emailAddressType.
     */
    public String getEmailAddressType() {
        return emailAddressType;
    }
    /**
     * Sets the emailAddressType attribute value.
     * @param emailAddressType The emailAddressType to set.
     */
    public void setEmailAddressType(String emailAddressType) {
        this.emailAddressType = emailAddressType;
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
     * Gets the emailAddress attribute.
     * @return  Returns the emailAddress.
     */
    public String getEmailAddress() {
        return emailAddress;
    }
    /**
     * Sets the emailAddress attribute value.
     * @param emailAddress The emailAddress to set.
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
