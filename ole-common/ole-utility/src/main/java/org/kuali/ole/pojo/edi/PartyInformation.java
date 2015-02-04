package org.kuali.ole.pojo.edi;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 9/26/13
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class PartyInformation {
    private String codeIdentification;
    private String partyIdentificationCode;
    private String codeListAgency;

    public String getCodeIdentification() {
        return codeIdentification;
    }

    public void setCodeIdentification(String codeIdentification) {
        this.codeIdentification = codeIdentification;
    }

    public String getPartyIdentificationCode() {
        return partyIdentificationCode;
    }

    public void setPartyIdentificationCode(String partyIdentificationCode) {
        this.partyIdentificationCode = partyIdentificationCode;
    }

    public String getCodeListAgency() {
        return codeListAgency;
    }

    public void setCodeListAgency(String codeListAgency) {
        this.codeListAgency = codeListAgency;
    }
}
