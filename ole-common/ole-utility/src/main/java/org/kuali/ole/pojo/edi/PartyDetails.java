package org.kuali.ole.pojo.edi;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/6/12
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class PartyDetails {
    private String additionalPartyIdentification;
    private String partyName;

    public String getAdditionalPartyIdentification() {
        return additionalPartyIdentification;
    }

    public void setAdditionalPartyIdentification(String additionalPartyIdentification) {
        this.additionalPartyIdentification = additionalPartyIdentification;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }
}
