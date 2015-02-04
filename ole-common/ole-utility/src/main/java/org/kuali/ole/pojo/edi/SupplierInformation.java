package org.kuali.ole.pojo.edi;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/6/12
 * Time: 4:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class SupplierInformation {
    private String supplierCodeIdentification;
    private String supplierPartyIdentificationCode;
    private String supplierCodeListAgency;

    public String getSupplierCodeIdentification() {
        return supplierCodeIdentification;
    }

    public void setSupplierCodeIdentification(String supplierCodeIdentification) {
        this.supplierCodeIdentification = supplierCodeIdentification;
    }

    public String getSupplierPartyIdentificationCode() {
        return supplierPartyIdentificationCode;
    }

    public void setSupplierPartyIdentificationCode(String supplierPartyIdentificationCode) {
        this.supplierPartyIdentificationCode = supplierPartyIdentificationCode;
    }

    public String getSupplierCodeListAgency() {
        return supplierCodeListAgency;
    }

    public void setSupplierCodeListAgency(String supplierCodeListAgency) {
        this.supplierCodeListAgency = supplierCodeListAgency;
    }
}
