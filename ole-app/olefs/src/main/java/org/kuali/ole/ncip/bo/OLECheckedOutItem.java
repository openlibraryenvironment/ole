package org.kuali.ole.ncip.bo;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 8/16/13
 * Time: 8:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLECheckedOutItem {
    private String itemId;
    private String itemType;
    private String catalogueId;
    private String loanDate;
    private String dueDate;
    private String acquiredFine;
    private boolean overDue;
    private String numberOfRenewals;
    private String dateRecalled;
    private String title;
    private String volumeNumber;
    private String callNumber;
    private String copyNumber;
    private String author;
    private String dateRenewed;
    private String numberOfOverdueSent;
    private String location;


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getCatalogueId() {
        return catalogueId;
    }

    public void setCatalogueId(String catalogueId) {
        this.catalogueId = catalogueId;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getAcquiredFine() {
        return acquiredFine;
    }

    public void setAcquiredFine(String acquiredFine) {
        this.acquiredFine = acquiredFine;
    }

    public boolean isOverDue() {
        return overDue;
    }

    public void setOverDue(boolean overDue) {
        this.overDue = overDue;
    }

    public String getNumberOfRenewals() {
        return numberOfRenewals;
    }

    public void setNumberOfRenewals(String numberOfRenewals) {
        this.numberOfRenewals = numberOfRenewals;
    }

    public String getDateRecalled() {
        return dateRecalled;
    }

    public void setDateRecalled(String dateRecalled) {
        this.dateRecalled = dateRecalled;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(String volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getDateRenewed() {
        return dateRenewed;
    }

    public void setDateRenewed(String dateRenewed) {
        this.dateRenewed = dateRenewed;
    }

    public String getNumberOfOverdueSent() {
        return numberOfOverdueSent;
    }

    public void setNumberOfOverdueSent(String numberOfOverdueSent) {
        this.numberOfOverdueSent = numberOfOverdueSent;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
