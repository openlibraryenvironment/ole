package org.kuali.ole.batch.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 7/26/13
 * Time: 3:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessProfileInstanceMatchPoint extends PersistableBusinessObjectBase {

    private String oleInstanceMatchPointId;
    private String oleInstanceMatchPointName;
    private String batchProcessProfileId;
    private OLEBatchProcessProfileBo oleBatchProcessProfileBo;

    public OLEBatchProcessProfileBo getOleBatchProcessProfileBo() {
        return oleBatchProcessProfileBo;
    }

    public void setOleBatchProcessProfileBo(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        this.oleBatchProcessProfileBo = oleBatchProcessProfileBo;
    }

    public String getOleInstanceMatchPointId() {
        return oleInstanceMatchPointId;
    }

    public void setOleInstanceMatchPointId(String oleInstanceMatchPointId) {
        this.oleInstanceMatchPointId = oleInstanceMatchPointId;
    }

    public String getOleInstanceMatchPointName() {
        return oleInstanceMatchPointName;
    }

    public void setOleInstanceMatchPointName(String oleInstanceMatchPointName) {
        this.oleInstanceMatchPointName = oleInstanceMatchPointName;
    }

    public String getBatchProcessProfileId() {
        return batchProcessProfileId;
    }

    public void setBatchProcessProfileId(String batchProcessProfileId) {
        this.batchProcessProfileId = batchProcessProfileId;
    }


}
