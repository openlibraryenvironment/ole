package org.kuali.ole;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/8/12
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */

import org.kuali.ole.pojo.edi.BuyerInformation;

public enum BuyerInformationFixture {
    BuyerInformation("DUL-WCS",
            null,
            "ZZ"
    ),;

    private String buyerCodeIdentification;
    private String buyerPartyIdentificationCode;
    private String buyerCodeListAgency;

    private BuyerInformationFixture(String buyerCodeIdentification, String buyerPartyIdentificationCode,
                                    String buyerCodeListAgency) {
        this.buyerCodeIdentification = buyerCodeIdentification;
        this.buyerPartyIdentificationCode = buyerPartyIdentificationCode;
        this.buyerCodeListAgency = buyerCodeListAgency;
    }

    public BuyerInformation createBuyerInformation(Class clazz) {
        BuyerInformation buyerInformation = null;
        try {
            buyerInformation = (BuyerInformation) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("BuyerInformation creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("BuyerInformation creation failed. class = " + clazz);
        }
        buyerInformation.setBuyerCodeIdentification(buyerCodeIdentification);
        buyerInformation.setBuyerPartyIdentificationCode(buyerPartyIdentificationCode);
        buyerInformation.setBuyerCodeListAgency(buyerCodeListAgency);
        return buyerInformation;
    }

}
