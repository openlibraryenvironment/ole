package org.kuali.ole.oleng.batch.profile.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajeshbabuk on 12/9/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchProcessProfile extends PersistableBusinessObjectBase{

    @JsonProperty("profileId")
    private long batchProcessProfileId;

    @JsonProperty("profileName")
    private String batchProcessProfileName;

    @JsonProperty("profileDescription")
    private String description;

    @JsonProperty("batchProcessType")
    private String batchProcessType;

    @JsonProperty("dataToImport")
    private String dataToImport;

    @JsonProperty("forceLoad")
    private Boolean forceLoad;

    @JsonIgnore
    private byte[] content;

    private List<BatchProfileMatchPoint> batchProfileMatchPointList = new ArrayList<>();
    private List<BatchProfileAddOrOverlay> batchProfileAddOrOverlayList = new ArrayList<>();
    private List<BatchProfileFieldOperation> batchProfileFieldOperationList = new ArrayList<>();
    private List<BatchProfileDataMapping> batchProfileDataMappingList = new ArrayList<>();
    private List<BatchProfileDataTransformer> batchProfileDataTransformerList = new ArrayList<>();

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

    public List<BatchProfileMatchPoint> getBatchProfileMatchPointList() {
        return batchProfileMatchPointList;
    }

    public void setBatchProfileMatchPointList(List<BatchProfileMatchPoint> batchProfileMatchPointList) {
        this.batchProfileMatchPointList = batchProfileMatchPointList;
    }

    public List<BatchProfileAddOrOverlay> getBatchProfileAddOrOverlayList() {
        return batchProfileAddOrOverlayList;
    }

    public void setBatchProfileAddOrOverlayList(List<BatchProfileAddOrOverlay> batchProfileAddOrOverlayList) {
        this.batchProfileAddOrOverlayList = batchProfileAddOrOverlayList;
    }

    public List<BatchProfileFieldOperation> getBatchProfileFieldOperationList() {
        return batchProfileFieldOperationList;
    }

    public void setBatchProfileFieldOperationList(List<BatchProfileFieldOperation> batchProfileFieldOperationList) {
        this.batchProfileFieldOperationList = batchProfileFieldOperationList;
    }

    public List<BatchProfileDataMapping> getBatchProfileDataMappingList() {
        return batchProfileDataMappingList;
    }

    public void setBatchProfileDataMappingList(List<BatchProfileDataMapping> batchProfileDataMappingList) {
        this.batchProfileDataMappingList = batchProfileDataMappingList;
    }

    public List<BatchProfileDataTransformer> getBatchProfileDataTransformerList() {
        return batchProfileDataTransformerList;
    }

    public void setBatchProfileDataTransformerList(List<BatchProfileDataTransformer> batchProfileDataTransformerList) {
        this.batchProfileDataTransformerList = batchProfileDataTransformerList;
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

    public String getDataToImport() {
        return dataToImport;
    }

    public void setDataToImport(String dataToImport) {
        this.dataToImport = dataToImport;
    }

    public Boolean getForceLoad() {
        return forceLoad;
    }

    public void setForceLoad(Boolean forceLoad) {
        this.forceLoad = forceLoad;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
