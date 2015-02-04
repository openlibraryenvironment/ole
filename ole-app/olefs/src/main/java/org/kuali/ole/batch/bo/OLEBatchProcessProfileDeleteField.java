package org.kuali.ole.batch.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 7/27/13
 * Time: 1:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessProfileDeleteField extends PersistableBusinessObjectBase{
    private String id;
    private String batchProcessProfileId;
    private String tag;
    private String firstIndicator;
    private String secondIndicator;
    private String subField;
    private String subFieldContains;
    private OLEBatchProcessProfileBo oleBatchProcessProfileBo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBatchProcessProfileId() {
        return batchProcessProfileId;
    }

    public void setBatchProcessProfileId(String batchProcessProfileId) {
        this.batchProcessProfileId = batchProcessProfileId;
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

    public void setSecondIndicator(String secondIndicator) {
        this.secondIndicator = secondIndicator;
    }

    public String getSubField() {
        return subField;
    }

    public void setSubField(String subField) {
        this.subField = subField;
    }

    public String getSubFieldContains() {
        return subFieldContains;
    }

    public void setSubFieldContains(String subFieldContains) {
        this.subFieldContains = subFieldContains;
    }

    public OLEBatchProcessProfileBo getOleBatchProcessProfileBo() {
        return oleBatchProcessProfileBo;
    }

    public void setOleBatchProcessProfileBo(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        this.oleBatchProcessProfileBo = oleBatchProcessProfileBo;
    }
}
