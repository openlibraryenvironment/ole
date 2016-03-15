package org.kuali.ole.docstore.common.response;

/**
 * Created by SheikS on 3/14/2016.
 */
public class FailureResponse {

    private Integer index;
    private String failureMessage;
    private String detailedMessage;
    private int noOfFailureHoldings;
    private int noOfFailureItems;
    private int noOfFailureEHoldings;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }

    public void setDetailedMessage(String detailedMessage) {
        this.detailedMessage = detailedMessage;
    }

    public int getNoOfFailureHoldings() {
        return noOfFailureHoldings;
    }

    public void setNoOfFailureHoldings(int noOfFailureHoldings) {
        this.noOfFailureHoldings = noOfFailureHoldings;
    }

    public int getNoOfFailureItems() {
        return noOfFailureItems;
    }

    public void setNoOfFailureItems(int noOfFailureItems) {
        this.noOfFailureItems = noOfFailureItems;
    }

    public int getNoOfFailureEHoldings() {
        return noOfFailureEHoldings;
    }

    public void setNoOfFailureEHoldings(int noOfFailureEHoldings) {
        this.noOfFailureEHoldings = noOfFailureEHoldings;
    }
}
