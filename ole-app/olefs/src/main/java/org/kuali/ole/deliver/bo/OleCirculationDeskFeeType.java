package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hemalathas on 2/18/16.
 */
public class OleCirculationDeskFeeType extends PersistableBusinessObjectBase {

    private String circulationDeskFeeTypeId;
    private String circulationDeskId;
    private String feeTypeCode;
    private String feeTypeId;
    private OleFeeType oleFeeType = new OleFeeType();
    private OleCirculationDesk oleCirculationDesk = new OleCirculationDesk();

    public OleCirculationDesk getOleCirculationDesk() {
        return oleCirculationDesk;
    }

    public void setOleCirculationDesk(OleCirculationDesk oleCirculationDesk) {
        this.oleCirculationDesk = oleCirculationDesk;
    }

    public OleFeeType getOleFeeType() {
        return oleFeeType;
    }

    public void setOleFeeType(OleFeeType oleFeeType) {
        this.oleFeeType = oleFeeType;
    }

    public String getCirculationDeskFeeTypeId() {
        return circulationDeskFeeTypeId;
    }

    public void setCirculationDeskFeeTypeId(String circulationDeskFeeTypeId) {
        this.circulationDeskFeeTypeId = circulationDeskFeeTypeId;
    }

    public String getCirculationDeskId() {
        return circulationDeskId;
    }

    public void setCirculationDeskId(String circulationDeskId) {
        this.circulationDeskId = circulationDeskId;
    }

    public String getFeeTypeCode() {

        if(feeTypeId != null){


        Map<String,String> map = new HashMap<>();
        map.put("feeTypeId",feeTypeId);
        List<OleFeeType> oleFeeTypes = (List<OleFeeType>) KRADServiceLocator.getBusinessObjectService().findMatching(OleFeeType.class,map);
        if(oleFeeTypes.get(0)!=null){
            feeTypeCode = oleFeeTypes.get(0).getFeeTypeCode();
        }
        }


        return feeTypeCode;
}

    public void setFeeTypeCode(String feeTypeCode) {
        this.feeTypeCode = feeTypeCode;
    }

    public String getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(String feeTypeId) {
        this.feeTypeId = feeTypeId;
    }
}
