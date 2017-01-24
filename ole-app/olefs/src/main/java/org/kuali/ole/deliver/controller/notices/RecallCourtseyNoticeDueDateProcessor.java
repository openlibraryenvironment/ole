package org.kuali.ole.deliver.controller.notices;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.util.NoticeInfo;

import java.util.*;

/**
 * Created by sheiksalahudeenm on 10/21/15.
 */
public class RecallCourtseyNoticeDueDateProcessor extends CourtseyNoticeDueDateProcessor {

    private static final Logger LOG = Logger.getLogger(RecallCourtseyNoticeDueDateProcessor.class);

    @Override
    public boolean isInterested(String noticeType) {
        return noticeType.equalsIgnoreCase(OLEConstants.RECALL_COURTESY_NOTICE);
    }

    @Override
    protected Map<String, Object> getNoticeInfoForTypeMap(NoticeInfo noticeInfo) {
        return noticeInfo.getNoticeInfoForTypeMap().get(OLEConstants.RECALL_COURTESY_NOTICE);
    }

    @Override
    protected String getNumberOfCourtesyNoticeToSent(NoticeInfo noticeInfo){
        return (String) noticeInfo.getNoticeInfoForTypeMap().get
                (OLEConstants.RECALL_COURTESY_NOTICE).get(DroolsConstants.NUMBER_OF_COURTESY_NOTICES_TO_BE_SENT);
    }
}
