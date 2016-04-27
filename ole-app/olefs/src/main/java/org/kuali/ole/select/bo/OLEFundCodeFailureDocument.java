package org.kuali.ole.select.bo;

import org.kuali.ole.coa.businessobject.OleFundCode;

public class OLEFundCodeFailureDocument {
    private String errorMessage;
    private OleFundCode oleFundCodeDocument;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public OleFundCode getOleFundCodeDocument() {
        return oleFundCodeDocument;
    }

    public void setOleFundCodeDocument(OleFundCode oleFundCodeDocument) {
        this.oleFundCodeDocument = oleFundCodeDocument;
    }
}
