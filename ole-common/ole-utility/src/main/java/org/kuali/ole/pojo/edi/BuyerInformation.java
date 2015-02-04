package org.kuali.ole.pojo.edi;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/6/12
 * Time: 3:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class BuyerInformation {

    private String buyerCodeIdentification;
    private String buyerPartyIdentificationCode;
    private String buyerCodeListAgency;


    public String getBuyerCodeIdentification() {
        return buyerCodeIdentification;
    }

    public void setBuyerCodeIdentification(String buyerCodeIdentification) {
        this.buyerCodeIdentification = buyerCodeIdentification;
    }

    public String getBuyerPartyIdentificationCode() {
        return buyerPartyIdentificationCode;
    }

    public void setBuyerPartyIdentificationCode(String buyerPartyIdentificationCode) {
        this.buyerPartyIdentificationCode = buyerPartyIdentificationCode;
    }

    public String getBuyerCodeListAgency() {
        return buyerCodeListAgency;
    }

    public void setBuyerCodeListAgency(String buyerCodeListAgency) {
        this.buyerCodeListAgency = buyerCodeListAgency;
    }
}
