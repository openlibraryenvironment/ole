package org.kuali.ole.pojo.edi;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/6/12
 * Time: 4:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class SupplierLineItemReference {
    private String suppliersOrderLine;
    private String vendorReferenceNumber;

    public String getSuppliersOrderLine() {
        return suppliersOrderLine;
    }

    public void setSuppliersOrderLine(String suppliersOrderLine) {
        this.suppliersOrderLine = suppliersOrderLine;
    }

    public String getVendorReferenceNumber() {
        return vendorReferenceNumber;
    }

    public void setVendorReferenceNumber(String vendorReferenceNumber) {
        this.vendorReferenceNumber = vendorReferenceNumber;
    }
}
