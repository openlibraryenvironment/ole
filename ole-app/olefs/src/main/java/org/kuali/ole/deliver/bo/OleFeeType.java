package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

public class OleFeeType extends PersistableBusinessObjectBase {

    private String feeTypeId;
    private String feeTypeCode;
    private String feeTypeName;

    public String getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(String feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    public String getFeeTypeCode() {
        return feeTypeCode;
    }

    public void setFeeTypeCode(String feeTypeCode) {
        this.feeTypeCode = feeTypeCode;
    }

    public String getFeeTypeName() {
        return feeTypeName;
    }

    public void setFeeTypeName(String feeTypeName) {
        this.feeTypeName = feeTypeName;
    }


    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("feeTypeId", feeTypeId);
        return toStringMap;
    }

}