package org.kuali.ole.deliver.controller.notices;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.util.NoticeInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 10/21/15.
 */
public abstract class NoticeDueDateProcessor {

    public abstract boolean isInterested(String noticeType);

    public  abstract List<OLEDeliverNotice> generateNotices(NoticeInfo noticeInfo, OleLoanDocument oleLoanDocument);

    protected  abstract Map<String, Object> getNoticeInfoForTypeMap(NoticeInfo noticeInfo);

    protected Date calculateNoticeDueDate(Date dueDate, int interval, String intervalType) {
        Date noticeDueDate = null;
        if(StringUtils.isNotBlank(intervalType)){
            if(intervalType.equalsIgnoreCase("H")){
                noticeDueDate = DateUtils.addHours(dueDate,interval);
            } else if(intervalType.equalsIgnoreCase("M")) {
                noticeDueDate = DateUtils.addMinutes(dueDate, interval);
            } else if(intervalType.equalsIgnoreCase("D")) {
                noticeDueDate = DateUtils.addDays(dueDate, interval);
            }
        }
        return noticeDueDate;
    }
}
