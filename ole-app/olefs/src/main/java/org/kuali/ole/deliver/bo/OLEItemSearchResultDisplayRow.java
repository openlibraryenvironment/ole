package org.kuali.ole.deliver.bo;

/**
 * Created by chenchulakshmig on 1/21/15.
 */
public class OLEItemSearchResultDisplayRow {

    private String id;
    private String location;
    private String itemType;
    private String itemStatus;
    private String itemBarCode;
    private String itemUUID;
    private String enumeration;
    private String callNumber;
    private String chronology;
    private String bibIdentifier;
    private String holdingsIdentifier;
    private boolean placeRequest;
    private boolean requestExists;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getItemBarCode() {
        return itemBarCode;
    }

    public void setItemBarCode(String itemBarCode) {
        this.itemBarCode = itemBarCode;
    }

    public String getItemUUID() {
        return itemUUID;
    }

    public void setItemUUID(String itemUUID) {
        this.itemUUID = itemUUID;
    }

    public String getEnumeration() {
        return enumeration;
    }

    public void setEnumeration(String enumeration) {
        this.enumeration = enumeration;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getChronology() {
        return chronology;
    }

    public void setChronology(String chronology) {
        this.chronology = chronology;
    }

    public String getBibIdentifier() {
        return bibIdentifier;
    }

    public void setBibIdentifier(String bibIdentifier) {
        this.bibIdentifier = bibIdentifier;
    }

    public String getHoldingsIdentifier() {
        return holdingsIdentifier;
    }

    public void setHoldingsIdentifier(String holdingsIdentifier) {
        this.holdingsIdentifier = holdingsIdentifier;
    }

    public boolean isPlaceRequest() {
        return placeRequest;
    }

    public void setPlaceRequest(boolean placeRequest) {
        this.placeRequest = placeRequest;
    }

    public boolean isRequestExists() {
        return requestExists;
    }

    public void setRequestExists(boolean requestExists) {
        this.requestExists = requestExists;
    }
}
