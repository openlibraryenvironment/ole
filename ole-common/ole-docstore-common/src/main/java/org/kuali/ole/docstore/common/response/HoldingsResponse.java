package org.kuali.ole.docstore.common.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SheikS on 1/11/2016.
 */
public class HoldingsResponse {
    private String holdingsId;
    private String operation;
    private List<ItemResponse> itemResponses;
    private String holdingsType;

    public String getHoldingsId() {
        return holdingsId;
    }

    public void setHoldingsId(String holdingsId) {
        this.holdingsId = holdingsId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public List<ItemResponse> getItemResponses() {
        if(null == itemResponses) {
            itemResponses = new ArrayList<>();
        }
        return itemResponses;
    }

    public void setItemResponses(List<ItemResponse> itemResponses) {
        this.itemResponses = itemResponses;
    }

    public String getHoldingsType() {
        return holdingsType;
    }

    public void setHoldingsType(String holdingsType) {
        this.holdingsType = holdingsType;
    }
}
