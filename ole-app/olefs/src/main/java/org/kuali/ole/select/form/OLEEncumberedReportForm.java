package org.kuali.ole.select.form;

import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 12/6/13
 * Time: 12:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEEncumberedReportForm extends UifFormBase {
    private String donorCode;
    private String donorId;
    private Date fromDate;
    private Date toDate;
    private List<OLEDonor> encumberedReportDocumentList = new ArrayList<OLEDonor>();

    public String getDonorCode() {
        return donorCode;
    }

    public void setDonorCode(String donorCode) {
        this.donorCode = donorCode;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public List<OLEDonor> getEncumberedReportDocumentList() {
        return encumberedReportDocumentList;
    }

    public void setEncumberedReportDocumentList(List<OLEDonor> encumberedReportDocumentList) {
        this.encumberedReportDocumentList = encumberedReportDocumentList;
    }
}
