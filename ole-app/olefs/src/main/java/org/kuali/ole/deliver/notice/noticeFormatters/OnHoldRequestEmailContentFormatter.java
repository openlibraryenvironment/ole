package org.kuali.ole.deliver.notice.noticeFormatters;

import org.kuali.ole.deliver.bo.OleDeliverRequestBo;

import java.util.Map;

/**
 * Created by maheswarang on 6/25/15.
 */
public class OnHoldRequestEmailContentFormatter extends RequestEmailContentFormatter {
    @Override
    public String getCustomItemHeaderInfo(OleDeliverRequestBo oleDeliverRequestBo,Map<String,String> fieldLabelMap) {
        return null;
    }

    @Override
    public String getCustomItemFooterInfo(OleDeliverRequestBo oleDeliverRequestBo,Map<String,String> fieldLabelMap) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<TR><TD>");
        stringBuffer.append(fieldLabelMap.get("Hold Expiration Date")!=null ? fieldLabelMap.get("Hold Expiration Date") : "Hold Expiration Date"+":</TD><TD>" + (oleDeliverRequestBo.getHoldExpirationDate() != null ? oleDeliverRequestBo.getHoldExpirationDate() : "") + "</TD></TR>");
        return stringBuffer.toString();
    }
}
