package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.BusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: JuliyaMonica.S
 * Date: 8/27/12
 * Time: 8:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleAgreementMetadata extends BusinessObjectBase {

    private String agreementDate;
    private String agreementUser;
    private String agreementStatus;
    private String agreementType;
    private String agreementMd;

    public String getAgreementDate() {
        return agreementDate;
    }

    public void setAgreementDate(String agreementDate) {
        this.agreementDate = agreementDate;
    }

    public String getAgreementUser() {
        return agreementUser;
    }

    public void setAgreementUser(String agreementUser) {
        this.agreementUser = agreementUser;
    }

    public String getAgreementStatus() {
        return agreementStatus;
    }

    public void setAgreementStatus(String agreementStatus) {
        this.agreementStatus = agreementStatus;
    }

    public String getAgreementType() {
        return agreementType;
    }

    public void setAgreementType(String agreementType) {
        this.agreementType = agreementType;
    }

    public String getAgreementMd() {
        return agreementMd;
    }

    public void setAgreementMd(String agreementMd) {
        this.agreementMd = agreementMd;
    }

    @Override
    public void refresh() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isPresent() {
        return (this.agreementDate != null || this.agreementUser != null || this.agreementStatus != null ||
                this.agreementType != null || this.agreementMd != null) ? true : false;
    }
}
