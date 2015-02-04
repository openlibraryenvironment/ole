package org.kuali.ole.deliver.form;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.FeeType;
import org.kuali.ole.deliver.bo.OleItemLevelBillPayment;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.PatronBillPayment;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.web.form.DocumentFormBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/17/12
 * Time: 9:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class PatronBillForm extends DocumentFormBase {

    private String patronId;
    private String paymentMethod;
    private KualiDecimal paymentAmount;
    private String message;
    private String freeTextNote;
    private OlePatronDocument olePatronDocument;
    private String billWisePayment = "default";
    private boolean printFlag = true;
    private List<FeeType> feeTypes;
    private List<PatronBillPayment> patronBillPaymentList;
    private boolean printBillReview;
    private String forgiveNote;
    private String errorNote;
    private KualiDecimal grandTotal = new KualiDecimal("0");
    private KualiDecimal patronAmount;
    private String olePatronId;
    private boolean doubleSubmit;
    private boolean modifyBill;
    private String transactionNumber;
    private String paidByUser;
    private String transactionNote;
    private String cancellationNote;
    private List<OleItemLevelBillPayment> currentSessionTransactions=new ArrayList<>();
    private String userAmount;//dummy variable used for only browser to keep the amount track

    public PatronBillForm() {
        this.userAmount=OLEConstants.ZERO;
    }

    public KualiDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(KualiDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getBillWisePayment() {
        return billWisePayment;
    }

    public void setBillWisePayment(String billWisePayment) {
        this.billWisePayment = billWisePayment;
    }

    public List<FeeType> getFeeTypes() {
        return feeTypes;
    }

    public void setFeeTypes(List<FeeType> feeTypes) {
        this.feeTypes = feeTypes;
    }

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public KualiDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(KualiDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFreeTextNote() {
        return freeTextNote;
    }

    public void setFreeTextNote(String freeTextNote) {
        this.freeTextNote = freeTextNote;
    }

    public OlePatronDocument getOlePatronDocument() {
        return olePatronDocument;
    }

    public void setOlePatronDocument(OlePatronDocument olePatronDocument) {
        this.olePatronDocument = olePatronDocument;
    }

    public List<PatronBillPayment> getPatronBillPaymentList() {
        return patronBillPaymentList;
    }

    public void setPatronBillPaymentList(List<PatronBillPayment> patronBillPaymentList) {
        this.patronBillPaymentList = patronBillPaymentList;
    }

    public boolean isPrintFlag() {
        return printFlag;
    }

    public void setPrintFlag(boolean printFlag) {
        this.printFlag = printFlag;
    }

    public boolean isPrintBillReview() {
        return printBillReview;
    }

    public void setPrintBillReview(boolean printBillReview) {
        this.printBillReview = printBillReview;
    }

    public String getForgiveNote() {
        return forgiveNote;
    }

    public void setForgiveNote(String forgiveNote) {
        this.forgiveNote = forgiveNote;
    }

    public String getErrorNote() {
        return errorNote;
    }

    public void setErrorNote(String errorNote) {
        this.errorNote = errorNote;
    }

    public KualiDecimal getPatronAmount() {
        return patronAmount;
    }

    public void setPatronAmount(KualiDecimal patronAmount) {
        this.patronAmount = patronAmount;
    }

    public String getOlePatronId() {
        return olePatronId;
    }

    public void setOlePatronId(String olePatronId) {
        this.olePatronId = olePatronId;
    }

    public boolean isDoubleSubmit() {
        return doubleSubmit;
    }

    public void setDoubleSubmit(boolean doubleSubmit) {
        this.doubleSubmit = doubleSubmit;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getPaidByUser() {
        return paidByUser;
    }

    public void setPaidByUser(String paidByUser) {
        this.paidByUser = paidByUser;
    }

    public String getTransactionNote() {
        return transactionNote;
    }

    public void setTransactionNote(String transactionNote) {
        this.transactionNote = transactionNote;
    }

    public boolean isModifyBill() {
        return modifyBill;
    }

    public void setModifyBill(boolean modifyBill) {
        this.modifyBill = modifyBill;
    }

    public List<OleItemLevelBillPayment> getCurrentSessionTransactions() {
        return currentSessionTransactions;
    }

    public void setCurrentSessionTransactions(List<OleItemLevelBillPayment> currentSessionTransactions) {
        this.currentSessionTransactions = currentSessionTransactions;
    }

    public String getCancellationNote() {
        return cancellationNote;
    }

    public void setCancellationNote(String cancellationNote) {
        this.cancellationNote = cancellationNote;
    }

    public String getUserAmount() {
        return userAmount;
    }

    public void setUserAmount(String userAmount) {
        this.userAmount = userAmount;
    }
}
