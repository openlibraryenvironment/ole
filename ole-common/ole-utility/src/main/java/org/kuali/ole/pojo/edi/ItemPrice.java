package org.kuali.ole.pojo.edi;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.basic.BigDecimalConverter;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/6/12
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */

public class ItemPrice {

    private String grossPrice;
    private String price;

    public String getGrossPrice() {
        return grossPrice;
    }

    public void setGrossPrice(String grossPrice) {
        this.grossPrice = grossPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
