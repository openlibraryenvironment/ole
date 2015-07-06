package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Blob;
import java.sql.Timestamp;

/**
 * Created by maheswarang on 9/15/14.
 */
public class OLEDeliverNoticeHistory extends PersistableBusinessObjectBase {
    private String id;
    private String loanId;
    private Timestamp noticeSentDate;
    private String patronId;
    private String noticeType;
    private String noticeSendType;
    private byte[] noticeContent;
    private String requestId;
    private String itemBarcode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public Timestamp getNoticeSentDate() {
        return noticeSentDate;
    }

    public void setNoticeSentDate(Timestamp noticeSentDate) {
        this.noticeSentDate = noticeSentDate;
    }

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getNoticeSendType() {
        return noticeSendType;
    }

    public void setNoticeSendType(String noticeSendType) {
        this.noticeSendType = noticeSendType;
    }

    public void setNoticeContent(byte[] noticeContent) {
        this.noticeContent = noticeContent;
    }

    public byte[] getNoticeContent() {
        return noticeContent;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }
}
