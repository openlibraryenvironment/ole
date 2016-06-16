package org.kuali.ole.docstore.common.response;

/**
 * Created by angelind on 3/15/16.
 */
public class BibFailureResponse extends FailureResponse {

    private int noOfFailureHoldings;
    private int noOfFailureItems;
    private int noOfFailureEHoldings;

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
