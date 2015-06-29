package org.kuali.ole.deliver.form;

import org.kuali.ole.deliver.bo.OLEBibSearchResultDisplayRow;
import org.kuali.ole.deliver.bo.OLESingleItemResultDisplayRow;
import org.kuali.ole.docstore.common.search.SearchParams;
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
    private String selectRowDetails;
    private boolean showSelectedRowDetails;
    private boolean nextFlag;
    private boolean previousFlag;
    private String sortOrder;
    private String sortField;
    private int totalRecordCount;
    private String pageNumber = "0";
    private SearchParams searchParams;
    private int pageSize = 10;
    protected int start = 0;


    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public boolean isNextFlag() {
        return nextFlag;
    }

    public void setNextFlag(boolean nextFlag) {
        this.nextFlag = nextFlag;
    }

    public boolean isPreviousFlag() {
        return previousFlag;
    }

    public void setPreviousFlag(boolean previousFlag) {
        this.previousFlag = previousFlag;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public boolean isShowSelectedRowDetails() {
        return showSelectedRowDetails;
    }

    public void setShowSelectedRowDetails(boolean showSelectedRowDetails) {
        this.showSelectedRowDetails = showSelectedRowDetails;
    }

    public String getSelectRowDetails() {
        return selectRowDetails;
    }

    public void setSelectRowDetails(String selectRowDetails) {
        this.selectRowDetails = selectRowDetails;
    }

    public int getTotalRecordCount() {
        return totalRecordCount;
    }

    public void setTotalRecordCount(int totalRecordCount) {
        this.totalRecordCount = totalRecordCount;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public SearchParams getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(SearchParams searchParams) {
        this.searchParams = searchParams;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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
