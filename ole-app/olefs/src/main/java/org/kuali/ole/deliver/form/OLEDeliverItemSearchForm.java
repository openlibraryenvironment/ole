package org.kuali.ole.deliver.form;

import org.kuali.ole.deliver.bo.OLEBibSearchResultDisplayRow;
import org.kuali.ole.deliver.bo.OLESingleItemResultDisplayRow;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchulakshmig on 1/12/15.
 */
public class OLEDeliverItemSearchForm extends UifFormBase {

    private String title;
    private String author;
    private String publisher;
    private String itemBarCode;
    private String itemCallNumber;
    private String itemUUID;
    private String itemType;
    private String itemLocation;
    private boolean singleItemFlag = false;
    private boolean multipleItemFlag = false;
    private List<OLEBibSearchResultDisplayRow> oleBibSearchResultDisplayRowList = new ArrayList<>();
    private OLESingleItemResultDisplayRow oleSingleItemResultDisplayRow;

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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getItemBarCode() {
        return itemBarCode;
    }

    public void setItemBarCode(String itemBarCode) {
        this.itemBarCode = itemBarCode;
    }

    public String getItemCallNumber() {
        return itemCallNumber;
    }

    public void setItemCallNumber(String itemCallNumber) {
        this.itemCallNumber = itemCallNumber;
    }

    public String getItemUUID() {
        return itemUUID;
    }

    public void setItemUUID(String itemUUID) {
        this.itemUUID = itemUUID;
    }

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

    public boolean isSingleItemFlag() {
        return singleItemFlag;
    }

    public void setSingleItemFlag(boolean singleItemFlag) {
        this.singleItemFlag = singleItemFlag;
    }

    public boolean isMultipleItemFlag() {
        return multipleItemFlag;
    }

    public void setMultipleItemFlag(boolean multipleItemFlag) {
        this.multipleItemFlag = multipleItemFlag;
    }

    public List<OLEBibSearchResultDisplayRow> getOleBibSearchResultDisplayRowList() {
        return oleBibSearchResultDisplayRowList;
    }

    public void setOleBibSearchResultDisplayRowList(List<OLEBibSearchResultDisplayRow> oleBibSearchResultDisplayRowList) {
        this.oleBibSearchResultDisplayRowList = oleBibSearchResultDisplayRowList;
    }

    public OLESingleItemResultDisplayRow getOleSingleItemResultDisplayRow() {
        return oleSingleItemResultDisplayRow;
    }

    public void setOleSingleItemResultDisplayRow(OLESingleItemResultDisplayRow oleSingleItemResultDisplayRow) {
        this.oleSingleItemResultDisplayRow = oleSingleItemResultDisplayRow;
    }

}
