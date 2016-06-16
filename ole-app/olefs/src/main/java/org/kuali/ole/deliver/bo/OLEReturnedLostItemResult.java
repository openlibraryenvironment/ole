package org.kuali.ole.deliver.bo;

/**
 * Created by gopalp on 4/4/16.
 */
public class OLEReturnedLostItemResult {

    private String itemBarcode;
    private String title;
    private String itemStatus;

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }
}
