package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.BusinessObjectBase;

/**
 * OleAgreementSearch is the business object class for Agreement Search.
 */
public class OleAgreementSearch extends BusinessObjectBase {
    /**
     * Stores the agreementTitle
     */
    private String agreementTitle;
    /**
     * Stores the contractNumber
     */
    private String contractNumber;
    /**
     * Stores the licensee
     */
    private String licensee;
    /**
     * Stores the licensor
     */
    private String licensor;

    private String uniqueId;

    private String methodName;

    private String status;

    private String type;

    /**
     * @return agreementTitle
     */
    public String getAgreementTitle() {
        return agreementTitle;
    }

    public void setAgreementTitle(String agreementTitle) {
        this.agreementTitle = agreementTitle;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getLicensee() {
        return licensee;
    }

    public void setLicensee(String licensee) {
        this.licensee = licensee;
    }

    public String getLicensor() {
        return licensor;
    }

    public void setLicensor(String licensor) {
        this.licensor = licensor;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public void refresh() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
