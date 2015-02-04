package org.kuali.ole.select.businessobject;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: gopalp
 * Date: 7/20/13
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class OlePurchaseOrderLineForInvoice implements Serializable {

    private String itemId;
    private String itemTitle;
    private String amount;
    private boolean deleteLine;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public boolean isDeleteLine() {
        return deleteLine;
    }

    public void setDeleteLine(boolean deleteLine) {
        this.deleteLine = deleteLine;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
