package org.kuali.ole.deliver.form;

import org.kuali.ole.deliver.bo.OLEDeliverNoticeHistory;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.List;

/**
 * Created by maheswarang on 12/18/15.
 */
public class OlePatronLoanNoticesSentForm extends UifFormBase {
    private String message;
    private List<OLEDeliverNoticeHistory> oleDeliverNoticeHistories;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public List<OLEDeliverNoticeHistory> getOleDeliverNoticeHistories() {
        return oleDeliverNoticeHistories;
    }

    public void setOleDeliverNoticeHistories(List<OLEDeliverNoticeHistory> oleDeliverNoticeHistories) {
        this.oleDeliverNoticeHistories = oleDeliverNoticeHistories;
    }
}

