package org.kuali.ole.ingest.pojo;

import java.util.List;

/**
 * OlePatronTelephoneNumberGroup is a business object class for Ole Patron Telephone Number Group Document
 */
public class OlePatronTelephoneNumberGroup {

    private List<OlePatronTelePhoneNumber> telephoneNumber;

    /**
     * Gets the telephoneNumber attribute
     * @return  Returns the telephoneNumbers.
     */
    public List<OlePatronTelePhoneNumber> getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * Sets the telephoneNumber attribute value.
     * @param telephoneNumber .The telephoneNumbers to set.
     */
    public void setTelephoneNumber(List<OlePatronTelePhoneNumber> telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
}
