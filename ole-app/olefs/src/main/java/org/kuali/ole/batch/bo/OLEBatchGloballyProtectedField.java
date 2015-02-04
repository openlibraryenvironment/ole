package org.kuali.ole.batch.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 7/10/13
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchGloballyProtectedField extends PersistableBusinessObjectBase {
    private String id;
    private String globallyProtectedFieldId;
    private String tag;
    private String firstIndicator;
    private String secondIndicator;
    private String subField;
    private boolean ignoreValue;
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

    public String getGloballyProtectedFieldId() {
        return globallyProtectedFieldId;
    }

    public void setGloballyProtectedFieldId(String globallyProtectedFieldId) {
        this.globallyProtectedFieldId = globallyProtectedFieldId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFirstIndicator() {
        return firstIndicator;
    }

    public void setFirstIndicator(String firstIndicator) {
        this.firstIndicator = firstIndicator;
    }

    public String getSecondIndicator() {
        return secondIndicator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSecondIndicator(String secondIndicator) {
        this.secondIndicator = secondIndicator;
    }

    public String getSubField() {
        return subField;
    }

    public void setSubField(String subField) {
        this.subField = subField;
    }

    public boolean isIgnoreValue() {
        return ignoreValue;
    }

    public void setIgnoreValue(boolean ignoreValue) {
        this.ignoreValue = ignoreValue;
    }
}
