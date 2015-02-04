package org.kuali.ole.batch.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 7/6/13
 * Time: 4:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessProfileConstantsBo extends PersistableBusinessObjectBase {

    private String oleBatchProcessProfileConstantsId;
    private String attributeName;
    private String dataType;
    private String attributeNameText;
    private String attributeValue;
    private String attributeValueText;
    private String oldAttributeName;

    /*public boolean isDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    private boolean defaultValue;*/
    private String defaultValue;
    private String batchProcessProfileId;
    private OLEBatchProcessProfileBo oleBatchProcessProfileBo;

    public String getOleBatchProcessProfileConstantsId() {
        return oleBatchProcessProfileConstantsId;
    }

    public void setOleBatchProcessProfileConstantsId(String oleBatchProcessProfileConstantsId) {
        this.oleBatchProcessProfileConstantsId = oleBatchProcessProfileConstantsId;
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

    public String getAttributeName() {
        if(attributeName==null || attributeName.isEmpty())
        {
            if(attributeNameText!=null && !attributeNameText.isEmpty()){
                return attributeNameText;
            }
        }
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        if(attributeValue==null || attributeValue.isEmpty())
        {
            if(attributeValueText!=null && !attributeValueText.isEmpty()){
                return attributeValueText;
            }
        }
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getAttributeNameText() {
        return attributeNameText;
    }

    public void setAttributeNameText(String attributeNameText) {
        this.attributeNameText = attributeNameText;

        if(attributeName==null || attributeName.isEmpty())
        {
            if(attributeNameText!=null && !attributeNameText.isEmpty()){
                this.attributeName= attributeNameText;
            }
        }

    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getAttributeValueText() {
        return attributeValueText;
    }

    public void setAttributeValueText(String attributeValueText) {
        this.attributeValueText = attributeValueText;

        if(attributeValue==null || attributeValue.isEmpty())
        {
            if(attributeValueText!=null && !attributeValueText.isEmpty()){
                this.attributeValue= attributeValueText;
            }
        }
    }

    public String getOldAttributeName() {
        return oldAttributeName;
    }

    public void setOldAttributeName(String oldAttributeName) {
        this.oldAttributeName = oldAttributeName;
    }
}
