package org.kuali.ole.batch.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.*;

/**
 * Created by sambasivam on 8/12/14.
 */
public class OLEBatchProcessBibDataMappingNew extends PersistableBusinessObjectBase {

    private String oleBatchProcessBibDataMappingNewId;
    private String tag;
    private String batchProcessProfileId;
    private OLEBatchProcessProfileBo oleBatchProcessProfileBo;
    private String gokbFieldBib;

    public String getGokbFieldBib() {
        return gokbFieldBib;
    }

    public void setGokbFieldBib(String gokbFieldBib) {
        this.gokbFieldBib = gokbFieldBib;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getOleBatchProcessBibDataMappingNewId() {
        return oleBatchProcessBibDataMappingNewId;
    }

    public void setOleBatchProcessBibDataMappingNewId(String oleBatchProcessBibDataMappingNewId) {
        this.oleBatchProcessBibDataMappingNewId = oleBatchProcessBibDataMappingNewId;
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
