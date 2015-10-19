package org.kuali.ole.deliver.form;

import org.kuali.ole.batch.bo.*;
import org.kuali.ole.deliver.bo.OLEDeliverNoticeSearchResult;
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

    private List<OLEDeliverNoticeSearchResult> oleDeliverNoticeSearchResult;

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

    public List<OLEDeliverNoticeSearchResult> getOleDeliverNoticeSearchResult() {
        if(null == oleDeliverNoticeSearchResult){
            oleDeliverNoticeSearchResult = new ArrayList<>();
        }
        return oleDeliverNoticeSearchResult;
    }

    public void setOleDeliverNoticeSearchResult(List<OLEDeliverNoticeSearchResult> oleDeliverNoticeSearchResult) {
        this.oleDeliverNoticeSearchResult = oleDeliverNoticeSearchResult;
    }
}
