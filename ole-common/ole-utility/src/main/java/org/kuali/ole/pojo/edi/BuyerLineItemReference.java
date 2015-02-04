package org.kuali.ole.pojo.edi;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/6/12
 * Time: 4:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class BuyerLineItemReference {

    private String buyersOrderLine;
    private String orderLineNumber;

    public String getBuyersOrderLine() {
        return buyersOrderLine;
    }

    public void setBuyersOrderLine(String buyersOrderLine) {
        this.buyersOrderLine = buyersOrderLine;
    }

    public String getOrderLineNumber() {
        return orderLineNumber;
    }

    public void setOrderLineNumber(String orderLineNumber) {
        this.orderLineNumber = orderLineNumber;
    }
}
