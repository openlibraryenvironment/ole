package org.kuali.ole.describe.krad;

import org.kuali.ole.select.document.OLEEResourceInstance;


/**
 * Created by jayabharathreddy on 10/20/15.
 */
public class OLEEResourceSearchLine {
    private int lineNumber;
    private String lineId;
    private String bindPath;
    private OLEEResourceInstance row;

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

    public String getBindPath() {
        return bindPath;
    }

    public void setBindPath(String bindPath) {
        this.bindPath = bindPath;
    }

    public OLEEResourceInstance getRow() {
        return row;
    }

    public void setRow(OLEEResourceInstance row) {
        this.row = row;
    }

    @Override
    public String toString() {
        return "OLEEResourceInstance [row=" + row + "]";
    }

}
