package org.kuali.ole.batch.bo;


import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.*;

/**
 * Created by sambasivam on 8/12/14.
 */

public class OLEBatchProcessBibDataMappingOverlay  extends PersistableBusinessObjectBase {

    private String oleBatchProcessBibDataMappingOverlayId;
    private String tag;
    private String addOrReplace;
    private String batchProcessProfileId;
    private OLEBatchProcessProfileBo oleBatchProcessProfileBo;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAddOrReplace() {
        return addOrReplace;
    }

    public void setAddOrReplace(String addOrReplace) {
        this.addOrReplace = addOrReplace;
    }

    public String getOleBatchProcessBibDataMappingOverlayId() {
        return oleBatchProcessBibDataMappingOverlayId;
    }

    public void setOleBatchProcessBibDataMappingOverlayId(String oleBatchProcessBibDataMappingOverlayId) {
        this.oleBatchProcessBibDataMappingOverlayId = oleBatchProcessBibDataMappingOverlayId;
    }

    public String getBatchProcessProfileId() {
        return batchProcessProfileId;
    }

    public void setBatchProcessProfileId(String batchProcessProfileId) {
        this.batchProcessProfileId = batchProcessProfileId;
    }

    public OLEBatchProcessProfileBo getOleBatchProcessProfileBo() {
        return oleBatchProcessProfileBo;
    }

    public void setOleBatchProcessProfileBo(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        this.oleBatchProcessProfileBo = oleBatchProcessProfileBo;
    }
}
