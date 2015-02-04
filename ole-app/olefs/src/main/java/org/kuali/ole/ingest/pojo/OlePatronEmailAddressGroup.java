package org.kuali.ole.ingest.pojo;

import java.util.List;

/**
 * OlePatronEmailAddressGroup is a business object class for Ole Patron Email Address Group Document
 */
public class OlePatronEmailAddressGroup {

    private List<OlePatronEmailAddress> emailAddress;
    /**
     * Gets the emailAddress attribute.
     * @return  Returns the emailAddress.
     */
    public List<OlePatronEmailAddress> getEmailAddress() {
        return emailAddress;
    }
    /**
     * Sets the emailAddress attribute value.
     * @param emailAddress The emailAddress to set.
     */
    public void setEmailAddress(List<OlePatronEmailAddress> emailAddress) {
        this.emailAddress = emailAddress;
    }
}
