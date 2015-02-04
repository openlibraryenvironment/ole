package org.kuali.ole.pojo.edi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/6/12
 * Time: 5:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class FundNumberReference {
    private List<BuyersFundNumberReference> buyersFundNumberReference = new ArrayList<BuyersFundNumberReference>();

    public void addBuyersFundNumberReference(BuyersFundNumberReference buyersFundNumberReference) {
        if (!this.buyersFundNumberReference.contains(buyersFundNumberReference)) {
            this.buyersFundNumberReference.add(buyersFundNumberReference);
        }
    }

    public List<BuyersFundNumberReference> getBuyersFundNumberReference() {
        return buyersFundNumberReference;
    }

    public void setBuyersFundNumberReference(List<BuyersFundNumberReference> buyersFundNumberReference) {
        this.buyersFundNumberReference = buyersFundNumberReference;
    }
}
