package org.kuali.ole.ingest.pojo;

import java.util.List;

/**
 * OlePatronAffiliations is a business object class for Ole Patron Affiliation Document
 */
public class OlePatronAffiliations {

    private String affiliationType;
    private String campusCode;
    private List<OlePatronEmployments> employments;
    private boolean active;



    /**
     * Gets the affiliationType attribute.
     * @return  Returns the affiliationType.
     */
    public String getAffiliationType() {
        return affiliationType;
    }
    /**
            * Sets the affiliationType attribute value.
    * @param affiliationType The affiliationType to set.
    */
    public void setAffiliationType(String affiliationType) {
        this.affiliationType = affiliationType;
    }
    /**
     * Gets the campusCode attribute.
     * @return  Returns the campusCode.
     */
    public String getCampusCode() {
        return campusCode;
    }
    /**
     * Sets the campusCode attribute value.
     * @param campusCode The campusCode to set.
     */
    public void setCampusCode(String campusCode) {
        this.campusCode = campusCode;
    }
    /**
     * Gets the employments attribute.
     * @return  Returns the employments.
     */
    public List<OlePatronEmployments> getEmployments() {
        return employments;
    }
    /**
     * Sets the employments attribute value.
     * @param employments The employments to set.
     */
    public void setEmployments(List<OlePatronEmployments> employments) {
        this.employments = employments;
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

}
