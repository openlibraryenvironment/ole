package org.kuali.ole.pojo.edi;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/6/12
 * Time: 5:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class Control {
    private String controlQualifier;
    private String totalQuantitySegments;

    public String getControlQualifier() {
        return controlQualifier;
    }

    public void setControlQualifier(String controlQualifier) {
        this.controlQualifier = controlQualifier;
    }

    public String getTotalQuantitySegments() {
        return totalQuantitySegments;
    }

    public void setTotalQuantitySegments(String totalQuantitySegments) {
        this.totalQuantitySegments = totalQuantitySegments;
    }
}
