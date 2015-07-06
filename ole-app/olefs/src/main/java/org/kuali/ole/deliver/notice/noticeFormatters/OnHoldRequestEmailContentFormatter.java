package org.kuali.ole.deliver.notice.noticeFormatters;

import org.kuali.ole.deliver.bo.OleDeliverRequestBo;

/**
 * Created by maheswarang on 6/25/15.
 */
public class OnHoldRequestEmailContentFormatter extends RequestEmailContentFormatter {
    @Override
    public String getCustomItemHeaderInfo(OleDeliverRequestBo oleDeliverRequestBo) {
        return null;
    }

    @Override
    public String getCustomItemFooterInfo(OleDeliverRequestBo oleDeliverRequestBo) {
        return "<TR><TD>Hold Expiration Date :</TD><TD>" + (oleDeliverRequestBo.getHoldExpirationDate() != null ? oleDeliverRequestBo.getHoldExpirationDate() : "") + "</TD></TR>";
    }
}
