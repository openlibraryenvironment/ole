package org.kuali.ole.batch.form;

import org.kuali.ole.batch.bo.*;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * To change this template use File | Settings | File Templates.
 */
public class OLEClaimNoticeForm extends UifFormBase {


    private List<OLEClaimNotice> oleClaimNoticeList;


    public List<OLEClaimNotice> getOleClaimNoticeList() {
        return oleClaimNoticeList;
    }

    public void setOleClaimNoticeList(List<OLEClaimNotice> oleClaimNoticeList) {
        this.oleClaimNoticeList = oleClaimNoticeList;
    }

}
