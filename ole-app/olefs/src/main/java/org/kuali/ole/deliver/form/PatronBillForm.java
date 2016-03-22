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
    private String patronUrl;
    private String creditNote;
    private KualiDecimal creditRemaining  = new KualiDecimal(0);
    private String transferNote;
    private String transferDebitNote;
    private String transferCreditNote;
    private KualiDecimal paidAmount;
    private KualiDecimal transferAmount;
    private String refundNote;
    private String refundType;
    private KualiDecimal totalCreditRemaining;
    private String userPaidAmount;
    private String creditRemainingAmt;
    private String defaultPatronAddress;
    private String userEnteredPatronAddress;
    private String patronAddressType;
    private String cancelCreditNote;
    private String patronAddress;
    private List<FeeType> closedFeeTypes;
    private List<FeeType> openFeeTypes;
    private KualiDecimal refundAmountToPatron;
    private AmountDetails amountDetails;

    public PatronBillForm() {
        this.userAmount=OLEConstants.ZERO;
        this.userPaidAmount =OLEConstants.ZERO;
        this.creditRemainingAmt =OLEConstants.ZERO;
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

    public String getPatronUrl() {
        return patronUrl;
    }

    public void setPatronUrl(String patronUrl) {
        this.patronUrl = patronUrl;
    }

    public String getCreditNote() {
        return creditNote;
}

    public void setCreditNote(String creditNote) {
        this.creditNote = creditNote;
    }

    public KualiDecimal getCreditRemaining() {
        return creditRemaining;
    }

    public void setCreditRemaining(KualiDecimal creditRemaining) {
        this.creditRemaining = creditRemaining;
    }

    public String getTransferNote() {
        return transferNote;
    }

    public void setTransferNote(String transferNote) {
        this.transferNote = transferNote;
    }

    public String getTransferDebitNote() {
        if(transferNote != null) {
            return transferNote;
        }
        return transferDebitNote;
    }

    public void setTransferDebitNote(String transferDebitNote) {
        this.transferDebitNote = transferDebitNote;
    }

    public String getTransferCreditNote() {
        if(transferNote != null) {
            return transferNote;
        }
        return transferCreditNote   ;
    }

    public void setTransferCreditNote(String transferCreditNote) {
        this.transferCreditNote = transferCreditNote;
    }

    public KualiDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(KualiDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public KualiDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(KualiDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getRefundNote() {
        return refundNote;
    }

    public void setRefundNote(String refundNote) {
        this.refundNote = refundNote;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }

    public KualiDecimal getTotalCreditRemaining() {
        return totalCreditRemaining;
    }

    public void setTotalCreditRemaining(KualiDecimal totalCreditRemaining) {
        this.totalCreditRemaining = totalCreditRemaining;
    }

    public String getUserPaidAmount() {
        return userPaidAmount;
    }

    public void setUserPaidAmount(String userPaidAmount) {
        this.userPaidAmount = userPaidAmount;
    }

    public String getCreditRemainingAmt() {
        return creditRemainingAmt;
    }

    public void setCreditRemainingAmt(String creditRemainingAmt) {
        this.creditRemainingAmt = creditRemainingAmt;
    }


    public String getUserEnteredPatronAddress() {
        return userEnteredPatronAddress;
    }

    public void setUserEnteredPatronAddress(String userEnteredPatronAddress) {
        this.userEnteredPatronAddress = userEnteredPatronAddress;
    }

    public String getDefaultPatronAddress() {
        return defaultPatronAddress;
    }

    public void setDefaultPatronAddress(String defaultPatronAddress) {
        this.defaultPatronAddress = defaultPatronAddress;
    }

    public String getPatronAddressType() {
        return patronAddressType;
    }

    public void setPatronAddressType(String patronAddressType) {
        this.patronAddressType = patronAddressType;
    }

    public String getCancelCreditNote() {
        return cancelCreditNote;
    }

    public void setCancelCreditNote(String cancelCreditNote) {
        this.cancelCreditNote = cancelCreditNote;
    }

    public String getPatronAddress() {
        return patronAddress;
    }

    public void setPatronAddress(String patronAddress) {
        this.patronAddress = patronAddress;
    }

    public List<FeeType> getClosedFeeTypes() {
        return closedFeeTypes;
    }

    public void setClosedFeeTypes(List<FeeType> closedFeeTypes) {
        this.closedFeeTypes = closedFeeTypes;
    }

    public List<FeeType> getOpenFeeTypes() {
        return openFeeTypes;
    }

    public void setOpenFeeTypes(List<FeeType> openFeeTypes) {
        this.openFeeTypes = openFeeTypes;
    }

    public KualiDecimal getRefundAmountToPatron() {
        return refundAmountToPatron;
    }

    public void setRefundAmountToPatron(KualiDecimal refundAmountToPatron) {
        this.refundAmountToPatron = refundAmountToPatron;
    }

    public AmountDetails getAmountDetails() {
        if(null == amountDetails) {
            amountDetails = new AmountDetails();
        }
        return amountDetails;
    }

    public void setAmountDetails(AmountDetails amountDetails) {
        this.amountDetails = amountDetails;
    }

    public class AmountDetails {

        private KualiDecimal amountRemaining;

        public KualiDecimal getAmountRemaining() {
            return amountRemaining;
        }

        public void setAmountRemaining(KualiDecimal amountRemaining) {
            this.amountRemaining = amountRemaining;
        }
    }
}