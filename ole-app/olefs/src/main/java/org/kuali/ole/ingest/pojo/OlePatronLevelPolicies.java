package org.kuali.ole.ingest.pojo;

/**
 * OlePatronLevelPolicies is a business object class for Ole Patron Level Policies Document
 */
public class OlePatronLevelPolicies {

    private boolean  isGenerallyBlocked;
    private boolean hasDeliveryPrivilege;
    private String generalBlockNotes;
    private boolean hasPagingPrivilege;
    private boolean receivesCourtesyNotice;
    /**
     * Gets the generallyBlocked attribute.
     * @return  Returns the generallyBlocked.
     */
    public boolean isGenerallyBlocked() {
        return isGenerallyBlocked;
    }
    /**
     * Sets the generallyBlocked attribute value.
     * @param generallyBlocked The generallyBlocked to set.
     */
    public void setGenerallyBlocked(boolean generallyBlocked) {
        isGenerallyBlocked = generallyBlocked;
    }
    /**
     * Gets the hasDeliveryPrivilege attribute.
     * @return  Returns the hasDeliveryPrivilege.
     */
    public boolean isHasDeliveryPrivilege() {
        return hasDeliveryPrivilege;
    }
    /**
     * Sets the hasDeliveryPrivilege attribute value.
     * @param hasDeliveryPrivilege The hasDeliveryPrivilege to set.
     */
    public void setHasDeliveryPrivilege(boolean hasDeliveryPrivilege) {
        this.hasDeliveryPrivilege = hasDeliveryPrivilege;
    }
    /**
     * Gets the hasPagingPrivilege attribute.
     * @return  Returns the hasPagingPrivilege.
     */
    public boolean isHasPagingPrivilege() {
        return hasPagingPrivilege;
    }
    /**
     * Sets the hasPagingPrivilege attribute value.
     * @param hasPagingPrivilege The hasPagingPrivilege to set.
     */
    public void setHasPagingPrivilege(boolean hasPagingPrivilege) {
        this.hasPagingPrivilege = hasPagingPrivilege;
    }
    /**
     * Gets the receivesCourtesyNotice attribute.
     * @return  Returns the receivesCourtesyNotice.
     */
    public boolean isReceivesCourtesyNotice() {
        return receivesCourtesyNotice;
    }
    /**
     * Sets the receivesCourtesyNotice attribute value.
     * @param receivesCourtesyNotice The receivesCourtesyNotice to set.
     */
    public void setReceivesCourtesyNotice(boolean receivesCourtesyNotice) {
        this.receivesCourtesyNotice = receivesCourtesyNotice;
    }
    /**
     * Gets the generalBlockNotes attribute.
     * @return  Returns the generalBlockNotes.
     */
    public String getGeneralBlockNotes() {
        return generalBlockNotes;
    }
    /**
     * Sets the generalBlockNotes attribute value.
     * @param generalBlockNotes The generalBlockNotes to set.
     */
    public void setGeneralBlockNotes(String generalBlockNotes) {
        this.generalBlockNotes = generalBlockNotes;
    }
}
