package org.kuali.ole.batch.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 7/26/13
 * Time: 3:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessProfileBibMatchPoint extends PersistableBusinessObjectBase {

    private String oleBibMatchPointId;
    private String oleBibMatchPoint;
    private String batchProcessProfileId;
    private OLEBatchProcessProfileBo oleBatchProcessProfileBo;

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

    public String getOleBibMatchPointId() {
        return oleBibMatchPointId;
    }

    public void setOleBibMatchPointId(String oleBibMatchPointId) {
        this.oleBibMatchPointId = oleBibMatchPointId;
    }

    public String getOleBibMatchPoint() {
        return oleBibMatchPoint;
    }

    public void setOleBibMatchPoint(String oleBibMatchPoint) {
        this.oleBibMatchPoint = oleBibMatchPoint;
    }
}
