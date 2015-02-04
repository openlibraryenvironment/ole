package org.kuali.ole.pojo.edi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/26/13
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class MonetaryDetail {
    private List<MonetaryLineItemInformation> monetaryLineItemInformation = new ArrayList<>();


    public void addMonetaryLineItemInformation(MonetaryLineItemInformation monetaryLineItemInformation) {
        if (!this.monetaryLineItemInformation.contains(monetaryLineItemInformation)) {
            this.monetaryLineItemInformation.add(monetaryLineItemInformation);
        }
    }

    public List<MonetaryLineItemInformation> getMonetaryLineItemInformation() {
        return monetaryLineItemInformation;
    }

    public void setMonetaryLineItemInformation(List<MonetaryLineItemInformation> monetaryLineItemInformation) {
        this.monetaryLineItemInformation = monetaryLineItemInformation;
    }
}
