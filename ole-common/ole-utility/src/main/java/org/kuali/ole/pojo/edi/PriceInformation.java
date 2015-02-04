package org.kuali.ole.pojo.edi;

import org.kuali.ole.pojo.edi.ItemPrice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/6/12
 * Time: 4:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class PriceInformation {
    private List<ItemPrice> itemPrice = new ArrayList<ItemPrice>();

    public void addPrice(ItemPrice itemPrice) {
        if (!this.itemPrice.contains(itemPrice)) {
            this.itemPrice.add(itemPrice);
        }
    }

    public List<ItemPrice> getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(List<ItemPrice> itemPrice) {
        this.itemPrice = itemPrice;
    }
}
