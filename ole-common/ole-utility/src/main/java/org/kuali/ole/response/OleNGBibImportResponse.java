package org.kuali.ole.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SheikS on 1/7/2016.
 */
public class OleNGBibImportResponse {
    private String status;
    private String message;
    private List<String> createdBibIds;
    private List<String> updatedBibIds;
    private List<String> createdHoldingIds;
    private List<String> updatedHoldingIds;
    private List<String> createdItemIds;
    private List<String> updatedItemIds;
    private List<String> failureRecordQueries;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getCreatedBibIds() {
        if(null == createdBibIds) {
            createdBibIds = new ArrayList<>();
        }
        return createdBibIds;
    }

    public void setCreatedBibIds(List<String> createdBibIds) {
        this.createdBibIds = createdBibIds;
    }

    public List<String> getUpdatedBibIds() {
        if(null == updatedBibIds) {
            updatedBibIds = new ArrayList<>();
        }
        return updatedBibIds;
    }

    public void setUpdatedBibIds(List<String> updatedBibIds) {
        this.updatedBibIds = updatedBibIds;
    }

    public List<String> getCreatedHoldingIds() {
        if(null == createdHoldingIds) {
            createdHoldingIds = new ArrayList<>();
        }
        return createdHoldingIds;
    }

    public void setCreatedHoldingIds(List<String> createdHoldingIds) {
        this.createdHoldingIds = createdHoldingIds;
    }

    public List<String> getUpdatedHoldingIds() {
        if(null == updatedHoldingIds) {
            updatedHoldingIds = new ArrayList<>();
        }
        return updatedHoldingIds;
    }

    public void setUpdatedHoldingIds(List<String> updatedHoldingIds) {
        this.updatedHoldingIds = updatedHoldingIds;
    }

    public List<String> getCreatedItemIds() {
        if(null == createdItemIds) {
            createdItemIds = new ArrayList<>();
        }
        return createdItemIds;
    }

    public void setCreatedItemIds(List<String> createdItemIds) {
        this.createdItemIds = createdItemIds;
    }

    public List<String> getUpdatedItemIds() {
        if(null == updatedItemIds) {
            updatedItemIds = new ArrayList<>();
        }
        return updatedItemIds;
    }

    public void setUpdatedItemIds(List<String> updatedItemIds) {
        this.updatedItemIds = updatedItemIds;
    }

    public List<String> getFailureRecordQueries() {
        if(null == failureRecordQueries) {
            failureRecordQueries = new ArrayList<>();
        }
        return failureRecordQueries;
    }

    public void setFailureRecordQueries(List<String> failureRecordQueries) {
        this.failureRecordQueries = failureRecordQueries;
    }
}
