package org.kuali.ole.deliver.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchulakshmig on 1/13/15.
 */
public class OLEHoldingsSearchResultDisplayRow {

    private String id;
    private String location;
    private String callNumber;
    private String itemLocation;
    private String bibIdentifier;
    private List<OLEItemSearchResultDisplayRow> oleItemSearchResultDisplayRowList = new ArrayList<>();

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

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getBibIdentifier() {
        return bibIdentifier;
    }

    public void setBibIdentifier(String bibIdentifier) {
        this.bibIdentifier = bibIdentifier;
    }

    public List<OLEItemSearchResultDisplayRow> getOleItemSearchResultDisplayRowList() {
        return oleItemSearchResultDisplayRowList;
    }

    public void setOleItemSearchResultDisplayRowList(List<OLEItemSearchResultDisplayRow> oleItemSearchResultDisplayRowList) {
        this.oleItemSearchResultDisplayRowList = oleItemSearchResultDisplayRowList;
    }
}
