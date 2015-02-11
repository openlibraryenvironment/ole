package org.kuali.ole.deliver.bo;

/**
 * Created by maheswarang on 2/4/15.
 */
public class OleTitleLevelRequestItem {
    private String itemBarcode;
    private String holdingsId;
    private String itemLocation;
    private String itemType;
    private String itemStatus;
    private boolean selectedItem;

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getHoldingsId() {
        return holdingsId;
    }

    public void setHoldingsId(String holdingsId) {
        this.holdingsId = holdingsId;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public boolean isSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(boolean selectedItem) {
        this.selectedItem = selectedItem;
    }
}
