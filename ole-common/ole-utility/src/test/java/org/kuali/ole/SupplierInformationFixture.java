package org.kuali.ole;

import org.kuali.ole.pojo.edi.SupplierInformation;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/8/12
 * Time: 7:22 PM
 * To change this template use File | Settings | File Templates.
 */
public enum SupplierInformationFixture {
    SupplierInformation("HAR2",
            null,
            "ZZ"
    ),;

    private String supplierCodeIdentification;
    private String supplierPartyIdentificationCode;
    private String supplierCodeListAgency;

    private SupplierInformationFixture(String supplierCodeIdentification, String supplierPartyIdentificationCode,
                                       String supplierCodeListAgency) {
        this.supplierCodeIdentification = supplierCodeIdentification;
        this.supplierPartyIdentificationCode = supplierPartyIdentificationCode;
        this.supplierCodeListAgency = supplierCodeListAgency;
    }

    public SupplierInformation createSupplierInformation(Class clazz) {
        SupplierInformation supplierInformation = null;
        try {
            supplierInformation = (SupplierInformation) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("SupplierInformation creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("SupplierInformation creation failed. class = " + clazz);
        }
        supplierInformation.setSupplierCodeIdentification(supplierCodeIdentification);
        supplierInformation.setSupplierPartyIdentificationCode(supplierPartyIdentificationCode);
        supplierInformation.setSupplierCodeListAgency(supplierCodeListAgency);
        return supplierInformation;
    }

}
