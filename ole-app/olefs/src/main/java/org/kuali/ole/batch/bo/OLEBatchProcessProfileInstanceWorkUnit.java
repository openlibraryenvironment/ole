package org.kuali.ole.batch.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 7/26/13
 * Time: 7:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessProfileInstanceWorkUnit extends PersistableBusinessObjectBase {

    private String batchProcessInstanceWorkUnitId;
    private String batchProcessInstanceWorkUnit;
    private String batchProcessProfileId;
    private OLEBatchProcessProfileBo oleBatchProcessProfileBo;

    public OLEBatchProcessProfileBo getOleBatchProcessProfileBo() {
        return oleBatchProcessProfileBo;
    }

    public void setOleBatchProcessProfileBo(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        this.oleBatchProcessProfileBo = oleBatchProcessProfileBo;
    }

    public String getBatchProcessInstanceWorkUnitId() {
        return batchProcessInstanceWorkUnitId;
    }

    public void setBatchProcessInstanceWorkUnitId(String batchProcessInstanceWorkUnitId) {
        this.batchProcessInstanceWorkUnitId = batchProcessInstanceWorkUnitId;
    }

    public String getBatchProcessInstanceWorkUnit() {
        return batchProcessInstanceWorkUnit;
    }

    public void setBatchProcessInstanceWorkUnit(String batchProcessInstanceWorkUnit) {
        this.batchProcessInstanceWorkUnit = batchProcessInstanceWorkUnit;
    }

    public String getBatchProcessProfileId() {
        return batchProcessProfileId;
    }

    public void setBatchProcessProfileId(String batchProcessProfileId) {
        this.batchProcessProfileId = batchProcessProfileId;
    }
}
