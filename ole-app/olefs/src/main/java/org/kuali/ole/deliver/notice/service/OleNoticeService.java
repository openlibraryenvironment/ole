package org.kuali.ole.deliver.notice.service;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.calendar.service.DateUtil;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maheswarang on 7/1/15.
 */
public interface OleNoticeService {
    public OLEDeliverNotice createNotice(OleDeliverRequestBo oleDeliverRequestBo,String noticeType,Timestamp noticeToBeSendDate);

    public OleDeliverRequestBo processNoticeForRequest(OleDeliverRequestBo oleDeliverRequestBo);

    public void updateHoldNoticesDate(String itemBarcode);

    public String getNoticeSubjectForNoticeType(String noticeName);
}
