package org.kuali.ole.deliver.bo;


import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: balakumaranm
 * Date: 4/26/13
 * Time: 7:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleItemLevelBillPayment extends PersistableBusinessObjectBase {

    private String paymentId;
    private String lineItemId;
    private KualiDecimal amount;
    private Timestamp paymentDate;
    private String createdUser;
    private String paymentMode;
    private String transactionNumber;
    private String transactionNote;
    private String note;
    private FeeType feeType = new FeeType();

    /**
     * @return
     */
    public String getPaymentId() {
        return paymentId;
    }

    /**
     * @param paymentId
     */
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    /**
     * @return
     */
    public KualiDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount
     */
    public void setAmount(KualiDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return
     */
    public String getLineItemId() {
        return lineItemId;
    }

    /**
     * @param lineItemId
     */
    public void setLineItemId(String lineItemId) {
        this.lineItemId = lineItemId;
    }

    /**
     * @return
     */
    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    /**
     * @param paymentDate
     */
    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * @return
     */
    public String getCreatedUser() {
        return createdUser;
    }

    /**
     * @param createdUser
     */
    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getTransactionNote() {
        return transactionNote;
    }

    public void setTransactionNote(String transactionNote) {
        this.transactionNote = transactionNote;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
}
