package org.kuali.ole.oleng.batch.profile.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajeshbabuk on 12/9/15.
 */
public class BatchProcessProfile {

    private long batchProcessProfileId;
    private String batchProcessProfileName;
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
}
