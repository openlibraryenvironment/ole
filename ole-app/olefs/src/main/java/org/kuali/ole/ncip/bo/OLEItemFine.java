package org.kuali.ole.ncip.bo;

import org.kuali.ole.deliver.bo.FeeType;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.mvel2.util.Make;

import java.util.*;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 8/19/13
 * Time: 4:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEItemFine {
    private String itemId;
    private String catalogueId;
    private BigDecimal amount;
    private BigDecimal balance;
    private String billDate;
    private String noOfPayments;
    private String withItems;
    private String reason;
    private String dateCharged;
    private String feeType;
    private String paymentMethod;
    private String title;
    private String author;
   // private List<FeeType> feeType;


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCatalogueId() {
        return catalogueId;
    }

    public void setCatalogueId(String catalogueId) {
        this.catalogueId = catalogueId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getNoOfPayments() {
        return noOfPayments;
    }

    public void setNoOfPayments(String noOfPayments) {
        this.noOfPayments = noOfPayments;
    }

    public String getWithItems() {
        return withItems;
    }

    public void setWithItems(String withItems) {
        this.withItems = withItems;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDateCharged() {
        return dateCharged;
    }

    public void setDateCharged(String dateCharged) {
        this.dateCharged = dateCharged;
    }

   /* public List<FeeType> getFeeType() {
        return feeType;
    }

    public void setFeeType(List<FeeType> feeType) {
        this.feeType = feeType;
    }*/

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
