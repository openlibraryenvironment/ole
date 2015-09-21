package org.kuali.ole.describe.krad;


import org.kuali.ole.deliver.bo.OleLoanDocument;

/**
 * Created by jayabharathreddy on 8/14/15.
 */
public class ExistingSearchLine {
    private int lineNumber;
    private String lineId;
    private String bindPath;
    private OleLoanDocument row;

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public OleLoanDocument getRow() {
        return row;
    }

    public void setRow(OleLoanDocument row) {
        this.row = row;
    }

    public String getBindPath() {
        return bindPath;
    }

    public void setBindPath(String bindPath) {
        this.bindPath = bindPath;
    }



    @Override
    public String toString() {
        return "OleLoanDocument [row=" + row + "]";
    }

}
