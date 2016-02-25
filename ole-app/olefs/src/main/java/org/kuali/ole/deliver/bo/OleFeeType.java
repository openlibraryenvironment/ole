package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

public class OleFeeType extends PersistableBusinessObjectBase {

    private String feeTypeId;
    private String feeTypeCode;
    private String feeTypeName;
    private String circulationDeskId;
    private OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();


    public OleCirculationDesk getOleCirculationDesk() {
        return oleCirculationDesk;
    }

    public void setOleCirculationDesk(OleCirculationDesk oleCirculationDesk) {
        this.oleCirculationDesk = oleCirculationDesk;
    }

    public String getCirculationDeskId() {
        return circulationDeskId;
    }

    public void setCirculationDeskId(String circulationDeskId) {
        this.circulationDeskId = circulationDeskId;
    }

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