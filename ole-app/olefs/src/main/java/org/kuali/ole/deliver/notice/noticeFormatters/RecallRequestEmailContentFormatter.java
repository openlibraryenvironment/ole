package org.kuali.ole.deliver.notice.noticeFormatters;

import org.kuali.ole.deliver.bo.OleDeliverRequestBo;

import java.util.Map;

/**
 * Created by maheswarang on 6/29/15.
 */
public class RecallRequestEmailContentFormatter  extends RequestEmailContentFormatter {
    @Override
    public String getCustomItemHeaderInfo(OleDeliverRequestBo oleDeliverRequestBo,Map<String,String> fieldLabelMap) {
        return null;
    }

    @Override
    public String getCustomItemFooterInfo(OleDeliverRequestBo oleDeliverRequestBo,Map<String,String> fieldLabelMap) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<TR><TD>");
        stringBuffer.append(fieldLabelMap.get("Original Due Date")!=null ?fieldLabelMap.get("Original Due Date"):"Original Due Date" +":</TD><TD>" + ( oleDeliverRequestBo.getOriginalDueDate()!= null ? oleDeliverRequestBo.getOriginalDueDate() : "") + "</TD></TR><TR><TD>");
        stringBuffer.append(fieldLabelMap.get("Recall Due Date")!=null ?fieldLabelMap.get("Recall Due Date"):"Recall Due Date" +":</TD><TD>" + ( oleDeliverRequestBo.getRecallDueDate()!= null ? oleDeliverRequestBo.getRecallDueDate() : "") + "</TD></TR>");
        return stringBuffer.toString();
    }
}
