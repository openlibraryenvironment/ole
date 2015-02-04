package org.kuali.ole.deliver.form;

import org.kuali.ole.deliver.bo.PatronBillReviewDocument;
import org.kuali.rice.krad.web.form.DocumentFormBase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/6/12
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class PatronBillReviewForm extends DocumentFormBase {

    private boolean select;
    private boolean reviewed;
    private String itemBarcode;
    private String billNumber;
    private Date billDate;
    private String patronId;
    private String feeType;
    private BigDecimal feeAmount;
    private String paymentStatus;
    private BigDecimal paymentAmount;
    private String firstName;
    private String lastName;
    private BigDecimal totalAmount;
    List<PatronBillReviewDocument> patronBillReviewDocumentList = new ArrayList<PatronBillReviewDocument>();
    List<PatronBillReviewForm> patronBillReviewFormList = new ArrayList<PatronBillReviewForm>();
    List<PatronBillReviewForm> patronBillReviewedFormList = new ArrayList<PatronBillReviewForm>();


    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public List<PatronBillReviewForm> getPatronBillReviewFormList() {
        return patronBillReviewFormList;
    }

    public void setPatronBillReviewFormList(List<PatronBillReviewForm> patronBillReviewFormList) {
        this.patronBillReviewFormList = patronBillReviewFormList;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
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

    public List<PatronBillReviewDocument> getPatronBillReviewDocumentList() {
        return patronBillReviewDocumentList;
    }

    public void setPatronBillReviewDocumentList(List<PatronBillReviewDocument> patronBillReviewDocumentList) {
        this.patronBillReviewDocumentList = patronBillReviewDocumentList;
    }

    public List<PatronBillReviewForm> getPatronBillReviewedFormList() {
        return patronBillReviewedFormList;
    }

    public void setPatronBillReviewedFormList(List<PatronBillReviewForm> patronBillReviewedFormList) {
        this.patronBillReviewedFormList = patronBillReviewedFormList;
    }

}
