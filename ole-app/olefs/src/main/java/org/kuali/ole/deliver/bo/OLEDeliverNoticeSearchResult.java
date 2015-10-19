package org.kuali.ole.deliver.bo;

import java.util.Date;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 10/19/15.
 */
public class OLEDeliverNoticeSearchResult {

    private String noticeContent;
    private Date dateSentTo;
    private String noticeType;
    private String patronId;

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public Date getDateSentTo() {
        return dateSentTo;
    }

    public void setDateSentTo(Date dateSentTo) {
        this.dateSentTo = dateSentTo;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }
}
