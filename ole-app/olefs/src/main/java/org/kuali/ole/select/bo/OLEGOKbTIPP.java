package org.kuali.ole.select.bo;

import org.kuali.ole.select.gokb.OleGokbTipp;

/**
 * This class is used to hold the TIPP detail from GOKb service for displaying in UI.
 */
public class OLEGOKbTIPP {

    private String title;
    private String gokbStatus;
    private String type;
    private String issn;
    private String startDate;
    private String endDate;
    private String url;
    private String dateCreated;
    private String dateUpdated;
    private boolean select;
    private Integer publisherId;
    private boolean tippExists;
    private OleGokbTipp oleGokbTipp;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGokbStatus() {
        return gokbStatus;
    }

    public void setGokbStatus(String gokbStatus) {
        this.gokbStatus = gokbStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public Integer getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Integer publisherId) {
        this.publisherId = publisherId;
    }

    public boolean isTippExists() {
        return tippExists;
    }

    public void setTippExists(boolean tippExists) {
        this.tippExists = tippExists;
    }

    public OleGokbTipp getOleGokbTipp() {
        return oleGokbTipp;
    }

    public void setOleGokbTipp(OleGokbTipp oleGokbTipp) {
        this.oleGokbTipp = oleGokbTipp;
    }
}
