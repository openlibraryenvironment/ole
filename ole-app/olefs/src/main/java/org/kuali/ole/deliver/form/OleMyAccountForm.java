package org.kuali.ole.deliver.form;

import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OleRenewalLoanDocument;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.List;

/**
 * The OleLoanForm is the form class that defines all the loan fields required for a loan processing using getters and setters
 * and is involved in passing the data to the UI layer
 */
public class OleMyAccountForm extends UifFormBase {

    private String patronBarcode;
    private String patronName;
    private String patronId;
    private String patronPassword;
    private String borrowerType;
    private String borrowerTypeId;
    private String information;
    private String item;

    private String preferredAddress;
    private String email;
    private String phoneNumber;
    private List<OleRenewalLoanDocument> existingLoanList;
    private boolean validPatronFlag = false;

    // Patron details
    private OlePatronDocument olePatronDocument;
    private String message;
    private String barcode;
    private String myAccProxyFirstName;
    private String myAccProxyLastName;
    private String myAccProxyBarcode;
    private boolean showReturnMyAccount;

    public String getMyAccProxyFirstName() {
        return myAccProxyFirstName;
    }

    public void setMyAccProxyFirstName(String myAccProxyFirstName) {
        this.myAccProxyFirstName = myAccProxyFirstName;
    }

    public String getMyAccProxyLastName() {
        return myAccProxyLastName;
    }

    public void setMyAccProxyLastName(String myAccProxyLastName) {
        this.myAccProxyLastName = myAccProxyLastName;
    }

    public String getMyAccProxyBarcode() {
        return myAccProxyBarcode;
    }

    public void setMyAccProxyBarcode(String myAccProxyBarcode) {
        this.myAccProxyBarcode = myAccProxyBarcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isShowReturnMyAccount() {
        return showReturnMyAccount;
    }

    public void setShowReturnMyAccount(boolean showReturnMyAccount) {
        this.showReturnMyAccount = showReturnMyAccount;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public OlePatronDocument getOlePatronDocument() {
        return olePatronDocument;
    }

    public void setOlePatronDocument(OlePatronDocument olePatronDocument) {
        this.olePatronDocument = olePatronDocument;
    }

    public boolean isValidPatronFlag() {
        return validPatronFlag;
    }

    public void setValidPatronFlag(boolean validPatronFlag) {
        this.validPatronFlag = validPatronFlag;
    }

    public OleMyAccountForm() {

        existingLoanList = new ArrayList<OleRenewalLoanDocument>();
    }


    public String getPatronPassword() {
        return patronPassword;
    }

    public void setPatronPassword(String patronPassword) {
        this.patronPassword = patronPassword;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getPreferredAddress() {
        return preferredAddress;
    }

    public void setPreferredAddress(String preferredAddress) {
        this.preferredAddress = preferredAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBorrowerTypeId() {
        return borrowerTypeId;
    }

    public void setBorrowerTypeId(String borrowerTypeId) {
        this.borrowerTypeId = borrowerTypeId;
    }

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public String getPatronName() {
        return patronName;
    }

    public void setPatronName(String patronName) {
        this.patronName = patronName;
    }

    public String getBorrowerType() {
        return borrowerType;
    }

    public void setBorrowerType(String borrowerType) {
        this.borrowerType = borrowerType;
    }

    public String getPatronBarcode() {
        return patronBarcode;
    }

    public void setPatronBarcode(String patronBarcode) {
        this.patronBarcode = patronBarcode;
    }

    public List<OleRenewalLoanDocument> getExistingLoanList() {
        return existingLoanList;
    }

    public void setExistingLoanList(List<OleRenewalLoanDocument> existingLoanList) {
        this.existingLoanList = existingLoanList;
    }
}
