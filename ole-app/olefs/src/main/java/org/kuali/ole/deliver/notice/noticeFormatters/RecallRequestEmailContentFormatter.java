package org.kuali.ole.deliver.notice.noticeFormatters;

import org.kuali.ole.deliver.bo.OleDeliverRequestBo;

/**
 * Created by maheswarang on 6/29/15.
 */
public class RecallRequestEmailContentFormatter  extends RequestEmailContentFormatter {
    @Override
    public String getCustomItemHeaderInfo(OleDeliverRequestBo oleDeliverRequestBo) {
        return null;
    }

    @Override
    public String getCustomItemFooterInfo(OleDeliverRequestBo oleDeliverRequestBo) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<TR><TD>Original Due Date :</TD><TD>" + ( oleDeliverRequestBo.getOriginalDueDate()!= null ? oleDeliverRequestBo.getOriginalDueDate() : "") + "</TD></TR>");
        stringBuffer.append("<TR><TD>Recall Due Date :</TD><TD>" + ( oleDeliverRequestBo.getRecallDueDate()!= null ? oleDeliverRequestBo.getRecallDueDate() : "") + "</TD></TR>");
        return stringBuffer.toString();
    }
}
