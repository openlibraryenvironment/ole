package org.kuali.ole.pojo.edi;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/26/13
 * Time: 4:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class MonetaryLineItemInformation {
    private String amountType;
    private String amount;

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
