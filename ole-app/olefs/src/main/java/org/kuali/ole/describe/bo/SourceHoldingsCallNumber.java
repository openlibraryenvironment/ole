package org.kuali.ole.describe.bo;

/**
 * Created with IntelliJ IDEA.
 * User: Poornima
 * Date: 6/9/12
 * Time: 6:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class SourceHoldingsCallNumber {

    private String number;
    private String prefix;
    private String shelvingScheme;
    private String shelvingOrder;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getShelvingScheme() {
        return shelvingScheme;
    }

    public void setShelvingScheme(String shelvingScheme) {
        this.shelvingScheme = shelvingScheme;
    }

    public String getShelvingOrder() {
        return shelvingOrder;
    }

    public void setShelvingOrder(String shelvingOrder) {
        this.shelvingOrder = shelvingOrder;
    }
}
