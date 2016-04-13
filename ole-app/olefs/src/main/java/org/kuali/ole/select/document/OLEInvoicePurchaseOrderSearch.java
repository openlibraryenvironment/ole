package org.kuali.ole.select.document;

import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: gayathria
 * Date: 7/10/13
 * Time: 7:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEInvoicePurchaseOrderSearch extends PersistableBusinessObjectBase {


    private String documentNumber;
    private Integer postingYear;
    private KualiDecimal amount;
    private String vendorNumber;
    private String purapDocumentIdentifier;
    private Integer vendorHeaderGeneratedIdentifier;
    private Integer vendorDetailAssignedIdentifier;
    private String title;
    private String author;
    private String isbn;
    private String vendorName;
    private Date poDateFrom;
    private Date poDateTo;

    public String getVendorNumber() {
        return vendorNumber;
    }

    public void setVendorNumber(String vendorNumber) {
        this.vendorNumber = vendorNumber;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Integer getPostingYear() {
        return postingYear;
    }

    public void setPostingYear(Integer postingYear) {
        this.postingYear = postingYear;
    }

    public KualiDecimal getAmount() {
        return amount;
    }

    public void setAmount(KualiDecimal amount) {
        this.amount = amount;
    }

    public String getPurapDocumentIdentifier() {
        return purapDocumentIdentifier;
    }

    public void setPurapDocumentIdentifier(String purapDocumentIdentifier) {
        this.purapDocumentIdentifier = purapDocumentIdentifier;
    }

    public Integer getVendorHeaderGeneratedIdentifier() {
        return vendorHeaderGeneratedIdentifier;
    }

    public void setVendorHeaderGeneratedIdentifier(Integer vendorHeaderGeneratedIdentifier) {
        this.vendorHeaderGeneratedIdentifier = vendorHeaderGeneratedIdentifier;
    }

    public Integer getVendorDetailAssignedIdentifier() {
        return vendorDetailAssignedIdentifier;
    }

    public void setVendorDetailAssignedIdentifier(Integer vendorDetailAssignedIdentifier) {
        this.vendorDetailAssignedIdentifier = vendorDetailAssignedIdentifier;
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

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Date getPoDateFrom() {
        return poDateFrom;
    }

    public void setPoDateFrom(Date poDateFrom) {
        this.poDateFrom = poDateFrom;
    }

    public Date getPoDateTo() {
        return poDateTo;
    }

    public void setPoDateTo(Date poDateTo) {
        this.poDateTo = poDateTo;
    }
}
