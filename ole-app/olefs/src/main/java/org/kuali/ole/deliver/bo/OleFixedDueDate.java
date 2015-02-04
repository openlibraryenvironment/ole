package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/18/12
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleFixedDueDate extends PersistableBusinessObjectBase {

    private String circulationPolicySetId;
    private String fixedDueDateId;
    private List<OleFixedDateTimeSpan> oleFixedDateTimeSpanList = new ArrayList<OleFixedDateTimeSpan>();
    private List<OleFixedDateTimeSpan> oleDeleteFixedDateTimeSpanList = new ArrayList<OleFixedDateTimeSpan>();

    public List<OleFixedDateTimeSpan> getOleFixedDateTimeSpanList() {
        return oleFixedDateTimeSpanList;
    }

    public void setOleFixedDateTimeSpanList(List<OleFixedDateTimeSpan> oleFixedDateTimeSpanList) {
        this.oleFixedDateTimeSpanList = oleFixedDateTimeSpanList;
    }

    public String getCirculationPolicySetId() {
        return circulationPolicySetId;
    }

    public void setCirculationPolicySetId(String circulationPolicySetId) {
        this.circulationPolicySetId = circulationPolicySetId;
    }

    public String getFixedDueDateId() {
        return fixedDueDateId;
    }

    public void setFixedDueDateId(String fixedDueDateId) {
        this.fixedDueDateId = fixedDueDateId;
    }

    public List<OleFixedDateTimeSpan> getOleDeleteFixedDateTimeSpanList() {
        return oleDeleteFixedDateTimeSpanList;
    }

    public void setOleDeleteFixedDateTimeSpanList(List<OleFixedDateTimeSpan> oleDeleteFixedDateTimeSpanList) {
        this.oleDeleteFixedDateTimeSpanList = oleDeleteFixedDateTimeSpanList;
    }
}
