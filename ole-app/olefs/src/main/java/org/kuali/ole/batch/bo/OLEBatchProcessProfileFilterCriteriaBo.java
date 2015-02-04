package org.kuali.ole.batch.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 7/6/13
 * Time: 4:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessProfileFilterCriteriaBo extends PersistableBusinessObjectBase {

    private String filterId;
    private String filterFieldName;
    private String filterFieldNameText;
    private String filterFieldValue;
    private String filterRangeFrom;
    private String filterRangeTo;
    private String batchProcessProfileId;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    private String dataType;
    private OLEBatchProcessProfileBo oleBatchProcessProfileBo;
    private OLEBatchProcessFilterCriteriaBo oleBatchProcessFilterCriteriaBo;

    public OLEBatchProcessProfileBo getOleBatchProcessProfileBo() {
        return oleBatchProcessProfileBo;
    }

    public void setOleBatchProcessProfileBo(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        this.oleBatchProcessProfileBo = oleBatchProcessProfileBo;
    }

    public String getBatchProcessProfileId() {
        return batchProcessProfileId;
    }

    public void setBatchProcessProfileId(String batchProcessProfileId) {
        this.batchProcessProfileId = batchProcessProfileId;
    }

    public String getFilterId() {
        return filterId;
    }

    public void setFilterId(String filterId) {
        this.filterId = filterId;
    }

    public String getFilterFieldName() {
        if(filterFieldName==null || filterFieldName.isEmpty())
        {
            if(filterFieldNameText!=null && !filterFieldNameText.isEmpty()){
                return filterFieldNameText;
            }
        }
        return filterFieldName;
    }

    public void setFilterFieldName(String filterFieldName) {

        this.filterFieldName = filterFieldName;


    }

    public String getFilterFieldValue() {
        return filterFieldValue;
    }

    public void setFilterFieldValue(String filterFieldValue) {
        this.filterFieldValue = filterFieldValue;
    }

    public String getFilterRangeFrom() {
        return filterRangeFrom;
    }

    public void setFilterRangeFrom(String filterRangeFrom) {
        this.filterRangeFrom = filterRangeFrom;
    }

    public String getFilterRangeTo() {
        return filterRangeTo;
    }

    public void setFilterRangeTo(String filterRangeTo) {
        this.filterRangeTo = filterRangeTo;
    }

    public OLEBatchProcessFilterCriteriaBo getOleBatchProcessFilterCriteriaBo() {
        return oleBatchProcessFilterCriteriaBo;
    }

    public void setOleBatchProcessFilterCriteriaBo(OLEBatchProcessFilterCriteriaBo oleBatchProcessFilterCriteriaBo) {
        this.oleBatchProcessFilterCriteriaBo = oleBatchProcessFilterCriteriaBo;
    }

    public String getFilterFieldNameText() {
        return filterFieldNameText;
    }

    public void setFilterFieldNameText(String filterFieldNameText) {
        this.filterFieldNameText = filterFieldNameText;

        if(filterFieldName==null || filterFieldName.isEmpty())
        {
            if(filterFieldNameText!=null && !filterFieldNameText.isEmpty()){
                this.filterFieldName= filterFieldNameText;
            }
        }

    }
}
