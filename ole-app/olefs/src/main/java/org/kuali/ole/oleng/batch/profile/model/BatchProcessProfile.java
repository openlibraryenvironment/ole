package org.kuali.ole.oleng.batch.profile.model;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajeshbabuk on 12/9/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchProcessProfile extends PersistableBusinessObjectBase{

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

    @JsonProperty("exportScope")
    private String exportScope;

    @JsonIgnore
    private byte[] content;

    @JsonIgnore
    private boolean uploadExportInputFile = false;

    private List<BatchProfileFilterCriteria> batchProfileFilterCriteriaList = new ArrayList<>();
    private List<BatchProfileMatchPoint> batchProfileMatchPointList = new ArrayList<>();
    private List<BatchProfileAddOrOverlay> batchProfileAddOrOverlayList = new ArrayList<>();
    private List<BatchProfileFieldOperation> batchProfileFieldOperationList = new ArrayList<>();
    private List<BatchProfileConstantAndDefault> batchProfileConstantAndDefaultList = new ArrayList<>();
    private List<BatchProfileDataMapping> batchProfileDataMappingList = new ArrayList<>();
    private List<BatchProfileDataTransformer> batchProfileDataTransformerList = new ArrayList<>();
    private List<BatchProfileLocalDataMapping> batchProfileLocalDataMappings = new ArrayList<>();

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

    public List<BatchProfileFilterCriteria> getBatchProfileFilterCriteriaList() {
        return batchProfileFilterCriteriaList;
    }

    public void setBatchProfileFilterCriteriaList(List<BatchProfileFilterCriteria> batchProfileFilterCriteriaList) {
        this.batchProfileFilterCriteriaList = batchProfileFilterCriteriaList;
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

    public List<BatchProfileConstantAndDefault> getBatchProfileConstantAndDefaultList() {
        return batchProfileConstantAndDefaultList;
    }

    public void setBatchProfileConstantAndDefaultList(List<BatchProfileConstantAndDefault> batchProfileConstantAndDefaultList) {
        this.batchProfileConstantAndDefaultList = batchProfileConstantAndDefaultList;
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

    public String getExportScope() {
        return exportScope;
    }

    public void setExportScope(String exportScope) {
        this.exportScope = exportScope;
    }

    public boolean isUploadExportInputFile() {
        if (CollectionUtils.isNotEmpty(getBatchProfileFilterCriteriaList())) {
            if ("Bib Local Id From File".equalsIgnoreCase(getBatchProfileFilterCriteriaList().get(0).getFieldName())) {
                uploadExportInputFile = true;
            }
        }
        return uploadExportInputFile;
    }

    public void setUploadExportInputFile(boolean uploadExportInputFile) {
        this.uploadExportInputFile = uploadExportInputFile;
    }

    public List<BatchProfileLocalDataMapping> getBatchProfileLocalDataMappings() {
        return batchProfileLocalDataMappings;
    }

    public void setBatchProfileLocalDataMappings(List<BatchProfileLocalDataMapping> batchProfileLocalDataMappings) {
        this.batchProfileLocalDataMappings = batchProfileLocalDataMappings;
    }
}
