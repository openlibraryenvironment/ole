package org.kuali.ole.deliver.util;

import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemStatusRecord;

/**
 * Created by pvsubrah on 6/5/15.
 */
public class OleItemRecordForCirc {
    private String itemType;
    private String itemLocation;
    private String itemLibraryLocation;
    private ItemStatusRecord itemStatusRecord;
    private String itemUUID;
    private String institutionLocation;
    private String campusLocation;
    private String collectionLocation;


    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getItemLibraryLocation() {
        return itemLibraryLocation;
    }

    public void setItemLibraryLocation(String itemLibraryLocation) {
        this.itemLibraryLocation = itemLibraryLocation;
    }

    public ItemStatusRecord getItemStatusRecord() {
        return itemStatusRecord;
    }

    public void setItemStatusRecord(ItemStatusRecord itemStatusRecord) {
        this.itemStatusRecord = itemStatusRecord;
    }

    public String getItemUUID() {
        return itemUUID;
    }

    public void setItemUUID(String itemUUID) {
        this.itemUUID = itemUUID;
    }

    public void setInstitutionLocation(String institutionLocation) {
        this.institutionLocation = institutionLocation;
    }

    public void setCampusLocation(String campusLocation) {
        this.campusLocation = campusLocation;
    }

    public void setCollectionLocation(String collectionLocation) {
        this.collectionLocation = collectionLocation;
    }

    public String getInstitutionLocation() {
        return institutionLocation;
    }

    public String getCampusLocation() {
        return campusLocation;
    }

    public String getCollectionLocation() {
        return collectionLocation;
    }
}
