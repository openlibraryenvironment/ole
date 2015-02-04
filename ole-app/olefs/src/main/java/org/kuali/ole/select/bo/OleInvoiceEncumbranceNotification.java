package org.kuali.ole.select.bo;

/**
 * Created with IntelliJ IDEA.
 * User: syedk
 * Date: 2/18/14
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleInvoiceEncumbranceNotification {

    private String itemTitle;
    private String purchaseOrderIdentifier;
    private String purchaseOrderAmount;
    private String invoiceAmount;
    private String differenceByThresholdAmount;


    public String getDifferenceByThresholdAmount() {
        return differenceByThresholdAmount;
    }

    public void setDifferenceByThresholdAmount(String differenceByThresholdAmount) {
        this.differenceByThresholdAmount = differenceByThresholdAmount;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getPurchaseOrderAmount() {
        return purchaseOrderAmount;
    }

    public void setPurchaseOrderAmount(String purchaseOrderAmount) {
        this.purchaseOrderAmount = purchaseOrderAmount;
    }

    public String getPurchaseOrderIdentifier() {
        return purchaseOrderIdentifier;
    }

    public void setPurchaseOrderIdentifier(String purchaseOrderIdentifier) {
        this.purchaseOrderIdentifier = purchaseOrderIdentifier;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }
}
