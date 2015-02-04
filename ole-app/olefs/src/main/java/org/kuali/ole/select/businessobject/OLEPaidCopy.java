package org.kuali.ole.select.businessobject;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: juliyamonicas
 * Date: 12/20/13
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEPaidCopy  extends PersistableBusinessObjectBase{
    private Integer olePaidCopyId;
    private Integer copyId;
    private Integer invoiceItemId;
    private Integer invoiceIdentifier;
    private Integer paymentRequestItemId;
    private Integer paymentRequestIdentifier;

    private OleInvoiceItem invoiceItem;
    private OlePaymentRequestItem preqItem;
    private OleCopy oleCopy;

    public Integer getOlePaidCopyId() {
        return olePaidCopyId;
    }

    public void setOlePaidCopyId(Integer olePaidCopyId) {
        this.olePaidCopyId = olePaidCopyId;
    }

    public Integer getCopyId() {
        return copyId;
    }

    public void setCopyId(Integer copyId) {
        this.copyId = copyId;
    }

    public Integer getInvoiceItemId() {
        return invoiceItemId;
    }

    public void setInvoiceItemId(Integer invoiceItemId) {
        this.invoiceItemId = invoiceItemId;
    }

    public Integer getInvoiceIdentifier() {
        return invoiceIdentifier;
    }

    public void setInvoiceIdentifier(Integer invoiceIdentifier) {
        this.invoiceIdentifier = invoiceIdentifier;
    }

    public Integer getPaymentRequestItemId() {
        return paymentRequestItemId;
    }

    public void setPaymentRequestItemId(Integer paymentRequestItemId) {
        this.paymentRequestItemId = paymentRequestItemId;
    }

    public Integer getPaymentRequestIdentifier() {
        return paymentRequestIdentifier;
    }

    public void setPaymentRequestIdentifier(Integer paymentRequestIdentifier) {
        this.paymentRequestIdentifier = paymentRequestIdentifier;
    }

    public OleInvoiceItem getInvoiceItem() {
        return invoiceItem;
    }

    public void setInvoiceItem(OleInvoiceItem invoiceItem) {
        this.invoiceItem = invoiceItem;
    }

    public OlePaymentRequestItem getPreqItem() {
        return preqItem;
    }

    public void setPreqItem(OlePaymentRequestItem preqItem) {
        this.preqItem = preqItem;
    }

    public OleCopy getOleCopy() {
        return oleCopy;
    }

    public void setOleCopy(OleCopy oleCopy) {
        this.oleCopy = oleCopy;
    }
}
