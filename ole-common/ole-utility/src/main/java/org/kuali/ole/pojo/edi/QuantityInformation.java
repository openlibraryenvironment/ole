package org.kuali.ole.pojo.edi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/6/12
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class QuantityInformation {
    private List<Qunatity> qunatity = new ArrayList<Qunatity>();

    public void addQuantity(Qunatity quantity) {
        if (!this.qunatity.contains(quantity)) {
            this.qunatity.add(quantity);
        }
    }

    public List<Qunatity> getQunatity() {
        return qunatity;
    }

    public void setQunatity(List<Qunatity> qunatity) {
        this.qunatity = qunatity;
    }
}
