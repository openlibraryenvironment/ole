package org.kuali.ole.describe.form;


import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.model.bo.WorkBibDocument;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.apache.commons.lang.StringUtils;

import java.util.List;


public class CallNumberBrowseForm extends UifFormBase {


    private String message;
    private String information;
    private int pageSize = 10;
    private List<WorkBibDocument> workBibDocumentList;
    private List<Holdings> holdingsList;
    private List<Item> itemList;
    private String location;
    private String classificationScheme;
    private String callNumberBrowseText;

    public boolean nextFlag;
    public boolean previousFlag;
    public String pageShowEntries;
    public String docType;
    public boolean closeBtnShowFlag;


    public boolean isCloseBtnShowFlag() {
        return closeBtnShowFlag;
    }

    public void setCloseBtnShowFlag(boolean closeBtnShowFlag) {
        this.closeBtnShowFlag = closeBtnShowFlag;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        if (docType != null && docType.contains(","))
            this.docType = docType.substring(0, docType.indexOf(","));
        else
            this.docType = docType;

    }

    public String getPageShowEntries() {
        return pageShowEntries;
    }

    public void setPageShowEntries(String pageShowEntries) {
        this.pageShowEntries = pageShowEntries;
    }

    public boolean getNextFlag() {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getClassificationScheme() {
        return classificationScheme;
    }

    public void setClassificationScheme(String classificationScheme) {
        this.classificationScheme = classificationScheme;
    }

    public String getCallNumberBrowseText() {
        return callNumberBrowseText;
    }

    public void setCallNumberBrowseText(String callNumberBrowseText) {
        this.callNumberBrowseText = callNumberBrowseText;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<WorkBibDocument> getWorkBibDocumentList() {
        return workBibDocumentList;
    }

    public void setWorkBibDocumentList(List<WorkBibDocument> workBibDocumentList) {
        this.workBibDocumentList = workBibDocumentList;
    }

    public List<Holdings> getHoldingsList() {
        return holdingsList;
    }

    public void setHoldingsList(List<Holdings> holdingsList) {
        this.holdingsList = holdingsList;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}
