package org.kuali.ole.pojo.edi;

/**
 * Created by palanivel on 14/8/14.
 */
public class BuyerIdentifier {

    private String buyerReferenceQualifier;

    private String buyerReferenceNumber;

    public String getBuyerReferenceQualifier() {
        return buyerReferenceQualifier;
    }

    public void setBuyerReferenceQualifier(String buyerReferenceQualifier) {
        this.buyerReferenceQualifier = buyerReferenceQualifier;
    }

    public String getBuyerReferenceNumber() {
        return buyerReferenceNumber;
    }

    public void setBuyerReferenceNumber(String buyerReferenceNumber) {
        this.buyerReferenceNumber = buyerReferenceNumber;
    }
}
