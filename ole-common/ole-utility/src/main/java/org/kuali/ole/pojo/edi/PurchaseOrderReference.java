package org.kuali.ole.pojo.edi;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 8/7/13
 * Time: 6:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseOrderReference {

    private String purchaseOrderLine;
    private String purchaseOrderNumber;


    public String getPurchaseOrderLine() {
        return purchaseOrderLine;
    }

    public void setPurchaseOrderLine(String purchaseOrderLine) {
        this.purchaseOrderLine = purchaseOrderLine;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }
}
