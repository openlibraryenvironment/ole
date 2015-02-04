package org.kuali.ole.batch.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: krishnamohanv
 * Date: 7/8/13
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessTypeBo extends PersistableBusinessObjectBase {

    private String batchProcessTypeId;
    private String batchProcessTypeName;
    private String batchProcessTypeCode;
    private boolean active;


    public String getBatchProcessTypeId() {
        return batchProcessTypeId;
    }

    public void setBatchProcessTypeId(String batchProcessTypeId) {
        this.batchProcessTypeId = batchProcessTypeId;
    }

    public String getBatchProcessTypeName() {
        return batchProcessTypeName;
    }

    public void setBatchProcessTypeName(String batchProcessTypeName) {
        this.batchProcessTypeName = batchProcessTypeName;
    }

    public String getBatchProcessTypeCode() {
        return batchProcessTypeCode;
    }

    public void setBatchProcessTypeCode(String batchProcessTypeCode) {
        this.batchProcessTypeCode = batchProcessTypeCode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
