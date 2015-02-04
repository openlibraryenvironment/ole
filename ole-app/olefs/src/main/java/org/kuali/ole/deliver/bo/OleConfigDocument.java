package org.kuali.ole.deliver.bo;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/15/12
 * Time: 8:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleConfigDocument {
    private String documentName;
    private String attribute;

    /**
     * Gets the value of documentName property
     *
     * @return documentName
     */
    public String getDocumentName() {
        return documentName;
    }

    /**
     * Sets the value for olePatronId property
     *
     * @param documentName
     */
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    /**
     * Gets the value of attribute property
     *
     * @return attribute
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * Sets the value for olePatronId property
     *
     * @param attribute
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}
