package org.kuali.ole.batch.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 7/26/13
 * Time: 7:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessProfileBibWorkUnit extends PersistableBusinessObjectBase {

    private String batchProcessBibWorkUnitId;
    private String batchProcessBibWorkUnit;
    private String batchProcessProfileId;
    private OLEBatchProcessProfileBo oleBatchProcessProfileBo;

    public OLEBatchProcessProfileBo getOleBatchProcessProfileBo() {
        return oleBatchProcessProfileBo;
    }

    public void setOleBatchProcessProfileBo(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        this.oleBatchProcessProfileBo = oleBatchProcessProfileBo;
    }

    public String getBatchProcessBibWorkUnitId() {
        return batchProcessBibWorkUnitId;
    }

    public void setBatchProcessBibWorkUnitId(String batchProcessBibWorkUnitId) {
        this.batchProcessBibWorkUnitId = batchProcessBibWorkUnitId;
    }

    public String getBatchProcessBibWorkUnit() {
        return batchProcessBibWorkUnit;
    }

    public void setBatchProcessBibWorkUnit(String batchProcessBibWorkUnit) {
        this.batchProcessBibWorkUnit = batchProcessBibWorkUnit;
    }

    public String getBatchProcessProfileId() {
        return batchProcessProfileId;
    }

    public void setBatchProcessProfileId(String batchProcessProfileId) {
        this.batchProcessProfileId = batchProcessProfileId;
    }
}
