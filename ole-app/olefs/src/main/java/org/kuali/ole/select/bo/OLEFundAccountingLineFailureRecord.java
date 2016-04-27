package org.kuali.ole.select.bo;

import org.kuali.ole.coa.businessobject.OleFundCodeAccountingLine;

public class OLEFundAccountingLineFailureRecord {
    private String errorMessage;
    private OleFundCodeAccountingLine oleFundCodeAccountingLine;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public OleFundCodeAccountingLine getOleFundCodeAccountingLine() {
        return oleFundCodeAccountingLine;
    }

    public void setOleFundCodeAccountingLine(OleFundCodeAccountingLine oleFundCodeAccountingLine) {
        this.oleFundCodeAccountingLine = oleFundCodeAccountingLine;
    }
}
