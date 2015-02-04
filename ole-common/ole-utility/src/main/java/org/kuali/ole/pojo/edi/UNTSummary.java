package org.kuali.ole.pojo.edi;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/6/12
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class UNTSummary {
    private String segmentNumber;
    private String linSegmentTotal;

    public String getSegmentNumber() {
        return segmentNumber;
    }

    public void setSegmentNumber(String segmentNumber) {
        this.segmentNumber = segmentNumber;
    }

    public String getLinSegmentTotal() {
        return linSegmentTotal;
    }

    public void setLinSegmentTotal(String linSegmentTotal) {
        this.linSegmentTotal = linSegmentTotal;
    }
}
