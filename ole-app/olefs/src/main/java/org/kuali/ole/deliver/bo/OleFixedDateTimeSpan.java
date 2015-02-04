package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/26/12
 * Time: 8:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleFixedDateTimeSpan extends PersistableBusinessObjectBase {

    private String OleFixedDateTimeSpanId;
    private OleFixedDueDate oleFixedDueDate;
    private String fixedDueDateId;
    private Date fixedDueDate;
    private Date fromDueDate;
    private Date toDueDate;
    private String timeSpan;

    public OleFixedDueDate getOleFixedDueDate() {
        return oleFixedDueDate;
    }

    public void setOleFixedDueDate(OleFixedDueDate oleFixedDueDate) {
        this.oleFixedDueDate = oleFixedDueDate;
    }

    public String getFixedDueDateId() {
        return fixedDueDateId;
    }

    public void setFixedDueDateId(String fixedDueDateId) {
        this.fixedDueDateId = fixedDueDateId;
    }

    public String getOleFixedDateTimeSpanId() {
        return OleFixedDateTimeSpanId;
    }

    public void setOleFixedDateTimeSpanId(String oleFixedDateTimeSpanId) {
        OleFixedDateTimeSpanId = oleFixedDateTimeSpanId;
    }

    public Date getFixedDueDate() {
        return fixedDueDate;
    }

    public void setFixedDueDate(Date fixedDueDate) {
        this.fixedDueDate = fixedDueDate;
    }

    public Date getFromDueDate() {
        return fromDueDate;
    }

    public void setFromDueDate(Date fromDueDate) {
        this.fromDueDate = fromDueDate;
    }

    public Date getToDueDate() {
        return toDueDate;
    }

    public void setToDueDate(Date toDueDate) {
        this.toDueDate = toDueDate;
    }

    public String getTimeSpan() {
        return getFromDueDate() + "-" + getToDueDate();
    }

    public void setTimeSpan(String timeSpan) {
        this.timeSpan = timeSpan;
    }
}
