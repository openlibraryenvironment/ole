package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 7/28/13
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEInvoiceBo extends PersistableBusinessObjectBase {
    private Date invoiceDate;
    private String invoiceNumber;
    private String invoiceNbr;
    private String documentNumber;

    public String getInvoiceNbr() {
        return invoiceNbr;
    }

    public void setInvoiceNbr(String invoiceNbr) {
        this.invoiceNbr = invoiceNbr;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
}
