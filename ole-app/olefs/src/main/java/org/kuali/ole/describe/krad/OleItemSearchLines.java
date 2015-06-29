package org.kuali.ole.describe.krad;

import org.kuali.ole.deliver.bo.OLEBibSearchResultDisplayRow;
import org.kuali.ole.describe.bo.SearchResultDisplayRow;
import org.kuali.rice.krad.uif.component.Component;

/**
 * Created by jayabharathreddy on 6/29/15.
 */
public class OleItemSearchLines {
    private int lineNumber;
    private String lineId;
    private String bindPath;
    private OLEBibSearchResultDisplayRow deliverRow;
    private Component rowDetails;

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


    public OLEBibSearchResultDisplayRow getDeliverRow() {
        return deliverRow;
    }

    public void setDeliverRow(OLEBibSearchResultDisplayRow deliverRow) {
        this.deliverRow = deliverRow;
    }

    public Component getRowDetails() {
        return rowDetails;
    }

    public void setRowDetails(Component rowDetails) {
        this.rowDetails = rowDetails;
    }

    @Override
    public String toString() {
        return "OleSearchLine [row=" + deliverRow + "]";
    }

}

