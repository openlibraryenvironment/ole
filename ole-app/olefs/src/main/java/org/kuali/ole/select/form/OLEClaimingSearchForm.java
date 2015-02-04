package org.kuali.ole.select.form;

import org.kuali.ole.select.bo.OLEPOClaimHistory;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 1/22/14
 * Time: 2:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEClaimingSearchForm extends UifFormBase {

    private String vendorName;
    private String title;
    private Date claimDate;
    private String claimErrorMessage;

    private String successMsg;

    public String getSuccessMsg() {
        return successMsg;
    }

    public void setSuccessMsg(String successMsg) {
        this.successMsg = successMsg;
    }


    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(Date claimDate) {
        this.claimDate = claimDate;
    }

    private List<OLEPOClaimHistory> oleClaimingSearchRecordList = new ArrayList<>();

    public List<OLEPOClaimHistory> getOleClaimingSearchRecordList() {
        return oleClaimingSearchRecordList;
    }

    public void setOleClaimingSearchRecordList(List<OLEPOClaimHistory> oleClaimingSearchRecordList) {
        this.oleClaimingSearchRecordList = oleClaimingSearchRecordList;
    }

    public String getClaimErrorMessage() {
        return claimErrorMessage;
    }

    public void setClaimErrorMessage(String claimErrorMessage) {
        this.claimErrorMessage = claimErrorMessage;
    }
}
