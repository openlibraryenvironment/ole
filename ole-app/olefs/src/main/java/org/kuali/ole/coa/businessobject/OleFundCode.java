package org.kuali.ole.coa.businessobject;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jating on 19/11/14.
 */

public class OleFundCode extends PersistableBusinessObjectBase {


    private String fundCodeId;

    private String fundCode;

    private boolean active;

    private List<OleFundCodeAccountingLine> oleFundCodeAccountingLineList  = new ArrayList<OleFundCodeAccountingLine>();

    public String getFundCodeId() {
        return fundCodeId;
    }

    public void setFundCodeId(String fundCodeId) {
        this.fundCodeId = fundCodeId;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public List<OleFundCodeAccountingLine> getOleFundCodeAccountingLineList() {
        return oleFundCodeAccountingLineList;
    }

    public void setOleFundCodeAccountingLineList(List<OleFundCodeAccountingLine> oleFundCodeAccountingLineList) {
        this.oleFundCodeAccountingLineList = oleFundCodeAccountingLineList;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


}
