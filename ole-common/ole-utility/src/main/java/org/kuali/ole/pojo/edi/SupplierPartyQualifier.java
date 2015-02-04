package org.kuali.ole.pojo.edi;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/6/12
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class SupplierPartyQualifier {
    private String supplier;
    private SupplierInformation supplierInformation;


    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public SupplierInformation getSupplierInformation() {
        return supplierInformation;
    }

    public void setSupplierInformation(SupplierInformation supplierInformation) {
        this.supplierInformation = supplierInformation;
    }
}
