package org.kuali.ole.describe.bo;

import org.kuali.ole.docstore.common.document.ItemOleml;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: venkatasrinathy
 * Date: 12/27/13
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleWorkItemDocument extends ItemOleml {
    private String localId;
    private String itemIdentifier;
    private String bibIdentifier;
    private String instanceIdentifier;
    private String bibTitle;
    private String linkedBibCount;
    private List<String> bibUUIDList;
    private String staffOnly;
    protected String dateEntered;
    protected String dateUpdated;
    protected String createdBy;
    protected String updatedBy;

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getItemIdentifier() {
        return itemIdentifier;
    }

    public void setItemIdentifier(String itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
    }

    public String getBibIdentifier() {
        return bibIdentifier;
    }

    public void setBibIdentifier(String bibIdentifier) {
        this.bibIdentifier = bibIdentifier;
    }

    public String getInstanceIdentifier() {
        return instanceIdentifier;
    }

    public void setInstanceIdentifier(String instanceIdentifier) {
        this.instanceIdentifier = instanceIdentifier;
    }

    public String getBibTitle() {
        return bibTitle;
    }

    public void setBibTitle(String bibTitle) {
        this.bibTitle = bibTitle;
    }

    public String getLinkedBibCount() {
        return linkedBibCount;
    }

    public void setLinkedBibCount(String linkedBibCount) {
        this.linkedBibCount = linkedBibCount;
    }

    public List<String> getBibUUIDList() {
        return bibUUIDList;
    }

    public void setBibUUIDList(List<String> bibUUIDList) {
        this.bibUUIDList = bibUUIDList;
    }

    public String getStaffOnly() {
        return staffOnly;
    }

    public void setStaffOnly(String staffOnly) {
        this.staffOnly = staffOnly;
    }

    public String getDateEntered() {
        return dateEntered;
    }

    public void setDateEntered(String dateEntered) {
        this.dateEntered = dateEntered;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
