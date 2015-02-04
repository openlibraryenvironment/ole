package org.kuali.ole.deliver.form;

import org.kuali.ole.deliver.bo.OlePatronMergeDocument;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/11/12
 * Time: 12:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class OlePatronMergeForm extends UifFormBase {

    private OlePatronMergeDocument patronMergeDocument;
    private List<OlePatronMergeDocument> patronList;
    private String barcode;
    private String firstName;
    private String lastName;
    private String patronType;
    private boolean checkSurvivor;
    private boolean checkDyingPatron;

    public boolean isCheckSurvivor() {
        return checkSurvivor;
    }

    public void setCheckSurvivor(boolean checkSurvivor) {
        this.checkSurvivor = checkSurvivor;
    }

    public boolean isCheckDyingPatron() {
        return checkDyingPatron;
    }

    public void setCheckDyingPatron(boolean checkDyingPatron) {
        this.checkDyingPatron = checkDyingPatron;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronType() {
        return patronType;
    }

    public void setPatronType(String patronType) {
        this.patronType = patronType;
    }

    public List<OlePatronMergeDocument> getPatronList() {
        return patronList;
    }

    public void setPatronList(List<OlePatronMergeDocument> patronList) {
        this.patronList = patronList;
    }

    public OlePatronMergeDocument getPatronMergeDocument() {
        return patronMergeDocument;
    }

    public void setPatronMergeDocument(OlePatronMergeDocument patronMergeDocument) {
        this.patronMergeDocument = patronMergeDocument;
    }
}
