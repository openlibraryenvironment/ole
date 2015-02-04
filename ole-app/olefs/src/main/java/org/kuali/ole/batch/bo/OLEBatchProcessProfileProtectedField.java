package org.kuali.ole.batch.bo;

import org.kuali.ole.select.bo.OleGloballyProtectedField;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 7/6/13
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessProfileProtectedField extends PersistableBusinessObjectBase {

    private String oleProfileProtectedFieldId;
    private String dataType;
    private String tag;
    private String firstIndicator;
    private String secondIndicator;
    private String subField;
    private String subFieldContains;
    private boolean ignoreValue;
    private String batchProcessProfileId;
    private String oleGloballyProtectedFieldId;
    private OleGloballyProtectedField oleGloballyProtectedField;
    private OLEBatchProcessProfileBo oleBatchProcessProfileBo;

    public String getOleGloballyProtectedFieldId() {
        return oleGloballyProtectedFieldId;
    }

    public void setOleGloballyProtectedFieldId(String oleGloballyProtectedFieldId) {
        this.oleGloballyProtectedFieldId = oleGloballyProtectedFieldId;
    }

    public OleGloballyProtectedField getOleGloballyProtectedField() {
        return oleGloballyProtectedField;
    }

    public void setOleGloballyProtectedField(OleGloballyProtectedField oleGloballyProtectedField) {
        this.oleGloballyProtectedField = oleGloballyProtectedField;
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

    public String getOleProfileProtectedFieldId() {
        return oleProfileProtectedFieldId;
    }

    public void setOleProfileProtectedFieldId(String oleProfileProtectedFieldId) {
        this.oleProfileProtectedFieldId = oleProfileProtectedFieldId;
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

    public boolean isIgnoreValue() {
        return ignoreValue;
    }

    public void setIgnoreValue(boolean ignoreValue) {
        this.ignoreValue = ignoreValue;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getSubFieldContains() {
        return subFieldContains;
    }

    public void setSubFieldContains(String subFieldContains) {
        this.subFieldContains = subFieldContains;
    }
}
