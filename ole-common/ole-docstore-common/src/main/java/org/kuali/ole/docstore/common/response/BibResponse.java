package org.kuali.ole.docstore.common.response;

import java.util.List;

/**
 * Created by SheikS on 1/11/2016.
 */
public class BibResponse {
    private String bibId;
    private String valueOf001;
    private String operation;
    private List<HoldingsResponse> holdingsResponses;

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public List<HoldingsResponse> getHoldingsResponses() {
        return holdingsResponses;
    }

    public void setHoldingsResponses(List<HoldingsResponse> holdingsResponses) {
        this.holdingsResponses = holdingsResponses;
    }

    public String getValueOf001() {
        return valueOf001;
    }

    public void setValueOf001(String valueOf001) {
        this.valueOf001 = valueOf001;
    }
}
