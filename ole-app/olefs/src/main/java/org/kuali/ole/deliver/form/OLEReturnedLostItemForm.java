package org.kuali.ole.deliver.form;

import org.kuali.ole.deliver.bo.OLEDeliverNoticeSearchResult;
import org.kuali.ole.deliver.bo.OLEReturnedLostItemResult;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gopalp on 4/4/16.
 */
public class OLEReturnedLostItemForm extends UifFormBase {

    private Date dateSentFrom;
    private Date dateSentTo;
    private int startValue=0;
    private int pageSize=10;
    private List<OLEReturnedLostItemResult> oleReturnedLostItemResults;

    public Date getDateSentFrom() {
        return dateSentFrom;
    }

    public void setDateSentFrom(Date dateSentFrom) {
        this.dateSentFrom = dateSentFrom;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartValue() {
        return startValue;
    }

    public void setStartValue(int startValue) {
        this.startValue = startValue;
    }

    public Date getDateSentTo() {
        return dateSentTo;
    }

    public void setDateSentTo(Date dateSentTo) {
        this.dateSentTo = dateSentTo;
    }

    public List<OLEReturnedLostItemResult> getOleReturnedLostItemResults() {
        return oleReturnedLostItemResults;
    }

    public void setOleReturnedLostItemResults(List<OLEReturnedLostItemResult> oleReturnedLostItemResults) {
        this.oleReturnedLostItemResults = oleReturnedLostItemResults;
    }

    public void reset(){
        oleReturnedLostItemResults = new ArrayList<>();
    }
}
