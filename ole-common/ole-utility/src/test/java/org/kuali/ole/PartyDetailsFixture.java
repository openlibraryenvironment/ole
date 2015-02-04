package org.kuali.ole;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/8/12
 * Time: 6:56 PM
 * To change this template use File | Settings | File Templates.
 */

import org.kuali.ole.pojo.edi.PartyDetails;

public enum PartyDetailsFixture {
    PartyDetails("API",
            "DUL-WCS"
    ),;

    private String additionalPartyIdentification;
    private String partyName;

    private PartyDetailsFixture(String additionalPartyIdentification, String partyName) {
        this.additionalPartyIdentification = additionalPartyIdentification;
        this.partyName = partyName;
    }

    public PartyDetails createPartyDetails(Class clazz) {
        PartyDetails partyDetails = null;
        try {
            partyDetails = (PartyDetails) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("PartyDetails creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("PartyDetails creation failed. class = " + clazz);
        }
        partyDetails.setAdditionalPartyIdentification(additionalPartyIdentification);
        partyDetails.setPartyName(partyName);

        return partyDetails;
    }
}
