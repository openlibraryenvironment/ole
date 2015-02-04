package org.kuali.ole.pojo.edi;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/6/12
 * Time: 3:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class BuyerPartyQualifier {
    private String buyer;
    private BuyerInformation buyerInformation;

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public BuyerInformation getBuyerInformation() {
        return buyerInformation;
    }

    public void setBuyerInformation(BuyerInformation buyerInformation) {
        this.buyerInformation = buyerInformation;
    }
}
