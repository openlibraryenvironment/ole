package org.kuali.ole.deliver.calendar.bo;

import org.kuali.rice.krad.document.TransactionalDocumentBase;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/21/13
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCalendarDocument extends TransactionalDocumentBase {

    private Timestamp beginDate;
    private Timestamp endDate;
    private String openTime;
    private String closeTime;
    private String startDay;
    private String endDay;
    private String openTimeSession;
    private String closeTimeSession;


    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public String getOpenTimeSession() {
        return openTimeSession;
    }

    public void setOpenTimeSession(String openTimeSession) {
        this.openTimeSession = openTimeSession;
    }

    public String getCloseTimeSession() {
        return closeTimeSession;
    }

    public void setCloseTimeSession(String closeTimeSession) {
        this.closeTimeSession = closeTimeSession;
    }

    public Timestamp getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Timestamp beginDate) {
        this.beginDate = beginDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }
}
