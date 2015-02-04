package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/11/12
 * Time: 12:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class OlePatronMergeDocument extends PersistableBusinessObjectBase {

    private String barcode;
    private String firstName;
    private String lastName;
    private String patronType;
    private boolean checkSurvivor;
    private boolean checkDuplicatePatron;
    private OlePatronDocument olePatronDocument;

    /**
     * Gets the value of checkSurvivor property
     *
     * @return checkSurvivor
     */
    public boolean isCheckSurvivor() {
        return checkSurvivor;
    }

    public void setCheckSurvivor(boolean checkSurvivor) {
        this.checkSurvivor = checkSurvivor;
    }

    /**
     * Gets the value of checkDuplicatePatron property
     *
     * @return checkDuplicatePatron
     */
    public boolean isCheckDuplicatePatron() {
        return checkDuplicatePatron;
    }

    /**
     * Sets the value for documents property
     *
     * @param checkDuplicatePatron
     */
    public void setCheckDuplicatePatron(boolean checkDuplicatePatron) {
        this.checkDuplicatePatron = checkDuplicatePatron;
    }

    /**
     * Gets the value of barcode property
     *
     * @return barcode
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * Sets the value for documents property
     *
     * @param barcode
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * Gets the value of firstName property
     *
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value for documents property
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the value of lastName property
     *
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value for documents property
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the value of patronType property
     *
     * @return patronType
     */
    public String getPatronType() {
        return patronType;
    }

    /**
     * Sets the value for documents property
     *
     * @param patronType
     */
    public void setPatronType(String patronType) {
        this.patronType = patronType;
    }


    public OlePatronMergeDocument(OlePatronDocument olePatronDocument) {
        this.setOlePatronDocument(olePatronDocument);
        this.setFirstName(olePatronDocument.getEntity().getNames().get(0).getFirstName());
        this.setLastName(olePatronDocument.getEntity().getNames().get(0).getLastName());
        this.setBarcode(olePatronDocument.getBarcode());
        this.setPatronType(olePatronDocument.getBorrowerTypeName());
        this.setCheckSurvivor(false);
        this.setCheckDuplicatePatron(false);
    }

    /**
     * Gets the value of olePatronDocument property
     *
     * @return olePatronDocument
     */
    public OlePatronDocument getOlePatronDocument() {
        return olePatronDocument;
    }

    /**
     * Sets the value for documents property
     *
     * @param olePatronDocument
     */
    public void setOlePatronDocument(OlePatronDocument olePatronDocument) {
        this.olePatronDocument = olePatronDocument;
    }

    public OlePatronMergeDocument() {
    }
}
