package org.kuali.ole.deliver.controller.notices;

import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.util.NoticeInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 10/21/15.
 */
public abstract class NoticeDueDateProcessor {

    public abstract boolean isInterested(String noticeType);

    public  abstract List<OLEDeliverNotice> generateNotices(NoticeInfo noticeInfo, OleLoanDocument oleLoanDocument);

    protected  abstract Map<String, Object> getNoticeInfoForTypeMap(NoticeInfo noticeInfo);
}
