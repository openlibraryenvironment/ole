package org.kuali.ole.docstore.common.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SheikS on 1/11/2016.
 */
public class BibResponse {
    private String bibId;
    private Integer recordIndex;
    private String operation;
    private List<HoldingsResponse> holdingsResponses;
    private List<String> validationErrorMessages = new ArrayList<>();

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
        if(null == holdingsResponses) {
            holdingsResponses = new ArrayList<>();
        }
        return holdingsResponses;
    }

    public void setHoldingsResponses(List<HoldingsResponse> holdingsResponses) {
        this.holdingsResponses = holdingsResponses;
    }

    public Integer getRecordIndex() {
        return recordIndex;
    }

    public void setRecordIndex(Integer recordIndex) {
        this.recordIndex = recordIndex;
    }

    public List<String> getValidationErrorMessages() {
        return validationErrorMessages;
    }

    public void setValidationErrorMessages(List<String> validationErrorMessages) {
        this.validationErrorMessages = validationErrorMessages;
    }
}
