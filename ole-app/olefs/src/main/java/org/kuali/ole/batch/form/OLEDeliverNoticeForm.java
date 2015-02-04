package org.kuali.ole.batch.form;

import org.kuali.ole.batch.bo.*;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 8/7/13
 * Time: 7:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEDeliverNoticeForm extends UifFormBase {

    private List<OLECourtesyNotice> oleCourtesyNoticeList;
    private List<OLEExpiredRequestNotice> oleExpiredRequestNoticeList;
    private List<OLEOnHoldNotice> oleOnHoldNoticeList;
    private List<OLEOnHoldCourtesyNotice> oleOnHoldCourtesyNoticeList;
    private List<OLEOverDueNotice>  oleOverDueNoticeList;
    private List<OLERecallNotice> oleRecallNoticeList;
    private List<OLEPickupNotice> olePickupNoticeList;

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
