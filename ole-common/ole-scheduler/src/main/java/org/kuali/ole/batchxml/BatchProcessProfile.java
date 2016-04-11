package org.kuali.ole.batchxml;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchProcessProfile extends PersistableBusinessObjectBase {

    @JsonProperty(OleNGConstants.PROFILE_ID)
    private long batchProcessProfileId;

    @JsonProperty(OleNGConstants.PROFILE_NAME)
    private String batchProcessProfileName;

    @JsonProperty("profileDescription")
    private String description;

    @JsonProperty("batchProcessType")
    private String batchProcessType;

    @JsonProperty("bibImportProfileForOrderImport")
    private String bibImportProfileForOrderImport;

    @JsonProperty("requisitionForTitlesOption")
    private String requisitionForTitlesOption;

    @JsonProperty("forceLoad")
    private Boolean forceLoad;

    @JsonProperty("marcOnly")
    private Boolean marcOnly;

    @JsonProperty("orderType")
    private String orderType;

    @JsonProperty("matchPointToUse")
    private String matchPointToUse;

    @JsonIgnore
    private byte[] content;

    public long getBatchProcessProfileId() {
        return batchProcessProfileId;
    }

    public void setBatchProcessProfileId(long batchProcessProfileId) {
        this.batchProcessProfileId = batchProcessProfileId;
    }

    public String getBatchProcessProfileName() {
        return batchProcessProfileName;
    }

    public void setBatchProcessProfileName(String batchProcessProfileName) {
        this.batchProcessProfileName = batchProcessProfileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBatchProcessType() {
        return batchProcessType;
    }

    public void setBatchProcessType(String batchProcessType) {
        this.batchProcessType = batchProcessType;
    }

    public String getBibImportProfileForOrderImport() {
        return bibImportProfileForOrderImport;
    }

    public void setBibImportProfileForOrderImport(String bibImportProfileForOrderImport) {
        this.bibImportProfileForOrderImport = bibImportProfileForOrderImport;
    }

    public String getRequisitionForTitlesOption() {
        return requisitionForTitlesOption;
    }

    public void setRequisitionForTitlesOption(String requisitionForTitlesOption) {
        this.requisitionForTitlesOption = requisitionForTitlesOption;
    }

    public Boolean getForceLoad() {
        return forceLoad;
    }

    public void setForceLoad(Boolean forceLoad) {
        this.forceLoad = forceLoad;
    }

    public Boolean getMarcOnly() {
        return marcOnly;
    }

    public void setMarcOnly(Boolean marcOnly) {
        this.marcOnly = marcOnly;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getMatchPointToUse() {
        return matchPointToUse;
    }

    public void setMatchPointToUse(String matchPointToUse) {
        this.matchPointToUse = matchPointToUse;
    }
}
