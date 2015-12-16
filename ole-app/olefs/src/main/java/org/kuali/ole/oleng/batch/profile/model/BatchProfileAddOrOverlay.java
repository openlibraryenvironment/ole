package org.kuali.ole.oleng.batch.profile.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by rajeshbabuk on 12/14/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchProfileAddOrOverlay {

    private String matchOption;

    @JsonProperty("addOrOverlayDocType")
    private String dataType;
    private String operation;
    private String bibStatus;
    private String addOperation;
    private boolean addItems;

    public String getMatchOption() {
        return matchOption;
    }

    public void setMatchOption(String matchOption) {
        this.matchOption = matchOption;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getBibStatus() {
        return bibStatus;
    }

    public void setBibStatus(String bibStatus) {
        this.bibStatus = bibStatus;
    }

    public String getAddOperation() {
        return addOperation;
    }

    public void setAddOperation(String addOperation) {
        this.addOperation = addOperation;
    }

    public boolean isAddItems() {
        return addItems;
    }

    public void setAddItems(boolean addItems) {
        this.addItems = addItems;
    }
}
