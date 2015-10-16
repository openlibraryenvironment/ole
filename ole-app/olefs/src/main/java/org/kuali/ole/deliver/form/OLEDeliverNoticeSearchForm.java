package org.kuali.ole.deliver.form;

import org.kuali.ole.batch.bo.*;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chenchulakshmig on 10/16/15.
 */
public class OLEDeliverNoticeSearchForm extends UifFormBase {

    private String patronBarcode;
    private String itemBarcode;
    private Date dateSentFrom;
    private Date dateSentTo;
    private String deskLocation;
    private String noticeType;
    private List<OLECourtesyNotice> oleCourtesyNoticeList = new ArrayList<>();
    private List<OLEExpiredRequestNotice> oleExpiredRequestNoticeList = new ArrayList<>();
    private List<OLEOnHoldNotice> oleOnHoldNoticeList = new ArrayList<>();
    private List<OLEOnHoldCourtesyNotice> oleOnHoldCourtesyNoticeList = new ArrayList<>();
    private List<OLEOverDueNotice> oleOverDueNoticeList = new ArrayList<>();
    private List<OLERecallNotice> oleRecallNoticeList = new ArrayList<>();
    private List<OLEPickupNotice> olePickupNoticeList = new ArrayList<>();

    public String getPatronBarcode() {
        return patronBarcode;
    }

    public void setPatronBarcode(String patronBarcode) {
        this.patronBarcode = patronBarcode;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public Date getDateSentFrom() {
        return dateSentFrom;
    }

    public void setDateSentFrom(Date dateSentFrom) {
        this.dateSentFrom = dateSentFrom;
    }

    public Date getDateSentTo() {
        return dateSentTo;
    }

    public void setDateSentTo(Date dateSentTo) {
        this.dateSentTo = dateSentTo;
    }

    public String getDeskLocation() {
        return deskLocation;
    }

    public void setDeskLocation(String deskLocation) {
        this.deskLocation = deskLocation;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public List<OLECourtesyNotice> getOleCourtesyNoticeList() {
        return oleCourtesyNoticeList;
    }

    public void setOleCourtesyNoticeList(List<OLECourtesyNotice> oleCourtesyNoticeList) {
        this.oleCourtesyNoticeList = oleCourtesyNoticeList;
    }

    public List<OLEExpiredRequestNotice> getOleExpiredRequestNoticeList() {
        return oleExpiredRequestNoticeList;
    }

    public void setOleExpiredRequestNoticeList(List<OLEExpiredRequestNotice> oleExpiredRequestNoticeList) {
        this.oleExpiredRequestNoticeList = oleExpiredRequestNoticeList;
    }

    public List<OLEOnHoldNotice> getOleOnHoldNoticeList() {
        return oleOnHoldNoticeList;
    }

    public void setOleOnHoldNoticeList(List<OLEOnHoldNotice> oleOnHoldNoticeList) {
        this.oleOnHoldNoticeList = oleOnHoldNoticeList;
    }

    public List<OLEOnHoldCourtesyNotice> getOleOnHoldCourtesyNoticeList() {
        return oleOnHoldCourtesyNoticeList;
    }

    public void setOleOnHoldCourtesyNoticeList(List<OLEOnHoldCourtesyNotice> oleOnHoldCourtesyNoticeList) {
        this.oleOnHoldCourtesyNoticeList = oleOnHoldCourtesyNoticeList;
    }

    public List<OLEOverDueNotice> getOleOverDueNoticeList() {
        return oleOverDueNoticeList;
    }

    public void setOleOverDueNoticeList(List<OLEOverDueNotice> oleOverDueNoticeList) {
        this.oleOverDueNoticeList = oleOverDueNoticeList;
    }

    public List<OLERecallNotice> getOleRecallNoticeList() {
        return oleRecallNoticeList;
    }

    public void setOleRecallNoticeList(List<OLERecallNotice> oleRecallNoticeList) {
        this.oleRecallNoticeList = oleRecallNoticeList;
    }

    public List<OLEPickupNotice> getOlePickupNoticeList() {
        return olePickupNoticeList;
    }

    public void setOlePickupNoticeList(List<OLEPickupNotice> olePickupNoticeList) {
        this.olePickupNoticeList = olePickupNoticeList;
    }
}
