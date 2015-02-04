package org.kuali.ole.ingest.pojo;

import java.util.List;

/**
 * OlePatronPostalAddressGroup is a business object class for Ole Patron Postal Address Group Document
 */
public class OlePatronPostalAddressGroup {

    private List<OlePatronPostalAddress> postalAddress;

    /**
     * Gets the postalAddress attribute
     * @return  Returns the postalAddress.
     */
    public List<OlePatronPostalAddress> getPostalAddress() {
        return postalAddress;
    }

    /**
     * Sets the postalAddress attribute value.
     * @param postalAddress The postalAddress to set.
     */
    public void setPostalAddress(List<OlePatronPostalAddress> postalAddress) {
        this.postalAddress = postalAddress;
    }
}
