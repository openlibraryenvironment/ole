package org.kuali.ole.select.businessobject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: gopalp
 * Date: 7/23/13
 * Time: 12:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class OlePurchaseOrderTotal implements Serializable {

    private String poNumber;
    private String encumbranceAmount;
    private BigDecimal encumbranceTotalAmount;
    private BigDecimal paidAmount;

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getEncumbranceTotalAmount() {
        return encumbranceTotalAmount;
    }

    public void setEncumbranceTotalAmount(BigDecimal encumbranceTotalAmount) {
        this.encumbranceTotalAmount = encumbranceTotalAmount;
    }

    public String getEncumbranceAmount() {
        return encumbranceAmount;
    }

    public void setEncumbranceAmount(String encumbranceAmount) {
        this.encumbranceAmount = encumbranceAmount;
    }
}
