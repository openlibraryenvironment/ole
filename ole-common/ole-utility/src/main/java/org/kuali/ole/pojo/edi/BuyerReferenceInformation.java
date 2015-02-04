package org.kuali.ole.pojo.edi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/6/12
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class BuyerReferenceInformation {
    private List<BuyerLineItemReference> buyerLineItemReference = new ArrayList<BuyerLineItemReference>();

    public void addBuyerLineItemReference(BuyerLineItemReference buyerLineItemReference) {
        if (!this.buyerLineItemReference.contains(buyerLineItemReference)) {
            this.buyerLineItemReference.add(buyerLineItemReference);
        }
    }

    public List<BuyerLineItemReference> getBuyerLineItemReference() {
        return buyerLineItemReference;
    }

    public void setBuyerLineItemReference(List<BuyerLineItemReference> buyerLineItemReference) {
        this.buyerLineItemReference = buyerLineItemReference;
    }

}
