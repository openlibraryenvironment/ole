package org.kuali.ole.batch.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 7/26/13
 * Time: 7:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessProfileBibStatus extends PersistableBusinessObjectBase {

    private String batchProcessBibStatusId;
    private String batchProcessBibStatus;
    private String batchProcessProfileId;
    private OLEBatchProcessProfileBo oleBatchProcessProfileBo;

    public OLEBatchProcessProfileBo getOleBatchProcessProfileBo() {
        return oleBatchProcessProfileBo;
    }

    public void setOleBatchProcessProfileBo(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        this.oleBatchProcessProfileBo = oleBatchProcessProfileBo;
    }

    public String getBatchProcessBibStatusId() {
        return batchProcessBibStatusId;
    }

    public void setBatchProcessBibStatusId(String batchProcessBibStatusId) {
        this.batchProcessBibStatusId = batchProcessBibStatusId;
    }

    public String getBatchProcessBibStatus() {
        return batchProcessBibStatus;
    }

    public void setBatchProcessBibStatus(String batchProcessBibStatus) {
        this.batchProcessBibStatus = batchProcessBibStatus;
    }

    public String getBatchProcessProfileId() {
        return batchProcessProfileId;
    }

    public void setBatchProcessProfileId(String batchProcessProfileId) {
        this.batchProcessProfileId = batchProcessProfileId;
    }
}
