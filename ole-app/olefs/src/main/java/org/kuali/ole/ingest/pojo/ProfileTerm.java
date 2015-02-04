package org.kuali.ole.ingest.pojo;

/**
 * ProfileTerm is a business object class for Profile Term
 */
public class ProfileTerm {
    private String incomingField;
    private String existingField;
    /**
     * Gets the incomingField attribute.
     * @return  Returns the incomingField.
     */
    public String getIncomingField() {
        return incomingField;
    }
    /**
     * Sets the incomingField attribute value.
     * @param incomingField The incomingField to set.
     */
    public void setIncomingField(String incomingField) {
        this.incomingField = incomingField;
    }
    /**
     * Gets the existingField attribute.
     * @return  Returns the existingField.
     */
    public String getExistingField() {
        return existingField;
    }
    /**
     * Sets the existingField attribute value.
     * @param existingField The existingField to set.
     */
    public void setExistingField(String existingField) {
        this.existingField = existingField;
    }
}
