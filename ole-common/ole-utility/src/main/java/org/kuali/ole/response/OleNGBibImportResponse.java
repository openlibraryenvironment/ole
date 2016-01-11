package org.kuali.ole.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SheikS on 1/7/2016.
 */
public class OleNGBibImportResponse {
    private String status;
    private String message;
    private int noOfBibsCreated;
    private int noOfBibsUpdated;
    private int noOfHoldingsCreated;
    private int noOfHoldingsUpdated;
    private int noOfItemsCreated;
    private int noOfItemsUpdated;
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

    public int getNoOfBibsCreated() {
        return noOfBibsCreated;
    }

    public void setNoOfBibsCreated(int noOfBibsCreated) {
        this.noOfBibsCreated = noOfBibsCreated;
    }

    public int getNoOfBibsUpdated() {
        return noOfBibsUpdated;
    }

    public void setNoOfBibsUpdated(int noOfBibsUpdated) {
        this.noOfBibsUpdated = noOfBibsUpdated;
    }

    public int getNoOfHoldingsCreated() {
        return noOfHoldingsCreated;
    }

    public void setNoOfHoldingsCreated(int noOfHoldingsCreated) {
        this.noOfHoldingsCreated = noOfHoldingsCreated;
    }

    public int getNoOfHoldingsUpdated() {
        return noOfHoldingsUpdated;
    }

    public void setNoOfHoldingsUpdated(int noOfHoldingsUpdated) {
        this.noOfHoldingsUpdated = noOfHoldingsUpdated;
    }

    public int getNoOfItemsCreated() {
        return noOfItemsCreated;
    }

    public void setNoOfItemsCreated(int noOfItemsCreated) {
        this.noOfItemsCreated = noOfItemsCreated;
    }

    public int getNoOfItemsUpdated() {
        return noOfItemsUpdated;
    }

    public void setNoOfItemsUpdated(int noOfItemsUpdated) {
        this.noOfItemsUpdated = noOfItemsUpdated;
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
