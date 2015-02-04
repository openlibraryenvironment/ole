package org.kuali.ole.batch.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 7/6/13
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessProfileMappingOptionsBo extends PersistableBusinessObjectBase {

    private String oleBatchProcessDataMapId;
    private int oleBatchProcessDataMapOptionNum;
    private String oleBatchProcessDataMapOptionId;
    private String batchProcessProfileId;
    private OLEBatchProcessProfileBo oleBatchProcessProfileBo;
    private List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBoList = new ArrayList<OLEBatchProcessProfileDataMappingOptionsBo>();

    public OLEBatchProcessProfileBo getOleBatchProcessProfileBo() {
        return oleBatchProcessProfileBo;
    }

    public void setOleBatchProcessProfileBo(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        this.oleBatchProcessProfileBo = oleBatchProcessProfileBo;
    }

    public String getOleBatchProcessDataMapOptionId() {
        return oleBatchProcessDataMapOptionId;
    }

    public void setOleBatchProcessDataMapOptionId(String oleBatchProcessDataMapOptionId) {
        this.oleBatchProcessDataMapOptionId = oleBatchProcessDataMapOptionId;
    }

    public String getBatchProcessProfileId() {
        return batchProcessProfileId;
    }

    public void setBatchProcessProfileId(String batchProcessProfileId) {
        this.batchProcessProfileId = batchProcessProfileId;
    }

    public String getOleBatchProcessDataMapId() {
        return oleBatchProcessDataMapId;
    }

    public void setOleBatchProcessDataMapId(String oleBatchProcessDataMapId) {
        this.oleBatchProcessDataMapId = oleBatchProcessDataMapId;
    }

    public int getOleBatchProcessDataMapOptionNum() {
        return oleBatchProcessDataMapOptionNum;
    }

    public void setOleBatchProcessDataMapOptionNum(int oleBatchProcessDataMapOptionNum) {
        this.oleBatchProcessDataMapOptionNum = oleBatchProcessDataMapOptionNum;
    }

    public List<OLEBatchProcessProfileDataMappingOptionsBo> getOleBatchProcessProfileDataMappingOptionsBoList() {
        return oleBatchProcessProfileDataMappingOptionsBoList;
    }

    public void setOleBatchProcessProfileDataMappingOptionsBoList(List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBoList) {
        this.oleBatchProcessProfileDataMappingOptionsBoList = oleBatchProcessProfileDataMappingOptionsBoList;
    }
}
