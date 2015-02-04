package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;


/**
 * Created by chenchulakshmig on 10/30/14.
 */
public class OLEGOKbMappingValue extends PersistableBusinessObjectBase {

    private String gokbMappingId;

    private String recordId;

    private String dataElementId;

    private String gokbValue;

    private String localValue;

    private String recordType;

    private boolean reset;

    private boolean gokbFlag;

    private GOKbDataElement goKbDataElement;

    public String getGokbMappingId() {
        return gokbMappingId;
    }

    public void setGokbMappingId(String gokbMappingId) {
        this.gokbMappingId = gokbMappingId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getDataElementId() {
        return dataElementId;
    }

    public void setDataElementId(String dataElementId) {
        this.dataElementId = dataElementId;
    }

    public String getGokbValue() {
        return gokbValue;
    }

    public void setGokbValue(String gokbValue) {
        this.gokbValue = gokbValue;
    }

    public String getLocalValue() {
        return localValue;
    }

    public void setLocalValue(String localValue) {
        this.localValue = localValue;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public GOKbDataElement getGoKbDataElement() {
        return goKbDataElement;
    }

    public void setGoKbDataElement(GOKbDataElement goKbDataElement) {
        this.goKbDataElement = goKbDataElement;
    }

    public boolean isGokbFlag() {
        return gokbFlag;
    }

    public void setGokbFlag(boolean gokbFlag) {
        this.gokbFlag = gokbFlag;
    }
}
