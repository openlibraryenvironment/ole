package org.kuali.ole.deliver.controller.notices;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.util.NoticeInfo;

import java.util.*;

/**
 * Created by sheiksalahudeenm on 10/21/15.
 */
public class RecallOverDueAndLostNoticeDueDateProcessor extends OverDueAndLostNoticeDueDateProcessor {
    
    private static final Logger LOG = Logger.getLogger(RecallOverDueAndLostNoticeDueDateProcessor.class);

    @Override
    public boolean isInterested(String noticeType) {
        return noticeType.equalsIgnoreCase(OLEConstants.RECALL_OVERDUE_NOTICE);
    }

    @Override
    protected Map<String, Object> getNoticeInfoForTypeMap(NoticeInfo noticeInfo) {
        return noticeInfo.getNoticeInfoForTypeMap().get(OLEConstants.RECALL_OVERDUE_NOTICE);
    }

    @Override
    protected String getNumberOfOverDueNoticeToSent(NoticeInfo noticeInfo) {
        return (String) noticeInfo.getNoticeInfoForTypeMap().get
                (OLEConstants.RECALL_OVERDUE_NOTICE).get(DroolsConstants.NUMBER_OF_OVERDUE_NOTICES_TO_BE_SENT);
    }

    @Override
    public Map<String, Object> getLostNoticeMap(NoticeInfo noticeInfo) {
        return noticeInfo.getNoticeInfoForTypeMap().get(OLEConstants.RECALL_LOST_NOTICE);
    }
}
