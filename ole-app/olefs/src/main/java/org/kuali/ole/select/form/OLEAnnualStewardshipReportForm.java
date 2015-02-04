package org.kuali.ole.select.form;

import org.kuali.ole.describe.bo.SearchResultDisplayRow;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 12/6/13
 * Time: 7:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEAnnualStewardshipReportForm extends UifFormBase {
    private String donorCode;
    private String status;
    private Date fromDate;
    private Date toDate;
    private int pageSize;
    List<SearchResultDisplayRow> saerSearchResultDisplayRowList = new ArrayList<SearchResultDisplayRow>();

    public String getDonorCode() {
        return donorCode;
    }

    public void setDonorCode(String donorCode) {
        this.donorCode = donorCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<SearchResultDisplayRow> getSaerSearchResultDisplayRowList() {
        return saerSearchResultDisplayRowList;
    }

    public void setSaerSearchResultDisplayRowList(List<SearchResultDisplayRow> saerSearchResultDisplayRowList) {
        this.saerSearchResultDisplayRowList = saerSearchResultDisplayRowList;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
