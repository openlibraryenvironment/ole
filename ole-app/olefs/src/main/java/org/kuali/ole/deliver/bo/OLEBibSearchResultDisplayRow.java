package org.kuali.ole.deliver.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchulakshmig on 1/12/15.
 */
public class OLEBibSearchResultDisplayRow {

    private String id;
    private String title;
    private String author;
    private String publicationYear;
    private String holdingsLocation;
    private String itemLocation;
    private String itemCallNumber;
    private String itemStatus;
    private String itemType;
    private List<OLEHoldingsSearchResultDisplayRow> oleHoldingsSearchResultDisplayRowList = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getHoldingsLocation() {
        return holdingsLocation;
    }

    public void setHoldingsLocation(String holdingsLocation) {
        this.holdingsLocation = holdingsLocation;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getItemCallNumber() {
        return itemCallNumber;
    }

    public void setItemCallNumber(String itemCallNumber) {
        this.itemCallNumber = itemCallNumber;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public List<OLEHoldingsSearchResultDisplayRow> getOleHoldingsSearchResultDisplayRowList() {
        return oleHoldingsSearchResultDisplayRowList;
    }

    public void setOleHoldingsSearchResultDisplayRowList(List<OLEHoldingsSearchResultDisplayRow> oleHoldingsSearchResultDisplayRowList) {
        this.oleHoldingsSearchResultDisplayRowList = oleHoldingsSearchResultDisplayRowList;
    }
}
