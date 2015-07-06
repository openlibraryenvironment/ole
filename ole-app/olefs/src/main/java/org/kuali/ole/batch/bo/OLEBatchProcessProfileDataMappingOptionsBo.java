package org.kuali.ole.batch.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 7/6/13
 * Time: 4:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessProfileDataMappingOptionsBo  extends PersistableBusinessObjectBase implements Comparable<OLEBatchProcessProfileDataMappingOptionsBo>{

    private String oleBatchProcessProfileDataMappingOptionId;
    private String oleBatchProcessDataMapId;
    private String fileType;
    private String dataType;
    private String dataTypeDestinationField;
    private String sourceField;
    private String sourceFieldText;
    private String sourceFieldValue;
    private String destinationField;
    private String destinationFieldText;
    private String destinationFieldValue;
    private String gokbField;
    private int priority;
    private boolean lookUp;
    private OLEBatchProcessProfileMappingOptionsBo oleBatchProcessProfileMappingOptionsBo;
    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    public String getOleBatchProcessDataMapId() {
        return oleBatchProcessDataMapId;
    }

    public void setOleBatchProcessDataMapId(String oleBatchProcessDataMapId) {
        this.oleBatchProcessDataMapId = oleBatchProcessDataMapId;
    }

    public String getOleBatchProcessProfileDataMappingOptionId() {
        return oleBatchProcessProfileDataMappingOptionId;
    }

    public void setOleBatchProcessProfileDataMappingOptionId(String oleBatchProcessProfileDataMappingOptionId) {
        this.oleBatchProcessProfileDataMappingOptionId = oleBatchProcessProfileDataMappingOptionId;
    }

    public OLEBatchProcessProfileMappingOptionsBo getOleBatchProcessProfileMappingOptionsBo() {
        return oleBatchProcessProfileMappingOptionsBo;
    }

    public void setOleBatchProcessProfileMappingOptionsBo(OLEBatchProcessProfileMappingOptionsBo oleBatchProcessProfileMappingOptionsBo) {
        this.oleBatchProcessProfileMappingOptionsBo = oleBatchProcessProfileMappingOptionsBo;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getSourceField() {
        if(sourceField==null || sourceField.isEmpty())
        {
            if(sourceFieldText!=null && !sourceFieldText.isEmpty()){
                return sourceFieldText;
            }
        }
        return sourceField;
    }

    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }

    public String getSourceFieldValue() {
        return sourceFieldValue;
    }

    public void setSourceFieldValue(String sourceFieldValue) {
        this.sourceFieldValue = sourceFieldValue;
    }

    public String getDestinationField() {
        if(destinationField==null || destinationField.isEmpty())
        {
            if(destinationFieldText!=null && !destinationFieldText.isEmpty()){
                return destinationFieldText;
            }
        }
        return destinationField;
    }

    public void setDestinationField(String destinationField) {
        this.destinationField = destinationField;
    }

    public String getDestinationFieldValue() {
        return destinationFieldValue;
    }

    public void setDestinationFieldValue(String destinationFieldValue) {
        this.destinationFieldValue = destinationFieldValue;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isLookUp() {
        return lookUp;
    }

    public void setLookUp(boolean lookUp) {
        this.lookUp = lookUp;
    }

    public String getSourceFieldText() {
        return sourceFieldText;
    }

    public void setSourceFieldText(String sourceFieldText) {
        this.sourceFieldText = sourceFieldText;

        if(sourceField==null || sourceField.isEmpty())
        {
            if(sourceFieldText!=null && !sourceFieldText.isEmpty()){
                this.sourceField= sourceFieldText;
            }
        }
    }

    public String getDestinationFieldText() {
        return destinationFieldText;
    }

    public void setDestinationFieldText(String destinationFieldText) {
        this.destinationFieldText = destinationFieldText;

        if(destinationField==null || destinationField.isEmpty())
        {
            if(destinationFieldText!=null && !destinationFieldText.isEmpty()){
                this.destinationField= destinationFieldText;
            }
        }
    }

    public String getDataTypeDestinationField() {
        return dataTypeDestinationField;
    }

    public void setDataTypeDestinationField(String dataTypeDestinationField) {
        this.dataTypeDestinationField = dataTypeDestinationField;
    }

    public String getGokbField() {
        return gokbField;
    }

    public void setGokbField(String gokbField) {
        this.gokbField = gokbField;
    }

    @Override
    public int compareTo(OLEBatchProcessProfileDataMappingOptionsBo o) {

        if(this.getDataTypeDestinationField().equals(o.getDataTypeDestinationField())) {
            if(this.getDestinationField().equals(o.getDestinationField())) {
               return this.getPriority() - o.getPriority();
            }
            else {
                return this.getDestinationField().compareTo(o.getDestinationField());
            }
        }
        else {
            return this.getDataTypeDestinationField().compareTo(o.getDataTypeDestinationField());
        }

    }
}
